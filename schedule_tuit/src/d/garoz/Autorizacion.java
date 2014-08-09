package d.garoz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class Autorizacion {
	private String[] tokens;
	private String APIkey;
	private String APIsecret;

	public Autorizacion(String APIkey, String APIsecret) {
		tokens = new String[2];
		this.APIkey = APIkey;
		this.APIsecret = APIsecret;
	}

	public Twitter getTuitterTokenLess() {
		ConfigurationBuilder configBuilder = new ConfigurationBuilder();
		System.out.println(APIkey);
		System.out.println(APIsecret);
		configBuilder.setDebugEnabled(true).setOAuthConsumerKey(APIkey)
				.setOAuthConsumerSecret(APIsecret);
		return new TwitterFactory(configBuilder.build()).getInstance();
	}

	private String LeePin(RequestToken requestToken) throws IOException {
		String url = null;
		url = requestToken.getAuthorizationURL();
		BufferedReader lectorTeclado = new BufferedReader(
				new InputStreamReader(System.in));
		// Abro el navegador. Firefox, en este caso.
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("C:/Program Files (x86)/Mozilla Firefox/firefox.exe "
					+ url);
		} catch (Exception e) {
		}
		// Nos avisa de que introduciremos el PIN a continuación
		System.out
				.print("Introduce el PIN del navegador y pulsa intro.nn PIN: ");
		// Leemos el PIN
		return lectorTeclado.readLine();
	}

	public String[] getTokens() throws IOException {
		Twitter OAuthTwitter = getTuitterTokenLess();

		RequestToken requestToken = null;
		AccessToken accessToken = null;
		do {

			try {
				requestToken = OAuthTwitter.getOAuthRequestToken();
				String pin = LeePin(requestToken);
				if (pin.length() > 0) {
					accessToken = OAuthTwitter.getOAuthAccessToken(
							requestToken, pin);
				}
				tokens[0] = accessToken.getToken();
				tokens[1] = accessToken.getTokenSecret();

			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (accessToken == null);
		return tokens;
	}

	public void guardaTokens() {
		FileOutputStream fileOS = null;
		File file;
		String content = tokens[0] + "\n" + tokens[1];
		try {
			file = new File("auth_file.txt");
			fileOS = new FileOutputStream(file);
			// Si el archivo no existe, se crea
			if (!file.exists()) {
				file.createNewFile();
			}
			// Se obtiene el contenido en Bytes
			byte[] contentInBytes = content.getBytes();
			fileOS.write(contentInBytes);
			fileOS.flush();
			fileOS.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOS != null) {
					fileOS.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
