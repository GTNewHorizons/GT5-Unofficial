package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMTE_ChemicalPlant extends GregtechMeta_MultiBlockBase {

	private int mSolidCasingTier = 0;
	private int mMachineCasingTier = 0;
	private int mPipeCasingTier = 0;
	private int mCoilTier = 0;

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
				"Grows Algae!",
				"Controller Block for the Algae Farm",
				"Size: 9x3x9 [WxHxL] (open)",
				"X           X",
				"X           X", 
				"XXXXXXXXX", 
				"Can process (Tier * 10) recipes",
				"Machine Hulls (all bottom layer)", 
				"Sterile Farm Casings (all non-hatches)", 
				"Controller (front centered)",
				"All hatches must be on the bottom layer",
				"All hulls must be the same tier, this dictates machine speed",
				"Does not require power or maintenance",
				"1x Output Bus", 
				"1x Input Bus (optional)",
				"1x Input Hatch (fill with water)",
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
		return null;
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
		return 0;
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
			aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(zDir, 0, 0);
			aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(zDir, 0, 0);
		}
		else {			
			aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(0, 0, xDir);
			aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(0, 0, xDir);
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
		return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 0);
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
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(zDir, 0, 0);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(zDir, 0, 0);
			}
			else {			
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(0, 0, xDir);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(0, 0, xDir);
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
