import java.util.Random;

import org.newdawn.slick.SlickException;

import utilities.BoundingBox;

/** PowerUp class, child of Sprite
 *  contains powerup drops for player to collect
 *  changes player's ability 
 */
public class PowerUp extends Sprite{

	// basic info
	private BoundingBox box;
	private float movement = 0;
	private static final float SPEED = 0.1f;
	private static final int MAXTIME = 5000 ;  

	// random generator
	private static int num;
	private static final int MAX    = 100;
	private static final int CHANCE = 5 ;
	private static final int TOOBAD = -1;
	
	/* getter */
	/** @return maximun powerup time */
	public static int getMAXTIME() {
		return MAXTIME;
	}
	
	
	/** constructor 
	 * @param imageSrc - image for sprite
	 * @param x & y - starting coordinate for laser*/
	public PowerUp(String imageSrc, float x, float y) throws SlickException {
		super(imageSrc, x, y);
	}
	
	
	/** update movement 
	 * @param delta - time passed since last frame (milliseconds).*/
	public void update(int delta) {
		
		movement = delta * SPEED;
		this.setSpriteY(this.getSpriteY() + movement);
		
		// update power up hit box
		box = new BoundingBox(this.getSpriteImg(), this.getSpriteX(), this.getSpriteY()) ;
		setBox(box);
		
		// when power up is out of bound
		if(this.getSpriteY() > App.getHeight()) {
			this.setIsPresent(false);
		}
	}
	
	
	/** random drop method 
	 * @return integers 0 or 1, suggesting power up type
	 * otherwise returns -1, for no power ups*/
	public static int toDropOrNotToDrop() {
		// generate random position
		Random rand = new Random();
		num = rand.nextInt(MAX);
		
		// in the 5%
		if(num<CHANCE) {
			// choose among the 2 choices
			// 0-> shield	1-> shoot speed
			num = rand.nextInt(2);
			return num;
		}
		// too bad
		else {
			return TOOBAD;
		}
	}
}
