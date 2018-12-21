package com.insaneXs.netty.reactor.multiple;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @Author: insaneXs
 * @Description:
 * @Date: Create at 2018-12-21
 */
public class SubReactor implements Runnable{
    private final Selector selector;

    public SubReactor() throws IOException {
        selector = Selector.open();
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            try {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while(iter.hasNext()){
                    SelectionKey sk = iter.next();
                    ((Runnable)sk.attachment()).run();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Selector getSelector(){
        return selector;
    }
}
