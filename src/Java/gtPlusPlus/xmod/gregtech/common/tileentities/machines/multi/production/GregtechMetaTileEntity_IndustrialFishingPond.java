package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_IndustrialFishingPond extends GregtechMeta_MultiBlockBase {

	private boolean isUsingControllerCircuit = false;
	private static final Item circuit = CI.getNumberedCircuit(0).getItem();
	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialFishingPond> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_IndustrialFishingPond(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialFishingPond(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialFishingPond(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Fish Trap";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Fishing Pond")
				.addInfo("Can process (Tier + 1) * 2 recipes")
				.addInfo("Put a numbered circuit into the input bus.")
				.addInfo("Circuit 14 for Fish")
				.addInfo("Circuit 15 for Junk")
				.addInfo("Circuit 16 for Treasure")
				.addPollutionAmount(getPollutionPerTick(null) * 20)
				.addSeparator()
				.beginStructureBlock(9, 3, 9, true)
				.addController("Front Center")
				.addCasingInfo("Aquatic Casings", 64)
				.addInputBus("Any Casing", 1)
				.addOutputBus("Any Casing", 1)
				.addInputHatch("Any Casing", 1)
				.addEnergyHatch("Any Casing", 1)
				.addMaintenanceHatch("Any Casing", 1)
				.addMufflerHatch("Any Casing", 1)
				.toolTipFinisher("GT++");
		return tt;
	}

	@Override
	protected IAlignmentLimits getInitialAlignmentLimits() {
		// fuck
		return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialFishingPond> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialFishingPond>builder()
					.addShape(mName, transpose(new String[][]{
							{"XXXXXXXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X", "XXXXXXXXX"},
							{"XXXX~XXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X", "XXXXXXXXX"},
							{"XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX"},
					}))
					.addElement(
							'X',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_IndustrialFishingPond::addIndustrialFishingPondList, getCasingTextureIndex(), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													getCasingBlock(), getCasingMeta()
											)
									)
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	public final boolean addIndustrialFishingPondList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
			}
		}
		return false;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 4, 1, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 4, 1, 0) && mCasing >= 64;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()),
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE
							: Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER) };
		}
		return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()) };
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
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		if (aStack != null) {
			log("Found " + aStack.getDisplayName());
			if (aStack.getItem() == circuit) {
				this.isUsingControllerCircuit = true;
				this.mMode = aStack.getItemDamage();
				log("Found Circuit!");
			} else {
				this.isUsingControllerCircuit = false;
			}
		} else {
			this.isUsingControllerCircuit = false;
		}
		if (!hasGenerateRecipes) {
			log("Generating Recipes.");
			generateRecipes();
		}
		if (hasGenerateRecipes) {
			if (!checkForWater()) {
				return false;
			}

			log("Trying to run recipe.");
			ArrayList<ItemStack> tItems = getStoredInputs();
			ArrayList<FluidStack> tFluids = getStoredFluids();
			ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
			FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);

			if (!isUsingControllerCircuit && tItems.size() == 0) {
				return false;
			}

			return checkRecipeGeneric(tItemInputs, tFluidInputs, getMaxParallelRecipes(), 100, 80, 100);
		}
		return true;
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return (2 * (GT_Utility.getTier(this.getMaxInputVoltage())+1));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 100;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 1;
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
		return ModBlocks.blockCasings3Misc;
	}

	public byte getCasingMeta() {
		return 0;
	}

	public int getCasingTextureIndex() {
		return TAE.GTPP_INDEX(32);
	}

	public boolean checkForWater() {

		// Get Facing direction
		IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
		int mDirectionX = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int mCurrentDirectionX;
		int mCurrentDirectionZ;
		int mOffsetX_Lower = 0;
		int mOffsetX_Upper = 0;
		int mOffsetZ_Lower = 0;
		int mOffsetZ_Upper = 0;

		mCurrentDirectionX = 4;
		mCurrentDirectionZ = 4;

		mOffsetX_Lower = -4;
		mOffsetX_Upper = 4;
		mOffsetZ_Lower = -4;
		mOffsetZ_Upper = 4;

		// if (aBaseMetaTileEntity.fac)

		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX
				* mCurrentDirectionX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ
				* mCurrentDirectionZ;

		int tAmount = 0;
		for (int i = mOffsetX_Lower + 1; i <= mOffsetX_Upper - 1; ++i) {
			for (int j = mOffsetZ_Lower + 1; j <= mOffsetZ_Upper - 1; ++j) {
				for (int h = 0; h < 2; h++) {
					Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
					// byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
					if (tBlock == Blocks.air || tBlock == Blocks.flowing_water || tBlock == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)) {
						if (this.getStoredFluids() != null) {
							for (FluidStack stored : this.getStoredFluids()) {
								if (stored.isFluidEqual(FluidUtils.getFluidStack("water", 1))) {
									if (stored.amount >= 1000) {
										// Utils.LOG_WARNING("Going to try swap an air block for water from inut bus.");
										stored.amount -= 1000;
										Block fluidUsed = Blocks.water;																			
										aBaseMetaTileEntity.getWorld().setBlock(
												aBaseMetaTileEntity.getXCoord() + xDir + i,
												aBaseMetaTileEntity.getYCoord() + h,
												aBaseMetaTileEntity.getZCoord() + zDir + j, fluidUsed);

									}
								}
							}
						}
					}
					tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
					if (tBlock == Blocks.water || tBlock == Blocks.flowing_water) {
						++tAmount;
						// log("Found Water");
					}
				}
			}
		}
		
		boolean isValidWater = tAmount >= 60;
		
		if (isValidWater) {
			log("Filled structure.");
			return true;
		}
		else {			
			
			long aAvgVoltage = 0;
			for (GT_MetaTileEntity_Hatch_Energy g : this.mEnergyHatches) {
				if (g != null) {
					aAvgVoltage += (g.maxEUInput() * g.maxAmperesIn());
				}
			}			
			this.mEUt = (int) Math.max(30, aAvgVoltage);
			this.mMaxProgresstime = (int) Math.max(((aAvgVoltage/8)*20/10), 100);
			this.mProgresstime = 1;
			log("Did not fill structure. Consuming "+aAvgVoltage+"eu/t to try fill.");			
			return false;
		}
	}

	private static AutoMap<AutoMap<WeightedRandomFishable>> categories = new AutoMap<AutoMap<WeightedRandomFishable>>();
	private static AutoMap<WeightedRandomFishable> categoryFish = new AutoMap<WeightedRandomFishable>();
	private static AutoMap<WeightedRandomFishable> categoryJunk = new AutoMap<WeightedRandomFishable>();
	private static AutoMap<WeightedRandomFishable> categoryLoot = new AutoMap<WeightedRandomFishable>();
	private static boolean hasGenerateRecipes = false;
	private int mMode = 14;
	private int mMax = 8;

	private boolean generateRecipes() {
		if (!hasGenerateRecipes) {
			categories.put(categoryFish);
			categories.put(categoryJunk);
			categories.put(categoryLoot);
			for (WeightedRandomFishable h : FishPondFakeRecipe.fish) {
				categoryFish.put(h);
			}
			for (WeightedRandomFishable h : FishPondFakeRecipe.junk) {
				categoryJunk.put(h);
			}
			for (WeightedRandomFishable h : FishPondFakeRecipe.treasure) {
				categoryLoot.put(h);
			}
			hasGenerateRecipes = true;
			return true;
		} else {
			return true;
		}
	}

	private int getCircuit(ItemStack[] t) {
		if (!this.isUsingControllerCircuit) {
			for (ItemStack j : t) {
				if (j.getItem() == CI.getNumberedCircuit(0).getItem()) {
					// Fish
					if (j.getItemDamage() == 14) {
						mMax = 8 + (this.getMaxParallelRecipes() - 2);
						this.mMode = 14;
						break;
					}
					// Junk
					else if (j.getItemDamage() == 15) {
						this.mMode = 15;
						mMax = 4;
						break;
					}
					// Loot
					else if (j.getItemDamage() == 16) {
						this.mMode = 16;
						mMax = 4;
						break;
					} else {
						this.mMode = 0;
						mMax = 0;
						break;
					}
				} else {
					this.mMode = 0;
					mMax = 0;
					break;
				}
			}
		}
		return this.mMode;
	}

	// reflection map
	private static Map<WeightedRandomFishable, ItemStack> reflectiveFishMap = new HashMap<WeightedRandomFishable, ItemStack>();

	private ItemStack reflectiveFish(WeightedRandomFishable y) {
		if (reflectiveFishMap.containsKey(y)) {
			return reflectiveFishMap.get(y);
		}
		ItemStack t;
		try {
			t = (ItemStack) ReflectionUtils.getField(WeightedRandomFishable.class, "field_150711_b").get(y);
			ItemStack k = ItemUtils.getSimpleStack(t, 1);
			reflectiveFishMap.put(y, k);
			return t;
		} catch (IllegalArgumentException | IllegalAccessException e) {
		}
		return null;
	}

	private ItemStack[] generateLoot(int mode) {
		ItemStack[] mFishOutput = new ItemStack[this.mMax];
		if (this.mMode == 14) {
			for (int k = 0; k < this.mMax; k++) {
				if (mFishOutput[k] == null)
					for (WeightedRandomFishable g : categoryFish.values()) {
						if (MathUtils.randInt(0, (65 - getMaxParallelRecipes())) <= 2) {
							ItemStack t = reflectiveFish(g);
							if (t != null) {
								mFishOutput[k] = ItemUtils.getSimpleStack(t, 1);
							}
						}
					}
			}
		} else if (this.mMode == 15) {
			for (int k = 0; k < this.mMax; k++) {
				if (mFishOutput[k] == null)
					for (WeightedRandomFishable g : categoryJunk.values()) {
						if (MathUtils.randInt(0, 100) <= 1) {
							ItemStack t = reflectiveFish(g);
							if (t != null) {
								mFishOutput[k] = ItemUtils.getSimpleStack(t, 1);
							}
						}
					}
			}
		} else if (this.mMode == 16) {
			for (int k = 0; k < this.mMax; k++) {
				if (mFishOutput[k] == null)
					for (WeightedRandomFishable g : categoryLoot.values()) {
						if (MathUtils.randInt(0, 1000) <= 2) {
							ItemStack t = reflectiveFish(g);
							if (t != null) {
								mFishOutput[k] = ItemUtils.getSimpleStack(t, 1);
							}
						}
					}
			}
		} else {
			mFishOutput = null;
		}
		return mFishOutput;
	}

	@Override
	public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
			int aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll) {



		//Control Core to control the Multiblocks behaviour.
		int aControlCoreTier = getControlCoreTier();

		//If no core, return false;
		if (aControlCoreTier == 0 && CORE.ConfigSwitches.requireControlCores) {
			log("Invalid/No Control Core");
			return false;
		}


		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};
		

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		log("Running checkRecipeGeneric(0)");

		//Check to see if Voltage Tier > Control Core Tier
		if (tTier > aControlCoreTier && CORE.ConfigSwitches.requireControlCores) {
			return false;
		}		
		
		// Based on the Processing Array. A bit overkill, but very flexible.
		getCircuit(aItemInputs);

		/*
		 * GT_Recipe tRecipe = this.getRecipeMap().findRecipe( getBaseMetaTileEntity(),
		 * mLastRecipe, false, gregtech.api.enums.GT_Values.V[tTier], aFluidInputs,
		 * aItemInputs);
		 */

		ItemStack[] mFishOutput = generateLoot(this.mMode);
		mFishOutput = removeNulls(mFishOutput);
		GT_Recipe g = new GTPP_Recipe(true, new ItemStack[] {}, mFishOutput, null, new int[] {}, aFluidInputs, mOutputFluids, 200, 16, 0);
		aMaxParallelRecipes = this.canBufferOutputs(g, aMaxParallelRecipes);
		if (aMaxParallelRecipes == 0) {
			log("No Space");
			return false;
		}		
		
		log("Mode: " + this.mMode + " | Is loot valid? " + (mFishOutput != null));

		int jslot = 0;
		for (ItemStack x : mFishOutput) {
			if (x != null) {
				log(
						"Slot " + jslot + " in mFishOutput contains " + x.stackSize + "x " + x.getDisplayName() + ".");
			} else {
				log("Slot " + jslot + " in mFishOutput was null.");
			}
			jslot++;
		}
		
		// EU discount
		float tRecipeEUt = (8 * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		int parallelRecipes = 0;

		log("parallelRecipes: "+parallelRecipes);
		log("aMaxParallelRecipes: "+aMaxParallelRecipes);
		log("tTotalEUt: "+tTotalEUt);
		log("tVoltage: "+tVoltage);
		log("tRecipeEUt: "+tRecipeEUt);
		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tVoltage - tRecipeEUt); parallelRecipes++) {			
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
		this.mMaxProgresstime = (int)(20 * tTimeFactor * 4);

		this.mEUt = (int)Math.ceil(tTotalEUt);

		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;


		//Only Overclock as high as the control circuit.
		byte tTierOld = tTier;
		tTier = CORE.ConfigSwitches.requireControlCores ? (byte) aControlCoreTier : tTierOld;

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
		

		log("Recipe Step. [3]");
		// Collect output item types
		ItemStack[] tOutputItems = mFishOutput;

		int rslot = 0;
		tOutputItems = removeNulls(mFishOutput);

		for (ItemStack x : tOutputItems) {
			if (x != null) {
				log(
						"rSlot " + rslot + " in mFishOutput contains " + x.stackSize + "x " + x.getDisplayName() + ".");
			} else {
				log("rSlot " + rslot + " in mFishOutput was null.");
			}
			rslot++;
		}

		// Commit outputs
		this.mOutputItems = tOutputItems;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		return true;
	}

}