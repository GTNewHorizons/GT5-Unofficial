package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_IndustrialCokeOven
extends GregtechMeta_MultiBlockBase {
	private int mLevel = 0;

	public GregtechMetaTileEntity_IndustrialCokeOven(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialCokeOven(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialCokeOven(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Processes Logs and Coal into Charcoal and Coal Coke.",
				"Controller Block for the Industrial Coke Oven",
				"Gain 4% speed bonus per voltage tier increased",
				"Process 12x materials with Heat Resistant Casings",
				"Or 24x materials with Heat Proof Casings",
				"Size: 3x3x3 (Hollow)",
				"Controller (front middle at bottom)",
				"8x Heat Resistant/Proof Coke Oven Casings (middle Layer, hollow)",
				"1x Input Hatch (one of bottom)",
				"1x Output Hatch (one of bottom)",
				"1x Input Bus (one of bottom)",
				"1x Output Bus (one of bottom)",
				"1x Energy Hatch (one of bottom) [EV or better recommended]",
				"1x Maintenance Hatch (one of bottom)",
				"1x Muffler Hatch (top middle)",
				"Structural Coke Oven Casings for the rest",
				CORE.GT_Tooltip};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(207));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "CokeOven.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return Recipe_GT.Gregtech_Recipe_Map.sCokeOvenRecipes;

	}

	/* @Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }*/

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric(this.mLevel * 12, (100-(Utils.calculateVoltageTier(this.getMaxInputVoltage())*4)), 0);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		this.mLevel = 0;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			return false;
		}
		this.addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 2, zDir), TAE.GTPP_INDEX(1));

		final byte tUsedMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, 1, zDir);
		switch (tUsedMeta) {
		case 2:
			this.mLevel = 1;
			break;
		case 3:
			this.mLevel = 2;
			break;
		default:
			return false;
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i != 0) || (j != 0)) {
					if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j) != ModBlocks.blockCasingsMisc) {
						return false;
					}
					if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j) != tUsedMeta) {
						return false;
					}
					if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j) != ModBlocks.blockCasingsMisc) {
						return false;
					}
					if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j) != 1) {
						return false;
					}
				}
			}
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (((xDir + i) != 0) || ((zDir + j) != 0)) {
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
					if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(1))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(1))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(1))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(1)))) {
						if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != ModBlocks.blockCasingsMisc) {
							return false;
						}
						if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j) != 1) {
							return false;
						}
					}
				}
			}
		}
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

	/* @Override
	public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }*/

	@Override
	public int getAmountOfOutputs() {
		return 24;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}
}
