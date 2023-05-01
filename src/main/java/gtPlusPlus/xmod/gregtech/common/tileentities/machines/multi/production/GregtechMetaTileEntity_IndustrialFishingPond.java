package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.FishPondFakeRecipe;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
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

public class GregtechMetaTileEntity_IndustrialFishingPond extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialFishingPond> implements ISurvivalConstructable {

    private boolean isUsingControllerCircuit = false;
    private static final Item circuit = CI.getNumberedCircuit(0).getItem();
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialFishingPond> STRUCTURE_DEFINITION = null;
    private static final Class<?> cofhWater;

    static {
        cofhWater = ReflectionUtils.getClass("cofh.asmhooks.block.BlockWater");
    }

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
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Fishing Pond")
                .addInfo("Can process (Tier + 1) * 2 recipes").addInfo("Put a numbered circuit into the input bus.")
                .addInfo("Circuit 14 for Fish").addInfo("Circuit 15 for Junk").addInfo("Circuit 16 for Treasure")
                .addInfo("Need to be filled with water.").addInfo("Will automatically fill water from input hatch.")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(9, 3, 9, true)
                .addController("Front Center").addCasingInfo("Aquatic Casings", 64).addInputBus("Any Casing", 1)
                .addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1).addEnergyHatch("Any Casing", 1)
                .addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialFishingPond> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialFishingPond>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] {
                                            { "XXXXXXXXX", "X       X", "X       X", "X       X", "X       X",
                                                    "X       X", "X       X", "X       X", "XXXXXXXXX" },
                                            { "XXXX~XXXX", "X       X", "X       X", "X       X", "X       X",
                                                    "X       X", "X       X", "X       X", "XXXXXXXXX" },
                                            { "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX",
                                                    "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX" }, }))
                    .addElement(
                            'X',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialFishingPond.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch)
                                    .casingIndex(getCasingTextureIndex()).dot(1).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.mCasing,
                                                    ofBlock(getCasingBlock(), getCasingMeta()))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 4, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 4, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 4, 1, 0) && mCasing >= 64 && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER;
    }

    @Override
    protected int getCasingTextureId() {
        return getCasingTextureIndex();
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
        return (2 * (GT_Utility.getTier(this.getMaxInputVoltage()) + 1));
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
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialFishingPond;
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
        int mDirectionX = aBaseMetaTileEntity.getBackFacing().offsetX;
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

        final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * mCurrentDirectionX;
        final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * mCurrentDirectionZ;

        int tAmount = 0;
        for (int i = mOffsetX_Lower + 1; i <= mOffsetX_Upper - 1; ++i) {
            for (int j = mOffsetZ_Lower + 1; j <= mOffsetZ_Upper - 1; ++j) {
                for (int h = 0; h < 2; h++) {
                    Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                    byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                    if (isNotStaticWater(tBlock, tMeta)) {
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
                                                aBaseMetaTileEntity.getZCoord() + zDir + j,
                                                fluidUsed);
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
        } else {
            log("Did not fill structure.");
        }
        return isValidWater;
    }

    private boolean isNotStaticWater(Block block, byte meta) {
        return block == Blocks.air || block == Blocks.flowing_water
                || block == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)
                || (cofhWater != null && cofhWater.isAssignableFrom(block.getClass()) && meta != 0);
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
        } catch (IllegalArgumentException | IllegalAccessException e) {}
        return null;
    }

    private ItemStack[] generateLoot(int mode) {
        ItemStack[] mFishOutput = new ItemStack[this.mMax];
        if (this.mMode == 14) {
            for (int k = 0; k < this.mMax; k++) {
                if (mFishOutput[k] == null) for (WeightedRandomFishable g : categoryFish.values()) {
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
                if (mFishOutput[k] == null) for (WeightedRandomFishable g : categoryJunk.values()) {
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
                if (mFishOutput[k] == null) for (WeightedRandomFishable g : categoryLoot.values()) {
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
            long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll) {

        // Control Core to control the Multiblocks behaviour.
        int aControlCoreTier = getControlCoreTier();

        // If no core, return false;
        if (aControlCoreTier == 0 && CORE.ConfigSwitches.requireControlCores) {
            return false;
        }

        // Reset outputs and progress stats
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        long tEnergy = getMaxInputEnergy();

        // Check to see if Voltage Tier > Control Core Tier
        if (tTier > aControlCoreTier && CORE.ConfigSwitches.requireControlCores) {
            return false;
        }

        // Based on the Processing Array. A bit overkill, but very flexible.
        getCircuit(aItemInputs);

        /*
         * GT_Recipe tRecipe = this.getRecipeMap().findRecipe( getBaseMetaTileEntity(), mLastRecipe, false,
         * gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);
         */

        ItemStack[] mFishOutput = generateLoot(this.mMode);
        mFishOutput = removeNulls(mFishOutput);
        GT_Recipe g = new GTPP_Recipe(
                true,
                new ItemStack[] {},
                mFishOutput,
                null,
                new int[] {},
                aFluidInputs,
                mOutputFluids,
                200,
                16,
                0);
        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(g).setItemInputs(aItemInputs)
                .setFluidInputs(aFluidInputs).setAvailableEUt(tEnergy).setMaxParallel(aMaxParallelRecipes)
                .enableConsumption().enableOutputCalculation().setEUtModifier(aEUPercent / 100.0f);
        if (!voidExcess) {
            helper.enableVoidProtection(this);
        }

        if (batchMode) {
            helper.enableBatchMode(128);
        }

        helper.build();

        if (helper.getCurrentParallel() == 0) {
            return false;
        }

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(g.mEUt).setEUt(tEnergy)
                .setDuration(g.mDuration).setEUtDiscount(aEUPercent / 100.0f)
                .setSpeedBoost(100.0f / (100.0f + aSpeedBonusPercent))
                .setParallel((int) Math.floor(helper.getCurrentParallel() / helper.getDurationMultiplier()))
                .calculate();
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = (int) Math.ceil(calculator.getDuration() * helper.getDurationMultiplier());

        mOutputItems = helper.getItemOutputs();
        mOutputFluids = helper.getFluidOutputs();
        updateSlots();

        // Play sounds (GT++ addition - GT multiblocks play no sounds)
        startProcess();

        return true;
    }
}
