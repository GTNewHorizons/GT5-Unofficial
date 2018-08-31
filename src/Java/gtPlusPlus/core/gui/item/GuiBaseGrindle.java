package gtPlusPlus.core.gui.item;

import java.util.LinkedHashSet;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.container.Container_Grindle;
import gtPlusPlus.core.inventories.BaseInventoryGrindle;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class GuiBaseGrindle extends GuiContainer {

	/** The FontRenderer used by GuiScreen */
	protected FontRenderer fontRenderer;

	private GrindleGuiButton mButtonNextPage;
	private GrindleGuiButton mButtonPreviousPage;
	private String[][] mPageDataArray;
	private short mCurrentPage = 0;

	private static final ResourceLocation iconLocation = new ResourceLocation(CORE.MODID, "textures/gui/itemGrindle.png");

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
		this.updateButtons();
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {

		try {

			// Title
			this.fontRendererObj.drawStringWithShadow(I18n.format("Gregtech Information Transponder", new Object[0]), 0,
					-12, Utils.rgbtoHexValue(255, 255, 255));
			ItemStack aStack = this.inventory.getStackInSlot(0);

			if (aStack != null) {

				GrindleData aDataCurrent = new GrindleData(aStack);

				if (aDataCurrent.mValid) {

					// Debug NBT Information
					//NBTUtils.tryIterateNBTData(aStack);

					this.fontRendererObj.drawString(I18n.format(aDataCurrent.mTitle), 10, 8, Utils.rgbtoHexValue(125, 255, 125));

					int tTier = aDataCurrent.mExtraInformation;
					if (tTier >= 0) {
						//Draw the GUI
						// List prospection
						this.fontRendererObj.drawString(I18n.format(aDataCurrent.mTierDim+" | Page: "+this.mCurrentPage, new Object[0]), 10, 18, Utils.rgbtoHexValue(125, 255, 125));
						// Divider
						this.fontRendererObj.drawString(I18n.format("-------------------", new Object[0]), 10, 23, Utils.rgbtoHexValue(125, 125, 255));
						// Pos data
						this.fontRendererObj.drawString(I18n.format(aDataCurrent.mPosInfo, new Object[0]), 10, 29, Utils.rgbtoHexValue(125, 125, 255));
						// Divider
						this.fontRendererObj.drawString(I18n.format("-------------------", new Object[0]), 10, 35, Utils.rgbtoHexValue(125, 125, 255));
						int aLastYUsed = 41;

						int posOuter = 0;
						int posInner = 0;

						mPageDataArray = new String[MathUtils.roundToClosestInt(Math.ceil(aDataCurrent.mListData.size() / 9.00))][9];
						for (String e : aDataCurrent.mListData) {
							if (e != null) {								
								mPageDataArray[posInner][posOuter] = e;
								if (posOuter < 8) {
									posOuter++;
								}
								else {
									posOuter = 0;
									posInner++;									
								}
							}
							else {
								continue;
							}
						}

						String[] aCurrentPage = mPageDataArray[this.mCurrentPage];	
						
						if (aCurrentPage != null) {
							//Logger.INFO("valid Current page " + this.mCurrentPage);
							if (aCurrentPage.length > 0) {
								for (int i=0;i<aCurrentPage.length;i++) {
									if ((aLastYUsed + 9) <= (68 + 56)) {
										String aCP = aCurrentPage[i];
										//Logger.INFO("Printing "+aCP);
										if (aCP != null && aCP.length() > 0) {											
											if (!aCP.toLowerCase().contains("empty") && !aCP.toLowerCase().contains("null")) {
												this.fontRendererObj.drawString(I18n.format(aCP), 10, aLastYUsed, Utils.rgbtoHexValue(125, 255, 125));
												aLastYUsed += 9;
											}											
										}
									}
								}	
							}
						}




					}

					//Non-Prospecting Data
					else {
						//Draw the GUI
						// List prospection
						this.fontRendererObj.drawString(I18n.format(aDataCurrent.mTierDim, new Object[0]), 10, 18, Utils.rgbtoHexValue(125, 255, 125));
						// Divider
						this.fontRendererObj.drawString(I18n.format("-------------------", new Object[0]), 10, 23, Utils.rgbtoHexValue(125, 125, 255));
						// Pos data
						this.fontRendererObj.drawString(I18n.format(aDataCurrent.mPosInfo, new Object[0]), 10, 29, Utils.rgbtoHexValue(125, 125, 255));
						// Divider
						this.fontRendererObj.drawString(I18n.format("-------------------", new Object[0]), 10, 35, Utils.rgbtoHexValue(125, 125, 255));
						int aLastYUsed = 41;
						/*for (int i=0;i<aDataCurrent.mListData.size();i++) {
					if ((aLastYUsed + 9) <= (68 + 56)) {
						this.fontRendererObj.drawString(I18n.format(aDataCurrent.mListData.get(i), new Object[0]), 10, aLastYUsed, Utils.rgbtoHexValue(125, 255, 125));
						aLastYUsed = aLastYUsed + 9;
					}
				}	*/
					}
				}
				else {
					this.fontRendererObj.drawStringWithShadow(I18n.format("Invalid data item stored in slot.", new Object[0]), 10, 8,
							Utils.rgbtoHexValue(255, 125, 125));
				}
			} else {
				// Valid Datastick?
				this.fontRendererObj.drawStringWithShadow(I18n.format("Insert device into port.", new Object[0]), 10, 8, Utils.rgbtoHexValue(255, 125, 125));
				this.mPageDataArray = new String[][] {{}};
			}

			// Inventory Label
			this.fontRendererObj.drawStringWithShadow(I18n.format("container.inventory", new Object[0]), 8, 131,
					Utils.rgbtoHexValue(255, 255, 255));

		}
		catch (Throwable t) {
			Logger.INFO("GUI CRASH - "+t);
			t.printStackTrace();
		}

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
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initGui(){
		super.initGui();
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		final int k = (this.width - this.xSize) - (this.width/16);
		this.mButtonNextPage = new GrindleGuiButton(1,  k, 155, true);
		this.mButtonPreviousPage = new GrindleGuiButton(2, k-20, 155, false);
		this.buttonList.add(this.mButtonNextPage);
		this.buttonList.add(this.mButtonPreviousPage);
		this.mCurrentPage = 0;
		this.updateButtons();
	}


	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		this.mCurrentPage = 0;
		super.onGuiClosed();
	}

	private void updateButtons(){
		if (this.mCurrentPage >= 0) {
			if (this.mPageDataArray != null) {
				if (this.mCurrentPage < (this.mPageDataArray.length - 1)) {
					this.mButtonNextPage.visible = true;
				} else {
					this.mButtonNextPage.visible = false;
				}
			} else {
				this.mButtonNextPage.visible = false;
			}
		}		
		if (this.mCurrentPage > 0) {
			this.mButtonPreviousPage.visible = true;
		}
		else {
			this.mButtonPreviousPage.visible = false;
		}
	}


	/**
	 * Scrolling Related
	 */	
	@Override
	protected void actionPerformed(GuiButton aButton) {
		if (aButton.enabled && aButton.visible) {
			if (aButton.id == 1) {
				if (this.mCurrentPage < this.mPageDataArray.length - 1) {
					++this.mCurrentPage;
				}
			} else if (aButton.id == 2) {
				if (this.mCurrentPage > 0) {
					--this.mCurrentPage;
				}
			}
			this.updateButtons();
		}
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		/*
		 * if (i != 0) {
		 * 
		 * 
		 * this.currentScroll = (float) ((double) this.currentScroll - (double) i /
		 * (double) j);
		 * 
		 * if (this.currentScroll < 0.0F) { this.currentScroll = 0.0F; }
		 * 
		 * if (this.currentScroll > 1.0F) { this.currentScroll = 1.0F; }
		 * 
		 * this.scrollTo(this.currentScroll); }
		 */
	}

	/**
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		/*
		 * if (p_146286_3_ == 0) { int l = p_146286_1_ - this.guiLeft; int i1 =
		 * p_146286_2_ - this.guiTop; CreativeTabs[] acreativetabs =
		 * CreativeTabs.creativeTabArray; int j1 = acreativetabs.length;
		 * 
		 * for (int k1 = 0; k1 < j1; ++k1) { CreativeTabs creativetabs =
		 * acreativetabs[k1];
		 * 
		 * if (creativetabs != null && this.func_147049_a(creativetabs, l, i1)) {
		 * this.setCurrentCreativeTab(creativetabs); return; } } }
		 */

		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
	}

	/**
	 * Allows Mouseover Tooltips
	 */
	@Override
	protected void renderToolTip(ItemStack aStack, int p_146285_2_, int p_146285_3_) {
		super.renderToolTip(aStack, p_146285_2_, p_146285_3_);
		}

	/**
	 * Custom Buttons
	 */

	@SideOnly(Side.CLIENT)
	static class GrindleGuiButton extends GuiButton {

		public static final ResourceLocation mBookTexture;

		static {
			ResourceLocation r;
			try {			 
				r = (ResourceLocation) ReflectionUtils.getField(GuiScreenBook.class, "bookGuiTextures").get(null);

			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
				r = new ResourceLocation("textures/gui/book.png");
			}
			mBookTexture = r;
		}

		private final boolean aPageForward;

		public GrindleGuiButton(int aID, int aX, int aY, boolean aForwards) {
			this(aID, aX, aY, 20, 12, aForwards);
		}

		public GrindleGuiButton(int aID, int aX, int aY, int aWidth, int aHeight, boolean aForwards) {
			super(aID, aX, aY, aWidth, aHeight, "");
			this.aPageForward = aForwards;
		}

		/**
		 * Draws this button to the screen.
		 */
		public void drawButton(Minecraft aGame, int aX, int aY) {
			if (this.visible) {
				boolean flag = aX >= this.xPosition && aY >= this.yPosition
						&& aX < this.xPosition + this.width && aY < this.yPosition + this.height;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);			
				aGame.getTextureManager().bindTexture(iconLocation);
				int k = 0;
				int l = 192;

				if (flag) {
					k += 23;
				}

				if (!this.aPageForward) {
					l += 13;
				}

				this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 22, 12);
			}
		}
	}	


	public static class GrindleData {

		public static enum GrindleMode {
			PROSPECTING(0),
			ELEMENT(1);
			private final int aModeID;
			private GrindleMode (final int aMode)
			{
				this.aModeID = aMode;
			}

			public int getMode() {
				return this.aModeID;
			}
		}

		final boolean mValid;
		final ItemStack mStack;
		final String mTitle, mPosInfo, mTierDim;
		final AutoMap<String> mListData;
		final GrindleMode mMode;
		final int mExtraInformation;


		public GrindleData(ItemStack iStack) {

			if (iStack == null) {
				mValid = false;
				mStack = null;
				mTitle = null;
				mListData = null;
				mMode = null;
				mExtraInformation = 0;
				this.mTierDim = "";
				this.mPosInfo = "";
			} else {

				this.mStack = iStack;

				int aType = -1;
				boolean isProspecting = false;

				// If Input stack is a Data stick/Orb, set a valid type.
				if (GT_Utility.areStacksEqual(iStack, CI.getDataStick(), true)) {
					aType = 0;
				} else if (GT_Utility.areStacksEqual(iStack, CI.getDataOrb(), true)) {
					aType = 1;
				}

				NBTTagCompound tNBT = iStack.getTagCompound();
				if (tNBT == null) {
					tNBT = new NBTTagCompound();
				}

				mTitle = tNBT.hasKey("title") ? tNBT.getString("title")
						: (aType == 0 ? "Empty Data Stick" : (aType == 1 ? "Empty Data Orb" : "Unknown Item"));
				if (mTitle.toLowerCase().contains("raw prospection data")) {
					isProspecting = true;
				}

				byte tTier = -1;
				if (isProspecting) {
					if (!tNBT.hasKey("prospection_tier") && tNBT.hasKey("prospection")) {
						tTier = 0;
					} else if (tNBT.hasKey("prospection_tier") && !tNBT.hasKey("prospection")) {
						tTier = tNBT.getByte("prospection_tier");
					}
				}

				mExtraInformation = isProspecting ? tTier : -1;

				if (tTier >= 0) {
					String xPos, yPos, zPos, aDim;
					String aPositionString, aInfoString;
					Set<String> aOreTypes = new LinkedHashSet<String>();
					Set<String> aOilTypes = new LinkedHashSet<String>();

					// Set variables that are shared between prospection types.
					if (tTier == 0) {
						String tData = tNBT.getString("prospection");
						String[] tDataArray = tData.split(",");
						xPos = tDataArray[0];
						yPos = tDataArray[1];
						zPos = tDataArray[2];
						aDim = tDataArray[3];
						aOilTypes.add("Oil Type: " + tDataArray[5]);
						aOilTypes.add("--------------------");
						aOreTypes.add("Ore Types: ");
						for (int i = 6; tDataArray.length > i; i++) {
							aOreTypes.add("-" + tDataArray[i]);
						}
					} else {
						String tPos = tNBT.getString("prospection_pos");
						String[] tPosData = tPos.split(" ");
						xPos = tPosData[1];
						yPos = tPosData[3];
						zPos = tPosData[5];
						aDim = tPosData[7];
						// Oil
						String tOil = tNBT.getString("prospection_oils");
						String[] tOilData = tOil.split("\\|");
						if (tOilData.length > 0) {
							aOilTypes.add("Oil Types:");
							for (String s : tOilData) {
								if (s != null) {
									aOilTypes.add(s);
								}
							}
							aOilTypes.add("--------------------");
						}

						// Near
						String tOresNear = tNBT.getString("prospection_near");
						String[] tOresNearData = tOresNear.split("\\|");
						// Middle
						String tOresMid = tNBT.getString("prospection_middle");
						String[] tOresMidData = tOresMid.split("\\|");
						// Far
						String tOresFar = tNBT.getString("prospection_far");
						String[] tOresFarData = tOresFar.split("\\|");

						if ((tOresNearData.length + tOresMidData.length + tOresFarData.length) > 0) {
							aOreTypes.add("Ore Types:");
							if (tOresNearData.length > 0) {
								for (String s : tOresNearData) {
									if (s != null) {
										aOreTypes.add("-" + s + " - Near");
									}
								}
							}
							if (tOresMidData.length > 0) {
								for (String s : tOresMidData) {
									if (s != null) {
										aOreTypes.add("-" + s + " - Mid");
									}
								}
							}
							if (tOresFarData.length > 0) {
								for (String s : tOresFarData) {
									if (s != null) {
										aOreTypes.add("-" + s + " - Far");
									}
								}
							}
						}
					}

					mListData = new AutoMap<String>();
					if (aOilTypes.size() > 0) {
						for (String aOils : aOilTypes) {
							if (aOils != null && aOils.length() > 0) {
								mListData.put(aOils);
							}
						}
					}

					if (aOreTypes.size() > 0) {
						for (String aOres : aOreTypes) {
							if (aOres != null && aOres.length() > 0) {
								mListData.put(aOres);
							}
						}
					}

					// Set Types
					mMode = GrindleMode.PROSPECTING;
					this.mTierDim = (aInfoString = "Tier: " + tTier + " | Dim: " + aDim);
					this.mPosInfo = (aPositionString = "X:" + xPos + ", Y:" + yPos + ", Z:" + zPos);
					this.mValid = true;
				}

				// Non-Prospectic Data
				else {
					mValid = true;
					mListData = null;
					mMode = GrindleMode.ELEMENT;
					this.mTierDim = "PLACEHOLDER";
					this.mPosInfo = "PLACEHOLDER";
				}

			}

		}

	}


}
