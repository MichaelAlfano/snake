package snake;

import javax.swing.*;
import java.awt.*;

public class RunSnake implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Snake");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset (r)");
        reset.addActionListener(e -> board.reset());
        reset.setFocusable(false);
        control_panel.add(reset);

        final JButton pausePlay = new JButton("Pause/Play (space)");
        pausePlay.addActionListener(e -> board.togglePause());
        pausePlay.setFocusable(false);
        control_panel.add(pausePlay);

        final JButton loadGame = new JButton("Load (l)");
        loadGame.addActionListener(e ->
                board.loadSnakeFromFile(GameBoard.chooseFile())
        );
        loadGame.setFocusable(false);
        control_panel.add(loadGame);

        final JButton saveGame = new JButton("Save (s)");
        saveGame.addActionListener(e -> board.saveSnakeToFile());
        saveGame.setFocusable(false);
        control_panel.add(saveGame);

        final JButton instructions = new JButton("Instructions(i)");
        instructions.addActionListener(e -> showInstructions());
        instructions.setFocusable(false);
        control_panel.add(instructions);

        control_panel.setFocusable(false);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();

        // Prevent the game from starting right away
        board.togglePause();

        // Display the instructions for the game
        showInstructions();
    }

    public static void showInstructions() {
        JOptionPane.showMessageDialog(null, """
                Welcome to snake!
                Snake is a classic single player video game. The goal of the
                game is to eat as much food as possible. The more food you eat,
                the longer the snake gets. The rules are as follows:
                1. If the snake hits the wall or itself the game is over.
                2. The snake moves in 4 directions: north, south, east, west. Movement is
                controlled by the arrow keys.
                3. The final score is the total number of food that the snake
                eats.
                I hope you enjoy!
                """
        );
    }
}
