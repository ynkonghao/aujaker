package org.konghao.aujaker.tools;

import org.konghao.aujaker.dto.ClsDto;
import org.konghao.aujaker.dto.DBDto;
import org.konghao.aujaker.dto.MavenDto;
import org.konghao.aujaker.dto.PropertyDto;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/8 15:58.
 * 通过表单创建项目的工具类
 */
public class BuildItemTools {

    public static void buildXml(HttpServletRequest request, File targetFile) {
        MavenDto maven = ConstructionSessionTools.getMave(request);
        DBDto db = ConstructionSessionTools.getDB(request);
        List<ClsDto> list = ConstructionSessionTools.getCls(request);
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(targetFile), true, "UTF-8");
            ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
            ps.println("<aujaker>\n");
            ps.println("\n<maven groupId=\""+maven.getGroupId()+"\" artifactId=\""+maven.getArtifactId()+"\"/>\n");

            if("mysql".equalsIgnoreCase(db.getType())) {
                ps.println("\t<database type=\"mysql\" name=\"msg_2017\">\n");
                ps.println("\t\t<username>"+db.getUsername()+"</username>\n");
                ps.println("\t\t<password>"+db.getPassword()+"</password>\n");
                ps.println("\t\t<url>jdbc:mysql://"+db.getUrl()+":"+db.getPort()+"/"+db.getDatabase()+"</url>\n");
                ps.println("\t\t<driver>com.mysql.jdbc.Driver</driver> \n");
                ps.println("\t</database>\n");
            } else if("sqlite3".equalsIgnoreCase(db.getType())) {
                ps.println("\t<database type=\"sqlite3\" name=\""+db.getDatabase()+".db\">\n");
                ps.println("\t\t<url>jdbc:sqlite::resource:"+db.getDatabase()+".db</url>\n");
                ps.println("\t\t<driver>org.sqlite.JDBC</driver>\n");
                ps.println("\t</database>\n");
            }

            ps.println("\t<model>\n");

            for(ClsDto cd : list) {
                ps.println("\t\t<class className=\""+cd.getClassName()+"\" tableName=\""+cd.getTableName()+"\" comment=\""+cd.getClassShowName()+"信息\" author=\""+cd.getAuthor()+"\" classShowName=\""+cd.getClassShowName()+"\">\n");
                ps.println("\t\t\t<properties>\n");
                for(PropertyDto pd : cd.getPropertyDtoList()) {
                    ps.println("\t\t\t\t<prop name=\""+pd.getName()+"\" columnName=\""+pd.getColumnName()+"\" type=\""+pd.getType()+"\" isLob=\""+("1".equals(pd.getIsLob())?"true":"false")+"\" comment=\""+pd.getComment()+"\" isPk=\""+("1".equals(pd.getIsPk())?"true":"false")+"\" pkType=\""+("Integer".equalsIgnoreCase(pd.getType())?"0":"1")+"\"/>\n");
                }
                ps.println("\t\t\t</properties>\n");
                ps.println("\t\t</class>\n");
            }

            ps.println("\t</model>\n");

            ps.println("</aujaker>");
        } catch (Exception e) {
        } finally {
            if(ps!=null) {ps.close();}
        }
    }
}
