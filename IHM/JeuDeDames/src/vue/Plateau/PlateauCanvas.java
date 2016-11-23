package vue.Plateau;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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

	private Plateau plateau;

	private Case caseSelectionnee = null;

	public PlateauCanvas(double width, double height) {
		super(width, height);
		this.calculerZoom();
		this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				PlateauCanvas.this.onCaseClicked(mouseEvent.getX(), mouseEvent.getY());
				PlateauCanvas.this.dessinerPlateau();
			}
		});
	}

	public Plateau getPlateau() {
		return this.plateau;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}

	public Case getCaseSelectionnee() {
		return this.caseSelectionnee;
	}

	public void setCaseSelectionnee(Case caseSelectionnee) {
		this.caseSelectionnee = caseSelectionnee;
	}

	public double getPlateauWidth() {
		return this.plateauWidth;
	}

	public void setPlateauWidth(double plateauWidth) {
		this.plateauWidth = plateauWidth;
	}

	public double getPlateauHeight() {
		return this.plateauHeight;
	}

	public void setPlateauHeight(double plateauHeight) {
		this.plateauHeight = plateauHeight;
	}

	public double getOffsetX() {
		return this.offsetX;
	}

	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
	}

	public double getOffsetY() {
		return this.offsetY;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}

	private void effacer() {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}

	public void dessinerPlateau() {
		this.effacer();
		if (this.plateau != null) {
			this.dessinerFond();
			this.dessinerFondPlateau();
			this.dessinerCases();
			// this.dessinerPions();
			// this.dessinerCaseSelectionnee();
		}
	}

	@Override
	public void resize(double width, double height) {
		this.setWidth(width);
		this.setHeight(height);
		this.calculerZoom();
	}

	private void dessinerFond() {
		GraphicsContext gc = this.getGraphicsContext2D();
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

	private void dessinerCases() {
		double largeurCase = this.plateauWidth / Plateau.NB_LIGNES;
		double longueurCase = this.plateauHeight / Plateau.NB_COLONNES;
		GraphicsContext gc = this.getGraphicsContext2D();

		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				Case caseCourante = this.plateau.getCases()[i][j];
				if (caseCourante.getCouleur() == Couleur.BLANC) {
					gc.setFill(new Color(1, 1, 1, 0.5));

				} else {
					gc.setFill(new Color(0, 0, 0, 0.5));
				}
				gc.fillRect((j * largeurCase) + this.offsetX, (i * longueurCase) + this.offsetY, largeurCase,
						longueurCase);
			}
		}
	}

	private void dessinerPions() {
		double largeurCase = this.plateauWidth / Plateau.NB_LIGNES;
		double hauteurCase = this.plateauHeight / Plateau.NB_COLONNES;
		GraphicsContext gc = this.getGraphicsContext2D();
		double margin = 5;

		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				Case caseCourante = this.plateau.getCases()[i][j];

				if (!caseCourante.estVide()) {
					String imagePath = null;
					Piece piece = caseCourante.getPiece();
					if (Dame.class.isInstance(caseCourante.getPiece())) {
						if (piece.getCouleur() == Couleur.BLANC) {
							imagePath = this.classLoader.getResource("dame_blanche.png").toString();
						} else {
							imagePath = this.classLoader.getResource("dame_noire.png").toString();
						}
					} else {
						if (piece.getCouleur() == Couleur.BLANC) {
							imagePath = this.classLoader.getResource("pion_blanc.png").toString();
						} else {
							imagePath = this.classLoader.getResource("pion_noir.png").toString();
						}
					}
					gc.drawImage(
							new Image(imagePath, largeurCase - (2 * margin), hauteurCase - (2 * margin), false, false),
							(largeurCase * j) + this.offsetX + margin, (hauteurCase * i) + this.offsetY + margin);
				}
			}
		}
	}

	private void dessinerCaseSelectionnee() {
		if (this.caseSelectionnee != null) {
			double largeurCase = this.plateauWidth / Plateau.NB_LIGNES;
			double hauteurCase = this.plateauHeight / Plateau.NB_COLONNES;
			GraphicsContext gc = this.getGraphicsContext2D();

			gc.setFill(new Color(0, 0, 1, 0.5));
			gc.fillRect((this.caseSelectionnee.getColonne() * largeurCase) + this.offsetX,
					(this.caseSelectionnee.getLigne() * hauteurCase) + this.offsetY, largeurCase, hauteurCase);
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

	private void onCaseClicked(double x, double y) {
		double largeurCase = this.plateauWidth / Plateau.NB_LIGNES;
		double hauteurCase = this.plateauHeight / Plateau.NB_COLONNES;
		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				double x1 = (j * largeurCase) + this.offsetX;
				double x2 = x1 + largeurCase;
				double y1 = (i * hauteurCase) + this.offsetY;
				double y2 = y1 + hauteurCase;
				if ((x >= x1) && (x < x2) && (y >= y1) && (y < y2)) {
					this.caseSelectionnee = this.plateau.getCases()[i][j];
					System.out.println(this.caseSelectionnee);
					break;
				}
			}
		}
	}

}
