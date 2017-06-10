package snake;

/**
 * An enum that manages the direction of various objects on the game grid
 * @author Ashley
 */
public enum Direction {
    NORTH, EAST, SOUTH, WEST;
    
    private static final Direction[] values = values();
    
    /**
     * Get the next Direction in the enum
     * @return the next Direction
     */
    public Direction next() {
        return values[(this.ordinal() + 1) % values.length];
    }
    
    /**
     * Get the previous Direction in the enum
     * @return the previous Direction
     */
    public Direction previous() {
        return values[(this.ordinal() - 1) % values.length];
    }
}