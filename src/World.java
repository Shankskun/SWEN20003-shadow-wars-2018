/* Written by Soong Ze Shaun, 900793
 * SWEN20003 assignment 1 2018
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;
import java.util.ArrayList;

/** World class, contains all objects where everything interact in it 
 */
public class World {
	
	// background variables
	private Image background;
	private float backMovement = 0;
	private float X1=0,X2=App.getWidth()/2;
	private float Y1=0,Y2=App.getHeight()/3 * 2;
	private static final float BACKGROUNDSPEED =  0.2f;
	
	// player variables
	private Player player;
    private float playerX = 480, playerY = 688;
    private float x,y;

    // power up variables
    private PowerUp powerup;
    private ArrayList<PowerUp> powerUp_list = new ArrayList<PowerUp>();
    
    //laser variables
    private Laser laser;
    private float playerTime = 0;
    private float enemyTime  = 0;
    private ArrayList<Laser> laser_list  = new ArrayList<Laser>();
    
    //enemy variables
    private ArrayList<Enemy> enemy_list  = new ArrayList<Enemy>();
    private static final int BOSS_OFFSET_1  = 74;
    private static final int BOSS_OFFSET_2  = 97;
    private static final int BOSS_MAX_LASER = 4;
    
	//reading input 
	private Reader read;
	
	//other add on stuff
	private Image YouWin;
	private Font font;
	private TrueTypeFont newFont;
	
	/** constructor */
	public World() throws SlickException {
		
		read       = new Reader("res/waves.txt");
		player     = new Player(playerX, playerY);
		background = new Image("res/space.png");
		// create enemies
		read.createEnemy(enemy_list);
		
		// minor add-ons
		YouWin	= new Image("res/youWin.png");
		font    = new Font("Comic Sans", Font.PLAIN , 20);
		newFont = new TrueTypeFont(font, true);
	}
	
	/** Update all of the sprite in the game 
	 * @param input - keyboard input key
	 * @param delta - Time passed since last frame (milliseconds).*/
	public void update(Input input, int delta) throws SlickException{

		/* to exit game (esc) */
        if (input.isKeyDown(Input.KEY_ESCAPE)) {
			System.exit(0);
		}
        
		/* SPEED UP 5X */
		if (input.isKeyDown(Input.KEY_S)) {
			delta = delta * 5;
		}else {
			delta = delta * 1;
		}
		
        /* PLAYER LASER */
		createPlayerLaser(delta, input);
		
		/* ENEMY */
		enemyTime += delta;
		for(Enemy enemy : enemy_list) {
			
			// enemy's movement
			enemy.update(delta);
			
			/* ENEMY LASERS */
			createEnemyLaser(enemy);
			
			/* COLLISIONS */
			if (enemy.getIsPresent() != false) {
				
				// player hit enemy
				collisionEnemy_Player(enemy);
				
				// laser with enemy
				for(Laser x: laser_list) {
					collisionEnemy_Laser(enemy, x);
				}
			}			
		}
		/* LASER */
		updateLaser(delta);
		
		/* POWERUP */
		updatePowerUp(delta);
        
        /* PLAYER MOVEMENT */        
		player.update(delta, input);
		player.move(x,y);
		player.shieldUp(player.getShield(), delta);
		
		/* BACKGROUND */
		backMovement -= delta * BACKGROUNDSPEED;

        /* RESET VALUES */
        // resets player movement after every input
        x=0;
        y=0;
	}
	
	/** Draw all of the sprites in the game */
	public void render() {
		
		/* BACKGROUND */
		renderBackground();
		
		/* PLAYER */
		player.render();
		
		/* POWERUP */
		// remove when powerUp is collected
		powerUp_list.removeIf(powerUp_list->powerUp_list.getIsPresent() == false);
		for(PowerUp p : powerUp_list) {
			p.render();
		}
		
		/* ENEMY */
		// removed destroyed enemies
		enemy_list.removeIf(enemy_list->enemy_list.getLives() <= 0);
		for(Enemy enemy : enemy_list) {
			if(enemy.getIsPresent() != false) {
				enemy.render();
			}	
		}
		
		/* LASER */
		// remove over bounded or destroyed lasers
		laser_list.removeIf(laser_list->laser_list.getIsPresent() == false);
		for(Laser x : laser_list) {
			if(x.getIsPresent() == true) {
				x.render();
			}	
		}

		/* you won */
		if(enemy_list.size() == 0) {
			YouWin.drawCentered(App.getWidth()/2, App.getHeight()/2);
			newFont.drawString(App.getWidth()-600, App.getHeight()-168, "Press esc to Exit");
		}
	}
	
	/** powerUp drop 
	 * @param x & y - starting coordinate of power up
	 * @param type - power up type (0 or 1)*/
	private void powerUpDrop(float x, float y, int type) throws SlickException {
		
		// not both choices
		if(type == -1) {
			return;
		}
		// drop power up
		else if(type == 0) {
			powerup = new Shield(x, y);
		}
		else if(type == 1) {
			powerup = new ShotSpeed(x, y);
		}
		powerUp_list.add(powerup);
	}
	
