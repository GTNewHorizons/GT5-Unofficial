package gregtech.common.misc.techtree.base;

import gregtech.common.misc.techtree.TechnologyRegistry;
import gregtech.common.misc.techtree.interfaces.ITechnology;

/**
 * Helper class to more easily construct researchable technologies and add them to the registry
 * 
 * @author NotAPenguin0
 */
public class TechnologyBuilder {

    private String internalName;
    private String unlocalizedname = "";

    /**
     * Start constructing a new researchable technology with the given internal name
     * 
     * @param internalName Internal name for this technology. This must be globally unique
     */
    public TechnologyBuilder(String internalName) {
        this.internalName = internalName;
    }

    public TechnologyBuilder unlocalizedName(String name) {
        this.unlocalizedname = name;
        return this;
    }

    /**
     * Builds the technology object and registers it to the global technology registry.
     * 
     * @return The researchable tech, so you can store it for easy referencing
     */
    public ITechnology buildAndRegister() {
        ITechnology tech = new Technology(this.internalName, this.unlocalizedname);
        TechnologyRegistry.register(tech);
        return tech;
    }
}
