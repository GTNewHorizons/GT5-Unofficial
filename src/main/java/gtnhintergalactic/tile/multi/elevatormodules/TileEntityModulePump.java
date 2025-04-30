package gtnhintergalactic.tile.multi.elevatormodules;

import static gtnhintergalactic.recipe.SpacePumpingRecipes.RECIPES;
import static tectech.Reference.MODID;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableArray;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.TabTexture;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.FluidSlot;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.common.tileentities.machines.MTEHatchOutputME;
import gtneioreplugin.plugin.block.ModBlocks;
import gtnhintergalactic.recipe.SpacePumpingRecipes;
import gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameter;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

/**
 * Space Pump project module of the Space Elevator
 *
 * @author minecraft7771
 */
public abstract class TileEntityModulePump extends TileEntityModuleBase {

    /** Energy consumption of the module (1A UHV) */
    public static final long ENERGY_CONSUMPTION = (int) GTValues.VP[9];

    /** Input parameters */
    Parameter.IntegerParameter fluidIndexParameter;
    Parameters.Group.ParameterIn[] parallelSettings;

    Parameters.Group.ParameterIn[] gasTypeSettings;
    Parameters.Group.ParameterIn[] planetTypeSettings;
    Parameters.Group.ParameterIn batchSetting;

