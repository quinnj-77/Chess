import java.awt.BorderLayout;

import javax.swing.*;


public class ChessFrame {
	
	
	public ChessFrame() {
		
		JFrame window = new JFrame("Chess");
		final int TILE_SIZE = 70;
        final int SIDE_SIZE = 0; 

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        ChessBoard board = new ChessBoard(TILE_SIZE, SIDE_SIZE);
        new ChessListener(board, window);
        
        window.setContentPane(board);
        window.setSize(TILE_SIZE * 9 - 38, TILE_SIZE * 9 + 20); 
        window.setLocationRelativeTo(null); 
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
		
	}

	
}
