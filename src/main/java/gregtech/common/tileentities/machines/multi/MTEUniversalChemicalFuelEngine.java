package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.util.DescTextLocalization;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class MTEUniversalChemicalFuelEngine extends TTMultiblockBase implements ISurvivalConstructable {

    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    protected final double DIESEL_EFFICIENCY_COEFFICIENT = 0.04D;
    protected final double GAS_EFFICIENCY_COEFFICIENT = 0.04D;
    protected final double ROCKET_EFFICIENCY_COEFFICIENT = 0.005D;
    protected final double EFFICIENCY_CEILING = 1.5D;
    protected final int HEATING_TIMER = TickTime.SECOND * 10;

    private long tEff;
    private int heatingTicks;
    private boolean isStoppingSafe;
    private int casingAmount;

    private IStructureDefinition<MTEUniversalChemicalFuelEngine> STRUCTURE_DEFINITION = null;

    public MTEUniversalChemicalFuelEngine(String name) {
        super(name);
        super.useLongPower = true;
    }

    public MTEUniversalChemicalFuelEngine(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        super.useLongPower = true;
    }

    public final boolean addInputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchInput) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mInputHatches.add((MTEHatchInput) aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addDynamoHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchDynamo) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mDynamoHatches.add((MTEHatchDynamo) aMetaTileEntity);
            } else if (aMetaTileEntity instanceof MTEHatchDynamoMulti) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.eDynamoMulti.add((MTEHatchDynamoMulti) aMetaTileEntity);
            }
        }
        return false;
    }

    @Override
    public IStructureDefinition<MTEUniversalChemicalFuelEngine> getStructure_EM() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEUniversalChemicalFuelEngine>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] { { "       ", "       ", "       ", "  BBB  ", "  B~B  ", "  BBB  ", "       " },
                        { "B     B", "FB   BF", "FAFEFAF", " FBBBF ", " EB BE ", " FBBBF ", "  FEF  " },
                        { "       ", " D   D ", " D   D ", "  BBB  ", "  B B  ", "  CBC  ", "  EEE  " },
                        { "B     B", "FB   BF", "FAFEFAF", " FBBBF ", " EB BE ", " FBBBF ", "  FEF  " },
                        { "       ", " D   D ", " D   D ", "  BBB  ", "  B B  ", "  CBC  ", "  EEE  " },
                        { "B     B", "FB   BF", "FAFEFAF", " FBBBF ", " EB BE ", " FBBBF ", "  FEF  " },
                        { "       ", " D   D ", " D   D ", "  BBB  ", "  B B  ", "  CBC  ", "  EEE  " },
                        { "B     B", "FB   BF", "FAFEFAF", " FBBBF ", " EB BE ", " FBBBF ", "  FEF  " },
                        { "       ", " D   D ", " D   D ", "  BBB  ", "  B B  ", "  CBC  ", "  EEE  " },
                        { "B     B", "FB   BF", "FAFEFAF", " FBBBF ", " EB BE ", " FBBBF ", "  FEF  " },
                        { "       ", " D   D ", " D   D ", "  BBB  ", "  B B  ", "  CBC  ", "  EEE  " },
                        { "B     B", "FB   BF", "FAFEFAF", " FBBBF ", " EB BE ", " FBBBF ", "  FEF  " },
                        { "       ", "       ", "       ", "  BBB  ", "  BGB  ", "  BBB  ", "       " } })
                .addElement('A', Casings.TitaniumPipeCasing.asElement())
                .addElement(
                    'B',
                    ofChain(
                        buildHatchAdder(MTEUniversalChemicalFuelEngine.class).atLeast(InputHatch, Muffler, Maintenance)
                            .casingIndex(Casings.StableTitaniumMachineCasing.textureId)
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.casingAmount, Casings.StableTitaniumMachineCasing.asElement())))
                .addElement('C', Casings.TitaniumFireboxCasing.asElement())
                .addElement('D', Casings.EngineIntakeCasing.asElement())
                .addElement('E', Casings.ChemicallyInertMachineCasing.asElement())
                .addElement('F', ofFrame(Materials.Polytetrafluoroethylene))
                .addElement('G', Dynamo.newAny(Casings.StableTitaniumMachineCasing.textureId, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        return structureCheck_EM(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && checkHatch()
            && casingAmount >= 100;
    }

    public boolean checkHatch() {
        return !mMufflerHatches.isEmpty() && !mInputHatches.isEmpty();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, stackSize, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("UniversalChemicalFuelEngine.hint", 11);
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return (int) Math.sqrt(this.getPowerFlow());
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Chemical Engine, UCFE")
            .addInfo("BURNING BURNING BURNING")
            .addInfo("Use combustible liquid to generate power")
            .addInfo("You need to supply Combustion Promoter to keep it running")
            .addInfo("It will consume all the fuel and combustion promoter in the hatch every second")
            .addInfo("Energy output to the dynamo will be distributed over the next second")
            .addInfo("If the Dynamo Hatch's buffer fills up, the machine will stop")
            .addInfo(
                "If the amount of energy to be produced is higher "
                    + "than the dynamo hatch can handle then all produced energy will void")
            .addInfo("When turned on, there is a 10-second period where the machine will not stop")
            .addInfo("Even if it doesn't stop, all the fuel in the hatch will be consumed")
            .addInfo("The efficiency is determined by the proportion of Combustion Promoter to fuel")
            .addInfo("The higher the amount of promoter, the higher the efficiency")
            .addInfo(
                "Follows an exponential curve exp(-C/(p/x))*1.5, "
                    + "where x is the amount of fuel in liters, p is the amount of promoter in liters")
            .addInfo("and C depends on the fuel type. Diesel: C=0.04; Gas: C=0.04; Rocket fuel: C=0.005")
            .addInfo("It creates sqrt(Current Output Power) pollution every second")
            .addInfo(
                "If you forget to supply Combustion Promoter, this engine will swallow all the fuel "
                    + EnumChatFormatting.YELLOW
                    + "without outputting energy")
            .addInfo("The efficiency is up to 150%")
            .addTecTechHatchInfo()
            .beginStructureBlock(7, 7, 13, false)
            .addController("Front center")
            .addCasingInfoMin("Stable Titanium Machine Casing", 100, false)
            .addCasingInfoExactly("Titanium Pipe Casing", 12, false)
            .addCasingInfoExactly("Engine Intake Casing", 20, false)
            .addCasingInfoExactly("Titanium Firebox Casing", 10, false)
            .addCasingInfoExactly("Chemically Inert Machine Casing", 39, false)
            .addCasingInfoExactly("PTFE Frame Box", 72, false)
            .addMaintenanceHatch("Any Stable Titanium Machine Casing")
            .addMufflerHatch("Any Stable Titanium Machine Casing")
            .addInputHatch("Any Stable Titanium Machine Casing")
            .addDynamoHatch("Back center of the machine")
            .addStructureAuthors(EnumChatFormatting.GOLD + "TimTems")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {

        ArrayList<FluidStack> tFluids = getStoredFluids();

        long PromoterAmount = findLiquidAmount(getPromoter(), tFluids);

        CheckRecipeResult result;

        result = processFuel(tFluids, RecipeMaps.dieselFuels, PromoterAmount, DIESEL_EFFICIENCY_COEFFICIENT, 1);
        if (result.wasSuccessful()) return result;

        result = processFuel(tFluids, RecipeMaps.gasTurbineFuels, PromoterAmount, GAS_EFFICIENCY_COEFFICIENT, 1);
        if (result.wasSuccessful()) return result;

        result = processFuel(tFluids, GTPPRecipeMaps.rocketFuels, PromoterAmount, ROCKET_EFFICIENCY_COEFFICIENT, 3);
        if (result.wasSuccessful()) return result;

        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    protected CheckRecipeResult processFuel(ArrayList<FluidStack> tFluids, RecipeMap<FuelBackend> recipeMap,
        long PromoterAmount, double efficiencyCoefficient, double FuelsValueBonus) {
        for (GTRecipe recipe : recipeMap.getAllRecipes()) {
            FluidStack tFuel = findFuel(recipe);
            if (tFuel == null) continue;
            long FuelAmount = findLiquidAmount(tFuel, tFluids);
            if (FuelAmount == 0) continue;
            calculateEfficiency(FuelAmount, PromoterAmount, efficiencyCoefficient);
            consumeAllLiquid(tFuel, tFluids);
            consumeAllLiquid(getPromoter(), tFluids);
            this.setPowerFlow((long) (FuelAmount * recipe.mSpecialValue * FuelsValueBonus / 20.0D));
            this.mMaxProgresstime = 20;
            this.updateSlots();
            return CheckRecipeResultRegistry.GENERATING;
        }
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        // Reset the counter for heating, so that it works again when the machine restarts
        heatingTicks = 0;
        super.stopMachine(reason);
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        super.onRunningTick(stack);
        // Counts ticks up to the defined timer (200 ticks, 10 seconds)
        // The multiblock will not stop due to excess energy during this time
        // Machine used to explode in the past, this timer was first made to prevent that
        if (heatingTicks < HEATING_TIMER) {
            heatingTicks++;
            isStoppingSafe = true;
        } else if (isStoppingSafe) isStoppingSafe = false;

        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            addAutoEnergy();
        }
        return true;
    }

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        info[4] = StatCollector.translateToLocalFormatted(
            "gg.scanner.info.generator.generates",
            EnumChatFormatting.RED + formatNumber(this.getPowerFlow() * tEff / 10000) + EnumChatFormatting.RESET);
        info[6] = StatCollector.translateToLocal("gg.scanner.info.generator.problems") + " "
            + EnumChatFormatting.RED
            + formatNumber(this.getIdealStatus() - this.getRepairStatus())
            + EnumChatFormatting.RESET
            + " "
            + StatCollector.translateToLocal("gg.scanner.info.generator.efficiency")
            + " "
            + EnumChatFormatting.YELLOW
            + formatNumber(tEff / 100D)
            + EnumChatFormatting.RESET
            + " %";
        return info;
    }

    void addAutoEnergy() {
        long exEU = this.getPowerFlow() * tEff / 10000;
        if (!mDynamoHatches.isEmpty()) {
            MTEHatchDynamo tHatch = mDynamoHatches.get(0);
            if (tHatch.maxEUOutput() * tHatch.maxAmperesOut() >= exEU) {
                tHatch.setEUVar(
                    Math.min(
                        tHatch.maxEUStore(),
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + exEU));
            } else if (!isStoppingSafe) {
                stopMachine(ShutDownReasonRegistry.INSUFFICIENT_DYNAMO);
            }
        }
        if (!eDynamoMulti.isEmpty()) {
            MTEHatchDynamoMulti tHatch = eDynamoMulti.get(0);
            if (tHatch.maxEUOutput() * tHatch.maxAmperesOut() >= exEU) {
                tHatch.setEUVar(
                    Math.min(
                        tHatch.maxEUStore(),
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + exEU));
            } else if (!isStoppingSafe) {
                stopMachine(ShutDownReasonRegistry.INSUFFICIENT_DYNAMO);
            }
        }
    }

    public FluidStack getPromoter() {
        return FluidRegistry.getFluidStack("combustionpromotor", 1);
    }

    public FluidStack findFuel(GTRecipe aFuel) {
        if (aFuel.mInputs != null && aFuel.mInputs.length > 0)
            return GTUtility.getFluidForFilledItem(aFuel.mInputs[0], true);
        else return aFuel.mFluidInputs[0];
    }

    public void calculateEfficiency(long aFuel, long aPromoter, double coefficient) {
        if (aPromoter == 0) {
            this.tEff = 0;
            return;
        }
        this.tEff = (int) (Math.exp(-coefficient * (double) aFuel / (double) aPromoter) * EFFICIENCY_CEILING * 10000);
    }

    public long findLiquidAmount(FluidStack liquid, List<FluidStack> input) {
        long cnt = 0;
        for (FluidStack fluid : input) {
            if (fluid.isFluidEqual(liquid)) {
                cnt += fluid.amount;
            }
        }
        if (cnt < 0) cnt = 0;
        return cnt;
    }

    public void consumeAllLiquid(FluidStack liquid, List<FluidStack> input) {
        for (FluidStack fluid : input) {
            if (fluid.isFluidEqual(liquid)) fluid.amount = 0;
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Casings.StableTitaniumMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.StableTitaniumMachineCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DIESEL_ENGINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.StableTitaniumMachineCasing.getCasingTexture() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEUniversalChemicalFuelEngine(STRUCTURE_PIECE_MAIN);
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

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTPPRecipeMaps.rocketFuels, RecipeMaps.dieselFuels, RecipeMaps.gasTurbineFuels);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -2;
    }
}
