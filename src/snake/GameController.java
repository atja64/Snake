package snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 * Controls the entire functioning of the game including drawing graphical elements,
 * calculating game states, controlling movement and actions and processing
 * keyboard input.
 * @author Ashley
 */
public class GameController {
    private final GraphicsContext gc;
    private final int gridWidth, gridHeight, cellSize;
    
    private int score = 0;
    private boolean shouldGrow = false;
    private boolean keyPressedThisTick = false;
    private boolean paused = false;
    private boolean auto = false;
    
    private final List<Coordinates> snake = new ArrayList<>();
    private Coordinates apple;
    
    private enum Direction {NORTH, EAST, SOUTH, WEST}
    private Direction direction;
    
    /**
     * Initialises a new GameController with the specified parameters and
     * immediately creates a new game state.
     * @param gc the GraphicsContext that the game will be drawn to
     * @param gridWidth the width of the game grid
     * @param gridHeight the height of the game grid
     * @param cellSize the size in pixels of each cell in the grid
     */
    public GameController(GraphicsContext gc, int gridWidth, int gridHeight, int cellSize) {
        this.gc = gc;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.cellSize = cellSize;
        
        startGame();
    }
    
    /**
     * Initialise the game
     */
    private void startGame() {
        createSnake();
        createApple();
        direction = Direction.WEST;
        score = 0;
    }
    
    /**
     * Create a new snake at the centre of the game grid
     */
    private void createSnake() {
        int halfWidth = gridWidth / 2;
        int halfHeight = gridHeight / 2;
        
        for (int i = 0; i < 5; i++) {
            addSnakeCell(halfWidth + i, halfHeight);
        }
    }
    
    /**
     * Kill the snake :(
     */
    private void killSnake() {
        snake.clear();
    }
    
    /**
     * Add a cell to the snake at the specified coordinates
     * @param x the x coordinate of the grid location to add
     * @param y the y coordinate of the grid location to add
     */
    private void addSnakeCell(int x, int y) {
        snake.add(new Coordinates(x, y));
    }
    
    /**
     * Create the apple at a random location on the game grid.
     * TODO: Fix so it doesn't generate the apple on top of the snake
     */
    private void createApple() {
        Random rnd = new Random();
        apple = new Coordinates(rnd.nextInt(gridWidth), rnd.nextInt(gridHeight));
    }
    
    /**
     * Draws the game to the GraphicsContext
     */
    private void drawGame() {
        //For  each each cell of the grid if there is a snake draw green,
        //an apple draw red or nothing draw white.
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                if (contains(snake, new Coordinates(x, y))) {
                    drawCell(x, y, Color.GREEN);
                } else if (apple.equals(new Coordinates(x, y))) {
                    drawCell(x, y, Color.RED);
                } else {
                    drawCell(x, y, Color.WHITE);
                }
            }
        }
        //Draw a grid around the game grid
        gc.strokeRect(0, 0, gridWidth * cellSize, gridHeight * cellSize);        
    }
    
    private boolean contains(List<Coordinates> array, Coordinates obj) {
        return array.stream().anyMatch((elem) -> (obj.equals(elem)));
    }
    
    /**
     * Draws a cell of the specified colour and x and y coordinates to the game grid
     * @param x the x coordinate of the cell to draw
     * @param y the y coordinate of the cell to draw
     * @param color the Color of the cell to draw
     */
    private void drawCell(int x, int y, Color color) {
        gc.setFill(color);
        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
    }
    
    /**
     * Move the snake in the current direction
     */
    private void moveSnake() {
        Coordinates snakeTail = snake.get(snake.size() - 1);
        
        //Starting from the end of the snake set each cell to the position of the
        //next cell except for the head
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.set(i, new Coordinates(snake.get(i - 1).getX(), snake.get(i - 1).getY()));
        }
        
        //For the head of the snake calculate its new position based on the
        //current direction
        Coordinates snakeHead = snake.get(0);
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
            addSnakeCell((int) snakeTail.getX(), (int) snakeTail.getY());
            shouldGrow = false;
        }        
    }
    
