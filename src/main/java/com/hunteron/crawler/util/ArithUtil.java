package com.hunteron.crawler.util;

import java.math.BigDecimal;

/**
 * Double计算工具类,解决精度问题
 */
public class ArithUtil {

    //设置除法的默认精度
    private static final int DEF_DIV_SCALE = 2;

    private ArithUtil(){}
    /**
     * 加法计算
     * @param d1 数值1
     * @param d2 数值2
     * @return 计算结果
     */
    public static double add(double d1,double d2){
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return b1.add(b2).doubleValue();

    }

    /**
     * 减法计算
     * @param d1 数值1
     * @param d2 数值2
     * @return 计算结果
     */
    public static double subtract(double d1,double d2){
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法计算(使用默认精度,精度处理四舍五入)
     * @param d1 数值1
     * @param d2 数值2
     * @return 计算结果
     */
    public static double multiply(double d1,double d2){

        return multiply(d1,d2,DEF_DIV_SCALE);
    }

    /**
     * 乘法计算(使用自定义精度,精度处理四舍五入)
     * @param d1 数值1
     * @param d2 数值2
     * @return 计算结果
     */
    public static double multiply(double d1,double d2, int scale){
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return roundUp(b1.multiply(b2).doubleValue(),scale);
    }

    /**
     * 除法计算(使用默认精度,精度处理四舍五入)
     * @param d1 数值1
     * @param d2 数值2
     * @return 计算结果
     */
    public static double divide(double d1,double d2){

        return divide(d1,d2,DEF_DIV_SCALE);
    }

    /**
     * 除法计算(使用自定义精度,精度处理四舍五入)
     * @param d1 数值1
     * @param d2 数值2
     * @param scale 精度
     * @return 计算结果
     */
    private static double divide(double d1, double d2, int scale){

        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Double四舍五入
     * @param val
     * @return
     */
    public static double roundUp(Double val,int scale){
        BigDecimal b =  new BigDecimal(val);
        return b.setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Double截取
     * @param val
     * @return
     */
    public static double roundDown(Double val,int scale){
        BigDecimal b =  new BigDecimal(val);
        return b.setScale(scale,BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }
}