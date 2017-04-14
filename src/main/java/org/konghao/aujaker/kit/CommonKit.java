package org.konghao.aujaker.kit;

/**
 * 通用工具集合
 * @author konghao
 *
 */
public class CommonKit {
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
}
