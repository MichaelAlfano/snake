package snake;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public Direction opposite() {
        return switch (this) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case RIGHT -> Direction.LEFT;
            case LEFT -> Direction.RIGHT;
        };
    }
}