package org.xqzp.httpcomponent;

public class Response {
    private static String statusLine ;

    public static Response ok(){
        statusLine = "HTTP/1.1 202 Accepted";
        return new Response();
    }

    public static Response error(){
        statusLine = "HTTP/1.1 400 Bad Request";
        return new Response();
    }

    @Override
    public String toString() {
        return statusLine;
    }
}
