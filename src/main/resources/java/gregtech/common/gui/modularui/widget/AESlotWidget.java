package gregtech.common.gui.modularui.widget;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import appeng.api.storage.IItemDisplayRegistry.ItemRenderHook;
import appeng.client.render.AppEngRenderItem;
import appeng.core.AELog;
import appeng.util.Platform;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AESlotWidget extends SlotWidget {

    private static class HookHolder {

        static ItemRenderHook SKIP_ITEM_STACK_SIZE_HOOK = new ItemRenderHook() {

            @Override
            public boolean renderOverlay(FontRenderer fr, TextureManager tm, ItemStack is, int x, int y) {
                return true;
            }

            @Override
            public boolean showStackSize(ItemStack is) {
                return false;
            }
        };
    }

    public AESlotWidget(BaseSlot slot) {
        super(slot);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void drawSlot(Slot slotIn) {
        final AppEngRenderItem aeRenderItem = new AppEngRenderItem();
        AppEngRenderItem.POST_HOOKS.add(HookHolder.SKIP_ITEM_STACK_SIZE_HOOK);
        final RenderItem pIR = this.setItemRender(aeRenderItem);
        try {
            aeRenderItem.setAeStack(Platform.getAEStackInSlot(slotIn));
            super.drawSlot(slotIn, true);
        } catch (final Exception err) {
            AELog.warn("[AppEng] AE prevented crash while drawing slot: " + err);
        }
        AppEngRenderItem.POST_HOOKS.remove(HookHolder.SKIP_ITEM_STACK_SIZE_HOOK);
        this.setItemRender(pIR);
    }

    @SideOnly(Side.CLIENT)
    private RenderItem setItemRender(final RenderItem item) {
        final RenderItem ri = ModularGui.getItemRenderer();
        ModularGui.setItemRenderer(item);
        return ri;
    }
}
