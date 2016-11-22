package controleur;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modele.Plateau;
import vue.VueJeu.VueJeu;

public class Controleur extends Application {

	protected Stage stage;

	private VueJeu vueJeu;

	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		// this.stage.setMinWidth(900); // Largeur minimum fixée
		// this.stage.setMinHeight(620); // Hauteur minimum fixée
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/vue/vueJeu/VueJeu.fxml"));
			Parent root;
			root = fxmlLoader.load();
			this.vueJeu = (VueJeu) fxmlLoader.getController();
			this.vueJeu.setControleur(this);
			Plateau plateau = new Plateau();
			plateau.initPions();
			this.vueJeu.dessinerPlateau(plateau);
			Scene scene = new Scene(root, this.stage.getWidth(), this.stage.getHeight());
			this.stage.setTitle("Jeu De Dames");
			this.stage.setScene(scene);
			this.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
