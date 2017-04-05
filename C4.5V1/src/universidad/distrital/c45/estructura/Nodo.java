package universidad.distrital.c45.estructura;
import java.util.ArrayList;

/**
 * Clase Nodo para generar el arbol que contiene su valor, su identificador y sus nodos hijos
 * @author nicolas
 *
 */
public class Nodo {
	public String valor;
	public String id;
	public ArrayList<Nodo> hijos;
	public Nodo(){
		hijos= new ArrayList<Nodo>();
	}
}
