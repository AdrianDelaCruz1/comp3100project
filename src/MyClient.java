import java.io.*;
import java.net.*;  

//./ds-server -c ./ds-sample-config01.xml -v brief -n

public class MyClient {  
	public static void main(String[] args) {  
	    	try{      
	    	int count = 0;
	    	String cmd;
		Socket s=new Socket("127.0.0.1",50000);  
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());
			
			dout.write(("HELO\n").getBytes()); //Sending HELO to server
			String str = (String)in.readLine();
			System.out.println(str);
			dout.flush();  
			
			dout.write(("AUTH Adrian\n").getBytes()); //Authentication
			str = (String)in.readLine();
			System.out.println(str);
		
			dout.write(("REDY\n").getBytes());
			str = (String)in.readLine();
			System.out.println(str);
			
			
			cmd = (String)in.readLine();
			String[] arrOfCmd = cmd.split(" ");
			dout.write(("GETS Capable " + arrOfCmd[4] + " " + arrOfCmd[5] + " " + arrOfCmd[6] +" \n").getBytes());
			str = (String)in.readLine();
			System.out.println(str);
			dout.flush();
			
			
			dout.write(("OK\n").getBytes());
			str = (String)in.readLine();
			System.out.println(str);
			
			dout.write(("OK\n").getBytes());
			str = (String)in.readLine();
			System.out.println(str);
			
			dout.write(("QUIT\n").getBytes());
			dout.close(); 
			dout.flush(); 
			s.close();  
				
		}catch(Exception e){
		System.out.println(e);}  
	}  
} 