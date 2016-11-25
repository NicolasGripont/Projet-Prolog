package modele;

import java.util.ArrayList;
import java.util.List;

public class Plateau {

	public static final int NB_COLONNES = 10;

	public static final int NB_LIGNES = 10;

	private Case[][] cases;

	private final List<Piece> noires;

	private final List<Piece> blanches;

	public Plateau() {
		this.cases = new Case[NB_LIGNES][NB_COLONNES];
		for (int i = 0; i < NB_LIGNES; i++) {
			for (int j = 0; j < NB_COLONNES; j++) {
				if (((i + j) % 2) == 0) {
					this.cases[i][j] = new Case(Couleur.BLANC, i, j);
				} else {
					this.cases[i][j] = new Case(Couleur.NOIR, i, j);
				}
			}
		}
		this.noires = new ArrayList<>();
		this.blanches = new ArrayList<>();
	}

	public Case[][] getCases() {
		return this.cases;
	}

	public void setCases(Case[][] cases) {
		this.cases = cases;
	}

	public List<Piece> getNoires() {
		return this.noires;
	}

	public List<Piece> getBlanches() {
		return this.blanches;
	}

	// public void placerPiece(Piece piece, int ligne, int colonne) {
	// this.cases[ligne][colonne].setPiece(piece);
	// }

	public void initPions() {
		for (int i = 0; i < NB_LIGNES; i++) {
			for (int j = 0; j < NB_COLONNES; j++) {
				if ((i <= 3) && (this.cases[i][j].getCouleur() == Couleur.NOIR)) {
					Piece noire = new Pion(Couleur.NOIR, this.cases[i][j]);
					this.cases[i][j].setPiece(noire);
					this.noires.add(noire);
				} else if ((i >= 6) && (this.cases[i][j].getCouleur() == Couleur.NOIR)) {
					Piece blanche = new Pion(Couleur.BLANC, this.cases[i][j]);
					this.cases[i][j].setPiece(blanche);
					this.blanches.add(blanche);
				}
			}
		}
	}

	/**
	 * Deplace la piece a la nouvelle position. Precondition, la piece et la
	 * case doivent appartenir(references) au plateau
	 * 
	 * @param piece
	 * @param nouvellePosition
	 */
	public void deplacerPiece(Piece piece, Case nouvellePosition) {
		piece.getPosition().setPiece(null);
		piece.setPosition(nouvellePosition);
		nouvellePosition.setPiece(piece);
	}

	/**
	 * Supprime la piece. Precondition, la piece appartient au plateau
	 * 
	 * @param piece
	 */
	public void supprimerPiece(Piece piece) {
		piece.getPosition().setPiece(null);
		if (piece.getCouleur() == Couleur.NOIR) {
			this.noires.remove(piece);
		} else {
			this.blanches.remove(piece);
		}
	}

}
