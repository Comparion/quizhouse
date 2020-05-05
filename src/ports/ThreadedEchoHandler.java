package ports;

import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedEchoHandler implements Runnable {

	private Socket incoming;

	public ThreadedEchoHandler(Socket i) {
		incoming = i;
	}

	public void run() {
		try {
			InputStream inStream = incoming.getInputStream();
			OutputStream outStream = incoming.getOutputStream();
			try {
				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
				out.println("Hello!");
				boolean done = false;
				while (!done && in.hasNextLine()) {
					String line = in.nextLine();
					out.println("Twoja odpowiedz to: " + line + losowanie.wylosuj() );
					if (line.trim().equals("BYE"))
						done = true;
				}
				in.close();
			} finally {
				inStream.close();
				outStream.close();
				incoming.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/*
 * public void run() { Scanner sc=new Scanner(incoming.getInputStream());
 * number= sc.nextInt();
 * 
 * temp=number*2;
 * 
 * PrintStream p=new PrintStream(incoming.getOutputStream()); p.println(temp); }
 */
