package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Hatch_Air_Intake;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchAirIntake;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

@IMetaTileEntity.SkipGenerateDescription
public class MTELargeRocketEngine extends MTEExtendedPowerMultiBlockBase<MTELargeRocketEngine>
    implements ISurvivalConstructable, ICasingTextureProvider {

    public static final String mLubricantName = "Carbon Dioxide";
    public static final String mCoolantName = "Liquid Hydrogen";

    private static final int LUBRICANT_CONSUMPTION_PER_HOUR = 1000;
    private static final int MIN_FUEL_INPUT_PER_SECOND = 6;
    private static final int AIR_PERCENT = 1;
    private static final int SOFT_CAP_1 = 49_000;
    private static final int SOFT_CAP_2 = 94_000;
    private static final int BOOST_MULTIPLIER = 3;
    private static final int WARMUP_MIN_SECONDS = 60;
    private static final int WARMUP_MAX_SECONDS = 180;
    private static final int POLLUTION_PER_EUT = 1500;
    private static final int POLLUTION_EUT_UNIT = 16384;
    private static final double COOLANT_BOOST_PERCENT = 0.3;

    private int freeFuelTicks = 0;
    private int euProduction = 0;
    private boolean boostEu;
    private static FluidStack airFluidStack;

    private int casingAmount;
    private final List<MTEHatchAirIntake> airIntakeHatches = new ArrayList<>();
    // spotless:off
    private static final String[][] structure = new String[][] {
        { "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC" },
        { "C~C", "SIS", "SIS", "SIS", "SIS", "SIS", "SIS", "SIS", "SIS", "CMC" },
        { "CCC", "CSC", "CSC", "CSC", "CSC", "CSC", "CSC", "CSC", "CSC", "CCC" }, };
    // spotless:on
    private static IStructureDefinition<MTELargeRocketEngine> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    public MTELargeRocketEngine(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.boostEu = false;
        setAir();
    }

    public MTELargeRocketEngine(final String aName) {
        super(aName);
        this.boostEu = false;
        setAir();
    }

    public static void setAir() {
        if (airFluidStack == null) {
            airFluidStack = Materials.Air.getGas(1);
        }
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.rocket_engine"))
            .addMarkdown(
                new ResourceLocation("gregtech", "large-rocket-engine"),
                ImmutableMap.<String, Object>builder()
                    .put("cap1", formatNumber(SOFT_CAP_1))
                    .put("cap2", formatNumber(SOFT_CAP_2))
                    .put("min_fuel", formatNumber(MIN_FUEL_INPUT_PER_SECOND))
                    .put("lubricant_amount", formatNumber(LUBRICANT_CONSUMPTION_PER_HOUR))
                    .put("lubricant", mLubricantName)
                    .put("boost", formatNumber(BOOST_MULTIPLIER))
                    .put("air_percent", formatNumber(AIR_PERCENT))
                    .put("coolant_percent", formatNumber(COOLANT_BOOST_PERCENT))
                    .put("coolant", mCoolantName)
                    .put("cap1_boosted", formatNumber(SOFT_CAP_1 * BOOST_MULTIPLIER))
                    .put("cap2_boosted", formatNumber(SOFT_CAP_2 * BOOST_MULTIPLIER))
                    .put("warmup_min", formatNumber(WARMUP_MIN_SECONDS))
                    .put("warmup_max", formatNumber(WARMUP_MAX_SECONDS))
                    .put("pollution", formatNumber(POLLUTION_PER_EUT))
                    .put("pollution_unit", formatNumber(POLLUTION_EUT_UNIT))
                    .build())
            .addSupportAny()
            .beginStructureBlock(3, 3, 10, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_2nd_layer"))
            .addCasing("62-76", Casings.TurbodyneCasing.getLocalizedName(), false)
            .addCasing("8", Casings.InconelReinforcedCasing.getLocalizedName(), false)
            .addMiscHatch("1+", StatCollector.translateToLocal("GT5U.MBTT.AirIntakeHatch"), StatCollector.translateToLocal("gt.mbtt.structure.any_center_casing"), 1, 2)
            .addDynamoHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_top_center_casing"), 2)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_center_casing"), 1, 2)
            .addMufflerHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.back_center_casing"), 3)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_side_or_bottom_center_casing"), 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public IStructureDefinition<MTELargeRocketEngine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTELargeRocketEngine>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(structure))
                .addElement('C', Casings.TurbodyneCasing.asElement())
                .addElement('I', Casings.InconelReinforcedCasing.asElement())
                .addElement(
                    'S',
                    buildHatchAdder(MTELargeRocketEngine.class)
                        .atLeast(ImmutableMap.of(IntakeHatchElement.AIR_INTAKE_HATCH, 8, InputHatch, 1, Maintenance, 1))
                        .casingIndex(Casings.TurbodyneCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.TurbodyneCasing.asElement())))
                .addElement(
                    'T',
                    buildHatchAdder(MTELargeRocketEngine.class)
                        .atLeast(
                            ImmutableMap
                                .of(IntakeHatchElement.AIR_INTAKE_HATCH, 1, Dynamo.or(ExoticDynamo), 1, Maintenance, 1))
                        .casingIndex(Casings.TurbodyneCasing.textureId)
                        .hint(2)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.TurbodyneCasing.asElement())))
                .addElement('M', Muffler.newAny(Casings.TurbodyneCasing.textureId, 3))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        clearCraftingIcons(airIntakeHatches);
        airIntakeHatches.clear();
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        this.casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0, errors)) return;
        checkCasingMin(errors, casingAmount, 16);
        if (airIntakeHatches.isEmpty()) {
            errors.add(StructureErrors.missingHatch(Hatch_Air_Intake.get(1)));
        }
        checkHasAnyDynamo(errors);
        checkHasMaintenanceHatch(errors);
        checkHasInputHatch(errors);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            TexturesGtBlock.oMCALargeRocketEngine,
            TexturesGtBlock.oMCALargeRocketEngineGlow,
            TexturesGtBlock.oMCALargeRocketEngineActive,
            TexturesGtBlock.oMCALargeRocketEngineActiveGlow);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.TurbodyneCasing.getCasingTexture();
    }

    public int getAir() {
        int totalAir = 0;
        for (MTEHatchAirIntake mte : airIntakeHatches) {
            if (mte.mFluid != null && mte.mFluid.isFluidEqual(airFluidStack)) {
                totalAir += mte.mFluid.amount;
            }
        }
        return totalAir;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.rocketFuels;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        int airCount = getAir();
        int airToConsume = this.euProduction / 100;
        if (airCount < airToConsume) {
            stopMachine(ShutDownReasonRegistry.outOfFluid(Materials.Air.getGas(airToConsume)));
            return SimpleCheckRecipeResult.ofFailure("no_air");
        }

        int splitAmount = (airToConsume / airIntakeHatches.size());
        if (splitAmount > 0) {
            for (MTEHatchAirIntake airHatch : airIntakeHatches) {
                boolean hasIntakeAir = airHatch.drain(splitAmount, true) != null;
                if (!hasIntakeAir) {
                    this.freeFuelTicks = 0;
                    return SimpleCheckRecipeResult.ofFailure("no_air");
                }
            }
        }
        // reset fuel ticks in case it does not reset when it stops
        if (this.freeFuelTicks != 0 && this.mProgresstime == 0 && this.mEfficiency == 0) this.freeFuelTicks = 0;

        final ArrayList<FluidStack> storedFluids = this.getStoredFluids();
        RecipeMap<?> recipeMap = getRecipeMap();
        if (!storedFluids.isEmpty() && recipeMap != null) {
            if (this.mRuntime % 72 == 0) {
                if (!this.depleteInput(Materials.CarbonDioxide.getGas(this.boostEu ? BOOST_MULTIPLIER : 1))) {
                    this.freeFuelTicks = 0;
                    return SimpleCheckRecipeResult.ofFailure("no_co2");
                }
            }
            if (this.freeFuelTicks == 0) {
                this.boostEu = consumeLOH();
            }

            Collection<GTRecipe> recipes = recipeMap.getAllRecipes();
            for (final FluidStack hatchFluid : storedFluids) {
                if (hatchFluid.isFluidEqual(airFluidStack)) {
                    continue;
                }
                if (this.freeFuelTicks != 0) {
                    this.mEfficiencyIncrease = this.euProduction / 2000;
                    this.freeFuelTicks--;
                    this.lEUt = ((this.mEfficiency < 1000) ? 0 : GTValues.V[5] << 1);
                    this.mProgresstime = 1;
                    this.mMaxProgresstime = 1;
                    return CheckRecipeResultRegistry.GENERATING;
                }
                for (final GTRecipe recipe : recipes) {
                    final FluidStack fuel = recipe.mFluidInputs[0];
                    if (!hatchFluid.isFluidEqual(fuel) || !consumeFuel(recipe, hatchFluid.amount)) {
                        continue;
                    }
                    this.lEUt = ((this.mEfficiency < 2000) ? 0 : GTValues.V[5] << 1);
                    this.mProgresstime = 1;
                    this.mMaxProgresstime = 1;
                    this.mEfficiencyIncrease = this.euProduction / 2000;
                    return CheckRecipeResultRegistry.GENERATING;
                }
            }
        }
        this.lEUt = 0;
        this.mEfficiency = 0;
        this.freeFuelTicks = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    public boolean consumeFuel(GTRecipe aFuel, int amount) {
        amount *= this.boostEu ? COOLANT_BOOST_PERCENT : 0.9;
        this.freeFuelTicks = 0;
        int value = aFuel.mSpecialValue * 3;
        int energy = value * amount;
        if (amount < 5) return false;
        FluidStack tLiquid = new FluidStack(aFuel.mFluidInputs[0], (this.boostEu ? amount * BOOST_MULTIPLIER : amount));
        if (!this.depleteInput(tLiquid)) {
            return false;
        }
        this.freeFuelTicks = 20;
        setEUProduction(energy);
        return true;
    }

    public void setEUProduction(int energy) {
        energy /= 20;
        double energyEfficiency = energy;
        if (energy > 30000) {
            double tDivideEnergy = Math.cbrt(energy);
            // cbrt(30 000) /
            energyEfficiency = (31.072325 / tDivideEnergy);
            if (energy >= 80000)
                // cbrt(80 000) /
                energyEfficiency *= (43.0886938 / tDivideEnergy);
            energyEfficiency *= energy;
        }
        this.euProduction = (int) (energyEfficiency);
        if (this.boostEu) this.euProduction *= BOOST_MULTIPLIER;
    }

    public boolean consumeLOH() {
        // (40 * ((long) euProduction / 10000))
        int LOHamount = (BOOST_MULTIPLIER * this.euProduction) / 1000;
        return this.depleteInput(new FluidStack(GTPPFluids.LiquidHydrogen, LOHamount));
    }

    @Override
    protected boolean requiresMuffler() {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTELargeRocketEngine(this.mName);
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        aNBT.setInteger("freeFuelTicks", this.freeFuelTicks);
        aNBT.setInteger("euProduction", this.euProduction);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.freeFuelTicks = aNBT.getInteger("freeFuelTicks");
        this.euProduction = aNBT.getInteger("euProduction");
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return this.euProduction;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return POLLUTION_PER_EUT * (this.euProduction / 10000);
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    private enum IntakeHatchElement implements IHatchElement<MTELargeRocketEngine> {

        AIR_INTAKE_HATCH("GT5U.MBTT.AirIntakeHatch");

        private final String name;

        IntakeHatchElement(String name) {
            this.name = name;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchAirIntake.class);
        }

        @Override
        public IGTHatchAdder<? super MTELargeRocketEngine> adder() {
            return (self, igte, texture) -> {
                if (igte.getMetaTileEntity() instanceof MTEHatchAirIntake imte) {
                    imte.updateTexture(texture);
                    imte.updateCraftingIcon(self.getMachineCraftingIcon(), self.getBaseMetaTileEntity());
                    self.airIntakeHatches.add(imte);
                    return true;
                }
                return false;
            };
        }

        @Override
        public long count(MTELargeRocketEngine mte) {
            return mte.airIntakeHatches.size();
        }

        @Override
        public String getDisplayName() {
            return StatCollector.translateToLocal(name);
        }

        @Override
        public String getDescriptionLangKey() {
            return name;
        }
    }
}
