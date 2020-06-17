package serwer;

import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedEchoHandler implements Runnable {
	int licznik = 1;
	int punkty = 0;
	int choose = 0;
	int nr_rozgrywka = 0;
	static int wskaznik = 0;
	String nazwa_usera;
	protected Socket incoming;

	public ThreadedEchoHandler(Socket i) {
		incoming = i;
	}

	public void reset_potwierdzenia() {
		Uzytkownik.u1_pot = "nic";
		Uzytkownik.u2_pot = "nic";

	}

	public void czyszczenie_tablicy() {
		int i = 0;
		while (Losowanie.tablica[i] != 0) {
			Losowanie.tablica[i] = 0;
			i++;
		}
		Losowanie.nr_tab = 0;
		Uzytkownik.potwierdzenie = false;
		Uzytkownik.temat1 = "pusto";
		Uzytkownik.temat2 = "pusto";
		Uzytkownik.u1_wynik = false;
		Uzytkownik.u2_wynik = false;
	}

	public void werdykt(PrintWriter out) {
		out.println("Twoj wynik wynosi : " + punkty);
		if (nazwa_usera.equals(Uzytkownik.user1)) {
			if (punkty > Uzytkownik.u2_punk)
				out.println("WYGRALES!!!");
			else if (punkty == Uzytkownik.u2_punk)
				out.println("REMIS!!!");
			else
				out.println("PRZEGRALES!!!");
			out.println("Wynik twojego przeciwnika: " + Uzytkownik.user2 + " wynosi : " + Uzytkownik.u2_punk);
			Uzytkownik.temat1 = "pusto";
		} else if (nazwa_usera.equals(Uzytkownik.user2)) {
			if (punkty > Uzytkownik.u1_punk)
				out.println("WYGRALES!!!");
			else if (punkty == Uzytkownik.u1_punk)
				out.println("REMIS!!!");
			else
				out.println("PRZEGRALES!!!");
			out.println("Wynik twojego przeciwnika: " + Uzytkownik.user1 + " wynosi : " + Uzytkownik.u1_punk);
			Uzytkownik.temat2 = "pusto";
		}
	}

	public boolean game_continue(Czasomierz t1) {
		boolean done = true;
		while (t1.slowo == "pusto") {
			Uspij.spij();
		}
		if (t1.slowo.equals("nie")) {
			done = true;
			t1.done = true;
		} else if (t1.slowo.equals("tak")) {
			punkty = 0;
			licznik = 1;
			done = false;
		}
		if (nazwa_usera.equals(Uzytkownik.user1)) {
			Uzytkownik.u1_pot = t1.slowo;
		} else if (nazwa_usera.equals(Uzytkownik.user2)) {
			Uzytkownik.u2_pot = t1.slowo;
		}
		return done;
	}

	public int wybor_kategorii(Czasomierz t1) {
		int tem = 1;
		while (t1.slowo == "pusto") {
			Uspij.spij();
		}
		if (t1.slowo.equals("B")) {
			tem = 2;
		} else if (t1.slowo.equals("C")) {
			tem = 3;
		} else if (t1.slowo.equals("D")) {
			tem = 4;
		}
		if (nazwa_usera.equals(Uzytkownik.user1)) {
			Uzytkownik.temat1 = t1.slowo;
		} else if (nazwa_usera.equals(Uzytkownik.user2)) {
			Uzytkownik.temat2 = t1.slowo;
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
				int czas;
				String sprawdzenie;
				String pytanie = null;
				String odpowiedzi = null;
				File file = new File("pytania/pyt.txt");
				File file3 = new File("pytania/pyt2.txt");
				Czasomierz t1 = new Czasomierz(incoming);
				out.println("Masz 10 sekund na kazda odpowiedz.Jedna runda to 5 pytan. Powodzenia!");
				if (wskaznik == 0) {
					wskaznik++;
					nazwa_usera = in.nextLine();
					Uzytkownik.user1 = nazwa_usera;
				} else {
					nazwa_usera = in.nextLine();
					Uzytkownik.user2 = nazwa_usera;
				}
				boolean done = false;
				t1.start();
				glowna: while (!done && Uzytkownik.koniec == false) {
					if (Server.i == 2) {
						while (Uzytkownik.user1 == null || Uzytkownik.user2 == null) {
							out.println("Drugi gracz wpisuje nazwe uzytkownika, badz cierpliwy!");
							Uspij.spij();
						}
						if (nr_rozgrywka == 0 && licznik == 1) {
							if (nazwa_usera.equals(Uzytkownik.user1)) {
								out.println("Polaczono z graczem " + Uzytkownik.user2);
							} else if (nazwa_usera.equals(Uzytkownik.user2)) {
								out.println("Polaczono z graczem " + Uzytkownik.user1);
							}
						}
						Scanner plik = new Scanner(file);
						Scanner plik2 = new Scanner(file3);
						czas = 0;
						int timer = 0;
						int i, wylosowana;
						if (licznik == 1) {
							if (nr_rozgrywka > 0) {
								if (nazwa_usera.equals(Uzytkownik.user1)) {
									while (!Uzytkownik.u2_pot.equals("tak")) {
										out.println("Drugi gracz nie potwierdzil jeszcze dalszej rozgrywki!");
										if (Uzytkownik.u2_pot.equals("nie") || timer > 10) {
											out.println("Koniec rozgrywki!");
											done = true;
											continue glowna;
										}
										timer++;
										Uspij.spij();
									}
								} else if (nazwa_usera.equals(Uzytkownik.user2)) {
									while (!Uzytkownik.u1_pot.equals("tak")) {
										out.println("Drugi gracz nie potwierdzil jeszcze dalszej rozgrywki!");
										if (Uzytkownik.u1_pot.equals("nie") || timer > 10) {
											out.println("Koniec rozgrywki!");
											done = true;
											continue glowna;
										}
										timer++;
										Uspij.spij();
									}
								}
							}
							t1.slowo = "pusto";
							if (nazwa_usera.equals(Uzytkownik.user1) && nr_rozgrywka % 2 == 0) {

								out.println("Wybierz kategorie pytan!");
								out.println("A-1.Ogolna  B-2.Zwierzeta  C-3.Rosliny  D-4.Informatyka / Wybierz litere");
								choose = wybor_kategorii(t1);
								Uzytkownik.wybor = choose;
								out.println("Wybrales: " + choose);
								Uzytkownik.potwierdzenie = true;
								t1.slowo = "pusto";
							}
							if (nazwa_usera.equals(Uzytkownik.user2) && nr_rozgrywka % 2 == 1) {

								out.println("Wybierz kategorie pytan!");
								out.println("A-1.Ogolna  B-2.Zwierzeta  C-3.Rosliny  D-4.Informatyka / Wybierz litere");
								choose = wybor_kategorii(t1);
								Uzytkownik.wybor = choose;
								out.println("Wybrales: " + choose);
								Uzytkownik.potwierdzenie = true;
								t1.slowo = "pusto";
							}
							while (!Uzytkownik.potwierdzenie) {
								if (timer > 10) {
									out.println("Koniec rozgrywki!");
									done = true;
									continue glowna;
								}
								out.println("Drugi gracz wybiera kategorie.");
								if (Uzytkownik.u2_pot.equals("nie") || Uzytkownik.u1_pot.equals("nie")) {
									done = true;
									continue glowna;
								}
								Uspij.spij();
								timer++;
							}
						}
						wylosowana = Losowanie.wylosuj();
						for (i = 0; i < wylosowana; i++) {
							pytanie = plik.nextLine();
							odpowiedzi = plik2.nextLine();
						}
						out.println("Pytanie " + licznik + " : " + pytanie);
						out.println(odpowiedzi);
						while (czas < 10) {
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
							if (nazwa_usera.equals(Uzytkownik.user1)) {
								Uzytkownik.u1_licz = licznik;
							} else if (nazwa_usera.equals(Uzytkownik.user2)) {
								Uzytkownik.u2_licz = licznik;
							}
							if (licznik > 5) {
								if (nazwa_usera.equals(Uzytkownik.user1)) {
									Uzytkownik.u1_wynik = true;
								} else if (nazwa_usera.equals(Uzytkownik.user2)) {
									Uzytkownik.u2_wynik = true;
								}
								if ((Uzytkownik.u1_licz < 5 || Uzytkownik.u2_licz < 5) || Uzytkownik.u1_wynik == false
										|| Uzytkownik.u2_wynik == false) {
									out.println("Oczekiwanie, az drugi gracz skonczy gre...");
									Uspij.spij();
								}
								werdykt(out);
								reset_potwierdzenia();
								out.println("Czy chcesz grac dalej? tak/nie");
								
								czyszczenie_tablicy();
								done = game_continue(t1);
								nr_rozgrywka++;
							}
						} else {
							line = t1.slowo;
							licznik++;
							sprawdzenie = Losowanie.sprawdz(line, wylosowana);
							if (!line.equals("BYE"))
								out.println("Twoja odpowiedz to: " + line + ". Jest to " + sprawdzenie);
							if (sprawdzenie.equals("poprawna odpowiedz!")) {
								punkty++;
							}
							if (nazwa_usera.equals(Uzytkownik.user1)) {
								Uzytkownik.u1_punk = punkty;
								Uzytkownik.u1_licz = licznik;
							} else if (nazwa_usera.equals(Uzytkownik.user2)) {
								Uzytkownik.u2_punk = punkty;
								Uzytkownik.u2_licz = licznik;
							}
							if (licznik > 5) {
								if (nazwa_usera.equals(Uzytkownik.user1)) {
									Uzytkownik.u1_wynik = true;
								} else if (nazwa_usera.equals(Uzytkownik.user2)) {
									Uzytkownik.u2_wynik = true;
								}
								if ((Uzytkownik.u1_licz < 5 || Uzytkownik.u2_licz < 5) || Uzytkownik.u1_wynik == false
										|| Uzytkownik.u2_wynik == false) {
									out.println("Oczekiwanie, az drugi gracz skonczy gre...");
									Uspij.spij();
								}
								t1.slowo = "pusto";
								werdykt(out);
								reset_potwierdzenia();
								out.println("Czy chcesz grac dalej? tak/nie");
								czyszczenie_tablicy();
								done = game_continue(t1);
								nr_rozgrywka++;

							}
							if (line.trim().equals("BYE")) {
								if (nazwa_usera.equals(Uzytkownik.user1)) {
									Uzytkownik.u1_exit = true;
								} else if (nazwa_usera.equals(Uzytkownik.user2)) {
									Uzytkownik.u2_exit = true;
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
						Uspij.spij();
					}

				}
				in.close();
			} finally {
				Uzytkownik.koniec = true;
				inStream.close();
				outStream.close();
				incoming.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class Czasomierz extends Thread {

	String slowo = "pusto";
	Socket incoming;
	boolean done = false;

	Czasomierz(Socket incoming) {
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

class Uspij {
	public static void spij() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
