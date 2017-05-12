package com.cn.calix.server.service;

import com.cn.calix.DemoApplication;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.File;

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
        System.out.println(name);

    }

    @Test
    public void loadXML() throws Exception {

    }

}