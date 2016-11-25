package modele;

public class Case {

	private Couleur couleur;

	private int ligne;

	private int colonne;

	private Piece piece;

	public Case(Couleur couleur, int ligne, int colonne) {
		super();
		this.couleur = couleur;
		this.ligne = ligne;
		this.colonne = colonne;
		this.piece = null;
	}

	public Couleur getCouleur() {
		return this.couleur;
	}

	public void setCouleur(Couleur couleur) {
		this.couleur = couleur;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public int getLigne() {
		return this.ligne;
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}

	public int getColonne() {
		return this.colonne;
	}

	public void setColonne(int colonne) {
		this.colonne = colonne;
	}

	public Boolean estVide() {
		return this.piece == null;
	}

	@Override
	public String toString() {
		return "Case [couleur=" + this.couleur + ", ligne=" + this.ligne + ", colonne=" + this.colonne + ", piece="
				+ this.piece + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.colonne;
		result = (prime * result) + ((this.couleur == null) ? 0 : this.couleur.hashCode());
		result = (prime * result) + this.ligne;
		result = (prime * result) + ((this.piece == null) ? 0 : this.piece.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Case other = (Case) obj;
		if (this.colonne != other.colonne) {
			return false;
		}
		if (this.couleur != other.couleur) {
			return false;
		}
		if (this.ligne != other.ligne) {
			return false;
		}
		return true;
	}

}
