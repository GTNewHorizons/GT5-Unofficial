package miscutil.core.gui.machine;

import miscutil.core.container.Container_NHG;
import miscutil.core.lib.CORE;
import miscutil.core.tileentities.TileEntityNHG;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GUI_NHG extends GuiContainer
{
    private ResourceLocation texture = new ResourceLocation(CORE.MODID, "textures/gui/helium_collector_gui_12.png");
 
    private InventoryPlayer inventory;
    private TileEntityNHG te;
 
    public GUI_NHG(TileEntityNHG te, EntityPlayer player)
    {
        super(new Container_NHG(te, player));
        inventory = player.inventory;
        this.te = te;
    }
 
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
 
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
 
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
 
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
 
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRendererObj.drawString(I18n.format(te.getInventoryName()), (xSize / 2) - (fontRendererObj.getStringWidth(I18n.format(te.getInventoryName())) / 2), 6, 4210752, false);
        fontRendererObj.drawString(I18n.format(inventory.getInventoryName()), 8, ySize - 96 + 2, 4210752);
    }
}