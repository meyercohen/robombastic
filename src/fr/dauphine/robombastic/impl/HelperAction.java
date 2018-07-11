package fr.dauphine.robombastic.impl;

import java.util.HashMap;
import java.util.Map;

import fr.dauphine.robombastic.ArenaAction;
import fr.dauphine.robombastic.BombAction;
import fr.dauphine.robombastic.BotContext.Action;
import fr.dauphine.robombastic.BotContext.ActionResponse;

/**
 * Helper technique to "merge" the enum that implements Action marker interface
 * @author Meyer, 
 * @author question asked on stackoverflow : http://stackoverflow.com/questions/41890736/java-switch-on-enum-that-implements-same-interface
 *
 */
public enum HelperAction {
    UP(ArenaAction.UP),
    DOWN(ArenaAction.DOWN),
    LEFT(ArenaAction.LEFT),
    RIGHT(ArenaAction.RIGHT),
    DROP_BOMB(BombAction.DROP_BOMB);

    private Action<? extends ActionResponse> source;

    private <T extends ActionResponse> HelperAction(Action<T> source) {
        this.source = source;
    }

    private static Map<Action<? extends ActionResponse>, HelperAction> map;
    
    /**
     * Permits to make a unique switch statement to evaluate the action asked by the bots
     * @param source Action
     * @return the appropriate action according to the source
     */
    public static HelperAction lookUp(Action<? extends ActionResponse> source) {
        if(map == null){
        	map = new HashMap<>();
        	for(HelperAction e : HelperAction.values()){
        		map.put(e.source, e);
        	}
                
        }
        
        return map.get(source);
    }
}
