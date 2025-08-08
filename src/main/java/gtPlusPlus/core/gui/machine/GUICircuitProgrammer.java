package gtPlusPlus.core.gui.machine;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.container.ContainerCircuitProgrammer;
import gtPlusPlus.core.tileentities.general.TileEntityCircuitProgrammer;

@SideOnly(Side.CLIENT)
public class GUICircuitProgrammer extends GuiContainer {

    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(
        GTPlusPlus.ID,
        "textures/gui/CircuitProgrammer.png");

    public GUICircuitProgrammer(final InventoryPlayer player_inventory, final TileEntityCircuitProgrammer te) {
        super(new ContainerCircuitProgrammer(player_inventory, te));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int i, final int j) {
        super.drawGuiContainerForegroundLayer(i, j);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(craftingTableGuiTextures);
        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    // This method is called when the Gui is first called!
    @Override
    public void initGui() {
        super.initGui();
    }
}
