package gregtech.common.misc.techtree.interfaces;

/**
 * Represents
 * 
 * @author NotAPenguin0
 */
public interface ITechnology {

    /**
     * @return The internal name of the technology
     */
    String getInternalName();

    /**
     * @return The unlocalized name of the technology
     */
    String getUnlocalizedName();

    /**
     * @return The localized name of the technology
     */
    String getLocalizedName();
}
