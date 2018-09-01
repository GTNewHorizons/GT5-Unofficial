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

	public GregtechMTE_LargeNaqReactor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_LargeNaqReactor(String aName) {
		super(aName);
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_LargeNaqReactor(this.mName);
	}

	public String[] getDescription() {
		return new String[]{
				"Assembly Line",
				"Size: 3x(5-16)x4, variable length",				
				"Bottom: Steel Machine Casing(or Maintenance or Input Hatch),",
				"Input Bus (Last Output Bus), Steel Machine Casing",
				"Middle: Reinforced Glass, Assembly Line, Reinforced Glass",
				"UpMiddle: Grate Machine Casing,",
				"    Assembler Machine Casing,", 
				"    Grate Machine Casing (or Controller or Data Access Hatch)",
				"Top: Steel Casing(or Energy Hatch)",
				"Up to 16 repeating slices, last is Output Bus",
				"Optional 1x Data Access Hatch next to the Controller",
				getPollutionTooltip(),
				getMachineTooltip(),
				CORE.GT_Tooltip
				};
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
			boolean aActive, boolean aRedstone) {
		return aSide == aFacing
				? new ITexture[]{BlockIcons.CASING_BLOCKS[TAE.getIndexFromPage(3, 0)],
						new GT_RenderedTexture(aActive
								? TexturesGtBlock.Overlay_Machine_Controller_Default_Active
								: TexturesGtBlock.Overlay_Machine_Controller_Default)}
				: new ITexture[]{BlockIcons.CASING_BLOCKS[TAE.getIndexFromPage(3, 0)]};
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
		return ModBlocks.blockCasings4Misc;
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
						&& (aBaseMetaTileEntity.getBlockOffset(0, 0, i) != GregTech_API.sBlockCasings3
								|| aBaseMetaTileEntity.getMetaIDOffset(0, 0, i) != 10)
						&& r == 1 && !this.addNaquadahHatchToMachineInput(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					return false;
				}

				if (!aBaseMetaTileEntity.getBlockOffset(0, -1, i).getUnlocalizedName().equals("blockAlloyGlass")) {
					return false;
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(0, -2, i);
				if (!this.addMaintenanceToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))
						&& !this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(0, -2, i) != ModBlocks.blockCasings4Misc) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(0, -2, i) != 0) {
						return false;
					}
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 1, i);
				if (!this.addEnergyInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(xDir, 1, i) != ModBlocks.blockCasings4Misc) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, i) != 0) {
						return false;
					}
				}

				if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(xDir, 0, i) != ModBlocks.blockCasings4Misc
						|| aBaseMetaTileEntity.getMetaIDOffset(xDir, 0, i) != 1)) {
					return false;
				}

				if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(xDir, -1, i) != ModBlocks.blockCasings4Misc
						|| aBaseMetaTileEntity.getMetaIDOffset(xDir, -1, i) != 2)) {
					return false;
				}

				if (aBaseMetaTileEntity.getBlockOffset(xDir * 2, 0, i) != GregTech_API.sBlockCasings3
						|| aBaseMetaTileEntity.getMetaIDOffset(xDir * 2, 0, i) != 10) {
					return false;
				}

				if (!aBaseMetaTileEntity.getBlockOffset(xDir * 2, -1, i).getUnlocalizedName()
						.equals("blockAlloyGlass")) {
					return false;
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir * 2, -2, i);
				if (!this.addMaintenanceToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))
						&& !this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(xDir * 2, -2, i) != ModBlocks.blockCasings4Misc) {
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
						&& (aBaseMetaTileEntity.getBlockOffset(i, 0, 0) != GregTech_API.sBlockCasings3
								|| aBaseMetaTileEntity.getMetaIDOffset(i, 0, 0) != 10)
						&& r == 1 && !this.addNaquadahHatchToMachineInput(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					return false;
				}

				if (!aBaseMetaTileEntity.getBlockOffset(i, -1, 0).getUnlocalizedName().equals("blockAlloyGlass")) {
					return false;
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, -2, 0);
				if (!this.addMaintenanceToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))
						&& !this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(i, -2, 0) != ModBlocks.blockCasings4Misc) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(i, -2, 0) != 0) {
						return false;
					}
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, 1, zDir);
				if (!this.addEnergyInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(i, 1, zDir) != ModBlocks.blockCasings4Misc) {
						return false;
					}

					if (aBaseMetaTileEntity.getMetaIDOffset(i, 1, zDir) != 0) {
						return false;
					}
				}

				if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(i, 0, zDir) != ModBlocks.blockCasings4Misc
						|| aBaseMetaTileEntity.getMetaIDOffset(i, 0, zDir) != 1)) {
					return false;
				}

				if (i != 0 && (aBaseMetaTileEntity.getBlockOffset(i, -1, zDir) != ModBlocks.blockCasings4Misc
						|| aBaseMetaTileEntity.getMetaIDOffset(i, -1, zDir) != 2)) {
					return false;
				}

				if (aBaseMetaTileEntity.getBlockOffset(i, 0, zDir * 2) != GregTech_API.sBlockCasings3
						|| aBaseMetaTileEntity.getMetaIDOffset(i, 0, zDir * 2) != 10) {
					return false;
				}

				if (!aBaseMetaTileEntity.getBlockOffset(i, -1, zDir * 2).getUnlocalizedName()
						.equals("blockAlloyGlass")) {
					return false;
				}

				tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(i, -2, zDir * 2);
				if (!this.addMaintenanceToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))
						&& !this.addInputToMachineList(tTileEntity, TAE.getIndexFromPage(3, 0))) {
					if (aBaseMetaTileEntity.getBlockOffset(i, -2, zDir * 2) != ModBlocks.blockCasings4Misc) {
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