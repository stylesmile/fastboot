package io.github.stylesmile.tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class HttpUtil {

    /**
     * The Selector used for handling channel events.
     */
    private static final Selector selector;

    /**
     * Stores the responses received from the server, keyed by SocketChannel.
     */
    private static final Map<SocketChannel, StringBuilder> responses;

    /**
     * Tracks the number of active requests.
     */
    private static final AtomicInteger activeRequests = new AtomicInteger(0);

    /**
     * Static initializer to open the Selector and start the processRequests thread.
     */
    static {
        try {
            selector = Selector.open(); // Open a new Selector
            responses = new ConcurrentHashMap<>(); // Initialize the responses map
            // Start a new thread to process requests
            new Thread(() -> {
                try {
                    processRequests();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize selector", e); // Throw an exception if initialization fails
        }
    }

    /**
     * Sends a GET request with query parameters.
     *
     * @param url        The URL to send the GET request to.
     * @param queryParams A map of query parameters.
     * @throws IOException If an I/O error occurs.
     */
    public static void sendGetRequest(String url, Map<String, String> queryParams) throws IOException {
        // Construct the full URL with query parameters
        String fullUrl = url + "?" + queryParams.entrySet().stream()
                .map(e -> {
                    try {
                        return e.getKey() + "=" + URLEncoder.encode(e.getValue(), String.valueOf(StandardCharsets.UTF_8)); // Encode each parameter value
                    } catch (UnsupportedEncodingException ex) {
                        throw new RuntimeException(ex); // Throw an exception if encoding fails
                    }
                })
                .collect(Collectors.joining("&")); // Join all parameters with '&'
        sendRequest(fullUrl, "GET", null, null); // Send the request with the constructed URL
    }

    /**
     * Sends a POST request with a body and headers.
     *
     * @param url     The URL to send the POST request to.
     * @param body    The body of the POST request.
     * @param headers A map of headers for the POST request.
     * @throws IOException If an I/O error occurs.
     */
    public static void sendPostRequest(String url, String body, Map<String, String> headers) throws IOException {
        sendRequest(url, "POST", body, headers); // Send the request with the specified method, body, and headers
    }

    /**
     * Sends a PUT request with a body and headers.
     *
     * @param url     The URL to send the PUT request to.
     * @param body    The body of the PUT request.
     * @param headers A map of headers for the PUT request.
     * @throws IOException If an I/O error occurs.
     */
    public static void sendPutRequest(String url, String body, Map<String, String> headers) throws IOException {
        sendRequest(url, "PUT", body, headers); // Send the request with the specified method, body, and headers
    }

    /**
     * Sends a DELETE request with headers.
     *
     * @param url     The URL to send the DELETE request to.
     * @param headers A map of headers for the DELETE request.
     * @throws IOException If an I/O error occurs.
     */
    public static void sendDeleteRequest(String url, Map<String, String> headers) throws IOException {
        sendRequest(url, "DELETE", null, headers); // Send the request with the specified method and headers
    }

    /**
     * General method for sending HTTP requests.
     *
     * @param url     The request URL.
     * @param method  The HTTP method (GET, POST, PUT, DELETE).
     * @param body    The request body, can be null.
     * @param headers A map of request headers, can be null.
     * @throws IOException If an I/O error occurs.
     */
    private static void sendRequest(String url, String method, String body, Map<String, String> headers) throws IOException {
        activeRequests.incrementAndGet(); // Increment the active request count
        URI uri = URI.create(url); // Create a URI object from the URL string
        SocketChannel socketChannel = SocketChannel.open(); // Open a new SocketChannel
        socketChannel.configureBlocking(false); // Configure the channel to be non-blocking
        socketChannel.connect(new InetSocketAddress(uri.getHost(), uri.getPort() == -1 ? 80 : uri.getPort())); // Connect to the server
        socketChannel.register(selector, SelectionKey.OP_CONNECT); // Register the channel with the selector for connection events
        StringBuilder requestBuilder = new StringBuilder(); // Initialize a StringBuilder for the request
        requestBuilder.append(method).append(" ").append(uri.getRawPath()).append(" HTTP/1.1\r\n"); // Append the request line
        requestBuilder.append("Host: ").append(uri.getHost()).append("\r\n"); // Append the Host header
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n"); // Append each header
            }
        }
        if (body != null) {
            requestBuilder.append("Content-Length: ").append(body.length()).append("\r\n"); // Append the Content-Length header if there is a body
        }
        requestBuilder.append("\r\n"); // Append a blank line to end the headers
        if (body != null) {
            requestBuilder.append(body); // Append the request body
        }
        socketChannel.write(ByteBuffer.wrap(requestBuilder.toString().getBytes(StandardCharsets.UTF_8))); // Write the request to the channel
    }

    /**
     * Processes all pending requests.
     *
     * @throws IOException If an I/O error occurs.
     */
    private static void processRequests() throws IOException {
        while (activeRequests.get() > 0) { // Continue processing while there are active requests
            selector.select(); // Wait for events on the registered channels
            Set<SelectionKey> selectedKeys = selector.selectedKeys(); // Get the keys for the channels with events
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator(); // Create an iterator for the keys

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next(); // Get the next key

                if (key.isConnectable()) {
                    finishConnection(key); // Complete the connection if the channel is connectable
                } else if (key.isReadable()) {
                    readResponse(key); // Read the response if the channel is readable
                }

                keyIterator.remove(); // Remove the key from the set
            }
        }
    }

    /**
     * Completes the connection process.
     *
     * @param key The SelectionKey whose connection needs to be completed.
     * @throws IOException If an I/O error occurs.
     */
    private static void finishConnection(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel(); // Get the channel from the key
        if (channel.finishConnect()) { // Complete the connection
            channel.register(selector, SelectionKey.OP_READ); // Register the channel for read events
        }
    }

    /**
     * Reads the server's response.
     *
     * @param key The SelectionKey from which to read the response.
     * @throws IOException If an I/O error occurs.
     */
    private static void readResponse(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel(); // Get the channel from the key
        ByteBuffer buffer = ByteBuffer.allocate(1024); // Allocate a buffer for reading
        int bytesRead = channel.read(buffer); // Read data from the channel into the buffer

        if (bytesRead == -1) {
            channel.close(); // Close the channel if no more data is available
            activeRequests.decrementAndGet(); // Decrement the active request count
            return;
        }

        buffer.flip(); // Prepare the buffer for reading
        StringBuilder response = responses.getOrDefault(channel, new StringBuilder()); // Get the existing response or create a new one
        response.append(StandardCharsets.UTF_8.decode(buffer)); // Decode the buffer and append to the response
        responses.put(channel, response); // Store the updated response

        if (isResponseComplete(response.toString())) { // Check if the response is complete
            System.out.println("Response: " + response.toString()); // Print the response
            channel.close(); // Close the channel
            activeRequests.decrementAndGet(); // Decrement the active request count
            responses.remove(channel); // Remove the response from the map
        }
    }

    /**
     * Checks if the HTTP response is complete.
     *
     * @param response The response string.
     * @return true if the response is complete, otherwise false.
     */
    private static boolean isResponseComplete(String response) {
        return response.contains("\r\n\r\n"); // Check for the end of the headers
    }

    /**
     * Main method for testing HTTP requests.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            // Example GET request
            Map<String, String> queryParams = new ConcurrentHashMap<>();
            queryParams.put("param1", "value1");
            queryParams.put("param2", "value2");
            sendGetRequest("http://httpbin.org/get", queryParams);

            // Example POST request
            Map<String, String> postHeaders = new ConcurrentHashMap<>();
            postHeaders.put("Content-Type", "application/json");
            String postBody = "{\"key\":\"value\"}";
            sendPostRequest("http://httpbin.org/post", postBody, postHeaders);

            // Example PUT request
            Map<String, String> putHeaders = new ConcurrentHashMap<>();
            putHeaders.put("Content-Type", "application/json");
            String putBody = "{\"key\":\"value\"}";
            sendPutRequest("http://httpbin.org/put", putBody, putHeaders);

            // Example DELETE request
            Map<String, String> deleteHeaders = new ConcurrentHashMap<>();
            deleteHeaders.put("Content-Type", "application/json");
            sendDeleteRequest("http://httpbin.org/delete", deleteHeaders);

            // No need to call processRequests explicitly
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace if an exception occurs
        }
    }
}
