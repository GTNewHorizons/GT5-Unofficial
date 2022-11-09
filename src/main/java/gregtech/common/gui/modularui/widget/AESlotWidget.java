package gregtech.common.gui.modularui.widget;

import appeng.client.render.AppEngRenderItem;
import appeng.core.AELog;
import appeng.util.Platform;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;
import net.minecraft.network.PacketBuffer;

public class AESlotWidget extends SlotWidget {

    public AESlotWidget(BaseSlot slot) {
        super(slot);
    }

    @Override
    public void detectAndSendChanges(boolean init) {
        super.detectAndSendChanges(init);
        if (getMcSlot().getHasStack()) {
            syncToClient(99, buffer -> buffer.writeVarIntToBuffer(getMcSlot().getStack().stackSize));
        }
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) {
        super.readOnClient(id, buf);
        if (id == 99) {
            if (getMcSlot().getHasStack()) {
                getMcSlot().getStack().stackSize = buf.readVarIntFromBuffer();
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void drawSlot(Slot slotIn) {
        final AppEngRenderItem aeRenderItem = new AppEngRenderItem();
        final RenderItem pIR = this.setItemRender(aeRenderItem);
        try {
            aeRenderItem.setAeStack(Platform.getAEStackInSlot(slotIn));
            super.drawSlot(slotIn, false);
        } catch (final Exception err) {
            AELog.warn("[AppEng] AE prevented crash while drawing slot: " + err);
        }
        this.setItemRender(pIR);
    }

    @SideOnly(Side.CLIENT)
    private RenderItem setItemRender(final RenderItem item) {
        final RenderItem ri = ModularGui.getItemRenderer();
        ModularGui.setItemRenderer(item);
        return ri;
    }
}
