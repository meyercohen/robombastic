package fr.dauphine.robombastic.example;

import fr.umlv.lawrence.svg.SVGImageProvider;

/**
 * 
 * Manage the image in the Arena
 *
 */
public class ArenaImage {
	public ArenaImage(SVGImageProvider<ArenaImage> provider, String spriteName){
		System.out.println("registering : " + spriteName);
		String path = "/sprites/" + spriteName + ".svg";

		/* Fetch image relative to the location of the build of ArenaImage */
		provider.registerImage(this, ArenaImage.class.getResource(path)); 			
	}	
}
