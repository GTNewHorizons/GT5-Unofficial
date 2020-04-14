package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GTPP_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.GT_MetaTileEntity_Hatch_MillingBalls;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_IsaMill extends GregtechMeta_MultiBlockBase {

	protected int fuelConsumption = 0;
	protected int fuelValue = 0;
	protected int fuelRemaining = 0;
	protected boolean boostEu = false;

	private static ITexture frontFace;
	private static ITexture frontFaceActive;

	private ArrayList<GT_MetaTileEntity_Hatch_MillingBalls> mMillingBallBuses = new ArrayList<GT_MetaTileEntity_Hatch_MillingBalls>();
	private static final DamageSource mIsaMillDamageSource = new DamageSource("gtpp.grinder").setDamageBypassesArmor();

	public GregtechMetaTileEntity_IsaMill(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		frontFaceActive = new GT_RenderedTexture(new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE5"));
		frontFace = new GT_RenderedTexture(new CustomIcon("iconsets/Grinder/GRINDER5"));
	}

	public GregtechMetaTileEntity_IsaMill(String aName) {
		super(aName);
	}

	public String[] getTooltip() {
		return new String[]{
				"Controller Block for the Large Grinding Machine",
				"Engine Intake Casings must not be obstructed in front (only air blocks)",
				"Supply Semifluid Fuels and 2000L of Lubricant per hour to run.",
				"Supply 80L of Oxygen per second to boost output (optional).",
				"Default: Produces 2048EU/t at 100% efficiency",
				"Boosted: Produces 6144EU/t at 150% efficiency",
				"Size(WxHxD): 3x3x4, Controller (front centered)",
				"3x3x4 of Stable Titanium Machine Casing (hollow, Min 16!)",
				"All hatches except dynamo can replace any Stable Titanium casing in middle two segments",
				"2x Steel Gear Box Machine Casing inside the Hollow Casing",
				"8x Engine Intake Machine Casing (around controller)",
				"2x Input Hatch (Fuel/Lubricant)",
				"1x Maintenance Hatch",
				"1x Muffler Hatch",
				"1x Dynamo Hatch (back centered)",
		};
	}


	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return new ITexture[]{
				Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(2)],
				aFacing == aSide ? aActive ? frontFaceActive : frontFace : Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(2)]};
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return getMaxEfficiency(aStack) > 0;
	}

	@Override
	public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {

		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MillingBalls) {
			log("Found GT_MetaTileEntity_Hatch_MillingBalls");
			return addToMachineListInternal(mMillingBallBuses, aMetaTileEntity, aBaseCasingIndex);
		}		
		return super.addToMachineList(aTileEntity, aBaseCasingIndex);
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeDieselEngine.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GTPP_Recipe.GTPP_Recipe_Map.sOreMillRecipes;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
				this.mMillingBallBuses.clear();
			}
		}
		if (aTick % 20 == 0) {
			checkForEntities(aBaseMetaTileEntity, aTick);			
		}
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	private final AutoMap<BlockPos> mFrontBlockPosCache = new AutoMap<BlockPos>();

	public void checkForEntities(IGregTechTileEntity aBaseMetaTileEntity, long aTime) {

		if (aTime % 100 == 0) {
			mFrontBlockPosCache.clear();
		}
		if (mFrontBlockPosCache.isEmpty()) {
			byte tSide = aBaseMetaTileEntity.getBackFacing();
			int aTileX = aBaseMetaTileEntity.getXCoord();
			int aTileY = aBaseMetaTileEntity.getYCoord();
			int aTileZ = aBaseMetaTileEntity.getZCoord();
			boolean xFacing = (tSide == 4 || tSide == 5);
			boolean zFacing = (tSide == 2 || tSide == 3);

			// Check Casings
			int aDepthOffset = (tSide == 2 || tSide == 4) ? 1 : -1;
			for (int aHorizontalOffset = -1; aHorizontalOffset < 2; aHorizontalOffset++) {
				for (int aVerticalOffset = -1; aVerticalOffset < 2; aVerticalOffset++) {			
					int aX = !xFacing ? (aTileX + aHorizontalOffset) : (aTileX + aDepthOffset);
					int aY = aTileY + aVerticalOffset;
					int aZ = !zFacing ? (aTileZ + aHorizontalOffset) : (aTileZ + aDepthOffset);					
					mFrontBlockPosCache.add(new BlockPos(aX, aY, aZ, aBaseMetaTileEntity.getWorld()));
				}
			}
		}

		AutoMap<EntityLivingBase> aEntities = getEntities(mFrontBlockPosCache, aBaseMetaTileEntity.getWorld());
		if (!aEntities.isEmpty()) {
			for (EntityLivingBase aFoundEntity : aEntities) {
				if (aFoundEntity instanceof EntityPlayer) {
					EntityPlayer aPlayer = (EntityPlayer) aFoundEntity;
					if (PlayerUtils.isCreative(aPlayer) || !PlayerUtils.canTakeDamage(aPlayer)) {
						continue;
					}
					else {
						if (aFoundEntity.getHealth() > 0) {
							EntityUtils.doDamage(aFoundEntity, mIsaMillDamageSource, (int) (aFoundEntity.getMaxHealth() / 5));
							if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())) {
								generateParticles(aFoundEntity);
							}
						}
					}
				}
				if (aFoundEntity.getHealth() > 0) {
					EntityUtils.doDamage(aFoundEntity, mIsaMillDamageSource, Math.max(1, (int) (aFoundEntity.getMaxHealth() / 3)));
					if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())) {
						generateParticles(aFoundEntity);
					}
				}
			}
		}
	}

	private static final AutoMap<EntityLivingBase> getEntities(AutoMap<BlockPos> aPositionsToCheck, World aWorld){
		AutoMap<EntityLivingBase> aEntities = new AutoMap<EntityLivingBase>();
		HashSet<Chunk> aChunksToCheck = new HashSet<Chunk>();
		if (!aPositionsToCheck.isEmpty()) {
			Chunk aLocalChunk;
			for (BlockPos aPos : aPositionsToCheck) {
				aLocalChunk = aWorld.getChunkFromBlockCoords(aPos.xPos, aPos.zPos);
				aChunksToCheck.add(aLocalChunk);				
			}
		}
		if (!aChunksToCheck.isEmpty()) {	
			AutoMap<EntityLivingBase> aEntitiesFound = new AutoMap<EntityLivingBase>();
			for (Chunk aChunk : aChunksToCheck) {				
				if (aChunk.isChunkLoaded) {
					List[] aEntityLists = aChunk.entityLists;
					for (List aEntitySubList : aEntityLists) {
						for (Object aEntity : aEntitySubList) {
							if (aEntity instanceof EntityLivingBase) {
								EntityLivingBase aPlayer = (EntityLivingBase) aEntity;
								aEntitiesFound.add(aPlayer);
							}
						}
					}
				}				
			}
			if (!aEntitiesFound.isEmpty()) {
				for (EntityLivingBase aEntity : aEntitiesFound) {
					BlockPos aPlayerPos = EntityUtils.findBlockPosOfEntity(aEntity);
					for (BlockPos aBlockSpaceToCheck : aPositionsToCheck) {
						if (aBlockSpaceToCheck.equals(aPlayerPos)) {
							aEntities.add(aEntity);
						}
					}
				}
			}
		}		
		return aEntities;
	}

	private static void generateParticles(EntityLivingBase aEntity) {
		BlockPos aPlayerPosBottom = EntityUtils.findBlockPosOfEntity(aEntity);
		BlockPos aPlayerPosTop = aPlayerPosBottom.getUp();
		AutoMap<BlockPos> aEntityPositions = new AutoMap<BlockPos>();
		aEntityPositions.add(aPlayerPosBottom);
		aEntityPositions.add(aPlayerPosTop);
		for (int i = 0; i < 64; i++) {	
			BlockPos aEffectPos = aEntityPositions.get(aEntity.height > 1f ? MathUtils.randInt(0, 1) : 0);
			float aOffsetX = MathUtils.randFloat(-0.35f, 0.35f);
			float aOffsetY = MathUtils.randFloat(-0.25f, 0.35f);
			float aOffsetZ = MathUtils.randFloat(-0.35f, 0.35f);
			aEntity.worldObj.spawnParticle("reddust", aEffectPos.xPos + aOffsetX, aEffectPos.yPos + 0.3f + aOffsetY, aEffectPos.zPos + aOffsetZ, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		return checkRecipeGeneric();
	}

	@Override
	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		byte tSide = aBaseMetaTileEntity.getBackFacing();
		int aTileX = aBaseMetaTileEntity.getXCoord();
		int aTileY = aBaseMetaTileEntity.getYCoord();
		int aTileZ = aBaseMetaTileEntity.getZCoord();
		boolean xFacing = (tSide == 4 || tSide == 5);
		boolean zFacing = (tSide == 2 || tSide == 3);
		int aCasingCount = 0;
		// Check Intake Hatches
		for (int aHorizontalOffset = -1; aHorizontalOffset < 2; aHorizontalOffset++) {
			for (int aVerticalOffset = -1; aVerticalOffset < 2; aVerticalOffset++) {
				if (aHorizontalOffset == 0 && aVerticalOffset == 0) {
					continue;
				}			
				int aX = !xFacing ? (aTileX + aHorizontalOffset) : aTileX;
				int aY = aTileY + aVerticalOffset;
				int aZ = !zFacing ? (aTileZ + aHorizontalOffset) : aTileZ;
				Block aIntakeBlock = aBaseMetaTileEntity.getBlock(aX, aY, aZ);
				int aIntakeMeta = aBaseMetaTileEntity.getMetaID(aX, aY, aZ);				
				if (!isValidBlockForStructure(null, 0, false, aIntakeBlock, aIntakeMeta, getIntakeBlock(), getIntakeMeta())) {
					return false; // Not intake casing surrounding controller					
				}
			}
		}
		// Check Casings
		int aStartDepthOffset = (tSide == 2 || tSide == 4) ? -1 : 1;
		int aFinishDepthOffset = (tSide == 2 || tSide == 4) ? -8 : 8;
		for (int aDepthOffset = aStartDepthOffset; aDepthOffset != aFinishDepthOffset;) {
			for (int aHorizontalOffset = -1; aHorizontalOffset < 2; aHorizontalOffset++) {
				for (int aVerticalOffset = -1; aVerticalOffset < 2; aVerticalOffset++) {
					if (aHorizontalOffset == 0 && aVerticalOffset == 0) {
						continue;
					}				
					int aX = !xFacing ? (aTileX + aHorizontalOffset) : (aTileX + aDepthOffset);
					int aY = aTileY + aVerticalOffset;
					int aZ = !zFacing ? (aTileZ + aHorizontalOffset) : (aTileZ + aDepthOffset);
					Block aCasingBlock = aBaseMetaTileEntity.getBlock(aX, aY, aZ);
					int aCasingMeta = aBaseMetaTileEntity.getMetaID(aX, aY, aZ);	
					IGregTechTileEntity aTileEntity = aBaseMetaTileEntity.getIGregTechTileEntity(aX, aY, aZ);
					if (aTileEntity != null) {
						final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
						if (aMetaTileEntity != null) {
							if (aMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill) {
								Logger.INFO("Don't be cheeky, only one controller per Mill.");
								return false;
							}
						}
					}

					if (!isValidBlockForStructure(aTileEntity, getCasingTextureIndex(), true, aCasingBlock, aCasingMeta, getCasingBlock(), getCasingMeta())) {
						Logger.INFO("Bad casing.");
						return false; // Not valid casing					
					}
					else {
						if (aTileEntity == null) {
							aCasingCount++;
						}
					}
				}
			}
			// Count Backwards for 2 axis
			if (aStartDepthOffset == -1) {
				aDepthOffset--;
			}
			// Count Forwards for 2 axis
			else {
				aDepthOffset++;
			}
		}

		// Check Gear Boxes
		for (int aInternalDepthAxis = 1; aInternalDepthAxis < 7; aInternalDepthAxis++) {
			if(aBaseMetaTileEntity.getBlockAtSideAndDistance(tSide, aInternalDepthAxis) != getGearboxBlock() || aBaseMetaTileEntity.getMetaIDAtSideAndDistance(tSide, aInternalDepthAxis) != getGearboxMeta()) {
				Logger.INFO("Missing Gearbox at depth "+aInternalDepthAxis);
				return false;
			}
		}
		return aCasingCount >= 48;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasings5Misc;
	}

	public byte getCasingMeta() {
		return 0;
	}

	public Block getIntakeBlock() {
		return ModBlocks.blockCasings5Misc;
	}

	public byte getIntakeMeta() {
		return 0;
	}

	public Block getGearboxBlock() {
		return ModBlocks.blockCasings5Misc;
	}

	public byte getGearboxMeta() {
		return 2;
	}

	public byte getCasingTextureIndex() {
		return 66;
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IsaMill(this.mName);
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 1;
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return boostEu ? 20000 : 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 64;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public String[] getExtraInfoData() {
		return new String[]{
				"IsaMill Grinding Machine",
				"Current Efficiency: " + (mEfficiency / 100) + "%",
				getIdealStatus() == getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance"
		};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
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
	public String getMachineType() {
		return "Grinding Machine";
	}

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	/*
	 *  Milling Ball Handling
	 */
	
	@Override
    public ArrayList<ItemStack> getStoredInputs() {
		ArrayList<ItemStack> tItems = super.getStoredInputs();
        for (GT_MetaTileEntity_Hatch_MillingBalls tHatch : mMillingBallBuses) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {                
            	tItems.addAll(tHatch.getContentUsageSlots());            	
            }
        }		
		return tItems;
	}


	public int getMaxBallDurability(ItemStack aStack) {
		return ItemGenericChemBase.getMaxBallDurability(aStack);
	}

	private ItemStack findMillingBall(ItemStack[] aItemInputs) {		
		if (mMillingBallBuses.isEmpty() || mMillingBallBuses.size() > 1) {
			return null;
		}
		else {
			GT_MetaTileEntity_Hatch_MillingBalls aBus = mMillingBallBuses.get(0);
			if (aBus != null) {
				AutoMap<ItemStack> aAvailableItems = aBus.getContentUsageSlots();
				if (!aAvailableItems.isEmpty()) {
					for (final ItemStack aInput : aItemInputs) {
						if (ItemUtils.isMillingBall(aInput)) {
							for (ItemStack aBall : aAvailableItems) {
								if (GT_Utility.areStacksEqual(aBall, aInput, true)) {
									Logger.INFO("Found a valid milling ball to use.");
									return aBall;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}


	private void damageMillingBall(ItemStack aStack) {
		if (MathUtils.randFloat(0, 10000000)/10000000f < (1.2f - (0.2 * 1))) {
			int damage = getMillingBallDamage(aStack) + 1;
			log("damage milling ball "+damage);
			if (damage >= getMaxBallDurability(aStack)) {
				log("consuming milling ball");
				aStack.stackSize -= 1;
			} 
			else {
				setDamage(aStack,damage);
			}
		} 
		else {
			log("not damaging milling ball");
		}
	}

	private int getMillingBallDamage(ItemStack aStack) {
		return ItemGenericChemBase.getMillingBallDamage(aStack);
	}

	private void setDamage(ItemStack aStack,int aAmount) {
		ItemGenericChemBase.setMillingBallDamage(aStack, aAmount);
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

		// checks if it has a milling ball with enough durability
		ItemStack tMillingBallRecipe = findMillingBall(aItemInputs);
		if (tMillingBallRecipe == null) {
			log("does not have milling ball");
			return false;
		}

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

		aMaxParallelRecipes = this.canBufferOutputs(tRecipe, aMaxParallelRecipes);
		if (aMaxParallelRecipes == 0) {
			log("BAD RETURN - 2");
			return false;
		}

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;
		log("aEUPercent "+aEUPercent);
		log("mEUt "+tRecipe.mEUt);

		int parallelRecipes = 0;

		log("parallelRecipes: "+parallelRecipes);
		log("aMaxParallelRecipes: "+1);
		log("tTotalEUt: "+tTotalEUt);
		log("tVoltage: "+tVoltage);
		log("tEnergy: "+tEnergy);
		log("tRecipeEUt: "+tRecipeEUt);
		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < 1 && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
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


		// Damage Milling ball once all is said and done.
		if (tMillingBallRecipe != null) {
			log("damaging milling ball");
			damageMillingBall(tMillingBallRecipe);
		}
		
		// Commit outputs
		this.mOutputItems = tOutputItems;
		this.mOutputFluids = tOutputFluids;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		log("GOOD RETURN - 1");
		return true;
	}

}
