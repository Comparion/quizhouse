package ports;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
	
	static long ziarno=100 ;
	static int i=0;
	public static void main(String[] args) throws IOException
	{
		ServerSocket s1 = new ServerSocket(1364);
		try
		{
			
			while(i<2)
			{
				
				Socket ss=s1.accept();
				Runnable r= new ThreadedEchoHandler(ss);
				Thread t = new Thread(r);
				t.start();
				i++;
			}
			
			
		}
		catch(IOException e)
		{ 
			e.printStackTrace();
		} 
		finally
		{
			s1.close();
		}
		
	}

}
