package org.xqzp.sevice;

import java.io.IOException;
import java.util.logging.Logger;

public class Cmd {
    /**
     * 调用firewalld 富规则添加白名单ip
     * firewalld 富规则不会重复添加ip
     * @param ip 接受到的ip值
     */
    public static void executeCommand(String ip) {
        Logger log = Logger.getLogger("org.xqzp.sevice.Cmd");

        //将ip的末尾转成1,减少重复的规则

        char[] newIPArray = new char[ip.length()];

        int index = 0;
        for(int i=0;i<ip.length();i++){
            if('.'==ip.charAt(i)) index++;
            newIPArray[i]=ip.charAt(i);
            if(index==3){
                newIPArray[i+1]='1';
                break;
            }
        }
        //去掉末尾的空格，有空格的话，填充到模板里面，会有一个null
        //从而报错 invalid null character in command
        String newIP = new String(newIPArray).trim();


        String template = "firewall-cmd --permanent --add-rich-rule=\"rule family=\"ipv4\" source address=\"%s/24\" accept \" ";

        String cmd1= String.format(template,newIP);
        String cmd2 = "firewall-cmd --reload";

        //用;分割多条命令
        String finalCmd = cmd1+";"+cmd2;

        String[] cmd = new String[] { "/bin/sh","-c",finalCmd};

        try {
            //多次调用Runtime.getRuntime()不生效 不知道为什么
            Runtime.getRuntime().exec(cmd);
            log.info("执行了cmd命令");

        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO 开一个线程定时清除规则
    }
}
