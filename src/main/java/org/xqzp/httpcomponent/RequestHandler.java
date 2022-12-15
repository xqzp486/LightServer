package org.xqzp.httpcomponent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xqzp.exception.HttpParseException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class RequestHandler {
    public static HttpRequest parseRequest(SocketChannel socketChannel) throws HttpParseException {
        Logger log = LoggerFactory.getLogger("org.xqzp.util.RequestHandler");
        HttpRequest request = null;

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024*1024);
        String receive = "";

        //读取socketChannel中的数据
        try {
            int readLength = socketChannel.read(byteBuffer);

            while(readLength>0){
                //切换读模式
                byteBuffer.flip();
                receive = ""+StandardCharsets.UTF_8.decode(byteBuffer);
                byteBuffer.clear();

                //切换成写模式
                byteBuffer.flip();
                readLength = socketChannel.read(byteBuffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.info("数据读取异常");
        }

        //如果请求没有数据，则返回null
        if("".equals(receive)){
            return null;
        }

        try {
            request = new HttpRequest(receive);
            log.info(request.toString());
        } catch (Exception e) {
            log.error("Http请求报文解析异常");
            log.info(receive);
            e.printStackTrace();
            throw new HttpParseException();
        }
        return request;
    }
}
