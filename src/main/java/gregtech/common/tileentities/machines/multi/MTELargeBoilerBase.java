package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GTValues.STEAM_PER_WATER;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.ItemList.Circuit_Integrated;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.LargeBoilerFuelBackend;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTELargeBoilerGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTELargeBoilerBase extends MTEExtendedPowerMultiBlockBase<MTELargeBoilerBase>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;

    protected Casings casing;
    protected Casings pipeCasing;
    protected Casings fireboxCasing;

    private final int eut;
    private final int efficiencyIncrease;
    private final boolean isSuperheated;

    private boolean firstRun = true;
    private int efficiencyChangePerTick = 0;
    private int integratedCircuitConfig = 0;
    private int excessWater = 0;
    private int excessFuel = 0;
    private int excessProjectedEU = 0;
    private int excessSuperheatedSteam = 0;
    private int casingAmount;
    private int fireboxAmount;
    protected final int pollutionPerSecond;
    private final IStructureDefinition<MTELargeBoilerBase> structureDefinition;
    private final int superToNormalSteam = 3;
    private int fluidBurnTime = 0;
    private int solidBurnTime = 0;
    private int maxFluidBurnTime = 0;
    private int maxSolidBurnTime = 0;
    private int burnDecrease = 2;

    protected MTELargeBoilerBase(int aID, String aName, String aNameRegional, Casings casing, Casings pipeCasing,
        Casings fireboxCasing, int eut, int efficiencyIncrease, boolean isSuperheated, int pollutionPerSecond) {
        super(aID, aName, aNameRegional);
        this.casing = casing;
        this.pipeCasing = pipeCasing;
        this.fireboxCasing = fireboxCasing;
        this.eut = eut;
        this.efficiencyIncrease = efficiencyIncrease;
        this.isSuperheated = isSuperheated;
        this.pollutionPerSecond = pollutionPerSecond;
        this.structureDefinition = createStructureDefinition();
        this.efficiencyDecrease = 1;
    }

    protected MTELargeBoilerBase(String aName, Casings casing, Casings pipeCasing, Casings fireboxCasing, int eut,
        int efficiencyIncrease, boolean isSuperheated, int pollutionPerSecond) {
        super(aName);
        this.casing = casing;
        this.pipeCasing = pipeCasing;
        this.fireboxCasing = fireboxCasing;
        this.eut = eut;
        this.efficiencyIncrease = efficiencyIncrease;
        this.isSuperheated = isSuperheated;
        this.pollutionPerSecond = pollutionPerSecond;
        this.structureDefinition = createStructureDefinition();
        this.efficiencyDecrease = 1;
    }

    private IStructureDefinition<MTELargeBoilerBase> createStructureDefinition() {
        return StructureDefinition.<MTELargeBoilerBase>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                // spotless:off
                new String[][]{{
                    "   C ",
                    "   C ",
                    "   C ",
                    " CCC ",
                    "CC~CC",
                    "FFFFF"
                }, {
                    "  CCC",
                    "  C C",
                    "PPC C",
                    "PCC P",
                    "C   C",
                    "FFFFF"
                }, {
                    "   C ",
                    "   C ",
                    "   C ",
                    " CCC ",
                    "CCCCC",
                    "FFFFF"
                }})
            //spotless:on
            .addElement('P', ofBlock(pipeCasing.getBlock(), pipeCasing.getBlockMeta()))
            .addElement(
                'C',
                buildHatchAdder(MTELargeBoilerBase.class)
                    .atLeast(InputHatch, InputBus, OutputHatch, Maintenance, Muffler)
                    .casingIndex(casing.getTextureId())
                    .hint(1)
                    .buildAndChain(
                        onElementPass(
                            MTELargeBoilerBase::onCasingAdded,
                            ofBlock(casing.getBlock(), casing.getBlockMeta()))))
            .addElement(
                'F',
                buildHatchAdder(MTELargeBoilerBase.class)
                    .atLeast(InputHatch, InputBus, OutputHatch, Maintenance, Muffler)
                    .casingIndex(fireboxCasing.getTextureId())
                    .hint(1)
                    .buildAndChain(
                        onElementPass(
                            MTELargeBoilerBase::onFireboxAdded,
                            ofBlock(fireboxCasing.getBlock(), fireboxCasing.getBlockMeta()))))
            .build();
    }

    public Casings getCasing() {
        return casing;
    }

    public Casings getPipeCasing() {
        return pipeCasing;
    }

    public Casings getFireboxCasing() {
        return fireboxCasing;
    }

    public abstract String getCasingMaterial();

    public abstract String getCasingBlockType();

    public final int getEUt() {
        return eut;
    }

    public final int getEfficiencyIncrease() {
        return efficiencyIncrease;
    }

    public boolean isSuperheated() {
        return isSuperheated;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Boiler");
        if (isSuperheated()) {
            tt.addInfo(
                StatCollector.translateToLocalFormatted(
                    "GT5U.machines.large_boiler.info.tooltip.1.sh",
                    formatNumber((getEUt() * 40) * ((runtimeBoost(20) / (20f)) / 3)),
                    formatNumber((getEUt() * 40L) / 3)))
                .addInfo(StatCollector.translateToLocal("GT5U.machines.large_boiler.info.tooltip.2.sh"));
        } else {
            tt.addInfo(
                StatCollector.translateToLocalFormatted(
                    "GT5U.machines.large_boiler.info.tooltip.1.normal",
                    formatNumber((getEUt() * 40) * (runtimeBoost(20) / 20f)),
                    formatNumber(getEUt() * 40L)))
                .addInfo(StatCollector.translateToLocal("GT5U.machines.large_boiler.info.tooltip.2.normal"));
        }
        tt.addInfo(StatCollector.translateToLocal("GT5U.machines.large_boiler.info.tooltip.3"))
            .addInfo(StatCollector.translateToLocal("GT5U.machines.large_boiler.info.tooltip.4"))
            .addInfo(StatCollector.translateToLocal("GT5U.machines.large_boiler.info.tooltip.5"))
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "GT5U.machines.large_boiler.info.tooltip.6",
                    formatNumber(500.0 / getEfficiencyIncrease())))
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 6, 3, false)
            .addController("Front center, 2nd layer")
            .addCasing("20-28", getCasingMaterial() + " " + getCasingBlockType(), false)
            .addCasing("5-15", getCasingMaterial() + " Firebox Casing", false)
            .addCasing("4", getCasingMaterial() + " Pipe Casing", false)
            .addMaintenanceHatch("1", "Any machine or firebox casing", 1)
            .addMufflerHatch("1", "Any machine or firebox casing", 1)
            .addInputBus("0+", "Any machine or firebox casing", 1)
            .addInputHatch("1+", "Any machine or firebox casing", 1)
            .addOutputHatch("1+", "Any machine or firebox casing", 1)
            .addStructureInfo("")
            .addStructureFooter("Use solid fuel, liquid fuel, or both")
            .addStructureFooter("Use regular or distilled water")
            .addStructureAuthors(EnumChatFormatting.GOLD + "PCGMatt")
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
            OVERLAY_FRONT_LARGE_BOILER,
            OVERLAY_FRONT_LARGE_BOILER_GLOW,
            OVERLAY_FRONT_LARGE_BOILER_ACTIVE,
            OVERLAY_FRONT_LARGE_BOILER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return BlockIcons.getCasingTextureForId(casing.getTextureId());
    }

    boolean isFuelValid(ItemStack tInput) {
        if (!isSuperheated()) return true;
        return LargeBoilerFuelBackend.isAllowedFuel(tInput);
    }

    boolean isFuelValid(FluidStack tInput) {
        if (!isSuperheated()) return true;
        return LargeBoilerFuelBackend.isAllowedFuel(tInput);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Only for visual
        return RecipeMaps.largeBoilerFakeFuels;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        // Do we have an integrated circuit with a valid configuration?
        if (Circuit_Integrated.isStackEqual(mInventory[1], true, true)) {
            int circuit_config = mInventory[1].getItemDamage();
            if (circuit_config >= 1 && circuit_config <= 25) {
                // If so, overwrite the current config
                this.integratedCircuitConfig = circuit_config;
            }
        } else {
            // If not, set the config to zero
            this.integratedCircuitConfig = 0;
        }

        if (fluidBurnTime == 0) {
            for (FluidStack fluidInput : this.getStoredFluids()) {
                GTRecipe foundRecipe = RecipeMaps.largeBoilerFakeFuels.findRecipeQuery()
                    .fluids(fluidInput)
                    .find();
                if (foundRecipe != null && isFuelValid(fluidInput)) {
                    FluidStack toDeplete = new FluidStack(fluidInput.getFluid(), 1000);
                    if (depleteInput(toDeplete)) {
                        setupBoilerRecipe(foundRecipe.mDuration, getEfficiencyIncrease(), true);
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
                }
            }
        }

        if (solidBurnTime == 0) {
            ArrayList<ItemStack> tInputList = getStoredInputs();
            if (!tInputList.isEmpty()) {
                for (ItemStack tInput : tInputList) {
                    long fuelValue = GTModHandler.getFuelValue(tInput);
                    if (GTUtility.getFluidForFilledItem(tInput, true) == null && (fuelValue / 80) > 0
                        && (fuelValue * 2L / this.getEUt()) > 1
                        && isFuelValid(tInput)) {
                        int burnTime = GTUtility.safeInt(fuelValue / 80, 1);
                        this.excessFuel += (int) (fuelValue % 80);
                        burnTime += this.excessFuel / 80;
                        this.excessFuel %= 80;
                        setupBoilerRecipe(burnTime, getEfficiencyIncrease(), false);
                        tInput.stackSize -= 1;
                        updateSlots();
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
                }
            }
        }

        if (solidBurnTime > 0 || fluidBurnTime > 0) {
            setupBoiler();
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        this.mMaxProgresstime = 0;
        this.mEfficiencyIncrease = 0;
        this.efficiencyChangePerTick = 0;
        this.lEUt = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    private void tickBurnTime() {
        if (fluidBurnTime > 0 && solidBurnTime > 0) burnDecrease = 1;
        else burnDecrease = 2;
        fluidBurnTime = Math.max(fluidBurnTime - burnDecrease, 0);
        solidBurnTime = Math.max(solidBurnTime - burnDecrease, 0);
    }

    private void setupBoilerRecipe(int rawBurnTime, int changePerTick, boolean isFluid) {
        rawBurnTime = runtimeBoost(rawBurnTime);
        int safeBurnTime = Math.max(1, rawBurnTime);
        this.mMaxProgresstime = 1;
        int adjustedTime = adjustBurnTimeForConfig(safeBurnTime) * 2;
        if (isFluid) {
            this.fluidBurnTime = this.maxFluidBurnTime = adjustedTime;
        } else {
            this.solidBurnTime = this.maxSolidBurnTime = adjustedTime;
        }
        this.efficiencyChangePerTick = getEfficiencyChangePerTick(safeBurnTime, changePerTick);
        this.mEfficiencyIncrease = 1;
        this.lEUt = adjustEUtForConfig(getEUt());
    }

    private void setupBoiler() {
        // Is used when boiler is force shut down, which resets values except internal burn time, soft locking the
        // progress
        this.mMaxProgresstime = 1;
        this.mEfficiencyIncrease = 1;
        this.lEUt = adjustEUtForConfig(getEUt());
    }

    private int getEfficiencyChangePerTick(int burnTime, int changePerTick) {
        return (long) burnTime * changePerTick > 5000L ? 20 : Math.max(1, changePerTick);
    }

    abstract int runtimeBoost(int mTime);

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.lEUt > 0) {
            int maxEff = getCorrectedMaxEfficiency(mInventory[1]);
            adjustEfficiencyTowards(maxEff, Math.max(1, efficiencyChangePerTick));
            int tGeneratedEU = GTUtility.safeInt(this.lEUt * 2L * this.mEfficiency / 10000L);
            if (tGeneratedEU > 0) {
                int waterToConsume = getWaterConsumptionForSteam(tGeneratedEU);
                startRecipeProcessing();
                if (isSuperheated()) {
                    if (consumeWater(waterToConsume)) {
                        int superheatedSteam = getSuperheatedSteam(tGeneratedEU);
                        if (superheatedSteam > 0) {
                            tickBurnTime();
                            addOutputPartial(FluidRegistry.getFluidStack("ic2superheatedsteam", superheatedSteam));
                        }
                    } else {
                        GTLog.writeExplosionLog(this, "Boiler had no water");
                        explodeMultiblock();
                    }
                } else {
                    if (consumeWater(waterToConsume)) {
                        tickBurnTime();
                        addOutputPartial(Materials.Steam.getGas(tGeneratedEU));
                    } else {
                        GTLog.writeExplosionLog(this, "Boiler had no water");
                        explodeMultiblock();
                    }
                }
                endRecipeProcessing();
            }
            return true;
        }
        return true;
    }

    @Override
    public int getMaxEfficiency(@Nullable ItemStack aStack) {
        return burnDecrease == 1 ? 12500 : 10000;
    }

    private void adjustEfficiencyTowards(int targetEfficiency, int changePerTick) {
        if (mEfficiency < targetEfficiency) {
            mEfficiency = Math.min(mEfficiency + changePerTick, targetEfficiency);
        } else if (mEfficiency > targetEfficiency) {
            mEfficiency = Math.max(mEfficiency - efficiencyDecrease, targetEfficiency);
        }
    }

    private int getWaterConsumptionForSteam(long steamEquivalent) {
        long waterDenominator = (long) STEAM_PER_WATER * (isSuperheated() ? superToNormalSteam : 1);
        long waterToConsume = steamEquivalent / waterDenominator;
        excessWater += (int) (steamEquivalent % waterDenominator);
        waterToConsume += excessWater / waterDenominator;
        excessWater %= waterDenominator;
        return GTUtility.safeInt(waterToConsume);
    }

    private boolean consumeWater(int amount) {
        if (amount <= 0) return depleteInput(Materials.Water.getFluid(1), true)
            || depleteInput(GTModHandler.getDistilledWater(1), true);
        return depleteInput(Materials.Water.getFluid(amount)) || depleteInput(GTModHandler.getDistilledWater(amount));
    }

    private int getSuperheatedSteam(long steamEquivalent) {
        long steamToOutput = steamEquivalent / superToNormalSteam;
        excessSuperheatedSteam += (int) (steamEquivalent % superToNormalSteam);
        steamToOutput += excessSuperheatedSteam / superToNormalSteam;
        excessSuperheatedSteam %= superToNormalSteam;
        return GTUtility.safeInt(steamToOutput);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("excessFuel", excessFuel);
        aNBT.setInteger("excessWater", excessWater);
        aNBT.setInteger("excessProjectedEU", excessProjectedEU);
        aNBT.setInteger("excessSuperheatedSteam", excessSuperheatedSteam);
        aNBT.setInteger("efficiencyChangePerTick", efficiencyChangePerTick);
        aNBT.setInteger("burnDecrease", burnDecrease);
        aNBT.setInteger("solidBurnTime", solidBurnTime);
        aNBT.setInteger("fluidBurnTime", fluidBurnTime);
        aNBT.setInteger("maxSolidBurnTime", maxSolidBurnTime);
        aNBT.setInteger("maxFluidBurnTime", maxFluidBurnTime);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        excessFuel = aNBT.getInteger("excessFuel");
        excessWater = aNBT.getInteger("excessWater");
        excessProjectedEU = aNBT.getInteger("excessProjectedEU");
        excessSuperheatedSteam = aNBT.getInteger("excessSuperheatedSteam");
        efficiencyChangePerTick = aNBT.hasKey("efficiencyChangePerTick") ? aNBT.getInteger("efficiencyChangePerTick")
            : getEfficiencyIncrease();
        mEfficiencyIncrease = 0;
        burnDecrease = aNBT.hasKey("burnDecrease") ? aNBT.getInteger("burnDecrease") : 2;
        solidBurnTime = aNBT.getInteger("solidBurnTime");
        fluidBurnTime = aNBT.getInteger("fluidBurnTime");
        maxSolidBurnTime = aNBT.getInteger("maxSolidBurnTime");
        maxFluidBurnTime = aNBT.getInteger("maxFluidBurnTime");
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mProgresstime > 0 && firstRun) {
            firstRun = false;
            GTMod.achievements.issueAchievement(
                aBaseMetaTileEntity.getWorld()
                    .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                "extremepressure");
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public IStructureDefinition<MTELargeBoilerBase> getStructureDefinition() {
        return structureDefinition;
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    private void onFireboxAdded() {
        fireboxAmount++;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        fireboxAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 20);
        checkCasingMin(errors, fireboxAmount, 5);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
    }

    private int adjustEUtForConfig(int rawEUt) {
        int adjustedSteamOutput = rawEUt - (isSuperheated() ? 75 : 25) * integratedCircuitConfig;
        return Math.max(adjustedSteamOutput, 25);
    }

    private int getCorrectedMaxEfficiency(ItemStack itemStack) {
        return Math.max(0, getMaxEfficiency(itemStack) - ((getIdealStatus() - getRepairStatus()) * 1000));
    }

    private int adjustBurnTimeForConfig(int rawBurnTime) {
        int adjustedEUt = Math.max(25, getEUt() - (isSuperheated() ? 75 : 25) * integratedCircuitConfig);
        int adjustedBurnTime = (int) (rawBurnTime * (long) getEUt() / adjustedEUt);
        this.excessProjectedEU += getEUt() * rawBurnTime - adjustedEUt * adjustedBurnTime;
        adjustedBurnTime += this.excessProjectedEU / adjustedEUt;
        this.excessProjectedEU %= adjustedEUt;
        return adjustedBurnTime;
    }

    public double getCurrentSteamOutputPerTick() {
        if (lEUt <= 0 || mMaxProgresstime <= 0) return 0D;
        double steamEquivalent = lEUt * 2D * mEfficiency / 10000D;
        return isSuperheated() ? steamEquivalent / superToNormalSteam : steamEquivalent;
    }

    public double getCurrentWaterConsumptionPerTick() {
        if (lEUt <= 0 || mMaxProgresstime <= 0) return 0D;
        double steamEquivalent = lEUt * 2D * mEfficiency / 10000D;
        return steamEquivalent / STEAM_PER_WATER / (isSuperheated() ? superToNormalSteam : 1);
    }

    private double getCurrentEnergyOutputPerTick() {
        return getCurrentSteamOutputPerTick() * (isSuperheated() ? 1.5D : 0.5D);
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>();
        for (String line : super.getInfoData()) {
            info.add(line);
        }
        double steamPerTick = getCurrentSteamOutputPerTick();
        if (steamPerTick > 0D) {
            info.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.large_boiler.scanner.steam_output",
                    formatNumber(steamPerTick),
                    "L"));
        }
        double waterPerTick = getCurrentWaterConsumptionPerTick();
        if (waterPerTick > 0D) {
            info.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.large_boiler.scanner.water_consumption",
                    formatNumber(waterPerTick),
                    "L"));
        }
        return info.toArray(new String[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        double steamPerTick = tag.getDouble("largeBoilerSteamPerTick");
        if (steamPerTick > 0D) {
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    isSuperheated() ? "GT5U.waila.large_boiler.superheated_steam_output"
                        : "GT5U.waila.large_boiler.steam_output",
                    formatNumber(steamPerTick),
                    "L"));
        }
        double waterPerTick = tag.getDouble("largeBoilerWaterPerTick");
        if (waterPerTick > 0D) {
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.large_boiler.water_consumption",
                    formatNumber(waterPerTick),
                    "L"));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setDouble("largeBoilerSteamPerTick", getCurrentSteamOutputPerTick());
        tag.setDouble("largeBoilerEnergyPerTick", getCurrentEnergyOutputPerTick());
        tag.setDouble("largeBoilerWaterPerTick", getCurrentWaterConsumptionPerTick());
    }

    public int getFluidBurnTime() {
        return fluidBurnTime;
    }

    public int getSolidBurnTime() {
        return solidBurnTime;
    }

    public int getMaxFluidBurnTime() {
        return maxFluidBurnTime;
    }

    public int getMaxSolidBurnTime() {
        return maxSolidBurnTime;
    }

    public int getBurnDecrease() {
        return burnDecrease;
    }

    @Override
    protected @NotNull MTELargeBoilerGui getGui() {
        return new MTELargeBoilerGui(this);
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

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_BOILER;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return Math.max(
            0,
            (int) (pollutionPerSecond * (1 - GTMod.proxy.mPollutionReleasedByThrottle * integratedCircuitConfig)));
    }
}
