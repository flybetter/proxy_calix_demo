package com.cn.telnet;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/5
 * Time: 下午1:58
 */
public class Telnet {

    public void send(String ip,int port,String username,String password){

        TelnetClient telnet=new TelnetClient("vt200");

        try {
            telnet.connect(ip,port);

            InputStream in=telnet.getInputStream();

            PrintWriter out=new PrintWriter(telnet.getOutputStream());

            colon(in);

            write(out,username);

            colon(in);

            write(out,password);

            secondCheck(in);

            write(out,"db");

            secondCheck(in);

            write(out,"create usr USR-admin test123 USRGRP-root-Administrators");

            secondCheck(in);

            if (telnet != null && telnet.isConnected()) {
                telnet.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void write(PrintWriter out,String command){
        out.println(command);
        out.flush();
//        System.out.println(command);
    }


    public void colon(InputStream in){
        try {
            char ch=(char)in.read();
            StringBuffer respones=new StringBuffer("");
            boolean flag=false;
            while(true){

                System.out.print(ch);
                respones.append(ch);
               if (ch==':'){
                   break;
               }
               ch=(char)in.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void secondCheck (InputStream in){
        try {
            char ch=(char)in.read();
            StringBuffer respones=new StringBuffer("");
            while(true ){
                System.out.print(ch);
                if (ch=='$'){
                    break;
                }
                ch=(char)in.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect(){


    }

    public static void main(String[] args) {
        Telnet telnet=new Telnet();
        telnet.send("10.245.250.38",8179,"cmssupport","cmssupport");


    }
}
