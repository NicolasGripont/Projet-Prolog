package vue.Plateau;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import modele.Case;
import modele.Couleur;
import modele.Dame;
import modele.Piece;
import modele.Plateau;

public class PlateauCanvas extends Canvas {

	private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	public PlateauCanvas(double width, double height) {
		super(width, height);
	}

	private void dessinerFond() {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.setFill(new Color(0.250, 0.250, 0.250, 0.2));
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public void dessinerPlateau(Plateau plateau) {
		this.dessinerFond();
		this.dessinerCases(plateau);
		this.dessinerPions(plateau);
	}

	private void dessinerCases(Plateau plateau) {
		double largeurCase = this.getWidth() / Plateau.NB_LIGNES;
		double longueurCase = this.getHeight() / Plateau.NB_COLONNES;
		GraphicsContext gc = this.getGraphicsContext2D();

		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				Case caseCourante = plateau.getCases()[i][j];
				if (caseCourante.getCouleur() == Couleur.BLANCHE) {
					gc.setFill(new Color(0, 0, 0, 0));

				} else {
					gc.setFill(new Color(0.250, 0.250, 0.250, 0.5));
				}
				gc.fillRect(i * largeurCase, j * longueurCase, largeurCase, longueurCase);
			}
		}
	}

	private void dessinerPions(Plateau plateau) {
		double largeurCase = this.getWidth() / Plateau.NB_LIGNES;
		double longueurCase = this.getHeight() / Plateau.NB_COLONNES;
		GraphicsContext gc = this.getGraphicsContext2D();
		double offset = 5;

		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				Case caseCourante = plateau.getCases()[i][j];

				if (!caseCourante.estVide()) {
					String imagePath = null;
					Piece piece = caseCourante.getPiece();
					if (Dame.class.isInstance(caseCourante.getPiece())) {
						if (piece.getCouleur() == Couleur.BLANCHE) {
							imagePath = this.classLoader.getResource("dame_blanche.png").toString();
						} else {
							imagePath = this.classLoader.getResource("dame_noire.png").toString();
						}
					} else {
						if (piece.getCouleur() == Couleur.BLANCHE) {
							imagePath = this.classLoader.getResource("pion_blanc.png").toString();
						} else {
							imagePath = this.classLoader.getResource("pion_noir.png").toString();
						}
					}
					gc.drawImage(
							new Image(imagePath, largeurCase - (2 * offset), longueurCase - (2 * offset), false, false),
							(largeurCase * i) + offset, (longueurCase * j) + offset);
				}
			}
		}
	}

}
