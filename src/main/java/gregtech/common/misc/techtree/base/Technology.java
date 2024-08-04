package gregtech.common.misc.techtree.base;

import net.minecraft.util.StatCollector;

import gregtech.common.misc.techtree.interfaces.ITechnology;

/**
 * @author NotAPenguin0
 */
public class Technology implements ITechnology {

    /**
     * Internal name of the technology.
     */
    private String internalName;
    /**
     * Unlocalized name of the technology
     */
    private String unlocalizedName;

    /**
     * Create a new researchable technology
     * 
     * @param name            The internal name of the technology
     * @param unlocalizedName The unlocalized name of the technology
     */
    public Technology(String name, String unlocalizedName) {
        this.internalName = name;
        this.unlocalizedName = unlocalizedName;
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
}
