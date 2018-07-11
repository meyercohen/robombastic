package fr.dauphine.robombastic.enums;

/**
 * Enumeration that contains all the default values for the game
 * It is used when the user don't enter a parameter, the values used instead are these one
 * @author Meyer
 *
 */
public enum DefaultValues {
	DEFAULT_ARENA{
		@Override
		public String toString() {
			return "robombastic_arena";
		}
	},
	DEFAULT_ROBOTS_DIR{
		@Override
		public String toString() {
			return "robombastic_bots";
		}
	},
	DEFAULT_RADAR_SIZE{
		@Override
		public String toString() {
			return "1";
		}
	},
	DEFAULT_ARMY_SIZE{
		@Override
		public String toString() {
			return "4";
		}
	},
	DEFAULT_TURN_DURATION{
		@Override
		public String toString() {
			return "500";
		}
	},
	DEFAULT_BOMB_DURATION{
		@Override
		public String toString() {
			return "3";
		}
	},
	
}
