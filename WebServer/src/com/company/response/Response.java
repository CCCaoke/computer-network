package com.company.response;

import com.company.entity.User;
import com.company.repository.UserRepository;
import com.company.request.Request;
import com.company.server.HttpServer;

import java.io.*;
import java.util.Date;

public class Response {
    private Request request;
    private OutputStream output;
    private UserRepository userrepo= new UserRepository();
    public Response(OutputStream output)
    {
        this.output = output;
    }
    public void setRequest(Request request) {
        this.request = request;
    }
    //根据url和请求类型返回相应的内容
    public void setReturn() throws IOException {
        if(request.getUrl()==null)
            return;
        if(request.getType().equals("POST"))
        {
            if(request.getUrl().equals("/")||request.getUrl().equals(""))
            {
                //1.无id，自动生成一个id
                User u = new User();
                u.setId(String.valueOf(new Date().getTime()));
                u.setAge(String.valueOf((int)(Math.random()*100)));
                userrepo.addUser(u.getId(),u);
                String msg = "create user "+u.getId()+" successfully."+"\n"+userrepo.getUserById(u.getId());;
                String correctMsg = "HTTP/1.1 200\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + msg.length() + "\r\n" + "\r\n";
                output.write(correctMsg.getBytes());
                output.write(msg.getBytes());
            }
            else {
                //有id
                String id = request.getUrl().split("/")[1];
                User u = new User();
                u.setId(id);
                u.setAge(request.getContent());
                if (userrepo.updateUser(id, u)) {
                    String msg = "update user " + u.getId() + " successfully."+"\n"+userrepo.getUserById(id);;
                    String correctMsg = "HTTP/1.1 200\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + msg.length() + "\r\n" + "\r\n";
                    output.write(correctMsg.getBytes());
                    output.write(msg.getBytes());
                } else {
                    //没有这个user
                    String errorMessage = "HTTP/1.1 404 User Not Found\r\n" + "Content-Type: text/html\r\n"
                            + "Content-Length: 23\r\n" + "\r\n" + "<h1>User Not Found</h1>";
                    output.write(errorMessage.getBytes());
                }

            }
        }
        else if(request.getType().equals("GET"))
        {
            if(request.getUrl().contains(".html"))
            {
                byte[] buffer = new byte[1024];
                FileInputStream fileInput;
                File file = new File(HttpServer.WEB_ROOT,request.getUrl());
                if(file.exists())
                {
                    try {
                        String correctMsg = "HTTP/1.1 200\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + file.length() + "\r\n" + "\r\n";
                        output.write(correctMsg.getBytes());
                        fileInput = new FileInputStream(file);
                        int cur = fileInput.read(buffer,0,buffer.length);
                        while(cur!=-1)
                        {
                            output.write(buffer,0,cur);
                            cur = fileInput.read(buffer,0,buffer.length);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
                            + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
                    output.write(errorMessage.getBytes());
                }
            }
            else
            {
                String id = request.getUrl().split("/")[1];
                if(userrepo.getUserById(id)!=null)
                {
                    String msg = "get user "+id+" successfully."+"<br>"+userrepo.getUserById(id);
                    String correctMsg = "HTTP/1.1 200\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + msg.length() + "\r\n" + "\r\n";
                    output.write(correctMsg.getBytes());
                    output.write(msg.getBytes());
                }
                else
                {
                    //没有这个user
                    String errorMessage = "HTTP/1.1 404 User Not Found\r\n" + "Content-Type: text/html\r\n"
                            + "Content-Length: 23\r\n" + "\r\n" + "<h1>User Not Found</h1>";
                    output.write(errorMessage.getBytes());
                }
            }
        }
        else if(request.getType().equals("PUT"))
        {
            String id = request.getUrl().split("/")[1];
            User u = new User();
            u.setAge(request.getContent());
            u.setId(id);
            if(userrepo.updateUser(id,u)==false)
            {
                //应该create
                userrepo.addUser(id,u);
                String msg = "create user " + u.getId() + " successfully."+"\n"+userrepo.getUserById(id);
                String correctMsg = "HTTP/1.1 200\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + msg.length() + "\r\n" + "\r\n";
                output.write(correctMsg.getBytes());
                output.write(msg.getBytes());
                return;
            }
            String msg = "update user " + u.getId() + " successfully."+"\n"+userrepo.getUserById(id);
            String correctMsg = "HTTP/1.1 200\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + msg.length() + "\r\n" + "\r\n";
            output.write(correctMsg.getBytes());
            output.write(msg.getBytes());
        }

    }
}
