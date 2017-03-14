package com.hunteron.crawler.handler;

import com.hunteron.crawler.downloader.HttpProxy;
import com.hunteron.crawler.model.Param;
import com.hunteron.crawler.util.ThreadSleep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class DetailProcessTransit implements Runnable {

	private static Logger logger = LogManager.getLogger(DetailProcessTransit.class.getName());
	private String forename;

    private LinkedBlockingQueue<Param> paramQueue = new LinkedBlockingQueue<>();
    public DetailProcessTransit(String methods, LinkedBlockingQueue<Param> paramQueue) {
        this.paramQueue = paramQueue;
        this.forename = methods;
    }

	public void run() {
        DetailProcessTemplate toConsumers = null;
        try {
        	 toConsumers = (DetailProcessTemplate) Class.forName(forename).newInstance();
         } catch (InstantiationException e) {
            e.printStackTrace();
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         }
         while (true) {
              //代理检测
             HttpProxy.check();
             try {
                 toConsumers.handle(paramQueue.take());
             } catch (InterruptedException e) {
                 ThreadSleep.sleep(60);
                 logger.info("没有待爬取的数据，休眠60秒");
             }
         }
    }

}
