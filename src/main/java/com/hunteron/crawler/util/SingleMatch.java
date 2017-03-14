package com.hunteron.crawler.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SingleMatch {
	
	private static Logger logger = LogManager.getLogger(SingleMatch.class.getName());
	/**
	 * 从code中匹配出一条符合reg正则表达式的结果
	 */
	public static String match(String code, String reg) {

		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(code);
		
		if (matcher.find()) {
			try {
				return matcher.group(1);
			} catch (Exception e) {
				return matcher.group();
			}
		} else {
			return "";
		}
	}
	/**
	 * 从code中匹配出一条符合reg正则表达式的结果
	 * num：返回正则表达式中第num个()中的值
	 */
	public static String match(String code, String reg, int num) {

		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(code);
		
		if (matcher.find()) {
			return matcher.group(num);
		} else {
			return "";
		}
	}

	public static String getString(String code,String param){
		
		String reg = "\"" + param + "\":\"?([\\s\\S]*?)\"?(,|})";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(code);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "";
		}
	}
}
