package modele;

import java.util.ArrayList;
import java.util.List;

public class Plateau {

	public static final int NB_COLONNES = 10;

	public static final int NB_LIGNES = 10;

	private final Case[][] cases;

	private List<Piece> noires;

	private List<Piece> blanches;

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

	public void initPions(List<Piece> blanches, List<Piece> noires) {

		for (Piece piece : blanches) {
			Piece newPiece;
			Case c = this.cases[piece.getPosition().getLigne()][piece.getPosition().getColonne()];
			if (piece.getClass().equals(Pion.class)) {
				newPiece = new Pion(Couleur.BLANC, c);
			} else {
				newPiece = new Dame(Couleur.BLANC, c);
			}
			c.setPiece(newPiece);
			this.blanches.add(newPiece);
		}

		for (Piece piece : noires) {
			Piece newPiece;
			Case c = this.cases[piece.getPosition().getLigne()][piece.getPosition().getColonne()];
			if (piece.getClass().equals(Pion.class)) {
				newPiece = new Pion(Couleur.NOIR, c);
			} else {
				newPiece = new Dame(Couleur.NOIR, c);
			}
			c.setPiece(newPiece);
			this.noires.add(newPiece);
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

	@Override
	public Plateau clone() {
		// TODO Auto-generated method stub
		Plateau plateau = new Plateau();

		for (Piece p : this.getNoires()) {
			Piece piece = null;
			Case position = plateau.getCases()[p.getPosition().getLigne()][p.getPosition().getColonne()];
			if (p.getClass().equals(Pion.class)) {
				piece = new Pion(Couleur.NOIR, position);
			} else if (p.getClass().equals(Dame.class)) {
				piece = new Dame(Couleur.NOIR, position);
			}
			if (piece != null) {
				position.setPiece(piece);
				plateau.noires.add(p);
			}
		}

		for (Piece p : this.getBlanches()) {
			Piece piece = null;
			Case position = plateau.cases[p.getPosition().getLigne()][p.getPosition().getColonne()];
			if (p.getClass().equals(Pion.class)) {
				piece = new Pion(Couleur.BLANC, position);
			} else if (p.getClass().equals(Dame.class)) {
				piece = new Dame(Couleur.BLANC, position);
			}
			if (piece != null) {
				position.setPiece(piece);
				plateau.blanches.add(p);
			}
		}

		return plateau;
	}

	public Dame promouvoirPion(Pion pion) {
		Dame dame = new Dame(pion.getCouleur(), pion.getPosition());
		dame.getPosition().setPiece(dame);
		if (pion.getCouleur() == Couleur.BLANC) {
			this.blanches.remove(pion);
			this.blanches.add(dame);
		} else {
			this.noires.remove(pion);
			this.noires.add(dame);
		}
		return dame;
	}

	public void trierPieces(List<Piece> blanches, List<Piece> noires) {
		List<Piece> newBlanches = new ArrayList<>();
		List<Piece> newNoires = new ArrayList<>();
		for (Piece p1 : blanches) {
			for (Piece p2 : this.blanches) {
				if ((p2.getPosition().getLigne() == p1.getPosition().getLigne())
						&& (p2.getPosition().getColonne() == p1.getPosition().getColonne())) {
					newBlanches.add(p2);
					break;
				}
			}
		}

		for (Piece p1 : noires) {
			for (Piece p2 : this.noires) {
				if ((p2.getPosition().getLigne() == p1.getPosition().getLigne())
						&& (p2.getPosition().getColonne() == p1.getPosition().getColonne())) {
					newNoires.add(p2);
					break;
				}
			}
		}

		this.blanches = newBlanches;
		this.noires = newNoires;
	}
}
