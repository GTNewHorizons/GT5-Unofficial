package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.multiblock.MTEElectrochemicalCutterGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class MTEElectrochemicalCutter extends MTEExtendedPowerMultiBlockBase<MTEElectrochemicalCutter>
    implements ISurvivalConstructable {

    private static final String MAIN = "main";
    private static final String TAG_SOLUTION = "electrochemicalSolution";
    private static final String TAG_CYCLE_MIN_SOLUTION = "electrochemicalCycleMinSolution";
    private static final String TAG_CYCLE_MAX_SOLUTION = "electrochemicalCycleMaxSolution";

    private static final int OFFSET_X = 6;
    private static final int OFFSET_Y = 5;
    private static final int OFFSET_Z = 0;
    private static final int SOLUTION_CAPACITY_MB = 1_024_000;
    private static final int MINIMUM_SOLUTION_MB = 256_000;
    private static final int PEAK_SOLUTION_MB = 512_000;
    private boolean needsWaterFill = false;
    private static final String[][] structure = transpose(
        new String[][] {
            { "             ", "             ", "   C     C   ", "  CCDDDDDCC  ", "   D     D   ", "   D     D   ",
                "   D     D   ", "   D     D   ", "   D     D   ", "  CCDDDDDCC  ", "   C     C   ", "             ",
                "             " },
            { "             ", "   CEEEEEC   ", "  CCEEEEECC  ", " CCCCCCCCCCC ", " EEDEEEEEDEE ", " EECEEEEECEE ",
                " EEDEEEEEDEE ", " EECEEEEECEE ", " EEDEEEEEDEE ", " CCCCCCCCCCC ", "  CCEEEEECC  ", "   CEEEEEC   ",
                "             " },
            { "    EEEEE    ", "   C     C   ", "  C       C  ", " C         C ", "E ADA   ADA E", "E           E",
                "E ADA   ADA E", "E           E", "E ADA   ADA E", " C         C ", "  C       C  ", "   C     C   ",
                "    EEEEE    " },
            { "    CCCCC    ", "   CFFBFFC   ", "  CFFFBFFFC  ", " CFFFFBFFFFC ", "CFADAFBFADAFC", "CFFFFFBFFFFFC",
                "CFADAFBFADAFC", "CFFFFFBFFFFFC", "CFADAFBFADAFC", " CFFFFBFFFFC ", "  CFFFBFFFC  ", "   CFFBFFC   ",
                "    CCCCC    " },
            { "   CCCCCCC   ", "  CCFFBFFCC  ", " CCFFFBFFFCC ", "CCFFFFBFFFFCC", "CFADAFBFADAFC", "CFFFFFBFFFFFC",
                "CFADAFBFADAFC", "CFFFFFBFFFFFC", "CFADAFBFADAFC", "CCFFFFBFFFFCC", " CCFFFBFFFCC ", "  CCFFBFFCC  ",
                "   CCCCCCC   " },
            { "    CC~CC    ", "   CFFBFFC   ", "  CFFFBFFFC  ", " CFFFFBFFFFC ", "CFFFFFBFFFFFC", "CFFFFFBFFFFFC",
                "CFFFFFBFFFFFC", "CFFFFFBFFFFFC", "CFFFFFBFFFFFC", " CFFFFBFFFFC ", "  CFFFBFFFC  ", "   CFFBFFC   ",
                "    CCCCC    " },
            { "             ", "    CCCCC    ", "   CCCCCCC   ", "  CCCCCCCCC  ", " CCCCCCCCCCC ", " CCCCCCCCCCC ",
                " CCCCCCCCCCC ", " CCCCCCCCCCC ", " CCCCCCCCCCC ", "  CCCCCCCCC  ", "   CCCCCCC   ", "    CCCCC    ",
                "             " } });
    private static final IStructureDefinition<MTEElectrochemicalCutter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEElectrochemicalCutter>builder()
        .addShape(MAIN, structure)
        .addElement('A', Casings.ElectrodeCasing.asElement())
        .addElement('B', Casings.PBIPipeCasing.asElement())
        .addElement(
            'C',
            buildHatchAdder(MTEElectrochemicalCutter.class)
                .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(Casings.NaquadriaReinforcedWaterPlantCasing.textureId)
                .hint(1)
                .buildAndChain(Casings.NaquadriaReinforcedWaterPlantCasing.asElement()))
        .addElement('D', ofFrame(Materials.Naquadria))
        .addElement('E', Casings.ChemicalGradeGlass.asElement())
        .addElement('F', ofChain(ofAnyWater(false), isAir()))
        .build();

    private FluidStack solutionTank;
    private int cycleMinSolution;
    private int cycleMaxSolution;
    private long solutionDumpButtonUntilTick;

    public MTEElectrochemicalCutter(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    public MTEElectrochemicalCutter(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new MTEElectrochemicalCutter(mName);
    }

    @Override
    public IStructureDefinition<MTEElectrochemicalCutter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.electrochemicalCutterRecipes;
    }

    @Override
    public void checkMachine(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        needsWaterFill = false;
        if (!checkPiece(MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) {
            needsWaterFill = GTStructureUtility
                .hasWaterAtStructurePosition(base, getExtendedFacing(), structure, OFFSET_X, OFFSET_Y, OFFSET_Z, 'F');
            return;
        }
        checkHasAnyEnergy(errors);
        checkOneMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
        checkHasOutputBus(errors);
        checkHasOutputHatch(errors);

        if (!errors.isEmpty()) return;
        needsWaterFill = true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                OverclockCalculator calculator = super.createOverclockCalculator(recipe);
                if (GTUtility.getTier(getMaxInputVoltage()) >= VoltageIndex.UMV) {
                    calculator.setDurationDecreasePerOC(4);
                }
                return calculator;
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (solutionTank == null || solutionTank.amount <= 0) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        collectSolutionFluid();
        CheckRecipeResult result = super.checkProcessing();
        if (!result.wasSuccessful() || mMaxProgresstime <= 0) return result;
        cycleMinSolution = solutionTank.amount;
        cycleMaxSolution = solutionTank.amount;
        return result;
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        boolean consumeSolution = (mProgresstime + 1) % 20 == 0 || mProgresstime + 1 >= mMaxProgresstime;
        int requestedMb = consumeSolution ? 1 + getBaseMetaTileEntity().getWorld().rand.nextInt(4) : 0;
        if (!super.onRunningTick(stack)) return false;
        if (consumeSolution) {
            int available = solutionTank == null ? 0 : solutionTank.amount;
            int consumed = Math.min(requestedMb, available);
            if (solutionTank != null) {
                solutionTank.amount -= consumed;
                if (solutionTank.amount == 0) solutionTank = null;
            }
            int amount = getSolutionAmount();
            cycleMinSolution = Math.min(cycleMinSolution, amount);
            cycleMaxSolution = Math.max(cycleMaxSolution, amount);
        }
        if (mProgresstime + 1 >= mMaxProgresstime) {
            double chance = calculateCycleSuccessChance();
            boolean successfulCut = getBaseMetaTileEntity().getWorld().rand.nextDouble() < chance;
            if (!successfulCut) {
                int wasteAmount = getSolutionAmount();
                solutionTank = null;
                mOutputItems = null;
                FluidStack saltWater = wasteAmount > 0 ? Materials.SaltWater.getFluid(wasteAmount) : null;
                mOutputFluids = saltWater == null ? null : new FluidStack[] { saltWater };
            }
        }
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity base, long tick) {
        super.onPostTick(base, tick);
        if (!base.isServerSide() || tick % 20 != 0) return;
        collectSolutionFluid();
        if (base.isServerSide() && needsWaterFill) {
            if (GTStructureUtility
                .fillStructureWithWater(base, getExtendedFacing(), structure, OFFSET_X, OFFSET_Y, OFFSET_Z, 'F')) {
                needsWaterFill = false;
            }
        }
    }

    private void collectSolutionFluid() {
        FluidStack solution = Materials.ElectrochemicalElectrolyteSolution.getFluid(1);
        if (solution == null || solution.getFluid() == null) return;
        for (MTEHatchInput hatch : validMTEList(mInputHatches)) {
            int free = SOLUTION_CAPACITY_MB - getSolutionAmount();
            if (free <= 0) break;
            FluidStack drained = hatch.drain(ForgeDirection.UNKNOWN, new FluidStack(solution.getFluid(), free), true);
            if (drained == null || drained.amount <= 0) continue;
            if (solutionTank == null) {
                solutionTank = new FluidStack(solution.getFluid(), drained.amount);
            } else solutionTank.amount += drained.amount;
        }
        updateSlots();
    }

    private double calculateSuccessChance(int solutionAmountMb) {
        int amount = Math.clamp(solutionAmountMb, 0, SOLUTION_CAPACITY_MB);
        if (amount <= MINIMUM_SOLUTION_MB) return 0.0;
        if (amount <= PEAK_SOLUTION_MB) return 1.0;
        return (double) (SOLUTION_CAPACITY_MB - amount) / (SOLUTION_CAPACITY_MB - PEAK_SOLUTION_MB);
    }

    private double calculateCycleSuccessChance() {
        if (cycleMinSolution < MINIMUM_SOLUTION_MB) return 0.0;
        return cycleMaxSolution <= PEAK_SOLUTION_MB ? 1.0 : calculateSuccessChance(cycleMaxSolution);
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        super.saveNBTData(tag);
        if (solutionTank != null) tag.setTag(TAG_SOLUTION, solutionTank.writeToNBT(new NBTTagCompound()));
        tag.setInteger(TAG_CYCLE_MIN_SOLUTION, cycleMinSolution);
        tag.setInteger(TAG_CYCLE_MAX_SOLUTION, cycleMaxSolution);
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        super.loadNBTData(tag);
        solutionTank = tag.hasKey(TAG_SOLUTION) ? FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(TAG_SOLUTION))
            : null;
        if (solutionTank != null) solutionTank.amount = Math.clamp(solutionTank.amount, 0, SOLUTION_CAPACITY_MB);
        int currentSolution = getSolutionAmount();
        cycleMinSolution = tag.hasKey(TAG_CYCLE_MIN_SOLUTION)
            ? Math.clamp(tag.getInteger(TAG_CYCLE_MIN_SOLUTION), 0, SOLUTION_CAPACITY_MB)
            : currentSolution;
        cycleMaxSolution = tag.hasKey(TAG_CYCLE_MAX_SOLUTION)
            ? Math.clamp(tag.getInteger(TAG_CYCLE_MAX_SOLUTION), 0, SOLUTION_CAPACITY_MB)
            : currentSolution;
    }

    public int getSolutionAmount() {
        return solutionTank == null ? 0 : solutionTank.amount;
    }

    public int getSolutionAmountKiloLitres() {
        return getSolutionAmount() / 1000;
    }

    public int getSolutionCapacityKiloLitres() {
        return SOLUTION_CAPACITY_MB / 1000;
    }

    public void dumpSolution() {
        solutionTank = null;
        cycleMinSolution = 0;
        cycleMaxSolution = 0;
        solutionDumpButtonUntilTick = getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime() + 10;
    }

    public boolean isSolutionDumpButtonActive() {
        return getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime() < solutionDumpButtonUntilTick;
    }

    public int getSuccessChancePercent() {
        double chance = mMaxProgresstime > 0 ? calculateCycleSuccessChance()
            : calculateSuccessChance(getSolutionAmount());
        return (int) Math.round(chance * 100.0);
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEElectrochemicalCutterGui(this);
    }

    @Override
    public void construct(ItemStack stack, boolean hintsOnly) {
        buildPiece(MAIN, stack, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(MAIN, stack, OFFSET_X, OFFSET_Y, OFFSET_Z, elementBudget, env, false, true);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder()
            .addMachineType(StatCollector.translateToLocal("gt.recipe.electrochemical_cutter"))
            .beginStructureBlock(13, 7, 13, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addInputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addOutputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addOutputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .toolTipFinisher();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, ForgeDirection facing, int color,
        boolean active, boolean redstone) {
        ITexture casing = Textures.BlockIcons
            .getCasingTextureForId(Casings.NaquadriaReinforcedWaterPlantCasing.textureId);
        if (side != facing) return new ITexture[] { casing };
        return new ITexture[] { casing, TextureFactory.builder()
            .addIcon(
                active ? Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER)
            .extFacing()
            .build() };
    }
}
