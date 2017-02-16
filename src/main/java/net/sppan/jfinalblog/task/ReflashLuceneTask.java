package net.sppan.jfinalblog.task;

import net.sppan.jfinalblog.lucene.SearcherKit;

import com.jfinal.plugin.cron4j.ITask;

public class ReflashLuceneTask implements ITask{

	@Override
	public void run() {
		System.out.println("定时任务开始重建全文检索索引");
		SearcherKit.reloadIndex();
		System.out.println("重建全文检索索引完毕");
	}

	@Override
	public void stop() {
		
	}

}
