package vue.Plateau;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import modele.Case;
import modele.Couleur;
import modele.Dame;
import modele.Piece;
import modele.Pion;
import modele.Plateau;
import vue.VueJeu.VueJeu;

public class PlateauGroup extends Group {
	private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	double orgSceneX;
	double orgSceneY;

	private Plateau plateau;

	private final PlateauCanvas plateauCanvas;

	private List<PieceVue> pieceVuesBlanches;

	private List<PieceVue> pieceVuesNoires;

	private final VueJeu vueJeu;

	public PlateauGroup(double width, double height, VueJeu vueJeu) {
		super();
		this.plateauCanvas = new PlateauCanvas(width, height, this);
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

	public void dessinerPlateauCanvas() {
		this.plateauCanvas.dessinerPlateau();
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
		this.pieceVuesBlanches = new ArrayList<>();
		this.pieceVuesNoires = new ArrayList<>();

		for (Piece p : this.plateau.getBlanches()) {
			PieceVue pieceVue = new PieceVue(p);
			this.getChildren().add(pieceVue);
			this.pieceVuesBlanches.add(pieceVue);
			this.setPieceVueClickable(pieceVue, false);
		}

		for (Piece p : this.plateau.getNoires()) {
			PieceVue pieceVue = new PieceVue(p);
			this.getChildren().add(pieceVue);
			this.pieceVuesNoires.add(pieceVue);
			this.setPieceVueClickable(pieceVue, false);
		}
	}

	public void setPiecesVuesNoiresClickable(boolean clickable) {
		for (PieceVue p : this.pieceVuesNoires) {
			this.setPieceVueClickable(p, clickable);
		}
	}

	public void setPiecesVuesBlanchesClickable(boolean clickable) {
		for (PieceVue p : this.pieceVuesBlanches) {
			this.setPieceVueClickable(p, clickable);
		}
	}

	private void setPieceVueClickable(PieceVue pieceVue, boolean clickable) {
		if (clickable) {
			pieceVue.setCursor(Cursor.HAND);

			pieceVue.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
					// PlateauGroup.this.orgSceneX = t.getSceneX();
					// PlateauGroup.this.orgSceneY = t.getSceneY();
					//
					// PieceVue c = (PieceVue) (t.getSource());
					// c.toFront();

				}
			});

