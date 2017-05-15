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
 * Date: 2017/5/3
 * Time: 上午11:16
 */
public class TemplateSender {

    public void send(String host,int port,int timeout,String username,String password,int i) {
        String  linenum= i==0 ? "" :" 第"+i+"次请求";
        try {
            Socket socket=new Socket(host,port);
            socket.setSoTimeout(timeout);
            PrintWriter out=new PrintWriter(socket.getOutputStream());
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("openconnection user="+username+" password="+password+" \n.\n");
            System.out.println(linenum +"openconnection user="+username+" password="+password+" \n.\n");
            out.flush();
            String line="";
            StringBuffer response=new StringBuffer("");
            while((line=in.readLine())!=null){
                if(line!=null&&line.equals(".")){
                    break;
                }
                response.append(line);
            }

            System.out.println(linenum +" login:"+response.toString());

            if(response.toString().contains("success")){
                String origin_text="<?xml version=\"1.0\" encoding=\"UTF-8\"?><command name=\"RunWorkflow\">\t<param name=\"templateOid\">\t\t<value>{[WorkflowTemplate(Name=FPCMaster.template)]}</value>\t</param>\t<param name=\"workflowAttributes\">\t\t<value>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=service)]}</ID>\t\t\t\t<Value type=\"String\">1</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=service_type)]}</ID>\t\t\t\t<Value type=\"String\">HSI</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=operation)]}</ID>\t\t\t\t<Value type=\"String\">MOD</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=device_host_name)]}</ID>\t\t\t\t<Value type=\"String\">RING_3_C2_S2_10.3.132.14</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=dsl_interface_number)]}</ID>\t\t\t\t<Value type=\"String\">7</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=dsl_profile_name)]}</ID>\t\t\t\t<Value type=\"String\">test</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=access_profile_name_int)]}</ID>\t\t\t\t<Value type=\"String\">test</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=service_number_int)]}</ID>\t\t\t\t<Value type=\"String\">1</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=pvc_service_int)]}</ID>\t\t\t\t<Value type=\"String\">0/35</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=activate_service_int)]}</ID>\t\t\t\t<Value type=\"String\">true</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=activate_dsl_port)]}</ID>\t\t\t\t<Value type=\"String\">true</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=key_info)]}</ID>\t\t\t\t<Value type=\"String\">CIRCUITID 71/ARDA/111111//NH</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=key_info_old)]}</ID>\t\t\t\t<Value type=\"String\">CIRCUITID 71/ARDA/111111//NH</Value>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=mac_learning_int)]}</ID>\t\t\t\t<Value type=\"String\"/>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=mac_limit_int)]}</ID>\t\t\t\t<Value type=\"String\"/>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=igmp_group_limit_int)]}</ID>\t\t\t\t<Value type=\"String\"/>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=mac_address_int)]}</ID>\t\t\t\t<Value type=\"String\"/>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=ip_address_int)]}</ID>\t\t\t\t<Value type=\"String\"/>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=ipmask_int)]}</ID>\t\t\t\t<Value type=\"String\"/>\t\t\t</IWorkflowStringAttribute>\t\t\t<IWorkflowStringAttribute>\t\t\t\t<ID type=\"Oid\">{[WorkflowAttribute(Name=ipdg_int)]}</ID>\t\t\t\t<Value type=\"String\"/>\t\t\t</IWorkflowStringAttribute>\t\t</value>\t\t<IMObject_Array/>\t</param></command>";
                out.println(origin_text+"\n.\n");
                System.out.println(linenum +origin_text);
                out.flush();
                line="";
                response=new StringBuffer("");
                while ((line=in.readLine())!=null){
                    if (line!=null&&line.contains(".")){
                        break;
                    }
                    response.append(line);
                }
                System.out.println(linenum +" work:"+response.toString());

                StringBuffer request=new StringBuffer("");
                request.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                request.append("<command name=\"GetWorkflowOutput\">");
                if (response != null) {

                }
                request.append("</command>");
                out.println(request.toString()+"\n.\n");
                System.out.println(linenum +request.toString());
                out.flush();
                line="";
                response=new StringBuffer("");
                while((line=in.readLine())!=null){
                    if (line != null&&line.equals(".")) {
                        break;
                    }
                    response.append(line);
                }
                System.out.println(linenum +" result:"+response.toString());
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {


        String ip="192.168.38.179";
        //192.168.38.179


        ExecutorService cacheService= Executors.newCachedThreadPool();
//        for (int i =1; i <11; i++) {

//            cacheService.execute((i)->{
//        TemplateSender templateSender=new TemplateSender();
//
//        templateSender.send("192.168.38.179",9002,1800*1000,"rootgod","root" ,a);
//            });

//            cacheService.execute(new TestThread(i,ip));
//        }


//        Runnable runnable=()->{
//            System.out.println("111");
//        };
//
//        runnable.run();


        TemplateSender templateSender=new TemplateSender();
        templateSender.send("127.0.0.1",9002,60*1000,"rootgod","root",1);

    }
}
