import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ConsolaPanel extends JPanel {

    private JTextPane console;
    private StyledDocument doc;
    private String actualRoute;

    public ConsolaPanel() {
        setLayout(new java.awt.BorderLayout());
        console = new JTextPane();
        console.setBackground(Color.BLACK);
        console.setForeground(Color.WHITE);
        console.setCaretColor(Color.WHITE);
        console.setFont(new Font("Consolas", Font.PLAIN, 14));

        doc = console.getStyledDocument();

        JScrollPane scroll = new JScrollPane(console);
        scroll.setBorder(null);
        add(scroll, java.awt.BorderLayout.CENTER);

        actualRoute = "C:\\Program Files\\Java";

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
        // Por ahora solo hace return para simular que se ejecutó el comando
        return;
    }
}