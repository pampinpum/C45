package universidad.distrital.c45.reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


/**
 * Concatena los valores asociados para generar una única salida por atributo con los valores que puede tomar 
 * la clase asociada al valor y el número de apariciones
 * @author nicolas
 *
 */
 public  class ConcatenaValores extends Reducer<Text, Text, Text, String> {
     private int numero = 0;

    public void reduce(Text key, Iterable<Text> values, Context context) 
      throws IOException, InterruptedException {
         Text llave =  new Text (context.getCurrentKey().toString());
         String salida = "";
         for (Text val : values){
        	  numero = numero + Integer.parseInt(val.toString().split("@")[2]);
        	  salida = salida + "," + val.toString() ;
         }
       salida = "," + salida.substring(1) + "," + String.valueOf(numero);
    	context.write(llave, salida);
    	numero = 0;
    }
 }