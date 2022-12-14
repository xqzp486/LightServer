package org.xqzp.exception;

public class HttpParseException extends Exception{

    static final long serialVersionUID = -7034897193246939L;

    public HttpParseException() {
        super("Http请求报文解析异常");
    }

    public HttpParseException(String message) {
        super(message);
    }
}