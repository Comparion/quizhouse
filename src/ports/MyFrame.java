package ports;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;



public class MyFrame extends JFrame implements ActionListener {
	
	private Rozgrywka rozgrywka;
	JLabel podaj;
	JTextField nazwa;
	public static String odpo;
	public static String nazwa_u="";
	public static Scanner sc = new Scanner(System.in);
	public static Socket s;
	public static Scanner sc1;
	public static PrintStream p;
	public pobieranie t1;
 	public MyFrame() throws UnknownHostException, IOException {
 		
 		s = new Socket("127.0.0.1",1364);
 		sc1= new Scanner (s.getInputStream());
 		p= new PrintStream(s.getOutputStream());
 		t1=new pobieranie();
 		setSize(350,200);
 		setTitle("QuizHouse: podaj nazwe");
 		setLayout(null);
 		setLocationRelativeTo(null);
 		JButton wejdz = new JButton("wejdz do gry");
 		wejdz.setBounds(85,100,120,20);
 		add(wejdz);
 		podaj = new JLabel("Podaj swoj¹ nazwê:");
 		podaj.setBounds(20, 20, 120, 20);
 		add(podaj);
 		nazwa= new JTextField("");
 		nazwa.setBounds(20,50,100,20);
 		add(nazwa);
 		wejdz.addActionListener(this);
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		setVisible(true);
 	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!getUser().equals(""))
		{
		if(rozgrywka==null)
			rozgrywka = new Rozgrywka(this);
		p.println(nazwa_u);
		rozgrywka.setVisible(true);
		t1.start();
		}
	}
	
	public String getUser()
	{
		nazwa_u=nazwa.getText();
		return nazwa_u;
	}
	
	public static void odbierz(String odebrane) {
		Rozgrywka.powiadomienia.append(odebrane);
		Rozgrywka.powiadomienia.append("\n");
	}
 }

class Rozgrywka extends JFrame implements ActionListener {
	
	private JButton buttonA,buttonB,buttonC,buttonD;
	private JButton buttonTAK, buttonNIE;
	private JButton buttonEXIT;
	public static JTextArea powiadomienia;
	private JScrollPane scrolPane;
	private JLabel nick;
	
	public Rozgrywka(JFrame owner){
		setSize(400,600);
 		setTitle("QuizHouse");
 		setLayout(null);
 		setLocationRelativeTo(null);
 		nick = new JLabel("nazwa: "+MyFrame.nazwa_u);
 		nick.setBounds(0, 0, 120, 20);
 		add(nick);
 		powiadomienia = new JTextArea();
 		powiadomienia.setLineWrap(true);
 		powiadomienia.setWrapStyleWord(true);
 		//powiadomienia.setBounds(30,30,330,300);
 		scrolPane =  new JScrollPane(powiadomienia);
 		scrolPane.setBounds(30,30,330,300);
 		DefaultCaret caret = (DefaultCaret)powiadomienia.getCaret(); 
 		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); 
 		//scrolPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
       // scrolPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scrolPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //int max = scrolPane.getVerticalScrollBar().getMaximum();
        //scrolPane.getVerticalScrollBar().setValue(max);
 		add(scrolPane);
 		JScrollBar vertical = scrolPane.getVerticalScrollBar();
 		vertical.setValue(vertical.getMaximum());      
 		//add(powiadomienia);
 		buttonA = new JButton("A");
 		buttonA.setBounds(30, 360, 50, 40);
 		add(buttonA);
 		buttonB = new JButton("B");
 		buttonB.setBounds(120, 360, 50, 40);
 		add(buttonB);
 		buttonC = new JButton("C");
 		buttonC.setBounds(210, 360, 50, 40);
 		add(buttonC);
 		buttonD = new JButton("D");
 		buttonD.setBounds(300, 360, 50, 40);
 		add(buttonD);
 		buttonTAK = new JButton("TAK");
 		buttonTAK.setBounds(30, 410, 140, 40);
 		add(buttonTAK);
 		buttonNIE = new JButton("NIE");
 		buttonNIE.setBounds(210, 410, 140, 40);
 		add(buttonNIE);
 		buttonEXIT = new JButton("ZAKONCZ ROZGRYWKE");
 		buttonEXIT.setBounds(30, 460, 320, 40);
 		add(buttonEXIT);
 		buttonA.addActionListener(this);
 		buttonB.addActionListener(this);
 		buttonC.addActionListener(this);
 		buttonD.addActionListener(this);
 		buttonTAK.addActionListener(this);
 		buttonNIE.addActionListener(this);
 		buttonEXIT.addActionListener(this);
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		owner.setVisible(false);
 		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object zrodlo =  e.getSource();
		if(zrodlo==buttonA)
			odpowiedz("A");
		else if(zrodlo==buttonB)
			odpowiedz("B");
		else if(zrodlo==buttonC)
			odpowiedz("C");
		else if(zrodlo==buttonD)
			odpowiedz("D");
		else if(zrodlo==buttonTAK)
			odpowiedz("tak");
		else if(zrodlo==buttonNIE)
			odpowiedz("nie");
		else if(zrodlo==buttonEXIT)
		{
			odpowiedz("BYE");
			dispose();
		}
	}
	
	public void odpowiedz(String odp) {
		MyFrame.odpo=odp;
		MyFrame.p.println(odp);
	}
}

class pobieranie extends Thread{
	String przy;
	public void run(){
		while(true)
		{
			przy=MyFrame.sc1.nextLine();
			MyFrame.odbierz(przy);
		}
	}
}