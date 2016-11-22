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

}
