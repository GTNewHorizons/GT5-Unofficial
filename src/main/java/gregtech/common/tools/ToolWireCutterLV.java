package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.material.MU;

public class ToolWireCutterLV extends ToolWireCutter {

    @Override
    public float getBaseDamage() {
        return 1.0F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 2.0F;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : MU.rgba(Materials2Materials.Steel);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool
                .getPrimaryMaterial(aStack).mIconSet.mTextures[MaterialIconRegistry.IconType.TOOL_HEAD_ELECTRIC_SNIPS
                    .ordinal()]
            : Textures.ItemIcons.POWER_UNIT_LV;
    }

}
