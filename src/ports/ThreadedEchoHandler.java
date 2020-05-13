package ports;

import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedEchoHandler implements Runnable {
	int licznik = 1;
	int punkty = 0;
	int nr_rozgrywka=0;
	static int wskaznik = 0;
	String nazwa_usera;
	protected Socket incoming;

	public ThreadedEchoHandler(Socket i) {
		incoming = i;
	}
	
	public void reset_potwierdzenia()
	{
		uzytkownik.u1_pot="nic";
		uzytkownik.u2_pot="nic";
		
	}

	public void werdykt(PrintWriter out) {
		out.println("Twoj wynik wynosi : " + punkty);
		if (nazwa_usera.equals(uzytkownik.user1)) {
			if (punkty > uzytkownik.u2_punk)
				out.println("WYGRALES!!!");
			else if (punkty == uzytkownik.u2_punk)
				out.println("REMIS!!!");
			else
				out.println("PRZEGRALES!!!");
			out.println("Wynik twojego przeciwnika: " + uzytkownik.user2 + " wynosi : " + uzytkownik.u2_punk);

		} else if (nazwa_usera.equals(uzytkownik.user2)) {
			if (punkty > uzytkownik.u1_punk)
				out.println("WYGRALES!!!");
			else if (punkty == uzytkownik.u1_punk)
				out.println("REMIS!!!");
			else
				out.println("PRZEGRALES!!!");
			out.println("Wynik twojego przeciwnika: " + uzytkownik.user1 + " wynosi : " + uzytkownik.u1_punk);
		}
	}
	
	public boolean game_continue(czasomierz t1)
	{
		boolean done=true;
		while(t1.slowo == "pusto")
		{
			uspij.spij();
		}
		if(t1.slowo.equals("nie"))
		{
		done = true;
		t1.done = true;
		}
		else if(t1.slowo.equals("tak"))
		{
			punkty=0;
			licznik=1;
			done = false;
		}
		if (nazwa_usera.equals(uzytkownik.user1)) {
			uzytkownik.u1_pot=t1.slowo;
		} else if (nazwa_usera.equals(uzytkownik.user2)) {
			uzytkownik.u2_pot=t1.slowo;
		}
		return done;
	}

	public void run() {
		try {
			InputStream inStream = incoming.getInputStream();
			OutputStream outStream = incoming.getOutputStream();
			try {
				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);
				String line;
				String dalej;
				int czas;
				String sprawdzenie;
				String pytanie = null;
				File file = new File("pytania/pyt.txt");
				czasomierz t1 = new czasomierz(incoming);
				out.println(
						"Pamietaj aby tylko raz udzielac opdowiedzi na pytanie uzywajac tylko MALYCH liter! Masz 10 s na kazde z nich.");
				out.println("Powodzenia!");
				out.println("Podaj swoja nazwe bohaterze!");
				if (wskaznik == 0) {
					wskaznik++;
					nazwa_usera = in.nextLine();
					uzytkownik.user1 = nazwa_usera;
				} else {
					nazwa_usera = in.nextLine();
					uzytkownik.user2 = nazwa_usera;
				}
				boolean done = false;
				t1.start();
				while (!done) {
					if (server.i == 2) {
						Scanner plik = new Scanner(file);
						czas = 0;
						int i, wylosowana;
						wylosowana = losowanie.wylosuj();
						pytanie = plik.nextLine();
						for (i = 0; i < wylosowana; i++) {
							pytanie = plik.nextLine();
						}
						if (licznik == 1) {

							while (uzytkownik.user1 == null || uzytkownik.user2 == null) {
								out.println("Drugi gracz wpisuje nazwe uzytkownika, badz cierpliwy!");
								uspij.spij();
							}
							if (nazwa_usera.equals(uzytkownik.user1)) {
								out.println("Polaczono z graczem " + uzytkownik.user2);
							} else if (nazwa_usera.equals(uzytkownik.user2)) {
								out.println("Polaczono z graczem " + uzytkownik.user1);
							}
							if(nr_rozgrywka>0)
							{
							if (nazwa_usera.equals(uzytkownik.user1)) {
								while(!uzytkownik.u2_pot.equals("tak"))
								{
									out.println("Drugi gracz nie potwierdzil jeszcze dalszej rozgrywki!");
									if(uzytkownik.u2_pot.equals("nie"))
									{
										done=true;
										break;
									}
									uspij.spij();
								}
							} else if (nazwa_usera.equals(uzytkownik.user2)) {
								while(!uzytkownik.u1_pot.equals("tak"))
								{
									out.println("Drugi gracz nie potwierdzil jeszcze dalszej rozgrywki!");
									if(uzytkownik.u1_pot.equals("nie"))
									{
										done=true;
										break;
									}
									uspij.spij();
								}
							}
							}
				
						}
						out.println("Pytanie " + licznik + " : " + pytanie);
						while (czas < 5) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							czas++;
						}
						if (t1.slowo == "pusto") {
							out.println("Koniec czasu!");
							licznik++;
							if (nazwa_usera.equals(uzytkownik.user1)) {
								uzytkownik.u1_licz = licznik;
							} else if (nazwa_usera.equals(uzytkownik.user2)) {
								uzytkownik.u2_licz = licznik;
							}
							if (licznik > 5) {
								while ((uzytkownik.u1_licz < 5 && uzytkownik.u2_licz < 5) && uzytkownik.u1_exit == false
										&& uzytkownik.u2_exit == false) {
									out.println("Oczekiwanie, az drugi gracz skonczy gre...");
									uspij.spij();
								}
								werdykt(out);
								reset_potwierdzenia();
								out.println("Czy chcesz grac dalej? tak/nie");
								done=game_continue(t1);
								nr_rozgrywka++;
							}
						} else {
							line = t1.slowo;
							licznik++;
							sprawdzenie = losowanie.sprawdz(line, wylosowana);
							if(!line.equals("BYE"))
							out.println("Twoja odpowiedz to: " + line + ". Jest to " + sprawdzenie);
							if (sprawdzenie.equals("poprawna odpowiedz!")) {
								punkty++;
								if (nazwa_usera.equals(uzytkownik.user1)) {
									uzytkownik.u1_punk = punkty;
									uzytkownik.u1_licz = licznik;
								} else if (nazwa_usera.equals(uzytkownik.user2)) {
									uzytkownik.u2_punk = punkty;
									uzytkownik.u2_licz = licznik;
								}

							}
							if (licznik > 5) {
								while ((uzytkownik.u1_licz < 5 && uzytkownik.u2_licz < 5) && uzytkownik.u1_exit == false
										&& uzytkownik.u2_exit == false) {
									uspij.spij();
								}
								t1.slowo = "pusto";
								werdykt(out);
								reset_potwierdzenia();
								out.println("Czy chcesz grac dalej? tak/nie");
								done=game_continue(t1);
								nr_rozgrywka++;
								
								
							}
							if (line.trim().equals("BYE")) {
								if (nazwa_usera.equals(uzytkownik.user1)) {
									uzytkownik.u1_exit = true;
								} else if (nazwa_usera.equals(uzytkownik.user2)) {
									uzytkownik.u2_exit = true;
								}
								out.println("Twoj wynik wynosi : " + punkty);
								out.println("Pamietej ze nalezy zawsze konczyc swoje rozgrywki!");
								done = true;
								t1.done = true;
							}
							t1.slowo = "pusto";
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

}

class czasomierz extends Thread {

	String slowo = "pusto";
	Socket incoming;
	boolean done = false;

	czasomierz(Socket incoming) {
		this.incoming = incoming;
	}

	public void wyraz() {
		while (!done) {
			try {
				InputStream inStream = incoming.getInputStream();
				Scanner in = new Scanner(inStream);
				slowo = in.nextLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		wyraz();
	}

}

class uspij {
	public static void spij() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
