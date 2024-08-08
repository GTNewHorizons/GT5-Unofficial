package gregtech.common.tileentities.machines.multi.fuelboilers;

import static gregtech.api.GregTech_API.*;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;

/**
 * The base class for fuel-based boilers. These burn fuels (like Benzene or Diesel) into Steam, instead of direct
 * combustion - in exchange for the extra step, they burn more efficiently at 1 EU to 2.4L steam.
 */
public abstract class FueledBoiler<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    protected FueledBoiler(int id, String name, String localizedName) {
        super(id, name, localizedName);
    }

    public FueledBoiler(String name) {
        super(name);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true; // No part needed to operate, always return true
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    protected static int getTierCasing(Block b, int meta) {
        if (b == sBlockCasings1 && meta == 10) return 1; // Bronze
        if (b == sBlockCasings2 && meta == 0) return 2; // Steel
        // TODO: Stainless steel, Inconel, Incoloy
        return 0;
    }

    protected static int getTierPipe(Block b, int meta) {
        if (b == sBlockCasings2 && meta == 12) return 1; // Bronze
        if (b == sBlockCasings2 && meta == 13) return 2; // Steel
        // TODO: Stainless steel, Inconel, Incoloy
        return 0;
    }

    protected static int getTierHotplate(Block b, int meta) {
        if (b == sBlockMetal1 && meta == 3) return 2; // Annealed Copper
        // TODO: Stainless steel, Inconel, Incoloy? others?
        return 0;
    }

    protected static int getTierFirebox(Block b, int meta) {
        if (b == sBlockCasings3 && meta == 13) return 1; // Bronze
        if (b == sBlockCasings3 && meta == 14) return 2; // Steel
        // TODO: Stainless steel, Inconel, Incoloy
        return 0;
    }
}
