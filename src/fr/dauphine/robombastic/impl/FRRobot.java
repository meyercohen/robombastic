package fr.dauphine.robombastic.impl;
/**
 * Implementation of Robot - French army
 * @author Meyer
 *
 */
public class FRRobot extends Robot{

	
	private static final long serialVersionUID = 1L;
	
	public FRRobot() {
		super();
		team = "FR";
		DEFAULT_ARMY_SIZE = 3;
		IMAGE_NAME = "soldierFrance";
	} 

}
