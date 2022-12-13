package snake;

import java.awt.*;
import java.util.Random;

public class Food extends Location {

    public Food(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public Food(Snake snake) {
        Food newFood = movedLocation(snake);
        this.setX(newFood.getX());
        this.setY(newFood.getY());
    }

    public void reset(Snake snake) {
        Location l = this.movedLocation(snake);
        this.setX(l.getX());
        this.setY(l.getY());
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(getX(), getY(), GameBoard.ROW_DIMENSION, GameBoard.ROW_DIMENSION);
    }

    @Override
    public Food movedLocation(Snake snake) {
        Random r = new Random();
        // Getting a random row number between 1 and `ROW_COUNT` - 1 and
        // adjusting that for the size of the row to get position
        int x = (r.nextInt(GameBoard.ROW_COUNT - 2) + 1) * GameBoard.ROW_DIMENSION;
        int y = (r.nextInt(GameBoard.ROW_COUNT - 2) + 1) * GameBoard.ROW_DIMENSION;

        Food newFood = new Food(x, y);
        if (snake.hasComponent(newFood) || newFood.equals(this)) {
            return movedLocation(snake);
        } else {
            return newFood;
        }
    }
}
