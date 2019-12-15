package cn.com.usercenter.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import cn.com.usercenter.util.JdbcUtill;

public class DataTest {

	public static void test() {
		String sql = "insert t_user(id,name,password,createtime,endtime,uuid) values(?,?,?,?,?,?)";
		Connection conn = JdbcUtill.getConnection();
		try {
			PreparedStatement pre = conn.prepareStatement(sql);
			Date date = new Date();
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dataStr = dateFormate.format(date);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.YEAR, 1);// 过去一年(-1),当前的一年后（1）
			Date m = c.getTime();
			String year = dateFormate.format(m);
			pre.setObject(1, 11);
			pre.setObject(2, "test1");
			pre.setObject(3, "test1");
			pre.setObject(4, dataStr);
			pre.setObject(5, year);
			pre.setObject(6, UUID.randomUUID().toString());

			pre.execute();
			pre.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 删除文件夹下所用内容。delete删除的目录（文件夹）下面是没有子文件的；删除的文件（非文件夹）
	public static void test1(File file) {
		if (file.exists()) {// 文件或目录存在
			if (file.isDirectory() && file.listFiles().length > 0) {// 文件是目录并且有子目录
				// 目录下的子文件
				for (File childrenfile : file.listFiles()) {
					test1(childrenfile);
				}

			} else {// 文件不是目录删除
				file.delete();
			}
		}
	}

	public static void stream_writer() {
		try {
			FileWriter fileWriter = new FileWriter(new File("D:/usr/file/test.txt"));
			BufferedWriter buffer = new BufferedWriter(fileWriter);
			buffer.write("create new data");
			buffer.flush();
			buffer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void stream_read() {
		try {
			FileReader fileReader = new FileReader(new File("D:/usr/file/test.txt"));
			BufferedReader buffer = new BufferedReader(fileReader);
			// 读取一行数据
			String res = buffer.readLine();
			while (null != res) {
				System.out.println(res);
				res = buffer.readLine();
			}
			buffer.close();
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// DataTest.test();
		// File file = new File("D:/usr/file");
		// ApplicationContext application=new
		// ClassPathXmlApplicationContext("app");
		// DataTest.test1(file);
		// file.delete();
		// DataTest.stream_read();
		/*
		 * Parent parent = new Parent(); parent.getTest(); Parent node = new
		 * Node(); node.getTest(); Parent children = new Children();
		 * children.getTest();
		 */
		/**
		 * java8x新特性：Date API java.time
		 */
		// Clock 时钟
		Clock clock = Clock.systemDefaultZone();
		long millis = clock.millis();

		Instant instant = clock.instant();
		Date legacyDate = Date.from(instant); // legacy java.util.Date
		System.out.println(System.currentTimeMillis());
		System.out.println(millis);
		System.out.println(ZoneId.getAvailableZoneIds());
		// prints all available timezone ids
		// Timezones 时区
		ZoneId zone1 = ZoneId.of("Europe/Berlin");
		ZoneId zone2 = ZoneId.of("Brazil/East");
		System.out.println(zone1.getRules());
		System.out.println(zone2.getRules());
		// LocalTime 本地时间
		LocalTime now1 = LocalTime.now(zone1);
		LocalTime now2 = LocalTime.now(zone2);

		System.out.println(now1.isBefore(now2)); // false

		long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
		long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

		System.out.println(hoursBetween); // -3
		System.out.println(minutesBetween); // -239

		LocalTime late = LocalTime.of(23, 59, 59);
		System.out.println(late); // 23:59:59

		DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
				.withLocale(Locale.GERMAN);

		LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
		System.out.println(leetTime); // 13:37
		// LocalDate 本地日期
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
		LocalDate yesterday = tomorrow.minusDays(2);

		LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
		DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();

		System.out.println(dayOfWeek); // FRIDAY
		// 从字符串解析一个LocalDate类型和解析LocalTime一样简单：
		DateTimeFormatter germanFormatter1 = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(Locale.GERMAN);

		LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter1);
		System.out.println(xmas); // 2014-12-24

		// LocalDateTime 本地日期时间
		LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

		DayOfWeek dayOfWeek1 = sylvester.getDayOfWeek();
		System.out.println(dayOfWeek1); // WEDNESDAY

		Month month = sylvester.getMonth();
		System.out.println(month); // DECEMBER

		long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
		System.out.println(minuteOfDay); // 1439

		Instant instant1 = sylvester.atZone(ZoneId.systemDefault()).toInstant();

		Date legacyDate1 = Date.from(instant1);
		System.out.println(legacyDate1); // Wed Dec 31 23:59:59 CET 2014

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm");

		// LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13",
		// formatter);
		// String string = formatter.format(parsed);
		// System.out.println(string); // Nov 03, 2014 - 07:13

		/**
		 * java新特性 Map putIfAbsent
		 */
		Map<Integer, String> map = new HashMap<Integer, String>();

		/*
		 * for (int i = 0; i < 10; i++) { map.putIfAbsent(i, "val" + i); }
		 */
		map.putIfAbsent(1, "asa");// key存在就添加数据
		map.putIfAbsent(1, "as1a");
		map.forEach((idzs, val) -> System.out.println(val));

	}
}
