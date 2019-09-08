import org.newdawn.slick.SlickException;

/** ShotSpeed class, child of PowerUp class
 *  sets shooting speed of player, after collecting powerup
 */
public class ShotSpeed extends PowerUp{
	
	private static boolean guard = false;
	private static int time = 0;
	private static final int OFFSET = 200;
	
	/** constructor 
	 * @param x & y - starting coordinate of sprite */ 
	public ShotSpeed(float x, float y) throws SlickException {
		super("res/shotspeed-powerup.png", x, y);
	}

	
	/** time limiter 
	 * @param isOn  - boolean of whether shield is activated 
	 * @param delta - time passed since last frame (milliseconds).
	 * @return returns 0 when using normal shooting rate,
	 * returns offset for reduced intervals (faster shots)*/
	public static int shotSpeed_OffSet(boolean isOn, int delta) {
		
		// reset activator, and start timer
    	if(isOn == true) {
    		Player.setPowerShoot(false);
    		guard = true;
    		time = 0;
    	}
		// count time (mode activated)
    	time += delta;
    	
    	// reduce time interval of shooting rate
    	if(guard == true && time < PowerUp.getMAXTIME()) {
    		return OFFSET;
    	}
    	// turn off mode
    	else {
    		guard = false;
    		return 0;
    	}
	}
}
