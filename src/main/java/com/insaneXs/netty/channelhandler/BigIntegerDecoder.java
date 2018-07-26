package com.insaneXs.netty.channelhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.math.BigInteger;
import java.util.List;


public class BigIntegerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        System.out.println("BigIntegerDecoder");
        // Wait until the length prefix is available.
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();

        int length = in.readInt();
        if (in.readableBytes() < length){
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[length];
        in.readBytes(data);

        out.add(new BigInteger(data));
    }
}
