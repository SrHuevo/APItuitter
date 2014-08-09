package d.garoz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterMain {

	private Twitter twitter;
	private ConfigurationBuilder configBuilder = new ConfigurationBuilder();
	private ThreadPoolExecutor executor;

	public void construirTwitter() {
		executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10000);
		String[] tokens = new String[2]; // token[0] = Access token; token[1] =
											// Access token secret

		tokens = leerAuth();
		if (tokens == null) {
			Autorizacion auth = new Autorizacion();
			try {
				tokens = auth.getTokens();
				auth.guardaTokens();
			} catch (TwitterException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		configBuilder
				.setDebugEnabled(true)
				.setOAuthConsumerKey("zrKVQDdDwFlkLZiKTCcJnynXZ")
				.setOAuthConsumerSecret(
						"fUpi89OWThIIfNap2zsMq3TvMZZ2MNj9qlEaTjRXyieUllRLn5")
				.setOAuthAccessToken(tokens[0])
				.setOAuthAccessTokenSecret(tokens[1]);
		twitter = new TwitterFactory(configBuilder.build()).getInstance();
	}

	public String[] leerAuth() {

		File archivo = null;
		FileReader fileR = null;
		BufferedReader lecturaTeclado = null;
		String[] tokens = new String[2];

		try {
			// Apertura del fichero y creacion de BufferedReader
			archivo = new File("auth_file.txt");
			fileR = new FileReader(archivo);
			lecturaTeclado = new BufferedReader(fileR);
			// Lectura del fichero
			String linea = new String();
			if ((linea = lecturaTeclado.readLine()) != null) { //token
				tokens[0] = linea;
			}
			if ((linea = lecturaTeclado.readLine()) != null) { // token secreto
				tokens[1] = linea;
			}
		} catch (Exception e) {
			e.printStackTrace();
			tokens = null;
		}
		try {
			if (null != fileR) {
				fileR.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return tokens;
	}

	// // Recuperar listado de ultimos tweets escritos
	// Paging pagina = new Paging();
	// pagina.setCount(50);
	// ResponseList listado = twitter.getHomeTimeline(pagina);
	// for (int i = 0; i < listado.size(); i++) {
	// System.out.println(((Status) listado.get(i)).getText());
	// }
	//

	public void programaTuit(String sdate, String tuit) {
		Date date = FactoryDate(sdate);
		long timeSet = date.getTime() - (new Date()).getTime();
		System.out.println("se publicara en: " + timeSet + "milisegundos");
		EscribeTuit escribeTuit = new EscribeTuit(twitter, tuit, timeSet);
		executor.execute(escribeTuit);
	}
	
	private Date FactoryDate(String sdate){
		Calendar cal = Calendar.getInstance();
		String[] splitDate = sdate.split(" ");
		String[] splitFecha = splitDate[0].split("/");
		String[] splitTime = splitDate[1].split(":");
		
		int year = Integer.valueOf("20" + splitFecha[2]);
		int month = Integer.valueOf(splitFecha[1]);
		int date = Integer.valueOf(splitFecha[0]);

		int hour = Integer.valueOf(splitTime[0]);
		int min = Integer.valueOf(splitTime[1]); 
		int sec;
		if(splitTime.length > 2)
			sec = Integer.valueOf(splitTime[2]); 
		else
			sec = 0;
		
		cal.set(year, month-1, date, hour, min, sec);
		
		System.out.println(cal.getTime());
		
		return cal.getTime();
	}

	public static void main(String ar[]) throws TwitterException {
		BufferedReader lectorTeclado = new BufferedReader(
				new InputStreamReader(System.in));
		String date;
		String tuit;
		for (;;) {
			try {
				TwitterMain twitter = new TwitterMain();
				System.out
						.println("Escribe cuando quieres que se publique el tuit con el formato dd/mm/yy hh:mm:ss");
				date = lectorTeclado.readLine();
				System.out.println("Escribe el tuit");
				tuit = lectorTeclado.readLine();
				twitter.construirTwitter();
				twitter.programaTuit(date, tuit);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}