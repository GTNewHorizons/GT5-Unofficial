package client.gui;

import org.lwjgl.opengl.GL11;

import common.container.Container_ModularNuclearReactor;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import kekztech.KekzCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIContainer_ModularNuclearReactor extends GuiContainer {
	
	private ResourceLocation texture = new ResourceLocation(KekzCore.MODID, "textures/gui/MultiblockDisplay_REACTOR.png");
	 
    private InventoryPlayer inventory;
    private IGregTechTileEntity te;
 
    public GUIContainer_ModularNuclearReactor(IGregTechTileEntity te, EntityPlayer player)
    {
        super(new Container_ModularNuclearReactor(te, player));
        inventory = player.inventory;
        this.te = te;
    }
 
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
 
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        final int x = (super.width - super.xSize) / 2;
		final int y = (super.height - super.ySize) / 2;
		super.drawTexturedModalRect(x, y, 0, 0, super.xSize, super.ySize);
    }
 
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
       
    }

}
