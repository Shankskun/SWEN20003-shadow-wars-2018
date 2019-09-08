import org.newdawn.slick.SlickException;


import utilities.BoundingBox;

/** PlayerLaser class, child of Laser
 *  contains laser type where player can shoot
 */
public class PlayerLaser extends Laser{

	// laser variables
	private BoundingBox box = getBox();
	private float constant  = 1;
	private static final float SPEED = 3;
	
	/** @param x - a constant determining laser speed*/
	@Override 
	public void setConstant(float x) {
		// for custom laser speeds
		this.constant = x;
	}
	
	/** constructor 
	 * @param imageSrc - image used for sprite
	 * @param x & y - starting coordinates for laser */
	public PlayerLaser(String imageSrc, float x, float y) throws SlickException {
		super(imageSrc, x, y);
		this.setImage(imageSrc);
	}
	
	/** update laser's movement 
	 * @param delta - Time passed since last frame (milliseconds)*/
	@Override
	public void update(int delta) {
		// laser's movement
		setMovement(delta * SPEED * constant);
		this.setSpriteY( this.getSpriteY() - getMovement() );
			
		// update laser hit box, and xy coordinates
		box = new BoundingBox(this.getImage(), this.getSpriteX(), this.getSpriteY()) ;
		setBox(box);
		
		// when laser is out of bound
		if(this.getSpriteY() < 0) {
			this.setIsPresent(false);
		}
	}
	
	/** check laser type 
	 * @return false indicates non-enemy laser*/
	@Override
	public boolean isEnemyLaser() {
		return false;
	}
	
}
