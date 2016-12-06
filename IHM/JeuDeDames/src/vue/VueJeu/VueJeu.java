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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modele.Case;
import modele.Dame;
import modele.Piece;
import modele.Pion;
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
	private Label labelVitesse;

	@FXML
	private HBox hBoxVitesse;

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

	@FXML
	private ImageView imageViewPlay;

	@FXML
	private ImageView imageViewFastForward;

	@FXML
	private ImageView imageViewPause;

	private final Tooltip tooltipPlay = new Tooltip("Play");

	private final Tooltip tooltipFastForward = new Tooltip("Avance rapide");

	private final Tooltip tooltipPause = new Tooltip("Pause");

	private PlateauGroup plateauGroup;

	private Plateau plateau;

	@FXML
	private HBox hBoxSimulation;

	@FXML
	private VBox vBox;

	@FXML
	private Label labelPionsBlancs;

	@FXML
	private Label labelDamesBlanches;

	@FXML
	private Label labelPionsNoirs;

	@FXML
	private Label labelDamesNoires;

	@FXML
	private VBox vBoxJoueur1;

	@FXML
	private VBox vBoxJoueur2;

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

		this.imageViewPlay.setImage(new Image(this.classLoader.getResource("play_noir.png").toString()));
		this.imageViewFastForward.setImage(new Image(this.classLoader.getResource("fast_forward_noir.png").toString()));
		this.imageViewPause.setImage(new Image(this.classLoader.getResource("pause_noir.png").toString()));

		this.imageViewPlay.setDisable(true);

		Tooltip.install(this.imageViewPlay, this.tooltipPlay);
		Tooltip.install(this.imageViewFastForward, this.tooltipFastForward);
		Tooltip.install(this.imageViewPause, this.tooltipPause);
	}

	public Controleur getControleur() {
		return this.controleur;
	}

	public void setControleur(Controleur controleur) {
		this.controleur = controleur;
	}

	public void dessinerPlateauCanvas() {
		this.plateauGroup.dessinerPlateauCanvas();
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

	public void tuerPiece(Piece piece, int dureeDAttente) {
		this.plateauGroup.tuerPiece(piece, dureeDAttente);
	}

	public void tuerPieces(List<Piece> pieces, int dureeDAttente) {
		for (Piece p : pieces) {
			this.plateauGroup.tuerPiece(p, dureeDAttente);
		}
	}

	@FXML
	public void onButtonQuitterPartieClicked() {
		this.controleur.cliquerSurQuitterPartie();
	}

	public void setTextLabelJoueur1(String text) {
		this.labelJoueur1.setText(text);
	}

	public void setTextLabelJoueur2(String text) {
		this.labelJoueur2.setText(text);
	}

	public void setPiecesNoiresClickable(boolean clickable) {
		this.plateauGroup.setPiecesVuesNoiresClickable(clickable);
	}

	public void setPiecesBlanchesClickable(boolean clickable) {
		this.plateauGroup.setPiecesVuesBlanchesClickable(clickable);
	}

	@FXML
	private void onImageViewPlayClicked() {
		this.controleur.playSimulation();
	}

	@FXML
	private void onImageViewPauseClicked() {
		this.controleur.pauseSimulation();
	}

	@FXML
	private void onImageViewFastForwardClicked() {
		this.controleur.fastForwardSimulation();
	}

	@FXML
	private void onImageViewPlayEntered() {
		if (!this.imageViewPlay.isDisable()) {
			this.imageViewPlay.setImage(new Image(this.classLoader.getResource("play_gris.png").toString()));
		}
	}

	@FXML
	private void onImageViewPauseEntered() {
		if (!this.imageViewPause.isDisable()) {
			this.imageViewPause.setImage(new Image(this.classLoader.getResource("pause_gris.png").toString()));
		}
	}

	@FXML
	private void onImageViewFastForwardEntered() {
		if (!this.imageViewFastForward.isDisable()) {
			this.imageViewFastForward
					.setImage(new Image(this.classLoader.getResource("fast_forward_gris.png").toString()));
		}
	}

	@FXML
	private void onImageViewPlayExited() {
		if (!this.imageViewPlay.isDisable()) {
			this.imageViewPlay.setImage(new Image(this.classLoader.getResource("play_noir.png").toString()));
		}
	}

	@FXML
	private void onImageViewPauseExited() {
		if (!this.imageViewPause.isDisable()) {
			this.imageViewPause.setImage(new Image(this.classLoader.getResource("pause_noir.png").toString()));
		}
	}

	@FXML
	private void onImageViewFastForwardExited() {
		if (!this.imageViewFastForward.isDisable()) {
			this.imageViewFastForward
					.setImage(new Image(this.classLoader.getResource("fast_forward_noir.png").toString()));
		}
	}

	public void setImageViewPlayDisable(boolean disable) {
		this.imageViewPlay.setDisable(disable);
		if (disable) {
			this.imageViewPlay.setImage(new Image(this.classLoader.getResource("play_gris.png").toString()));
		} else {
			this.imageViewPlay.setImage(new Image(this.classLoader.getResource("play_noir.png").toString()));
		}
	}

	public void setImageViewPauseDisable(boolean disable) {
		this.imageViewPause.setDisable(disable);
		if (disable) {
			this.imageViewPause.setImage(new Image(this.classLoader.getResource("pause_gris.png").toString()));
		} else {
			this.imageViewPause.setImage(new Image(this.classLoader.getResource("pause_noir.png").toString()));
		}
	}

	public void setImageViewFastForwardDisable(boolean disable) {
		this.imageViewFastForward.setDisable(disable);
		if (disable) {
			this.imageViewFastForward
					.setImage(new Image(this.classLoader.getResource("fast_forward_gris.png").toString()));
		} else {
			this.imageViewFastForward
					.setImage(new Image(this.classLoader.getResource("fast_forward_noir.png").toString()));
		}
	}

	public void creerDame(Pion pion, Dame dame, int dureeDAttente) {
		this.plateauGroup.creerDame(pion, dame, dureeDAttente);
	}

	public void setSimulationMode(boolean simulation) {
		if (!simulation) {
			this.vBox.getChildren().remove(this.hBoxSimulation);
			this.vBox.getChildren().remove(this.hBoxVitesse);
		}
	}

	public void setTextLabelVitesse(String text) {
		this.labelVitesse.setText(text);
	}

	public void setTextLabelNbCoups(String text) {
		this.labelNbCoups.setText(text);
	}

	public void pieceSelectionnee(Piece piece) {
		this.controleur.pieceSelectionnee(piece);
	}

	public void setCaseEnSurBrillance(List<Case> cases) {
		this.plateauGroup.setCaseEnSurBrillance(cases);
	}

	public void caseEnSurBrillanceSelectionnee(Case c) {
		this.controleur.caseEnSurBrillanceSelectionnee(c);
	}

	public void majAffichageNbPieces(int nbPionsNoirs, int nbDamesNoires, int nbPionsBlancs, int nbDamesBlanches) {
		this.labelPionsNoirs.setText("" + nbPionsNoirs);
		this.labelDamesNoires.setText("" + nbDamesNoires);
		this.labelPionsBlancs.setText("" + nbPionsBlancs);
		this.labelDamesBlanches.setText("" + nbDamesBlanches);
	}

	public void setBorderColorJoueur1(boolean activ) {
		if (activ) {
			this.vBoxJoueur1.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		} else {
			this.vBoxJoueur1.setStyle("-fx-border-color: red; -fx-border-width: 0px;");
		}
	}

	public void setBorderColorJoueur2(boolean activ) {
		if (activ) {
			this.vBoxJoueur2.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		} else {
			this.vBoxJoueur2.setStyle("-fx-border-color: red; -fx-border-width: 0px;");
		}
	}
}
