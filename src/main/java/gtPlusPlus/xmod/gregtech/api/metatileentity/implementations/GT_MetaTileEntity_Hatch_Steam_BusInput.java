package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.GT_Mod;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;

public class GT_MetaTileEntity_Hatch_Steam_BusInput extends GT_MetaTileEntity_Hatch {

    public RecipeMap<?> mRecipeMap = null;
    public boolean disableSort;

    public GT_MetaTileEntity_Hatch_Steam_BusInput(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                getSlots(aTier),
                new String[] { "Item Input for Steam Multiblocks",
                        "Shift + right click with screwdriver to toggle automatic item shuffling", "Capacity: 4 stacks",
                        "Does not work with non-steam multiblocks", CORE.GT_Tooltip.get() });
    }

    public GT_MetaTileEntity_Hatch_Steam_BusInput(String aName, int aTier, String aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_Steam_BusInput(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
                ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN) }
                : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
                ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN) }
                : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Steam_BusInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            fillStacksIntoFirstSlots();
        }
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        if (disableSort) {
            for (int i = 0; i < mInventory.length; i++)
                if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        } else {
            for (int i = 0; i < mInventory.length; i++)
                for (int j = i + 1; j < mInventory.length; j++) if (mInventory[j] != null
                        && (mInventory[i] == null || GT_Utility.areStacksEqual(mInventory[i], mInventory[j])))
                    GT_Utility.moveStackFromSlotAToSlotB(
                            getBaseMetaTileEntity(),
                            getBaseMetaTileEntity(),
                            j,
                            i,
                            (byte) 64,
                            (byte) 1,
                            (byte) 64,
                            (byte) 1);
        }
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
            GT_Utility.sendChatToPlayer(
                    aPlayer,
                    GT_Utility.trans("200.1", "Automatic Item Shuffling: ")
                            + (disableSort ? GT_Utility.trans("087", "Disabled") : GT_Utility.trans("088", "Enabled")));
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return side == getBaseMetaTileEntity().getFrontFacing()
                && (mRecipeMap == null || mRecipeMap.containsInput(aStack));
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
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP) };
    }

    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP) };
    }

    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM
                        : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM) };
    }

    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM
                        : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM) };
    }

    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[] {
                new GT_RenderedTexture(
                        mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM
                                : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[] {
                new GT_RenderedTexture(
                        mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM
                                : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        getBaseMetaTileEntity().add2by2Slots(builder);
    }
}
