package org.xqzp;

public class Main {
    public static void main(String[] args) {
        int port = 1234;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        try {
            Server.start(port);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}