package org.konghao.aujaker.controller;

import org.konghao.aujaker.dto.ClsDto;
import org.konghao.aujaker.dto.DBDto;
import org.konghao.aujaker.dto.PropertyDto;
import org.konghao.aujaker.dto.ResDto;
import org.konghao.aujaker.service.IProjectService;
import org.konghao.aujaker.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过页面构造项目
 * Created by 钟述林 393156105@qq.com on 2017/5/5 9:37.
 */
@Controller
@RequestMapping(value = "construction")
public class ConstructionController {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private RecordTools recordTools;

    @GetMapping(value = "index")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("maven", ConstructionSessionTools.getMave(request));
        model.addAttribute("db", ConstructionSessionTools.getDB(request));
        model.addAttribute("cls", ConstructionSessionTools.getCls(request));
        return "construction/index";
    }

    @GetMapping(value = "show")
    public String show(Model model, HttpServletRequest request) {
        model.addAttribute("maven", ConstructionSessionTools.getMave(request));
        model.addAttribute("db", ConstructionSessionTools.getDB(request));
        model.addAttribute("cls", ConstructionSessionTools.getCls(request));
        return "construction/show";
    }

    @GetMapping(value = "test")
    public @ResponseBody String test(HttpServletRequest request) {
        List<ClsDto> list = ConstructionSessionTools.getCls(request);
        return list.toString();
    }

    @PostMapping(value = "save")
    public @ResponseBody String save(String step, HttpServletRequest request) {
        if("1".equalsIgnoreCase(step)) {
            String groupId = request.getParameter("groupId");
            String artifactId = request.getParameter("artifactId");
            if(NormalTools.isOkName(groupId) && NormalTools.isOkName(artifactId)) {
                ConstructionSessionTools.saveMaven(request, groupId, artifactId);
            } else {return "0";}
        } else if("2".equalsIgnoreCase(step)) {
            String type = request.getParameter("type");
            String url = request.getParameter("url");
            String port = request.getParameter("port");
            Integer p = null;
            String database = request.getParameter("database");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if("mysql".equalsIgnoreCase(type)) {
                try {
                    p = Integer.parseInt(port);
                } catch (NumberFormatException e) {
                    return  "-1"; //端口号必须是整数
                }
                if(NormalTools.isNull(url) || NormalTools.isNull(database) || NormalTools.isNull(username) || NormalTools.isNull(password)) {
                    return "-2"; //不能为空
                }
            } else if("sqlite3".equalsIgnoreCase(type)) {
                if(NormalTools.isNull(database)) {
                    return "-2";
                }
            }
            DBDto dto = new DBDto();
            dto.setDatabase(database);
            dto.setPassword(password);
            dto.setPort(p);
            dto.setType(type);
            dto.setUrl(url);
            dto.setUsername(username);
            ConstructionSessionTools.setDB(request, dto);
        } else if("3".equalsIgnoreCase(step)) {
            String type = request.getParameter("type"); //提交类型，cls或pro
            String className = request.getParameter("className");
            if("cls".equalsIgnoreCase(type)) {
                String tableName = request.getParameter("tableName");
                String showName = request.getParameter("showName");
                String author = request.getParameter("author");
                ClsDto dto = new ClsDto();
                dto.setAuthor(author);
                dto.setClassName(className);
                dto.setClassShowName(showName);
                dto.setTableName(tableName);
                dto.setPropertyDtoList(new ArrayList<>());
                ConstructionSessionTools.addCls(request, dto);
            } else if("pro".equalsIgnoreCase(type)) {
                String name = request.getParameter("name");
                String columnName = request.getParameter("columnName");
                String fieldType = request.getParameter("fieldType");
                String comment = request.getParameter("comment");
                String pk = request.getParameter("pk");
                String lob = request.getParameter("lob");
                PropertyDto dto = new PropertyDto();
                dto.setColumnName(columnName);
                dto.setComment(comment);
                dto.setIsLob(lob);
                dto.setIsPk(pk);
                dto.setName(name);
                dto.setType(fieldType);
                ConstructionSessionTools.addProperty(request, className, dto);
            }
        }
        return "1";
    }

    @PostMapping(value = "remove")
    public @ResponseBody String remove(HttpServletRequest request, String type) {
        String className = request.getParameter("className");
        if("cls".equals(type)) {
            ConstructionSessionTools.removeCls(request, className);
        } else if("pro".equalsIgnoreCase(type)) {
            String name = request.getParameter("name");
            ConstructionSessionTools.removePro(request, className, name);
        }
        return "1";
    }

    //创建项目
    @PostMapping(value = "build")
    public @ResponseBody
    ResDto build(HttpServletRequest request) {
        try {
            String dirName = System.currentTimeMillis()+"";
            File targetFile = new File(configTools.getUploadPath("/"+dirName)+"/"+dirName+".xml");
            BuildItemTools.buildXml(request, targetFile);
            String artId = projectService.initProject(configTools.getUploadPath("/"+dirName), targetFile.getAbsolutePath());

            String path = "/"+dirName+"/"+artId+"/"+artId+".tar.gz";
            recordTools.addRecord(RecordTools.WEB_TYPE, request.getRemoteAddr());
            return new ResDto("1", path);
        } catch (Exception e) {
            return new ResDto("0", e.getMessage());
        }
    }
}
