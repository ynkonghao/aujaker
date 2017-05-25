package org.konghao.aujaker.controller;

import org.apache.commons.io.FileUtils;
import org.konghao.aujaker.dto.ResDto;
import org.konghao.aujaker.service.IProjectService;
import org.konghao.aujaker.tools.ConfigTools;
import org.konghao.aujaker.tools.ConstructionSessionTools;
import org.konghao.aujaker.tools.IPTools;
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
    public String index(Model model, String hasJar) {
        model.addAttribute("hasJar", hasJar);
        return "index";
    }
   
    @PostMapping(value="uploadXml")
    public @ResponseBody
    ResDto uploadXml(HttpServletRequest request, String hasJar, @RequestParam("file")MultipartFile[] files) {
        String dirName = UUID.randomUUID().toString();
        ConstructionSessionTools.addItem(request, RecordTools.XML_TYPE, dirName, "0", null);
        try {
            if(files.length>=1) {
                String fileName = files[0].getOriginalFilename();

                File targetFile = new File(configTools.getUploadPath("/item/"+ dirName)+dirName+getFileName(fileName));
                FileUtils.copyInputStreamToFile(files[0].getInputStream(), targetFile);

                String artId = projectService.initProject(configTools.getUploadPath("/item/"+dirName), targetFile.getAbsolutePath(), "1".equals(hasJar));

                String path = "/item/"+dirName+"/"+artId+"/"+artId+".tar.gz";

//                recordTools.addRecord(RecordTools.XML_TYPE, request.getRemoteAddr());
                ConstructionSessionTools.addItem(request, RecordTools.XML_TYPE, dirName, "1", path);
                recordTools.addRecord(RecordTools.XML_TYPE, IPTools.getIpAddress(request));
                return new ResDto("1", path);
            } else {
            }
            return new ResDto("0", "未检测到上传文件");
        } catch (Exception e) {
//            throw new SystemException("文件解析出错");
            e.printStackTrace();
//            return "0";
            ConstructionSessionTools.addItem(request, RecordTools.XML_TYPE, dirName, "0", "创建项目出错："+e.getMessage());
            return new ResDto("0", "创建项目出错："+e.getMessage());
        }
    }

    private String getFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    @GetMapping(value = "record")
    public String record(Model model, HttpServletRequest request) {
        model.addAttribute("datas", recordTools.readRecord());
        return "record";
    }

    @GetMapping(value = "items")
    public String items(Model model, HttpServletRequest request) {
        model.addAttribute("datas", ConstructionSessionTools.getItems(request));
        return "items";
    }
}
