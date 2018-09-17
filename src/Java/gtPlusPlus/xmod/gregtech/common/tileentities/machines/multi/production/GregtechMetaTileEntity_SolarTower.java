package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
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
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_SolarTower
extends GregtechMeta_MultiBlockBase {

	//862
	private static final int mCasingTextureID = TAE.getIndexFromPage(3, 4);
	public static String mCasingName = "";
	private int mHeight = 0;

	public GregtechMetaTileEntity_SolarTower(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 4);
	}

	public GregtechMetaTileEntity_SolarTower(final String aName) {
		super(aName);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 4);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_SolarTower(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Solar Tower";
	}

	@Override
	public String[] getDescription() {

		if (mCasingName.toLowerCase().contains(".name")) {
			mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 4);
		}

		return new String[]{
				"Controller Block for Industrial Arc Furnace",
				mCasingName+"s for the rest (28 at least!)",
				getPollutionTooltip(),
				getMachineTooltip(),
				CORE.GT_Tooltip};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(203));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == 0 || aSide == 1) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.getIndexFromPage(2, 1)],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.getIndexFromPage(2, 1)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing <= 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
        //this.mEfficiencyIncrease = 100;
        //this.mMaxProgresstime = 100;
        //this.mEUt = -4;
        return true;
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		
		for (int i = 0; i < 18; i++) {
			if (!checkLayer(i)) {
				Logger.INFO("Invalid Structure on Y level "+i);
				return false;
			}
		}
		
		if (mMaintenanceHatches.size() != 1) {
			Logger.INFO("Bad Hatches");
			return false;
		}
		Logger.INFO("Built Structure");
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
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasings4Misc;
	}


	public byte getCasingMeta() {
		return 4;
	}


	public byte getCasingMeta2() {
		return 5;
	}


	public byte getCasingMeta3() {
		return 6;
	}

	public boolean isValidCasingBlock(Block aBlock, int aMeta) {
		if (aBlock == getCasingBlock() && aMeta == getCasingMeta()) {
			return true;
		}
		if (aBlock == getCasingBlock() && aMeta == getCasingMeta2()) {
			return true;
		}
		if (aBlock == getCasingBlock() && aMeta == getCasingMeta3()) {
			return true;
		}
		Logger.INFO("Found "+(aBlock != null ? aBlock.getLocalizedName() : "Air") + "With Meta "+aMeta);
		return false;
	}

	public byte getCasingTextureIndex() {
		return (byte) mCasingTextureID;
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
		if (this.mHeight > 3) {}
		
		
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setInteger("mHeight", mHeight);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mHeight = aNBT.getInteger("mHeight");
	}
	
	public boolean checkLayer(int aY) {
		if (aY >= 0 && aY <= 4) {
			return checkTopLayers(-aY);
		}
		if (aY >= 5 && aY <= 14) {
			return checkTowerLayer(-aY);			
		}
		else if (aY >= 15 && aY <= 17) {
			return checkBaseLayer(-aY);			
		}		
		Logger.INFO("Bad Y level to check");
		return false;
	}
	
	public boolean checkTopLayers(int aY) {
		Block aBlock;
		int aMeta;
		if (aY == 0) {
			for (int x = -2; x < 2; x++) {
				for (int z = -2; z < 2; z++) {
					aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
					aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);
					if (x != 0 && z != 0) {
						if (aBlock == getCasingBlock() && aMeta == getCasingMeta3()) {
							continue;
						} else {
							Logger.INFO("Found Bad Block on Top Layer: "+(aBlock != null ? aBlock.getLocalizedName() : "Air"));
							return false;
						}
					}
					else {
						continue;
					}
				}
			}
		} else if (aY > 0 && aY < 4) {
			for (int x = -2; x < 2; x++) {
				for (int z = -2; z < 2; z++) {
					aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
					aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);
					if (x != -2 || x == 2 || z == -2 || z == 2) {
						if (aBlock == getCasingBlock() && aMeta == getCasingMeta3()) {
							continue;
						} else {
							Logger.INFO("Found Bad Block on Exterior of Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
							return false;
						}
					} else {
						if (aBlock == getCasingBlock() && aMeta == getCasingMeta2()) {
							continue;
						} else {
							Logger.INFO("Found Bad Block on Internally on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
							return false;
						}
					}
				}
			}
		} else if (aY == 4) {
			for (int x = -2; x < 2; x++) {
				for (int z = -2; z < 2; z++) {
					aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
					aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);
					
					if (x == 0 && z == 0) {
						if (aBlock == getCasingBlock() && aMeta == getCasingMeta2()) {
							continue;
						}
						else {
							return false;
						}
					}
					
					if (aBlock == getCasingBlock() && aMeta == getCasingMeta3()) {
						continue;
					} else {
						Logger.INFO("Found Bad Block on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean checkTowerLayer(int aY) {
		Block aBlock;
		int aMeta;		
		for (int x = -1; x < 1; x++) {
			for (int z = -1; z < 1; z++) {
				aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
				aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);
				if (x == -1 || x == 1 || z == -1 || z == 1) {
					if (aBlock == getCasingBlock() && aMeta == getCasingMeta()) {
						continue;
					} else {
						Logger.INFO("Found Bad Block Externally on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
						return false;
					}
				} else {
					if (aBlock == getCasingBlock() && aMeta == getCasingMeta2()) {
						continue;
					} else {
						Logger.INFO("Found Bad Block Internally on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
						return false;
					}
				}
			}
		}
		return true;	
	}
	
	public boolean checkBaseLayer(int aY) {
		Block aBlock;
		int aMeta;		
		for (int x = -2; x < 2; x++) {
			for (int z = -1; z < 1; z++) {
				aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
				aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);								
				if (aBlock == getCasingBlock() && aMeta == getCasingMeta()) {
					continue;
				} else {
                    IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);					
                    if (addToMachineList(tTileEntity, mCasingTextureID)) {
                    	continue;
                    }     
					Logger.INFO("Found Bad Block on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);               
					return false;
				}
			}
		}
		return true;	
	}
	
	
}
