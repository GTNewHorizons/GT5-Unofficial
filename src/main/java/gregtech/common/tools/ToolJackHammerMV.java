package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.items.MetaGeneratedTool;

public class ToolJackHammerMV extends ToolJackHammerLV {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 200;
    }

    @Override
    public float getBaseDamage() {
        return 2.5F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 6.0F;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.Aluminium.mRGBa;
    }

}
