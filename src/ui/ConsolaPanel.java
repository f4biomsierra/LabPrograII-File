package ui;
import javax.swing.*;
import javax.swing.text.*;

import Consola.ComandLogic;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ConsolaPanel extends JPanel {

    private JTextPane console;
    private StyledDocument doc;
    private String actualRoute;
    private ComandLogic comandLogic;
    private int promptStartPosition = 0;
    private boolean modoEscritura = false;
    private String archivoEscritura = "";
    private int ultimaPosicionEscritura = 0;

    public ConsolaPanel(ComandLogic comandLogic) {
        this.comandLogic = comandLogic;
        setLayout(new java.awt.BorderLayout());
        actualRoute = comandLogic.getRuta();
        System.out.println(actualRoute);
        console = new JTextPane();
        console.setBackground(Color.BLACK);
        console.setForeground(Color.WHITE);
        console.setCaretColor(Color.WHITE);
        console.setFont(new Font("Consolas", Font.PLAIN, 14));

        doc = console.getStyledDocument();
        
        ((AbstractDocument) doc).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (offset >= promptStartPosition) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                if (offset >= promptStartPosition) {
                    super.remove(fb, offset, length);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (offset >= promptStartPosition) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(console);
        scroll.setBorder(null);
        add(scroll, java.awt.BorderLayout.CENTER);

        console.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    ejecutarComando();
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    int caretPos = console.getCaretPosition();
                    if (caretPos <= promptStartPosition) {
                        e.consume();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    int caretPos = console.getCaretPosition();
                    if (caretPos < promptStartPosition) {
                        e.consume();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    int caretPos = console.getCaretPosition();
                    if (caretPos <= promptStartPosition && e.isControlDown() == false) {
                        e.consume();
                        console.setCaretPosition(promptStartPosition);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_HOME) {
                    e.consume();
                    console.setCaretPosition(promptStartPosition);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        imprimirHeader();
        imprimirPrompt();
    }

    private void imprimirHeader() {
        append("Microsoft Windows [Version 10.0.22621.521]\n");
        append("(c) Microsoft Corporation. All rights reserved.\n\n");
    }

    private void imprimirPrompt() {
        append(actualRoute + ">");
        promptStartPosition = doc.getLength();
        console.setCaretPosition(doc.getLength());
    }

    private void append(String text) {
        try {
            doc.insertString(doc.getLength(), text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public String getActualRoute() {
        return actualRoute;
    }

    public void setActualRoute(String newRoute) {
        this.actualRoute = newRoute;
    }

    private void ejecutarComando() {
        try {
            String textoCompleto = doc.getText(0, doc.getLength());
            String[] lineas = textoCompleto.split("\n");
            String ultimaLinea = lineas[lineas.length - 1];

            if (modoEscritura) {
                int posicionActual = doc.getLength();
                String textoNuevo = doc.getText(ultimaPosicionEscritura, posicionActual - ultimaPosicionEscritura);
                String textoIngresado = textoNuevo.trim();
                
                append("\n");
                if (textoIngresado.equals("EXIT")) {
                    modoEscritura = false;
                    append("Escritura finalizada.\n");
                    promptStartPosition = doc.getLength();
                } else if (!textoIngresado.isEmpty()) {
                    comandLogic.escribirWr(archivoEscritura, textoIngresado);
                }
                ultimaPosicionEscritura = doc.getLength();
                if (!modoEscritura) {
                    imprimirPrompt();
                }
                console.setCaretPosition(doc.getLength());
                return;
            }

            String prompt = actualRoute + ">";
            String comando = "";
            if (ultimaLinea.startsWith(prompt)) {
                comando = ultimaLinea.substring(prompt.length()).trim();
            } else {
                // Si no empieza con el prompt, intentar extraer el comando de otra forma
                // Buscar el último ">" y tomar lo que viene después
                int indicePrompt = ultimaLinea.lastIndexOf(">");
                if (indicePrompt >= 0 && indicePrompt < ultimaLinea.length() - 1) {
                    comando = ultimaLinea.substring(indicePrompt + 1).trim();
                } else {
                    comando = ultimaLinea.trim();
                }
            }
            
            append("\n");
            
            manejarComando(comando);
            
            if (!modoEscritura) {
                imprimirPrompt();
            } else {
                promptStartPosition = doc.getLength();
            }
            console.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void manejarComando(String comando) {
        if (comando.trim().isEmpty()) {
            return;
        }
        
        String[] partes = comando.split(" ", 2);
        String comandoPrincipal = partes[0].trim();
        String argumento = partes.length > 1 ? partes[1].trim() : "";
        
        switch (comandoPrincipal) {
            case "Cd":
                if (argumento.isEmpty()) {
                    append("Uso: Cd <nombre carpeta>\n");
                    break;
                }
                String resultadoCd = comandLogic.cmdCd(argumento);
                if (resultadoCd.isEmpty()) {
                    actualRoute = comandLogic.getRuta();
                } else {
                    append(resultadoCd + "\n");
                }
                break;
            case "...":
                String resultadoBack = comandLogic.cmdBack();
                if (resultadoBack.isEmpty()) {
                    actualRoute = comandLogic.getRuta();
                } else {
                    append(resultadoBack + "\n");
                }
                break;
            case "Dir":
                append(comandLogic.cmdDir());
                break;
            case "Date":
                append(comandLogic.cmdDate() + "\n");
                break;
            case "Time":
                append(comandLogic.cmdHora() + "\n");
                break;
            case "Mkdir":
                if (argumento.isEmpty()) {
                    append("Uso: Mkdir <nombre>\n");
                    break;
                }
                append(comandLogic.cmdMkdir(argumento) + "\n");
                break;
            case "Mfile":
                if (argumento.isEmpty()) {
                    append("Uso: Mfile <nombre.ext>\n");
                    break;
                }
                append(comandLogic.cmdMfile(argumento) + "\n");
                break;
            case "Rm":
                if (argumento.isEmpty()) {
                    append("Uso: Rm <nombre>\n");
                    break;
                }
                append(comandLogic.cmdRm(argumento) + "\n");
                break;
            case "Wr":
                if (argumento.isEmpty()) {
                    append("Uso: Wr <archivo.ext>\n");
                    break;
                }
                if (!comandLogic.verificarExistencia(argumento)) {
                    append("El archivo '" + argumento + "' no existe.\n");
                    break;
                }
                archivoEscritura = argumento;
                modoEscritura = true;
                append("Escribiendo en " + argumento + ". Escriba EXIT para terminar.\n");
                // Establecer la posición DESPUÉS del mensaje para que no se guarde en el archivo
                ultimaPosicionEscritura = doc.getLength();
                promptStartPosition = doc.getLength();
                break;
            case "Rd":
                // Asegurar que no estamos en modo escritura
                modoEscritura = false;
                if (argumento.isEmpty()) {
                    append("Uso: Rd <archivo.ext>\n");
                    break;
                }
                if (!comandLogic.verificarExistencia(argumento)) {
                    append("El archivo '" + argumento + "' no existe.\n");
                    break;
                }
                String contenido = comandLogic.leerRd(argumento);
                if (contenido != null && !contenido.isEmpty()) {
                    append(contenido);
                } else {
                    append("El archivo está vacío.\n");
                }
                break;
            default:
                append("'" + comando + "' no se reconoce como un comando.\n");
                break;
        }
    }
}