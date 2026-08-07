package gregtech.api.hazards;

import java.util.HashSet;
import java.util.Set;

/**
 * Categories that can be checked for "Hazmat suit" type checks. Other mods may add their own Hazmat/protective gear
 * that interacts with the relevant checks in GregTech
 */
public enum Hazard {

    BIOLOGICAL,
    FROST,
    HEAT,
    RADIOLOGICAL,
    ELECTRICAL,
    GAS,
    SPACE;

    public static final Hazard[] VALUES = Hazard.values();

    public static final Set<Hazard> CBRN_HAZARDS = cbrnHazards();
    public static final Set<Hazard> TEMPERATURE_HAZARDS = temperatureHazards();
    public static final Set<Hazard> STANDARD_HAZARDS = standardHazards();

    private static HashSet<Hazard> cbrnHazards() {
        HashSet<Hazard> set = new HashSet<>();
        set.add(Hazard.BIOLOGICAL);
        set.add(Hazard.GAS);
        set.add(Hazard.RADIOLOGICAL);
        return set;
    }

    private static HashSet<Hazard> temperatureHazards() {
        HashSet<Hazard> set = new HashSet<>();
        set.add(Hazard.FROST);
        set.add(Hazard.HEAT);
        return set;
    }

    private static HashSet<Hazard> standardHazards() {
        HashSet<Hazard> set = new HashSet<>();
        set.addAll(cbrnHazards());
        set.addAll(temperatureHazards());
        set.add(Hazard.ELECTRICAL);
        return set;
    }
}
