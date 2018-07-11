package fr.dauphine.robombastic.impl;

import java.io.Serializable;

import fr.dauphine.robombastic.GeneralInfo;

/**
 * Implementation of GeneralInfo - Represents the informations given to the bot by the game
 * Usage of Singleton Pattern to be sure to have only one instance of the class shared among all other classes
 * @see GeneralInfo
 * @author Meyer
 *
 */
public class GeneralInfoImpl implements GeneralInfo, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static GeneralInfoImpl instance; 
	private Grid arenaGrid;
	
	private GeneralInfoImpl() { 
		this.arenaGrid = Grid.getInstance();
	}
	
	public static GeneralInfoImpl getInstance(){
		if(instance == null){
			 instance = new GeneralInfoImpl();
		}
		return instance;
	}

	@Override
	public int getArenaWidth() {
		return arenaGrid.getWidth();
	}

	@Override
	public int getArenaHeight() {
		return arenaGrid.getHeight();
	}

	@Override
	public int getRadarSize() {
		return Game.getInstance().getRadarSize();
	}

	@Override
	public int getBombDuration() {
		return Game.getInstance().getBombDuration();
	}

	@Override
	public int getMaxBombs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBombSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTourTime() {
		return Game.getInstance().getTurnDuration();
	}

}
