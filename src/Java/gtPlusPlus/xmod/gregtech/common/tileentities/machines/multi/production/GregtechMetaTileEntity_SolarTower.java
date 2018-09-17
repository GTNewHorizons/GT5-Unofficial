package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
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
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.getIndexFromPage(3, 6)],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.getIndexFromPage(3, 6)]};
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

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {

		for (int i = 0; i < 19; i++) {
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
		if (aY >= 0 && aY <= 6) {
			return checkTopLayers(-aY);
		}
		if (aY >= 7 && aY <= 16) {
			return checkTowerLayer(-aY);			
		}
		else if (aY >= 17 && aY <= 19) {
			return checkBaseLayer(-aY);			
		}	
		Logger.INFO("Bad Y level to check");
		return false;
	}

	public boolean checkTopLayers(int aY) {
		Block aBlock;
		int aMeta;

		if (aY == 0) {
			return true;
		} else if (aY == -1) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
					aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);
					if (aBlock == getCasingBlock() && aMeta == getCasingMeta3()) {
						continue;
					} else {
						Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
						Logger.INFO("Found Bad Block on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta); 
						return false;						
					}
				}
			}
		} else if (aY == -2 || aY == -6) {
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
					aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);					
					//Edge Casing
					if (x == -2 || x == 2 || z == -2 || z == 2) {						
						//Edge Corners
						if ((x == 2 || x == -2) && (z == 2 | z == -2)) {
							if (!getBaseMetaTileEntity().getAirOffset(x, aY, z)) {
								Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
								Logger.INFO("Found Bad Block on Outside Corner "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
								return false;								
							}
							else {
								continue;
							}
						}
						else {
							//Edge Sides
							if (aBlock == getCasingBlock() && aMeta == getCasingMeta3()) {
								continue;
							} else {
								Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
								Logger.INFO("Found Bad Block on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
								return false;								
							}
						}						
					}

					//Internal
					else {
						if (aBlock == getCasingBlock() && aMeta == getCasingMeta2()) {
							continue;
						} else {
							Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
							Logger.INFO("Found Bad Block Internally on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
							return false;
						}
					}				
				}
			}
		} 
		//Top Layers 7x7
		else if (aY == -3 || aY == -5) {
			for (int x = -3; x <= 3; x++) {
				for (int z = -3; z <= 3; z++) {
					aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
					aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);					
					//Edge Casing
					if (x == -3 || x == 3 || z == -3 || z == 3) {

						//3, 3
						//2, 3
						//3, 2

						//Air Spacing
						if (x == 3 && (z == -3 || z == -2 || z == 2 || z == 3)) {
							if (!getBaseMetaTileEntity().getAirOffset(x, aY, z)) {
								Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
								Logger.INFO("Found Bad Block on Outside Corner "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
								return false;
							}
							else {
								continue;
							}
						}
						else if (x == -3 && (z == -3 || z == -2 || z == 2 || z == 3)) {
							if (!getBaseMetaTileEntity().getAirOffset(x, aY, z)) {
								Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
								Logger.INFO("Found Bad Block on Outside Corner "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
								return false;
							}
							else {
								continue;
							}
						}
						else if (z == 3 && (x == -3 || x == -2 || x == 2 || x == 3)) {
							if (!getBaseMetaTileEntity().getAirOffset(x, aY, z)) {
								Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
								Logger.INFO("Found Bad Block on Outside Corner "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
								return false;
							}
							else {
								continue;
							}
						}
						else if (z == -3 && (x == -3 || x == -2 || x == 2 || x == 3)) {
							if (!getBaseMetaTileEntity().getAirOffset(x, aY, z)) {
								Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
								Logger.INFO("Found Bad Block on Outside Corner "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
								return false;
							}
							else {
								continue;
							}
						}
						else {
							//Edge Sides
							if (aBlock == getCasingBlock() && aMeta == getCasingMeta3()) {
								continue;
							} else {
								Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
								Logger.INFO("Found Bad Block on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
								return false;
							}
						}						
					}
					else if ((x == -2 || x == 2) & (z == -2 || z == 2)) {
						if (aBlock == getCasingBlock() && aMeta == getCasingMeta3()) {
							continue;
						} else {
							Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
							Logger.INFO("Found Bad Block on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
							return false;
						}
					}
					//Internal
					else {
						if (aBlock == getCasingBlock() && aMeta == getCasingMeta2()) {
							continue;
						} else {
							Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
							Logger.INFO("Found Bad Block Internally on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
							return false;
						}
					}				
				}
			}
		} 
		//Midle Top Layer 9x9
		else if (aY == -4) {
			//Check Inner 5x5
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					if (getBaseMetaTileEntity().getBlockOffset(x, aY, z) != getCasingBlock() && this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z) != getCasingMeta2()) {
						Logger.INFO("Error at offset: X:" + x + ", Y:" + aY + ", Z:" + z);
						return false;
					}
					else {
						continue;
					}
				}	
			}
			//Check Pos Sides
			for (int z = -1; z <= -1; z++) {
				if (getBaseMetaTileEntity().getBlockOffset(3, aY, z) == getCasingBlock()
						&& this.getBaseMetaTileEntity().getMetaIDOffset(3, aY, z) == getCasingMeta2()) {
					continue;
				} else if (getBaseMetaTileEntity().getBlockOffset(-3, aY, z) == getCasingBlock()
						&& this.getBaseMetaTileEntity().getMetaIDOffset(-3, aY, z) == getCasingMeta2()) {
					continue;
				} else {
					Logger.INFO("1 Error at offset: X:3/-3" + ", Y:" + aY + ", Z:" + z);
					return false;
				}

			}
			for (int x = -1; x <= -1; x++) {
				if (getBaseMetaTileEntity().getBlockOffset(x, aY, 3) == getCasingBlock()
						&& this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, 3) == getCasingMeta2()) {
					continue;
				} else if (getBaseMetaTileEntity().getBlockOffset(x, aY, -3) == getCasingBlock()
						&& this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, -3) == getCasingMeta2()) {
					continue;
				} else {
					Logger.INFO("1 Error at offset: X:" + x + ", Y:" + aY + ", Z:3/-3");
					return false;
				}
			}	

			//Corner Casings
			for (int z = -2; z <= -2; z++) {
				if (z == -2 || z == 2) {
					if (getBaseMetaTileEntity().getBlockOffset(3, aY, z) == getCasingBlock()
							&& this.getBaseMetaTileEntity().getMetaIDOffset(3, aY, z) == getCasingMeta3()) {
						continue;
					} else if (getBaseMetaTileEntity().getBlockOffset(-3, aY, z) == getCasingBlock()
							&& this.getBaseMetaTileEntity().getMetaIDOffset(-3, aY, z) == getCasingMeta3()) {
						continue;
					} else {
						Logger.INFO("2 Error at offset: X:3/-3" + ", Y:" + aY + ", Z:" + z);
						return false;
					}
				}
			}
			for (int x = -2; x <= -2; x++) {
				if (x == -2 || x == 2) {
					if (getBaseMetaTileEntity().getBlockOffset(x, aY, 3) == getCasingBlock()
							&& this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, 3) == getCasingMeta3()) {
						continue;
					} else if (getBaseMetaTileEntity().getBlockOffset(x, aY, -3) == getCasingBlock()
							&& this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, -3) == getCasingMeta3()) {
						continue;
					} else {
						Logger.INFO("2 Error at offset: X:" + x + ", Y:" + aY + ", Z:3/-3");
						return false;
					}
				}
			}
			//Check Sides Casings
			for (int z = -1; z <= -1; z++) {
				if (getBaseMetaTileEntity().getBlockOffset(4, aY, z) == getCasingBlock()
						&& this.getBaseMetaTileEntity().getMetaIDOffset(4, aY, z) == getCasingMeta3()) {
					continue;
				} else if (getBaseMetaTileEntity().getBlockOffset(-4, aY, z) == getCasingBlock()
						&& this.getBaseMetaTileEntity().getMetaIDOffset(-4, aY, z) == getCasingMeta3()) {
					continue;
				} else {
					Logger.INFO("1 Error at offset: X:3/-3" + ", Y:" + aY + ", Z:" + z);
					return false;
				}

			}
			for (int x = -1; x <= -1; x++) {
				if (getBaseMetaTileEntity().getBlockOffset(x, aY, 4) == getCasingBlock()
						&& this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, 4) == getCasingMeta3()) {
					continue;
				} else if (getBaseMetaTileEntity().getBlockOffset(x, aY, -4) == getCasingBlock()
						&& this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, -4) == getCasingMeta3()) {
					continue;
				} else {
					Logger.INFO("1 Error at offset: X:" + x + ", Y:" + aY + ", Z:3/-3");
					return false;
				}
			}		
		}
		return true;
	}

	public boolean checkTowerLayer(int aY) {
		Block aBlock;
		int aMeta;		
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
				aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);
				if (x == -1 || x == 1 || z == -1 || z == 1) {
					if (aBlock == getCasingBlock() && aMeta == getCasingMeta()) {
						continue;
					} else {
						Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
						Logger.INFO("Found Bad Block Externally on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
						return false;
					}
				} else {
					if (aBlock == getCasingBlock() && aMeta == getCasingMeta2()) {
						continue;
					} else {
						Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
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

		int requiredMeta = getCasingMeta2();
		if (aY == 19) {
			requiredMeta = getCasingMeta();
		}		

		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				aBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
				aMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);		

				if ((x == 3 && z == 3) || (x == 2 && z == 2) || (x == 3 && z == 2) || (x == 2 && z == 3)) {
					if (!getBaseMetaTileEntity().getAirOffset(x, aY, z)) {
						Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
						return false;
					}
				}
				else if ((x == -3 && z == -3) || (x == -2 && z == -2) || (x == -3 && z == -2) || (x == -2 && z == -3)) {
					if (!getBaseMetaTileEntity().getAirOffset(x, aY, z)) {
						Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
						return false;
					}
				}
				else if ((x == -3 && z == 3) || (x == -2 && z == 2) || (x == -3 && z == 2) || (x == -2 && z == 3)) {
					if (!getBaseMetaTileEntity().getAirOffset(x, aY, z)) {
						Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
						return false;
					}
				}
				else if ((x == 3 && z == -3) || (x == 2 && z == -2) || (x == 3 && z == -2) || (x == 2 && z == -3)) {
					if (!getBaseMetaTileEntity().getAirOffset(x, aY, z)) {
						Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
						return false;
					}
				}
				else {
					IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);					
					if (addToMachineList(tTileEntity, mCasingTextureID)) {
						continue;
					}
					if (x == 0 && z == 0) {
						if (aBlock == getCasingBlock() && aMeta == requiredMeta) {
							continue;
						} else {
							Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
							Logger.INFO("Found Bad Block Internally on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
							return false;
						}					
					}
					else {
						if (aBlock == getCasingBlock() && aMeta == getCasingMeta()) {
							continue;
						} else {
							Logger.INFO("Error at offset: X:"+x+", Y:"+aY+", Z:"+z);
							Logger.INFO("Found Bad Block Internally on Layer "+aY+": "+(aBlock != null ? aBlock.getLocalizedName() : "Air")+" | Meta: "+aMeta);
							return false;
						}
					}					
				}
			}
		}
		return true;	
	}









}
