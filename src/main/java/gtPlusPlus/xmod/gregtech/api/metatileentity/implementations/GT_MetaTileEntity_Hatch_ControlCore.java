package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.item.general.ItemControlCore;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_Hatch_ControlCore extends GT_MetaTileEntity_Hatch {

    public GT_Recipe_Map mRecipeMap = null;

    public BlockPos mControllerLocation;

    public GT_MetaTileEntity_Hatch_ControlCore(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                getSlots(aTier),
                "Core determines maximum tier machine will operate at");
    }

    public GT_MetaTileEntity_Hatch_ControlCore(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 9 : 16, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_ControlCore(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 9 : 16, aDescription[0], aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { this.mDescription, CORE.GT_Tooltip.get() };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Control_Core) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Control_Core) };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
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
        return new GT_MetaTileEntity_Hatch_ControlCore(mName, mTier, mDescriptionArray, mTextures);
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
        for (int i = 0; i < mInventory.length; i++)
            for (int j = i + 1; j < mInventory.length; j++) if (mInventory[j] != null
                    && (mInventory[i] == null || GT_Utility.areStacksEqual(mInventory[i], mInventory[j]))) {
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
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == getBaseMetaTileEntity().getFrontFacing()
                && (aStack != null && aStack.getItem() instanceof ItemControlCore);
    }

    public boolean setOwner(TileEntity aTileEntity) {
        if (mControllerLocation != null) {
            return false;
        } else {
            mControllerLocation = new BlockPos(aTileEntity);
            return true;
        }
    }

    public boolean setOwner(IGregTechTileEntity aTileEntity) {
        if (mControllerLocation != null) {
            return false;
        } else {
            mControllerLocation = new BlockPos(aTileEntity);
            return true;
        }
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        getBaseMetaTileEntity().add1by1Slot(builder);
    }
}
