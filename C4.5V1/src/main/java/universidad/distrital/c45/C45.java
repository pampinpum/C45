package universidad.distrital.c45;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.log4j.BasicConfigurator;
import universidad.distrital.c45.estructura.Atributo;
import universidad.distrital.c45.estructura.Nodo;
import universidad.distrital.c45.estructura.ValorAtributo;
import universidad.distrital.c45.mapper.AgrupaValores;
import universidad.distrital.c45.mapper.MapearAtributos;
import universidad.distrital.c45.reducer.ConcatenaValores;
import universidad.distrital.c45.reducer.SumarAtributos;
        
/**
 * Clase principal del algotimo
 * @author nicolas
 *
 */
public class C45 {
              
 public static void main(String[] args) throws Exception {
 	BasicConfigurator.configure();
	Date date = new Date();
    String empece = "empecé a esta hora " + date.toString();
	Log.getInstance().infoEjecucionHora("empecé a esta hora", C45.class);
	C45 algoritmo = new C45();
	Nodo raiz = new Nodo();
	raiz.valor = "raiz";
	String inp = new String(args[0]);
	//inp = "";
	String out = new String(args[1]);
	String nombreArchivo = new String(args[2]);
	double valorGananciaMin = new Double(args[3]);
    algoritmo.calcularC45(inp, out, nombreArchivo, raiz, valorGananciaMin);    
	Date date2 = new Date();
	Long segundos = (date2.getTime() - date.getTime())/1000;
	Log.getInstance().infoEjecucionHora("terminé a esta hora" + " duración " + segundos.toString(), C45.class);
    String termine = "terminé a esta hora " + date.toString();
    leerArbol(raiz);
	//EMail e = new EMail();
	//e.enviar(empece, termine, segundos.toString(), nombreArchivo); 
 }
 
 /**
  * Lee el árbol generado después de finalizado el proceso de generación
  * @param arbol árbol a leer
  */
 public static void leerArbol(Nodo arbol){
	
	 for(int i = 0; i<arbol.hijos.size();i++){
		 Log.getInstance().infoResultado("ID ATRIBUTO: " + arbol.hijos.get(i).id + " VALOR: " + arbol.hijos.get(i).valor, C45.class);
		 //System.out.println("ID ATRIBUTO: " + arbol.id + " VALOR: " + arbol.hijos.get(i).valor);
		 leerArbol(arbol.hijos.get(i));
	 }
 }

 /**
  * Crea un archivo .txt con las subcadenas del atributo con mejor entropia 
  * @param directorio ruta del archivo .txt
  * @param conf configuración del hadoop
  * @param valorAtributo atributo ganador
  * @param idAtributo identificador del atributo
  * @throws IOException excepción de lectura
  */
 public void crearTxtAtributos(Path directorioLectura, String path, Configuration conf, ValorAtributo valorAtributo, String idAtributo) throws IOException{
	 Path salida = new Path(path + valorAtributo.nombre + ".txt");
	 Path pt=directorioLectura;
	 FileSystem fs = FileSystem.get(conf);
	 BufferedWriter br1=new BufferedWriter(new OutputStreamWriter(fs.create(salida, true)));
	 BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
	 try {
	   String line;
	   line=br.readLine();
	   while (line != null){		  
	   String[] cadena = line.split(","); 
	   if (cadena[Integer.parseInt(idAtributo)].equals(valorAtributo.nombre)){
		   String linea = "";
		   for(int i = 0; i<cadena.length;i++){
			   if(cadena[i].equals(valorAtributo.nombre)){
			   }else{
				   linea = linea + cadena[i] + ",";
			   }
		   }
		   linea = linea.substring(0, linea.length()-1) + "\n";
		   br1.write(linea);          
	   }
	   line = br.readLine();
	   }
	   br1.close();
	 } finally {
	   br.close();
	 }
 }
 
