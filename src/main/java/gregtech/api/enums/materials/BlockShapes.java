package gregtech.api.enums.materials;

import static gregtech.api.enums.materials.GTShapeStore.reg;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StandardProperties;

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
        block = reg(
            MaterialLibAPI.registerBlockShape(new GTStorageShapeBlock("gregtech", "block", "Block of %s", "block"))
                .setProperty(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.block_of_material"));
        blockCasing = reg(
            MaterialLibAPI
                .registerBlockShape(
                    new GTCasingShapeBlock("gregtech", "blockCasing", "Bolted %s Casing", "blockCasing"))
                .setProperty(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.bolted_material_casing"));
        blockCasingAdvanced = reg(
            MaterialLibAPI.registerBlockShape(
                new GTCasingShapeBlock("gregtech", "blockCasingAdvanced", "Rebolted %s Casing", "blockCasingAdvanced"))
                .setProperty(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.rebolted_material_casing"));
        sheetmetal = reg(
            MaterialLibAPI.registerBlockShape(new SheetmetalShapeBlock("sheetmetal", "%s Sheetmetal", "sheetmetal"))
                .setProperty(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_sheetmetal"));
    }

    private BlockShapes() {}
}
