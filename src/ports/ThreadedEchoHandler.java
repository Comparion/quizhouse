package ports;

import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedEchoHandler implements Runnable {
	int licznik = 1;
	int punkty = 0;
	int choose=0;
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
	
	public void czyszczenie_tablicy()
	{
		int i=0;
		while(losowanie.tablica[i]!=0)
		{
			losowanie.tablica[i]=0;
			i++;
		}
		losowanie.nr_tab=0;
		uzytkownik.potwierdzenie=false;
		uzytkownik.temat1="pusto";
		uzytkownik.temat2="pusto";
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
			uzytkownik.temat1="pusto";
		} else if (nazwa_usera.equals(uzytkownik.user2)) {
			if (punkty > uzytkownik.u1_punk)
				out.println("WYGRALES!!!");
			else if (punkty == uzytkownik.u1_punk)
				out.println("REMIS!!!");
			else
				out.println("PRZEGRALES!!!");
			out.println("Wynik twojego przeciwnika: " + uzytkownik.user1 + " wynosi : " + uzytkownik.u1_punk);
			uzytkownik.temat2="pusto";
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
	
	public int wybor_kategorii(czasomierz t1)
	{
		int tem=1;
		while(t1.slowo == "pusto")
		{
			uspij.spij();
		}
		if(t1.slowo.equals("2"))
		{
		tem=2;
		}
		else if(t1.slowo.equals("3"))
		{
		tem =3;
		}
		if (nazwa_usera.equals(uzytkownik.user1)) {
			uzytkownik.temat1=t1.slowo;
		} else if (nazwa_usera.equals(uzytkownik.user2)) {
			uzytkownik.temat2=t1.slowo;
		}
		return tem;
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
						
						pytanie = plik.nextLine();
						
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
							t1.slowo="pusto";
							if (nazwa_usera.equals(uzytkownik.user1)&&nr_rozgrywka%2==0)
							{
								
								out.println("Wybierz kategorie pytan!");
								out.println("1.Koty 2.Psy 3.Konie / Wybierz liczbe");
								choose=wybor_kategorii(t1);
								uzytkownik.wybor=choose;
								out.println("Wybrales: "+choose);
								uzytkownik.potwierdzenie=true;
								t1.slowo="pusto";
							}
							if (nazwa_usera.equals(uzytkownik.user2)&&nr_rozgrywka%2==1)
							{
								
								out.println("Wybierz kategorie pytan!");
								out.println("1.Koty 2.Psy 3.Konie / Wybierz liczbe");
								choose=wybor_kategorii(t1);
								uzytkownik.wybor=choose;
								out.println("Wybrales: "+choose);
								uzytkownik.potwierdzenie=true;
								t1.slowo="pusto";
							}
								int timer=0;
								while(!uzytkownik.potwierdzenie)
								{
									if(timer>15)
									{
										out.println("Koniec rozgrywki!");
										done=true;
										break;
									}
									out.println("Drugi gracz wybiera kategorie.");
									if(uzytkownik.u2_pot.equals("nie")||uzytkownik.u1_pot.equals("nie"))
									{
										done=true;
										break;
									}
									uspij.spij();
									timer++;
								}
						}
						wylosowana = losowanie.wylosuj();
						for (i = 0; i < wylosowana; i++) {
							pytanie = plik.nextLine();
						}
						if(uzytkownik.u1_pot.equals("nie")||uzytkownik.u2_pot.equals("nie"))
							continue;
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
								czyszczenie_tablicy();
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
							}
							if (nazwa_usera.equals(uzytkownik.user1)) {
								uzytkownik.u1_punk = punkty;
								uzytkownik.u1_licz = licznik;
							} else if (nazwa_usera.equals(uzytkownik.user2)) {
								uzytkownik.u2_punk = punkty;
								uzytkownik.u2_licz = licznik;
							}
							if (licznik > 5) {
								while ((uzytkownik.u1_licz < 5 && uzytkownik.u2_licz < 5) && uzytkownik.u1_exit == false
										&& uzytkownik.u2_exit == false) {
									uspij.spij();
								}
								t1.slowo = "pusto";
								werdykt(out);
								reset_potwierdzenia();
								czyszczenie_tablicy();
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
