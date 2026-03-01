package gtnhintergalactic.tile.multi.elevatormodules;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ggfab.mte.MTELinkedInputBus;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.XSTR;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.enums.SolarSystem;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gtPlusPlus.core.material.MaterialsElements;
import gtnhintergalactic.gui.IG_UITextures;
import gtnhintergalactic.item.ItemMiningDrones;
import gtnhintergalactic.recipe.IGRecipeMaps;
import gtnhintergalactic.recipe.SpaceMiningData;
import gtnhintergalactic.recipe.SpaceMiningRecipes;
import gtnhintergalactic.recipe.SpaceMiningRecipes.WeightedAsteroidList;
import gtnhintergalactic.spaceprojects.ProjectAsteroidOutpost;
import gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import tectech.TecTech;
import tectech.thing.gui.TecTechUITextures;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

/**
 * Base class for space mining modules
 *
 * @author minecraft7771
 */
public abstract class TileEntityModuleMiner extends TileEntityModuleBase implements IOverclockDescriptionProvider {

    /** Max chance to get a bonus stack from space mining */
    protected static int BONUS_STACK_MAX_CHANCE = 7500;

    /** Minimal factor used to calculate the recipe time */
    protected static final double MIN_RECIPE_TIME_MODIFIER = 0.5D;

    /** Max distance a mining drone can travel */
    protected static final double MAX_DISTANCE = 300D;

    // Tiered plasmas, the mining operation uses one of them. Using higher tier plasmas boosts the mining operation
    /** Usage of helium plasma per mining operation */
    protected static int PLASMA_HELIUM_USAGE = 825;
    /** Usage of bismuth plasma per mining operation */
    protected static int PLASMA_BISMUTH_USAGE = 550;
    /** Usage of radon plasma per mining operation */
    protected static int PLASMA_RADON_USAGE = 375;
    /** Usage of technetium plasma per mining operation */
    protected static int PLASMA_TECHNETIUM_USAGE = 250;
    /** Usage of plutonium 241 plasma per mining operation */
    protected static int PLASMA_PLUTONIUM241_USAGE = 150;

    @Override
    protected long getAvailableData_EM() {
        if (eInputData.isEmpty()) return this.parent.getAvailableDataForModules();
        return super.getAvailableData_EM();
    }

    /* Size of the whitelist in stacks **/
    protected static int WHITELIST_SIZE = 64;
    /** ID of the whitelist config window */
    protected static int WHITELIST_WINDOW_ID = 200;

    /** String of the NBT tag that saves if whitelist mode is enabled */
    protected static String IS_WHITELISTED_NBT_TAG = "isWhitelisted";
    /** String of the NBT tag that saves the whitelist */
    protected static String WHITELIST_NBT_TAG = "whitelist";
    /** Flag if the user modified the filter */
    protected boolean wasFilterModified;

    protected static final ISpaceProject ASTEROID_OUTPOST = SpaceProjectManager.getProject("AsteroidOutput");

    // region Parameters

    /** Input parameters */
    Parameters.Group.ParameterIn distanceSetting, parallelSetting, overdriveSetting, modeSetting, rangeSetting,
        stepSetting;

    Parameters.Group.ParameterOut distanceDisplay;

