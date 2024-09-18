package gregtech.common.tileentities.machines;

import static gregtech.api.enums.GTValues.AuthorEvgenWarGold;
import static gregtech.api.enums.GTValues.V;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.Overlay_Charging;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.api.gui.widget.ElectricSlotWidget;

public class MTEHatchChargingBus extends MTEHatchInputBus {

    public MTEHatchChargingBus(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier));
    }

    public MTEHatchChargingBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier), aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchChargingBus(this.mName, this.mTier, mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Item Charger Bus for Tree Growth Simulator", getSlots(this.mTier) + " Slots",
            "Transfer EU : MaxInputEU - ActualUsage", "Author: " + AuthorEvgenWarGold };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            fillStacksIntoFirstSlots();
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            if (aBaseMetaTileEntity.getMetaTileEntity() instanceof MetaTileEntity mMetaTileEntity) {
                if (mMetaTileEntity.rechargerSlotCount() > 0 && aBaseMetaTileEntity.getStoredEU() > 0) {
                    this.onSetActive(true);

                    for (int i = mMetaTileEntity.rechargerSlotStartIndex(),
                        k = mMetaTileEntity.rechargerSlotCount() + i; i < k; i++) {
                        if (aBaseMetaTileEntity.getStoredEU() > 0 && mMetaTileEntity.mInventory[i] != null) {
                            for (int u = 0; u < 10; u++) {
                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(
                                    GTModHandler.chargeElectricItem(
                                        mMetaTileEntity.mInventory[i],
                                        (int) Math.min(V[this.mTier] * 16, aBaseMetaTileEntity.getStoredEU()),
                                        Integer.MAX_VALUE,
                                        false,
                                        false),
                                    true);
                                if (mMetaTileEntity.mInventory[i].stackSize <= 0) {
                                    mMetaTileEntity.mInventory[i] = null;
                                }
                            }
                        }
                    }
                } else {
                    this.onSetActive(false);
                }
            }
        }

        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public long maxEUInput() {
        return 8192;
    }

    @Override
    public int rechargerSlotCount() {
        return getSlots(this.mTier);
    }

    @Override
    public long maxAmperesIn() {
        return getSlots(this.mTier);
    }

    @Override
    public long maxEUStore() {
        return maxEUInput() * 16;
    }

    @Override
    public boolean allowSelectCircuit() {
        return false;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(Overlay_Charging) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(Overlay_Charging) };
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        for (int i = 0; i < mInventory.length; i++)
            for (int j = i + 1; j < mInventory.length; j++) if (mInventory[j] != null
                && (mInventory[i] == null || GTUtility.areStacksEqual(mInventory[i], mInventory[j]))) {
                    GTUtility.moveStackFromSlotAToSlotB(
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
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        for (int i = 0; i < 16; i++) {
            builder.widget(new ElectricSlotWidget(inventoryHandler, i).setPos(52 + (i % 4) * 18, 7 + (i / 4) * 18));
        }
    }
}
