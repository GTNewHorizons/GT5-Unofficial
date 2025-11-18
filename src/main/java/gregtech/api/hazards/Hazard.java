package gregtech.api.hazards;

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
}
