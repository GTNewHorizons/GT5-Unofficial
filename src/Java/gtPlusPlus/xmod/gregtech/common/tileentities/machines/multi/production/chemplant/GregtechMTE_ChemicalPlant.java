package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant;

import static gregtech.api.enums.GT_Values.E;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.helpers.GregtechPlusPlus_API.Multiblock_API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.multi.SpecialMultiBehaviour;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_ChemicalPlant extends GregtechMeta_MultiBlockBase {
	
	private int mSolidCasingTier = 0;
	private int mMachineCasingTier = 0;
	private int mPipeCasingTier = 0;
	private int mCoilTier = 0;


	/**
	 * Internal Recipe Map which holds the actual recipes, backed by the real map, shown by NEI.
	 */
	private static final GT_Recipe_Map mFluidChemicalReactorRecipes = new GT_Recipe_Map(
			new HashSet<GT_Recipe>(100),
			"gt.recipe.fluidchemicaleactor",
			"Chemical Plant",
			null,
			CORE.MODID+":textures/gui/FluidReactor",
			0,
			0,
			0,
			2,
			1,
			"Tier: ",
			1,
			E,
			true,
			false);
	

	public GregtechMTE_ChemicalPlant(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_ChemicalPlant(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_ChemicalPlant(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Chemical Plant";
	}

	@Override
	public String[] getTooltip() {
		return new String[] {
				"Controller Block for the Chemical Plant",
				"Heavy Industry, now right at your doorstep!",
				"Solid Casings dictate Chemical Plant tier",
				"Machine Casings dictate Hatch tier",			
				"Higher tier coils speed up the machine",
				"CuNi 50% , FeAlCr 100% , Ni4Cr 150% , ...",
				"Higher tier pipe casings boost parallel and reduce catalyst consumption",
				"+2 parallel per tier, 20% extra chance of not damaging catalyst per tier",
				"27x Coils",
				"18x Pipe Casings",
				"57x Tiered Machine Casings",
				"80+ Solid Casings",
				"Construction Guide:",
				"Controller is placed on a middle casing in the bottom layer",
				"Hatches can only be placed on the bottom layer edges",
				"7x7x7 Hollow frame of solid casings",
				"5x1x5 layer of solid casings (fills in top layer)",
				"5x1x5 layer of machine casings (fills in bottom layer)",
				"In the central 3x5x3:",
				"3x1x3 layer of Coils, surrounded by ring of Machine Casings",
				"3x1x3 layer of Pipe Casings",
				"3x1x3 layer of Coils",
				"3x1x3 layer of Pipe Casings",
				"3x1x3 layer of Coils, surrounded by ring of Machine Casings",
		};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(207));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {

		ITexture aOriginalTexture;

		// Check things exist client side (The worst code ever)
		if (aBaseMetaTileEntity.getWorld() != null) {

		}
		// Check the Tier Client Side
		int aTier = mSolidCasingTier;		

		if (aTier == 1) {
			aOriginalTexture = Textures.BlockIcons.CASING_BLOCKS[16];
		}
		else if (aTier == 2) {
			aOriginalTexture = Textures.BlockIcons.CASING_BLOCKS[49];
		}
		else if (aTier == 3) {
			aOriginalTexture = Textures.BlockIcons.CASING_BLOCKS[50];
		}
		else if (aTier == 4) {
			aOriginalTexture = Textures.BlockIcons.CASING_BLOCKS[48];
		}
		else {
			aOriginalTexture = Textures.BlockIcons.CASING_BLOCKS[11];
		}

		if (aSide == aFacing) {
			return new ITexture[]{aOriginalTexture, new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{aOriginalTexture};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		if (mFluidChemicalReactorRecipes.mRecipeList.isEmpty() || mFluidChemicalReactorRecipes.mRecipeList.size() !=  Recipe_GT.Gregtech_Recipe_Map.sFluidChemicalReactorRecipes.mRecipeList.size()) {
			if (!mFluidChemicalReactorRecipes.mRecipeList.isEmpty()) {
				mFluidChemicalReactorRecipes.mRecipeList.clear();
			}
			for (Recipe_GT i : Recipe_GT.Gregtech_Recipe_Map.sFluidChemicalReactorRecipes.mRecipeList) {
				mFluidChemicalReactorRecipes.add(i);
			}
		}
		return mFluidChemicalReactorRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public int getMaxParallelRecipes() {
		return 2 * getPipeCasingTier();
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 100;
	}

	private int getSolidCasingTier() {
		return mSolidCasingTier;
	}

	private int getMachineCasingTier() {
		return mMachineCasingTier;
	}
	private int getPipeCasingTier() {
		return mPipeCasingTier;
	}
	private int getCoilTier() {
		return mCoilTier;
	}

	private int getCasingTextureID() {
		// Check the Tier Client Side
		int aTier = mSolidCasingTier;		

		if (aTier == 1) {
			return 16;
		}
		else if (aTier == 2) {
			return 49;
		}
		else if (aTier == 3) {
			return 50;
		}
		else if (aTier == 4) {
			return 48;
		}
		else {
			return 11;
		}
	}

	public boolean addToMachineList(IGregTechTileEntity aTileEntity) {		
		int aMaxTier = getMachineCasingTier();		
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();		
		if (aMetaTileEntity instanceof GT_MetaTileEntity_TieredMachineBlock) {
			GT_MetaTileEntity_TieredMachineBlock aMachineBlock = (GT_MetaTileEntity_TieredMachineBlock) aMetaTileEntity;
			int aTileTier = aMachineBlock.mTier;
			if (aTileTier > aMaxTier) {
				Logger.INFO("Hatch tier too high.");
				return false;
			}
			else {
				return addToMachineList(aTileEntity, getCasingTextureID());
			}
		}
		else {
			Logger.INFO("Bad Tile Entity being added to hatch map."); // Shouldn't ever happen, but.. ya know..
			return false;
		}		
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setInteger("mSolidCasingTier", this.mSolidCasingTier);
		aNBT.setInteger("mMachineCasingTier", this.mMachineCasingTier);
		aNBT.setInteger("mPipeCasingTier", this.mPipeCasingTier);
		aNBT.setInteger("mCoilTier", this.mCoilTier);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mSolidCasingTier = aNBT.getInteger("mSolidCasingTier");
		mMachineCasingTier = aNBT.getInteger("mMachineCasingTier");
		mPipeCasingTier = aNBT.getInteger("mPipeCasingTier");
		mCoilTier = aNBT.getInteger("mCoilTier");
	}

	private static boolean isBlockSolidCasing(Block aBlock, int aMeta) {
		if (aBlock == null) {
			return false;
		}		
		if (aBlock == GregTech_API.sBlockCasings2 && aMeta == 0) {
			return true;
		}
		if (aBlock == GregTech_API.sBlockCasings4) {
			int aMetaStainlessCasing = 1;
			int aMetaTitaniumCasing = 2;
			int aMetaTungstenCasing = 0;
			if (aMeta == aMetaStainlessCasing || aMeta == aMetaTitaniumCasing || aMeta == aMetaTungstenCasing) {
				return true;				
			}
		}		
		return false;
	}
	private static boolean isBlockMachineCasing(Block aBlock, int aMeta) {
		Block aCasingBlock1 = GregTech_API.sBlockCasings1;
		if (aBlock == aCasingBlock1) {				
			if (aMeta > 9 || aMeta < 0) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
	}
	private static boolean isBlockPipeCasing(Block aBlock, int aMeta) {
		Block aCasingBlock2 = GregTech_API.sBlockCasings2;
		if (aBlock == aCasingBlock2) {
			int aMetaBronzePipeCasing = 12;
			int aMetaSteelPipeCasing = 13;
			int aMetaTitaniumPipeCasing = 14;
			int aMetaTungstenPipeCasing = 15;			
			if (aMeta > aMetaTungstenPipeCasing || aMeta < aMetaBronzePipeCasing) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
	}

	private static AutoMap<Integer> mValidCoilMetaCache;

	private static boolean isBlockCoil(Block aBlock, int aMeta) {		
		Block aCasingBlock;	
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			aCasingBlock = StaticFields59.getBlockCasings5();
		}
		else {
			aCasingBlock = GregTech_API.sBlockCasings1;				
		}		
		// Cache the meta values for later
		if (mValidCoilMetaCache == null || mValidCoilMetaCache.isEmpty()) {
			AutoMap<Integer> aValidCoilMeta = new AutoMap<Integer>();
			// Only use the right meta values available.
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
				aValidCoilMeta = Meta_GT_Proxy.GT_ValidHeatingCoilMetas;
			}
			else {
				aValidCoilMeta.put(12);
				aValidCoilMeta.put(13);
				aValidCoilMeta.put(14);
			}
			mValidCoilMetaCache = aValidCoilMeta;
		}		
		if (aBlock == aCasingBlock) {	
			for (int i: mValidCoilMetaCache.values()) {
				if (i == aMeta) {
					return true;
				}
			}			
		}
		return false;
	}


	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {	

		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 3;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 3;

		int tAmount = 0;
		Logger.INFO("Checking ChemPlant Structure");	

		// Require Air above controller
		boolean aAirCheck = aBaseMetaTileEntity.getAirOffset(0, 1, 0);

		if (!aAirCheck) {
			Logger.INFO("No Air Above Controller");
			return false;
		} else {

			//String aName = aInitStructureCheck != null ? ItemUtils.getLocalizedNameOfBlock(aInitStructureCheck, aInitStructureCheckMeta) : "Air";

			mSolidCasingTier = getSolidCasingTierCheck(aBaseMetaTileEntity, xDir, zDir);
			mMachineCasingTier = getMachineCasingTierCheck(aBaseMetaTileEntity, xDir, zDir);

			Logger.INFO("Initial Casing Check Complete, Solid Casing Tier: "+mSolidCasingTier+", Machine Casing Tier: "+mMachineCasingTier);

			int aSolidCasingCount = 0;
			int aMachineCasingCount = 0;
			int aPipeCount = 0;
			int aCoilCount = 0;

			aSolidCasingCount = checkSolidCasings(aBaseMetaTileEntity, aStack, xDir, zDir);
			aMachineCasingCount = checkMachineCasings(aBaseMetaTileEntity, aStack, xDir, zDir);
			aPipeCount = checkPipes(aBaseMetaTileEntity, aStack, xDir, zDir);
			aCoilCount = checkCoils(aBaseMetaTileEntity, aStack, xDir, zDir);

			Logger.INFO("Casing Counts: ");
			Logger.INFO("Solid: "+aSolidCasingCount+", Machine: "+aMachineCasingCount);
			Logger.INFO("Pipe: "+aPipeCount+", Coil: "+aCoilCount);


			Logger.INFO("Casing Tiers: ");
			Logger.INFO("Solid: "+getSolidCasingTier()+", Machine: "+getMachineCasingTier());
			Logger.INFO("Pipe: "+getPipeCasingTier()+", Coil: "+getCoilTier());

			// Attempt to sync fields here, so that it updates client side values.
			aBaseMetaTileEntity.getWorld().markBlockForUpdate(aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord());



			// Minimum 80/93 Solid Casings
			if (aSolidCasingCount < 80) {
				Logger.INFO("Not enough solid casings. Found "+aSolidCasingCount+", require: 80.");
				return false;
			}
			if (aMachineCasingCount != 57) {
				Logger.INFO("Not enough machine casings. Found "+aMachineCasingCount+", require: 57.");
				return false;
			}
			if (aPipeCount != 18) {
				Logger.INFO("Not enough pipe casings. Found "+aPipeCount+", require: 18.");
				return false;
			}
			if (aCoilCount != 27) {
				Logger.INFO("Not enough coils. Found "+aCoilCount+", require: 27.");
				return false;
			}	

			Logger.INFO("Structure Check Complete!");

			return true;
		}
	}


	public int checkCoils(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack, int aOffsetX, int aOffsetZ) {
		int tAmount = 0;
		int aCurrentCoilMeta = -1;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = 0; h < 8; h++) {					
					if (h == 1 || h == 3 || h == 5) {
						Block aBlock = aBaseMetaTileEntity.getBlockOffset(aOffsetX + i, h, aOffsetZ + j);
						int aMeta = aBaseMetaTileEntity.getMetaIDOffset(aOffsetX + i, h, aOffsetZ + j);							
						if (isBlockCoil(aBlock, aMeta)) {							
							if (aCurrentCoilMeta < 0) {
								aCurrentCoilMeta = aMeta;
							}							
							if (aCurrentCoilMeta != aMeta) {
								return tAmount;
							}
							else {
								tAmount++;
							}
						}					
					}
				}
			}
		}

		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			this.mCoilTier = (aCurrentCoilMeta+1);
		}
		else {
			if (aCurrentCoilMeta == 12) {
				this.mCoilTier = 1;
			}
			else if (aCurrentCoilMeta == 13) {
				this.mCoilTier = 2;				
			}
			else if (aCurrentCoilMeta == 14) {
				this.mCoilTier = 3;				
			}
			else {
				this.mCoilTier = 0;								
			}
		}		

		return tAmount;
	}


	public int checkPipes(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack, int aOffsetX, int aOffsetZ) {
		int tAmount = 0;
		int aCurrentPipeMeta = -1;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = 0; h < 8; h++) {					
					if (h == 2 || h == 4) {
						Block aBlock = aBaseMetaTileEntity.getBlockOffset(aOffsetX + i, h, aOffsetZ + j);
						int aMeta = aBaseMetaTileEntity.getMetaIDOffset(aOffsetX + i, h, aOffsetZ + j);							
						if (isBlockPipeCasing(aBlock, aMeta)) {							
							if (aCurrentPipeMeta < 0) {
								aCurrentPipeMeta = aMeta;
							}							
							if (aCurrentPipeMeta != aMeta) {
								return tAmount;
							}
							else {
								tAmount++;
							}
						}					
					}
				}
			}
		}

		if (aCurrentPipeMeta == 12) {
			this.mPipeCasingTier = 1;
		}
		else if (aCurrentPipeMeta == 13) {
			this.mPipeCasingTier = 2;				
		}
		else if (aCurrentPipeMeta == 14) {
			this.mPipeCasingTier = 3;				
		}
		else if (aCurrentPipeMeta == 15) {
			this.mPipeCasingTier = 4;				
		}
		else {
			this.mPipeCasingTier = 0;								
		}

		return tAmount;
	}


	public int checkSolidCasings(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack, int aOffsetX, int aOffsetZ) {	

		int tAmount = 0;

		// Only check a 7x7 ring
		for (int i = -3; i < 4; i++) {
			for (int j = -3; j < 4; j++) {
				// If we are on the 7x7 ring, proceed
				if (i == -3 || i == 3 || j == -3 || j == 3) {		
					IGregTechTileEntity aTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(aOffsetX + i, 0, aOffsetZ + j);
					Block aBlock = aBaseMetaTileEntity.getBlockOffset(aOffsetX + i, 0, aOffsetZ + j);
					int aMeta = aBaseMetaTileEntity.getMetaIDOffset(aOffsetX + i, 0, aOffsetZ + j);

					if (aTileEntity != null) {

						if (this.addToMachineList(aTileEntity)) {
							tAmount++;							
						}
						else {							
							final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
							if (aMetaTileEntity == null) {
								Logger.INFO("Error counting Bottom Layer Casing Ring. Bad Tile Entity. Found "+aTileEntity.getInventoryName());
								return tAmount;
							}
							//Handle controller
							if (aMetaTileEntity instanceof GregtechMTE_ChemicalPlant) {
								continue;
							}
							else {
								Logger.INFO("Error counting Bottom Layer Casing Ring. Bad Tile Entity. Found "+aMetaTileEntity.getInventoryName());
								return tAmount;
							}
						}						
					}
					else {
						if (isBlockSolidCasing(aBlock, aMeta)) {
							tAmount++;									
						}
						else {
							Logger.INFO("Error counting Bottom Layer Casing Ring. Found "+aBlock.getLocalizedName()+":"+aMeta);
							return tAmount;
						}
					}										
				}		
			}				
		}	

		// Check 5 layers Pillars
		for (int r=1;r<6;r++) {
			// Check Each Pillar/Corner
			for (int aPillar=0;aPillar<4;aPillar++) {				
				int i = 0;
				int j = 0;				
				if (aPillar == 0) {
					i = -3;
					j = -3;
				}
				else if (aPillar == 1) {
					i = 3;
					j = 3;
				}
				else if (aPillar == 2) {
					i = -3;
					j = 3;
				}
				else if (aPillar == 3) {
					i = 3;
					j = -3;
				}				
				Block aBlock = aBaseMetaTileEntity.getBlockOffset(aOffsetX + i, r, aOffsetZ + j);
				int aMeta = aBaseMetaTileEntity.getMetaIDOffset(aOffsetX + i, r, aOffsetZ + j);
				if (isBlockSolidCasing(aBlock, aMeta)) {
					tAmount++;									
				}
				else {
					Logger.INFO("Error counting Pillars. Found "+ItemUtils.getLocalizedNameOfBlock(aBlock, aMeta));
					return tAmount;
				}			
			}		
		}


		// Check top layer 7x7
		for (int i = -3; i < 4; i++) {
			for (int j = -3; j < 4; j++) {
				Block aBlock = aBaseMetaTileEntity.getBlockOffset(aOffsetX + i, 6, aOffsetZ + j);
				int aMeta = aBaseMetaTileEntity.getMetaIDOffset(aOffsetX + i, 6, aOffsetZ + j);
				if (isBlockSolidCasing(aBlock, aMeta)) {
					tAmount++;									
				}
				else {
					Logger.INFO("Error counting Top Layer casings. Found "+ItemUtils.getLocalizedNameOfBlock(aBlock, aMeta));
					return tAmount;
				}
			}
		}

		return tAmount;
	}


	public int checkMachineCasings(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack, int aOffsetX, int aOffsetZ) {	
		int tAmount = 0;
		int aHeight = 0;
		// Iterate once for each layer
		for (int aIteration=0;aIteration<3;aIteration++) {
			// Dynamically set height
			aHeight = (aIteration == 0 ? 0 : aIteration == 1 ? 1 : 5);
			// Only check a 5x5 area
			for (int i = -2; i < 3; i++) {
				for (int j = -2; j < 3; j++) {
					// If we are on the 5x5 ring, proceed
					if (i == -2 || i == 2 || j == -2 || j == 2) {	
						// If the second axis is on the outer ring, continue
						if (i < -2 || i > 2 || j < -2 || j > 2) {
							continue;
						}
						Block aBlock = aBaseMetaTileEntity.getBlockOffset(aOffsetX + i, aHeight, aOffsetZ + j);
						int aMeta = aBaseMetaTileEntity.getMetaIDOffset(aOffsetX + i, aHeight, aOffsetZ + j);
						if (isBlockMachineCasing(aBlock, aMeta)) {
							tAmount++;									
						}
						else {
							return tAmount;
						}					
					}		
				}				
			}			
		}		

		// Check bottom layer inner 3x3
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				Block aBlock = aBaseMetaTileEntity.getBlockOffset(aOffsetX + i, 0, aOffsetZ + j);
				int aMeta = aBaseMetaTileEntity.getMetaIDOffset(aOffsetX + i, 0, aOffsetZ + j);
				if (isBlockMachineCasing(aBlock, aMeta)) {
					tAmount++;									
				}
				else {
					return tAmount;
				}
			}
		}

		return tAmount;
	}

	public int getSolidCasingTierCheck(IGregTechTileEntity aBaseMetaTileEntity, int xDir, int zDir) {
		Block aInitStructureCheck;
		int aInitStructureCheckMeta;
		if (xDir == 0) {			
			aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(zDir, 1, 0);
			aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(zDir, 1, 0);
		}
		else {			
			aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(0, 1, xDir);
			aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(0, 1, xDir);
		}
		if (aInitStructureCheck == GregTech_API.sBlockCasings2) {
			int aMetaSteelCasing = 0;
			if (aInitStructureCheckMeta == aMetaSteelCasing) {
				return 1;
			}
		}
		else if (aInitStructureCheck == GregTech_API.sBlockCasings4) {
			int aMetaStainlessCasing = 1;
			int aMetaTitaniumCasing = 2;
			int aMetaTungstenCasing = 0;
			if (aInitStructureCheckMeta == aMetaStainlessCasing) {
				return 2;
			}
			else if (aInitStructureCheckMeta == aMetaTitaniumCasing) {
				return 3;
			}
			else if (aInitStructureCheckMeta == aMetaTungstenCasing) {
				return 4;
			}
		}
		return 0;
	}

	public int getMachineCasingTierCheck(IGregTechTileEntity aBaseMetaTileEntity, int xDir, int zDir) {
		Block aInitStructureCheck;
		int aInitStructureCheckMeta;
		Logger.INFO(""+xDir+", "+zDir);
		if (xDir == 0) {		
			if (zDir > 0) {
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(0, 0, 1);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(0, 0, 1);				
			}
			else {
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(0, 0, -1);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(0, 0, -1);				
			}

		}
		else {			
			if (xDir > 0) {
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(1, 0, 0);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(1, 0, 0);				
			}
			else {
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(-1, 0, 0);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(-1, 0, 0);				
			}
		}

		if (isBlockMachineCasing(aInitStructureCheck, aInitStructureCheckMeta)) {
			Logger.INFO("Using Meta "+aInitStructureCheckMeta);
			return aInitStructureCheckMeta;			
		}
		return 0;
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

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}
	
	// Same speed bonus as pyro oven
	public int getSpeedBonus() {
		return 50 * (this.mCoilTier - 2);
	}

	public int getMaxCatalystDurability() {
		return 50;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		// Silly Client Syncing
		if (aBaseMetaTileEntity.isClientSide()) {
			this.mSolidCasingTier = getCasingTierOnClientSide();
		}

	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {		
		return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), getSpeedBonus());
	}
	
	
	@Override
	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {
		
		// Based on the Processing Array. A bit overkill, but very flexible.		

		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		long tEnergy = getMaxInputEnergy();
		log("Running checkRecipeGeneric(0)");
		
		GT_Recipe tRecipe = findRecipe(
				getBaseMetaTileEntity(), mLastRecipe, false,
				gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);
		
		
		log("Running checkRecipeGeneric(1)");
		// Remember last recipe - an optimization for findRecipe()
		this.mLastRecipe = tRecipe;
		
		
		if (tRecipe == null) {
			log("BAD RETURN - 1");
			return false;
		}	
		
		if (tRecipe.mSpecialValue > this.mSolidCasingTier) {
			log("solid tier is too low");
			return false;
		}
		
		
		if (!this.canBufferOutputs(tRecipe, aMaxParallelRecipes)) {
			log("BAD RETURN - 2");
			return false;
		}
		
		// checks if it has enough catalyst durabilety
		ArrayList<ItemStack>tCatalysts = null; 
		int tMaxParrallelCatalyst = aMaxParallelRecipes;
		ItemStack tCatalystRecipe = findCatalyst(tRecipe.mInputs);
		if (tCatalystRecipe != null) {
			log("needs catalyst");
			tCatalysts = new ArrayList<ItemStack>();
			tMaxParrallelCatalyst = getCatalysts(aItemInputs, tCatalystRecipe, aMaxParallelRecipes,tCatalysts);
		}
			
		if (tMaxParrallelCatalyst == 0) {
			log("found not enough catalists catalyst");
			return false;
		}

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;
		log("aEUPercent "+aEUPercent);
		log("mEUt "+tRecipe.mEUt);

		int parallelRecipes = 0;

		log("parallelRecipes: "+parallelRecipes);
		log("aMaxParallelRecipes: "+tMaxParrallelCatalyst);
		log("tTotalEUt: "+tTotalEUt);
		log("tVoltage: "+tVoltage);
		log("tEnergy: "+tEnergy);
		log("tRecipeEUt: "+tRecipeEUt);
		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < tMaxParrallelCatalyst && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
				log("Broke at "+parallelRecipes+".");
				break;
			}
			log("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+".");
			tTotalEUt += tRecipeEUt;
		}

		if (parallelRecipes == 0) {
			log("BAD RETURN - 3");
			return false;
		}
		
		if (tCatalysts != null) {
			log("damaging catalyst");
			for (int j = 0;j<parallelRecipes;j++) {
				log("j = "+j);
				for (int i = 0;i<tCatalysts.size();i++) {
					log("i = "+i);
					if (tCatalysts.get(i) != null && tCatalysts.get(i).stackSize != 0) {
						damageCatalyst(tCatalysts.get(i));
						break;
					}		
				}
			}
		}

		// -- Try not to fail after this point - inputs have already been consumed! --
		

		// Convert speed bonus to duration multiplier
		// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
		aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
		float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
		this.mMaxProgresstime = (int)(tRecipe.mDuration * tTimeFactor);

		this.mEUt = (int)Math.ceil(tTotalEUt);

		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;		

		// Overclock
		if (this.mEUt <= 16) {
			this.mEUt = (this.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
			this.mMaxProgresstime = (this.mMaxProgresstime / (1 << tTier - 1));
		} else {
			while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
				this.mEUt *= 4;
				this.mMaxProgresstime /= 2;
			}
		}

		if (this.mEUt > 0) {
			this.mEUt = (-this.mEUt);
		}
		

		this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

		// Collect fluid outputs
		FluidStack[] tOutputFluids = new FluidStack[tRecipe.mFluidOutputs.length];
		for (int h = 0; h < tRecipe.mFluidOutputs.length; h++) {
			if (tRecipe.getFluidOutput(h) != null) {
				tOutputFluids[h] = tRecipe.getFluidOutput(h).copy();
				tOutputFluids[h].amount *= parallelRecipes;
			}
		}

		// Collect output item types
		ItemStack[] tOutputItems = new ItemStack[tRecipe.mOutputs.length];
		for (int h = 0; h < tRecipe.mOutputs.length; h++) {
			if (tRecipe.getOutput(h) != null) {
				tOutputItems[h] = tRecipe.getOutput(h).copy();
				tOutputItems[h].stackSize = 0;
			}
		}

		// Set output item stack sizes (taking output chance into account)
		for (int f = 0; f < tOutputItems.length; f++) {
			if (tRecipe.mOutputs[f] != null && tOutputItems[f] != null) {
				for (int g = 0; g < parallelRecipes; g++) {
					if (getBaseMetaTileEntity().getRandomNumber(aOutputChanceRoll) < tRecipe.getOutputChance(f))
						tOutputItems[f].stackSize += tRecipe.mOutputs[f].stackSize;
				}
			}
		}

		tOutputItems = removeNulls(tOutputItems);

		// Sanitize item stack size, splitting any stacks greater than max stack size
		List<ItemStack> splitStacks = new ArrayList<ItemStack>();
		for (ItemStack tItem : tOutputItems) {
			while (tItem.getMaxStackSize() < tItem.stackSize) {
				ItemStack tmp = tItem.copy();
				tmp.stackSize = tmp.getMaxStackSize();
				tItem.stackSize = tItem.stackSize - tItem.getMaxStackSize();
				splitStacks.add(tmp);
			}
		}

		if (splitStacks.size() > 0) {
			ItemStack[] tmp = new ItemStack[splitStacks.size()];
			tmp = splitStacks.toArray(tmp);
			tOutputItems = ArrayUtils.addAll(tOutputItems, tmp);
		}

		// Strip empty stacks
		List<ItemStack> tSList = new ArrayList<ItemStack>();
		for (ItemStack tS : tOutputItems) {
			if (tS.stackSize > 0) tSList.add(tS);
		}
		tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);

		// Commit outputs
		this.mOutputItems = tOutputItems;
		this.mOutputFluids = tOutputFluids;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		log("GOOD RETURN - 1");
		return true;
	}

	private int getCatalysts(ItemStack[] aItemInputs,ItemStack aRecipeCatalyst,int aMaxParrallel,ArrayList<ItemStack> aOutPut) {
		int allowedParrallel = 0;
		for (final ItemStack aInput : aItemInputs) {
			if (aRecipeCatalyst.isItemEqual(aInput)) {
					if (aInput.stackSize == 1) {
						int damage = getDamage(aInput) + aMaxParrallel;
						if (damage >getMaxCatalystDuarbilerty() ) {
							aOutPut.add(aInput);
							allowedParrallel += aMaxParrallel + (getMaxCatalystDuarbilerty() - damage);
							if (allowedParrallel >aMaxParrallel ) {
								return aMaxParrallel;
							}	
							continue;
						}
					}
					aOutPut.add(aInput);
					return aMaxParrallel;
			}
		}
		return allowedParrallel;
	}

	private ItemStack findCatalyst(ItemStack[] aItemInputs) {
		if (aItemInputs != null) {
			for (final ItemStack aInput : aItemInputs) {
				if (aInput != null) {
					if (aInput.isItemEqual(GenericChem.mRedCatalyst))
						return aInput;
					else if (aInput.isItemEqual(GenericChem.mYellowCatalyst))
						return aInput;
					else if (aInput.isItemEqual(GenericChem.mBlueCatalyst))
						return aInput;
					else if (aInput.isItemEqual(GenericChem.mOrangeCatalyst))
						return aInput;
					else if (aInput.isItemEqual(GenericChem.mPurpleCatalyst))
						return aInput;
					else if (aInput.isItemEqual(AgriculturalChem.mGreenCatalyst))
						return aInput;
					else if (aInput.isItemEqual(GenericChem.mBrownCatalyst))
						return aInput;
				}
			}
		}
		return null;
	}
	
	
	private void damageCatalyst(ItemStack aStack) {
		if (MathUtils.randFloat(0, 10000000)/10000000f < (1.2f - (0.2 * this.mPipeCasingTier))) {
			int damage = getDamage(aStack) + 1;
			log("damage catalyst "+damage);
			if (damage >= getMaxCatalystDurability()) {
				log("consume catalyst");
				ItemStack emptyCatalyst = ItemUtils.getSimpleStack(AgriculturalChem.mCatalystCarrier,1);
				addOutput(emptyCatalyst);
				setDamage(aStack,0);
				aStack.stackSize -= 1;
			} 
			else {
				setDamage(aStack,damage);
			}
		} 
		else 
			log("not consuming catalyst");
		}
	}
	
	private int getDamage(ItemStack aStack) {
		if (aStack.getTagCompound() == null || aStack.getTagCompound().hasNoTags()) {
			final NBTTagCompound tagMain = new NBTTagCompound();
			final NBTTagCompound tagNBT = new NBTTagCompound();
			tagNBT.setInteger("Damage", 0);
			tagMain.setTag("catalyst", tagNBT);		
			aStack.setTagCompound(tagMain);		
		}
		NBTTagCompound aNBT = aStack.getTagCompound();
		return aNBT.getCompoundTag("catalyst").getInteger("Damage");
	}
	
	private void setDamage(ItemStack aStack,int aAmount) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		aNBT = aNBT.getCompoundTag("catalyst");
		aNBT.setInteger("Damage", aAmount);
	}
	


	@SideOnly(Side.CLIENT)
	private final int getCasingTierOnClientSide() {

		if (this == null || this.getBaseMetaTileEntity().getWorld() == null) {
			return 0;
		}
		try {
			Block aInitStructureCheck;
			int aInitStructureCheckMeta;
			IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
			int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 3;
			int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 3;
			if (xDir == 0) {			
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(zDir, 1, 0);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(zDir, 1, 0);
			}
			else {			
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(0, 1, xDir);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(0, 1, xDir);
			}
			if (aInitStructureCheck == GregTech_API.sBlockCasings2) {
				int aMetaSteelCasing = 0;
				if (aInitStructureCheckMeta == aMetaSteelCasing) {
					return 1;
				}
			}
			else if (aInitStructureCheck == GregTech_API.sBlockCasings4) {
				int aMetaStainlessCasing = 1;
				int aMetaTitaniumCasing = 2;
				int aMetaTungstenCasing = 0;
				if (aInitStructureCheckMeta == aMetaStainlessCasing) {
					return 2;
				}
				else if (aInitStructureCheckMeta == aMetaTitaniumCasing) {
					return 3;
				}
				else if (aInitStructureCheckMeta == aMetaTungstenCasing) {
					return 4;
				}
			}
			return 0;
		}
		catch (Throwable t) {
			t.printStackTrace();
			return 0;
		}

	}








}
