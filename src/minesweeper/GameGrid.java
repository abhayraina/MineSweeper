// This Class is responsible for creating the button grid
package minesweeper;

// Import Statements
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameGrid extends GridPane {

	/**
	 * Blank Constructor
	 */
	public GameGrid() {}

	/**
	 * Clears the previous grid and initializes a new one
	 * @param game Takes a Game Object
	 */
	public void changeGame(Game game) {
		this.getChildren().clear();
		for (int x = 0; x < game.height; x++) {
			for (int y = 0; y < game.width; y++) {
				ButtonCell button = new ButtonCell(game.getCells()[x][y]);
				this.add(button, x, y);
			}
		}

		// Resizes the stage according to the scene size
		Stage stage = (Stage) this.getScene().getWindow();
		stage.sizeToScene();
	}
}
