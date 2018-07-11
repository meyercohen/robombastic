package fr.dauphine.robombastic.impl;
/**
 * Implementation of Robot - Russian Army
 * @author Meyer
 *
 */
public class RURobot extends Robot{

	
	private static final long serialVersionUID = 1L;
	
	public RURobot() {
		super();
		team = "RU";
		DEFAULT_ARMY_SIZE = 4;
		IMAGE_NAME = "soldierRussia";
	}
}
