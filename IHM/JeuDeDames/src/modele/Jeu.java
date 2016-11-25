package modele;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Jeu {
	private String nameServer = "localhost";
	private String portServer = "5000";
	private static final String urlBlancs = "blancs";
	private static final String urlNoirs = "noirs";
	private static final String urlPions = "pions";
	private static final String urlPosition = "positions";
	
	public static void main(String[] args) {
		Jeu jeu = new Jeu("localhost", "5000");
		jeu.getBlancs();
	}
	
	public Jeu(String nameServer, String portServer) {
		this.setNameServer(nameServer);
		this.portServer = portServer;
	}
	
	public void getBlancs() {
		JsonObject root = getJsonAtUrl("http://" + nameServer + ":" + portServer + "/" + urlBlancs);
	}
	
	public void getNoirs() {
		JsonObject root = getJsonAtUrl("http://" + nameServer + ":" + portServer + "/" + urlNoirs);
	}
	
	public void getPions() {
		JsonObject root = getJsonAtUrl("http://" + nameServer + ":" + portServer + "/" + urlPions);
	}
	
	public void getPositions() {
		JsonObject root = getJsonAtUrl("http://" + nameServer + ":" + portServer + "/" + urlPosition);
	}
	
	private JsonObject getJsonAtUrl(String sURL) {
		try {
		    URL url = new URL(sURL);
		    HttpURLConnection request;
			request = (HttpURLConnection) url.openConnection();
			request.connect();
		    JsonParser jp = new JsonParser();
		    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		    JsonObject rootobj = root.getAsJsonObject(); 
		    System.out.println(rootobj);
		    //zipcode = rootobj.get("zip_code").getAsString(); //just grab the zipcode
		    return rootobj;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
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

	public static String getUrlBlancs() {
		return urlBlancs;
	}

	public static String getUrlNoirs() {
		return urlNoirs;
	}

	public static String getUrlPosition() {
		return urlPosition;
	}
	
	public static String getUrlPions() {
		return urlPions;
	}

}
