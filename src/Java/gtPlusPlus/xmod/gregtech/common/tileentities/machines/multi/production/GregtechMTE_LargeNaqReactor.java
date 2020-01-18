package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.LangUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Naquadah;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMTE_LargeNaqReactor extends GregtechMeta_MultiBlockBase {

	public ArrayList<GT_MetaTileEntity_Hatch_Naquadah> mNaqHatches = new ArrayList<GT_MetaTileEntity_Hatch_Naquadah>();
	public static String[] mCasingName = new String[5];
	public static String mHatchName = "Naquadah Fuel Hatch";	

	private final int CASING_TEXTURE_ID = TAE.getIndexFromPage(0, 13);
	private final int META_BaseCasing = 0; //4
	private final int META_ContainmentCasing = 15; //3
	private final int META_Shielding = 13; //1
	private final int META_PipeCasing = 1; //4
	private final int META_IntegralCasing = 6; //0
	private final int META_ContainmentChamberCasing = 2; //4

	public GregtechMTE_LargeNaqReactor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		mCasingName[0] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 0);
		mCasingName[1] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 1);
		mCasingName[2] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 2);
		mCasingName[3] = LangUtils.getLocalizedNameOfBlock(getCasing(3), 15);
		mCasingName[4] = LangUtils.getLocalizedNameOfBlock(getCasing(1), 13);		
		mHatchName = LangUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 969);
	}

	public GregtechMTE_LargeNaqReactor(String aName) {
		super(aName);
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_LargeNaqReactor(this.mName);
	}

	public String[] getTooltip() {
		if (mCasingName[0].toLowerCase().contains(".name")) {
			mCasingName[0] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 0);
		}
		if (mCasingName[1].toLowerCase().contains(".name")) {
			mCasingName[1] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 1);
		}
		if (mCasingName[2].toLowerCase().contains(".name")) {
			mCasingName[2] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 2);
		}
		if (mCasingName[3].toLowerCase().contains(".name")) {
			mCasingName[3] = LangUtils.getLocalizedNameOfBlock(getCasing(3), 15);
		}
		if (mCasingName[4].toLowerCase().contains(".name")) {
			mCasingName[4] = LangUtils.getLocalizedNameOfBlock(getCasing(1), 13);
		}		
		if (mHatchName.toLowerCase().contains(".name")) {
			mHatchName = LangUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 969);
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
		return aFacing == 1;
	}

	public boolean checkRecipe(ItemStack aStack) {
		return false;
	}	

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 20) {
			GT_Utility.doSoundAtClient((String) GregTech_API.sSoundList.get(Integer.valueOf(212)), 10, 1.0F, aX, aY,
					aZ);
		}
	}

	@Override
	public String getSound() {
		return (String) GregTech_API.sSoundList.get(Integer.valueOf(212)); 
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
			return ModBlocks.blockCasingsTieredGTPP;
		}
	}



	//Casing3, Meta 10 - "Grate Machine Casing");
	//Casing2, Meta 0 - "Solid Steel Machine Casing"
	//Casing2, Meta 5 - "Assembling Line Casing"
	//Casing2, Meta 9 - "Assembler Machine Casing"
	//Magic Glass - blockAlloyGlass

	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 4;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 4;

		// Counts for all Casing Types
		int aBaseCasingCount = 0;
		int aContainmentCasingCount = 0;
		int aShieldingCount = 0;
		int aPipeCount = 0;
		int aIntegralCasingCount = 0;
		int aContainmentChamberCount = 0;		

		// Bottom Layer
		aBaseCasingCount += checkEntireLayer(aBaseMetaTileEntity, getCasing(4), META_BaseCasing, -7, xDir, zDir);
		log("Bottom Layer is Valid. Moving to Layer 1.");

		// Layer 1
		aShieldingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(1), this.META_Shielding, -6, xDir, zDir);
		aIntegralCasingCount += checkIntegralRing(aBaseMetaTileEntity, getCasing(0), this.META_IntegralCasing, -6, xDir, zDir);
		aContainmentChamberCount += checkContainmentRing(aBaseMetaTileEntity, getCasing(4), this.META_ContainmentChamberCasing, -6, xDir, zDir);
		log("Layer 1 is Valid. Moving to Layer 2.");

		// Layer 2
		aShieldingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(1), this.META_Shielding, -5, xDir, zDir);
		aPipeCount += checkPipes(aBaseMetaTileEntity, getCasing(4), this.META_PipeCasing, -5, xDir, zDir);
		log("Layer 2 is Valid. Moving to Layer 3.");

		// Layer 3
		aContainmentCasingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(3), this.META_ContainmentCasing, -4, xDir, zDir);
		aPipeCount += checkPipes(aBaseMetaTileEntity, getCasing(4), this.META_PipeCasing, -4, xDir, zDir);
		log("Layer 3 is Valid. Moving to Layer 4.");

		// Layer 4
		aContainmentCasingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(3), this.META_ContainmentCasing, -3, xDir, zDir);
		aPipeCount += checkPipes(aBaseMetaTileEntity, getCasing(4), this.META_PipeCasing, -3, xDir, zDir);
		log("Layer 4 is Valid. Moving to Layer 5.");

		// Layer 5
		aShieldingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(1), this.META_Shielding, -2, xDir, zDir);
		aPipeCount += checkPipes(aBaseMetaTileEntity, getCasing(4), this.META_PipeCasing, -2, xDir, zDir);
		log("Layer 5 is Valid. Moving to Layer 6.");

		// Layer 6
		aShieldingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(1), this.META_Shielding, -1, xDir, zDir);
		aIntegralCasingCount += checkIntegralRing(aBaseMetaTileEntity, getCasing(0), this.META_IntegralCasing, -1, xDir, zDir);
		aContainmentChamberCount += checkContainmentRing(aBaseMetaTileEntity, getCasing(4), this.META_ContainmentChamberCasing, -1, xDir, zDir);
		log("Layer 6 is Valid. Moving to Top Layer.");

		// Top Layer
		aBaseCasingCount += checkEntireLayer(aBaseMetaTileEntity, getCasing(4), META_BaseCasing, 0, xDir, zDir);

		log("Found "+aBaseCasingCount+" "+mCasingName[0]+"s");
		log("Found "+aShieldingCount+" "+mCasingName[4]+"s");
		log("Found "+aPipeCount+" "+mCasingName[1]+"s");
		log("Found "+aContainmentCasingCount+" "+mCasingName[3]+"s");
		log("Found "+aIntegralCasingCount+" "+LangUtils.getLocalizedNameOfBlock(getCasing(0), 6)+"s");
		log("Found "+aContainmentChamberCount+" "+mCasingName[2]+"s");

		// Try mesage player
		String aOwnerName = this.getBaseMetaTileEntity().getOwnerName();
		EntityPlayer aOwner = null;
		if (aOwnerName != null && aOwnerName.length() > 0) {
			aOwner = PlayerUtils.getPlayer(aOwnerName);
		}
		
		if (aShieldingCount != 128) {
			log("Not enough "+mCasingName[4]+"s, require 128.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[4]+"s, require 128.");
			}
			return false;
		}
		if (aPipeCount != 20) {
			log("Not enough "+mCasingName[1]+"s, require 20.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[1]+"s, require 20.");
			}
			return false;
		}
		if (aContainmentCasingCount != 64) {
			log("Not enough "+mCasingName[3]+"s, require 64.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[3]+"s, require 64.");
			}
			return false;
		}
		if (aContainmentChamberCount != 42) {
			log("Not enough "+mCasingName[2]+"s, require 42.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[2]+"s, require 42.");
			}
			return false;
		}
		if (aBaseCasingCount < 140) {
			log("Not enough "+mCasingName[0]+"s, require 140 at a minimum.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[0]+"s, require 140 at a minimum.");
			}
			return false;
		}
		if (aIntegralCasingCount != 48) {
			log("Not enough "+LangUtils.getLocalizedNameOfBlock(getCasing(0), 6)+"s, require 48.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+LangUtils.getLocalizedNameOfBlock(getCasing(0), 6)+"s, require 48.");
			}
			return false;
		}	
		log("LNR Formed.");
		if (aOwner != null) {
			PlayerUtils.messagePlayer(aOwner, "Large Naquadah Reactor has formed successfully.");
		}
		return true;
	}

	public boolean addNaquadahHatchToMachineInput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Naquadah) {
				this.updateTexture(aMetaTileEntity, aBaseCasingIndex);
				return this.mNaqHatches.add((GT_MetaTileEntity_Hatch_Naquadah) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}	

	public int checkEntireLayer(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {
		int aCasingCount = 0;
		for (int x = -4; x < 5; x++) {
			for (int z = -4; z < 5; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 4 && z == 4) || (x == -4 && z == -4) || (x == 4 && z == -4) || (x == -4 && z == 4)) {
					continue;
				}		
				// Skip controller
				if (aY == 0 && x == 0 && z == 0) {
					continue;
				}
				
				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
				if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
					aCasingCount++;
				}				
				final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
				if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, true, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
					log("Layer has error. Height: "+aY);
					//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
					return 0;
				}
			}	
		}		
		return aCasingCount;
	}

	public int checkOuterRing(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {		
		int aCasingCount = 0;
		for (int x = -4; x < 5; x++) {
			for (int z = -4; z < 5; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 4 && z == 4) || (x == -4 && z == -4) || (x == 4 && z == -4) || (x == -4 && z == 4)) {
					continue;
				}	

				// If we are on the 5x5 ring, proceed
				if ((x > -4 && x < 4 ) && (z > -4 && z < 4)) {	
					if ((x == 3 && z == 3) || (x == -3 && z == -3) || (x == 3 && z == -3) || (x == -3 && z == 3)) {
						//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);					
					}
					else {
						continue;
					}
				}

				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
				if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
					aCasingCount++;
				}				
				final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
				if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, false, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
					log("Layer has error. Height: "+aY);
					//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
					return 0;
				}
			}	
		}
		return aCasingCount;
	}
	public int checkIntegralRing(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {
		int aCasingCount = 0;
		for (int x = -3; x < 4; x++) {
			for (int z = -3; z < 4; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 3 && z == 3) || (x == -3 && z == -3) || (x == 3 && z == -3) || (x == -3 && z == 3)) {
					continue;
				}	

				// If we are on the 5x5 ring, proceed
				if ((x > -3 && x < 3 ) && (z > -3 && z < 3)) {	
					if ((x == 2 && z == 2) || (x == -2 && z == -2) || (x == 2 && z == -2) || (x == -2 && z == 2)) {
						//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);					
					}
					else {
						continue;
					}
				}

				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
				if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
					aCasingCount++;
				}				
				final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
				if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, false, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
					log("Layer has error. Height: "+aY);
					//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
					return 0;
				}
			}	
		}
		return aCasingCount;
	}
	
	public int checkPipes(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {
		int aCasingCount = 0;
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 1 && z == 1) || (x == -1 && z == -1) || (x == 1 && z == -1) || (x == -1 && z == 1) || (x == 0 && z == 0)) {
					Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
					int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
					if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
						aCasingCount++;
					}				
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
					if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, false, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
						log("Pipe has error. Height: "+aY);
						//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
						return 0;
					};
				}
			}	
		}
		return aCasingCount;
	}
	
	public int checkContainmentRing(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {
		int aCasingCount = 0;
		for (int x = -2; x < 3; x++) {
			for (int z = -2; z < 3; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 2 && z == 2) || (x == -2 && z == -2) || (x == 2 && z == -2) || (x == -2 && z == 2)) {
					continue;
				}

				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
				if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
					aCasingCount++;
				}				
				final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
				if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, false, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
					log("Layer has error. Height: "+aY);
					//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
					return 0;
				}
			}	
		}
		return aCasingCount;
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