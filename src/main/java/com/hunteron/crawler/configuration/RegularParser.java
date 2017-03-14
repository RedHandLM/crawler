package com.hunteron.crawler.configuration;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;

/**
 * 配置文件解析
 */
public class RegularParser {

    private static Element root;

    public static HashMap<String,HashMap<String, String>> getXML(Path path) {
        HashMap<String,HashMap<String, String>> lst = new HashMap<>();
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(RegularParser.class.getClassLoader().getResourceAsStream(path.getValue()));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        root = document.getRootElement();
		List<?> keyword = RegularParser.root.elements();
        for (Object aKeyword : keyword) {
            Element firstEle = ((Element) aKeyword);
            List<?> keywordNum = firstEle.elements();
            HashMap<String, String> param = new HashMap<>();

            for (Object aKeywordNum : keywordNum) {
                Element arr = ((Element) aKeywordNum);
                if (arr == null) {
                    break;
                }
                param.put(arr.getName(), arr.getText());

            }
            lst.put(firstEle.getName(), param);
        }

        return lst;
    }

    public static void main(String[] args){
        System.out.println(RegularParser.getXML(Path.REDIS));
    }
}
