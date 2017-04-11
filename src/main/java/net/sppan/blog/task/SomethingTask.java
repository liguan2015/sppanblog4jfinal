package net.sppan.blog.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.cron4j.ITask;
import com.jfinal.plugin.ehcache.CacheKit;

import net.sppan.blog.service.TagService;

public class SomethingTask implements ITask{
	private final Logger logger = LoggerFactory.getLogger(SomethingTask.class);
	
	@Override
	public void run() {
		synTagCount();
	}

	@Override
	public void stop() {
		
	}
	
	/**
	 * 同步标签的文章数量<br>
	 * 
	 * 由于修改和添加博客的时候标签统计只增不减，<br>
	 * 
	 * 本定时任务用于校正数量
	 * 
	 */
	public void synTagCount(){
		long begin = System.currentTimeMillis(); 
		logger.debug("开始同步标签关联的文章数量");
		//查询所有标签
		List<String> list = TagService.me.findAllNameList();
		long count;
		for (String tagName : list) {
			//统计标签的文章数量
			count = Db.queryLong("SELECT COUNT(1) from tb_blog WHERE tags LIKE ?","%" + tagName + "%");
			//更新标签的文章数量
			Db.update("UPDATE tb_tag SET count = ? WHERE name = ?",count,tagName);
		}
		//清除缓存，立即生效
		CacheKit.removeAll(TagService.tagCache);
		long end = System.currentTimeMillis();
		long cost = end - begin;
		logger.debug("同步标签管理的文章数量结束,耗时：" + cost + "毫秒");
		
	}

}