//    /**
//     * Calculate which direction the snake should move in when the game is in
//     * auto mode.
//     * TODO: Make better, snake tends to kill itself as it stands ;)
//     */
//    private void calculateBestDirection() {
//        Point2D snakeHead = snake.get(0);
//        double xDif = snakeHead.getX() - apple.getX();
//        double yDif = snakeHead.getY() - apple.getY();
//        if (Math.abs(xDif) < Math.abs(yDif)) {
//            if (yDif < 0) {
//                if (direction == Direction.NORTH) {
//                    direction = Direction.EAST;
//                } else {
//                    direction = Direction.SOUTH;
//                }
//            } else {
//                if (direction == Direction.SOUTH) {
//                    direction = Direction.EAST;
//                } else {
//                    direction = Direction.NORTH;
//                }
//            }
//        } else {
//            if (xDif < 0) {
//                if (direction == Direction.WEST) {
//                    direction = Direction.NORTH;
//                } else {
//                    direction = Direction.EAST;
//                }                
//            } else {
//                if (direction == Direction.EAST) {
//                    direction = Direction.NORTH;
//                } else {
//                    direction = Direction.WEST;
//                }                
//            }
//        }
//    }
//    
//    private boolean wouldCollide() {
//        boolean wouldCollide = false;
//        List<Point2D> tempSnake = new ArrayList<>(snake);
//        moveSnake();
//        if (hasCollided()) {
//            wouldCollide = true;
//        }
//        snake = tempSnake;
//    }
    
    /**
     * Calculates whether the snake has eaten an apple
     * @return true if the snake's head is in the same position as the apple
     */
    private boolean hasEatenApple() {
        return apple.equals(snake.get(0));
    }
    
    /**
     * Calculates whether the snake has collided
     * @return true if the snake has collided with itself or the edge of the
     * game grid
     */
    private boolean hasCollided() {
        //Has the snake collided with itself?
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).equals(snake.get(i))) {
                return true;
            }
        }
        //Has the snake colided with the edge of the game grid?
        return snake.get(0).getX() < 0 || snake.get(0).getX() > gridWidth || snake.get(0).getY() < 0 || snake.get(0).getY() > gridHeight;
    }
    
    /**
     * Calculates the next state of the game. First moves the snake then checks
     * for collisions with itself, the edge of the game grid and the apple. Then
     * draws the updated game state.
     */
    public void tick() {
        if (!paused) {
//            if (auto) {
//                calculateBestDirection();
//            }
            moveSnake();
            if (hasEatenApple()) {
                createApple();
                shouldGrow = true;
                score++;
            }
            if (hasCollided()) {
                killSnake();
                startGame();
            }
            drawGame();
        }
        keyPressedThisTick = false;        
    }
    
    /**
     * Handles keyboard input
     * @param ke the KeyEvent to handle
     */
    public void handleKeyPressed(KeyEvent ke) {
        KeyCode keyCode = ke.getCode();
        if (!paused) {
            if (!keyPressedThisTick) {
                switch(keyCode) {
                    case UP:
                        if (direction != Direction.SOUTH) direction = Direction.NORTH;
                        break;
                    case RIGHT:
                        if (direction != Direction.WEST) direction = Direction.EAST;
                        break;
                    case DOWN:
                        if (direction != Direction.NORTH) direction = Direction.SOUTH;
                        break;
                    case LEFT:
                        if (direction != Direction.EAST) direction = Direction.WEST;
                        break;
                }
                keyPressedThisTick = true;
            }
            if (keyCode == KeyCode.A) {
                auto = !auto;
            }
        }
        if (keyCode == KeyCode.P) {
            paused = !paused;
        }
        ke.consume();
    }
    
    /**
     * Returns the value of score
     * @return score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Returns whether the game is paused or not
     * @return paused
     */
    public boolean isPaused() {
        return paused;
    }
    
    /**
     * Returns whether the game is set to automatic mode
     * @return auto
     */
    public boolean isAuto() {
        return auto;
    }
}
