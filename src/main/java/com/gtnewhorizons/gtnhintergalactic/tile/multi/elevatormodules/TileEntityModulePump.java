package com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules;

import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.recipe.SpacePumpingRecipes;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * Space Pump project module of the Space Elevator
 *
 * @author minecraft7771
 */
public abstract class TileEntityModulePump extends TileEntityModuleBase {

    /** Energy consumption of the module (1A UHV) */
    public static final int ENERGY_CONSUMPTION = (int) GT_Values.VP[9];

    /** Input parameters */
    Parameters.Group.ParameterIn[] parallelSettings;

    Parameters.Group.ParameterIn[] gasTypeSettings;
    Parameters.Group.ParameterIn[] planetTypeSettings;

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
     * @param aStack Item stack that is placed in the controller GUI
     * @return True if a recipe could be started, else false
     */
    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        if (gregtech.api.enums.GT_Values.V[tTier] * getParallelRecipes() * getParallels() > getEUVar()) {
            return false;
        }

        List<FluidStack> outputs = new ArrayList<>();
        int usedEUt = 0;
        for (int i = 0; i < getParallelRecipes(); i++) {
            FluidStack fluid = SpacePumpingRecipes.RECIPES
                    .get(Pair.of((int) planetTypeSettings[i].get(), (int) gasTypeSettings[i].get()));
            if (fluid != null) {
                fluid = fluid.copy();
                fluid.amount = fluid.amount * Math.min((int) parallelSettings[i].get(), getParallels());
                usedEUt += ENERGY_CONSUMPTION * Math.min((int) parallelSettings[i].get(), getParallels());
                outputs.add(fluid);
            }
        }

        lEUt = -usedEUt;
        mOutputFluids = outputs.toArray(new FluidStack[0]);
        eAmpereFlow = 1;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;

        return outputs.size() > 0;
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
                        .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine));

        for (int i = 0; i < getParallelRecipes(); i++) {
            final int fluidIndex = i;
            screenElements.widget(TextWidget.dynamicString(() -> {
                String fluidName = getPumpedFluid(fluidIndex);
                if (fluidName != null) {
                    return " - " + fluidName;
                }
                return "";
            }).setSynced(false).setDefaultColor(COLOR_TEXT_WHITE.get())
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
        FluidStack fluid = SpacePumpingRecipes.RECIPES
                .get(Pair.of((int) planetTypeSettings[index].get(), (int) gasTypeSettings[index].get()));
        if (fluid == null) {
            return null;
        }
        return fluid.getLocalizedName();
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
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc0")) // Module that
                    // adds Space
                    // Pumping
                    // Operations to the
                    .addInfo(
                            EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                                    + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t1.desc1")) // Sucking
                    // up
                    // entire
                    // gas planets was
                    // never easier
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc3"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT2")).addSeparator()
                    .beginStructureBlock(1, 5, 2, false)
                    .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                    .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
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
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc0")) // Module that
                    // adds Space
                    // Pumping
                    // Operations to the
                    .addInfo(
                            EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                                    + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t2.desc1")) // Literally
                    // sucks
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.desc3"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT3"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t2.desc4"))
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
