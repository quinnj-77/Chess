
import javax.swing.ImageIcon;

public class Queen extends ChessPiece {

	public Queen(Boolean isWhite) {
		super(isWhite, new ImageIcon("WhiteQueen.png").getImage(), new ImageIcon("BlackQueen.png").getImage(),  13, 0, -2);
	}
	


	
	@Override
	public void seePosibleMoves(Tile[][] tiles, int Sx, int Sy) {   
		int startX = Sx;
		int startY = Sy;
		
		for(int x = -1; x < 2; x++) {
			for(int y = -1; y < 2; y++) {
				if( x + y != 0) {
					super.floodFill(startX+x, startY+y, x, y, tiles);
				}
			}
		}
		
	}

}
