package ui;
import Consola.ComandLogic;
import Consola.Paneles;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class MainFrame extends JFrame {  
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private ComandLogic comandLogic;
    public MainFrame() { 
        comandLogic = new ComandLogic();
        setTitle("Administrator: Command Prompt");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        ConsolaPanel consolaPanel = new ConsolaPanel(comandLogic);
        mainPanel.add(consolaPanel, Paneles.CONSOLA.name());
        add(mainPanel);

        cardLayout.show(mainPanel, Paneles.CONSOLA.name());
    }
}
