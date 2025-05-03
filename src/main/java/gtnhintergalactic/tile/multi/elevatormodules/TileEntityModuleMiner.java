package gtnhintergalactic.tile.multi.elevatormodules;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.util.GTUtility.validMTEList;
import static gtnhintergalactic.recipe.IGRecipeMaps.spaceMiningRecipes;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.MINING_DRILLS;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.MINING_DRONES;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.MINING_RODS;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.uniqueAsteroidList;
import static java.util.stream.Collectors.toList;
import static tectech.Reference.MODID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableArray;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.RichTextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;

import akka.japi.Pair;
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
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.common.gui.modularui.widget.StringKeyCustom;
import gregtech.common.gui.modularui.widget.TextFieldWidgetWithOverlay;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.enums.SolarSystem;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.common.modularui2.widget.SlotLikeButtonWidget;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gtPlusPlus.core.material.MaterialsElements;
import gtnhintergalactic.item.ItemMiningDrones;
import gtnhintergalactic.recipe.AsteroidData;
import gtnhintergalactic.recipe.IGRecipeMaps;
import gtnhintergalactic.recipe.SpaceMiningData;
import gtnhintergalactic.recipe.SpaceMiningRecipes;
import gtnhintergalactic.recipe.SpaceMiningRecipes.WeightedAsteroidList;
import gtnhintergalactic.spaceprojects.ProjectAsteroidOutpost;
import gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
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
    // protected ItemStackHandler whiteListHandler = new ItemStackHandler(WHITELIST_SIZE);
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
        // if (whiteListHandler != null) {
        // whiteListHandler.deserializeNBT(aNBT.getCompoundTag(WHITELIST_NBT_TAG));
        // }
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
        // if (whiteListHandler != null) {
        // aNBT.setTag(WHITELIST_NBT_TAG, whiteListHandler.serializeNBT());
        // }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return spaceMiningRecipes;
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
        List<FluidStack> inputFluids = getStoredFluids();
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
                spaceMiningRecipes.findRecipeQuery()
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
        int totalChance = Arrays.stream(tRecipe.mChances)
            .sum();
        try {
            for (int i = 0; i < data.maxSize * parallels; i++) {
                int bonusStackChance = 0;
                if (i >= data.minSize * parallels) {
                    bonusStackChance = getBonusStackChance(availablePlasmaTier);
                }
                if (i < data.minSize * parallels || bonusStackChance > XSTR.XSTR_INSTANCE.nextInt(10000)) {
                    int random = XSTR.XSTR_INSTANCE.nextInt(totalChance);
                    int currentChance = 0;
                    for (int j = 0; j < tRecipe.mChances.length; j++) {
                        currentChance += tRecipe.mChances[j];
                        if (random <= currentChance) {
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
        } catch (Exception ignored) {
            return CheckRecipeResultRegistry.NO_RECIPE;
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
        // TODO: Implement way to get computation from master controller. Or maybe keep it this way so
        // people can route computation to their liking?
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
                    input -> itemCounts.getOrDefault(GTUtility.ItemId.createWithoutNBT(input), 0l)
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
            (int) ((Math.pow((double) plasmaTier / 6, 3) * 10000) * (2.0D - overdriveSetting.get())),
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
        // if (whiteListHandler != null) {
        // for (ItemStack item : whiteListHandler.getStacks()) {
        // configuredOres.add(getOreString(item));
        // }
        // }
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
                    data.getAsteroidNameLocalized(),
                    data.recipeWeight / totalWeight,
                    data.recipeWeight * r.mDuration / totalTimedensity,
                    Math.min(maxParallels, Math.min((int) (effectiveComp / data.computation), (int) (power / r.mEUt))));
            })
            .collect(toList());
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
    //
    // /**
    // * @return Button that will be generated in place of the safe void button
    // */
    // @Override
    // protected ButtonWidget createSafeVoidButton() {
    // Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
    // TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
    // if (!widget.isClient()) {
    // widget.getContext()
    // .openSyncedWindow(WHITELIST_WINDOW_ID);
    // }
    // })
    // .setPlayClickSound(false)
    // .setBackground(TecTechUITextures.BUTTON_STANDARD_16x16, IG_UITextures.OVERLAY_BUTTON_OPTIONS)
    // .setPos(174, 132)
    // .setSize(16, 16);
    // button.addTooltip("Configure Filter")
    // .setTooltipShowUpDelay(TOOLTIP_DELAY);
    // return (ButtonWidget) button;
    // }

    // /**
    // * Add widgets to the GUI
    // *
    // * @param builder Used window builder
    // * @param buildContext Context of the GUI
    // */
    // @Override
    // public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
    // super.addUIWidgets(builder, buildContext);
    // builder.widget(new FakeSyncWidget.BooleanSyncer(() -> isWhitelisted, val -> isWhitelisted = val));
    // buildContext.addSyncedWindow(WHITELIST_WINDOW_ID, this::createWhitelistConfigWindow);
    // }

    // /**
    // * Create the window that is used to configure the module white-/blacklist
    // *
    // * @param player Player that opened the window
    // * @return Window object
    // */
    // protected ModularWindow createWhitelistConfigWindow(final EntityPlayer player) {
    // return ModularWindow.builder(158, 180)
    // .setBackground(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
    // .setGuiTint(getGUIColorization())
    // // Toggle white-/blacklist
    // .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
    // TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
    // isWhitelisted = !isWhitelisted;
    // wasFilterModified = true;
    // })
    // .setPlayClickSound(false)
    // .setBackground(() -> {
    // List<UITexture> ret = new ArrayList<>();
    // ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
    // if (isWhitelisted) {
    // ret.add(IG_UITextures.OVERLAY_BUTTON_WHITELIST);
    // } else {
    // ret.add(IG_UITextures.OVERLAY_BUTTON_BLACKLIST);
    // }
    // return ret.toArray(new IDrawable[0]);
    // })
    // .setPos(7, 9)
    // .setSize(16, 16)
    // .addTooltip("Mode")
    // .setTooltipShowUpDelay(TOOLTIP_DELAY))
    // // Clear list
    // .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
    // TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
    // wasFilterModified = true;
    // if (!widget.isClient()) {
    // if (whiteListHandler != null) {
    // for (int i = 0; i < whiteListHandler.getSlots(); i++) {
    // whiteListHandler.setStackInSlot(i, null);
    // }
    // }
    // }
    // })
    // .setPlayClickSound(false)
    // .setBackground(TecTechUITextures.BUTTON_STANDARD_16x16, IG_UITextures.OVERLAY_BUTTON_CROSS)
    // .setPos(25, 9)
    // .setSize(16, 16)
    // .addTooltip("Clear")
    // .setTooltipShowUpDelay(TOOLTIP_DELAY))
    // // Configure from bus
    // .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
    // TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
    // wasFilterModified = true;
    // if (!widget.isClient()) {
    // int i = 0;
    // for (ItemStack itemStack : getStoredInputs()) {
    // if (i < WHITELIST_SIZE) {
    // ItemStack copy = itemStack.copy();
    // copy.stackSize = 1;
    // whiteListHandler.setStackInSlot(i++, copy);
    // }
    // }
    //
    // }
    // })
    // .setPlayClickSound(false)
    // .setBackground(TecTechUITextures.BUTTON_STANDARD_16x16, IG_UITextures.OVERLAY_BUTTON_CONFIGURE)
    // .setPos(43, 9)
    // .setSize(16, 16)
    // .addTooltip("Load from Bus")
    // .setTooltipShowUpDelay(TOOLTIP_DELAY))
    // // List
    // .widget(
    // SlotGroup.ofItemHandler(whiteListHandler, 8)
    // .startFromSlot(0)
    // .endAtSlot(WHITELIST_SIZE - 1)
    // .applyForWidget(slotWidget -> slotWidget.setChangeListener(() -> wasFilterModified = true))
    // .phantom(true)
    // .background(getGUITextureSet().getItemSlot())
    // .build()
    // .setPos(7, 27))
    // .build();
    // }

    /**
     * Draw texts on the project module GUI
     *
     * @param screenElements Column that holds all screen elements
     * @param inventorySlot  Inventory slot of the controller
     */
    // @Override
    // protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
    // super.drawTexts(screenElements, inventorySlot);
    //
    // screenElements.widget(TextWidget.dynamicString(() -> {
    // StringBuilder res = new StringBuilder();
    // res.append(StatCollector.translateToLocal("gt.blockmachines.multimachine.project.ig.miner.cfgi.4"));
    // res.append(": ");
    // res.append(
    // StatCollector.translateToLocal(
    // (int) modeSetting.get() == 0 ? "gt.blockmachines.multimachine.project.ig.miner.cfgi.4.1"
    // : "gt.blockmachines.multimachine.project.ig.miner.cfgi.4.2"));
    // res.append('\n');
    // if (prevRecipes != null) {
    // res.append(
    // StatCollector.translateToLocal("gt.blockmachines.multimachine.project.ig.miner.activedronetiers"));
    // res.append(": ");
    // boolean found = false;
    // for (ItemMiningDrones.DroneTiers tier : ItemMiningDrones.DroneTiers.values()) {
    // if (((1 << tier.ordinal()) & prevAvailDroneMask) != 0) {
    // if (found) {
    // res.append(", ");
    // }
    // res.append(tier.toString());
    // found = true;
    // }
    // }
    // if (!found) {
    // res.append(" None");
    // }
    // res.append('\n');
    // res.append(
    // StatCollector
    // .translateToLocal("gt.blockmachines.multimachine.project.ig.miner.asteroidsummaries.0"));
    // res.append(":\n");
    // float effectiveComp = getAvailableData_EM()
    // / (asteroidOutpost == null ? 1f : 1f - asteroidOutpost.getComputationDiscount());
    // for (AsteroidSummary summ : getAsteroidSummaries(
    // Math.min(getMaxParallels(), (int) parallelSetting.get()),
    // effectiveComp)) {
    // res.append(StatCollector.translateToLocal("ig.asteroid." + summ.name));
    // res.append(
    // String.format(
    // ": %.3f%% / %s, %.3f%% / %s, %s %dx",
    // summ.chance * 100f,
    // StatCollector
    // .translateToLocal("gt.blockmachines.multimachine.project.ig.miner.asteroidchance"),
    // summ.timeDensity * 100f,
    // StatCollector
    // .translateToLocal("gt.blockmachines.multimachine.project.ig.miner.asteroidtimedensity"),
    // StatCollector.translateToLocal(
    // "gt.blockmachines.multimachine.project.ig.miner.asteroidmaxparallels"),
    // summ.maxParallels));
    // res.append('\n');
    // }
    // }
    // return res.toString();
    // })
    // .setSynced(true)
    // .setTextAlignment(Alignment.TopLeft)
    // .setScale(0.5f)
    // .setDefaultColor(COLOR_TEXT_WHITE.get())
    // .setEnabled(widget -> mMachine))
    // .widget(
    // new FakeSyncWidget.IntegerSyncer(
    // () -> (int) modeSetting.get(),
    // val -> parametrization
    // .trySetParameters(modeSetting.id % 10, modeSetting.id / 10, modeSetting.get())));
    // }

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

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    public boolean hasCustomButtons() {
        return true;
    }

    @Override
    public boolean shouldMakeEditParametersButtonEnabled() {
        return false;
    }

    @Override
    public void insertTexts(ListWidget<IWidget, ?> machineInfo, ItemStackHandler invSlot, PanelSyncManager syncManager,
        ModularPanel parentPanel) {
        super.insertTexts(machineInfo, invSlot, syncManager, parentPanel);
    }

    @Override
    public void addCustomButtons(ModularPanel panel, PanelSyncManager syncManager) {

        UITexture spaceMinerConfigTexture = UITexture.fullImage(MODID, "gui/overlay_button/edit_parameters");
        ButtonWidget spaceMinerConfig = new ButtonWidget();
        spaceMinerConfig.tooltip(new RichTooltip(spaceMinerConfig).add("Get Asteroid Info"))
            .overlay(spaceMinerConfigTexture);
        spaceMinerConfig.pos(173, doesBindPlayerInventory() ? 109 + 18 : 133 + 18)
            .size(18, 18);

        IPanelHandler spaceMinerConfigPanel = syncManager.panel(
            "asteroidList",
            (p_syncManager, syncHandler) -> getSpaceMinerConfigPanel(panel, p_syncManager, syncHandler),
            true);
        spaceMinerConfig.onMousePressed(mouseData -> {
            if (!spaceMinerConfigPanel.isPanelOpen()) {
                spaceMinerConfigPanel.openPanel();
            } else {
                spaceMinerConfigPanel.closePanel();
            }
            return true;
        });
        panel.child(spaceMinerConfig);
    }

    private ModularPanel getSpaceMinerConfigPanel(ModularPanel parent, PanelSyncManager syncManager,
        IPanelHandler thisPanel) {
        String[] planetTiers = new String[] { "De", "As", "Io", "En", "Pr", "Ha", "BC" }; // for tab icons
        FontRenderer fontRenderer = NetworkUtils.isClient() ? Minecraft.getMinecraft().fontRenderer : null;
        UITexture calculatorTexture = UITexture.fullImage(MODID, "gui/overlay_button/calculator");

        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("asteroidList") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        };
        panel.size(175, ((uniqueAsteroidList.size() / 8) + 1) * 22 + 18 * 5)
            .pos(parentArea.x, parentArea.y)
            .padding(5);
        AtomicReference<String> search = new AtomicReference<>("");
        StringSyncValue textFieldSyncer = new StringSyncValue(search::get, search::set);

        AtomicInteger distanceFilter = new AtomicInteger(0);
        IntSyncValue distanceSyncer = new IntSyncValue(distanceFilter::get, distanceFilter::set);

        AtomicInteger moduleTier = new AtomicInteger(0);
        IntSyncValue moduleTierSyncer = new IntSyncValue(moduleTier::get, moduleTier::set);

        AtomicInteger droneFilter = new AtomicInteger(-1);
        IntSyncValue droneSyncer = new IntSyncValue(droneFilter::get, droneFilter::set);
        syncManager.syncValue("droneFilter", droneSyncer);

        Flow asteroidColumn = new Column().sizeRel(1);
        Flow asteroidRow = new Row().widthRel(1)
            .height(18)
            .marginBottom(4);
        List<IPanelHandler> asteroidPanels = new ArrayList<>();
        IPanelHandler droneSelectorPanel = syncManager.panel(
            "droneSelectorPanel",
            (p_syncManager,
                syncHandler) -> opendroneSelectorPanel(p_syncManager, syncHandler, droneSyncer, "asteroidInfo", panel),
            true);
        IPanelHandler minerCalculator = syncManager.panel(
            "spaceMinerCalculator",
            (p_syncManager,
                syncHandler) -> openSpaceMinerCalculator(p_syncManager, syncHandler, parent, asteroidPanels),
            true);
        for (int i = 0; i < uniqueAsteroidList.size(); i++) {
            int finalI = i;
            AsteroidData data = SpaceMiningRecipes.uniqueAsteroidList.get(i);
            IPanelHandler asteroidInfo = syncManager.panel(
                "asteroidInfo" + finalI,
                (p_syncManager, syncHandler) -> getAsteroidPanel(p_syncManager, syncHandler, finalI),
                true);
            asteroidPanels.add(asteroidInfo);

            ItemStack oreItem = data.outputItems != null ? data.outputItems[0]
                : GTOreDictUnificator.get(data.orePrefixes, data.output[0], 1);
            ButtonWidget asteroidButton = new ButtonWidget<>().size(18, 18)
                .overlay(new DynamicDrawable(() -> {
                    // Temporary solution for jank item rendering TODO: REMOVE THIS WHEN IT'S FIXED!
                    if (asteroidPanels.stream()
                        .anyMatch(IPanelHandler::isPanelOpen) || minerCalculator.isPanelOpen()) return null;
                    if (matchesFilters(
                        data,
                        textFieldSyncer.getValue(),
                        distanceSyncer.getValue(),
                        droneSyncer.getValue(),
                        moduleTierSyncer.getValue())) {
                        return new DrawableArray(
                            new Rectangle().setColor(Color.rgb(0, 255, 0))
                                .asIcon()
                                .size(16, 16),
                            new ItemDrawable(oreItem).asIcon()
                                .size(16, 16));
                    } else {
                        return new ItemDrawable(oreItem).asIcon()
                            .size(16, 16);
                    }
                }))
                .tooltipBuilder(
                    t -> t.addLine(IKey.str(EnumChatFormatting.RED + data.getAsteroidNameLocalized()))
                        .addLine(IKey.str("Click me to get more info!")))
                .onMousePressed(mouseData -> {
                    if (!asteroidInfo.isPanelOpen()) {
                        asteroidInfo.openPanel();
                    } else {
                        asteroidInfo.closePanel();
                    }
                    return true;
                });
            asteroidButton.marginRight(2);
            asteroidRow.child(asteroidButton);
            if ((i + 1) % 8 == 0 || i == uniqueAsteroidList.size() - 1) {
                asteroidColumn.child(asteroidRow);
                asteroidRow = new Row().widthRel(1)
                    .height(18)
                    .marginBottom(4);
            } ;
        }
        asteroidColumn.child(
            new Column().widthRel(1)
                .height(18 * 4)
                .child(
                    new TextFieldWidgetWithOverlay(
                        () -> !search.get()
                            .isEmpty()).size(60, 9)
                                .marginBottom(4)
                                .alignX(0)
                                .value(textFieldSyncer)
                                .overlay(new StringKeyCustom("Ore...")))
                .child(
                    new TextFieldWidgetWithOverlay(() -> distanceFilter.get() > 0).size(60, 9)
                        .marginBottom(4)
                        .alignX(0)
                        .overlay(new StringKeyCustom("Distance..."))
                        .value(distanceSyncer)
                        .setDefaultNumber(0)
                        .setNumbers(0, Integer.MAX_VALUE))
                .child(
                    new TextFieldWidgetWithOverlay(() -> moduleTier.get() > 0).size(60, 9)
                        .marginBottom(4)
                        .alignX(0)
                        .overlay(new StringKeyCustom("Tier..."))
                        .value(moduleTierSyncer)
                        .setDefaultNumber(0)
                        .setNumbers(0, 3))
                .child(
                    new Row().widthRel(1)
                        .height(18)
                        .child(
                            new SlotLikeButtonWidget(
                                () -> droneSyncer.getValue() >= 0 ? MINING_DRONES[droneSyncer.getValue()] : null)
                                    .size(18, 18)
                                    .marginBottom(4)
                                    .alignX(0)
                                    .onMousePressed(mouseData -> {
                                        if (!droneSelectorPanel.isPanelOpen()) {
                                            droneSelectorPanel.openPanel();
                                        } else {
                                            droneSelectorPanel.closePanel();
                                        }
                                        return true;
                                    })
                                    .align(Alignment.CenterLeft))
                        .child(
                            new ButtonWidget<>().size(18, 18)
                                .overlay(calculatorTexture)
                                .align(Alignment.CenterRight)
                                .tooltipBuilder(t -> t.addLine(IKey.str("Open Space Miner Calculator")))
                                .onMousePressed(mouseData -> {
                                    if (!minerCalculator.isPanelOpen()) {
                                        minerCalculator.openPanel();
                                    } else {
                                        minerCalculator.closePanel();
                                    }
                                    return true;
                                }))));
        panel.child(asteroidColumn);
        return panel;
    }

    private boolean matchesFilters(AsteroidData data, String oreFilter, int distanceFilter, int droneFilter,
        int moduleTier) {
        boolean result = true;
        boolean checkedAny = false;
        if (!oreFilter.isEmpty()) {
            result = asteroidContainsOre(data, oreFilter);
            checkedAny = true;
        }
        if (distanceFilter > 0) {
            result = result && asteroidMatchesDistance(data, distanceFilter);
            checkedAny = true;
        }
        if (droneFilter >= 0) {
            result = result && asteroidMatchesDroneRange(data, droneFilter);
            checkedAny = true;
        }
        if (moduleTier > 0) {
            result = result && asteroidMatchesModuleTier(data, moduleTier);
            checkedAny = true;
        }
        return result && checkedAny;
    }

    private boolean asteroidMatchesModuleTier(AsteroidData data, int moduleTier) {
        return moduleTier >= data.requiredModuleTier;
    }

    private boolean asteroidMatchesDroneRange(AsteroidData data, int droneFilter) {
        return droneFilter >= data.minDroneTier && droneFilter <= data.maxDroneTier;
    }

    private boolean asteroidMatchesDistance(AsteroidData data, Integer value) {
        return value >= data.minDistance && value <= data.maxDistance;
    }

    private boolean asteroidContainsOre(AsteroidData data, String stringValue) {
        if (stringValue.isEmpty()) return false;
        for (Materials output : data.output) {
            ItemStack itemOutput = GTOreDictUnificator.get(data.orePrefixes, output, 1);
            if (itemOutput.getDisplayName()
                .toLowerCase()
                .contains(stringValue.toLowerCase())) return true;
        }
        return false;
    }

    private ModularPanel openSpaceMinerCalculator(PanelSyncManager syncManager, IPanelHandler panelSyncHandler,
        ModularPanel parent, List<IPanelHandler> asteroidPanels) {
        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("spaceMinerCalculator") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.size(200, 164)
            .pos(parentArea.x, parentArea.y)
            .paddingTop(3)
            .paddingLeft(5)
            .paddingRight(5);
        UITexture nerdTexture = UITexture.fullImage(MODID, "gui/overlay_button/nerd");

        AtomicInteger distance = new AtomicInteger(0);
        IntSyncValue distanceSyncer = new IntSyncValue(distance::get, distance::set);

        AtomicInteger moduleTier = new AtomicInteger(0);
        IntSyncValue moduleTierSyncer = new IntSyncValue(moduleTier::get, moduleTier::set);

        AtomicInteger droneFilter = new AtomicInteger(-1);
        IntSyncValue droneSyncer = new IntSyncValue(droneFilter::get, droneFilter::set);
        syncManager.syncValue("droneSyncerCalculator", droneSyncer);

        IPanelHandler droneSelectorPanel = syncManager.panel(
            "droneSelectorPanelSpaceMiner",
            (p_syncManager,
                syncHandler) -> opendroneSelectorPanel(p_syncManager, syncHandler, droneSyncer, "calculator", panel),
            true);

        Flow column = new Column().sizeRel(1);
        ListWidget<IWidget, ?> outputListWidget = new ListWidget<>()
            .background(new DrawableArray(new Rectangle().setColor(Color.rgb(91, 110, 225))))
            .widthRel(1)
            .height(100)
            .marginBottom(4)
            .child(
                new Column().widthRel(1)
                    .child(
                        IKey.str("Missing distance setting!")
                            .asWidget()
                            .setEnabledIf(w -> distance.get() <= 0)
                            .alignX(0)
                            .marginBottom(4))
                    .child(
                        IKey.str("Missing module tier!")
                            .asWidget()
                            .setEnabledIf(w -> moduleTier.get() <= 0)
                            .alignX(0)
                            .marginBottom(4))
                    .child(
                        IKey.str("Missing drone!")
                            .asWidget()
                            .setEnabledIf(w -> droneFilter.get() <= 0)
                            .alignX(0)
                            .marginBottom(4)));
        column.child(outputListWidget);
        column.child(
            new Column().widthRel(1)
                .height(18 * 4)
                .child(
                    new TextFieldWidgetWithOverlay(() -> distance.get() > 0).size(60, 9)
                        .marginBottom(4)
                        .alignX(0)
                        .overlay(new StringKeyCustom("Distance..."))
                        .value(distanceSyncer)
                        .setDefaultNumber(0)
                        .setNumbers(0, Integer.MAX_VALUE))
                .child(
                    new TextFieldWidgetWithOverlay(() -> moduleTier.get() > 0).size(60, 9)
                        .marginBottom(4)
                        .alignX(0)
                        .overlay(new StringKeyCustom("Tier..."))
                        .value(moduleTierSyncer)
                        .setDefaultNumber(0)
                        .setNumbers(0, 3))
                .child(
                    new Row().widthRel(1)
                        .height(18)
                        .child(
                            new SlotLikeButtonWidget(
                                () -> droneSyncer.getValue() >= 0 ? MINING_DRONES[droneSyncer.getValue()] : null)
                                    .size(18, 18)
                                    .marginBottom(4)
                                    .alignX(0)
                                    .onMousePressed(mouseData -> {
                                        if (!droneSelectorPanel.isPanelOpen()) {
                                            droneSelectorPanel.openPanel();
                                        } else {
                                            droneSelectorPanel.closePanel();
                                        }
                                        return true;
                                    })
                                    .align(Alignment.CenterLeft))
                        .child(
                            new ButtonWidget<>().size(18, 18)
                                .overlay(
                                    nerdTexture.asIcon()
                                        .size(16, 16))
                                .tooltipBuilder(t -> t.addLine(IKey.str("Calculate")))
                                .align(Alignment.CenterRight)
                                .onMousePressed(mouseData -> {
                                    List<IWidget> output = calculateOutput(
                                        distanceSyncer.getValue(),
                                        moduleTierSyncer.getValue(),
                                        droneSyncer.getValue(),
                                        asteroidPanels);
                                    outputListWidget.getChildren()
                                        .clear();
                                    outputListWidget.children(output);
                                    WidgetTree.resize(outputListWidget);
                                    return true;
                                }))));
        return panel.child(column);
    }

    private List<IWidget> calculateOutput(Integer distance, Integer moduleTier, Integer droneTier,
        List<IPanelHandler> asteroidPanels) {
        List<IWidget> listResult = new ArrayList<>();

        List<Pair<Integer, GTRecipe>> asteroids = SpaceMiningRecipes.asteroidDistanceMap.get(droneTier)
            .computeIfAbsent(distance, w -> new ArrayList<>())
            .stream()
            .filter(pair -> {
                GTRecipe recipe = pair.second();
                Integer requiredModuleTier = recipe.getMetadata(IGRecipeMaps.MODULE_TIER);
                assert requiredModuleTier != null;
                if (moduleTier < requiredModuleTier) return false;
                return true;
            })
            .sorted(
                (a, b) -> b.second()
                    .getMetadata(IGRecipeMaps.SPACE_MINING_DATA).recipeWeight
                    - a.second()
                        .getMetadata(IGRecipeMaps.SPACE_MINING_DATA).recipeWeight)
            .collect(toList());

        AtomicInteger weightSum = new AtomicInteger();
        asteroids.forEach(
            asteroid -> weightSum.addAndGet(
                asteroid.second()
                    .getMetadata(IGRecipeMaps.SPACE_MINING_DATA).recipeWeight));

        for (Pair<Integer, GTRecipe> asteroidPair : asteroids) {
            GTRecipe asteroid = asteroidPair.second();
            SpaceMiningData data = asteroid.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
            ButtonWidget asteroidButton = new ButtonWidget<>().size(18, 18)
                .overlay(new DynamicDrawable(() -> {
                    // Temporary solution for jank item rendering TODO: REMOVE THIS WHEN IT'S FIXED!
                    if (asteroidPanels.stream()
                        .anyMatch(IPanelHandler::isPanelOpen)) return null;
                    return new ItemDrawable(asteroid.mOutputs[0]).asIcon()
                        .size(16, 16);
                }))
                .tooltipBuilder(
                    t -> t.addLine(IKey.str(EnumChatFormatting.DARK_RED + data.getAsteroidNameLocalized()))
                        .addLine(IKey.str("Click me to get more info!")))
                .onMousePressed(mouseData -> {
                    asteroidPanels.get(asteroidPair.first())
                        .openPanel();
                    return true;
                });
            listResult.add(
                new Row().widthRel(1)
                    .height(18)
                    .child(
                        IKey.str(
                            String.format("%.3f", ((double) data.recipeWeight / weightSum.get() * 100))
                                + "% Chance for: ")
                            .asWidget()
                            .marginRight(4))
                    .child(asteroidButton));
        }
        return listResult;
    }

    private ModularPanel opendroneSelectorPanel(PanelSyncManager syncManager, IPanelHandler syncHandler,
        IntSyncValue syncer, String suffix, ModularPanel parent) {
        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("droneSelectorPanel" + suffix) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.size(18 * 5 + 6, 6 + 18 * (MINING_DRONES.length / 5 + 1))
            .pos(parentArea.x + 2, parentArea.y + parentArea.height - 36 - (6 + 18 * (MINING_DRONES.length / 5 + 1)))
            .padding(3);
        Grid grid = new Grid();
        List<List<IWidget>> drones = new ArrayList<>();
        drones.add(new ArrayList<>());
        drones.get(0)
            .add(new SlotLikeButtonWidget(() -> null).onMousePressed(mouseData -> {
                syncer.setValue(-1);
                syncHandler.closePanel();
                return true;
            }));

        int row = 0;
        for (int i = 0; i < MINING_DRONES.length; i++) {
            ItemStack drone = MINING_DRONES[i];
            int finalI = i;
            drones.get(row)
                .add(new SlotLikeButtonWidget(drone).onMousePressed(mouseData -> {
                    syncer.setValue(finalI);
                    syncHandler.closePanel();
                    return true;
                }));
            if (drones.get(row)
                .size() % 5 == 0 || i == MINING_DRONES.length - 1) {
                row++;
                drones.add(new ArrayList<>());
            }
        }
        grid.matrix(drones);

        return panel.child(grid.sizeRel(1));
    }

    private ModularPanel getAsteroidPanel(PanelSyncManager pSyncManager, IPanelHandler syncHandler, int asteroidIndex) {
        ModularPanel panel = new ModularPanel("asteroidInformationPanel" + asteroidIndex) {

            @Override
            public boolean isDraggable() {
                return true;
            }
        };
        AsteroidData data = SpaceMiningRecipes.uniqueAsteroidList.get(asteroidIndex);

        int outputLength = data.output != null ? data.output.length : data.outputItems.length;
        panel.size(250, 100 + 22 * (((outputLength - 1) / 9) + ((data.maxDroneTier - data.minDroneTier - 1) / 10) + 1))
            .pos(140, 30)
            .padding(5);
        Flow column = new Column().sizeRel(1);

        // Ore Icon, Asteroid Name
        ItemStack oreItem = data.outputItems != null ? data.outputItems[0]
            : GTOreDictUnificator.get(data.orePrefixes, data.output[0], 1);
        column.child(
            new Row().widthRel(1)
                .height(18)
                .child(
                    new ItemDrawable(oreItem).asWidget()
                        .marginRight(5))
                .child(
                    IKey.str(EnumChatFormatting.DARK_RED + data.getAsteroidNameLocalized())
                        .asWidget())
                .marginBottom(4));
        // Can be mined by: X-Y Mining Drones
        Flow miningDroneRow = new Row().widthRel(1)
            .coverChildrenHeight();
        miningDroneRow.child(
            IKey.str("Can be mined by: ")
                .asWidget()
                .topRel(0, 9, 0));

        Flow droneDrawables = new Column().coverChildren()
            .marginBottom(4);

        Flow droneRow = new Row().widthRel(1)
            .height(18)
            .marginBottom(4);
        for (int i = data.minDroneTier; i < data.maxDroneTier; i++) {
            ItemStack droneItem = MINING_DRONES[i];
            ItemStack droneRodItem = MINING_RODS[i];
            ItemStack droneDrillItem = MINING_DRILLS[i];
            int finalI = i;
            droneRow.child(
                new ItemDrawable(droneItem).asWidget()
                    .tooltipBuilder(
                        t -> t.addLine(IKey.str(droneItem.getDisplayName()))
                            .add(IKey.str("Uses 4 "))
                            .add(new ItemDrawable(droneRodItem))
                            .add(IKey.str(" And 4"))
                            .add(new ItemDrawable(droneDrillItem))
                            .add(IKey.str(" Per parallel\n"))
                            .addLine(
                                IKey.str(
                                    "Asteroid size with this drone: "
                                        + (data.minSize + Math.pow(2, finalI - data.minDroneTier) - 1)
                                        + "-"
                                        + (data.maxSize + Math.pow(2, finalI - data.minDroneTier) - 1)))));
            if ((i - data.minDroneTier + 1) % 10 == 0 || i == data.maxDroneTier - 1) {
                droneDrawables.child(droneRow);
                droneRow = new Row().widthRel(1)
                    .height(18)
                    .marginBottom(4);
            }
        }
        miningDroneRow.child(droneDrawables);
        column.child(miningDroneRow);
        // Distance Information
        column.child(
            new SingleChildWidget<>().widthRel(1)
                .height(9)
                .child(
                    IKey.str(
                        "Found at: " + EnumChatFormatting.GREEN
                            + data.minDistance
                            + "-"
                            + data.maxDistance
                            + EnumChatFormatting.RESET
                            + " distance")
                        .asWidget())
                .marginBottom(4))
            // Computation
            .child(
                new SingleChildWidget<>().widthRel(1)
                    .height(9)
                    .child(
                        IKey.str(
                            "Requires " + EnumChatFormatting.BLUE + data.computation + " computation/s per parallel")
                            .asWidget())
                    .marginBottom(4))
            // Module tier
            .child(
                new SingleChildWidget<>().widthRel(1)
                    .height(9)
                    .child(
                        IKey.str("Requires at least tier " + data.requiredModuleTier + " Space Mining module")
                            .asWidget())
                    .marginBottom(4));

        // Drops
        Flow drops = new Row().coverChildrenHeight();

        Flow dropsColumns = new Column().widthRel(1)
            .height(18)
            .marginBottom(4);

        Flow dropRow = new Row().widthRel(1)
            .height(18)
            .marginBottom(4);
        drops.child(
            IKey.str("Drops: ")
                .asWidget());
        int totalWeight = Arrays.stream(data.chances)
            .sum();
        for (int i = 0; i < outputLength; i++) {
            ItemStack ore = data.outputItems != null ? data.outputItems[i]
                : GTOreDictUnificator.get(data.orePrefixes, data.output[i], 1);
            int finalI = i;
            dropRow.child(
                new SlotLikeButtonWidget(ore).tooltipBuilder(
                    t -> t.addLine(IKey.str(ore.getDisplayName()))
                        .addLine(IKey.str(((double) data.chances[finalI] / totalWeight) * 100 + "% chance")))
                    .marginRight(5));

            if ((i + 1) % 9 == 0 || i == outputLength - 1) {
                dropsColumns.child(dropRow);
                dropRow = new Row().widthRel(1)
                    .height(18)
                    .marginBottom(4);
            }
        }
        drops.child(dropsColumns);
        column.child(drops);
        return panel.child(column);
    }

    private ModularPanel getAsteroidPanelFromRecipe(PanelSyncManager pSyncManager, IPanelHandler syncHandler,
        GTRecipe asteroid, Integer droneTier) {
        ModularPanel panel = new ModularPanel("asteroidFromRecipe" + asteroid.hashCode()) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        };
        SpaceMiningData data = asteroid.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
        Integer moduleTier = asteroid.getMetadata(IGRecipeMaps.MODULE_TIER);
        panel.size(240, 100 + 22 * (((asteroid.mOutputs.length - 1) / 9) + 1))
            .pos(140, 30)
            .padding(5);
        Flow column = new Column().sizeRel(1);

        // Ore Icon, Asteroid Name
        column.child(
            new Row().widthRel(1)
                .height(18)
                .child(
                    new ItemDrawable(asteroid.mOutputs[0]).asWidget()
                        .marginRight(5))
                .child(
                    IKey.str(EnumChatFormatting.DARK_RED + data.getAsteroidNameLocalized())
                        .asWidget())
                .marginBottom(4));
        // Size data for this drone
        ItemStack droneItem = MINING_DRONES[droneTier];

        column.child(
            new RichTextWidget().textBuilder(
                t -> t.add("Size for ")
                    .add(new ItemDrawable(droneItem))
                    .add(": " + data.minSize + "-" + data.maxSize))
                .topRel(0, 9, 0));

        // Distance Information
        column.child(
            new SingleChildWidget<>().widthRel(1)
                .height(9)
                .child(
                    IKey.str(
                        "Found at: " + EnumChatFormatting.GREEN
                            + data.minDistance
                            + "-"
                            + data.maxDistance
                            + EnumChatFormatting.RESET
                            + " distance")
                        .asWidget())
                .marginBottom(4))
            // Computation
            .child(
                new SingleChildWidget<>().widthRel(1)
                    .height(9)
                    .child(
                        IKey.str(
                            "Requires " + EnumChatFormatting.BLUE + data.computation + " computation/s per parallel")
                            .asWidget())
                    .marginBottom(4))
            // Module tier
            .child(
                new SingleChildWidget<>().widthRel(1)
                    .height(9)
                    .child(
                        IKey.str("Requires at least tier " + moduleTier + " Space Mining module")
                            .asWidget())
                    .marginBottom(4));

        // Drops
        Flow drops = new Row().coverChildrenHeight();

        Flow dropsColumns = new Column().widthRel(1)
            .height(18)
            .marginBottom(4);

        Flow dropRow = new Row().widthRel(1)
            .height(18)
            .marginBottom(4);
        drops.child(
            IKey.str("Drops: ")
                .asWidget());
        int totalWeight = Arrays.stream(asteroid.mChances)
            .sum();
        for (int i = 0; i < asteroid.mOutputs.length; i++) {
            ItemStack ore = asteroid.mOutputs[i];
            int finalI = i;
            dropRow.child(
                new SlotLikeButtonWidget(ore).tooltipBuilder(
                    t -> t.addLine(IKey.str(ore.getDisplayName()))
                        .addLine(IKey.str(((double) asteroid.mChances[finalI] / totalWeight) * 100 + "% chance")))
                    .marginRight(5));

            if ((i + 1) % 9 == 0 || i == asteroid.mOutputs.length - 1) {
                dropsColumns.child(dropRow);
                dropRow = new Row().widthRel(1)
                    .height(18)
                    .marginBottom(4);
            }
        }
        drops.child(dropsColumns);
        column.child(drops);
        return panel.child(column);
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
            tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc0")) // Module
                                                                                                       // that
                // adds Space
                // Mining
                // Operations to the
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t1.desc1")) // Does
                // this
                // violate
                // drone rights?
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc3"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc4"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc5.1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t1.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc6"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addOtherStructurePart(
                    GCCoreUtil.translate("ig.elevator.structure.OpticalConnector"),
                    GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"),
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
            tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc0")) // Module
                                                                                                       // that
                // adds Space
                // Mining
                // Operations to the
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t2.desc1")) // This
                // definitely
                // violates
                // drone rights.
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc3"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc4"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc5.1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t2.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc6"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addOtherStructurePart(
                    GCCoreUtil.translate("ig.elevator.structure.OpticalConnector"),
                    GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"),
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
            tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc0")) // Module
                                                                                                       // that
                // adds Space
                // Mining
                // Operations to the
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t3.desc1")) // Great
                // treasures
                // beyond
                // your imagination await!
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc3"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc4"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc5.1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t3.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT3"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc6"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addOtherStructurePart(
                    GCCoreUtil.translate("ig.elevator.structure.OpticalConnector"),
                    GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"),
                    1)
                .toolTipFinisher();
            return tt;
        }
    }
}
