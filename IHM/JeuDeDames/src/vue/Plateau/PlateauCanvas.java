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
	private static final double margin = 50;
	private double plateauWidth;
	private double plateauHeight;
	private double offsetX;
	private double offsetY;

	public PlateauCanvas(double width, double height) {
		super(width, height);
		this.calculerZoom();
	}

	private void effacer() {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}

	public void dessinerPlateau(Plateau plateau) {
		this.effacer();
		this.dessinerFond();
		this.dessinerFondPlateau();
		this.dessinerCases(plateau);
		this.dessinerPions(plateau);
	}

	@Override
	public void resize(double width, double height) {
		this.setWidth(width);
		this.setHeight(height);
		this.calculerZoom();
	}

	private void dessinerFond() {
		GraphicsContext gc = this.getGraphicsContext2D();
		// gc.setFill(new Color(0, 0, 0, 1));
		// gc.fillRect(0, 0, this.getWidth(), this.getHeight());
		String imagePath = this.classLoader.getResource("fond.png").toString();
		gc.drawImage(new Image(imagePath, this.getWidth(), this.getHeight(), false, false), 0, 0);
	}

	private void dessinerFondPlateau() {
		GraphicsContext gc = this.getGraphicsContext2D();
		double borderWidth = 10;

		String imagePath = this.classLoader.getResource("plateau.png").toString();
		gc.drawImage(new Image(imagePath, this.plateauWidth + (2 * borderWidth), this.plateauHeight + (2 * borderWidth),
				false, false), this.offsetX - borderWidth, this.offsetY - borderWidth);
	}

	private void dessinerCases(Plateau plateau) {
		double largeurCase = this.plateauWidth / Plateau.NB_LIGNES;
		double longueurCase = this.plateauHeight / Plateau.NB_COLONNES;
		GraphicsContext gc = this.getGraphicsContext2D();

		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				Case caseCourante = plateau.getCases()[i][j];
				if (caseCourante.getCouleur() == Couleur.BLANCHE) {
					gc.setFill(new Color(1, 1, 1, 0.5));

				} else {
					gc.setFill(new Color(0, 0, 0, 0.5));
				}
				gc.fillRect((i * largeurCase) + this.offsetX, (j * longueurCase) + this.offsetY, largeurCase,
						longueurCase);
			}
		}
	}

	private void dessinerPions(Plateau plateau) {
		double largeurCase = this.plateauWidth / Plateau.NB_LIGNES;
		double longueurCase = this.plateauHeight / Plateau.NB_COLONNES;
		GraphicsContext gc = this.getGraphicsContext2D();
		double margin = 5;

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
							new Image(imagePath, largeurCase - (2 * margin), longueurCase - (2 * margin), false, false),
							(largeurCase * i) + this.offsetX + margin, (longueurCase * j) + this.offsetY + margin);
				}
			}
		}
	}

	private void calculerZoom() {
		double width = this.getWidth();
		double height = this.getHeight();
		if (width == Math.min(width, height)) { // width < height
			this.plateauHeight = width - (2 * margin);
			this.plateauWidth = width - (2 * margin);
			this.offsetX = margin;
			this.offsetY = margin + ((height - width) / 2);
		} else {
			this.plateauHeight = height - (2 * margin);
			this.plateauWidth = height - (2 * margin);
			this.offsetX = margin + ((width - height) / 2);
			this.offsetY = margin;
		}
	}

}
