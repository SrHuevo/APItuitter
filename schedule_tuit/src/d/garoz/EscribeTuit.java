package d.garoz;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class EscribeTuit implements Runnable{

	private Twitter twitter;
	private String tuit;
	private Long time;
	
	public EscribeTuit(Twitter twitter, String tuit, long time){
		this.twitter = twitter;
		this.tuit = tuit;
		this.time = time;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(time);
			twitter.updateStatus(tuit);
			System.out.println(tuit);
			System.err.println("ha sido publicado");
		} catch (TwitterException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
