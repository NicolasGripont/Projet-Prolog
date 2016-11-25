package vue.VueMenu;

import java.net.URL;
import java.util.ResourceBundle;

import controleur.Controleur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;


public class VueMenu implements Initializable {
	
	private Controleur controleur;
	
	@FXML
	private ComboBox<String> comboxJoueur1;

	@FXML
	private ComboBox<String> comboxJoueur2;
	
	@FXML
	private Button buttonLancement;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "IA 1",
			        "IA 2"
			    );
		this.comboxJoueur1.setItems(options);
		this.comboxJoueur1.setValue("IA 1");
		this.comboxJoueur2.setItems(options);
		this.comboxJoueur2.setValue("IA 1");
		
	}

	public Controleur getControleur() {
		return this.controleur;
	}

	public void setControleur(Controleur controleur) {
		this.controleur = controleur;
	}
	
	@FXML
	public void onButtonLancerPartieClicked() {
		System.out.println("partie");
	}

}
