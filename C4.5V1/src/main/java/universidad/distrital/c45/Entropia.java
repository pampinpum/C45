package universidad.distrital.c45;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import universidad.distrital.c45.estructura.Atributo;
import universidad.distrital.c45.estructura.ValorAtributo;
import universidad.distrital.c45.estructura.ValorClase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Clase principal para calcular la entropia de los atributos
 *
 * @author nicolas
 */
public class Entropia {
    private int totalAtributos = 0;
    double valorGananciaMin;

    public Entropia(double valorGanancia) {
        valorGananciaMin = valorGanancia;
    }

    /**
     * Método para procesar la entropia
     *
     * @param directorio ruta con la salida del Reduce ConcatenaValores
     * @param conf       configuración de la tarea de Hadoop
     * @throws IOException excepción de lectura
     * @return regresa el atributo con la mejor ganancia de entropia
     */
    public Atributo calcularEntropia(Path directorio, Configuration conf) throws IOException {
        return elegirMejorAtributo(crearAtributos(directorio, conf));
    }

    /**
     * Método que ordena los atributos en estruturas
     *
     * @param directorio ruta con la salida del Reduce ConcatenaValores
     * @param conf       configuración de la tarea de Hadoop
     * @throws IOException excepción de lectura
     * @return regresa el atributo con la mejor ganancia de entropia
     */
    private ArrayList<Atributo> crearAtributos(Path directorio, Configuration conf) throws IOException {
        ArrayList<Atributo> listaAtributos = new ArrayList<Atributo>();
        //abre el archivo .txt para su lectura
        Path pt = directorio;
        FileSystem fs = FileSystem.get(conf);
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fs.open(pt)))) {
            String linea = bufferedReader.readLine();
            //mientras existan valores se itera para llenar la estructura de datos
            while (linea != null) {
                Atributo atributo = new Atributo();
                ArrayList<ValorAtributo> valoresAtributo = new ArrayList<>();
                String[] cadena = linea.split(",");
                atributo.idAtributo = cadena[0].replace("	", "");
                totalAtributos = Integer.parseInt(cadena[cadena.length - 1]);
                ValorAtributo valorAtributo;
                for (int i = 1; i <= cadena.length - 2; i++) {
                    String[] subcadena = cadena[i].split("@");
                    int indice = verificarValorAtributo(valoresAtributo, subcadena[0]);
                    if (indice == -1) {
                        valorAtributo = new ValorAtributo();
                        ValorClase valorClase = new ValorClase();
                        valorClase.apariciones = Integer.parseInt(subcadena[2]);
                        valorClase.clase = subcadena[1];
                        valorAtributo.valores.add(valorClase);
                        valorAtributo.nombre = subcadena[0];
                        valoresAtributo.add(valorAtributo);
                    } else {
                        valorAtributo = valoresAtributo.get(indice);
                        ValorClase valorClase = new ValorClase();
                        valorClase.apariciones = Integer.parseInt(subcadena[2]);
                        valorClase.clase = subcadena[1];
                        valorAtributo.valores.add(valorClase);
                    }
                }
                atributo.valorAtributos = valoresAtributo;
                listaAtributos.add(atributo);
                linea = bufferedReader.readLine();
            }
            return listaAtributos;
        }
    }

    /**
     * Elige el atributo con la mejor entropia en caso de que no se cumpla regresa los valores de clase
     *
     * @param listaAtributos lista de atributos a comparar la entropia generada
     * @return objeto del tipo atributo
     */
    private Atributo elegirMejorAtributo(ArrayList<Atributo> listaAtributos) {
        procesarAtributos(listaAtributos);
        double entropiaMenor = -1;
        //si el valor de la ganancia es menor a la constante se descarta como opción valida
        //double valorGananciaMin = 0.1;
        int indiceAtributo = -1;
        for (int i = 0; i < listaAtributos.size() - 1; i++) {
            if (listaAtributos.get(i).entropiaAtributo < entropiaMenor || entropiaMenor < 0) {
                entropiaMenor = listaAtributos.get(i).entropiaAtributo;
                indiceAtributo = i;
            }
        }
        //Si la ganancia es menor al valor dado regresa el valor de clase
        if (listaAtributos.get(listaAtributos.size() - 1).entropiaAtributo - entropiaMenor <= valorGananciaMin) {
            return listaAtributos.get(listaAtributos.size() - 1);
        }
        return listaAtributos.get(indiceAtributo);
    }

    /**
     * Itera la lista de atributos
     *
     * @param listaAtributos ArrayList de los atribuos a iterar
     */
    private void procesarAtributos(ArrayList<Atributo> listaAtributos) {
        Iterator<Atributo> iteradorLista = listaAtributos.iterator();
        while (iteradorLista.hasNext()) {
            Atributo atributoTemp = iteradorLista.next();
            ArrayList<ValorAtributo> tempValorAtributo = atributoTemp.valorAtributos;
            Iterator<ValorAtributo> iteradorAtributo = tempValorAtributo.iterator();
            while (iteradorAtributo.hasNext()) {
                calcularTotal(iteradorAtributo.next());
            }
            atributoTemp.entropiaAtributo = calcularGanancia(tempValorAtributo);
        }
    }

    /**
     * Cuenta el número de apariciones de cada valor de atributo
     *
     * @param valorAtributo valor atributo a contabilizar
     */
    private void calcularTotal(ValorAtributo valorAtributo) {
        double total = 0;
        Iterator<ValorClase> iteradorValor = valorAtributo.valores.iterator();
        while (iteradorValor.hasNext()) {
            total = total + iteradorValor.next().apariciones;
        }
        valorAtributo.totalApariciones = total;
    }

    /**
     * Calcula la ganancia de entropia
     *
     * @param valoresAtributo ArrayList de valrosAtributo que puede tomar cada atributo
     * @return ganancia del valor de atributo
     */
    private double calcularGanancia(ArrayList<ValorAtributo> valoresAtributo) {
        Iterator<ValorAtributo> iteradorValorAtributo = valoresAtributo.iterator();
        double ganancia = 0;
        while (iteradorValorAtributo.hasNext()) {
            ValorAtributo valorAtributo = iteradorValorAtributo.next();
            Iterator<ValorClase> iteradorValorClase = valorAtributo.valores.iterator();
            double entropia = 0;
            while (iteradorValorClase.hasNext()) {
                ValorClase vClase = iteradorValorClase.next();
                //formula par calcular la entropia
                entropia = entropia + ((Math.log(vClase.apariciones / valorAtributo.totalApariciones)) / Math.log(2)) * (-vClase.apariciones / valorAtributo.totalApariciones);
            }
            //parte de la formula para calcular la entropia
            entropia = entropia * (valorAtributo.totalApariciones / totalAtributos);
            ganancia = ganancia + entropia;
        }
        return ganancia;
    }

    /**
     * Verifica si en la lista existe el elemento ValorAtributo
     *
     * @param valoresAtributo ArrayList con los valroes de atributo de la clase
     * @param nombre          nombre del ValorAtributo a buscar
     * @return indice del ValorAtributo || -1 en caso de que no exista
     */
    private int verificarValorAtributo(ArrayList<ValorAtributo> valoresAtributo, String nombre) {
        int indice = -1;
        for (int i = 0; i < valoresAtributo.size(); i++) {
            ValorAtributo valorAtributo = valoresAtributo.get(i);
            if (valorAtributo.nombre.equals(nombre)) {
                indice = i;
            }
        }
        return indice;
    }
}
