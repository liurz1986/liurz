package cn.com.usercenter.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

public class LambdaTest {
	static StringBuffer buffer = new StringBuffer();

	public static void test1() {
		new Thread(() -> System.out.println("In Java8, Lambda expression rocks !!")).start();
	}

	public static void test2() {
		JButton show = new JButton("Show");
		show.addActionListener((e) -> {
			System.out.println("Light, Camera, Action !! Lambda expressions Rocks");
		});
	}

	public static void test3() {
		List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		features.forEach(n -> System.out.println(n));
	}

	/**
	 * ForEach例子
	 * 
	 * @Title: forEachTest
	 * @Description: TODO
	 * @return void
	 */
	public static void test4() {
		int i = 0;
		List<Integer> list = new ArrayList<>();
		List<Integer> result1 = new ArrayList<Integer>();
		list.add(++i);
		list.add(++i);
		list.add(++i);
		list.add(++i);
		list.forEach(cc -> {
			if (cc / 2 == 0) {
				result1.add(cc);
			}
		});
		System.out.println(result1.size());
		Map<String, Object> params = new HashMap<String, Object>();
		params.putIfAbsent("name", "阿里");
		params.putIfAbsent("address", "杭州");
		List<String> result = new ArrayList<String>();
		params.forEach((key, value) -> {
			if (key.equalsIgnoreCase("name")) {
				result.add(String.valueOf(value));
			}
		});
		System.out.println(result.get(0));
	}

	public static void main(String[] args) {
		test3();
	}
}
