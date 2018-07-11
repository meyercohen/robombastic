package fr.dauphine.robombastic.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import fr.dauphine.robombastic.BotContext;
import fr.dauphine.robombastic.CurrentBotInfo;
import fr.dauphine.robombastic.DeadBotException;
import fr.dauphine.robombastic.GeneralInfo;
/**
 * Context unique to each bot. A bot can interact with the game by using this class
 * @see BotContext
 * @author Meyer
 *
 */
public class BotContextImpl implements BotContext, Serializable { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GeneralInfo arenaInfo;
	private CurrentBotInfo currentBotInfo;
	private Queue<Message> queue;
	private Robot robot;
	
	public BotContextImpl() {
		this.arenaInfo = GeneralInfoImpl.getInstance();
		this.currentBotInfo = new CurrentBotInfoImpl();
		this.queue = Game.getInstance().getFifo();
	}
	
	public BotContextImpl(Robot robot){
		this();
		this.robot = robot;
		this.currentBotInfo = new CurrentBotInfoImpl(robot);
	}

	@Override
	public GeneralInfo getGeneralInfo() {
		return arenaInfo;
	}

	@Override
	public CurrentBotInfo getCurrentBotInfo() {
		return currentBotInfo;
	}

	@Override
	public Set<? extends Class<? extends Info>> infoTypes() {
		// TODO On progress - Contains all infoTypes
		Set<Class<? extends Info>> infoTypes = new HashSet<>();
		infoTypes.add(GeneralInfo.class);
		infoTypes.add(CurrentBotInfo.class);
		return infoTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <I extends Info> I info(Class<I> infoType) {
		// Useful for the bonus exercice
		// For V1 project, we can simply use the above getters
		Set<? extends Class<? extends Info>> infoTypes = infoTypes();
		if(!infoTypes.contains(infoType)){
			throw new IllegalArgumentException();
		}
		else{
			Class<I> infoClass = null;
			try {
				infoClass = (Class<I>) Class.forName(infoType.getName()+"Impl");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return createObject(infoClass);
		}
		
	}
	/**
	 * Create an instance of the class entered in parameter
	 * @param classToCreate
	 * @return I instance of Info class
	 */
	private <I extends Info> I createObject(Class<I> classToCreate){
		I clazz = null;
		try {
			clazz = classToCreate.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	@Override
	public <R extends ActionResponse, A extends Action<R>> R nextTurn(A action) throws DeadBotException {
		R response = null;
		if(!robot.getAlive()){
			throw new DeadBotException("the robot "+robot+"is dead, let him rest in peace !");
		}
		//Send Message that contains the robot and the action to perform for him to the Message Queue of the Game 
		//and wait until the action has been processed
		synchronized(queue){
			Message message = new Message(robot, action);
			queue.offer(message);
			while(queue.contains(message)){
				try{
					Thread.sleep(Game.getInstance().getTurnDuration());
					queue.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			queue.notify();
		}

		
		return response;
	}

}
