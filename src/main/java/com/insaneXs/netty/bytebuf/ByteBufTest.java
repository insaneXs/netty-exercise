package com.insaneXs.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @Author: insaneXs
 * @Description:
 * @Date: Create at 2018-07-13
 */
public class ByteBufTest {

    public static void main(String[] args){
        /*******************初始阶段**********************/
        ByteBuf buf = Unpooled.buffer(100, 200);
        System.out.println("writable bytes " + buf.writableBytes());
        System.out.println("readable bytes " + buf.readableBytes());

        /*******************写入数据**********************/
        String str = "write test";//UTF-8 英文字符占一个字节
        int length = str.getBytes().length;//因此得到的字节长度为10
        buf.writeBytes(str.getBytes());
        System.out.println("writable bytes " + buf.writableBytes());
        System.out.println("readable bytes " + buf.readableBytes());

        /*******************读取数据*********************/
        char c = (char)buf.readByte();//读取第一个字节，并转换成字符打印
        System.out.println("c:" + c);
        System.out.println("writable bytes " + buf.writableBytes());
        System.out.println("readable bytes " + buf.readableBytes());

        /*******************丢弃已读*********************/
        buf.discardReadBytes();
        System.out.println("writable bytes " + buf.writableBytes());
        System.out.println("readable bytes " + buf.readableBytes());

    }
}
