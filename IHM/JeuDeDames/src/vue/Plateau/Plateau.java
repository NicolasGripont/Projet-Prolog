package vue.Plateau;

import javafx.scene.canvas.Canvas;
import modele.Case;
import modele.Couleur;

public class Plateau extends Canvas {

	private static int NB_COLONNES = 10;

	private static int NB_LIGNES = 10;

	private Case[][] cases;

	public Plateau() {
		// TODO Auto-generated constructor stub
		this.cases = new Case[NB_LIGNES][NB_COLONNES];
		for (int i = 0; i < NB_LIGNES; i++) {
			for (int j = 0; j < NB_COLONNES; j++) {
				if (((i + j) % 2) == 0) {
					this.cases[i][j] = new Case(Couleur.BLANCHE);
				} else {
					this.cases[i][j] = new Case(Couleur.NOIRE);
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

}
