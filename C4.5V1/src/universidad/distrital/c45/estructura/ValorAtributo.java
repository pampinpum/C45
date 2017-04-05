package universidad.distrital.c45.estructura;
import java.util.ArrayList;


/**
 * Clase que contiene los valores que pueden tomar sus atributos, su nombre y el total de apariciones
 * @author nicolas
 *
 */
public class ValorAtributo {
public String nombre;
public ArrayList<ValorClase> valores;
public double totalApariciones;
	public ValorAtributo(){
		valores = new ArrayList<ValorClase>();
	}
}
