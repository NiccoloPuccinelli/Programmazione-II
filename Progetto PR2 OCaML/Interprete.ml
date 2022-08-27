type ide = string;;
type exp = 
	| Eint of int 
	| Ebool of bool 
        | Estring of string           (*Exp per definizione stringa*)
	| Den of ide 
	| Prod of exp * exp 
	| Sum of exp * exp 
	| Diff of exp * exp 
	| Eq of exp * exp 
	| Minus of exp 
	| IsZero of exp 
	| Or of exp * exp 
	| And of exp * exp 
	| Not of exp 
	| Ifthenelse of exp * exp * exp 
	| Let of ide * exp * exp 
	| Fun of ide * exp 
	| FunCall of exp * exp 
	| Letrec of ide * exp * exp
        | EDict of edictionary        (*Exp per definizione dizionario*)
        | Get of exp * ide            (*Exp per ottenere elemento del dizionario*)
        | Remove of exp * ide         (*Exp per rimuovere elemento da dizionario*)
        | Add of exp * ide * exp      (*Exp per aggiungere elemento a dizionario*)
        | Clear of exp                (*Exp per rimuovere tutti gli elementi da dizionario*)
        | ApplyOver of exp * exp      (*Exp per applicare una funzione a tutti gli elementi del dizionario*)

and edictionary= EEmpty| EItem of ide * exp * edictionary
;;

