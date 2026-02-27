
import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fabio Sierra
 */
public class ComandLogic {
    
    public String cmdMkdir(String nombre){
        if(nombre.isEmpty())
            return "Uso: MKdir <nombre>";
        File dir=new File(ruta, nombre);
        if(dir.mkdir())
            return "Carpeta "+nombre+" creada exitosamente.";
        
        return "No se pudo crear carpeta: "+nombre;
        
    }
    
    
    
    
    
    
    
    
    
 
   
}
