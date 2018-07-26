package com.insaneXs.netty.channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @Author: insaneXs
 * @Description:
 * @Date: Create at 2018-07-19
 */
public class AdditionalOutBoundHandler extends ChannelOutboundHandlerAdapter {
    private String name;

    public AdditionalOutBoundHandler(String name) {
        this.name = name;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("go through additional outbound handler[" + name + "];msg type [" + msg.getClass() + "]");
        ctx.write(msg, promise);
    }
}
