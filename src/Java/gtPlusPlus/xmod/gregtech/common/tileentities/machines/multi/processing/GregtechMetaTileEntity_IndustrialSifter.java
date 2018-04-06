package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_IndustrialSifter
extends GregtechMeta_MultiBlockBase {
	private boolean controller;

	public GregtechMetaTileEntity_IndustrialSifter(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialSifter(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialSifter(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Industrial Sifter",
				"400% faster than single-block machines of the same voltage",
				"Only uses 75% of the eu/t normally required",
				"Processes four items per voltage tier",
				"Increased output chances on % outputs",
				"Size[WxHxL]: 5x3x5",
				"Controller (Center Bottom)",
				"1x Input Bus (Any top or bottom edge casing)",
				"4x Output Bus (Any top or bottom edge casing)",
				"1x Maintenance Hatch (Any top or bottom edge casing)",
				"1x Energy Hatch (Any top or bottom edge casing)",
				"18x Sieve Grate (Top and Middle 3x3)",
				"Sieve Casings for the rest (47)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(21)], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(21)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MacerationStack.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sSifterRecipes;
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
			if (tRandom.nextFloat() > 0.4) return;

			final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
			final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;

			aBaseMetaTileEntity.getWorld().spawnParticle("smoke",
					(aBaseMetaTileEntity.getXCoord() + xDir + 2.1F) - (tRandom.nextFloat() * 3.2F),
					aBaseMetaTileEntity.getYCoord() + 2.5f + (tRandom.nextFloat() * 1.2F),
					(aBaseMetaTileEntity.getZCoord() + zDir + 2.1F) - (tRandom.nextFloat() * 3.2F),
					0.0, 0.0, 0.0);

		}
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric((4* GT_Utility.getTier(this.getMaxInputVoltage())), 75, 400, 8800);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		Logger.MACHINE_INFO("Checking structure for Industrial Sifter.");
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;

		int tAmount = 0;
		this.controller = false;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				for (int h = 0; h < 3; h++) {

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);

					String sHeight = "";
					if (h == 2){
						sHeight = "top";
					}
					else if (h == 1){
						sHeight = "middle";
					}
					else {
						sHeight = "bottom";
					}

					// Sifter Floor/Roof inner 3x3
					if (((i != -2) && (i != 2)) && ((j != -2) && (j != 2))) {
						if (h != 0){
							if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(21))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
									Logger.MACHINE_INFO("Sifter Casing(s) Missing from one of the "+sHeight+" layers inner 3x3.");
									Logger.MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 6) {
									Logger.MACHINE_INFO("Sifter Casing(s) Missing from one of the "+sHeight+" layers inner 3x3. Wrong Meta for Casing. Found:"+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" with meta:"+aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
									return false;
								}
							}
						}
						else {
							if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(21))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
									Logger.MACHINE_INFO("Sifter Casing(s) Missing from one of the "+sHeight+" layers inner 3x3.");
									Logger.MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 5) {
									Logger.MACHINE_INFO("Sifter Casing(s) Missing from one of the "+sHeight+" layers inner 3x3. Wrong Meta for Casing. Found:"+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" with meta:"+aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
									return false;
								}
								tAmount++;
							}
						}
					}
					else {
						//Dealt with inner 5x5, now deal with the exterior.
						//Deal with all 4 sides (Sifter walls)
						boolean checkController = false;
						if (((xDir + i) != 0) || (((zDir + j) != 0) && (h == 0))) {//no controller
							checkController = true;
						}
						else {
							checkController = false;
						}

						if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(21))) {
							if (!checkController){
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
									if ((tTileEntity instanceof GregtechMetaTileEntity_IndustrialSifter) || (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) == GregTech_API.sBlockMachines)){
										if (h != 0){
											Logger.MACHINE_INFO("Found a secondary controller at the wrong Y level.");
											return false;
										}
									}
									else {
										Logger.MACHINE_INFO("Sifter Casings Missing from somewhere in the "+sHeight+" layer edge.");
										Logger.MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
								}

								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 5) {
									if ((tTileEntity instanceof GregtechMetaTileEntity_IndustrialSifter) || (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) == GregTech_API.sBlockMachines)){

									}
									else {
										Logger.MACHINE_INFO("Sifter Casings Missing from somewhere in the "+sHeight+" layer edge.");
										Logger.MACHINE_INFO("Incorrect Meta value for block, expected 5.");
										Logger.MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j)+".");
										return false;
									}
								}
							}
							tAmount++;
						}
						else {
							tAmount++;
						}
					}
				}
			}
		}
		if ((this.mInputBusses.size() != 1) || (this.mOutputBusses.size() != 4)
				|| (this.mMaintenanceHatches.size() != 1) || (this.mEnergyHatches.size() < 1)) {
			Logger.INFO("Returned False 3");
			Logger.INFO("Input Buses: "+this.mInputBusses.size()+" | expected: 1");
			Logger.INFO("Output Buses: "+this.mOutputBusses.size()+" | expected: 4");
			Logger.INFO("Energy Hatches: "+this.mEnergyHatches.size()+" | expected: 1");
			Logger.INFO("Maint. hatches: "+this.mMaintenanceHatches.size()+" | expected: 1");
			return false;
		}
		final int height = this.getBaseMetaTileEntity().getYCoord();

		final GT_MetaTileEntity_Hatch_OutputBus[] tmpHatches = new GT_MetaTileEntity_Hatch_OutputBus[4];
		for (int i = 0; i < this.mOutputBusses.size(); i++) {
			final int hatchNumber = this.mOutputBusses.get(i).getBaseMetaTileEntity().getYCoord() - 1 - height;
			if (tmpHatches[i] == null) {
				tmpHatches[i] = this.mOutputBusses.get(i);
			} else {
				Logger.MACHINE_INFO("Returned False 5 - "+this.mOutputBusses.size());
				return false;
			}
		}
		this.mOutputBusses.clear();
		for (int i = 0; i < tmpHatches.length; i++) {
			this.mOutputBusses.add(tmpHatches[i]);
		}

		Logger.INFO("Industrial Sifter - Structure Built? "+(tAmount>=35));

		return tAmount >= 35;
	}

	public boolean ignoreController(final Block tTileEntity) {
		if (!this.controller && (tTileEntity == GregTech_API.sBlockMachines)) {
			return true;
		}
		return false;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
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
}