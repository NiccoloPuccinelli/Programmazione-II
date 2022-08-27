

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class User<E> {
	/*
	OVERVIEW: Informazioni relative al singolo utente
	TYPICAL ELEMENT: Oggetto che rappresenta il singolo utente
	ABSTRACTION FUNCTION: a(passw,Id,amici,dati)=<passw,Id,amici=<amici[i_0]...amici[i_size]>,dati=<dati[i_0]...dati[i_size]>>
	REP INVARIANT: passw!=null && Id!=null && amici!=null && dati!=null &&  forall i.amici.get(i)!=null && dati.get(i)!=null &&
				   forall i,j.i!=j==>amici.get(i)!=amici.get(j)
	 */
	
	private String passw;																//Classe User con variabili String Id, String password, un ArrayList
	private String Id;																	//di dati (di tipo E) e un ArrayList di amici (di tipo String)
	private ArrayList<E> dati=new ArrayList<>();
	private ArrayList<String> amici=new ArrayList<>();
	public User(String Idnew, String passwnew) {										//Costruttore di User che riceve come parametri Id e password dell'utente
		this.Id=Idnew;
		this.passw=passwnew;
	}
	public String getId() {
		//EFFECTS: resituisce la stringa Id
		return Id;
	}
	public String getpassw() {
		//EFFECTS: restituisce la stringa password
		return passw;
	}
	public ArrayList<E> getData() {
		//EFFECTS: restituisce l'ArrayList di generici di dati
		return dati;
	}
	public ArrayList<String> getFriends() {
		//EFFECTS: restituisce l'ArrayList di stringhe di amici
		return amici;
	}
	public Iterator<E> iterator() {
		//EFFECTS: restituisce l'iteratore dei dati
		return new myIterator((Iterator<E>) dati.iterator());
	}
	
	public class myIterator implements Iterator<E> {									//Definisco la classe iterator senza remove()
        private Iterator<E> it;
        public myIterator(Iterator<E> iteratore) {
            it = iteratore;
        }
        @Override
        public boolean hasNext() {
           return  it.hasNext();
        }
        @Override
        public E next() {
            return it.next();
        }
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }
	
	@Override
	public boolean equals(Object o) {													//Restituisce true se e solo se il chiamante riferisce lo stesso oggetto, oppure
		if(this==o) return true;														//se due utenti hanno stesso Id e stessa password, altrimenti restituisce false
		if(o==null || this.getClass()!=o.getClass()) return false;						//Se i due oggetti appartengono ad una classe diversa, oppure l'oggetto o è uguale
		User check=(User) o;															//a null restituisce false
		return Objects.equals(this.Id,check.getId()) && 
				Objects.equals(this.passw,check.getpassw());
	}
}