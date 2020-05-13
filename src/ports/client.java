package ports;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class client {
	public static void main(String args[]) throws UnknownHostException, IOException
	{
		String napis = "test", temp;
		Scanner sc = new Scanner(System.in);
		Socket s = new Socket("127.0.0.1",1365);
		Scanner sc1= new Scanner (s.getInputStream());
		while(!napis.equals("BYE"))
		{
				temp=sc1.nextLine();
				System.out.println(temp);
				napis= sc.nextLine();
				PrintStream p= new PrintStream(s.getOutputStream());
				p.println(napis);
			
		
		
		}
		sc.close();
		s.close();
		sc1.close();
	}
}
