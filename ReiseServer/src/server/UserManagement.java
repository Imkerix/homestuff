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
		if(userfile.toString().isEmpty()){
			users = new Hashtable<String,String>();
		}else{
			users = (Hashtable<String, String>) deserialize();			
		}
	}

    private Object deserialize() {
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
	
    
    private void serialize() {
		try {
			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(userfile));
			o.writeObject(users);
			o.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
