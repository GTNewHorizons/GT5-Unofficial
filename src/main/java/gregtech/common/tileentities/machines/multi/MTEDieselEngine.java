package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
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

public class MTEDieselEngine extends MTEEnhancedMultiBlockBase<MTEDieselEngine> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<MTEDieselEngine>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<MTEDieselEngine> computeValue(Class<?> type) {
            return StructureDefinition.<MTEDieselEngine>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "---", "iii", "chc", "chc", "ccc", }, { "---", "i~i", "hgh", "hgh", "cdc", },
                            { "---", "iii", "chc", "chc", "ccc", }, }))
                .addElement('i', lazy(t -> ofBlock(t.getIntakeBlock(), t.getIntakeMeta())))
                .addElement('c', lazy(t -> ofBlock(t.getCasingBlock(), t.getCasingMeta())))
                .addElement('g', lazy(t -> ofBlock(t.getGearboxBlock(), t.getGearboxMeta())))
                .addElement('d', lazy(t -> Dynamo.newAny(t.getCasingTextureIndex(), 2)))
                .addElement(
                    'h',
                    lazy(
                        t -> buildHatchAdder(MTEDieselEngine.class)
                            .atLeast(InputHatch, InputHatch, InputHatch, Muffler, Maintenance)
                            .casingIndex(t.getCasingTextureIndex())
                            .hint(1)
                            .buildAndChain(t.getCasingBlock(), t.getCasingMeta())))
                .build();
        }
    };
    protected int fuelConsumption = 0;
    protected int fuelValue = 0;
    protected int fuelRemaining = 0;
    protected boolean boostEu = false;

    public MTEDieselEngine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEDieselEngine(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();

        String lubricantRate = TooltipHelper.fluidText(1000);
        String oxygenRate = TooltipHelper.fluidRateText(40);
        String defaultOutput = TooltipHelper.euText(2048);
        String defaultEfficiency = TooltipHelper.effText(1.0f);
        String boostedOutput = TooltipHelper.euText(6144);
        String boostedEfficiency = TooltipHelper.effText(1.5f);
        String waitPower = TooltipHelper.effText(3.0f);

        tt.addMachineType(GTUtility.translate("gt.multiblock.DieselEngine.machine_type"))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.desc1", lubricantRate))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.desc2", oxygenRate))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.default_output", defaultOutput, defaultEfficiency))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.boosted_output", boostedOutput, boostedEfficiency))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.wait_power", waitPower))
            .addInfo(GTUtility.translate("gt.multiblock.DieselEngine.intake_warning"))
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 4, false)
            .addController(GTUtility.translate("gt.mbtt.structure.front_center"))
            .addCasingInfoRange(GTUtility.translate("gt.blockcasings4.Casing_StableTitanium"), 16, 22, false)
            .addOtherStructurePart(
                GTUtility.translate("gt.blockcasings2.Casing_Gearbox_Titanium"),
                GTUtility.translate("gt.multiblock.DieselEngine.gear_box_inner"))
            .addOtherStructurePart(
                GTUtility.translate("gt.blockcasings4.Casing_EngineIntake"),
                GTUtility.translate("gt.multiblock.DieselEngine.engine_intake_ring"))
            .addDynamoHatch(GTUtility.translate("gt.mbtt.structure.back_center"), 2)
            .addMaintenanceHatch(GTUtility.translate("gt.multiblock.DieselEngine.maintenance_hatch"), 1)
            .addMufflerHatch(GTUtility.translate("gt.multiblock.DieselEngine.muffler_hatch"), 1)
            .addInputHatch(GTUtility.translate("gt.multiblock.DieselEngine.diesel_fuel"), 1)
            .addInputHatch(GTUtility.translate("gt.multiblock.DieselEngine.lubricant"), 1)
            .addInputHatch(GTUtility.translate("gt.multiblock.DieselEngine.oxygen_optional"), 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][50], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][50], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DIESEL_ENGINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][50] };
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    @Override
    public RecipeMap<FuelBackend> getRecipeMap() {
        return RecipeMaps.dieselFuels;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    /**
     * The nominal energy output This can be further multiplied by {@link #getMaxEfficiency(ItemStack)} when boosted
     */
    protected int getNominalOutput() {
        return 2048;
    }

    protected Materials getBooster() {
        return Materials.Oxygen;
    }

    /**
     * x times fuel will be consumed when boosted This will however NOT increase power output Go tweak
     * {@link #getMaxEfficiency(ItemStack)} and {@link #getNominalOutput()} instead
     */
    protected int getBoostFactor() {
        return 2;
    }

    /**
     * x times of additive will be consumed when boosted
     */
    protected int getAdditiveFactor() {
        return 1;
    }

    /**
     * Efficiency will increase by this amount every tick
     */
    protected int getEfficiencyIncrease() {
        return 15;
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
                this.mEUt = mEfficiency < 2000 ? 0 : getNominalOutput(); // Output 0 if startup is less than 20%
                this.mProgresstime = 1;
                this.mMaxProgresstime = 1;
                this.mEfficiencyIncrease = getEfficiencyIncrease();
                return CheckRecipeResultRegistry.GENERATING;
            }
        }
        this.mEUt = 0;
        this.mEfficiency = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public IStructureDefinition<MTEDieselEngine> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 1) && !mMufflerHatches.isEmpty()
            && mMaintenanceHatches.size() == 1;
    }

    public Block getCasingBlock() {
        return GregTechAPI.sBlockCasings4;
    }

    public byte getCasingMeta() {
        return 2;
    }

    public Block getIntakeBlock() {
        return GregTechAPI.sBlockCasings4;
    }

    public byte getIntakeMeta() {
        return 13;
    }

    public Block getGearboxBlock() {
        return GregTechAPI.sBlockCasings2;
    }

    public byte getGearboxMeta() {
        return 4;
    }

    public byte getCasingTextureIndex() {
        return 50;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDieselEngine(this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mEfficiency", mEfficiency);
        aNBT.setBoolean("boostEu", boostEu);
        aNBT.setInteger("fuelConsumption", fuelConsumption);
        aNBT.setInteger("fuelValue", fuelValue);
        aNBT.setInteger("fuelRemaining", fuelRemaining);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mEfficiency = aNBT.getInteger("mEfficiency");
        boostEu = aNBT.getBoolean("boostEu");
        fuelConsumption = aNBT.getInteger("fuelConsumption");
        fuelValue = aNBT.getInteger("fuelValue");
        fuelRemaining = aNBT.getInteger("fuelRemaining");
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return boostEu ? 30000 : 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.proxy.mPollutionLargeCombustionEnginePerSecond;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
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
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.infodata.diesel_engine")
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
                + formatNumber(((long) mEUt * mEfficiency / 10000))
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
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 1, elementBudget, env, false, true);
    }
}
