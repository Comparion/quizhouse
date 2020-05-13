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
				int punkty=0;
				String sprawdzenie;
				String pytanie = null;
				File file = new File("pytania/pyt.txt");
				czasomierz t1= new czasomierz(incoming);
				t1.start();
				out.println("Pamietaj aby tylko raz udzielac opdowiedzi na pytanie! Masz 10 s na kazde z nich. Powodzenia!");
				boolean done = false;
				while (!done) {
					if (server.i == 2) {
						Scanner plik = new Scanner(file);
						czas=0;
						int i,wylosowana;
						wylosowana=losowanie.wylosuj();
						pytanie=plik.nextLine();
						for(i=0;i<wylosowana;i++)
						{
							pytanie=plik.nextLine();
						}
						if(licznik==1)
							out.println("Polaczono z innym graczem");
						out.println("Pytanie "+licznik+" : "  + pytanie);
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
							if (licznik>5)
							{
								out.println("Twoj wynik wynosi : "+punkty);
								done = true;
								t1.done=true;
							}	
						}
						else
						{
						line=t1.slowo;
						licznik++;
						sprawdzenie=losowanie.sprawdz(line, wylosowana);
						out.println("Twoja odpowiedz to: " + line+ ". Jest to "+sprawdzenie);
						if(sprawdzenie.equals("poprawna odpowiedz!"))
						{
							punkty++;
						}	
						if (line.trim().equals("BYE")||licznik>5)
						{
							out.println("Twoj wynik wynosi : "+punkty);
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
