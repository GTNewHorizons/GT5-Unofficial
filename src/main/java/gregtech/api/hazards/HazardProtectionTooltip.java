package gregtech.api.hazards;

import java.util.HashSet;
import java.util.Set;

public class HazardProtectionTooltip {

    public static String FULL_PROTECTION_TRANSLATION_KEY = "GT5U.providesfullhazmatprotection";
    public static String CBRN_TRANSLATION_KEY = "GT5U.providesCBRNprotection";
    public static String EXTREME_TEMP_TRANSLATION_KEY = "GT5U.providesextremetemperatureprotection";

    public static String singleHazardTranslationKey(Hazard hazard) {
        return switch (hazard) {
            case BIOLOGICAL -> "GT5U.providesbiologicalhazmatprotection";
            case FROST -> "GT5U.providesfrosthazmatprotection";
            case HEAT -> "GT5U.providesheathazmatprotection";
            case RADIOLOGICAL -> "GT5U.providesradiohazmatprotection";
            case ELECTRICAL -> "GT5U.provideselectricalhazmatprotection";
            case GAS -> "GT5U.providesgashazmatprotection";
            case SPACE -> "tooltip.spacesuit";
        };
    }

    public static final Set<Hazard> CBRN_HAZARDS = cbrnHazards();
    public static final Set<Hazard> TEMPERATURE_HAZARDS = temperatureHazards();

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
}
