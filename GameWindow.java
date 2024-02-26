import java.awt.Color;
import java.awt.event.*;
import javax.swing.JFrame;

public class GameWindow extends JFrame{

    public GameWindow (String title, GamePanel panel) {
        this.setTitle(title);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }
}
