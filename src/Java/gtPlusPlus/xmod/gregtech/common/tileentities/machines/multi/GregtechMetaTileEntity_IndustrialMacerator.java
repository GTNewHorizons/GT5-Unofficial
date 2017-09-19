package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_IndustrialMacerator
extends GregtechMeta_MultiBlockBase {
	private static boolean controller;

	public GregtechMetaTileEntity_IndustrialMacerator(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialMacerator(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialMacerator(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Industrial Maceration Stack",
				"60% faster than using single block machines of the same voltage",
				"Processes material several factors faster than single block macerators",
				"Has extra chances on bonus outputs",
				"Processes 8*tier materials at a time",
				"ULV = Tier 0, LV = Tier 1, etc.",
				"-------------------------------------------------------",
				"Size[WxHxL]: 3x6x3 (Hollow)",
				"Controller (Center Bottom)",
				"1x Input Bus (Any bottom layer casing)",
				"5x Output Bus (Any casing besides bottom layer)",
				"1x Maintenance Hatch (Any casing)",
				"1x Energy Hatch (Any casing)",
				"Maceration Stack Casings for the rest (26 at least!)",
				"Causes " + (20 * getPollutionPerTick(null)) + " Pollution per second",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(7)], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_MatterFab_Active : TexturesGtBlock.Overlay_MatterFab)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(7)]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MacerationStack.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
	}

	/*@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}*/

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public void onPreTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
		if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive()) && (aBaseMetaTileEntity.getFrontFacing() != 1) && (aBaseMetaTileEntity.getCoverIDAtSide((byte) 1) == 0) && (!aBaseMetaTileEntity.getOpacityAtSide((byte) 1))) {
			final Random tRandom = aBaseMetaTileEntity.getWorld().rand;
			aBaseMetaTileEntity.getWorld().spawnParticle("smoke", (aBaseMetaTileEntity.getXCoord() + 0.8F) - (tRandom.nextFloat() * 0.6F), aBaseMetaTileEntity.getYCoord() + 0.3f + (tRandom.nextFloat() * 0.2F), (aBaseMetaTileEntity.getZCoord() + 1.2F) - (tRandom.nextFloat() * 1.6F), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void startSoundLoop(final byte aIndex, final double aX, final double aY, final double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 1) {
			GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(Integer.valueOf(201)), 10, 1.0F, aX, aY, aZ);
		}
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		byte tTier = (byte)Math.max(1, GT_Utility.getTier(getMaxInputVoltage()));
		//int processing = 8*tTier;
		int processing = 1;
		//Get inputs.
		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		for (int i = 0; i < (tInputList.size() - 1); i++) {
			for (int j = i + 1; j < tInputList.size(); j++) {
				if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
					if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
						tInputList.remove(j--);
					} else {
						tInputList.remove(i--);
						break;
					}
				}
			}
		}

		//Temp var
		final ItemStack[] tInputs = Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

		//Don't check the recipe if someone got around the output bus size check.
		if (this.mOutputBusses.size() != 5){
			return false;
		}
		for (int rx=0;rx<processing;rx++){
			//Make a recipe instance for the rest of the method.
			GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.findRecipe(this.getBaseMetaTileEntity(), false, 9223372036854775807L, null, tInputs);
			tRecipe = this.reduceRecipeTimeByPercentage(tRecipe, 60F);

			final int tValidOutputSlots = this.getValidOutputSlots(this.getBaseMetaTileEntity(), tRecipe, tInputs);
			Utils.LOG_WARNING("Maceration Stack - Valid Output Hatches: "+tValidOutputSlots);

			//More than or one input
			if ((tInputList.size() > 0) && (tValidOutputSlots >= 1)) {
				if ((tRecipe != null) && (tRecipe.isRecipeInputEqual(true, null, tInputs))) {
					if (processRecipeXTimes(processing, tRecipe)){
						
						//final ItemStack[] outputs = new ItemStack[tRecipe.mOutputs.length];
						
						
						
						//this.mOutputItems = outputs;
						this.mEfficiency = (10000 - ((getIdealStatus() - getRepairStatus()) * 1000));
						this.mEfficiencyIncrease = 10000;

						this.mEUt = (-4 * (1 << tTier - 1) * (1 << tTier - 1) * tTier / 2);
						this.mMaxProgresstime = Math.max(1, 256 / (1 << tTier - 1));
					}
					//this.mMaxProgresstime = Math.max(1, (tRecipe.mDuration));
					this.sendLoopStart((byte) 20);
					this.updateSlots();
				}
			}
		}		
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			return false;
		}
		int tAmount = 0;
		controller = false;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = 0; h < 6; h++) {
					if (!((i == 0) && (j == 0) && ((h > 0) && (h < 5))))//((h > 0)&&(h<5)) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))
					{
						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(7))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(7))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(7))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(7))) && (!this.ignoreController(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)))) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Returned False 1");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 7) {
								Utils.LOG_INFO("Returned False 2");
								return false;
							}
							tAmount++;
						}
					}
				}
			}
		}
		if ((this.mOutputHatches.size() != 0) || (this.mInputBusses.size() != 1) || (this.mOutputBusses.size() != 5)) {
			Utils.LOG_INFO("Returned False 3");
			return false;
		}
		final int height = this.getBaseMetaTileEntity().getYCoord();
		if (this.mInputBusses.get(0).getBaseMetaTileEntity().getYCoord() != height) {
			Utils.LOG_INFO("height: "+height+" | Returned False 4");
			return false;
		}
		final GT_MetaTileEntity_Hatch_OutputBus[] tmpHatches = new GT_MetaTileEntity_Hatch_OutputBus[5];
		for (int i = 0; i < this.mOutputBusses.size(); i++) {
			final int hatchNumber = this.mOutputBusses.get(i).getBaseMetaTileEntity().getYCoord() - 1 - height;
			if (tmpHatches[hatchNumber] == null) {
				tmpHatches[hatchNumber] = this.mOutputBusses.get(i);
			} else {
				Utils.LOG_INFO("Returned False 5");
				return false;
			}
		}
		this.mOutputBusses.clear();
		for (int i = 0; i < tmpHatches.length; i++) {
			this.mOutputBusses.add(tmpHatches[i]);
		}
		return tAmount >= 26;
	}

	public boolean ignoreController(final Block tTileEntity) {
		if (!controller && (tTileEntity == GregTech_API.sBlockMachines)) {
			return true;
		}
		return false;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(final ItemStack aStack) {
		return 25;
	}

	@Override
	public int getAmountOfOutputs() {
		return 16;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean isOverclockerUpgradable() {
		return true;
	}

	private boolean processRecipeXTimes(int times, GT_Recipe tRecipe){

		byte tTier = (byte)Math.max(1, GT_Utility.getTier(getMaxInputVoltage()));
		int j = 0;
		this.mOutputItems = new ItemStack[8 * tTier];	
		
	      for (int i = 0; (i < 256) && (j < this.mOutputItems.length); ++i) {
					if (i==0) {
						Utils.LOG_WARNING("Adding the default output");						
						this.mOutputItems[0] =  tRecipe.getOutput(0);						
						Utils.LOG_INFO("Ading output. "+i+" | "+tRecipe.mOutputs.length + " | "+this.mOutputItems[0].stackSize+" | "+tRecipe.getOutput(0).stackSize);
					}
					else if (this.getBaseMetaTileEntity().getRandomNumber(7500) < tRecipe.getOutputChance(i)){
						Utils.LOG_WARNING("Adding a bonus output");
						this.mOutputItems[i] = tRecipe.getOutput(i);
					}
					else {
						Utils.LOG_WARNING("Adding null output");
						this.mOutputItems[i] = null;	  
					}
	        	++j;
	        }		
		updateSlots();
		return true;
	}
}