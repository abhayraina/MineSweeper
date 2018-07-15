// This Class is used for storing the bomb counter and time counter 
package minesweeper;

// Import Statements
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UpperBar extends HBox {

	/**
	 * Creates the counters and the state button
	 * @param game Takes a Game Object
	 */
	public UpperBar(Game game) {

		//This binds the following operation : result = bombList.size - markedList.size
		//A maximum of bombList.size flags can be used
		//Shows zero if all flags are used ( bombList.size - markedList . size = 0)
		NumberBinding bombCounterBinding = Bindings.subtract(Bindings.size(game.getBombList()), Bindings.size(game.getMarkedList()));

		// Declaring a label for the bomb counter
		Label bombCounter = new Label();

		// Sets the display property of the Label and changes are binded from the bombCounterBinding to the Label
		bombCounter.textProperty().bind(bombCounterBinding.asString());
		bombCounter.setTextFill(Color.RED);

		// Creates a label for indicating the bomb counter
		Label bombLabel = new Label("Bombs:");

		// Sets the VBox to hold the bomb counter
		VBox bombBox = new VBox();
		bombBox.getChildren().addAll(bombLabel, bombCounter);
		bombBox.setAlignment(Pos.CENTER);

		// Declaring a label for the timer
		Label timerCounter = new Label();

		// Sets the display property of the Label and changes are binded from the timer in game class to the Label
		timerCounter.textProperty().bind(game.getTime().asString());
		timerCounter.setTextFill(Color.RED);

		// Creates a label for indicating timer
		Label timerLabel = new Label("Time:");
		VBox timerBox = new VBox();
		timerBox.getChildren().addAll(timerLabel,timerCounter);
		timerBox.setAlignment(Pos.CENTER);

		// Adds the face to the button for state indication
		Button smileyFace = new Button();
		smileyFace.getStyleClass().add("run");
		smileyFace.setMinSize(32,32);

		//If the state of the game changes, change the smileyface
		game.stateProperty().addListener(new ChangeListener<GameState>() {

			// Changes the faces on the buttons depending on the game state. Needs a Observable Value to check which Object it is trying to respond to
			@Override
			public void changed(ObservableValue<? extends GameState> observable, GameState oldValue, GameState newValue) {

				// If running change it to the run face
				if (newValue == GameState.RUNNING) {
					smileyFace.getStyleClass().remove("win");
					smileyFace.getStyleClass().remove("lose");
					smileyFace.getStyleClass().add("run");
				}

				// If won change it to the winning face
				if (newValue == GameState.WIN) {
					smileyFace.getStyleClass().add("win");
					smileyFace.getStyleClass().remove("lose");
					smileyFace.getStyleClass().remove("run");
				}

				// If lost change it to the lost face
				if (newValue == GameState.LOSE) {
					smileyFace.getStyleClass().remove("win");
					smileyFace.getStyleClass().add("lose");
					smileyFace.getStyleClass().remove("run");
				}
			}
		});

		// Adds the bombBox, smileyFace and timerBox to the root pane and sets the properties
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.getChildren().addAll(bombBox, smileyFace, timerBox);


		// Resets the game when the face is clicked
		smileyFace.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				game.reset();	
			}
		});

	}
}
