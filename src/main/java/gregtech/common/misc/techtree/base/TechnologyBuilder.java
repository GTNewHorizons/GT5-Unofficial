package gregtech.common.misc.techtree.base;

import java.util.ArrayList;
import java.util.Arrays;

import gregtech.common.misc.techtree.TechList;
import gregtech.common.misc.techtree.TechnologyRegistry;
import gregtech.common.misc.techtree.interfaces.IPrerequisite;

/**
 * Helper class to more easily construct researchable technologies and add them to the registry
 *
 * @author NotAPenguin0
 */
public class TechnologyBuilder {

    private String internalName;
    private String unlocalizedname = "";
    private IPrerequisite[] prereqs = new IPrerequisite[] {};

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
     * Set prerequisite technologies for this technology
     *
     * @param prereqs List of prerequisites, required before researching this technology
     */
    public TechnologyBuilder prerequisites(IPrerequisite... prereqs) {
        this.prereqs = prereqs;
        return this;
    }

    /**
     * Builds the technology object and registers it to the global technology registry.
     *
     * @return The researchable tech, so you can store it for easy referencing
     */
    public Technology buildAndRegister() {
        // If no prerequisite technology exists, add the hidden root tech as a prerequisite
        boolean foundTech = false;
        for (IPrerequisite prereq : this.prereqs) {
            if (prereq.getTechnology() != null) {
                foundTech = true;
                break;
            }
        }
        if (!foundTech) {
            // Add root tech to the list
            ArrayList<IPrerequisite> prereqs = new ArrayList<>(Arrays.asList(this.prereqs));
            prereqs.add(TechList.HiddenRoot);
            this.prereqs = prereqs.toArray(new IPrerequisite[] {});
        }
        Technology tech = new Technology(this.internalName, this.unlocalizedname, this.prereqs);
        TechnologyRegistry.register(tech);
        return tech;
    }
}
