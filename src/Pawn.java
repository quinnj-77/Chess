
import javax.swing.ImageIcon;

public class Pawn extends ChessPiece{

	public Pawn(boolean isWhite) {
		super(isWhite, new ImageIcon("WhitePawn.png").getImage(), new ImageIcon("BlackPawn.png").getImage(), 32, 10, 1);
	}
	@Override
	public void seePosibleMoves(Tile[][] tiles, int Sx, int Sy) {   
		int startX = Sx;
		int startY = Sy;
		System.out.println("HERE"+ startY);
		
		if(startY == 6) {
			floodFillPawn(startX,startY-2, 0, tiles);
		}
		for(int i = -1; i < 2; i++) {
			floodFillPawn(startX+i,startY-1, i, tiles);
		}
	

	}
	
	public void floodFillPawn(int x, int y, int dir,  Tile[][] tiles) {
		if(x < 8 && y < 8 && x >=0 && y>=0) {

			ChessPiece p = tiles[x][y].getMyPiece();
			if(dir == 0) {
				if(p == null) {
					System.out.println("YEp"+x+y);
					tiles[y][x].setTargeted(true);
				}
			}
			else {
				if(p != null && p.isWhite() != this.isWhite()) {
					tiles[y][x].setTargeted(true);
				}
			}
		}
	}
}