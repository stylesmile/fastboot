package com.example.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
 
 
public class Connector {
 
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private Logger logger = Logger.getLogger(this.toString());
    private ThreadLocal<ByteBuffer> sendMap = new ThreadLocal<>();
    private ThreadLocal<ByteBuffer> receiveMap = new ThreadLocal<>();
    private ByteBuffer send = ByteBuffer.allocate(10000);
    private ByteBuffer receive = ByteBuffer.allocate(10000);
    private ThreadPoolExecutor threadPoolExecutor;
    boolean flag = false;
 
    public void init(int port) throws IOException {
        // 初始化线程池
        threadPoolExecutor =  new ThreadPoolExecutor(4, 10,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
 
        serverSocketChannel = ServerSocketChannel.open();
        // 配置为非阻塞
        serverSocketChannel.configureBlocking(false);
//        ServerSocket serverSocket = serverSocketChannel.socket();
        // 绑定端口
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocketChannel.bind(address);
        selector = Selector.open();
        // 在socket上使用select, 把链接放到accept槽位
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("服务正在启动....绑定的端口为" + port);
    }
 
    public void listen(){
        while (true){
            Iterator<SelectionKey> iterator = null;
            Set<SelectionKey> selectionKeys = null;
            try {
                selector.select();
                selectionKeys = selector.selectedKeys();
                // 遍历所有槽位
                iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    dealChannel(key);
                    // 把相应位置删除，免得无限处理同一个
                    iterator.remove();
 
                }
            } catch (Exception e) {
                e.printStackTrace();
                if(iterator != null) {
                    iterator.remove();
                }
//                System.out.println("----------");
            }
        }
    }
 
    // 判断key是accept，read，write哪个槽中，进行相应处理,创建相应信道
    private void dealChannel(SelectionKey selectionKey) throws Exception {
        SocketChannel client = null;
        try {
            if(!selectionKey.isValid()) {
                selectionKey.cancel();
                return;
            }
            // 若是主TCP连接，则发放客户端链接给子信道处理
            if (selectionKey.isAcceptable()) {
                // 获取父信道，转换成ServerSocketChannel是因为此接口才有accept的阻塞方法，不然得轮询
                ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                // 平常阻塞着，一旦有请求连接，主Channel创建子channel来处理业务
                client = serverChannel.accept();
                client.configureBlocking(false);
                // 子信道为了处理业务，所以可读
                client.register(selector, SelectionKey.OP_READ);
 
            }
            // 如果是可读的
            else if (selectionKey.isReadable()) {
                selectionKey.cancel();
                client = (SocketChannel) selectionKey.channel();
                if (!client.isOpen()) {
                    return;
                }
                ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                int count = client.read(byteBuffer);
                if (count <= 0) {
                    // 关闭都交给子线程
//                    client.close();
                    return;
                }
 
                final SocketChannel threadClient = client;
                byteBuffer.position(0);
                // 使用线程池执行业务逻辑
                threadPoolExecutor.execute(() ->{
                    try {
                        // 解析http
                        HTTPRequest headers = new HTTPParse().parse(byteBuffer);
                        // 处理并返回response给客户端
                        new Controller().control(headers, threadClient);
                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        try {
                            threadClient.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
 
                });
 
            }
            // 如果是可写的
            else if (selectionKey.isWritable()) {
                client = (SocketChannel) selectionKey.channel();
                if (!client.isOpen()) {
                    return;
                }
                client.write(send);
                client.close();
            }
        }
        catch (Exception e){
            if(client != null) {
                client.close();
            }
            throw e;
 
        }
    }
 
 

 
}
 
 