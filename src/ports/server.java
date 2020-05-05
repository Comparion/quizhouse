package ports;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

	public static void main(String[] args) throws IOException
	{
		ServerSocket s1 = new ServerSocket(1360);
		try
		{
			
			while(true)
			{
				Socket ss=s1.accept();
				Runnable r= new ThreadedEchoHandler(ss);
				Thread t = new Thread(r);
				t.start();
			
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
