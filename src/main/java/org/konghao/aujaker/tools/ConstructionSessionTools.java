package org.konghao.aujaker.tools;

import org.konghao.aujaker.dto.ClsDto;
import org.konghao.aujaker.dto.DBDto;
import org.konghao.aujaker.dto.MavenDto;
import org.konghao.aujaker.dto.PropertyDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/5 10:16.
 */
public class ConstructionSessionTools {

    private static final String MAVEN_NAME = "maven-session";
    private static final String DB_NAME = "db-session";
    private static final String CLS_NAME = "cls-session";
    private static final String DIR_NAME = "dir-names";

    public static void saveMaven(HttpServletRequest request, String groupId, String artifactId) {
        MavenDto dto = new MavenDto(groupId, artifactId);
        setMaven(request, dto);
    }

    public static void setMaven(HttpServletRequest request, MavenDto dto) {
        HttpSession session = request.getSession();
        session.setAttribute(MAVEN_NAME, dto);
    }

    public static MavenDto getMave(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (MavenDto) session.getAttribute(MAVEN_NAME);
    }

    public static void setDB(HttpServletRequest request, DBDto dto) {
        request.getSession().setAttribute(DB_NAME, dto);
    }

    public static DBDto getDB(HttpServletRequest request) {
        return (DBDto) request.getSession().getAttribute(DB_NAME);
    }

    public static void addCls(HttpServletRequest request, ClsDto dto) {
        HttpSession session = request.getSession();
        List<ClsDto> list = (List<ClsDto>) session.getAttribute(CLS_NAME);
        if(list==null) {
            list = new ArrayList<>();
        }
        if(list.contains(dto)) { //如果存在的话先删除，再添加，这样方便修改
            list.remove(dto);
        }
        list.add(dto);
        session.setAttribute(CLS_NAME, list);
    }

    public static List<ClsDto> getCls(HttpServletRequest request) {
        List<ClsDto> res = (List<ClsDto>) request.getSession().getAttribute(CLS_NAME);
        if(res==null) {res = new ArrayList<>();}
        return res;
    }

    public static void addProperty(HttpServletRequest request, String className, PropertyDto dto) {
        List<ClsDto> list = getCls(request);
        if(list==null) {return;}
        try {
            for(ClsDto c : list) {
                if(className.equals(c.getClassName())) {
                    for(PropertyDto pd : c.getPropertyDtoList()) {
                        if(pd.getName().equals(dto.getName())) {
                            c.getPropertyDtoList().remove(pd);
                        }
                        if("1".equals(dto.getIsPk()) && "1".equals(pd.getIsPk())) {
                            c.getPropertyDtoList().remove(pd);
                            pd.setIsPk("0");
                            c.getPropertyDtoList().add(pd);
                        }
                    }
                    c.getPropertyDtoList().add(dto);
                    addCls(request, c);
                }
            }
        } catch (Exception e) {
        }
    }

    public static void removeCls(HttpServletRequest request, String className) {
        List<ClsDto> list = getCls(request);
        try {
            for(ClsDto c : list) {
                if(c.getClassName().equals(className)) {
                    list.remove(c);
                }
            }
        } catch (Exception e) {
        }
        request.getSession().setAttribute(CLS_NAME, list);
    }

    public static void removePro(HttpServletRequest request, String className, String name) {
        List<ClsDto> list = getCls(request);
        for(ClsDto c : list) {
            if(c.getClassName().equals(className)) {
                List<PropertyDto> proList = c.getPropertyDtoList();
                if(proList==null) {proList = new ArrayList<>();}
                try {
                    for(PropertyDto dto : proList) {
                        if(name.equals(dto.getName())) {
                            proList.remove(dto);
                        }
                    }
                } catch (Exception e) {
                }
                c.setPropertyDtoList(proList);
            }
        }
        request.getSession().setAttribute(CLS_NAME, list);
    }
}
