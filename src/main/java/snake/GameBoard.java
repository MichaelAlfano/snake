package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Calendar;

public class GameBoard extends JPanel {

    private final JLabel status; // current status text
    private Snake snake;
    private final Food food;
    private boolean playing = false; // whether the game is running
    private boolean canTurn = true; // whether the snake is allowed to turn
    private boolean gameOver = false;

    // Game constants
    public static final int ROW_DIMENSION = 25;
    public static final int ROW_COUNT = 18;
    public static final int BOARD_DIMENSION = ROW_DIMENSION * ROW_COUNT;
    public static final int INTERVAL = 120;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel status) {
        // creates border around the snake pen area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Enable keyboard focus on the snake pen area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // Initializing the snake
        snake = new Snake(this, true);

        // Initializing the food
        food = new Food(snake);

        // Initializing the status JLabel
        this.status = status;

        // Iterates `tick` with interval `INTERVAL`
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT && canTurn(Direction.LEFT)) {
                    snake.changeDirection(Direction.LEFT);
                } else if (key == KeyEvent.VK_RIGHT && canTurn(Direction.RIGHT)) {
                    snake.changeDirection(Direction.RIGHT);
                } else if (key == KeyEvent.VK_DOWN && canTurn(Direction.DOWN)) {
                    snake.changeDirection(Direction.DOWN);
                } else if (key == KeyEvent.VK_UP && canTurn(Direction.UP)) {
                    snake.changeDirection(Direction.UP);
                } else if (key == KeyEvent.VK_SPACE) {
                    togglePause();
                } else if (key == KeyEvent.VK_R) {
                    reset();
                } else if (key == KeyEvent.VK_I) {
                    RunSnake.showInstructions();
                } else if (key == KeyEvent.VK_S) {
                    saveSnakeToFile();
                } else if (key == KeyEvent.VK_L) {
                    loadSnakeFromFile(chooseFile());
                }
            }
        });
    }

    private boolean canTurn(Direction newDir) {
        Direction oldDir = snake.getDirection();
        canTurn = !canTurn;
        return (oldDir.opposite() != newDir && playing && !gameOver && !canTurn);
    }

    private void tick() {
        if (playing && !gameOver) {
            snake.move();
            canTurn = true;
            // update the display
            repaint();
        }
    }

    public void playerLost() {
        playing = false;
        gameOver = true;
        status.setText("Game Over. Final Score: " + snake.getScore());
    }

    public void loadSnakeFromFile(File file) {
        if (file != null) {
            reset();
            togglePause();
            snake.reset();
            SnakeComponent.reset();
            snake = Snake.fromFile(file);
            repaint();
            requestFocusInWindow();
            snake.fixDirection();
            status.setText("Press space to start.");
        }
    }

    public void saveSnakeToFile() {
        try {
            File file = new File("./files/Snake - " +
                    Calendar.getInstance().getTime() + ".csv");
            if (file.createNewFile()) {
                FileWriter w = new FileWriter(file);
                w.write(String.valueOf(snake.toCSVString()));
                w.close();
            }
        } catch (IOException ignored) { }
    }

    public static File chooseFile() {
        JFileChooser c = new JFileChooser();
        c.showOpenDialog(null);
        return c.getSelectedFile();
    }

    /**
     * Resets the game to its initial state.
     */
    public void reset() {
        snake.reset();
        food.reset(snake);

        // Starting gameplay
        playing = true;
        gameOver = false;
        status.setText("Running...");

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public Food getFood() {
        return this.food;
    }

    public void hasEaten() {
        this.food.reset(snake);
    }

    public void togglePause() {
        if (gameOver) {
            return;
        }
        this.playing = !this.playing;
        if (this.playing) {
            status.setText("Running...");
        } else {
            status.setText("Press space to start.");
        }
    }

    public String getStatusText() {
        return this.status.getText();
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_DIMENSION, BOARD_DIMENSION);
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws vertical board lines
        g.setColor(Color.LIGHT_GRAY);
        for (int w = ROW_DIMENSION; w <= BOARD_DIMENSION; w += ROW_DIMENSION) {
            g.drawLine(w, 0, w, BOARD_DIMENSION);
        }

        // Draws horizontal board lines
        for (int h = ROW_DIMENSION; h <= BOARD_DIMENSION; h += ROW_DIMENSION) {
            g.drawLine(0, h, BOARD_DIMENSION, h);
        }

        // Draw the snake
        snake.draw(g);

        // Draw the food
        food.draw(g);

    }

    /**
     * Checks if 2 `GameBoard`s are equal to each other.
     * @param o represents the other object being compared.
     * @return true if the two `GameBoard`s have the same
     * `playing`, `snake`, and `status.getText()`.
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != GameBoard.class) {
            return false;
        }
        GameBoard other = (GameBoard) o;

        boolean sameSnake = this.snake.equals(other.snake);
        boolean samePlaying = this.playing == other.playing;
        boolean sameText = this.status.getText().equals(other.status.getText());

        return (samePlaying && sameSnake && sameText);
    }
}
