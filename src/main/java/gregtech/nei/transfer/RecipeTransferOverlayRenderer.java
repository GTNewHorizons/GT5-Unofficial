package gregtech.nei.transfer;

import java.util.Map;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.guihook.GuiContainerManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Draws the ghost stacks of a recipe on the machine slots they would go to. */
@SideOnly(Side.CLIENT)
class RecipeTransferOverlayRenderer implements IRecipeOverlayRenderer {

    private final Map<Slot, ItemStack> ghosts;

    RecipeTransferOverlayRenderer(Map<Slot, ItemStack> ghosts) {
        this.ghosts = ghosts;
    }

    @Override
    public void renderOverlay(GuiContainerManager gui, Slot slot) {
        ItemStack ghost = ghosts.get(slot);
        if (ghost == null || slot.getHasStack()) return;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_LIGHTING_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GuiContainerManager.drawItem(slot.xDisplayPosition, slot.yDisplayPosition, ghost);
        NEIClientUtils.gl2DRenderContext(
            () -> GuiDraw.drawRect(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, 0x66555555));
        GL11.glPopAttrib();
    }
}
