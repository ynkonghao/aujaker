package org.konghao.aujaker.tools;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/8 17:15.
 */
@Component
public class MyTimer {

    @Autowired
    private ConfigTools configTools;

    //每天22点20分50秒时执行
    @Scheduled(cron = "50 20 22 * * ?")
    public void deleteFiles() {
        try {
            File f = new File(configTools.getUploadPath("/"));
            File [] files = f.listFiles();
            for(File file : files) {
                if(file.isFile()) {
                    FileUtils.forceDelete(file);
                } else if(file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
