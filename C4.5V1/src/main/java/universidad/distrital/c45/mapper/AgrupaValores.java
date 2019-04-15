package universidad.distrital.c45.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Agrupa los valores (valor atributo, valor clase, apariciones) en una sola cadena separa por arroba "@"
 *
 * @author nicolas
 */
public class AgrupaValores extends Mapper<LongWritable, Text, Text, Text> {
    static private Text llave = new Text();
    static private Text valor = new Text();

    //Agrupa por el atributo la contabilizacion de los mismos
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        var linea = value.toString();
        var tokenizer = new StringTokenizer(linea, "\r");
        while (tokenizer.hasMoreTokens()) {
            linea = tokenizer.nextToken();
            String[] cadena = linea.split(",");
            llave.set(cadena[0]);
            valor.set(cadena[1] + "@" + cadena[2] + "@" + cadena[cadena.length - 1].replace("	", ""));
            context.write(llave, valor);
        }
    }
}