package gtPlusPlus.api.interfaces;

import gtPlusPlus.api.objects.Logger;

public interface IPlugin {

    /**
     * @return A {@link String} object which returns the {@link IPlugin}'s name.
     */
    String getPluginName();

    /**
     * @return A {@link String} object which returns the {@link IPlugin}'s short name. This String should only contain 4
     *         Characters.
     */
    String getPluginAbbreviation();

    /**
     * @param message - A {@link String} object which holds a message to be logged to console.
     */
    default void log(String message) {
        Logger.INFO("[" + getPluginAbbreviation() + "] " + message);
    }

    boolean preInit();

    boolean init();

    boolean postInit();
}
