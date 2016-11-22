package modele;

public abstract class Piece {

	private Couleur couleur;

	private Case position;

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
