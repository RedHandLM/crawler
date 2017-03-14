package com.hunteron.crawler.handler;

public class ProcessTransit {

    /**
     * 通过传递进来的参数type来判断初始url类型 决定初始哪个方法
     */
    public static ProcessTemplate getClass(String type) {

        ProcessTemplate temp = new ProcessTemplate();

        try {
            temp = (ProcessTemplate) Class.forName(type).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
