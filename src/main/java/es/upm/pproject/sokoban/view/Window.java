package es.upm.pproject.sokoban.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;

public class Window extends JFrame {

    private Panel panel;
    private JLabel level;
    private int currentLevel;

    public Window() {
        panel = new Panel(this);
        
        // Add the panel into the window
        this.add(panel);
        
        this.setTitle("Sokoban"); // Window title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To close the window clicking 'X'
        this.setResizable(false); // The user cannot resize the window
        
        // Adapt the size of the window to the panel size and centers if
        this.pack();
        this.setLocationRelativeTo(null);
        
        // Add a border layout
        setLayout(new BorderLayout());

        // Create the text tag
        level = new JLabel(" ", JLabel.CENTER);
        level.setFont(new Font("Serif", Font.PLAIN, 18));

        add(level, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // Make the window visible
        this.setVisible(true);
    }

    public Panel getPanel() {
        return panel;
    }

    public int getLevel() {
        return currentLevel;
    }

    public void updateSteps(int newStep) {
        if (currentLevel > 0) {
            level.setText("Level: " + currentLevel + "  Steps: " + newStep);
        }
        else {
            level.setText(" ");
        }
    }

    public void newLevel(int newLevel) {
        currentLevel = newLevel;
        if (currentLevel > 0) {
            level.setText("Level: " + currentLevel + "  Steps: 0");
        }
        else {
            level.setText(" ");
        }
    }
}
