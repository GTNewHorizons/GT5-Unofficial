package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static goodgenerator.main.GGConfigLoader.*;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.DynamoMulti;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.EnergyMulti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.CrackRecipeAdder;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

@IMetaTileEntity.SkipGenerateDescription
public class MTELargeNaquadahReactor extends TTMultiblockBase
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final int COOLANT_AMT_HIGH = 1000;
    private static final int COOLANT_AMT_LOW = 20;
    private static final int EXCITED_AMT_HIGH = 180;
    private static final int EXCITED_AMT_LOW = 20;

    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 10;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private int casingAmount = 0;
    protected long trueOutput = 0;
    protected int trueEff = 0;
    protected FluidStack lockedFluid = null;
    protected int times = 1;
    protected int basicOutput;

    private static List<Pair<FluidStack, Integer>> excitedLiquid;

    private static List<Pair<FluidStack, Integer>> coolant;

    private static List<Pair<FluidStack, Integer>> getExcitedLiquid() {
        if (excitedLiquid == null) {
            excitedLiquid = Arrays.asList(
                Pair.of(Materials.Space.getMolten(EXCITED_AMT_LOW), ExcitedLiquidCoe[0]),
                Pair.of(GGMaterial.atomicSeparationCatalyst.getMolten(EXCITED_AMT_LOW), ExcitedLiquidCoe[1]),
                Pair.of(Materials.Naquadah.getMolten(EXCITED_AMT_LOW), ExcitedLiquidCoe[2]),
                Pair.of(Materials.Uranium235.getMolten(EXCITED_AMT_HIGH), ExcitedLiquidCoe[3]),
                Pair.of(Materials.Caesium.getMolten(EXCITED_AMT_HIGH), ExcitedLiquidCoe[4]));
        }
        return excitedLiquid;
    }

    private static List<Pair<FluidStack, Integer>> getCoolant() {
        if (coolant == null) {
            coolant = Arrays.asList(
                Pair.of(Materials.Time.getMolten(COOLANT_AMT_LOW), CoolantEfficiency[0]),
                Pair.of(new FluidStack(TFFluids.fluidCryotheum, COOLANT_AMT_HIGH), CoolantEfficiency[1]),
                Pair.of(Materials.SuperCoolant.getFluid(COOLANT_AMT_HIGH), CoolantEfficiency[2]),
                Pair.of(GTModHandler.getIC2Coolant(COOLANT_AMT_HIGH), CoolantEfficiency[3]));
        }
        return coolant;
    }

    private static final IStructureDefinition<MTELargeNaquadahReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeNaquadahReactor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] {
                // spotless:off
                { "  AAA  ", "   A   ", "   C   ", "   C   ", "   A   ", "  AAA  ", "   A   ", "   C   ", "   C   ", "   A   ", "  A~A  " },
                { " AAAAA ", "  ACA  ", "       ", "   A   ", "  BBB  ", " ABBBA ", "  BBB  ", "   A   ", "       ", "  ACA  ", " AAAAA " },
                { "AAAAAAA", " ACACA ", "   A   ", "  BBB  ", " BBBBB ", "ABBBBBA", " BBBBB ", "  BBB  ", "   A   ", " ACACA ", "AAAAAAA" },
                { "AAAAAAA", "ACAAACA", "C A A C", "CABBBAC", "ABBBBBA", "ABBBBBA", "ABBBBBA", "CABBBAC", "C A A C", "ACA ACA", "AAAAAAA" },
                { "AAAAAAA", " ACACA ", "   A   ", "  BBB  ", " BBBBB ", "ABBBBBA", " BBBBB ", "  BBB  ", "   A   ", " ACACA ", "AAAAAAA" },
                { " AAAAA ", "  ACA  ", "       ", "   A   ", "  BBB  ", " ABBBA ", "  BBB  ", "   A   ", "       ", "  ACA  ", " AAAAA " },
                { "  AAA  ", "   A   ", "   C   ", "   C   ", "   A   ", "  AAA  ", "   A   ", "   C   ", "   C   ", "   A   ", "  AAA  " } })
                // spotless:on
        .addElement(
            'A',
            ofChain(
                buildHatchAdder(MTELargeNaquadahReactor.class)
                    .atLeast(DynamoMulti.or(Dynamo), EnergyMulti.or(Energy), InputHatch, OutputHatch, Maintenance)
                    .casingIndex(Casings.NaquadahReactorCasing.textureId)
                    .hint(1)
                    .build(),
                onElementPass(x -> x.casingAmount++, Casings.NaquadahReactorCasing.asElement())))
        .addElement('B', Casings.FieldRestrictionCasing.asElement())
        .addElement('C', Casings.RadiantProofSteelFrameBox.asElement())
        .build();

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public IStructureDefinition<MTELargeNaquadahReactor> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    public MTELargeNaquadahReactor(String name) {
        super(name);
    }

    public MTELargeNaquadahReactor(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.times = aNBT.getInteger("mTimes");
        this.basicOutput = aNBT.getInteger("mbasicOutput");
        if (FluidRegistry.getFluid(aNBT.getString("mLockedFluidName")) != null) this.lockedFluid = new FluidStack(
            FluidRegistry.getFluid(aNBT.getString("mLockedFluidName")),
            aNBT.getInteger("mLockedFluidAmount"));
        else this.lockedFluid = null;
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mTimes", this.times);
        aNBT.setInteger("mbasicOutput", this.basicOutput);
        if (lockedFluid != null) {
            aNBT.setString(
                "mLockedFluidName",
                this.lockedFluid.getFluid()
                    .getName());
            aNBT.setInteger("mLockedFluidAmount", this.lockedFluid.amount);
        }
        super.saveNBTData(aNBT);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GoodGeneratorRecipeMaps.naquadahReactorFuels;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        ArrayList<FluidStack> tFluids = getStoredFluids();
        for (int i = 0; i < tFluids.size() - 1; i++) {
            for (int j = i + 1; j < tFluids.size(); j++) {
                if (GTUtility.areFluidsEqual(tFluids.get(i), tFluids.get(j))) {
                    if ((tFluids.get(i)).amount >= (tFluids.get(j)).amount) {
                        tFluids.remove(j--);
                    } else {
                        tFluids.remove(i--);
                        break;
                    }
                }
            }
        }

        GTRecipe tRecipe = GoodGeneratorRecipeMaps.naquadahReactorFuels.findRecipeQuery()
            .fluids(tFluids.toArray(new FluidStack[0]))
            .find();
        if (tRecipe != null) {
            Pair<FluidStack, Integer> excitedInfo = getExcited(tFluids.toArray(new FluidStack[0]), false);
            int pall = excitedInfo == null ? 1 : excitedInfo.getValue();
            if (consumeFuel(
                CrackRecipeAdder.copyFluidWithAmount(tRecipe.mFluidInputs[0], pall),
                tFluids.toArray(new FluidStack[0]))) {
                mOutputFluids = new FluidStack[] {
                    CrackRecipeAdder.copyFluidWithAmount(tRecipe.mFluidOutputs[0], pall) };
                basicOutput = tRecipe.mSpecialValue;
                times = pall;
                lockedFluid = excitedInfo == null ? null : excitedInfo.getKey();
                mMaxProgresstime = tRecipe.mDuration;
                return CheckRecipeResultRegistry.GENERATING;
            }
        }
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            if (mMaxProgresstime != 0 && mProgresstime % 20 == 0) {
                // If there's no startRecipeProcessing, ME input hatch wouldn't work
                startRecipeProcessing();
                FluidStack[] input = getStoredFluids().toArray(new FluidStack[0]);
                int time = 1;
                if (LiquidAirConsumptionPerSecond != 0
                    && !consumeFuel(Materials.LiquidAir.getFluid(LiquidAirConsumptionPerSecond), input)) {
                    this.mEUt = 0;
                    this.trueEff = 0;
                    this.trueOutput = 0;
                    endRecipeProcessing();
                    return true;
                }
                int eff = consumeCoolant(input);
                if (consumeFuel(lockedFluid, input)) time = times;
                this.mEUt = basicOutput * eff * time / 100;
                this.trueEff = eff;
                this.trueOutput = (long) basicOutput * (long) eff * (long) time / 100;
                endRecipeProcessing();
            }
            addAutoEnergy(trueOutput);
        }
        return true;
    }

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        info[4] = IGregTechDeviceInformation
            .encode("gg.scanner.info.generator.generates", "§c" + formatNumber(Math.abs(this.trueOutput)) + "§r");
        info[6] = IGregTechDeviceInformation.encode(
            "GT5U.multiblock.problems.efficiency.fmt",
            this.getIdealStatus() - this.getRepairStatus(),
            trueEff + " %");
        return info;
    }

    public boolean consumeFuel(FluidStack target, FluidStack[] input) {
        if (target == null) return false;
        for (FluidStack inFluid : input) {
            if (inFluid != null && inFluid.isFluidEqual(target) && inFluid.amount >= target.amount) {
                inFluid.amount -= target.amount;
                return true;
            }
        }
        return false;
    }

    public Pair<FluidStack, Integer> getExcited(FluidStack[] input, boolean isConsume) {
        for (Pair<FluidStack, Integer> fluidPair : getExcitedLiquid()) {
            FluidStack tFluid = fluidPair.getKey();
            for (FluidStack inFluid : input) {
                if (inFluid != null && inFluid.isFluidEqual(tFluid) && inFluid.amount >= tFluid.amount) {
                    if (isConsume) inFluid.amount -= tFluid.amount;
                    return fluidPair;
                }
            }
        }
        return null;
    }

    /**
     * Finds valid coolant from given inputs and consumes if found.
     *
     * @param input Fluid inputs.
     * @return Efficiency of the coolant. 100 if not found.
     */
    private int consumeCoolant(FluidStack[] input) {
        for (Pair<FluidStack, Integer> fluidPair : getCoolant()) {
            FluidStack tFluid = fluidPair.getKey();
            for (FluidStack inFluid : input) {
                if (inFluid != null && inFluid.isFluidEqual(tFluid) && inFluid.amount >= tFluid.amount) {
                    inFluid.amount -= tFluid.amount;
                    return fluidPair.getValue();
                }
            }
        }
        return 100;
    }

    public void addAutoEnergy(long outputPower) {
        if (!this.eDynamoMulti.isEmpty()) {
            MTEHatchDynamoMulti tHatch = this.eDynamoMulti.get(0);
            if (tHatch.maxEUOutput() * tHatch.maxAmperesOut() >= outputPower) {
                tHatch.setEUVar(
                    Math.min(
                        tHatch.maxEUStore(),
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + outputPower));
            } else {
                stopMachine(ShutDownReasonRegistry.INSUFFICIENT_DYNAMO);
            }
        }
        if (!this.mDynamoHatches.isEmpty()) {
            MTEHatchDynamo tHatch = this.mDynamoHatches.get(0);
            if (tHatch.maxEUOutput() * tHatch.maxAmperesOut() >= outputPower) {
                tHatch.setEUVar(
                    Math.min(
                        tHatch.maxEUStore(),
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + outputPower));
            } else {
                stopMachine(ShutDownReasonRegistry.INSUFFICIENT_DYNAMO);
            }
        }
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 130);
        int dynamoCount = mDynamoHatches.size() + eDynamoMulti.size();
        if (dynamoCount == 0) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, Dynamo, 0, 1));
        } else if (dynamoCount > 1) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_MANY, Dynamo, dynamoCount, 1));
        }
        checkOneMaintenanceHatch(errors);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeNaquadahReactor(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.naquadah_reactor"))
            .addMarkdown(
                new ResourceLocation("gregtech", "large-naquadah-reactor"),
                ImmutableMap.<String, Object>builder()
                    .put("liquid_air", LiquidAirConsumptionPerSecond)
                    .put("coolant_ic2", CoolantEfficiency[3])
                    .put("coolant_super", CoolantEfficiency[2])
                    .put("coolant_cryo", CoolantEfficiency[1])
                    .put("coolant_tachyon", CoolantEfficiency[0])
                    .put("excited_caesium", ExcitedLiquidCoe[4])
                    .put("excited_uranium", ExcitedLiquidCoe[3])
                    .put("excited_naquadah", ExcitedLiquidCoe[2])
                    .put("excited_asc", ExcitedLiquidCoe[1])
                    .put("excited_space", ExcitedLiquidCoe[0])
                    .put("coolant_amt_high", COOLANT_AMT_HIGH)
                    .put("coolant_amt_low", COOLANT_AMT_LOW)
                    .put("excited_amt_high", EXCITED_AMT_HIGH)
                    .put("excited_amt_low", EXCITED_AMT_LOW)
                    .build())
            .addSupportAny()
            .beginStructureBlock(7, 11, 7, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("130-141", Casings.NaquadahReactorCasing.getLocalizedName(), false)
            .addCasing("81", Casings.FieldRestrictionCasing.getLocalizedName(), false)
            .addCasing("32", Casings.RadiantProofSteelFrameBox.getLocalizedName(), false)
            .addDynamoHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_reactor_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_reactor_casing"), 1)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_reactor_casing"), 1)
            .addOutputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_reactor_casing"), 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT,
            Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_GLOW,
            Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE,
            Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.NaquadahReactorCasing.getCasingTexture();
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
    public boolean showRecipeTextInGUI() {
        return false;
    }

    public String getCoolantTextFormatted(String fluidType, String litersConsumed, int effBoost) {
        return String.format(
            "%s%s L/s%s : %s%d%% %s: %s%s",
            EnumChatFormatting.WHITE,
            litersConsumed,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.LIGHT_PURPLE,
            effBoost,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.BLUE,
            fluidType);
    }

    public String getExcitedTextFormatted(String fluidType, String litersConsumed, int multiplier) {
        return String.format(
            "%s%s L/s %s: %s%dx power %s: %s%s",
            EnumChatFormatting.WHITE,
            litersConsumed,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.LIGHT_PURPLE,
            multiplier,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.RED,
            fluidType);

    }

}
