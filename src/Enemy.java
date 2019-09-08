import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;

/** Enemy abstract class child of Sprite
 *  contains basic details of enemies
 *  and all the abilities they can do (will be overwritten for specific enemies)
 */
public abstract class Enemy extends Sprite {
	
	// basic enemy info
	private Image enemy;
	private boolean toShoot = false;
	private boolean guard   = true;
	private int  lives;
	private int points;
	private int  delay;
	private float movement;
	
	private static final int INITIAL_START = -64;
	private static final int DEFAULT_LIVE  = 1;
	
	/* getters */
	/** @return points of enemy */
	public int getPoints() {
		return new Integer(points);
	}
	/** @return lives of enemy */
	public int getLives() {
		return new Integer(lives);
	}
	/** @return image of enemy */
	public Image getImage() {
		return enemy;
	}
	/** @return true to shoot, false to not*/
	public boolean getToShoot() {
		return new Boolean(toShoot);
	}
	/** @return guard controlling mode changes */
	public boolean getGuard() {
		return new Boolean(guard);
	}
	/** @return movement of enemy */
	public float getMovement() {
		return new Float(movement);
	}
	/** @return delay time of enemy */
	public int getDelay() {
		return new Integer(delay);
	}
	
	/* setters */
	/**@param x - lifes */
	public void setLives(int x) {
		this.lives = x;
	}
	/**@param img - string of image name
	 * @throws SlickException */
	public void setImage(String img) throws SlickException {
		enemy = new Image(img);
	}
	/**@param x - set to shoot or not */
	public void setToShoot(boolean x) {
		this.toShoot = x;
	}
	/**@param x - set guard for controlling modes */
	public void setGuard(boolean x) {
		guard = x;
	}
	/**@param x - set enemy movement rate */
	public void setMovement(float x) {
		movement = x;
	}
	
	/** constructor
	 * @param imageSrc - Image for sprite
	 * @param x - starting x coordinate
	 * @param delay  - delay time for enemy
	 * @param points - points for each enemy */
	public Enemy(String imageSrc, float x, int delay, int points) throws SlickException {
		super(imageSrc, x, INITIAL_START);
		
		// every enemy's basic info
		this.lives = DEFAULT_LIVE;
		this.delay = delay;
		this.points= points;
		
		setIsPresent(false);
		setImage(imageSrc);
	}
	
	/** reduce enemy live number*/
	public void reduceLives() {
		this.lives -= 1;
	}
	
	/** enemy's movements
	 * @param delta - time passed since last frame (milliseconds) */
	public abstract void update(int delta);
	
	/** enemy's laser 
	 * @param t - time interval between shots
	 * @return indicate when to shoot (true), else don't shoot (false) */
	public abstract boolean timeToShoot(float t);
 
}