    /** Name of the distance setting */
    private static final INameFunction<TileEntityModuleMiner> DISTANCE_SETTING_NAME = (base, p) -> GCCoreUtil
        .translate("gt.blockmachines.multimachine.project.ig.miner.cfgi.0"); // Distance
    /** Status of the distance setting */
    private static final IStatusFunction<TileEntityModuleMiner> DISTANCE_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 200, MAX_DISTANCE);
    /** Name of the parallel setting */
    private static final INameFunction<TileEntityModuleMiner> PARALLEL_SETTING_NAME = (base, p) -> GCCoreUtil
        .translate("gt.blockmachines.multimachine.project.ig.miner.cfgi.1"); // Max parallels
    /** Status of the parallel setting */
    private static final IStatusFunction<TileEntityModuleMiner> PARALLEL_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 100, base.getMaxParallels());
    /** Name of the overdrive setting */
    private static final INameFunction<TileEntityModuleMiner> OVERDRIVE_SETTING_NAME = (base, p) -> GCCoreUtil
        .translate("gt.blockmachines.multimachine.project.ig.miner.cfgi.2"); // Overdrive
    /** Status of the overdrive setting */
    private static final IStatusFunction<TileEntityModuleMiner> OVERDRIVE_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 1.5, 2);
    /** Name of the mode setting */
    private static final INameFunction<TileEntityModuleMiner> MODE_SETTING_NAME = (base, p) -> GCCoreUtil
        .translate("gt.blockmachines.multimachine.project.ig.miner.cfgi.4"); // Mode
    /** Status of the mode setting */
    private static final IStatusFunction<TileEntityModuleMiner> MODE_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 0, 1.1, 1.1);
    /** Name of the mode setting */
    private static final INameFunction<TileEntityModuleMiner> RANGE_SETTING_NAME = (base, p) -> GCCoreUtil
        .translate("gt.blockmachines.multimachine.project.ig.miner.cfgi.5"); // Range
    /** Status of the mode setting */
    private static final IStatusFunction<TileEntityModuleMiner> RANGE_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 0, 50, 150);
    /** Name of the step setting */
    private static final INameFunction<TileEntityModuleMiner> STEP_SETTING_NAME = (base, p) -> GCCoreUtil
        .translate("gt.blockmachines.multimachine.project.ig.miner.cfgi.6"); // Step
    /** Status of the step setting */
    private static final IStatusFunction<TileEntityModuleMiner> STEP_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 0, 10, 20);

    // endregion

    /** Power object used for displaying in NEI */
    protected final OverclockDescriber overclockDescriber;

    /** Asteroid outpost that the player can additionally build */
    protected ProjectAsteroidOutpost asteroidOutpost;

    /** Flag if the module has a whitelist to generate ores, else it will use a blacklist */
    protected boolean isWhitelisted = false;
    /** List for ore generation. Can either be a white- or blacklist */
    protected HashSet<String> configuredOres;
    /** Handler that holds the visual whitelist */
    protected ItemStackHandler whiteListHandler = new ItemStackHandler(WHITELIST_SIZE);
    /** The distance when prevRecipes was computed */
    protected int prevDistance = 0;
    /** Bitmask of tiers for which a drone, drills, and rods were present when prevRecipes was computed */
    protected int prevAvailDroneMask = 0;
    /**
     * The last computed list of possible recipes. Can be reused if distance etc don't change, and used to display stats
     * to the user
     */
    protected WeightedAsteroidList prevRecipes = null;

    /**
     * Create new Space Mining module
     *
     * @param aID           ID of the module
     * @param aName         Name of the module
     * @param aNameRegional Localized name of the module
     * @param tTier         Voltage tier of the module
     * @param tModuleTier   Tier of the module
     * @param tMinMotorTier Minimum needed motor tier
     */
    public TileEntityModuleMiner(int aID, String aName, String aNameRegional, int tTier, int tModuleTier,
        int tMinMotorTier) {
        super(aID, aName, aNameRegional, tTier, tModuleTier, tMinMotorTier);
        overclockDescriber = new ModuleOverclockDescriber((byte) tTier, tModuleTier);
    }

    /**
     * Create new Space Mining module
     *
     * @param aName         Name of the module
     * @param tTier         Voltage tier of the module
     * @param tModuleTier   Tier of the module
     * @param tMinMotorTier Minimum needed motor tier
     */
    public TileEntityModuleMiner(String aName, int tTier, int tModuleTier, int tMinMotorTier) {
        super(aName, tTier, tModuleTier, tMinMotorTier);
        overclockDescriber = new ModuleOverclockDescriber((byte) tTier, tModuleTier);
    }

    @Override
    public OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    /**
     * Load additional NBT data
     *
     * @param aNBT NBT data from which will be loaded
     */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        isWhitelisted = aNBT.getBoolean(IS_WHITELISTED_NBT_TAG);
        if (whiteListHandler != null) {
            whiteListHandler.deserializeNBT(aNBT.getCompoundTag(WHITELIST_NBT_TAG));
        }
        generateOreConfigurationList();
    }

    /**
     * Save additional NBT data
     *
     * @param aNBT NBT data to which will be written
     */
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean(IS_WHITELISTED_NBT_TAG, isWhitelisted);
        if (whiteListHandler != null) {
            aNBT.setTag(WHITELIST_NBT_TAG, whiteListHandler.serializeNBT());
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return IGRecipeMaps.spaceMiningRecipes;
    }

    /**
     * Check if any recipe can be started with the given inputs
     *
     * @return True if a recipe could be started, else false
     */
    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        if (!overdriveSetting.getStatus(false).isOk) return SimpleCheckRecipeResult.ofFailure("invalid_overdrive");
        if (V[tTier] * (long) parallelSetting.get() > getEUVar()) {
            return CheckRecipeResultRegistry.insufficientPower(V[tTier] * (long) parallelSetting.get());
        }

        lEUt = 0;
        eAmpereFlow = 0;
        eRequiredData = 0;
        mEfficiencyIncrease = 0;
        mPollution = 0;
        mOutputItems = null;
        mOutputFluids = null;
        List<FluidStack> inputFluids = new ArrayList<>();
        inputFluids.addAll(parent.getStoredFluids());
        inputFluids.addAll(this.getStoredFluids());
        if (inputFluids.isEmpty()) {
            return SimpleCheckRecipeResult.ofFailure("no_plasma");
        }

        // Check for valid item inputs
        ItemStack[] itemInputs = validInputs();

        // Look for a valid plasma to start a mining operation
        for (FluidStack fluidStack : inputFluids) {
            int availablePlasmaTier = getTierFromPlasma(fluidStack);
            if (availablePlasmaTier > 0) {
                // Check if valid inputs for a mining operation are present
                CheckRecipeResult result = process(
                    itemInputs,
                    inputFluids.toArray(new FluidStack[0]),
                    availablePlasmaTier,
                    fluidStack,
                    getParallels(fluidStack, getPlasmaUsageFromTier(availablePlasmaTier)));
                if (result.wasSuccessful()) {
                    cycleDistance();
                    return result;
                }
            }
        }
        cycleDistance();
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    /** Determine which drones and items are in the correct buses */
    protected ItemStack[] validInputs() {
        ArrayList<ItemStack> validatedInputs = new ArrayList<>();
        // Accept item from controller if it's a drone
        ItemStack controllerSlot = this.getControllerSlot();
        if (controllerSlot != null && controllerSlot.getItem() instanceof ItemMiningDrones) {
            validatedInputs.add(controllerSlot);
        }
        Map<GTUtility.ItemId, ItemStack> inputsFromME = new HashMap<>();

        for (MTEHatchInputBus inputBus : validMTEList(mInputBusses)) {
            IGregTechTileEntity tileEntity = inputBus.getBaseMetaTileEntity();
            boolean isMEBus = inputBus instanceof MTEHatchInputBusME;
            boolean isLinkedBus = inputBus instanceof MTELinkedInputBus;
            boolean isSharableBus = isMEBus || isLinkedBus;
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    if (isSharableBus && itemStack.getItem() instanceof ItemMiningDrones) {
                        continue;
                    }
                    if (isMEBus) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GTUtility.ItemId.createNoCopy(itemStack), itemStack);
                    } else {
                        validatedInputs.add(itemStack);
                    }
                }
            }
        }
        if (!inputsFromME.isEmpty()) {
            validatedInputs.addAll(inputsFromME.values());
        }
        return validatedInputs.toArray(new ItemStack[0]);
    }

    /**
     * Try to process the input resources
     *
     * @param inputs      Item inputs
     * @param fluidInputs Fluid inputs
     * @return Multiblock control structure that contains all process data or null if nothing can be processed
     */
    public CheckRecipeResult process(ItemStack[] inputs, FluidStack[] fluidInputs, int availablePlasmaTier,
        FluidStack plasma, int maxParallels) {
        // Check inputs
        if ((inputs == null && fluidInputs == null)) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        if (plasma == null || availablePlasmaTier <= 0) {
            return SimpleCheckRecipeResult.ofFailure("no_plasma");
        }

        // Get all asteroid pools that this drone can pull from
        long tVoltage = getMaxInputVoltage();
        int distance = (int) distanceDisplay.get();
        int availDroneMask = getAvailDroneMask(inputs);
        WeightedAsteroidList recipes = null;
        // Try to use the cached recipe list if the distance and available drones are the same as when it was computed
        if (prevRecipes != null && prevDistance == distance && prevAvailDroneMask == availDroneMask) {
            recipes = prevRecipes;
        } else {
            recipes = new WeightedAsteroidList(
                IGRecipeMaps.spaceMiningRecipes.findRecipeQuery()
                    .items(inputs)
                    .fluids(fluidInputs)
                    .voltage(tVoltage)
                    .findAll()
                    .filter(r -> {
                        // Check module tier
                        int recipeTier = r.getMetadataOrDefault(IGRecipeMaps.MODULE_TIER, 1);
                        if (recipeTier > tModuleTier) return false;

                        // Check mining recipe distance
                        SpaceMiningData data = r.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
                        if (data == null) return false;
                        return data.minDistance <= distance && data.maxDistance >= distance;
                    })
                    .distinct());
            // The original implementation had each recipe added multiple times redundantly, so I implemented
            // hashCode/equals
            // and use .distinct() here
            // It's possible to avoid this by arranging the recipes into a kind of interval tree, but the complexity is
            // not worth it.
            // Interval tree code still exists in the commit history if anyone ever wants it.
            prevRecipes = recipes;
            prevDistance = distance;
            prevAvailDroneMask = availDroneMask;
        }

        // Return if no recipe was found
        if (recipes.totalWeight == 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        float compModifier = 1f;
        float plasmaModifier = 1f;

        if (asteroidOutpost != null) {
            compModifier -= asteroidOutpost.getComputationDiscount();
            plasmaModifier -= asteroidOutpost.getPlasmaDiscount();
        }

        GTRecipe tRecipe = recipes.getRandom();

        // Make sure recipe really exists and we have enough power
        if (tRecipe == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        if (tRecipe.mEUt > tVoltage) {
            return CheckRecipeResultRegistry.insufficientPower(tRecipe.mEUt);
        }

        SpaceMiningData data = tRecipe.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
        if (data == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // Limit parallels by available computation, return if not enough computation is available
        maxParallels = (int) Math.min(maxParallels, getAvailableData_EM() / (data.computation * compModifier));
        if (maxParallels <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // Check how many parallels we can actually do, return if none
        ParallelHelper helper = new ParallelHelper().setMaxParallel(maxParallels)
            .setRecipe(tRecipe)
            .setFluidInputs(fluidInputs)
            .setItemInputs(inputs)
            .setAvailableEUt(GTValues.V[tTier])
            .setMachine(this, false, false)
            .setConsumption(true)
            .build();
        int parallels = helper.getCurrentParallel();
        if (parallels <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // Randomly generate ore stacks with the given chances, ores and size
        Map<GTUtility.ItemId, Long> outputs = new HashMap<>();

        int totalChance = 0;
        final int[] mOutputChances;
        if (tRecipe.mOutputChances == null) {
            totalChance = tRecipe.mOutputs.length * 10000;
            mOutputChances = new int[tRecipe.mOutputs.length];
            Arrays.fill(mOutputChances, 10000);
        } else {
            mOutputChances = tRecipe.mOutputChances;
            for (int mChance : mOutputChances) totalChance += mChance;
        }

        final int bonusStackChance = getBonusStackChance(availablePlasmaTier);
        for (int i = 0; i < data.maxSize * parallels; i++) {
            if (i < data.minSize * parallels || bonusStackChance > XSTR.XSTR_INSTANCE.nextInt(10000)) {
                int random = XSTR.XSTR_INSTANCE.nextInt(totalChance);
                for (int j = 0; j < mOutputChances.length; j++) {
                    random -= mOutputChances[j];
                    if (random < 0) {
                        ItemStack generatedOre = tRecipe.mOutputs[j];
                        if (configuredOres == null || configuredOres.isEmpty()
                            || isWhitelisted == configuredOres.contains(getOreString(generatedOre))) {
                            outputs.merge(
                                GTUtility.ItemId.createNoCopy(generatedOre),
                                (long) generatedOre.stackSize,
                                Long::sum);
                        }
                        break;
                    }
                }
            }
        }

        plasma.amount = (int) Math.max(
            0,
            Math.ceil(plasma.amount - parallels * getPlasmaUsageFromTier(availablePlasmaTier) * plasmaModifier));

        // Assign recipe parameters
        ArrayList<ItemStack> outputItems = new ArrayList<>();
        for (Map.Entry<GTUtility.ItemId, Long> entry : outputs.entrySet()) {
            ParallelHelper.addItemsLong(
                outputItems,
                entry.getKey()
                    .getItemStack(),
                entry.getValue());
        }
        mOutputItems = outputItems.toArray(new ItemStack[0]);

        lEUt = (long) -tRecipe.mEUt * parallels;
        eAmpereFlow = 1;
        eRequiredData = (int) Math.ceil(data.computation * parallels * compModifier);
        mMaxProgresstime = getRecipeTime(tRecipe.mDuration, availablePlasmaTier);
        mEfficiencyIncrease = 10000;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /** Determine what drones exist and have the required drills/rods for at least one recipe in a list of inputs */
    protected int getAvailDroneMask(ItemStack[] inputs) {
        Map<GTUtility.ItemId, Long> itemCounts = new HashMap<>();
        for (ItemStack input : inputs) {
            // XXX: all space mining recipes are nbt insensitive, but if this ever changes, we would need to compare
            // items including nbt
            if (input == null) continue;
            GTUtility.ItemId key = GTUtility.ItemId.createWithoutNBT(input);
            itemCounts.merge(key, (long) input.stackSize, Long::sum);
        }
        int res = 0;
        for (int tier = ItemMiningDrones.DroneTiers.LV.ordinal(); tier
            <= ItemMiningDrones.DroneTiers.UXV.ordinal(); ++tier) {
            if (Arrays.stream(SpaceMiningRecipes.getTieredInputs(tier))
                .allMatch(
                    input -> itemCounts.getOrDefault(GTUtility.ItemId.createWithoutNBT(input), 0L)
                        >= Math.max(input.stackSize, 1))) {
                res |= 1 << tier;
            }
        }
        return res;
    }

    /**
     * Get the tier from the input fluid stack that should be a valid plasma
     *
     * @param fluidStack Fluid stack containing a valid plasma
     * @return Tier of the input plasma if valid, else 0
     */
    protected int getTierFromPlasma(FluidStack fluidStack) {
        if (fluidStack == null) {
            return 0;
        }
        if ((fluidStack.isFluidEqual(Materials.Plutonium241.getPlasma(1))
            && fluidStack.amount >= PLASMA_PLUTONIUM241_USAGE)) {
            return 5;
        } else if ((fluidStack.isFluidEqual(new FluidStack(MaterialsElements.getInstance().TECHNETIUM.getPlasma(), 1))
            && fluidStack.amount >= PLASMA_TECHNETIUM_USAGE)) {
                return 4;
            } else
            if (fluidStack.isFluidEqual(Materials.Radon.getPlasma(1)) && fluidStack.amount >= PLASMA_RADON_USAGE) {
                return 3;
            } else if (fluidStack.isFluidEqual(Materials.Bismuth.getPlasma(1))
                && fluidStack.amount >= PLASMA_BISMUTH_USAGE) {
                    return 2;
                } else if (fluidStack.isFluidEqual(Materials.Helium.getPlasma(1))
                    && fluidStack.amount >= PLASMA_HELIUM_USAGE) {
                        return 1;
                    }
        return 0;
    }

    /**
     * Get the plasma usage for the input plasma tier
     *
     * @param plasmaTier Used plasma tier
     * @return Usage of the input plasma tier
     */
    protected int getPlasmaUsageFromTier(int plasmaTier) {
        return switch (plasmaTier) {
            case 1 -> PLASMA_HELIUM_USAGE;
            case 2 -> PLASMA_BISMUTH_USAGE;
            case 3 -> PLASMA_RADON_USAGE;
            case 4 -> PLASMA_TECHNETIUM_USAGE;
            case 5 -> PLASMA_PLUTONIUM241_USAGE;
            default -> 0;
        };
    }

    /**
     * Get the chance for additional stacks in Space Mining
     *
     * @param plasmaTier Available plasma tier
     * @return Chance for the bonus stack
     */
    protected int getBonusStackChance(int plasmaTier) {
        if (plasmaTier <= 0 || plasmaTier > 5) {
            return 0;
        }
        // getBonusStackChance with tiered plasmas:
        // T1: 0.004
        // T2: 0.037
        // T3: 0.125
        // T4: 0.296
        // T5: 0.578
        // The whole chance is multiplied by 2 - overdrive setting
        return Math.min(
            (int) ((GTUtility.powInt((double) plasmaTier / 6, 3) * 10000) * (2.0D - overdriveSetting.get())),
            BONUS_STACK_MAX_CHANCE);
    }

    /**
     * Generate configured ore list from input ore block stacks
     */
    protected void generateOreConfigurationList() {
        if (configuredOres == null) {
            configuredOres = new HashSet<>();
        } else {
            configuredOres.clear();
        }
        if (whiteListHandler != null) {
            for (ItemStack item : whiteListHandler.getStacks()) {
                configuredOres.add(getOreString(item));
            }
        }
    }

    /**
     * Get the string that represents the input ore stack (name:itemDamage)
     *
     * @param oreStack Ore stack of which the string will be gotten
     * @return String that represents the input ore stack
     */
    protected String getOreString(ItemStack oreStack) {
        if (oreStack == null || oreStack.getItem() == null) {
            return null;
        }
        // For GT ores we want to save the ore independent of its stone type
        if (oreStack.getUnlocalizedName()
            .startsWith("gt.blockores")) {
            return oreStack.getItem()
                .getUnlocalizedName() + ":"
                + oreStack.getItemDamage() % 1000;
        } else {
            return oreStack.getItem()
                .getUnlocalizedName() + ":"
                + oreStack.getItemDamage();
        }
    }

    /**
     * Get the number of parallels that this module can handle
     *
     * @return Number of possible parallels
     */
    protected abstract int getMaxParallels();

    /**
     * Get the number of parallels that this module can handle
     *
     * @param plasma      Valid plasma
     * @param plasmaUsage Usage of this plasma per parallel
     * @return Number of possible parallels
     */
    protected int getParallels(FluidStack plasma, int plasmaUsage) {
        if (plasma == null) {
            return 0;
        }
        float plasmaModifier = asteroidOutpost != null ? 1f - asteroidOutpost.getPlasmaDiscount() : 1f;
        return Math.min(
            (int) Math.min(getMaxParallels(), parallelSetting.get()),
            (int) (plasma.amount / (plasmaUsage * plasmaModifier)));
    }

    /**
     * Applies time boost to the recipe
     *
     * @param unboostedTime Recipe time without any bonuses
     * @return Boosted recipe time
     */
    protected int getRecipeTime(int unboostedTime, int plasmaTier) {
        // Reduce recipe time by 10% for every plasma tier above T1 and divide recipe time by the overdrive value
        return plasmaTier > 0
            ? (int) ((double) unboostedTime
                * Math.max((1D - 0.1D * (plasmaTier - 1)) / overdriveSetting.get(), MIN_RECIPE_TIME_MODIFIER))
            : unboostedTime;
    }

    /**
     * Cycle the current distance according to parameters
     */
    protected void cycleDistance() {
        if (((int) modeSetting.get()) != 0) {
            // cycle distanceDisplay from (distance - range)
            // to (distance + range) in increments of step.
            if (distanceDisplay.get() + stepSetting.get()
                <= Math.min(MAX_DISTANCE, distanceSetting.get() + rangeSetting.get())) {
                distanceDisplay.set(distanceDisplay.get() + stepSetting.get());
            } else {
                distanceDisplay.set(Math.max(0, distanceSetting.get() - rangeSetting.get()));
            }
        } else {
            distanceDisplay.set(Math.min(MAX_DISTANCE, Math.max(0, distanceSetting.get())));
        }
    }

    /**
     * POD class, Data for the asteroid summary in the mui in the space miner
     *
     * @author hacatu
     */
    protected static class AsteroidSummary {

        public String name;
        public float chance;
        public float timeDensity;
        public int maxParallels;

        public AsteroidSummary(String name, float chance, float timeDensity, int maxParallels) {
            this.name = name;
            this.chance = chance;
            this.timeDensity = timeDensity;
            this.maxParallels = maxParallels;
        }
    }

    /**
     * Get a list of summaries for some set of recipes. For each recipe, find: - chance: the probability of choosing it
     * at each operation - timeDensity: the fraction of time that the recipe takes up in the long run - maxParallels:
     * the number of parallels the mining module will do at its current settings, level, and computation. (assuming that
     * power, computation, plasma, and drills/rods don't run out)
     *
     * @author hacatu
     */
    protected List<AsteroidSummary> getAsteroidSummaries(int maxParallels, float effectiveComp) {
        long power = GTValues.V[tTier];
        if (prevRecipes == null) {
            return Collections.<AsteroidSummary>emptyList();
        }
        float totalWeight = prevRecipes.totalWeight; // save to float, so we don't have to cast in the following loop
        float totalTimedensity = prevRecipes.totalTimedensity;
        return prevRecipes.recipes.stream()
            .map(r -> {
                SpaceMiningData data = r.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
                if (data == null) throw new IllegalStateException("Illegal space miner recipe found");
                return new AsteroidSummary(
                    data.asteroidName,
                    data.recipeWeight / totalWeight,
                    data.recipeWeight * r.mDuration / totalTimedensity,
                    Math.min(maxParallels, Math.min((int) (effectiveComp / data.computation), (int) (power / r.mEUt))));
            })
            .collect(Collectors.toList());
    }

    /**
     * Instantiate parameters of the controller
     */
    @Override
    protected void parametersInstantiation_EM() {
        super.parametersInstantiation_EM();
        Parameters.Group hatch_0 = parametrization.getGroup(0, false);
        Parameters.Group hatch_1 = parametrization.getGroup(1, false);
        Parameters.Group hatch_2 = parametrization.getGroup(2, false);
        Parameters.Group hatch_3 = parametrization.getGroup(3, false);
        distanceSetting = hatch_0.makeInParameter(0, 1, DISTANCE_SETTING_NAME, DISTANCE_STATUS);
        parallelSetting = hatch_0.makeInParameter(1, getMaxParallels(), PARALLEL_SETTING_NAME, PARALLEL_STATUS);
        overdriveSetting = hatch_1.makeInParameter(0, 1, OVERDRIVE_SETTING_NAME, OVERDRIVE_STATUS);
        modeSetting = hatch_2.makeInParameter(0, 0, MODE_SETTING_NAME, MODE_STATUS);
        rangeSetting = hatch_2.makeInParameter(1, 0, RANGE_SETTING_NAME, RANGE_STATUS);
        stepSetting = hatch_3.makeInParameter(0, 0, STEP_SETTING_NAME, STEP_STATUS);
        distanceDisplay = hatch_0.makeOutParameter(1, 1, DISTANCE_SETTING_NAME, DISTANCE_STATUS);
    }

    /**
     * @return Button that will be generated in place of the safe void button
     */
    @Override
    protected ButtonWidget createSafeVoidButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            if (!widget.isClient()) {
                widget.getContext()
                    .openSyncedWindow(WHITELIST_WINDOW_ID);
            }
        })
            .setPlayClickSound(false)
            .setBackground(TecTechUITextures.BUTTON_STANDARD_16x16, IG_UITextures.OVERLAY_BUTTON_OPTIONS)
            .setPos(174, 132)
            .setSize(16, 16);
        button.addTooltip("Configure Filter")
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
        return (ButtonWidget) button;
    }

    /**
     * Add widgets to the GUI
     *
     * @param builder      Used window builder
     * @param buildContext Context of the GUI
     */
    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(new FakeSyncWidget.BooleanSyncer(() -> isWhitelisted, val -> isWhitelisted = val));
        buildContext.addSyncedWindow(WHITELIST_WINDOW_ID, this::createWhitelistConfigWindow);
    }

    /**
     * Create the window that is used to configure the module white-/blacklist
     *
     * @param player Player that opened the window
     * @return Window object
     */
    protected ModularWindow createWhitelistConfigWindow(final EntityPlayer player) {
        return ModularWindow.builder(158, 180)
            .setBackground(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
            .setGuiTint(getGUIColorization())
            // Toggle white-/blacklist
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                isWhitelisted = !isWhitelisted;
                wasFilterModified = true;
            })
                .setPlayClickSound(false)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
                    if (isWhitelisted) {
                        ret.add(IG_UITextures.OVERLAY_BUTTON_WHITELIST);
                    } else {
                        ret.add(IG_UITextures.OVERLAY_BUTTON_BLACKLIST);
                    }
                    return ret.toArray(new IDrawable[0]);
                })
                .setPos(7, 9)
                .setSize(16, 16)
                .addTooltip("Mode")
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
            // Clear list
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                wasFilterModified = true;
                if (!widget.isClient()) {
                    if (whiteListHandler != null) {
                        for (int i = 0; i < whiteListHandler.getSlots(); i++) {
                            whiteListHandler.setStackInSlot(i, null);
                        }
                    }
                }
            })
                .setPlayClickSound(false)
                .setBackground(TecTechUITextures.BUTTON_STANDARD_16x16, IG_UITextures.OVERLAY_BUTTON_CROSS)
                .setPos(25, 9)
                .setSize(16, 16)
                .addTooltip("Clear")
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
            // Configure from bus
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                wasFilterModified = true;
                if (!widget.isClient()) {
                    int i = 0;
                    for (ItemStack itemStack : getStoredInputs()) {
                        if (i < WHITELIST_SIZE) {
                            ItemStack copy = itemStack.copy();
                            copy.stackSize = 1;
                            whiteListHandler.setStackInSlot(i++, copy);
                        }
                    }

                }
            })
                .setPlayClickSound(false)
                .setBackground(TecTechUITextures.BUTTON_STANDARD_16x16, IG_UITextures.OVERLAY_BUTTON_CONFIGURE)
                .setPos(43, 9)
                .setSize(16, 16)
                .addTooltip("Load from Bus")
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
            // List
            .widget(
                SlotGroup.ofItemHandler(whiteListHandler, 8)
                    .startFromSlot(0)
                    .endAtSlot(WHITELIST_SIZE - 1)
                    .applyForWidget(slotWidget -> slotWidget.setChangeListener(() -> wasFilterModified = true))
                    .phantom(true)
                    .background(getGUITextureSet().getItemSlot())
                    .build()
                    .setPos(7, 27))
            .build();
    }

    /**
     * Draw texts on the project module GUI
     *
     * @param screenElements Column that holds all screen elements
     * @param inventorySlot  Inventory slot of the controller
     */
    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements.widget(TextWidget.dynamicString(() -> {
            StringBuilder res = new StringBuilder();
            res.append(StatCollector.translateToLocal("gt.blockmachines.multimachine.project.ig.miner.cfgi.4"));
            res.append(": ");
            res.append(
                StatCollector.translateToLocal(
                    (int) modeSetting.get() == 0 ? "gt.blockmachines.multimachine.project.ig.miner.cfgi.4.1"
                        : "gt.blockmachines.multimachine.project.ig.miner.cfgi.4.2"));
            res.append('\n');
            if (prevRecipes != null) {
                res.append(
                    StatCollector.translateToLocal("gt.blockmachines.multimachine.project.ig.miner.activedronetiers"));
                res.append(": ");
                boolean found = false;
                for (ItemMiningDrones.DroneTiers tier : ItemMiningDrones.DroneTiers.values()) {
                    if (((1 << tier.ordinal()) & prevAvailDroneMask) != 0) {
                        if (found) {
                            res.append(", ");
                        }
                        res.append(tier.toString());
                        found = true;
                    }
                }
                if (!found) {
                    res.append(" None");
                }
                res.append('\n');
                res.append(
                    StatCollector
                        .translateToLocal("gt.blockmachines.multimachine.project.ig.miner.asteroidsummaries.0"));
                res.append(":\n");
                float effectiveComp = getAvailableData_EM()
                    / (asteroidOutpost == null ? 1f : 1f - asteroidOutpost.getComputationDiscount());
                for (AsteroidSummary summ : getAsteroidSummaries(
                    Math.min(getMaxParallels(), (int) parallelSetting.get()),
                    effectiveComp)) {
                    res.append(StatCollector.translateToLocal("ig.asteroid." + summ.name));
                    res.append(
                        String.format(
                            ": %.3f%% / %s, %.3f%% / %s, %s %dx",
                            summ.chance * 100f,
                            StatCollector
                                .translateToLocal("gt.blockmachines.multimachine.project.ig.miner.asteroidchance"),
                            summ.timeDensity * 100f,
                            StatCollector
                                .translateToLocal("gt.blockmachines.multimachine.project.ig.miner.asteroidtimedensity"),
                            StatCollector.translateToLocal(
                                "gt.blockmachines.multimachine.project.ig.miner.asteroidmaxparallels"),
                            summ.maxParallels));
                    res.append('\n');
                }
            }
            return res.toString();
        })
            .setSynced(true)
            .setTextAlignment(Alignment.TopLeft)
            .setScale(0.5f)
            .setDefaultColor(COLOR_TEXT_WHITE.get())
            .setEnabled(widget -> mMachine))
            .widget(
                new FakeSyncWidget.IntegerSyncer(
                    () -> (int) modeSetting.get(),
                    val -> parametrization
                        .trySetParameters(modeSetting.id % 10, modeSetting.id / 10, modeSetting.get())));
    }

    /** Texture that will be displayed on the side of the module */
    protected static Textures.BlockIcons.CustomIcon engraving;

    /**
     * Get the texture of this controller
     *
     * @param aBaseMetaTileEntity This
     * @param side                Side for which the texture will be gotten
     * @param facing              Facing side of the controller
     * @param colorIndex          Color index
     * @param aActive             Flag if the controller is active
     * @param aRedstone           Flag if Redstone is present
     * @return Texture array of this controller
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE),
                new TTRenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        } else if (facing.getRotation(ForgeDirection.UP) == side || facing.getRotation(ForgeDirection.DOWN) == side) {
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE),
                new TTRenderedExtendedFacingTexture(engraving) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE) };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        engraving = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_SIDE_MINER_MODULE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!super.checkMachine_EM(aBaseMetaTileEntity, aStack)) {
            return false;
        }
        if (wasFilterModified) {
            wasFilterModified = false;
            generateOreConfigurationList();
        }
        if (SpaceProjectManager.teamHasProject(getBaseMetaTileEntity().getOwnerUuid(), ASTEROID_OUTPOST)) {
            ISpaceProject proj = SpaceProjectManager
                .getTeamProject(getBaseMetaTileEntity().getOwnerUuid(), SolarSystem.KuiperBelt, "AsteroidOutpost");
            if (proj.isFinished()) {
                asteroidOutpost = (ProjectAsteroidOutpost) proj;
            }
        }
        return true;
    }

    /**
     * Space Miner project module T1 of the Space Elevator
     *
     * @author minecraft7771
     */
    public static class TileEntityModuleMinerT1 extends TileEntityModuleMiner {

        /** Voltage tier of this module */
        protected static final int MODULE_VOLTAGE_TIER = 8;
        /** Tier of this module */
        protected static final int MODULE_TIER = 1;
        /** Minimum motor tier that is needed for this module */
        protected static final int MINIMUM_MOTOR_TIER = 1;
        /** Maximum parallels that the module supports */
        protected static final int MAXIMUM_PARALLELS = 2;

        /**
         * Create a new T1 mining module controller
         *
         * @param aID           ID of the controller
         * @param aName         Name of the controller
         * @param aNameRegional Localized name of the controller
         */
        public TileEntityModuleMinerT1(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Create a new T1 mining module controller
         *
         * @param aName Name of the controller
         */
        public TileEntityModuleMinerT1(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Get a new meta tile entity of this controller
         *
         * @param iGregTechTileEntity this
         * @return New meta tile entity
         */
        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TileEntityModuleMinerT1(mName);
        }

        /**
         * Get the number of parallels that this module can handle
         *
         * @return Number of possible parallels
         */
        protected int getMaxParallels() {
            return MAXIMUM_PARALLELS;
        }

        /**
         * Create the tooltip of this controller
         *
         * @return Tooltip builder
         */
        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addMachineType(GTUtility.translate("gt.blockmachines.module.name"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc0")) // Module
                                                                                                      // that
                // adds Space
                // Mining
                // Operations to the
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.t1.desc1")) // Does
                // this
                // violate
                // drone rights?
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc3"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc4"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc5"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc5.1"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc5.2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.t1.desc5"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.motorT1"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc6"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GTUtility.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addInputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addOutputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addInputHatch(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addOtherStructurePart(
                    GTUtility.translate("ig.elevator.structure.OpticalConnector"),
                    GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"),
                    1)
                .toolTipFinisher();
            return tt;
        }
    }

    /**
     * Space Miner project module T2 of the Space Elevator
     *
     * @author minecraft7771
     */
    public static class TileEntityModuleMinerT2 extends TileEntityModuleMiner {

        /** Voltage tier of this module */
        protected static final int MODULE_VOLTAGE_TIER = 9;
        /** Tier of this module */
        protected static final int MODULE_TIER = 2;
        /** Minimum motor tier that is needed for this module */
        protected static final int MINIMUM_MOTOR_TIER = 2;
        /** Maximum parallels that the module supports */
        protected static final int MAXIMUM_PARALLELS = 4;

        /**
         * Create a new T2 mining module controller
         *
         * @param aID           ID of the controller
         * @param aName         Name of the controller
         * @param aNameRegional Localized name of the controller
         */
        public TileEntityModuleMinerT2(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Create a new T2 mining module controller
         *
         * @param aName Name of the controller
         */
        public TileEntityModuleMinerT2(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Get a new meta tile entity of this controller
         *
         * @param iGregTechTileEntity this
         * @return New meta tile entity
         */
        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TileEntityModuleMinerT2(mName);
        }

        /**
         * Get the number of parallels that this module can handle
         *
         * @return Number of possible parallels
         */
        protected int getMaxParallels() {
            return MAXIMUM_PARALLELS;
        }

        /**
         * Create the tooltip of this controller
         *
         * @return Tooltip builder
         */
        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addMachineType(GTUtility.translate("gt.blockmachines.module.name"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc0")) // Module
                                                                                                      // that
                // adds Space
                // Mining
                // Operations to the
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.t2.desc1")) // This
                // definitely
                // violates
                // drone rights.
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc3"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc4"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc5"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc5.1"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc5.2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.t2.desc5"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.motorT2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc6"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GTUtility.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addInputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addOutputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addInputHatch(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addOtherStructurePart(
                    GTUtility.translate("ig.elevator.structure.OpticalConnector"),
                    GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"),
                    1)
                .toolTipFinisher();
            return tt;
        }
    }

    /**
     * Space Miner project module T3 of the Space Elevator
     *
     * @author minecraft7771
     */
    public static class TileEntityModuleMinerT3 extends TileEntityModuleMiner {

        /** Voltage tier of this module */
        protected static final int MODULE_VOLTAGE_TIER = 10;
        /** Tier of this module */
        protected static final int MODULE_TIER = 3;
        /** Minimum motor tier that is needed for this module */
        protected static final int MINIMUM_MOTOR_TIER = 3;
        /** Maximum parallels that the module supports */
        protected static final int MAXIMUM_PARALLELS = 8;

        /**
         * Create a new T3 mining module controller
         *
         * @param aID           ID of the controller
         * @param aName         Name of the controller
         * @param aNameRegional Localized name of the controller
         */
        public TileEntityModuleMinerT3(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Create a new T3 mining module controller
         *
         * @param aName Name of the controller
         */
        public TileEntityModuleMinerT3(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Get a new meta tile entity of this controller
         *
         * @param iGregTechTileEntity this
         * @return New meta tile entity
         */
        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TileEntityModuleMinerT3(mName);
        }

        /**
         * Get the number of parallels that this module can handle
         *
         * @return Number of possible parallels
         */
        protected int getMaxParallels() {
            return MAXIMUM_PARALLELS;
        }

        /**
         * Create the tooltip of this controller
         *
         * @return Tooltip builder
         */
        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addMachineType(GTUtility.translate("gt.blockmachines.module.name"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc0")) // Module
                                                                                                      // that
                // adds Space
                // Mining
                // Operations to the
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.t3.desc1")) // Great
                // treasures
                // beyond
                // your imagination await!
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc3"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc4"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc5"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc5.1"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc5.2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.t3.desc5"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.motorT3"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.miner.desc6"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GTUtility.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addInputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addOutputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addInputHatch(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addOtherStructurePart(
                    GTUtility.translate("ig.elevator.structure.OpticalConnector"),
                    GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"),
                    1)
                .toolTipFinisher();
            return tt;
        }
    }
}
