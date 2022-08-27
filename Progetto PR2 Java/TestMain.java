

import java.rmi.AlreadyBoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TestMain {
	public static void main(String[] args) throws Exception {
		//TestMain per MySecureDataContainer<String>
		//Se si vuole modificare il tipo di data, modificare il campo <String> e i dati passati nei test
		//Se si vuole testare MySecureDataContainerHash, decommentare la riga 14 e commentare la riga 13
		MySecureDataContainer<String> database=new MySecureDataContainer<>();  
		//MySecureDataContainerHash<String> database=new MySecureDataContainerHash<>();  
		
		boolean ok=true;
		while(ok){												//utilizza un ciclo while perchè altrimenti la prima eccezione termina il programma
			//Caso corretto
			try { 			
				System.out.println("Caso corretto");
				database.createUser("Gerry", "Scotti");
				database.createUser("Paolo", "Bonolis");
				database.createUser("Valerio", "Staffelli");
				database.createUser("Ezio", "Greggio");
				database.createUser("Enzo", "Iacchetti");
				database.createUser("Max", "Laudadio");
				database.createUser("Moreno", "Morello");
				database.RemoveUser("Valerio", "Staffelli");
				System.out.println(database.getSize("Gerry", "Scotti"));
				System.out.println(database.put("Ezio", "Greggio", "tapiro"));
				System.out.println(database.getSize("Ezio", "Greggio"));
				System.out.println(database.get("Ezio","Greggio","tapiro"));
				System.out.println(database.remove("Ezio", "Greggio", "tapiro"));
				database.put("Ezio", "Greggio", "tapiro");
				database.copy("Ezio", "Greggio", "tapiro");
				System.out.println(database.getSize("Ezio", "Greggio"));
				System.out.println(database.get("Ezio","Greggio","tapiro"));
				database.addFriend("Enzo", "Iacchetti", "Ezio");
				database.share("Ezio", "Greggio", "Enzo", "tapiro");
				System.out.println(database.getSize("Enzo", "Iacchetti"));
				System.out.println(database.get("Enzo", "Iacchetti","tapiro"));
				database.removeFriend("Enzo", "Iacchetti", "Ezio");
				database.put("Ezio", "Greggio", "gabibbo");
				System.out.println(database.getSize("Ezio", "Greggio"));
				Iterator<String> iterator=database.getIterator("Ezio", "Greggio");
				while(iterator.hasNext()) {
					String dato=iterator.next();
					System.out.println(dato);
				}
			}
			/*
			Expected:
			0
			true
			1
			tapiro
			tapiro
			2
			tapiro
			1
			tapiro
			3
			tapiro
			tapiro
			gabibbo
			*/
			catch (AlreadyBoundException|NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			//Casi in cui vengono catturate eccezioni
			System.out.println("Casi in cui vengono catturati eccezioni");
			try { //Caso Id già presente
				database.createUser("Gerry", "Scotti");
				database.createUser("Gerry", "Scotti");
			}
			catch (AlreadyBoundException|NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Utente non presente
				database.RemoveUser("Ficarra", "Picone");
			}
			catch (NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Password errata
				database.RemoveUser("Max", "Ghione");
			}
			catch (NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Dato non presente
				database.get("Gerry", "Scotti","gabibbo");
			}
			catch (NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Dato non presente
				database.remove("Gerry", "Scotti","gabibbo");
			}
			catch (NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Dato non presente
				database.copy("Gerry", "Scotti","gabibbo");
			}
			catch (NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Dato non presente 
				database.share("Gerry", "Scotti","Marco","gabibbo");
			}
			catch (NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Amico non presente nella collezione
				database.put("Gerry", "Scotti", "gabibbo");
				database.share("Gerry", "Scotti","Marco","gabibbo");
			}
			catch (NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Amico già presente
				database.addFriend("Gerry", "Scotti","Max");
				database.addFriend("Gerry", "Scotti","Max");
			}
			catch (AlreadyBoundException|NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Amico non presente
				database.share("Gerry", "Scotti","Moreno","gabibbo");
			}
			catch (NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Operazione non consentita
				database.addFriend("Gerry", "Scotti","Gerry");
			}
			catch (AlreadyBoundException|NullPointerException|NoSuchElementException|IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
			try { //Caso Amico non presente
				database.removeFriend("Gerry", "Scotti","Paolo");
			}
			catch (NullPointerException|NoSuchElementException e){
				System.out.println(e.getMessage());
			}
			ok=false;										//Per terminare il ciclo (e quindi il programma) assegno false a ok 							
		}
	}
}