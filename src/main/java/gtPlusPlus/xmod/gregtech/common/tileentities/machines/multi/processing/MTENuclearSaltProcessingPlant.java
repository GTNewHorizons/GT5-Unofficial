package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;

public class MTENuclearSaltProcessingPlant extends GTPPMultiBlockBase<MTENuclearSaltProcessingPlant>
    implements ISurvivalConstructable {

    protected GTRecipe lastRecipeToBuffer;
    private int casing;
    private static IStructureDefinition<MTENuclearSaltProcessingPlant> STRUCTURE_DEFINITION = null;

    public MTENuclearSaltProcessingPlant(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTENuclearSaltProcessingPlant(String mName) {
        super(mName);
    }

    @Override
    public String getMachineType() {
        return "Reactor Processing Unit, Cold Trap";
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MTENuclearSaltProcessingPlant(this.mName);
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTPPCore.ConfigSwitches.pollutionPerSecondMultiAutoCrafter;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Nuclear Salt Processing Plant")
            .addInfo("Processes depleted nuclear salts that come from the LFTR")
            .addInfo("Handles the recipes of the Reactor Processor Unit and Cold Trap")
            .addInfo("Only Thermally Insulated Casings can be replaced with hatches")
            .addInfo("Mufflers on top, Energy Hatches on bottom, exactly 2 of each are required")
            .addInfo("Maintenance Hatch goes on the back, opposite of the controller block")
            .addInfo("Inputs go on the left side of the multi, outputs on the right side")
            .addInfo("150% faster than using single block machines of the same voltage")
            .addInfo("Processes two items per voltage tier")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("IV Machine Casing", 58, false)
            .addCasingInfoMin("Thermally Insulated Casing", 1, false)
            .addInputBus("Left Half", 2)
            .addInputHatch("Left Half", 2)
            .addOutputBus("Right Half", 3)
            .addOutputHatch("Right Half", 3)
            .addMufflerHatch("Top Side, 2 Required", 4)
            .addEnergyHatch("Bottom Side, 2 Required", 5)
            .addMaintenanceHatch("Back Side, Opposite of Controller", 6)
            .toolTipFinisher(GTPPCore.GT_Tooltip_Builder.get());
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
    public IStructureDefinition<MTENuclearSaltProcessingPlant> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTENuclearSaltProcessingPlant>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "AAA   AAA", "ADA   ADA", "AAA   AAA" },
                            { "ABBA ACCA", "B  AAA  C", "ABBA ACCA" }, { "ABBB~CCCA", "B       C", "ABBBFCCCA" },
                            { "ABBA ACCA", "B  AAA  C", "ABBA ACCA" }, { "AAA   AAA", "AEA   AEA", "AAA   AAA" } }))
                .addElement('A', ofBlock(GregTechAPI.sBlockCasings1, 5))
                .addElement(
                    'B',
                    buildHatchAdder(MTENuclearSaltProcessingPlant.class).atLeast(InputBus, InputHatch)
                        .casingIndex(TAE.getIndexFromPage(0, 10))
                        .dot(2)
                        .buildAndChain(onElementPass(x -> ++x.casing, ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
                .addElement(
                    'C',
                    buildHatchAdder(MTENuclearSaltProcessingPlant.class).atLeast(OutputBus, OutputHatch)
                        .casingIndex(TAE.getIndexFromPage(0, 10))
                        .dot(3)
                        .buildAndChain(onElementPass(x -> ++x.casing, ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
                .addElement(
                    'D',
                    buildHatchAdder(MTENuclearSaltProcessingPlant.class).atLeast(Muffler)
                        .casingIndex(TAE.getIndexFromPage(0, 10))
                        .dot(4)
                        .buildAndChain(onElementPass(x -> ++x.casing, ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
                .addElement(
                    'E',
                    buildHatchAdder(MTENuclearSaltProcessingPlant.class).atLeast(Energy)
                        .casingIndex(TAE.getIndexFromPage(0, 10))
                        .dot(5)
                        .buildAndChain(onElementPass(x -> ++x.casing, ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
                .addElement(
                    'F',
                    buildHatchAdder(MTENuclearSaltProcessingPlant.class).atLeast(Maintenance)
                        .casingIndex(TAE.getIndexFromPage(0, 10))
                        .dot(6)
                        .buildAndChain(onElementPass(x -> ++x.casing, ofBlock(ModBlocks.blockSpecialMultiCasings, 8))))
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
        return new ProcessingLogic().setSpeedBonus(1F / 2.5F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 2 * (Math.max(1, GTUtility.getTier(getMaxInputVoltage())));
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
