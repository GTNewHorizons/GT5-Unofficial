package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.FishPondFakeRecipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.ModBlocks;
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
	public String[] getTooltip() {
		return new String[] { "Controller Block for the Fishing Pond", "Size: 9x3x9 [WxHxL] (open)", "X           X",
				"X           X", "XXXXXXXXX", "Put a numbered circuit into the input bus.", "Circuit 14 for Fish",
				"Circuit 15 for Junk", "Circuit 16 for Treasure", "Controller (front centered)",
				"1x Output Bus (Any casing)", "1x Input Bus (Any casing)",
				"1x Input Hatch (Any casing, fill with water)", "1x Maintenance Hatch (Any casing)",
				"1x Energy Hatch (Any casing)", "Aquatic Casings for the rest",  };
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE
							: Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER) };
		}
		return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()] };
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
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		if (aStack != null) {
			Logger.WARNING("Found " + aStack.getDisplayName());
			if (aStack.getItem() == circuit) {
				this.isUsingControllerCircuit = true;
				this.mMode = aStack.getItemDamage();
				Logger.WARNING("Found Circuit!");
			} else {
				this.isUsingControllerCircuit = false;
			}
		} else {
			this.isUsingControllerCircuit = false;
		}
		if (!hasGenerateRecipes) {
			Logger.WARNING("Generating Recipes.");
			generateRecipes();
		}
		if (hasGenerateRecipes && checkForWater()) {
			Logger.WARNING("Trying to run recipe.");
			ArrayList<ItemStack> tItems = getStoredInputs();
			ArrayList<FluidStack> tFluids = getStoredFluids();
			ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
			FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);

			if (!isUsingControllerCircuit && tItems.size() == 0) {
				return false;
			}

			return checkRecipeGeneric(tItemInputs, tFluidInputs, 1, 100, 80, 100);
		}
		return true;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		// Get Facing direction
		int mDirectionX = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int mCurrentDirectionX;
		int mCurrentDirectionZ;
		int mOffsetX_Lower = 0;
		int mOffsetX_Upper = 0;
		int mOffsetZ_Lower = 0;
		int mOffsetZ_Upper = 0;

		if (mDirectionX == 0) {
			mCurrentDirectionX = 2;
			mCurrentDirectionZ = 3;
		} else {
			mCurrentDirectionX = 3;
			mCurrentDirectionZ = 2;
		}

		mOffsetX_Lower = -4;
		mOffsetX_Upper = 4;
		mOffsetZ_Lower = -4;
		mOffsetZ_Upper = 4;

		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX
				* mCurrentDirectionX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ
				* mCurrentDirectionZ;

		Logger.WARNING("xDir" + (xDir));
		Logger.WARNING("zDir" + (zDir));
		/*
		 * if (!(aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir))) { return false; }
		 */
		int tAmount = 0;
		for (int i = mOffsetX_Lower; i <= mOffsetX_Upper; ++i) {
			for (int j = mOffsetZ_Lower; j <= mOffsetZ_Upper; ++j) {
				for (int h = -1; h < 2; ++h) {
					if ((h != 0) || ((((xDir + i != 0) || (zDir + j != 0))) && (((i != 0) || (j != 0))))) {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h,
								zDir + j);
						if (!addToMachineList(tTileEntity)) {
							Logger.WARNING("X: " + i + " | Z: " + j);
							Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);

							if ((tBlock == getCasingBlock()) && (tMeta == getCasingMeta())) {
								++tAmount;
							} else {
								if ((i != mOffsetX_Lower && j != mOffsetZ_Lower && i != mOffsetX_Upper
										&& j != mOffsetZ_Upper) && (h == 0 || h == 1)) {
									if (tBlock == Blocks.air) {
										Logger.WARNING("Found Air");
									} else if (tBlock == Blocks.water) {
										Logger.WARNING("Found Water");
									}
								} else {
									if (tBlock.getLocalizedName().contains("gt.blockmachines") || tBlock == Blocks.water
											|| tBlock == Blocks.flowing_water
											|| tBlock == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)) {

									} else {
										Logger.WARNING("[x] Did not form - Found: " + tBlock.getLocalizedName() + " | "
												+ tBlock.getDamageValue(aBaseMetaTileEntity.getWorld(),
														aBaseMetaTileEntity.getXCoord() + i,
														aBaseMetaTileEntity.getYCoord(),
														aBaseMetaTileEntity.getZCoord() + j)
												+ " | Special Meta: "
												+ (tTileEntity == null ? "0" : tTileEntity.getMetaTileID()));
										Logger.WARNING("[x] Did not form - Found: "
												+ (aBaseMetaTileEntity.getXCoord() + xDir + i) + " | "
												+ aBaseMetaTileEntity.getYCoord() + " | "
												+ (aBaseMetaTileEntity.getZCoord() + zDir + j));
										return false;
									}
								}

							}
						}
					}
				}
			}
		}
		if ((tAmount >= 64)) {
			Logger.WARNING("Made structure.");
		} else {
			Logger.WARNING("Did not make structure.");
		}
		return (tAmount >= 64);
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

	private boolean addToMachineList(final IGregTechTileEntity tTileEntity) {
		return ((this.addMaintenanceToMachineList(tTileEntity, this.getCasingTextureIndex()))
				|| (this.addInputToMachineList(tTileEntity, this.getCasingTextureIndex()))
				|| (this.addOutputToMachineList(tTileEntity, this.getCasingTextureIndex()))
				|| (this.addMufflerToMachineList(tTileEntity, this.getCasingTextureIndex()))
				|| (this.addEnergyInputToMachineList(tTileEntity, this.getCasingTextureIndex())));
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

		if (mDirectionX == 0) {
			mCurrentDirectionX = 2;
			mCurrentDirectionZ = 3;
		} else {
			mCurrentDirectionX = 3;
			mCurrentDirectionZ = 2;
		}
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
				for (int h = 0; h < 2; ++h) {
					Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
					// byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
					if (tBlock == Blocks.air || tBlock == Blocks.flowing_water || tBlock == Blocks.water) {
						if (this.getStoredFluids() != null) {
							for (FluidStack stored : this.getStoredFluids()) {
								if (stored.isFluidEqual(FluidUtils.getFluidStack("water", 1))) {
									if (stored.amount >= 1000) {
										// Utils.LOG_WARNING("Going to try swap an air block for water from inut bus.");
										stored.amount -= 1000;
										Block fluidUsed = null;
										if (tBlock == Blocks.air || tBlock == Blocks.flowing_water) {
											fluidUsed = Blocks.water;
										}
										if (tBlock == Blocks.water) {
											fluidUsed = BlocksItems.getFluidBlock(InternalName.fluidDistilledWater);
										}
										aBaseMetaTileEntity.getWorld().setBlock(
												aBaseMetaTileEntity.getXCoord() + xDir + i,
												aBaseMetaTileEntity.getYCoord() + h,
												aBaseMetaTileEntity.getZCoord() + zDir + j, fluidUsed);

									}
								}
							}
						}
					}
					if (tBlock == Blocks.water) {
						++tAmount;
						// Logger.WARNING("Found Water");
					} else if (tBlock == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)) {
						++tAmount;
						++tAmount;
						// Logger.WARNING("Found Distilled Water");
					}
				}
			}
		}
		if ((tAmount >= 80)) {
			Logger.WARNING("Filled structure.");
		} else {
			Logger.WARNING("Did not fill structure.");
		}
		return (tAmount >= 80);
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
						mMax = 8;
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
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
		}
		return null;
	}

	private ItemStack[] generateLoot(int mode) {
		ItemStack[] mFishOutput = new ItemStack[this.mMax];
		if (this.mMode == 14) {
			for (int k = 0; k < this.mMax; k++) {
				if (mFishOutput[k] == null)
					for (WeightedRandomFishable g : categoryFish.values()) {
						if (MathUtils.randInt(0, 75) <= 2) {
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

		// Based on the Processing Array. A bit overkill, but very flexible.
		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

		int parallelRecipes = 1;
		getCircuit(aItemInputs);

		/*
		 * GT_Recipe tRecipe = this.getRecipeMap().findRecipe( getBaseMetaTileEntity(),
		 * mLastRecipe, false, gregtech.api.enums.GT_Values.V[tTier], aFluidInputs,
		 * aItemInputs);
		 */

		ItemStack[] mFishOutput = generateLoot(this.mMode);
		Logger.WARNING("Mode: " + this.mMode + " | Is loot valid? " + (mFishOutput != null));

		int jslot = 0;
		for (ItemStack x : mFishOutput) {
			if (x != null) {
				Logger.WARNING(
						"Slot " + jslot + " in mFishOutput contains " + x.stackSize + "x " + x.getDisplayName() + ".");
			} else {
				Logger.WARNING("Slot " + jslot + " in mFishOutput was null.");
			}
			jslot++;
		}

		// EU discount
		// float tRecipeEUt = (32 * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[] {};
		this.mOutputFluids = new FluidStack[] {};

		tTotalEUt = 8;
		Logger.WARNING("Recipe Step. [1]");

		if (parallelRecipes == 0) {
			Logger.WARNING("Recipe Step. [-1]");
			return false;
		}

		// Convert speed bonus to duration multiplier
		// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
		float tTimeFactor = 100.0f / (100.0f + 0);

		float modeMulti = 1;
		modeMulti = (this.mMode == 14 ? 5 : (this.mMode == 15 ? 1 : 20));
		this.mMaxProgresstime = (int) ((60 * modeMulti) * tTimeFactor);

		this.mEUt = (int) Math.ceil(tTotalEUt);

		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;

		Logger.WARNING("Recipe Step. [2]");
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

		Logger.WARNING("Recipe Step. [3]");
		// Collect output item types
		ItemStack[] tOutputItems = mFishOutput;

		int rslot = 0;
		tOutputItems = removeNulls(mFishOutput);

		for (ItemStack x : tOutputItems) {
			if (x != null) {
				Logger.WARNING(
						"rSlot " + rslot + " in mFishOutput contains " + x.stackSize + "x " + x.getDisplayName() + ".");
			} else {
				Logger.WARNING("rSlot " + rslot + " in mFishOutput was null.");
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