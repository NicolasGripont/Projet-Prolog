package controleur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modele.Case;
import modele.Couleur;
import modele.Coup;
import modele.Dame;
import modele.Jeu;
import modele.Joueur;
import modele.Piece;
import modele.Pion;
import modele.Plateau;
import modele.TypeJoueur;
import vue.VueJeu.VueJeu;
import vue.VueMenu.VueMenu;

public class Controleur extends Application {

	private final int DUREE_UN_DEPLACEMENT_NORMAL = 1000;

	private int dureeUnDeplacement = this.DUREE_UN_DEPLACEMENT_NORMAL;

	protected Stage stage;

	private VueJeu vueJeu;

	private VueMenu vueMenu;

	private Plateau plateau;

	private Joueur joueur1;

	private Joueur joueur2;

	private Joueur joueurCourant;

	private Jeu jeu;

	private Thread threadSimulerPartie = null;

	private final Semaphore sem = new Semaphore(1);

	private int coefVitesse = 1;

	private int nbCoups = 0;

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
			this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent we) {
					if (Controleur.this.vueJeu != null) {
						Controleur.this.pauseSimulation();
					}
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Quitter");
					alert.setHeaderText("Voulez-vous vraiment quitter le jeu ?");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) { // Quitter
						Controleur.this.stage.close();
					}
					// Si on arrive la alors c'est qu'on a annuler
					we.consume();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void cliquerSurQuitterPartie() {
		this.pauseSimulation();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quitter");
		alert.setHeaderText("Voulez-vous vraiment arrêter la partie et revenir à l'écran d'accueil ?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) { // Quitter
			this.plateau = null;
			this.joueur1 = null;
			this.joueur2 = null;
			this.jeu = null;
			this.joueurCourant = null;
			this.vueJeu = null;
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/vue/vueMenu/VueMenu.fxml"));
				Parent root;
				root = fxmlLoader.load();
				this.vueMenu = (VueMenu) fxmlLoader.getController();
				this.vueMenu.setControleur(this);
				this.plateau = new Plateau();
				this.plateau.initPions();
				Scene scene = new Scene(root, this.stage.getWidth(), this.stage.getHeight());
				this.stage.setScene(scene);
				this.stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		Coup coup = this.getCoupIA();

		/**
		 * 0 : Blancs gagnent ,1 : Noir gagnent ,2 : Egalite ,3 : Non terminé
		 */
		if (coup.getEtat() != 3) {
			this.pauseSimulation();
			this.vueJeu.setImageViewPlayDisable(true);
			this.vueJeu.setImageViewFastForwardDisable(true);
			this.vueJeu.setImageViewPauseDisable(true);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Partie terminée");
			String message = "Egalité !"; // coup = 2
			if (coup.getEtat() == 0) {
				alert.setHeaderText(this.joueur1.getNom() + " a gagné !");
			} else if (coup.getEtat() == 1) {
				alert.setHeaderText(this.joueur2.getNom() + " a gagné !");
			}
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) { // Quitter
				Controleur.this.stage.close();
			}
		} else {
			this.jouerCoup(coup.getPiecesBlanches(), coup.getPiecesNoires(), coup.getPiece(), coup.getDeplacement());
			if (this.joueurCourant == this.joueur1) {
				this.joueurCourant = this.joueur2;
			} else {
				this.joueurCourant = this.joueur1;
			}
		}
	}

	public Coup getCoupIA() {
		Plateau plateauClone = this.plateau.clone();
		Coup coup = this.jeu.play(this.joueurCourant.getId(), plateauClone.getBlanches(), plateauClone.getNoires());
		System.out.println(coup.getEtat());
		return coup;
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
	private void jouerCoup(List<Piece> blanches, List<Piece> noires, Piece piece, List<Case> deplacement) {
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

		int dureeDeplacement = this.calculDureeDeplacement(piece, deplacement);
		int dureeCoup = this.calculDureeCoup(dureeDeplacement);
		// appelle vue
		this.vueJeu.deplacerPiece(piecePlateau, deplacement, dureeDeplacement);
		this.vueJeu.tuerPieces(piecesMortes, dureeCoup);

		// Creation dame si besoin TODO : a mettre dans plateau
		if (piecePlateau.getClass().equals(Pion.class)
				&& (((piecePlateau.getPosition().getLigne() == 0) && (piecePlateau.getCouleur() == Couleur.BLANC))
						|| ((piecePlateau.getPosition().getLigne() == (Plateau.NB_LIGNES - 1))
								&& (piecePlateau.getCouleur() == Couleur.NOIR)))) {

			Dame dame = new Dame(piecePlateau.getCouleur(), piecePlateau.getPosition());
			dame.getPosition().setPiece(dame);
			this.vueJeu.creerDame((Pion) piecePlateau, dame, dureeCoup);
		}
		this.nbCoups++;
		this.vueJeu.setTextLabelNbCoups("" + this.nbCoups);
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
			alert.setContentText("Lancer le serveur : " + "\n- Lancer SWI prolog "
					+ "\n- File > Consult... > Choisir le fichier Jeu.pl " + "\n- Taper la commande : 'server(5000).'");
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
			this.stage.setScene(scene);
			this.stage.show();

			// Si on a pas 2 IA on masque les boutons de simulation
			this.pauseSimulation();
			this.vueJeu.setSimulationMode((this.joueur1.getTypeJoueur() != TypeJoueur.JOUEUR_REEL)
					&& (this.joueur1.getTypeJoueur() != TypeJoueur.INCONNU)
					&& (this.joueur2.getTypeJoueur() != TypeJoueur.JOUEUR_REEL)
					&& (this.joueur2.getTypeJoueur() != TypeJoueur.INCONNU));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void simulerPartie() {
		if (this.threadSimulerPartie == null) {
			try {
				this.sem.acquire();

				this.threadSimulerPartie = new Thread() {
					@Override
					public void run() {

						while (Controleur.this.threadSimulerPartie.isInterrupted() == false) {
							try {
								final Coup coup = Controleur.this.getCoupIA();
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										try {
											Controleur.this.sem.acquire();
											Controleur.this.jouerCoup(coup.getPiecesBlanches(), coup.getPiecesNoires(),
													coup.getPiece(), coup.getDeplacement());
											if (Controleur.this.joueurCourant == Controleur.this.joueur1) {
												Controleur.this.joueurCourant = Controleur.this.joueur2;
											} else {
												Controleur.this.joueurCourant = Controleur.this.joueur1;
											}
											Controleur.this.sem.release();
										} catch (InterruptedException e) {

										}

									}
								});
								int dureeDeplacement = Controleur.this.calculDureeDeplacement(coup.getPiece(),
										coup.getDeplacement());
								int dureeCoup = Controleur.this.calculDureeCoup(dureeDeplacement);
								Thread.sleep(dureeCoup + (Controleur.this.dureeUnDeplacement / 2));
							} catch (InterruptedException e) {
								return;
							}
						}
					}
				};
				this.threadSimulerPartie.setDaemon(true);
				this.threadSimulerPartie.start();
				this.sem.release();
			} catch (InterruptedException e) {

			}
		}
	}

	public void playSimulation() {
		this.coefVitesse = 1;
		this.dureeUnDeplacement = this.DUREE_UN_DEPLACEMENT_NORMAL;
		this.pauseSimulation();
		this.vueJeu.setTextLabelVitesse("x" + this.coefVitesse);
		this.simulerPartie();
		this.vueJeu.setImageViewPlayDisable(true);
		// this.vueJeu.setImageViewFastForwardDisable(false);
		this.vueJeu.setImageViewPauseDisable(false);
	}

	public void fastForwardSimulation() {
		this.stopThreadSimulerPartie();

		switch (this.coefVitesse) {
		case 1:
			this.coefVitesse = 2;
			break;
		case 2:
			this.coefVitesse = 4;
			break;
		case 4:
			this.coefVitesse = 8;
			break;
		default:
			this.coefVitesse = 1;
			break;
		}

		this.dureeUnDeplacement = this.DUREE_UN_DEPLACEMENT_NORMAL / this.coefVitesse;
		this.vueJeu.setTextLabelVitesse("x" + this.coefVitesse);
		this.simulerPartie();
		this.vueJeu.setImageViewPlayDisable(false);
		// this.vueJeu.setImageViewFastForwardDisable(false);
		this.vueJeu.setImageViewPauseDisable(false);
	}

	public void pauseSimulation() {
		this.stopThreadSimulerPartie();
		this.coefVitesse = 1;
		this.vueJeu.setTextLabelVitesse("x0");
		this.vueJeu.setImageViewPlayDisable(false);
		// this.vueJeu.setImageViewFastForwardDisable(false);
		this.vueJeu.setImageViewPauseDisable(true);
	}

	private void stopThreadSimulerPartie() {
		if ((this.threadSimulerPartie != null) && this.threadSimulerPartie.isAlive()) {
			try {
				this.sem.acquire();
				this.threadSimulerPartie.interrupt();
				this.threadSimulerPartie = null;

				this.sem.release();
			} catch (InterruptedException e) {

			}
		}
	}

	/**
	 * 
	 * @param piece
	 *            : la piece a deplacer etant a sa position d'origine
	 * @param deplacement
	 *            : les cases constituant le trajet
	 * @return
	 */
	private int calculDureeDeplacement(Piece piece, List<Case> deplacement) {
		int coefDeplacement = Math.abs(deplacement.get(0).getColonne() - piece.getPosition().getColonne());

		for (int i = 1; i < deplacement.size(); i++) {
			coefDeplacement += Math.abs(deplacement.get(i - 1).getColonne() - deplacement.get(i).getColonne());
		}
		return (coefDeplacement * this.dureeUnDeplacement);
	}

	private int calculDureeCoup(int dureeDeplacement) {
		return dureeDeplacement + (this.dureeUnDeplacement / 2);
	}
}
