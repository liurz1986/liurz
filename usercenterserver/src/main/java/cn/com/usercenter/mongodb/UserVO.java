package cn.com.usercenter.mongodb;

public class UserVO {
   private int id;
   private String name;
   private String password;
   private String email;
   private long telephone;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public long getTelephone() {
	return telephone;
}
public void setTelephone(long telephone) {
	this.telephone = telephone;
}
   
}
