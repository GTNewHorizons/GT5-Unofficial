package gregtech.api.items;

public class ItemShapeExtruder extends GTGenericItem {

    public ItemShapeExtruder(String aUnlocalizedOutput, String aEnglishOutput, String aEnglishOutputTooltip) {
        super(
            "shape_extruder_" + aUnlocalizedOutput,
            "Extruder Shape (" + aEnglishOutput + ")",
            "Extruder Shape for making " + aEnglishOutputTooltip + "s");
    }

    public ItemShapeExtruder(String aUnlocalizedOutput, String aEnglishOutput) {
        this(aUnlocalizedOutput, aEnglishOutput, aEnglishOutput);
    }
}
