

import java.rmi.AlreadyBoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public interface SecureDataContainerInterface<E>{
	/*
	OVERVIEW: Tipo modificabile che rappresenta un Data Storage per la rappresentazione e condivisione di dati di tipo E
	*/
	
	public void createUser (String Id, String passw) throws NullPointerException, AlreadyBoundException;
		/*
		 Crea l’identità un nuovo utente della collezione
		 REQUIRES: Id!=null && passw!=null 
		 THROWS: se (Id==null || passw==null) lancia NullPointerException (unchecked)
		 		 se Id è già presente nella collezione lancia AlreadyBoundException (unchecked)
		 MODIFIES: this
		 EFFECTS: Crea un nuovo utente nella collezione se questo non è già presente nella collezione
		 */
	
	public void RemoveUser (String Id, String passw) throws NullPointerException, NoSuchElementException;
		/*
		Rimuove l’utente dalla collezione
	 	REQUIRES: Id!=null && passw!=null
	 	THROWS: se (Id==null || passw==null) lancia NullPointerException (unchecked)
		 	    se Id non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	    se la password è errata lancia NoSuchElementException (unchecked)
	 	MODIFIES: this
	 	EFFECTS: Rimuove l'utente dalla collezione se vengono rispettati i controlli di identità
		 */
	public int getSize (String Owner, String passw) throws NullPointerException, NoSuchElementException;
		/*
		Restituisce il numero degli elementi di un utente presenti nella collezione
	 	REQUIRES: Owner!=null && passw!=null 
	 	THROWS: se (Owner==null || passw==null) lancia NullPointerException (unchecked)
		 	 	se Owner non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	 	se la password è errata lancia NoSuchElementException (unchecked)
	 	MODIFIES: \
	 	EFFECTS: Restituisce il numero di dati di tipo E dell'utente se vengono rispettati i controlli di identità
		 */
	public boolean put (String Owner, String passw, E data) throws NullPointerException, NoSuchElementException;
		/*
		Inserisce il valore del dato nella collezione se vengono rispettati i controlli di identità
	 	REQUIRES: Owner!=null && passw!=null && data!=null
	 	THROWS: se (Owner==null || passw==null || data==null) lancia NullPointerException (unchecked)
		 	 	se Owner non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	 	se la password è errata lancia NoSuchElementException (unchecked)
	 	MODIFIES: this
	 	EFFECTS: Inserisce il valore del dato nel vettore di dati dell'utente se vengono rispettati i controlli di identità, restituendo true
	 			 nel caso l'operazione vada a buon fine
		 */
	public E get (String Owner, String passw, E data) throws NullPointerException, NoSuchElementException;
		/*
		Ottiene una copia del valore del dato nella collezione se vengono rispettati i controlli di identità
	 	REQUIRES: Owner!=null && passw!=null && data!=null
	 	THROWS: se (Owner==null || passw==null || data==null) lancia NullPointerException (unchecked)
		 	 	se Owner non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	 	se la password è errata lancia NoSuchElementException (unchecked)
         	 	se data non è presente nel vettore di dati di Owner lancia NoSuchElementException (unchecked)
	 	MODIFIES: \
	 	EFFECTS: Restituisce una copia di data se vengono rispettati i controlli di identità && data è presente nel vettore di dati di Owner
		 */
	public E remove (String Owner, String passw, E data) throws NullPointerException, NoSuchElementException;
		/*
		Rimuove il dato nella collezione se vengono rispettati i controlli di identità
	 	REQUIRES: Owner!=null && passw!=null && data!=null
	 	THROWS: se (Owner==null || passw==null || data==null) lancia NullPointerException (unchecked)
		 	 	se Owner non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	 	se la password è errata lancia NoSuchElementException (unchecked)
         	 	se data non è presente nel vettore di dati di Owner lancia NoSuchElementException (unchecked)
	 	MODIFIES: this
	 	EFFECTS: Rimuove (e restituisce) data dal vettore di dati di Owner se vengono rispettati i controlli di identità && data è presente nel vettore di dati di Owner
		 */
	public void copy (String Owner, String passw, E data) throws NullPointerException, NoSuchElementException;
		/*
		Crea una copia del dato nella collezione se vengono rispettati i controlli di identità
	 	REQUIRES: Owner!=null && passw!=null && data!=null
	 	THROWS: se (Owner==null || passw==null || data==null) lancia NullPointerException (unchecked)
		 	 	se Owner non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	 	se la password è errata lancia NoSuchElementException (unchecked)
         	 	se data non è presente nel vettore di dati di Owner lancia NoSuchElementException (unchecked)
	 	MODIFIES: this
	 	EFFECTS: Aggiunge data al vettore di dati di Owner se vengono rispettati i controlli di identità && data è presente nel vettore di dati di Owner
		 */
	public void share (String Owner, String passw, String Other, E data) throws NullPointerException, NoSuchElementException;
		/*
		Condivide il dato nella collezione con un altro utente se vengono rispettati i controlli di identità
	 	REQUIRES: Owner!=null && passw!=null && Other!=null && data!=null
	 	THROWS: se (Owner==null || passw==null || Other==null || data==null) lancia NullPointerException (unchecked)
		 	 	se Owner non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	 	se la password è errata lancia NoSuchElementException (unchecked)
         	 	se Other non è presente nella collezione lancia NoSuchElementException (unchecked)
         	 	se data non è presente nel vettore di dati di Owner lancia NoSuchElementException (unchecked)
         	 	se Owner non è presente nella lista amici di Other lancia NoSuchElementException (unchecked)
	 	MODIFIES: this
	 	EFFECTS: condivide il dato con un altro utente se vengono rispettati i controlli di identità && data è presente nel vettore di dati di Owner && 
	 			 Owner è presente nella lista amici di Other && Other è presente nella collezione
		 */
	public Iterator<E> getIterator (String Owner, String passw) throws NullPointerException, NoSuchElementException;
		/*
		restituisce un iteratore (senza remove) che genera tutti i dati dell’utente in ordine arbitrario se vengono rispettati i controlli di identità
	 	REQUIRES: Owner!=null && passw!=null 
	 	THROWS: se (Owner==null || passw==null) lancia NullPointerException (unchecked)
	 			se Owner non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	 	se la password è errata lancia NoSuchElementException (unchecked)
	 	MODIFIES: \
	 	EFFECTS: restituisce un iteratore di dati appartenenti al vettore di dati Owner se vengono rispettati i controlli di identità
		 */
	public void addFriend (String Owner, String passw, String Other) throws NullPointerException, NoSuchElementException, AlreadyBoundException, IllegalArgumentException;
		/*
		aggiunge Other alla propria lista di amici
		REQUIRES: Owner!=null && passw!=null && Other!=null
		THROWS: se (Owner==null || passw==null || Other==null) lancia NullPointerException (unchecked)
	 			se Owner non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	 	se la password è errata lancia NoSuchElementException (unchecked)
		 	 	se Owner tenta di aggiungere Owner alla lista amici lancia IllegalArgumentException (unchecked)
		 	 	se Other non è presente nella collezione lancia NoSuchElementException (unchecked)
		 	 	se Other è già presente nella lista amici di Owner lancia AlreadyBoundException (unchecked)
		 MODIFIES: this
		 EFFECTS: aggiunge Other alla propria lista di amici se vengono rispettati i controlli di identità && Other non è già presente nella lista amici di Owner
		 		  && non si tenta di aggiungere se stessi a tale lista && Other è presente nella collezione
		 */
	
	public void removeFriend (String Owner, String passw, String Other) throws NullPointerException, NoSuchElementException;
		/*
		rimuove Other dalla propria lista di amici
		REQUIRES: Owner!=null && passw!=null && Other!=null
		THROWS: se (Owner==null || passw==null || Other==null) lancia NullPointerException (unchecked)
 				se Owner non è presente nella collezione lancia NoSuchElementException (unchecked)
	 	 		se la password è errata lancia NoSuchElementException (unchecked)
	 	 		se Other non è presente nella collezione lancia NoSuchElementException (unchecked)
	 	 		se Other non è presente nella lista amici di Owner lancia NoSuchElementException (unchecked)
	 	MODIFIES: this
	 	EFFECTS: rimuove Other dalla propria lista di amici se vengono rispettati i controlli di identità && Other è presente nella lista amici di Owner
		 		 && Other è presente nella collezione
		 */
}