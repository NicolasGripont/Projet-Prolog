package modele;

public class Plateau {

	public static final int NB_COLONNES = 10;

	public static final int NB_LIGNES = 10;

	private Case[][] cases;

	public Plateau() {
		// TODO Auto-generated constructor stub
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
	}

	public Case[][] getCases() {
		return this.cases;
	}

	public void setCases(Case[][] cases) {
		this.cases = cases;
	}

	public void placerPiece(Piece piece, int ligne, int colonne) {
		this.cases[ligne][colonne].setPiece(piece);
	}

	public void initPions() {
		for (int i = 0; i < NB_LIGNES; i++) {
			for (int j = 0; j < NB_COLONNES; j++) {
				if ((i <= 3) && (this.cases[i][j].getCouleur() == Couleur.NOIR)) {
					this.cases[i][j].setPiece(new Pion(Couleur.NOIR, this.cases[i][j]));
				} else if ((i >= 6) && (this.cases[i][j].getCouleur() == Couleur.NOIR)) {
					this.cases[i][j].setPiece(new Pion(Couleur.BLANC, this.cases[i][j]));
				}
			}
		}
	}
}
