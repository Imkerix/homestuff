
import java.io.IOException;
import server.ReiseServer;
import client.ReiseClient;

public class Main {

	public static void main(String[] args) throws IOException , InterruptedException {

//// Server Part //////// Server Part //////// Server Part //////// Server Part ////		
																	 
		//Server Thread nur noetig wenn ein PC server und Client in einer Anwendung zu verfuegung stellt.
		Thread serverThread = new Thread() {
			public void run() {     
				try {
					ReiseServer MyReiseServer = new ReiseServer(12343);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		serverThread.start();
		
//// Server Part //////// Server Part //////// Server Part //////// Server Part ////
		Thread.sleep(500);
//// Client Part //////// Client Part //////// Client Part //////// Client Part ////		
		
		Thread clientThread0 =  new Thread() { //A Client Thread
			public void run() { 
				ReiseClient MyReiseClient = new ReiseClient();
				MyReiseClient.verbinden("127.0.0.1", 12343);
				String[] array = new String[2];
				array[0]= "Bienen Königin";
				array[1]= "Drohn";
				
				try {
					MyReiseClient.buchen("Berlin", array );
					MyReiseClient.abmelden();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		};
		clientThread0.start();
		
		Thread clientThread1 =  new Thread() { //A Client Thread
			public void run() { 
				ReiseClient MyReiseClient = new ReiseClient();
				MyReiseClient.verbinden("127.0.0.1", 12343);
				String[] array = new String[2];
				array[0]= "Mutter Made";
				array[1]= "Madenkind";
				
				try {
					MyReiseClient.buchen("dönerbude", array );
					MyReiseClient.abmelden();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		};
		clientThread1.start();
		
		Thread clientThread2 =  new Thread() { //A Client Thread
			public void run() { 
				ReiseClient MyReiseClient = new ReiseClient();
				MyReiseClient.verbinden("127.0.0.1", 12343);
				String[] array = new String[2];
				array[0]= "Ein Druide";
				array[1]= "Sein Rabe";
				
				try {
					MyReiseClient.buchen("moskau", array );
					MyReiseClient.abmelden();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		};
		clientThread2.start();
			
		Thread clientThread3 =  new Thread() { //A Client Thread
			public void run(){
				ReiseClient MyReiseClient = new ReiseClient();
				MyReiseClient.verbinden("127.0.0.1", 12343);
				String[] array = new String[3];
				array[0]= "Herr Müller Lübenscheid";
				array[1]= "Frau Dr. Klöbner";
				array[2]= "Ihr Kind";
				
				try {
					MyReiseClient.buchen("paris", array );
					
//					for(String s : MyReiseClient.getReiseziele()){
//						System.out.println(s);
//					}
//					System.out.println(MyReiseClient.getFreiePlaetze("moskau"));
					MyReiseClient.abmelden();
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
			
		};	
		clientThread3.start();
				
	//// Client Part //////// Client Part //////// Client Part //////// Client Part ////		

	}
	
}
