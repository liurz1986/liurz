package cn.com.usercenter.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.com.usercenter.entity.User;
import cn.com.usercenter.service.UserService;
import cn.com.usercenter.util.ResultDateUtil;
import cn.com.usercenter.zookeeper.ZooKeeperService;

/**
 * 用户管理 1.用户信息录入 2.用户信息的修改 3.用户信息的删除 4.获取用户所有信息 5.用户验证
 */
@Controller
@RequestMapping(value = "/user", produces = { "application/json;charset=UTF-8" })
public class UserController {
	private Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userServiceImpl;
	
	@Autowired
	private ZooKeeperService zooKeeperService;

	/**
	 * 用户验证：根据用户名和密码
	 * 
	 * @param jsonparams
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/check", produces = { "application/json" }, method = RequestMethod.POST)
	public void check(HttpServletRequest request,HttpServletResponse response) {
		try {
			String name=request.getParameter("name");
			String password=request.getParameter("password");
			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
				log.error("用户名和密码不能为空");
				response.sendRedirect("WEB-INF/log.jsp");
			}
			String token = userServiceImpl.check(name,password);
			if (null != token) {
				response.setHeader("token", token);
				response.getWriter().write(ResultDateUtil.successResult("user check success"));
			} else {
				response.sendRedirect("WEB-INF/log.jsp");
			}

		} catch (Exception e) {
			try {
				response.sendRedirect("log.jsp");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("Query database error:" + e.getMessage());
		}
	}
    @ResponseBody
	@RequestMapping(value = "/save", produces = { "application/json" }, method = RequestMethod.POST)
	public String save(@RequestBody User user) {

		try {
			user.setUuid(UUID.randomUUID().toString());
			userServiceImpl.saveOrUpdate(user);
			log.info("user create sucess");
			return ResultDateUtil.successResult("user create sucess");
		} catch (Exception e) {
			log.info("user create failure" + e.getMessage());
			return ResultDateUtil.errorResult("user create failure:" + e.getMessage());
		}
	}
    @ResponseBody
	@RequestMapping(value = "/update", produces = { "application/json" }, method = RequestMethod.POST)
	public String update(@RequestBody User user) {

		try {
			userServiceImpl.saveOrUpdate(user);
			log.info("user update sucess");
			return ResultDateUtil.successResult("user update sucess");
		} catch (Exception e) {
			log.info("user update failure:" + e.getMessage());
			return ResultDateUtil.errorResult("user update failure:" + e.getMessage());
		}
	}
    @ResponseBody
	@RequestMapping(value = "/update/{uuid}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("uuid") String uuid) {

		try {
			userServiceImpl.deleteUser(uuid);
			log.info("user delete sucess:uuid is" + uuid);
			return ResultDateUtil.successResult("user delete sucess:uuid is" + uuid);
		} catch (Exception e) {
			log.info("user delete failure:" + e.getMessage());
			return ResultDateUtil.errorResult("user delete failure:" + e.getMessage());
		}
	}
    @ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String findAll(@RequestParam("page") int page, @RequestParam("row") int row) {
		try {
			List<User> users=userServiceImpl.findAll(row, page);
			log.info(ResultDateUtil.successResult(users));
			return ResultDateUtil.successResult(users);
		} catch (Exception e) {
			log.info("user all failure" + e.getMessage());
			return ResultDateUtil.errorResult("user all failure:" + e.getMessage());
		}
	}
    /**
     * 对外提供验证用户是否存在的接口
     * 通过zookeeper中的存放的name的值，判断name是否存在
     * @param name
     * @return
     */
    @ResponseBody
   	@RequestMapping(value = "/name", method = RequestMethod.GET)
   	public String  nameUnique(@RequestParam("name") String name) {
   		try {
   			boolean result=zooKeeperService.isUserNameExist(name);
   			if(result){
   				log.info(ResultDateUtil.errorResult("name is exist"));
   				return ResultDateUtil.errorResult("name is exist");
   			}else{
   				log.info(ResultDateUtil.successResult("name no exist"));
   				return ResultDateUtil.successResult("name no exist");
   			}
   		} catch (Exception e) {
   			log.info("name validate error" + e.getMessage());
   			return ResultDateUtil.errorResult("name validate error:" + e.getMessage());
   		}
   	}
    
    /**
     * 获取当前所有服务ip
     * @return
     */
	public List<String> currentAllServer(){
		
		return zooKeeperService.getServerNodes();
	}

	/**
	 * 上传文件解析后保存到数据库中
	 * 
	 * @param file
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value = "/uplod/excel", method = RequestMethod.POST)
	public String uplod(MultipartFile file) {
		boolean res = userServiceImpl.uploadExcel(file);
		if (res) {
			return ResultDateUtil.successResult("uploda  sucess");
		} else {
			return ResultDateUtil.errorResult("uploda  failure");
		}
	}

	/**
	 * 将用户信息，生成excel下载到本地
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/down/t_user", method = RequestMethod.GET)
	public void down(HttpServletResponse response) {
		
		userServiceImpl.downExcel(response);
		
	}
}
