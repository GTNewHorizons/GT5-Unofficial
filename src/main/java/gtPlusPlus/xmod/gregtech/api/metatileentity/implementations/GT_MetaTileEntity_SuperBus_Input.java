package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.extensions.ArrayExt;
import gtPlusPlus.core.lib.CORE;

public class GT_MetaTileEntity_SuperBus_Input extends GT_MetaTileEntity_Hatch_InputBus {

    public GT_MetaTileEntity_SuperBus_Input(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier) + 1);
    }

    public GT_MetaTileEntity_SuperBus_Input(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
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
        return new GT_MetaTileEntity_SuperBus_Input(
                this.mName,
                this.mTier,
                ArrayExt.of(this.mDescription),
                this.mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Item Input for Multiblocks", "" + getSlots(this.mTier) + " Slots",
                CORE.GT_Tooltip.get() };
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
        builder.widget(scrollable.setSize(18 * 4 + 4, 18 * 4).setPos(52, 7));
    }
}
