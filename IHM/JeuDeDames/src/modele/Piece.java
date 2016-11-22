package modele;

public abstract class Piece {

	protected Couleur couleur;

	protected Case position;

	public Piece(Couleur couleur, Case position) {
		super();
		this.couleur = couleur;
		this.position = position;
	}

	public Couleur getCouleur() {
		return this.couleur;
	}

	public void setCouleur(Couleur couleur) {
		this.couleur = couleur;
	}

	public Case getPosition() {
		return this.position;
	}

	public void setPosition(Case position) {
		this.position = position;
	}

}
