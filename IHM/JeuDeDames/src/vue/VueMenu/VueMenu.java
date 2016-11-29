package vue.VueMenu;

import java.net.URL;
import java.util.ResourceBundle;

import controleur.Controleur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import modele.TypeJoueur;

public class VueMenu implements Initializable {

	private static final String IA_ALEATOIRE = "IA Aléatoire";
	private static final String JOUEUR_REEL = "Joueur Réel";

	private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	private Controleur controleur;

	@FXML
	private ComboBox<String> comboxJoueur1;

	@FXML
	private ComboBox<String> comboxJoueur2;

	@FXML
	private Button buttonLancement;

	@FXML
	private TextField textFieldNomJoueur1;

	@FXML
	private TextField textFieldNomJoueur2;

	@FXML
	private ImageView imageViewPionBlanc;

	@FXML
	private ImageView imageViewDameBlanche;

	@FXML
	private ImageView imageViewPionNoir;

	@FXML
	private ImageView imageViewDameNoire;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ObservableList<String> options = FXCollections
				.observableArrayList(FXCollections.observableArrayList(IA_ALEATOIRE, JOUEUR_REEL));
		this.comboxJoueur1.setItems(options);
		this.comboxJoueur1.setValue(this.comboxJoueur1.getItems().get(0));
		this.comboxJoueur2.setItems(options);
		this.comboxJoueur2.setValue(this.comboxJoueur2.getItems().get(0));

		this.imageViewPionBlanc.setImage(new Image(this.classLoader.getResource("pion_blanc.png").toString()));
		this.imageViewDameBlanche.setImage(new Image(this.classLoader.getResource("dame_blanche.png").toString()));
		this.imageViewPionNoir.setImage(new Image(this.classLoader.getResource("pion_noir.png").toString()));
		this.imageViewDameNoire.setImage(new Image(this.classLoader.getResource("dame_noire.png").toString()));

		this.textFieldNomJoueur1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				VueMenu.this.textFieldNomJoueur1.selectAll();
			}
		});

		this.textFieldNomJoueur2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				VueMenu.this.textFieldNomJoueur2.selectAll();
			}
		});
	}

	public Controleur getControleur() {
		return this.controleur;
	}

	public void setControleur(Controleur controleur) {
		this.controleur = controleur;
	}

	@FXML
	public void onButtonLancerPartieClicked() {
		TypeJoueur typeJoueur1 = this.getTypeJoueur(this.comboxJoueur1.getSelectionModel().getSelectedItem());
		TypeJoueur typeJoueur2 = this.getTypeJoueur(this.comboxJoueur2.getSelectionModel().getSelectedItem());

		if ((typeJoueur1 != TypeJoueur.INCONNU) && (typeJoueur2 != TypeJoueur.INCONNU)) {
			this.controleur.lancerPartie(typeJoueur1, this.textFieldNomJoueur1.getText(), typeJoueur2,
					this.textFieldNomJoueur2.getText());
		} else {
			System.out.println(typeJoueur1 + " " + typeJoueur2);
		}
	}

	private TypeJoueur getTypeJoueur(String type) {
		System.out.println(type);
		if (type.equals(IA_ALEATOIRE)) {
			return TypeJoueur.IA_ALEATOIRE;
		} else if (type.equals(JOUEUR_REEL)) {
			return TypeJoueur.JOUEUR_REEL;
		}
		return TypeJoueur.INCONNU;
	}
}
