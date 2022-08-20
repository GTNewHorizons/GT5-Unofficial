package gregtech.common.gui;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import appeng.client.render.AppEngRenderItem;
import appeng.core.AELog;
import appeng.integration.IntegrationRegistry;
import appeng.integration.IntegrationType;
import appeng.integration.abstraction.INEI;
import appeng.util.Platform;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.GT_GUIDialogSelectItem;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.gui.GT_Slot_Holo_ME;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_SetConfigurationCircuit_Bus;
import gregtech.api.util.GT_Utility;

public class GT_GUIContainer_InputBus_ME  extends GT_GUIContainerMetaTile_Machine {

    private final AppEngRenderItem aeRenderItem = new AppEngRenderItem();

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

    // base method is made public by AE2 ASM
    public void func_146977_a( final Slot s )
    {
        this.drawSlot( s );
    }


    @Optional.Method(modid = "appliedenergistics2")
    private RenderItem setItemRender( final RenderItem item )
    {
        if( IntegrationRegistry.INSTANCE.isEnabled( IntegrationType.NEI ) )
        {
            return ( (INEI) IntegrationRegistry.INSTANCE.getInstance( IntegrationType.NEI ) ).setItemRender( item );
        }
        else
        {
            final RenderItem ri = itemRender;
            itemRender = item;
            return ri;
        }
    }

    @Optional.Method(modid = "appliedenergistics2")
    private void drawSlot( final Slot s )
    {
        if (s instanceof GT_Slot_Holo_ME) {
            final RenderItem pIR = this.setItemRender( this.aeRenderItem );
            try
            {
                this.zLevel = 0.0F;
                itemRender.zLevel = 0.0F;

                this.aeRenderItem.setAeStack( Platform.getAEStackInSlot( s ) );

                this.safeDrawSlot( s );
            }
            catch( final Exception err )
            {
                AELog.warn( "[AppEng] AE prevented crash while drawing slot: " + err.toString() );
            }
            this.setItemRender( pIR );
            return;
        }
        safeDrawSlot(s);
    }

    @Optional.Method(modid = "appliedenergistics2")
    private void safeDrawSlot( final Slot s )
    {
        try
        {
            GuiContainer.class.getDeclaredMethod( "func_146977_a_original", Slot.class ).invoke( this, s );
        }
        catch( final Exception err )
        {
        }
    }
}
