package fr.dauphine.robombastic.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.dauphine.robombastic.ArenaItem;
import fr.dauphine.robombastic.BombAction.BombResponse;
import fr.dauphine.robombastic.BotContext.ActionResponse;
import fr.dauphine.robombastic.DeadBotException;
import fr.dauphine.robombastic.enums.DefaultValues;
import fr.dauphine.robombastic.example.Arena;
import fr.dauphine.robombastic.loader.BotLoader;
import fr.dauphine.robombastic.utils.JarUtils;

/**
 * Game Engine - Core of the game
 * @author Meyer & Swamynathan
 *
 */
public class Game implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String arena;
	private String robotsDir;
	private int radarSize;
	private int armyLimit;
	private int turnDuration;
	private int bombDuration;
	/**
	 * Fifo queue threadsafe that will store action to perform
	 */
	private Queue<Message> fifo = new ConcurrentLinkedQueue<>();
	/**
	 * List of bombs released by the bots
	 */
	private Map<PositionImpl, Bomb> allBombs = new HashMap<>();
	/**
	 * List of all the robots of the game
	 */
	private List<Robot> allRobots = new LinkedList<>();
	/**
	 * Store the winner between the two teams that compete
	 */
	private String winnerTeam;
	/**
	 * Reference to the management Grid that stores all the logical elements of the game
	 */
	private Grid grid;
	/**
	 * Flag to know whether the game is terminated or not
	 */
	private boolean done = false;
	
	private int numberMessagePolled = 0;
	
	private static Game game;
	
	private Game(){
		grid = Grid.getInstance();
	}
	
	public static Game getInstance() {
		if(game == null){
			game = new Game();
		}
		return game;
	}
	
	/**
	 * Execute the action sent in the queue by robot nextTurn call
	 * @param m
	 * @return ActionResponse
	 * @throws DeadBotException 
	 */
	public ActionResponse executeAction(Message m) throws DeadBotException{
		// Execute the corresponding action associated to each robot in the fifoQueue 
		// Send the response to a List<ActionResponse> and send it to nextTurn
		if(!m.getRobot().getAlive()){
			throw new DeadBotException(m.getRobot()+" is dead");
		}
		PositionImpl currentPosition = m.getRobot().getPosition();
		int old_x = currentPosition.getX();
		int old_y = currentPosition.getY();
		//TODO change the declaration here to something better 
		PositionImpl newPosition = currentPosition;
		ActionResponse a = null;
		Grid g = Grid.getInstance();
		if(m.getAction() == null){
			throw new IllegalStateException();
		}
		switch(HelperAction.lookUp(m.getAction())){
			case UP:
				newPosition.setY(old_y-1);
				a = ArenaItem.EMPTY;
				g.updateGrid(new PositionImpl(old_x, old_y), newPosition, ArenaItem.FRIEND_BOT);
				break;
			case LEFT:
				newPosition.setX(old_x-1);
				a = ArenaItem.EMPTY;
				g.updateGrid(new PositionImpl(old_x, old_y), newPosition, ArenaItem.FRIEND_BOT);
				break;
			case DOWN:
				newPosition.setY(old_y+1);
				a = ArenaItem.EMPTY;
				g.updateGrid(new PositionImpl(old_x, old_y), newPosition, ArenaItem.FRIEND_BOT);
				break;
			case RIGHT:
				newPosition.setX(old_x+1);
				a = ArenaItem.EMPTY;
				g.updateGrid(new PositionImpl(old_x, old_y), newPosition, ArenaItem.FRIEND_BOT);
				break;
			case DROP_BOMB:
				a = BombResponse.BOMB_DROPPED;
				g.updateGrid(currentPosition, newPosition, ArenaItem.BOMB);
				allBombs.put(new PositionImpl(old_x, old_y), new Bomb(new PositionImpl(newPosition.getX(), newPosition.getY())));
				break;
		}
		return a;
		
	}

	/**
	 * Take off one turn to all the bombs of the game
	 */
	public void tickBomb(){
		if((game.getNumberMessagePolled() % game.getAllAliveBots().size()) == 0){
			game.setNumberMessagePolled(0);
			for (Entry<PositionImpl, Bomb> entry : game.getAllBombs().entrySet()) {
				if(!entry.getValue().isDone()){
					entry.getValue().triggerTimer();
				}
			}
		}
	}
	
	/**
	 * Get all alive bots of the game
	 * @return a list of bots
	 */
	public List<Robot> getAllAliveBots(){
		List<Robot> aliveBots = new LinkedList<>();
		for (Robot robot : allRobots) {
			if(robot.getAlive()){
				aliveBots.add(robot);
			}
		}
		return aliveBots;
	}
	
	/**
	 * Check which team wins the game
	 * @return true if there is a winner, false else
	 */
	public boolean checkWinner(){
		boolean isThereAWinner = true;
		String team = null;
		
		// Search for the first alive bot and get his team
		for (Robot robot : allRobots) {
			if(robot.getAlive()){
				System.out.println("FIRST FOUND BOT ALIVE : "+robot.team);
				team = robot.getTeam();
				break;
			}
			
		}
		// Search for other team bot alive
		for (Robot robot : allRobots) {
			if(robot.getAlive() && !team.equals(robot.getTeam())){
				System.out.println("BOT OF DIFFERENT TEAM ALIVE :"+robot.team);
				isThereAWinner = false;
			}
		}
		// Store the winner team if there is one
		if(isThereAWinner){
			System.out.println("WINNER TEAM : "+team);
			this.winnerTeam = team;
		}
		
		return isThereAWinner;
		
	}
	
	/**
	 * Save the game state
	 */
	public void serialize(){
		FileOutputStream output;
		ObjectOutput out;
		try{
			output = new FileOutputStream("game_settings.ser");
			out = new ObjectOutputStream(output);
			out.writeObject(grid);
			out.writeObject(allRobots);
			out.writeObject(allBombs);
			out.writeObject(arena);
			out.writeObject(robotsDir);
			out.writeObject(radarSize);
			out.writeObject(armyLimit);
			out.writeObject(turnDuration);
			out.writeObject(bombDuration);
			out.writeObject(fifo);
			out.writeObject(winnerTeam);
			out.close();
			output.close();
		}
		catch (Exception ex){
			ex.printStackTrace();
			System.out.println("Game settings save failed");
		}
	}
	
	/**
	 * Load the saved game
	 */
	@SuppressWarnings("unchecked")
	public void deserialize(){
		try{
			FileInputStream is = new FileInputStream("game_settings.ser");
			ObjectInputStream in = new ObjectInputStream(is);
			grid = (Grid)in.readObject();
			allRobots = (List<Robot>)in.readObject();
			allBombs = (Map<PositionImpl, Bomb>)in.readObject();
			arena = (String)in.readObject();
			robotsDir = (String)in.readObject();
			radarSize = (int)in.readObject();
			armyLimit = (int)in.readObject();
			turnDuration = (int)in.readObject();
			bombDuration = (int)in.readObject();
			fifo = (Queue<Message>)in.readObject();
			winnerTeam = (String)in.readObject();
			in.close();
			// Create the threads to load the bots
			loadBots();
		}
		catch(Exception e){
			System.out.println("Game loading failed");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Load the bots when we reload a saved game
	 */
	public void loadBots(){
		ExecutorService executor = Executors.newCachedThreadPool();
		for (Robot robot : game.getAllRobots()) {
			if(robot.getAlive()){
				// Run the game while it isn't finished
				executor.submit(new Runnable(){
					@Override
					public void run() {
						while(!game.isDone()){
							robot.run();
						}
						
					}
					
				});
			}	
		}
		
	}
	/**
	 * Correspond to how far a bot can see 
	 * @return int radarSize
	 */
	public int getRadarSize() {
		if(radarSize == 0){
			return Integer.parseInt(DefaultValues.DEFAULT_RADAR_SIZE.toString());
		}
		return radarSize;
	}
	
	public void setRadarSize(int radarSize) {
		this.radarSize = radarSize;
	}

	/**
	 * Access to arena file
	 * @return String
	 */
	public String getArena() {
		if(arena == null){
			try {
				// Extract the arena folder from the jar to the temp directory
				JarUtils.extractFromJar("CANDASSAMYCOHEN.jar", System.getProperty("java.io.tmpdir"), DefaultValues.DEFAULT_ARENA.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return System.getProperty("java.io.tmpdir") + File.separator +DefaultValues.DEFAULT_ARENA.toString()+ File.separator +"arena.txt";
		}
		return arena;
	}


	public void setArena(String arena) {
		this.arena = arena;
	}

	/**
	 * Access to robot directory where armies are stored
	 * @return String path to the directory
	 */
	public String getRobotsDir() {
		if(robotsDir == null){
			try {
				JarUtils.extractFromJar("CANDASSAMYCOHEN.jar", System.getProperty("java.io.tmpdir"), DefaultValues.DEFAULT_ROBOTS_DIR.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return System.getProperty("java.io.tmpdir") + File.separator + DefaultValues.DEFAULT_ROBOTS_DIR.toString();
		}
		return robotsDir;
	}


	public void setRobotsDir(String robotsDir) {
		this.robotsDir = robotsDir;
	}

	/**
	 * Correspond to the max number of bot per army
	 * @return int army size
	 */
	public int getArmyLimit() {
		if(armyLimit == 0){
			return Integer.parseInt(DefaultValues.DEFAULT_ARMY_SIZE.toString());
		}
		return armyLimit;
	}


	public void setArmyLimit(int armyLimit) {
		this.armyLimit = armyLimit;
	}

	/**
	 * Correspond to the duration of one turn
	 * @return int turn duration
	 */
	public int getTurnDuration() {
		if(turnDuration == 0){
			return Integer.parseInt(DefaultValues.DEFAULT_TURN_DURATION.toString());
		}
		return turnDuration;
	}


	public void setTurnDuration(int turnDuration) {
		this.turnDuration = turnDuration;
	}

	/**
	 * Correspond to the duration of one bomb before the explosion
	 * @return int bomb duration
	 */
	public int getBombDuration() {
		if(bombDuration == 0){
			return Integer.parseInt(DefaultValues.DEFAULT_BOMB_DURATION.toString());
		}
		return bombDuration;
	}


	public void setBombDuration(int bombDuration) {
		this.bombDuration = bombDuration;
	}
	
	/**
	 * Access to the queue that contains all the action messages send by the bots
	 * @return
	 */
	public Queue<Message> getFifo() {
		return fifo;
	}
	
	/**
	 * Access to the list of all robots of the game
	 * @return List of robots
	 */
	public List<Robot> getAllRobots() {
		return allRobots;
	}
	
	public void setAllRobots(List<Robot> allRobots) {
		this.allRobots = allRobots;
	}
	
	/**
	 * Access to a map of all the bomb dropped
	 * @return Map
	 */
	public Map<PositionImpl, Bomb> getAllBombs() {
		return allBombs;
	}
	
	/**
	 * Access to the winner team
	 * @return String winner team
	 */
	public String getWinnerTeam() {
		return winnerTeam;
	}
	
	/**
	 * Access to the grid that contains all the elements of the game
	 * @return Grid
	 */
	public Grid getGrid() {
		return grid;
	}
	
	/**
	 * 
	 * @return true if the game is done, false else
	 */
	public boolean isDone() {
		return done;
	}
	
	public void setDone(boolean done) {
		this.done = done;
	}
	
	/**
	 * Access to the number of message processed by the game
	 * @return int number of message polled
	 */
	public int getNumberMessagePolled() {
		return numberMessagePolled;
	}
	
	public void setNumberMessagePolled(int numberMessagePolled) {
		this.numberMessagePolled = numberMessagePolled;
	}

	
	public static void main(String[] args) {
		boolean loadLastSave = false;
		Game game = Game.getInstance();
		// Look for arguments
		for(int i=0;i<args.length;i++){
			switch (args[i]) {
			case "-arena":
				game.setArena(args[i+1]);
				break;
			case "-robotsDir":
				game.setRobotsDir(args[i+1]);
				break;
			case "-radarSize":
				game.setRadarSize(Integer.parseInt(args[i+1]));
				break;
			case "-armyLimit":
				game.setArmyLimit(Integer.parseInt(args[i+1]));
				break;
			case "-turnDuration":
				game.setTurnDuration(Integer.parseInt(args[i+1]));
				break;
			case "-bombDuration":
				game.setBombDuration(Integer.parseInt(args[i+1]));
				break;
			case "-loadSave":
				game.deserialize();
				loadLastSave = true;
			default:
				break;
			}
		}
		if(!loadLastSave){
		
			// Init Grid
			game.getGrid().init(Paths.get(game.getArena()));

			// Load robots from the robotsDir directory
			game.setAllRobots(BotLoader.loadModules(game.getRobotsDir()));
			
			// Load threads associated with the robots
			game.loadBots();
		}
		// Creating the graphical arena
		Arena arena = new Arena(game.getGrid());
		arena.display(); 
		
		while(true){
			if(!game.getFifo().isEmpty()){
				// Try to get the token that is the shared queue
				synchronized(game.getFifo()){
				    Message m = game.getFifo().poll();
				    game.setNumberMessagePolled(game.getNumberMessagePolled()+1);
				    try {
				    	// Stop the game if there's a winner
				    	if(game.checkWinner()){
							game.setDone(true);
				    		break;
						}
				    	// Try to process a message sent by a bot
						game.executeAction(m);
						// Tick bomb only when all alive bots have performed their action which signify that one turn is done
						game.tickBomb();
						
					} catch (DeadBotException e) {
						e.printStackTrace();
					}
				    game.getFifo().notify();
				}
			}
		}
	
	}
	
}
