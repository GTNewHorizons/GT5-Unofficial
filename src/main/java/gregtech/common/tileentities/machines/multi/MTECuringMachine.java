package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.api.util.GTStructureUtility.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILayerProducer;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTELayerSignal;

public class MTECuringMachine extends MTEExtendedPowerMultiBlockBase<MTECuringMachine>
    implements ISurvivalConstructable, ILayerProducer {

    private static IStructureDefinition<MTECuringMachine> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;

    private static final int PARALLEL_PER_TIER = 4;
    private static final float SPEED = 1f;
    private static final float EU_EFFICIENCY = 1f;

    public enum ChallengePhase {
        NEED_BOTH,
        NEED_ITEM,
        NEED_FLUID
    }

    private ChallengePhase phase = ChallengePhase.NEED_BOTH;
    private GTRecipe lockedRecipe;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("challengePhase", phase.ordinal());
        if (lockedRecipe != null) {
            aNBT.setTag("lockedRecipe", writeRecipe(lockedRecipe));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        ChallengePhase[] values = ChallengePhase.values();
        int idx = aNBT.getInteger("challengePhase");
        phase = (idx >= 0 && idx < values.length) ? values[idx] : ChallengePhase.NEED_BOTH;
        if (aNBT.hasKey("lockedRecipe")) {
            lockedRecipe = readRecipe(aNBT.getCompoundTag("lockedRecipe"));
        }
        // default
        if (lockedRecipe == null) {
            phase = ChallengePhase.NEED_BOTH;
        }
    }

    private static NBTTagCompound writeRecipe(GTRecipe r) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("in", writeItems(r.mInputs));
        tag.setTag("out", writeItems(r.mOutputs));
        tag.setTag("fin", writeFluids(r.mFluidInputs));
        tag.setTag("fout", writeFluids(r.mFluidOutputs));
        if (r.mInputChances != null) tag.setIntArray("inC", r.mInputChances);
        if (r.mOutputChances != null) tag.setIntArray("outC", r.mOutputChances);
        if (r.mFluidInputChances != null) tag.setIntArray("finC", r.mFluidInputChances);
        if (r.mFluidOutputChances != null) tag.setIntArray("foutC", r.mFluidOutputChances);
        tag.setInteger("dur", r.mDuration);
        tag.setInteger("eut", r.mEUt);
        tag.setInteger("sv", r.mSpecialValue);
        return tag;
    }

    private static GTRecipe readRecipe(NBTTagCompound tag) {
        ItemStack[] in = readItems(tag.getTagList("in", 10));
        ItemStack[] out = readItems(tag.getTagList("out", 10));
        FluidStack[] fin = readFluids(tag.getTagList("fin", 10));
        FluidStack[] fout = readFluids(tag.getTagList("fout", 10));
        int[] inC = tag.hasKey("inC") ? tag.getIntArray("inC") : null;
        int[] outC = tag.hasKey("outC") ? tag.getIntArray("outC") : null;
        int[] finC = tag.hasKey("finC") ? tag.getIntArray("finC") : null;
        int[] foutC = tag.hasKey("foutC") ? tag.getIntArray("foutC") : null;
        return new GTRecipe(
            false,
            in,
            out,
            null,
            inC,
            outC,
            finC,
            foutC,
            fin,
            fout,
            tag.getInteger("dur"),
            tag.getInteger("eut"),
            tag.getInteger("sv"));
    }

    private static NBTTagList writeItems(ItemStack[] l) {
        NBTTagList out = new NBTTagList();
        if (l != null) for (ItemStack s : l) {
            NBTTagCompound c = new NBTTagCompound();
            if (s != null) s.writeToNBT(c);
            out.appendTag(c);
        }
        return out;
    }

    private static ItemStack[] readItems(NBTTagList l) {
        ItemStack[] out = new ItemStack[l.tagCount()];
        for (int i = 0; i < out.length; i++) out[i] = ItemStack.loadItemStackFromNBT(l.getCompoundTagAt(i));
        return out;
    }

    private static NBTTagList writeFluids(FluidStack[] l) {
        NBTTagList out = new NBTTagList();
        if (l != null) for (FluidStack f : l) {
            NBTTagCompound c = new NBTTagCompound();
            if (f != null) f.writeToNBT(c);
            out.appendTag(c);
        }
        return out;
    }

    private static FluidStack[] readFluids(NBTTagList l) {
        FluidStack[] out = new FluidStack[l.tagCount()];
        for (int i = 0; i < out.length; i++) out[i] = FluidStack.loadFluidStackFromNBT(l.getCompoundTagAt(i));
        return out;
    }

    public MTECuringMachine(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECuringMachine(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTECuringMachine(this.mName);
    }

    @Override
    public IStructureDefinition<MTECuringMachine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTECuringMachine>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    // spotless:off
                    new String[][]{{
                        "     ",
                        " EEE ",
                        "     ",
                        "     ",
                        " E~E ",
                        " EEE "
                    },{
                        " EEE ",
                        "EFFFE",
                        " AAA ",
                        " AAA ",
                        "EGGGE",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "EFDFE",
                        " A A ",
                        " A A ",
                        "EGCGE",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "EFFFE",
                        " AAA ",
                        "EAAAE",
                        "EGGGE",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "E   E",
                        "EBBBE",
                        "EBBBE",
                        "E   E",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "EE EE",
                        "H B H",
                        "H B H",
                        "EE EE",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "E   E",
                        "EB BE",
                        "EB BE",
                        "E E E",
                        "EEEEE"
                    },{
                        " EEE ",
                        " EEE ",
                        " EHE ",
                        " EHE ",
                        " EEE ",
                        " EEE "
                    }})
                // spotless:on
                .addElement('A', chainAllGlasses())
                .addElement('B', Casings.SolidifierRadiator.asElement())
                .addElement('C', Casings.FluxedElectrumItemPipeCasing.asElement())
                .addElement('D', Casings.TungstensteelPipeCasing.asElement())
                .addElement(
                    'E',
                    buildHatchAdder(MTECuringMachine.class)
                        .atLeast(
                            InputBus,
                            OutputBus,
                            Maintenance,
                            Energy,
                            MultiAmpEnergy,
                            InputHatch,
                            MTELayerSignal.LayerSignalHatchElement.LayerSignal)
                        .casingIndex(Casings.RadiantNaquadahAlloyCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(
                                MTECuringMachine::onCasingAdded,
                                Casings.RadiantNaquadahAlloyCasing.asElement())))
                .addElement('F', Casings.HighEnergyUltravioletEmitterCasing.asElement())
                .addElement('G', Casings.UVSolenoidSuperconductorCoil.asElement())
                .addElement('H', ofFrame(Materials.Infinity))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private final ArrayList<MTELayerSignal> signalHatches = new ArrayList<>();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.RadiantNaquadahAlloyCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE)
                    .extFacing()
                    .build() };
            return new ITexture[] { Casings.RadiantNaquadahAlloyCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_MULTI_BREWERY)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Casings.RadiantNaquadahAlloyCasing.getCasingTexture() };
    }

    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(
            StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.machinetype"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.tooltip1"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.tooltip2"))
            .addSeparator()
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.tooltip3"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.tooltip4"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.tooltip5"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.tooltip6"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.tooltip7"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.tooltip8"))
            .addSeparator()
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.curingmachine.tooltip9"))
            .addBulkMachineInfo(PARALLEL_PER_TIER, SPEED, EU_EFFICIENCY)
            .beginStructureBlock(5, 6, 8, false)
            .addController("Front center, 2nd layer")
            .addCasing("6-121", "Radiant Naquadah Alloy Casing", false)
            .addCasing("16", "Any Tiered Glass", false)
            .addCasing("6", "Infinity Frame Box", false)
            .addCasing("1", "Fluxed Electrum Item Pipe Casing", false)
            .addCasing("1", "Tungstensteel Pipe Casing", false)
            .addCasing("8", "High Energy Ultraviolet Emitter Casing", false)
            .addCasing("8", "UV Solenoid Superconductor Coil", false)
            .addCasing("12", "Solidifier Radiator", false)
            .addMiscHatch(
                "0+",
                StatCollector.translateToLocal("GT5U.tooltip.structure.layer_signal_hatch"),
                "Any casing",
                1)
            .addEnergyHatch("1+", "Any casing", 1)
            .addMaintenanceHatch("1", "Any casing", 1)
            .addInputBus("1+", "Any casing", 1)
            .addInputHatch("1+", "Any casing", 1)
            .addOutputBus("1+", "Any casing", 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                switch (phase) {
                    case NEED_ITEM:
                        return lockedRecipe == null ? Stream.empty() : Stream.of(itemOnly(lockedRecipe));
                    case NEED_FLUID:
                        return lockedRecipe == null ? Stream.empty() : Stream.of(fluidOnly(lockedRecipe));
                    case NEED_BOTH:
                    default:
                        return super.findRecipeMatches(map); // normal map query with both inputs
                }
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                setSpeedBonus(1F / SPEED);
                setEuModifier(EU_EFFICIENCY);

                boolean recipeItem = recipe.mInputs != null && recipe.mInputs.length > 0;
                boolean recipeFluid = recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0;

                boolean haveItem = anyItem(MTECuringMachine.this.getStoredInputs());
                boolean haveFluid = anyFluid(MTECuringMachine.this.getStoredFluids());

                switch (phase) {
                    case NEED_BOTH:
                        if (!(recipeItem && recipeFluid)) return CheckRecipeResultRegistry.NO_RECIPE;
                        lockedRecipe = recipe;
                        break;
                    case NEED_ITEM:
                        if (!(recipeItem && !recipeFluid)) return CheckRecipeResultRegistry.NO_RECIPE;
                        if (haveFluid) return CheckRecipeResultRegistry.NO_RECIPE; // enforce ONLY item
                        break;
                    case NEED_FLUID:
                        if (!(recipeFluid && !recipeItem)) return CheckRecipeResultRegistry.NO_RECIPE;
                        if (haveItem) return CheckRecipeResultRegistry.NO_RECIPE; // enforce ONLY fluid
                        break;
                }
                return super.validateRecipe(recipe);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setNoOverclock(true);
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                // this cycle is committed - telegraph what the NEXT cycle demands
                phase = pickNextPhase();
                emitSignal(phaseSignal(phase));
                return super.onRecipeStart(recipe);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    private void resetMachine() {
        phase = ChallengePhase.NEED_BOTH;
        emitSignal(0);
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        CheckRecipeResult result = super.checkProcessing();
        if (!result.wasSuccessful() && phase != ChallengePhase.NEED_BOTH) {
            resetMachine();
        }
        return result;
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        resetMachine();
        super.stopMachine(reason);
    }

    @Override
    public void onDisableWorking() {
        resetMachine();
        super.onDisableWorking();
    }

    private static int phaseSignal(ChallengePhase p) {
        switch (p) {
            case NEED_ITEM:
                return 1;
            case NEED_FLUID:
                return 2;
            default:
                return 0; // NEED_BOTH
        }
    }

    private ChallengePhase pickNextPhase() {
        // on current cycle, select and output signal for next cycle
        return XSTR_INSTANCE.nextBoolean() ? ChallengePhase.NEED_ITEM : ChallengePhase.NEED_FLUID;
    }

    private void emitSignal(int strength) {
        for (MTELayerSignal hatch : signalHatches) {
            hatch.setLayerValue(strength);
        }
    }

    private static GTRecipe itemOnly(GTRecipe base) {
        return new GTRecipe(
            false,
            base.mInputs,
            base.mOutputs,
            base.mSpecialItems,
            base.mInputChances,
            base.mOutputChances,
            base.mFluidInputChances,
            base.mFluidOutputChances,
            null,
            base.mFluidOutputs,
            base.mDuration,
            base.mEUt,
            base.mSpecialValue);
    }

    private static GTRecipe fluidOnly(GTRecipe base) {
        return new GTRecipe(
            false,
            null,
            base.mOutputs,
            base.mSpecialItems,
            base.mInputChances,
            base.mOutputChances,
            base.mFluidInputChances,
            base.mFluidOutputChances,
            base.mFluidInputs,
            base.mFluidOutputs,
            base.mDuration,
            base.mEUt,
            base.mSpecialValue);
    }

    private static boolean anyItem(List<ItemStack> l) {
        if (l == null) return false;
        for (ItemStack s : l) if (s != null && s.stackSize > 0) return true;
        return false;
    }

    private static boolean anyFluid(List<FluidStack> l) {
        if (l == null) return false;
        for (FluidStack f : l) if (f != null && f.amount > 0) return true;
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && (aTick % 20 == 0)) emitSignal(phaseSignal(phase));
    }

    @Override
    public int getMaxParallelRecipes() {
        return (PARALLEL_PER_TIER * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        signalHatches.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 6);
        if (!mExoticEnergyHatches.isEmpty()) {
            int count = mEnergyHatches.size() + mExoticEnergyHatches.size();
            if (count != 1) {
                errors.add(StructureErrors.hatchCount(ErrorType.TOO_MANY, Energy, count, 1));
            }
        } else {
            checkHasEnergyHatch(errors);
        }
        checkHasMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public List<MTELayerSignal> getLayerSignalHatches() {
        return Collections.unmodifiableList(signalHatches);
    }

    @Override
    public boolean addLayerSignalHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null && aTileEntity.getMetaTileEntity() instanceof MTELayerSignal sensor) {
            sensor.updateTexture(aBaseCasingIndex);
            return signalHatches.add(sensor);
        }
        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.curingMachineRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
