package snake;

/**
 * Stores x and y coordinates
 * @author Ashley
 */
public class Coordinates {
    private final int x;
    private final int y;
    
    /**
     * Creates a new coordinate pair given x and y integer values
     * @param x the x coordinate of the coordinate pair
     * @param y the y coordinate of the coordinate pair
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Get the x coordinate
     * @return x
     */
    public int getX() {
        return x;
    }
    
    /**
     * Get the y coordinate
     * @return y
     */
    public int getY() {
        return y;
    }
}
