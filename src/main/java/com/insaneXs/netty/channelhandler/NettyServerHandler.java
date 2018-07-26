package com.insaneXs.netty.channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.math.BigInteger;


public class NettyServerHandler extends SimpleChannelInboundHandler<BigInteger> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, BigInteger msg) throws Exception {
        System.out.println("NettyServerHandler");
        ctx.writeAndFlush(msg.add(BigInteger.ONE));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
