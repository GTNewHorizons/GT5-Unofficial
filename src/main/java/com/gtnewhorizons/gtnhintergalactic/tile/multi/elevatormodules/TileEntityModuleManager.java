package com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;
import static net.minecraft.util.EnumChatFormatting.WHITE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.gui.IG_UITextures;
import com.gtnewhorizons.modularui.api.drawable.*;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.*;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.SpaceProjectWorldSavedData;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * Module that allows the user to manage their space projects
 *
 * @author minecraft7771
 */
public class TileEntityModuleManager extends TileEntityModuleBase {

    /** Voltage tier of this module */
    protected static final int MODULE_VOLTAGE_TIER = 12;
    /** Tier of this module */
    protected static final int MODULE_TIER = 1;
    /** Minimum motor tier that is needed for this module */
    protected static final int MINIMUM_MOTOR_TIER = 1;

    /** ID of the project child window */
    protected static final int PROJECT_WINDOW_ID = 200;

    /** ID of the popup child window */
    private static final int POP_UP_WINDOW_ID = 201;
    /** String which {@link #selectedLocation} equals, when no location is selected */
    private static final String LOCATON_UNSELECTED = "NONE";

    /** Current text of the popup */
    private String popupText = "";

    /** Project Selected for the Project Manager to add to the menu */
    private ISpaceProject selectedProject;
    /** Name of the selected project, used to sync the project to client */
    private String selectedProjectName;
    /** Project upgrade that is currently selected */
    private ISpaceProject.ISP_Upgrade selectedUpgrade;
    /** Location the project will be made at. also used for the background */
    private ISpaceBody selectedLocation;
    /** The project that project Manager will be working on */
    private ISpaceProject projectWorkingOn;

    private boolean upgradeMode = false;
    private boolean projectMode = true;
    private int locationIndex = -1;
    private List<ISpaceProject.ISP_Upgrade> upgradeFromProject;

    /**
     * Create a new T1 project manager module controller
     *
     * @param aID           ID of the controller
     * @param aName         Name of the controller
     * @param aNameRegional Localized name of the controller
     */
    public TileEntityModuleManager(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
    }

    /**
     * Create a new T1 project manager module controller
     *
     * @param aName Name of the controller
     */
    public TileEntityModuleManager(String aName) {
        super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.manager.desc0"))
                .addInfo(
                        EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                                + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.manager.desc1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT1")).addSeparator()
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
        return tt;
    }

    /**
     * Get a new meta tile entity of this controller
     *
     * @param aTileEntity this
     * @return New meta tile entity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TileEntityModuleManager(mName);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        if (projectWorkingOn == null || gregtech.api.enums.GT_Values.V[tTier] > getEUVar()) {
            return false;
        }

        GT_Recipe recipe = null;

        if (projectWorkingOn.isFinished() && projectWorkingOn.getUpgradeBeingBuilt() != null
                && !projectWorkingOn.getUpgradeBeingBuilt().isFinished()) {
            ISpaceProject.ISP_Upgrade upgrade = projectWorkingOn.getUpgradeBeingBuilt();
            recipe = new GT_Recipe(
                    false,
                    upgrade.getItemsCostPerStage(),
                    null,
                    null,
                    null,
                    upgrade.getFluidsCostPerStage(),
                    null,
                    upgrade.getUpgradeBuildTime(),
                    (int) upgrade.getVoltage(),
                    0);
        } else if (!projectWorkingOn.isFinished()) {
            recipe = new GT_Recipe(
                    false,
                    projectWorkingOn.getItemsCostPerStage(),
                    null,
                    null,
                    null,
                    projectWorkingOn.getFluidsCostPerStage(),
                    null,
                    projectWorkingOn.getProjectBuildTime(),
                    (int) projectWorkingOn.getProjectVoltage(),
                    0);
        }

        if (recipe == null) {
            return false;
        }

        if (!recipe.isRecipeInputEqual(
                true,
                getStoredFluids().toArray(new FluidStack[0]),
                getStoredInputs().toArray(new ItemStack[0]))) {
            return false;
        }

        mMaxProgresstime = projectWorkingOn.getProjectBuildTime();
        mEUt = recipe.mEUt;

        return true;
    }

    @Override
    public void outputAfterRecipe_EM() {
        super.outputAfterRecipe_EM();
        upgradeProjectOrUpgrade();
    }

    private void upgradeProjectOrUpgrade() {
        if (projectWorkingOn != null) {
            if (projectWorkingOn.isFinished() && projectWorkingOn.getUpgradeBeingBuilt() != null
                    && !projectWorkingOn.getUpgradeBeingBuilt().isFinished()) {
                projectWorkingOn.getUpgradeBeingBuilt().goToNextStage();
            } else if (!projectWorkingOn.isFinished()) {
                projectWorkingOn.goToNextStage();
            }
            SpaceProjectWorldSavedData.INSTANCE.markDirty();
        }
    }
    /*
     * Other
     */

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mLocation")) {
            selectedLocation = SpaceProjectManager.getLocation(aNBT.getString("mLocation"));
        }

        if (aNBT.hasKey("mWorkingProject")) {
            projectWorkingOn = SpaceProjectManager.getTeamProject(
                    getBaseMetaTileEntity().getOwnerUuid(),
                    SpaceProjectManager.getLocation(aNBT.getString("workingLocation")),
                    aNBT.getString("mWorkingProject"));
        }

