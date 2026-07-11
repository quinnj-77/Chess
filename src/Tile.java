
import java.awt.*;

public class Tile {
	private int myXCord;
	private int myYCord;
	private ChessPiece myPiece;
	private boolean isACastleTile;
	private ChessBoard b;
	private boolean isdragedOn;
	private int tileSize;
	private int sideSize;
	private boolean isTargeted;
	private boolean isActivated;


	public void seeMoves(Tile[][] tiles) {
		myPiece.seePosibleMoves(tiles, myXCord, myYCord );
	}
	
	public boolean isTargeted() {
		return isTargeted;
	}
	public void setTargeted(boolean isTargeted) {
		this.isTargeted = isTargeted;
	}
	public boolean isActivated() {
		return isActivated;
	}
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}
	public boolean isIsdragedOn() {
		return isdragedOn;
	}
	public void setIsdragedOn(boolean isdragedOn) {
		this.isdragedOn = isdragedOn;
	}
	public Tile(int x, int y, int size, int sideSize, ChessBoard board) {
		isACastleTile = false;
		this.myXCord = x;
		this.tileSize = size;
		this.b = board;
		this.myYCord = y;
		this.myPiece = null;
		this.isdragedOn = false;
		this.sideSize = sideSize;
		this.isTargeted = false;


	}
	public void draw(Graphics g,  Color c) {
		int xCord = myXCord*tileSize+sideSize; //+20
		int yCord = myYCord*tileSize+sideSize; //+20
		Color color = c;
		
		if(this.isActivated) {
			if(c.equals(new Color(45, 113, 161))) {
				color = new Color(66, 169, 157);
			}
			else {
				color = new Color(122, 226, 205);
			}
		}
		if(this.isdragedOn) {
			g.setColor(Color.yellow);
			g.drawRect(xCord, yCord, tileSize, tileSize);
			g.fillRect(xCord, yCord, tileSize, tileSize);
			g.setColor(color);
			int offset = 6;
			g.fillRect(xCord+(offset/2), yCord+(offset/2), tileSize-6, tileSize-offset);
		}
		else {
			g.setColor(color);
			g.fillRect(xCord, yCord, tileSize, tileSize);
		}
		if(this.isTargeted) {
			System.out.println("*&*");
			g.setColor(Color.DARK_GRAY);
			int offset = 50;
			g.fillRect(xCord+(offset/2), yCord+(offset/2), tileSize-6, tileSize-offset);
		}
		
		if(myPiece != null && (b.getMovingPiece() == null || !b.getMovingPiece().equals(myPiece))) {
			int pieceSize = myPiece.getMySize();
			int ratio = myPiece.getMyRatio();
			int bottomDistance = myPiece.getMyBottomDistance();
			myPiece.setX(xCord+(pieceSize/2));
			myPiece.setY(yCord+(pieceSize/2)-bottomDistance);
			g.drawImage(myPiece.getImage(), xCord+(pieceSize/2), yCord+(pieceSize/2)-bottomDistance, tileSize-pieceSize, tileSize-pieceSize+ratio, b);
		}
	}

	
	public int getMyXCord() {
		return myXCord;
	}
	public void setMyXCord(int myXCord) {
		this.myXCord = myXCord;
	}
	public int getMyYCord() {
		return myYCord;
	}
	public void setMyYCord(int myYCord) {
		this.myYCord = myYCord;
	}
	public void setMyPiece(ChessPiece p) {
		this.myPiece = p;
	}

	public ChessPiece getMyPiece() {
		return myPiece;
	}
	
	public boolean isACastleTile() {
		return isACastleTile;
	}
	public void setACastleTile(boolean isACastleTile) {
		this.isACastleTile = isACastleTile;
	}
}