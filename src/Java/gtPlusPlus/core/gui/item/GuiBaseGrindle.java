package gtPlusPlus.core.gui.item;

import java.util.LinkedHashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.container.Container_Grindle;
import gtPlusPlus.core.inventories.BaseInventoryGrindle;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class GuiBaseGrindle extends GuiContainer {

	/** The FontRenderer used by GuiScreen */
	protected FontRenderer fontRenderer;

	private static final ResourceLocation iconLocation = new ResourceLocation(CORE.MODID,
			"textures/gui/itemGrindle.png");

	/** The inventory to render on screen */
	private final BaseInventoryGrindle inventory;

	public GuiBaseGrindle(final Container_Grindle containerItem) {
		super(containerItem);
		this.inventory = containerItem.inventory;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		super.drawScreen(par1, par2, par3);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {

		// Title
		this.fontRendererObj.drawStringWithShadow(I18n.format("Gregtech Information Transponder", new Object[0]), 0,
				-12, Utils.rgbtoHexValue(255, 255, 255));
		ItemStack aStack = this.inventory.getStackInSlot(0);
		if (aStack != null) {

			int aType = -1;
			boolean isProspecting = false;
			if (GT_Utility.areStacksEqual(aStack, CI.getDataStick(), true)) {
				aType = 0;
			} else if (GT_Utility.areStacksEqual(aStack, CI.getDataOrb(), true)) {
				aType = 1;
			}

			NBTTagCompound tNBT = this.inventory.getStackInSlot(0).getTagCompound();
			if (tNBT == null) {
				tNBT = new NBTTagCompound();
			}

			String aName = tNBT.hasKey("title") ? tNBT.getString("title")
					: (aType == 0 ? "Empty Data Stick" : (aType == 1 ? "Empty Data Orb" : "Unknown Item"));

			this.fontRendererObj.drawString(I18n.format(aName), 10, 8, Utils.rgbtoHexValue(125, 255, 125));

			if (aName.toLowerCase().contains("raw prospection data")) {
				isProspecting = true;
			}

			// Debug NBT Information
			//NBTUtils.tryIterateNBTData(aStack);

			byte tTier = -1;
			if (isProspecting) {
				if (!tNBT.hasKey("prospection_tier") && tNBT.hasKey("prospection")) {
					tTier = 0;
				} else if (tNBT.hasKey("prospection_tier") && !tNBT.hasKey("prospection")) {
					tTier = tNBT.getByte("prospection_tier");
				}
			}

			if (tTier >= 0) {

				String xPos, yPos, zPos, aDim;
				String aPositionString, aInfoString;
				Set<String> aOreTypes = new LinkedHashSet<String>();
				Set<String> aOilTypes = new LinkedHashSet<String>();

				//Set variables that are shared between prospection types.
				if (tTier == 0) {
					String tData = tNBT.getString("prospection");
					String[] tDataArray = tData.split(",");					
					xPos = tDataArray[0];
					yPos = tDataArray[1];
					zPos = tDataArray[2];
					aDim = tDataArray[3];					
					aOilTypes.add("Oil Type: "+tDataArray[5]);		
					aOreTypes.add("Ore Types: ");			
					for (int i = 6; tDataArray.length > i; i++) {
						aOreTypes.add("-"+tDataArray[i]);
					}
				}
				else {
					String tPos = tNBT.getString("prospection_pos");
					String[] tPosData = tPos.split(" ");								
					xPos = tPosData[1];
					yPos = tPosData[3];
					zPos = tPosData[5];
					aDim = tPosData[7];						
					//Oil
					String tOil = tNBT.getString("prospection_oils");
					String[] tOilData = tOil.split("\\|");
					if (tOilData.length > 0) {
						aOilTypes.add("Oil Types:");	
						for (String s : tOilData) {
							if (s != null) {
								aOilTypes.add(s);
							}
						}					
					}

					//Near
					String tOresNear = tNBT.getString("prospection_near");
					String[] tOresNearData = tOresNear.split("\\|");
					//Middle
					String tOresMid = tNBT.getString("prospection_middle");
					String[] tOresMidData = tOresMid.split("\\|");
					//Far
					String tOresFar = tNBT.getString("prospection_far");
					String[] tOresFarData = tOresFar.split("\\|");

					if ((tOresNearData.length + tOresMidData.length + tOresFarData.length) > 0) {
						aOreTypes.add("Ore Types:");
						if (tOresNearData.length > 0) {				
							for (String s : tOresNearData) {
								if (s != null) {
									aOreTypes.add(s);
								}
							}	
						}
						if (tOresMidData.length > 0) {					
							for (String s : tOresMidData) {
								if (s != null) {
									aOreTypes.add(s);
								}
							}		
						}	
						if (tOresFarData.length > 0) {				
							for (String s : tOresFarData) {
								if (s != null) {
									aOreTypes.add(s);
								}
							}	
						}
					}					

				}

				aInfoString = "Tier: "+tTier+" | Dim: "+aDim;
				aPositionString = "X:"+xPos+", Y:"+yPos+", Z:"+zPos;

				//Draw the GUI
				// List prospection
				this.fontRendererObj.drawString(I18n.format(aInfoString, new Object[0]), 10, 18, Utils.rgbtoHexValue(125, 255, 125));
				// Divider
				this.fontRendererObj.drawString(I18n.format("-------------------", new Object[0]), 10, 23, Utils.rgbtoHexValue(125, 125, 255));
				// Pos data
				this.fontRendererObj.drawString(I18n.format(aPositionString, new Object[0]), 10, 29, Utils.rgbtoHexValue(125, 125, 255));
				// Divider
				this.fontRendererObj.drawString(I18n.format("-------------------", new Object[0]), 10, 35, Utils.rgbtoHexValue(125, 125, 255));

				int aLastYUsed = 40;

				AutoMap<String> aInfoList = new AutoMap<String>();

				if (aOilTypes.size() > 0) {					
					for (String aOils : aOilTypes) {
						if (aOils != null && aOils.length() > 0) {
							aInfoList.put(aOils);
						}
					}
				}

				if (aOreTypes.size() > 0) {					
					for (String aOres : aOreTypes) {
						if (aOres != null && aOres.length() > 0) {
							aInfoList.put(aOres);
						}
					}
				}

				for (int i=0;i<aInfoList.size();i++) {
					if ((aLastYUsed + 9) <= (68 + 56)) {
						this.fontRendererObj.drawString(I18n.format(aInfoList.get(i), new Object[0]), 10, aLastYUsed, Utils.rgbtoHexValue(125, 255, 125));
						aLastYUsed = aLastYUsed + 9;
					}
				}			
			}
			else {

			}



			/*if (tDataArray.length > 6) {
					// Adds weird info to item in slot but this data is not retained.
					// tNBT.setString("author", ".");
					NBTTagList tNBTList = new NBTTagList();
					String[] mOreTypes = new String[50];
					String tOres = " Prospected Ores: ";
					for (int i = 6; tDataArray.length > i; i++) {
						mOreTypes[i] = (tDataArray[i] + " ");
						if ((68 + (i - 6) * 8) < (68 + 56)) {
							this.fontRendererObj.drawString(I18n.format(mOreTypes[i], new Object[0]), 10,
									40 + ((i - 6) * 8), Utils.rgbtoHexValue(125, 255, 125));
						}
					}
					// tNBTList.appendTag(new NBTTagString("Prospection Data From: X" +
					// tDataArray[0] + " Z:" + tDataArray[2] + " Dim:" + tDataArray[3] + " Produces
					// " + tDataArray[4] + "L " + tDataArray[5] + " " + tOres));
					// tNBT.setTag("pages", tNBTList);

				} else {
					this.fontRendererObj.drawString(I18n.format("Bad prospection data.", new Object[0]), 10, 68,
							Utils.rgbtoHexValue(255, 125, 125));
				}*/
		} else {
			// Valid Datastick?
			this.fontRendererObj.drawStringWithShadow(I18n.format("Insert device into port.", new Object[0]), 10, 8,
					Utils.rgbtoHexValue(255, 125, 125));
		}

		// Inventory Label
		this.fontRendererObj.drawStringWithShadow(I18n.format("container.inventory", new Object[0]), 8, 131,
				Utils.rgbtoHexValue(255, 255, 255));
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(iconLocation);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		final int i1;
		// drawPlayerModel(k + 51, l + 75, 30, k + 51 - this.xSize_lo, (l + 75) - 50 -
		// this.ySize_lo, this.mc.thePlayer);
	}
}
