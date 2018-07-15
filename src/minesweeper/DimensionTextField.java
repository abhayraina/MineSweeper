// This class is used to check and set the dimensions acquired from the TexField in New Game Modal
package minesweeper;

// Import Statements
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class DimensionTextField extends TextField {

	// BooleanProperty which is used for binding and helps for listening for changes in the TextFields
	BooleanProperty valid = new SimpleBooleanProperty();

	/**
	 * This Constructor takes in a String Value and sends this value to the constructor in the superclass and inherits all the public methods of the super class
	 * @param string A String Value
	 */
	public DimensionTextField(String string) {

		// Inherits super class methods 
		super(string);

		// Checks if the input conditions are met 
		validate();

		// A change listener to the BooleanProperty which checks if the conditions are met for removing the red border for error or not
		valid.addListener(new ChangeListener<Boolean>() {

			// Checks if the values entered are acceptable and changes the border of TextField to red in order to alert the user of their selection and needs an ObservableValue, which determines what data type's change it is responding to
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				// If new value is valid remove the red border effect or adds it
				if (newValue) {
					getStyleClass().remove("error");
				}
				else {
					getStyleClass().add("error");
				}

			}
		});

		// Refers to the TextField
		this.focusedProperty().addListener(new ChangeListener<Boolean>() {

			// If there is no change still keeps the effects and needs an ObserableValue, which determines which data type's change it is responding to
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				// Checks if the focus is lost
				if (!newValue) { 
					validate();
				}
			}
		});
	}

	/**
	 * This method checks user input and if it matches the range and its restrictions
	 */
	private void validate() {

		// Only allows one and two digits numbers from 0-9
		if(getText().matches("[0-9]{1,2}+")) {

			// Stores the value temporarily for checking
			int counter = Integer.valueOf(getText());

			// Checks if the value matches the expectation and sets BooleanProperty to true
			if (counter >= 9 && counter <= 16) {
				valid.set(true);
				return;
			}
		}

		// Sets it to false if it is the same
		valid.set(false);
	}

	/**
	 * Returns the value that is acceptable
	 * @return An Integer Object
	 */
	public Integer getValidatedValue() {

		// Checks if the TextField is left blank and prevents the user from proceeding depending on the BooleanProperty
		if (!valid.get()) {
			return null;
		}

		// Returns the Integer Object
		return Integer.valueOf(getText());
	}
}
