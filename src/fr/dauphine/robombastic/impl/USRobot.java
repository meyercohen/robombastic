package fr.dauphine.robombastic.impl;
/**
 * Implementation of Robot - US Army
 * @author Meyer
 *
 */
public class USRobot extends Robot{

	private static final long serialVersionUID = 1L;
	
	public USRobot() {
		super();
		team = "USA";
		DEFAULT_ARMY_SIZE = 5;
		IMAGE_NAME = "soldierUSA";
	}
}
