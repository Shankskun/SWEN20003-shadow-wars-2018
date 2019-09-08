import org.newdawn.slick.SlickException;

import utilities.BoundingBox;

/** EnemyLaser class, child of Laser
 *  contains laser type where enemy can shoot
 */
public class EnemyLaser extends Laser {
	
	// laser variables
	private BoundingBox box = getBox();	
	private float constant  = 1;
	private static final float SPEED = 0.7f;
	
	/** @param x - a constant determining laser speed*/
	@Override 
	public void setConstant(float x) {
		// for custom laser speeds (default=1)
		this.constant = x;
	}
	
	/** constructor 
	 * @param imageSrc - image used for sprite
	 * @param x & y - starting coordinates for laser */
	public EnemyLaser(String imageSrc, float x, float y) throws SlickException {
		super(imageSrc, x, y);
		setImage(imageSrc);
	}

	/** update laser's movement 
	 * @param delta - Time passed since last frame (milliseconds)*/
	@Override
	public void update(int delta) {
		
		// laser's movement
		setMovement(delta * SPEED * constant);
		this.setSpriteY( this.getSpriteY() + getMovement() );
		
		// update laser hit box, and xy coordinates
		box = new BoundingBox(this.getImage(), this.getSpriteX(), this.getSpriteY()) ;
		setBox(box);
				
		// when laser is out of bound
		if(this.getSpriteY() > App.getHeight()) {
			this.setIsPresent(false);
		}
	}
	
	/** check laser type 
	 * @return true indicate it is type enemy laser */
	@Override
	public boolean isEnemyLaser() {
		return true;
	}
	
}
