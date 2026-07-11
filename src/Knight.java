
import javax.swing.ImageIcon;

public class Knight extends ChessPiece{
	public Knight(boolean isWhite) {
		super(isWhite, new ImageIcon("WhiteKnight.png").getImage(), new ImageIcon("BlackKnight.png").getImage(), 21, 10, 4);
	}
	
	
	@Override
	public void seePosibleMoves(Tile[][] tiles, int Sx, int Sy) {   
		int startX = Sx;
		int startY = Sy;
		
		for(int x = -2; x < 3; x++) {
			for(int y = -2; y < 3; y++) {
				if((x+y) % 2 != 0 && x != 0 && y != 0) {
					floodFillKnight(startX+x, startY+y, tiles);
				}
			}
		}
		
	}
	public void floodFillKnight(int x, int y, Tile[][] tiles) {
		if(x < 8 && y < 8 && x >=0 && y>=0) {
			ChessPiece p = tiles[y][x].getMyPiece();
			if(p != null && tiles[y][x].getMyPiece().isWhite() == this.isWhite()) {
				return;
			}
			tiles[y][x].setTargeted(true);
		}
	}

}