    /** Name of the planet type setting */
    private static final INameFunction<TileEntityModulePump> PLANET_TYPE_SETTING_NAME = (base,
        p) -> GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.cfgi.0") + " "
            + (p.hatchId() / 2 + 1); // Planet Type
    /** Status of the planet type setting */
    private static final IStatusFunction<TileEntityModulePump> PLANET_TYPE_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 100, 100);
    /** Name of the gas type setting */
    private static final INameFunction<TileEntityModulePump> GAS_TYPE_SETTING_NAME = (base,
        p) -> GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.cfgi.1") + " "
            + (p.hatchId() / 2 + 1); // Gas Type
    /** Status of the gas type setting */
    private static final IStatusFunction<TileEntityModulePump> GAS_TYPE_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 100, 100);
    /** Name of the parallel setting */
    private static final INameFunction<TileEntityModulePump> PARALLEL_SETTING_NAME = (base,
        p) -> GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.cfgi.2") + " "
            + (p.hatchId() / 2 + 1); // Parallels
    /** Status of the parallel setting */
    private static final IStatusFunction<TileEntityModulePump> PARALLEL_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 100, base.getParallels());
    /** Name of the batch setting */
    private static final INameFunction<TileEntityModulePump> BATCH_SETTING_NAME = (base, p) -> GCCoreUtil
        .translate("gt.blockmachines.multimachine.project.ig.pump.cfgi.3"); // Batch size
    /** Status of the batch setting */
    private static final IStatusFunction<TileEntityModulePump> BATCH_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 32, 128);

    /** Flag if this machine has an ME output hatch, will be updated in the structure check */
    protected boolean hasMeOutputHatch = false;

    /**
     * Create new Space Pump module
     *
     * @param aID           ID of the module
     * @param aName         Name of the module
     * @param aNameRegional Localized name of the module
     * @param tTier         Voltage tier of the module
     * @param tModuleTier   Tier of the module
     * @param tMinMotorTier Minimum needed motor tier
     */
    public TileEntityModulePump(int aID, String aName, String aNameRegional, int tTier, int tModuleTier,
        int tMinMotorTier) {
        super(aID, aName, aNameRegional, tTier, tModuleTier, tMinMotorTier);
    }

    /**
     * Create new Space Pump module
     *
     * @param aName         Name of the module
     * @param tTier         Voltage tier of the module
     * @param tModuleTier   Tier of the module
     * @param tMinMotorTier Minimum needed motor tier
     */
    public TileEntityModulePump(String aName, int tTier, int tModuleTier, int tMinMotorTier) {
        super(aName, tTier, tModuleTier, tMinMotorTier);
    }

    /**
     * Check if any recipe can be started with the given inputs
     *
     * @return True if a recipe could be started, else false
     */
    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        if (ENERGY_CONSUMPTION * getParallelRecipes() * getParallels() > getEUVar()) {
            return CheckRecipeResultRegistry
                .insufficientPower(ENERGY_CONSUMPTION * getParallelRecipes() * getParallels());
        }

        ArrayList<FluidStack> outputs = new ArrayList<>();
        int usedEUt = 0;
        // We store the highest batch size as time multiplier
        int maxBatchSize = (int) Math.min(Math.max(batchSetting.get(), 1.0D), 128.0D);
        for (int i = 0; i < getParallelRecipes(); i++) {
            FluidStack fluid = SpacePumpingRecipes.RECIPES_OLD
                .get(Pair.of((int) planetTypeSettings[i].get(), (int) gasTypeSettings[i].get()));
            if (fluid != null) {
                int batchSize = (int) Math.min(Math.max(batchSetting.get(), 1.0D), 128.0D);
                MTEHatchOutput targetOutput = null;
                if (!hasMeOutputHatch && !eSafeVoid) {
                    for (MTEHatchOutput output : mOutputHatches) {
                        if (output.mFluid != null && output.mFluid.getFluid() != null
                            && output.getLockedFluidName() != null
                            && output.getLockedFluidName()
                                .equals(
                                    fluid.getFluid()
                                        .getName())
                            && output.mFluid.getFluid()
                                .equals(fluid.getFluid())) {
                            targetOutput = output;
                            break;
                        }
                    }
                }
                int parallels = Math.min((int) parallelSettings[i].get(), getParallels());
                if (targetOutput != null) {
                    int outputSpace = targetOutput.getCapacity() - targetOutput.getFluidAmount();
                    if (outputSpace < fluid.amount) {
                        continue;
                    }
                    parallels = Math.min(parallels, outputSpace / fluid.amount);
                    batchSize = Math.min(batchSize, outputSpace / (fluid.amount * parallels));
                    maxBatchSize = Math.max(maxBatchSize, batchSize);
                }
                if (parallels > 0 && batchSize > 0) {
                    fluid = fluid.copy();
                    long fluidLong = (long) fluid.amount * parallels * batchSize;
                    usedEUt += (int) (ENERGY_CONSUMPTION * parallels);
                    ParallelHelper.addFluidsLong(outputs, fluid, fluidLong);
                }
            }
        }

        lEUt = -usedEUt;
        mOutputFluids = outputs.toArray(new FluidStack[0]);
        eAmpereFlow = 1;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20 * maxBatchSize;

        return !outputs.isEmpty() ? CheckRecipeResultRegistry.SUCCESSFUL : CheckRecipeResultRegistry.NO_RECIPE;
    }

    /**
     * Check if the structure of this machine is valid
     *
     * @param aBaseMetaTileEntity This
     * @param aStack              Item stack present in the controller GUI
     * @return True if valid, else false
     */
    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        boolean state = super.checkMachine_EM(aBaseMetaTileEntity, aStack);
        hasMeOutputHatch = false;
        if (state) {
            for (MTEHatchOutput output : mOutputHatches) {
                if (output instanceof MTEHatchOutputME) {
                    hasMeOutputHatch = true;
                    break;
                }
            }
        }
        return state;
    }

    /**
     * Get the number of parallels that this module can handle
     *
     * @return Number of possible parallels
     */
    protected abstract int getParallels();

    /**
     * Get the number of parallel recipes that this module can handle
     *
     * @return Number of possible parallel recipes
     */
    protected abstract int getParallelRecipes();

    /**
     * Instantiate parameters of the controller
     */
    @Override
    protected void parametersInstantiation_EM() {
        super.parametersInstantiation_EM();
        int parallels = getParallelRecipes();
        planetTypeSettings = new Parameters.Group.ParameterIn[parallels];
        gasTypeSettings = new Parameters.Group.ParameterIn[parallels];
        parallelSettings = new Parameters.Group.ParameterIn[parallels];
        for (int i = 0; i < getParallelRecipes(); i++) {
            planetTypeSettings[i] = parametrization.getGroup(i * 2, false)
                .makeInParameter(0, 1, PLANET_TYPE_SETTING_NAME, PLANET_TYPE_STATUS);
            gasTypeSettings[i] = parametrization.getGroup(i * 2, false)
                .makeInParameter(1, 1, GAS_TYPE_SETTING_NAME, GAS_TYPE_STATUS);
            parallelSettings[i] = parametrization.getGroup(i * 2 + 1, false)
                .makeInParameter(0, getParallels(), PARALLEL_SETTING_NAME, PARALLEL_STATUS);
        }
        batchSetting = parametrization.getGroup(9, false)
            .makeInParameter(1, 1, BATCH_SETTING_NAME, BATCH_STATUS);
    }

    @Override
    protected void initParameters() {}

    @Override
    public void insertTexts(ListWidget<IWidget, ?> machineInfo, ItemStackHandler invSlot, PanelSyncManager syncManager,
        ModularPanel parentPanel) {
        super.insertTexts(machineInfo, invSlot, syncManager, parentPanel);
        Flow row = new Row().coverChildrenWidth()
            .height(24);
        for (int i = 0; i < this.getParallelRecipes(); i++) {
            int finalI1 = i;
            IPanelHandler fluidSelectorPanel = syncManager.panel(
                "fluid_selector_panel_slot" + i,
                (p_syncManager, syncHandler) -> getFluidSelectorPanel(parentPanel, p_syncManager, syncHandler, finalI1),
                true);
            syncManager.syncValue("recipeFluid" + i, new FluidSlotSyncHandler(new FluidTank(Integer.MAX_VALUE)));
            row.child(
                new Row().coverChildrenWidth()
                    .height(24)
                    .child(
                        IKey.str(EnumChatFormatting.WHITE + "Fluid " + (i + 1) + ": ")
                            .asWidget())
                    .child(new FluidSlot() {

                        @NotNull
                        @Override
                        public Result onMouseTapped(int mouseButton) {
                            fluidSelectorPanel.openPanel();
                            return Result.SUCCESS;
                        }
                    }.syncHandler("recipeFluid" + i)
                        .size(24, 24)
                        .marginRight(10)));
            if ((i + 1) % 2 == 0 || i == this.getParallelRecipes() - 1) {
                machineInfo.child(row);
                row = new Row().coverChildrenWidth()
                    .height(24);
            }
        }
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
    public void addCustomButtons(ModularPanel panel, PanelSyncManager syncManager) {
        UITexture fluidSelectorTexture = UITexture.fullImage(MODID, "gui/overlay_button/edit_parameters");
        ButtonWidget fluidSelector = new ButtonWidget();
        fluidSelector.tooltip(new RichTooltip(fluidSelector).add("Configure Fluids"))
            .overlay(fluidSelectorTexture);
        fluidSelector.pos(173, doesBindPlayerInventory() ? 109 + 18 : 133 + 18)
            .size(18, 18);

        IPanelHandler fluidSelectorPanel = syncManager.panel(
            "fluid_selector_panel",
            (p_syncManager, syncHandler) -> getFluidSelectorPanel(panel, p_syncManager, syncHandler, 0),
            true);
        fluidSelector.onMousePressed(mouseData -> {
            if (!fluidSelectorPanel.isPanelOpen()) {
                fluidSelectorPanel.openPanel();
            } else {
                fluidSelectorPanel.closePanel();
            }
            return true;
        });
        panel.child(fluidSelector);
    }

    private ModularPanel getFluidSelectorPanel(ModularPanel parent, PanelSyncManager syncManager,
        IPanelHandler thisPanel, int slot) {
        String[] planetTiers = new String[] { "De", "As", "Io", "En", "Pr", "Ha", "BC" }; // for tab icons

        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("fluid_selector_panel" + slot);
        panel.size(planetTiers.length * 24, 90);
        panel.pos(parentArea.x, parentArea.y + 30);

        AtomicReference<String> search = new AtomicReference<>(EnumChatFormatting.GRAY + "Search...");
        StringSyncValue textFieldSyncer = new StringSyncValue(search::get, search::set);

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        Flow row = new Row().topRel(0, 4, 1)
            .coverChildren();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);
        syncManager.syncValue(
            "pageSwitcher",
            new IntSyncValue(
                () -> firstTierWithFluid(
                    textFieldSyncer.getStringValue()
                        .toLowerCase()),
                val -> { if (val >= 0) pagedWidget.setPage(val); }));
        for (int i = 0; i < planetTiers.length; i++) {

            TabTexture TAB_TOP = TabTexture
                .of(UITexture.fullImage("modularui2", "gui/tab/tabs_top", true), GuiAxis.Y, false, 24, 24, 4);
            Item planetItem = ModBlocks.blocks.get(planetTiers[i])
                .getItemDropped(0, null, 0);
            int finalI = i;
            row.child(
                new PageButton(i, tabController).size(18, 18)
                    .alignY(1)
                    .tab(TAB_TOP, 0)
                    .overlay(new DynamicDrawable(() -> {
                        if (tierContainsFluid(
                            finalI,
                            textFieldSyncer.getStringValue()
                                .toLowerCase())) {
                            return new DrawableArray(
                                new Rectangle().setColor(Color.rgb(0, 255, 0))
                                    .asIcon()
                                    .size(16, 16),
                                new ItemDrawable(new ItemStack(planetItem)).asIcon());
                        } else {
                            return new ItemDrawable(new ItemStack(planetItem)).asIcon();
                        }
                    })));
            Flow fluidColumn = new Column().sizeRel(1)
                .marginTop(10)
                .marginLeft(5);
            Flow tempRow = new Row().widthRel(1)
                .height(24);

            for (int j = 0; j < RECIPES.get(i)
                .size(); j++) {
                FluidTank displayFluid = new FluidTank(Integer.MAX_VALUE);
                FluidSlotSyncHandler displayFluidSyncer = new FluidSlotSyncHandler(displayFluid);
                FluidSlot bla = new FluidSlot() {

                    @NotNull
                    @Override
                    public Result onMousePressed(int mouseButton) {
                        ((FluidSlotSyncHandler) syncManager.getModularSyncManager()
                            .getPanelSyncManager("tt_multiblock")
                            .getSyncHandler("recipeFluid" + slot + ":0")).setValue(displayFluidSyncer.getValue());
                        thisPanel.closePanel();
                        return Result.SUCCESS;
                    }
                }.syncHandler(displayFluidSyncer);
                bla.getFluidTank()
                    .fill(
                        RECIPES.get(i)
                            .get(j),
                        true);
                int finalJ = j;
                tempRow.child(
                    new SingleChildWidget<>().size(24, 24)
                        .background(new DynamicDrawable(() -> {
                            if (fluidMatchesSearchString(
                                finalI,
                                finalJ,
                                textFieldSyncer.getStringValue()
                                    .toLowerCase()))
                                return new Rectangle().setColor(Color.rgb(0, 255, 0));
                            else return null;
                        }))
                        .child(
                            bla.size(22, 22)
                                .align(Alignment.Center)));
                if ((j + 1) % 6 == 0 || j == RECIPES.get(i)
                    .size() - 1) {
                    fluidColumn.child(tempRow);
                    tempRow = new Row().widthRel(1)
                        .height(24);
                }

            }
            pagedWidget.addPage(fluidColumn);
        }
        panel.child(row)
            .child(
                new Column().sizeRel(1)
                    .child(pagedWidget.sizeRel(1, 0.6f))
                    .child(
                        new TextFieldWidget().size(80, 9)
                            .bottomRel(0, 9 + 4, 0)
                            .left(4)
                            .value(textFieldSyncer)));
        return panel;
    }

    private boolean fluidMatchesSearchString(int i, int j, String stringValue) {
        if (stringValue.isEmpty()) return false;
        FluidStack fluid = RECIPES.get(i)
            .get(j);
        return fluid.getLocalizedName()
            .toLowerCase()
            .contains(stringValue);
    }

    private boolean tierContainsFluid(int index, String target) {
        if (target.isEmpty()) return false;
        return RECIPES.get(index)
            .stream()
            .anyMatch(
                fluidStack -> fluidStack.getLocalizedName()
                    .toLowerCase()
                    .contains(target));
    }

    private int firstTierWithFluid(String target) {
        if (target.isEmpty()) return -1;
        for (int i = 0; i < RECIPES.size(); i++) {
            List<FluidStack> tierRecipes = RECIPES.get(i);
            if (tierRecipes.stream()
                .anyMatch(
                    fluid -> fluid.getLocalizedName()
                        .toLowerCase()
                        .contains(target))) {
                return i;
            }
        }
        return -1;
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
            new TextWidget(StatCollector.translateToLocal("gt.blockmachines.multimachine.ig.elevator.gui.config"))
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> mMachine));

        for (int i = 0; i < getParallelRecipes(); i++) {
            final int fluidIndex = i;
            screenElements.widget(TextWidget.dynamicString(() -> {
                String fluidName = getPumpedFluid(fluidIndex);
                if (fluidName != null) {
                    return " - " + fluidName;
                }
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> mMachine && getPumpedFluid(fluidIndex) != null))
                .widget(
                    new FakeSyncWidget.IntegerSyncer(
                        () -> (int) planetTypeSettings[fluidIndex].get(),
                        val -> parametrization.trySetParameters(
                            planetTypeSettings[fluidIndex].id % 10,
                            planetTypeSettings[fluidIndex].id / 10,
                            planetTypeSettings[fluidIndex].get())))
                .widget(
                    new FakeSyncWidget.IntegerSyncer(
                        () -> (int) planetTypeSettings[fluidIndex].get(),
                        val -> parametrization.trySetParameters(
                            gasTypeSettings[fluidIndex].id % 10,
                            gasTypeSettings[fluidIndex].id / 10,
                            gasTypeSettings[fluidIndex].get())));
        }
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
        engraving = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_SIDE_PUMP_MODULE");
        super.registerIcons(aBlockIconRegister);
    }

    /**
     * Get the output name of a recipe
     *
     * @param index Recipe index, which is needs to be between 0 and getParallelRecipes()
     * @return Name of the fluid if settings are valid, else null
     */
    private String getPumpedFluid(int index) {
        if (index < 0 || index >= getParallelRecipes()) {
            return null;
        }
        FluidStack fluid = SpacePumpingRecipes.RECIPES_OLD
            .get(Pair.of((int) planetTypeSettings[index].get(), (int) gasTypeSettings[index].get()));
        if (fluid == null) {
            return null;
        }
        return fluid.getLocalizedName();
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    public static class TileEntityModulePumpT1 extends TileEntityModulePump {

        /** Voltage tier of this module */
        protected static final int MODULE_VOLTAGE_TIER = 9;
        /** Tier of this module */
        protected static final int MODULE_TIER = 1;
        /** Minimum motor tier that is needed for this module */
        protected static final int MINIMUM_MOTOR_TIER = 2;
        /** Maximum amount of parallels of one recipe */
        protected static final int MAX_PARALLELS = 4;
        /** Maximum amount of different recipes that can be done at once */
        protected static final int MAX_PARALLEL_RECIPES = 1;

        /**
         * Create a new T1 mining module controller
         *
         * @param aID           ID of the controller
         * @param aName         Name of the controller
         * @param aNameRegional Localized name of the controller
         */
        public TileEntityModulePumpT1(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Create a new T1 mining module controller
         *
         * @param aName Name of the controller
         */
        public TileEntityModulePumpT1(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Get the number of parallels that this module can handle
         *
         * @return Number of possible parallels
         */
        protected int getParallels() {
            return MAX_PARALLELS;
        }

        /**
         * Get the number of parallel recipes that this module can handle
         *
         * @return Number of possible parallel recipes
         */
        protected int getParallelRecipes() {
            return MAX_PARALLEL_RECIPES;
        }

        /**
         * Get a new meta tile entity of this controller
         *
         * @param iGregTechTileEntity this
         * @return New meta tile entity
         */
        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TileEntityModulePumpT1(mName);
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
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc0"))
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t1.desc1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc3"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc4"))

                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t1.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT2"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher();
            return tt;
        }
    }

    public static class TileEntityModulePumpT2 extends TileEntityModulePump {

        /** Voltage tier of this module */
        protected static final int MODULE_VOLTAGE_TIER = 10;
        /** Tier of this module */
        protected static final int MODULE_TIER = 2;
        /** Minimum motor tier that is needed for this module */
        protected static final int MINIMUM_MOTOR_TIER = 3;
        /** Maximum amount of parallels of one recipe */
        protected static final int MAX_PARALLELS = 4;
        /** Maximum amount of different recipes that can be done at once */
        protected static final int MAX_PARALLEL_RECIPES = 4;

        /**
         * Create a new T2 mining module controller
         *
         * @param aID           ID of the controller
         * @param aName         Name of the controller
         * @param aNameRegional Localized name of the controller
         */
        public TileEntityModulePumpT2(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Create a new T2 mining module controller
         *
         * @param aName Name of the controller
         */
        public TileEntityModulePumpT2(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Get the number of parallels that this module can handle
         *
         * @return Number of possible parallels
         */
        protected int getParallels() {
            return MAX_PARALLELS;
        }

        /**
         * Get the number of parallel recipes that this module can handle
         *
         * @return Number of possible parallel recipes
         */
        protected int getParallelRecipes() {
            return MAX_PARALLEL_RECIPES;
        }

        /**
         * Get a new meta tile entity of this controller
         *
         * @param iGregTechTileEntity this
         * @return New meta tile entity
         */
        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TileEntityModulePumpT2(mName);
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
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc0"))
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t2.desc1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc3"))

                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc4"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t2.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t2.desc6"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT3"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher();
            return tt;
        }
    }

    public static class TileEntityModulePumpT3 extends TileEntityModulePump {

        /** Voltage tier of this module */
        protected static final int MODULE_VOLTAGE_TIER = 13;
        /** Tier of this module */
        protected static final int MODULE_TIER = 3;
        /** Minimum motor tier that is needed for this module */
        protected static final int MINIMUM_MOTOR_TIER = 4;
        /** Maximum amount of parallels of one recipe */
        protected static final int MAX_PARALLELS = 64;
        /** Maximum amount of different recipes that can be done at once */
        protected static final int MAX_PARALLEL_RECIPES = 4;

        /**
         * Create a new T3 mining module controller
         *
         * @param aID           ID of the controller
         * @param aName         Name of the controller
         * @param aNameRegional Localized name of the controller
         */
        public TileEntityModulePumpT3(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Create a new T3 mining module controller
         *
         * @param aName Name of the controller
         */
        public TileEntityModulePumpT3(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        /**
         * Get the number of parallels that this module can handle
         *
         * @return Number of possible parallels
         */
        protected int getParallels() {
            return MAX_PARALLELS;
        }

        /**
         * Get the number of parallel recipes that this module can handle
         *
         * @return Number of possible parallel recipes
         */
        protected int getParallelRecipes() {
            return MAX_PARALLEL_RECIPES;
        }

        /**
         * Get a new meta tile entity of this controller
         *
         * @param iGregTechTileEntity this
         * @return New meta tile entity
         */
        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TileEntityModulePumpT3(mName);
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
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc0"))
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t3.desc1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc3"))

                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc4"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t3.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t2.desc6"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT4"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher();
            return tt;
        }
    }
}
