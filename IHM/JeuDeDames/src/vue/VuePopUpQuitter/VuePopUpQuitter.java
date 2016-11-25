package vue.VuePopUpQuitter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controleur.Controleur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class VuePopUpQuitter implements Initializable {

	private Controleur myControleur;

	@FXML
	private Button buttonOui;

	@FXML
	private Button buttonNon;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// // TODO Auto-generated method stub
		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {
		// VuePopUpQuitter.this.buttonNon.requestFocus();
		// }
		// });
	}

	@FXML
	public void onButtonOui() {

	}

	@FXML
	public void onButtonNon() {
		System.out.println(this.myControleur);
		try {
			this.myControleur.cliquerSurNonQuitter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Controleur getMyControleur() {
		return this.myControleur;
	}

	public void setMyControleur(Controleur myControleur) {
		this.myControleur = myControleur;
		System.out.println(this.myControleur);
	}
}
