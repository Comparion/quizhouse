package ports;

import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedEchoHandler implements Runnable {
	int licznik=1;
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
				PrintWriter out = new PrintWriter(outStream, true);
				out.println("Hello!");
				boolean done = false;
				while (!done) {
					if (server.i == 2) {
						out.println("Pytanie "+licznik+" : "+ " "+ losowanie.wylosuj());
						String line = in.nextLine();
						out.println("Twoja odpowiedz to: " + line+ ". Jest to "+losowanie.sprawdz(line));
						licznik++;
						if (line.trim().equals("BYE"))
							done = true;
					} else {
						out.println("Oczekiwanie na drugiego gracza....");
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

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
