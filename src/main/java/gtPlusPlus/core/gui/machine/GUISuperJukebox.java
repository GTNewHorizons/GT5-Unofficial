package gtPlusPlus.core.gui.machine;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.block.machine.BlockSuperJukebox.TileEntitySuperJukebox;
import gtPlusPlus.core.container.ContainerSuperJukebox;
import gtPlusPlus.core.gui.GUIBaseTileEntity;

@SideOnly(Side.CLIENT)
public class GUISuperJukebox extends GUIBaseTileEntity {

    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(
        GTPlusPlus.ID,
        "textures/gui/SuperJukebox.png");
    private final ContainerSuperJukebox mThisContainer;

    public GUISuperJukebox(final InventoryPlayer player_inventory, final TileEntitySuperJukebox te) {
        super(new ContainerSuperJukebox(player_inventory, te));
        mThisContainer = (ContainerSuperJukebox) this.mContainer;
    }

    // This method is called when the Gui is first called!
    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
        super.drawGuiContainerForegroundLayer(par1, par2);

        boolean a = mThisContainer.isPlaying;
        boolean b = mThisContainer.isLooping;

        if (a && b) {
            this.fontRendererObj.drawString("[X]  [X]", 72, 74, 4210752);
        } else if (a && !b) {
            this.fontRendererObj.drawString("[X]  [ ]", 72, 74, 4210752);
        } else if (!a && b) {
            this.fontRendererObj.drawString("[ ]  [X]", 72, 74, 4210752);
        } else {
            this.fontRendererObj.drawString("[ ]  [ ]", 72, 74, 4210752);
        }

        this.drawTooltip(par1, par2);
    }

    private void drawTooltip(final int x2, final int y2) {
        final int xStart = (this.width - this.xSize) / 2;
        final int yStart = (this.height - this.ySize) / 2;
        final int x3 = x2 - xStart;
        final int y3 = y2 - yStart + 5;
        final List<String> list = new ArrayList<>();

        if (y3 >= 17 && y3 <= 33) {
            if (x3 >= 80 && x3 <= 96) {
                list.add("Play");
            }
        }
        if (y3 >= 35 && y3 <= 53) {
            if (x3 >= 80 && x3 <= 96) {
                list.add("Loop");
            }
        }
        if (!list.isEmpty()) {
            this.drawHoveringText(list, x3, y3, this.fontRendererObj);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(craftingTableGuiTextures);
        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
