package com.cn.high_concurrent;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/8
 * Time: 上午11:13
 */
public class TestThread implements Runnable {

    private int i=0;

    public TestThread( int i) {
        this.i=i;
    }

    @Override
    public void run() {

        TemplateSender templateSender=new TemplateSender();
        templateSender.send("192.168.38.179",9002,1800*1000,"rootgod","root",i );
        //192.168.38.179
        //192.168.33.216
    }
}
