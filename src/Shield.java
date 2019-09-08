import org.newdawn.slick.SlickException;

/** Shield class, child of PowerUp class
 *  sets shield's condition of player, after collecting powerup
 */
public class Shield extends PowerUp {

	private static int time = 0;
	private static final int OFFSET = 2000;
	
	/** constructor
	 * @param x & y - starting coordinate of sprite */
	public Shield(float x, float y) throws SlickException {
		super("res/shield-powerup.png", x, y);
	}
	
	/** time limiter 
	 * @param isOn  - boolean of whether shield is activated 
	 * @param delta - time passed since last frame (milliseconds)
	 * @return returns 0 when no shield is up,
	 * returns offset for shield on time*/
	public static int shield_OffSet(boolean isOn, int delta) {
		
		// stops activator, and resets timer
		if(isOn == true) {
			Player.setPowerShield(false);
			time = 0;
		}
		// count time (mode activated)
		time += delta;

		// offset for extra shield time
		if(time < PowerUp.getMAXTIME()) {
			return OFFSET;
		}
		// turn off mode
		else {
			return 0;
		}
	}
	
}
