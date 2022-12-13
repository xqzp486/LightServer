# LightServer
一个简陋的服务器，可以解析HTTP报文

## 实现功能

- HTTP报文的简单解析
- 从HTTP报文中,获取到一个IP参数，进行服务器白名单设置

### 1、HTTP报文简单解析

本质上我们收到的是一串字符串，
我们将这串字符串，按照约定好的格式，进行切割转换，就是HTTP报文<br/>
实现的逻辑就是
- 将SocketChannel中的数据，读取到byte数组
- byte数组转换成char数组，或者转换成String
- 然后根据HTTP报文的规范进行切割，转换成Request请求对象

>PS：注意点<br/>
> 转换成String，然后用String提供的方法进行切割，效率没有用char数组切割效率高<br/>
> 但是用String比较好写，我就直接偷懒了，用String提供的subString来切割字符串了

### 2、Http报文的格式
~~~text

     * |---------------HTTP 请求报文的格式----------------|
     * --------------------------------------------------
     * | method    | sp | URI | Protocol | CRLF |   请求行
     * | 首部字段名 | :  | sp  | 值       |CRLF  |   请求头
     * | 首部字段名 | :  | sp  | 值       |CRLF  |   请求头
     * |CRLF|
     * key = value   如果是Post请求则此处还有请求体
~~~

根据第一个，CRLF 回车换行，进行正则匹配，我们可以获得请求行结束的位置<br/>
根据连续两个CRLF，我们可以获得请求头结束的位置<br/>
根据这两个位置，调用subString，就可以进行拆分了。<br/>
请求行和请求头内部的拆分原理也相同。

~~~java
#拆分的方法
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
~~~

注意：这种拆分效率并不高，要遍历多次，直接在char数组中边过滤边拆分，或许效率更高

### 3、服务器白名单

调用iptables的命令，添加防火墙规则，实现白名单

~~~java
public static void executeCommand(String ip) {
    try {
        String firewalldCMD= String.format("iptables -I INPUT -s %s -j DROP", ip);
        String[] cmd = new String[] { "/bin/sh", "-c", firewalldCMD};
        Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
~~~
