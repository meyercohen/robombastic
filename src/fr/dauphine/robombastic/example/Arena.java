package fr.dauphine.robombastic.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fr.dauphine.robombastic.ArenaItem;
import fr.dauphine.robombastic.impl.Game;
import fr.dauphine.robombastic.impl.Grid;
import fr.dauphine.robombastic.impl.Robot;
import fr.umlv.lawrence.Application;
import fr.umlv.lawrence.DefaultGridModel;
import fr.umlv.lawrence.GridPane;
import fr.umlv.lawrence.InputListener;
import fr.umlv.lawrence.Key;
import fr.umlv.lawrence.svg.SVGImageProvider;
/**
 * Represents the graphical Arena
 * @author Meyer
 *
 */
public class Arena {

	private final DefaultGridModel<ArenaImage> model; 
	private final int NB_TILE_X;
	private final int NB_TILE_Y;
	private static final int TILE_WIDTH = 30;
	private static final int TILE_HEIGHT = 30;
	private final GridPane<ArenaImage> pane; 
	private final Map<String, ArenaImage> allImages = new HashMap<>();	
	private final Grid arenaMap;
	public Arena(Grid grid){
		
		arenaMap = grid;
		NB_TILE_X = arenaMap.getWidth();
		NB_TILE_Y = arenaMap.getHeight();	
		
		/* Set up the arena and the grid based display */ 
		model=new DefaultGridModel<>(NB_TILE_X, NB_TILE_Y); 
		SVGImageProvider<ArenaImage> images = new SVGImageProvider<>();		 
		images.setDPI(93);
		 
		/* Load all images */ 
		allImages.put("gColor", new ArenaImage(images, "gColor"));
		allImages.put("wall", new ArenaImage(images, "wall"));
		allImages.put("fire", new ArenaImage(images, "fire"));
		allImages.put("bomb", new ArenaImage(images, "bomb"));
		allImages.put("soldier", new ArenaImage(images, "soldier"));
		allImages.put("soldierRussia", new ArenaImage(images, "soldierRussia"));
		allImages.put("soldierUSA", new ArenaImage(images, "soldierUSA"));
		allImages.put("soldierFrance", new ArenaImage(images, "soldierFrance"));
		 
		pane = new GridPane<>(model,images,TILE_WIDTH, TILE_HEIGHT);

		drawGrid();
	}

	/**
	 * Draw the arena according to the elements of the Grid
	 */
	private void drawGrid() {
		for (int x = 0; x < model.getWidth(); x++) {
			for (int y = 0; y < model.getHeight(); y++) {
				List<ArenaItem> arenaItems = arenaMap.getGrid().get(y).get(x).getItems();
				for (ArenaItem arenaItem : arenaItems) {
					if(arenaMap.getGrid().get(y).get(x).isOnFire() && arenaItem != ArenaItem.WALL){
						model.addDeffered(x, y, allImages.get("fire"));
					}
					else if(arenaItem == ArenaItem.EMPTY){
						model.addDeffered(x, y, allImages.get("gColor"));
					}
					else if(arenaItem == ArenaItem.FRIEND_BOT){
						for (Robot robot : Game.getInstance().getAllAliveBots()) {
							if(robot.getPosition().getX() == x && robot.getPosition().getY() == y){
								model.addDeffered(x, y, allImages.get(robot.getImageName()));
							}
						}
					}
					else if(arenaItem == ArenaItem.WALL){
						model.addDeffered(x, y, allImages.get("wall"));
					}
					else if(arenaItem == ArenaItem.BOMB){
						model.addDeffered(x, y, allImages.get("bomb"));
					}
				}
			}
		}

		model.swap();
	}
	
	/**
	 * Display a message with the name of the winner team
	 */
	private void displayWinner(){
		if(Game.getInstance().isDone()){
			pane.displayMessage("The winner team is : "+Game.getInstance().getWinnerTeam());
			Application.close(pane);
			System.exit(0);
		}
	}
	
	/**
	 * Runnable that is executed by the Thread to draw the Grid and show up the winner when there is one
	 * @return Runnable
	 */
	public Runnable getRunnable(){
		return new Runnable(){
			public void run(){
				drawGrid();		
				displayWinner();
			}
		};
	}
	
	/**
	 * Entry point of the Arena class that initialize the thread and update the view peridically
	 */
	public void display() {
		Application.display(pane, "Robombastic CANDASSAMY COHEN", false, true);

		final ScheduledFuture<?> periodicUpdate = Application.scheduleWithFixedDelay(getRunnable(), 0, 1000,TimeUnit.MILLISECONDS);

		pane.addInputListener(new InputListener() {

			@Override
			public void keyTyped(int arg0, int arg1, Key arg2) {
				if (arg2.equals(Key.ESCAPE)) {
					System.out.println("escape key pressed!");
					periodicUpdate.cancel(true);
					Application.close(pane);
					System.exit(0);
				}

				else if (arg2.equals(Key.S)) {

					System.out.println("Save key pressed!");
					Game.getInstance().serialize();
					pane.displayMessage("Game has been saved !");

				}
			}

			@Override
			public void mouseClicked(int arg0, int arg1, int arg2) {
			}
		});
	}
}

