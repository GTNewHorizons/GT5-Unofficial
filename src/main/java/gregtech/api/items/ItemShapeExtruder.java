package gregtech.api.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemShapeExtruder extends GTGenericItem {

    private static final List<ItemShapeExtruder> instances = new ArrayList<>();
    public static final List<ItemShapeExtruder> shapes = Collections.unmodifiableList(instances);

    public ItemShapeExtruder(String aUnlocalizedOutput, String aEnglishOutput, String aEnglishTooltip,
        boolean defaultTooltip) {
        String englishTooltip;
        if (defaultTooltip) {
            englishTooltip = "Extruder Shape for making " + aEnglishTooltip + "s";
        } else {
            englishTooltip = aEnglishTooltip;
        }
        super("shape_extruder." + aUnlocalizedOutput, "Extruder Shape (" + aEnglishOutput + ")", englishTooltip);
        instances.add(this);
    }

    public ItemShapeExtruder(String aUnlocalizedOutput, String aEnglishOutput, String aEnglishOutputTooltip) {
        this(aUnlocalizedOutput, aEnglishOutput, aEnglishOutputTooltip, true);
    }

    public ItemShapeExtruder(String aUnlocalizedOutput, String aEnglishOutput) {
        this(aUnlocalizedOutput, aEnglishOutput, aEnglishOutput);
    }
}
