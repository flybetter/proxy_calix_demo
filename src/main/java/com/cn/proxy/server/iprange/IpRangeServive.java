package com.cn.proxy.server.iprange;

import com.cn.proxy.server.CacheConsts.IpRangeCache;
import com.cn.proxy.server.ana.ANAConstants;
import com.cn.proxy.server.domain.IpRange;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.util.Iterator;
import java.util.List;




public final class IpRangeServive {
	
	private static final Logger logger = Logger.getLogger(IpRangeServive.class);
	
	private static final String NODE_PATH="ANASetting/iprange/cmsserver";
	
	private static final String CMSSERVER_KEY="value";

	

	public static void init(){
		if(logger.isDebugEnabled()){
			logger.debug(" start time:"+System.currentTimeMillis());
		}
		 SAXReader reader = new SAXReader();   
		 Document document=null;
	     try {
//			 document = reader.read(new File( ANAConstants.ROOT_DIR
//					+ ANAConstants.ANA_SETTING_FILE));

          document = reader.read(new File(ANAConstants.ANA_PATH));
		} catch (DocumentException e) {
			logger.error(" DocumentException  ", e);
		}  
	     
	     List<Element> cmsServers = document.selectNodes(NODE_PATH);
         if(cmsServers != null && cmsServers.size() > 0){
        	 for(Iterator it = cmsServers.iterator();it.hasNext();){
        		 IpRange ipRange=new IpRange();
        		 Element element = (Element)it.next();
        		 ipRange.setCmsServerIp(element.attributeValue(CMSSERVER_KEY));
        		 List<Element> childrenElements=element.elements();
        		 if(childrenElements != null && childrenElements.size() > 0){
        			 for(Element children:childrenElements){
        				String [] temp= children.getText().split("_");
        				try {
        					ipRange.addIps(temp[0], temp[1]);
							IpRangeCache.addIpRange(ipRange);
						} catch (Exception e) {
							logger.error(" temp split error ",e);
						}
        				
            		 }
        		 }

        	 }
         }
         
         if(logger.isDebugEnabled()){
 			logger.debug(" end time:"+System.currentTimeMillis());
 		}
         
	}
	
	public static void main(String[] args) {


//	    IpRangeServive.init();
	}
}
