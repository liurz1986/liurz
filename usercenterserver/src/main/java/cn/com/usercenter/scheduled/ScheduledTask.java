package cn.com.usercenter.scheduled;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.usercenter.mail.MailService;
/**
 * spring定时任务
 * 
 *
 */
@Component
public class ScheduledTask {
	
	@Autowired
	private MailService mailService;
	// 创建一个可重用固定线程数的线程池,创建10个线程
	 private ExecutorService pool = Executors.newFixedThreadPool(10);
	
    //每天凌晨1点 
	@Scheduled(cron="0 0 1 * * ?")  
	public void excute(){
		pool.execute(mailService);
	}
}
