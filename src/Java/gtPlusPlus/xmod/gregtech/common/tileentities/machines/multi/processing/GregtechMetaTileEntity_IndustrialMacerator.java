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
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
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
				"Increased output chances on % outputs",
				"Processes 8*tier materials at a time",
				"ULV = Tier 0, LV = Tier 1, etc.",
				"-------------------------------------------------------",
				"Size[WxHxL]: 3x6x3 (Hollow)",
				"Controller (Center Bottom)",
				"1x Input Bus (Any bottom layer casing)",
				"5x Output Bus (Any casing besides bottom layer)",
				"1x Maintenance Hatch (Any casing)",
				"1x Energy Hatch (Any casing)",
				"1x Muffler Hatch (Any casing)",
				"Maceration Stack Casings for the rest (26 at least!)",
				getPollutionTooltip(),
				CORE.GT_Tooltip};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(201));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(7)], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_MatterFab_Active : TexturesGtBlock.Overlay_MatterFab)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(7)]};
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
	public boolean checkRecipe(final ItemStack aStack) {
		final long tVoltage = getMaxInputVoltage();
		final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		final int maxParallelRecipes = Math.max(1, 8 * tTier);

		return checkRecipeGeneric(maxParallelRecipes, 100, 60, 7500);
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
						if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(7))) &&(!this.addMufflerToMachineList(tTileEntity, TAE.GTPP_INDEX(7))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(7))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(7))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(7))) && (!this.ignoreController(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)))) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Logger.INFO("Returned False 1");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 7) {
								Logger.INFO("Returned False 2");
								return false;
							}
							tAmount++;
						}
					}
				}
			}
		}
		if ((this.mOutputHatches.size() != 0) ||(this.mMufflerHatches.size() == 0) || (this.mInputBusses.size() != 1) || (this.mOutputBusses.size() != 5)) {
			Logger.INFO("Returned False 3");
			return false;
		}
		final int height = this.getBaseMetaTileEntity().getYCoord();
		if (this.mInputBusses.get(0).getBaseMetaTileEntity().getYCoord() != height) {
			Logger.INFO("height: "+height+" | Returned False 4");
			return false;
		}
		final GT_MetaTileEntity_Hatch_OutputBus[] tmpHatches = new GT_MetaTileEntity_Hatch_OutputBus[5];
		for (int i = 0; i < this.mOutputBusses.size(); i++) {
			final int hatchNumber = this.mOutputBusses.get(i).getBaseMetaTileEntity().getYCoord() - 1 - height;
			if (tmpHatches[hatchNumber] == null) {
				tmpHatches[hatchNumber] = this.mOutputBusses.get(i);
			} else {
				Logger.INFO("Returned False 5");
				return false;
			}
		}
		this.mOutputBusses.clear();
		for (int i = 0; i < tmpHatches.length; i++) {
			this.mOutputBusses.add(tmpHatches[i]);
		}
		return tAmount >= 24;
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

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 20;
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