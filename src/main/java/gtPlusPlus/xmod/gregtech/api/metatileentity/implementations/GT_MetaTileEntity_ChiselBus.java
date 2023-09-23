package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;

public class GT_MetaTileEntity_ChiselBus extends GT_MetaTileEntity_Hatch_InputBus implements IAddUIWidgets {

    public GT_MetaTileEntity_ChiselBus(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier);
    }

    public GT_MetaTileEntity_ChiselBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier), aDescription, aTextures);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < getSlots(this.mTier);
    }

    public static int getSlots(int aTier) {
        return (1 + aTier) * 16 + 1;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ChiselBus(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean allowSelectCircuit() {
        return false;
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Item Input Bus for Industrial Chisel", getSlots(this.mTier) - 1 + " + 1 " + " Slots",
                "Added by: " + EnumChatFormatting.AQUA
                        + "Quetz4l"
                        + " - "
                        + EnumChatFormatting.RED
                        + "[GT++]"
                        + EnumChatFormatting.RESET };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        int slotIndex = 0;
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int row = 0; row * 4 < inventoryHandler.getSlots() - 1; row++) {
            int columnsToMake = Math.min(inventoryHandler.getSlots() - row * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.widget(
                        new SlotWidget(inventoryHandler, slotIndex++).setPos(column * 18, row * 18).setSize(18, 18));

            }
        }

        builder.widget(scrollable.setSize(18 * 4 + 4, 18 * 4).setPos(52, 7)); // main slots
        builder.widget(new SlotWidget(inventoryHandler, slotIndex).setPos(18, 18).setSize(18, 18)); // slot for target
    }

}
