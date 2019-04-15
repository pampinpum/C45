package universidad.distrital.c45.reducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
      
/**
 * Cuenta el número de veces que se asocia el valor de atributo al valor de clase y las adiciona al contexto
 * @author nicolas
 *
 */
 public  class SumarAtributos extends Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, Iterable<IntWritable> values, Context context) 
      throws IOException, InterruptedException {    	
    	  int sumar = 0;          	 	
          for (IntWritable val : values) {
              sumar += val.get();
          }    
          //Regresa el atributo su valor, el valor de clase asociado y el numero de veces que se repite
          context.write(key, new IntWritable(sumar));         
        }
 }
 
 