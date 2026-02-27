import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ConsolaPanel extends JPanel {

    private JTextPane console;
    private StyledDocument doc;
    private String actualRoute;
    private ComandLogic comandLogic;

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
            
            String prompt = actualRoute + ">";
            String comando = "";
            if (ultimaLinea.startsWith(prompt)) {
                comando = ultimaLinea.substring(prompt.length()).trim();
            }
            
            append("\n");
            
            manejarComando(comando);
            
            imprimirPrompt();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void manejarComando(String comando) {
        if (comando.trim().isEmpty()) {
            return;
        }
        
        String[] partes = comando.split(" ");
        String comandoPrincipal = partes[0].toLowerCase();
        String[] argumentos = new String[partes.length - 1];
        if (argumentos.length > 0) {
            System.arraycopy(partes, 1, argumentos, 0, argumentos.length);
        }
        
        switch (comandoPrincipal) {
            case "cd":
                if (argumentos.length == 0 || argumentos[0].isEmpty()) {
                    append("Uso: cd <nombre carpeta>\n");
                    break;
                }
                String resultado = comandLogic.cmdCd(argumentos[0]);
                if (resultado.isEmpty()) {
                    actualRoute = comandLogic.getRuta();
                } else {
                    append(resultado + "\n");
                }
                break;
            case "dir":
                append(comandLogic.cmdDir().toLowerCase());
                break;
            case "date":
                comandLogic.cmdDate().toLowerCase();
                break;
            case "hora":
                comandLogic.cmdHora().toLowerCase();
                break;
            case "mkdir":
                comandLogic.cmdMkdir(argumentos[0].toLowerCase());
                break;
            case "mfile":
                comandLogic.cmdMfile(argumentos[0].toLowerCase());
                break;
            case "rm":
                comandLogic.cmdRm(argumentos[0].toLowerCase());
                break;
            case "rd":
        }
    }
}