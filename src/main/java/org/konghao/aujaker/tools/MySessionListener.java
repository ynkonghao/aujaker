package org.konghao.aujaker.tools;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/8 16:36.
 */
@WebListener
public class MySessionListener implements HttpSessionListener {

    @Autowired
    private ConfigTools configTools;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
//        System.out.println("======");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        List<String> dirList = ConstructionSessionTools.getDirs(session);
        for(String d : dirList) {
            System.out.println(d);
            File f = new File(configTools.getUploadPath("/"+d));
            if(f.exists()) {f.delete();}
        }
    }
}
