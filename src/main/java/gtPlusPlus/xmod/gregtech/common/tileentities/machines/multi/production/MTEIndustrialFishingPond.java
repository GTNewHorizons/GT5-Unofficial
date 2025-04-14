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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cofh.asmhooks.block.BlockTickingWater;
import cofh.asmhooks.block.BlockWater;
import gregtech.api.enums.Mods;
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
import gregtech.api.util.ReflectionUtil;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class MTEIndustrialFishingPond extends GTPPMultiBlockBase<MTEIndustrialFishingPond>
    implements ISurvivalConstructable {

    public static final int FISH_MODE = 14;
    public static final int JUNK_MODE = 15;
    public static final int TREASURE_MODE = 16;
    private static final Item CONTROL_CIRCUIT = GTUtility.getIntegratedCircuit(0)
        .getItem();
    private static IStructureDefinition<MTEIndustrialFishingPond> STRUCTURE_DEFINITION;

    private boolean isUsingControllerCircuit = false;
    private int mCasing;
    private int mMode = FISH_MODE;
    private int mMax = 8;

    private static final Class<?> cofhWater;

    static {
        cofhWater = ReflectionUtil.getClass("cofh.asmhooks.block.BlockWater");
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
            .addInfo("Can process (Tier + 1) * 2 recipes")
            .addInfo("Put a numbered circuit into the input bus.")
            .addInfo("Circuit " + FISH_MODE + " for Fish")
            .addInfo("Circuit " + JUNK_MODE + " for Junk")
            .addInfo("Circuit " + TREASURE_MODE + " for Treasure")
            .addInfo("Need to be filled with water.")
            .addInfo("Will automatically fill water from input hatch.")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 3, 9, true)
            .addController("Front Center")
            .addCasingInfoMin("Aquatic Casings", 64, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher();
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
    protected IIconContainer getActiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW;
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
            if (controllerStack.getItem() == CONTROL_CIRCUIT) {
                this.isUsingControllerCircuit = true;
                this.mMode = controllerStack.getItemDamage();
            } else {
                this.isUsingControllerCircuit = false;
            }
        } else {
            this.isUsingControllerCircuit = false;
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

        setModeFromInputStacks(tItemInputs);

        ItemStack[] mFishOutput = generateLoot();
        if (mFishOutput == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        List<ItemStack> list = new ObjectArrayList<>(mFishOutput);
        list.removeAll(Collections.singleton((ItemStack) null));
        mFishOutput = list.toArray(new ItemStack[0]);
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
            .setMaxParallel(getTrueParallel())
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
        return PollutionConfig.pollutionPerSecondMultiIndustrialFishingPond;
    }

    private Block getCasingBlock() {
        return ModBlocks.blockCasings3Misc;
    }

    private byte getCasingMeta() {
        return 0;
    }

    private int getCasingTextureIndex() {
        return TAE.GTPP_INDEX(32);
    }

    private boolean checkForWater() {

        // Get Facing direction
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        final int mCurrentDirectionX = 4;
        final int mCurrentDirectionZ = 4;
        final int mOffsetX_Lower = -4;
        final int mOffsetX_Upper = 4;
        final int mOffsetZ_Lower = -4;
        final int mOffsetZ_Upper = 4;
        final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * mCurrentDirectionX;
        final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * mCurrentDirectionZ;

        int tAmount = 0;
        for (int i = mOffsetX_Lower + 1; i <= mOffsetX_Upper - 1; ++i) {
            for (int j = mOffsetZ_Lower + 1; j <= mOffsetZ_Upper - 1; ++j) {
                for (int h = 0; h < 2; h++) {
                    Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                    int tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                    if (isNotStaticWater(tBlock, tMeta)) {
                        if (this.getStoredFluids() != null) {
                            for (FluidStack stored : this.getStoredFluids()) {
                                if (stored.isFluidEqual(FluidUtils.getFluidStack("water", 1))) {
                                    if (stored.amount >= 1000) {
                                        // Utils.LOG_WARNING("Going to try swap an air block for water from inut bus.");
                                        stored.amount -= 1000;
                                        aBaseMetaTileEntity.getWorld()
                                            .setBlock(
                                                aBaseMetaTileEntity.getXCoord() + xDir + i,
                                                aBaseMetaTileEntity.getYCoord() + h,
                                                aBaseMetaTileEntity.getZCoord() + zDir + j,
                                                Blocks.water);
                                    }
                                }
                            }
                        }
                    }
                    tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                    if (tBlock == Blocks.water || tBlock == Blocks.flowing_water) {
                        ++tAmount;
                    } else if (Mods.COFHCore.isModLoaded()) {
                        if (tBlock instanceof BlockWater || tBlock instanceof BlockTickingWater) {
                            ++tAmount;
                        }
                    }
                }
            }
        }

        return tAmount >= 60;
    }

    private boolean isNotStaticWater(Block block, int meta) {
        return block == Blocks.air || block == Blocks.flowing_water
            || block == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)
            || (cofhWater != null && cofhWater.isAssignableFrom(block.getClass()) && meta != 0);
    }

    private void setModeFromInputStacks(ItemStack[] stacks) {
        if (this.isUsingControllerCircuit) return;
        for (ItemStack stack : stacks) {
            if (stack.getItem() == CONTROL_CIRCUIT) {
                if (stack.getItemDamage() == FISH_MODE) {
                    mMode = FISH_MODE;
                    mMax = 8 + (this.getMaxParallelRecipes() - 2);
                    return;
                } else if (stack.getItemDamage() == JUNK_MODE) {
                    mMode = JUNK_MODE;
                    mMax = 4;
                    return;
                } else if (stack.getItemDamage() == TREASURE_MODE) {
                    mMode = TREASURE_MODE;
                    mMax = 4;
                    return;
                } else {
                    mMode = 0;
                    mMax = 0;
                    return;
                }
            } else {
                mMode = 0;
                mMax = 0;
                break;
            }
        }
    }

    private ItemStack[] generateLoot() {
        if (this.mMode == FISH_MODE) {
            return getLootFromList(FishPondFakeRecipe.fish, (65 - getMaxParallelRecipes()));
        } else if (this.mMode == JUNK_MODE) {
            return getLootFromList(FishPondFakeRecipe.junk, 200);
        } else if (this.mMode == TREASURE_MODE) {
            return getLootFromList(FishPondFakeRecipe.treasure, 100);
        } else {
            return null;
        }
    }

    private ItemStack[] getLootFromList(ArrayList<ItemStack> list, int max) {
        ItemStack[] out = new ItemStack[this.mMax];
        for (int i = 0; i < this.mMax; i++) {
            for (ItemStack stack : list) {
                if (MathUtils.randInt(0, max) <= 2) {
                    out[i] = ItemUtils.getSimpleStack(stack, 1);
                    break;
                }
            }
        }
        return out;
    }
}
