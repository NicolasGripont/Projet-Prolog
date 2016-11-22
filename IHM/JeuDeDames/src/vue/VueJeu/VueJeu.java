package vue.VueJeu;

import java.net.URL;
import java.util.ResourceBundle;

import controleur.Controleur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public Controleur getControleur() {
		return this.controleur;
	}

	public void setControleur(Controleur controleur) {
		this.controleur = controleur;
	}

}
