package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.widget.ElectricSlotWidget;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_Hatch_OutputBattery extends GT_MetaTileEntity_Hatch {

    public GT_MetaTileEntity_Hatch_OutputBattery(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier), "Dischargeable Item Bus for Multiblocks");
    }

    public GT_MetaTileEntity_Hatch_OutputBattery(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 9 : 16, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        int mSlots = 0;
        if (this.mTier == 2) {
            mSlots = 4;
        } else if (this.mTier == 4) {
            mSlots = 16;
        } else {
            mSlots = 16;
        }
        return ArrayUtils.addAll(this.mDescriptionArray, "Capacity: " + mSlots + " slots", CORE.GT_Tooltip.get());
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public long maxEUOutput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512 + V[mTier + 1] * 8;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Discharger) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Discharger) };
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
        return new GT_MetaTileEntity_Hatch_OutputBattery(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing();
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
    public int rechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return 0;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int dechargerSlotCount() {
        return mTier == 2 ? 4 : 16;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            fillStacksIntoFirstSlots();
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            if (aBaseMetaTileEntity.getMetaTileEntity() instanceof MetaTileEntity mMetaTileEntity) {
                if (mMetaTileEntity.dechargerSlotCount() > 0
                        && mMetaTileEntity.getEUVar() < aBaseMetaTileEntity.getEUCapacity()) {
                    for (int i = mMetaTileEntity.dechargerSlotStartIndex(),
                            k = mMetaTileEntity.dechargerSlotCount() + i; i < k; i++) {
                        if (mMetaTileEntity.mInventory[i] != null
                                && mMetaTileEntity.getEUVar() < aBaseMetaTileEntity.getEUCapacity()) {
                            aBaseMetaTileEntity.increaseStoredEnergyUnits(
                                    GT_ModHandler.dischargeElectricItem(
                                            mMetaTileEntity.mInventory[i],
                                            (int) Math.min(
                                                    V[mTier] * 15,
                                                    aBaseMetaTileEntity.getEUCapacity()
                                                            - aBaseMetaTileEntity.getStoredEU()),
                                            (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getInputTier()),
                                            true,
                                            false,
                                            false),
                                    true);
                            if (mMetaTileEntity.mInventory[i].stackSize <= 0) mMetaTileEntity.mInventory[i] = null;
                        }
                    }
                }
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (mTier == 2) {
            for (int i = 0; i < 4; i++) {
                builder.widget(
                        new ElectricSlotWidget(inventoryHandler, i).setPos(70 + (i % 2) * 18, 25 + (i / 2) * 18));
            }
        } else {
            for (int i = 0; i < 16; i++) {
                builder.widget(new ElectricSlotWidget(inventoryHandler, i).setPos(52 + (i % 4) * 18, 7 + (i / 4) * 18));
            }
        }
    }
}
