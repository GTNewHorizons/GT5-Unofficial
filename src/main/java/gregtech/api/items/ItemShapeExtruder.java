package gregtech.api.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemShapeExtruder extends GTGenericItem {

    private static final List<ItemShapeExtruder> instances = new ArrayList<>();
    public static final List<ItemShapeExtruder> shapes = Collections.unmodifiableList(instances);

    public ItemShapeExtruder(String aUnlocalizedOutput) {
        super(
            "shape_extruder." + aUnlocalizedOutput,
            "shape_extruder." + aUnlocalizedOutput + ".name",
            "shape_extruder." + aUnlocalizedOutput + ".tooltip");
        instances.add(this);
    }
}
