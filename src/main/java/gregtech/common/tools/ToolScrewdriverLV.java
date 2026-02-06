package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public class ToolScrewdriverLV extends ToolScrewdriver {

    @Override
    public int getToolDamagePerContainerCraft() {
        return 200;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.Steel.mRGBa;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.toolHeadScrewdriver
                .getTextureIndex()]
            : Textures.ItemIcons.HANDLE_ELECTRIC_SCREWDRIVER;
    }

}
