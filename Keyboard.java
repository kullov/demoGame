package finalGame;
//Tài liệu tham khảo: FaTal Cubez
import java.awt.event.KeyEvent;

public class Keyboard {
	public static boolean pressed[] = new boolean[256];
	public static boolean prev[] = new boolean[256];

	private Keyboard() {
	}

	public static void update() {
		for (int i = 0; i < 4; i++) {
			if (i == 0)
				prev[37] = pressed[37];
			if (i == 1)
				prev[39] = pressed[39];
			if (i == 2)
				prev[38] = pressed[38];
			if (i == 3)
				prev[40] = pressed[40];
		}

	}

	public static void keyPressed(KeyEvent e) {
		pressed[e.getKeyCode()] = true;
	}

	public static void keyReleased(KeyEvent e) {
		pressed[e.getKeyCode()] = false;
	}

	public static boolean typed(int keyEvent) {
		return !pressed[keyEvent] && prev[keyEvent];
	}

}
