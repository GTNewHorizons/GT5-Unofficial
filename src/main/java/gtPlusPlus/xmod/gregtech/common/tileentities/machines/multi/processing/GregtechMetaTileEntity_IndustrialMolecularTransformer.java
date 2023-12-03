package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialMolecularTransformer
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialMolecularTransformer>
        implements ISurvivalConstructable {

    private static final int CASING_TEXTURE_ID = 48;
    private int mCasing = 0;

    public GregtechMetaTileEntity_IndustrialMolecularTransformer(final int aID, final String aName,
            final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialMolecularTransformer(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialMolecularTransformer(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Molecular Transformer";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {

        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Changes the structure of items to produce new ones")
                .addInfo("Maximum 1x of each bus/hatch.").addPollutionAmount(getPollutionPerSecond(null)).addSeparator()
                .beginStructureBlock(7, 7, 7, false).addController("Top Center")
                .addCasingInfoMin("Robust Tungstensteel Machine Casing", 40, false)
                .addCasingInfoMin("Tungstensteel Coils", 16, false)
                .addCasingInfoMin("Molecular Containment Casing", 52, false)
                .addCasingInfoMin("High Voltage Current Capacitor", 32, false)
                .addCasingInfoMin("Particle Containment Casing", 4, false)
                .addCasingInfoMin("Resonance Chamber I", 5, false).addCasingInfoMin("Modulator I", 4, false)
                .addInputBus("Any Robust Tungstensteel Machine Casing", 1)
                .addOutputBus("Any Robust Tungstensteel Machine Casing", 1)
                .addEnergyHatch("Any Robust Tungstensteel Machine Casing", 1)
                .addMaintenanceHatch("Any Robust Tungstensteel Machine Casing", 1)
                .addMufflerHatch("Any Robust Tungstensteel Machine Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialMolecularTransformer> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialMolecularTransformer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialMolecularTransformer>builder()
                    .addShape(
                            STRUCTURE_PIECE_MAIN,
                            (new String[][] {
                                    { "       ", "       ", "  xxx  ", "  x~x  ", "  xxx  ", "       ", "       " },
                                    { "       ", "  xxx  ", " xyyyx ", " xyzyx ", " xyyyx ", "  xxx  ", "       " },
                                    { "       ", "  xxx  ", " xyyyx ", " xyzyx ", " xyyyx ", "  xxx  ", "       " },
                                    { "       ", "  xxx  ", " xyyyx ", " xyzyx ", " xyyyx ", "  xxx  ", "       " },
                                    { "   t   ", " ttxtt ", " tyyyt ", "txyzyxt", " tyyyt ", " ttxtt ", "   t   " },
                                    { "   c   ", " ccecc ", " cxfxc ", "cefefec", " cxfxc ", " ccecc ", "   c   " },
                                    { "   h   ", " hhhhh ", " hhhhh ", "hhhhhhh", " hhhhh ", " hhhhh ", "   h   " }, }))
                    .addElement('x', ofBlock(getCasingBlock(), getCasingMeta()))
                    .addElement('y', ofBlock(getCasingBlock(), getCasingMeta2()))
                    .addElement('z', ofBlock(getCasingBlock(), getCasingMeta3()))
                    .addElement('e', ofBlock(getCasingBlock2(), 0)).addElement('f', ofBlock(getCasingBlock2(), 4))
                    .addElement('c', ofBlock(getCoilBlock(), 3))
                    .addElement('t', ofBlock(getCasingBlock3(), getTungstenCasingMeta()))
                    .addElement(
                            'h',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialMolecularTransformer.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                                    .casingIndex(getCasingTextureIndex()).dot(1).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.mCasing,
                                                    ofBlock(getCasingBlock3(), getTungstenCasingMeta()))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        boolean aDidBuild = checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 0);
        if (this.mInputBusses.size() != 1 || this.mOutputBusses.size() != 1 || this.mEnergyHatches.size() != 1) {
            return false;
        }
        // there are 16 slot that only allow casing, so we subtract this from the grand total required
        return aDidBuild && mCasing >= 40 - 16 && checkHatch();
    }

    protected static int getCasingTextureIndex() {
        return CASING_TEXTURE_ID;
    }

    protected static Block getCasingBlock() {
        return ModBlocks.blockSpecialMultiCasings;
    }

    protected static Block getCasingBlock2() {
        return ModBlocks.blockSpecialMultiCasings2;
    }

    protected static Block getCasingBlock3() {
        return GregTech_API.sBlockCasings4;
    }

    protected static Block getCoilBlock() {
        return GregTech_API.sBlockCasings5;
    }

    protected static int getCasingMeta() {
        return 11;
    }

    protected static int getCasingMeta2() {
        return 12;
    }

    protected static int getCasingMeta3() {
        return 13;
    }

    protected static int getTungstenCasingMeta() {
        return 0;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d == ForgeDirection.UP;
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
        return 44;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.molecularTransformerRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic();
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiMolecularTransformer;
    }

    @Override
    public int getDamageToComponent(final ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }
}
