package modele;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Jeu {
	private String nameServer = "localhost";
	private String portServer = "5000";
	private static final String urlInit = "init";
	private static final String urlPlayZack = "play_zack";
	private static final String urlPlayPenny = "play_penny";
	private static final String urlPlayHoward = "play_howard";
	private static final String urlPlayRaj = "play_raj";
	private static final String urlPlayLeonard = "play_leonard";
	private static final String urlPlayAmy = "play_amy";
	private static final String urlPlaySheldon = "play_sheldon";
	private static final String urlMovesAllowed = "moves_allowed";
	private static final String urlGameState = "game_state";

	public static void main(String[] args) {
		Jeu jeu = new Jeu("localhost", "5000");
		jeu.stats();
		/*
		 * ArrayList<Piece> blancs = new ArrayList<>(); ArrayList<Piece> noirs =
		 * new ArrayList<>(); Coup c = jeu.init(blancs, noirs); blancs =
		 * (ArrayList<Piece>) c.piecesBlanches; noirs = (ArrayList<Piece>)
		 * c.piecesNoires; blancs.add(new Dame(Couleur.BLANC, new
		 * Case(Couleur.NOIR, 0, 0))); jeu.play(TypeJoueur.IA_ZACK, 1, blancs,
		 * noirs); // Map<Piece, List<Coup>> res = jeu.movesAllowed(1, blancs,
		 * noirs); ArrayList<Piece> noirs2 = noirs; noirs2.remove(0); int r =
		 * jeu.gameState(blancs, noirs, blancs, noirs2);
		 * System.out.print("State : " + r);
		 */

	}

	public Jeu(String nameServer, String portServer) {
		this.setNameServer(nameServer);
		this.portServer = portServer;
	}

	public void stats() {
		System.out.println(
				"IA1;IA2;Nb de gagné;Nb d'égalité;Nb de défaite;Nb de coup moyen;Temps moyen d'une partie en s");
		int[][][] gameover = new int[50][7][7];
		int[][][] coup = new int[50][7][7];
		long[][][] tps = new long[50][7][7];
		int partieMax = 10;
		int nbIa = 7;
		for (int ia = 6; ia < nbIa; ia++) {
			for (int ia2 = 0; ia2 < nbIa; ia2++) {
				if ((ia == 0) || (ia2 == 0)) {
					partieMax = 10;
				} else {
					partieMax = 1;
				}
				for (int partie = 0; partie < partieMax; partie++) {
					gameover[partie][ia][ia2] = 0;
					coup[partie][ia][ia2] = 0;
					tps[partie][ia][ia2] = 0;
					ArrayList<Piece> blancs = new ArrayList<>();
					ArrayList<Piece> noirs = new ArrayList<>();
					Coup c = this.init(blancs, noirs);
					blancs = (ArrayList<Piece>) c.getPiecesBlanches();
					noirs = (ArrayList<Piece>) c.getPiecesNoires();
					long begin = java.lang.System.currentTimeMillis();
					int j = 0;
					int co = 0;
					do {
						if (j == 0) {
							c = this.play(this.getIAFromInt(ia), j, blancs, noirs);
							j = 1;
						} else {
							c = this.play(this.getIAFromInt(ia2), j, blancs, noirs);
							j = 0;
						}
						co++;
						blancs = (ArrayList<Piece>) c.getPiecesBlanches();
						noirs = (ArrayList<Piece>) c.getPiecesNoires();
					} while (c.getEtat() == 3);
					coup[partie][ia][ia2] = co;
					gameover[partie][ia][ia2] = c.getEtat();
					tps[partie][ia][ia2] = java.lang.System.currentTimeMillis() - begin;
				}
				int C = 0;
				long T = 0;
				int G = 0;
				int D = 0;
				int E = 0;
				for (int p = 0; p < partieMax; p++) {
					if (gameover[p][ia][ia2] == 0) {
						G++;
					} else if (gameover[p][ia][ia2] == 1) {
						D++;
					} else {
						E++;
					}
					C += coup[p][ia][ia2];
					T += tps[p][ia][ia2];
				}
				System.out.println(this.getIAFromInt(ia) + ";" + this.getIAFromInt(ia2) + ";" + G + ";" + E + ";" + D
						+ ";" + (C / partieMax) + ";" + ((T / partieMax) / 1000));
			}
		}
	}

	public TypeJoueur getIAFromInt(int ia) {
		if (ia == 0) {
			return TypeJoueur.IA_ZACK;
		} else if (ia == 1) {
			return TypeJoueur.IA_PENNY;
		} else if (ia == 2) {
			return TypeJoueur.IA_HOWARD;
		} else if (ia == 3) {
			return TypeJoueur.IA_RAJ;
		} else if (ia == 4) {
			return TypeJoueur.IA_LEONARD;
		} else if (ia == 5) {
			return TypeJoueur.IA_AMY;
		} else if (ia == 6) {
			return TypeJoueur.IA_SHELDON;
		}
		return TypeJoueur.INCONNU;
	}

	/*
	 * 
	 * { "joueur":0, "blancs": [ {"x":1, "y":2, "type":"dame"} ], "noirs": [
	 * {"x":1, "y":3, "type":"pion"} ] }
	 */
	public Coup init(ArrayList<Piece> blancs, ArrayList<Piece> noirs) {
		try {
			URL url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlInit);
			HttpURLConnection request;
			request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
			// System.out.println(root);
			int j = -1;
			if (root.get("joueur") != null) {
				j = root.get("joueur").getAsInt();
			}
			if ((root.get("blancs") != null) && (blancs != null)) {
				// System.out.println(root.get("blancs"));
				blancs = this.buildListPiece(root.get("blancs").getAsJsonArray(), Couleur.BLANC);
			}
			if ((root.get("noirs") != null) && (noirs != null)) {
				noirs = this.buildListPiece(root.get("noirs").getAsJsonArray(), Couleur.NOIR);
			}
			request.disconnect();
			return new Coup(j, blancs, noirs, null, null);
		} catch (IOException e) {
			// e.printStackTrace();
			return null;
		}
	}

	public Coup play(TypeJoueur ia, int joueur, List<Piece> blancs, List<Piece> noirs) {
		try {
			JsonArray blancsArray = this.buildObjectListPiece(blancs, "blancs");
			JsonArray noirsArray = this.buildObjectListPiece(noirs, "noirs");
			JsonObject parameters = new JsonObject();
			parameters.addProperty("joueur", joueur);
			parameters.add("blancs", blancsArray);
			parameters.add("noirs", noirsArray);
			URL url;

			if (ia == TypeJoueur.IA_ZACK) {
				url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlPlayZack);
			} else if (ia == TypeJoueur.IA_PENNY) {
				url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlPlayPenny);
			} else if (ia == TypeJoueur.IA_HOWARD) {
				url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlPlayHoward);
			} else if (ia == TypeJoueur.IA_RAJ) {
				url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlPlayRaj);
			} else if (ia == TypeJoueur.IA_LEONARD) {
				url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlPlayLeonard);
			} else if (ia == TypeJoueur.IA_AMY) {
				url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlPlayAmy);
			} else if (ia == TypeJoueur.IA_SHELDON) {
				url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlPlaySheldon);
			} else {
				url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlPlayZack);
			}

			HttpURLConnection request;
			request = (HttpURLConnection) url.openConnection();
			request.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			request.setRequestMethod("POST");
			request.setDoOutput(true);
			request.setDoInput(true);
			OutputStream os = request.getOutputStream();
			// System.out.println("parameters : " + parameters.toString());
			os.write(parameters.toString().getBytes("UTF8"));
			os.close();
			// System.out.println(parameters.toString());
			JsonParser jp = new JsonParser();
			JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
			// System.out.println(root);
			request.disconnect();
			int etat = -1;
			if ((root.get("etat") != null)) {
				etat = root.get("etat").getAsInt();
			}
			ArrayList<Piece> newBlancs = new ArrayList<>();
			if ((root.get("blancs") != null)) {
				newBlancs = this.buildListPiece(root.get("blancs").getAsJsonArray(), Couleur.BLANC);
			}
			ArrayList<Piece> newNoirs = new ArrayList<>();
			if ((root.get("noirs") != null)) {
				newNoirs = this.buildListPiece(root.get("noirs").getAsJsonArray(), Couleur.NOIR);
			}
			ArrayList<Case> deplacements = new ArrayList<>();
			if ((root.get("mouvements") != null)) {
				deplacements = this.buildListCase(root.get("mouvements").getAsJsonArray());
			}
			Piece piece = null;
			if ((root.get("pion") != null)) {
				JsonObject elt = root.get("pion").getAsJsonObject();
				int x = 0, y = 0;
				if (elt.get("x") != null) {
					x = elt.get("x").getAsInt();
				}
				if (elt.get("y") != null) {
					y = elt.get("y").getAsInt();
				}
				if (elt.get("type").getAsString().equals("pion")) {
					piece = new Pion(Couleur.NOIR, new Case(Couleur.NOIR, y, x));
				} else if (elt.get("type").getAsString().equals("dame")) {
					piece = new Dame(Couleur.NOIR, new Case(Couleur.NOIR, y, x));
				}
			}
			Coup coup = new Coup(etat, newBlancs, newNoirs, deplacements, piece);
			return coup;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<Piece, List<Coup>> movesAllowed(int joueur, List<Piece> blancs, List<Piece> noirs) {
		try {
			JsonArray blancsArray = this.buildObjectListPiece(blancs, "blancs");
			JsonArray noirsArray = this.buildObjectListPiece(noirs, "noirs");
			JsonObject parameters = new JsonObject();
			parameters.addProperty("joueur", joueur);
			parameters.add("blancs", blancsArray);
			parameters.add("noirs", noirsArray);
			URL url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlMovesAllowed);
			HttpURLConnection request;
			request = (HttpURLConnection) url.openConnection();
			request.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			request.setRequestMethod("POST");
			request.setDoOutput(true);
			request.setDoInput(true);
			OutputStream os = request.getOutputStream();
			os.write(parameters.toString().getBytes("UTF8"));
			os.close();
			// System.out.println(parameters.toString());
			JsonParser jp = new JsonParser();
			JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
			// System.out.println(root);
			request.disconnect();
			Map<Piece, List<Coup>> mapPossibilites = new HashMap<>();
			if ((root.get("move") != null)) {
				JsonArray possibilites = root.get("move").getAsJsonArray();
				// System.out.println("Nb possibilites de pion: " +
				// possibilites.size());
				for (int i = 0; i < possibilites.size(); i++) {
					JsonObject possibilite = possibilites.get(i).getAsJsonObject();
					Piece p = null;
					// Gestion du pion
					if (possibilite.get("pion") != null) {
						JsonObject elt = possibilite.get("pion").getAsJsonObject();
						int x = 0, y = 0;
						if (elt.get("x") != null) {
							x = elt.get("x").getAsInt();
						}
						if (elt.get("y") != null) {
							y = elt.get("y").getAsInt();
						}
						if (elt.get("type").getAsString().equals("pion")) {
							p = new Pion(Couleur.BLANC, new Case(Couleur.NOIR, y, x));
						} else if (elt.get("type").getAsString().equals("dame")) {
							p = new Dame(Couleur.BLANC, new Case(Couleur.NOIR, y, x));
						}
					}
					// Gestion des possibilite
					if (possibilite.get("posibilite") != null) {
						JsonArray possi = possibilite.get("posibilite").getAsJsonArray();
						ArrayList<Coup> listCoups = new ArrayList<>();
						// System.out.println("Nb possibilite pour ce pion : " +
						// possi.size());
						for (int j = 0; j < possi.size(); j++) {
							ArrayList<Piece> newBlancs = new ArrayList<>();
							if ((possi.get(j).getAsJsonObject().get("blancs") != null)) {
								newBlancs = this.buildListPiece(
										possi.get(j).getAsJsonObject().get("blancs").getAsJsonArray(), Couleur.BLANC);
							}
							ArrayList<Piece> newNoirs = new ArrayList<>();
							if ((possi.get(j).getAsJsonObject().get("noirs") != null)) {
								newNoirs = this.buildListPiece(
										possi.get(j).getAsJsonObject().get("noirs").getAsJsonArray(), Couleur.NOIR);
							}
							ArrayList<Case> deplacements = new ArrayList<>();
							if ((possi.get(j).getAsJsonObject().get("mouvements") != null)) {
								deplacements = this.buildListCase(
										possi.get(j).getAsJsonObject().get("mouvements").getAsJsonArray());
							}
							Coup c = new Coup(3, newBlancs, newNoirs, deplacements, p);
							listCoups.add(c);
						}
						mapPossibilites.put(p, listCoups);
					}
				}
			}
			return mapPossibilites;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int gameState(List<Piece> blancs, List<Piece> noirs, List<Piece> blancs2, List<Piece> noirs2) {
		try {
			JsonArray blancsArray = this.buildObjectListPiece(blancs, "blancs");
			JsonArray noirsArray = this.buildObjectListPiece(noirs, "noirs");
			JsonArray blancsArray2 = this.buildObjectListPiece(blancs2, "blancs2");
			JsonArray noirsArray2 = this.buildObjectListPiece(noirs2, "noirs2");
			JsonObject parameters = new JsonObject();
			parameters.add("blancs", blancsArray);
			parameters.add("noirs", noirsArray);
			parameters.add("blancs2", blancsArray2);
			parameters.add("noirs2", noirsArray2);
			URL url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlGameState);
			HttpURLConnection request;
			request = (HttpURLConnection) url.openConnection();
			request.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			request.setRequestMethod("POST");
			request.setDoOutput(true);
			request.setDoInput(true);
			OutputStream os = request.getOutputStream();
			System.out.println("parameters : " + parameters.toString());
			os.write(parameters.toString().getBytes("UTF8"));
			os.close();
			System.out.println(parameters.toString());
			JsonParser jp = new JsonParser();
			JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
			System.out.println(root);
			request.disconnect();
			int etat = -1;
			if ((root.get("etat") != null)) {
				etat = root.get("etat").getAsInt();
			}
			return etat;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private ArrayList<Case> buildListCase(JsonArray array) {
		ArrayList<Case> deplacements = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			JsonObject elt = array.get(i).getAsJsonObject();
			int x = 0, y = 0;
			if (elt.get("x") != null) {
				x = elt.get("x").getAsInt();
			}
			if (elt.get("y") != null) {
				y = elt.get("y").getAsInt();
			}
			deplacements.add(new Case(Couleur.NOIR, y, x));
		}
		return deplacements;
	}

	private ArrayList<Piece> buildListPiece(JsonArray array, Couleur couleur) {
		ArrayList<Piece> arrayPiece = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			JsonObject elt = array.get(i).getAsJsonObject();
			int x = 0, y = 0;
			if (elt.get("x") != null) {
				x = elt.get("x").getAsInt();
			}
			if (elt.get("y") != null) {
				y = elt.get("y").getAsInt();
			}
			if (elt.get("type").getAsString().equals("pion")) {
				Pion pion = new Pion(couleur, new Case(Couleur.NOIR, y, x));
				arrayPiece.add(pion);
			} else if (elt.get("type").getAsString().equals("dame")) {
				Dame d = new Dame(couleur, new Case(Couleur.NOIR, y, x));
				arrayPiece.add(d);
			}
		}
		return arrayPiece;
	}

	private JsonArray buildObjectListPiece(List<Piece> pieces, String key) {
		JsonArray array = new JsonArray();
		for (Piece p : pieces) {
			JsonObject objPiece = new JsonObject();
			int y = p.getPosition().getLigne();
			int x = p.getPosition().getColonne();
			objPiece.addProperty("x", x);
			objPiece.addProperty("y", y);
			String str = "pion";
			if (p.getClass().equals(Dame.class)) {
				str = "dame";
			}
			objPiece.addProperty("type", str);
			array.add(objPiece);
		}
		return array;
	}

	public String getNameServer() {
		return this.nameServer;
	}

	public void setNameServer(String nameServer) {
		this.nameServer = nameServer;
	}

	public String getPortServer() {
		return this.portServer;
	}

	public void setPortServer(String portServer) {
		this.portServer = portServer;
	}
}
