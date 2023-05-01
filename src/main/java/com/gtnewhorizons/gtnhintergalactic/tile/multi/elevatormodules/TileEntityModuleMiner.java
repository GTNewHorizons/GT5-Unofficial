package com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.gui.IG_UITextures;
import com.gtnewhorizons.gtnhintergalactic.recipe.IG_Recipe;
import com.gtnewhorizons.gtnhintergalactic.recipe.IG_RecipeAdder;
import com.gtnewhorizons.gtnhintergalactic.spaceprojects.ProjectAsteroidOutpost;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.*;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.enums.SolarSystem;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.common.power.BasicMachineEUPower;
import gregtech.common.power.Power;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

public abstract class TileEntityModuleMiner extends TileEntityModuleBase {

    /** Base chance to get a bonus stack from space mining, will be multiplied with other factors */
    protected static int BONUS_STACK_BASE_CHANCE = 5000;
    /** Max chance to get a bonus stack from space mining */
    protected static int BONUS_STACK_MAX_CHANCE = 7500;

    /** Minimal factor used to calculate the recipe time */
    protected static final double MIN_RECIPE_TIME_MODIFIER = 0.5D;

    // Tiered plasmas, the mining operation uses one of them. Using higher tier plasmas boosts the mining operation
    /** Usage of helium plasma per mining operation */
    protected static int PLASMA_HELIUM_USAGE = 1000;
    /** Usage of bismuth plasma per mining operation */
    protected static int PLASMA_BISMUTH_USAGE = 500;
    /** Usage of radon plasma per mining operation */
    protected static int PLASMA_RADON_USAGE = 300;

    /* Size of the whitelist in stacks **/
    protected static int WHITELIST_SIZE = 64;
    /** ID of the whitelist config window */
    protected static int WHITELIST_WINDOW_ID = 200;