			pieceVue.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
					// double largeurCase =
					// PlateauGroup.this.plateauCanvas.getPlateauWidth() /
					// Plateau.NB_LIGNES;
					// double hauteurCase =
					// PlateauGroup.this.plateauCanvas.getPlateauHeight() /
					// Plateau.NB_COLONNES;
					// double offsetX = t.getSceneX() -
					// PlateauGroup.this.orgSceneX;
					// double offsetY = t.getSceneY() -
					// PlateauGroup.this.orgSceneY;
					//
					// PieceVue c = (PieceVue) (t.getSource());
					//
					// if (((c.getCenterX() + offsetX) > (largeurCase / 2))
					// && ((c.getCenterX() + offsetX) <
					// (PlateauGroup.this.plateauCanvas.getWidth()
					// - (largeurCase / 2)))
					// && ((c.getCenterY() + offsetY) > (hauteurCase / 2)) &&
					// ((c.getCenterY()
					// + offsetY) < (PlateauGroup.this.plateauCanvas.getHeight()
					// - (hauteurCase / 2)))) {
					// c.setCenterX(c.getCenterX() + offsetX);
					// c.setCenterY(c.getCenterY() + offsetY);
					//
					// PlateauGroup.this.orgSceneX = t.getSceneX();
					// PlateauGroup.this.orgSceneY = t.getSceneY();
					// }
				}
			});

			pieceVue.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {

					// double x = t.getX();
					// double y = t.getY();
					// double largeurCase =
					// PlateauGroup.this.plateauCanvas.getPlateauWidth() /
					// Plateau.NB_LIGNES;
					// double hauteurCase =
					// PlateauGroup.this.plateauCanvas.getPlateauHeight() /
					// Plateau.NB_COLONNES;
					// Case nouvellePosition = null;
					// for (int i = 0; i < Plateau.NB_LIGNES; i++) {
					// for (int j = 0; j < Plateau.NB_COLONNES; j++) {
					// double x1 = (j * largeurCase) +
					// PlateauGroup.this.plateauCanvas.getOffsetX();
					// double x2 = x1 + largeurCase;
					// double y1 = (i * hauteurCase) +
					// PlateauGroup.this.plateauCanvas.getOffsetY();
					// double y2 = y1 + hauteurCase;
					// if ((x >= x1) && (x < x2) && (y >= y1) && (y < y2)) {
					// nouvellePosition =
					// PlateauGroup.this.plateau.getCases()[i][j];
					// break;
					// }
					// }
					// }
					// PlateauGroup.this.vueJeu.dragAndDropPiece(pieceVue.getPiece(),
					// nouvellePosition);

					PlateauGroup.this.vueJeu.pieceSelectionnee(pieceVue.getPiece());
				}
			});
		} else {
			pieceVue.setCursor(Cursor.DEFAULT);
			pieceVue.setOnMousePressed(null);
			pieceVue.setOnMouseDragged(null);
			pieceVue.setOnMouseClicked(null);
		}
	}

	private void dessinerPions() {
		double largeurCase = this.plateauCanvas.getPlateauWidth() / Plateau.NB_LIGNES;
		double hauteurCase = this.plateauCanvas.getPlateauHeight() / Plateau.NB_COLONNES;
		double margin = 5;

		List<PieceVue> piecesVues = new ArrayList<>();
		piecesVues.addAll(this.pieceVuesBlanches);
		piecesVues.addAll(this.pieceVuesNoires);

		for (PieceVue pieceVue : piecesVues) {
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

	public void deplacerPiece(Piece piece, List<Case> deplacement, int dureeDeplacement) {
		double largeurCase = this.plateauCanvas.getPlateauWidth() / Plateau.NB_LIGNES;
		double hauteurCase = this.plateauCanvas.getPlateauHeight() / Plateau.NB_COLONNES;
		double margin = 5;
		PieceVue pieceVue = null;
		if (piece.getCouleur() == Couleur.BLANC) {
			for (PieceVue p : this.pieceVuesBlanches) {
				if (p.getPiece() == piece) {
					pieceVue = p;
					break;
				}
			}
		} else {
			for (PieceVue p : this.pieceVuesNoires) {
				if (p.getPiece() == piece) {
					pieceVue = p;
					break;
				}
			}
		}

		if (pieceVue != null) {
			double xStart = pieceVue.getCenterX();
			double yStart = pieceVue.getCenterY();
			double xEnd = xStart;
			double yEnd = yStart;

			pieceVue.toFront();

			Path path = new Path();

			path.getElements().add(new MoveTo(xStart, yStart));

			for (Case c : deplacement) {
				xEnd = (largeurCase * c.getColonne()) + this.plateauCanvas.getOffsetX() + (largeurCase / 2);
				yEnd = (hauteurCase * c.getLigne()) + this.plateauCanvas.getOffsetY() + (hauteurCase / 2);
				path.getElements().add(new CubicCurveTo(xStart, yStart, xStart, yStart, xEnd, yEnd));
				xStart = xEnd;
				yStart = yEnd;
			}

			PathTransition pathTransition = new PathTransition();
			pathTransition.setDuration(Duration.millis(dureeDeplacement));
			pathTransition.setNode(pieceVue);
			pathTransition.setPath(path);
			pathTransition.setOrientation(OrientationType.NONE);
			pathTransition.setCycleCount(1);
			pathTransition.setAutoReverse(false);
			pathTransition.play();

			pieceVue.setCenterX(xEnd);
			pieceVue.setCenterY(yEnd);
		}
	}

	public void tuerPiece(Piece piece, int dureeDAttente) {

		PieceVue pieceATuer = null;

		if (piece.getCouleur() == Couleur.BLANC) {
			for (PieceVue p : this.pieceVuesBlanches) {
				if (p.getPiece() == piece) {
					pieceATuer = p;
					break;
				}
			}
		} else {
			for (PieceVue p : this.pieceVuesNoires) {
				if (p.getPiece() == piece) {
					pieceATuer = p;
					break;
				}
			}
		}

		if (pieceATuer != null) {
			Path path = new Path();

			path.getElements().add(new MoveTo(pieceATuer.getCenterX(), pieceATuer.getCenterY()));
			path.getElements()
					.add(new CubicCurveTo(pieceATuer.getCenterX(), pieceATuer.getCenterY(), pieceATuer.getCenterX(),
							pieceATuer.getCenterY(), pieceATuer.getCenterX(), pieceATuer.getCenterY()));
			PathTransition pathTransition = new PathTransition();
			pathTransition.setDelay(Duration.millis(dureeDAttente));
			pathTransition.setDuration(Duration.millis(1));
			pathTransition.setNode(pieceATuer);
			pathTransition.setPath(path);
			pathTransition.setOrientation(OrientationType.NONE);
			pathTransition.setCycleCount(1);
			pathTransition.setAutoReverse(false);
			pathTransition.play();
			pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					PlateauGroup.this.getChildren().remove(pathTransition.getNode());
					PieceVue pieceVue = (PieceVue) pathTransition.getNode();
					if (pieceVue.getPiece().getCouleur() == Couleur.BLANC) {
						PlateauGroup.this.pieceVuesBlanches.remove(pieceVue);
					} else {
						PlateauGroup.this.pieceVuesNoires.remove(pieceVue);
					}
					PlateauGroup.this.vueJeu.getControleur().majAffichageNbPieces();
				}
			});
		}
	}

	public void creerDame(Pion pion, Dame dame, int dureeDAttente) {
		PieceVue pieceVue = null;

		if (pion.getCouleur() == Couleur.BLANC) {
			for (PieceVue p : this.pieceVuesBlanches) {
				if (p.getPiece() == pion) {
					pieceVue = p;
					break;
				}
			}
		} else {
			for (PieceVue p : this.pieceVuesNoires) {
				if (p.getPiece() == pion) {
					pieceVue = p;
					break;
				}
			}
		}

		if (pieceVue != null) {
			pieceVue.setPiece(dame);

			Path path = new Path();

			path.getElements().add(new MoveTo(pieceVue.getCenterX(), pieceVue.getCenterY()));
			path.getElements().add(new CubicCurveTo(pieceVue.getCenterX(), pieceVue.getCenterY(), pieceVue.getCenterX(),
					pieceVue.getCenterY(), pieceVue.getCenterX(), pieceVue.getCenterY()));
			PathTransition pathTransition = new PathTransition();
			pathTransition.setDelay(Duration.millis(dureeDAttente));
			pathTransition.setDuration(Duration.millis(1));
			pathTransition.setNode(pieceVue);
			pathTransition.setPath(path);
			pathTransition.setOrientation(OrientationType.NONE);
			pathTransition.setCycleCount(1);
			pathTransition.setAutoReverse(false);
			pathTransition.play();
			pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					double largeurCase = PlateauGroup.this.plateauCanvas.getPlateauWidth() / Plateau.NB_LIGNES;
					double hauteurCase = PlateauGroup.this.plateauCanvas.getPlateauHeight() / Plateau.NB_COLONNES;
					double margin = 5;
					String imagePath = null;
					if (pion.getCouleur() == Couleur.BLANC) {
						imagePath = PlateauGroup.this.classLoader.getResource("dame_blanche.png").toString();
					} else {
						imagePath = PlateauGroup.this.classLoader.getResource("dame_noire.png").toString();
					}
					Image img = new Image(imagePath, largeurCase - (2 * margin), hauteurCase - (2 * margin), false,
							false);
					PieceVue pionVue = (PieceVue) pathTransition.getNode();
					PieceVue dameVue = new PieceVue(pionVue.getPiece());
					double x = (largeurCase * pionVue.getPiece().getPosition().getColonne())
							+ PlateauGroup.this.plateauCanvas.getOffsetX() + (largeurCase / 2);
					double y = (hauteurCase * pionVue.getPiece().getPosition().getLigne())
							+ PlateauGroup.this.plateauCanvas.getOffsetY() + (hauteurCase / 2);
					double r = (largeurCase - (2 * margin)) / 2;
					dameVue.setCenterX(x);
					dameVue.setCenterY(y);
					dameVue.setRadius(r);
					Paint paint = new ImagePattern(img);
					dameVue.setFill(paint);

					if (pionVue.getPiece().getCouleur() == Couleur.NOIR) {
						PlateauGroup.this.pieceVuesNoires.remove(pionVue);
						PlateauGroup.this.pieceVuesNoires.add(dameVue);
					} else {
						PlateauGroup.this.pieceVuesBlanches.remove(pionVue);
						PlateauGroup.this.pieceVuesBlanches.add(dameVue);
					}

					PlateauGroup.this.getChildren().remove(pionVue);
					PlateauGroup.this.getChildren().add(dameVue);
					PlateauGroup.this.vueJeu.getControleur().majAffichageNbPieces();
				}
			});

		}
	}

	public void setCaseEnSurBrillance(List<Case> cases) {
		this.plateauCanvas.setCaseEnSurBrillance(cases);
	}

	public void caseEnSurBrillanceSelectionnee(Case c) {
		this.vueJeu.caseEnSurBrillanceSelectionnee(c);
	}

}
