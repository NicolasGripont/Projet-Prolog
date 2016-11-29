package controleur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Case;
import modele.Couleur;
import modele.Coup;
import modele.Jeu;
import modele.Joueur;
import modele.Piece;
import modele.Plateau;
import modele.TypeJoueur;
import vue.VueJeu.VueJeu;
import vue.VueMenu.VueMenu;
import vue.VuePopUpQuitter.VuePopUpQuitter;

public class Controleur extends Application {

	protected Stage stage;

	private VueJeu vueJeu;

	private VueMenu vueMenu;

	private VuePopUpQuitter myVuePopUpQuitter;

	private Plateau plateau;

	private Stage stagePopUpQuitter;

	private Joueur joueur1;

	private Joueur joueur2;

	private Joueur joueurCourant;

	private Jeu jeu;

	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		this.stage.setMinWidth(900); // Largeur minimum fixée
		this.stage.setMinHeight(620); // Hauteur minimum fixée
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/vue/vueMenu/VueMenu.fxml"));
			Parent root;
			root = fxmlLoader.load();
			this.vueMenu = (VueMenu) fxmlLoader.getController();
			this.vueMenu.setControleur(this);
			this.plateau = new Plateau();
			this.plateau.initPions();
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
			FXMLLoader fxmlLoader = new FXMLLoader(
					this.getClass().getResource("/vue/VuePopUpQuitter/VuePopUpQuitter.fxml"));
			Parent root;
			root = fxmlLoader.load();
			this.myVuePopUpQuitter = (VuePopUpQuitter) fxmlLoader.getController();
			this.myVuePopUpQuitter.setMyControleur(this);
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
			this.plateau.deplacerPiece(piece, nouvellePosition);
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

	public void jouerCoupIA() {
		Plateau plateauClone = this.plateau.clone();
		Coup coup = this.jeu.play(this.joueurCourant.getId(), plateauClone.getBlanches(), plateauClone.getNoires());
		this.jouerCoup(coup.getPiecesBlanches(), coup.getPiecesNoires(), coup.getPiece(), coup.getDeplacement());
		if (this.joueurCourant == this.joueur1) {
			this.joueurCourant = this.joueur2;
		} else {
			this.joueurCourant = this.joueur1;
		}
	}

	/**
	 * 
	 * @param blancs
	 *            liste des pieces blancs avec leur position finale
	 * @param noirs
	 *            liste des pieces noirs avec leur position finale
	 * @param piece
	 *            la piece avec sa position initiale
	 * @param deplacement
	 *            liste des cases qui par lesquelles passe la piece 'piece'
	 * 
	 */
	public void jouerCoup(List<Piece> blanches, List<Piece> noires, Piece piece, List<Case> deplacement) {
		Case positionInitiale = this.plateau.getCases()[piece.getPosition().getLigne()][piece.getPosition()
				.getColonne()];
		Piece piecePlateau = positionInitiale.getPiece();
		List<Piece> piecesMortes = new ArrayList<>();

		// on check les pieces mortes
		if (piecePlateau.getCouleur() == Couleur.NOIR) {
			for (Piece p : this.plateau.getBlanches()) {
				if (!this.contains(blanches, p)) {
					piecesMortes.add(p);
				}
			}
		} else {
			for (Piece p : this.plateau.getNoires()) {
				if (!this.contains(noires, p)) {
					piecesMortes.add(p);
				}
			}
		}

		// recuperation de la case destination
		Case nouvelleCase = this.plateau.getCases()[deplacement.get(deplacement.size() - 1).getLigne()][deplacement
				.get(deplacement.size() - 1).getColonne()];

		// on set la nouvelle position de la piece qui bouge
		this.plateau.deplacerPiece(piecePlateau, nouvelleCase);

		// supprimer piece de l'objet plateau et fire le deplacement 2 sens
		for (Piece p : piecesMortes) {
			this.plateau.supprimerPiece(p);
		}

		// appelle vue
		this.vueJeu.deplacerPiece(piecePlateau, deplacement, deplacement.size() * 1000);
		this.vueJeu.tuerPieces(piecesMortes, (deplacement.size() * 1000) + 500);

	}

	private boolean contains(List<Piece> pieces, Piece piece) {
		boolean result = false;

		for (Piece p : pieces) {
			if (p.getPosition().equals(piece.getPosition())) {
				result = true;
			}
		}

		return result;
	}

	public void lancerPartie(TypeJoueur typeJoueur1, String nomJoueur1, TypeJoueur typeJoueur2, String nomJoueur2) {

		// Initialisation du jeu
		this.jeu = new Jeu("localhost", "5000");
		if (this.jeu.init(new ArrayList<>(), new ArrayList<>()) == -1) {
			this.jeu = null;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Verifier que le serveur est lancé.");
			alert.setContentText(
					"Lancer le serveur : \n- Lancer SWI prolog \n- File > Consult... > Choisir le fichier Jeu.pl\n- Taper la commande : 'server(5000).'");
			alert.showAndWait();
			return;
		}
		this.joueur1 = new Joueur(0, typeJoueur1, nomJoueur1, Couleur.BLANC);
		this.joueur2 = new Joueur(1, typeJoueur2, nomJoueur2, Couleur.NOIR);
		this.plateau = new Plateau();
		this.plateau.initPions();
		this.joueurCourant = this.joueur1;

		// Affichage
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/vue/vueJeu/VueJeu.fxml"));
			Parent root;
			root = fxmlLoader.load();
			this.vueJeu = (VueJeu) fxmlLoader.getController();
			this.vueJeu.setControleur(this);
			this.vueJeu.setTextLabelJoueur1(this.joueur1.getNom());
			this.vueJeu.setTextLabelJoueur2(this.joueur2.getNom());
			this.vueJeu.setPlateau(this.plateau);
			this.vueJeu.dessinerPlateau();
			Scene scene = new Scene(root, this.stage.getScene().getWidth(), this.stage.getScene().getHeight());
			this.stage.setTitle("Jeu De Dames");
			this.stage.setScene(scene);
			this.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
