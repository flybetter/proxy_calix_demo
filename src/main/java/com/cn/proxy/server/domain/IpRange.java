package com.cn.proxy.server.domain;

import com.cn.proxy.server.iprange.IpRangeServive;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;



public class IpRange {
	private static final Logger logger = Logger.getLogger(IpRangeServive.class);
	
	private String cmsServerIp;
		
	private Set<String>ips=new HashSet<String>();

	public String getCmsServerIp() {
		return cmsServerIp;
	}

	public void setCmsServerIp(String cmsServerIp) {
		this.cmsServerIp = cmsServerIp;
	}

	public Set<String> getIps() {
		return ips;
	}

	public void setIps(Set<String> ips) {
		this.ips = ips;
	}
	
	public boolean  addIps(String begin,String end) {
		
		String [] beginSplit=begin.split("\\.");
		String [] endSplit=end.split("\\.");
		
		if(beginSplit.length!=endSplit.length|| beginSplit.length!=4 ||endSplit.length!=4 ){
			logger.error("ipRange form ip and to ip have some erro");
			return false;
		}
		
		Integer startNum=Integer.parseInt(beginSplit[3]);
		Integer endNum=Integer.parseInt(endSplit[3]);
		for(Integer i=startNum;i<=endNum;i++){
			ips.add(beginSplit[0]+"."+beginSplit[1]+"."+beginSplit[2]+"."+i.toString());
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "IpRange [cmsServerIp=" + cmsServerIp + ", ips=" + ips + "]";
	}

	
	public static void main(String[] args) {
		String aa="10.1.1.250";
		String[] bb=aa.split("\\.");
		System.out.println(bb.length);
	}	

}
