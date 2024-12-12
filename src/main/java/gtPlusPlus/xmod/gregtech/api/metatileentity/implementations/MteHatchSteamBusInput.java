package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gtPlusPlus.core.lib.GTPPCore;

public class MteHatchSteamBusInput extends MTEHatchInputBus {

    public RecipeMap<?> mRecipeMap = null;

    public MteHatchSteamBusInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier) + 1);
    }

    public MteHatchSteamBusInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier) + 1, aDescription, aTextures);
    }

    public static int getSlots(int aTier) {
        return MTEHatch.getSlots(aTier + 1);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Item Input for Steam Multiblocks",
            "Shift + right click with screwdriver to toggle automatic item shuffling", "Capacity: 4 stacks",
            "Does not work with non-steam multiblocks", GTPPCore.GT_Tooltip.get() };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MteHatchSteamBusInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public int getCircuitSlot() {
        return getSlots(mTier);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }
}
