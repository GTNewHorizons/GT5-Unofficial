package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.material.MU;

public class ToolBuzzSawHV extends ToolBuzzSawLV {

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MU.rgba(Materials2Materials.StainlessSteel);
    }

}
