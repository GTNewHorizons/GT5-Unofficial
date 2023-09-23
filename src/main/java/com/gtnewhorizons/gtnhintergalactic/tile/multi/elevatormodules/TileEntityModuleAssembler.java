package com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules;

import static gregtech.api.enums.GT_Values.V;
import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.recipe.IG_Recipe;
import com.gtnewhorizons.gtnhintergalactic.recipe.IG_RecipeAdder;
import com.gtnewhorizons.gtnhintergalactic.recipe.ResultNoSpaceProject;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevator.ElevatorUtil;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.common.power.BasicMachineEUPower;
import gregtech.common.power.Power;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * Space Assembler project module of the Space Elevator
 *
 * @author minecraft7771
 */
public abstract class TileEntityModuleAssembler extends TileEntityModuleBase {

    /** Name of the parallel setting */
    private static final INameFunction<TileEntityModuleAssembler> PARALLEL_SETTING_NAME = (base, p) -> GCCoreUtil
            .translate("gt.blockmachines.multimachine.project.ig.assembler.cfgi.0"); // Parallels
    /** Status of the parallel setting */
    private static final IStatusFunction<TileEntityModuleAssembler> PARALLEL_STATUS = (base, p) -> LedStatus
            .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 100, base.getMaxParallels());

    /** Power object used for displaying in NEI */
    protected final Power power;
    /** Input parameters */
    Parameters.Group.ParameterIn parallelSetting;

    /**
     * Create new Space Assembler module
     *
     * @param aID                  ID of the module
     * @param aName                Name of the module
     * @param aNameRegional        Localized name of the module
     * @param tTier                Voltage tier of the module
     * @param tModuleTier          Tier of the module
     * @param tMinMotorTier        Minimum needed motor tier
     * @param bufferSizeMultiplier Multiplier for the EU buffer size, if the standard buffer is too small
     */
    public TileEntityModuleAssembler(int aID, String aName, String aNameRegional, int tTier, int tModuleTier,
            int tMinMotorTier, int bufferSizeMultiplier) {
        super(aID, aName, aNameRegional, tTier, tModuleTier, tMinMotorTier, bufferSizeMultiplier);
        power = new AssemblerPower((byte) tTier, tModuleTier);
    }

    /**
     * Create new Space Assembler module
     *
     * @param aName                Name of the module
     * @param tTier                Voltage tier of the module
     * @param tModuleTier          Tier of the module
     * @param tMinMotorTier        Minimum needed motor tier
     * @param bufferSizeMultiplier Multiplier for the EU buffer size, if the standard buffer is too small
     */
    public TileEntityModuleAssembler(String aName, int tTier, int tModuleTier, int tMinMotorTier,
            int bufferSizeMultiplier) {
        super(aName, tTier, tModuleTier, tMinMotorTier, bufferSizeMultiplier);
        power = new AssemblerPower((byte) tTier, tModuleTier);
    }

    /**
     * @return Maximum parallels that this module allows
     */
    protected abstract int getMaxParallels();

    /**
     * @return Power object used for displaying in NEI
     */
    @Override
    public Power getPower() {
        return power;
    }

    /**
     * @return The recipe map of this machine
     */
    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return IG_RecipeAdder.instance.sSpaceAssemblerRecipes;
    }

    /**
     * Set the power that is available to the processing logic
     *
     * @param logic Logic that will be configured
     */
    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(V[tTier]);
        logic.setAvailableAmperage(getMaxParallels());
    }

    /**
     * @return Processing logic of this machine
     */
    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (lastRecipe != recipe && recipe instanceof IG_Recipe igRecipe) {
                    String neededProject = igRecipe.getNeededSpaceProject();
                    String neededLocation = igRecipe.getNeededSpaceProjectLocation();
                    if (!ElevatorUtil.isProjectAvailable(
                            getBaseMetaTileEntity().getOwnerUuid(),
                            neededProject,
                            neededLocation)) {
                        return new ResultNoSpaceProject(neededProject, neededLocation);
                    }
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setAmperageOC(false).setMaxParallelSupplier(() -> Math.min(getMaxParallels(), (int) parallelSetting.get()));
    }

    /**
     * Instantiate parameters of the controller
     */
    @Override
    protected void parametersInstantiation_EM() {
        super.parametersInstantiation_EM();
        Parameters.Group hatch_0 = parametrization.getGroup(0, false);
        parallelSetting = hatch_0.makeInParameter(0, getMaxParallels(), PARALLEL_SETTING_NAME, PARALLEL_STATUS);
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
                    new TT_RenderedExtendedFacingTexture(
                            aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                                    : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        } else if (facing.getRotation(ForgeDirection.UP) == side || facing.getRotation(ForgeDirection.DOWN) == side) {
            return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE),
                    new TT_RenderedExtendedFacingTexture(engraving) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE) };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        engraving = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_SIDE_ASSEMBLER_MODULE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public boolean protectsExcessItem() {
        return !eSafeVoid;
    }

    @Override
    public boolean protectsExcessFluid() {
        return !eSafeVoid;
    }

    /**
     * Power object used to display the assembler in NEI
     */
    private static class AssemblerPower extends BasicMachineEUPower {

        /**
         * Create a new power object for assembler modules
         *
         * @param tier       Voltage tier of the miner
         * @param moduleTier Module tier of the miner
         */
        public AssemblerPower(byte tier, int moduleTier) {
            super(tier, 1, moduleTier);
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
     * Space Assembler project module T1 of the Space Elevator
     *
     * @author minecraft7771
     */
    public static class TileEntityModuleAssemblerT1 extends TileEntityModuleAssembler {

        /** Voltage tier of this module */
        protected static final int MODULE_VOLTAGE_TIER = 9;
        /** Tier of this module */
        protected static final int MODULE_TIER = 1;
        /** Minimum motor tier that is needed for this module */
        protected static final int MINIMUM_MOTOR_TIER = 1;
        /** Maximum parallels which this module can handle */
        protected static final int MAX_PARALLELS = 4;

        /**
         * Create a new T1 assembler module controller
         *
         * @param aID           ID of the controller
         * @param aName         Name of the controller
         * @param aNameRegional Localized name of the controller
         */
        public TileEntityModuleAssemblerT1(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        /**
         * Create a new T1 assembler module controller
         *
         * @param aName Name of the controller
         */
        public TileEntityModuleAssemblerT1(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        /**
         * Get a new meta tile entity of this controller
         *
         * @param iGregTechTileEntity this
         * @return New meta tile entity
         */
        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TileEntityModuleAssemblerT1(mName);
        }

        /**
         * @return Maximum parallels that this module allows
         */
        protected int getMaxParallels() {
            return MAX_PARALLELS;
        }

        /**
         * @return Tooltip builder for this module
         */
        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.desc0"))
                    .addInfo(
                            EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                                    + GCCoreUtil
                                            .translate("gt.blockmachines.multimachine.project.ig.assembler.t1.desc1"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t1.desc2"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT1")).addSeparator()
                    .beginStructureBlock(1, 5, 2, false)
                    .addCasingInfoMin(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, false)
                    .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
            return tt;
        }
    }

    /**
     * Space Assembler project module T2 of the Space Elevator
     *
     * @author minecraft7771
     */
    public static class TileEntityModuleAssemblerT2 extends TileEntityModuleAssembler {

        /** Voltage tier of this module */
        protected static final int MODULE_VOLTAGE_TIER = 11;
        /** Tier of this module */
        protected static final int MODULE_TIER = 2;
        /** Minimum motor tier that is needed for this module */
        protected static final int MINIMUM_MOTOR_TIER = 3;
        /** Maximum parallels which this module can handle */
        protected static final int MAX_PARALLELS = 16;

        /**
         * Create a new T2 assembler module controller
         *
         * @param aID           ID of the controller
         * @param aName         Name of the controller
         * @param aNameRegional Localized name of the controller
         */
        public TileEntityModuleAssemblerT2(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        /**
         * Create a new T2 assembler module controller
         *
         * @param aName Name of the controller
         */
        public TileEntityModuleAssemblerT2(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        /**
         * Get a new meta tile entity of this controller
         *
         * @param iGregTechTileEntity this
         * @return New meta tile entity
         */
        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TileEntityModuleAssemblerT2(mName);
        }

        /**
         * @return Maximum parallels that this module allows
         */
        protected int getMaxParallels() {
            return MAX_PARALLELS;
        }

        /**
         * @return Tooltip builder for this module
         */
        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.desc0"))
                    .addInfo(
                            EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                                    + GCCoreUtil
                                            .translate("gt.blockmachines.multimachine.project.ig.assembler.t2.desc1"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t2.desc2"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT3")).addSeparator()
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

    /**
     * Space Assembler project module T3 of the Space Elevator
     *
     * @author minecraft7771
     */
    public static class TileEntityModuleAssemblerT3 extends TileEntityModuleAssembler {

        /** Voltage tier of this module */
        protected static final int MODULE_VOLTAGE_TIER = 13;
        /** Tier of this module */
        protected static final int MODULE_TIER = 3;
        /** Minimum motor tier that is needed for this module */
        protected static final int MINIMUM_MOTOR_TIER = 5;
        /** Maximum parallels which this module can handle */
        protected static final int MAX_PARALLELS = 64;

        /**
         * Create a new T3 assembler module controller
         *
         * @param aID           ID of the controller
         * @param aName         Name of the controller
         * @param aNameRegional Localized name of the controller
         */
        public TileEntityModuleAssemblerT3(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        /**
         * Create a new T3 assembler module controller
         *
         * @param aName Name of the controller
         */
        public TileEntityModuleAssemblerT3(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        /**
         * Get a new meta tile entity of this controller
         *
         * @param iGregTechTileEntity this
         * @return New meta tile entity
         */
        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TileEntityModuleAssemblerT3(mName);
        }

        /**
         * @return Maximum parallels that this module allows
         */
        protected int getMaxParallels() {
            return MAX_PARALLELS;
        }

        /**
         * @return Tooltip builder for this module
         */
        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.desc0"))
                    .addInfo(
                            EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                                    + GCCoreUtil
                                            .translate("gt.blockmachines.multimachine.project.ig.assembler.t3.desc1"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t3.desc2"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT5")).addSeparator()
                    .beginStructureBlock(1, 5, 2, false)
                    .addCasingInfoMin(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, false)
                    .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
            return tt;
        }
    }
}
