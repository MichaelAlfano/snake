package snake;

public class SnakeComponent extends Location implements Comparable<SnakeComponent> {

    private static int componentCount = 0;
    private int componentNumber;

    public SnakeComponent(int x, int y) {
        incrementComponentCount();
        componentNumber = getComponentCount();
        this.setX(x);
        this.setY(y);
    }

    public SnakeComponent(int x, int y, int componentNumber) {
        this (x, y);
        this.componentNumber = componentNumber;
        componentCount = componentNumber - 1;
    }

    public SnakeComponent(Location l) {
        this(l.getX(), l.getY());
    }

    public boolean isHead() {
        return componentNumber == componentCount;
    }

    public static int getComponentCount() {
        return componentCount;
    }

    public int getComponentNumber() {
        return this.componentNumber;
    }

    public static void incrementComponentCount() {
        componentCount++;
    }

    public static void reset() {
        SnakeComponent.componentCount = 0;
    }

    @Override
    public int compareTo(SnakeComponent o) {
        return Integer.compare(this.componentNumber, o.componentNumber);
    }

    @Override
    public SnakeComponent movedLocation(Snake snake) {
        int x = this.getX();
        int y = this.getY();
        return switch (snake.getDirection()) {
            case UP -> new SnakeComponent(x, y - GameBoard.ROW_DIMENSION);
            case DOWN -> new SnakeComponent(x, y + GameBoard.ROW_DIMENSION);
            case LEFT -> new SnakeComponent(x - GameBoard.ROW_DIMENSION, y);
            case RIGHT -> new SnakeComponent(x + GameBoard.ROW_DIMENSION, y);
        };
    }
}

