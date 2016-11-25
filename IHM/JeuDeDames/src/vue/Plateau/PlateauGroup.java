package vue.Plateau;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import modele.Case;
import modele.Couleur;
import modele.Dame;
import modele.Piece;
import modele.Plateau;
import vue.VueJeu.VueJeu;

public class PlateauGroup extends Group {
	private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	double orgSceneX;
	double orgSceneY;

	private Plateau plateau;

	private final PlateauCanvas plateauCanvas;

	private List<PieceVue> pieceVues;

	private final VueJeu vueJeu;

	public PlateauGroup(double width, double height, VueJeu vueJeu) {
		super();
		this.plateauCanvas = new PlateauCanvas(width, height);
		this.getChildren().add(this.plateauCanvas);
		this.vueJeu = vueJeu;
	}

	public Plateau getPlateau() {
		return this.plateau;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
		this.plateauCanvas.setPlateau(plateau);
		this.creerPieces();
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

	public void creerPieces() {
		this.pieceVues = new ArrayList<>();
		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				Case caseCourante = this.plateau.getCases()[i][j];
				if (!caseCourante.estVide()) {
					PieceVue circle = new PieceVue(caseCourante.getPiece());
					this.getChildren().add(circle);
					this.pieceVues.add(circle);
					this.setPieceVueDragable(circle, true);
				}
			}
		}
	}

	private void setPieceVueDragable(PieceVue pieceVue, boolean dragable) {
		if (dragable) {
			pieceVue.setCursor(Cursor.HAND);

			pieceVue.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
					PlateauGroup.this.orgSceneX = t.getSceneX();
					PlateauGroup.this.orgSceneY = t.getSceneY();

					PieceVue c = (PieceVue) (t.getSource());
					c.toFront();
				}
			});

			pieceVue.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
					double largeurCase = PlateauGroup.this.plateauCanvas.getPlateauWidth() / Plateau.NB_LIGNES;
					double hauteurCase = PlateauGroup.this.plateauCanvas.getPlateauHeight() / Plateau.NB_COLONNES;
					double offsetX = t.getSceneX() - PlateauGroup.this.orgSceneX;
					double offsetY = t.getSceneY() - PlateauGroup.this.orgSceneY;

					PieceVue c = (PieceVue) (t.getSource());

					if (((c.getCenterX() + offsetX) > (largeurCase / 2))
							&& ((c.getCenterX() + offsetX) < (PlateauGroup.this.plateauCanvas.getWidth()
									- (largeurCase / 2)))
							&& ((c.getCenterY() + offsetY) > (hauteurCase / 2)) && ((c.getCenterY()
									+ offsetY) < (PlateauGroup.this.plateauCanvas.getHeight() - (hauteurCase / 2)))) {
						c.setCenterX(c.getCenterX() + offsetX);
						c.setCenterY(c.getCenterY() + offsetY);

						PlateauGroup.this.orgSceneX = t.getSceneX();
						PlateauGroup.this.orgSceneY = t.getSceneY();
					}
				}
			});

			pieceVue.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {

					double x = t.getX();
					double y = t.getY();
					double largeurCase = PlateauGroup.this.plateauCanvas.getPlateauWidth() / Plateau.NB_LIGNES;
					double hauteurCase = PlateauGroup.this.plateauCanvas.getPlateauHeight() / Plateau.NB_COLONNES;
					Case nouvellePosition = null;
					for (int i = 0; i < Plateau.NB_LIGNES; i++) {
						for (int j = 0; j < Plateau.NB_COLONNES; j++) {
							double x1 = (j * largeurCase) + PlateauGroup.this.plateauCanvas.getOffsetX();
							double x2 = x1 + largeurCase;
							double y1 = (i * hauteurCase) + PlateauGroup.this.plateauCanvas.getOffsetY();
							double y2 = y1 + hauteurCase;
							if ((x >= x1) && (x < x2) && (y >= y1) && (y < y2)) {
								nouvellePosition = PlateauGroup.this.plateau.getCases()[i][j];
								break;
							}
						}
					}
					PlateauGroup.this.vueJeu.dragAndDropPiece(pieceVue.getPiece(), nouvellePosition);
				}
			});
		} else {
			pieceVue.setCursor(Cursor.HAND);
		}
	}

	private void dessinerPions() {
		double largeurCase = this.plateauCanvas.getPlateauWidth() / Plateau.NB_LIGNES;
		double hauteurCase = this.plateauCanvas.getPlateauHeight() / Plateau.NB_COLONNES;
		double margin = 5;

		for (PieceVue pieceVue : this.pieceVues) {
			Case caseCourante = pieceVue.getPiece().getPosition();
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

				Image img = new Image(imagePath, largeurCase - (2 * margin), hauteurCase - (2 * margin), false, false);
				double x = (largeurCase * caseCourante.getColonne()) + this.plateauCanvas.getOffsetX()
						+ (largeurCase / 2);
				double y = (hauteurCase * caseCourante.getLigne()) + this.plateauCanvas.getOffsetY()
						+ (hauteurCase / 2);
				double r = (largeurCase - (2 * margin)) / 2;

				pieceVue.setCenterX(x);
				pieceVue.setCenterY(y);
				pieceVue.setRadius(r);
				Paint paint = new ImagePattern(img);
				pieceVue.setFill(paint);

				this.getChildren().add(pieceVue);
			}
		}
	}

	public void deplacerPiece(Piece piece, List<Case> deplacement) {
		double largeurCase = this.plateauCanvas.getPlateauWidth() / Plateau.NB_LIGNES;
		double hauteurCase = this.plateauCanvas.getPlateauHeight() / Plateau.NB_COLONNES;
		double margin = 5;
		PieceVue pieceVue = null;
		for (PieceVue p : this.pieceVues) {
			if (p.getPiece() == piece) {
				pieceVue = p;
				break;
			}
		}
		if (pieceVue != null) {
			double xStart = pieceVue.getCenterX();
			double yStart = pieceVue.getCenterY();
			double xEnd = xStart;
			double yEnd = yStart;

			// Path path = new Path();
			//
			// path.getElements().add(new MoveTo(xStart, yStart));
			//
			for (Case c : deplacement) {
				xEnd = (largeurCase * c.getColonne()) + this.plateauCanvas.getOffsetX() + (largeurCase / 2);
				yEnd = (hauteurCase * c.getLigne()) + this.plateauCanvas.getOffsetY() + (hauteurCase / 2);
				// path.getElements().add(new CubicCurveTo(xStart, yStart,
				// xStart, yStart, xEnd, yEnd));
				xStart = xEnd;
				yStart = yEnd;
			}
			//
			// PathTransition pathTransition = new PathTransition();
			// pathTransition.setDuration(Duration.millis(deplacement.size() *
			// 1000));
			// pathTransition.setNode(pieceVue);
			// pathTransition.setPath(path);
			// pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
			// pathTransition.setCycleCount(1);
			// pathTransition.setAutoReverse(false);
			// pathTransition.play();

			pieceVue.setCenterX(xEnd);
			pieceVue.setCenterY(yEnd);
		}
	}

	public void tuerPiece(Piece piece) {
		PieceVue pieceATuer = null;
		for (PieceVue pieceVue : this.pieceVues) {
			if (pieceVue.getPiece() == piece) {
				pieceATuer = pieceVue;
				break;
			}
		}

		if (pieceATuer != null) {
			this.getChildren().remove(pieceATuer);
		}
	}

}
