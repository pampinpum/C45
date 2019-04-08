package universidad.distrital.c45;


import java.io.File;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Log {
    
    //--------------------------------------------------------------------------
    // Atributo de clase
    //--------------------------------------------------------------------------
    
    private Logger logger;
    
    //--------------------------------------------------------------------------
    // Solitario
    //--------------------------------------------------------------------------
    
    private static Log log;
    
    /**
     * Devuelve una instancia única de la clase que implementa el Log de la apliación
     * @return instancia única del Log de la aplicación
     */
    public static synchronized Log getInstance() 
    {
        if (log == null)
            log = new Log();
        
        return log;
    }//getInstance
    
    //--------------------------------------------------------------------------
    // Constructor de clase
    //--------------------------------------------------------------------------
        
    /**
     * Constructor por defecto
     */
    private Log() 
    {        
        try 
        {   
        	//Path ruta = new Path("/");
            //File carpetaLog = new File("/home/nicolas/C4.5V1/log");
        	File carpetaLog = new File("./log");
            if (!carpetaLog.exists())
                carpetaLog.mkdirs();
            
            //String rutaYNombreLog = "/home/nicolas/C4.5V1/log" + "/logHadoop";
            String rutaYNombreLog = "./log" + "/logHadoop";
            logger = Logger.getLogger(rutaYNombreLog);
            logger.setLevel(Level.ALL);
            Calendar fechaActual = new GregorianCalendar();
            String fecha = fechaActual.get(Calendar.DATE)+"-"+(fechaActual.get(Calendar.MONTH)+1)+"-"+fechaActual.get(Calendar.YEAR);
            rutaYNombreLog += "_" + fecha + ".log";
            FileHandler handler = new FileHandler(rutaYNombreLog, true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } 
        catch (Exception e) 
        {
            throw new IllegalStateException("Ocurrió un error creando el log de la aplicación: "+e.getMessage());
        }
    }//Logs
    
    //--------------------------------------------------------------------------
    // Métodos implementados
    //--------------------------------------------------------------------------
    
    /**
     * Adiciona un mensaje informativo al log
     * @param mensaje Mensaje informativo que se adicionará al log
     */
    public void info(String mensaje)
    {
        logger.info(mensaje);        
    }//info
    
    /**
     * Adiciona un mensaje de error al log a partir de un mensaje
     * @param mensaje Mensaje de error que se adicionará al log
     */
    public void severe(String mensaje)
    {
        logger.severe(mensaje);
    }//severe
    
    /**
     * Adiciona un mensaje de error al log a partir de una exepción
     * @param excepcion Excepción con la traza del error
     */
    public void severe(Exception excepcion)
    {
        if (excepcion == null)
            throw new IllegalArgumentException("La excepción no puede ser nula.\n");
        
        logger.severe(excepcion.getMessage()+"\nDescripción técnica de la excepción: \n"); //+obtenerExcepcionConStackTrace(excepcion));
    }//severe
  
    /**
     * Adiciona un mensaje informativo del inicio de la ejecucion de un método al alog
     * @param metodo Nombre del método
     * @param clase Clase a la cual pertenece el método que se ejecuta
     */
    public void infoEjecucionMetodo(String metodo, Class<?> clase) 
    {
      //  if (ESCRIBIR_TRAZA_METODOS_EN_LOG)
            logger.info(metodo);
    }//infoEjecucionMetodo
    
    public void infoEjecucionHora(String mensaje, Class<?> clase) 
    {
    	Date date = new Date();
            logger.info(mensaje + " " + date.toString());
    }//infoEjecucionMetodo
    public void infoResultado(String metodo, Class<?> clase) 
    {
    	
    	logger.info(metodo);
    	
    }//infoEjecucionMetodo
  
}