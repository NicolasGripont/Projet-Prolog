package modele;

public class Pion extends Piece {

	public Pion(Couleur couleur, Case position) {
		super(couleur, position);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Pion [couleur=" + this.couleur + "]";
	}

}
