
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChessGameSystem {
	
	private JFrame window;
	private Tile[][] tiles;
	private boolean isWhitePiecesTurn;
	private int numOfChecks;
	

	public ChessGameSystem(Tile[][] tiles, JFrame window) {
		this.isWhitePiecesTurn = true;
		this.tiles = tiles;
		this.window = window;
	}

	public void swapTurn() {
		for(int y = 0; y < tiles.length/2; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				int newX = tiles.length-1-x;
				int newY = tiles.length-1-y;
				Tile t = tiles[y][x];
				tiles[y][x] = tiles[newY][newX];
				tiles[y][x].setMyXCord(x);
				tiles[y][x].setMyYCord(y);
				tiles[newY][newX] = t;

				tiles[newY][newX].setMyXCord(newX);
				tiles[newY][newX].setMyYCord(newY);
			}                                                               
		}	 			
		this.clearTargetedTiles();
	}

	
	public void endGame(Tile kingTile) {
		String str = "White Won!";;
		if(kingTile.getMyPiece().isWhite()) {
			str = "Black Won!";
		}
        int choice = JOptionPane.showConfirmDialog(
            window, 
            str+"\nDo you want to play again?", "Checkmate!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE 
        );

        if (choice == JOptionPane.YES_OPTION) {
        	window.dispose();
            ChessFrame newGame = new ChessFrame(); 
        } 
        else {
            System.exit(0);
        }
	}
	
	public int getNumOfChecks() {
		return numOfChecks;
	}

	public void setNumOfChecks(int numOfChecks) {
		this.numOfChecks = numOfChecks;
	}

	public void movePiece(Tile startTile, Tile newTile, ChessBoard b ) {
		
		newTile.setMyPiece(startTile.getMyPiece());
		newTile.setActivated(true);
		startTile.setMyPiece(null);
	}
	public void pawnPromotion(Tile promotionTile){
		Object[] options = {"Queen", "Rook", "Bishop", "Knight"};

        String message = "Your pawn has reached the end of the board!\n" +
                         "Promote your " + "pawn to:";

        String title = "Pawn Promotion";

        int choice = JOptionPane.showOptionDialog(
        	window,
            message,                
            title,                  
            JOptionPane.YES_NO_CANCEL_OPTION, 
            JOptionPane.QUESTION_MESSAGE,    
            null,                  
            options,              
            options[0]             
        );
        
       
        if(choice == 1) { //rook
        	promotionTile.setMyPiece(new Rook(promotionTile.getMyPiece().isWhite()));
        }
        else if(choice == 2) { //bishop
        	promotionTile.setMyPiece(new Bishop(promotionTile.getMyPiece().isWhite()));
        }
        else if(choice == 3) { //knight
        	promotionTile.setMyPiece(new Knight(promotionTile.getMyPiece().isWhite()));
        }
        else { //queen
        	promotionTile.setMyPiece(new Queen(promotionTile.getMyPiece().isWhite()));
        }
        promotionTile.setActivated(true);

        try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}  
        this.swapTurn();

		
	}
	
	
	public void seePosibleMoves(Tile t) {
		t.seeMoves(tiles);
	}
	public void doCastling(Tile startTile, Tile endTile) {
		if(startTile.getMyPiece().isWhite() && endTile.getMyXCord() == 6 ) {
			tiles[7][5].setMyPiece(tiles[7][7].getMyPiece());
			((Rook)tiles[7][5].getMyPiece()).setHasMoved(true);
			tiles[7][7].setMyPiece(null);
		}
		else if(startTile.getMyPiece().isWhite() && endTile.getMyXCord() == 2 ) {
			tiles[7][3].setMyPiece(tiles[7][0].getMyPiece());
			((Rook)tiles[7][3].getMyPiece()).setHasMoved(true);
			tiles[7][0].setMyPiece(null);
		}
		else if(!startTile.getMyPiece().isWhite() && endTile.getMyXCord() == 5 ) {
			tiles[7][4].setMyPiece(tiles[7][7].getMyPiece());
			((Rook)tiles[7][4].getMyPiece()).setHasMoved(true);
			tiles[7][7].setMyPiece(null);
		}
		else if(!startTile.getMyPiece().isWhite() && endTile.getMyXCord() == 1 ) {
			tiles[7][2].setMyPiece(tiles[7][0].getMyPiece());
			((Rook)tiles[7][2].getMyPiece()).setHasMoved(true);
			tiles[7][0].setMyPiece(null);
		}
	}
	
	
	
	
	
	public void clearTargetedTiles() {
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				tiles[y][x].setTargeted(false);
			}
		}
	}
	public void clearOldActivedTiles(Tile t1, Tile t2) {
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				if(tiles[y][x] != t1 && tiles[y][x] != t2) {
					tiles[y][x].setActivated(false);
				}
			}
		}
	}

	public void clearDragedOnTiles() {
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				tiles[y][x].setIsdragedOn(false);
			}
		}
	}	
	
	public boolean isWhitePiecesTurn() {
		return isWhitePiecesTurn;
	}

	public void setWhitePiecesTurn(boolean isWhitePiecesTurn) {
		this.isWhitePiecesTurn = isWhitePiecesTurn;
	}

}
