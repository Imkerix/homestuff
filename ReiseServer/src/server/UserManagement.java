package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList; 
import java.util.List;

public class UserManagement {
	
	private List<User> users; 
	private File userfile;
	
	
	UserManagement(String p_userfile){
		
		userfile = new File(p_userfile);	
		
		if(userfile.length() == 0){
			users = new ArrayList<User>();
			users.add(new User("gast", ""));
		}else{
			users = (List<User>) deserialize();			
		}
	}
	
	public boolean adduser(String p_username, String p_passwd){
		boolean isThere = false; 
		
			for(User i : users){
				if(i.username.equals(p_username)){
					isThere = true;						
				}
			}
			if(isThere == false){
				users.add(new User(p_username, p_passwd));	
				return true;
			}else{
				System.err.println("add methode wurde verbotenerweise mit null oder einem Exitierenden account als parameter genervt!");
				System.err.println("Das ist >> BÖSE <<");
				return false;
			}	
	}
	
	public boolean deluser(String p_username){
		for(User u : users){
			if(u.username.equals(p_username)){
				users.remove(u);
				return true;
			}			
		}
		return false;
		
	}
	public boolean checkauthentification(String p_claimedusername, String p_claimedpasswd){
		for(User i : users){
			if(i.username.equals(p_claimedusername) &&  i.passwd.equals(p_claimedpasswd)){
				return true;
			}
		}
		return false;
	}
	
    public Object deserialize() {
    	Object Kommt = null;
		try {
			ObjectInputStream o = new ObjectInputStream(new FileInputStream(userfile));
			Kommt = o.readObject();	
			o.close();
			
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		return Kommt;
		
	}
	
    
    public void serialize() {
		try {
			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(userfile));
			o.writeObject(users);
			o.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