	/** create and manage player's laser (shoot rate & laser type)
	 *  @param delta - time
	 *  @param input - input key type
	 * @throws SlickException */
	private void createPlayerLaser(int delta, Input input) throws SlickException {
		
		if(playerTime != 0) {
			playerTime += delta;
		}
		// limit one shot for every 0.35 sec
		if(player.readyToShoot(playerTime, delta)) {
			
			// reset timer
			playerTime = 0;
			
			// regular laser
			if ((input.isKeyDown(Input.KEY_SPACE))) {
				laser = new PlayerLaser("res/shot.png", player.getSpriteX(), player.getSpriteY());
				laser_list.add(laser);
				playerTime += delta;
			}
			// dodge
			if (input.isKeyDown(Input.KEY_D)){
				laser = new PlayerLaser("res/doge.png", player.getSpriteX(), player.getSpriteY());
				laser.setConstant(0.4f);
				laser_list.add(laser);
				playerTime += delta;
			}
		}
	}
	
	/** create enemy lasers (shooter & boss)
	 * @param e - object of type enemy
	 * @throws SlickException */
	private void createEnemyLaser(Enemy e) throws SlickException {
		
		if (e.getIsPresent() != false) {
			if(e.getToShoot() == true) {
				
				if(e.timeToShoot(enemyTime) == true) {
					// basic shooter
					if(!(e instanceof Boss)) {
						laser = new EnemyLaser("res/enemy-shot.png", e.getSpriteX(), 
								e.getSpriteY());
						laser_list.add(laser);
					}
					// boss
					else {
						int array[] = new int[] {BOSS_OFFSET_1,-BOSS_OFFSET_1,BOSS_OFFSET_2,-BOSS_OFFSET_2};
						
						for(int j=0; j<BOSS_MAX_LASER; j++) {
							laser = new EnemyLaser("res/enemy-shot.png", e.getSpriteX()+array[j], 
									e.getSpriteY());
							laser_list.add(laser);
						}
					}
				}
			}
		}
	}
	/** collision between enemy and player 
	 * @param e - object of type Enemy*/
	private void collisionEnemy_Player(Enemy e) {
		
		if (player.getBox().intersects(e.getBox())) {
					
			// don't reduce life when there is shield
			if(player.getShield() == false) {
				player.contactSprite(e);
			}
			// when player runs out of lives
			if(player.getLives() == 0) {
				System.out.println("GG");
				System.exit(0);
			}
		}
	}
	
	/** Collision between enemy and lasers, adds points to player onces destroyed
	 * @param e - object of type Enemy
	 * @param x - object of type Laser 
	 * @throws SlickException */
	private void collisionEnemy_Laser(Enemy e, Laser x) throws SlickException {
		
		// laser hit enemy
		if(x.isEnemyLaser() == false) {
			if((x.getBox().intersects(e.getBox())) && (e.getIsPresent() == true)) {
				
				// reduce lives and remove laser
				e.reduceLives();
				x.setIsPresent(false);
				
				// add points & drop lootbox when enemy is destroyed
				if(e.getLives() <= 0) {
					x.contactSprite(e);
					player.addPoints(e);
					powerUpDrop(e.getSpriteX(), e.getSpriteY(), PowerUp.toDropOrNotToDrop());								
				}
			}
		}
	}
	
	/** update laser's movements & player's contact with enemy laser
	 * @param delta - time*/
	private void updateLaser(int delta) {
		
		for(Laser x : laser_list) {
			x.update(delta);
			
			// enemy laser hit player
			if(x.getBox().intersects(player.getBox()) && x.isEnemyLaser() == true) {
				// don't reduce life when there is shield
				if(player.getShield() == false) {
					player.contactSprite(x);
				}
				// when player runs out of lives
				if(player.getLives() == 0) {
					System.out.println("GG");
					System.exit(0);
				}
			}
		}
	}
	
	/** update PowerUp movements and player's collection of it 
	 * @param delta - time*/
	private void updatePowerUp(int delta) {
		
		for(PowerUp p : powerUp_list) {
			// movement
			p.update(delta);
			
			// powerUp hit player
			if(p.getBox().intersects(player.getBox())) {
				
				player.setPowerUp(p);
				p.setIsPresent(false);
			}
		}
	}
	
	/** render background*/
	private void renderBackground() {
		
		// SET BACKGROUND (4 corners of screen) 
		background.draw(X1,Y1,X1,backMovement,X2,Y2 + backMovement);
		background.draw(X2,Y1,X1,backMovement,X2,Y2 + backMovement);
		background.draw(X1,Y2,X1,backMovement,X2,Y2 + backMovement);
		background.draw(X2,Y2,X1,backMovement,X2,Y2 + backMovement);
	}
}