package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
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
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

public class GregtechMetaTileEntity_Adv_DistillationTower extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_Adv_DistillationTower> implements ISurvivalConstructable {

    private Mode mMode = Mode.DistillationTower;
    private boolean mUpgraded = false;

    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String STRUCTURE_PIECE_LAYER = "layer";
    protected static final String STRUCTURE_PIECE_LAYER_HINT = "layerHint";
    protected static final String STRUCTURE_PIECE_TOP_HINT = "topHint";

    protected final List<List<GT_MetaTileEntity_Hatch_Output>> mOutputHatchesByLayer = new ArrayList<>();
    protected int mHeight;
    protected int mCasing;
    protected boolean mTopLayerFound;

    private static IStructureDefinition<GregtechMetaTileEntity_Adv_DistillationTower> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_Adv_DistillationTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_Adv_DistillationTower(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Adv_DistillationTower(this.mName);
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_Adv_DistillationTower> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            IHatchElement<GregtechMetaTileEntity_Adv_DistillationTower> layeredOutputHatch = OutputHatch
                    .withCount(GregtechMetaTileEntity_Adv_DistillationTower::getCurrentLayerOutputHatchCount)
                    .withAdder(GregtechMetaTileEntity_Adv_DistillationTower::addLayerOutputHatch);
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_Adv_DistillationTower>builder()
                    .addShape(STRUCTURE_PIECE_BASE, transpose(new String[][] { { "b~b", "bbb", "bbb" }, }))
                    .addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][] { { "lll", "lcl", "lll" }, }))
                    .addShape(STRUCTURE_PIECE_LAYER_HINT, transpose(new String[][] { { "lll", "l-l", "lll" }, }))
                    .addShape(STRUCTURE_PIECE_TOP_HINT, transpose(new String[][] { { "ttt", "ttt", "ttt" }, }))
                    .addElement(
                            'b',
                            ofChain(
                                    buildHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower.class)
                                            .atLeast(Energy, OutputBus, InputHatch, InputBus, Maintenance)
                                            .disallowOnly(ForgeDirection.UP).casingIndex(getCasingTextureId()).dot(1)
                                            .build(),
                                    ofBlock(GregTech_API.sBlockCasings4, 1)))
                    .addElement(
                            'l',
                            ofChain(
                                    buildHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower.class)
                                            .atLeast(layeredOutputHatch, Energy, Maintenance)
                                            .disallowOnly(ForgeDirection.UP, ForgeDirection.DOWN)
                                            .casingIndex(getCasingTextureId()).dot(2).build(),
                                    ofHatchAdder(
                                            GregtechMetaTileEntity_Adv_DistillationTower::addMufflerToMachineList,
                                            getCasingTextureId(),
                                            3),
                                    ofBlock(GregTech_API.sBlockCasings4, 1)))
                    .addElement(
                            'c',
                            ofChain(
                                    onElementPass(
                                            GregtechMetaTileEntity_Adv_DistillationTower::onTopLayerFound,
                                            ofHatchAdder(
                                                    GregtechMetaTileEntity_Adv_DistillationTower::addMufflerToMachineList,
                                                    getCasingTextureId(),
                                                    3)),
                                    onElementPass(
                                            GregtechMetaTileEntity_Adv_DistillationTower::onTopLayerFound,
                                            ofHatchAdder(
                                                    GregtechMetaTileEntity_Adv_DistillationTower::addOutputToMachineList,
                                                    getCasingTextureId(),
                                                    3)),
                                    onElementPass(
                                            GregtechMetaTileEntity_Adv_DistillationTower::onTopLayerFound,
                                            ofHatchAdder(
                                                    GregtechMetaTileEntity_Adv_DistillationTower::addMaintenanceToMachineList,
                                                    getCasingTextureId(),
                                                    3)),
                                    onElementPass(
                                            GregtechMetaTileEntity_Adv_DistillationTower::onTopLayerFound,
                                            ofBlock(GregTech_API.sBlockCasings4, 1)),
                                    isAir()))
                    .addElement(
                            't',
                            buildHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower.class)
                                    .atLeast(layeredOutputHatch, Muffler).disallowOnly(ForgeDirection.DOWN)
                                    .casingIndex(getCasingTextureId()).dot(2)
                                    .buildAndChain(GregTech_API.sBlockCasings4, 1))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    protected int getCurrentLayerOutputHatchCount() {
        return mOutputHatchesByLayer.size() < mHeight || mHeight <= 0 ? 0
                : mOutputHatchesByLayer.get(mHeight - 1).size();
    }

    protected boolean addLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead()
                || !(aTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Output tHatch))
            return false;
        while (mOutputHatchesByLayer.size() < mHeight) mOutputHatchesByLayer.add(new ArrayList<>());
        tHatch.updateTexture(aBaseCasingIndex);
        return mOutputHatchesByLayer.get(mHeight - 1).add(tHatch) && mOutputHatches.add(tHatch);
    }

    protected void onTopLayerFound() {
        mTopLayerFound = true;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Advanced Distillation Tower")
                .addInfo("Use 85% less energy in distillery mode").addInfo("250%/100% faster in DT/distillery mode")
                .addInfo("Right click the controller with screwdriver to change mode.")
                .addInfo("Max parallel dictated by tower tier and mode").addInfo("DTower Mode: T1=4, T2=12")
                .addInfo("Distilery Mode: Tower Tier * (4*InputTier)")
                .addInfo("Distilery Mode require a full height tower").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().addCasingInfoMin("Clean Stainless Steel Machine Casing", 7, false)
                .addInputBus("Bottom Casing", 1).addOutputBus("Bottom Casing", 1).addInputHatch("Bottom Casing", 1)
                .addMaintenanceHatch("Any Casing", 1).addEnergyHatch("Any Casing", 1)
                .addOutputHatch("One per layer except bottom", 2).addMufflerHatch("Top Casing", 3)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
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
            if (mOutputHatchesByLayer.size() < mHeight || mOutputHatchesByLayer.get(mHeight - 1).isEmpty())
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a freaking tower, it won't work
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        if (this.mMode == Mode.Distillery)
            return CORE.ConfigSwitches.pollutionPerSecondMultiAdvDistillationTower_ModeDistillery;
        return CORE.ConfigSwitches.pollutionPerSecondMultiAdvDistillationTower_ModeDT;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mMode", (byte) mMode.ordinal());
        aNBT.setBoolean("mUpgraded", mUpgraded);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mMode = Mode.values()[aNBT.getByte("mMode")];
        mUpgraded = aNBT.getBoolean("mUpgraded");
        super.loadNBTData(aNBT);
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (mHeight < 11) {
            PlayerUtils.messagePlayer(aPlayer, "Cannot switch mode if not in full height.");
            return;
        }
        mMode = mMode.next();
        PlayerUtils.messagePlayer(aPlayer, "Now running in " + mMode + " Mode.");
        mLastRecipe = null;
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
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        FluidStack copiedFluidStack = aLiquid.copy();
        for (List<GT_MetaTileEntity_Hatch_Output> hatches : mOutputHatchesByLayer) {
            if (dumpFluid(hatches, copiedFluidStack, true)) return true;
        }
        for (List<GT_MetaTileEntity_Hatch_Output> hatches : mOutputHatchesByLayer) {
            if (dumpFluid(hatches, copiedFluidStack, false)) return true;
        }
        return false;
    }

    @Override
    protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
        if (mMode == Mode.DistillationTower) {
            // dt mode
            for (int i = 0; i < mOutputFluids2.length && i < mOutputHatchesByLayer.size(); i++) {
                FluidStack tStack = mOutputFluids2[i].copy();
                if (!dumpFluid(mOutputHatchesByLayer.get(i), tStack, true))
                    dumpFluid(mOutputHatchesByLayer.get(i), tStack, false);
            }
        } else {
            // distillery mode
            for (FluidStack outputFluidStack : mOutputFluids2) {
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
        return new ProcessingLogic().setMaxParallelSupplier(this::getMaxParallelRecipes);
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
            case DistillationTower -> getTierOfTower() == 1 ? 4 : getTierOfTower() == 2 ? 12 : 0;
            case Distillery -> getTierOfTower() * (4 * GT_Utility.getTier(this.getMaxInputVoltage()));
            default -> 0;
        };
    }

    private int getTierOfTower() {
        return mUpgraded ? 2 : 1;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
    }

    @Override
    protected int getCasingTextureId() {
        return 49;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 20 == 0 && !mUpgraded) {
            ItemStack aGuiStack = this.getControllerSlot();
            if (aGuiStack != null) {
                if (GT_Utility.areStacksEqual(aGuiStack, GregtechItemList.Distillus_Upgrade_Chip.get(1))) {
                    this.mUpgraded = true;
                    mInventory[1] = ItemUtils.depleteStack(aGuiStack);
                }
            }
        }
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        aNBT.setBoolean("mUpgraded", mUpgraded);
        super.setItemNBT(aNBT);
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        super.addAdditionalTooltipInformation(stack, tooltip);
        NBTTagCompound aNBT = stack.getTagCompound();
        if (aNBT != null && aNBT.hasKey("mUpgraded")) {
            tooltip.add(StatCollector.translateToLocal("tooltip.large_distill_tower.upgraded"));
        }
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
}
