package miscutil.core.xmod.gregtech.common.tileentities.machines.multi;

import static miscutil.core.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks.GTID;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;

import miscutil.core.block.ModBlocks;
import miscutil.core.xmod.gregtech.api.gui.GUI_MultiMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntityIndustrialPlatePress
extends GT_MetaTileEntity_MultiBlockBase {
	public GregtechMetaTileEntityIndustrialPlatePress(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityIndustrialPlatePress(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityIndustrialPlatePress(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Controller Block for the Material Press",
				"Size: 3x3x3 (Hollow)",
				"Controller (front centered)",
				"1x Input (anywhere)",
				"1x Output (anywhere)",
				"1x Energy Hatch (anywhere)",
				"1x Maintenance Hatch (anywhere)",
				"Material Press Machine Casings for the rest (16 at least!)"};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+4], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+4]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "CokeOven.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sBenderRecipes;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ArrayList<ItemStack> tInputList = getStoredInputs();
		for (ItemStack tInput : tInputList) {
			long tVoltage = getMaxInputVoltage();
			byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

			GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBenderRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], null, new ItemStack[]{tInput});
			if (tRecipe != null) {
				if (tRecipe.isRecipeInputEqual(true, null, new ItemStack[]{tInput})) {
					this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
					this.mEfficiencyIncrease = 10000;
					if (tRecipe.mEUt <= 16) {
						this.mEUt = (tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
						this.mMaxProgresstime = (tRecipe.mDuration / (1 << tTier - 1));
					} else {
						this.mEUt = tRecipe.mEUt;
						this.mMaxProgresstime = tRecipe.mDuration;
						while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
							this.mEUt *= 4;
							this.mMaxProgresstime /= 2;
						}
					}
					if (this.mEUt > 0) {
						this.mEUt = (-this.mEUt);
					}
					this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
					this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
					updateSlots();
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
            return false;
        }
        int tAmount = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 3; h++) {
                    if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                        if ((!addMaintenanceToMachineList(tTileEntity, GTID+4)) && (!addInputToMachineList(tTileEntity, GTID+4)) && (!addOutputToMachineList(tTileEntity, GTID+4)) && (!addEnergyInputToMachineList(tTileEntity, GTID+4))) {
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
                                return false;
                            }
                            if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 1) {
                                return false;
                            }
                            tAmount++;
                        }
                    }
                }
            }
        }
        return tAmount >= 16;
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
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}
}
