package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.misc;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GMTE_AmazonPackager extends GregtechMeta_MultiBlockBase<GMTE_AmazonPackager>
        implements ISurvivalConstructable {

    private int mCasing;

    private static IStructureDefinition<GMTE_AmazonPackager> STRUCTURE_DEFINITION = null;

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GMTE_AmazonPackager(mName);
    }

    public GMTE_AmazonPackager(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GMTE_AmazonPackager(String aName) {
        super(aName);
    }

    @Override
    public String getMachineType() {
        return "Packager";
    }

    @Override
    public IStructureDefinition<GMTE_AmazonPackager> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GMTE_AmazonPackager>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GMTE_AmazonPackager.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.getIndexFromPage(2, 9)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 9))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Amazon Warehouse")
                .addInfo("This Multiblock is used for EXTREME packaging requirements")
                .addInfo("Dust Schematics are inserted into the input busses")
                .addInfo("If inserted into the controller, it is shared across all busses")
                .addInfo("1x, 2x, 3x & Other Schematics are to be placed into the controller GUI slot")
                .addInfo("500% faster than using single block machines of the same voltage")
                .addInfo("Only uses 75% of the EU/t normally required").addInfo("Processes 16 items per voltage tier")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 3, 3, true)
                .addController("Front center").addCasingInfoMin("Supply Depot Casings", 10, false)
                .addInputBus("Any casing", 1).addOutputBus("Any casing", 1).addEnergyHatch("Any casing", 1)
                .addMaintenanceHatch("Any casing", 1).addMufflerHatch("Any casing", 1).toolTipFinisher("GT++");
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(2, 9);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.packagerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 6F).setEuModifier(0.75F)
                .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
    }

    @Override
    public int getMaxEfficiency(ItemStack p0) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack arg0) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiPackager;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (16 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
