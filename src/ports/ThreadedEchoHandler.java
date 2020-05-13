package ports;

import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedEchoHandler implements Runnable {
	int licznik=1;
	protected Socket incoming;

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
				String line;
				int czas;
				czasomierz t1= new czasomierz(incoming);
				t1.start();
				out.println("Polaczyles sie z graczem! Pamietaj aby tylko raz udzielac opdowiedzi na pytanie! Masz 10 s na kazde z nich. Powodzenia!");
				boolean done = false;
				while (!done) {
					if (server.i == 2) {
						czas=0;
						out.println("Pytanie "+licznik+" : "+ " "+ losowanie.wylosuj());
						while(czas<10)
						{
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							czas++;
						}
						if(t1.slowo=="pusto")
						{
							out.println("Koniec czasu!");
							licznik++;
						}
						else
						{
						line=t1.slowo;
						licznik++;
						out.println("Twoja odpowiedz to: " + line+ ". Jest to "+losowanie.sprawdz(line));
						if (line.trim().equals("BYE"))
						{
							done = true;
							t1.done=true;
						}	
						t1.slowo="pusto";
						}
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
	
class czasomierz extends Thread {
    
	String slowo="pusto";
	Socket incoming;
	boolean done = false;
	
	czasomierz(Socket incoming)
	{
		this.incoming=incoming;
	}
	
	public void wyraz()
	{
		while(!done)
		{
		try {
			InputStream inStream = incoming.getInputStream();
			Scanner in = new Scanner(inStream);
			slowo = in.nextLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}
	
	public void run()
	{
		wyraz();
	}
	
    }
}
