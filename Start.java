package finalGame;

//Tài liệu tham khảo: FaTal Cubez
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Start {
	public Start() {
	}

	public static void main(String args[]) {
		JFrame window = new JFrame("2048");
		JMenuBar menuBar = new JMenuBar();
		window.setJMenuBar(menuBar);

		JMenuItem reset = new JMenuItem("Reset");
		menuBar.add(reset);

		JMenuItem exit = new JMenuItem("Exit");
		menuBar.add(exit);

		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GameBoard.reset();
			}
		});

		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				window.setVisible(false);
			}
		});

		Game game = new Game();
		window.setDefaultCloseOperation(3);
		window.setResizable(false);
		window.add(game);
		window.pack();
		window.dispose();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		game.start();
	}

}
