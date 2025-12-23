package gtnhintergalactic.tile.multi.elevatormodules;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtnhintergalactic.recipe.IGRecipeMaps;
import gtnhintergalactic.recipe.ResultNoSpaceProject;
import gtnhintergalactic.tile.multi.elevator.ElevatorUtil;

/**
 * Space Research project module of the Space Elevator
 *
 * @author minecraft7771
 *
 *         RECIPE IS CURRENTLY TEMPORARILY COMMENTED OUT IN MachineLoader.java FYI
 *         BLOCK IS ALSO HIDDEN IN NEIGTNewHorizonsConfig.java IN COREMOD
 */
public class TileEntityModuleResearch extends TileEntityModuleBase {

    /** Voltage tier of this module */
    private static final int MODULE_VOLTAGE_TIER = 12;
    /** Tier of this module */
    protected static final int MODULE_TIER = 1;
    /** Minimum motor tier that is needed for this module */
    protected static final int MINIMUM_MOTOR_TIER = 2;

    /**
     * Create new Space Assembler module
     *
     * @param aID           ID of the module
     * @param aName         Name of the module
     * @param aNameRegional Localized name of the module
     */
    public TileEntityModuleResearch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
    }

    /**
     * Create new Space Assembler module
     *
     * @param aName Name of the module
     */
    public TileEntityModuleResearch(String aName) {
        super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
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
            .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.research.desc0"))
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                    + GTUtility.translate("gt.blockmachines.multimachine.project.ig.research.desc1"))
            .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.desc2"))
            .addInfo(GTUtility.translate("gt.blockmachines.multimachine.project.ig.motorT2"))
            .beginStructureBlock(1, 5, 2, false)
            .addCasingInfoRange(GTUtility.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
            .addInputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
            .addOutputBus(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
            .addInputHatch(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
            .addOutputHatch(GTUtility.translate("ig.elevator.structure.AnyBaseCasingWithHintNumber1"), 1)
            .toolTipFinisher();
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
        return new TileEntityModuleResearch(mName);
    }

    /**
     * @return The recipe map of this machine
     */
    @Override
    public RecipeMap<?> getRecipeMap() {
        return IGRecipeMaps.spaceResearchRecipes;
    }

    /**
     * Set the power that is available to the processing logic
     *
     * @param logic Logic that will be configured
     */
    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(V[tTier]);
        logic.setAvailableAmperage(1);
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
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        };
    }

    @Override
    public boolean protectsExcessItem() {
        return !eSafeVoid;
    }

    @Override
    public boolean protectsExcessFluid() {
        return !eSafeVoid;
    }
}
