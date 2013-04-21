package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

public class UserManagement {
	
	private Hashtable<String,String> users; 
	private File userfile;
	
	
	UserManagement(String p_userfile){
		
		userfile = new File(p_userfile);	
		
		if(userfile.length() == 0){
			users = new Hashtable<String,String>();
			users.put("gast", "");
		}else{
			users = (Hashtable<String, String>) deserialize();			
		}
	}
	
	public boolean adduser(String p_username, String p_passwd){
		if (p_username != null && p_passwd != null) {  
			users.put(p_username, p_passwd);	
			return true;					
		}
		else{
			System.err.println("add methode wurde verbotenerweise mit null als parameter genervt!");
			return false;
		}
		
	}
	
	public boolean deluser(String p_username){
		if(users.containsKey(p_username)){
			users.remove(p_username);
			return true;
		}else{
			return false;			
		}
		
	}
	public boolean checkauthentification(String p_claimedusername, String p_claimedpasswd){
		if(users.containsKey(p_claimedusername) &&  users.get(p_claimedusername).equals(p_claimedpasswd)){
			return true;
		}else{
			return false;			
		}
		
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
