package vue.VueJeu;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import controleur.Controleur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import modele.Case;
import modele.Piece;
import modele.Plateau;
import vue.Plateau.PlateauGroup;

public class VueJeu implements Initializable {

	private Controleur controleur;

	private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	@FXML
	private StackPane stackPanePlateau;

	@FXML
	private Button buttonQuitterPartie;

	@FXML
	private Label labelTimer;

	@FXML
	private Label labelNbCoups;

	@FXML
	private Label labelJoueur1;

	@FXML
	private Label labelJoueur2;

	@FXML
	private ImageView imageViewPionsJoueur1;

	@FXML
	private ImageView imageViewDamesJoueur1;

	@FXML
	private ImageView imageViewPionsJoueur2;

	@FXML
	private ImageView imageViewDamesJoueur2;

	private PlateauGroup plateauGroup;

	private Plateau plateau;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.plateauGroup = new PlateauGroup(this.stackPanePlateau.getPrefWidth(),
				this.stackPanePlateau.getPrefHeight(), this);
		this.stackPanePlateau.getChildren().add(this.plateauGroup);

		final ChangeListener<Number> listener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				VueJeu.this.plateauGroup.resize(VueJeu.this.stackPanePlateau.getWidth(),
						VueJeu.this.stackPanePlateau.getHeight());
				VueJeu.this.dessinerPlateau();
			}
		};

		this.stackPanePlateau.widthProperty().addListener(listener);
		this.stackPanePlateau.heightProperty().addListener(listener);

		this.imageViewDamesJoueur1.setImage(new Image(this.classLoader.getResource("dame_blanche.png").toString()));
		this.imageViewPionsJoueur1.setImage(new Image(this.classLoader.getResource("pion_blanc.png").toString()));
		this.imageViewDamesJoueur2.setImage(new Image(this.classLoader.getResource("dame_noire.png").toString()));
		this.imageViewPionsJoueur2.setImage(new Image(this.classLoader.getResource("pion_noir.png").toString()));
	}

	public Controleur getControleur() {
		return this.controleur;
	}

	public void setControleur(Controleur controleur) {
		this.controleur = controleur;
	}

	public void dessinerPlateau() {
		this.plateauGroup.dessinerPlateau();
	}

	public Plateau getPlateau() {
		return this.plateau;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
		this.plateauGroup.setPlateau(plateau);
	}

	public void dragAndDropPiece(Piece piece, Case nouvellePosition) {
		this.controleur.deplacerPiece(piece, nouvellePosition);
	}

	public void deplacerPiece(Piece piece, List<Case> deplacement, int dureeDeplacement) {
		this.plateauGroup.deplacerPiece(piece, deplacement, dureeDeplacement);
	}

	public void tuerPieces(List<Piece> pieces, int dureeDAttente) {
		for (Piece p : pieces) {
			this.plateauGroup.tuerPiece(p, dureeDAttente);
		}
	}

	@FXML
	public void onButtonQuitterPartieClicked() {
		this.controleur.cliquerSurQuitter();
	}

}
