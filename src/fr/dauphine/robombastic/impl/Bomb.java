package fr.dauphine.robombastic.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.dauphine.robombastic.ArenaItem;

/**
 * 
 *  Represents a bomb in the game
 * @author Swamynathan & Meyer 
 * 
 */
public class Bomb implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static int DEFAULT_DURATION = 3;
	private PositionImpl position;
	private int duration;
	private Game game;
	private boolean active;
	private boolean done;
	
	public Bomb(PositionImpl position){
		game = Game.getInstance();
		this.position = position;
		this.duration = getDuration();
		this.active = false;
		this.done = false;
	}
	
	public PositionImpl getPosition() {
		return position;
	}

	public void setPosition(PositionImpl position) {
		this.position = position;
	}
	/**
	 * If the duration is not set by the play, we take the default duration
	 * @return int duration of the bomb
	 */
	public int getDuration() {
		return (game.getBombDuration() != 0) ? game.getBombDuration() : DEFAULT_DURATION;
	}
	/**
	 * If active is true, this means that the bomb is being exploded
	 * @return true if the bomb is active, false else
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * 
	 * @return true if the bomb has exploded, false else
	 */
	public boolean isDone() {
		return done;
	}
	
	/**
	 * Manage the timer. The timer is decremented after a turn, in other term, when all the robots have perform their action during one iteration.
	 * 
	 */
	public void triggerTimer(){
		
		synchronized(game.getFifo()){
			duration--;
			System.out.println("Bomb turn remaining : "+this+" "+duration);
		}
			
		if(duration==0){
			explode();
			System.out.println("BOMB EXPLODED");
		}
		// Extinguish the fire at the next turn in order to get the fire be visible to human eye
		else if(duration==-1){
			this.done = true;
			noMoreFire();
		}
	}
	
	/**
	 * Explode the bomb
	 */
	public void explode(){
		Grid g = Grid.getInstance();
		// If the bomb is not in an exploding state
		// This is used to avoid a bomb to be exploded two times which can cause an exception
		if(active == false){
			active = true;
			// Lookup upside elements
			lookUpElements(position.getY(), position.getX(), "up", g);
			// Lookup downside elements
			lookUpElements(position.getY(), position.getX(), "bottom", g);
			// Lookup leftside elements
			lookUpElements(position.getY(), position.getX(), "left", g);
			// Lookup rightside elements
			lookUpElements(position.getY(), position.getX(), "right", g);
			g.updateGrid(position, position, ArenaItem.EMPTY);	
		}
		else{
			return;
		}
		
		
	}
	
	/**
	 * Browse the map in the specified direction and look for bots to destroy
	 * @param pos_y
	 * @param pos_x
	 * @param direction
	 * @param g
	 */
	private void lookUpElements(int pos_y, int pos_x, String direction, Grid g){
		int i = 0;
		List<ArenaItem> itemFound = new LinkedList<>();
		int new_pos_y = 0;
		int new_pos_x = 0;
		//Search at all cardinal positions. If there's a bomb, it should explode and if there's a robot it should be destroyed
		while(!itemFound.contains(ArenaItem.WALL)){
			new_pos_y = pos_y;
			new_pos_x = pos_x;
			if(direction == "up"){
				itemFound = g.getGrid().get(pos_y-i).get(pos_x).getItems();
				g.getGrid().get(pos_y-i).get(pos_x).setOnFire(true);
				new_pos_y = pos_y-i;
			}
			else if(direction == "left"){
				itemFound = g.getGrid().get(pos_y).get(pos_x-i).getItems();
				g.getGrid().get(pos_y).get(pos_x-i).setOnFire(true);
				new_pos_x = pos_x-i;
			}
			else if(direction == "bottom"){
				itemFound = g.getGrid().get(pos_y+i).get(pos_x).getItems();
				g.getGrid().get(pos_y+i).get(pos_x).setOnFire(true);
				new_pos_y = pos_y+i;
			}
			else if(direction == "right"){
				itemFound = g.getGrid().get(pos_y).get(pos_x+i).getItems();
				g.getGrid().get(pos_y).get(pos_x+i).setOnFire(true);
				new_pos_x = pos_x+i;
			}
			i++;
			if(itemFound.contains(ArenaItem.BOMB) && i > 1){
				game.getAllBombs().get(new PositionImpl(new_pos_x, new_pos_y)).explode();
			}
			else if(itemFound.contains(ArenaItem.FRIEND_BOT) || itemFound.contains(ArenaItem.ENEMY_BOT)){
				PositionImpl robotPosition = new PositionImpl(new_pos_x, new_pos_y);
				for (Robot robot : game.getAllRobots()) {
					if(robot.getPosition().equals(robotPosition)){
						System.out.println("DESTROYING ROBOT(S)");
						robot.destroy();
					}
				}
			}
			// The bomb stops when it encounter a wall
			else if(itemFound.contains(ArenaItem.WALL)){
				System.out.println(direction + " " +itemFound);
				break;
			}
			
	   }
	}
	
	/**
	 * Remove fire from the grid once the bomb have exploded
	 */
	private void noMoreFire(){
		Iterator<List<ArenaCell>> it = Grid.getInstance().getGrid().values().iterator();
		while(it.hasNext()){
			List<ArenaCell> cell = (List<ArenaCell>) it.next();
			for (ArenaCell arenaCell : cell) {
				//Extinguish the fire
				arenaCell.setOnFire(false);
			}
		}
	}
}
