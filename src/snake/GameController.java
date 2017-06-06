package snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author Ashley
 */
public class GameController {
    private final GraphicsContext gc;
    private final int gridWidth, gridHeight, cellSize;
    
    private int score = 0;
    private boolean shouldGrow = false;
    private boolean keyPressedThisTick = false;
    
    private final List<Point2D> snake = new ArrayList<>();
    private Point2D apple;
    
    private enum Direction {NORTH, EAST, SOUTH, WEST}
    private Direction direction = Direction.WEST;
    
    public GameController(GraphicsContext gc, int gridWidth, int gridHeight, int cellSize) {
        this.gc = gc;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.cellSize = cellSize;
        
        createSnake();
        createApple();
        drawGame();
    }
    
    private void createSnake() {
        int halfWidth = gridWidth / 2;
        int halfHeight = gridHeight / 2;
        
        for (int i = 0; i < 5; i++) {
            addSnakeCell(halfWidth + i, halfHeight);
        }
    }
    
    private void killSnake() {
        snake.clear();
    }
    
    private void addSnakeCell(int x, int y) {
        snake.add(new Point2D(x, y));
    }
    
    private void createApple() {
        Random rnd = new Random();
        apple = new Point2D(rnd.nextInt(gridWidth), rnd.nextInt(gridHeight));
    }
    
    private void drawGame() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                if (snake.contains(new Point2D(x, y))) {
                    drawCell(x, y, Color.GREEN);
                } else if (apple.equals(new Point2D(x, y))) {
                    drawCell(x, y, Color.RED);
                } else {
                    drawCell(x, y, Color.WHITE);
                }
            }
        }
    }
    
//    private boolean contains(List<Integer[]> ar, Integer[] coords) {
//        return ar.stream().anyMatch((elem) -> (Arrays.equals(elem, coords)));
//    }
    
    private void drawCell(int x, int y, Color color) {
        gc.setFill(color);
        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
    }
    
    private void moveSnake() {
        Point2D snakeTail = snake.get(snake.size() - 1);
        
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.set(i, new Point2D(snake.get(i - 1).getX(), snake.get(i - 1).getY()));
        }
//        System.out.println("snake is " + snake.size() + " cells big");
        
        Point2D snakeHeadCoords = snake.get(0);
        switch (direction) {
            case NORTH:
                snakeHeadCoords = snakeHeadCoords.add(0, -1);
                break;
            case EAST:
                snakeHeadCoords = snakeHeadCoords.add(1, 0);
                break;
            case SOUTH:
                snakeHeadCoords = snakeHeadCoords.add(0, 1);
                break;
            case WEST:
                snakeHeadCoords = snakeHeadCoords.add(-1, 0);
                break;
        }
        snake.set(0, snakeHeadCoords);
        
        if (shouldGrow) {
            addSnakeCell((int) snakeTail.getX(), (int) snakeTail.getY());
            shouldGrow = false;
        }        
    }
    
    private boolean hasEatenApple() {
        return apple.equals(snake.get(0));
    }
    
    private boolean hasCollided() {
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).equals(snake.get(i))) {
                return true;
            }
        }
        return snake.get(0).getX() < 0 || snake.get(0).getX() > gridWidth || snake.get(0).getY() < 0 || snake.get(0).getY() > gridHeight;
    }
    
    public void tick() {
        moveSnake();
        if (hasEatenApple()) {
            createApple();
            shouldGrow = true;
            score++;
        }
        if (hasCollided()) {
            killSnake();
            createSnake();
            score = 0;
        }
        keyPressedThisTick = false;
        drawGame();
    }
    
    public void keyPressed(KeyEvent ke) {
        if (keyPressedThisTick) {
            return;
        }
        switch(ke.getCode()) {
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
        ke.consume();
        keyPressedThisTick = true;
    }
}
