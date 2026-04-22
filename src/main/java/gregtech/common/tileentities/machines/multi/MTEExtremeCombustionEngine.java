package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;

public class MTEExtremeCombustionEngine extends MTEExtendedPowerMultiBlockBase<MTEExtremeCombustionEngine>
    implements ISurvivalConstructable {

    protected int casingAmount;
    protected int turbineCasingAmount;
    private static final int OFFSET_X = 1;
    private static final int OFFSET_Y = 3;
    private static final int OFFSET_Z = 1;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    protected int fuelConsumption = 0;
    protected int fuelValue = 0;
    protected int fuelRemaining = 0;
    protected boolean boostEu = false;

    public MTEExtremeCombustionEngine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEExtremeCombustionEngine(String aName) {
        super(aName);
    }

    private static final IStructureDefinition<MTEExtremeCombustionEngine> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEExtremeCombustionEngine>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "          ", "    H    H", "H H H    H", "H H H    H", "FEFEFEEEEF" },
                { "    H----H", "H H CGGGGC", "CCC CDDDDC", "C~C CBBBBC", "FEFEFEEEEF" },
                { "    H----H", "H H CGGGGC", "CCCAAAAAAI", "CCC CBBBBC", "FEFEFEEEEF" },
                { "    H----H", "H H CGGGGC", "CCC CDDDDC", "CCC CBBBBC", "FEFEFEEEEF" },
                { "          ", "    H    H", "H H H    H", "H H H    H", "FEFEFEEEEF" } })
        .addElement('A', Casings.SteelGearBoxCasing.asElement())
        .addElement('B', Casings.TungstensteelFireboxCasing.asElement())
        .addElement(
            'C',
            buildHatchAdder(MTEExtremeCombustionEngine.class).atLeast(Muffler, Maintenance)
                .casingIndex(Casings.RobustTungstenSteelMachineCasing.textureId)
                .hint(1)
                .buildAndChain(
                    onElementPass(x -> ++x.casingAmount, Casings.RobustTungstenSteelMachineCasing.asElement())))
        .addElement(
            'D',
            buildHatchAdder(MTEExtremeCombustionEngine.class).atLeast(InputHatch, InputHatch, InputHatch)
                .casingIndex(Casings.TungstensteelTurbineCasing.textureId)
                .hint(2)
                .buildAndChain(
                    onElementPass(x -> ++x.turbineCasingAmount, Casings.TungstensteelTurbineCasing.asElement())))
        .addElement('E', Casings.ChemicallyInertMachineCasing.asElement())
        .addElement('F', Casings.PTFEPipeCasing.asElement())
        .addElement('G', Casings.ExtremeEngineIntakeCasing.asElement())
        .addElement('H', ofFrame(Materials.Polytetrafluoroethylene))
        .addElement(
            'I',
            buildHatchAdder(MTEExtremeCombustionEngine.class).atLeast(Dynamo)
                .casingIndex(Casings.RobustTungstenSteelMachineCasing.textureId)
                .hint(3)
                .buildAndChain(Casings.RobustTungstenSteelMachineCasing.asElement()))
        .build();

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();

        String lubricantRate = TooltipHelper.fluidText(8000);
        String oxygenRate = TooltipHelper.fluidRateText(40);
        String defaultOutput = TooltipHelper.euRateText(10900);
        String defaultEfficiency = TooltipHelper.effText(1.0f);
        String boostedOutput = TooltipHelper.euRateText(32700);
        String boostedEfficiency = TooltipHelper.effText(1.5f);
        String waitPower = TooltipHelper.effText(3.0f);

        tt.addMachineType("Combustion Generator, ECE")
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.desc1_1", lubricantRate))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.desc2_1", oxygenRate))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.default_output", defaultOutput, defaultEfficiency))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.boosted_output", boostedOutput, boostedEfficiency))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.wait_power", waitPower))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.intake_warning2"))
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(10, 5, 5, false)
            .addController("Front left, 2nd layer")
            .addCasingInfoMin("Robust Tungstensteel Machine Casing", 30, false)
            .addCasingInfoExactly("Steel Gear Box Casing", 6, false)
            .addCasingInfoExactly("Extreme Engine Intake Casing", 12, false)
            .addCasingInfoExactly("PTFE Frame Box", 32, false)
            .addCasingInfoExactly("PTFE Pipe Casing", 10, false)
            .addCasingInfoMin("Tungstensteel Turbine Casing", 4, false)
            .addCasingInfoExactly("Tungstensteel Firebox Casing", 12, false)
            .addCasingInfoExactly("Chemically Inert Machine Casing", 30, false)
            .addDynamoHatch("Back center", 3)
            .addMaintenanceHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addMufflerHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addInputHatch("High Rating Fuel, next to a Gear Box", 2)
            .addInputHatch("Lubricant, next to a Gear Box", 2)
            .addInputHatch("Liquid Oxygen, optional, next to a Gear Box", 2)
            .addStructureAuthors(EnumChatFormatting.GOLD + "N7Paddy")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEExtremeCombustionEngine> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<FuelBackend> getRecipeMap() {
        return RecipeMaps.extremeDieselFuels;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.RobustTungstenSteelMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.RobustTungstenSteelMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.RobustTungstenSteelMachineCasing.getCasingTexture() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEExtremeCombustionEngine(this.mName);
    }

    protected int getNominalOutput() {
        return 10900;
    }

    protected Materials getBooster() {
        return Materials.LiquidOxygen;
    }

    protected int getEfficiencyIncrease() {
        return 20;
    }

    protected int getBoostFactor() {
        return 2;
    }

    protected int getAdditiveFactor() {
        return 1;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.proxy.mPollutionExtremeCombustionEnginePerSecond;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchDynamo tHatch : validMTEList(mDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.infodata.extreme_diesel_engine")
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            getIdealStatus() == getRepairStatus()
                ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.maintenance.false")
                    + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.maintenance.true")
                    + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.engine.output") + ": "
                + EnumChatFormatting.RED
                + formatNumber(lEUt * mEfficiency / 10000)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.engine.consumption") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(fuelConsumption)
                + EnumChatFormatting.RESET
                + " L/t",
            StatCollector.translateToLocal("GT5U.engine.value") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(fuelValue)
                + EnumChatFormatting.RESET
                + " EU/L",
            StatCollector.translateToLocal("GT5U.turbine.fuel") + ": "
                + EnumChatFormatting.GOLD
                + formatNumber(fuelRemaining)
                + EnumChatFormatting.RESET
                + " L",
            StatCollector.translateToLocal("GT5U.engine.efficiency") + ": "
                + EnumChatFormatting.YELLOW
                + (mEfficiency / 100F)
                + EnumChatFormatting.YELLOW
                + " %",
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + getAveragePollutionPercentage()
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        turbineCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && !mMufflerHatches.isEmpty()
            && casingAmount >= 30
            && turbineCasingAmount >= 4;
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
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ArrayList<FluidStack> tFluids = getStoredFluids();

        // fast track lookup
        if (!tFluids.isEmpty()) {
            double boostedFuelValue = 0;
            double boostedOutput = 0;
            double extraFuelFraction = 0;
            for (FluidStack tFluid : tFluids) {
                GTRecipe tRecipe = getRecipeMap().getBackend()
                    .findFuel(tFluid);
                if (tRecipe == null) continue;
                fuelValue = tRecipe.mSpecialValue;

                FluidStack tLiquid = tFluid.copy();
                if (boostEu) {
                    boostedFuelValue = GTUtility.safeInt((long) (fuelValue * 1.5));
                    boostedOutput = getNominalOutput() * 3;

                    fuelConsumption = tLiquid.amount = getBoostFactor() * getNominalOutput() / fuelValue;

                    // HOG consumption rate is normally 1 L/t, when it's supposed to be around 1.64 L/t
                    // This code increases fuel consumption by 1 at random, but with a weighted chance
                    if (boostedFuelValue * 2 > boostedOutput) {
                        extraFuelFraction = boostedOutput / boostedFuelValue;
                        extraFuelFraction = extraFuelFraction - (int) extraFuelFraction;
                        double rand = Math.random();
                        if (rand < extraFuelFraction) {
                            tLiquid.amount += 1;
                        }
                    }

                } else {
                    fuelConsumption = tLiquid.amount = getNominalOutput() / fuelValue;
                }

                // Deplete that amount
                if (!depleteInput(tLiquid)) return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                boostEu = depleteInput(getBooster().getGas(2L * getAdditiveFactor()));

                // Check to prevent burning HOG without consuming it, if not boosted
                if (!boostEu && fuelValue > getNominalOutput()) {
                    return SimpleCheckRecipeResult.ofFailure("fuel_quality_too_high");
                }

                // Deplete Lubricant. 1000L should = 1 hour of runtime (if baseEU = 2048)
                if ((mRuntime % 72 == 0 || mRuntime == 0)
                    && !depleteInput(Materials.Lubricant.getFluid((boostEu ? 2L : 1L) * getAdditiveFactor())))
                    return SimpleCheckRecipeResult.ofFailure("no_lubricant");

                fuelRemaining = tFluid.amount; // Record available fuel
                this.lEUt = mEfficiency < 2000 ? 0 : getNominalOutput(); // Output 0 if startup is less than 20%
                this.mProgresstime = 1;
                this.mMaxProgresstime = 1;
                this.mEfficiencyIncrease = getEfficiencyIncrease();
                return CheckRecipeResultRegistry.GENERATING;
            }
        }
        this.lEUt = 0;
        this.mEfficiency = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }
}
