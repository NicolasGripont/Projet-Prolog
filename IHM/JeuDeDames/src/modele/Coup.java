package modele;

import java.util.List;

public class Coup {
	/**
	 * 0 : Blancs gagnent ,1 : Noir gagnent ,2 : Egalite ,3 : Non terminé
	 */
	int etat;

	List<Piece> piecesBlanches;

	List<Piece> piecesNoires;

	List<Case> deplacement;

	Piece piece;

	public Coup() {
		super();
		this.etat = 0;
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
		return this.etat;
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

	@Override
	public String toString() {
		return "Coup [etat=" + this.etat + ", piecesBlanches=" + this.piecesBlanches + ", piecesNoires="
				+ this.piecesNoires + ", deplacement=" + this.deplacement + ", piece=" + this.piece + "]";
	}

}
