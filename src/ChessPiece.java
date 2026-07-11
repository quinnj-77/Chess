import java.awt.Graphics;
import java.awt.Image;

public class ChessPiece {
	private Image i;
	private int mySize;
	private int myRatio;
	private int myBottomDistance;
	private boolean isWhite;
	
	private int xCord;
	private int yCord;
	private long animationStartTime; 
    private int animationTargetX;
    private int animationTargetY;
    private int animationStartX;
    private int animationStartY;


	public ChessPiece(Boolean isWhite, Image white, Image black, int s, int r, int b) {
		if(isWhite) {
			this.i = white;
		}
		else {
			this.i = black;
		}
		this.isWhite = isWhite;
		this.mySize = s;
		this.myRatio = r;
		this.myBottomDistance = b;
	}
	
	public void setX(int xCord) {
		this.xCord = xCord;
	}
	public void setY(int yCord) {
		this.yCord = yCord;
	}
	public int getY() {
		return this.yCord;
	}
	public int getX() {
		return this.xCord;
	}
	public void startMovingAnimation(Tile tile, ChessBoard b) {
		b.setMovingPiece(this);
		this.animationTargetX = tile.getMyXCord()* b.getTileSize()+ b.getSideSize() + mySize/2;

		this.animationTargetY = tile.getMyYCord()* b.getTileSize()+b.getSideSize() + mySize/2- myBottomDistance;

		this.animationStartX = xCord;
		this.animationStartY = yCord;

		this.animationStartTime = System.currentTimeMillis();
	}
	
	public void updateMovingAnimation(ChessBoard b) {
		long elapsed = System.currentTimeMillis() - animationStartTime;

		if(elapsed >= b.getAnimationDurationMS()) {
			endMovingAnimation(b);
            return;
        }
		double progress = (double) elapsed / b.getAnimationDurationMS();
		xCord = (int) (animationStartX + (animationTargetX - animationStartX) * progress);
		yCord = (int) (animationStartY + (animationTargetY - animationStartY) * progress);
		
	}
	public void endMovingAnimation(ChessBoard b) {
		xCord = animationTargetX;
		yCord = animationTargetY;
		b.setMovingPiece(null);
	}
	
	public int getMyBottomDistance() {
		return myBottomDistance;
	}
	
	
	public void seePosibleMoves(Tile[][] tiles, int x, int y) {   
	}
	
	public void floodFill(int x, int y, int dirX, int dirY, Tile[][] tiles) {
		if(x < 8 && y < 8 && x >=0 && y>=0) {
			ChessPiece p = tiles[y][x].getMyPiece();
			if(p != null && tiles[y][x].getMyPiece().isWhite == this.isWhite) {
				return;
			}
			tiles[y][x].setTargeted(true);
			if(p == null) {
				floodFill(x+dirX, y+dirY, dirX, dirY, tiles);
			}
		}
	}
	public void draw(Graphics g, int size, ChessBoard b) {
		
		g.drawImage(i, xCord, yCord, size-mySize, size-mySize+myRatio, b);
	}
	public int getMySize() {
		return mySize;
	}
	public void setWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}
	public int getMyRatio() {
		return myRatio;
	}
	public Image getImage() {
		return i;
	}
	public boolean isWhite() {
		return isWhite;
	}

}