package gregtech.api.interfaces;

/**
 * Classes implementing this interface can act as cleanroom, used to satisfy environment required for some recipes.
 */
public interface ICleanroom {

    /**
     * @return Current cleanness of this cleanroom. Max at 10000.
     */
    int getCleanness();

    /**
     * @return If this cleanroom is valid.
     */
    boolean isValidCleanroom();

    /**
     * Release pollution to this cleanroom.
     */
    void pollute();
}
