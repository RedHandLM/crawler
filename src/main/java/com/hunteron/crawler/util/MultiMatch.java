package com.hunteron.crawler.util;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MultiMatch {

    /**
     * 从code中匹配出多个符合reg正则表达式的结果 并在每条结果前加上urlHead
     */
	public static LinkedList<String> match(String code , String reg ,String urlHead){

		LinkedList<String>  result = new LinkedList<String>();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(code);

		while(matcher.find()){
            try {
                result.add(urlHead+matcher.group(1));
            }catch (Exception e){
                result.add(urlHead+matcher.group());
            }
		}
		return result;
	}

	public static LinkedList<String> match(String code , String reg){

		LinkedList<String>  result = new LinkedList<String>();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(code);

		while(matcher.find()){
			try {
				result.add(matcher.group(1));
			}catch (Exception e){
				result.add(matcher.group());
			}
		}
		return result;
	}
}
