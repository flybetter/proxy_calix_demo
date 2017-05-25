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

    public final static int templatePort=9002;

    public final static int turnupToolPort=9000;

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

    @Override
    public boolean equals(Object o) {
        if (o instanceof CMSServer) {
            CMSServer cmsServer=(CMSServer)o;
            return ip.equals(cmsServer.getIp())&&port.equals(cmsServer.getPort());
        }else{
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
