package gregtech.api.items;

public class ItemShapeExtruder extends GTGenericItem {

    public ItemShapeExtruder(String aUnlocalizedOutput, String aEnglishOutput, String aEnglishTooltip,
        boolean defaultTooltip) {
        String englishTooltip;
        if (defaultTooltip) {
            englishTooltip = "Extruder Shape for making " + aEnglishTooltip + "s";
        } else {
            englishTooltip = aEnglishTooltip;
        }
        super("shape_extruder." + aUnlocalizedOutput, "Extruder Shape (" + aEnglishOutput + ")", englishTooltip);
    }

    public ItemShapeExtruder(String aUnlocalizedOutput, String aEnglishOutput, String aEnglishOutputTooltip) {
        this(aUnlocalizedOutput, aEnglishOutput, aEnglishOutputTooltip, true);
    }

    public ItemShapeExtruder(String aUnlocalizedOutput, String aEnglishOutput) {
        this(aUnlocalizedOutput, aEnglishOutput, aEnglishOutput);
    }
}
