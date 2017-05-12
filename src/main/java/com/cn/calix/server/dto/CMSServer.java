package com.cn.calix.server.dto;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/10
 * Time: 下午2:50
 */
public class CMSServer {

    private String ip;
    private Integer port;

    public CMSServer(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
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

    @Override
    public String toString() {
        return "CMSServer{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
