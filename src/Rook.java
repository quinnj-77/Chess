
import javax.swing.ImageIcon;

public class Rook extends ChessPiece {
	
	private boolean hasMoved;
 
	public Rook(boolean isWhite) {
		super(isWhite, new ImageIcon("WhiteRook.png").getImage(), new ImageIcon("BlackRook.png").getImage(), 28, 13, 4);
		hasMoved = false;
	}
	@Override
	public void seePosibleMoves(Tile[][] tiles, int Sx, int Sy) {   
		int startX = Sx;
		int startY = Sy;
		
		for(int x = -1; x < 2; x++) {
			for(int y = -1; y < 2; y++) {
				if((x+y) % 2 != 0) {
					super.floodFill(startX+x, startY+y, x, y, tiles);
				}
			}
		}
		
	}

	public boolean getHasMoved() {
		return hasMoved;
	}


	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
}








