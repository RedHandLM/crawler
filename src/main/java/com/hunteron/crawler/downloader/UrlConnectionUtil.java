package com.hunteron.crawler.downloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;


public class UrlConnectionUtil {
	private static Logger logger = LogManager.getLogger(UrlConnectionUtil.class.getName());

	public static String getHtmlCode(String url, HashMap<String,HashMap<String, String>> config, String coding) {
		// 从配置文件中获取header值
		HashMap<String, String> header = config.get("header");
		StringBuilder pageHTML = new StringBuilder();
		try {
			URL path = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) path.openConnection();
			// 设置爬取时间限制，防止线程忙等
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			// 伪装爬虫
			Iterator<String> iter = header.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				String val = header.get(key);
				if (!val.equals("")) {
					connection.setRequestProperty(key, val);
				}
			}
			connection.connect();
			InputStream urlStream = connection.getInputStream();
			// 如果出SocketException,连接问题，return null
			if (urlStream.available() == 0) {
				return null;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					urlStream, coding));
			String line;
			while ((line = br.readLine()) != null) {
				pageHTML.append(line);
				pageHTML.append("\r\n");
			}
			br.close();
			urlStream.close();
			connection.disconnect();
		} catch (FileNotFoundException e){
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return pageHTML.toString();
	}

	public static String getHtmlCodes(String url, HashMap<String,HashMap<String, String>> config, String coding) {
		// 获取源码
		String code = UrlConnectionUtil.getHtmlCode(url, config, coding);

		// 源码未获取到，重复3次获取
		if (code == null) {
			for (int i = 0; i < 3; i++) {
				code = UrlConnectionUtil.getHtmlCode(url, config, coding);
				if (code != null) {
					return code;
				}
			}
		}
		return code;
	}

}
