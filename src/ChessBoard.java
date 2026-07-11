
import java.awt.*;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ChessBoard extends JPanel  {

	private Tile[][] tiles;
	private int tileSize;
	private int sideSize;

	private final int ANIMATION_FPS = 60;
	private final int ANIMATION_DELAY_MS = 1000 / ANIMATION_FPS;
	private int animationDurationMS;
	private ChessPiece movingPiece; 


	public ChessBoard(int tilesize, int sideSize) {
		animationDurationMS = 250;
		this.sideSize = sideSize;
		this.tileSize = tilesize;
		this.movingPiece = null;
		tiles = new Tile[8][8];


		ChessPiece[][] startSetup = {
				{new Rook(false), new Knight(false), new Bishop(false), 
					new Queen(false), new King(false), new Bishop(false),
					new Knight(false), new Rook(false)},
				{new Pawn(false), new Pawn(false), new Pawn(false),
						new Pawn(false), new Pawn(false), new Pawn(false),
						new Pawn(false), new Pawn(false)},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{new Pawn(true), new Pawn(true), new Pawn(true),
					new Pawn(true), new Pawn(true), new Pawn(true),
					new Pawn(true), new Pawn(true)},
				{new Rook(true), new Knight(true), new Bishop(true), 
						new Queen(true), new King(true), new Bishop(true),
						new Knight(true), new Rook(true)}
		};
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				Tile tile = new Tile(x, y, this.tileSize, this.sideSize, this);
				tiles[y][x] = tile; 
				tile.setMyPiece(startSetup[y][x]);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color c;
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				if((x+y) % 2 == 1) {
					c = new Color(45, 113, 161);
				}
				else {
					c = new Color(137, 207, 240);
				}
				tiles[y][x].draw(g, c);
			}
		}
		if(movingPiece != null) {
			movingPiece.draw(g, this.tileSize, this);
		}
	}	

	public ChessPiece getMovingPiece() {
		return movingPiece;
	}

	public void setMovingPiece(ChessPiece movingPiece) {
		this.movingPiece = movingPiece;
	}



	public int getTileSize() {
		return this.tileSize;
	}

	public int getSideSize() {
		return this.sideSize;
	}

	public Tile[][] getTiles() {
		return this.tiles;
	}

	public int getAnimationDurationMS() {
		return animationDurationMS;
	}

	public void setAnimationDurationMS(int animationDurationMS) {
		this.animationDurationMS = animationDurationMS;
	}

	public int getANIMATION_DELAY_MS() {
		return ANIMATION_DELAY_MS;
	}
}