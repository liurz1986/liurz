package cn.com.usercenter.mainpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * �����¼����
 * @author Administrator
 *
 */
@Controller
public class Main {
    
	/**
	 * �����¼���淽ʽһ
	 * @return
	 */
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String login(){
		
		return "login";
	}
	
	/**
	 * �����¼���淽ʽ��
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String logins(){
		
		return "login";
	}
}
