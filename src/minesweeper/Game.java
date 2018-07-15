// This Class creates a Game Object
package minesweeper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Game {

	// Fields to be used by the class
	int width;
	int height;
	GameCell[][] cells;
	private Timeline timer;

	//Time property, the counter and the timer are bound to this
	private IntegerProperty time = new SimpleIntegerProperty(); 

	// Recreate game if player clicked directly on bomb
	private boolean newGame;

	// List of flagged cells
	private ObservableList<GameCell> markedList = FXCollections.observableArrayList();

	// List of cells that have bombs
	private ObservableList<GameCell> bombList = FXCollections.observableArrayList();

	private GameGrid grid;
	private int numberOfBombs;

	// Property used to check when the game state changes, in order to show the modal dialog, stop the timer, etc.
	private ObjectProperty<GameState> state = new SimpleObjectProperty<>(GameState.START); 
	private int cellsLeft;

	/**
	 * Creates a default Game object using the GameGrid object
	 * @param grid A GameGrid object
	 */
	public Game(GameGrid grid) {
		this.grid = grid;
		timer = new Timeline();
	}

	/**
	 * Creates a new game with specified number of bombs and with width and height specified
	 * @param width An integer value
	 * @param height An integer value
	 * @param numberOfBombs An integer value
	 */
	public void createNew(int width, int height, int numberOfBombs) {

		// Sets the number of bombs, the width and the height
		this.width = width;
		this.height = height;
		this.numberOfBombs = numberOfBombs;

		// Sets new game to true indicating a new game needs to start
		newGame = true;

		// Creates a grid with randomly assigned bombs and does the checking for placement of numbers
		createRandom(width, height, numberOfBombs);

		// Changes the GameState to indicate it is running
		this.state.set(GameState.RUNNING);

		// Sets up the timer
		time.set(0);
		timer.stop();
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.getKeyFrames().clear();

		//Starts the timer 
		timer.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				//Increase the timer by 1 second
				time.set(time.get() + 1);
			}
		}));

		// Changes the properties of the GameGrid depending on the changes made to this Game Object
		grid.changeGame(this);
	}

	/**
	 * Creates a grid with randomized bomb location and sets the neighboring numbers
	 * @param width An integer value
	 * @param height An integer value
	 * @param numberOfBombs An integer value
	 */
	public	void createRandom(int width, int height, int numberOfBombs) {

		// Sets the new game to true implying there is a new game
		newGame = true;

		// Changes the GameState indicating that it is running
		state.set(GameState.RUNNING);

		// Clears the Observable List of cells with bombs
		bombList.clear();

		// Clears the Observable List of cells that are flagged
		markedList.clear();

		// Holds the cells that are not occupied
		cellsLeft = width * height - numberOfBombs;

		// Creates a new grid of cells
		GameCell[][] newGrid = new GameCell[height][width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				newGrid[y][x] = new GameCell(this, x, y);
			}
		}

		// Stores the amount of bombs that still need to be placed
		int bombsToPlace = numberOfBombs;
		while (bombsToPlace != 0) {

			// Generates a random X and Y position for a bomb
			int bombLocationX = (int) (Math.random() * width);
			int bombLocationY = (int) (Math.random() * height);

			// If a cell does not have bomb, place it or try again
			if (!newGrid[bombLocationY][bombLocationX].getBomb()) {
				newGrid[bombLocationY][bombLocationX].setBomb(true);
				newGrid[bombLocationY][bombLocationX].setNumber(-1);
				bombsToPlace--;
				getBombList().add(newGrid[bombLocationY][bombLocationX]);
			}
		}

		// Put numbers on all other remaining cells
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				// Ignore cell if it has bomb otherwise put the number
				if (!newGrid[y][x].getBomb()) {

					// Get number of neighboring bombs
					int neighborBombs = 0;

					// Check North of cell
					if (y - 1 >= 0) {
						if (newGrid[y - 1][x].getBomb()) {
							neighborBombs++;
						}
					}
					// Check South of the cell
					if (y + 1 < height) {
						if (newGrid[y + 1][x].getBomb()) {
							neighborBombs++;
						}
					}
					// Check East of the cell
					if (x + 1 < width) {
						if (newGrid[y][x + 1].getBomb()) {
							neighborBombs++;
						}
					}
					// Check West of the cell
					if (x - 1 >= 0) {
						if (newGrid[y][x - 1].getBomb()) {
							neighborBombs++;
						}
					}
					// Check the North-East of the cell
					if ((y - 1 >= 0) && (x + 1 < width)) {
						if (newGrid[y - 1][x + 1].getBomb()) {
							neighborBombs++;
						}
					}

					// Check the North-West of the cell
					if ((y - 1 >= 0) && (x - 1 >= 0)) {
						if (newGrid[y - 1][x - 1].getBomb()) {
							neighborBombs++;
						}
					}

					// Check South-East of the cell
					if ((y + 1 < height) && (x + 1 < width)) {
						if (newGrid[y + 1][x + 1].getBomb()) {
							neighborBombs++;
						}
					}

					// Check South-West of the cell
					if ((y + 1 < height) && (x - 1 >= 0)) {
						if (newGrid[y + 1][x - 1].getBomb()) {
							neighborBombs++;
						}
					}

					// Sets the number of the cell indicating surrounding bombs
					newGrid[y][x].setNumber(neighborBombs);
				}
			}
		}

		// Adds the newGrid to its parent member 
		this.cells = newGrid;
	}

	/**
	 * This method returns the GameCell
	 * @return A 2D array of GameCell Objects
	 */
	public GameCell[][] getCells() {
		return this.cells;
	}

	/**
	 * Shows the cell that is clicked upon
	 * @param x An integer value
	 * @param y An integer value
	 */
	public void show(int x, int y) {

		// Ignores all events if the game is not Running (like WIN or LOSE)
		if(state.get() != GameState.RUNNING) {
			return;
		}
		//Start the timer at the first click
		if(newGame) {
			timer.playFromStart();
		}

		// If the click is outside the grid
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return;
		}

		// Stores the cell clicked upon
		GameCell currentCell = cells[y][x];

		// Does not allow to click Flagged cells
		if (currentCell.getFlag()) {
			return;
		}

		// Checks if the cell contains a bomb
		if (currentCell.getBomb()) {

			// If newGame is true resets the game
			if (newGame) {
				reset();
				show(x, y);
				return;
			}

			// Stops the timer and changes the GameState to lose
			timer.stop();
			state.set(GameState.LOSE);

			// Shows the clicked cell
			currentCell.click();

			// Clicked on a bomb, shows all other bombs
			for (GameCell cell : getBombList()) {
				cell.setRevealed(true);
			}
			return;
		}

		// If the Cell is shown normally
		if (currentCell.getShow() == true) {
			return;
		}

		// Shows the clicked cell
		currentCell.click();

		// Changes newGame to false so the user can still play
		newGame = false;

		// Reduces the number of cells without bombs left
		cellsLeft --;

		// If all cells without bombs are shown changes the GameState to win and stops the timer
		if (cellsLeft == 0) {
			state.set(GameState.WIN);
			timer.stop();
		}

		// If a cell with number 0 is clicked
		if (currentCell.getNumber() == 0) {

			// Calls all other directions recursively
			show(x + 1, y);
			show(x + 1, y + 1);
			show(x, y + 1);
			show(x - 1, y + 1);
			show(x - 1, y);
			show(x - 1, y - 1);
			show(x, y - 1);
			show(x + 1, y - 1);
		}
	}

	/**
	 * Used for flagging a cell and counting the amount of flags
	 * @param x An integer value
	 * @param y An integer value
	 */
	public void mark(int x, int y) {

		// If GameState is not running prevents user from flagging
		if (state.get() != GameState.RUNNING) {
			return;
		}

		// Selects the cell
		GameCell currentCell = cells[y][x];

		// Can't mark an already shown cell
		if (currentCell.getShow()) {
			return;
		}
		// If cell has flag, takes it off
		if (currentCell.getFlag()) {
			currentCell.setFlag(false);
			getMarkedList().remove(currentCell);
			return;
		}

		// Prevents from putting more flags than bombs
		if (getMarkedList().size() == getBombList().size()) {
			return;
		}

		// Sets the flag condition of the selected cell to true
		currentCell.setFlag(true);

		// Calls the Observable List of GameCell objects and adds the flagged cell
		getMarkedList().add(currentCell);

	}

	/**
	 * Returns the list of marked GameCells
	 * @return An Observable List of GameCells
	 */
	public ObservableList<GameCell> getMarkedList() {
		return markedList;
	}

	/**
	 * Sets the data in the list of marked GameCells
	 * @param markedList An Observable List of GameCells
	 */
	public void setMarkedList(ObservableList<GameCell> markedList) {
		this.markedList = markedList;
	}

	/**
	 * Returns the list of GameCells with bombs
	 * @return An Observable List of GameCells
	 */
	public ObservableList<GameCell> getBombList() {
		return bombList;
	}

	/**
	 * Sets the data in the list of GameCells with bombs
	 * @param bombList
	 */
	public void setBombList(ObservableList<GameCell> bombList) {
		this.bombList = bombList;
	}

	/**
	 * Returns the timer
	 * @return A Timeline object
	 */
	public Timeline getTimer() {
		return timer;
	}

	/**
	 * Sets the timer
	 * @param timer A Timeline object
	 */
	public void setTimer(Timeline timer) {
		this.timer = timer;
	}

	/**
	 * Returns the time
	 * @return An IntegerProperty
	 */
	public IntegerProperty getTime() {
		return time;
	}

	/**
	 * Sets the time 
	 * @param time An IntegerProperty
	 */
	public void setTime(IntegerProperty time) {
		this.time = time;
	}

	/**
	 * Resets the whole grid and the game
	 */
	public void reset() {

		// Reassigns the bombs and numbers
		createRandom(width, height, numberOfBombs);

		// Applies the changes
		grid.changeGame(this);

		// Resets the time
		this.time.set(0);

		// Stops the timer
		this.timer.stop();

		// Sets the GameState to running
		state.set(GameState.RUNNING);

	}

	/**
	 * Takes a list and loads its content into the places needed by the game
	 * @param fileContent A String List
	 */
	public void load(List<String> fileContent) {

		// Clears the bombList and the markedList
		bombList.clear();
		markedList.clear();

		// Sets newGame to false
		this.newGame = false;

		// Sets the GameState to running
		state.set(GameState.RUNNING);

		// Restarts the timer
		timer.playFromStart();

		// Calculates the amount of cells left without bombs
		cellsLeft = width * height - numberOfBombs;

		// Runs through the contents of the list and returns false if there are no contents stopping the reading process
		Iterator<String> it = fileContent.iterator();

		// Sets the time
		time.set(Integer.valueOf(it.next()));

		// Sets the width of grid
		width = Integer.valueOf(it.next());

		// Sets the height of the grid
		height = Integer.valueOf(it.next());

		// Calculates the total amount of cells
		cellsLeft = width * height;

		// Stores the number of GameCells
		cells = new GameCell[height][width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				GameCell currentCell = new GameCell(this, x, y);
				cells[y][x] = currentCell;
			}
		}

		// Applies the changes to the GameGrid
		grid.changeGame(this);

		// Gets property of each button such as the number, its showing status and flags
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				GameCell currentCell = cells[y][x];
				currentCell.setNumber(Integer.valueOf(it.next()));
				currentCell.setShow(Boolean.valueOf(it.next()));
				currentCell.setFlag(Boolean.valueOf(it.next()));
				if(currentCell.getShow()) {
					cellsLeft--;
				}

				// Indicates it contains a bomb
				if (currentCell.getNumber() == -1) {
					bombList.add(currentCell);
					currentCell.setBomb(true);
					cellsLeft--;
				}

				// Indicates it has a flag
				if (currentCell.getFlag()) {
					markedList.add(currentCell);
				}
			}
		}

	}

	/**
	 * This method is used for saving the game
	 * @return A String List
	 */
	public List<String> save() {
		List<String> saveText = new ArrayList<>();

		// Adds the current time
		saveText.add(String.valueOf(time.get()));

		// Adds the width of the grid
		saveText.add(String.valueOf(width));

		// Adds the height of the grid
		saveText.add(String.valueOf(height));

		// Adds the properties of GameCell such as the number, show status and the flag
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				GameCell currentCell = cells[y][x];
				saveText.add(String.valueOf(currentCell.getNumber()));
				saveText.add(String.valueOf(currentCell.getShow()));
				saveText.add(String.valueOf(currentCell.getFlag()));
			}
		}

		// Returns the saveText List
		return saveText;
	}

	/**
	 * Returns the stateProperty
	 * @return An ObjectProperty
	 */
	public ObjectProperty<GameState> stateProperty(){
		return this.state;
	}
}
