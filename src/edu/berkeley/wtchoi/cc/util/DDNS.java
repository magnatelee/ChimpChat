package edu.berkeley.wtchoi.cc.util;

import java.io.*;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: cusgadmin
 * Date: 4/3/12
 * Time: 12:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class DDNS {
    //Using API of oa.ta DDNS
    //URL scrapping code snippet from "http://www.mungchung.com/xe/index.php?mid=protip&category=3009&document_srl=4474"
    public static boolean regist(String addr, String id, String id2){
        try{
            URL url;
            StringBuffer url_content = new StringBuffer();
            String url_str = "http://oa.to/api/?type=update&id="+id+"&password="+id2 +"&host[" + addr + "]";
            System.out.println(url_str);
            url = new URL(url_str);

            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while(true){
                String inStr = br.readLine();
                if(inStr == null) break;
                url_content.append(inStr+"\r\n");
            }

            br.close();
            isr.close();
            is.close();

            System.out.println("URL updated");
            System.out.println(url_content.toString());
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
