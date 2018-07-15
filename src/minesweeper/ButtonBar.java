// This class is used for setting the ButtonBar with options of New Game, Save and Load and their properties
package minesweeper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ButtonBar extends HBox {

	// Declares the NewGameDialog box
	public NewGameDialog dialog = null;

	/**
	 * Makes a ButtonBar with a Game Object and a Stage Object for FileChooser Interface
	 * @param game A Game Object
	 * @param stage A Stage Object
	 */
	public ButtonBar(Game game, Stage stage) {

		// Sets the properties of the HBox
		this.setAlignment(Pos.CENTER);
		this.setSpacing(5);
		this.setPadding(new Insets(5));

		// Sets up the buttons for new game, load game and save game
		Button newGame = new Button("New Game");
		Button loadGame = new Button("Load Game");
		Button saveGame = new Button("Save Game");

		// Adds the buttons to the ButtonBar
		this.getChildren().addAll(newGame, loadGame, saveGame);

		// Instance of ButtonBar is created
		ButtonBar that = this;

		// Commands to be executed once the the newGame button is clicked
		newGame.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {

				// Initializes a NewGameDialog
				if (dialog != null) {
					return;
				}
				game.getTimer().stop();
				dialog = new NewGameDialog(game,that);
				dialog.show();

			}
		});

		// Commands to be executed when save button is clicked
		saveGame.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {

				// Stops the timer
				game.getTimer().stop(); 

				// Sets the properties of a FileChooser
				FileChooser fileChooser = new FileChooser();
				fileChooser.setInitialDirectory(new File("..."));
				fileChooser.setTitle("Save Game");
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text","*.txt"));
				File file = fileChooser.showSaveDialog(stage);


				// If the cancel button is clicked it does not allow to save and replays the game timer
				if (file == null) {
					game.getTimer().play();
					return;
				}

				// Stores the saveData in a list by using a save method from the Game class
				List<String> saveData = game.save();
				try {

					// Initializes the BufferedWriter
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));

					// Saves data to a text file
					for (String data: saveData) {
						writer.write(data);
						writer.newLine();
					}

					// Closes the writer
					writer.close();

					// Catches IOException
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Replays the game timer after saving
				game.getTimer().play();

			}
		});

		// Commands to be executed when the save loadGame button is clicked
		loadGame.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {

				// Stops the timer
				game.getTimer().stop(); 

				// Sets the properties of a FileChooser 
				FileChooser fileChooser = new FileChooser();
				fileChooser.setInitialDirectory(new File("..."));
				fileChooser.setTitle("Save Game");
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
				File file = fileChooser.showOpenDialog(stage);


				// If file is blank does not return anything and replays the timer if the game is running
				if (file == null) {
					if (game.stateProperty().get() == GameState.RUNNING) {
						game.getTimer().play(); 
					}
					return;
				}
				try {

					// Initializes a BufferedReader for reading the text file
					BufferedReader reader = new BufferedReader(new FileReader(file));

					// Declaring a List to temporarily store data
					List<String> loadData = new ArrayList<>();

					// Informs the reader if the the file is readable and adds the data from file to the ArrayList
					while (reader.ready()) {
						loadData.add(reader.readLine());
					}

					// Closes the reader
					reader.close();

					// Loads the information and changes the conditions of the game depending on the input
					game.load(loadData);

					// Catches the exceptions listed below
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		//Disable the save game button if the state is WIN or LOSE
		game.stateProperty().addListener(new ChangeListener<GameState>() {

			//  Observable Value responds to the change in a GameState Object, compares the old value to the new value
			@Override
			public void changed(ObservableValue<? extends GameState> observable, GameState oldValue, GameState newValue) {

				// If the GameState is running it keeps the save button enabled
				if (newValue == GameState.RUNNING) {
					saveGame.setDisable(false);
				}

				// If the GameState is win it disables the save button
				if (newValue == GameState.WIN) {
					saveGame.setDisable(true);
				}

				// If the GameState is lose it disables the save button
				if (newValue == GameState.LOSE) {
					saveGame.setDisable(true);
				}

				// If the GameState is at start disables the save button
				if (newValue == GameState.START) {
					saveGame.setDisable(true);
				}
			}
		});
	}

	/**
	 * Closes the NewGameDialog box by passing a null value
	 */
	public void closeDialog() {
		this.dialog = null;
	}
}
