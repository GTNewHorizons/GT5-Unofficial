package container;

import org.lwjgl.opengl.GL11;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import kekztech.KekzCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIContainer_ModularNuclearReactor extends GuiContainer {
	
	private ResourceLocation texture = new ResourceLocation(KekzCore.MODID, "textures/gui/ReaktorGUI_background.png");
	 
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
        
        drawTexturedModalRect(100, 100, 0, 0, 300, 200);
    }
 
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
       
    }

}
