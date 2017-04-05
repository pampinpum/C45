package universidad.distrital.c45.mapper;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Recibe un archivo plano con los valores de los atributos del conjunto de datos y la clase asociada 
 * los atributos de cada clase vienen separados por coma ","
 * @author nicolas
 *
 */
public class MapearAtributos extends Mapper<LongWritable, Text, Text, IntWritable> { 
	    private final static IntWritable one = new IntWritable(1);
	    private Text texto = new Text();
	    private static String[] nombreClase;
    //método de la clase Map
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException { 	     
     	String linea = value.toString();
     	//para identificar la primera linea del archivo de entrada se va a comparar la llave, la comparacion se hara TODAS las veces
     	if (key.get() == 0){
     		nombreClase= linea.split(",");    
     	} else{
          	 String[] cadena = linea.split(",");       
        	 for (int i = 0; i<=cadena.length -2; i++){    
        		 //Adiciona el número de atributo, el valor del atributo y la clase asociada
        		 texto.set(nombreClase[i] + "," + cadena[i] + "," + cadena[cadena.length -1] + ",");   
        		 System.out.println(texto);
                 context.write(texto, one);               
        	 	}   
        	 texto.set("total," + "total" + "," + cadena[cadena.length-1] +",");
        	 context.write(texto, one);   
     	}
    }
 } 