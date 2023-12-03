package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
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

public class GregtechMetaTileEntity_IndustrialFluidHeater extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialFluidHeater> implements ISurvivalConstructable {

    private int mCasing1;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialFluidHeater> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialFluidHeater(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialFluidHeater(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialFluidHeater(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Fluid Heater";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Industrial Fluid Heater")
                .addInfo("120% faster than using single block machines of the same voltage")
                .addInfo("Only uses 90% of the EU/t normally required")
                .addInfo("Processes eight items per voltage tier").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().beginStructureBlock(5, 6, 5, true).addController("Front Center")
                .addCasingInfoMin("Top/Bottom layer: Multi-use Casings", 34, false)
                .addCasingInfoMin("Middle layers: Thermal Containment Casing", 47, false)
                .addInputBus("Bottom Layer (optional)", 1).addInputHatch("Bottom Layer", 1)
                .addOutputBus("Top Layer (optional)", 1).addOutputHatch("Top Layer", 1)
                .addEnergyHatch("Any Multi-use Casing", 1).addMaintenanceHatch("Any Multi-use Casing", 1)
                .addMufflerHatch("Any Multi-use Casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialFluidHeater> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialFluidHeater>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { " TTT ", "TTTTT", "TTTTT", "TTTTT", " TTT " },
                                            { " XXX ", "X---X", "X---X", "X---X", " XXX " },
                                            { " XXX ", "X---X", "X---X", "X---X", " XXX " },
                                            { " XXX ", "X---X", "X---X", "X---X", " XXX " },
                                            { " X~X ", "X---X", "X---X", "X---X", " XXX " },
                                            { " BBB ", "BBBBB", "BBBBB", "BBBBB", " BBB " }, }))
                    .addElement(
                            'B',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialFluidHeater.class)
                                    .atLeast(InputBus, InputHatch, Maintenance, Energy, Muffler)
                                    .casingIndex(getCasingTextureIndex()).dot(1).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.mCasing1,
                                                    ofBlock(getCasingBlock2(), getCasingMeta2()))))
                    .addElement('X', ofBlock(getCasingBlock1(), getCasingMeta1()))
                    .addElement(
                            'T',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialFluidHeater.class)
                                    .atLeast(OutputBus, OutputHatch, Maintenance, Energy, Muffler)
                                    .casingIndex(getCasingTextureIndex()).dot(1).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.mCasing1,
                                                    ofBlock(getCasingBlock2(), getCasingMeta2()))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 2, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing1 = 0;
        boolean didBuild = checkPiece(mName, 2, 4, 0);
        log("Built? " + didBuild + ", " + mCasing1);
        return didBuild && mCasing1 >= 34 && checkHatch();
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
        return TAE.getIndexFromPage(0, 1);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidHeaterRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2.2F).setEuModifier(0.9F)
                .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialThermalCentrifuge;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    public Block getCasingBlock1() {
        return ModBlocks.blockCasings2Misc;
    }

    public byte getCasingMeta1() {
        return 11;
    }

    public Block getCasingBlock2() {
        return ModBlocks.blockCasings3Misc;
    }

    public byte getCasingMeta2() {
        return 2;
    }

    public byte getCasingTextureIndex() {
        return (byte) TAE.getIndexFromPage(2, 2);
    }
}
