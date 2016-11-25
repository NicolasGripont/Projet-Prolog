package controleur;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Case;
import modele.Couleur;
import modele.Piece;
import modele.Plateau;
import vue.VueJeu.VueJeu;
import vue.VuePopUpQuitter.VuePopUpQuitter;

public class Controleur extends Application {

	protected Stage stage;

	private VueJeu vueJeu;

	private Plateau plateau;

	private VuePopUpQuitter myVuePopUpQuitter;

	private Stage stagePopUpQuitter;

	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		this.stage.setMinWidth(900); // Largeur minimum fixée
		this.stage.setMinHeight(620); // Hauteur minimum fixée
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/vue/vueJeu/VueJeu.fxml"));
			Parent root;
			root = fxmlLoader.load();
			this.vueJeu = (VueJeu) fxmlLoader.getController();
			this.vueJeu.setControleur(this);
			this.plateau = new Plateau();
			this.plateau.initPions();
			this.vueJeu.setPlateau(this.plateau);
			this.vueJeu.dessinerPlateau();
			Scene scene = new Scene(root, this.stage.getWidth(), this.stage.getHeight());
			this.stage.setTitle("Jeu De Dames");
			this.stage.setScene(scene);
			this.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cliquerSurQuitter() {
		try {
			this.myVuePopUpQuitter = new VuePopUpQuitter();
			this.myVuePopUpQuitter.setMyControleur(this);
			FXMLLoader fxmlLoader = new FXMLLoader(
					this.getClass().getResource("/vue/VuePopUpQuitter/VuePopUpQuitter.fxml"));
			Parent root;
			root = fxmlLoader.load();
			this.stagePopUpQuitter = new Stage();
			this.stagePopUpQuitter.initModality(Modality.APPLICATION_MODAL);
			Scene myScene = new Scene(root);
			this.stagePopUpQuitter.setScene(myScene);
			this.stagePopUpQuitter.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cliquerSurNonQuitter() throws IOException {
		this.stagePopUpQuitter.close();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void deplacerPiece(Piece piece, Case nouvellePosition) {
		if ((nouvellePosition != null) && nouvellePosition.estVide()
				&& (nouvellePosition.getCouleur() == Couleur.NOIR)) {
			piece.getPosition().setPiece(null);
			piece.setPosition(nouvellePosition);
			nouvellePosition.setPiece(piece);
		}
		this.vueJeu.dessinerPlateau();
	}

	public Plateau getPlateau() {
		return this.plateau;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
		this.vueJeu.setPlateau(plateau);
	}

}
