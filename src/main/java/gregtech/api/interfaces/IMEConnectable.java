package gregtech.api.interfaces;

/**
 * A hatch that has a connection to an ME cable. Used by the matter manipulator to copy this setting.
 */
public interface IMEConnectable {

    /** If this hatch can accept connects on the side, or just the front. */
    boolean connectsToAllSides();

    void setConnectsToAllSides(boolean connects);
}
