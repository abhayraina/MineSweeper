// This Class is used for setting the properties of each cell in the grid
package minesweeper;

// Import Statements
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameCell {

	// Fields to be used by the class
	private boolean hasBomb = false;

	// BooleanProrperty for marking flags
	private BooleanProperty hasFlag= new SimpleBooleanProperty(false);
	private int number;
	private int x;
	private int y;

	// BooleanProperty marking weather the value of cell is shown or not
	private BooleanProperty show = new SimpleBooleanProperty(false);

	// Revealed cells are those that are shown when the game is lost
	// Can't use Show, because the bomb must be drawn over an unclicked button
	private BooleanProperty revealed = new SimpleBooleanProperty(false);

	public Game game;

	/**
	 * Allows to initialize a game cell with a game object and by setting its coordinates on the grid
	 * @param game A Game Object
	 * @param x	An integer value
	 * @param y An integer value
	 */
	public GameCell(Game game, int x, int y) {
		this.game = game;
		this.setX(x);
		this.setY(y);
	}

	/**
	 * Returns the number of the cell for identification
	 * @return An integer value
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets the number of the cell for identification
	 * @param number An integer value
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Sets the condition of the cell for flags
	 * @param hasFlag A boolean value
	 */
	public void setFlag(boolean hasFlag) {
		this.hasFlag.set(hasFlag);
	}

	/**
	 * Returns the condition of the cell for flags
	 * @return A boolean value
	 */
	public boolean getFlag() {
		return this.hasFlag.getValue();
	}

	/**
	 * Returns the flagProperty for DataBinding
	 * @return A BooleanProperty
	 */
	public BooleanProperty flagProperty() {
		return this.hasFlag;
	}

	/**
	 * Sets the condition of the cell for bombs
	 * @param hasBomb A boolean value
	 */
	public void setBomb(boolean hasBomb) {
		this.hasBomb = hasBomb;
	}

	/**
	 * Returns the condition of the cell for bombs
	 * @return A boolean value
	 */
	public boolean getBomb() {
		return this.hasBomb;
	}

	/**
	 * Used for showing the cell when clicked
	 */
	public void click() {
		show.set(true);
	}

	/**
	 * Returns the x-coordinate of the the cell
	 * @return An integer value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x-coordinate of the cell
	 * @param x An integer value
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the y-coordinate of the cell
	 * @return An integer value
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y-coordinate of the cell
	 * @param y An integer value
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Returns showProperty which allows to listen for changes regarding showing the cell contents
	 * @return A BooleanProperty
	 */
	public BooleanProperty showProperty() {
		return this.show;
	}

	/**
	 * Returns the condition for showing of the cell from the BooleanProperty
	 * @return A boolean Value
	 */
	public boolean getShow() {
		return this.show.get();
	}

	/**
	 * Sets the showing property for the BooleanProperty
	 * @param show A boolean value
	 */
	public void setShow(boolean show) {
		this.show.set(show);
	}

	/**
	 * Returns the condition for showing revealing the cell from the BooleanProperty
	 * @return A boolean value
	 */
	public boolean getRevealed() {
		return revealed.get();
	}

	/**
	 * Sets the revealed property for the BooleanProperty
	 * @param revealed A boolean value
	 */
	public void setRevealed(boolean revealed) {
		this.revealed.set(revealed);
	}

	/**
	 * Returns revealedProperty which allows to listen for changes regarding revealing the cell contents
	 * @return A BooleanProperty
	 */
	public BooleanProperty revealedProperty() {
		return this.revealed;
	}
}
