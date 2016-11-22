package vue.VueJeu;

import java.net.URL;
import java.util.ResourceBundle;

import controleur.Controleur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import modele.Plateau;
import vue.Plateau.PlateauCanvas;

public class VueJeu implements Initializable {

	private Controleur controleur;

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

	private PlateauCanvas plateauCanvas;

	private Plateau plateau;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.plateauCanvas = new PlateauCanvas(this.stackPanePlateau.getPrefWidth(),
				this.stackPanePlateau.getPrefHeight());
		this.stackPanePlateau.getChildren().add(this.plateauCanvas);

		final ChangeListener<Number> listener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				VueJeu.this.plateauCanvas.resize(VueJeu.this.stackPanePlateau.getWidth(),
						VueJeu.this.stackPanePlateau.getHeight());
				VueJeu.this.dessinerPlateau();
			}

		};
		this.stackPanePlateau.widthProperty().addListener(listener);
		this.stackPanePlateau.heightProperty().addListener(listener);
	}

	public Controleur getControleur() {
		return this.controleur;
	}

	public void setControleur(Controleur controleur) {
		this.controleur = controleur;
	}

	public void dessinerPlateau() {
		this.plateauCanvas.dessinerPlateau(this.plateau);
	}

	public Plateau getPlateau() {
		return this.plateau;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}

}
