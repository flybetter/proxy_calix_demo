package com.cn.high_concurrent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/2
 * Time: 下午10:18
 */
public class MessageSender2 {

    public void send(String ip,int port, int timeout,String username,String password){

        try {
            Socket socket=new Socket(ip,port);
            socket.setSoTimeout(timeout);

            PrintWriter out=new PrintWriter(socket.getOutputStream());
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("openconnection user="+username+" password="+password+" \n.\n");
            out.flush();

            StringBuffer response=new StringBuffer("");
            String line=null;
            while((line=in.readLine())!=null){
                if (line != null&&line.equals(".")) {
                    break;
                }
                response.append(line);
            }
            System.out.println(" login:"+response.toString());

            if (response.toString().contains("success")){

                 String origin_text="<command name=\"Create\">    <param name=\"imobject\">        <value>            <management.IElementManagement>                <ID type=\"Oid\">{[MCNetwork][MCVM(IP=10.245.57.111)][Avm(AvmNumber=555)][ElementManagement(Key=10.245.57.111)]}</ID>                <AdminStatusEnum type=\"Integer\">0</AdminStatusEnum>                <ElementClassEnum type=\"Integer\">0</ElementClassEnum>                <ElementName type=\"String\">10.245.57.111</ElementName>                <ICMPEnabled type=\"Boolean\">false</ICMPEnabled>                <ICMPPollingRate type=\"Integer\">0</ICMPPollingRate>                <IP type=\"com.sheer.types.IPAddress\">1.2.3.4</IP>                <MaintenanceEnabled type=\"Boolean\">false</MaintenanceEnabled>                <PollingGroup type=\"management.IPollingGroupManagement\">                    <ID type=\"Oid\">{[MCNetwork][MCVM(IP=10.245.57.111)][Avm(AvmNumber=555)][ElementManagement(Key=Test)][PollingGroupManagement(Name=default)]}</ID>                </PollingGroup>                <SNMPEnabled type=\"Boolean\">true</SNMPEnabled>                <SNMPReadCommunity type=\"String\">public</SNMPReadCommunity>                <SNMPWriteCommunity type=\"String\">private</SNMPWriteCommunity>                <SchemeName type=\"\">Null</SchemeName>                <SnmpV3AuthenticationEnum type=\"Integer\">0</SnmpV3AuthenticationEnum>                <SnmpV3AuthenticationPassword type=\"String\" />                <SnmpV3AuthenticationUserProfile type=\"String\" />                <SnmpV3EncryptionPassword type=\"String\" />                <SnmpVersionEnum type=\"Integer\">0</SnmpVersionEnum>                <TelnetEnabled type=\"Boolean\">true</TelnetEnabled>                <TelnetPortNumber type=\"Integer\">23</TelnetPortNumber>                <TelnetProtocolEnum type=\"Integer\">0</TelnetProtocolEnum>                <TelnetSequence type=\"String\">User:,admin,Password,admin,enable,admin,#,</TelnetSequence>            </management.IElementManagement>        </value>    </param></command>.";

                 out.println(origin_text+"\n.\n");
                 out.flush();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response=new StringBuffer("");
                 line=null;
                 while((line=in.readLine())!=null){
                    if (line != null&&line.equals(".")) {
                        break;
                    }
                    response.append(line);
                }

                System.out.println("result:"+response.toString());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

        ExecutorService executorService= Executors.newCachedThreadPool();

        for (int i = 0; i <10; i++) {
            executorService.execute(()->{
                MessageSender2 messageSender2=new MessageSender2();
                messageSender2.send("192.168.38.179",9000,50*1000,"admin","test123");
            });
        }


//        MessageSender2 messageSender2=new MessageSender2();
//        messageSender2.send("192.168.33.216",9000,50*1000,"rootgod","root");

    }
}
