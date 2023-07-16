package com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules;

import static gregtech.api.enums.GT_Values.V;
import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.recipe.IG_Recipe;
import com.gtnewhorizons.gtnhintergalactic.recipe.IG_RecipeAdder;
import com.gtnewhorizons.gtnhintergalactic.recipe.ResultNoSpaceProject;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevator.ElevatorUtil;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * Space Research project module of the Space Elevator
 *
 * @author minecraft7771
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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.research.desc0"))
                .addInfo(
                        EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                                + GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.research.desc1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT2")).addSeparator()
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addInputBus(GCCoreUtil.translate("i.gelevator.structure.AnyBaseCasingWith1Dot"), 1)
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
        return new TileEntityModuleResearch(mName);
    }

    /**
     * @return The recipe map of this machine
     */
    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return IG_RecipeAdder.instance.sSpaceResearchRecipes;
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
