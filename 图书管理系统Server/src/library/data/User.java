package library.data;
import java.io.Serializable;

public class User implements Serializable{
	private String UserName;		//用户名
	private String PassWord;		//密码
	private String UserType;		//用户类型,true为管理员,false为读者
	//序列化ID
	private static final long serialVersionUID = 7216851521247515883L;
	
	public User() {
		UserName = "";
		PassWord = "";
		UserType = "";
	}
	
	public User(String username, String password,String usertype) {
		UserName = username;
		PassWord = password;
		setUserType(usertype);
	}
	
	public void setUserName(String name) {
		UserName = name;
	}
	
	public String getUserName() {
		return UserName;
	}
	
	public void setPassword(String password) {
		PassWord = password;
	}
	
	public String getPassword() {
		return PassWord;
	}

	public String getUserType() {
		return UserType;
	}

	public void setUserType(String userType) {
		UserType = userType;
	}
}
