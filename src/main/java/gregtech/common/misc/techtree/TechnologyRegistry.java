package gregtech.common.misc.techtree;

import java.util.Collection;
import java.util.HashMap;

import gregtech.common.misc.techtree.interfaces.ITechnology;

public class TechnologyRegistry {

    private static final HashMap<String, ITechnology> registeredTechs = new HashMap<>();

    /**
     * Check if a technology exists
     *
     * @param name The name of the technology to look up
     * @return True if the technology exists and is registered, false otherwise
     */
    public static boolean technologyExists(String name) {
        return registeredTechs.containsKey(name);
    }

    /**
     * Register a new technology into the system
     *
     * @param tech The technology to register.
     * @throws RuntimeException if there already is a technology with the same internal name
     */
    public static void register(ITechnology tech) {
        if (technologyExists(tech.getInternalName())) {
            throw new RuntimeException("Technology with internal name" + tech.getInternalName() + " already exists.");
        }
        registeredTechs.put(tech.getInternalName(), tech);
    }

    /**
     * @return A list of all registered technologies
     */
    public static Collection<ITechnology> getTechnologies() {
        return registeredTechs.values();
    }
}
