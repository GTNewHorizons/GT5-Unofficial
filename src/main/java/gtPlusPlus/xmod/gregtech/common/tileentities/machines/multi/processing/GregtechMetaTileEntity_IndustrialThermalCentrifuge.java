package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
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

public class GregtechMetaTileEntity_IndustrialThermalCentrifuge
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialThermalCentrifuge>
        implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialThermalCentrifuge> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialThermalCentrifuge(final int aID, final String aName,
            final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialThermalCentrifuge(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialThermalCentrifuge(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Thermal Centrifuge";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Industrial Thermal Centrifuge")
                .addInfo("150% faster than using single block machines of the same voltage")
                .addInfo("Only uses 80% of the EU/t normally required")
                .addInfo("Processes eight items per voltage tier").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().beginStructureBlock(3, 2, 3, false).addController("Front Center")
                .addCasingInfoMin("Thermal Processing Casings/Noise Hazard Sign Blocks", 8, false)
                .addInputBus("Bottom Casing", 1).addOutputBus("Bottom Casing", 1).addEnergyHatch("Bottom Casing", 1)
                .addMaintenanceHatch("Bottom Casing", 1).addMufflerHatch("Bottom Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialThermalCentrifuge> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialThermalCentrifuge>builder()
                    .addShape(mName, transpose(new String[][] { { "X~X", "XXX", "XXX" }, { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            ofChain(
                                    buildHatchAdder(GregtechMetaTileEntity_IndustrialThermalCentrifuge.class)
                                            .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                                            .casingIndex(getCasingTextureIndex()).dot(1).build(),
                                    onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 0)),
                                    onElementPass(x -> ++x.mCasing, ofBlock(GregTech_API.sBlockCasings3, 9))))
                    .addElement(
                            'X',
                            ofChain(
                                    onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 0)),
                                    onElementPass(x -> ++x.mCasing, ofBlock(GregTech_API.sBlockCasings3, 9))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 0, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 0, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 0, 0) && mCasing >= 8 && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default;
    }

    @Override
    protected int getCasingTextureId() {
        return getCasingTextureIndex();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.thermalCentrifugeRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2.5F).setEuModifier(0.8F)
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

    public Block getCasingBlock() {
        return ModBlocks.blockCasings2Misc;
    }

    public byte getCasingMeta() {
        return 0;
    }

    public byte getCasingTextureIndex() {
        return (byte) TAE.GTPP_INDEX(16);
    }
}
