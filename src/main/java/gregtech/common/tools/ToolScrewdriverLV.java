package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public class ToolScrewdriverLV extends ToolScrewdriver {

    @Override
    public float getMaxDurabilityMultiplier() {
        return 1.0F;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 200;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(
                aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.toolHeadScrewdriver.mTextureIndex]
            : Textures.ItemIcons.HANDLE_ELECTRIC_SCREWDRIVER;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }
}
