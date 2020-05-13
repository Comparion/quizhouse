package ports;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class losowanie {
	 static Random liczba = new Random(server.ziarno);
	 static int b=1;
	 static int los, los2;
	 static File file2 = new File("odpowiedzi/odp.txt");
	 static public int wylosuj()
	 {
		 if(b%2==0)
		 {
			 b++;
			 return los2;
		 }
		 else
		 {
		 los = liczba.nextInt(10);
		 los2 =los;
		 b++;
		 return los;
		 }
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
		 for(i=0;i<wylosowana+1;i++)
			{
				odpowiedz=plik.nextLine();
			}
		 if(zmienna.equals(odpowiedz))
			 return "poprawna odpowiedz!";
		 else
			 return "niepoprawna odpowiedz!";
	 }
}
