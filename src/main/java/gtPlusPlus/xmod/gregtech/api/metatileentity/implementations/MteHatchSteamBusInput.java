package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.lib.GTPPCore;

public class MteHatchSteamBusInput extends MTEHatchInputBus {

    public RecipeMap<?> mRecipeMap = null;

    public MteHatchSteamBusInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier) + 1);
    }

    public MteHatchSteamBusInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier) + 1, aDescription, aTextures);
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            fillStacksIntoFirstSlots();
        }
    }

    @Override
    public void updateSlots() {
        for (int i = 0; i < mInventory.length - 1; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        if (!disableSort) fillStacksIntoFirstSlots();
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

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[14][17][];
        for (byte c = -1; c < 16; c++) {
            if (rTextures[0][c + 1] == null) rTextures[0][c + 1] = getSideFacingActive(c);
            if (rTextures[1][c + 1] == null) rTextures[1][c + 1] = getSideFacingInactive(c);
            if (rTextures[2][c + 1] == null) rTextures[2][c + 1] = getFrontFacingActive(c);
            if (rTextures[3][c + 1] == null) rTextures[3][c + 1] = getFrontFacingInactive(c);
            if (rTextures[4][c + 1] == null) rTextures[4][c + 1] = getTopFacingActive(c);
            if (rTextures[5][c + 1] == null) rTextures[5][c + 1] = getTopFacingInactive(c);
            if (rTextures[6][c + 1] == null) rTextures[6][c + 1] = getBottomFacingActive(c);
            if (rTextures[7][c + 1] == null) rTextures[7][c + 1] = getBottomFacingInactive(c);
            if (rTextures[8][c + 1] == null) rTextures[8][c + 1] = getBottomFacingPipeActive(c);
            if (rTextures[9][c + 1] == null) rTextures[9][c + 1] = getBottomFacingPipeInactive(c);
            if (rTextures[10][c + 1] == null) rTextures[10][c + 1] = getTopFacingPipeActive(c);
            if (rTextures[11][c + 1] == null) rTextures[11][c + 1] = getTopFacingPipeInactive(c);
            if (rTextures[12][c + 1] == null) rTextures[12][c + 1] = getSideFacingPipeActive(c);
            if (rTextures[13][c + 1] == null) rTextures[13][c + 1] = getSideFacingPipeInactive(c);
        }
        return rTextures;
    }

    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP) };
    }

    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP) };
    }

    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM) };
    }

    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM) };
    }

    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[] {
            new GTRenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[] {
            new GTRenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[] {
            new GTRenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[] {
            new GTRenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

//    @Override
//    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
//        getBaseMetaTileEntity().add2by2Slots(builder);
//    }
}
