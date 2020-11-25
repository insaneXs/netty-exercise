package com.insaneXs.netty.channeloption;

import com.google.common.collect.Lists;
import com.insaneXs.netty.handler.EchoClientHandler;
import io.netty.channel.ChannelOption;

/**
 * @Author: xieshang
 * @Description:
 * @Date: Create at 2020-07-09
 */
public class SO_TIMEOUT {


    public static void main(String[] args) throws InterruptedException {

        ChannelOptionHolder holder = new ChannelOptionHolder(ChannelOption.SO_TIMEOUT, 5);
        BasicServer server = new BasicServer(Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(new EchoClientHandler()));
    }
}
