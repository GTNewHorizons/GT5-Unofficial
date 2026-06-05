package gregtech.api.hazards;

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
}
