import java.util.Random;

import org.newdawn.slick.SlickException;
import utilities.BoundingBox;

/** Boss class, child of Enemy
 *  an enemy type with more specific methods and attributes from Enemy class
 *  contains more lives so its harder to die, and can shoot lasers
 *  has very complicated moving abilities
 */
public class Boss extends Enemy {
	
	//move
	private static final float SPEED = 0.05f;
	private static final float HORIZONTAL_SPEED = 0.2f;
	private static final float HORIZONTAL_SHOOT_SPEED = 0.1f;
	
	// basic info
	private float oriX = this.getSpriteX();
	private BoundingBox box;
	private int time = 0;
	private static final int MAXLIVES = 60;
	private static final int DESTINATION = 72;
 	private static final int POINTS = 5000;
	
	// waiting, delay and shoot
	private int    wait;
	private float speed;
	private int shootTime = 0;
	private float endTime = 0;
	private static final int MAX_SHOOT_TIME = 3000;
	private static final int SHOOT_INTERVAL = 200; 
	
	// wait modes
	private boolean mode_1 = true;
	private boolean mode_2 = false;
	private static final int WAIT_1 = 5000;
	private static final int WAIT_2 = 2000;
	
	// random destinations
	private float destination;
	private static final int MAX = 896;
	private static final int MIN = 128;

	
	/** constructor 
	 * @param x - initial starting x coordinate
	 * @param d - delay wait time*/
	public Boss(float x,int d) throws SlickException {
		
		super("res/boss.png", x, d, POINTS);
		setLives(MAXLIVES);
	}

	/** enemy's movement
	 * @param delta - time passed since last frame (milliseconds)*/
	@Override
	public void update(int delta) {
		
		time += delta;	
		
		/* moving y axis */
		if(time >= getDelay()) {
		
			// initial starting point, prevent lasers from killing boss
			if(getGuard()) {
				setGuard(false);
				setIsPresent(true);
				
				wait  = WAIT_1;
			}
			// moving downwards
			if (getSpriteY() < DESTINATION) {
				setMovement(delta * SPEED);
				move('y', 1);
			}
			// reset timer, initial wait (5000ms)
			else {
				time = 0;
				setGuard(true);
			}
		}
		
		/* enable shooting (for 3000 msec) */
		if(getToShoot() == true) {
			shootTime += delta;
			
			if(shootTime >= MAX_SHOOT_TIME) {
				setToShoot(false);
				shootTime = 0;
			}
		}
		
		/* moving x axis */
		if(getSpriteY() >= DESTINATION && time >= wait){
			
			// new movement speed
			this.setMovement(delta * speed);
			
			// create new random x point, reset necessary variables
			// mark its original x coordinate
			if(getGuard()) {
				setGuard(false);
				
				Random rand = new Random();
				destination = rand.nextInt(MAX-MIN+1) + MIN;
				oriX = getSpriteX();
				
				// setting different modes after getting to destination
				changeMode();
				
				// prevent stuttering
				return;
			}
			
			/* turn left */
			if (oriX >= destination) {				
				
				// moving to x coordinates on the left
				if (getSpriteX() >= destination) {
					move('x', -1);
				}	
				// upon reaching
				else {
					resetTime();
				}
			}
			/* turn right */
			else if (oriX <= destination) {		
				
				// moving to x coordinate on the right
				if (getSpriteX() <= destination) {
					move('x', 1);
				}
				// upon reaching
				else {
					resetTime();
				}
			}
		}
		
	}
	
	/** start the shooting spree 
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

	/** change mode (wait time) 
	 * @param x -  old wait time
	 * @return new wait times*/
	private int changeWait(int x) {
		
		if(x == WAIT_1) {
			return WAIT_2;
		}
		else{
			return WAIT_1;
		}
	}
	
	/** changes between modes
	 *  shooting and not shooting */
	private void changeMode() {
		
		/* setting different modes after getting to destination */
		if(mode_1) {
			speed = HORIZONTAL_SPEED;
			
			mode_1 = false;
			mode_2 = true;
		}
		else if(mode_2) {
			speed = HORIZONTAL_SHOOT_SPEED;
			setToShoot(true);
			
			mode_2 = false;
			mode_1 = true;
		}
	}
	
	/** boss's movement, axis and direction
	 * @param axis - axis type of boss
	 * @param sign - direction of movement (L or R) */
	private void move(char axis, int sign) {
		
		if(axis == 'x') {
			setSpriteX(getSpriteX() + sign * getMovement());
		}
		else if(axis == 'y') {
			setSpriteY(getSpriteY() + sign * getMovement());
		}
		// update enemy hit box, and xy coordinates
		box = new BoundingBox(getImage(), getSpriteX(), getSpriteY());
		setBox(box);
	}
	
	/** wait after reaching new x coordinate, resets times and enables changeMode */
	private void resetTime() {
		
		// get new original x coordinates and reset timer
		oriX = getSpriteX();
		wait = changeWait(wait);
		time = 0;
		
		// guard for changing mode
		setGuard(true);
	}
}
