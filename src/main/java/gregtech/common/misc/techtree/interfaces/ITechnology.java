package gregtech.common.misc.techtree.interfaces;

import java.util.Collection;

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

    /**
     * Adds a technology as a child technology of this technology. Note that this should NEVER be called without making
     * sure the reverse relation is also satisfied. Essentially, do not call this outside of the Technology constructor.
     * 
     * @param tech The technology to add as a child
     */
    void addChildTechnology(ITechnology tech);

    /**
     * @return The depth of this technology in the technology tree.
     *         Zero means this technology has no ancestors.
     */
    int getDepth();

    /**
     * @return A collection of required prerequisites for this technology to be researchable
     */
    Collection<IPrerequisite> getPrerequisites();

    /**
     * @return A collection of child technologies of this technology. All these child technologies
     *         require this technology (and possibly more) to unlock.
     */
    Collection<ITechnology> getChildren();
}
