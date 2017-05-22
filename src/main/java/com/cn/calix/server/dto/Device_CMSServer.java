package com.cn.calix.server.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/22
 * Time: 上午9:45
 */
public class Device_CMSServer {

    private CMSServer cmsServer;

    private Set<String> deviceSet=new HashSet<>();

    public CMSServer getCmsServer() {
        return cmsServer;
    }

    public void setCmsServer(CMSServer cmsServer) {
        this.cmsServer = cmsServer;
    }

    public Set<String> getDeviceSet() {
        return deviceSet;
    }

    public void setDeviceSet(Set<String> deviceSet) {
        this.deviceSet = deviceSet;
    }

    public CMSServer getCMSServerByDeviceName(String deviceName){
        if (deviceSet.contains(deviceName)){
            return this.cmsServer;
        }else {
            return null;
        }
    };
    
    public void addDeviceName(String deviceName){
        deviceSet.add(deviceName);
    };

    @Override
    public String toString() {
        return "Device_CMSServer{" +
                "cmsServer=" + cmsServer +
                ", deviceList=" + deviceSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device_CMSServer that = (Device_CMSServer) o;

        if (cmsServer != null ? !cmsServer.equals(that.cmsServer) : that.cmsServer != null) return false;
        return deviceSet != null ? deviceSet.equals(that.deviceSet) : that.deviceSet == null;
    }

    @Override
    public int hashCode() {
        int result = cmsServer != null ? cmsServer.hashCode() : 0;
        result = 31 * result + (deviceSet != null ? deviceSet.hashCode() : 0);
        return result;
    }
}
