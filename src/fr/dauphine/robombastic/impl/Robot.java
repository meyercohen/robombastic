package fr.dauphine.robombastic.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.dauphine.robombastic.ArenaItem;
import fr.dauphine.robombastic.Bot;
import fr.dauphine.robombastic.BotContext;
import fr.dauphine.robombastic.BotContext.Action;
import fr.dauphine.robombastic.BotContext.ActionResponse;
import fr.dauphine.robombastic.DeadBotException;
import fr.dauphine.robombastic.Position;

/**
 * Represents a bot in the game
 * @see Bot
 * @author Meyer & Swamynathan
 *
 */
public abstract class Robot implements Bot, Serializable{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int DEFAULT_ARMY_SIZE = 2;
	protected String IMAGE_NAME = "soldier";
	private static int idRobot = 0;
	
	private int id;
	protected String team;
	private BotContext context;
	private PositionImpl position;
	private boolean alive;
	
	public Robot() {
		this(0,0);
		this.position = RobotUtils.randomBotPosition();
	}
	
	public Robot(int x, int y) {
		init(new BotContextImpl(this));
		this.alive = true;
		this.position = new PositionImpl(x,y);
		this.id = ++idRobot;
	}
	
	public Robot(String team) {
		this();
		this.team = team;
	}
	
	@Override
	public void init(BotContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		while(context.getCurrentBotInfo().isAlive()){	
			try {
				Random random = new Random();
				System.out.println(position);
				// Get all near and visible elements
				Map<? extends Position, ArenaItem> visibleElements = context.getCurrentBotInfo().radar();
				// Evaluate all the possible actions
				List<Action<? extends ActionResponse>> possibleActions = RobotUtils.possibleAction(visibleElements);
				// Choose of a random action among possible actions
				Action<? extends ActionResponse> actionToPerform = possibleActions.get(random.nextInt(possibleActions.size()));
				System.out.println("Action to Perform : Robot "+id+" "+actionToPerform);
				// Ask the game to execute the action
				context.nextTurn(actionToPerform);
			} catch (DeadBotException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	@Override
	public void destroy() {
		this.alive = false;
		// If the robot die, the grid must be updated
		Grid.getInstance().getGrid().get(position.getY()).get(position.getX()).getItems().remove(ArenaItem.FRIEND_BOT);
	}

	@Override
	public String getImageName() {
		return IMAGE_NAME; 
	}

	@Override
	public int getDefaultArmySize() {
		return DEFAULT_ARMY_SIZE;
	}
	
	public BotContext getContext() {
		return context;
	}
	
	public PositionImpl getPosition() {
		return position;
	}
	
	public void setPosition(PositionImpl position){
		this.position = position;
	}
	
	public boolean getAlive(){
		return this.alive;
	}
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Robot other = (Robot) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
