package cn.com.usercenter.mail;

public class MailUtil extends Mail {
	
	public MailUtil( String[] to, String mailSubject, String mailBody) {

		setMail(DefaultHostname, DefaultProptocol, DefaultPort, DefaultUserName, DefaultPassword, DefaultFrom, to, null, mailSubject, mailBody, null,
				DefaultNeedAuth,DefaultTimeOut);
	}
	public MailUtil( String[] to, String[] cc, String mailSubject, String mailBody) {

		setMail(DefaultHostname, DefaultProptocol, DefaultPort, DefaultUserName, DefaultPassword, DefaultFrom, to, cc, mailSubject, mailBody, null,
				DefaultNeedAuth,DefaultTimeOut);
	}
	public MailUtil( String[] to,String mailSubject, String mailBody, String fileAffixPath) {

		setMail(DefaultHostname, DefaultProptocol, DefaultPort, DefaultUserName, DefaultPassword, DefaultFrom, to,null, mailSubject, mailBody, fileAffixPath,
				DefaultNeedAuth,DefaultTimeOut);
	}
	public MailUtil( String[] to, String[] cc, String mailSubject, String mailBody, String fileAffixPath) {

		setMail(DefaultHostname, DefaultProptocol, DefaultPort, DefaultUserName, DefaultPassword, DefaultFrom, to, cc, mailSubject, mailBody, fileAffixPath,
				DefaultNeedAuth,DefaultTimeOut);
	}
	public MailUtil( String from,String[] to, String[] cc, String mailSubject, String mailBody, String fileAffixPath) {

		setMail(DefaultHostname, DefaultProptocol, DefaultPort, DefaultUserName, DefaultPassword, from, to, cc, mailSubject, mailBody, fileAffixPath,
				DefaultNeedAuth,DefaultTimeOut);
	}
	public MailUtil(String hostname, String proptocol, String port, String userName, String password, String from,
			String[] to, String[] cc, String mailSubject, String mailBody, String fileAffixPath) {

		setMail(hostname, proptocol, port, userName, password, from, to, cc, mailSubject, mailBody, fileAffixPath,
				true,DefaultTimeOut);
	}

	public MailUtil(String hostname, String proptocol, String port, String userName, String password, String from,
			String[] to, String[] cc, String mailSubject, String mailBody, String fileAffixPath, boolean needAuth,
			String timeOut) {
		setMail(hostname, proptocol, port, userName, password, from, to, cc, mailSubject, mailBody, fileAffixPath,
				true,timeOut);
	}
	
	
	
	public static void main(String[] args) {
		String[] to={ "hgdxglrz1986@126.com" };
		String mailSubject="邮件标题";
		String mailBody="邮件内容";
		String filepath="D:\\content.doc";
		MailUtil mailUtil=new MailUtil(to,mailSubject,mailBody,filepath);
		mailUtil.sendCommon();
	}
}

