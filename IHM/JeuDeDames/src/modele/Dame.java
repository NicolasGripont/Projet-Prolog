package modele;

public class Dame extends Piece {

	public Dame(Couleur couleur, Case position) {
		super(couleur, position);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Dame [couleur=" + this.couleur + "]";
	}

}
