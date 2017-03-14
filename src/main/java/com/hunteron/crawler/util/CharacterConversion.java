package com.hunteron.crawler.util;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CharacterConversion {
    private static Logger logger = LogManager.getLogger(CharacterConversion.class.getName());
    public static int stringToInt(String str){
        int rs = 0;
        try{
            rs = Integer.parseInt(str);
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        return rs;
    }

    public static Double jsonStringToDouble(String str){
        Double rs = 0.0;
        try{
            if(str!=null){
                rs = Double.parseDouble(str);
            }else{
                logger.error("json字符为空,str=" + str);
            }
        }catch(NumberFormatException e){
            logger.error("json字符转换出错,str=" + str + e.toString());
        }
        return rs;
    }

    public static Double jedisStringToDouble(String str){
        Double rs = 0.0;
        try{
            if(StringUtils.isNotBlank(str)){
                rs = Double.parseDouble(str);
            }else{
                logger.error("jedis字符为空,str=" + str);
            }
        }catch(NumberFormatException e){
            logger.error("jedis字符转换出错,str=" + str + e.toString());
        }catch(NullPointerException e){

        }
        return rs;
    }

}
