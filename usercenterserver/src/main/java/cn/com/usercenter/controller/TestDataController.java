package cn.com.usercenter.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.usercenter.entity.TestData;
import cn.com.usercenter.service.TestDataService;

@RestController
@RequestMapping("/test")
public class TestDataController {
	@Autowired
	private TestDataService testDataService;

	// https://localhost:8080/usercenter/test/data
	@GetMapping("/data")
	public String testData() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				long start = System.currentTimeMillis();
				TestData testData = null;
				for (int i = 201436; i < 100000000; i++) {
					testData = new TestData();
					testData.setMessage("测试数据，编号：" + i);
					testDataService.save(testData);

				}
				long end = System.currentTimeMillis();
				System.out.println("执行数据插入所花的时间：" + (end - start));

			}
		});
		// 启动线程
		thread.start();
		return "status:success";
	}

	@GetMapping("/testdata")
	public String testData1() {

		TestData testData = new TestData();
		testData.setMessage("测试数据，编号test12");
		testDataService.save(testData);

		return "status:success";
	}

	@GetMapping("/queryDemo")
	public Map<String, Object> queryTest() {

		Map<String, Object> returndata = new HashMap<String, Object>();
		returndata.put("date", new Date());

		return returndata;
	}
}
