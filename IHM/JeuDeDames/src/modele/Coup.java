package modele;

import java.util.List;

public class Coup {
	int etat;
	
	List<Piece> piecesBlanches;

	List<Piece> piecesNoires;

	List<Case> deplacement;

	Piece piece;

	public Coup() {
		super();
		this.piecesNoires = null;
		this.piecesBlanches = null;
		this.deplacement = null;
		this.piece = null;
	}

	/**
	 * Attention pas de copie profonde des listes (partage de reference)
	 * 
	 * @param piecesBlances
	 * @param picesNoires
	 * @param deplacement
	 * @param piece
	 */
	public Coup(int etat, List<Piece> piecesBlanches, List<Piece> piecesNoires, List<Case> deplacement, Piece piece) {
		super();
		this.etat = etat;
		this.piecesBlanches = piecesBlanches;
		this.piecesNoires = piecesNoires;
		this.deplacement = deplacement;
		this.piece = piece;
	}
	
	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public List<Piece> getPiecesBlanches() {
		return this.piecesBlanches;
	}

	public void setPiecesBlanches(List<Piece> piecesBlanches) {
		this.piecesBlanches = piecesBlanches;
	}

	public List<Piece> getPiecesNoires() {
		return this.piecesNoires;
	}

	public void setPiecesNoires(List<Piece> piecesNoires) {
		this.piecesNoires = piecesNoires;
	}

	public List<Case> getDeplacement() {
		return this.deplacement;
	}

	public void setDeplacement(List<Case> deplacement) {
		this.deplacement = deplacement;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

}
