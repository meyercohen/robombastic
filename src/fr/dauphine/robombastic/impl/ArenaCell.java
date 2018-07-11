package fr.dauphine.robombastic.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import fr.dauphine.robombastic.ArenaItem;

/** 
 * Represents an element that is on the Grid.
 * 
 * @author Meyer & Swamynathan
 *
 */
public class ArenaCell implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final PositionImpl position;
	/**
	 * Contains all the item that are in a cell
	 */
	private List<ArenaItem> items;
	/**
	 * Flag that permits to know when a bomb has explosed and fire is declared
	 */
	private boolean onFire;
	
	public ArenaCell(int x, int y, ArenaItem item) {
		Objects.requireNonNull(item);
		this.position = new PositionImpl(x, y);
		this.items = new LinkedList<>();
		items.add(item);
		this.onFire = false;
	}
	
	public PositionImpl getPosition() {
		return position;
	}

	public void setItems(List<ArenaItem> items) {
		this.items = items;
	}
	
	public List<ArenaItem> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return items.toString();
	}
	
	public void setOnFire(boolean onFire) {
		this.onFire = onFire;
	}
	
	public boolean isOnFire() {
		return onFire;
	}
	
}
