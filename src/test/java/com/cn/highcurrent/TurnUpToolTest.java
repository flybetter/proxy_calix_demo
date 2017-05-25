package com.cn.highcurrent;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/4/24
 * Time: 下午3:26
 */
public class TurnUpToolTest {
        public void send(String ip,Integer port,int timeout,String username,String password){
            try {
                Socket socket=new Socket(ip,port);
                socket.setSoTimeout(timeout);
                BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out=new PrintWriter(socket.getOutputStream());
                out.println("openconnection user="+username+" password="+password+"\n.\n");
                System.out.println(" command: openconnection user="+username+" password="+password+" \n.\n");
                out.flush();
                String line="";
                StringBuffer response=new StringBuffer("");
                while((line=in.readLine())!=null){
                    if (line != null&&line.equals(".")) {
                        break;
                    }
                    response.append(line);
                }
                System.out.println(" result:"+response.toString());

                if (response.toString().contains("success")) {
                    String origin_text="<command name=\"Create\">    <param name=\"imobject\">        <value>            <management.IElementManagement>                <ID type=\"Oid\">{[MCNetwork][MCVM(IP=10.245.57.111)][Avm(AvmNumber=555)][ElementManagement(Key=10.245.57.111)]}</ID>                <AdminStatusEnum type=\"Integer\">0</AdminStatusEnum>                <ElementClassEnum type=\"Integer\">0</ElementClassEnum>                <ElementName type=\"String\">10.245.57.111</ElementName>                <ICMPEnabled type=\"Boolean\">false</ICMPEnabled>                <ICMPPollingRate type=\"Integer\">0</ICMPPollingRate>                <IP type=\"com.sheer.types.IPAddress\">1.2.3.4</IP>                <MaintenanceEnabled type=\"Boolean\">false</MaintenanceEnabled>                <PollingGroup type=\"management.IPollingGroupManagement\">                    <ID type=\"Oid\">{[MCNetwork][MCVM(IP=10.245.57.111)][Avm(AvmNumber=555)][ElementManagement(Key=Test)][PollingGroupManagement(Name=default)]}</ID>                </PollingGroup>                <SNMPEnabled type=\"Boolean\">true</SNMPEnabled>                <SNMPReadCommunity type=\"String\">public</SNMPReadCommunity>                <SNMPWriteCommunity type=\"String\">private</SNMPWriteCommunity>                <SchemeName type=\"\">Null</SchemeName>                <SnmpV3AuthenticationEnum type=\"Integer\">0</SnmpV3AuthenticationEnum>                <SnmpV3AuthenticationPassword type=\"String\" />                <SnmpV3AuthenticationUserProfile type=\"String\" />                <SnmpV3EncryptionPassword type=\"String\" />                <SnmpVersionEnum type=\"Integer\">0</SnmpVersionEnum>                <TelnetEnabled type=\"Boolean\">true</TelnetEnabled>                <TelnetPortNumber type=\"Integer\">23</TelnetPortNumber>                <TelnetProtocolEnum type=\"Integer\">0</TelnetProtocolEnum>                <TelnetSequence type=\"String\">User:,admin,Password,admin,enable,admin,#,</TelnetSequence>            </management.IElementManagement>        </value>    </param></command>";
                    out.println(origin_text+"\n.\n");
                    out.flush();
                    System.out.println(" command:"+origin_text);
                    line="";
                    response=new StringBuffer("");
                    while ((line=in.readLine())!=null){
                        if (line!=null&&line.equals(".")){
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
        TurnUpToolTest turnUpToolTest=new TurnUpToolTest();
        turnUpToolTest.send("127.0.0.1",9002,1000*1000,"rootgod","root");
    }
}
