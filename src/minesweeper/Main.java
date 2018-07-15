/*
 * Abhay Raina
 * ICS4U1
 * Mr. Bulhao
 * January 19, 2018
 * Minesweeper
 */
package minesweeper;

// Import Statements
import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Main extends Application {

	// Fields to be used by the class
	public GameGrid grid = new GameGrid();
	public VBox toolBar = new VBox();
	public HBox gridBox = new HBox();
	public Game game = new Game(grid);

	@Override
	public void start(Stage primaryStage) {
		try {

			// Sets the VBox which stores all of the other nodes
			VBox root = new VBox();

			// Sets the UpperBar which stores the bomb counter and time counter as well as the button for the game state
			UpperBar upperBar = new UpperBar(game);

			// Sets the ButtonBar which contains New Game, Save and Load
			ButtonBar buttonBar = new ButtonBar(game, primaryStage);

			// Stores the UpperBar and ButtonBar
			toolBar.getChildren().addAll(upperBar, buttonBar);

			// Sets the HBox which stores the button grid
			gridBox.setAlignment(Pos.CENTER);

			// Stores the grid
			gridBox.getChildren().add(grid);

			// Label for storing instructions
			Label instructionlbl = new Label("Instructions: To play the game click the face or the New Game Button. \n\t\t     Left click to show the cell or Right click to flag it.");
			instructionlbl.setPadding(new Insets(5, 5, 5, 5));
			instructionlbl.setTextFill(Color.BLUE);

			// Add everything to the root VBox
			root.getChildren().addAll(toolBar, gridBox, instructionlbl);



			// Alerts the user if they won the game depending on the change in GameState
			game.stateProperty().addListener(new ChangeListener<GameState>() {

				// Checks for changes in GameState and needs ObservableValue to know which change it is responding to
				@Override
				public void changed(ObservableValue<? extends GameState> observable, GameState oldValue, GameState newValue) {

					// If the state of the game does not equal win, it doesn't show
					if (newValue != GameState.WIN) {
						return;
					}

					// Stops the timer
					game.getTimer().stop();

					// Runs the message dialog about the result and it is multi-threaded
					Thread t = new Thread(new Runnable(){
						public void run(){

							// Displays the congratulations message and the time it took them
							JOptionPane.showMessageDialog(null, "You won in " + game.getTime().get() + " seconds", "You won!", JOptionPane.INFORMATION_MESSAGE);
						}
					});

					// Starts the thread
					t.start();
				}
			});

			// Sets the properties of the scene and stage, as wells as a closing protocol and loads CSS
			Scene scene = new Scene(root);		
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Minesweeper");
			primaryStage.setOnCloseRequest(new EventHandler <WindowEvent> () {
				public void handle(WindowEvent event) {
					int exitProgram = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Minesweeper", JOptionPane.YES_NO_OPTION);

					// Stops the time counter temporarily
					game.getTimer().stop();

					// If yes close the program
					if (exitProgram == JOptionPane.YES_OPTION) {
						primaryStage.close();
					}

					// If no consumes the event
					if (exitProgram == JOptionPane.NO_OPTION) {
						event.consume();

						// Replays the timer if the GameState is Running
						if (game.stateProperty().get() == GameState.RUNNING) {
							game.getTimer().play();
						}
					}
				}

			});
			primaryStage.show();

			// Creates a default game on startup
			createNewGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method creates a default grid of 9 by 9 with 10 bombs
	 */
	public void createNewGame() {
		int height = 9;
		int width = 9;
		int numberOfBombs = 10;
		game.createNew(width, height, numberOfBombs);
		game.stateProperty().set(GameState.START);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
