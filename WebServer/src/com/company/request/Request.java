package com.company.request;

import java.io.IOException;
import java.io.InputStream;

public class Request {
    private InputStream input;
    private String url;
    private String type;
    private String content;
    public Request(InputStream input){
        this.input = input;
    }
    public void parse()
    {
        StringBuffer request = new StringBuffer();
        byte[] buffer = new byte[1024];
        try {
            int i = input.read(buffer);
            for(int j=0;j<i;j++)
            {
                request.append((char)buffer[j]);//从input中读取到request信息
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(request);
        //得到请求的url
        url = parseUrl(request.toString());
        if(url!=null)
        {
            type = parseType(request.toString());
            if(type.toUpperCase().equals("POST")||
                    type.toUpperCase().equals("PUT"))
            {
                //得到数据对象
                content = parseContent(request.toString());
            }
        }
    }
    //得到请求的url
    public String parseUrl(String request)
    {
        int start = request.indexOf(' ');
        if(start!=-1)
        {
            int end = request.indexOf(' ',start+1);
            if(end!=-1)
                return request.substring(start+1,end);
        }
        return null;
    }
    //得到请求类型
    public String parseType(String request)
    {
        return request.split(" ")[0].toUpperCase();
    }
    //得到传来的数据
    public String parseContent(String request)
    {
        String[] args = request.split("\n");
        String res = args[args.length-1];
        if(res.equals("\r"))
            res = "no data";
        return res;
    }
    public String getUrl() {
        return url;
    }
    public String getType(){
        return type;
    }
    public String getContent()
    {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
