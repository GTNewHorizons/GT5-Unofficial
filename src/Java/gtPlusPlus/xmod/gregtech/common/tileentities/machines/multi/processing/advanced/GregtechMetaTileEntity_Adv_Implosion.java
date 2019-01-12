package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.api.GregTech_API.sBlockCasings4;

public class GregtechMetaTileEntity_Adv_Implosion
extends GregtechMeta_MultiBlockBase {

	private String mCasingName = "Robust Tungstensteel Machine Casing";
	public GregtechMetaTileEntity_Adv_Implosion(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		mCasingName = ItemList.Casing_RobustTungstenSteel.get(1).getDisplayName();
	}

	public GregtechMetaTileEntity_Adv_Implosion(String aName) {
		super(aName);
		mCasingName = ItemList.Casing_RobustTungstenSteel.get(1).getDisplayName();
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Adv_Implosion(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Implosion Compressor";
	}

	public String[] getTooltip() {    	
		if (mCasingName.contains("gt.blockcasings")) {
			mCasingName = ItemList.Casing_RobustTungstenSteel.get(1).getDisplayName();
		}    	
		return new String[]{
				"Controller Block for the Advanced Implosion Compressor",
				"Processes upto ((Tier/2)+1) recipes at once",
				"Size(WxHxD): 3x3x3 (Hollow)",
				mCasingName+"s (10 at least!)",
				"Controller (Front centered)",
				"1x Input Bus",
				"1x Output Bus",
				"1x Energy Hatch",
		};
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[48], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[48]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "ImplosionCompressor";
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sImplosionRecipes;
	}

	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric((GT_Utility.getTier(this.getMaxInputVoltage())/2+1), 100, 100);
	}

	public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 20) {
			GT_Utility.doSoundAtClient((String) GregTech_API.sSoundList.get(Integer.valueOf(5)), 10, 1.0F, aX, aY, aZ);
		}
	}

	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {	
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int tAmount = 0;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		} else {
			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					for (int h = -1; h < 2; ++h) {
						if (h != 0 || (xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0)) {
							IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i,
									h, zDir + j);
							Block aBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							int aMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);

							if (!isValidBlockForStructure(tTileEntity, 48, true, aBlock, aMeta,
									sBlockCasings4, 0)) {
								Logger.INFO("Bad centrifuge casing");
								return false;
							}
							++tAmount;

						}
					}
				}
			}
			return tAmount >= 10;
		}
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(ItemStack aStack) {
		return 250;
	}

	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public int getMaxParallelRecipes() {
		return (GT_Utility.getTier(this.getMaxInputVoltage())/2+1);
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 100;
	}

}