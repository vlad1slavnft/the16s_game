import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame {
    public static final int windowWidth = 1280;
    public static final int windowHeight = 835;

    public Main(){
        setTitle("The 16S game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(windowWidth, windowHeight);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - windowWidth)/2, (Toolkit.getDefaultToolkit().getScreenSize().height - windowHeight)/2);
        add(new GameField());
        setVisible(true);
    }

    public static void main(String[] args) {
        Main mw = new Main();
    }
}
