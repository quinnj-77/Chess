

import javax.swing.ImageIcon;

public class Bishop extends ChessPiece{

	public Bishop(boolean isWhite) {
		super(isWhite, new ImageIcon("WhiteBishop.png").getImage(), new ImageIcon("BlackBishop.png").getImage(), 28, 15, 6);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void seePosibleMoves(Tile[][] tiles, int Sx, int Sy) {   
		int startX = Sx;
		int startY = Sy;
		
		for(int x = -1; x < 2; x += 2) {
			for(int y = -1; y < 2; y +=2) {
				if( x + y != 0) {
					super.floodFill(startX+x, startY+y, x, y, tiles);
				}
			}
		}
		
	}
}