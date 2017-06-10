package snake;

import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 * Controls the entire functioning of the game including drawing graphical elements,
 * calculating game states, controlling movement and actions and processing
 * keyboard input.
 * @author Ashley Allen
 */
public class GameController {
    private final GraphicsContext gc;
    private final int gridWidth, gridHeight, cellSize;
    
    private int score = 0;
    private boolean keyPressedThisTick = false;
    private boolean paused = false;
    private boolean auto = false;
    
    private Snake snake;
    private Coordinates apple;
    private Path path;
    
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
        score = 0;
    }
    
    /**
     * Create a new snake at the centre of the game grid
     */
    private void createSnake() {
        int halfWidth = gridWidth / 2;
        int halfHeight = gridHeight / 2;
        
        snake = new Snake(new Coordinates(halfWidth, halfHeight), 5, Direction.WEST);
    }
    
    /**
     * Create the apple at a random location on the game grid other than where
     * the snake is.
     */
    private void createApple() {
        Random rnd = new Random();
        while (isSnakeAt(apple)) {
            apple = new Coordinates(rnd.nextInt(gridWidth), rnd.nextInt(gridHeight));
        }
    }
    
    /**
     * Draws the game to the GraphicsContext
     */
    private void drawGame() {
        //For  each each cell of the grid if there is a snake draw green,
        //an apple draw red or nothing draw white.
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                if (isSnakeAt(new Coordinates(x, y))) {
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
    
    private boolean isSnakeAt(Coordinates pos) {
        for (int i = 0; i < snake.getLength(); i++) {
            if (pos.equals(snake.getBody(i))) {
                return true;
            }
        }
        return false;
    }
    
//    private void getPath() {
//        path = new Path(snake.getHead());
//        while (!path.getPos().equals(apple)) {
//            int xDif = path.getPos().getX() - apple.getX();
//            int yDif = path.getPos().getY() - apple.getY();
//            if (Math.abs(xDif) < Math.abs(yDif)) {
//                
//            } else {
//                
//            }
//        }
//    }
    
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
     * Calculates whether the snake has eaten an apple
     * @return true if the snake's head is in the same position as the apple
     */
    private boolean hasEatenApple() {
        return snake.getHead().equals(apple);
    }
    
    /**
     * Calculates whether the snake has collided
     * @return true if the snake has collided with itself or the edge of the
     * game grid
     */
    private boolean hasCollided() {
        //Has the snake collided with itself?
        for (int i = 1; i < snake.getLength(); i++) {
            if (snake.getHead().equals(snake.getBody(i))) {
                return true;
            }
        }
        //Has the snake colided with the edge of the game grid?
        return snake.getHead().getX() < 0 || snake.getHead().getX() >= gridWidth || snake.getHead().getY() < 0 || snake.getHead().getY() >= gridHeight;
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
            snake.move();
            if (hasEatenApple()) {
                snake.eatApple();
                createApple();
                score++;
            }
            if (hasCollided()) {
                createSnake();
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
                        snake.changeDirection(Direction.NORTH);
                        break;
                    case RIGHT:
                        snake.changeDirection(Direction.EAST);
                        break;
                    case DOWN:
                        snake.changeDirection(Direction.SOUTH);
                        break;
                    case LEFT:
                        snake.changeDirection(Direction.WEST);
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
