package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
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
import static gregtech.api.util.GTStructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gregtech.common.tileentities.machines.MTEHatchOutputME;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;

public class MTEAdvDistillationTower extends GTPPMultiBlockBase<MTEAdvDistillationTower>
    implements ISurvivalConstructable {

    private Mode mMode = Mode.DistillationTower;

    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String STRUCTURE_PIECE_LAYER = "layer";
    protected static final String STRUCTURE_PIECE_LAYER_HINT = "layerHint";
    protected static final String STRUCTURE_PIECE_TOP_HINT = "topHint";
    protected static final int DT_MODE_MAX_PARALLELS = 12;

    protected final List<List<MTEHatchOutput>> mOutputHatchesByLayer = new ArrayList<>();
    protected int mHeight;
    protected int mCasing;
    protected boolean mTopLayerFound;

    private static IStructureDefinition<MTEAdvDistillationTower> STRUCTURE_DEFINITION = null;

    public MTEAdvDistillationTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAdvDistillationTower(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvDistillationTower(this.mName);
    }

    @Override
    public IStructureDefinition<MTEAdvDistillationTower> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            IHatchElement<MTEAdvDistillationTower> layeredOutputHatch = OutputHatch
                .withCount(MTEAdvDistillationTower::getCurrentLayerOutputHatchCount)
                .withAdder(MTEAdvDistillationTower::addLayerOutputHatch);
            STRUCTURE_DEFINITION = StructureDefinition.<MTEAdvDistillationTower>builder()
                .addShape(STRUCTURE_PIECE_BASE, transpose(new String[][] { { "b~b", "bbb", "bbb" }, }))
                .addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][] { { "lll", "lcl", "lll" }, }))
                .addShape(STRUCTURE_PIECE_LAYER_HINT, transpose(new String[][] { { "lll", "l-l", "lll" }, }))
                .addShape(STRUCTURE_PIECE_TOP_HINT, transpose(new String[][] { { "ttt", "ttt", "ttt" }, }))
                .addElement(
                    'b',
                    ofChain(
                        buildHatchAdder(MTEAdvDistillationTower.class)
                            .atLeast(Energy, OutputBus, InputHatch, InputBus, Maintenance)
                            .disallowOnly(ForgeDirection.UP)
                            .casingIndex(getCasingTextureId())
                            .hint(1)
                            .build(),
                        ofBlock(GregTechAPI.sBlockCasings4, 1)))
                .addElement(
                    'l',
                    ofChain(
                        buildHatchAdder(MTEAdvDistillationTower.class).atLeast(layeredOutputHatch, Energy, Maintenance)
                            .disallowOnly(ForgeDirection.UP, ForgeDirection.DOWN)
                            .casingIndex(getCasingTextureId())
                            .hint(2)
                            .build(),
                        ofHatchAdder(MTEAdvDistillationTower::addMufflerToMachineList, getCasingTextureId(), 3),
                        ofBlock(GregTechAPI.sBlockCasings4, 1)))
                .addElement(
                    'c',
                    ofChain(
                        onElementPass(
                            MTEAdvDistillationTower::onTopLayerFound,
                            ofHatchAdder(MTEAdvDistillationTower::addMufflerToMachineList, getCasingTextureId(), 3)),
                        onElementPass(
                            MTEAdvDistillationTower::onTopLayerFound,
                            ofHatchAdder(MTEAdvDistillationTower::addOutputToMachineList, getCasingTextureId(), 3)),
                        onElementPass(
                            MTEAdvDistillationTower::onTopLayerFound,
                            ofHatchAdder(
                                MTEAdvDistillationTower::addMaintenanceToMachineList,
                                getCasingTextureId(),
                                3)),
                        onElementPass(MTEAdvDistillationTower::onTopLayerFound, ofBlock(GregTechAPI.sBlockCasings4, 1)),
                        isAir()))
                .addElement(
                    't',
                    buildHatchAdder(MTEAdvDistillationTower.class).atLeast(layeredOutputHatch, Muffler)
                        .disallowOnly(ForgeDirection.DOWN)
                        .casingIndex(getCasingTextureId())
                        .hint(2)
                        .buildAndChain(GregTechAPI.sBlockCasings4, 1))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    protected int getCurrentLayerOutputHatchCount() {
        return mOutputHatchesByLayer.size() < mHeight || mHeight <= 0 ? 0
            : mOutputHatchesByLayer.get(mHeight - 1)
                .size();
    }

    protected boolean addLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead()
            || !(aTileEntity.getMetaTileEntity() instanceof MTEHatchOutput tHatch)) return false;
        while (mOutputHatchesByLayer.size() < mHeight) mOutputHatchesByLayer.add(new ArrayList<>());
        tHatch.updateTexture(aBaseCasingIndex);
        return mOutputHatchesByLayer.get(mHeight - 1)
            .add(tHatch) && mOutputHatches.add(tHatch);
    }

    protected void onTopLayerFound() {
        mTopLayerFound = true;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Stats dictated by tower mode")
            .addInfo("Right click the controller with screwdriver to change mode.")
            .addSeparator()
            .addInfo("Distillery Mode (requires full height tower)")
            .addInfo(TooltipHelper.parallelText("8 * Voltage Tier") + " Parallels")
            .addStaticSpeedInfo(2f)
            .addStaticEuEffInfo(0.15f)
            .addSeparator()
            .addInfo("Distillation Tower Mode")
            .addStaticParallelInfo(DT_MODE_MAX_PARALLELS)
            .addStaticSpeedInfo(3.5f)
            .addStaticEuEffInfo(1f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginVariableStructureBlock(3, 3, 3, 12, 3, 3, true)
            .addController("Front bottom")
            .addCasingInfoMin("Clean Stainless Steel Machine Casing", 7, false)
            .addInputBus("Bottom Casing", 1)
            .addOutputBus("Bottom Casing", 1)
            .addInputHatch("Bottom Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addOutputHatch("One per layer except bottom", 2)
            .addMufflerHatch("Top Casing", 3)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 1, 0, 0);
        // min 2 output layer, so at least 1 + 2 height
        int tTotalHeight = GTStructureChannels.STRUCTURE_HEIGHT.getValueClamped(stackSize, 3, 12);
        for (int i = 1; i < tTotalHeight - 1; i++) {
            buildPiece(STRUCTURE_PIECE_LAYER_HINT, stackSize, hintsOnly, 1, i, 0);
        }
        buildPiece(STRUCTURE_PIECE_TOP_HINT, stackSize, hintsOnly, 1, tTotalHeight - 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        mHeight = 0;
        int built = survivalBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 1, 0, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        // min 2 output layer, so at least 1 + 2 height
        int tTotalHeight = GTStructureChannels.STRUCTURE_HEIGHT.getValueClamped(stackSize, 3, 12);
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // reset
        mOutputHatchesByLayer.forEach(List::clear);
        mHeight = 1;
        mTopLayerFound = false;

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
            if (mTopLayerFound || !mMufflerHatches.isEmpty()) {
                break;
            }
            // not top
            mHeight++;
        }
        boolean check = mTopLayerFound && mHeight >= 2 && checkHatch();
        if (check && mHeight < 11) {
            // force the mode to DT if not in full height
            mMode = Mode.DistillationTower;
            mLastRecipe = null;
        }
        return check;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return mMode.getRecipeMap();
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.distilleryRecipes, RecipeMaps.distillationTowerRecipes);
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a freaking tower, it won't work
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        if (this.mMode == Mode.Distillery)
            return PollutionConfig.pollutionPerSecondMultiAdvDistillationTower_ModeDistillery;
        return PollutionConfig.pollutionPerSecondMultiAdvDistillationTower_ModeDT;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mMode", (byte) mMode.ordinal());
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mMode = Mode.VALUES[aNBT.getByte("mMode")];
        super.loadNBTData(aNBT);
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (mHeight < 11) {
            GTUtility.sendChatToPlayer(aPlayer, "Cannot switch mode if not in full height.");
            return;
        }
        mMode = mMode.next();
        GTUtility.sendChatToPlayer(aPlayer, "Now running in " + mMode + " Mode.");
        mLastRecipe = null;
    }

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        FluidStack copiedFluidStack = aLiquid.copy();
        for (List<MTEHatchOutput> hatches : mOutputHatchesByLayer) {
            if (dumpFluid(hatches, copiedFluidStack, true)) return true;
        }
        for (List<MTEHatchOutput> hatches : mOutputHatchesByLayer) {
            if (dumpFluid(hatches, copiedFluidStack, false)) return true;
        }
        return false;
    }

    @Override
    protected void addFluidOutputs(FluidStack[] outputFluids) {
        if (mMode == Mode.DistillationTower) {
            // dt mode
            for (int i = 0; i < outputFluids.length && i < mOutputHatchesByLayer.size(); i++) {
                FluidStack tStack = outputFluids[i].copy();
                if (!dumpFluid(mOutputHatchesByLayer.get(i), tStack, true))
                    dumpFluid(mOutputHatchesByLayer.get(i), tStack, false);
            }
        } else {
            // distillery mode
            for (FluidStack outputFluidStack : outputFluids) {
                addOutput(outputFluidStack);
            }
        }
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return getFluidOutputSlotsByLayer(toOutput, mOutputHatchesByLayer);
    }

    @Override
    public String getMachineType() {
        return "Distillery, Distillation Tower";
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.setEuModifier(mMode == Mode.Distillery ? 0.15F : 1F);
        logic.setSpeedBonus(mMode == Mode.Distillery ? 1F / 2F : 1F / 3.5F);
    }

    @Override
    public int getMaxParallelRecipes() {
        return switch (mMode) {
            case DistillationTower -> DT_MODE_MAX_PARALLELS;
            case Distillery -> 8 * GTUtility.getTier(this.getMaxInputVoltage());
        };
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
    }

    @Override
    protected int getCasingTextureId() {
        return 49;
    }

    @Override
    public boolean canDumpFluidToME() {
        // All fluids can be dumped to ME only if each layer contains a ME Output Hatch.
        return this.mOutputHatchesByLayer.stream()
            .allMatch(
                tLayerOutputHatches -> tLayerOutputHatches.stream()
                    .anyMatch(tHatch -> (tHatch instanceof MTEHatchOutputME tMEHatch) && (tMEHatch.canFillFluid())));
    }

    private enum Mode {

        DistillationTower(RecipeMaps.distillationTowerRecipes),
        Distillery(RecipeMaps.distilleryRecipes),;

        static final Mode[] VALUES = values();
        private final RecipeMap<?> recipeMap;

        Mode(RecipeMap<?> recipeMap) {
            this.recipeMap = recipeMap;
        }

        public RecipeMap<?> getRecipeMap() {
            return recipeMap;
        }

        public Mode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("mode", getMachineModeName());
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("GT5U.GTPP_MULTI_ADV_DISTILLATION_TOWER.mode." + mMode.ordinal());
    }
}
