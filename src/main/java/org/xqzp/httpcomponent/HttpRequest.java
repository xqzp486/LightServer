package org.xqzp.httpcomponent;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    RequestLine requestLine = new RequestLine();
    Map<String,String> requestHead = new HashMap<>();
    Map<String,String> requestBody = new HashMap<>();

    /**
     * |---------------HTTP 请求报文的格式----------------|
     * --------------------------------------------------
     * | method    | sp | URI | Protocol | CRLF |   请求行
     * | 首部字段名 | :  | sp  | 值       |CRLF  |   请求头
     * | 首部字段名 | :  | sp  | 值       |CRLF  |   请求头
     * |CRLF|
     * key = value   如果是Post请求则此处还有请求体
     */

    public HttpRequest(String rawRequest) throws Exception{
        //确定请求行的结束位置
        int indexOfFirstLine = rawRequest.indexOf("\r\n");

        //确定请求体的开始位置，同时确定请求头的结束位置
        int indexOfRequestBody = rawRequest.indexOf("\r\n\r\n");

        String rawRequestHeadLine = rawRequest.substring(0,indexOfFirstLine);
        this.requestLine = requestLine.getRequestLine(rawRequestHeadLine);

        //加2，是因为要去掉/r/n,因为第一个index的值，是/r的位置
        parseRequestHead(rawRequest.substring(indexOfFirstLine+2,indexOfRequestBody));

        //请求是Post请求,且请求体存在
        if(rawRequest.startsWith("POST")&&rawRequest.length()!=(indexOfRequestBody+4)){
            parseRequestBody(rawRequest.substring(indexOfRequestBody+4));
        }
    }



    public class RequestLine{
        String type;
        String path;
        Map<String,String> requestParams =new HashMap<>();
        String protocol;

        //解析请求行
        RequestLine getRequestLine(String rawRequestLine){
            String[]  requestLines= rawRequestLine.split(" ");
            //获取请求类型
            type = requestLines[0];

            //处理路径和请求参数
            String[] pathAndRequestParams = requestLines[1].split("\\?");
            path = pathAndRequestParams[0];

            //如果存在请求参数，则处理请求参数
            if(pathAndRequestParams.length>1){
                String[] keyAndValue = pathAndRequestParams[1].split("=");
                for(int i=0;i<keyAndValue.length;i+=2){
                    requestParams.put(keyAndValue[i],keyAndValue[i+1]);
                }
            }

            //获取请求协议协议
            protocol=requestLines[2];

            return this;
        }

        @Override
        public String toString() {
            return "RequestLine{" +
                    "type='" + type + '\'' +
                    ", path='" + path + '\'' +
                    ", requestParams=" + requestParams +
                    ", protocol='" + protocol + '\'' +
                    '}';
        }

        public String getType() {
            return type;
        }

        public String getPath() {
            return path;
        }

        public Map<String, String> getRequestParams() {
            return requestParams;
        }

        public String getProtocol() {
            return protocol;
        }
    }

    //解析请求头
    private  void parseRequestHead(String rawRequestHead){
        String[] heads = rawRequestHead.split("\r\n");
        for (String head : heads) {
            String[] headkeyvalue = head.split(": ");
            this.requestHead.put(headkeyvalue[0], headkeyvalue[1]);
        }
    }

    //解析请求体
    private  void parseRequestBody(String rawRequestBody){
        String[] heads = rawRequestBody.split("\r\n");
        for (String head : heads) {
            String[] bodykeyvalue = head.split("=");
            this.requestBody.put(bodykeyvalue[0], bodykeyvalue[1]);
        }
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestLine=" + requestLine +
                ", requestHead=" + requestHead +
                ", requestbody=" + requestBody +
                '}';
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getRequestHead() {
        return requestHead;
    }

    public Map<String, String> getRequestBody() {
        return requestBody;
    }
}
