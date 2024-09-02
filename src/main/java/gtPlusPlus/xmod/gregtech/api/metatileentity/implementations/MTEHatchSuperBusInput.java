package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gtPlusPlus.core.lib.GTPPCore;

public class MTEHatchSuperBusInput extends MTEHatchInputBus {

    public MTEHatchSuperBusInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier) + 1);
    }

    public MTEHatchSuperBusInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier) + 1, aDescription, aTextures);
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
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchSuperBusInput(this.mName, this.mTier, mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Item Input for Multiblocks", "" + getSlots(this.mTier) + " Slots",
            GTPPCore.GT_Tooltip.get() };
    }

    @Override
    public int getCircuitSlot() {
        return getSlots(mTier);
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
        builder.widget(
            scrollable.setSize(18 * 4 + 4, 18 * 4)
                .setPos(52, 7));
    }
}
