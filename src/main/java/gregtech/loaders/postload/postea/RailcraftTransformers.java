package gregtech.loaders.postload.postea;

import static gregtech.loaders.postload.postea.Transformers.replaceItem;

import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialParts;

final class RailcraftTransformers {

    private RailcraftTransformers() {}

    static void register() {
        registerItems();
    }

    // spotless:off
    private static void registerItems() {
        replaceItem("Railcraft:fluid.creosote.cell", 0, MaterialParts.cell(Materials.Creosote, 1));
    }
    // spotless:on
}
