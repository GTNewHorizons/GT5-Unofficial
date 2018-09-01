package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Naquadah;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMTE_LargeNaqReactor extends GregtechMeta_MultiBlockBase {
	
	public ArrayList<GT_MetaTileEntity_Hatch_Naquadah> mNaqHatches = new ArrayList<GT_MetaTileEntity_Hatch_Naquadah>();
	public static String[] mCasingName = new String[5];
	public static String mHatchName = "Naquadah Fuel Hatch";

	public GregtechMTE_LargeNaqReactor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		mCasingName[0] = ItemUtils.getLocalizedNameOfBlock(getCasing(4), 0);
		mCasingName[1] = ItemUtils.getLocalizedNameOfBlock(getCasing(4), 1);
		mCasingName[2] = ItemUtils.getLocalizedNameOfBlock(getCasing(4), 2);
		mCasingName[3] = ItemUtils.getLocalizedNameOfBlock(getCasing(3), 15);
		mCasingName[4] = ItemUtils.getLocalizedNameOfBlock(getCasing(3), 3);		
		mHatchName = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 969);
	}

	public GregtechMTE_LargeNaqReactor(String aName) {
		super(aName);
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_LargeNaqReactor(this.mName);
	}

	public String[] getDescription() {
		if (mCasingName[0].toLowerCase().contains(".name")) {
			mCasingName[0] = ItemUtils.getLocalizedNameOfBlock(getCasing(4), 0);
		}
		if (mCasingName[1].toLowerCase().contains(".name")) {
			mCasingName[1] = ItemUtils.getLocalizedNameOfBlock(getCasing(4), 1);
		}
		if (mCasingName[2].toLowerCase().contains(".name")) {
			mCasingName[2] = ItemUtils.getLocalizedNameOfBlock(getCasing(4), 2);
		}
		if (mCasingName[3].toLowerCase().contains(".name")) {
			mCasingName[3] = ItemUtils.getLocalizedNameOfBlock(getCasing(3), 15);
		}
		if (mCasingName[4].toLowerCase().contains(".name")) {
			mCasingName[4] = ItemUtils.getLocalizedNameOfBlock(getCasing(3), 3);
		}		
		if (mHatchName.toLowerCase().contains(".name")) {
			mHatchName = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 969);
		}
		return new String[]{
				"Naquadah reacts violently with potassium, ",
				"resulting in massive explosions with radioactive potential.",
				"Size: 3x4x12, WxHxL",	
				"Bottom Layer: "+mCasingName[0]+"s, (30x min)",
				"Middle Layer: "+mCasingName[2]+"s (10x), with",
				"                 "+mCasingName[3]+"s on either side",
				"                 "+mCasingName[3]+"s also on each end (x26)",
				"Middle Layer2: "+mCasingName[1]+" (12x total), with",
				"                 "+mCasingName[4]+"s on either side (x24)",
				"Top: Single row of "+mCasingName[0]+" along the middle (x12) ",
				"",
				"1x " + mHatchName + " (Any bottom layer casing)",
				"1x " + "Maintenance Hatch" + " (Any bottom layer side casing)",
				"1x " + "Energy Hatch" + " (Any top layer casing)",				
				getPollutionTooltip(),
				getMachineTooltip(),
				CORE.GT_Tooltip
				};
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
			boolean aActive, boolean aRedstone) {
		return aSide == aFacing
				? new ITexture[]{BlockIcons.CASING_BLOCKS[TAE.getIndexFromPage(2, 3)],
						new GT_RenderedTexture(aActive
								? TexturesGtBlock.Overlay_Machine_Controller_Default_Active
								: TexturesGtBlock.Overlay_Machine_Controller_Default)}
				: new ITexture[]{BlockIcons.CASING_BLOCKS[TAE.getIndexFromPage(2, 3)]};
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"AssemblyLine.png");
	}

	public GT_Recipe_Map getRecipeMap() {
		return null;
	}

	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	public boolean checkRecipe(ItemStack aStack) {
		return false;
	}

	public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 20) {
			GT_Utility.doSoundAtClient((String) GregTech_API.sSoundList.get(Integer.valueOf(212)), 10, 1.0F, aX, aY,
					aZ);
		}
	}
	
	private Block getCasing(int casingID) {
		if (casingID == 1) {
			return ModBlocks.blockCasingsMisc;
		}
		else if (casingID == 2) {
			return ModBlocks.blockCasings2Misc;
		}
		else if (casingID == 3) {
			return ModBlocks.blockCasings3Misc;
		}
		else if (casingID == 4) {
			return ModBlocks.blockCasings4Misc;
		}
		else {
			return ModBlocks.blockCasingsMisc;
		}
	}
	
	
	
	//Casing3, Meta 10 - "Grate Machine Casing");
	//Casing2, Meta 0 - "Solid Steel Machine Casing"
	//Casing2, Meta 5 - "Assembling Line Casing"
	//Casing2, Meta 9 - "Assembler Machine Casing"
	//Magic Glass - blockAlloyGlass

	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int r;
		int i;
		IGregTechTileEntity tTileEntity;
		if (xDir != 0) {
			for (r = 0; r <= 16; ++r) {
				i = r * xDir;
				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(0, 0, i);
				if (i != 0
						&& (aBaseMetaTileEntity.getBlockOffset(0, 0, i) != getCasing(3)
								|| aBaseMetaTileEntity.getMetaIDOffset(0, 0, i) != 3)
						&& r == 1 && !this.addNaquadahHatchToMachineInput(tTileEntity, TAE.getIndexFromPage(2, 3))) {
					return false;
				}

				if (aBaseMetaTileEntity.getBlockOffset(0, -1, i) != getCasing(3) || aBaseMetaTileEntity.getMetaIDOffset(0, -1, i) != 15) {
					return false;
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(0, -2, i);
				if (!this.addMaintenanceToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))
						&& !this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(0, -2, i) != getCasing(4)) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(0, -2, i) != 0) {
						return false;
					}
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 1, i);
				if (!this.addDynamoToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(xDir, 1, i) != getCasing(4)) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, i) != 0) {
						return false;
					}
				}

				if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(xDir, 0, i) != getCasing(4)
						|| aBaseMetaTileEntity.getMetaIDOffset(xDir, 0, i) != 1)) {
					return false;
				}

				if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(xDir, -1, i) != getCasing(4)
						|| aBaseMetaTileEntity.getMetaIDOffset(xDir, -1, i) != 2)) {
					return false;
				}

				if (aBaseMetaTileEntity.getBlockOffset(xDir * 2, 0, i) != getCasing(3)
						|| aBaseMetaTileEntity.getMetaIDOffset(xDir * 2, 0, i) != 3) {
					return false;
				}

				if (aBaseMetaTileEntity.getBlockOffset(xDir * 2, -1, i) != getCasing(3) || aBaseMetaTileEntity.getMetaIDOffset(xDir * 2, -1, i) != 15) {
					return false;
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir * 2, -2, i);
				if (!this.addMaintenanceToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))
						&& !this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(xDir * 2, -2, i) != getCasing(4)) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(xDir * 2, -2, i) != 0) {
						return false;
					}
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, -2, i);
				if (!this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0)) && this.addOutputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					return r > 0 && this.mEnergyHatches.size() > 0;
				}
			}
		} else {
			for (r = 0; r <= 16; ++r) {
				i = r * -zDir;
				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, 0, 0);
				if (i != 0
						&& (aBaseMetaTileEntity.getBlockOffset(i, 0, 0) != getCasing(3)
								|| aBaseMetaTileEntity.getMetaIDOffset(i, 0, 0) != 3)
						&& r == 1 && !this.addNaquadahHatchToMachineInput(tTileEntity, TAE.getIndexFromPage(2, 3))) {
					return false;
				}

				if (aBaseMetaTileEntity.getBlockOffset(i, -1, 0) != getCasing(3) || aBaseMetaTileEntity.getMetaIDOffset(i, -1, 0) != 15) {
					return false;
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, -2, 0);
				if (!this.addMaintenanceToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))
						&& !this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(i, -2, 0) != getCasing(4)) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(i, -2, 0) != 0) {
						return false;
					}
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, 1, zDir);
				if (!this.addDynamoToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(i, 1, zDir) != getCasing(4)) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(i, 1, zDir) != 0) {
						return false;
					}
				}

				if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(i, 0, zDir) != getCasing(4)
						|| aBaseMetaTileEntity.getMetaIDOffset(i, 0, zDir) != 1)) {
					return false;
				}

				if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(i, -1, zDir) != getCasing(4)
						|| aBaseMetaTileEntity.getMetaIDOffset(i, -1, zDir) != 2)) {
					return false;
				}

				if (aBaseMetaTileEntity.getBlockOffset(i, 0, zDir * 2) != getCasing(3)
						|| aBaseMetaTileEntity.getMetaIDOffset(i, 0, zDir * 2) != 3) {
					return false;
				}

				if (aBaseMetaTileEntity.getBlockOffset(i, -1, zDir * 2) != getCasing(3) || aBaseMetaTileEntity.getMetaIDOffset(i, -1, zDir * 2) != 15) {
					return false;
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, -2, zDir * 2);
				if (!this.addMaintenanceToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))
						&& !this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(i, -2, zDir * 2) != getCasing(4)) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(i, -2, zDir * 2) != 0) {
						return false;
					}
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, -2, zDir);
				if (!this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0)) && this.addOutputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					return r > 0 && this.mEnergyHatches.size() > 0;
				}
			}
		}

		return false;
	}
	
	public boolean addNaquadahHatchToMachineInput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Naquadah) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mNaqHatches.add((GT_MetaTileEntity_Hatch_Naquadah) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}	

	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(ItemStack aStack) {
		return 133;
	}

	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public String getMachineType() {
		return "Reactor";
	}
}