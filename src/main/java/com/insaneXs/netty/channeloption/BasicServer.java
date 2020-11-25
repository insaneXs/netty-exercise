package com.insaneXs.netty.channeloption;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

/**
 * @Author: xieshang
 * @Description:
 * @Date: Create at 2020-07-09
 */
public class BasicServer {

    public BasicServer(List<ChannelOptionHolder> channelOptions, List<ChannelHandler> channelHandlers,
                       List<ChannelOptionHolder> childChannelOptions, List<ChannelHandler> childChannelHandlers) throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioServerSocketChannel.class);

//        channelOptions.forEach(channelOption ->
//            bootstrap.option(channelOption.channelOption, channelOption.value)
//        );
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {


            @Override
            protected void initChannel(NioServerSocketChannel ch) throws Exception {
                ChannelPipeline channelPipeline = ch.pipeline();
                for(ChannelHandler channelHandler : channelHandlers){
                    channelPipeline.addLast(channelHandler);
                }
            }
        });

        childChannelOptions.forEach(channelOptionHolder ->
                bootstrap.option(channelOptionHolder.channelOption, channelOptionHolder.value));

        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline channelPipeline = ch.pipeline();
                for(ChannelHandler channelHandler : childChannelHandlers){
                    channelPipeline.addLast(channelHandler);
                }
            }
        });

        bootstrap.bind(18080).sync().channel().closeFuture().sync();
        System.out.println("Server Closed");
    }


}
