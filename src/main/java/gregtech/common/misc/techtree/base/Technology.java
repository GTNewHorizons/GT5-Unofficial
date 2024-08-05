package gregtech.common.misc.techtree.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import net.minecraft.util.StatCollector;

import gregtech.common.misc.techtree.interfaces.IPrerequisite;
import gregtech.common.misc.techtree.interfaces.ITechnology;

/**
 * @author NotAPenguin0
 */
public class Technology implements ITechnology, IPrerequisite {

    /**
     * Internal name of the technology.
     */
    private final String internalName;
    /**
     * Unlocalized name of the technology
     */
    private final String unlocalizedName;

    /**
     * Prerequisite technologies required to research this technology.
     */
    private final IPrerequisite[] prerequisites;

    /**
     * Depth of the technology
     */
    private int depth = 0;

    /**
     * Technologies of which this technology is a prerequisite
     */
    private final ArrayList<ITechnology> children = new ArrayList<>();

    /**
     * Create a new researchable technology
     *
     * @param name            The internal name of the technology
     * @param unlocalizedName The unlocalized name of the technology
     * @param prerequisites   The technologies required to unlock this technology. This cannot be changed afterwards.
     */
    public Technology(String name, String unlocalizedName, IPrerequisite[] prerequisites) {
        this.internalName = name;
        this.unlocalizedName = unlocalizedName;
        this.prerequisites = prerequisites;

        // Calculate depth based on depth of prerequisite technologies.
        // The depth into the tree is one more than the depth of the deepest prerequisite technology.
        // Note that we can do this, because you cannot add prerequisite technologies after construction,
        // so the depth of all prerequisite techs is final once we reach this constructor.
        for (IPrerequisite prereq : prerequisites) {
            // If this prerequisite is a technology, modify depth and children for it.
            ITechnology tech = prereq.getTechnology();
            if (tech != null) {
                depth = Math.max(depth, tech.getDepth() + 1);
                // Also store this technology as a child tech of each prerequisite, since this makes graph traversal
                // a lot easier
                tech.addChildTechnology(this);
            }
        }
    }

    @Override
    public void addChildTechnology(ITechnology tech) {
        this.children.add(tech);
    }

    @Override
    public String getInternalName() {
        return internalName;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(getUnlocalizedName());
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public Collection<IPrerequisite> getPrerequisites() {
        return Arrays.asList(this.prerequisites);
    }

    @Override
    public Collection<ITechnology> getChildren() {
        return children;
    }

    @Override
    public boolean isSatisfied(UUID playerUuid) {
        return false;
    }

    @Override
    public ITechnology getTechnology() {
        return this;
    }
}
