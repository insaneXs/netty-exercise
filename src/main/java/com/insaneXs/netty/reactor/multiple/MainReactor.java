package com.insaneXs.netty.reactor.multiple;

import com.insaneXs.netty.reactor.threadpooled.ThreadPooledHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: insaneXs
 * @Description:
 * @Date: Create at 2018-12-21
 */
public class MainReactor implements Runnable{
    final Selector selector;

    final ServerSocketChannel serverSocket;

    private final static int SUB_REACTOR_COUNT = 3;

    private final Selector[] selectors = new Selector[SUB_REACTOR_COUNT];

    MainReactor(int port) throws Exception{
        //创建ServerSocketChannel，绑定端口，设置为非阻塞，选择器上注册ACCEPT事件
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();

        serverSocket.bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        for(int i=0; i<selectors.length; i++){
            //创建SUB-REACTOR，并保存对应的Selector对象
            SubReactor subReactor = new SubReactor();
            selectors[i] = subReactor.getSelector();
            //为SUB-REACTOR启动独立的线程
            new Thread(subReactor).start();
        }

        sk.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //阻塞，直到注册的事件发生
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()){
                    //任务派发
                    dispatch((SelectionKey)(it.next()));
                }
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    void dispatch(SelectionKey k) {
        //通过将不同的附件绑定到SelectionKey上，实现dispatch统一派发Acceptor和Handler的逻辑
        Runnable r = (Runnable)(k.attachment());
        if (r != null)
            r.run();
    }

    class Acceptor implements Runnable{
        private int idx = 0;
        @Override
        public void run() {
            try {
                //ACCEPT负责接收链接
                SocketChannel sc = serverSocket.accept();
                if(sc != null)//将SocketChannel与SubReactor的Selector均匀绑定
                    new ThreadPooledHandler(selectors[idx], sc);

                idx++;
                if(idx == SUB_REACTOR_COUNT)
                    idx = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
