

import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MySecureDataContainer<E> implements SecureDataContainerInterface<E>{			
	/*
	TYPICAL ELEMENT: Vettore di utenti che contiene tutte le informzioni di ogni singolo utente
	ABSTRACTION FUNCTION: a(TotalUser)=<TotalUser[i_0]...TotalUser[i_size]>
	REP INVARIANT: TotalUser!=null && forall i.TotalUser.get(i)!=null &&
				   forall i,j.i!=j==>TotalUser.get(i)!=TotalUser.get(j)  
	 */

	ArrayList<User> TotalUser;																//Dichiaro un ArrayList di utenti che rappresenta la collezione di utenti
	public MySecureDataContainer() {														//Costruttore di MySecureDataContainer
		TotalUser=new ArrayList<>();																	
	}
	
	@Override
	public void createUser(String Id, String passw) throws NullPointerException, AlreadyBoundException { 
		if(Id==null) throw new NullPointerException("Null Id");											
		if(passw==null) throw new NullPointerException("Null password");									//Nota: non è chiamato il metodo Test() poichè altrimenti lancerebbe
		User utente=new User(Id, passw);																	//un'eccezione nel caso l'utente non fosse presente nella collezione
		for(User u:TotalUser) {																				//Per ogni utente u appartenente alla collezione controlla che non ce
			if(u.getId().compareTo(Id)==0) throw new AlreadyBoundException("Id già presente");				//ne sia uno con lo stesso Id dell'utente che voglio creare
		}
		TotalUser.add(utente);																				//Se vengono superati i controlli aggiunge l'utente alla collezione
	}

	@Override
	public void RemoveUser(String Id, String passw) throws NullPointerException, NoSuchElementException { 
		User utente=Test(Id,passw);																		 	//Attraverso il metodo Test effettua i controlli di identità
		TotalUser.remove(utente);																		 	//Se vengono superati i controlli rimuove l'utente dalla collezione
	}

	@Override
	public int getSize(String Owner, String passw) throws NullPointerException, NoSuchElementException { 
		User utente=Test(Owner,passw);																		//Attraverso il metodo Test effettua i controlli di identità
		return utente.getData().size();																		//Se vengono superati i controlli restituisce il numero di data di Owner
	}

	@Override
	public boolean put(String Owner, String passw, E data) throws NullPointerException, NoSuchElementException { 
		boolean esito=false;
		User utente=Test(Owner,passw);																		//Attraverso il metodo Test effettua i controlli di identità
		if(data==null) throw new NullPointerException("Null data");												
		utente.getData().add(data);																			//Se vengono superati i controlli aggiunge data al vettore di dati dell'utente
		esito=true;																								
		return esito;																						//Se il dato è stato aggiunto, esito avrà assunto valore true e verrà restituito
	}

	public E get(String Owner, String passw, E data) throws NullPointerException, NoSuchElementException { 
		User utente=Test(Owner,passw);																		//Attraverso il metodo Test effettua i controlli di identità
		if(data==null) throw new NullPointerException("Null data");	
		if(utente.getData().contains(data)) 																//Se vengono superati i controlli e il dato è presente nel vettore di dati dell'utente, 
			return data;																					//allora restituisce una copia del dato
		throw new NoSuchElementException("Dato non presente");												//Se non è stato restituito alcun dato (e sono stati superati i controlli precedenti),
	}																										//allora vuol dire che tale dato non è presente nel vettore di dati dell'utente e lancia un'eccezione
	
	public E remove (String Owner, String passw, E data) throws NullPointerException, NoSuchElementException { 
		User utente=Test(Owner,passw);																		//Attraverso il metodo Test effettua i controlli di identità
		if(data==null) throw new NullPointerException("Null data");											  
		if(utente.getData().contains(data)) {																//Se vengono superati i controlli e il dato è presente nel vettore di dati dell'utente,
			utente.getData().remove(data);																	//allora rimuove data dal vettore di dati dell'utente																										
			return data;																					//Se il dato viene rimosso, lo restituisce
		}
		throw new NoSuchElementException("Dato non presente");												//Se non è stato restituito alcun dato (e sono stati superati i controlli precedenti),
	}																								  		//allora vuol dire che tale dato non è presente nel vettore di dati dell'utente e lancia un'eccezione
	
	@Override
	public void copy(String Owner, String passw, E data) throws NullPointerException, NoSuchElementException { 
		User utente=Test(Owner,passw);																		//Attraverso il metodo Test effettua i controlli di identità
		if(data==null) throw new NullPointerException("Null data");
		if(utente.getData().contains(data))     															//Se vengono superati i controlli e il dato è presente nel vettore di dati dell'utente,																	
			utente.getData().add(data);																		//allora aggiunge data al vettore di dati dell'utente
		else throw new NoSuchElementException("Dato non presente");
	}

	@Override
	public void share(String Owner, String passw, String Other, E data) throws NullPointerException, NoSuchElementException {
		User utente=Test(Owner,passw);																		//Attraverso il metodo Test effettua i controlli di identità
		if(Other==null) throw new NullPointerException("Null Other");												
		boolean amicotrovato=false;
		if(data==null) throw new NullPointerException("Null data");
		if(!utente.getData().contains(data)) throw new NoSuchElementException("Dato non presente");
		for(User u: TotalUser) {																			//Utilizza un ciclo for per controllare che Other sia presente nella collezione
			if(u.getId().compareTo(Other)==0) {
				if(!u.getFriends().contains(Owner)) throw new NoSuchElementException("Amico non presente");	//Se Owner non è presente nella lista amici di Other lancia un'eccezione
				u.getData().add(data);
				amicotrovato=true;																			//Se trova l'amico, allora aggiunge data al suo vettore di dati
			}
		}
		if(!amicotrovato) throw new NoSuchElementException("Amico non presente nella collezione");			//Se non ha trovato l'amico, allora lancia un'eccezione
	}
	
	@Override
	public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, NoSuchElementException {
		User utente=Test(Owner,passw);																		//Attraverso il metodo Test effettua i controlli di identità
		return utente.getData().iterator();																	//Restituisce l'iteratore del vettore di dati di Owner
	}
	
	public void addFriend(String Owner, String passw, String Other) throws NullPointerException, NoSuchElementException, AlreadyBoundException {
		User utente=Test(Owner,passw);																		//Attraverso il metodo Test effettua i controlli di identità
		boolean trovato=false;
		if(Owner.compareTo(Other)==0) throw new IllegalArgumentException("Operazione non consentita");		//Se si prova ad aggiungere se stessi lancia un'eccezione
		for(User u: TotalUser) {																			//Utilizza un ciclo for per controllare che Other sia presente nella collezione
			if(u.getId().compareTo(Other)==0)
				trovato=true;	
		}
		if(!trovato) throw new NoSuchElementException("Amico non presente nella collezione");				//Se non ha trovato l'amico, allora lancia un'eccezione
		if(utente.getFriends().contains(Other)) throw new AlreadyBoundException("Amico già presente");		//Se Other è già presente nella lista di amici lancia un'eccezione
		utente.getFriends().add(Other);																		//Se vengono superati i controlli aggiunge Other alla lista amici di Owner
	}
	
	public void removeFriend(String Owner, String passw, String Other) {
		User utente=Test(Owner,passw);																		//Attraverso il metodo Test effettua i controlli di identità
		boolean trovato=false;
		for(User u: TotalUser) {																			//Utilizza un ciclo for per controllare che Other sia presente nella collezione
			if(u.getId().compareTo(Other)==0)
				trovato=true;	
		}
		if(!trovato) throw new NoSuchElementException("Amico non presente nella collezione");				//Se non ha trovato l'amico, allora lancia un'eccezione
		if(!utente.getFriends().contains(Other)) throw new NoSuchElementException("Amico non presente");	//Se Other non è presente nella lista di amici lancia un'eccezione
		utente.getFriends().remove(Other);																	//Se vengono superati i controlli rimuove Other dalla lista amici di Owner
	}
	
	private User Test(String Id, String passw) throws NullPointerException, NoSuchElementException { 		//Metodo privato che restituisce l'utente con password passw e nome Id, 
		if(Id==null) throw new NullPointerException("Null Id");												//facendo gli opportuni controlli di identità
		if(passw==null) throw new NullPointerException("Null password");
		int i=0;										
		while(i<TotalUser.size()) {																			//Utilizza un ciclo per controllare che Id e password siano giusti
			if(TotalUser.get(i).getId().compareTo(Id)==0) {
				if(TotalUser.get(i).getpassw().compareTo(passw)==0)
					return TotalUser.get(i);																//Una volta superati i controlli, restituisce l'utente
				else throw new NoSuchElementException("Password errata");									//Se la password di Id non è uguale a passw, allora lancia un'eccezione 
			}
			else i++;
		}
		throw new NoSuchElementException("Utente non presente");											//Se non ha trovato l'utente, allora lancia un'eccezione
	}
	
}