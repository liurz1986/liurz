package cn.com.usercenter.service;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import cn.com.usercenter.entity.User;

public interface UserService {
    /**
     * �����½����޸��û���Ϣ
     * @param user
     * @throws java.text.ParseException 
     */
	public void saveOrUpdate(User user) throws java.text.ParseException;
	/***
	 * ����uuidɾ���û���Ϣ
	 * @param uuid
	 */
	public void deleteUser(String uuid);
	/**
	 * ��ȡ�����û���Ϣ����ҳ��ʾ
	 * @param row
	 * @param page
	 * @return
	 */
	public  List<User> findAll(int row ,int page);
	/**
	 * �����û�����������֤
	 * @param user
	 * @return
	 */
	public String check(String name,String password);
	/**
	 * ��ȡ�������ڵ��û���Ϣ,Ĭ����30��.Ϊ��ʱ���ʼ�
	 * @param days
	 * @return
	 */
	public List<User> findUsersByEndTime(int days);
	/**
	 * ͨ���ϴ�excel�ļ��������û���Ϣ
	 * @param file
	 */
	public boolean uploadExcel(MultipartFile file);
	/**
	 * �����û���Ϣ
	 * @param response
	 */
	public boolean downExcel(HttpServletResponse response);
	
	/**
	 * ��ȡ�����û���
	 * @return
	 */
	public List<String> allUserNames();
}