        if (aNBT.hasKey("mProject")) {
            selectedProjectName = aNBT.getString("mProject");
            selectedProject = SpaceProjectManager.getTeamProjectOrCopy(
                    getBaseMetaTileEntity().getOwnerUuid(),
                    selectedProjectName,
                    selectedLocation);
            upgradeFromProject = new ArrayList<>(selectedProject.getAllUpgrades());
            selectedUpgrade = selectedProject.getUpgradeBeingBuilt();
        }

        projectMode = aNBT.getBoolean("projectMode");
        upgradeMode = aNBT.getBoolean("upgradeMode");
        locationIndex = aNBT.getInteger("locationIndex");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (selectedProject != null) {
            aNBT.setString("mProject", selectedProject.getProjectName());
        }

        if (selectedLocation != null) {
            aNBT.setString("mLocation", selectedLocation.getName());
        }

        if (projectWorkingOn != null) {
            aNBT.setString("mWorkingProject", projectWorkingOn.getProjectName());
            aNBT.setString("workingLocation", projectWorkingOn.getProjectLocation().getName());
        }

        aNBT.setBoolean("projectMode", projectMode);
        aNBT.setBoolean("upgradeMode", upgradeMode);
        aNBT.setInteger("locationIndex", locationIndex);
    }

    /*
     * GUI Section
     */

    private static final IDrawable buttonUp = GT_UITextures.BUTTON_STANDARD_TOGGLE.getSubArea(0f, 0f, 0.5f, 0.5f);
    private static final IDrawable buttonUpDisabled = GT_UITextures.BUTTON_STANDARD_TOGGLE_DISABLED
            .getSubArea(0f, 0f, 0.5f, 0.5f);
    private static final IDrawable buttonDown = GT_UITextures.BUTTON_STANDARD_TOGGLE.getSubArea(0.5f, 0.5f, 1f, 1f);
    private static final IDrawable buttonDownDisabled = GT_UITextures.BUTTON_STANDARD_TOGGLE_DISABLED
            .getSubArea(0.5f, 0.5f, 1f, 1f);

    /**
     * Add the UI widgets to the GUI of this controller
     *
     * @param builder      Builder used tor GUI creation
     * @param buildContext Context of the GUI creation
     */
    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.setShowNEI(false);
        buildContext.addSyncedWindow(PROJECT_WINDOW_ID, (player) -> createProjectWindow());
        buildContext.addSyncedWindow(POP_UP_WINDOW_ID, (player) -> createPopUp());
    }

    private ModularWindow createPopUp() {
        ModularWindow.Builder builder = ModularWindow.builder(300, 150);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.widget(
                // Text for displaying the popup message
                TextWidget.dynamicString(() -> popupText).setTextAlignment(Alignment.Center).setSize(280, 130)
                        .setPos(0, 20))
                // Button to close the popup
                .widget(ButtonWidget.closeWindowButton(true).setSize(20, 20).setPos(280, 0))
                // Syncer for the popup text
                .widget(new FakeSyncWidget.StringSyncer(() -> popupText, val -> popupText = val));
        return builder.build();
    }

    /**
     * Create the project creation window button in the place of the safe void button
     *
     * @return Button for project creation window
     */
    @Override
    protected ButtonWidget createSafeVoidButton() {
        Widget button = ButtonWidget.openSyncedWindowButton(PROJECT_WINDOW_ID).setPlayClickSound(false)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
                    ret.add(IG_UITextures.OVERLAY_BUTTON_PROJECTS);
                    return ret.toArray(new IDrawable[0]);
                }).setPos(174, doesBindPlayerInventory() ? 132 : 156).setSize(16, 16);
        button.addTooltip(GCCoreUtil.translate("ig.button.projects")).setTooltipShowUpDelay(TOOLTIP_DELAY);
        return (ButtonWidget) button;
    }

    /**
     * Create the project window which lets the user select a project for construction
     *
     * @return Project child window
     */
    protected ModularWindow createProjectWindow() {
        // List of all available projects
        final DynamicPositionedColumn projects = new DynamicPositionedColumn();
        for (ISpaceProject project : SpaceProjectManager.getAllProjects()) {
            projects.widget(generateCustomButton(project));
        }
        final Scrollable projectListScrollable = new Scrollable().setVerticalScroll();
        projectListScrollable.widget(
                projects.setSizeProvider(
                        (screenSize, window, parent) -> new Size(
                                parent.getSize().width,
                                SpaceProjectManager.getAllProjects().size() * 40)));
        // Display for the details of the selected project
        final MultiChildWidget activeProjectDetail = new MultiChildWidget();
        activeProjectDetail.addChild(
                generateCustomButton(StatCollector.translateToLocal("ig.text.project"), (tClickData, tWidget) -> {
                    projectMode = true;
                    upgradeMode = false;
                },
                        () -> projectMode,
                        () -> true,
                        (screenSize, window,
                                parent) -> new Size(parent.getSize().width * 0.5, parent.getSize().height * 0.05),
                        (screenSize, window, parent) -> new Pos2d(0, 0)))
                .addChild(
                        generateCustomButton(
                                StatCollector.translateToLocal("ig.text.upgrades"),
                                (tClickData, tWidget) -> {
                                    if (isUpgradeButtonClickable()) {
                                        projectMode = false;
                                        upgradeMode = true;
                                    }
                                },
                                () -> upgradeMode,
                                this::isUpgradeButtonClickable,
                                (screenSize, window, parent) -> new Size(
                                        parent.getSize().width * 0.5,
                                        parent.getSize().height * 0.05),
                                (screenSize, window, parent) -> new Pos2d(parent.getSize().width * 0.5, 0)))
                .addChild(generateCustomButton(StatCollector.translateToLocal("ig.text.start"), (clickData, widget) -> {
                    if (widget.getContext().isClient()) return;
                    if (selectedLocation != null && !selectedLocation.getName().equals(LOCATON_UNSELECTED)) {
                        if (selectedProject != null) {
                            selectedProject.setProjectLocation(selectedLocation);
                            if (!selectedProject.isFinished()) {
                                if (selectedProject.meetsRequirements(getBaseMetaTileEntity().getOwnerUuid())) {
                                    SpaceProjectManager.addTeamProject(
                                            getBaseMetaTileEntity().getOwnerUuid(),
                                            selectedLocation,
                                            selectedProject.getProjectName(),
                                            selectedProject);
                                    projectWorkingOn = selectedProject;
                                    popupText = StatCollector.translateToLocal("ig.text.started");
                                    widget.getContext().openSyncedWindow(POP_UP_WINDOW_ID);
                                } else {
                                    popupText = StatCollector.translateToLocal("ig.text.projectrequirements");
                                    widget.getContext().openSyncedWindow(POP_UP_WINDOW_ID);
                                }
                            } else if (selectedUpgrade != null) {
                                if (selectedUpgrade.meetsRequirements(getBaseMetaTileEntity().getOwnerUuid())) {
                                    selectedProject.setCurrentUpgradeBeingBuilt(selectedUpgrade);
                                    projectWorkingOn = selectedProject;
                                    popupText = StatCollector.translateToLocal("ig.text.started");
                                    widget.getContext().openSyncedWindow(POP_UP_WINDOW_ID);
                                } else {
                                    popupText = StatCollector.translateToLocal("ig.text.upgraderequirements");
                                    widget.getContext().openSyncedWindow(POP_UP_WINDOW_ID);
                                }
                            } else {
                                popupText = StatCollector.translateToLocal("ig.text.finishedproject");
                                widget.getContext().openSyncedWindow(POP_UP_WINDOW_ID);
                            }
                        } else {
                            popupText = StatCollector.translateToLocal("ig.text.noproject");
                            widget.getContext().openSyncedWindow(POP_UP_WINDOW_ID);
                        }
                    } else {
                        popupText = StatCollector.translateToLocal("ig.text.nolocation");
                        widget.getContext().openSyncedWindow(POP_UP_WINDOW_ID);
                    }
                },
                        () -> false,
                        () -> true,
                        (screenSize, window,
                                parent) -> new Size(parent.getSize().width, parent.getSize().height * 0.05),
                        (screenSize, window, parent) -> new Pos2d(0, parent.getSize().height * 0.95)));
        generateProjectDetails(activeProjectDetail);
        // Display for the project. Will render a stellar body with the project orbiting it
        final MultiChildWidget activeProjectDisplay = new MultiChildWidget().addChild(
                // Background
                // TODO: Background picture size should be dependent on GUI scale or else it looks bad on small scale
                new RepeatingDrawable().setDrawable(IG_UITextures.BACKGROUND_SPACE_WITH_STARS).setDrawableSize(64, 64)
                        .asWidget().setSizeProvider(
                                ((screenSize, window,
                                        parent) -> new Size(parent.getSize().width, parent.getSize().height))))
                // Celestial body
                .addChild(
                        new DrawableWidget()
                                .setDrawable(
                                        () -> selectedLocation != null && selectedLocation.getTexture() != null
                                                ? selectedLocation.getTexture().withRotationDegree(340)
                                                : IG_UITextures.PICTURE_CELESTIAL_BODY_NEPTUNE.withRotationDegree(340))
                                .setEnabled(
                                        (widget -> selectedLocation != null
                                                && !selectedLocation.getName().equals(LOCATON_UNSELECTED)))
                                .setPosProvider(
                                        (screenSize, window, parent) -> new Pos2d(
                                                (parent.getSize().width - parent.getSize().width * 0.5) / 2,
                                                (parent.getSize().height - parent.getSize().width * 0.5) / 2))
                                .setSizeProvider(
                                        (screenSize, window, parent) -> new Size(
                                                parent.getSize().width * 0.5,
                                                parent.getSize().width * 0.5)))
                // Project icon
                .addChild(
                        new DrawableWidget()
                                .setDrawable(
                                        () -> selectedProject != null && selectedProject.getTexture() != null
                                                ? selectedProject.getTexture()
                                                : IG_UITextures.PICTURE_CELESTIAL_BODY_PROTEUS)
                                .setPosProvider(
                                        (screenSize, window, parent) -> new Pos2d(
                                                (parent.getSize().width) / 2f,
                                                (parent.getSize().height - parent.getSize().width * 0.5) / 2f))
                                .setSizeProvider(
                                        (screenSize, window, parent) -> new Size(
                                                parent.getSize().width * 0.2,
                                                parent.getSize().width * 0.2)));
        // Actual project window
        return ModularWindow.builderFullScreen().setDraggable(false)
                .setBackground(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
                // Scrollable list of available projects
                .widget(
                        projectListScrollable.setPos(20, 20).setSizeProvider(
                                (screenSize, window,
                                        parent) -> new Size(screenSize.width * 0.2, screenSize.height - 70)))
                // Select project detail view in the middle of the window
                .widget(
                        activeProjectDetail
                                .setPosProvider(
                                        (screenSize, window, parent) -> new Pos2d(screenSize.width * 0.2 + 40, 20))
                                .setSizeProvider(
                                        (screenSize, window,
                                                parent) -> new Size(screenSize.width * 0.2, screenSize.height - 40)))
                // Selected project display on the right side of the window
                .widget(
                        activeProjectDisplay
                                .setSizeProvider(
                                        (screenSize, window,
                                                parent) -> new Size(screenSize.width / 2 - 20, screenSize.height - 40))
                                .setPosProvider((screenSize, window, parent) -> new Pos2d(screenSize.width / 2, 20)))
                // Elevator logo in bottom right corner
                .widget(
                        new DrawableWidget().setDrawable(IG_UITextures.PICTURE_ELEVATOR_LOGO_DARK).setSize(36, 36)
                                .setPosProvider(
                                        (screenSize, mainWindow,
                                                parent) -> new Pos2d(screenSize.width - 40, screenSize.height - 40)))
                // Drop down to select location
                .widget(
                        new DropDownWidget().addDropDownItemsSimple(
                                new ArrayList<>(SpaceProjectManager.getLocationNames()),
                                (buttonWidget, index, label, setSelected) -> buttonWidget
                                        .setOnClick((clickData, widget) -> {
                                            selectedLocation = SpaceProjectManager.getLocation(label);
                                            locationIndex = index;
                                            projectMode = true;
                                            upgradeMode = false;
                                            if (!widget.getContext().isClient()) {
                                                selectedProject = SpaceProjectManager.getTeamProjectOrCopy(
                                                        getBaseMetaTileEntity().getOwnerUuid(),
                                                        selectedProject != null ? selectedProject.getProjectName()
                                                                : selectedProjectName,
                                                        selectedLocation);
                                            }
                                            setSelected.run();
                                        }),
                                true).setDirection(DropDownWidget.Direction.UP).setTextUnselected(LOCATON_UNSELECTED)
                                .setSelected(locationIndex).setPos(20, 259).setSize(128, 20)
                                .setBackground(GT_UITextures.BUTTON_STANDARD))
                // Syncer for selected location
                .widget(
                        new FakeSyncWidget.StringSyncer(
                                () -> selectedLocation != null ? selectedLocation.getName() : "",
                                val -> {
                                    selectedLocation = SpaceProjectManager.getLocation(val);
                                    if (selectedProject != null) {
                                        projectMode = true;
                                        upgradeMode = false;
                                    }
                                }))
                // Syncer for selected project
                .widget(
                        new FakeSyncWidget.StringSyncer(
                                () -> selectedProject != null ? selectedProject.getProjectName() : "",
                                val -> {
                                    selectedProjectName = val;
                                    selectedProject = SpaceProjectManager.getProject(val);
                                    if (selectedProject != null) {
                                        upgradeFromProject = new ArrayList<>(selectedProject.getAllUpgrades());
                                        projectMode = true;
                                        upgradeMode = false;
                                    }
                                }))
                // Syncer for selected project upgrade
                .widget(
                        new FakeSyncWidget.StringSyncer(
                                () -> selectedUpgrade != null ? selectedUpgrade.getUpgradeName() : "",
                                val -> {
                                    if (selectedProject != null) {
                                        selectedUpgrade = selectedProject.getUpgrade(val);
                                        selectedProject.setCurrentUpgradeBeingBuilt(selectedUpgrade);
                                    }
                                }))
                // Syncer for upgrade being build
                .widget(
                        new FakeSyncWidget.StringSyncer(
                                () -> selectedProject != null && selectedProject.getUpgradeBeingBuilt() != null
                                        ? selectedProject.getUpgradeBeingBuilt().getUpgradeName()
                                        : "",
                                val -> {
                                    if (selectedProject != null)
                                        selectedProject.setCurrentUpgradeBeingBuilt(selectedProject.getUpgrade(val));
                                }))
                // Syncer for selected project stage
                .widget(
                        new FakeSyncWidget.IntegerSyncer(
                                () -> selectedProject != null ? selectedProject.getCurrentStage() : 0,
                                val -> {
                                    if (selectedProject != null) {
                                        selectedProject.setProjectCurrentStage(val);
                                    }
                                }))
                // Syncer for selected upgrade stage
                .widget(new FakeSyncWidget.IntegerSyncer(() -> {
                    if (selectedProject != null && selectedUpgrade != null) {
                        ISpaceProject.ISP_Upgrade upgradeFromProject = selectedProject.getUpgradeBeingBuilt();
                        if (upgradeFromProject != null) {
                            return upgradeFromProject.getCurrentStage();
                        } else {
                            upgradeFromProject = selectedProject.getUpgrade(selectedUpgrade.getUpgradeName());
                            if (upgradeFromProject != null) {
                                upgradeFromProject.getCurrentStage();
                            } else if (selectedProject.hasUpgrade(selectedUpgrade.getUpgradeName())) {
                                return selectedProject.getTotalStages();
                            }
                        }
                    } else if (selectedUpgrade != null) {
                        return selectedUpgrade.getCurrentStage();
                    }
                    return 0;
                }, val -> {
                    if (selectedProject != null && selectedProject.getUpgradeBeingBuilt() != null) {
                        selectedProject.getUpgradeBeingBuilt().setUpgradeCurrentStage(val);
                    }
                    if (selectedUpgrade != null) {
                        selectedUpgrade.setUpgradeCurrentStage(val);
                    }
                }))
                // Syncer for if the project detail page is enabled
                .widget(new FakeSyncWidget.BooleanSyncer(() -> projectMode, val -> projectMode = val))
                // Syncer for if the upgrade detail page is enabled
                .widget(new FakeSyncWidget.BooleanSyncer(() -> upgradeMode, val -> upgradeMode = val)).build();
    }

    /**
     * Generate widget for project details
     *
     * @param projectDetails Widget to which will be added
     */
    private void generateProjectDetails(final MultiChildWidget projectDetails) {
        projectDetails.addChild(
                // Name of the project as title
                TextWidget.dynamicString(() -> selectedProject != null ? selectedProject.getLocalizedName() : "")
                        .setScale(2).setDefaultColor(WHITE).setTextAlignment(Alignment.Center)
                        .setPosProvider(((screenSize, window, parent) -> new Pos2d(0, parent.getSize().height * 0.05)))
                        .setSizeProvider(
                                ((screenSize, window,
                                        parent) -> new Size(parent.getSize().width, parent.getSize().height * 0.15))))
                // Description of project
                .addChild(
                        TextWidget
                                .dynamicString(
                                        () -> selectedProject != null
                                                ? StatCollector.translateToLocal(
                                                        selectedProject.getUnlocalizedName() + ".description")
                                                : "")
                                .setDefaultColor(WHITE)
                                .setPosProvider(
                                        ((screenSize, window, parent) -> new Pos2d(0, parent.getSize().height * 0.2)))
                                .setSizeProvider(
                                        (screenSize, window, parent) -> new Size(
                                                parent.getSize().width,
                                                parent.getSize().height * 0.4))
                                .setEnabled(widget -> projectMode));
        generateProjectCosts(projectDetails);
        generateUpgradeCost(projectDetails);
        generateUpgrades(projectDetails);
    }

    /**
     * Generate widget for the costs of the project
     *
     * @param projectDetails Widget to which will be added
     */
    private void generateProjectCosts(final MultiChildWidget projectDetails) {
        final DynamicPositionedColumn activeProjectItemsAndFluids = new DynamicPositionedColumn();
        final DynamicPositionedColumn activeProjectCosts = new DynamicPositionedColumn();
        for (int i = 0; i < 12; i++) {
            int index = i;
            activeProjectItemsAndFluids.addChild(
                    new ItemDrawable(() -> selectedProject != null ? selectedProject.getItemCostPerStage(index) : null)
                            .asWidget().dynamicTooltip(() -> {
                                List<String> tTooltip = new ArrayList<>();
                                if (selectedProject != null) {
                                    tTooltip.add(
                                            selectedProject.getItemCostPerStage(index) != null
                                                    ? selectedProject.getItemCostPerStage(index).getDisplayName()
                                                    : "");
                                } else {
                                    tTooltip.add("");
                                }
                                return tTooltip;
                            })
                            .setSizeProvider(
                                    (screenSize, window, parent) -> new Size(
                                            parent.getSize().height / 16,
                                            parent.getSize().height / 16))
                            .setBackground(GT_UITextures.BUTTON_STANDARD).setEnabled(
                                    widget -> selectedProject != null
                                            && selectedProject.getItemCostPerStage(index) != null));
        }
        for (int i = 0; i < 4; i++) {
            int index = i;
            activeProjectItemsAndFluids.addChild(
                    new FluidDrawable()
                            .setFluid(
                                    () -> selectedProject != null ? selectedProject.getFluidCostPerStage(index) : null)
                            .asWidget()
                            .dynamicTooltip(
                                    () -> Collections.singletonList(
                                            selectedProject != null
                                                    ? selectedProject.getFluidCostPerStage(index).getLocalizedName()
                                                    : ""))
                            .setSizeProvider(
                                    (screenSize, window, parent) -> new Size(
                                            parent.getSize().height / 16,
                                            parent.getSize().height / 16))
                            .setBackground(GT_UITextures.BUTTON_STANDARD).setEnabled(
                                    widget -> selectedProject != null
                                            && selectedProject.getFluidCostPerStage(index) != null));
        }
        for (int i = 0; i < 12; i++) {
            int index = i;
            activeProjectCosts.addChild(
                    TextWidget.dynamicString(
                            () -> (selectedProject != null && selectedProject.getItemCostPerStage(index) != null
                                    ? selectedProject.getItemCostPerStage(index).stackSize
                                    : "")
                                    + " Per Stage ("
                                    + (selectedProject != null && selectedProject.getCurrentItemProgress(index) != null
                                            ? selectedProject.getCurrentItemProgress(index).stackSize
                                            : "")
                                    + "/"
                                    + (selectedProject != null && selectedProject.getTotalItemCost(index) != null
                                            ? selectedProject.getTotalItemCost(index).stackSize
                                            : "")
                                    + ")")
                            .setDefaultColor(WHITE).setTextAlignment(Alignment.CenterRight)
                            .setSizeProvider(
                                    (screenSize, window,
                                            parent) -> new Size(parent.getSize().width, parent.getSize().height / 16))
                            .setEnabled(
                                    widget -> selectedProject != null
                                            && selectedProject.getItemCostPerStage(index) != null));
        }
        for (int i = 0; i < 4; i++) {
            int index = i;
            activeProjectCosts.addChild(
                    TextWidget.dynamicString(
                            () -> (selectedProject != null && selectedProject.getCurrentFluidProgress(index) != null
                                    ? selectedProject.getFluidCostPerStage(index).amount
                                    : "")
                                    + " Per Stage ("
                                    + (selectedProject != null && selectedProject.getCurrentFluidProgress(index) != null
                                            ? selectedProject.getCurrentFluidProgress(index).amount
                                            : "")
                                    + "/"
                                    + (selectedProject != null && selectedProject.getTotalFluidCost(index) != null
                                            ? selectedProject.getTotalFluidCost(index).amount
                                            : "")
                                    + ")")
                            .setDefaultColor(WHITE).setTextAlignment(Alignment.CenterRight)
                            .setSizeProvider(
                                    (screenSize, window,
                                            parent) -> new Size(parent.getSize().width, parent.getSize().height / 16))
                            .setEnabled(
                                    widget -> selectedProject != null
                                            && selectedProject.getFluidCostPerStage(index) != null));
        }
        projectDetails.addChild(
                new Scrollable().setVerticalScroll()
                        .widget(
                                activeProjectCosts.setPos(0, 0).setSizeProvider(
                                        (screenSize, window, parent) -> new Size(
                                                parent.getSize().width * 0.85,
                                                parent.getSize().height * 0.2 * 16)))
                        .widget(
                                activeProjectItemsAndFluids
                                        .setPosProvider(
                                                (screenSize, window, parent) -> new Pos2d(
                                                        parent.getSize().width * 0.85,
                                                        -((Scrollable) parent).getVerticalScrollOffset()))
                                        .setSizeProvider(
                                                (screenSize, window, parent) -> new Size(
                                                        parent.getSize().width * 0.15,
                                                        parent.getSize().height * 0.2 * 16)))
                        .setSizeProvider(
                                (screenSize, window,
                                        parent) -> new Size(parent.getSize().width, parent.getSize().height * 0.3))
                        .setPosProvider((screenSize, window, parent) -> new Pos2d(0, parent.getSize().height * 0.6))
                        .setEnabled(widget -> projectMode));
    }

    /**
     * Generate widget for the upgrades of the project
     *
     * @param projectDetails Widget to which will be added
     */
    private void generateUpgrades(MultiChildWidget projectDetails) {
        final DynamicPositionedColumn activeProjectUpgrades = new DynamicPositionedColumn();
        for (int i = 0; i < 12; i++) {
            int index = i;
            activeProjectUpgrades.addChild(
                    generateCustomButton(
                            () -> upgradeFromProject != null && upgradeFromProject.size() > index
                                    ? upgradeFromProject.get(index)
                                    : null));
        }
        projectDetails
                .addChild(
                        new Scrollable().setVerticalScroll().widget(
                                activeProjectUpgrades.setSizeProvider(
                                        (screenSize, window, parent) -> new Size(parent.getSize().width, 25 * 12)))
                                .setPosProvider(
                                        (screenSize, window, parent) -> new Pos2d(0, parent.getSize().height * 0.2))
                                .setSizeProvider(
                                        (screenSize, window, parent) -> new Size(
                                                parent.getSize().width * 0.5,
                                                parent.getSize().height * 0.4))
                                .setEnabled(widget -> upgradeMode))
                .addChild(
                        new Scrollable().setVerticalScroll().widget(
                                TextWidget
                                        .dynamicString(
                                                () -> selectedUpgrade != null
                                                        ? StatCollector.translateToLocal(
                                                                selectedUpgrade.getUnlocalizedName() + ".description")
                                                        : "")
                                        .setDefaultColor(WHITE).setSizeProvider(
                                                (screenSize, window, parent) -> new Size(parent.getSize().width, 200)))
                                .setSizeProvider(
                                        (screenSize, window, parent) -> new Size(
                                                parent.getSize().width,
                                                parent.getSize().height * 0.4))
                                .setPosProvider(
                                        (screenSize, window, parent) -> new Pos2d(
                                                parent.getSize().width * 0.5,
                                                parent.getSize().height * 0.2))
                                .setEnabled(widget -> upgradeMode));
    }

    /**
     * Generate widget for the costs of the upgrade
     *
     * @param projectDetails Widget to which will be added
     */
    private void generateUpgradeCost(MultiChildWidget projectDetails) {
        final DynamicPositionedColumn activeUpgradeItemsAndFluids = new DynamicPositionedColumn();
        final DynamicPositionedColumn activeUpgradeCosts = new DynamicPositionedColumn();
        for (int i = 0; i < GT_Recipe.GT_Recipe_Map.sFakeSpaceProjectRecipes.mUsualInputCount; i++) {
            int index = i;
            activeUpgradeItemsAndFluids.addChild(
                    new ItemDrawable(() -> selectedUpgrade != null ? selectedUpgrade.getItemCostPerStage(index) : null)
                            .asWidget().dynamicTooltip(() -> {
                                List<String> tTooltip = new ArrayList<>();
                                if (selectedUpgrade != null) {
                                    tTooltip.add(
                                            selectedUpgrade.getItemCostPerStage(index) != null
                                                    ? selectedUpgrade.getItemCostPerStage(index).getDisplayName()
                                                    : "");
                                } else {
                                    tTooltip.add("");
                                }
                                return tTooltip;
                            })
                            .setSizeProvider(
                                    (screenSize, window, parent) -> new Size(
                                            parent.getSize().height / 16,
                                            parent.getSize().height / 16))
                            .setBackground(GT_UITextures.BUTTON_STANDARD).setEnabled(
                                    widget -> selectedUpgrade != null
                                            && selectedUpgrade.getItemCostPerStage(index) != null));
        }
        for (int i = 0; i < GT_Recipe.GT_Recipe_Map.sFakeSpaceProjectRecipes.getUsualFluidInputCount(); i++) {
            int index = i;
            activeUpgradeItemsAndFluids.addChild(
                    new FluidDrawable()
                            .setFluid(
                                    () -> selectedUpgrade != null ? selectedUpgrade.getFluidCostPerStage(index) : null)
                            .asWidget()
                            .dynamicTooltip(
                                    () -> Collections.singletonList(
                                            selectedUpgrade != null
                                                    ? selectedUpgrade.getFluidCostPerStage(index).getLocalizedName()
                                                    : ""))
                            .setSizeProvider(
                                    (screenSize, window, parent) -> new Size(
                                            parent.getSize().height / 16,
                                            parent.getSize().height / 16))
                            .setBackground(GT_UITextures.BUTTON_STANDARD).setEnabled(
                                    widget -> selectedUpgrade != null
                                            && selectedUpgrade.getFluidCostPerStage(index) != null));
        }
        for (int i = 0; i < GT_Recipe.GT_Recipe_Map.sFakeSpaceProjectRecipes.mUsualInputCount; i++) {
            int index = i;
            activeUpgradeCosts.addChild(
                    TextWidget.dynamicString(
                            () -> (selectedUpgrade != null && selectedUpgrade.getItemCostPerStage(index) != null
                                    ? selectedUpgrade.getItemCostPerStage(index).stackSize
                                    : "")
                                    + " Per Stage ("
                                    + (selectedUpgrade != null && selectedUpgrade.getCurrentItemProgress(index) != null
                                            ? selectedUpgrade.getCurrentItemProgress(index).stackSize
                                            : "")
                                    + "/"
                                    + (selectedUpgrade != null && selectedUpgrade.getTotalItemCost(index) != null
                                            ? selectedUpgrade.getTotalItemCost(index).stackSize
                                            : "")
                                    + ")")
                            .setDefaultColor(WHITE).setTextAlignment(Alignment.CenterRight)
                            .setSizeProvider(
                                    (screenSize, window,
                                            parent) -> new Size(parent.getSize().width, parent.getSize().height / 16))
                            .setEnabled(
                                    widget -> selectedUpgrade != null
                                            && selectedUpgrade.getItemCostPerStage(index) != null));
        }
        for (int i = 0; i < 4; i++) {
            int index = i;
            activeUpgradeCosts.addChild(
                    TextWidget.dynamicString(
                            () -> (selectedUpgrade != null && selectedUpgrade.getCurrentFluidProgress(index) != null
                                    ? selectedUpgrade.getFluidCostPerStage(index).amount
                                    : "")
                                    + " Per Stage ("
                                    + (selectedUpgrade != null && selectedUpgrade.getCurrentFluidProgress(index) != null
                                            ? selectedUpgrade.getCurrentFluidProgress(index).amount
                                            : "")
                                    + "/"
                                    + (selectedUpgrade != null && selectedUpgrade.getTotalFluidCost(index) != null
                                            ? selectedUpgrade.getTotalFluidCost(index).amount
                                            : "")
                                    + ")")
                            .setDefaultColor(WHITE).setTextAlignment(Alignment.CenterRight)
                            .setSizeProvider(
                                    (screenSize, window,
                                            parent) -> new Size(parent.getSize().width, parent.getSize().height / 16))
                            .setEnabled(
                                    widget -> selectedUpgrade != null
                                            && selectedUpgrade.getFluidCostPerStage(index) != null));
        }
        projectDetails.addChild(
                new Scrollable().setVerticalScroll()
                        .widget(
                                activeUpgradeCosts.setPos(0, 0).setSizeProvider(
                                        (screenSize, window, parent) -> new Size(
                                                parent.getSize().width * 0.85,
                                                parent.getSize().height * 0.2 * 16)))
                        .widget(
                                activeUpgradeItemsAndFluids.setPosProvider(
                                        (screenSize, window, parent) -> new Pos2d(parent.getSize().width * 0.85, 0))
                                        .setSizeProvider(
                                                (screenSize, window, parent) -> new Size(
                                                        parent.getSize().width * 0.15,
                                                        parent.getSize().height * 0.2 * 16)))
                        .setSizeProvider(
                                (screenSize, window,
                                        parent) -> new Size(parent.getSize().width, parent.getSize().height * 0.3))
                        .setPosProvider((screenSize, window, parent) -> new Pos2d(0, parent.getSize().height * 0.6))
                        .setEnabled(widget -> upgradeMode));
    }

    /**
     * Generate the button for the project tab of the information panel
     *
     * @param project Project that is displayed
     * @return Button
     */
    private Widget generateCustomButton(ISpaceProject project) {
        MultiChildWidget customButton = new MultiChildWidget();

        ButtonWidget button = new ButtonWidget();
        customButton.addChild(button.setOnClick((clickData, widget) -> {
            projectMode = true;
            upgradeMode = false;
            if (widget.getContext().isClient()) {
                selectedProject = SpaceProjectManager.getProject(widget.getInternalName());
            } else {
                selectedProject = SpaceProjectManager.getTeamProjectOrCopy(
                        getBaseMetaTileEntity().getOwnerUuid(),
                        widget.getInternalName(),
                        selectedLocation);
            }
            if (selectedProject != null) {
                upgradeFromProject = new ArrayList<>(selectedProject.getAllUpgrades());
            }
        }).setBackground(() -> {
            if (selectedProject != null && selectedProject.getProjectName().equals(button.getInternalName())) {
                return new IDrawable[] { buttonDown };
            }
            return new IDrawable[] { buttonUp };
        }).setInternalName(project.getProjectName())
                .setSizeProvider((screenSize, window, parent) -> new Size(parent.getSize().width, 40))).addChild(
                        new TextWidget(project.getLocalizedName()).setTextAlignment(Alignment.Center)
                                .setSizeProvider((screenSize, window, parent) -> new Size(parent.getSize().width, 40)));

        return customButton.setSizeProvider((screenSize, window, parent) -> new Size(parent.getSize().width, 45));
    }

    /**
     * Generate the button for the upgrade tab of the information panel
     *
     * @param supplier Upgrade supplier of the displayed upgrade
     * @return Button
     */
    private Widget generateCustomButton(Supplier<ISpaceProject.ISP_Upgrade> supplier) {
        MultiChildWidget customButton = new MultiChildWidget();

        ButtonWidget button = new ButtonWidget();
        customButton.addChild(button.setOnClick((clickData, widget) -> {
            if (selectedProject.getUpgradeBeingBuilt() != null
                    && selectedProject.getUpgradeBeingBuilt().getUpgradeName().equals(widget.getInternalName())) {
                selectedUpgrade = selectedProject.getUpgradeBeingBuilt();
            } else {
                selectedUpgrade = selectedProject.getUpgrade(widget.getInternalName());
                if (selectedProject.hasUpgrade(widget.getInternalName())) {
                    selectedUpgrade.setUpgradeCurrentStage(selectedUpgrade.getTotalStages());
                }
            }
        }).setBackground(() -> {
            if (selectedUpgrade != null && selectedUpgrade.getUpgradeName().equals(button.getInternalName())) {
                return new IDrawable[] { buttonDown };
            }
            return new IDrawable[] { buttonUp };
        }).setSizeProvider((screenSize, window, parent) -> new Size(parent.getSize().width, 35))
                .setInternalName(() -> supplier.get() != null ? supplier.get().getUpgradeName() : "")).addChild(
                        TextWidget.dynamicString(() -> supplier.get() != null ? supplier.get().getLocalizedName() : "")
                                .setTextAlignment(Alignment.Center)
                                .setSizeProvider((screenSize, window, parent) -> new Size(parent.getSize().width, 35)));
        return customButton.setSizeProvider((screenSize, window, parent) -> new Size(parent.getSize().width, 40))
                .setEnabled(widget -> !button.getInternalName().equals(""));
    }

    /**
     * Generate a custom text button
     *
     * @param text                Text that will be displayed
     * @param consumer            On click event
     * @param isToggledSupplier   Observer if button is pressed
     * @param isClickableSupplier Observer if button is clickable
     * @param buttonSize          Size of the button
     * @param buttonPos           Position of the button
     * @return Button
     */
    private Widget generateCustomButton(String text, BiConsumer<Widget.ClickData, Widget> consumer,
            Supplier<Boolean> isToggledSupplier, Supplier<Boolean> isClickableSupplier, Widget.SizeProvider buttonSize,
            Widget.PosProvider buttonPos) {
        MultiChildWidget customButton = new MultiChildWidget();
        customButton.addChild(new ButtonWidget().setOnClick(consumer).setBackground(() -> {
            if (!isClickableSupplier.get()) {
                if (isToggledSupplier.get()) {
                    return new IDrawable[] { buttonDownDisabled };
                }
                return new IDrawable[] { buttonUpDisabled };
            }
            if (isToggledSupplier.get()) {
                return new IDrawable[] { buttonDown };
            }
            return new IDrawable[] { buttonUp };
        }).setSizeProvider((screenSize, window, parent) -> new Size(parent.getSize().width, parent.getSize().height)))
                .addChild(
                        new TextWidget(text).setTextAlignment(Alignment.Center).setSizeProvider(
                                (screenSize, window,
                                        parent) -> new Size(parent.getSize().width, parent.getSize().height)));
        return customButton.setSizeProvider(buttonSize).setPosProvider(buttonPos);
    }

    /**
     * @return True is the upgrade button is clickable, else false
     */
    private boolean isUpgradeButtonClickable() {
        if (projectMode) {
            return upgradeFromProject != null && upgradeFromProject.size() > 0
                    && selectedProject != null
                    && selectedProject.isFinished()
                    && selectedProject.getAllUpgrades().size() > 0;
        } else {
            return true;
        }
    }
}
