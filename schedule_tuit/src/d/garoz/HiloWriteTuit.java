package d.garoz;

public class HiloWriteTuit implements Runnable{
	private String tuit;
	private Long time;
	
	public HiloWriteTuit(String tuit, long time){
		this.tuit = tuit;
		this.time = time;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(time);
			TwitterMain twitter = new TwitterMain();
			twitter.construirTwitter();
			twitter.escribeTuit(tuit);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
