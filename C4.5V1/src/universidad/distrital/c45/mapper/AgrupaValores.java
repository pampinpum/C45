package universidad.distrital.c45.mapper;
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Agrupa los valores (valor atributo, valor clase, apariciones) en una sola cadena separa por arroba "@" 
 * @author nicolas
 *
 */
public  class AgrupaValores extends Mapper <LongWritable, Text, Text, Text> {
	    private Text llave = new Text();
	    private Text valor = new Text();
    //Agrupa por el atributo la contabilizacion de los mismos
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {       	
     		String linea = value.toString();
        	String[] cadena = linea.split(",");
        		 llave.set(cadena[0]);   
        		 valor.set(cadena[1]+"@"+cadena[2]+"@"+cadena[cadena.length-1].replace("	", ""));
                 context.write(llave, valor);                         
    }
 } 