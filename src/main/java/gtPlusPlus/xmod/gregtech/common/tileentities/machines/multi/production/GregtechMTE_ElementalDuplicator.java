package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
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
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ElementalDataOrbHolder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMTE_ElementalDuplicator extends GregtechMeta_MultiBlockBase<GregtechMTE_ElementalDuplicator>
        implements ISurvivalConstructable {

    private final ArrayList<GT_MetaTileEntity_Hatch_ElementalDataOrbHolder> mReplicatorDataOrbHatches = new ArrayList<>();
    private static final int CASING_TEXTURE_ID = TAE.getIndexFromPage(0, 3);
    private int mCasing = 0;

    public GregtechMTE_ElementalDuplicator(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMTE_ElementalDuplicator(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMTE_ElementalDuplicator(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Replicator";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {

        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Produces Elemental Material from UU Matter")
                .addInfo("Speed: +100% | EU Usage: 100% | Parallel: 8 * Tier").addInfo("Maximum 1x of each bus/hatch.")
                .addInfo("Requires circuit 1-16 in your Data Orb Repository")
                .addInfo("depending on what Data Orb you want to prioritize")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(9, 6, 9, true)
                .addController("Top Center").addCasingInfoMin("Elemental Confinement Shell", 138, false)
                .addCasingInfoMin("Matter Fabricator Casing", 24, false)
                .addCasingInfoMin("Particle Containment Casing", 24, false)
                .addCasingInfoMin("Matter Generation Coil", 24, false)
                .addCasingInfoMin("High Voltage Current Capacitor", 20, false)
                .addCasingInfoMin("Resonance Chamber III", 24, false).addCasingInfoMin("Modulator III", 16, false)
                .addOtherStructurePart("Data Orb Repository", "1x", 1).addInputHatch("Any 1 dot hint", 1)
                .addOutputBus("Any 1 dot hint", 1).addOutputHatch("Any 1 dot hint", 1)
                .addEnergyHatch("Any 1 dot hint", 1).addMaintenanceHatch("Any 1 dot hint", 1)
                .addMufflerHatch("Any 1 dot hint", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<GregtechMTE_ElementalDuplicator> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<GregtechMTE_ElementalDuplicator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_ElementalDuplicator>builder()

                    // h = Hatch
                    // c = Casing

                    // a = MF Casing 1
                    // b = Matter Gen Coil

                    // d = Current Capacitor
                    // e = Particle

                    // f = Resonance III
                    // g = Modulator III

                    .addShape(
                            STRUCTURE_PIECE_MAIN,
                            (new String[][] {
                                    { "   ccc   ", "  ccccc  ", " ccccccc ", "ccchhhccc", "ccch~hccc", "ccchhhccc",
                                            " ccccccc ", "  ccccc  ", "   ccc   " },
                                    { "   cac   ", "  abfba  ", " abfgfba ", "cbfgdgfbc", "afgdddgfa", "cbfgdgfbc",
                                            " abfgfba ", "  abfba  ", "   cac   " },
                                    { "   cec   ", "  e   e  ", " e     e ", "c   d   c", "e  ddd  e", "c   d   c",
                                            " e     e ", "  e   e  ", "   cec   " },
                                    { "   cec   ", "  e   e  ", " e     e ", "c   d   c", "e  ddd  e", "c   d   c",
                                            " e     e ", "  e   e  ", "   cec   " },
                                    { "   cac   ", "  abfba  ", " abfgfba ", "cbfgdgfbc", "afgdddgfa", "cbfgdgfbc",
                                            " abfgfba ", "  abfba  ", "   cac   " },
                                    { "   ccc   ", "  ccccc  ", " ccccccc ", "ccchhhccc", "ccchhhccc", "ccchhhccc",
                                            " ccccccc ", "  ccccc  ", "   ccc   " }, }))
                    .addElement('a', ofBlock(getCasingBlock4(), getCasingMeta6()))
                    .addElement('b', ofBlock(getCasingBlock4(), getCasingMeta7()))
                    .addElement('d', ofBlock(getCasingBlock2(), getCasingMeta2()))
                    .addElement('e', ofBlock(getCasingBlock2(), getCasingMeta3()))
                    .addElement('f', ofBlock(getCasingBlock3(), getCasingMeta4()))
                    .addElement('g', ofBlock(getCasingBlock3(), getCasingMeta5()))
                    .addElement(
                            'c',
                            lazy(t -> onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
                    .addElement(
                            'h',
                            lazy(
                                    t -> ofChain(
                                            buildHatchAdder(GregtechMTE_ElementalDuplicator.class).atLeast(
                                                    InputHatch,
                                                    OutputBus,
                                                    OutputHatch,
                                                    Maintenance,
                                                    Muffler,
                                                    Energy).casingIndex(getCasingTextureIndex()).dot(1).build(),
                                            buildHatchAdder(GregtechMTE_ElementalDuplicator.class)
                                                    .hatchClass(GT_MetaTileEntity_Hatch_ElementalDataOrbHolder.class)
                                                    .shouldReject(x -> x.mReplicatorDataOrbHatches.size() >= 1)
                                                    .adder(GregtechMTE_ElementalDuplicator::addDataOrbHatch)
                                                    .casingIndex(getCasingTextureIndex()).dot(1).build(),
                                            onElementPass(
                                                    x -> ++x.mCasing,
                                                    ofBlock(getCasingBlock(), getCasingMeta())))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 4, 0);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        boolean aDidBuild = checkPiece(STRUCTURE_PIECE_MAIN, 4, 4, 0);
        if (this.mInputHatches.size() != 1 || (this.mOutputBusses.size() != 1 && this.mOutputHatches.size() != 0)
                || this.mEnergyHatches.size() != 1
                || this.mReplicatorDataOrbHatches.size() != 1) {
            return false;
        }
        log("Casings: " + mCasing);
        return aDidBuild && mCasing >= 138 && checkHatch();
    }

    @Override
    public int survivalConstruct(ItemStack itemStack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, itemStack, 4, 4, 0, elementBudget, env, false, true);
    }

    protected static int getCasingTextureIndex() {
        return CASING_TEXTURE_ID;
    }

    protected static Block getCasingBlock() {
        return ModBlocks.blockCasings5Misc;
    }

    protected static Block getCasingBlock2() {
        return ModBlocks.blockSpecialMultiCasings;
    }

    protected static Block getCasingBlock3() {
        return ModBlocks.blockSpecialMultiCasings2;
    }

    protected static Block getCasingBlock4() {
        return ModBlocks.blockCasingsMisc;
    }

    protected static int getCasingMeta() {
        return 3;
    }

    protected static int getCasingMeta2() {
        return 12;
    }

    protected static int getCasingMeta3() {
        return 13;
    }

    protected static int getCasingMeta4() {
        return 2;
    }

    protected static int getCasingMeta5() {
        return 6;
    }

    protected static int getCasingMeta6() {
        return 9;
    }

    protected static int getCasingMeta7() {
        return 8;
    }

    private boolean addDataOrbHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            }
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_ElementalDataOrbHolder) {
                try {
                    return addToMachineListInternal(mReplicatorDataOrbHatches, aMetaTileEntity, aBaseCasingIndex);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        return false;
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
        return CASING_TEXTURE_ID;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.replicatorRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2F).enablePerfectOverclock()
                .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        for (GT_MetaTileEntity_Hatch_ElementalDataOrbHolder hatch : filterValidMTEs(mReplicatorDataOrbHatches)) {
            ItemStack orb = hatch.getOrbByCircuit();
            logic.setSpecialSlotItem(orb);
            break;
        }
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

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mReplicatorDataOrbHatches.clear();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> tItems = super.getStoredInputs();
        for (GT_MetaTileEntity_Hatch_ElementalDataOrbHolder tHatch : filterValidMTEs(mReplicatorDataOrbHatches)) {
            tItems.add(tHatch.getOrbByCircuit());
        }
        tItems.removeAll(Collections.singleton(null));
        return tItems;
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }
}
