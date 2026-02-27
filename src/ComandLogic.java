/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;

public class ComandLogic {
    private File ruta=null;
    
    public void setFile(){
        ruta=new File(System.getProperty("user.home"));
    }
    
    public String cmdDir(){
        String resultado="";
        File[] lista=ruta.listFiles();
        if(lista!=null){
            for(File file: lista){
                String prefijo = file.isDirectory() ? "<DIR> " : "      ";
                resultado = resultado + prefijo + file.getName() + "\n";
            }
        }
        return resultado;
    }
    
    public String cmdDate(){
        Date fechaObjeto = new Date();
        return fechaObjeto.toString();
    }
    
    public String cmdHora(){
        Date horaObjeto = new Date();
        String horaCompleta = horaObjeto.toString();
        String soloHora = horaCompleta.substring(11, 19);
        return soloHora;
    }
    
    public void escribirWr(String nombre, String texto){
        File archivo = new File(ruta, nombre);
        try{
            FileWriter escritor=new FileWriter(archivo, true);
            escritor.write(texto + System.lineSeparator());
            escritor.close();
        } catch(IOException e){
            System.out.println("Error al escribir: " + e.getMessage());
        }
    }
    
    public String leerRd(String nombre){
        File archivo = new File(ruta, nombre);
        String contenidoArchivo = "";
        try{
            FileReader lector = new FileReader(archivo);
            BufferedReader buffer = new BufferedReader(lector);
            String linea;
            while((linea=buffer.readLine())!=null){
                contenidoArchivo=contenidoArchivo+linea+"\n";
            }
            buffer.close();
            lector.close();
        } catch(IOException e){
            return "Error: No se pudo leer el archivo.";
        }
        return contenidoArchivo;
    }
    
    public boolean verificarExistencia(String nombre){
        File archivo = new File(ruta, nombre);
        return archivo.exists();
    }
    
}
