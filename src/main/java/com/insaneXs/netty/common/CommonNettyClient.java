package com.insaneXs.netty.common;

import com.insaneXs.netty.channelhandler.AdditionalInBoundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class CommonNettyClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .option(ChannelOption.SO_LINGER, 100000)
                .channel(NioSocketChannel.class)
                .remoteAddress("192.168.1.102", 6666)
                .handler(new AdditionalInBoundHandler(""));



        while(true){
            Channel channel = bootstrap.connect().sync().channel();
            Thread.sleep(20);
        }

//        long time = System.currentTimeMillis();
//        channel.close().sync();
//        System.out.println(System.currentTimeMillis() - time);
//        channel.eventLoop().shutdownGracefully();
    }
}
