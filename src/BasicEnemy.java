import org.newdawn.slick.SlickException;

import utilities.BoundingBox;


/** BasicEnemy class, child of Enemy
 *  an enemy type with more specific methods and attributes from Enemy class
 */
public class BasicEnemy extends Enemy {

	// basic info
	private int time=0;
	private BoundingBox box;
	private final float SPEED = 0.2f;
	
	private static final int POINTS = 50;

	/** constructor 
	 * @param x - initial starting x coordinate
	 * @param d - delay wait time*/
	public BasicEnemy(float x, int d) throws SlickException {
		
		super("res/basic-enemy.png", x, d, POINTS);
	}
	
	/** enemy's movement
	 * @param delta - time passed since last frame (milliseconds)*/
	@Override
	public void update(int delta) {
			
		time += delta;
		if(time >= getDelay()) {
			
			// initial starting point, prevent lasers from killing them
			if(getGuard()) {
				setIsPresent(true);
				setGuard(false);
			}
			setMovement(delta * SPEED);
			setSpriteY(getSpriteY() + getMovement());
			
			// update enemy hit box, and xy coordinates
			box = new BoundingBox(getImage(), getSpriteX(), getSpriteY());
			setBox(box);
					
			// when enemy is out of bound
			if(getSpriteY() > App.getHeight()) {
				setIsPresent(false);
				setLives(0);
			}
		}
	}
	
	/** indicate can't shoot 
	 * @param t - time interval between shots
	 * @return false indicates it can't shoot */
	@Override 
	public boolean timeToShoot(float t) {
		return false;
	}
}
