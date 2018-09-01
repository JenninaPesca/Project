package interpreter.parser.ast;

import interpreter.parser.ast.PrimLiteral;
import interpreter.visitors.Visitor;

public class BinLiteral extends PrimLiteral<Integer> {

	String pref; //variabile di classe aggiunta per salvare il prefisso del numero binario(che viene salvato come intero 
						//modifica: salvare con il metodo che salva n  anche la prima parte della stringa
	
	//costruttore modifica: nel costruttore va salvato anche il prefisso(?)
	public BinLiteral(String pref, int n) {
		super(n); //cancella(il commento): deve essere la prima istruzione del costruttore
		if (pref == "") 
			throw new IllegalArgumentException(); //controlla: motodo requireNonNull (?)
		this.pref = pref;
	}
	
	//metodo che trasforma da binario(stringa) a intero (salvaldo anche il prefisso) modifica:da fare (?) serve??
/*	public static int parseBin(String tokenString) {
		int i=tokenString.length();
		int binValue = 0;
		while (i>2) {
			char c = tokenString.charAt(i-1); //i-1 perchè l'ultimo indice è la lunghezza della stringa-1 dato che gli indici iniziano da 0 e la lunghezza da 1
			if (c == '1') {
				binValue = (int) (binValue + Math.pow(2.0, (double)((tokenString.length())-i)));
			}
			i--;			
		}
		return binValue;
	}
*/
	
	//metodo che trasforma da intero a binario(in stringa) controlla se è giusto. modifica: private o public(?), è un metodo di questa classe(?)
	public String intToBin() {
		String res=this.pref;
		int n = this.value;
		while(n != 0) {
			//se il resto intero della divisione è 0 aggiungi 0 in coda
			if(n%2 == 0)
				res += "0";
			//se il resto intero della divisione è 1 aggiungi 1 in coda 
			else 
				res += "1";
			n = n/2;
			System.out.println(n);
		}
		return res;
	}
	
	//controlla se è giusto
	@Override
	public String toString(){
		return getClass().getSimpleName() + "(" + this.intToBin() + ")";
	}
	
	//controlla: solo value(?)
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIntLiteral(value);
	}
	
	//main di prova
	/*public void main(String[] args) {
	BinLiteral b = new BinLiteral(2);
	System.out.println(this.intToBin());
	
}
*/
}


