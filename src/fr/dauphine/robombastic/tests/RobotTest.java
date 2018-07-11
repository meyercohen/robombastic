package fr.dauphine.robombastic.tests;

import java.nio.file.Paths;

import org.junit.Test;

import fr.dauphine.robombastic.impl.Game;
import fr.dauphine.robombastic.impl.Grid;

/**
 * Test class for Robot class
 * @author Swamynathan 
 */
public class RobotTest {

	@Test
	public void testRun() {

	}

	@Test
	public void testDestroy() {

		//PositionImpl pos = new PositionImpl(0, 0);
		Game game = Game.getInstance();
		Grid g = Grid.getInstance();
		game.setRadarSize(1);
		game.setArmyLimit(2);

		g.init(Paths.get("src/arena/arena.txt"));


	}

}
