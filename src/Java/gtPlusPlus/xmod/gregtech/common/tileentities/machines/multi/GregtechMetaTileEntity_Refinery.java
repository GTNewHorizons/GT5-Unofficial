package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import static gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks2.GTID;
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
import gtPlusPlus.core.util.Utils;

import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_Refinery extends GT_MetaTileEntity_MultiBlockBase {

	private boolean completedCycle = false;

	public GregtechMetaTileEntity_Refinery(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_Refinery(String aName) {
		super(aName);
	}

	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Fission Fuel Processing Unit",
				"Size(WxHxD): 3x9x3", "Controller (Front middle at bottom)",
				"3x2x3 Base platform of Hastelloy-X",
				"1x7x1 Incoloy-DS Fluid Containment Block pillar (Center of base)",
				"1x4x1 Hastelloy-N Sealant Blocks (Each Incoloy-DS Fluid Containment side and on top)",
				"1x1x1 Zeron-100 Reactor Shielding (Second Sealant Tower layer, Surrounding Fluid Containment)",
				"2x Input Hatch (One of base platform)",
				"2x Output Hatch (One of base platform)",
				"1x Output Bus (One of base platform)",
				"2x Maintenance Hatch (One of base platform)",
				"1x ZPM Muffler (One of base platform)",
		"1x Energy Hatch (One of base platform)"};
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+2], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+2]};
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LFTR.png");
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		if (mInventory[1] == null || (mInventory[1].isItemEqual(GT_ModHandler.getIC2Item("miningPipe", 1L)) && mInventory[1].stackSize < mInventory[1].getMaxStackSize())) {
			ArrayList<ItemStack> tItems = getStoredInputs();
			for (ItemStack tStack : tItems) {
				if (tStack.isItemEqual(GT_ModHandler.getIC2Item("miningPipe", 1L))) {
					if (tStack.stackSize < 2) {
						tStack = null;
					} else {
						tStack.stackSize--;
					}

				}
				if (mInventory[1] == null) {
					mInventory[1] = GT_ModHandler.getIC2Item("miningPipe", 1L);
				} else {
					mInventory[1].stackSize++;
				}
			}
		}
		FluidStack tFluid = null;//GT_Utility.getUndergroundOil(getBaseMetaTileEntity().getWorld(), getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getZCoord());
		if (tFluid == null) {
			return false;
		}
		if (getYOfPumpHead() > 0 && getBaseMetaTileEntity().getBlockOffset(ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetX, getYOfPumpHead() - 1 - getBaseMetaTileEntity().getYCoord(), ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetZ) != Blocks.bedrock) {
			if (completedCycle) {
				moveOneDown();
			}
			tFluid = null;
			if (mEnergyHatches.size() > 0 && mEnergyHatches.get(0).getEUVar() > (512 + getMaxInputVoltage() * 4))
				completedCycle = true;
		} else if (tFluid.amount < 5000) {
			stopMachine();
			return false;
		} else {
			tFluid.amount = tFluid.amount / 5000;
		}
		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;
		int tEU = 24;
		int tDuration = 160;
		if (tEU <= 16) {
			this.mEUt = (tEU * (1 << tTier - 1) * (1 << tTier - 1));
			this.mMaxProgresstime = (tDuration / (1 << tTier - 1));
		} else {
			this.mEUt = tEU;
			this.mMaxProgresstime = tDuration;
			while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
				this.mEUt *= 4;
				this.mMaxProgresstime /= 2;
			}
		}
		if (this.mEUt > 0) {
			this.mEUt = (-this.mEUt);
		}
		this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
		this.mOutputFluids = new FluidStack[]{tFluid};
		return true;
	}

	private boolean moveOneDown() {
		if ((this.mInventory[1] == null) || (this.mInventory[1].stackSize < 1)
				|| (!GT_Utility.areStacksEqual(this.mInventory[1], GT_ModHandler.getIC2Item("miningPipe", 1L)))) {
			return false;
		}
		int xDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetZ;
		int yHead = getYOfPumpHead();
		if (yHead < 1) {
			return false;
		}
		if (getBaseMetaTileEntity().getBlock(getBaseMetaTileEntity().getXCoord() + xDir, yHead - 1, getBaseMetaTileEntity().getZCoord() + zDir) == Blocks.bedrock) {
			return false;
		}
		if (!(getBaseMetaTileEntity().getWorld().setBlock(getBaseMetaTileEntity().getXCoord() + xDir, yHead - 1, getBaseMetaTileEntity().getZCoord() + zDir, GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipeTip", 1L))))) {
			return false;
		}
		if (yHead != getBaseMetaTileEntity().getYCoord()) {
			getBaseMetaTileEntity().getWorld().setBlock(getBaseMetaTileEntity().getXCoord() + xDir, yHead, getBaseMetaTileEntity().getZCoord() + zDir, GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipe", 1L)));
		}
		getBaseMetaTileEntity().decrStackSize(1, 1);
		return true;
	}

	private int getYOfPumpHead() {
		int xDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetZ;
		int y = getBaseMetaTileEntity().getYCoord() - 1;
		while (getBaseMetaTileEntity().getBlock(getBaseMetaTileEntity().getXCoord() + xDir, y, getBaseMetaTileEntity().getZCoord() + zDir) == GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipe", 1L))) {
			y--;
		}
		if (y == getBaseMetaTileEntity().getYCoord() - 1) {
			if (getBaseMetaTileEntity().getBlock(getBaseMetaTileEntity().getXCoord() + xDir, y, getBaseMetaTileEntity().getZCoord() + zDir) != GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipeTip", 1L))) {
				return y + 1;
			}
		} else if (getBaseMetaTileEntity().getBlock(getBaseMetaTileEntity().getXCoord() + xDir, y, getBaseMetaTileEntity().getZCoord() + zDir) != GT_Utility
				.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipeTip", 1L)) && this.mInventory[1] != null && this.mInventory[1].stackSize > 0 && GT_Utility.areStacksEqual(this.mInventory[1], GT_ModHandler.getIC2Item("miningPipe", 1L))) {
			getBaseMetaTileEntity().getWorld().setBlock(getBaseMetaTileEntity().getXCoord() + xDir, y, getBaseMetaTileEntity().getZCoord() + zDir,
					GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipeTip", 1L)));
			getBaseMetaTileEntity().decrStackSize(0, 1);
		}
		return y;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					int Y = 0;
					if ((xDir + i != 0) || (zDir + j != 0)) {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, Y, zDir + j);
						if ((!addToMachineList(tTileEntity, GTID+2)) && (!addEnergyInputToMachineList(tTileEntity, GTID+1))) {

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
						IGregTechTileEntity tTileEntity2 = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, Y, zDir + j);
						if ((!addToMachineList(tTileEntity2, GTID+2)) && (!addEnergyInputToMachineList(tTileEntity2, GTID+1))) {

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
		
		if (mInputHatches.size() != 4 || mOutputHatches.size() != 2 ||
				mOutputBusses.size() != 1 || mMufflerHatches.size() != 1 ||
				mMaintenanceHatches.size() != 2 || mEnergyHatches.size() < 1){
			Utils.LOG_INFO("Wrong Hatch count.");
			return false;
		}
		if (mMufflerHatches.size() == 1){
			if (mMufflerHatches.get(0).mTier < 7){
				Utils.LOG_INFO("Your Muffler must be AT LEAST ZPM tier or higher.");
			}
		}
		Utils.LOG_INFO("Multiblock Formed.");		
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 3;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Refinery(this.mName);
	}

}