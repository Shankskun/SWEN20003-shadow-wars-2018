/* Written by Soong Ze Shaun, 900793
 * SWEN20003 assignment 1 2018
 */

import org.newdawn.slick.SlickException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Scanner;

/** reader class
 *  reads input file, and create the appropriate enemy, 
 *  with location and delay details
 */
public class Reader{

	private ArrayList<String>  enemyType  = new ArrayList<String>();
	private ArrayList<Integer> startPoint = new ArrayList<Integer>();
	private ArrayList<Integer> delayTime  = new ArrayList<Integer>();

	private int len;
	private Scanner scanner;
	
	/** constructor 
	 * @param file - file name*/
	public Reader(String file) throws SlickException {
		
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        	
        	scanner = new Scanner(br);
        	// reading line by line
        	while(scanner.hasNext()) {
        		
        		String newLine = scanner.nextLine();
        		Character ch=(newLine.charAt(0));
        		
        		// ignoring comments
        		if(!ch.equals('#')) {

        			String array[] = newLine.split(",");
        			// storing data
        			enemyType .add(array[0]);
        			startPoint.add(Integer.parseInt(array[1]));
        			delayTime .add(Integer.parseInt(array[2]));
        		}
        	}
        }
        catch (Exception e) {
          	e.printStackTrace();
        }	
	}
	
	/** create enemies, after reading input file
	 *  stores them into Enemy array 
	 *  @param array - ArrayList of type Enemy */
	public void createEnemy(ArrayList<Enemy> array) throws SlickException {
		
		len = enemyType.size();
		
		for(int i=0; i<len; i++) {
			
			if(enemyType.get(i).equals("BasicEnemy")) {
				array.add(new BasicEnemy(startPoint.get(i), delayTime.get(i)));		
			}
			else if(enemyType.get(i).equals("SineEnemy")) {
				array.add(new SineEnemy(startPoint.get(i), delayTime.get(i)));				
			}
			else if(enemyType.get(i).equals("BasicShooter")) {
				array.add(new BasicShooter(startPoint.get(i), delayTime.get(i)));		
			}
			else if(enemyType.get(i).equals("Boss")) {
				array.add(new Boss(startPoint.get(i), delayTime.get(i)));		
			}
		}
	}
	
}