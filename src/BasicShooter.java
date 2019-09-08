import org.newdawn.slick.SlickException;
import java.util.Random;

import utilities.BoundingBox;

/** BasicShooter class, child of Enemy
 *  an enemy type with more specific methods and attributes from Enemy class
 *  can shoot lasers
 */
public class BasicShooter extends Enemy{
	
	// basic sprite info
	private static final float SPEED = 0.2f;
	private BoundingBox box;
	private int time=0;
	
	// random destination
	private int destination;
	private static final int MAX = 464;
	private static final int MIN = 48;
	
	// lasers
	private float endTime   = 0;
	private static final float SHOOT_INTERVAL = 3500;
	private static final int POINTS = 200;

	
	/** constructor 
	 * @param x - initial starting x coordinate
	 * @param d - delay wait time*/
	public BasicShooter(float x, int d) throws SlickException {
		
		super("res/basic-shooter.png", x, d, POINTS);

		// generate random position
		Random rand = new Random();
		destination = rand.nextInt(MAX) + MIN;
	}

	/** enemy's movement
	 * @param delta - time passed since last frame (milliseconds)*/
	@Override
	public void update(int delta) {
		
		time += delta;
		if (time >= getDelay()) {
			
			// initial starting point, prevent lasers from killing them
			if (getGuard()) {
				setGuard(false);
				setIsPresent(true);
			}
			
			if (getSpriteY() < destination) {
				setMovement(delta * SPEED);
				setSpriteY(getSpriteY() + getMovement());
					
				// update enemy hit box, and xy coordinates
				box = new BoundingBox(getImage(), getSpriteX(), getSpriteY());
				setBox(box);
			}
			// time to shoot bois
			else {
				setToShoot(true);
			}
		}
	}
	
	/** start the shooting spree, with time management
	 * @param t - time interval between shots
	 * @return true when its time to shoot, false when its not*/
	@Override 
	public boolean timeToShoot(float t) {
		
		if(endTime == 0) {
			endTime = t + SHOOT_INTERVAL;
			return false;
		}
		// to shoot, and reset counter
		else if(t >= endTime) {
			endTime = 0;
			return true;
		}
		else {
			return false;
		}
	}

}