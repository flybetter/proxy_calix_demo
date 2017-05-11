package com.cn.calix.server.service;

import com.cn.calix.server.dto.CMSServer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/10
 * Time: 上午11:02
 */
@Component
public class XMLService {

    @Value("${Proxy.linux_xmlPath}")
    private String linux_xmlPath;

    @Value("${Proxy.mac_xmlPath}")
    private String mac_xmlPath;

    @Value("${Proxy.win_xmlPath}")
    private String win_xmlPath;

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(XMLService.class);

    private SAXReader reader=null;

    private Document document=null;

    private String CLIENT_PATH="/ANASetting/clients/client";

    //TODO
    public String getPath(){

        String os_name=System.getProperties().getProperty("os.name");

        if (os_name.startsWith("win")||os_name.startsWith("Win") ) {
            /*
             *TODO
             *2017/5/10
             * no test
             */
            return win_xmlPath;
        }else if(os_name.startsWith("Linux")){
            return linux_xmlPath;
        }else if(os_name.startsWith("Mac")){
            return mac_xmlPath;
        }else{
            return "";
        }

    };


    @PostConstruct
    public void loadXML(){
        logger.info("loading xml..");
        String path=this.getPath();

        File file=new File(path);
        reader=new SAXReader();
        try {
            document=reader.read(file);
            List<CMSServer> cmsServers=getValueByPath(document,CLIENT_PATH);
            CMSServerService.cmsServers=cmsServers;
            logger.info("loading xml successful cmsServers:"+cmsServers.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public List<CMSServer> getValueByPath(Document document, String path){
        List<Element> nodes=document.selectNodes(path);
        List<CMSServer> cmsServers=nodes.stream().map((e)-> {
            return  new CMSServer(e.elementText("ipaddress"),Integer.parseInt(e.elementText("port")));
        }).collect(Collectors.toList());
        return  cmsServers;
    }

}
