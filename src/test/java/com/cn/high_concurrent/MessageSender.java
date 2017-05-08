package com.cn.high_concurrent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/2
 * Time: 下午2:47
 */
public class MessageSender {

    private Socket cSocket=null;

    private PrintWriter out=null;

    private BufferedReader in=null;

    private String username=null;

    private String password=null;


    private String origin_text="<command name=\"Create\">    <param name=\"imobject\">        <value>            <management.IElementManagement>                <ID type=\"Oid\">{[MCNetwork][MCVM(IP=10.245.57.111)][Avm(AvmNumber=555)][ElementManagement(Key=10.245.57.111)]}</ID>                <AdminStatusEnum type=\"Integer\">0</AdminStatusEnum>                <ElementClassEnum type=\"Integer\">0</ElementClassEnum>                <ElementName type=\"String\">10.245.57.111</ElementName>                <ICMPEnabled type=\"Boolean\">false</ICMPEnabled>                <ICMPPollingRate type=\"Integer\">0</ICMPPollingRate>                <IP type=\"com.sheer.types.IPAddress\">1.2.3.4</IP>                <MaintenanceEnabled type=\"Boolean\">false</MaintenanceEnabled>                <PollingGroup type=\"management.IPollingGroupManagement\">                    <ID type=\"Oid\">{[MCNetwork][MCVM(IP=10.245.57.111)][Avm(AvmNumber=555)][ElementManagement(Key=Test)][PollingGroupManagement(Name=default)]}</ID>                </PollingGroup>                <SNMPEnabled type=\"Boolean\">true</SNMPEnabled>                <SNMPReadCommunity type=\"String\">public</SNMPReadCommunity>                <SNMPWriteCommunity type=\"String\">private</SNMPWriteCommunity>                <SchemeName type=\"\">Null</SchemeName>                <SnmpV3AuthenticationEnum type=\"Integer\">0</SnmpV3AuthenticationEnum>                <SnmpV3AuthenticationPassword type=\"String\" />                <SnmpV3AuthenticationUserProfile type=\"String\" />                <SnmpV3EncryptionPassword type=\"String\" />                <SnmpVersionEnum type=\"Integer\">0</SnmpVersionEnum>                <TelnetEnabled type=\"Boolean\">true</TelnetEnabled>                <TelnetPortNumber type=\"Integer\">23</TelnetPortNumber>                <TelnetProtocolEnum type=\"Integer\">0</TelnetProtocolEnum>                <TelnetSequence type=\"String\">User:,admin,Password,admin,enable,admin,#,</TelnetSequence>            </management.IElementManagement>        </value>    </param></command>.";

//    public MessageSender(String host,int timeout,int port,String username,String password) throws IOException{
//
//        this.username=username;
//
//        this.password=password;
//
//        cSocket=new Socket(host,port);
//        cSocket.setSoTimeout(timeout);
//
//        out=new PrintWriter(cSocket.getOutputStream());
//        in=new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
//
//    }

    public void  init(String host,int timeout,int port,String username,String password) throws IOException{
        this.username=username;

        this.password=password;

        cSocket=new Socket(host,port);
        cSocket.setSoTimeout(timeout);

        out=new PrintWriter(cSocket.getOutputStream());
        in=new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
    }


    public void process(){

    }

    public boolean login(){
        out.print("openconnection user="+username+" password="+password+" \n.\n");
        out.flush();
        String result=this.receive();
        if (result.contains("success") ) {
            System.out.println("login in success");
            return true;
        }else{
            System.out.println("login in fail");
            return false;
        }
    }

    public String send (String request){
        System.out.println( "request:"+request);
        out.println(request+"\n.\n");
        out.flush();
        return this.receive();
    }


    public String receive(){
        String line=null;
        StringBuffer respone=new StringBuffer("");
        try {
            while((line=in.readLine())!=null){
                if (line != null&&line.equals(".")) {
                    break;
                }
                respone.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("resposne:"+respone.toString());
        return respone.toString();
    }



    public void turnuptool_test(Map<String,String> proxy_ip,Map<String,Integer>port ) throws IOException{

        this.init(proxy_ip.get("linux_ip"),50*1000,port.get("turnup_port"),"rootgod","root");
        if (login()) {
            send(origin_text);
        }
    }
    public void turnuptool_thread_test (Map<String,String> proxy_ip,Map<String,Integer>port ) throws IOException{

        ExecutorService cacheThreadPool= Executors.newCachedThreadPool();

        int i =0;

        for ( i = 0; i <2; i++) {
            cacheThreadPool.execute(()-> {

                MessageSender messageSender=new MessageSender();
                        try {
                            messageSender.init(proxy_ip.get("linux_ip"),50*1000,port.get("turnup_port"),"rootgod","root");
                            if (login()) {
                                System.out.println(Thread.currentThread().getName());
                                send(origin_text);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
//        this.init(proxy_ip.get("linux_ip"),50*1000,port.get("turnup_port"),"rootgod","root");
//        if (login()) {
//            send(origin_text);
//        }
    }


    public static void main(String[] args) throws IOException {

        Map<String,Integer> port=new HashMap<>();
        port.put("turnup_port",9000);
        port.put("template_port",9002);

        Map<String,String> proxy_ip=new HashMap<>();
        proxy_ip.put("pc_ip","192.168.38.179");
        proxy_ip.put("linux_ip","192.168.33.216");
        proxy_ip.put("test_ip","10.245.250.30");

        MessageSender messageSender=new MessageSender();

        messageSender.turnuptool_test(proxy_ip,port);


    }
}
