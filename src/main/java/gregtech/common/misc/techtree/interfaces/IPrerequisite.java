package gregtech.common.misc.techtree.interfaces;

import java.util.UUID;

/**
 * Represents a prerequisite for a technology to be researchable. Typically, this is another technology,
 * but it could also be something completely different such as a space project being built.
 */
public interface IPrerequisite {

    /**
     * Check if this prerequisite is satisfied for the player with given UUID
     * 
     * @return True if it was satisfied, false otherwise.
     */
    boolean isSatisfied(UUID playerUuid);

    /**
     * Get the technology associated with this prerequisite technology. If this is not null, this technology
     * will be used to draw dependencies in the technology tree.
     * 
     * @return If this prerequisite is linked to a technology, return it. Otherwise, return null.
     */
    default ITechnology getTechnology() {
        return null;
    }
}
