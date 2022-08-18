package gregtech.common.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.GT_GUIDialogSelectItem;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_SetConfigurationCircuit_Bus;
import gregtech.api.util.GT_Utility;

public class GT_GUIContainer_InputBus_ME  extends GT_GUIContainerMetaTile_Machine {

    public GT_GUIContainer_InputBus_ME(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_InputBus_ME(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/InputBusME.png");
        getContainer().setCircuitSlotClickCallback(this::openSelectCircuitDialog);
    }

    private GT_Container_InputBus_ME getContainer() {
        return (GT_Container_InputBus_ME) mContainer;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    private void onCircuitSelected(ItemStack selected) {
        GT_Values.NW.sendToServer(new GT_Packet_SetConfigurationCircuit_Bus(mContainer.mTileEntity, selected));
        // we will not do any validation on client side
        // it doesn't get to actually decide what inventory contains anyway
        mContainer.mTileEntity.setInventorySlotContents(GT_Container_InputBus_ME.CIRCUIT_SLOT, selected);
    }
    private void openSelectCircuitDialog() {
        List<ItemStack> circuits = GregTech_API.getConfigurationCircuitList(1);
        mc.displayGuiScreen(new GT_GUIDialogSelectItem(
            StatCollector.translateToLocal("GT5U.machines.select_circuit"),
            mContainer.mTileEntity.getMetaTileEntity().getStackForm(0),
            this,
            this::onCircuitSelected,
            circuits,
            GT_Utility.findMatchingStackInList(circuits,
                mContainer.mTileEntity.getStackInSlot(GT_Container_InputBus_ME.CIRCUIT_SLOT))));
    }
}
