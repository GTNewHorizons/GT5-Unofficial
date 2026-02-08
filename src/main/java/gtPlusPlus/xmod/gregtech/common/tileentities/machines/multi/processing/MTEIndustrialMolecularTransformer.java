package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialMolecularTransformer extends GTPPMultiBlockBase<MTEIndustrialMolecularTransformer>
    implements ISurvivalConstructable {

    private static final int CASING_TEXTURE_ID = 48;
    private int mCasing = 0;

    public MTEIndustrialMolecularTransformer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialMolecularTransformer(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialMolecularTransformer(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Molecular Transformer";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {

        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Changes the structure of items to produce new ones")
            .addInfo("Maximum 1x of each bus/hatch")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 7, 7, false)
            .addController("Top Center")
            .addCasingInfoMin("Robust Tungstensteel Machine Casing", 40, false)
            .addCasingInfoMin("TPV-Alloy Coils", 16, false)
            .addCasingInfoMin("Molecular Containment Casing", 52, false)
            .addCasingInfoMin("High Voltage Current Capacitor", 32, false)
            .addCasingInfoMin("Particle Containment Casing", 4, false)
            .addCasingInfoMin("Resonance Chamber I", 5, false)
            .addCasingInfoMin("Modulator I", 4, false)
            .addInputBus("Any Robust Tungstensteel Machine Casing", 1)
            .addOutputBus("Any Robust Tungstensteel Machine Casing", 1)
            .addEnergyHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addMaintenanceHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addMufflerHatch("Any Robust Tungstensteel Machine Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<MTEIndustrialMolecularTransformer> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<MTEIndustrialMolecularTransformer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialMolecularTransformer>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    (new String[][] { { "       ", "       ", "  xxx  ", "  x~x  ", "  xxx  ", "       ", "       " },
                        { "       ", "  xxx  ", " xyyyx ", " xyzyx ", " xyyyx ", "  xxx  ", "       " },
                        { "       ", "  xxx  ", " xyyyx ", " xyzyx ", " xyyyx ", "  xxx  ", "       " },
                        { "       ", "  xxx  ", " xyyyx ", " xyzyx ", " xyyyx ", "  xxx  ", "       " },
                        { "   t   ", " ttxtt ", " tyyyt ", "txyzyxt", " tyyyt ", " ttxtt ", "   t   " },
                        { "   c   ", " ccecc ", " cxfxc ", "cefefec", " cxfxc ", " ccecc ", "   c   " },
                        { "   h   ", " hhhhh ", " hhhhh ", "hhhhhhh", " hhhhh ", " hhhhh ", "   h   " }, }))
                .addElement('x', ofBlock(getCasingBlock(), getCasingMeta()))
                .addElement('y', ofBlock(getCasingBlock(), getCasingMeta2()))
                .addElement('z', ofBlock(getCasingBlock(), getCasingMeta3()))
                .addElement('e', ofBlock(getCasingBlock2(), 0))
                .addElement('f', ofBlock(getCasingBlock2(), 4))
                .addElement('c', ofBlock(getCoilBlock(), 3))
                .addElement('t', ofBlock(getCasingBlock3(), getTungstenCasingMeta()))
                .addElement(
                    'h',
                    buildHatchAdder(MTEIndustrialMolecularTransformer.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(getCasingTextureIndex())
                        .hint(1)
                        .buildAndChain(
                            onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock3(), getTungstenCasingMeta()))))
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
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        boolean aDidBuild = checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 0);
        if (this.mOutputBusses.size() != 1 || this.mEnergyHatches.size() != 1) {
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
        return GregTechAPI.sBlockCasings4;
    }

    protected static Block getCoilBlock() {
        return GregTechAPI.sBlockCasings5;
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
        return TexturesGtBlock.oMCAIndustrialMolecularTransformerActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialMolecularTransformerActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialMolecularTransformer;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialMolecularTransformerGlow;
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
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching();
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiMolecularTransformer;
    }

}
