package com.insaneXs.netty.channelhandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class AdditionalInBoundHandler extends ChannelInboundHandlerAdapter {
    private String name;

    public AdditionalInBoundHandler(String name){
        this.name = name;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println("go through additional InBoundHandler[" + name + "]; msg type[" + msg.getClass() + "]");
        ctx.fireChannelRead(msg);
    }
}
