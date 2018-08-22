package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class GT_MetaTileEntity_LargeChemicalReactor extends GT_MetaTileEntity_MultiBlockBase {

	private final int CASING_INDEX = 176;

	public GT_MetaTileEntity_LargeChemicalReactor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GT_MetaTileEntity_LargeChemicalReactor(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_LargeChemicalReactor(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Controller block for the Large Chemical Reactor",
				"Has the same recipes as the Chemical Reactor",
				"Does not lose efficiency when overclocked",
				"Accepts fluids instead of fluid cells",
				"Size(WxHxD): 3x3x3",
				"3x3x3 of Chemically Inert Machine Casings (hollow, min 8!)",
				"Controller (Front centered)",
				"1x PTFE Pipe Machine Casing (inside the hollow casings)",
				"1x Cupronickel Coil Block (next to PTFE Pipe Machine Casing)",
				"1x Input Bus/Hatch (Any inert casing)",
				"1x Output Bus/Hatch (Any inert casing)",
				"1x Maintenance Hatch (Any inert casing)",
				"1x Energy Hatch (Any inert casing)" };
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive,
			boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] {
					Textures.BlockIcons.casingTexturePages[1][48],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE
							: Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR) };
		}
		return new ITexture[] { Textures.BlockIcons.casingTexturePages[1][48] };
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeChemicalReactor.png");
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ArrayList<ItemStack> tInputList = getStoredInputs();
		int tInputList_sS = tInputList.size();
		for (int i = 0; i < tInputList_sS - 1; i++) {
			for (int j = i + 1; j < tInputList_sS; j++) {
				if (GT_Utility.areStacksEqual((ItemStack) tInputList.get(i), (ItemStack) tInputList.get(j))) {
					if (((ItemStack) tInputList.get(i)).stackSize >= ((ItemStack) tInputList.get(j)).stackSize) {
						tInputList.remove(j--);
						tInputList_sS = tInputList.size();
					} else {
						tInputList.remove(i--);
						tInputList_sS = tInputList.size();
						break;
					}
				}
			}
		}
		tInputList.add(mInventory[1]);
		ItemStack[] inputs = tInputList.toArray(new ItemStack[tInputList.size()]);

		ArrayList<FluidStack> tFluidList = getStoredFluids();
		int tFluidList_sS = tFluidList.size();
		for (int i = 0; i < tFluidList_sS - 1; i++) {
			for (int j = i + 1; j < tFluidList_sS; j++) {
				if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
					if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
						tFluidList.remove(j--);
						tFluidList_sS = tFluidList.size();
					} else {
						tFluidList.remove(i--);
						tFluidList_sS = tFluidList.size();
						break;
					}
				}
			}
		}
		FluidStack[] fluids = tFluidList.toArray(new FluidStack[tFluidList.size()]);

		if (inputs.length > 0 || fluids.length > 0) {
			long voltage = getMaxInputVoltage();
			byte tier = (byte) Math.max(1, GT_Utility.getTier(voltage));
			GT_Recipe recipe = GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.findRecipe(getBaseMetaTileEntity(), false,
					false, gregtech.api.enums.GT_Values.V[tier], fluids, inputs);
			if (recipe != null && recipe.isRecipeInputEqual(true, fluids, inputs)) {
				this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
				this.mEfficiencyIncrease = 10000;

				int EUt = recipe.mEUt;
				int maxProgresstime = recipe.mDuration;

				while (EUt <= gregtech.api.enums.GT_Values.V[tier - 1] && maxProgresstime > 2) {
					EUt *= 4;
					maxProgresstime /= 4;
				}
				if (maxProgresstime < 2) {
					maxProgresstime = 2;
					EUt = recipe.mEUt * recipe.mDuration / 2;
				}

				this.mEUt = -EUt;
				this.mMaxProgresstime = maxProgresstime;
				this.mOutputItems = recipe.mOutputs;
				this.mOutputFluids = recipe.mFluidOutputs;
				this.updateSlots();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int casingAmount = 0;
		boolean hasHeatingCoil = false;
		// x=width, z=depth, y=height
		for (int x = -1 + xDir; x <= xDir + 1; x++) {
			for (int z = -1 + zDir; z <= zDir + 1; z++) {
				for (int y = -1; y <= 1; y++) {
					if (x == 0 && y == 0 && z == 0) {
						continue;
					}
					IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
					Block block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
					int centerCoords = 0;
					if (x == xDir) {
						centerCoords++;
					}
					if (y == 0) {
						centerCoords++;
					}
					if (z == zDir) {
						centerCoords++;
					}
					if (centerCoords == 3) {
						if (block == GregTech_API.sBlockCasings8 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 1) {
							continue;
						} else {
							return false;
						}
					}
					if (centerCoords == 2 && block == GregTech_API.sBlockCasings5 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 0) {
						hasHeatingCoil = true;
						continue;
					}
					if (!addInputToMachineList(tileEntity, CASING_INDEX) && !addOutputToMachineList(tileEntity, CASING_INDEX)
							&& !addMaintenanceToMachineList(tileEntity, CASING_INDEX)
							&& !addEnergyInputToMachineList(tileEntity, CASING_INDEX)) {
						if (block == GregTech_API.sBlockCasings8 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 0) {
							casingAmount++;
						} else {
							return false;
						}
					}

				}
			}

		}
		return casingAmount >= 8 && hasHeatingCoil;
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
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

}
