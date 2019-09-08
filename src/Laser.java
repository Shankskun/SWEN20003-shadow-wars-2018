import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;

/** Laser abstract class, child of Sprite class
 *  contains basic details of lasers present in the game
 */
public abstract class Laser extends Sprite{

	private Image laser;
	private float movement; 
	
	/* getter and setter methods */
	/** @return Image of laser */
	public Image getImage() {
		return laser;
	}
	/** @param img - name of image  */
	public void setImage(String img) throws SlickException {
		laser = new Image(img);
	}
	/** @return laser's movement */
	public float getMovement() {
		return movement;
	}
	/** @param set laser's movement */
	public void setMovement(float x) {
		this.movement = x;
	}
	/** @param x - a constant determining laser speed*/
	public abstract void setConstant(float x);
	
	/** constructor 
	 * @param imageSrc - image used for sprite
	 * @param x & y - starting coordinates for laser */
	public Laser(String imageSrc, float x, float y) throws SlickException {
		super(imageSrc, x, y);
	}
	
	/** update laser's movement 
	 * @param delta - Time passed since last frame (milliseconds)*/
	public abstract void update(int delta) ;
	
	/** check laser type 
	 * @return boolean, is type enemy laser(true), or player laser(false)*/
	public abstract boolean isEnemyLaser() ;

}
