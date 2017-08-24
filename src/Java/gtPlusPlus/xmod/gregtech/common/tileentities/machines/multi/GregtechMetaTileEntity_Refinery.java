package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_Refinery extends GT_MetaTileEntity_MultiBlockBase {

	private boolean completedCycle = false;

	public GregtechMetaTileEntity_Refinery(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_Refinery(final String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Fission Fuel Processing Unit",
				"Size(WxHxD): 3x9x3", "Controller (Front middle at bottom)",
				"3x2x3 Base platform of Hastelloy-X",
				"1x7x1 Incoloy-DS Fluid Containment Block pillar (Center of base)",
				"1x4x1 Hastelloy-N Sealant Blocks (Each Incoloy-DS Fluid Containment side and on top)",
				"1x1x1 Zeron-100 Reactor Shielding (Second Sealant Tower layer, Surrounding Fluid Containment)",
				"4x Input Hatch (One of base platform)",
				"2x Output Hatch (One of base platform)",
				"1x Output Bus (One of base platform)",
				"2x Maintenance Hatch (One of base platform)",
				"1x ZPM or better Muffler (One of base platform)",
				"1x Energy Hatch (One of base platform)",
				CORE.GT_Tooltip
		};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(18)], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(18)]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "LFTR.png");
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				int Y = 0;
				if (((xDir + i) != 0) || ((zDir + j) != 0)) {
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, Y, zDir + j);
					if ((!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(18))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(18)))) {

						if (aBaseMetaTileEntity.getBlockOffset(xDir + i, Y, zDir + j) != ModBlocks.blockCasings2Misc) {
							Utils.LOG_INFO("Wrong Block.");
							return false;
						}
						if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, Y, zDir + j) != 2) {
							Utils.LOG_INFO("Wrong Meta 1.");
							return false;
						}
					}
					else {
						Utils.LOG_INFO("Added Hatch. "+tTileEntity.getInventoryName());
					}
				}
				Y = 1;
				Utils.LOG_INFO("Checking at Y+1 as well.");
				final IGregTechTileEntity tTileEntity2 = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, Y, zDir + j);
				if ((!this.addToMachineList(tTileEntity2, TAE.GTPP_INDEX(18))) && (!this.addEnergyInputToMachineList(tTileEntity2, TAE.GTPP_INDEX(18)))) {

					if (aBaseMetaTileEntity.getBlockOffset(xDir + i, Y, zDir + j) != ModBlocks.blockCasings2Misc) {
						Utils.LOG_INFO("Wrong Block.");
						return false;
					}
					if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, Y, zDir + j) != 2) {
						Utils.LOG_INFO("Wrong Meta 1.");
						return false;
					}
				}
				else {
					Utils.LOG_INFO("Added Hatch. "+tTileEntity2.getInventoryName());
				}
			}
		}

		for (int y = 2; y < 6; y++) {
			if (aBaseMetaTileEntity.getBlockOffset(xDir, y, zDir) != ModBlocks.blockCasings2Misc) { //Must Define meta for center blocks
				Utils.LOG_INFO("Wrong Block.");
				return false;
			}
			if (aBaseMetaTileEntity.getMetaIDOffset(xDir, y, zDir) != 3) {
				Utils.LOG_INFO("Wrong Meta. 2");
				return false;
			}
			if (aBaseMetaTileEntity.getBlockOffset(xDir + 1, y, zDir) != ModBlocks.blockCasings2Misc) {
				Utils.LOG_INFO("Wrong Block.1");
				if (y==3){
					if (aBaseMetaTileEntity.getBlockOffset(xDir + 1, y, zDir) == ModBlocks.blockCasingsMisc) {
						if (aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, y, zDir) != 13) {
							return false;
						}
						Utils.LOG_INFO("Found Zeron-Casing.");
					}
				}
				else {
					Utils.LOG_INFO("debug.1");
					return false;
				}
			}

			if (aBaseMetaTileEntity.getBlockOffset(xDir - 1, y, zDir) != ModBlocks.blockCasings2Misc) {
				Utils.LOG_INFO("Wrong Block.2");
				if (y==3){
					if (aBaseMetaTileEntity.getBlockOffset(xDir + 1, y, zDir) == ModBlocks.blockCasingsMisc) {
						if (aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, y, zDir) != 13) {
							return false;
						}
						Utils.LOG_INFO("Found Zeron-Casing.");
					}
				}
				else {
					Utils.LOG_INFO("debug.2");
					return false;
				}
			}

			if (aBaseMetaTileEntity.getBlockOffset(xDir, y, zDir + 1) != ModBlocks.blockCasings2Misc) {
				Utils.LOG_INFO("Wrong Block.3");
				if (y==3){
					if (aBaseMetaTileEntity.getBlockOffset(xDir + 1, y, zDir) == ModBlocks.blockCasingsMisc) {
						if (aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, y, zDir) != 13) {
							return false;
						}
						Utils.LOG_INFO("Found Zeron-Casing.");
					}
				}
				else {
					Utils.LOG_INFO("debug.3");
					return false;
				}
			}
			if (aBaseMetaTileEntity.getBlockOffset(xDir, y, zDir - 1) != ModBlocks.blockCasings2Misc) {
				Utils.LOG_INFO("Wrong Block.4");
				if (y==3){
					if (aBaseMetaTileEntity.getBlockOffset(xDir + 1, y, zDir) == ModBlocks.blockCasingsMisc) {
						if (aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, y, zDir) != 13) {
							return false;
						}
						Utils.LOG_INFO("Found Zeron-Casing.");
					}
				}
				else {
					Utils.LOG_INFO("debug.4");
					return false;
				}
			}
			if (aBaseMetaTileEntity.getBlockOffset(xDir, y + 3, zDir) != ModBlocks.blockCasings2Misc) {
				Utils.LOG_INFO("Wrong Block.5");
				return false;
			}
			if (aBaseMetaTileEntity.getMetaIDOffset(xDir, y + 3, zDir) != 3) {
				Utils.LOG_INFO("Wrong Meta. 3");
				return false;
			}
		}

		if ((this.mInputHatches.size() != 4) || (this.mOutputHatches.size() != 2) ||
				(this.mOutputBusses.size() != 1) || (this.mMufflerHatches.size() != 1) ||
				(this.mMaintenanceHatches.size() != 2) || (this.mEnergyHatches.size() < 1)){
			Utils.LOG_INFO("Wrong Hatch count.");
			return false;
		}
		if (this.mMufflerHatches.size() == 1){
			if (this.mMufflerHatches.get(0).mTier < 7){
				Utils.LOG_INFO("Your Muffler must be AT LEAST ZPM tier or higher.");
			}
		}
		Utils.LOG_INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 200;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public int getAmountOfOutputs() {
		return 5;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Refinery(this.mName);
	}

}