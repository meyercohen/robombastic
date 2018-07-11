package fr.dauphine.robombastic.impl;

import java.io.Serializable;

import fr.dauphine.robombastic.BotContext;
import fr.dauphine.robombastic.BotContext.ActionResponse;

/**
 * Class used to encapsulate the action sent in the queue. The goal is to have access to which bot have sent the action.
 * 
 * @author Meyer
 *
 */
public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static volatile int count = 0;
	private final int id;
	private final Robot robot;
	private final BotContext.Action<? extends ActionResponse> action;
	
	public Message(Robot robot, BotContext.Action<? extends ActionResponse> action) {
		id = ++count;
		this.robot = robot;
		this.action = action;
	}
	
	public Robot getRobot() {
		return robot;
	}
	
	public BotContext.Action<? extends ActionResponse> getAction() {
		return action;
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
		Message other = (Message) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", robot=" + robot + ", action=" + action + "]";
	}
	
	
	
	
}
