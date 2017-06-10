package snake;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the position of each cell of the snake and performs calculations such
 * as moving the snake
 * @author Ashley
 */
public class Snake {
    private final List<Coordinates> snake = new ArrayList<>();
    
    private boolean shouldGrow = false;
    private Direction direction;
    
    /**
     * Creates a snake at the specified starting position with the
     * specified length in the specified starting direction
     * @param startPos the starting position for the snake
     * @param startLen the starting length for the snake
     * @param startDir the starting direction of the snake
     */
    public Snake(Coordinates startPos, int startLen, Direction startDir) {
        create(startPos, startLen, startDir);
    }
    
    /**
     * Create the snake at the specified position with the specified
     * length in the specified direction
     * @param pos the position to create the snake at
     * @param len the length of the snake
     * @param dir the direction to create the snake in
     */
    private void create(Coordinates pos, int len, Direction dir) {
        this.direction = dir;
        for (int i = 0; i < len; i++) {
            switch (dir) {
                case NORTH:
                    grow(pos.add(0, i));
                    break;
                case EAST:
                    grow(pos.add(-i, 0));
                    break;
                case SOUTH:
                    grow(pos.add(0, -i));
                    break;
                case WEST:
                    grow(pos.add(i, 0));
                    break;
            }
        }
    }
    
    /**
     * Move the snake in the current direction
     */
    public void move() {
        Coordinates snakeTail = getTail();
        
        //Starting from the end of the snake set each cell to the position of the
        //next cell except for the head
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.set(i, getBody(i - 1));
        }
        
        //For the head of the snake calculate its new position based on the
        //current direction
        Coordinates snakeHead = getHead();
        switch (direction) {
            case NORTH:
                snakeHead = snakeHead.add(0, -1);
                break;
            case EAST:
                snakeHead = snakeHead.add(1, 0);
                break;
            case SOUTH:
                snakeHead = snakeHead.add(0, 1);
                break;
            case WEST:
                snakeHead = snakeHead.add(-1, 0);
                break;
        }
        snake.set(0, snakeHead);
        
        //If the snake has eaten an apple append the cell that previously was
        //the tail to the end of the snake
        if (shouldGrow) {
            grow(snakeTail);
            shouldGrow = false;
        }
    }
    
    /**
     * Adds a coordinate pair at the specified position
     * @param pos the position to add the coordinates
     */
    private void grow(Coordinates pos) {
        snake.add(pos);
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
    
    /**
     * Gets the position of the snake's head
     * @return the position of the snake's head
     */
    public Coordinates getHead() {
        return snake.get(0);
    }
    
    /**
     * Gets the position of a snake's body part according to the specified
     * index
     * @param index the index of the body part to retrieve
     * @return the position of the snake's body part
     */
    public Coordinates getBody(int index) {
        return snake.get(index);
    }
    
    /**
     * Gets the position of the snake's tail
     * @return the position of the snake's tail 
     */
    public Coordinates getTail() {
        return snake.get(snake.size() - 1);
    }
    
    /**
     * Tells the snake that it has eaten an apple and should grow
     */
    public void eatApple() {
        this.shouldGrow = true;
    }
    
    /**
     * Gets the length of the snake
     * @return the length of the snake
     */
    public int getLength() {
        return snake.size();
    }
}
