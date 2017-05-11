package com.zzsharing.aujaker.util;

/**
 * Created by konghao on 2017/5/11 0011.
 */
public class Commonkit {
    /**
     * 将包名转换为路径名称
     * @param pkg
     * @return
     */
    public static String packageToPath(String pkg) {
        return pkg.replace(".", "/");
    }

    public static boolean isEmpty(Object obj) {
        if(obj==null) return true;
        if(obj instanceof String) {
            return ((String) obj).trim().equals("");
        }
        if(obj instanceof Integer) {
            return (Integer)obj==0;
        }
        return false;
    }




    public static String getObjType(String type) {
        if(type.equals("int")) {
            return "Integer";
        } else if(type.equals("double")) {
            return "Double";
        } else if(type.equals("float")) {
            return "Float";
        } else if(type.equals("char")) {
            return "Character";
        } else if(type.equals("byte")) {
            return "Byte";
        } else if(type.equals("short")) {
            return "Short";
        } else if(type.equals("boolean")) {
            return "Boolean";
        } else if(type.equals("long")) {
            return "Long";
        }
        return type;
    }

    /**
     * 把字符串的第一个字母转换为小写
     * @param val
     * @return
     */
    public static String lowcaseFirst(String val)
    {
        return val.substring(0,1).toLowerCase()+val.substring(1);
    }

    /**
     * 把字符串的第一个字母转换为大写
     * @param val
     * @return
     */
    public static String upcaseFirst(String val)
    {
        return val.substring(0,1).toUpperCase()+val.substring(1);
    }


    // GENERAL_PUNCTUATION 判断中文的“号
    // CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
    // HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
    private static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static final boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
}
