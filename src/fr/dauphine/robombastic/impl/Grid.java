package fr.dauphine.robombastic.impl;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.dauphine.robombastic.ArenaItem;

/**
 * Represents the grid in term of object management (i.e : the real grid behind the graphical one).
 * 
 * @author Meyer & Swamynathan
 *
 */
public class Grid implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	private Map<Integer, List<ArenaCell>> arenaCells;
	private static Grid arenaGrid;
	
	private Grid() {
		this.arenaCells = new HashMap<Integer, List<ArenaCell>>();
	}
	
	public static Grid getInstance() {
		if(arenaGrid == null){
			arenaGrid = new Grid();
		}
		return arenaGrid;
	}
	
	/**
	 * Initialize a grid corresponding to a text grid.
	 * @param arena
	 */
	public void init(Path arena){
		Objects.requireNonNull(arena);
		try{
			createGridFromFile(arenaCells, arena);
		}catch(IOException e){
			e.getMessage();
		}
	}

	
	/**
	 * Create a grid from a text file.
	 * @param arenaCells
	 * @param arena
	 * @throws IOException
	 */
	private void createGridFromFile(Map<Integer, List<ArenaCell>> arenaCells, Path arena) throws IOException{
		if(Files.isReadable(arena)){
			List<String> lines = Files.readAllLines(arena);
			int i = 0;
			for (String string : lines) {
				arenaCells.put(i, new LinkedList<ArenaCell>());
				for (int j = 0; j < string.length(); j++) {
					if(string.charAt(j) == ' ' || string.charAt(j) == '\t'){
						arenaCells.get(i).add(new ArenaCell(i, j, ArenaItem.EMPTY));
					}
					else if(string.charAt(j) == 'w'){
						arenaCells.get(i).add(new ArenaCell(i, j, ArenaItem.WALL));
					}
					else{
						throw new IllegalStateException("Arene non conforme");
					}
				}
				i++;
			}
		}
		else{
			throw new IllegalArgumentException("Fichier inexistant ou impossible à lire");
		}
	}
	
	/**
	 * Update the state of a position in the grid
	 * @param oldPosition
	 * @param newPosition
	 * @param state
	 */
	public void updateGrid(PositionImpl oldPosition, PositionImpl newPosition, ArenaItem state){
		arenaCells.get(oldPosition.getY()).get(oldPosition.getX()).getItems().remove(state);
		arenaCells.get(newPosition.getY()).get(newPosition.getX()).getItems().add(state);
	}
	
	/**
	 * @return the height of the arena
	 */
	public int getHeight() {
		return arenaCells.size();
	}
	
	/**
	 * 
	 * @return the width of the arena
	 */
	public int getWidth() {
		return arenaCells.get(1).size();

	}
	
	public Map<Integer, List<ArenaCell>> getGrid(){
		return arenaCells;
	}
	
	@Override
	public String toString() {
		return arenaCells.toString();
	}
	
	public void setArenaCells(Map<Integer, List<ArenaCell>> arenaCells) {
		this.arenaCells = arenaCells;
	}
	
	public Map<Integer, List<ArenaCell>> getArenaCells() {
		return arenaCells;
	}

	/**
	 * Replacing the object read from the stream to avoid singleton serialization/deserialization problem
	 * Ensures that no another instance is created by serializing and deserializing the singleton.
	 * @return the instance
	 */
	private Object readResolve() {
		arenaGrid.setArenaCells(arenaCells);
		return arenaGrid;
	}

}