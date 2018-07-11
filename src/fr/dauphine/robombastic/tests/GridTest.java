package fr.dauphine.robombastic.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import fr.dauphine.robombastic.ArenaItem;
import fr.dauphine.robombastic.impl.ArenaCell;
import fr.dauphine.robombastic.impl.Grid;

/**
 * Test class for Grid class
 * @author Swamynathan
 *
 */
public class GridTest {
	Grid g = Grid.getInstance();
	private Map<Integer, List<ArenaCell>> map = new HashMap<>();
	
	private Map<Integer, List<ArenaCell>> fileToMap(Path p) throws IOException{
		
		if(Files.isReadable(p)){
			List<String> lines = Files.readAllLines(p);
			int i = 0;
			for (String string : lines) {
				map.put(i, new LinkedList<ArenaCell>());
				for (int j = 0; j < string.length(); j++) {
					if(string.charAt(j) == ' '){
						map.get(i).add(new ArenaCell(i, j, ArenaItem.EMPTY));
					}
					else if(string.charAt(j) == 'w'){
						map.get(i).add(new ArenaCell(i, j, ArenaItem.WALL));
					}
				}
			i++;
			}
			
		}
		return map;
		
	}

	@Test(expected = NullPointerException.class)
	public void testInit() {
		g.init(null);
	}

	// to implement
	@Test
	public void testcreateGridFromFile() throws IOException {
		Map<Integer, List <ArenaCell>> map2 = new HashMap<>();
		
		map = fileToMap(Paths.get("src/tests/arenaTest.txt"));
		
		map2.put(0, new LinkedList<ArenaCell>());
		map2.put(1, new LinkedList<ArenaCell>());
		map2.put(2, new LinkedList<ArenaCell>());
		map2.put(3, new LinkedList<ArenaCell>());
		
		map2.get(0).add(new ArenaCell(0, 0, ArenaItem.EMPTY));
		map2.get(0).add(new ArenaCell(0, 1, ArenaItem.WALL));
		map2.get(0).add(new ArenaCell(0, 2, ArenaItem.WALL));
		map2.get(0).add(new ArenaCell(0, 3, ArenaItem.WALL));
		map2.get(0).add(new ArenaCell(0, 4, ArenaItem.WALL));
		map2.get(0).add(new ArenaCell(0, 5, ArenaItem.WALL));
		map2.get(0).add(new ArenaCell(0, 6, ArenaItem.WALL));
		map2.get(0).add(new ArenaCell(0, 7, ArenaItem.WALL));
		
		map2.get(1).add(new ArenaCell(1, 0, ArenaItem.WALL));
		map2.get(1).add(new ArenaCell(1, 1, ArenaItem.WALL));
		map2.get(1).add(new ArenaCell(1, 2, ArenaItem.WALL));
		map2.get(1).add(new ArenaCell(1, 3, ArenaItem.EMPTY));
		map2.get(1).add(new ArenaCell(1, 4, ArenaItem.EMPTY));
		map2.get(1).add(new ArenaCell(1, 5, ArenaItem.WALL));
		map2.get(1).add(new ArenaCell(1, 6, ArenaItem.EMPTY));
		map2.get(1).add(new ArenaCell(1, 7, ArenaItem.WALL));
		
		map2.get(2).add(new ArenaCell(2, 0, ArenaItem.WALL));
		map2.get(2).add(new ArenaCell(2, 1, ArenaItem.EMPTY));
		map2.get(2).add(new ArenaCell(2, 2, ArenaItem.EMPTY));
		map2.get(2).add(new ArenaCell(2, 3, ArenaItem.WALL));
		map2.get(2).add(new ArenaCell(2, 4, ArenaItem.EMPTY));
		map2.get(2).add(new ArenaCell(2, 5, ArenaItem.EMPTY));
		map2.get(2).add(new ArenaCell(2, 6, ArenaItem.EMPTY));
		map2.get(2).add(new ArenaCell(2, 7, ArenaItem.WALL));
		
		map2.get(3).add(new ArenaCell(3, 0, ArenaItem.WALL));
		map2.get(3).add(new ArenaCell(3, 1, ArenaItem.WALL));
		map2.get(3).add(new ArenaCell(3, 2, ArenaItem.WALL));
		map2.get(3).add(new ArenaCell(3, 3, ArenaItem.WALL));
		map2.get(3).add(new ArenaCell(3, 4, ArenaItem.WALL));
		map2.get(3).add(new ArenaCell(3, 5, ArenaItem.WALL));
		map2.get(3).add(new ArenaCell(3, 6, ArenaItem.WALL));
		map2.get(3).add(new ArenaCell(3, 7, ArenaItem.WALL));
		
		if(map.size() == map2.size()){
			for(int i=0;i<map2.size();i++){
				assertEquals(map.get(i), map2.get(i));
			}
		}
	}

}
