package gregtech.api.enums.materials;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.common.blocks.GTCasingShapeBlock;
import gregtech.common.blocks.GTStorageShapeBlock;
import gregtech.common.blocks.SheetmetalShapeBlock;

/// Simple block shapes. More advanced TE-holding ones are set in [TEBlockShapes].
public class BlockShapes {

    public static Shape block;
    public static Shape blockCasing;
    public static Shape blockCasingAdvanced;
    public static Shape sheetmetal;

    public static void init() {
        block = MaterialLibAPI.registerBlockShape(new GTStorageShapeBlock("gregtech", "block", "Block of %s", "block"));
        blockCasing = MaterialLibAPI
            .registerBlockShape(new GTCasingShapeBlock("gregtech", "blockCasing", "Bolted %s Casing", "blockCasing"));
        blockCasingAdvanced = MaterialLibAPI.registerBlockShape(
            new GTCasingShapeBlock("gregtech", "blockCasingAdvanced", "Rebolted %s Casing", "blockCasingAdvanced"));
        sheetmetal = MaterialLibAPI
            .registerBlockShape(new SheetmetalShapeBlock("sheetmetal", "%s Sheetmetal", "sheetmetal"));
    }

    private BlockShapes() {}
}
