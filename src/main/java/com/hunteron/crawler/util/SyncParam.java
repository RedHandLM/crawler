package com.hunteron.crawler.util;

import com.hunteron.crawler.model.Param;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SyncParam {

    private LinkedBlockingQueue<Param> paramQueue = new LinkedBlockingQueue<>();//共享隊列

    private static class Syn{
        private static final SyncParam syncParam = new SyncParam();
    }

    private SyncParam(){
    }

    public static SyncParam getInstance(){
        return Syn.syncParam;
    }

    public synchronized Param getParam() {
        try {
            return paramQueue.poll();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized int getSize() {
        return paramQueue.size();
    }

    public void addParam(Param param) {
        this.paramQueue.add(param);
    }

    public void addListParam(ConcurrentLinkedQueue<Param> queue){
        this.paramQueue.addAll(queue);
    }
}
