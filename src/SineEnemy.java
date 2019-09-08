import org.newdawn.slick.SlickException;

import utilities.BoundingBox;

/** SineEnemy class, child of Enemy
 *  an enemy type with more specific methods and attributes from Enemy class
 *  enemy type with sine mathematical movements
 */
public class SineEnemy extends Enemy{

	// basic info
	private float oriX = this.getSpriteX();
	private BoundingBox box;
	private final float SPEED = 0.15f;
	
	// constants sine movement
	private static final int A = 96;
	private static final int T = 1500;
	private final float PI = (float)Math.PI;
	private float time = 0;
	
	private static final int POINTS = 200;
	
	/** constructor 
	 * @param x - initial starting x coordinate
	 * @param d - delay wait time*/
	public SineEnemy(float x, int d) throws SlickException {
		
		super("res/sine-enemy.png", x, d, POINTS);
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
			setSpriteY(this.getSpriteY() + this.getMovement());
			
			// sine movement
			setSpriteX( oriX   + (float)(A * Math.sin( (2*PI/T) * (time - getDelay()))) );		
			
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
	 * @return false indicate it can't shoot*/
	@Override 
	public boolean timeToShoot(float t) {
		return false;
	}

}
