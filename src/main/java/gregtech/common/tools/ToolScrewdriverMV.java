package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.items.MetaGeneratedTool;

public class ToolScrewdriverMV extends ToolScrewdriverLV {

    @Override
    public int getToolDamagePerContainerCraft() {
        return 3200;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.Aluminium.mRGBa;
    }

}