(*ambiente polimorfo*)
type 't env = ide -> 't;;
let emptyenv (v : 't) = function x -> v;;
let applyenv (r : 't env) (i : ide) = r i;;
let bind (r : 't env) (i : ide) (v : 't) = function x -> if x = i then v else applyenv r x;;

(*tipi esprimibili*)
type evT = Int of int 
           | Bool of bool 
           | String of string 
           | Unbound 
           | FunVal of evFun 
           | RecFunVal of ide * evFun 
           | Dict of dictionary       (*Dict dizionario di tipo dictionary*)
and dictionary = Empty                (*dictionary vuoto*)
                 | Item of ide * evT * dictionary   (*dictionary tripla formata (ide, exp valutata, dictionary*)
and evFun = ide * exp * evT env

(*rts*)
(*type checking*)
let typecheck (s : string) (v : evT) : bool = match s with
	"int" -> (match v with
		Int(_) -> true |
		_ -> false) |
	"bool" -> (match v with
		Bool(_) -> true |
		_ -> false) |
        "string" -> (match v with
                String(_) -> true
                | _ -> false) |
        "dictionary" -> (match v with   (*Controllo di tipo: se v è un dizionario*)
                Dict(_) -> true |
                _ -> false) |
	_ -> failwith("not a valid type");;

(*funzioni primitive*)
let prod x y = if (typecheck "int" x) && (typecheck "int" y)
	then (match (x,y) with
		(Int(n),Int(u)) -> Int(n*u))
	else failwith("Type error");;

let sum x y = if (typecheck "int" x) && (typecheck "int" y)
	then (match (x,y) with
		(Int(n),Int(u)) -> Int(n+u))
	else failwith("Type error");;

let diff x y = if (typecheck "int" x) && (typecheck "int" y)
	then (match (x,y) with
		(Int(n),Int(u)) -> Int(n-u))
	else failwith("Type error");;

let eq x y = if (typecheck "int" x) && (typecheck "int" y)
	then (match (x,y) with
		(Int(n),Int(u)) -> Bool(n=u))
	else failwith("Type error");;

let minus x = if (typecheck "int" x) 
	then (match x with
	   	Int(n) -> Int(-n))
	else failwith("Type error");;

let iszero x = if (typecheck "int" x)
	then (match x with
		Int(n) -> Bool(n=0))
	else failwith("Type error");;

let vel x y = if (typecheck "bool" x) && (typecheck "bool" y)
	then (match (x,y) with
		(Bool(b),Bool(e)) -> (Bool(b||e)))
	else failwith("Type error");;

let et x y = if (typecheck "bool" x) && (typecheck "bool" y)
	then (match (x,y) with
		(Bool(b),Bool(e)) -> Bool(b&&e))
	else failwith("Type error");;

let non x = if (typecheck "bool" x)
	then (match x with
		Bool(true) -> Bool(false) |
		Bool(false) -> Bool(true))
	else failwith("Type error");;

let rec get (k:ide) (d) : evT =              (*F ausiliaria per Get: ricerca una chiave k nel dizionario e, se la trova, ne restituisce il valore associato v*)
        match d with                         (*:evT perchè deve ritornare un valore, mentre dopo sono solo procedure*)
            Empty -> Unbound |
            Item (key, v, ds) -> if key=k then v
                                 else get k ds;;

let rec add (k:ide) (x:evT) (d) =            (*F ausiliaria per Add: se la chiave k è già presente aggiorna il suo valore associato, altrimenti aggiunge in coda*)
        match d with
            Empty -> Item (k, x, Empty) |
            Item (key, v, ds) -> if key=k then Item (k, x, ds)
                                 else Item (key, v, add k x ds);;

let rec remove (k:ide) (d) =                 (*F ausiliaria per Remove: se la chiave k è presente la rimuove*)
        match d with
            Empty -> Empty |
            Item (key, v, ds) -> if key=k then ds
                                 else Item (key, v, remove k ds);;

let clear d = Empty;;                        (*F ausiliaria per Clear che restituisce un dizionario vuoto*)

(*interprete*)
let rec eval (e : exp) (r : evT env) : evT = match e with
	Eint n -> Int n |
	Ebool b -> Bool b |
        Estring s -> String s |
	IsZero a -> iszero (eval a r) |
	Den i -> applyenv r i |
	Eq(a, b) -> eq (eval a r) (eval b r) |
	Prod(a, b) -> prod (eval a r) (eval b r) |
	Sum(a, b) -> sum (eval a r) (eval b r) |
	Diff(a, b) -> diff (eval a r) (eval b r) |
	Minus a -> minus (eval a r) |
	And(a, b) -> et (eval a r) (eval b r) |
	Or(a, b) -> vel (eval a r) (eval b r) |
	Not a -> non (eval a r) |
	Ifthenelse(a, b, c) -> 
		let g = (eval a r) in
			if (typecheck "bool" g) 
				then (if g = Bool(true) then (eval b r) else (eval c r))
				else failwith ("nonboolean guard") |
	Let(i, e1, e2) -> eval e2 (bind r i (eval e1 r)) |
	Fun(i, a) -> FunVal(i, a, r) |
	FunCall(f, eArg) -> 
		let fClosure = (eval f r) in
			(match fClosure with
				FunVal(arg, fBody, fDecEnv) -> 
					eval fBody (bind fDecEnv arg (eval eArg r)) |
				RecFunVal(g, (arg, fBody, fDecEnv)) -> 
					let aVal = (eval eArg r) in
						let rEnv = (bind fDecEnv g fClosure) in
							let aEnv = (bind rEnv arg aVal) in
								eval fBody aEnv |
				_ -> failwith("non functional value")) |
        Letrec(f, funDef, letBody) ->
        		(match funDef with
            		Fun(i, fBody) -> let r1 = (bind r f (RecFunVal(f, (i, fBody, r)))) in
                         			                eval letBody r1 |
            		_ -> failwith("non functional def")) |
        EDict(d) -> let rec funDict (dict:edictionary) (env:evT env): dictionary =       (*F ausiliaria per fare eval di tutti i valori v del dizionario*)
                         (match dict with
                              EEmpty->Empty |
                              EItem (key, v, ds)->Item (key, (eval v env), (funDict  ds env))) in
                         Dict (funDict d r) |                                            (*Alla fine viene ritornato il dizionario con le exp valutate*)
        Get (d, k) -> let dict = (eval d r) in                                           (*Valutazione del dizionario*)
                if (typecheck "dictionary" dict) then let Dict(z) = dict in get k z      (*Se il tipo di d è dictionary, allora restituisce il valore associato a k*)
                else failwith("Type error") |
        Add (d, k, x) -> let dict = (eval d r) in                                        (*Valutazione del dizionario*)
        let v = (eval x r) in                                                            (*Valutazione v argomento della Add*)
                if (typecheck "dictionary" dict) then let Dict(z) = dict in Dict(add k v z)  (*Se il tipo di d è dictionary aggiunge l'associazione k-v e restituisce il dizionario*)
                else failwith("Type error") |
        Remove (d, k) -> let dict = (eval d r) in                                        (*Valutazione del dizionario*)
                if (typecheck "dictionary" dict) then let Dict(z) = dict in Dict (remove k z)(*Se il tipo di d è dictionary rimuove k dal dizionario e restituisce il dizionario*)
                else failwith("Type error") |
        Clear (d) -> let dict = (eval d r) in                                            (*Valutazione del dizionario*)
                if (typecheck "dictionary" dict) then let Dict(z) = dict in Dict(clear z)    (*Se il tipo di d è dictionary restituisce il dizionario vuoto*)
                else failwith("Type error") |                                                           
        ApplyOver (f, d) -> let d2 = (eval d r) in                                       (*Valutazione del dizionario*)
                                let  f2 = (eval f r) in                                  (*Valutazione della funzione*)
                                      let rec apply d2 fClosure =                        (*F ausiliaria apply che applica la funzione ad ogni elemento di d, simile a FunCall*)
                                          match d2 with
                                              Empty -> Empty |
                                              Item (k, v, ds) ->                         (*v è già stato valutato, diversamente da quanto accade nella FunCall*)
                                                  (match fClosure with
				                       FunVal(arg, fBody, fDecEnv) -> 
				                           let newval = (eval fBody (bind fDecEnv arg v))in   (*Non c'è quindi bisogno di fare eval dell'argomento di arg*)
                                                              Item(k, newval, apply ds fClosure) |            (*Sostituisce v col valore calcolato dalla funzione, cioè newval*)
				                       RecFunVal(g, (arg, fBody, fDecEnv)) -> 
						              let rEnv = (bind fDecEnv g fClosure) in
						                   let aEnv = (bind rEnv arg v) in            (*Non c'è quindi bisogno di fare eval dell'argomento di arg*)
                                                                       let newval = (eval fBody aEnv) in
								           Item(k, newval, apply ds fClosure) |   (*Sostituisce v col valore calcolato dalla funzione, cioè newval*)
			                        	_ -> failwith("non functional value")) in
                 if(typecheck "dictionary" d2) then let Dict(z) = d2 in Dict(apply z f2)  (*Se il tipo di d è dictionary restituisce il dizionario ai cui elementi è stata applicata f*)
                 else failwith("Type error")
;;
		
(*      TESTS      *)

let env0 = emptyenv Unbound;;(*Ambiente vuoto*)
 
let e1 = EDict(EItem("Ciacco",Eint(123),EItem("Ugolino",Eint(456),EEmpty)));;(*exp per un dizionario formato da due elementi*)

eval e1 env0;; (*definizione dizionario*)

eval (Get(e1,"Ciacco")) env0;;(*Get per il valore associato alla chiave Ciacco*)
 
eval (Get(e1,"Virgilio")) env0;;(*Get per il valore associato alla chiave Virgilio che restituisce Unbound poichè non ha nessun valore associato*)
 
eval (Add(e1,"Paolo",Sum(Eint(345),Eint(678)))) env0;;(*Add per aggiungere il valore associato alla chiave Paolo*)
 
eval (Add(e1,"Ugolino",Estring("Cerbero"))) env0;;(*Add sul valore associato alla chiave Ugolino che, essendo già presente all'interno del dizionario, modifica il valore associato*)
 
eval (Remove(e1,"Ciacco")) env0;;(*Remove per rimuovere il valore associato alla chiave Ciacco*)
 
eval (Remove(e1,"Caronte")) env0;;(*Remove per rimuovere il valore associato alla chiave Caronte che, non essendo presente, non modifica il dizionario*)
 
eval (Clear(e1)) env0;;(*Clear per rimuovere tutti i valori associati alle chiavi nel dizionario*)
 
eval (Clear(Eint(123))) env0;;(*Clear su un valore che non è un dizionario. Eccezione di tipo*)
 
let f1 = Fun("x", Diff(Den "x",Eint(56)));;(*Funzione che sottrae 56 al suo argomento*)
 
eval (ApplyOver(f1,e1)) env0;;(*Applica la funzione f a tutti gli elementi del dizionario*)
 
let f2 = Fun("x", ApplyOver(Fun("y",Diff(Den"y",Eint(200))),Den"x"));;(*Funzione che esegue ApplyOver su un argomento*)
 
let f3 = Fun("x", Add(Den "x", "Paolo", Eint(789)));;(*Funzione che aggiunge la chiave Paolo al dizionario argomento*)
 
let e2 = EDict(EItem("Inferno",e1,EItem("Purgatorio",EDict(EEmpty),EEmpty)));;(*exp per un dizionario contenente due dizionari come elementi*)
 
eval (ApplyOver(f2,e2)) env0;;(*ApplyOver di f2 sul dizionario e2*)
 
eval (ApplyOver(f3,e2)) env0;;(*ApplyOver di f3 sul dizionario e2*)
