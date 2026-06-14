package es.upm.pproject.sokoban.view;

import es.upm.pproject.sokoban.models.*;

import javax.swing.JFrame;

public class Window extends JFrame {

    private Panel panel;

    public Window() {
        panel = new Panel();
        
        // Add the panel into the window
        this.add(panel);
        
        this.setTitle("Sokoban"); // Window title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To close the window clicking 'X'
        this.setResizable(false); // The user cannot resize the window
        
        // Adapt the size of the window to the panel size and centers if
        this.pack();
        this.setLocationRelativeTo(null);
        
        // Make the window visible
        this.setVisible(true);
    }

    public void setBoard(Board board){
        panel.setBoard(board);
    }
}
