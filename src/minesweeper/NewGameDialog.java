// This Class is responsible for the New Game Modal, it extends Stage and inherits its methods
package minesweeper;

// Import Statements
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewGameDialog extends Stage {

	// Fields to be used by the class
	public DimensionTextField widthText;
	public DimensionTextField heightText;
	public BombsTextField bombsText;

	/**
	 * Constructor for creating a NewGameDialog
	 * @param game	Takes a Game Object
	 * @param bar	Takes a ButtonBar Object
	 */
	public NewGameDialog(Game game, ButtonBar bar) {

		// Sets the text of the Labels and the DimensionsTextField and BombsTextField
		Label widthLabel = new Label("Width (9-16):");
		widthText = new DimensionTextField("9");
		Label heightLabel = new Label("Height (9-16):");
		heightText = new DimensionTextField("9");
		Label bombsLabel = new Label("Bombs (10-79):");
		bombsText = new BombsTextField("10");

		// Sets up a VBox which will be used as root pane
		VBox root = new VBox();

		// Sets up the GridPane which stores the labels and TextFields
		GridPane gridPane = new GridPane();
		gridPane.addColumn(0, widthLabel, heightLabel, bombsLabel);
		gridPane.addColumn(1, widthText, heightText, bombsText);

		// Declaring Ok and Cancel Button
		Button ok = new Button("Ok");
		Button cancel = new Button("Cancel");

		// Sets up the HBox which holds the buttons
		HBox buttonBox = new HBox();
		buttonBox.setPadding(new Insets(20, 5, 5, 5));
		buttonBox.setSpacing(20);

		// Adds the buttons
		buttonBox.getChildren().addAll(ok, cancel);

		// Sets the alignment of items and adds them to the root VBox
		gridPane.setAlignment(Pos.CENTER);
		buttonBox.setAlignment(Pos.CENTER);
		root.getChildren().addAll(gridPane,buttonBox);

		// Sets up the properties of the scene and the stage and imports CSS
		Scene dialogScene = new Scene(root);
		dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		this.setTitle("New Game");
		this.setResizable(false);
		this.setScene(dialogScene);
		Stage that = this;

		// Commands to be executed when Ok is clicked
		ok.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				// Closes the stage and the ButtonBar
				that.close();
				bar.closeDialog();

				// Creates a new grid of buttons with Validated values for height, width and # of bombs
				game.createNew(widthText.getValidatedValue(), heightText.getValidatedValue(), bombsText.getValidatedValue());
			}
		});

		//This property is bound to the valid property of the UI elements
		BooleanBinding allValid = Bindings.and(Bindings.and(widthText.valid, heightText.valid), bombsText.valid);

		// Disables the Ok button if the validity is questionable
		ok.disableProperty().bind(Bindings.not(allValid));

		// Commands to be executed when cancel is clicked
		cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				// Closes the Stage with no changes to the grid
				that.close();

				// Resumes the timer if there is a game playing in the background and cancel is clicked
				if (game.stateProperty().get() == GameState.RUNNING) {
					game.getTimer().play();
				}

				// Closes the ButtonBar with no changes to the grid
				bar.closeDialog();
			}
		});
	}
}
