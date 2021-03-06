package com.cn.calix.server.service;

import com.cn.calix.DemoApplication;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/10
 * Time: 上午11:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@WebAppConfiguration()
public class XMLServiceTest {
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(XMLServiceTest.class);

    @Resource
    private XMLService service;


    @Test
    public void getValueByPath() throws Exception {

        Document document=null;
        SAXReader reader=new SAXReader();
//        document = reader.read(new File( "/Users/wubingyu/IdeaProjects/Proxy_b6/src/main/java/com/calix/conf/ProxySettings.xml" ));
//        XMLService service=new XMLService();
        String path=service.getPath();
        document = reader.read(new File(path));
        service.getValueByPath(document,"ANASetting/clients/client");

    }

    @Test
    public void getPath() throws Exception {
        String name=System.getProperties().getProperty("os.name");
        logger.info("name:"+name);

    }

    @Test
    public void loadXML() throws Exception {

    }

    @Test
    public void getDeviceNameByRequest()throws Exception{
        StringBuffer request=new StringBuffer();
        BufferedReader bufferedReader=new BufferedReader(new FileReader("/Users/wubingyu/IdeaProjects/ANA/input/Sample.xml"));
        String line="";
        while ((line=bufferedReader.readLine())!=null){
            request.append(line);
        }
        bufferedReader.close();
        service.getDeviceNameByRequest(request.toString());
    }






}