// This Class is used to handle events generated by the cell when it is clicked upon
package minesweeper;

// Import Statements
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ButtonCell extends Button {

	// Fields to be used by the class
	public GameCell cell;

	/**
	 * This constructor creates cell with buttons to handle changing events and sets the properties
	 * @param cell A GameCell Object
	 */
	ButtonCell(GameCell cell) {

		// Initializes the cell by setting its size and the font
		this.cell = cell;
		this.setMinSize(20, 20);
		this.setFont(Font.font("Monospaced", FontWeight.EXTRA_BOLD, 14));

		// Loads the CSS properties for the cell 
		this.getStyleClass().add("cell-button");

		// Listens actively for changes to the BooleanProperty when a button is clicked
		cell.showProperty().addListener(new ChangeListener<Boolean>() {

			// Needs an Observable Value to check which Objects change its listening to
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				// If there is a change and depending on the change shows a result
				if (newValue == true) {
					show();
				}
			}
		});

		// Listens actively for changes to the BooleanProperty only for revealing the bomb location
		cell.revealedProperty().addListener(new ChangeListener<Boolean>() {

			// Needs an Observable Value to check the Object type it is responding to
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				// If there is a change to the revealedProperty it shows the positions of the bombs
				if(newValue == true) {
					reveal();
				}
			}
		});

		// Listens actively for changes to the BooleanProperty only for flagging or unflagging
		cell.flagProperty().addListener(new ChangeListener<Boolean>() {

			// Needs an Observable Value to check the Object type it is responding to
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				// If there is a change to flagProperty it flags that cell, if not un-flags it
				if(newValue == true) {
					mark();
				}
				else {
					unmark();
				}
			}
		});

		// Event Handler for revealing the cells and for marking them
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				// If it is left click then shows the the cell or its neighboring cells too, depending on the result
				if (event.getButton() == MouseButton.PRIMARY) {
					cell.game.show(cell.getX(), cell.getY());
				}

				// Marks and unmarks cells depending on the right click
				if(event.getButton() == MouseButton.SECONDARY) {
					cell.game.mark(cell.getX(),cell.getY());
				}
			}
		});
	}

	/**
	 * This method checks if it needs to show the bombs, display blank areas for 0s or display the number of the cell when clicked
	 */
	public void show() {

		// If bomb is clicked
		if (cell.getBomb()) {
			this.setText("");
			this.getStyleClass().add("bomb");
			this.getStyleClass().add("clicked");
			return;
		}

		// If the clicked cell is blank
		if (cell.getNumber() == 0) {
			this.setText("");
		} 

		// If there is a number underneath
		else {

			// Sets the number of cell
			this.setText(Integer.toString(cell.getNumber()));

			// Color the numbers according to original Minesweeper colors
			this.getStyleClass().add("number" + cell.getNumber());
		}

		// Arms the button so they are selected
		this.arm();

		// Adds CSS properties for showing
		this.getStyleClass().add("show");
	}

	/**
	 * Reveals other bombs by adding them to the buttons
	 */
	public void reveal() {
		if (cell.getBomb()) {
			this.getStyleClass().add("bomb");
		}
	}

	/**
	 * Adds the flag to the button 
	 */
	public void mark() {
		this.getStyleClass().add("flag");
	}

	/**
	 * Removes the flag from the button
	 */
	private void unmark() {
		this.getStyleClass().remove("flag");
	}
}
