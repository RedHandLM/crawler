package com.hunteron.crawler.util;

import java.util.Random;

public class ThreadSleep{
    /**
     * 随机休眠2-4秒
     */
    public static void sleep(){
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(2001)+2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(int seconds){
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
