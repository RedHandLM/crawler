package com.hunteron.crawler.handler.news.sina;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import com.hunteron.crawler.downloader.HttpClientUtil;
import com.hunteron.crawler.handler.ProcessTemplate;
import com.hunteron.crawler.handler.news.sina.entity.SinaNews;
import com.hunteron.crawler.util.SingleMatch;
import com.hunteron.crawler.util.ThreadSleep;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class SinaProcess extends ProcessTemplate {

    /**
     * 新浪新闻，列表调研
     *
     * http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php?col=89&spec=&type=&ch=01&k=&offset_page=0&offset_num=0&num=20&asc=&page=1
     * param page
     */

    /**
     * 新浪新闻，内容调研
     *
     * 标题、创建时间、正文、编辑、标签、爬取时间
     */

    /**
     * 定制该网站的下载器
     * @param url
     * @return
     */
    private String download(String url){
        ThreadSleep.sleep();
        return HttpClientUtil.download(url,"gb2312");
    }

    /**
     * test download
     */
    /*public static void main(String[] args) {
        String url = "http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php?col=89&spec=&type=&ch=01&k=&offset_page=0&offset_num=0&num=20&asc=&page=1";
        System.out.println(HttpClientUtil.download(url,"gb2312"));
    }*/
    public void handle() {
        String initUrl = "http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php?col=89&spec=&type=&ch=01&k=&offset_page=0&offset_num=0&num=60&asc=&page=";
        String code = download(initUrl + 1);
        //获取列表的总页数
        String count = SingleMatch.match(code,"count\\s*:\\s*(\\d+),");
        System.out.println(count);
        int pageCount = 1;
        if(StringUtils.isNotBlank(count)){
            pageCount = Integer.parseInt(count)/100 + 1;
        }
        for(int i = 1;i<=pageCount;i++) {
            //开始解析List
            JSONObject jsObj = JSONObject.parseObject(code.replace("var jsonData = ","").replace("};","}").replaceAll("\\s",""));
            JSONArray jsArr= jsObj.getJSONArray("list");
            for (Iterator iterator = jsArr.iterator(); iterator.hasNext();) {
                JSONObject job = (JSONObject) iterator.next();
                extract(job.get("url").toString());
            }
            code = download(initUrl + i);
        }
    }

    public void extract(String url){
        HashMap<String,String> details =  RegularParser.getXML(Path.NEWS_SINA).get("details");
        String code = HttpClientUtil.download(url,"UTF-8").replaceAll("\\s","");
        if(StringUtils.isNotBlank(code)) {
            SinaNews news = new SinaNews();
            news.setTitle(SingleMatch.match(code, details.get("title")));
            news.setCreatedAt(SingleMatch.match(code, details.get("createdAt")).replaceAll("年|月","-").replace("日"," ") + ":00");
            /*news.setContent(SingleMatch.match(code, details.get("content"), 2).replaceAll("<.*?>", ""));
            news.setEditor(SingleMatch.match(code, details.get("editor")));*/
            String tagFlag = SingleMatch.match(code,details.get("tagFlag"));
            if(StringUtils.isNotBlank(tagFlag) && tagFlag.contains("|")){
                news.setTag(SingleMatch.match(tagFlag, details.get("tag")));
            }
            news.setUrl(url);
            System.out.println(JSON.toJSONString(news));
            try {
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(new File("sina.txt"), true));// true表示追加到末尾
                writer.append(JSON.toJSONString(news) + "\n");
                writer.flush();// 使用Buffered***时一定要先清缓冲区再关闭流
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println(url);
        }
    }

    public static void main(String[] args) {
        SinaProcess sinaProcess = new SinaProcess();
        sinaProcess.handle();
    }

}
