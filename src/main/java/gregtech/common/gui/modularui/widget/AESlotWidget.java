package gregtech.common.gui.modularui.widget;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;

import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import appeng.client.render.AppEngRenderItem;
import appeng.core.AELog;
import appeng.util.Platform;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AESlotWidget extends SlotWidget {

    public AESlotWidget(BaseSlot slot) {
        super(slot);
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
