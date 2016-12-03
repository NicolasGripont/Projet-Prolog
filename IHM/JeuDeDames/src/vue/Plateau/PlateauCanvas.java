package vue.Plateau;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.Case;
import modele.Couleur;
import modele.Plateau;

public class PlateauCanvas extends Canvas {

	private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	private static final double margin = 50;
	private double plateauWidth;
	private double plateauHeight;
	private double offsetX;
	private double offsetY;

	private Plateau plateau;

	private final PlateauGroup plateauGroup;

	private List<Case> casesEnSurbrillance = null;

	public PlateauCanvas(double width, double height, PlateauGroup plateauGroup) {
		super(width, height);
		this.plateauGroup = plateauGroup;
		this.calculerZoom();
		this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				PlateauCanvas.this.onPlateauClicked(mouseEvent.getX(), mouseEvent.getY());
			}
		});
	}

	public Plateau getPlateau() {
		return this.plateau;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
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
			this.dessinerCasesEnSurBrillances();
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

	private void onPlateauClicked(double x, double y) {
		double largeurCase = this.plateauWidth / Plateau.NB_LIGNES;
		double hauteurCase = this.plateauHeight / Plateau.NB_COLONNES;
		if (this.casesEnSurbrillance != null) {
			for (Case c : this.casesEnSurbrillance) {
				double x1 = (c.getColonne() * largeurCase) + this.offsetX;
				double x2 = x1 + largeurCase;
				double y1 = (c.getLigne() * hauteurCase) + this.offsetY;
				double y2 = y1 + hauteurCase;
				if ((x >= x1) && (x < x2) && (y >= y1) && (y < y2)) {
					this.plateauGroup.caseEnSurBrillanceSelectionnee(c);
					break;
				}
			}
		}
	}

	public void setCaseEnSurBrillance(List<Case> cases) {
		this.casesEnSurbrillance = cases;
	}

	private void dessinerCasesEnSurBrillances() {
		if (this.casesEnSurbrillance != null) {
			for (Case c : this.casesEnSurbrillance) {
				double largeurCase = this.plateauWidth / Plateau.NB_LIGNES;
				double hauteurCase = this.plateauHeight / Plateau.NB_COLONNES;
				GraphicsContext gc = this.getGraphicsContext2D();

				gc.setFill(new Color(0, 0, 1, 0.5));
				gc.fillRect((c.getColonne() * largeurCase) + this.offsetX, (c.getLigne() * hauteurCase) + this.offsetY,
						largeurCase, hauteurCase);
			}
		}
	}
}
