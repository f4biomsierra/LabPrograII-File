/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consola;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.io.FileReader;

public class ComandLogic {

    private File ruta = null;

    public ComandLogic() {
        ruta = new File(System.getProperty("user.home"));
    }

    public String cmdMkdir(String nombre) {
        if (nombre.isEmpty()) {
            return "Uso: MKdir <nombre>";
        }
        File dir = new File(ruta, nombre);
        if (dir.mkdir()) {
            return "Carpeta " + nombre + " creada exitosamente.";
        }

        return "No se pudo crear carpeta: " + nombre + ". Revisar si la carpeta ya existe.";

    }

    public String cmdMfile(String nombre) {
        if (nombre.isEmpty()) {
            return "Uso: MKdir <nombre.ext>";
        }
        File file = new File(ruta, nombre);
        try {
            if (file.createNewFile()) {
                return "Archivo " + nombre + " creado exitosamete.";
            } else {
                return "Archivo " + nombre + " ya existe.";
            }

        } catch (IOException e) {
            return "Error al crear el Archivo:" + e.getMessage();
        }
    }

    public String cmdRm(String nombre) {
        if (nombre.isEmpty()) {
            return "Uso: Rm <nombre>";
        }
        File deleted = new File(ruta, nombre);
        if (deleteRecursive(deleted)) {
            return nombre + " eliminado exitosamete.";
        } else {
            return nombre + " no se pudo eliminar.";
        }

    }

    private boolean deleteRecursive(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteRecursive(f);
                }
            }
        }
        return file.delete();
    }

    public String cmdCd(String nombre) {
        if (nombre.isEmpty()) {
            return "Uso: Cd <nombre carpeta>";
        }

        if (nombre.equals("..")) {
            return cmdBack();
        }

        File newRuta = new File(ruta, nombre);

        try {
            File canonicalRuta = newRuta.getCanonicalFile();

            if (canonicalRuta.exists() && canonicalRuta.isDirectory()) {
                ruta = canonicalRuta;
                return "";
            } else {
                return "No se encontro la carpeta " + nombre + ".";
            }
        } catch (IOException e) {
            return "No se encontro la carpeta " + nombre + ".";
        }
    }

    public String cmdBack() {
        File parent = ruta.getParentFile();
        if (parent != null) {
            ruta = parent;
            return "";
        }
        return "Ya se encuentra en el directorio raiz.";
    }

    public String cmdDir() {
        String contenido = "";
        File[] lista = ruta.listFiles();
        if (lista != null) {
            for (File files : lista) {
                contenido = contenido + (files.isDirectory() ? "[DIR] " : "[FILE] ") + files.getName() + "\n";
            }
        }
        return contenido;
    }

    public String cmdDate() {
        Date fechaObjeto = new Date();
        return fechaObjeto.toString();
    }

    public String cmdHora() {
        Date horaObjeto = new Date();
        String horaCompleta = horaObjeto.toString();
        String soloHora = horaCompleta.substring(11, 19);
        return soloHora;
    }

    public String escribirWr(String nombre, String texto) {
        File archivo = new File(ruta, nombre);
        try {
            FileWriter escritor = new FileWriter(archivo, true);
            escritor.write(texto + System.lineSeparator());
            escritor.close();
            return "";
        } catch (IOException e) {
            return "Error al escribir: " + e.getMessage();
        }
    }

    public String leerRd(String nombre) {
        File archivo = new File(ruta, nombre);
        String contenidoArchivo = "";
        try {
            FileReader lector = new FileReader(archivo);
            BufferedReader buffer = new BufferedReader(lector);
            String linea;
            while ((linea = buffer.readLine()) != null) {
                contenidoArchivo = contenidoArchivo + linea + "\n";
            }
            buffer.close();
            lector.close();
        } catch (IOException e) {
            return "Error: No se pudo leer el archivo.";
        }
        return contenidoArchivo;
    }

    public boolean verificarExistencia(String nombre) {
        File archivo = new File(ruta, nombre);
        return archivo.exists();
    }

    public String getRuta() {
        return ruta.getAbsolutePath();
    }
}
