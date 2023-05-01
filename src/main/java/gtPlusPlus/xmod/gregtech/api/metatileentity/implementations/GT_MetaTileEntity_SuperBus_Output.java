package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.inventory.IInventory;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.extensions.ArrayExt;
import gtPlusPlus.core.lib.CORE;

public class GT_MetaTileEntity_SuperBus_Output extends GT_MetaTileEntity_Hatch_OutputBus {

    public GT_MetaTileEntity_SuperBus_Output(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, getSlots(tier));
    }

    public GT_MetaTileEntity_SuperBus_Output(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, getSlots(tier), description, textures);
    }

    /**
     * Returns a factor of 16 based on tier.
     *
     * @param aTier The tier of this bus.
     * @return (1 + aTier) * 16
     */
    public static int getSlots(int aTier) {
        return (1 + aTier) * 16;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_SuperBus_Output(
                this.mName,
                this.mTier,
                ArrayExt.of(this.mDescription),
                this.mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            this.fillStacksIntoFirstSlots();
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    public void updateSlots() {
        for (int i = 0; i < this.mInventory.length; ++i) {
            if (this.mInventory[i] != null && this.mInventory[i].stackSize <= 0) {
                this.mInventory[i] = null;
            }
        }
        this.fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        for (int i = 0; i < this.mInventory.length; ++i) {
            for (int j = i + 1; j < this.mInventory.length; ++j) {
                if (this.mInventory[j] != null && (this.mInventory[i] == null
                        || GT_Utility.areStacksEqual(this.mInventory[i], this.mInventory[j]))) {
                    GT_Utility.moveStackFromSlotAToSlotB(
                            (IInventory) this.getBaseMetaTileEntity(),
                            (IInventory) this.getBaseMetaTileEntity(),
                            j,
                            i,
                            (byte) 64,
                            (byte) 1,
                            (byte) 64,
                            (byte) 1);
                }
            }
        }
    }

    @Override
    public String[] getDescription() {
        String[] aDesc = new String[] { "Item Output for Multiblocks", "" + getSlots(this.mTier) + " Slots",
                CORE.GT_Tooltip.get() };
        return aDesc;
    }

    @Override
    public void addUIWidgets(Builder builder, UIBuildContext buildContext) {
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int row = 0; row * 4 < inventoryHandler.getSlots() - 1; row++) {
            int columnsToMake = Math.min(inventoryHandler.getSlots() - row * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.widget(
                        new SlotWidget(inventoryHandler, row * 4 + column).setPos(column * 18, row * 18)
                                .setSize(18, 18));
            }
        }
        builder.widget(scrollable.setSize(18 * 4 + 4, 18 * 4).setPos(52, 7));
    }
}
