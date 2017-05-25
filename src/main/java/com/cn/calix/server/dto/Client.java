package com.cn.calix.server.dto;

import java.net.Socket;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/15
 * Time: 下午4:20
 */
public class Client {

    private String ip;

    private Integer port;

    private String userName;

    private CMSServer  cmsServer;

    private Socket socket;

    public Client(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public Client(String ip, Integer port, String userName) {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public CMSServer getCmsServer() {
        return cmsServer;
    }

    public void setCmsServer(CMSServer cmsServer) {
        this.cmsServer = cmsServer;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Client{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Client) {
            Client client=(Client)o;
            return (ip.equals(client.ip)&&port.equals(client.port));
        }else {
            return  super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }
}