    /** String of the NBT tag that saves if whitelist mode is enabled */
    protected static String IS_WHITELISTED_NBT_TAG = "isWhitelisted";
    /** String of the NBT tag that saves the whitelist */
    protected static String WHITELIST_NBT_TAG = "whitelist";

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
            .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 200, 300);
    /** Name of the parallel setting */
    private static final INameFunction<TileEntityModuleMiner> PARALLEL_SETTING_NAME = (base, p) -> GCCoreUtil
            .translate("gt.blockmachines.multimachine.project.ig.miner.cfgi.1"); // Max parallels
    /** Status of the parallel setting */
    private static final IStatusFunction<TileEntityModuleMiner> PARALLEL_STATUS = (base, p) -> LedStatus
            .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 100, base.getParallels());
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
    protected final Power power;

    /** Asteroid outpost that the player can additionally build */
    protected ProjectAsteroidOutpost asteroidOutpost;

    /** Flag if the module has a whitelist to generate ores, else it will use a blacklist */
    protected boolean isWhitelisted = false;
    /** List for ore generation. Can either be a white- or blacklist */
    protected HashSet<String> configuredOres;
    /** Handler that holds the visual whitelist */
    protected ItemStackHandler whiteListHandler = new ItemStackHandler(WHITELIST_SIZE);

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
        power = new MinerPower((byte) tTier, tModuleTier);
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
        power = new MinerPower((byte) tTier, tModuleTier);
    }

    /**
     * @return Power object used for displaying in NEI
     */
    @Override
    public Power getPower() {
        return power;
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

    /**
     * Check if any recipe can be started with the given inputs
     *
     * @param aStack Item stack that is placed in the controller GUI
     * @return True if a recipe could be started, else false
     */
    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        if (gregtech.api.enums.GT_Values.V[tTier] * (long) parallelSetting.get() > getEUVar()) {
            return false;
        }

        lEUt = 0;
        eAmpereFlow = 0;
        eRequiredData = 0;
        mEfficiencyIncrease = 0;
        mPollution = 0;
        mOutputItems = null;
        mOutputFluids = null;
        if (getStoredFluids().size() <= 0) {
            return false;
        }

        // Look for a valid plasma to start a mining operation
        for (FluidStack fluidStack : getStoredFluids()) {
            int availablePlasmaTier = getTierFromPlasma(fluidStack);
            if (availablePlasmaTier > 0) {
                // Check if valid inputs for a mining operation are present
                if (process(
                        getStoredInputs().toArray(new ItemStack[0]),
                        getStoredFluids().toArray(new FluidStack[0]),
                        availablePlasmaTier,
                        fluidStack,
                        getParallels(fluidStack, getPlasmaUsageFromTier(availablePlasmaTier)))) {
                    cycleDistance();
                    return true;
                }
            }
        }
        cycleDistance();
        return false;
    }

    /**
     * Try to process the input resources
     *
     * @param inputs      Item inputs
     * @param fluidInputs Fluid inputs
     * @return Multiblock control structure that contains all process data or null if nothing can be processed
     */
    public boolean process(ItemStack[] inputs, FluidStack[] fluidInputs, int availablePlasmaTier, FluidStack plasma,
            int maxParallels) {
        // Check inputs
        if ((inputs == null && fluidInputs == null) || plasma == null) {
            return false;
        }
        if (availablePlasmaTier <= 0) {
            return false;
        }

        // Get all asteroid pools that this drone can pull from
        long tVoltage = getMaxInputVoltage();
        List<IG_Recipe.IG_SpaceMiningRecipe> recipes = IG_RecipeAdder.instance.sSpaceMiningRecipes.findRecipes(
                getBaseMetaTileEntity(),
                null,
                false,
                false,
                tVoltage,
                fluidInputs,
                null,
                (int) distanceDisplay.get(),
                tModuleTier,
                inputs);

        // Return if no recipe was found
        if (recipes == null || recipes.size() <= 0) {
            return false;
        }

        float compModifier = 1f;
        float plasmaModifier = 1f;

        if (asteroidOutpost != null) {
            compModifier -= asteroidOutpost.getComputationDiscount();
            plasmaModifier -= asteroidOutpost.getPlasmaDiscount();
        }

        // Get a recipe randomly with weight from the pool
        int totalWeight = recipes.stream().mapToInt(IG_Recipe.IG_SpaceMiningRecipe::getRecipeWeight).sum();
        int recipeIndex = 0;
        for (double r = Math.random() * totalWeight; recipeIndex < recipes.size() - 1; ++recipeIndex) {
            r -= recipes.get(recipeIndex).getRecipeWeight();
            if (r <= 0.0) break;
        }
        IG_Recipe.IG_SpaceMiningRecipe tRecipe = recipes.get(recipeIndex);

        // Make sure recipe really exists and we have enough power
        if (tRecipe == null || tRecipe.mEUt > tVoltage) {
            return false;
        }

        // Limit parallels by available computation, return if not enough computation is available
        maxParallels = (int) Math.min(maxParallels, getAvailableData_EM() / (tRecipe.computation * compModifier));
        if (maxParallels <= 0) {
            return false;
        }

        // Check how many parallels we can actually do, return if none
        GT_ParallelHelper helper = new GT_ParallelHelper().setMaxParallel(maxParallels).setRecipe(tRecipe)
                .setFluidInputs(fluidInputs).setItemInputs(inputs).setAvailableEUt(GT_Values.V[tTier])
                .enableConsumption().build();
        int parallels = helper.getCurrentParallel();
        if (parallels <= 0) {
            return false;
        }

        // Randomly generate ore stacks with the given chances, ores and size
        ItemStack[] outputs = new ItemStack[tRecipe.maxSize * parallels];
        int totalChance = Arrays.stream(tRecipe.mChances).sum();
        try {
            for (int i = 0; i < tRecipe.maxSize * parallels; i++) {
                int bonusStackChance = 0;
                if (i >= tRecipe.minSize * parallels) {
                    bonusStackChance = getBonusStackChance(availablePlasmaTier);
                }
                if (i < tRecipe.minSize * parallels || bonusStackChance > XSTR.XSTR_INSTANCE.nextInt(10000)) {
                    int random = XSTR.XSTR_INSTANCE.nextInt(totalChance);
                    int currentChance = 0;
                    for (int j = 0; j < tRecipe.mChances.length; j++) {
                        currentChance += tRecipe.mChances[j];
                        if (random <= currentChance) {
                            ItemStack generatedOre = tRecipe.mOutputs[j];
                            if (configuredOres == null || configuredOres.isEmpty()
                                    || isWhitelisted == configuredOres.contains(getOreString(generatedOre))) {
                                outputs[i] = generatedOre.copy();
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            return false;
        }

        plasma.amount = (int) Math.max(
                0,
                Math.ceil(plasma.amount - parallels * getPlasmaUsageFromTier(availablePlasmaTier) * plasmaModifier));

        // Assign recipe parameters
        mOutputItems = outputs;
        lEUt = (long) -tRecipe.mEUt * parallels;
        eAmpereFlow = 1;
        // TODO: Implement way to get computation from master controller. Or maybe keep it this way so
        // people can route computation to their liking?
        eRequiredData = (int) Math.ceil(tRecipe.computation * parallels * compModifier);
        mMaxProgresstime = getRecipeTime(tRecipe.mDuration, availablePlasmaTier);
        mEfficiencyIncrease = 10000;
        return true;
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
        if (fluidStack.isFluidEqual(Materials.Radon.getPlasma(1)) && fluidStack.amount >= PLASMA_RADON_USAGE) {
            return 3;
        } else
            if (fluidStack.isFluidEqual(Materials.Bismuth.getPlasma(1)) && fluidStack.amount >= PLASMA_BISMUTH_USAGE) {
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
        switch (plasmaTier) {
            case 1:
                return PLASMA_HELIUM_USAGE;
            case 2:
                return PLASMA_BISMUTH_USAGE;
            case 3:
                return PLASMA_RADON_USAGE;
            default:
                return 0;
        }
    }

    /**
     * Get the chance for additional stacks in Space Mining
     *
     * @param plasmaTier Available plasma tier
     * @return Chance for the bonus stack
     */
    protected int getBonusStackChance(int plasmaTier) {
        if (plasmaTier <= 0 || plasmaTier > 3) {
            return 0;
        }
        // Base chance is 50% + 10% for every plasma tier above T1. The whole chance is multiplied by 2 - overdrive
        // setting
        return Math.min(
                (int) ((double) (BONUS_STACK_BASE_CHANCE + 1000 * (plasmaTier - 1)) * (2.0D - overdriveSetting.get())),
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
        if (oreStack.getUnlocalizedName().startsWith("gt.blockores")) {
            return oreStack.getItem().getUnlocalizedName() + ":" + oreStack.getItemDamage() % 1000;
        } else {
            return oreStack.getItem().getUnlocalizedName() + ":" + oreStack.getItemDamage();
        }
    }

    /**
     * Get the number of parallels that this module can handle
     *
     * @return Number of possible parallels
     */
    protected abstract int getParallels();

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
        return Math.min((int) parallelSetting.get(), (int) (plasma.amount / (plasmaUsage * plasmaModifier)));
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
            if (distanceDisplay.get() + stepSetting.get() < distanceSetting.get() + rangeSetting.get()) {
                distanceDisplay.set(distanceDisplay.get() + stepSetting.get());
            } else {
                distanceDisplay.set(distanceSetting.get() - rangeSetting.get());
            }
        } else {
            distanceDisplay.set(distanceSetting.get());
        }
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
        parallelSetting = hatch_0.makeInParameter(1, getParallels(), PARALLEL_SETTING_NAME, PARALLEL_STATUS);
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
                widget.getContext().openSyncedWindow(WHITELIST_WINDOW_ID);
            }
        }).setPlayClickSound(false)
                .setBackground(TecTechUITextures.BUTTON_STANDARD_16x16, IG_UITextures.OVERLAY_BUTTON_OPTIONS)
                .setPos(174, 132).setSize(16, 16);
        button.addTooltip("Configure Filter").setTooltipShowUpDelay(TOOLTIP_DELAY);
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
        return ModularWindow.builder(158, 180).setBackground(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
                .setGuiTint(getGUIColorization())
                // Toggle white-/blacklist
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                    isWhitelisted = !isWhitelisted;
                    if (!widget.isClient()) {
                        generateOreConfigurationList();
                    }
                }).setPlayClickSound(false).setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
                    if (isWhitelisted) {
                        ret.add(IG_UITextures.OVERLAY_BUTTON_WHITELIST);
                    } else {
                        ret.add(IG_UITextures.OVERLAY_BUTTON_BLACKLIST);
                    }
                    return ret.toArray(new IDrawable[0]);
                }).setPos(7, 9).setSize(16, 16).addTooltip("Mode").setTooltipShowUpDelay(TOOLTIP_DELAY))
                // Clear list
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                    if (!widget.isClient()) {
                        if (whiteListHandler != null) {
                            for (int i = 0; i < whiteListHandler.getSlots(); i++) {
                                whiteListHandler.setStackInSlot(i, null);
                            }
                        }
                        generateOreConfigurationList();
                    }
                }).setPlayClickSound(false)
                        .setBackground(TecTechUITextures.BUTTON_STANDARD_16x16, IG_UITextures.OVERLAY_BUTTON_CROSS)
                        .setPos(25, 9).setSize(16, 16).addTooltip("Clear").setTooltipShowUpDelay(TOOLTIP_DELAY))
                // Configure from bus
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                    if (!widget.isClient()) {
                        int i = 0;
                        for (ItemStack itemStack : getStoredInputs()) {
                            if (i < WHITELIST_SIZE) {
                                ItemStack copy = itemStack.copy();
                                copy.stackSize = 1;
                                whiteListHandler.setStackInSlot(i++, copy);
                            }
                        }
                        generateOreConfigurationList();
                    }
                }).setPlayClickSound(false)
                        .setBackground(TecTechUITextures.BUTTON_STANDARD_16x16, IG_UITextures.OVERLAY_BUTTON_CONFIGURE)
                        .setPos(43, 9).setSize(16, 16).addTooltip("Load from Bus").setTooltipShowUpDelay(TOOLTIP_DELAY))
                // List
                .widget(
                        SlotGroup.ofItemHandler(whiteListHandler, 8).startFromSlot(0).endAtSlot(WHITELIST_SIZE - 1)
                                .phantom(true).background(getGUITextureSet().getItemSlot()).build().setPos(7, 27))
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

        screenElements.widget(
                TextWidget
                        .dynamicString(
                                () -> StatCollector
                                        .translateToLocal("gt.blockmachines.multimachine.project.ig.miner.cfgi.4")
                                        + ": "
                                        + (((int) modeSetting.get() == 0)
                                                ? StatCollector.translateToLocal(
                                                        "gt.blockmachines.multimachine.project.ig.miner.cfgi.4.1")
                                                : StatCollector.translateToLocal(
                                                        "gt.blockmachines.multimachine.project.ig.miner.cfgi.4.2")))
                        .setSynced(false).setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        new FakeSyncWidget.IntegerSyncer(
                                () -> (int) modeSetting.get(),
                                val -> parametrization.trySetParameters(
                                        modeSetting.id % 10,
                                        modeSetting.id / 10,
                                        modeSetting.get())));
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!super.checkMachine_EM(aBaseMetaTileEntity, aStack)) {
            return false;
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
     * Power object used to display the miners in NEI
     */
    private static class MinerPower extends BasicMachineEUPower {

        /**
         * Create a new power object for mining modules
         *
         * @param tier       Voltage tier of the miner
         * @param moduleTier Module tier of the miner
         */
        public MinerPower(byte tier, int moduleTier) {
            super(tier, 1, moduleTier);
        }

        /**
         * Calculate the power usage for a given recipe with the specific miner tier
         *
         * @param euPerTick EU consumption of the recipe
         * @param duration  Duration of the recipe
         */
        @Override
        public void computePowerUsageAndDuration(int euPerTick, int duration) {
            originalVoltage = computeVoltageForEuRate(euPerTick);
            recipeEuPerTick = euPerTick;
            recipeDuration = duration;
        }

        /**
         * @return Tiered string which will be displayed, if this module tier is selected
         */
        @Override
        public String getTierString() {
            return GT_Values.TIER_COLORS[tier] + "MK " + specialValue + EnumChatFormatting.RESET;
        }
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
        protected int getParallels() {
            return MAXIMUM_PARALLELS;
        }

        /**
         * Create the tooltip of this controller
         *
         * @return Tooltip builder
         */
        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t1.desc5"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT1"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc6"))
                    .addSeparator().beginStructureBlock(1, 5, 2, false)
                    .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                    .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
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
        protected int getParallels() {
            return MAXIMUM_PARALLELS;
        }

        /**
         * Create the tooltip of this controller
         *
         * @return Tooltip builder
         */
        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t2.desc5"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT2"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc6"))
                    .addSeparator().beginStructureBlock(1, 5, 2, false)
                    .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                    .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
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
        protected int getParallels() {
            return MAXIMUM_PARALLELS;
        }

        /**
         * Create the tooltip of this controller
         *
         * @return Tooltip builder
         */
        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t3.desc5"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT3"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.desc6"))
                    .addSeparator().beginStructureBlock(1, 5, 2, false)
                    .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                    .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
            return tt;
        }
    }
}
