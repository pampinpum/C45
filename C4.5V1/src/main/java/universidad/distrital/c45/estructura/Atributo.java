package universidad.distrital.c45.estructura;
import java.util.ArrayList;


/**
 * Clase Atributo, contiene el indice con la mejor ganancia, la entropia del atributo, el nombre y los valores de atributo
 * @author nicolas
 *
 */
public class Atributo {
	public ArrayList<ValorAtributo> valorAtributos;
	public double entropiaAtributo;
	public String idAtributo;
	public Atributo(){
		valorAtributos = new ArrayList<ValorAtributo>();
	}
}
