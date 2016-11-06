package gtPlusPlus.xmod.forestry.bees.alveary.gui;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.forestry.bees.alveary.TileAlvearyFrameHousing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GUI_FrameHousing extends GuiContainer
{
    private ResourceLocation texture = new ResourceLocation(CORE.MODID, "textures/gui/machine_Charger.png");
 
    private InventoryPlayer inventory;
    private TileAlvearyFrameHousing te;
 
    public GUI_FrameHousing(TileAlvearyFrameHousing te, EntityPlayer player)
    {
        super(new CONTAINER_FrameHousing(te, player));
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
        fontRendererObj.drawString(I18n.format("Alveary Frame Housing"), (xSize / 2) - (fontRendererObj.getStringWidth(I18n.format("Alveary Frame Housing")) / 2), 6, 4210752, false);
        fontRendererObj.drawString(I18n.format(inventory.getInventoryName()), 8, ySize - 96 + 2, 4210752);
        //fontRendererObj.drawString(I18n.format("Charge:"+te.getCharge()+"~"), 8, ySize - 96 + 2, 4210752);
        //fontRendererObj.drawString(I18n.format("Progress:"+te.getProgress()+"ticks"), 80, ySize - 96 + 2, 4210752);
    }
}