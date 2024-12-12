package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.lib.GTPPCore;

public class MteHatchSteamBusInput extends MTEHatchInputBus {

    public RecipeMap<?> mRecipeMap = null;

    public MteHatchSteamBusInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier + 1) + 1);
    }

    public MteHatchSteamBusInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier + 1) + 1, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Item Input for Steam Multiblocks",
            "Shift + right click with screwdriver to toggle automatic item shuffling", "Capacity: 4 stacks",
            "Does not work with non-steam multiblocks", GTPPCore.GT_Tooltip.get() };
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MteHatchSteamBusInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("disableSort", disableSort);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        disableSort = aNBT.getBoolean("disableSort");
    }

    @Override
    public int getCircuitSlot() {
        return getSlots(mTier + 1);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            disableSort = !disableSort;
            GTUtility.sendChatToPlayer(
                aPlayer,
                GTUtility.trans("200.1", "Automatic Item Shuffling: ")
                    + (disableSort ? GTUtility.trans("087", "Disabled") : GTUtility.trans("088", "Enabled")));
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }
}
