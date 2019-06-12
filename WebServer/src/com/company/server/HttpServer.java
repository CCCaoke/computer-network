package com.company.server;

import com.company.request.Request;
import com.company.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/*
简单的Server类
 */
public class HttpServer {
    //服务器端口
    public static final int PORT = 8081;
    //静态资源根目录
    public static final String WEB_ROOT = "web/static";
    //关闭服务命令
    private static final String SHUTDOWN = "/shutdown";
    /*
    服务器等待连接
     */
    public void await()
    {
        //服务器套接字对象
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(PORT,1,InetAddress.getByName("localhost"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //循环等待请求
        while(true)
        {
            System.out.println("等待连接...");
            Socket s;
            InputStream in;
            OutputStream out;
            try {
                //连接成功
                s = socket.accept();
                in = s.getInputStream();
                out = s.getOutputStream();
                //解析request
                Request request = new Request(in);
                request.parse();
                if(request.getUrl()!=null&&request.getUrl().equals(SHUTDOWN))
                    break;
                //生成Responce
                Response response = new Response(out);
                response.setRequest(request);
                response.setReturn();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
                continue;//当前连接出错，循环继续
            }
        }
    }
}
