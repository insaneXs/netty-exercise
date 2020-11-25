package com.insaneXs.netty.channeloption;

import io.netty.channel.ChannelOption;

/**
 * @Author: xieshang
 * @Description:
 * @Date: Create at 2020-07-09
 */
public class ChannelOptionHolder {

    ChannelOption channelOption;
    Object value;
    public ChannelOptionHolder(ChannelOption channelOption, Object value){
        this.channelOption = channelOption;
        this.value = value;
    }
}
