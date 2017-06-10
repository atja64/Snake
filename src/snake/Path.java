package snake;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to calculate the best path to the apple for the snake
 * CURRENTLY NOT USED
 * @author Ashley
 */
public class Path {
    private final List<Direction> path = new ArrayList<>();
    private Direction direction;
    private Coordinates pos;
    
    public Path(Coordinates startPos) {
        pos = startPos;
    }
    
    public Coordinates getPos() {
        return pos;
    }
    
    public void changeDirection(Direction dir) {
        switch (dir) {
            case NORTH:
                if (direction != Direction.SOUTH) direction = Direction.NORTH;
                break;
            case EAST:
                if (direction != Direction.WEST) direction = Direction.EAST;
                break;
            case SOUTH:
                if (direction != Direction.NORTH) direction = Direction.SOUTH;
                break;
            case WEST:
                if (direction != Direction.EAST) direction = Direction.WEST;
                break;
        }
    }
    
    public void moveForward() {
        switch (direction) {
            case NORTH:
                pos = pos.add(0, -1);
                break;
            case EAST:
                pos = pos.add(1, 0);
                break;
            case SOUTH:
                pos = pos.add(0, 1);
                break;
            case WEST:
                pos = pos.add(-1, 0);
                break;
        }    
    }
    
    public void moveBack() {
        switch (direction) {
            case NORTH:
                pos = pos.add(0, 1);
                break;
            case EAST:
                pos = pos.add(-1, 0);
                break;
            case SOUTH:
                pos = pos.add(0, -1);
                break;
            case WEST:
                pos = pos.add(1, 0);
                break;
        }            
    }
    
    public void addDirection() {
        path.add(direction);
    }
    
    public Direction getPath(int index) {
        return path.get(index);
    }
}
