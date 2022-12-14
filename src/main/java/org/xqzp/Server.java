package org.xqzp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xqzp.httpcomponent.HttpRequest;
import org.xqzp.httpcomponent.RequestHandler;
import org.xqzp.httpcomponent.Response;
import org.xqzp.sevice.Cmd;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class Server {
    public static void start(int port)  {
        Logger log = LoggerFactory.getLogger(Server.class);
        HttpRequest httpRequest =null;

        //解析http请求
        try {
            //ServerSocketChannel
            ServerSocketChannel ssc = ServerSocketChannel.open();
            //绑定端口
            ssc.socket().bind(new InetSocketAddress(port));
            //非阻塞模式下运行
            ssc.configureBlocking(false);

            log.info("服务成功启动,开始监听"+port+"端口");

            while(true){
                //如果是阻塞模式,则会阻塞在这一行,直到有链接进来
                SocketChannel socketChannel = ssc.accept();
                if(socketChannel==null){
                    Thread.sleep(1000);
                }else {
                    SocketAddress remoteSocketAddress = socketChannel.socket().getRemoteSocketAddress();
                    String remoteIP = remoteSocketAddress.toString();
                    log.info("Accepted"+remoteIP);

                    try {
                        httpRequest = RequestHandler.parseRequest(socketChannel);
                        if (httpRequest!=null){
                            socketChannel.write(StandardCharsets.UTF_8.encode(Response.ok().toString()));
                        }{
                            socketChannel.write(StandardCharsets.UTF_8.encode(Response.error().toString()));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        log.info("对面的连接已经中断");
                    }

                    //=================================================
                    //执行业务逻辑
                    if(httpRequest!=null){
                        Map<String, String> requestParams = httpRequest.getRequestLine().getRequestParams();
                        String ip = requestParams.get("ip");
                        if(ip!=null){
                            Cmd.executeCommand(ip);
                        }
                    }
                    socketChannel.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
