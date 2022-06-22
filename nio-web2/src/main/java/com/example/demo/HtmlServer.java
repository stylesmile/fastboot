package com.example.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
 
/**
 * Created by zhang_ on 2019/5/1.
 */
public class HtmlServer {
 
    private static final String htmlStr = "HTTP/1.1 200 OK\n\n" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\"><html>\n" +
            "<title>Directory listing for /</title>\n" +
            "<body>\n" +
            "<h2>Welcome to NIO Server</h2>\n" +
            "<hr>\n" +
            "<ul>\n" +
            "<li><a href=\"hi.txt\">hi.txt</a>\n" +
            "</ul>\n" +
            "<hr>\n" +
            "</body>\n" +
            "</html>\n";
    public static void startServer(int port) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //注册接收连接的事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动监听 " + port + " 端口...");
            //持续select事件
            constantSelect(selector);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void constantSelect(Selector selector) {
        while (true) {
            try {
                if (selector.select() > 0) {
                    System.out.println("当前连接数:" + selector.keys().size());
                    System.out.println("检测到活跃的连接数:" + selector.selectedKeys().size());
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        if (key.isAcceptable()) {
                            //处理接收连接时间
                            boolean succ = dealAccept(selector, key);
                            if (succ)
                                it.remove();
                        }
                        if (key.isReadable()) {
                            //处理读事件
                            boolean succ = dealRead(key);
                            if (succ)
                                it.remove();
                        }
 
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
 
        }
 
    }
 
    private static boolean dealRead(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(10);
        //判定读取一个字节,就返回固定页面
        try {
            int len = channel.read(buf);
            if (len <= 0) {
                //处理连接已经断开的情况
                return false;
            }
            ByteBuffer htmlBuf = ByteBuffer.allocate(htmlStr.getBytes().length);
            htmlBuf.put(htmlStr.getBytes());
 
            htmlBuf.flip();
            int outLen = channel.write(htmlBuf);
 
 
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                channel.shutdownInput();
                channel.shutdownOutput();
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
 
        }
        return true;
    }
 
 
    private static boolean dealAccept(Selector selector, SelectionKey key) {
 
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            //接收连接后,注册读事件到selector
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
 
}