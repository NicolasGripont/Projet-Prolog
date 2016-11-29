package modele;

public class Joueur {

	String nom;

	TypeJoueur typeJoueur;

	Couleur couleur;

	public Joueur(TypeJoueur typeJoueur, String nom, Couleur couleur) {
		super();
		this.nom = nom;
		this.typeJoueur = typeJoueur;
		this.couleur = couleur;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public TypeJoueur getTypeJoueur() {
		return this.typeJoueur;
	}

	public void setTypeJoueur(TypeJoueur typeJoueur) {
		this.typeJoueur = typeJoueur;
	}

	public Couleur getCouleur() {
		return this.couleur;
	}

	public void setCouleur(Couleur couleur) {
		this.couleur = couleur;
	}

}
