package finalGame;
//Tài liệu tham khảo: FaTal Cubez
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Random;

import javax.swing.JOptionPane;

public class GameBoard {

	public static final int ROWS = 4;
	public static final int COLS = 4;
	private final int startingTiles = 2;
	private static Tile board[][];
	private static boolean dead;
	private static boolean won;
	private BufferedImage gameBoard;
	private BufferedImage finalBoard;
	private int x;
	private int y;
	private static int score;
	private static int highScore;
	private Font scoreFont;
	private static int SPACING;
	public static int BOARD_WIDTH;
	public static int BOARD_HEIGHT;
	public static boolean hasStarted;

	static {
		SPACING = 10;
		BOARD_WIDTH = 5 * SPACING + 320;
		BOARD_HEIGHT = 5 * SPACING + 320;
	}

	public GameBoard(int x, int y) {
		score = 0;

		scoreFont = Game.main.deriveFont(24F);
		this.x = x;
		this.y = y;
		board = new Tile[4][4];
		gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, 1);
		finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, 1);
		createBoardImage();
		start();
	}

	private void createBoardImage() {
		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		g.setColor(Color.lightGray);
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				int x = SPACING + SPACING * col + 80 * col;
				int y = SPACING + SPACING * row + 80 * row;
				g.fillRoundRect(x, y, 80, 80, 15, 15);
			}

		}

	}

	private static void start() {
		for (int i = 0; i < 2; i++)
			spawmRandom();

	}

	private static void spawmRandom() {
		Random random = new Random();
		boolean notValid = true;
		while (notValid) {
			int location = random.nextInt(16);
			int row = location / 4;
			int col = location % 4;
			Tile current = board[row][col];
			if (current == null) {
				int value = random.nextInt(10) >= 9 ? 4 : 2;
				Tile tile = new Tile(value, getTileX(col), getTileY(row));
				board[row][col] = tile;
				notValid = false;
			}
		}
	}

	public static int getTileX(int col) {
		return SPACING + col * 80 + col * SPACING;
	}

	public static int getTileY(int row) {
		return SPACING + row * 80 + row * SPACING;
	}

	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
		g2d.drawImage(gameBoard, 0, 0, null);
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				Tile current = board[row][col];
				if (current != null)
					current.render(g2d);
			}

		}

		g.drawImage(finalBoard, x, y, null);
		g2d.dispose();
		g.setColor(Color.green);
		g.setFont(scoreFont);
		g.drawString((new StringBuilder("Score: ")).append(score).toString(), 30, 40);
		g.setColor(Color.red);
		g.drawString((new StringBuilder("Best Score: ")).append(highScore).toString(), 400 - DrawUtils.getMessageWidth((new StringBuilder("Best Score: ")).append(highScore).toString(), scoreFont, g) - 20, 40);
	}

	public void update() {
		checkKey();

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				Tile current = board[row][col];
				if (current != null) {
					current.update();
					resetPosition(current, row, col);
					if (current.getValue() == 2048)
						won = true;

				}
			}

		}

	}

	private void resetPosition(Tile current, int row, int col) {
		if (current == null)
			return;
		int x = getTileX(col);
		int y = getTileY(row);
		int distX = current.getX() - x;
		int distY = current.getY() - y;
		if (Math.abs(distX) < 30)
			current.setX(current.getX() - distX);
		if (Math.abs(distY) < 30)
			current.setY(current.getY() - distY);
		if (distX < 0)
			current.setX(current.getX() + 30);
		if (distY < 0)
			current.setY(current.getY() + 30);
		if (distX > 0)
			current.setX(current.getX() - 30);
		if (distY > 0)
			current.setY(current.getY() - 30);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	private boolean move(int row, int col, int horizontalDirection, int verticalDirection, Direction dir) {
		boolean canMove = false;
		Tile current = board[row][col];
		if (current == null)
			return false;
		boolean move = true;
		int newCol = col;
		int newRow = row;
		while (move) {
			newCol += horizontalDirection;
			newRow += verticalDirection;
			if (checkOutOfBounds(dir, newRow, newCol))
				break;
			if (board[newRow][newCol] == null) {
				board[newRow][newCol] = current;
				board[newRow - verticalDirection][newCol - horizontalDirection] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
				canMove = true;
			} else if (board[newRow][newCol].getValue() == current.getValue() && board[newRow][newCol].canCombine()) {
				board[newRow][newCol].setCanCombine(false);
				board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2);
				canMove = true;
				board[newRow - verticalDirection][newCol - horizontalDirection] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
				board[newRow][newCol].setCombineAnimation(true);
				score += board[newRow][newCol].getValue();
			} else {
				move = false;
			}
		}
		return canMove;
	}

	private boolean checkOutOfBounds(Direction dir, int row, int col) {
		if (dir == Direction.LEFT)
			return col < 0;
		if (dir == Direction.RIGHT)
			return col > 3;
		if (dir == Direction.DOWN)
			return row > 3;
		if (dir == Direction.UP)
			return row < 0;
		else
			return false;
	}

	public static void reset() {
		board = new Tile[ROWS][COLS];
		start();
		dead = false;
		won = false;
		hasStarted = false;
		score = 0;

	}

	private void moveTiles(Direction dir) {
		boolean canMove = false;
		int horizontalDirection = 0;
		int verticalDirection = 0;
		if (dir == Direction.LEFT) {
			horizontalDirection = -1;
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 4; col++)
					if (!canMove)
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					else
						move(row, col, horizontalDirection, verticalDirection, dir);

			}

		} else if (dir == Direction.RIGHT) {
			horizontalDirection = 1;
			for (int row = 0; row < 4; row++) {
				for (int col = 3; col >= 0; col--)
					if (!canMove)
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					else
						move(row, col, horizontalDirection, verticalDirection, dir);

			}

		} else if (dir == Direction.DOWN) {
			verticalDirection = 1;
			for (int row = 3; row >= 0; row--) {
				for (int col = 0; col < 4; col++)
					if (!canMove)
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					else
						move(row, col, horizontalDirection, verticalDirection, dir);

			}

		} else if (dir == Direction.UP) {
			verticalDirection = -1;
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 4; col++)
					if (!canMove)
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					else
						move(row, col, horizontalDirection, verticalDirection, dir);

			}

		} else {
			System.out.println((new StringBuilder()).append(dir).append(" is not a valid direction.").toString());
		}
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				Tile current = board[row][col];
				if (current != null)
					current.setCanCombine(true);
			}

		}

		if (canMove) {
			spawmRandom();
			checkDead();
		}
	}

	private void checkDead() {
		if (score > highScore) {
			highScore = score;
		}
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				if (board[row][col] == null)
					return;
				if (checkSurroundingTiles(row, col, board[row][col]))
					return;
			}
		}
		dead = true;
		JOptionPane.showMessageDialog(null, "You lose!");
		reset();
	}

	private boolean checkSurroundingTiles(int row, int col, Tile current) {
		if (row > 0) {
			Tile check = board[row - 1][col];
			if (check == null)
				return true;
			if (current.getValue() == check.getValue())
				return true;
		}
		if (row < 3) {
			Tile check = board[row + 1][col];
			if (check == null)
				return true;
			if (current.getValue() == check.getValue())
				return true;
		}
		if (col > 0) {
			Tile check = board[row][col - 1];
			if (check == null)
				return true;
			if (current.getValue() == check.getValue())
				return true;
		}
		if (col < 3) {
			Tile check = board[row][col + 1];
			if (check == null)
				return true;
			if (current.getValue() == check.getValue())
				return true;
		}
		return false;
	}

	private void checkKey() {
		if (Keyboard.typed(37)) {
			moveTiles(Direction.LEFT);
			if (!hasStarted)
				hasStarted = true;
		}
		if (Keyboard.typed(39)) {
			moveTiles(Direction.RIGHT);
			if (!hasStarted)
				hasStarted = true;
		}
		if (Keyboard.typed(38)) {
			moveTiles(Direction.UP);
			if (!hasStarted)
				hasStarted = true;
		}
		if (Keyboard.typed(40)) {
			moveTiles(Direction.DOWN);
			if (!hasStarted)
				hasStarted = true;
		}
	}

}
