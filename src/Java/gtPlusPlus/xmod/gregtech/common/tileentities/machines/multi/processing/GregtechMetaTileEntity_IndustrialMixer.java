package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import java.util.ArrayList;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_IndustrialMixer
extends GregtechMeta_MultiBlockBase {

	public static int CASING_TEXTURE_ID;
	public static String mCasingName = "Advanced Blast Furnace Casing";
	public static String mCasingName2 = "Advanced Blast Furnace Casing";
	
	public GregtechMetaTileEntity_IndustrialMixer(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 2);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 2);
		mCasingName2 = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockCasings4, 11);
	}

	public GregtechMetaTileEntity_IndustrialMixer(final String aName) {
		super(aName);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 2);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 2);
		mCasingName2 = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockCasings4, 11);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialMixer(this.mName);
	}

	@Override
	public String[] getDescription() {
		
		if (mCasingName.toLowerCase().contains(".name")) {
			mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 2);
		}
		if (mCasingName2.toLowerCase().contains(".name")) {
			mCasingName2 = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockCasings4, 11);
		}
		
		return new String[]{
				"Controller Block for the Industrial Mixer",
				"250% faster than using single block machines of the same voltage",
				"Processes eight recipes per voltage tier",
				"Size: 3x4x3 (LxHxW)",
				"Controller (front centered)",
				"1x Input Bus (anywhere)",
				"1x Output Bus (anywhere)",
				"1x Energy Hatch (anywhere)",
				"1x Maintenance Hatch (anywhere)",
				"1x Muffler Hatch (anywhere)",
				mCasingName+"s for the rest (16 at least!)",
				mCasingName2+"s for the internal blocks (2)",
				"Produces "+this.getPollutionPerTick(null)+" pollution per tick",
				CORE.GT_Tooltip};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(203));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MaterialPress.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sMixerRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
			ArrayList<ItemStack> tBusItems = new ArrayList<ItemStack>();
			tBus.mRecipeMap = getRecipeMap();
			if (isValidMetaTileEntity(tBus)) {
				for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
					if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null)
						tBusItems.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
				}
			}

			if (checkRecipeGeneric(tBusItems.toArray(new ItemStack[]{}), new FluidStack[]{},
					(8 * GT_Utility.getTier(this.getMaxInputVoltage())), 100, 250, 10000)) return true;
		}
		return false;
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

		int tAmount = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = -1; h < 3; h++) {
					if ((h != 0) || ((((xDir + i) != 0) || ((zDir + j) != 0)) && ((i != 0) || (j != 0)))) {
						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!this.addToMachineList(tTileEntity, CASING_TEXTURE_ID)) && (!this.addMufflerToMachineList(tTileEntity, CASING_TEXTURE_ID)) && (!this.addInputToMachineList(tTileEntity, CASING_TEXTURE_ID)) && (!this.addOutputToMachineList(tTileEntity, CASING_TEXTURE_ID)) && (!this.addEnergyInputToMachineList(tTileEntity, CASING_TEXTURE_ID))) {
							final Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							final byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
							if (h == 0 || h == 1){
								if (i == 0 && j == 0){
									if (((tBlock != GregTech_API.sBlockCasings4) || (tMeta != 11))) {
										return false;
									}
									tAmount++;
								}
								else {
									if (((tBlock != ModBlocks.blockCasings3Misc) || (tMeta != 2))) {
										return false;
									}
									tAmount++;
								}
							}
							else {
								if (((tBlock != ModBlocks.blockCasings3Misc) || (tMeta != 2))) {
									return false;
								}
								tAmount++;
							}
						}
					}
				}
			}
		}
		return tAmount >= 16;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 40;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}
}
