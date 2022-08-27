
import java.rmi.AlreadyBoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MySecureDataContainerHash<E> implements SecureDataContainerInterface<E>{
	/*
	TYPICAL ELEMENT: Collezione di utenti che contiene tutte le informzioni di ogni singolo utente
	ABSTRACTION FUNCTION: a(TotalUser)=f:[String]->[User]
	REP INVARIANT: TotalUser!=null && forall i,j.i!=j==>TotalUser.keySet().get(i)!=TotalUser.keySet().get(j)
	 */
	
	HashMap<String,User> TotalUser;														//Dichiaro un HashMap di utenti che rappresenta la collezione di utenti
	public MySecureDataContainerHash() {												//Costruttore di MySecureDataContainerHash
		TotalUser=new HashMap<>();
	}
	
	@Override
	public void createUser(String Id, String passw) throws NullPointerException, AlreadyBoundException {
		if(Id==null) throw new NullPointerException("Null Id");
		if(passw==null) throw new NullPointerException("Null password");												//Nota: non è chiamato il metodo Test() poichè altrimenti lancerebbe			
		User utente=new User(Id, passw);																				//un'eccezione nel caso l'utente non fosse presente nella collezione
		if(TotalUser.containsKey(Id)) throw new AlreadyBoundException("Id già presente"); 						        //Se l'Id è già presente nella collezione lancia un'eccezione 
		else TotalUser.put(Id,utente);																					//Se vengono superati i controlli aggiunge l'utente alla collezione
	}

	@Override
	public void RemoveUser(String Id, String passw) throws NullPointerException, NoSuchElementException{
		Test(Id,passw);																									//Attraverso il metodo Test effettua i controlli di identità
		TotalUser.remove(Id);																							//Se vengono superati i controlli rimuove l'utente dalla collezione
	}

	@Override
	public int getSize(String Owner, String passw) throws NullPointerException, NoSuchElementException {
		Test(Owner,passw);																								//Attraverso il metodo Test effettua i controlli di identità
		return TotalUser.get(Owner).getData().size();																	//Se vengono superati i controlli restituisce il numero di data di Owner
	}

	@Override
	public boolean put(String Owner, String passw, E data) throws NullPointerException, NoSuchElementException {
		boolean esito=false;
		Test(Owner,passw);																								//Attraverso il metodo Test effettua i controlli di identità
		if(data==null) throw new NullPointerException("Null data");
		TotalUser.get(Owner).getData().add(data);																		//Se vengono superati i controlli aggiunge data al vettore di dati dell'utente
		esito=true;
		return esito;																									//Se il dato è stato aggiunto, esito avrà assunto valore true e verrà restituito
	}
	
	public E get(String Owner, String passw, E data) throws NullPointerException, NoSuchElementException {
		Test(Owner,passw);																								//Attraverso il metodo Test effettua i controlli di identità
		if(data==null) throw new NullPointerException("Null data");
		if(TotalUser.get(Owner).getData().contains(data))																//Se vengono superati i controlli e il dato è presente nel vettore di dati dell'utente, 
			return data;																								//allora restituisce una copia del dato
		throw new NoSuchElementException("Dato non presente");															//Se non è stato restituito alcun dato (e sono stati superati i controlli precedenti),
	}																													//allora vuol dire che tale dato non è presente nel vettore di dati dell'utente e lancia un'eccezione
	
	public E remove (String Owner, String passw, E data) throws NullPointerException, NoSuchElementException {
		Test(Owner,passw);																								//Attraverso il metodo Test effettua i controlli di identità
		if(data==null) throw new NullPointerException("Null data");
		if(TotalUser.get(Owner).getData().contains(data)) {																//Se vengono superati i controlli e il dato è presente nel vettore di dati dell'utente,
			TotalUser.get(Owner).getData().remove(data);																//allora rimuove il dato da tale vettore di dati
			return data;																								//Se il dato viene rimosso, lo restituisce
			}
		throw new NoSuchElementException("Dato non presente");															//Se non è stato restituito alcun dato (e sono stati superati i controlli precedenti),
	}																													//allora vuol dire che tale dato non è presente nel vettore di dati dell'utente e lancia un'eccezione
	
	@Override
	public void copy(String Owner, String passw, E data) throws NullPointerException, NoSuchElementException {
		Test(Owner,passw);																								//Attraverso il metodo Test effettua i controlli di identità
		if(data==null) throw new NullPointerException("Null data");	
		if(TotalUser.get(Owner).getData().contains(data))																//Se vengono superati i controlli e il dato è presente nel vettore di dati dell'utente,
			TotalUser.get(Owner).getData().add(data);																	//aggiunge data al vettore di dati dell'utente
		else throw new NoSuchElementException("Dato non presente");
	}

	@Override
	public void share(String Owner, String passw, String Other, E data) throws NullPointerException, NoSuchElementException {
		Test(Owner,passw);																								//Attraverso il metodo Test effettua i controlli di identità
		if(data==null) throw new NullPointerException("Null data");
		if(!TotalUser.get(Owner).getData().contains(data)) throw new NoSuchElementException("Dato non presente");		//Se il dato non è presente nel vettore di dati dell'utente lancia un'eccezione
		if(!TotalUser.containsKey(Other)) throw new NoSuchElementException("Amico non presente nella collezione");		//Se Other non è presente nella collezione lancia un'eccezione
		if(!TotalUser.get(Other).getFriends().contains(Owner)) throw new NoSuchElementException("Amico non presente");	//Se Owner non è presente nella lista amici di Other lancia un'eccezione
		TotalUser.get(Other).getData().add(data);																		//Se trova l'utente, allora aggiunge data al suo vettore di dati
	}
	
	@Override
	public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, NoSuchElementException {
        Test(Owner,passw);																								//Attraverso il metodo Test effettua i controlli di identità
        return TotalUser.get(Owner).getData().iterator();																//Restituisce l'iteratore del vettore di dati di Owner	
	}
	
	public void addFriend(String Owner, String passw, String Other) throws NullPointerException, NoSuchElementException, AlreadyBoundException, IllegalArgumentException {
		Test(Owner,passw);																								//Attraverso il metodo Test effettua i controlli di identità
		if(Owner.compareTo(Other)==0) throw new IllegalArgumentException("Operazione non consentita");					//Se si prova ad aggiungere se stessi lancia un'eccezione
		if(!TotalUser.containsKey(Other)) throw new NoSuchElementException("Amico non presente nella collezione");		//Se Other non è presente nella collezione lancia un'eccezione
		if(TotalUser.get(Owner).getFriends().contains(Other)) throw new AlreadyBoundException("Amico già presente");	//Se Other è già presente nella lista amici di Owner lancia un'eccezione
		TotalUser.get(Owner).getFriends().add(Other);																	//Se vengono superati i controlli aggiunge Other alla lista amici di Owner
	}
	
	public void removeFriend(String Owner, String passw, String Other) throws NullPointerException, NoSuchElementException {
		Test(Owner,passw);																								//Attraverso il metodo Test effettua i controlli di identità
		if(!TotalUser.containsKey(Other)) throw new NoSuchElementException("Amico non presente nella collezione");		//Se Other non è presente nella collezione lancia un'eccezione
		if(!TotalUser.get(Owner).getFriends().contains(Other)) throw new NoSuchElementException("Amico non presente");	//Se Other non è presente nella lista amici di Owner lancia un'eccezione
		TotalUser.get(Owner).getFriends().remove(Other);																//Se vengono superati i controlli rimuove Other dalla lista amici di Owner
	}
	
	private void Test(String Id, String passw) throws NullPointerException, NoSuchElementException {																		//Metodo privato che effettua gli opportuni controlli di identità
		if(Id==null) throw new NullPointerException("Null Id");
		if(passw==null) throw new NullPointerException("Null password");
		if(!TotalUser.containsKey(Id)) throw new NoSuchElementException("Utente non presente");							//Se non ha trovato l'utente, allora lancia un'eccezione
		if(TotalUser.get(Id).getpassw().compareTo(passw)!=0) throw new NoSuchElementException("Password errata");		//Se la password di Id non è uguale a passw, allora lancia un'eccezione
	}
}