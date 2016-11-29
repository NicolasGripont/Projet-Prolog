package modele;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Jeu {
	private String nameServer = "localhost";
	private String portServer = "5000";
	private static final String urlInit = "init";
	private static final String urlPlay = "play";
	
	public static void main(String[] args) {
		Jeu jeu = new Jeu("localhost", "5000");
		ArrayList<Piece> blancs = new ArrayList<>();
		ArrayList<Piece> noirs = new ArrayList<>();
		Integer j1 = jeu.init(blancs, noirs);
		System.out.println(j1);
		System.out.println(blancs);
		System.out.println(noirs);
		jeu.play(1,blancs, noirs);
	}
	
	public Jeu(String nameServer, String portServer) {
		this.setNameServer(nameServer);
		this.portServer = portServer;
	}
	
	/*
	 * 
	{
	  "joueur":0,
	  "blancs": [ {"x":1, "y":2, "type":"dame"} ],
	  "noirs": [ {"x":1, "y":3, "type":"pion"} ]
	}
	 */
	public int init(ArrayList<Piece> blancs, ArrayList<Piece> noirs) {
		try {
		    URL url = new URL("http://" + nameServer + ":" + portServer + "/" + urlInit);
		    HttpURLConnection request;
			request = (HttpURLConnection) url.openConnection();
			request.connect();
		    JsonParser jp = new JsonParser();
		    JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject(); 
		    System.out.println(root);
		    int j = -1;
			if(root.get("joueur") != null) {
				j = root.get("joueur").getAsInt();
			}
			if(root.get("blancs") != null && blancs != null) {
				JsonArray blancsArray = root.get("blancs").getAsJsonArray();
				for(int i=0;i<blancsArray.size();i++) {
		    		JsonObject elt = blancsArray.get(i).getAsJsonObject();
		    		int x = 0,y = 0;
		    		if(elt.get("x") != null) {
		    			x = elt.get("x").getAsInt();
		    		}
		    		if(elt.get("y") != null) {
		    			y = elt.get("y").getAsInt();
		    		}
		    		if(elt.get("type").getAsString().equals("pion")) {
		    			Pion p = new Pion(Couleur.BLANC,new Case(Couleur.NOIR,x,y));
		    			blancs.add(p);
		    		} else if(elt.get("type").getAsString().equals("dame")){
		    			Dame d = new Dame(Couleur.BLANC,new Case(Couleur.NOIR,x,y));
		    			blancs.add(d);
		    		}
		    	}
			}
			if(root.get("noirs") != null && noirs != null) {
				JsonArray noirsArray = root.get("noirs").getAsJsonArray();
				for(int i=0;i<noirsArray.size();i++) {
		    		JsonObject elt = noirsArray.get(i).getAsJsonObject();
		    		int x = 0,y = 0;
		    		if(elt.get("x") != null) {
		    			x = elt.get("x").getAsInt();
		    		}
		    		if(elt.get("y") != null) {
		    			y = elt.get("y").getAsInt();
		    		}
		    		if(elt.get("type").getAsString().equals("pion")) {
		    			Pion p = new Pion(Couleur.NOIR,new Case(Couleur.NOIR,x,y));
		    			noirs.add(p);
		    		} else if(elt.get("type").getAsString().equals("dame")){
		    			Dame d = new Dame(Couleur.NOIR,new Case(Couleur.NOIR,x,y));
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
	
	public int play(int joueur, ArrayList<Piece> blancs, ArrayList<Piece> noirs) {
		try {
			String json = "{\"joueur\":0,\"blancs\": [ {\"x\":1, \"y\":2, \"type\":\"dame\"} ],\"noirs\": [ {\"x\":1, \"y\":3, \"type\":\"pion\"} ]}";
			URL url = new URL("http://" + nameServer + ":" + portServer + "/" + urlInit);
		    HttpURLConnection request;
			request = (HttpURLConnection) url.openConnection();
			request.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			request.setRequestMethod("POST");
			request.setDoOutput(true);
			request.setDoInput(true);
			OutputStream os = request.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
            InputStream in = new BufferedInputStream(request.getInputStream());
            JsonParser jp = new JsonParser();
            JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
            System.out.println(root);
            request.disconnect();
		    /*JsonParser jp = new JsonParser();
		    JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject(); 
		    System.out.println(root);
		    int j = -1;
			if(root.get("joueur") != null) {
				j = root.get("joueur").getAsInt();
			}
			if(root.get("blancs") != null && blancs != null) {
				JsonArray blancsArray = root.get("blancs").getAsJsonArray();
				for(int i=0;i<blancsArray.size();i++) {
		    		JsonObject elt = blancsArray.get(i).getAsJsonObject();
		    		int x = 0,y = 0;
		    		if(elt.get("x") != null) {
		    			x = elt.get("x").getAsInt();
		    		}
		    		if(elt.get("y") != null) {
		    			y = elt.get("y").getAsInt();
		    		}
		    		if(elt.get("type").getAsString().equals("pion")) {
		    			Pion p = new Pion(Couleur.BLANC,new Case(Couleur.NOIR,x,y));
		    			blancs.add(p);
		    		} else if(elt.get("type").getAsString().equals("dame")){
		    			Dame d = new Dame(Couleur.BLANC,new Case(Couleur.NOIR,x,y));
		    			blancs.add(d);
		    		}
		    	}
			}
			if(root.get("noirs") != null && noirs != null) {
				JsonArray noirsArray = root.get("noirs").getAsJsonArray();
				for(int i=0;i<noirsArray.size();i++) {
		    		JsonObject elt = noirsArray.get(i).getAsJsonObject();
		    		int x = 0,y = 0;
		    		if(elt.get("x") != null) {
		    			x = elt.get("x").getAsInt();
		    		}
		    		if(elt.get("y") != null) {
		    			y = elt.get("y").getAsInt();
		    		}
		    		if(elt.get("type").getAsString().equals("pion")) {
		    			Pion p = new Pion(Couleur.NOIR,new Case(Couleur.NOIR,x,y));
		    			noirs.add(p);
		    		} else if(elt.get("type").getAsString().equals("dame")){
		    			Dame d = new Dame(Couleur.NOIR,new Case(Couleur.NOIR,x,y));
		    			noirs.add(d);
		    		}
		    	}
			}*/
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public String getNameServer() {
		return nameServer;
	}

	public void setNameServer(String nameServer) {
		this.nameServer = nameServer;
	}

	public String getPortServer() {
		return portServer;
	}

	public void setPortServer(String portServer) {
		this.portServer = portServer;
	}
}
