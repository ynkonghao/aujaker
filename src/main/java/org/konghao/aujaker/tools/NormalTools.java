package org.konghao.aujaker.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/5 16:04.
 */
public class NormalTools {

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean isCract(String str) {
        String reg = "[a-zA-Z]";
        return str.matches(reg);
    }

    /**
     * 是否是有效的groupId和artifactId
     * @param name
     * @return
     */
    public static boolean isOkName(String name) {
        return !isContainChinese(name) && isCract(name.substring(0, 1));
    }

    public static boolean isNull(String str) {
        return str==null || "".equalsIgnoreCase(str);
    }
}
