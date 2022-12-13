package snake;

public abstract class Location {
    private int x;
    private int y;

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Location movedLocation(Snake snake) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location l)) {
            return false;
        }

        return l.getX() == this.getX() &&
                l.getY() == this.getY();
    }
}
