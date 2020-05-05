package ports;
import java.util.Random;


public class losowanie {
	 static Random liczba = new Random();
	 static int los;
	 static public int wylosuj()
	 {
		 los = liczba.nextInt(101);
		 return los;
	 }
}
