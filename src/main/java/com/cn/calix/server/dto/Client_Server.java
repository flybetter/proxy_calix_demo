package com.cn.calix.server.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/15
 * Time: 下午7:31
 */
public class Client_Server {

    private CMSServer cmsServer;

    private List<Client> clientList=new ArrayList<>();

    public CMSServer getCmsServer() {
        return cmsServer;
    }

    public void setCmsServer(CMSServer cmsServer) {
        this.cmsServer = cmsServer;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    @Override
    public String toString() {
        return "Client_Server{" +
                "cmsServer=" + cmsServer +
                ", clientList=" + clientList +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client_Server that = (Client_Server) o;

        if (cmsServer != null ? !cmsServer.equals(that.cmsServer) : that.cmsServer != null) return false;
        return clientList != null ? clientList.equals(that.clientList) : that.clientList == null;
    }

    @Override
    public int hashCode() {
        int result = cmsServer != null ? cmsServer.hashCode() : 0;
        result = 31 * result + (clientList != null ? clientList.hashCode() : 0);
        return result;
    }
}
