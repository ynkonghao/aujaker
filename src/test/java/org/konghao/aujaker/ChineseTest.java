package org.konghao.aujaker;

import org.junit.Test;
import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.kit.PinyinUtil;

public class ChineseTest {

	@Test
	public void testCheckChinese() {
		System.out.println(CommonKit.isChinese("你好"));
		System.out.println(CommonKit.isChinese("你好123"));
		System.out.println(CommonKit.isChinese("123"));
		System.out.println(CommonKit.isChinese("啊abc"));
		System.out.println(PinyinUtil.str2Pinyin("你好a123", null));
		System.out.println(PinyinUtil.strAllFirst2Pinyin("大家好123123aaa"));
	}
	
	@Test
	public void testNumToChar() {
		int n = 10;
		
		System.out.println(Character.valueOf((char)('a'+n)));
	}
}
