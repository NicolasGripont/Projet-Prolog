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

	private List<PieceVue> pieces;

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
		this.pieces = new ArrayList<>();
		for (int i = 0; i < Plateau.NB_LIGNES; i++) {
			for (int j = 0; j < Plateau.NB_COLONNES; j++) {
				Case caseCourante = this.plateau.getCases()[i][j];
				if (!caseCourante.estVide()) {
					PieceVue circle = this.creerPiece(caseCourante.getPiece());
					this.getChildren().add(circle);
					this.pieces.add(circle);
				}
			}
		}
	}

	private PieceVue creerPiece(Piece piece) {
		PieceVue pieceVue = new PieceVue(piece);

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
				double offsetX = t.getSceneX() - PlateauGroup.this.orgSceneX;
				double offsetY = t.getSceneY() - PlateauGroup.this.orgSceneY;

				PieceVue c = (PieceVue) (t.getSource());

				c.setCenterX(c.getCenterX() + offsetX);
				c.setCenterY(c.getCenterY() + offsetY);

				PlateauGroup.this.orgSceneX = t.getSceneX();
				PlateauGroup.this.orgSceneY = t.getSceneY();
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
				PlateauGroup.this.vueJeu.deplacerPiece(piece, nouvellePosition);
			}
		});
		return pieceVue;
	}

	private void dessinerPions() {
		double largeurCase = this.plateauCanvas.getPlateauWidth() / Plateau.NB_LIGNES;
		double hauteurCase = this.plateauCanvas.getPlateauHeight() / Plateau.NB_COLONNES;
		double margin = 5;

		for (PieceVue pieceVue : this.pieces) {
			Case caseCourante = pieceVue.getPiece().getPosition();
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

}
