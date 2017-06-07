package snake;

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
 * The main class that handles displaying the game window and receiving user
 * input. Also handles some graphics elements such as labels as well as
 * providing the AnimationTimer for the game to function.
 * @author Ashley
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        final int gridWidth = 50;
        final int gridHeight = 50;
        final int cellSize = 20;
        final int frameDelay = 50_000_000;
        
        //The canvas that will display the game
        final Canvas canvas = new Canvas(gridWidth * cellSize, gridHeight * cellSize);
        
        //A label to display the player's current score
        final Label scoreLbl = new Label("Score: 0");
        StackPane.setAlignment(scoreLbl, Pos.TOP_LEFT);
        StackPane.setMargin(scoreLbl, new Insets(10));
        scoreLbl.setFont(new Font("Courier New", 20));
        
        //A label to display whether the game is paused or not
        final Label pausedLbl = new Label();
        StackPane.setAlignment(pausedLbl, Pos.TOP_RIGHT);
        StackPane.setMargin(pausedLbl, new Insets(10));
        pausedLbl.setFont(new Font("Courier New", 20));
        
        //A label to display whether the game is in automatic mode or not
        final Label autoLbl = new Label();
        StackPane.setAlignment(autoLbl, Pos.BOTTOM_LEFT);
        StackPane.setMargin(autoLbl, new Insets(10));
        autoLbl.setFont(new Font("Courier New", 20));
        
        //A StackPane to group the nodes together
        final StackPane root = new StackPane(canvas, scoreLbl, pausedLbl, autoLbl);
        
        //The main scene
        final Scene scene = new Scene(root);
        
        //A new GameController to handle the workings of the game
        final GameController game = new GameController(canvas.getGraphicsContext2D(), gridWidth, gridHeight, cellSize);
        
        //Set it so any keyboard input is redirected to the GameController
        scene.setOnKeyPressed((KeyEvent t) -> game.handleKeyPressed(t));
        
        //An animation timer which limits the framerate of the game and handles
        //updating of the score and paused labels.
        new AnimationTimer() {
            long updateNanoTime = System.nanoTime();
            
            @Override
            public void handle(long currentNanoTime) {
                //If enough time has passed between ticks then calculate the
                //next game state and update the labels
                if (currentNanoTime - updateNanoTime >= frameDelay) {
                    game.tick();
                    scoreLbl.setText("Score: " + game.getScore());
                    if (game.isPaused()) {
                        pausedLbl.setText("Paused");
                    } else {
                        pausedLbl.setText("");
                    }
                    if (game.isAuto()) {
                        autoLbl.setText("Auto");
                    } else {
                        autoLbl.setText("");
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
