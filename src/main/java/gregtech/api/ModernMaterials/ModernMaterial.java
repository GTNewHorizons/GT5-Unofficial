package gregtech.api.ModernMaterials;

import gregtech.api.ModernMaterials.PartProperties.BlockGenericProperties;
import gregtech.api.ModernMaterials.PartProperties.BlockPipeProperties;
import gregtech.api.ModernMaterials.PartProperties.BlockWirePartsProperties;
import gregtech.api.ModernMaterials.PartProperties.ItemPartsProperties;

public class ModernMaterial {

    public int getMaterialID() {
        return materialID;
    }

    public String getMaterialName() {
        return materialName;
    }

    private int materialID;
    private static int materialIDTracker;

    private String materialName;

    public final ItemPartsProperties itemPartsProperties = new ItemPartsProperties(false);
    public final BlockPipeProperties blockPipeProperties = new BlockPipeProperties(false);
    public final BlockWirePartsProperties blockWirePartsProperties = new BlockWirePartsProperties(false);
    public final BlockGenericProperties blockGenericProperties = new BlockGenericProperties(false);

    public ModernMaterial(final String materialName) {
        this.materialID = materialIDTracker++;
        this.materialName = materialName;
    }

}
