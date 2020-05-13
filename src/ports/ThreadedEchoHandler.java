package ports;

import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedEchoHandler implements Runnable {
	int licznik=1;
	static int wskaznik=0;
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
				String nazwa_usera;
				int czas;
				int punkty=0;
				String sprawdzenie;
				String pytanie = null;
				File file = new File("pytania/pyt.txt");
				czasomierz t1= new czasomierz(incoming);
				out.println("Pamietaj aby tylko raz udzielac opdowiedzi na pytanie uzywajac tylko MALYCH liter! Masz 10 s na kazde z nich.");
				out.println("Powodzenia!");
				out.println("Podaj swoja nazwe bohaterze!");
				if(wskaznik==0)
				{
					wskaznik++;
					nazwa_usera = in.nextLine();
					uzytkownik.user1 = nazwa_usera;
				}
				else
				{
					nazwa_usera = in.nextLine();
					uzytkownik.user2 = nazwa_usera;
				}
				boolean done = false;
				t1.start();
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
						{
							
							while(uzytkownik.user1==null||uzytkownik.user2==null)
							{
								out.println("Drugi gracz wpisuje nazwe uzytkownika, badz cierpliwy!");
								uspij.spij();
							}
							if(nazwa_usera.equals(uzytkownik.user1))
							{
								out.println("Polaczono z graczem " + uzytkownik.user2 );
							}
							else
							if(nazwa_usera.equals(uzytkownik.user2))
							{
								out.println("Polaczono z graczem " + uzytkownik.user1 );
							}
							
						} 
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
							if(nazwa_usera.equals(uzytkownik.user1))
							{
								uzytkownik.u1_licz=licznik;
							}
							else
							if(nazwa_usera.equals(uzytkownik.user2))
							{
								uzytkownik.u2_licz=licznik;
							}
							if (licznik>5)
							{
								out.println("Twoj wynik wynosi : "+punkty);
								while((uzytkownik.u1_licz<5&&uzytkownik.u2_licz<5)&&uzytkownik.u1_exit==false&&uzytkownik.u2_exit==false)
								{
									out.println("Oczekiwanie, az drugi gracz skonczy gre...");
									uspij.spij();
								}
								if(nazwa_usera.equals(uzytkownik.user1))
								{
									if(punkty>uzytkownik.u2_punk)
										out.println("WYGRALES!!!");
									else 
										if(punkty==uzytkownik.u2_punk)
											out.println("REMIS!!!");
										else
											out.println("PRZEGRALES!!!");
									out.println("Wynik twojego przeciwnika: "+uzytkownik.user2+" wynosi : "+uzytkownik.u2_punk);
										
								}
								else
								if(nazwa_usera.equals(uzytkownik.user2))
								{
									if(punkty>uzytkownik.u1_punk)
										out.println("WYGRALES!!!");
									else 
										if(punkty==uzytkownik.u1_punk)
											out.println("REMIS!!!");
										else
											out.println("PRZEGRALES!!!");
									out.println("Wynik twojego przeciwnika: "+uzytkownik.user1+" wynosi : "+uzytkownik.u1_punk);
								}
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
							if(nazwa_usera.equals(uzytkownik.user1))
							{
								uzytkownik.u1_punk=punkty;
								uzytkownik.u1_licz=licznik;
							}
							else
							if(nazwa_usera.equals(uzytkownik.user2))
							{
								uzytkownik.u2_punk=punkty;
								uzytkownik.u2_licz=licznik;
							}
							
						}	
						if (line.trim().equals("BYE")||licznik>5)
						{
							if(nazwa_usera.equals(uzytkownik.user1))
							{
								 uzytkownik.u1_exit = true;
							}
							else
							if(nazwa_usera.equals(uzytkownik.user2))
							{
								uzytkownik.u2_exit = true;
							}
							out.println("Twoj wynik wynosi : "+punkty);
							while((uzytkownik.u1_licz<5&&uzytkownik.u2_licz<5)&&uzytkownik.u1_exit==false&&uzytkownik.u2_exit==false)
							{
								uspij.spij();
							}
							if(nazwa_usera.equals(uzytkownik.user1))
							{
								if(punkty>uzytkownik.u2_punk)
									out.println("WYGRALES!!!");
								else 
									if(punkty==uzytkownik.u2_punk)
										out.println("REMIS!!!");
									else
										out.println("PRZEGRALES!!!");
								out.println("Wynik twojego przeciwnika: "+uzytkownik.user2+" wynosi : "+uzytkownik.u2_punk);
									
							}
							else
							if(nazwa_usera.equals(uzytkownik.user2))
							{
								if(punkty>uzytkownik.u1_punk)
									out.println("WYGRALES!!!");
								else 
									if(punkty==uzytkownik.u1_punk)
										out.println("REMIS!!!");
									else
										out.println("PRZEGRALES!!!");
								out.println("Wynik twojego przeciwnika: "+uzytkownik.user1+" wynosi : "+uzytkownik.u1_punk);
							}
							done = true;
							t1.done=true;
						}	
						t1.slowo="pusto";
						}
					} else {
						out.println("Oczekiwanie na drugiego gracza....");
						uspij.spij();
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


class uspij
{
	public static void spij()
	{
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