 /**
  * Rutina principal ejecuta las operaciones Map y Reduce que clasifican los atributos 
  * @param rutaOrigen archivo original .txt con los valores de clase
  * @param nodo nodo al que se va asociar el atributo salida para construir el árbol
  * @throws IllegalArgumentException excepción de atributos
  * @throws IOException excepción de lectura
  * @throws ClassNotFoundException excepción encontrando la clase
  * @throws InterruptedException excepción de rutina terminada de manera subita 
  */
 public void calcularC45(String rutaImput, String rutaOut, String archivo, Nodo nodo, double valorGananciaMin) throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException{
	   //TRABAJO 1 clasifica la entrada dada en el atributo, el valor del atributo y la clase asociada
	 //TODO debo cmabiar la forma del input y el output para que me sirva en la forma recursiva y además recogiendo los argunmentos
	   Job job = Job.getInstance();
	   job.setJobName("OrdenaAtributos");
	   job.setOutputKeyClass(Text.class);
	   job.setOutputValueClass(IntWritable.class);	         
	   job.setMapperClass(MapearAtributos.class);
	   job.setReducerClass(SumarAtributos.class);	         
	   job.setInputFormatClass(TextInputFormat.class);
	   FileInputFormat.addInputPath(job, new Path(rutaImput + archivo));
	   FileOutputFormat.setOutputPath(job, new Path(rutaOut));
	   FileSystem fs = FileSystem.get(job.getConfiguration());
	   fs.delete(new Path(rutaOut), true);
	   job.setJarByClass(C45.class);
	   job.waitForCompletion(true);
	     
	   //TRABAJO 2 Ordena los valores de los atributos para calcular la entropia
	   Job job2 = Job.getInstance();  
	   job2.setJobName("PreProcesoAtributos");
	   job2.setOutputKeyClass(Text.class);
	   job2.setOutputValueClass(Text.class);	         
	   job2.setMapperClass(AgrupaValores.class);
	   job2.setReducerClass(ConcatenaValores.class);  
	   job2.setInputFormatClass(TextInputFormat.class);
	   FileInputFormat.addInputPath(job2, new Path(rutaOut));
	   FileOutputFormat.setOutputPath(job2, new Path(rutaOut + "2"));
	   fs = FileSystem.get(job2.getConfiguration());
	   fs.delete(new Path(rutaOut + "2"), true);
	   job2.setJarByClass(C45.class);
	   job2.waitForCompletion(true);	   	   
	   //Se calcula la entropia
	   Entropia ent = new Entropia(valorGananciaMin);
	   //la ruta es la de la salida del segundo trabajo de Hadoop
	   Atributo atributo = ent.calcularEntropia(new Path(rutaOut + "2/part-r-00000"), job2.getConfiguration());
	  if(atributo.idAtributo.equals("total")){
		  //Si se llega al final de los atributos se agregan las clases asociadas
		   Nodo nodoHijo = new Nodo();
		   nodoHijo.id = atributo.idAtributo;
		   nodoHijo.valor = atributo.valorAtributos.get(0).valores.get(0).clase;
		   nodo.hijos.add(nodoHijo);  
		   //Log.getInstance().infoEjecucionMetodo("sali " + nodoHijo.id, C45.class);
	   }else{
		   //Llamado recursivo del calculo de la entropia
		   ArrayList<ValorAtributo> valorAtributos = atributo.valorAtributos;
		   for(int i = 0; i<valorAtributos.size();i++){
			   Nodo nodoHijo = new Nodo();
			   nodoHijo.id = atributo.idAtributo;
			   nodoHijo.valor = valorAtributos.get(i).nombre;			  
			   nodo.hijos.add(nodoHijo);
			   //Log.getInstance().infoEjecucionMetodo(("el siguiente atributo es " + atributo.idAtributo + " " + valorAtributos.get(i).nombre), C45.class);
			   crearTxtAtributos(new Path(rutaImput + archivo), rutaImput, job2.getConfiguration(), valorAtributos.get(i), atributo.idAtributo);
			   calcularC45(rutaImput, rutaOut, valorAtributos.get(i).nombre + ".txt", nodoHijo, valorGananciaMin);
			   fs.delete(new Path(rutaImput + valorAtributos.get(i).nombre + ".txt"), true);
		   		}
	   }
 }        
}
