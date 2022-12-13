package org.xqzp.sevice;

import java.io.IOException;

public class Cmd {
    public static void executeCommand(String ip) {
        try {
            String firewalldCMD= String.format("iptables -I INPUT -s %s -j DROP", ip);
            String[] cmd = new String[] { "/bin/sh", "-c", firewalldCMD};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
