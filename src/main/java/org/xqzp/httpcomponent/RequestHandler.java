package org.xqzp.httpcomponent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class RequestHandler {
    public static HttpRequest parseRequest(SocketChannel socketChannel) throws IOException {
        Logger log = LoggerFactory.getLogger("org.xqzp.util.RequestHandler");

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024*1024);
        String receive = "";

        //读取socketChannel中的数据
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

        if("".equals(receive)){
            return null;
        }

        HttpRequest request = null;
        try {
            request = new HttpRequest(receive);
        } catch (Exception e) {
            log.error("Http请求报文解析异常");
            e.printStackTrace();
        }
        log.info(request.toString());

        return request;
    }
}
