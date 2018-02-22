package gtPlusPlus.core.gui.item;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Utility.ItemNBT;
import gtPlusPlus.core.container.Container_BackpackBase;
import gtPlusPlus.core.container.Container_Grindle;
import gtPlusPlus.core.inventories.BaseInventoryBackpack;
import gtPlusPlus.core.inventories.BaseInventoryGrindle;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

public class GuiBaseGrindle extends GuiContainer {

	/** The FontRenderer used by GuiScreen */
	protected FontRenderer fontRenderer;

	private static final ResourceLocation iconLocation = new ResourceLocation(CORE.MODID, "textures/gui/itemGrindle.png");

	/** The inventory to render on screen */
	private final BaseInventoryGrindle inventory;

	public GuiBaseGrindle(final Container_Grindle containerItem){
		super(containerItem);
		this.inventory = containerItem.inventory;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(final int par1, final int par2, final float par3){
		super.drawScreen(par1, par2, par3);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2){
		final String s = "Git";
		//Title
		this.fontRendererObj.drawStringWithShadow(I18n.format("Gregtech Information Transponder", new Object[0]), 0, -12, Utils.rgbtoHexValue(255, 255, 255));

		if (this.inventory.getStackInSlot(0) != null){
			this.fontRendererObj.drawString(I18n.format(""+NBTUtils.getBookTitle(this.inventory.getStackInSlot(0)), new Object[0]), 10, 8, Utils.rgbtoHexValue(125, 255, 125));

			//if (!NBTUtils.tryIterateNBTData(this.inventory.getStackInSlot(0))){
			//	this.fontRendererObj.drawString(I18n.format("Very Bad prospection data.", new Object[0]), 10, 38, Utils.rgbtoHexValue(255, 125, 125));
			//}
			
			NBTTagCompound tNBT = ItemNBT.getNBT(this.inventory.getStackInSlot(0));
			byte tTier = tNBT.getByte("prospection_tier");
			//List Tier
			//this.fontRendererObj.drawStringWithShadow(I18n.format("Tier: "+tTier, new Object[0]), 10, 18, Utils.rgbtoHexValue(125, 255, 125));

			if (tTier == 0) { // basic prospection data
				String tData = tNBT.getString("prospection");
				//List prospection
				//this.fontRendererObj.drawStringWithShadow(I18n.format("Prospection : "+tData, new Object[0]), 10, 28, Utils.rgbtoHexValue(125, 255, 125));

				String[] tDataArray = tData.split(",");
				if (tDataArray.length > 6) {
					tNBT.setString("author", "X: " + tDataArray[0] + " Y: " + tDataArray[1] + " Z: " + tDataArray[2] + " Dim: " + tDataArray[3]);
					//List prospection
					this.fontRendererObj.drawString(I18n.format("X: " + tDataArray[0], new Object[0]), 10, 28, Utils.rgbtoHexValue(125, 125, 255));
					this.fontRendererObj.drawString(I18n.format("Y: " + tDataArray[1], new Object[0]), 10, 38, Utils.rgbtoHexValue(125, 125, 255));
					this.fontRendererObj.drawString(I18n.format("Z: " + tDataArray[2], new Object[0]), 10, 48, Utils.rgbtoHexValue(125, 125, 255));
					this.fontRendererObj.drawString(I18n.format("Dim: " + tDataArray[3], new Object[0]), 10, 58, Utils.rgbtoHexValue(125, 125, 255));
					
					//Divider
					this.fontRendererObj.drawString(I18n.format("-------------------", new Object[0]), 10, 63, Utils.rgbtoHexValue(125, 125, 255));
					
					NBTTagList tNBTList = new NBTTagList();
					String[] mOreTypes = new String[50];
					String tOres = " Prospected Ores: ";
					for (int i = 6; tDataArray.length > i; i++) {
						mOreTypes[i] = (tDataArray[i] + " ");
						if ((68+(i-6)*8) < (68+56)){
							this.fontRendererObj.drawString(I18n.format(mOreTypes[i], new Object[0]), 10, 68+((i-6)*8), Utils.rgbtoHexValue(125, 255, 125));
						}						
					}
					tNBTList.appendTag(new NBTTagString("Prospection Data From: X" + tDataArray[0] + " Z:" + tDataArray[2] + " Dim:" + tDataArray[3] + " Produces " + tDataArray[4] + "L " + tDataArray[5] + " " + tOres));
					tNBT.setTag("pages", tNBTList);
					
					
					//List prospection
					this.fontRendererObj.drawString(I18n.format("Tier: "+tTier+ " | Pages: "+tNBTList.tagCount(), new Object[0]), 10, 18, Utils.rgbtoHexValue(125, 255, 125));
					//Divider
					this.fontRendererObj.drawString(I18n.format("-------------------", new Object[0]), 10, 23, Utils.rgbtoHexValue(125, 125, 255));
					
				}
				else {
					this.fontRendererObj.drawString(I18n.format("Bad prospection data.", new Object[0]), 10, 68, Utils.rgbtoHexValue(255, 125, 125));
				}
			}
		}
		else {
			//Valid Datastick?
			this.fontRendererObj.drawStringWithShadow(I18n.format("Insert device into port.", new Object[0]), 10, 8, Utils.rgbtoHexValue(255, 125, 125));
		}



		//Inventory Label
		this.fontRendererObj.drawStringWithShadow(I18n.format("container.inventory", new Object[0]), 8, 131, Utils.rgbtoHexValue(255, 255, 255));

		//this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 0, 4210752);
		//this.fontRenderer.drawString(I18n.translate("container.inventory"), 26, this.ySize - 96 + 4, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(iconLocation);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		final int i1;
		//drawPlayerModel(k + 51, l + 75, 30, k + 51 - this.xSize_lo, (l + 75) - 50 - this.ySize_lo, this.mc.thePlayer);
	}
}
