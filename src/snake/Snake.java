package snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 *
 * @author Ashley
 */
public class Snake extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        final int gridWidth = 50;
        final int gridHeight = 50;
        final int cellSize = 20;
        final int frameDelay = 50_000_000;
        
        final Canvas canvas = new Canvas(gridWidth * cellSize, gridHeight * cellSize);
        final BorderPane root = new BorderPane(canvas,null,null,null,null);
        final Scene scene = new Scene(root);
        
        final GameController game = new GameController(canvas.getGraphicsContext2D(), gridWidth, gridHeight, cellSize);
        
        scene.setOnKeyPressed((KeyEvent t) -> game.handleKeyPressed(t));
        
        new AnimationTimer() {
            long updateNanoTime = System.nanoTime();
            
            @Override
            public void handle(long currentNanoTime) {
                if (currentNanoTime - updateNanoTime >= frameDelay) {
                    game.tick();
                    updateNanoTime = currentNanoTime;
                }
            }
            
        }.start();
        
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
