package snake;

import javax.swing.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {





    // -------------------------------------------------------
    // ------------------- GameBoard Tests -------------------
    // -------------------------------------------------------





    // ------------------ `newFood()` Tests ------------------

    @Test
    public void testNewFoodManyTimes() {
        final JLabel status = new JLabel("Setting up...");
        GameBoard gameBoard = new GameBoard(status);
        for (int i = 0; i < 1000; i++) {
            Food food = gameBoard.getFood();
            Food oldFood = new Food(food.getX(), food.getY());

            gameBoard.hasEaten();
            Food newFood = gameBoard.getFood();

            assertNotEquals(oldFood, newFood);
        }
    }

    // -------------------- `reset()` Tests ------------------

    @Test
    public void testResetSameBoard() {
        final JLabel status = new JLabel("Press space to start.");
        GameBoard gameBoard1 = new GameBoard(status);
        GameBoard gameBoard2 = new GameBoard(status);

        // Reset the boards
        gameBoard1.reset();
        gameBoard2.reset();

        assertEquals(gameBoard1, gameBoard2);
    }

    @Test
    public void testResetSnakeFromFile() {
        final JLabel status = new JLabel("Press space to start.");
        GameBoard gameBoard1 = new GameBoard(status);
        GameBoard gameBoard2 = new GameBoard(status);

        // Simulating gameplay
        gameBoard1.loadSnakeFromFile(Path.of("./files/TestSnake1.csv").toFile());
        gameBoard1.togglePause();
        gameBoard1.hasEaten();

        // Make sure the boards aren't equal
        assertNotEquals(gameBoard1, gameBoard2);

        // Reset the boards
        gameBoard1.reset();
        gameBoard2.reset();

        assertEquals(gameBoard1, gameBoard2);
    }

    // ---------------- `togglePause()` Tests ----------------

    @Test
    public void testTogglePause() {
        final JLabel status = new JLabel("Setting up...");
        GameBoard gameBoard = new GameBoard(status);

        // Setting up
        assertEquals(gameBoard.getStatusText(), "Setting up...");

        // Starting the game (it starts paused)
        gameBoard.togglePause();
        assertEquals(gameBoard.getStatusText(), "Running...");

        // Pausing the game
        gameBoard.togglePause();
        assertEquals(gameBoard.getStatusText(), "Press space to start.");
    }





    // -------------------------------------------------------
    // -------------------- Location Tests -------------------
    // -------------------------------------------------------





    // --------------- `movedLocation()` Tests ---------------

    @Test
    public void testMovedLocationOnceEachDirection() {
        final JLabel status = new JLabel("Setting up...");
        GameBoard gameBoard = new GameBoard(status);
        Snake snake = new Snake(gameBoard);
        SnakeComponent head = new SnakeComponent(250, 250);

        snake.changeDirection(Direction.LEFT);
        SnakeComponent newComponentLeft = head.movedLocation(snake);

        snake.changeDirection(Direction.RIGHT);
        SnakeComponent newComponentRight = head.movedLocation(snake);

        snake.changeDirection(Direction.UP);
        SnakeComponent newComponentUp = head.movedLocation(snake);

        snake.changeDirection(Direction.DOWN);
        SnakeComponent newComponentDown = head.movedLocation(snake);

        assertNotEquals(head, newComponentLeft);
        assertNotEquals(head, newComponentRight);
        assertNotEquals(head, newComponentUp);
        assertNotEquals(head, newComponentDown);

        assertEquals(
                new SnakeComponent(head.getX() - GameBoard.ROW_DIMENSION,
                        head.getY()),
                newComponentLeft
        );
        assertEquals(
                new SnakeComponent(head.getX() + GameBoard.ROW_DIMENSION,
                        head.getY()),
                newComponentRight
        );
        assertEquals(
                new SnakeComponent(head.getX(),
                        head.getY() - GameBoard.ROW_DIMENSION),
                newComponentUp
        );
        assertEquals(
                new SnakeComponent(head.getX(),
                        head.getY() + GameBoard.ROW_DIMENSION),
                newComponentDown
        );
    }

    @Test
    public void testMovedLocationManyTimes() {
        final JLabel status = new JLabel("Setting up...");
        GameBoard gameBoard = new GameBoard(status);
        Snake snake = new Snake(gameBoard);
        SnakeComponent component = new SnakeComponent(250, 250);
        for (int i = 0; i < 1000; i++) {
            snake.changeDirection(Direction.LEFT);
            SnakeComponent newLocation = component.movedLocation(snake);
            assertNotEquals(component, newLocation);
            assertEquals(
                    new SnakeComponent(
                            component.getX() - GameBoard.ROW_DIMENSION,
                            component.getY()
                    ),
                    newLocation
            );
        }
    }

    // ------------------- `equals()` Tests ------------------

    @Test
    public void testEquals() {
        Location l1 = new SnakeComponent(15, 15);
        Location l2 = new SnakeComponent(15, 30);
        Location l3 = new SnakeComponent(20, 15);
        Location l4 = new SnakeComponent(15, 15);

        assertNotEquals(l1, l2);
        assertNotEquals(l1, l3);
        assertNotEquals(l2, l3);
        assertNotEquals(l2, l4);
        assertEquals(l1, l4);
    }





    // -------------------------------------------------------
    // ---------------------- Snake Tests --------------------
    // -------------------------------------------------------





    // --------------- `getDirections()` Tests ---------------

    @Test
    public void testGetDirections() {
        final JLabel status = new JLabel("Setting up...");
        GameBoard gameBoard = new GameBoard(status);
        Snake snake = new Snake(gameBoard);

        // Snake is initialized to left
        assertEquals(snake.getDirection(), Direction.LEFT);

        snake.changeDirection(Direction.UP);
        assertEquals(snake.getDirection(), Direction.UP);

        snake.changeDirection(Direction.RIGHT);
        assertEquals(snake.getDirection(), Direction.RIGHT);

        snake.changeDirection(Direction.DOWN);
        assertEquals(snake.getDirection(), Direction.DOWN);
    }

    // ------------------ `getScore()` Tests -----------------

    @Test
    public void testGetScoreEmpty() {
        int score = Snake.fromFile(new File("empty")).getScore();
        // All snakes should at least have a head
        assertEquals(1, score);
    }

    @Test
    public void testGetScoreSimple() {
        int score = Snake.fromFile(Path.of("./files/TestSnake1.csv").toFile()).getScore();
        assertEquals(4, score);
    }

    @Test
    public void testGetScoreComplex() {
        int score = Snake.fromFile(Path.of("./files/TestSnake2.csv").toFile()).getScore();
        assertEquals(21, score);
    }

    // --------------- `hasComponent()` Tests ----------------

    @Test
    public void testHasComponentEmpty() {
        final JLabel status = new JLabel("Setting up...");
        GameBoard gameBoard = new GameBoard(status);
        Snake snake = new Snake(gameBoard, true);

        assertEquals(snake.getScore(), 1);
        assertTrue(snake.hasComponent(new SnakeComponent(Snake.START)));
        assertFalse(snake.hasComponent(new SnakeComponent(120, 155)));
        assertFalse(snake.hasComponent(new SnakeComponent(125, 150)));
    }

    @Test
    public void testHasComponentNonEmpty() {
        final JLabel status = new JLabel("Setting up...");
        GameBoard gameBoard = new GameBoard(status);
        Snake snake = new Snake(gameBoard);
        snake.addComponent(new SnakeComponent(120,150,512));
        snake.addComponent(new SnakeComponent(135,150,514));
        snake.addComponent(new SnakeComponent(150,150,516));
        snake.addComponent(new SnakeComponent(165,150,518));

        assertTrue(snake.hasComponent(new SnakeComponent(120, 150)));
        assertTrue(snake.hasComponent(new SnakeComponent(165, 150)));
        assertFalse(snake.hasComponent(new SnakeComponent(120, 155)));
        assertFalse(snake.hasComponent(new SnakeComponent(125, 150)));
    }

    // ------------------ `fromFile()` Tests -----------------

    @Test
    public void testFromFileEmpty() {
        final JLabel status = new JLabel("Setting up...");
        GameBoard gameBoard = new GameBoard(status);
        Snake expectedSnake = new Snake(gameBoard);
        expectedSnake.addComponent(new SnakeComponent(Snake.START));

        Snake fileSnake = Snake.fromFile(Path.of("empty").toFile());

        assertEquals(expectedSnake, fileSnake);
    }

    @Test
    public void testFromFileNonEmpty() {
        final JLabel status = new JLabel("Setting up...");
        GameBoard gameBoard = new GameBoard(status);
        Snake expectedSnake = new Snake(gameBoard);
        expectedSnake.addComponent(new SnakeComponent(175,100,8552));
        expectedSnake.addComponent(new SnakeComponent(200,100,8558));
        expectedSnake.addComponent(new SnakeComponent(225,100,8564));
        expectedSnake.addComponent(new SnakeComponent(250,100,8570));

        Snake fileSnake = Snake.fromFile(Path.of("./files/TestSnake1.csv").toFile());

        assertEquals(expectedSnake, fileSnake);
    }

    // ---------------- `toCSVString()` Tests ----------------

    @Test
    public void testToCSVString() {
        String expected = """
                175,100,8552
                200,100,8558
                225,100,8564
                250,100,8570
                """;

        Snake snake = Snake.fromFile(Path.of("./files/TestSnake1.csv").toFile());
        assertEquals(expected, snake.toCSVString());
    }





    // -------------------------------------------------------
    // ----------------- SnakeComponent Tests ----------------
    // -------------------------------------------------------





    // ----------------- `compareTo()` Tests -----------------

    @Test
    public void testCompareTo() {
        assertEquals(
                -1,
                (new SnakeComponent(100, 100, 100)).compareTo(
                        new SnakeComponent(100, 100, 200))
        );

        assertEquals(
                -1,
                (new SnakeComponent(100, 100, 100)).compareTo(
                        new SnakeComponent(100, 100, 101))
        );

        assertEquals(
                0,
                (new SnakeComponent(100, 100, 100)).compareTo(
                        new SnakeComponent(100, 100, 100))
        );

        assertEquals(
                1,
                (new SnakeComponent(100, 100, 101)).compareTo(
                        new SnakeComponent(100, 100, 100))
        );

        assertEquals(
                1,
                (new SnakeComponent(100, 100, 200)).compareTo(
                        new SnakeComponent(100, 100, 100))
        );
    }
}
