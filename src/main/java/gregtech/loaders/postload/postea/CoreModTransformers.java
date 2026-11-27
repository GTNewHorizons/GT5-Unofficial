package gregtech.loaders.postload.postea;

import static gregtech.loaders.postload.postea.Transformers.replaceItem;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;

final class CoreModTransformers {

    private CoreModTransformers() {}

    static void register() {
        registerItems();
    }

    // spotless:off
    private static void registerItems() {
        // dailies
        replaceItem("dreamcraft:PotassiumHydroxideDust", MaterialLibAPI.getStack(Materials.PotassiumHydroxide, Shapes.dust, 1));
        // 2.8.4 and earlier. The coremod suppresses the FML warning for this id
        replaceItem("dreamcraft:item.PotassiumHydroxideDust", MaterialLibAPI.getStack(Materials.PotassiumHydroxide, Shapes.dust, 1));
        // Extruder shape unification (#8771)
        replaceItem("dreamcraft:ExtruderShapeBoat", ItemList.Shape_Extruder_Boat.get(1));
    }
    // spotless:on
}
