/**
 * @BenLehrburger
 * CS 10
 * Short Assignment 2
 * Create a blob that moves in a fixed number of steps
 */

public class Jogger extends Blob {
    protected double steps, traveled;

    public Jogger(double x, double y) {
        super(x, y);
        steps = 12 + (Math.random() * 18);
        dx = 2 * (Math.random()-0.5);
        dy = 2 * (Math.random()-0.5);
    }

    public void step() {
        traveled += 1;
        if (traveled < steps) {
            x += dx;
            y += dy;
        }
        else {
            dx = 2 * (Math.random()-0.5);
            dy = 2 * (Math.random()-0.5);
            x += dx;
            y += dy;
            traveled = 0;
        }
    }
}
