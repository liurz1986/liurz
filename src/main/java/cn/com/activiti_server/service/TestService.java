package cn.com.activiti_server.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TestService {
	// 申请
	public void activiti() {
		System.out.println("任务已经执行.....................................");
	}

	// 审批
	public List<String> user() {
		return Arrays.asList("xiaoming", "xiaohong");
	}
}