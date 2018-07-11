package fr.dauphine.robombastic.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import fr.dauphine.robombastic.ArenaItem;
import fr.dauphine.robombastic.impl.Game;
import fr.dauphine.robombastic.impl.Grid;
import fr.dauphine.robombastic.impl.Robot;

/**
 * Load the robot classes by introspection that are within a jar file
 * @author Meyer
 * @author Inspired from http://baptiste-wicht.developpez.com/tutoriels/java/modularisation/application/?page=Modules
 *
 */
public class BotLoader {
	private static List<URL> urls = new ArrayList<URL>();
	
	private static ClassLoader classLoader;
	
	/**
	 * Load the robot army jar
	 * @param path_to_dir the directory where robot jars are stored
	 * @return An army of robots
	 */
	@SuppressWarnings("unchecked")
	public static List<Robot> loadModules(String path_to_dir){ 
		List<Robot> modules = new ArrayList<Robot>(); 
	 
		List<String> classes = getModuleClasses(path_to_dir); 
	 
		AccessController.doPrivileged(new PrivilegedAction<Object>(){ 
			@Override 
			public Object run() { 
				classLoader = new URLClassLoader( 
				urls.toArray(new URL[urls.size()]),  
				BotLoader.class.getClassLoader()); 
	 
				return null; 
			} 
		}); 
	 
		for(String c : classes){ 
			try { 
				Class<?> moduleClass = Class.forName(c, true, classLoader); 
	 
				if(Robot.class.isAssignableFrom(moduleClass)){ 
					
					Class<Robot> castedClass = (Class<Robot>)moduleClass; 
					Robot module = castedClass.newInstance();
					// Take the minimum between the user entered army limit and the default army size
					int armySize = min(module.getDefaultArmySize(), Game.getInstance().getArmyLimit());
					System.out.println("ARMY SIZE "+armySize);
					if(armySize == 1){
						modules.add(module);
					}
					else{
						for(int i = 0; i < armySize; i++){
							modules.add(module);
							// Update the grid by adding a bot item at the bot position
							Grid.getInstance().updateGrid(module.getPosition(), module.getPosition(), ArenaItem.FRIEND_BOT);
							module = castedClass.newInstance();
						}
					}
					
				}  
			} catch (ClassNotFoundException e1) { 
				e1.printStackTrace(); 
			} catch (InstantiationException e) { 
				e.printStackTrace(); 
			} catch (IllegalAccessException e) { 
				e.printStackTrace(); 
			} 
		} 
	 
		return modules; 
	}

	/**
	 *  
	 * @param path_to_dir
	 * @return
	 */
	private static List<String> getModuleClasses(String path_to_dir){ 
		List<String> classes = new ArrayList<String>();
		
		//List module files  
		File[] files = new File(path_to_dir).listFiles(new BotFilter()); 
 
		for(File f : files){ 
			JarFile jarFile = null; 
 
			try { 
				//Open the jar file 
				jarFile = new JarFile(f); 
 
				//Get the manifest 
				Manifest manifest = jarFile.getManifest(); 
 
				//Get the class 
				String moduleClassName = manifest.getMainAttributes().getValue("Bot-Class"); 
 
				classes.add(moduleClassName); 
 
				urls.add(f.toURI().toURL()); 
			} catch (IOException e) { 
				e.printStackTrace(); 
			} finally { 
				if(jarFile != null){ 
					try { 
						jarFile.close(); 
					} catch (IOException e) { 
						e.printStackTrace(); 
					} 
				} 
			} 
		} 
 
		return classes; 
	}
	
	/**
	 * Take the minimum between two integers
	 * @param defaultArmySize
	 * @param parameteredArmySize
	 * @return the minimum between the default army size and the parametered one
	 */
	private static int min(int defaultArmySize, int parameteredArmySize){
		return (parameteredArmySize < defaultArmySize) ? parameteredArmySize : defaultArmySize;
	}
	
	/**
	 * A filter class for jar files
	 */
	private static class BotFilter implements FileFilter { 
		// Accept only jar files
		@Override 
		public boolean accept(File file) { 
			return file.isFile() && file.getName().toLowerCase().endsWith(".jar"); 
		} 
	} 
}
