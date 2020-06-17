package serwer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Losowanie {
	 static Random liczba = new Random(Server.ziarno);
	 static int b=1;
	 static int los, los2;
	 static File file2 = new File("odpowiedzi/odp.txt");
	 static int[] tablica=new int[10];
	 static int nr_tab=0;
	 
	 static public void losuj(int Przeslana)
	 {
		 boolean porf=false;
		 int bufor=0;
		 while(!porf){
			 los = liczba.nextInt(10)+Przeslana; 
			 bufor=0;
			 do
			 {
				 if(tablica[bufor]==los)
				 {
					 porf=false;
					 break;
				 }
					 else
					 {
						porf=true;
					 }
				 bufor++;
			 }while(tablica[bufor]!=0);
			 
			
		 }
		 
		 tablica[nr_tab]=los;
		 nr_tab++;
		 los2 =los;
		 b++;
	 }
	 
	 static public int wylosuj()
	 {
		 if(b%2==0)
		 {
			 b++;
			 return los2;
		 }
		 else if(Uzytkownik.wybor==1)
		 {
			 losuj(1);
		 }
		 else if(Uzytkownik.wybor==2)
		 {
			 losuj(11);
		 }
		 else if(Uzytkownik.wybor==3)
		 {
			 losuj(21);
		 }
		 else if(Uzytkownik.wybor==4)
		 {
			 losuj(31);
		 }
		 return los;
	 }
	 
	 static public String sprawdz(String zmienna, int wylosowana)
	 {
		 String odpowiedz = null;
		 int i;
		 Scanner plik = null;
		try {
			plik = new Scanner(file2);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		 for(i=0;i<wylosowana;i++)
			{
				odpowiedz=plik.nextLine();
			}
		 if(zmienna.equals(odpowiedz))
			 return "poprawna odpowiedz!";
		 else
			 return "NIEpoprawna odpowiedz!";
	 }
}
