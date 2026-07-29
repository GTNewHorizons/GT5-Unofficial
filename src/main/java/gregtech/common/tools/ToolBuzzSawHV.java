package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.material.MaterialUtils;

public class ToolBuzzSawHV extends ToolBuzzSawLV {

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? MaterialUtils.rgba(MetaGeneratedTool.getPrimaryMaterialML(aStack))
            : MaterialUtils.rgba(Materials2Materials.StainlessSteel);
    }

}
