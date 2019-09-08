import java.awt.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Input;
import utilities.BoundingBox;

/** Player class, child of Sprite class
 *  contains basic details of player
 *  all controls and abilities it can do in the game 
 */
public class Player extends Sprite{
	
	// basic info
	private BoundingBox box= getBox();
	private int lives      = MAXLIVES;
	private int pointSum   = 0;
    private static final float SPEED = 0.5f;
	
	// shield and laser
	private Image lifeImg = new Image("res/lives.png");
	private Image shield  = new Image("res/Shield.png");
	private int shieldTime= 0;
	private boolean isShieldUp = false;
	private static final int lifeX = 40, lifeY = 696;
	private static final int MAX_SHOOT_RATE    = 350;
	
	// powerUp
	private static boolean powerShieldOn = false;
	private static boolean powerShootOn  = false;
	private static final int MAXSHIELD   = 3000;
	
	// points
	private static final int scoreX= 20, scoreY= 730;
	private Font font;
	private TrueTypeFont newFont;
	
	// boundary of window
    private static final int width  = App.getWidth() ;
    private static final int height = App.getHeight();
    
    // player dimensions
    private static final int PLAYERDIM = 64;
    private static final int MAXLIVES  = 3;

    /* getter methods */
    /** @return player live*/
    public int getLives() {
    	return new Integer(lives);
    }
    /** @return player shield is on/off*/
    public boolean getShield() {
    	return new Boolean(isShieldUp);
    }
    /** @return player score*/
    public int getScore() {
    	return new Integer(pointSum);
    }
    
    /* setter methods */
    /** @param set powerup faster shooting rate*/
    public static void setPowerShoot(boolean x) {
    	powerShootOn  = x;
    }
    /** @param set powerup shield */
    public static void setPowerShield(boolean x) {
    	powerShieldOn = x;
    }
    
    
    /** constructor 
     * @param x&y - starting coordinates of player*/
	public Player(float x, float y) throws SlickException {
		super("res/spaceship.png", x, y);
		
		// fonts for score indicator
		font    = new Font("Comic Sans", Font.PLAIN , 18);
		newFont = new TrueTypeFont(font, true); 
	}
	
	
	/** read player input and update 
	 * @param delta - Time passed since last frame (milliseconds).
	 * @param input - keyboard input keys*/
	public void update(int delta, Input input) {
		
	    // up down left right
        if (input.isKeyDown(Input.KEY_LEFT)) {
			setSpriteX(getSpriteX() - delta * SPEED);
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			setSpriteX(getSpriteX() + delta * SPEED);
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			setSpriteY(getSpriteY() - delta * SPEED);
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			setSpriteY(getSpriteY() + delta * SPEED);
		}
	}
	
	
	/** player's moving commands 
	 * @param dx & dy - change in movements*/ 
    public void move(float dx, float dy) {
    	setSpriteX(getSpriteX() + dx);
    	setSpriteY(getSpriteY() + dy);
        
    	// prevent leaving boundary (show the whole ship)
		if (getSpriteX() < 0 + PLAYERDIM/2) {
			setSpriteX(PLAYERDIM/2);
		}
		if (getSpriteX() > width - PLAYERDIM/2) {
			setSpriteX(width - PLAYERDIM/2);
		}
		if (getSpriteY() < 0 + PLAYERDIM/2) {
			setSpriteY(PLAYERDIM/2);
		}
		if (getSpriteY() > height - PLAYERDIM/2) {
			setSpriteY(height - PLAYERDIM/2);
		}
		
		// update hit box of player
		box = new BoundingBox(this.getSpriteImg(), this.getSpriteX(), this.getSpriteY()) ;
		setBox(box);
    }
    
    
    /** when player knocked into enemy, reduce life 
     * @param other - other sprite objects*/
    @Override
    public void contactSprite(Sprite other) {
		
		//reduce life
    	lives -= 1;
    	isShieldUp = true;
    	System.out.println("~~ Shield is up bois ~~");
	}
    
    
    /** set a fixed time for invulnerability 
     * @param shield - boolean of whether shield is on or off
     * @param delta  - Time passed since last frame (milliseconds).*/
    public void shieldUp(boolean shield, int delta) {
    	
    	// reset timer
    	if(powerShieldOn == true) {
    		shieldTime = 0;
    	}
    	// start timer
    	if(shield == true) {
    		shieldTime += delta;
    	}
    	// disable shield after 3sec (or 5sec with powerUp)
    	if(shieldTime >= MAXSHIELD + Shield.shield_OffSet(powerShieldOn, delta)) {
    		isShieldUp = false;
    		shieldTime = 0;
    	}
    }
    
    
    /** set a fixed time for shooting rate 
     * @param time - time interval between shots
     * @param delta- time passed since last frame (milliseconds).
     * @return true when its time to shoot, false when its not*/
    public boolean readyToShoot(float time, int delta) {
    	
    	// shooting rate
    	if(time == 0 || time > MAX_SHOOT_RATE - ShotSpeed.shotSpeed_OffSet(powerShootOn, delta)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    } 
    
    
 	/** enable powerUp with new timer
 	 * @param type - PowerUp type (using powerUp class)*/
    public void setPowerUp(PowerUp type) {
    	
    	// shield
    	if(type instanceof Shield) {
    		powerShieldOn = true;
    		isShieldUp    = true;
    	}
    	// speed shoot
    	else if(type instanceof ShotSpeed) {
    		powerShootOn  = true;
    	}
    }
    
    
    /** sum up points for ded enemies 
     * @param other - other enemy objects*/
    public void addPoints(Enemy other) {
    	pointSum += other.getPoints();
    }
    
    
    /** render player object, with its attributes */
    @Override
 	public void render() {
 		// player
 		getSpriteImg().drawCentered(this.getSpriteX(), this.getSpriteY());
 		
 		// shield
 		if(isShieldUp == true) {
 			shield.drawCentered(this.getSpriteX(), this.getSpriteY());
 		}
 		
 		// lives
 		int x = lifeX;
 		for(int i= 0; i<this.lives; i++) {
 			lifeImg.drawCentered(x, lifeY);
 			x += 40;
 		}
 		
 		// points
 		newFont.drawString(scoreX, scoreY, "Score: "+String.format("%d", pointSum));
 	}

}
