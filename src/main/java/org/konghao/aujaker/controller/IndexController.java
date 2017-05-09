package org.konghao.aujaker.controller;

import org.apache.commons.io.FileUtils;
import org.konghao.aujaker.service.IProjectService;
import org.konghao.aujaker.tools.ConfigTools;
import org.konghao.aujaker.tools.ConstructionSessionTools;
import org.konghao.aujaker.tools.RecordTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/4 10:14.
 */
@Controller
public class IndexController {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private RecordTools recordTools;

    @GetMapping({"", "/", "index", "/index"})
    public String index() {
        return "index";
    }

    @PostMapping(value="uploadXml")
    public @ResponseBody
    String uploadExcel(HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        try {
            if(files.length>=1) {
                String fileName = files[0].getOriginalFilename();
                String dirName = UUID.randomUUID().toString();
                File targetFile = new File(configTools.getUploadPath("/")+ dirName+"/"+dirName+getFileName(fileName));
                FileUtils.copyInputStreamToFile(files[0].getInputStream(), targetFile);

                String artId = projectService.initProject(configTools.getUploadPath("/")+dirName, targetFile.getAbsolutePath());

                String path = "/"+dirName+"/"+artId+"/"+artId+".tar.gz";

                recordTools.addRecord(RecordTools.XML_TYPE, request.getRemoteAddr());
                return path;
            } else {
            }
            return "1";
        } catch (Exception e) {
//            throw new SystemException("文件解析出错");
            //e.printStackTrace();
            return "0";
        }
    }

    private String getFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    @GetMapping(value = "record")
    public String record(Model model) {
        model.addAttribute("datas", recordTools.readRecord());
        return "record";
    }
}
