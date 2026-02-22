package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTRecipeConstants.SPARGE_MAX_BYPRODUCT;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTESpargeTower extends GTPPMultiBlockBase<MTESpargeTower> implements ISurvivalConstructable {

    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String STRUCTURE_PIECE_LAYER = "layer";
    protected static final String STRUCTURE_PIECE_LAYER_HINT = "layerHint";
    protected static final String STRUCTURE_PIECE_TOP_HINT = "topHint";
    private static final IStructureDefinition<MTESpargeTower> STRUCTURE_DEFINITION;

    static {
        IHatchElement<MTESpargeTower> layeredOutputHatch = OutputHatch
            .withCount(MTESpargeTower::getCurrentLayerOutputHatchCount)
            .withAdder(MTESpargeTower::addLayerOutputHatch);
        STRUCTURE_DEFINITION = StructureDefinition.<MTESpargeTower>builder()
            .addShape(STRUCTURE_PIECE_BASE, transpose(new String[][] { { "b~b", "bbb", "bbb" }, }))
            .addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][] { { "lll", "lcl", "lll" } }))
            .addShape(STRUCTURE_PIECE_LAYER_HINT, transpose(new String[][] { { "lll", "l-l", "lll" } }))
            .addShape(STRUCTURE_PIECE_TOP_HINT, transpose(new String[][] { { "lll", "lll", "lll" } }))
            .addElement(
                'b',
                buildHatchAdder(MTESpargeTower.class).atLeast(Energy, InputHatch, InputBus, Maintenance)
                    .disallowOnly(ForgeDirection.UP)
                    .casingIndex(getCasingIndex())
                    .hint(1)
                    .buildAndChain(
                        onElementPass(MTESpargeTower::onCasingFound, ofBlock(ModBlocks.blockCasings5Misc, 4))))
            .addElement(
                'l',
                ofChain(
                    buildHatchAdder(MTESpargeTower.class).atLeast(layeredOutputHatch)
                        .disallowOnly(ForgeDirection.UP, ForgeDirection.DOWN)
                        .casingIndex(getCasingIndex())
                        .hint(2)
                        .build(),
                    ofHatchAdder(MTESpargeTower::addEnergyInputToMachineList, getCasingIndex(), 2),
                    ofHatchAdder(MTESpargeTower::addMaintenanceToMachineList, getCasingIndex(), 2),
                    onElementPass(MTESpargeTower::onCasingFound, ofBlock(ModBlocks.blockCasings5Misc, 4))))
            .addElement(
                'c',
                ofChain(
                    onElementPass(
                        t -> t.onTopLayerFound(false),
                        ofHatchAdder(MTESpargeTower::addOutputToMachineList, getCasingIndex(), 3)),
                    onElementPass(
                        t -> t.onTopLayerFound(false),
                        ofHatchAdder(MTESpargeTower::addMaintenanceToMachineList, getCasingIndex(), 3)),
                    onElementPass(t -> t.onTopLayerFound(true), ofBlock(ModBlocks.blockCasings5Misc, 4)),
                    isAir()))
            .build();
    }

    protected final List<List<MTEHatchOutput>> mOutputHatchesByLayer = new ArrayList<>();
    protected int mHeight;
    protected int mCasing;
    protected boolean mTopLayerFound;

    public MTESpargeTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESpargeTower(String aName) {
        super(aName);
    }

    public static int getCasingIndex() {
        return 68;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESpargeTower(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Gas Sparge Tower")
            .addInfo("Runs gases through depleted molten salts to extract precious fluids")
            .addInfo("Works the same way as the Distillation Tower, but with a fixed height of 8")
            .addInfo("Fluids are only put out at the correct height")
            .addInfo("The correct height equals the slot number in the NEI recipe")
            .beginStructureBlock(3, 8, 3, true)
            .addController("Front bottom")
            .addOtherStructurePart("Sparge Tower Exterior Casing", "45 (minimum)")
            .addEnergyHatch("Any casing", 1, 2)
            .addMaintenanceHatch("Any casing", 1, 2, 3)
            .addInputHatch("2x Input Hatches (Any bottom layer casing)", 1)
            .addOutputHatch("6x Output Hatches (At least one per layer except bottom layer)", 2, 3)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCASpargeTowerActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCASpargeTowerActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCASpargeTower;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCASpargeTowerGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return getCasingIndex();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.spargeTowerRecipes;
    }

    protected void onCasingFound() {
        mCasing++;
    }

    private static FluidStack[] randomizeByproducts(FluidStack[] outputFluids, int maximum, FluidStack spargeGas) {
        int byproductTotal = 0;

        for (int i = 2; i < outputFluids.length; i++) {
            // At least 1L so all output hatches are guaranteed to be used, and a maximum that shifts so there
            // can always be at least 1L of overflow for the same reason.
            int gasAmount = MathUtils.randInt(1, Math.min(maximum, spargeGas.amount - byproductTotal - 1));
            outputFluids[i] = new FluidStack(outputFluids[i], gasAmount);
            byproductTotal += gasAmount;
        }

        outputFluids[1] = new FluidStack(spargeGas, spargeGas.amount - byproductTotal);
        return outputFluids;
    }

    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull ParallelHelper createParallelHelper(@NotNull GTRecipe recipe) {
                return super.createParallelHelper(modifyRecipe(recipe));
            }

            private GTRecipe modifyRecipe(GTRecipe recipe) {
                GTRecipe newRecipe = recipe.copy();
                newRecipe.mFluidOutputs = randomizeByproducts(
                    recipe.mFluidOutputs,
                    recipe.getMetadataOrDefault(SPARGE_MAX_BYPRODUCT, 0),
                    recipe.mFluidInputs[0]);
                return newRecipe;
            }
        };
    }

    protected void onTopLayerFound(boolean aIsCasing) {
        mTopLayerFound = true;
        if (aIsCasing) {
            onCasingFound();
        }
    }

    protected int getCurrentLayerOutputHatchCount() {
        return mOutputHatchesByLayer.size() < mHeight || mHeight <= 0 ? 0
            : mOutputHatchesByLayer.get(mHeight - 1)
                .size();
    }

    protected boolean addLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead()
            || !(aTileEntity.getMetaTileEntity() instanceof MTEHatchOutput tHatch)) {
            return false;
        }
        while (mOutputHatchesByLayer.size() < mHeight) {
            mOutputHatchesByLayer.add(new ArrayList<>());
        }
        tHatch.updateTexture(aBaseCasingIndex);
        boolean addedHatch = mOutputHatchesByLayer.get(mHeight - 1)
            .add(tHatch);
        return addedHatch;
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return getFluidOutputSlotsByLayer(toOutput, mOutputHatchesByLayer);
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a freaking tower, it won't work
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public IStructureDefinition<MTESpargeTower> getStructureDefinition() {
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
        if (!checkPiece(STRUCTURE_PIECE_BASE, 1, 0, 0)) {
            return false;
        }

        // check each layer
        while (mHeight < 8 && checkPiece(STRUCTURE_PIECE_LAYER, 1, mHeight, 0) && !mTopLayerFound) {
            if (mOutputHatchesByLayer.isEmpty() || mOutputHatchesByLayer.get(mHeight - 1)
                .isEmpty()) {

                return false;
            }
            // not top
            mHeight++;
        }

        return mCasing >= 45 && mTopLayerFound && mMaintenanceHatches.size() == 1;
    }

    @Override
    protected void addFluidOutputs(FluidStack[] outputFluids) {
        for (int i = 0; i < outputFluids.length && i < mOutputHatchesByLayer.size(); i++) {
            FluidStack tStack = outputFluids[i] != null ? outputFluids[i].copy() : null;
            if (tStack == null) {
                continue;
            }
            if (!dumpFluid(mOutputHatchesByLayer.get(i), tStack, true)) {
                dumpFluid(mOutputHatchesByLayer.get(i), tStack, false);
            }
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 1, 0, 0);
        int tTotalHeight = 8; // min 2 output layer, so at least 1 + 2 height
        for (int i = 1; i < tTotalHeight - 1; i++) {
            buildPiece(STRUCTURE_PIECE_LAYER_HINT, stackSize, hintsOnly, 1, i, 0);
        }
        buildPiece(STRUCTURE_PIECE_TOP_HINT, stackSize, hintsOnly, 1, tTotalHeight - 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        mHeight = 0;
        int built = survivalBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 1, 0, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int tTotalHeight = 8; // min 2 output layer, so at least 1 + 2 height
        for (int i = 1; i < tTotalHeight - 1; i++) {
            mHeight = i;
            built = survivalBuildPiece(STRUCTURE_PIECE_LAYER_HINT, stackSize, 1, i, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        mHeight = tTotalHeight - 1;
        return survivalBuildPiece(
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
    public String getMachineType() {
        return "Gas Sparger";
    }

    @Override
    public boolean onPlungerRightClick(EntityPlayer aPlayer, ForgeDirection side, float aX, float aY, float aZ) {
        int aLayerIndex = 0;
        GTUtility.sendChatToPlayer(
            aPlayer,
            "Trying to clear " + mOutputHatchesByLayer.size() + " layers of output hatches.");
        for (List<MTEHatchOutput> layer : this.mOutputHatchesByLayer) {
            int aHatchIndex = 0;
            for (MTEHatchOutput hatch : layer) {
                if (hatch.mFluid != null) {
                    GTUtility.sendChatToPlayer(
                        aPlayer,
                        "Clearing " + hatch.mFluid.amount
                            + "L of "
                            + hatch.mFluid.getLocalizedName()
                            + " from hatch "
                            + aHatchIndex
                            + " on layer "
                            + aLayerIndex
                            + ".");
                    hatch.mFluid = null;
                }
                aHatchIndex++;
            }
            aLayerIndex++;
        }
        return aLayerIndex > 0;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // Ensure that lEUt is negative from loaded NBT data, since this multi consumes EU
        if (lEUt > 0) {
            lEUt = (-lEUt);
        }
    }
}
