package modele;

public class Case {

	private Couleur typeCase;

	private Piece piece;

	public Case(Couleur typeCase) {
		super();
		this.typeCase = typeCase;
		this.piece = null;
	}

	public Couleur getTypeCase() {
		return this.typeCase;
	}

	public void setTypeCase(Couleur typeCase) {
		this.typeCase = typeCase;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public Boolean estVide() {
		return this.piece == null;
	}

}
