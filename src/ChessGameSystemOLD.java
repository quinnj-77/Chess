import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChessGameSystemOLD {
	
	private JFrame window;
	private Tile[][] tiles;
	private boolean isWhitePiecesTurn;
	private Tile whiteKingTile;
	private Tile blackKingTile;
	private int numOfChecks;
	
	

	public ChessGameSystemOLD(Tile[][] tiles, JFrame window) {
		this.isWhitePiecesTurn = true;
		this.tiles = tiles;
		this.whiteKingTile = findKingTile(true);
		this.blackKingTile = findKingTile(false);
		this.window = window;
	}

	public void swapTurn() {
		this.clearCheckPaths();
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
		Tile kingTile;
		if(isWhitePiecesTurn) {                       
			isWhitePiecesTurn = false;   
			kingTile = blackKingTile;                  
		}	                                                               
		else {
			isWhitePiecesTurn = true;
			kingTile = whiteKingTile;                  
		}
		numOfChecks = countNumOfAttacks(kingTile, kingTile.getMyPiece().isWhite(), kingTile.getMyPiece().isWhite() );
		if(numOfChecks == 1) {
			doCheckAngleProcess(kingTile);
		}
		if(checkForCheckMate(kingTile)) {
			clearTargetedTiles();
			endGame(kingTile);
		}
		this.clearTargetedTiles();
	}

	public void handlePieceMoveInCheckContext(Tile kingTile, Tile clickedTile ) {
		if(numOfChecks == 0) {
			if(kingTile == clickedTile) { 
				((King)kingTile.getMyPiece()).seePosibleMovesKing(kingTile.getMyXCord(), kingTile.getMyYCord() , tiles, this);			
			}
			else if(checkIfPieceIsSavingKing(clickedTile, kingTile)) {  
				clickedTile.getMyPiece().seeMovesOnlyOnCheckingPath(clickedTile.getMyXCord(), clickedTile.getMyYCord(), tiles);
			}
			else {
				this.clearTargetedTiles(); 
				clickedTile.getMyPiece().seePosibleMoves(clickedTile.getMyXCord(), clickedTile.getMyYCord(), tiles);
			}
		}
		else if(numOfChecks > 0) {

			if(numOfChecks == 1) {
		 		if(kingTile == clickedTile) { 
		 			((King)kingTile.getMyPiece()).seePosibleMovesKing(kingTile.getMyXCord(), kingTile.getMyYCord() , tiles, this);		
				}
		 		else {

		 			if(!clickedTile.isSavingKing()) {
		 				
				 		clickedTile.getMyPiece().seeMovesOnlyOnCheckingPath(clickedTile.getMyXCord(), clickedTile.getMyYCord() , tiles);
		 			}
		 		}
			}
			else if(numOfChecks > 1) {
				if(kingTile == clickedTile) { 
					((King)kingTile.getMyPiece()).seePosibleMovesKing(kingTile.getMyXCord(), kingTile.getMyYCord() , tiles, this);
				}
			}
		}
	}
	
	public boolean checkForCheckMate(Tile kingTile) {
		if(numOfChecks > 1) {
			return !((King)kingTile.getMyPiece()).seePosibleMovesKing(kingTile.getMyXCord(), kingTile.getMyYCord() , tiles, this); 
		}
		else if(numOfChecks == 1) {

			if(((King)kingTile.getMyPiece()).seePosibleMovesKing(kingTile.getMyXCord(), kingTile.getMyYCord() , tiles, this)) {
				return false;
			}
			else {

				if(!checkIfCheckAngleCanBeBlocked(kingTile.getMyPiece().isWhite())) {
					return true;
				}

			}	
		}

		return false;
	}
	
	public boolean checkIfCheckAngleCanBeBlocked(boolean isKingWhite) {
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {

				if(tiles[i][j].isOnCheckingPath() && (tiles[i][j].getMyPiece() == null 
													|| tiles[i][j].getMyPiece().isWhite() != isKingWhite)) {
					if(this.countNumOfAttacks(tiles[i][j], !isKingWhite, isKingWhite) > 0){
						return true;
					}
				}
			}
		}
		return false;
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
	
	public void seePosibleMoves(Tile tile) {
		Tile kingTile = blackKingTile;
		if(tile.getMyPiece().isWhite() ) {
			kingTile = whiteKingTile;
		}
		handlePieceMoveInCheckContext(kingTile, tile);
	}
	public int getNumOfChecks() {
		return numOfChecks;
	}

	public void setNumOfChecks(int numOfChecks) {
		this.numOfChecks = numOfChecks;
	}

	public void movePiece(Tile startTile, Tile endTile, ChessBoard b ) {
		if(startTile.getMyPiece() instanceof King ) {
			if(startTile.getMyPiece().isWhite()) {
				whiteKingTile = endTile;
				
			}
			else {
				blackKingTile = endTile;

			}
			((King)startTile.getMyPiece()).setHasMoved(true);
			if(endTile.isACastleTile()) {
				this.doCastling(startTile, endTile );
				endTile.setACastleTile(false);
			}
		}
		if(startTile.getMyPiece() instanceof Rook){
			((Rook)startTile.getMyPiece()).setHasMoved(true);
		}
		if(startTile.getMyPiece() instanceof Pawn && endTile.getMyYCord() == 0) {
        	endTile.setMyPiece(startTile.getMyPiece());
			startTile.setMyPiece(null);
			this.clearTargetedTiles();
			b.repaint();
			pawnPromotion(endTile);
		}
		else {
			endTile.setMyPiece(startTile.getMyPiece());
			endTile.setUsedForLastMove(true);
			startTile.setMyPiece(null);
		}
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
        promotionTile.setUsedForLastMove(true);

        try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}  
        this.swapTurn();

		
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
	public boolean checkIfPieceIsSavingKing(Tile tile, Tile kingTile) {
		ChessPiece savedPiece = tile.getMyPiece();
		tile.setMyPiece(null);
		int i = tile.getMyYCord();
		int j = tile.getMyXCord();
		int stepY = (int) Math.signum(i - kingTile.getMyYCord());
		int stepX = (int) Math.signum(j - kingTile.getMyXCord());
		ArrayList<Tile> enemyTileArray = new ArrayList<Tile>();
		ChessPiece p = new Queen(savedPiece.isWhite());
		p.addTilesOnMyPath(j, i, tiles, j+stepX, i+stepY, enemyTileArray); 
		Tile enemyTile = null;
		for(Tile t: enemyTileArray) {
			if(t.getMyPiece() != null) {
				enemyTile = t;
			}
		}
		if(enemyTile == null) {
			tile.setMyPiece(savedPiece);
			return false;
		}

		if(enemyTile == null || enemyTile.getMyPiece() instanceof Pawn || enemyTile.getMyPiece() instanceof King || enemyTile.getMyPiece() instanceof Knight) {
			tile.setMyPiece(savedPiece);
			return false;
		}
		enemyTile.getMyPiece().targetTilesUsingCheckingPath(enemyTile.getMyXCord(), enemyTile.getMyYCord(), tiles, enemyTile.getMyXCord()-stepX, enemyTile.getMyYCord()-stepY);
		
		if(kingTile.isOnCheckingPath()) {
			enemyTile.setOnCheckingPath(true);
			kingTile.setOnCheckingPath(false);
			tile.setOnCheckingPath(false);
			tile.setMyPiece(savedPiece);
			return true;
		}
		tile.setMyPiece(savedPiece);
		this.clearTargetedTiles();
		return false;
	}
	
	
	
	public int countNumOfAttacks(Tile tile, boolean colorNeeded, boolean kingColor) {
		ChessPiece savePiece = tile.getMyPiece();
		if(tile.getMyPiece() != null && tile.getMyPiece().isWhite() != colorNeeded) {
			tile.setMyPiece(null);
		}
		ArrayList<Tile> piecesAttackingTile = new ArrayList<Tile>();

		piecesAttackingTile = getArrayOfPiecesAttackingTile(tile, colorNeeded);

		int numTilesAttacking = 0;
		int pawnDirection = 1;
		if(colorNeeded != kingColor) {
			pawnDirection = -1;
		}
		for(Tile t : piecesAttackingTile ) {
			if(t.getMyPiece() == null ) {
				continue;
			}
			ArrayList<Tile> whoPiecesCanSee = new ArrayList<Tile>();
			if(t.getMyPiece() instanceof Pawn) {
				((Pawn)t.getMyPiece()).getTilesWithEnemyPiecesOnMyPathPawn(pawnDirection, t.getMyXCord(), t.getMyYCord(), tiles, whoPiecesCanSee);
			}
			if(t.getMyPiece() instanceof King) {
				((King)t.getMyPiece()).getTilesWithEnemyPiecesOnMyPathKing(t.getMyXCord(), t.getMyYCord(), tiles, whoPiecesCanSee, this);
			}
			else {
				t.getMyPiece().getTilesWithEnemyPiecesOnMyPath(t.getMyXCord(), t.getMyYCord(), tiles, whoPiecesCanSee);
			}
			if(whoPiecesCanSee.indexOf(tile) != -1) {
				numTilesAttacking++;
			}
			tile.setIsTargeted(false);
		}
		tile.setMyPiece(savePiece);
		return numTilesAttacking;
	}
	public ArrayList<Tile> getArrayOfPiecesAttackingTile(Tile t, boolean isWhite) {
		int x = t.getMyXCord();
		int y = t.getMyYCord();
	
		ChessPiece queenAngle = new Queen(isWhite);
		ChessPiece knightAngle = new Knight(isWhite);

		ArrayList<Tile> piecesAttackingTile = new ArrayList<Tile>();

		queenAngle.getTilesWithEnemyPiecesOnMyPath(x, y, tiles, piecesAttackingTile);
		knightAngle.getTilesWithEnemyPiecesOnMyPath(x, y, tiles, piecesAttackingTile);
		
		return piecesAttackingTile;
		
	}
	public void doCheckAngleProcess(Tile kingTile) {
		this.clearCheckPaths();
		ArrayList<Tile> piecesAttackingTile = new ArrayList<Tile>();

		ChessPiece queenAngle = new Queen(!kingTile.getMyPiece().isWhite());
		((Queen) queenAngle).getTilesWithEnemyPiecesOnMyPath(kingTile.getMyXCord(), kingTile.getMyYCord(), tiles, piecesAttackingTile);

		for(Tile t : piecesAttackingTile ) {
			if(t == kingTile || t.getMyPiece() == null) {
				continue;
			}
			if(this.checkIfPieceIsSavingKing(t, kingTile)) { 
				t.setSavingKing(true);
			}
		}
		piecesAttackingTile = new ArrayList<Tile>();
		piecesAttackingTile = getArrayOfPiecesAttackingTile(kingTile, kingTile.getMyPiece().isWhite());
		
		for(Tile t : piecesAttackingTile ) {
			if(t.getMyPiece() instanceof King || t.getMyPiece() == null) {
				continue;
			}
			int i = t.getMyYCord();
			int j = t.getMyXCord();
			if(t.getMyPiece() instanceof Pawn || t.getMyPiece() instanceof Knight) {
				t.getMyPiece().makeMovesCheckingPathMode(j, i, tiles);
				if(kingTile.isOnCheckingPath()) {
					this.clearCheckPaths();
					t.setOnCheckingPath(true);
					break;
				}
			}
			else {
				int stepY = (int) Math.signum(i - kingTile.getMyYCord());
				int stepX = (int) Math.signum(j - kingTile.getMyXCord());
				t.getMyPiece().targetTilesUsingCheckingPath(j, i, tiles, j-stepX, i-stepY ); 
				if(kingTile.isOnCheckingPath()) { 
					t.setOnCheckingPath(true);
					break;
				}
			}
			this.clearCheckPaths();
		}
		
		this.clearTargetedTiles();
	}
	
	public void clearTargetedTiles() {
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				tiles[y][x].setIsTargeted(false);
			}
		}
	}
	public void clearCheckPaths() {
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				tiles[y][x].setOnCheckingPath(false);
				tiles[y][x].setSavingKing(false);
			}
		}
	}
	public void clearHighLightedTilesNotUsedForLastMove(Tile t1, Tile t2) {
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				if(tiles[y][x] != t1 && tiles[y][x] != t2) {
					tiles[y][x].setUsedForLastMove(false);
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
	public Tile findKingTile(boolean isWhite) {
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				if(tiles[y][x].getMyPiece() instanceof King && tiles[y][x].getMyPiece().isWhite() == isWhite) {
					return tiles[y][x]; 
				}
			}
		}
		return null;
	}
	
	public boolean isWhitePiecesTurn() {
		return isWhitePiecesTurn;
	}

	public void setWhitePiecesTurn(boolean isWhitePiecesTurn) {
		this.isWhitePiecesTurn = isWhitePiecesTurn;
	}

}
