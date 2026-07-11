import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class ChessListener implements MouseListener, MouseMotionListener, ActionListener {

	private ChessBoard board;
	private boolean listeningForMoves;
	private Tile movingTile;
	private Tile tileThatMoved;
	private Tile tileMovedTo;
	private Timer animationTimer; 
	private ChessGameSystem boardSystem;
	private boolean onPawnPromotionPage;
	
	public ChessListener(ChessBoard board, JFrame window) {
		this.onPawnPromotionPage = false;
		this.board = board;
		this.board.addMouseListener(this);
		this.board.addMouseMotionListener(this);
		this.listeningForMoves = false;
		this.animationTimer = new Timer(board.getANIMATION_DELAY_MS(), this);
		this.boardSystem = new ChessGameSystem(board.getTiles(),window);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		board.setAnimationDurationMS(250);
		Tile tile = getTileUnderMouse(e, false);
		if(tile == null) {
			return;
		}
		if(tile.getMyPiece() != null && tile.getMyPiece().isWhite() == boardSystem.isWhitePiecesTurn()) {
			if(!listeningForMoves || (movingTile != null && movingTile.getMyPiece() != null 
							&& tile.getMyPiece().isWhite() == movingTile.getMyPiece().isWhite() )) {
				boardSystem.clearTargetedTiles();
				boardSystem.clearOldActivedTiles(tileMovedTo, tileThatMoved);
				movingTile = tile;		
				movingTile.setActivated(true);
				listeningForMoves = true;
				board.setMovingPiece(tile.getMyPiece());
				boardSystem.seePosibleMoves(tile);
			}	
		}
		board.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(this.movingTile == null || movingTile.getMyPiece() == null || movingTile.getMyPiece().isWhite() != boardSystem.isWhitePiecesTurn()) {
			return;
		}
		ChessPiece piece = this.movingTile.getMyPiece();
		board.setAnimationDurationMS(1);
		int centerXCord = (board.getTileSize()-piece.getMySize())/2;
		int centerYCord = (board.getTileSize()-piece.getMySize()+piece.getMyRatio())/2;
		int newX = e.getX() - centerXCord;
		int newY = e.getY() - centerYCord;
		
		int maxBoardSize = board.getSideSize()+board.getTileSize()*8;
		if(e.getX() > maxBoardSize) {
			newX = maxBoardSize-centerXCord;
		}
		else if(e.getX() < 0) {
			newX = 0;
		}
		if(e.getY() > maxBoardSize) {
			newY = maxBoardSize-centerYCord;
		}
		else if(e.getY() < 0) {
			newY = 0;
		}
		piece.setX(newX);
		piece.setY(newY);
		boardSystem.clearDragedOnTiles();
		Tile tile = getTileUnderMouse(e, true);
		tile.setIsdragedOn(true);

		board.repaint();	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	
		Tile newTile = getTileUnderMouse(e, true);
		
		if(listeningForMoves && newTile.isTargeted()) {
			if(movingTile.getMyPiece() instanceof Pawn && newTile.getMyYCord() == 0) {
				this.onPawnPromotionPage = true;
           	 	this.listeningForMoves = false;
			}
			else {
				this.onPawnPromotionPage = false;
				movingTile.getMyPiece().startMovingAnimation(newTile, board);
				animationTimer.start();
			}

			boardSystem.movePiece(movingTile, newTile, board);
			boardSystem.clearTargetedTiles();
			boardSystem.clearOldActivedTiles(newTile, movingTile);
			tileMovedTo = newTile;
			tileThatMoved = movingTile;
			listeningForMoves = false;
			boardSystem.clearTargetedTiles();
			newTile.setIsdragedOn(false);
		}
		if(listeningForMoves && !newTile.isTargeted()) {
			newTile.setIsdragedOn(false);
			board.setMovingPiece(null);
		}
		board.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(board.getMovingPiece() == null) {
            ((Timer)e.getSource()).stop();
            try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}    
            if(!this.onPawnPromotionPage) {
            	 this.listeningForMoves = false;
                 boardSystem.swapTurn();
            }
           
		}
		else {
			board.getMovingPiece().updateMovingAnimation(board);
		}
		board.repaint();
	} 

	private Tile getTileUnderMouse(MouseEvent e, boolean canGoOutOfBounds) {
		Tile[][] tiles = board.getTiles();
		int size = board.getTileSize();
		int xCord = (e.getX()-board.getSideSize())/size;
		int yCord = (e.getY()-board.getSideSize())/size;
		if(canGoOutOfBounds) {
			if(xCord < 0 ) {
				xCord = 0;
			}
			else if(xCord > 7 ) {
				xCord = 7;
			}
			if(yCord < 0 ) {
				yCord = 0;
			}
			else if(yCord > 7) {
				yCord = 7;
			}
		}
		else {
			if(xCord < 0 || yCord < 0 || xCord > 7 || yCord > 7) {
				return null;
			}
		}
		return tiles[yCord][xCord];
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {	
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mouseMoved(MouseEvent e) {
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}	 
}