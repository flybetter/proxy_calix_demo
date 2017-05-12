package com.cn.high_concurrent;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/8
 * Time: 上午11:13
 */
public class TestThread implements Runnable {

    private int i=0;

    private String ip="";

    public TestThread( int i,String ip) {
        this.i=i;
        this.ip=ip;
    }

    @Override
    public void run() {

        TemplateSender templateSender=new TemplateSender();
        templateSender.send(this.ip,9002,1800*1000,"rootgod","root",i );
        //192.168.38.179
        //192.168.33.216
    }
}
