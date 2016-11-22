package vue.Plateau;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import modele.Case;
import modele.Couleur;
import modele.Dame;
import modele.Piece;
import modele.Plateau;

public class PlateauGroup extends Group {
	private Plateau plateau;

	private final PlateauCanvas plateauCanvas;

	private HashMap<Case, Circle> pions;

	public PlateauGroup(double width, double height) {
		super();
		this.plateauCanvas = new PlateauCanvas(width, height);
		this.getChildren().add(this.plateauCanvas);

	}

	public Plateau getPlateau() {
		return this.plateau;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
		this.plateauCanvas.setPlateau(plateau);

	}

	public void dessinerPlateau() {
		this.getChildren().clear();
		this.getChildren().add(this.plateauCanvas);
		this.plateauCanvas.dessinerPlateau();
		this.dessinerPions();
	}

	@Override
	public void resize(double width, double height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		this.plateauCanvas.resize(width, height);
	}

	private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	double orgSceneX, orgSceneY;

	private Circle createCircle(double x, double y, double r) {
		Circle circle = new Circle(x, y, r);

		circle.setCursor(Cursor.HAND);

		circle.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				PlateauGroup.this.orgSceneX = t.getSceneX();
				PlateauGroup.this.orgSceneY = t.getSceneY();

				Circle c = (Circle) (t.getSource());
				c.toFront();
			}
		});
		circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				double offsetX = t.getSceneX() - PlateauGroup.this.orgSceneX;
				double offsetY = t.getSceneY() - PlateauGroup.this.orgSceneY;

				Circle c = (Circle) (t.getSource());

				c.setCenterX(c.getCenterX() + offsetX);
				c.setCenterY(c.getCenterY() + offsetY);

				PlateauGroup.this.orgSceneX = t.getSceneX();
				PlateauGroup.this.orgSceneY = t.getSceneY();
			}
		});

		circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				System.out.println(t.getX() + " " + t.getY());
			}
		});
		return circle;
	}

	private void dessinerPions() {
		double largeurCase = this.plateauCanvas.getPlateauWidth() / Plateau.NB_LIGNES;
		double hauteurCase = this.plateauCanvas.getPlateauHeight() / Plateau.NB_COLONNES;
		double margin = 5;

		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				Case caseCourante = this.plateau.getCases()[i][j];

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

					Image img = new Image(imagePath, largeurCase - (2 * margin), hauteurCase - (2 * margin), false,
							false);
					double x = (largeurCase * j) + this.plateauCanvas.getOffsetX() + (largeurCase / 2);
					double y = (hauteurCase * i) + this.plateauCanvas.getOffsetY() + (hauteurCase / 2);
					double r = (largeurCase - (2 * margin)) / 2;
					Circle circle = this.createCircle(x, y, r);
					Paint paint = new ImagePattern(img);
					circle.setFill(paint);

					this.getChildren().add(circle);
				}
			}
		}
	}
}
