package gtPlusPlus.api.interfaces;

import gtPlusPlus.api.objects.Logger;

public interface IPlugin {
	
	/**
	 * @return A {@link String} object which returns the {@link IPlugin}'s name.
	 */
	public String getPluginName();
	
	/**
	 * @return 
	 * A {@link String} object which returns the {@link IPlugin}'s short name.
	 * This String should only contain 4 Characters.
	 */
	public String getPluginAbbreviation();
	
	/**
	 * @param message - A {@link String} object which holds a message to be logged to console.
	 */
	default void log(String message) {
		Logger.INFO("["+getPluginAbbreviation()+"] "+message);
	}
	
	/**
	 * @param message - A {@link String} object which holds a warning/error message to be logged to console.
	 */
	default void logDebug(String message) {
		Logger.WARNING("["+getPluginAbbreviation()+"] "+message);
	}

	public boolean preInit();
	public boolean init();
	public boolean postInit();
	
}
