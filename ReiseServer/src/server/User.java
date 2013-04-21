package server;

import java.io.Serializable;

public class User implements Serializable{
	
	String username;
	String passwd;
	String email;
	
	public User(String p_username, String p_passwd){
		username = p_username;
		passwd = p_passwd;	
	}
	public User(String p_username, String p_passwd, String p_email){
		username = p_username;
		passwd = p_passwd;	
		email = p_email;
	}
	
}
