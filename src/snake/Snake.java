package snake;

import java.awt.GraphicsEnvironment;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
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
        
        final Label scoreLbl = new Label("Score: 0");
        StackPane.setAlignment(scoreLbl, Pos.TOP_LEFT);
        StackPane.setMargin(scoreLbl, new Insets(10));
        scoreLbl.setFont(new Font("Courier New", 20));
        
        final Label pausedLbl = new Label();
        StackPane.setAlignment(pausedLbl, Pos.TOP_RIGHT);
        StackPane.setMargin(pausedLbl, new Insets(10));
        pausedLbl.setFont(new Font("Courier New", 20));
        
        final StackPane root = new StackPane(canvas, scoreLbl, pausedLbl);
        
        final Scene scene = new Scene(root);
        
        final GameController game = new GameController(canvas.getGraphicsContext2D(), gridWidth, gridHeight, cellSize);
        
        scene.setOnKeyPressed((KeyEvent t) -> game.handleKeyPressed(t));
        
        new AnimationTimer() {
            long updateNanoTime = System.nanoTime();
            
            @Override
            public void handle(long currentNanoTime) {
                if (currentNanoTime - updateNanoTime >= frameDelay) {
                    game.tick();
                    scoreLbl.setText("Score: " + game.getScore());
                    if (game.isPaused()) {
                        pausedLbl.setText("Paused");
                    } else {
                        pausedLbl.setText("");
                    }
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
