package org.konghao.aujaker.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/9 14:56.
 */
@Component
public class RecordTools {

    @Autowired
    private ConfigTools configTools;

    private static final String FILE_NAME = "record.txt";

    public static final String XML_TYPE = "1";
    public static final String WEB_TYPE = "2";
    public static final String EXCEL_TYPE = "3";

    public void addRecord(String type, String ip) {
        ip = ip==null?"":ip;
        PrintStream ps = null;
        try {
            String old = readRecord();
            ps = new PrintStream(new FileOutputStream(configTools.getUploadPath("/")+FILE_NAME), true, "UTF-8");
            ps.println(buildCon(type, ip));
            ps.println(old);
        } catch (Exception e) {
        } finally {
            if(ps!=null) {ps.close();}
        }
    }

    public String readRecord() {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(configTools.getUploadPath("/")+FILE_NAME), "UTF-8"));
            String str ;
            while((str=br.readLine())!=null) {
                sb.append(str).append("\n");
            }
        } catch (Exception e) {
        } finally {
            try {
                if(br!=null) {br.close();}
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    private String buildCon(String type, String ip) {
        String t = (XML_TYPE.equals(type))?"XML":(WEB_TYPE.equals(type)?"在线":"Excel");
        StringBuffer sb = new StringBuffer();
        sb.append("<p>").append(curDate()).append(" 以[<b>").append(t).append("</b>]方式创建了项目 ").append(ip).append(" ").append(AddressTools.getAddressByIp(ip)).append("</p>\n");
        return sb.toString();
    }

    private String curDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
