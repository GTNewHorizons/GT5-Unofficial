package gregtech.common.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public class ToolWrenchLV extends ToolWrench {

    @Override
    public float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack,
        EntityPlayer aPlayer) {
        return aOriginalDamage;
    }

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
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.Steel.mRGBa;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.toolHeadWrench
            .getTextureIndex()] : Textures.ItemIcons.POWER_UNIT_LV;
    }

}
