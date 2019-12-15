package cn.com.usercenter.serviceImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import cn.com.usercenter.entity.User;
import cn.com.usercenter.redis.RedisService;
import cn.com.usercenter.repository.UserRepository;
import cn.com.usercenter.service.UserService;
import cn.com.usercenter.util.DateUtil;
import cn.com.usercenter.util.excelString;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private RedisService redisService;

	@Autowired
	private UserRepository userRepository;

	public void saveOrUpdate(User user) throws ParseException {
		// TODO Auto-generated method stub
		handUser(user);
		userRepository.save(user);
		String token = createToken(user);
		redisService.set("token_" + user.getUuid(), token);
	}

	public void deleteUser(String uuid) {
		// TODO Auto-generated method stub
		userRepository.deleteByUuid(uuid);
		redisService.delete("token_" + uuid);
	}

	public List<User> findAll(int row, int page) {
		if (row <= 0) {
			row = 10;
		}
		if (page <= 0) {
			page = 1;
		}
		Pageable pageable = new PageRequest(page - 1, row);
		return userRepository.findAll(pageable).getContent();
	}

	public String check(String name, String password) {

		User reslut = userRepository.findUser(name, password);
		if (!StringUtils.isEmpty(reslut)) {
			String token = createToken(reslut);
			redisService.set("token_" + reslut.getUuid(), token);
			return token;
		}
		return null;

	}

	public List<User> findUsersByEndTime(int days) {
		// TODO Auto-generated method stub
		return userRepository.findUsersByEndTime(days);
	}

	public boolean uploadExcel(MultipartFile file) {
		if (null == file) {
			return false;
		}
		Workbook wb;
		POIFSFileSystem fs = null;
		try {
			fs = new POIFSFileSystem(file.getInputStream());
			wb = new HSSFWorkbook(fs);
			List<User> users = new ArrayList<User>();
			parseExcel(wb, users);
			userRepository.saveAll(users);
			log.info("upload excel data to database success");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("upload excel data to database failure:" + e.getMessage());
			return false;
		} finally {
			if (null != fs) {
				try {
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public boolean downExcel(HttpServletResponse response) {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			List<User> users = findAllUser();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			HSSFWorkbook hssf = createExcel(users);
			hssf.write(os);

			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			bis = new BufferedInputStream(is);

			// 设置response
			setResponse(response, "t_user.xls");
			ServletOutputStream out = response.getOutputStream();
			bos = new BufferedOutputStream(out);
			// 写入数据
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			log.info("down user message sucess");
			return true;
		} catch (Exception e) {
			e.getStackTrace();
			log.error("down user message failure：" + e.getMessage());
			return false;
		} finally {
			if (null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != bis) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// token组成：{uuid,createTime,userName}
	private String createToken(User result) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(result.getUuid() + ",");
		builder.append(DateUtil.ToString(new Date()) + ",");
		builder.append(result.getName());
		builder.append("}");
		return builder.toString();
	}

	/**
	 * 解析excel
	 * 
	 * @param wb
	 * @param users
	 */
	private void parseExcel(Workbook wb, List<User> users) {
		Sheet sheet = wb.getSheet("sheet1"); // sheet = excel的工作表

		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(firstRowNum);

		int firstCellNum = row.getFirstCellNum();
		int lastCellNum = row.getLastCellNum();

		for (int i = 1; i < lastRowNum + 1; i++) {
			row = sheet.getRow(i);
			User user = new User();
			for (int j = firstCellNum; j < lastCellNum; j++) {
				Cell cell = row.getCell(j);
				switch (j - 1) {
				case 0:
					user.setName(cell.getStringCellValue());
					break;
				case 1:
					user.setPassword(cell.getStringCellValue());
					break;
				case 2:
					user.setEmail(cell.getStringCellValue());
					break;
				case 3:
					user.setUuid(cell.getStringCellValue());
					break;
				case 4:
					user.setEndTime(cell.getDateCellValue());
					break;
				case 5:
					user.setStatus(cell.getStringCellValue());
					break;
				case 6:
					user.setCreateTime(cell.getDateCellValue());
					break;
				case 7:
					user.setUpdateTime(cell.getDateCellValue());
					break;
				}

			}
			users.add(user);
		}

	}

	// 对创建时间、修改时间、有效期进行处理
	private void handUser(User user) throws ParseException {
		// TODO Auto-generated method stub
		if (null != user) {
			// 新加入的用户信息 创建时间和修改时间为null
			if (StringUtils.isEmpty(user.getCreateTime()) && StringUtils.isEmpty(user.getUpdateTime())) {
				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = simple.parse(simple.format(new Date()));
				user.setCreateTime(date);
				user.setUpdateTime(date);
				// 有效期一年，在创建的基础上加一年，
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);// 设置起时间
				cal.add(Calendar.YEAR, 1);// 增加一年
				user.setEndTime(date);
				return;
			}
			// 修改用户信息
			if (!StringUtils.isEmpty(user.getCreateTime()) && StringUtils.isEmpty(user.getUpdateTime())) {
				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = simple.parse(simple.format(new Date()));
				user.setUpdateTime(date);
				return;
			}
		}

	}

	private List<User> findAllUser() {

		List<User> users = userRepository.findAll();

		return users;
	}

	// 设置响应的参数
	private void setResponse(HttpServletResponse response, String fileName) {

		try {
			// 重置response
			response.reset();
			// 设置文件ContentType类型，
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			// 设置文件名
			String filename = new String(fileName.getBytes("gbk"), "iso-8859-1");
			response.addHeader("Content-Disposition", "attachment; filename=" + filename);

			response.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private HSSFWorkbook createExcel(List<User> users) {
		// TODO Auto-generated method stub

		HSSFWorkbook hssf = new HSSFWorkbook();
		// 创建第一个sheet（页），并命名 Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
		HSSFSheet sheet = hssf.createSheet("sheet1");
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < excelString.columnNames.length; i++) {

			sheet.setColumnWidth((short) i, (short) (35.7 * 200));
		}
		// 创建表第一行 表头
		HSSFRow row1 = sheet.createRow(0);
		createExcelHeader(8, row1);
		// 添加内容
		if (null != users && users.size() > 0) {

			createExcelBody(8, users, sheet);
		}
		return hssf;
	}

	// 创建excel头信息
	private void createExcelHeader(int cellRum, HSSFRow row) {
		for (int i = 0; i < cellRum; i++) {
			switch (i) {
			case 0:
				row.createCell(i).setCellValue(excelString.NUMBER);
				break;
			case 1:
				row.createCell(i).setCellValue(excelString.NAME);
				break;
			case 2:
				row.createCell(i).setCellValue(excelString.PASSWORD);
				break;
			case 3:
				row.createCell(i).setCellValue(excelString.EMAIL);
				break;
			case 4:
				row.createCell(i).setCellValue(excelString.UUID);
				break;
			case 5:
				row.createCell(i).setCellValue(excelString.ENDTIME);
				break;
			case 6:
				row.createCell(i).setCellValue(excelString.STATUS);
				break;
			case 7:
				row.createCell(i).setCellValue(excelString.CREATETIME);
				break;
			case 8:
				row.createCell(i).setCellValue(excelString.UPDATETIME);
				break;
			}
		}
	}

	// 创建excel信息
	private void createExcelBody(int cellRum, List<User> users, HSSFSheet sheet) {

		for (int i = 0; i < users.size(); i++) {
			Row row = sheet.createRow(i + 1);
			User user = users.get(i);
			for (int y = 0; y < cellRum; y++) {
				switch (y) {
				case 0:
					row.createCell(y).setCellValue(i + 1);
					break;
				case 1:
					row.createCell(y).setCellValue(user.getName());
					break;
				case 2:
					row.createCell(y).setCellValue(user.getPassword());
					break;
				case 3:
					row.createCell(y).setCellValue(user.getEmail());
					break;
				case 4:
					row.createCell(y).setCellValue(user.getUuid());
					break;
				case 5:
					row.createCell(y).setCellValue(user.getEndTime());
					break;
				case 6:
					row.createCell(y).setCellValue(user.getStatus());
					break;
				case 7:
					row.createCell(y).setCellValue(user.getCreateTime());
					break;
				case 8:
					row.createCell(y).setCellValue(user.getUpdateTime());
					break;
				}
			}
		}
	}

	public List<String> allUserNames() {
		// TODO Auto-generated method stub
		return userRepository.allUserName();
	}

	public void test() {
		int d = 1 / 0;
	}
}
