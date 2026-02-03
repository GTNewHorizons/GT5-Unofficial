package gtnhintergalactic.tile.multi.elevatormodules;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtnhintergalactic.recipe.IGRecipeMaps;
import gtnhintergalactic.recipe.ResultNoSpaceProject;
import gtnhintergalactic.tile.multi.elevator.ElevatorUtil;
import gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

/**
 * Space Assembler project module of the Space Elevator
 *
 * @author minecraft7771
 */
public abstract class TileEntityModuleAssembler extends TileEntityModuleBase implements IOverclockDescriptionProvider {

    /** Name of the parallel setting */
    private static final INameFunction<TileEntityModuleAssembler> PARALLEL_SETTING_NAME = (base, p) -> GCCoreUtil
        .translate("gt.blockmachines.multimachine.project.ig.assembler.cfgi.0"); // Parallels
    /** Status of the parallel setting */
    private static final IStatusFunction<TileEntityModuleAssembler> PARALLEL_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 100, base.getMaxParallels());

    /** Power object used for displaying in NEI */
    protected final OverclockDescriber overclockDescriber;
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
        overclockDescriber = new ModuleOverclockDescriber((byte) tTier, tModuleTier);
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
        overclockDescriber = new ModuleOverclockDescriber((byte) tTier, tModuleTier);
    }

    /**
     * @return Maximum parallels that this module allows
     */
    protected abstract int getMaxParallels();

    /**
     * @return Power object used for displaying in NEI
     */
    @Override
    public OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    /**
     * @return The recipe map of this machine
     */
    @Override
    public RecipeMap<?> getRecipeMap() {
        return IGRecipeMaps.spaceAssemblerRecipes;
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
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (lastRecipe != recipe) {
                    String neededProject = recipe.getMetadata(IGRecipeMaps.SPACE_PROJECT);
                    String neededLocation = recipe.getMetadata(IGRecipeMaps.SPACE_LOCATION);
                    if (!ElevatorUtil
                        .isProjectAvailable(getBaseMetaTileEntity().getOwnerUuid(), neededProject, neededLocation)) {
                        return new ResultNoSpaceProject(neededProject, neededLocation);
                    }
                }

                int recipeTier = recipe.getMetadataOrDefault(IGRecipeMaps.MODULE_TIER, 1);
                if (tModuleTier < recipeTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipeTier);
                }

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setAmperageOC(false)
            .setMaxParallelSupplier(() -> Math.min(getMaxParallels(), (int) parallelSetting.get()));
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
    protected static IIconContainer engraving;

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
        engraving = Textures.BlockIcons.CustomIcon.create("iconsets/OVERLAY_SIDE_ASSEMBLER_MODULE");
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
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addMachineType(GTUtility.translate("gt.blockmachines.module.name"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.assembler.desc0"))
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GTUtility.translate("gt.blockmachines.multimachine.project.ig.assembler.t1.desc1"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.assembler.t1.desc2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.motorT1"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoMin(GTUtility.translate("gt.blockcasings.ig.0.name"), 0, false)
                .addInputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addOutputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addInputHatch(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .toolTipFinisher();
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
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addMachineType(GTUtility.translate("gt.blockmachines.module.name"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.assembler.desc0"))
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GTUtility.translate("gt.blockmachines.multimachine.project.ig.assembler.t2.desc1"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.assembler.t2.desc2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.motorT3"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GTUtility.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addInputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addOutputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addInputHatch(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .toolTipFinisher();
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
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addMachineType(GTUtility.translate("gt.blockmachines.module.name"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.assembler.desc0"))
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GTUtility.translate("gt.blockmachines.multimachine.project.ig.assembler.t3.desc1"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.assembler.t3.desc2"))
                .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.motorT5"))
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoMin(GTUtility.translate("gt.blockcasings.ig.0.name"), 0, false)
                .addInputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addOutputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .addInputHatch(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
                .toolTipFinisher();
            return tt;
        }
    }
}
