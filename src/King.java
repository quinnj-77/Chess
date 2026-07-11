
import javax.swing.ImageIcon;

public class King extends ChessPiece {
	
	private boolean hasMoved;
	
	public King(Boolean isWhite) {
		super(isWhite, new ImageIcon("WhiteKing.png").getImage(), new ImageIcon("BlackKing.png").getImage(), 17, 5, 1);	
		hasMoved = false;
	}
	
	
	public boolean getHasMoved() {
		return hasMoved;
	}


	
	@Override
	public void seePosibleMoves(Tile[][] tiles, int Sx, int Sy) {   
		int startX = Sx;
		int startY = Sy;
		
		for(int x = -1; x < 2; x++) {
			for(int y = -1; y < 2; y++) {
				if( x + y != 0) {
					floodFillKing(startX+x, startY+y, tiles);
				}
			}
		}
		
	}
	
	public void floodFillKing(int x, int y, Tile[][] tiles) {
		if(x < 8 && y < 8 && x >=0 && y>=0) {
			ChessPiece p = tiles[y][x].getMyPiece();
			if(p != null && tiles[y][x].getMyPiece().isWhite() == this.isWhite()) {
				return;
			}
			tiles[y][x].setTargeted(true);
		}
	}
	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
	
	

}
