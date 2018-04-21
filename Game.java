package finalGame;
//Tài liệu tham khảo: FaTal Cubez
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

// Referenced classes of package demo:
//            GameBoard, Keyboard

public class Game extends JPanel implements KeyListener, Runnable {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 560;
	public static final Font main = new Font("Times New Roman", 0, 28);
	private Thread game;
	private boolean running;
	private BufferedImage image;
	private GameBoard board;

	public Game() {
		image = new BufferedImage(400, 560, 1);
		setFocusable(true);
		setPreferredSize(new Dimension(400, 560));
		addKeyListener(this);
		board = new GameBoard(200 - GameBoard.BOARD_WIDTH / 2, 560 - GameBoard.BOARD_HEIGHT - 10);
	}

	public void update() {
		board.update();
		Keyboard.update();
	}

	public void render() {
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, 400, 560);
		board.render(g);
		g.dispose();
		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
	}

	
	public void run() {
		int fps = 0;
		int update = 0;
		double nsPerUpdate = 16666666.666666666D;
		double then = System.nanoTime();
		double unprocessed = 0.0D;
		while (running) {
			boolean shouldRender = false;
			double now = System.nanoTime();
			unprocessed += (now - then) / nsPerUpdate;
			then = now;
			while (unprocessed >= 1.0D) {
				update++;
				update();
				unprocessed--;
				shouldRender = true;
			}
			if (shouldRender) {
				fps++;
				render();
				shouldRender = false;
			} else {
				try {
					Thread.sleep(1L);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public synchronized void start() {
		if (running) {
			return;
		} else {
			running = true;
			game = new Thread(this, "Game");
			game.start();
			return;
		}
	}

	public synchronized void stop() {
		if (!running) {
			return;
		} else {
			running = false;
			System.exit(0);
			return;
		}
	}

	public void keyPressed(KeyEvent e) {
		Keyboard.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		Keyboard.keyReleased(e);
	}

	public void keyTyped(KeyEvent keyevent) {
	}

}
