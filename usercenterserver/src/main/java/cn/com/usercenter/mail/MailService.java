package cn.com.usercenter.mail;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.com.usercenter.entity.User;
import cn.com.usercenter.service.UserService;

/**
 * 每天查询一次所有用户的注册是不是有效期是不是到了，到了发邮件告诉用户注册有效期快到了
 * 
 * @author Administrator
 *
 */
@Component
public class MailService implements Runnable {
	private Logger log = LoggerFactory.getLogger(MailService.class);
	private final int days = 30;
	private final String mailBody = "您的账号快到期，请及时申请用户权限";
	private final String mailSubject = "权限通知";
	@Autowired
	private UserService userService;

	public void run() {
		log.info("-----start QuartzStack----");
		try {
			List<User> users = userService.findUsersByEndTime(days);
			String[] to = getEmails(users);
			if (null != to && to.length > 0) {
				MailUtil mail = new MailUtil(to,mailSubject,mailBody);
			
				mail.sendSpring();
				log.info(" ---email  send success:--");
			} else {
				log.info("----目前没有存在用户账号快到期的用户----");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("---email  send failure :--" + e.getMessage());
		}
		log.info("-----end QuartzStack----");
	}

	private String[] getEmails(List<User> users) {
		if (null != users && users.size() > 0) {
			String[] email = new String[users.size()];
			for (int i = 0; i < users.size(); i++) {
				if (!StringUtils.isEmpty(users.get(i)) && !StringUtils.isEmpty(users.get(i).getEmail())) {
					email[i] = users.get(i).getEmail();
				}
			}
			return email;
		}
		return null;
	}
}
