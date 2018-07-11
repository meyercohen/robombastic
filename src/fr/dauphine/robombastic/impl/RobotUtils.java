package fr.dauphine.robombastic.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.dauphine.robombastic.ArenaAction;
import fr.dauphine.robombastic.ArenaItem;
import fr.dauphine.robombastic.BombAction;
import fr.dauphine.robombastic.BotContext.Action;
import fr.dauphine.robombastic.BotContext.ActionResponse;
import fr.dauphine.robombastic.Position;

/**
 * Useful methods for the bots
 * 
 * @author Meyer
 *
 */
public class RobotUtils {
	
	/** 
	 * Give the robot a list of possible action according to the elements returned by the radar
	 * 
	 * @return a List of robot possible actions. 
	 */
	public static List<Action<? extends ActionResponse>> possibleAction(Map<? extends Position, ArenaItem> nearElement){
		List<Action<? extends ActionResponse>> possibleActions = new LinkedList<>();
		Map<PositionImpl, Action<? extends ActionResponse>> mappingActionPosition = new HashMap<>();
		mappingActionPosition.put(new PositionImpl(-1, 0), ArenaAction.LEFT);
		mappingActionPosition.put(new PositionImpl(1, 0), ArenaAction.RIGHT);
		mappingActionPosition.put(new PositionImpl(0, -1), ArenaAction.UP);
		mappingActionPosition.put(new PositionImpl(0, 1), ArenaAction.DOWN);
		mappingActionPosition.put(new PositionImpl(0,0), BombAction.DROP_BOMB);
		
		// Remove action from the map that can't be made
		Iterator<? extends Position> it = nearElement.keySet().iterator();
		while(it.hasNext()){
			Position p = (Position) it.next();
			if(mappingActionPosition.containsKey(p)){
				mappingActionPosition.remove(p);
			}
		}
		
		// Adding the possible actions to a list
		Iterator<? extends Position> it2 = mappingActionPosition.keySet().iterator();
		while(it2.hasNext()){
			possibleActions.add(mappingActionPosition.get(it2.next()));
		}
		
		return possibleActions;	
	}
	
	/**
	 * Put the robot in a random free position on the map
	 * @return PositionImpl generated position of the robot
	 */
	public static PositionImpl randomBotPosition(){
		Grid g = Grid.getInstance();
		Random random = new Random();
		int x = random.nextInt((g.getWidth()));
		int y = random.nextInt((g.getHeight()));
		// if the (x,y) space is empty, return this position
		while(!(g.getGrid().get(y) != null && g.getGrid().get(y).get(x) != null && g.getGrid().get(y).get(x).getItems().contains(ArenaItem.EMPTY))){
			x = random.nextInt((g.getWidth()));
			y = random.nextInt((g.getHeight()));
		}
		PositionImpl newRobotPosition = new PositionImpl(x,y);
		return newRobotPosition;
	}

}
