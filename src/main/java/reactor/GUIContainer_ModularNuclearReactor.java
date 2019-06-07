package reactor;

import org.lwjgl.opengl.GL11;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import kekztech.KekzCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIContainer_ModularNuclearReactor extends GT_GUIContainerMetaTile_Machine {
	
	private final String resourceName;
	private final ResourceLocation texture;
	
	public GUIContainer_ModularNuclearReactor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity,
			String aName, String aTextureFile) {
		
		super(new Container_ModularNuclearReactor(aInventoryPlayer, aTileEntity), aTextureFile);
		
		this.resourceName = aTextureFile;
		this.texture = new ResourceLocation(KekzCore.MODID, "textures/gui/" + resourceName);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		
		// The parameters of GuiButton are (id, x, y, width, height, text)
		super.buttonList.add(new GuiButton(1, 100, 200, 100, 20, "Hello"));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		final int x = (super.width - super.xSize);
		final int y = (super.height - super.ySize);
		
		super.drawTexturedModalRect(x, y, 0, 0, super.xSize, super.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		
	}

}
