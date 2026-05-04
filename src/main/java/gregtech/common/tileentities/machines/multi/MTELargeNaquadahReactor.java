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
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class MTELargeNaquadahReactor extends TTMultiblockBase implements ISurvivalConstructable {

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
                Pair.of(Materials.Space.getMolten(20L), ExcitedLiquidCoe[0]),
                Pair.of(GGMaterial.atomicSeparationCatalyst.getMolten(20), ExcitedLiquidCoe[1]),
                Pair.of(Materials.Naquadah.getMolten(20L), ExcitedLiquidCoe[2]),
                Pair.of(Materials.Uranium235.getMolten(180L), ExcitedLiquidCoe[3]),
                Pair.of(Materials.Caesium.getMolten(180L), ExcitedLiquidCoe[4]));
        }
        return excitedLiquid;
    }

    private static List<Pair<FluidStack, Integer>> getCoolant() {
        if (coolant == null) {
            coolant = Arrays.asList(
                Pair.of(Materials.Time.getMolten(20L), CoolantEfficiency[0]),
                Pair.of(new FluidStack(TFFluids.fluidCryotheum, 1_000), CoolantEfficiency[1]),
                Pair.of(Materials.SuperCoolant.getFluid(1_000), CoolantEfficiency[2]),
                Pair.of(GTModHandler.getIC2Coolant(1_000), CoolantEfficiency[3]));
        }
        return coolant;
    }

    private static final IStructureDefinition<MTELargeNaquadahReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeNaquadahReactor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] {
                { "  AAA  ", "   A   ", "   C   ", "   C   ", "   A   ", "  AAA  ", "   A   ", "   C   ", "   C   ",
                    "   A   ", "  A~A  " },
                { " AAAAA ", "  ACA  ", "       ", "   A   ", "  BBB  ", " ABBBA ", "  BBB  ", "   A   ", "       ",
                    "  ACA  ", " AAAAA " },
                { "AAAAAAA", " ACACA ", "   A   ", "  BBB  ", " BBBBB ", "ABBBBBA", " BBBBB ", "  BBB  ", "   A   ",
                    " ACACA ", "AAAAAAA" },
                { "AAAAAAA", "ACAAACA", "C A A C", "CABBBAC", "ABBBBBA", "ABBBBBA", "ABBBBBA", "CABBBAC", "C A A C",
                    "ACA ACA", "AAAAAAA" },
                { "AAAAAAA", " ACACA ", "   A   ", "  BBB  ", " BBBBB ", "ABBBBBA", " BBBBB ", "  BBB  ", "   A   ",
                    " ACACA ", "AAAAAAA" },
                { " AAAAA ", "  ACA  ", "       ", "   A   ", "  BBB  ", " ABBBA ", "  BBB  ", "       ", "       ",
                    "  ACA  ", " AAAAA " },
                { "  AAA  ", "   A   ", "   C   ", "   C   ", "   A   ", "  AAA  ", "   A   ", "   C   ", "   C   ",
                    "   A   ", "  AAA  " } })
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
        structureBuild_EM(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, itemStack, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("MultiNqGenerator.hint", 8);
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
        info[4] = StatCollector.translateToLocalFormatted(
            "gg.scanner.info.generator.generates",
            EnumChatFormatting.RED + formatNumber(Math.abs(this.trueOutput)) + EnumChatFormatting.RESET);
        info[6] = StatCollector.translateToLocal("gg.scanner.info.generator.problems") + " "
            + EnumChatFormatting.RED
            + (this.getIdealStatus() - this.getRepairStatus())
            + EnumChatFormatting.RESET
            + " "
            + StatCollector.translateToLocal("gg.scanner.info.generator.efficiency")
            + " "
            + EnumChatFormatting.YELLOW
            + trueEff
            + EnumChatFormatting.RESET
            + " %";
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
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        return structureCheck_EM(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && checkHatch()
            && casingAmount >= 130;
    }

    public boolean checkHatch() {
        return mMaintenanceHatches.size() == 1 && (mDynamoHatches.size() + eDynamoMulti.size()) == 1;
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
        tt.addMachineType("Naquadah Reactor, LNR")
            .addInfo("Environmentally Friendly!")
            .addInfo("Generate power from high-energy liquids")
            .addInfo(
                String.format(
                    "Consumes %s%d L/s Liquid Air%s to keep running, otherwise" + EnumChatFormatting.YELLOW
                        + " it will void your fuel"
                        + EnumChatFormatting.GRAY
                        + ".",
                    EnumChatFormatting.AQUA,
                    LiquidAirConsumptionPerSecond,
                    EnumChatFormatting.GRAY))
            .addInfo(
                "The reactor will explode when there is more than" + EnumChatFormatting.RED
                    + " ONE"
                    + EnumChatFormatting.GRAY
                    + " type of fuel in hatches!")
            .addInfo("Input liquid nuclear fuel or liquid naquadah fuel")
            .addSeparator()
            .addInfo(
                "Can increase " + EnumChatFormatting.LIGHT_PURPLE
                    + "efficiency "
                    + EnumChatFormatting.GRAY
                    + "by "
                    + EnumChatFormatting.WHITE
                    + "consuming "
                    + EnumChatFormatting.BLUE
                    + "coolants:")
            .addInfo(getCoolantTextFormatted("IC2 Coolant", "1000", CoolantEfficiency[3]))
            .addInfo(getCoolantTextFormatted("Super Coolant", "1000", CoolantEfficiency[2]))
            .addInfo(getCoolantTextFormatted("Cryotheum", "1000", CoolantEfficiency[1]))
            .addInfo(getCoolantTextFormatted("Tachyon Rich Temporal Fluid", "20", CoolantEfficiency[0]))
            .addSeparator()
            .addInfo(
                "Can increase " + EnumChatFormatting.LIGHT_PURPLE
                    + "output power and fuel usage "
                    + EnumChatFormatting.GRAY
                    + "by "
                    + EnumChatFormatting.WHITE
                    + "consuming "
                    + EnumChatFormatting.RED
                    + "excited liquid:")
            .addInfo(getExcitedTextFormatted("Molten Caesium", "180", ExcitedLiquidCoe[4]))
            .addInfo(getExcitedTextFormatted("Molten Uranium-235", "180", ExcitedLiquidCoe[3]))
            .addInfo(getExcitedTextFormatted("Molten Naquadah", "20", ExcitedLiquidCoe[2]))
            .addInfo(getExcitedTextFormatted("Molten Atomic Separation Catalyst", "20", ExcitedLiquidCoe[1]))
            .addInfo(getExcitedTextFormatted("Spatially Enlarged Fluid", "20", ExcitedLiquidCoe[0]))
            .addTecTechHatchInfo()
            .beginStructureBlock(7, 11, 7, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Naquadah Reactor Casing", 130, false)
            .addCasingInfoExactly("Field Restriction Casing", 81, false)
            .addCasingInfoExactly("Radiation Proof Steel Frame Box", 36, false)
            .addDynamoHatch("Any Naquadah Reactor Casing, only accepts ONE!")
            .addInputHatch("Any Naquadah Reactor Casing")
            .addOutputHatch("Any Naquadah Reactor Casing")
            .addMaintenanceHatch("Any Naquadah Reactor Casing")
            .addStructureAuthors(EnumChatFormatting.GOLD + "N7Paddy")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Casings.NaquadahReactorCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.NaquadahReactorCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Casings.NaquadahReactorCasing.getCasingTexture() };
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
