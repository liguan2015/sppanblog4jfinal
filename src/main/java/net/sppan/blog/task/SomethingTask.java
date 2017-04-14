package net.sppan.blog.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.cron4j.ITask;

public class SomethingTask implements ITask{
	private final Logger logger = LoggerFactory.getLogger(SomethingTask.class);
	
	@Override
	public void run() {
		logger.debug("定时任务在执行咯");
	}

	@Override
	public void stop() {
		
	}
}
