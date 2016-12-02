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
	private static final String urlPlay = "play";
	private static final String urlMovesAllowed = "moves_allowed";

	public static void main(String[] args) {
		Jeu jeu = new Jeu("localhost", "5000");
		ArrayList<Piece> blancs = new ArrayList<>();
		ArrayList<Piece> noirs = new ArrayList<>();
		Integer j1 = jeu.init(blancs, noirs);
		System.out.println(j1);
		System.out.println(blancs);
		System.out.println(noirs);
		jeu.play(1, blancs, noirs);
		Map<Piece, ArrayList<Coup>> res = jeu.movesAllowed(1, blancs, noirs);
		System.out.println(res);
	}

	public Jeu(String nameServer, String portServer) {
		this.setNameServer(nameServer);
		this.portServer = portServer;
	}

	/*
	 * 
	 * { "joueur":0, "blancs": [ {"x":1, "y":2, "type":"dame"} ], "noirs": [
	 * {"x":1, "y":3, "type":"pion"} ] }
	 */
	public int init(ArrayList<Piece> blancs, ArrayList<Piece> noirs) {
		try {
			URL url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlInit);
			HttpURLConnection request;
			request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
			System.out.println(root);
			int j = -1;
			if (root.get("joueur") != null) {
				j = root.get("joueur").getAsInt();
			}
			if ((root.get("blancs") != null) && (blancs != null)) {
				JsonArray blancsArray = root.get("blancs").getAsJsonArray();
				for (int i = 0; i < blancsArray.size(); i++) {
					JsonObject elt = blancsArray.get(i).getAsJsonObject();
					int x = 0, y = 0;
					if (elt.get("x") != null) {
						x = elt.get("x").getAsInt();
					}
					if (elt.get("y") != null) {
						y = elt.get("y").getAsInt();
					}
					if (elt.get("type").getAsString().equals("pion")) {
						Pion p = new Pion(Couleur.BLANC, new Case(Couleur.NOIR, y, x));
						blancs.add(p);
					} else if (elt.get("type").getAsString().equals("dame")) {
						Dame d = new Dame(Couleur.BLANC, new Case(Couleur.NOIR, y, x));
						blancs.add(d);
					}
				}
			}
			if ((root.get("noirs") != null) && (noirs != null)) {
				JsonArray noirsArray = root.get("noirs").getAsJsonArray();
				for (int i = 0; i < noirsArray.size(); i++) {
					JsonObject elt = noirsArray.get(i).getAsJsonObject();
					int x = 0, y = 0;
					if (elt.get("x") != null) {
						x = elt.get("x").getAsInt();
					}
					if (elt.get("y") != null) {
						y = elt.get("y").getAsInt();
					}
					if (elt.get("type").getAsString().equals("pion")) {
						Pion p = new Pion(Couleur.NOIR, new Case(Couleur.NOIR, y, x));
						noirs.add(p);
					} else if (elt.get("type").getAsString().equals("dame")) {
						Dame d = new Dame(Couleur.NOIR, new Case(Couleur.NOIR, y, x));
						noirs.add(d);
					}
				}
			}
			request.disconnect();
			return j;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public Coup play(int joueur, List<Piece> blancs, List<Piece> noirs) {
		try {
			JsonArray blancsArray = this.buildObjectListPiece(blancs, "blancs");
			JsonArray noirsArray = this.buildObjectListPiece(noirs, "noirs");
			JsonObject parameters = new JsonObject();
			parameters.addProperty("joueur", joueur);
			parameters.add("blancs", blancsArray);
			parameters.add("noirs", noirsArray);
			URL url = new URL("http://" + this.nameServer + ":" + this.portServer + "/" + urlPlay);
			HttpURLConnection request;
			request = (HttpURLConnection) url.openConnection();
			request.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			request.setRequestMethod("POST");
			request.setDoOutput(true);
			request.setDoInput(true);
			OutputStream os = request.getOutputStream();
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
			ArrayList<Piece> newBlancs = new ArrayList<>();

			if ((root.get("blancs") != null)) {
				blancsArray = root.get("blancs").getAsJsonArray();
				for (int i = 0; i < blancsArray.size(); i++) {
					JsonObject elt = blancsArray.get(i).getAsJsonObject();
					int x = 0, y = 0;
					if (elt.get("x") != null) {
						x = elt.get("x").getAsInt();
					}
					if (elt.get("y") != null) {
						y = elt.get("y").getAsInt();
					}
					if (elt.get("type").getAsString().equals("pion")) {
						Pion p = new Pion(Couleur.BLANC, new Case(Couleur.NOIR, y, x));
						newBlancs.add(p);
					} else if (elt.get("type").getAsString().equals("dame")) {
						Dame d = new Dame(Couleur.BLANC, new Case(Couleur.NOIR, y, x));
						newBlancs.add(d);
					}
				}
			}
			ArrayList<Piece> newNoirs = new ArrayList<>();
			if ((root.get("noirs") != null)) {
				noirsArray = root.get("noirs").getAsJsonArray();
				for (int i = 0; i < noirsArray.size(); i++) {
					JsonObject elt = noirsArray.get(i).getAsJsonObject();
					int x = 0, y = 0;
					if (elt.get("x") != null) {
						x = elt.get("x").getAsInt();
					}
					if (elt.get("y") != null) {
						y = elt.get("y").getAsInt();
					}
					if (elt.get("type").getAsString().equals("pion")) {
						Pion p = new Pion(Couleur.NOIR, new Case(Couleur.NOIR, y, x));
						newNoirs.add(p);
					} else if (elt.get("type").getAsString().equals("dame")) {
						Dame d = new Dame(Couleur.NOIR, new Case(Couleur.NOIR, y, x));
						newNoirs.add(d);
					}
				}
			}
			ArrayList<Case> deplacements = new ArrayList<>();
			if ((root.get("mouvements") != null)) {
				JsonArray mouvementsArray = root.get("mouvements").getAsJsonArray();
				for (int i = 0; i < mouvementsArray.size(); i++) {
					JsonObject elt = mouvementsArray.get(i).getAsJsonObject();
					int x = 0, y = 0;
					if (elt.get("x") != null) {
						x = elt.get("x").getAsInt();
					}
					if (elt.get("y") != null) {
						y = elt.get("y").getAsInt();
					}
					deplacements.add(new Case(Couleur.NOIR, y, x));
				}
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

	public Map<Piece,ArrayList<Coup> > movesAllowed(int joueur, List<Piece> blancs, List<Piece> noirs) {
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
			System.out.println(parameters.toString());
			JsonParser jp = new JsonParser();
			JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
			System.out.println(root);
			request.disconnect();
			Map<Piece,ArrayList<Coup> > mapPossibilites = new HashMap<Piece, ArrayList<Coup>>();
			if ((root.get("move") != null)) {
				JsonArray possibilites = root.get("move").getAsJsonArray();
				for (int i = 0; i < possibilites.size(); i++) {
					JsonObject possibilite = possibilites.get(i).getAsJsonObject();
					Piece p = null;
					if(possibilite.get("pion") != null) {
						JsonObject elt = possibilite.get("pion").getAsJsonObject();
						//Gestion du pion
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
					if(possibilite.get("posibilite") != null) {
						JsonArray possi = possibilite.get("posibilite").getAsJsonArray();
						ArrayList<Coup> listCoups = new ArrayList<Coup>();
						for (int j = 0;j < possi.size(); j++) {
							ArrayList<Piece> newBlancs = new ArrayList<>();
							if ((root.get("blancs") != null)) {
								blancsArray = root.get("blancs").getAsJsonArray();
								for (int z = 0; z < blancsArray.size(); z++) {
									JsonObject elt = blancsArray.get(z).getAsJsonObject();
									int x = 0, y = 0;
									if (elt.get("x") != null) {
										x = elt.get("x").getAsInt();
									}
									if (elt.get("y") != null) {
										y = elt.get("y").getAsInt();
									}
									if (elt.get("type").getAsString().equals("pion")) {
										Pion pion = new Pion(Couleur.BLANC, new Case(Couleur.NOIR, y, x));
										newBlancs.add(pion);
									} else if (elt.get("type").getAsString().equals("dame")) {
										Dame d = new Dame(Couleur.BLANC, new Case(Couleur.NOIR, y, x));
										newBlancs.add(d);
									}
								}
							}
							ArrayList<Piece> newNoirs = new ArrayList<>();
							if ((root.get("noirs") != null)) {
								noirsArray = root.get("noirs").getAsJsonArray();
								for (int z = 0; z < noirsArray.size(); z++) {
									JsonObject elt = noirsArray.get(z).getAsJsonObject();
									int x = 0, y = 0;
									if (elt.get("x") != null) {
										x = elt.get("x").getAsInt();
									}
									if (elt.get("y") != null) {
										y = elt.get("y").getAsInt();
									}
									if (elt.get("type").getAsString().equals("pion")) {
										Pion pion = new Pion(Couleur.NOIR, new Case(Couleur.NOIR, y, x));
										newNoirs.add(pion);
									} else if (elt.get("type").getAsString().equals("dame")) {
										Dame d = new Dame(Couleur.NOIR, new Case(Couleur.NOIR, y, x));
										newNoirs.add(d);
									}
								}
							}
							ArrayList<Case> deplacements = new ArrayList<>();
							if ((root.get("mouvements") != null)) {
								JsonArray mouvementsArray = root.get("mouvements").getAsJsonArray();
								for (int z = 0; z < mouvementsArray.size(); z++) {
									JsonObject elt = mouvementsArray.get(z).getAsJsonObject();
									int x = 0, y = 0;
									if (elt.get("x") != null) {
										x = elt.get("x").getAsInt();
									}
									if (elt.get("y") != null) {
										y = elt.get("y").getAsInt();
									}
									deplacements.add(new Case(Couleur.NOIR, y, x));
								}
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
