package ports;
import java.util.*;


public class losowanie {
	 static Random liczba = new Random(server.ziarno);
	 static int b=1;
	 static int los, los2;
	 static public int wylosuj()
	 {
		 if(b%2==0)
		 {
			 b++;
			 return los2;
		 }
		 else
		 {
		 los = liczba.nextInt(101);
		 los2 =los;
		 b++;
		 return los;
		 }
	 }
	 
	 static public String sprawdz(String zmienna)
	 {
		 if(zmienna.equals("jeden"))
			 return "poprawna odpowiedz!";
		 else
			 return "niepoprawna odpowiedz!";
	 }
}
