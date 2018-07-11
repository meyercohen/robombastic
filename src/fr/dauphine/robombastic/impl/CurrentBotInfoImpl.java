package fr.dauphine.robombastic.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.dauphine.robombastic.ArenaItem;
import fr.dauphine.robombastic.Bot;
import fr.dauphine.robombastic.CurrentBotInfo;
import fr.dauphine.robombastic.DeadBotException;
import fr.dauphine.robombastic.Position;

/**
 * Informations available for the current bot
 * @see CurrentBotInfo
 * @author Meyer & Swamynathan
 *
 */

public class CurrentBotInfoImpl implements CurrentBotInfo, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Robot robot;
	
	public CurrentBotInfoImpl() {
	}
	
	public CurrentBotInfoImpl(Robot robot){
		this.robot = robot;
	}
	
	@Override
	public List<? extends Bot> getAllAliveFriendlyBots() throws DeadBotException {
		if(!robot.getAlive()){
			throw new DeadBotException("The robot : "+robot+" is dead");
		}
		ArrayList<Robot> allBots = new ArrayList<>();
		Game game = Game.getInstance();
		for (Robot bot : game.getAllRobots()) {
			if(!bot.equals(robot) && bot.getAlive() && bot.getTeam().equals(robot.getTeam())){
				allBots.add(bot);
			}
		}		
		return Collections.unmodifiableList(allBots);
	}

	@Override
	public boolean isAlive() {
		return robot.getAlive();
	}

	@Override
	public Map<? extends Position, ArenaItem> radar() throws DeadBotException {
		int radarSize = Game.getInstance().getRadarSize();
		HashMap<PositionImpl, ArenaItem> radarMap = new HashMap<>();
		Grid g = Grid.getInstance();
		int robot_pos_x = robot.getPosition().getX();
		int robot_pos_y = robot.getPosition().getY();
		if(radarSize == 0){
			throw new IllegalStateException("Radar should be initialize to more than 0");
		}
		// Ajout des elements de gauche
		addElements(robot_pos_y, robot_pos_x, "left",radarMap, g, radarSize);
		// Ajout des elements de droite
		addElements(robot_pos_y, robot_pos_x, "right",radarMap, g, radarSize);
		// Ajout des elements du haut
		addElements(robot_pos_y, robot_pos_x, "up",radarMap, g, radarSize);
		// Ajout des elements du bas
		addElements(robot_pos_y, robot_pos_x, "bottom",radarMap, g, radarSize);
		
		return Collections.unmodifiableMap(radarMap);
	}
	
	/**
	 * Add found elements in the radar map
	 * @param pos_y
	 * @param pos_x
	 * @param direction
	 * @param radarMap
	 * @param g
	 * @param radarSize
	 */
	private void addElements(int pos_y, int pos_x, String direction, Map<PositionImpl, ArenaItem> radarMap, Grid g, int radarSize){
		int i = 0;
		while(i < radarSize){
			i++;
			ArenaItem itemFound = null;
			if(direction == "up"){
				itemFound = g.getGrid().get(pos_y-i).get(pos_x).getItems().get(0);
			}
			else if(direction == "left"){
				itemFound = g.getGrid().get(pos_y).get(pos_x-i).getItems().get(0);
			}
			else if(direction == "bottom"){
				itemFound = g.getGrid().get(pos_y+i).get(pos_x).getItems().get(0);
			}
			else if(direction == "right"){
				itemFound = g.getGrid().get(pos_y).get(pos_x+i).getItems().get(0);
			}
			
			if(itemFound == ArenaItem.EMPTY){
				continue;
			}
			else if(itemFound == ArenaItem.BOMB){
				continue;
			}
			else{
				if(direction == "up"){
					radarMap.put(new PositionImpl(0, -i), itemFound);
				}
				else if(direction == "left"){
					radarMap.put(new PositionImpl(-i, 0), itemFound);
				}
				else if(direction == "bottom"){
					radarMap.put(new PositionImpl(0, i), itemFound);
				}
				else if(direction == "right"){
					radarMap.put(new PositionImpl(i, 0), itemFound);
				}
				
				if(itemFound == ArenaItem.WALL){
					break;
				}
			}
		}
	}
}
