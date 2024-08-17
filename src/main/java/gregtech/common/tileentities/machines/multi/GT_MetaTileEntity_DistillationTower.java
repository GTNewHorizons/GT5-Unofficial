package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;

public class GT_MetaTileEntity_DistillationTower extends
    GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_DistillationTower> implements ISurvivalConstructable {

    protected static final int CASING_INDEX = 49;
    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String STRUCTURE_PIECE_LAYER = "layer";
    protected static final String STRUCTURE_PIECE_LAYER_HINT = "layerHint";
    protected static final String STRUCTURE_PIECE_TOP_HINT = "topHint";
    private static final IStructureDefinition<GT_MetaTileEntity_DistillationTower> STRUCTURE_DEFINITION;

    static {
        IHatchElement<GT_MetaTileEntity_DistillationTower> layeredOutputHatch = OutputHatch
            .withCount(GT_MetaTileEntity_DistillationTower::getCurrentLayerOutputHatchCount)
            .withAdder(GT_MetaTileEntity_DistillationTower::addLayerOutputHatch);
        STRUCTURE_DEFINITION = StructureDefinition.<GT_MetaTileEntity_DistillationTower>builder()
            .addShape(STRUCTURE_PIECE_BASE, transpose(new String[][] { { "b~b", "bbb", "bbb" }, }))
            .addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][] { { "lll", "lcl", "lll" }, }))
            .addShape(STRUCTURE_PIECE_LAYER_HINT, transpose(new String[][] { { "lll", "l-l", "lll" }, }))
            .addShape(STRUCTURE_PIECE_TOP_HINT, transpose(new String[][] { { "LLL", "LLL", "LLL" }, }))
            .addElement(
                'b',
                ofChain(
                    buildHatchAdder(GT_MetaTileEntity_DistillationTower.class)
                        .atLeast(Energy, OutputBus, InputHatch, InputBus, Maintenance)
                        .casingIndex(CASING_INDEX)
                        .dot(1)
                        .build(),
                    onElementPass(
                        GT_MetaTileEntity_DistillationTower::onCasingFound,
                        ofBlock(GregTech_API.sBlockCasings4, 1))))
            .addElement(
                'l',
                ofChain(
                    buildHatchAdder(GT_MetaTileEntity_DistillationTower.class).atLeast(layeredOutputHatch)
                        .casingIndex(CASING_INDEX)
                        .dot(2)
                        .disallowOnly(ForgeDirection.UP, ForgeDirection.DOWN)
                        .build(),
                    ofHatchAdder(GT_MetaTileEntity_DistillationTower::addEnergyInputToMachineList, CASING_INDEX, 2),
                    ofHatchAdder(GT_MetaTileEntity_DistillationTower::addLayerOutputHatch, CASING_INDEX, 2),
                    ofHatchAdder(GT_MetaTileEntity_DistillationTower::addMaintenanceToMachineList, CASING_INDEX, 2),
                    onElementPass(
                        GT_MetaTileEntity_DistillationTower::onCasingFound,
                        ofBlock(GregTech_API.sBlockCasings4, 1))))
            // hint element only used in top layer
            .addElement(
                'L',
                buildHatchAdder(GT_MetaTileEntity_DistillationTower.class).atLeast(layeredOutputHatch)
                    .casingIndex(CASING_INDEX)
                    .dot(2)
                    .disallowOnly(ForgeDirection.UP)
                    .buildAndChain(GregTech_API.sBlockCasings4, 1))
            .addElement(
                'c',
                ofChain(
                    onElementPass(
                        t -> t.onTopLayerFound(false),
                        ofHatchAdder(GT_MetaTileEntity_DistillationTower::addOutputToMachineList, CASING_INDEX, 3)),
                    onElementPass(
                        t -> t.onTopLayerFound(false),
                        ofHatchAdder(
                            GT_MetaTileEntity_DistillationTower::addMaintenanceToMachineList,
                            CASING_INDEX,
                            3)),
                    onElementPass(t -> t.onTopLayerFound(true), ofBlock(GregTech_API.sBlockCasings4, 1)),
                    isAir()))
            .build();
    }

    protected final List<List<GT_MetaTileEntity_Hatch_Output>> mOutputHatchesByLayer = new ArrayList<>();
    protected int mHeight;
    protected int mCasing;
    protected boolean mTopLayerFound;

    public GT_MetaTileEntity_DistillationTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_DistillationTower(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DistillationTower(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Distillery")
            .addInfo("Controller block for the Distillation Tower")
            .addInfo("Fluids are only put out at the correct height")
            .addInfo("The correct height equals the slot number in the NEI recipe")
            .addSeparator()
            .beginVariableStructureBlock(3, 3, 3, 12, 3, 3, true)
            .addController("Front bottom")
            .addOtherStructurePart("Clean Stainless Steel Machine Casing", "7 x h - 5 (minimum)")
            .addEnergyHatch("Any casing except top centre", 1, 2)
            .addMaintenanceHatch("Any casing", 1, 2, 3)
            .addInputHatch("Any bottom layer casing", 1)
            .addOutputBus("Any bottom layer casing", 1)
            .addOutputHatch("2-11x Output Hatches (At least one per layer except bottom layer)", 2, 3)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.distillationTowerRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic();
    }

    protected void onCasingFound() {
        mCasing++;
    }

    protected void onTopLayerFound(boolean aIsCasing) {
        mTopLayerFound = true;
        if (aIsCasing) onCasingFound();
    }

    protected int getCurrentLayerOutputHatchCount() {
        return mOutputHatchesByLayer.size() < mHeight || mHeight <= 0 ? 0
            : mOutputHatchesByLayer.get(mHeight - 1)
                .size();
    }

    protected boolean addLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead()
            || !(aTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Output tHatch)) return false;
        while (mOutputHatchesByLayer.size() < mHeight) mOutputHatchesByLayer.add(new ArrayList<>());
        tHatch.updateTexture(aBaseCasingIndex);
        return mOutputHatchesByLayer.get(mHeight - 1)
            .add(tHatch);
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return getFluidOutputSlotsByLayer(toOutput, mOutputHatchesByLayer);
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a freaking tower, it won't work
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return false;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_DistillationTower> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // reset
        mOutputHatchesByLayer.forEach(List::clear);
        mHeight = 1;
        mTopLayerFound = false;
        mCasing = 0;

        // check base
        if (!checkPiece(STRUCTURE_PIECE_BASE, 1, 0, 0)) return false;

        // check each layer
        while (mHeight < 12) {
            if (!checkPiece(STRUCTURE_PIECE_LAYER, 1, mHeight, 0)) {
                return false;
            }
            if (mOutputHatchesByLayer.size() < mHeight || mOutputHatchesByLayer.get(mHeight - 1)
                .isEmpty())
                // layer without output hatch
                return false;
            if (mTopLayerFound) {
                break;
            }
            // not top
            mHeight++;
        }

        // validate final invariants... (actual height is mHeight+1)
        return mCasing >= 7 * (mHeight + 1) - 5 && mHeight + 1 >= 3
            && mTopLayerFound
            && mMaintenanceHatches.size() == 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for (int i = 0; i < mOutputFluids2.length && i < mOutputHatchesByLayer.size(); i++) {
            final FluidStack fluidStack = mOutputFluids2[i];
            if (fluidStack == null) continue;
            FluidStack tStack = fluidStack.copy();
            if (!dumpFluid(mOutputHatchesByLayer.get(i), tStack, true))
                dumpFluid(mOutputHatchesByLayer.get(i), tStack, false);
        }
    }

    @Override
    public boolean canDumpFluidToME() {
        // All fluids can be dumped to ME only if each layer contains a ME Output Hatch.
        return this.mOutputHatchesByLayer.stream()
            .allMatch(
                tLayerOutputHatches -> tLayerOutputHatches.stream()
                    .anyMatch(
                        tHatch -> (tHatch instanceof GT_MetaTileEntity_Hatch_Output_ME tMEHatch)
                            && (tMEHatch.canAcceptFluid())));
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 1, 0, 0);
        int tTotalHeight = Math.min(12, stackSize.stackSize + 2); // min 2 output layer, so at least 1 + 2 height
        for (int i = 1; i < tTotalHeight - 1; i++) {
            buildPiece(STRUCTURE_PIECE_LAYER_HINT, stackSize, hintsOnly, 1, i, 0);
        }
        buildPiece(STRUCTURE_PIECE_TOP_HINT, stackSize, hintsOnly, 1, tTotalHeight - 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        mHeight = 0;
        int built = survivialBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 1, 0, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int tTotalHeight = Math.min(12, stackSize.stackSize + 2); // min 2 output layer, so at least 1 + 2 height
        for (int i = 1; i < tTotalHeight - 1; i++) {
            mHeight = i;
            built = survivialBuildPiece(
                STRUCTURE_PIECE_LAYER_HINT,
                stackSize,
                1,
                i,
                0,
                elementBudget,
                env,
                false,
                true);
            if (built >= 0) return built;
        }
        mHeight = tTotalHeight - 1;
        return survivialBuildPiece(
            STRUCTURE_PIECE_TOP_HINT,
            stackSize,
            1,
            tTotalHeight - 1,
            0,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GT_MACHINES_DISTILLERY_LOOP;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
