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

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

public class GregtechMetaTileEntity_NuclearSaltProcessingPlant
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_NuclearSaltProcessingPlant>
        implements ISurvivalConstructable {

    protected GT_Recipe lastRecipeToBuffer;
    private int casing;
    private static IStructureDefinition<GregtechMetaTileEntity_NuclearSaltProcessingPlant> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_NuclearSaltProcessingPlant(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_NuclearSaltProcessingPlant(String mName) {
        super(mName);
    }

    @Override
    public String getMachineType() {
        return "Reactor Processing Unit, Cold Trap";
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new GregtechMetaTileEntity_NuclearSaltProcessingPlant(this.mName);
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiAutoCrafter;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Nuclear Salt Processing Plant")
                .addInfo("Processes depleted nuclear salts that come from the LFTR")
                .addInfo("Handles the recipes of the Reactor Processor Unit and Cold Trap")
                .addInfo("Only Thermally Insulated Casings can be replaced with hatches")
                .addInfo("Mufflers on top, Energy Hatches on bottom, exactly 2 of each are required")
                .addInfo("Maintenance Hatch goes on the back, opposite of the controller block")
                .addInfo("Inputs go on the left side of the multi, outputs on the right side")
                .addInfo("150% faster than using single block machines of the same voltage")
                .addInfo("Processes two items per voltage tier").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().beginStructureBlock(3, 3, 3, true).addController("Front Center")
                .addCasingInfoMin("IV Machine Casing", 58, false)
                .addCasingInfoMin("Thermally Insulated Casing", 1, false).addInputBus("Left Half", 2)
                .addInputHatch("Left Half", 2).addOutputBus("Right Half", 3).addOutputHatch("Right Half", 3)
                .addMufflerHatch("Top Side, 2 Required", 4).addEnergyHatch("Bottom Side, 2 Required", 5)
                .addMaintenanceHatch("Back Side, Opposite of Controller", 6)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(0, 10);
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_NuclearSaltProcessingPlant> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_NuclearSaltProcessingPlant>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "AAA   AAA", "ADA   ADA", "AAA   AAA" },
                                            { "ABBA ACCA", "B  AAA  C", "ABBA ACCA" },
                                            { "ABBB~CCCA", "B       C", "ABBBFCCCA" },
                                            { "ABBA ACCA", "B  AAA  C", "ABBA ACCA" },
                                            { "AAA   AAA", "AEA   AEA", "AAA   AAA" } }))
                    .addElement('A', ofBlock(GregTech_API.sBlockCasings1, 5))
                    .addElement(
                            'B',
                            buildHatchAdder(GregtechMetaTileEntity_NuclearSaltProcessingPlant.class)
                                    .atLeast(InputBus, InputHatch).casingIndex(TAE.getIndexFromPage(0, 10)).dot(2)
                                    .buildAndChain(
                                            onElementPass(
                                                    x -> ++x.casing,
                                                    ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_NuclearSaltProcessingPlant.class)
                                    .atLeast(OutputBus, OutputHatch).casingIndex(TAE.getIndexFromPage(0, 10)).dot(3)
                                    .buildAndChain(
                                            onElementPass(
                                                    x -> ++x.casing,
                                                    ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
                    .addElement(
                            'D',
                            buildHatchAdder(GregtechMetaTileEntity_NuclearSaltProcessingPlant.class).atLeast(Muffler)
                                    .casingIndex(TAE.getIndexFromPage(0, 10)).dot(4).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.casing,
                                                    ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
                    .addElement(
                            'E',
                            buildHatchAdder(GregtechMetaTileEntity_NuclearSaltProcessingPlant.class).atLeast(Energy)
                                    .casingIndex(TAE.getIndexFromPage(0, 10)).dot(5).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.casing,
                                                    ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
                    .addElement(
                            'F',
                            buildHatchAdder(GregtechMetaTileEntity_NuclearSaltProcessingPlant.class)
                                    .atLeast(Maintenance).casingIndex(TAE.getIndexFromPage(0, 10)).dot(6).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.casing,
                                                    ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        buildPiece(mName, itemStack, hintsOnly, 4, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack itemStack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, itemStack, 4, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity baseMetaTileEntity, ItemStack itemStack) {
        casing = 0;
        return checkPiece(mName, 4, 2, 0) && checkHatch();
    }

    @Override
    public boolean checkHatch() {
        return mEnergyHatches.size() == 2 && mMufflerHatches.size() == 2 && super.checkHatch();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.nuclearSaltProcessingPlantRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2.5F).setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 2 * (Math.max(1, GT_Utility.getTier(getMaxInputVoltage())));
    }

    @Override
    public String[] getExtraInfoData() {
        final String running = (this.mMaxProgresstime > 0 ? "Salt Plant running" : "Salt Plant stopped");
        final String maintenance = (this.getIdealStatus() == this.getRepairStatus() ? "No Maintenance issues"
                : "Needs Maintenance");
        String tSpecialText;

        if (lastRecipeToBuffer != null && lastRecipeToBuffer.mOutputs[0].getDisplayName() != null) {
            tSpecialText = "Currently processing: " + lastRecipeToBuffer.mOutputs[0].getDisplayName();
        } else {
            tSpecialText = "Currently processing: Nothing";
        }

        return new String[] { "Nuclear Salt Processing Plant", running, maintenance, tSpecialText };
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
