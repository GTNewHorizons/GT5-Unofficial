package gtPlusPlus.xmod.gregtech.api.gui;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The GUI-Container I use for all my Basic Machines
 * <p/>
 * As the NEI-RecipeTransferRect Handler can't handle one GUI-Class for all GUIs I needed to produce some dummy-classes which extend this class
 */
public class GUI_MultiMachine extends GT_GUIContainerMetaTile_Machine {

	private final static Materials GOOD;
	private final static Materials BAD;
	private final String mName;

	private final static ConcurrentHashMap<String, ItemStack> mToolStacks = new ConcurrentHashMap<String, ItemStack>();

	//net.minecraft.client.gui.inventory.GuiContainer.drawItemStack(ItemStack, int, int, String)
	private final static Method mDrawItemStack;

	static {
		GOOD = Materials.Uranium;
		BAD = Materials.Plutonium;

		//net.minecraft.client.gui.inventory.GuiContainer.drawItemStack(ItemStack, int, int, String)
		mDrawItemStack = ReflectionUtils.getMethod(GuiContainer.class, "drawItemStack", new Class[] {ItemStack.class, int.class, int.class, String.class});
	}

	public GUI_MultiMachine(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final String aName, final String aTextureFile) {
		super(new CONTAINER_MultiMachine_NoPlayerInventory(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
		this.mName = aName != null ? aName : "";
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {	
		if (this.mContainer != null) {
			drawGuiInfoTextLayer(par1, par2);
			drawGuiRepairStatusLayer(par1, par2);
		}
	}


	protected void drawGuiInfoTextLayer(final float par1, final int par2) {

		if ((((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 64) != 0) {
			this.fontRendererObj.drawString(mName, 6, 7, 16448255); // Move down 8px
			this.fontRendererObj.drawString("Incomplete Structure.", 6, 15, 16448255); // Move down 8px
		}
		else {
			int aTotalTickTime = ((CONTAINER_MultiMachine) this.mContainer).aTotalTickTime;
			int aMaxParallel = ((CONTAINER_MultiMachine) this.mContainer).aMaxParallel;
			int aPollutionTick = ((CONTAINER_MultiMachine) this.mContainer).aPollutionTick;
			int aMaxInputVoltage = ((CONTAINER_MultiMachine) this.mContainer).aMaxInputVoltage;
			int aInputTier = ((CONTAINER_MultiMachine) this.mContainer).aInputTier;
			int aOutputTier = ((CONTAINER_MultiMachine) this.mContainer).aOutputTier;
			int aRecipeDuration = ((CONTAINER_MultiMachine) this.mContainer).aRecipeDuration;
			int aRecipeEU = ((CONTAINER_MultiMachine) this.mContainer).aRecipeEU;
			int aRecipeSpecial = ((CONTAINER_MultiMachine) this.mContainer).aRecipeSpecial;
			int aEfficiency = ((CONTAINER_MultiMachine) this.mContainer).aEfficiency;

			int aPollutionReduction = ((CONTAINER_MultiMachine) this.mContainer).aPollutionReduction;


			long aStoredEnergy = ((CONTAINER_MultiMachine) this.mContainer).aStoredEnergy;
			long aMaxEnergy = ((CONTAINER_MultiMachine) this.mContainer).aMaxEnergy;

			/*
			 *  Logic Block
			 */

			long seconds = (aTotalTickTime/20);
			int weeks = (int) (TimeUnit.SECONDS.toDays(seconds) / 7);
			int days = (int) (TimeUnit.SECONDS.toDays(seconds) - 7 * weeks);
			long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(days) - TimeUnit.DAYS.toHours(7*weeks);
			long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
			long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

			ArrayList<String> mInfo = new ArrayList<String>();

			String EU = StatCollector.translateToLocal("GTPP.info.eu");

			//GTPP.machines.tier

			mInfo.add(mName);

			if (aInputTier > 0) {
				mInfo.add(StatCollector.translateToLocal("GTPP.machines.input")+" "+StatCollector.translateToLocal("GTPP.machines.tier")+": "+ EnumChatFormatting.GREEN +GT_Values.VOLTAGE_NAMES[aInputTier]);
			}
			if (aOutputTier > 0) {
				mInfo.add(StatCollector.translateToLocal("GTPP.machines.output")+" "+StatCollector.translateToLocal("GTPP.machines.tier")+": "+ EnumChatFormatting.GREEN +GT_Values.VOLTAGE_NAMES[aOutputTier]);
			}			

			mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.progress")+": "+
					EnumChatFormatting.GREEN + Integer.toString(((CONTAINER_MultiMachine) this.mContainer).mProgressTime/20) + EnumChatFormatting.RESET +" s / "+
					EnumChatFormatting.YELLOW + Integer.toString(((CONTAINER_MultiMachine) this.mContainer).mMaxProgressTime/20) + EnumChatFormatting.RESET +" s");


			mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.energy")+": "+
					EnumChatFormatting.GREEN + Long.toString(aStoredEnergy) + EnumChatFormatting.RESET +" "+EU+" / "+
					EnumChatFormatting.YELLOW + Long.toString(aMaxEnergy) + EnumChatFormatting.RESET +" "+EU+"");

			if (aRecipeEU != 0 && aRecipeDuration > 0) {
				mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.usage")+": "+
						EnumChatFormatting.RED + Integer.toString(-aRecipeEU) + EnumChatFormatting.RESET + " "+EU+"/t");			
				mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.duration")+": "+
						EnumChatFormatting.RED + Integer.toString(aRecipeDuration) + EnumChatFormatting.RESET + " ticks");
				if (aRecipeSpecial > 0) {
					mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.specialvalue")+": "+
							EnumChatFormatting.RED + Integer.toString(aRecipeEU) + EnumChatFormatting.RESET + "");
				}	
			}		

			mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.mei")+": "+
					EnumChatFormatting.YELLOW+Long.toString(aMaxInputVoltage)+EnumChatFormatting.RESET+ " "+EU+"/t"+EnumChatFormatting.RESET);

			mInfo.add(StatCollector.translateToLocal(StatCollector.translateToLocal("GTPP.machines.tier")+": "+
					EnumChatFormatting.YELLOW+GT_Values.VN[GT_Utility.getTier(aMaxInputVoltage)]+ EnumChatFormatting.RESET));

			mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.efficiency")+": "+ EnumChatFormatting.YELLOW+Float.toString(aEfficiency / 100.0F)+EnumChatFormatting.RESET + " %");

			mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.pollution")+": "+ EnumChatFormatting.RED + (aPollutionTick*20)+ EnumChatFormatting.RESET+"/sec");
			mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.pollutionreduced")+": "+ EnumChatFormatting.GREEN + aPollutionReduction + EnumChatFormatting.RESET+" %");

			mInfo.add(StatCollector.translateToLocal("GTPP.CC.parallel")+": "+EnumChatFormatting.GREEN+(aMaxParallel)+EnumChatFormatting.RESET);

			mInfo.add("Total Time Since Built: ");
			mInfo.add("" + EnumChatFormatting.DARK_GREEN + Integer.toString(weeks)+EnumChatFormatting.RESET+" Weeks,");
			mInfo.add("" + EnumChatFormatting.DARK_GREEN+ Integer.toString(days) +EnumChatFormatting.RESET+ " Days,");
			mInfo.add("" + EnumChatFormatting.DARK_GREEN+ Long.toString(hours) +EnumChatFormatting.RESET+ " Hours,");
			mInfo.add("" + EnumChatFormatting.DARK_GREEN+ Long.toString(minutes) +EnumChatFormatting.RESET+ " Minutes,");
			mInfo.add("" + EnumChatFormatting.DARK_GREEN+ Long.toString(second) +EnumChatFormatting.RESET+ " Seconds");



			// Machine Name
			//fontRendererObj.drawString(this.mName, 6, 7, 16448255);

			for (int i=0;i<mInfo.size();i++) {
				fontRendererObj.drawString(mInfo.get(i), 6, 7+(i*8), 16448255);				
			}

		}	




	}


	protected void drawGuiRepairStatusLayer(final float par1, final int par2) {

		boolean aWrench = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 1) != 0;
		boolean aScrewdriver = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 2) != 0;
		boolean aMallet = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 4) != 0;
		boolean aHammer = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 8) != 0;
		boolean aSoldering = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 16) != 0;
		boolean aCrowbar = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 32) != 0;	

		if (mToolStacks.isEmpty()) {
			// Map Stacks of Repair items
			aWrench = false;
			aScrewdriver = false;
			aMallet = false;
			aHammer = false;
			aSoldering = false;
			aCrowbar = false;				
			mToolStacks.put(aWrench+"WRENCH", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.WRENCH, 1, (aWrench ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aCrowbar+"CROWBAR", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.CROWBAR, 1, (aCrowbar ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aHammer+"HARDHAMMER",  GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, (aHammer ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aMallet+"SOFTHAMMER", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SOFTHAMMER, 1, (aMallet ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aScrewdriver+"SCREWDRIVER", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER, 1, (aScrewdriver ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aSoldering+"SOLDERING_IRON_LV", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SOLDERING_IRON_LV, 1, (aSoldering ? BAD : GOOD), Materials.Tungsten, null));

			// Map Stacks of valid items
			aWrench = true;
			aScrewdriver = true;
			aMallet = true;
			aHammer = true;
			aSoldering = true;
			aCrowbar = true;
			mToolStacks.put(aWrench+"WRENCH", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.WRENCH, 1, (aWrench ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aCrowbar+"CROWBAR", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.CROWBAR, 1, (aCrowbar ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aHammer+"HARDHAMMER",  GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, (aHammer ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aMallet+"SOFTHAMMER", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SOFTHAMMER, 1, (aMallet ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aScrewdriver+"SCREWDRIVER", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER, 1, (aScrewdriver ? BAD : GOOD), Materials.Tungsten, null));
			mToolStacks.put(aSoldering+"SOLDERING_IRON_LV", GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(GT_MetaGenerated_Tool_01.SOLDERING_IRON_LV, 1, (aSoldering ? BAD : GOOD), Materials.Tungsten, null));

			ItemStack aGlassPane1 = ItemUtils.getItemStackOfAmountFromOreDict("paneGlassRed", 1);
			ItemStack aGlassPane2 = ItemUtils.getItemStackOfAmountFromOreDict("paneGlassLime", 1);
			mToolStacks.put("falseGLASS", aGlassPane1);
			mToolStacks.put("trueGLASS", aGlassPane2);			

			// Reset vars to real state 
			aWrench = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 1) != 0;
			aScrewdriver = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 2) != 0;
			aMallet = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 4) != 0;
			aHammer = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 8) != 0;
			aSoldering = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 16) != 0;
			aCrowbar = (((CONTAINER_MultiMachine) this.mContainer).mDisplayErrorCode & 32) != 0;	

		}	

		ItemStack aWrenchStack;
		ItemStack aCrowbarStack;
		ItemStack aHammerStack;
		ItemStack aMalletStack;
		ItemStack aScrewdriverStack;
		ItemStack aSolderingStack;	
		if (!mToolStacks.isEmpty() && mDrawItemStack != null) {

			aWrenchStack = mToolStacks.get(aWrench + "WRENCH");
			aCrowbarStack = mToolStacks.get(aCrowbar + "CROWBAR");
			aHammerStack = mToolStacks.get(aHammer + "HARDHAMMER");
			aMalletStack = mToolStacks.get(aMallet + "SOFTHAMMER");
			aScrewdriverStack = mToolStacks.get(aScrewdriver + "SCREWDRIVER");
			aSolderingStack = mToolStacks.get(aSoldering + "SOLDERING_IRON_LV");

			try {

				ItemStack[] aToolStacks2 = new ItemStack[] {
						aWrenchStack, aCrowbarStack,
						aHammerStack, aMalletStack,
						aScrewdriverStack, aSolderingStack };

				int aIndex = 0;
				//Draw Repair status tools
				for (aIndex = 0; aIndex < 6; aIndex++) {
					int x = 156;
					int y = 112 - (18 * 3) + (aIndex * 18);
					mDrawItemStack.invoke(this,
							new Object[] {
									aToolStacks2[aIndex] != null ? aToolStacks2[aIndex]
											: ItemUtils.getErrorStack(1, "Bad Times"),
											x, y, "" + (aIndex == 2 ? "H" : aIndex == 3 ? "M" : "") // Stacksize Overlay
					});
					//this.fontRendererObj.drawString("", 10, 64, 16448255);
				}

				//Draw Running status
				boolean running = ((CONTAINER_MultiMachine) this.mContainer).mActive != 0;
				ItemStack aGlassPane = mToolStacks.get(running+"GLASS");

				if (aGlassPane == null) {
					aGlassPane = ItemUtils.getItemStackOfAmountFromOreDict("paneGlass" + (running ? "Lime" : "Red"), 1);
					mToolStacks.put(running+"GLASS", aGlassPane);
				}

				mDrawItemStack.invoke(this,
						new Object[] {
								aGlassPane != null ? aGlassPane
										: ItemUtils.getErrorStack(1, "Bad Times"),
										156,
										112 - (18 * 5),
										running ? "On" : "Off"}); // Stacksize Overlay

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}




	}


	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}
