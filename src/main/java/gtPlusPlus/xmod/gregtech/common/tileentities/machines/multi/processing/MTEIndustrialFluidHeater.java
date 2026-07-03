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

import java.util.List;

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
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialFluidHeater extends GTPPMultiBlockBase<MTEIndustrialFluidHeater>
    implements ISurvivalConstructable {

    private int mCasing1;
    private static IStructureDefinition<MTEIndustrialFluidHeater> STRUCTURE_DEFINITION = null;

    public MTEIndustrialFluidHeater(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialFluidHeater(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialFluidHeater(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Fluid Heater, THD";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addBulkMachineInfo(8, 2.2f, 0.9f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 5, 6, true)
            .addController("Front center, 2nd layer")
            .addCasing("47", "Thermal Containment Casing", false)
            .addCasing("34-37", "Multi-Use Casing", false)
            .addEnergyHatch("1+", "Any top or bottom casing", 1, 2)
            .addMaintenanceHatch("1", "Any top or bottom casing", 1, 2)
            .addMufflerHatch("1", "Any top or bottom casing", 1, 2)
            .addInputAny("1+", "Any bottom casing", 1)
            .addOutputAny("1+", "Any top casing", 2)
            .addAir("Interior of the structure")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialFluidHeater> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialFluidHeater>builder()
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
                    buildHatchAdder(MTEIndustrialFluidHeater.class)
                        .atLeast(InputBus, InputHatch, Maintenance, Energy, Muffler)
                        .casingIndex(getCasingTextureIndex())
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing1, ofBlock(getCasingBlock2(), getCasingMeta2()))))
                .addElement('X', ofBlock(getCasingBlock1(), getCasingMeta1()))
                .addElement(
                    'T',
                    buildHatchAdder(MTEIndustrialFluidHeater.class)
                        .atLeast(OutputBus, OutputHatch, Maintenance, Energy, Muffler)
                        .casingIndex(getCasingTextureIndex())
                        .hint(2)
                        .buildAndChain(onElementPass(x -> ++x.mCasing1, ofBlock(getCasingBlock2(), getCasingMeta2()))))
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
        return survivalBuildPiece(mName, stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mCasing1 = 0;
        if (!checkPiece(mName, 2, 4, 0, errors)) return;
        checkCasingMin(errors, mCasing1, 34);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAFluidHeaterActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAFluidHeaterActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAFluidHeater;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAFluidHeaterGlow;
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
        return new ProcessingLogic().setSpeedBonus(1F / 2.2F)
            .setEuModifier(0.9F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialFluidHeater;
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
