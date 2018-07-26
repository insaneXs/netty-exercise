package com.insaneXs.netty.channelhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {



    public NettyServerInitializer() {

    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new AdditionalInBoundHandler("in handler 1"));
        pipeline.addLast(new AdditionalInBoundHandler("in handler 2"));
        pipeline.addLast(new BigIntegerDecoder());
        pipeline.addLast(new NumberEncoder());

        pipeline.addLast(new AdditionalOutBoundHandler("out handler 1"));
        pipeline.addLast(new AdditionalInBoundHandler("in handler 3"));
        pipeline.addLast(new NettyServerHandler());
        pipeline.addLast(new AdditionalInBoundHandler("in handler 4"));
        pipeline.addLast(new AdditionalOutBoundHandler("out handler 2"));

    }
}
