package com.insaneXs.netty;

import com.insaneXs.netty.channelhandler.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.math.BigInteger;

import static junit.framework.TestCase.assertTrue;

/**
 * @Author: insaneXs
 * @Description:
 * @Date: Create at 2018-07-25
 */
public class ChannelHandlerTest {

    @Test
    public void channelHandlerTest(){
        ByteBuf buf = Unpooled.buffer();

        EmbeddedChannel embeddedChannel = new EmbeddedChannel();

        ChannelPipeline pipeline = embeddedChannel.pipeline();

        pipeline.addLast(new AdditionalInBoundHandler("in handler 1"));
        pipeline.addLast(new AdditionalInBoundHandler("in handler 2"));
        pipeline.addLast(new BigIntegerDecoder());
        pipeline.addLast(new NumberEncoder());

        pipeline.addLast(new AdditionalOutBoundHandler("out handler 1"));
        pipeline.addLast(new AdditionalInBoundHandler("in handler 3"));
        pipeline.addLast(new NettyServerHandler());
        pipeline.addLast(new AdditionalInBoundHandler("in handler 4"));
        pipeline.addLast(new AdditionalOutBoundHandler("out handler 2"));

        byte[] bytes = new byte[]{0x01, 0x02, 0x03, 0x04};
        BigInteger bi = new BigInteger(bytes);

        buf.writeInt(4);
        buf.writeBytes(bytes);

        //因为nettyServerHandler fire了写事件，因此channelpipeline尾部没数据可读
        assertTrue(!embeddedChannel.writeInbound(buf));

        ByteBuf readBuf = embeddedChannel.readOutbound();
        int length = readBuf.readInt();
        bytes = new byte[length];
        readBuf.readBytes(bytes);
        System.out.println(new BigInteger(bytes));
    }
}
