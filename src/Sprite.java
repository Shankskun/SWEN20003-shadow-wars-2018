/* Written by Soong Ze Shaun, 900793
 * SWEN20003 assignment 1 2018
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import utilities.BoundingBox;

/** Sprite class, contains all objects (player, laser ,enemy etc)
 *  with their basic attributes, methods and collision checker among all sprites
 */
public class Sprite {
		
    // new sprite object, and its variables
	private Image newSprite;
	private BoundingBox box;
	private float spriteX, spriteY;
	private boolean isPresent = true;
    
    /* getter methods */
	/** @return x position of sprite object */
    public float getSpriteX() {
    	return new Float(spriteX);
    }
	/** @return y position of sprite object */
    public float getSpriteY() {
    	return new Float(spriteY);
    }
	/** @return bounding box of sprite object */
    public BoundingBox getBox() {
    	return box;
    }
	/** @return sprite is present or not, in game */
    public boolean getIsPresent() {
    	return new Boolean(isPresent);
    }
	/** @return image of sprite object */
    public Image getSpriteImg() {
    	return this.newSprite;
    }
    /* setter methods */
    /** @param x - set new x position of sprite object */
    public void setSpriteX(float x) {
    	spriteX = x;
    }
    /** @param y - set new y position of sprite object */
    public void setSpriteY(float y) {
    	spriteY = y;
    }
    /** @param x - set new bounding box */
    public void setBox(BoundingBox bb) {
    	box = bb;
    }
    /** @param x - set sprite to be present in game*/
    public void setIsPresent(boolean x) {
    	isPresent = x;
    }
  
    /** constructor 
     * @param imageSrc - image for sprite
     * @param x & y - starting coordinates for sprite*/    
	public Sprite(String imageSrc, float x, float y) throws SlickException {
		
		newSprite = new Image(imageSrc);
		box = new BoundingBox(newSprite, x, y);
		
		spriteX = x;
		spriteY = y;
	}
	
	/** rendering sprite object */
	public void render() {
		newSprite.drawCentered(spriteX, spriteY);
	}
	
	/** Should be called when one sprite makes contact with another 
	 * @param other - other sprite objects*/
	public void contactSprite(Sprite other) {
		
		// remove both sprite off map
		other.setIsPresent(false);
		this.setIsPresent(false) ;
	}
}