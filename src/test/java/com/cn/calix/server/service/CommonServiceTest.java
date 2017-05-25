package com.cn.calix.server.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/24
 * Time: 下午1:30
 */
public class CommonServiceTest {


    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(CommonServiceTest.class);

    private static final String LOGIN_PATTERN_STR="usr=(.*) password=(.*)";

    private static final Pattern LOGIN_PATTERN=Pattern.compile(LOGIN_PATTERN_STR);

    @Test
    public void getKeyInfo() throws Exception {

    }

    @Test
    public void filter() throws Exception {

    }

    @Test
    public void judge() throws Exception {

    }

    @Test
    public void checkProxyResult() throws Exception {

    }

    @Test
    public void checkPattern()throws  Exception{

        String command="openconnection user=rootgod password=root \n.\n";

        if (LOGIN_PATTERN.matcher(command).find()) {
            logger.info("xxdd"+LOGIN_PATTERN.matcher(command).group(1) );
        }else {
            logger.info("not find");
        }

    }


}