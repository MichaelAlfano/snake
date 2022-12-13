package snake;

import java.awt.*;
import java.io.*;
import java.util.TreeSet;

public class Snake {
    private final TreeSet<SnakeComponent> components;
    private Direction direction;
    private static GameBoard game;

    public static final SnakeComponent START =
            new SnakeComponent(
                    (GameBoard.ROW_COUNT / 2) * GameBoard.ROW_DIMENSION,
                    (GameBoard.ROW_COUNT / 2) * GameBoard.ROW_DIMENSION
            );

    public Snake(GameBoard game) {
        components = new TreeSet<>();
        Snake.game = game;
        this.direction = Direction.LEFT;
    }

    public Snake(GameBoard game, boolean initHead) {
        components = new TreeSet<>();
        if (initHead) {
            SnakeComponent head = new SnakeComponent(START);
            components.add(head);
        }
        Snake.game = game;
        this.direction = Direction.LEFT;
    }

    public void reset() {
        components.clear();
        components.add(new SnakeComponent(START));
    }

    public void changeDirection(Direction newDirection) {
        this.direction = newDirection;
    }

    public TreeSet<SnakeComponent> getComponents() {
        return components;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getScore() {
        return components.size();
    }

    public SnakeComponent getHead() {
        return components.last();
    }

    public void move() {
        SnakeComponent head = getHead();
        Location newLocation = head.movedLocation(this);

        int x = newLocation.getX();
        int y = newLocation.getY();
        boolean xOutOfBounds = x < 0 || x > GameBoard.BOARD_DIMENSION - GameBoard.ROW_DIMENSION;
        boolean yOutOfBounds = y < 0 || y > GameBoard.BOARD_DIMENSION - GameBoard.ROW_DIMENSION;

        if (xOutOfBounds || yOutOfBounds || hasComponent(newLocation)) {
            game.playerLost();
            return;
        }

        // If the new location is where the food is then
        // we remove the last component of the snake, otherwise
        // we leave it which grows the snake.

        if (newLocation.equals(game.getFood())) {
            game.hasEaten();
        } else {
            components.remove(components.first());
        }
        addComponent(new SnakeComponent(newLocation));
    }

    public void fixDirection() {
        SnakeComponent head = components.last();
        SnakeComponent newLocation = head.movedLocation(this);
        if (hasComponent(newLocation)) {
            if (getDirection().equals(Direction.LEFT)) {
                changeDirection(Direction.RIGHT);
            } else {
                changeDirection(Direction.LEFT);
            }
        }
    }

    public boolean hasComponent(Location l) {
        return components
                .stream()
                .anyMatch(e -> e.equals(l));
    }

    public void addComponent(SnakeComponent l) {
        components.add(new SnakeComponent(l.getX(), l.getY()));
    }

    public void draw(Graphics g) {
        for (SnakeComponent component : getComponents()) {
            g.setColor(Color.BLUE);
            if (component.isHead()) {
                g.setColor(Color.PINK);
            }
            int x = component.getX();
            int y = component.getY();
            g.fillOval(x, y, GameBoard.ROW_DIMENSION, GameBoard.ROW_DIMENSION);
        }
    }

    public static Snake fromFile(File file) {
        Snake newSnake = new Snake(game);
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            String line = r.readLine();
            while (line != null) {
                try {
                    String[] values = line.split(",");
                    int x = Integer.parseInt(values[0]);
                    int y = Integer.parseInt(values[1]);
                    int componentNumber = Integer.parseInt(values[2]);
                    newSnake.addComponent(new SnakeComponent(x, y, componentNumber));
                    line = r.readLine();
                } catch (IOException ignored) {
                    // Ignoring IndexOutOfBoundsException or other IOException
                }
            }
        } catch (IOException ignored) {
            // Ignoring FileNotFoundException or other IOException
        }


        if (newSnake.getScore() == 0) {
            newSnake.addComponent(new SnakeComponent(START));
        }

        return newSnake;

    }

    public String toCSVString() {
        StringBuilder snakeData = new StringBuilder();
        for (SnakeComponent c : this.getComponents()) {
            snakeData
                    .append(c.getX())
                    .append(",")
                    .append(c.getY())
                    .append(",")
                    .append(c.getComponentNumber())
                    .append("\n");
        }
        return String.valueOf(snakeData);
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Snake.class) {
            return false;
        }
        Snake other = (Snake) o;

        for (SnakeComponent component1 : other.getComponents()) {
            boolean contains = false;
            for (SnakeComponent component2 : this.getComponents()) {
                if (component1.equals(component2)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return false;
            }
        }

        return true;
    }
}