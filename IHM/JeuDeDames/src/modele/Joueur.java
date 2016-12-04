package modele;

public class Joueur {

	int id;

	String nom;

	TypeJoueur typeJoueur;

	Couleur couleur;

	public Joueur(int id, TypeJoueur typeJoueur, String nom, Couleur couleur) {
		super();
		this.id = id;
		this.nom = nom;
		this.typeJoueur = typeJoueur;
		this.couleur = couleur;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Joueur [id=" + this.id + ", nom=" + this.nom + ", typeJoueur=" + this.typeJoueur + ", couleur="
				+ this.couleur + "]";
	}

}
