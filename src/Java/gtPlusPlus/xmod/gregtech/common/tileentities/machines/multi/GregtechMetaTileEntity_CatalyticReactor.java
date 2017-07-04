package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import static gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks2.GTID;

import java.util.ArrayList;

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
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_CatalyticReactor extends GT_MetaTileEntity_MultiBlockBase {

	private boolean completedCycle = false;

	public GregtechMetaTileEntity_CatalyticReactor(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_CatalyticReactor(final String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Catalytic Chemical Reactor",
				"Size(WxHxD): 5x2x5", "Controller (Front middle at bottom)",
				"6x Input Hatch (One of base layer)",
				"3x Output Hatch (One of base layer)",
				"1x Maintenance Hatch (One of base layer)",
				"1x MV or better Muffler (One of base layer)",
				"1x Energy Hatch (One of base layer)"
				};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{TexturesGtBlock.CASING_BLOCKS_GTPP[17], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
		}
		return new ITexture[]{TexturesGtBlock.CASING_BLOCKS_GTPP[17]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "LFTR.png");
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		if ((this.mInventory[1] == null) || (this.mInventory[1].isItemEqual(GT_ModHandler.getIC2Item("miningPipe", 1L)) && (this.mInventory[1].stackSize < this.mInventory[1].getMaxStackSize()))) {
			final ArrayList<ItemStack> tItems = this.getStoredInputs();
			for (ItemStack tStack : tItems) {
				if (tStack.isItemEqual(GT_ModHandler.getIC2Item("miningPipe", 1L))) {
					if (tStack.stackSize < 2) {
						tStack = null;
					} else {
						tStack.stackSize--;
					}

				}
				if (this.mInventory[1] == null) {
					this.mInventory[1] = GT_ModHandler.getIC2Item("miningPipe", 1L);
				} else {
					this.mInventory[1].stackSize++;
				}
			}
		}
		FluidStack tFluid = null;//GT_Utility.getUndergroundOil(getBaseMetaTileEntity().getWorld(), getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getZCoord());
		if (tFluid == null) {
			return false;
		}
		if ((this.getYOfPumpHead() > 0) && (this.getBaseMetaTileEntity().getBlockOffset(ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetX, this.getYOfPumpHead() - 1 - this.getBaseMetaTileEntity().getYCoord(), ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetZ) != Blocks.bedrock)) {
			if (this.completedCycle) {
				this.moveOneDown();
			}
			tFluid = null;
			if ((this.mEnergyHatches.size() > 0) && (this.mEnergyHatches.get(0).getEUVar() > (512 + (this.getMaxInputVoltage() * 4)))) {
				this.completedCycle = true;
			}
		} else if (tFluid.amount < 5000) {
			this.stopMachine();
			return false;
		} else {
			tFluid.amount = tFluid.amount / 5000;
		}
		final long tVoltage = this.getMaxInputVoltage();
		final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		this.mEfficiency = (10000 - ((this.getIdealStatus() - this.getRepairStatus()) * 1000));
		this.mEfficiencyIncrease = 10000;
		final int tEU = 24;
		final int tDuration = 160;
		if (tEU <= 16) {
			this.mEUt = (tEU * (1 << (tTier - 1)) * (1 << (tTier - 1)));
			this.mMaxProgresstime = (tDuration / (1 << (tTier - 1)));
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
		final int xDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetZ;
		final int yHead = this.getYOfPumpHead();
		if (yHead < 1) {
			return false;
		}
		if (this.getBaseMetaTileEntity().getBlock(this.getBaseMetaTileEntity().getXCoord() + xDir, yHead - 1, this.getBaseMetaTileEntity().getZCoord() + zDir) == Blocks.bedrock) {
			return false;
		}
		if (!(this.getBaseMetaTileEntity().getWorld().setBlock(this.getBaseMetaTileEntity().getXCoord() + xDir, yHead - 1, this.getBaseMetaTileEntity().getZCoord() + zDir, GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipeTip", 1L))))) {
			return false;
		}
		if (yHead != this.getBaseMetaTileEntity().getYCoord()) {
			this.getBaseMetaTileEntity().getWorld().setBlock(this.getBaseMetaTileEntity().getXCoord() + xDir, yHead, this.getBaseMetaTileEntity().getZCoord() + zDir, GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipe", 1L)));
		}
		this.getBaseMetaTileEntity().decrStackSize(1, 1);
		return true;
	}

	private int getYOfPumpHead() {
		final int xDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetZ;
		int y = this.getBaseMetaTileEntity().getYCoord() - 1;
		while (this.getBaseMetaTileEntity().getBlock(this.getBaseMetaTileEntity().getXCoord() + xDir, y, this.getBaseMetaTileEntity().getZCoord() + zDir) == GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipe", 1L))) {
			y--;
		}
		if (y == (this.getBaseMetaTileEntity().getYCoord() - 1)) {
			if (this.getBaseMetaTileEntity().getBlock(this.getBaseMetaTileEntity().getXCoord() + xDir, y, this.getBaseMetaTileEntity().getZCoord() + zDir) != GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipeTip", 1L))) {
				return y + 1;
			}
		} else if ((this.getBaseMetaTileEntity().getBlock(this.getBaseMetaTileEntity().getXCoord() + xDir, y, this.getBaseMetaTileEntity().getZCoord() + zDir) != GT_Utility
				.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipeTip", 1L))) && (this.mInventory[1] != null) && (this.mInventory[1].stackSize > 0) && GT_Utility.areStacksEqual(this.mInventory[1], GT_ModHandler.getIC2Item("miningPipe", 1L))) {
			this.getBaseMetaTileEntity().getWorld().setBlock(this.getBaseMetaTileEntity().getXCoord() + xDir, y, this.getBaseMetaTileEntity().getZCoord() + zDir,
					GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipeTip", 1L)));
			this.getBaseMetaTileEntity().decrStackSize(0, 1);
		}
		return y;
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
					if ((!this.addToMachineList(tTileEntity, GTID+2)) && (!this.addEnergyInputToMachineList(tTileEntity, GTID+1))) {

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
				if ((!this.addToMachineList(tTileEntity2, GTID+2)) && (!this.addEnergyInputToMachineList(tTileEntity2, GTID+1))) {

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
		return 0;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public int getAmountOfOutputs() {
		return 3;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_CatalyticReactor(this.mName);
	}

}