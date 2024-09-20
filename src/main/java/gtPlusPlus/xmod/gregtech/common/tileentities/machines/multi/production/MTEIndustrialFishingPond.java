package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
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

import org.jetbrains.annotations.NotNull;

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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.FishPondFakeRecipe;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.config.Configuration;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;

public class MTEIndustrialFishingPond extends GTPPMultiBlockBase<MTEIndustrialFishingPond>
    implements ISurvivalConstructable {

    private boolean isUsingControllerCircuit = false;
    private static final Item circuit = GTUtility.getIntegratedCircuit(0)
        .getItem();
    private int mCasing;
    private static IStructureDefinition<MTEIndustrialFishingPond> STRUCTURE_DEFINITION = null;
    private static final Class<?> cofhWater;

    static {
        cofhWater = ReflectionUtils.getClass("cofh.asmhooks.block.BlockWater");
    }

    public MTEIndustrialFishingPond(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialFishingPond(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialFishingPond(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Fish Trap";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Fishing Pond")
            .addInfo("Can process (Tier + 1) * 2 recipes")
            .addInfo("Put a numbered circuit into the input bus.")
            .addInfo("Circuit 14 for Fish")
            .addInfo("Circuit 15 for Junk")
            .addInfo("Circuit 16 for Treasure")
            .addInfo("Need to be filled with water.")
            .addInfo("Will automatically fill water from input hatch.")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(9, 3, 9, true)
            .addController("Front Center")
            .addCasingInfoMin("Aquatic Casings", 64, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher(GTPPCore.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public IStructureDefinition<MTEIndustrialFishingPond> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialFishingPond>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "XXXXXXXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X",
                                "X       X", "XXXXXXXXX" },
                            { "XXXX~XXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X",
                                "X       X", "XXXXXXXXX" },
                            { "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX",
                                "XXXXXXXXX", "XXXXXXXXX" }, }))
                .addElement(
                    'X',
                    buildHatchAdder(MTEIndustrialFishingPond.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch)
                        .casingIndex(getCasingTextureIndex())
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
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
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.fishPondRecipes;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        ItemStack controllerStack = getControllerSlot();
        if (controllerStack != null) {
            if (controllerStack.getItem() == circuit) {
                this.isUsingControllerCircuit = true;
                this.mMode = controllerStack.getItemDamage();
            } else {
                this.isUsingControllerCircuit = false;
            }
        } else {
            this.isUsingControllerCircuit = false;
        }
        if (!hasGenerateRecipes) {
            generateRecipes();
        }
        if (!checkForWater()) {
            return SimpleCheckRecipeResult.ofFailure("no_water");
        }
        ItemStack[] tItemInputs = getStoredInputs().toArray(new ItemStack[0]);
        FluidStack[] tFluidInputs = getStoredFluids().toArray(new FluidStack[0]);

        if (!isUsingControllerCircuit && tItemInputs.length == 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        long tEnergy = getMaxInputEnergy();

        getCircuit(tItemInputs);

        ItemStack[] mFishOutput = generateLoot(this.mMode);
        mFishOutput = removeNulls(mFishOutput);
        GTRecipe g = new GTRecipe(
            true,
            new ItemStack[] {},
            mFishOutput,
            null,
            new int[] {},
            tFluidInputs,
            null,
            200,
            16,
            0);
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(g.mEUt)
            .setEUt(tEnergy)
            .setDuration(g.mDuration);
        ParallelHelper helper = new ParallelHelper().setRecipe(g)
            .setItemInputs(tItemInputs)
            .setFluidInputs(tFluidInputs)
            .setAvailableEUt(tEnergy)
            .setMaxParallel(getMaxParallelRecipes())
            .setConsumption(true)
            .setOutputCalculation(true)
            .setMachine(this)
            .enableBatchMode(batchMode ? 128 : 1)
            .setCalculator(calculator);

        helper.build();

        if (helper.getCurrentParallel() == 0) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        lEUt = -calculator.getConsumption();
        mMaxProgresstime = (int) Math.ceil(calculator.getDuration() * helper.getDurationMultiplierDouble());

        mOutputItems = helper.getItemOutputs();
        mOutputFluids = null;
        updateSlots();

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (2 * (GTUtility.getTier(this.getMaxInputVoltage()) + 1));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return Configuration.pollution.pollutionPerSecondMultiIndustrialFishingPond;
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
                                        aBaseMetaTileEntity.getWorld()
                                            .setBlock(
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
                    }
                }
            }
        }

        return tAmount >= 60;
    }

    private boolean isNotStaticWater(Block block, byte meta) {
        return block == Blocks.air || block == Blocks.flowing_water
            || block == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)
            || (cofhWater != null && cofhWater.isAssignableFrom(block.getClass()) && meta != 0);
    }

    private static ArrayList<ArrayList<WeightedRandomFishable>> categories = new ArrayList<>();
    private static ArrayList<WeightedRandomFishable> categoryFish = new ArrayList<>();
    private static ArrayList<WeightedRandomFishable> categoryJunk = new ArrayList<>();
    private static ArrayList<WeightedRandomFishable> categoryLoot = new ArrayList<>();
    private static boolean hasGenerateRecipes = false;
    private int mMode = 14;
    private int mMax = 8;

    private void generateRecipes() {
        if (hasGenerateRecipes) return;

        categories.add(categoryFish);
        categories.add(categoryJunk);
        categories.add(categoryLoot);
        categoryFish.addAll(FishPondFakeRecipe.fish);
        categoryJunk.addAll(FishPondFakeRecipe.junk);
        categoryLoot.addAll(FishPondFakeRecipe.treasure);
        hasGenerateRecipes = true;
    }

    private int getCircuit(ItemStack[] t) {
        if (!this.isUsingControllerCircuit) {
            for (ItemStack j : t) {
                if (j.getItem() == GTUtility.getIntegratedCircuit(0)
                    .getItem()) {
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
    private static Map<WeightedRandomFishable, ItemStack> reflectiveFishMap = new HashMap<>();

    private ItemStack reflectiveFish(WeightedRandomFishable y) {
        if (reflectiveFishMap.containsKey(y)) {
            return reflectiveFishMap.get(y);
        }
        ItemStack t;
        try {
            t = (ItemStack) ReflectionUtils.getField(WeightedRandomFishable.class, "field_150711_b")
                .get(y);
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
                if (mFishOutput[k] == null) for (WeightedRandomFishable g : categoryFish) {
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
                if (mFishOutput[k] == null) for (WeightedRandomFishable g : categoryJunk) {
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
                if (mFishOutput[k] == null) for (WeightedRandomFishable g : categoryLoot) {
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
}
