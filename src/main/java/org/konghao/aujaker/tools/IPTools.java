package org.konghao.aujaker.tools;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/11 9:37.
 * 获取用户IP地址
 */
public class IPTools {

    public static String getIpAddress(HttpServletRequest request) {
        return request.getHeader("X-Real-IP"); //使用Nginx做反向代理需要使用此方法获取用户真实IP
        //return request.getRemoteAddr(); //
    }
}
