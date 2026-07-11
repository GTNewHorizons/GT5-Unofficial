package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.material.MU;

public class ToolJackHammerHV extends ToolJackHammerLV {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 800;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 1600;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 12800;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 3200;
    }

    @Override
    public float getSpeedMultiplier() {
        return 9.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MU.rgba(Materials2Materials.StainlessSteel);
    }

}
