package cn.com.usercenter.util;
/**
 * Token���û���Ψһ��ʶ
 * @author Administrator
 *
 */
public class Token {

	private String tokenId;//����token��Ψһ��ʶ
	private String createTime;//����token��ʱ��
	private String userName;//�û���
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
