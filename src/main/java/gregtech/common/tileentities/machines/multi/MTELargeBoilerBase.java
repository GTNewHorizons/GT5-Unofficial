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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.LargeBoilerFuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTELargeBoilerBase extends MTEExtendedPowerMultiBlockBase<MTELargeBoilerBase>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final long FUEL_TO_BURN_TIME_DIVISOR = 80L;

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
    private int currentMaxEfficiency = 10000;
    private int currentFuelCount = 0;
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
    public int getMaxEfficiency(ItemStack aStack) {
        return currentMaxEfficiency;
    }

    private int getMaxFuelInputs() {
        if (getEUt() >= MTELargeBoilerTungstenSteel.EUT_GENERATED) return 4;
        if (getEUt() >= MTELargeBoilerTitanium.EUT_GENERATED) return 3;
        if (getEUt() >= MTELargeBoilerSteel.EUT_GENERATED) return 2;
        return 1;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();

        tt.addMachineType("Boiler");
        if (isSuperheated()) {
            tt.addInfo(
                "Produces " + formatNumber((getEUt() * 40) * ((runtimeBoost(20) / (20f)) / 3))
                    + "L of Superheated Steam with 1 Coal at "
                    + formatNumber((getEUt() * 40L) / 3)
                    + "L/s")
                .addInfo("A programmed circuit in the main block throttles the boiler (-1000L/s per config)")
                .addInfo("Only some solid fuels are allowed (check the NEI Large Boiler tab for details)")
                .addInfo("If there are any disallowed fuels in the input bus, the boiler won't run!");
        } else {
            tt.addInfo(
                "Produces " + formatNumber((getEUt() * 40) * (runtimeBoost(20) / 20f))
                    + "L of Steam with 1 Coal at "
                    + formatNumber(getEUt() * 40L)
                    + "L/s")
                .addInfo("A programmed circuit in the main block throttles the boiler (-1000L/s per config)")
                .addInfo("Solid Fuels with a burn value that is too high or too low will not work");
        }
        tt.addInfo(
            String.format(
                "Diesel fuels have 1/4 efficiency - Takes %s seconds to heat up",
                formatNumber(500.0 / getEfficiencyIncrease())))
            .addInfo(
                "Combines up to " + getMaxFuelInputs()
                    + " fuel(s), up to "
                    + formatNumber(getMaxFuelInputs() * 100D)
                    + "% target efficiency")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 6, 3, false)
            .addController("Front center, 2nd layer")
            .addCasingInfoMin(getCasingMaterial() + " " + getCasingBlockType() + " Casing", 20, false)
            .addOtherStructurePart(getCasingMaterial() + " Fire Boxes", "Bottom layer, 5 minimum")
            .addOtherStructurePart(getCasingMaterial() + " Pipe Casing Blocks", "Inner 3 blocks")
            .addMaintenanceHatch("Any casing or firebox", 1)
            .addMufflerHatch("Any casing or firebox", 1)
            .addInputBus("Solid fuel, Any casing or firebox", 1)
            .addInputHatch("Liquid fuel, Any casing or firebox", 1)
            .addStructureInfo("You can use either, or both")
            .addInputHatch("Water, Any casing or firebox", 1)
            .addOutputHatch("Steam, Any casing or firebox", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "PCGMatt")
            .toolTipFinisher();

        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { BlockIcons.getCasingTextureForId(casing.getTextureId()),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { BlockIcons.getCasingTextureForId(casing.getTextureId()), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_BOILER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { BlockIcons.getCasingTextureForId(casing.getTextureId()) };
    }

    boolean isFuelValid() {
        if (!isSuperheated()) return true;
        for (ItemStack input : getStoredInputs()) {
            if (!LargeBoilerFuelBackend.isAllowedSolidFuel(input)
                && !Circuit_Integrated.isStackEqual(input, true, true)) {
                // if any item is not in ALLOWED_SOLID_FUELS, operation cannot be allowed because it might still be
                // consumed
                this.mMaxProgresstime = 0;
                this.mEUt = 0;
                return false;
            }
        }
        return true;
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
        if (!isFuelValid()) return CheckRecipeResultRegistry.NO_FUEL_FOUND;
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

        int maxFuelInputs = getMaxFuelInputs();
        List<FuelCandidate> fuels = new ArrayList<>();

        // Collect liquid fuel candidates (non-superheated only)
        if (!isSuperheated()) {
            for (GTRecipe tRecipe : RecipeMaps.dieselFuels.getAllRecipes()) {
                FluidStack tFluid = GTUtility.getFluidForFilledItem(tRecipe.getRepresentativeInput(0), true);
                if (tFluid != null && tRecipe.mSpecialValue > 1) {
                    fuels.add(
                        new FuelCandidate(
                            null,
                            new FluidStack(tFluid.getFluid(), 1000),
                            tRecipe.mSpecialValue * 250L,
                            true,
                            false));
                }
            }
            for (GTRecipe tRecipe : RecipeMaps.denseLiquidFuels.getAllRecipes()) {
                FluidStack tFluid = GTUtility.getFluidForFilledItem(tRecipe.getRepresentativeInput(0), true);
                if (tFluid != null && tRecipe.mSpecialValue > 0) {
                    fuels.add(
                        new FuelCandidate(
                            null,
                            new FluidStack(tFluid.getFluid(), 1000),
                            tRecipe.mSpecialValue * 1000L,
                            false,
                            false));
                }
            }
        }

        // Collect solid fuel candidates
        for (ItemStack tInput : getStoredInputs()) {
            if (tInput == null || tInput.stackSize <= 0) continue;
            if (tInput == GTOreDictUnificator.get(OrePrefixes.bucket, Materials.Lava, 1)) continue;
            if (GTUtility.getFluidForFilledItem(tInput, true) != null) continue;
            long fuelValue = GTModHandler.getFuelValue(tInput);
            if (fuelValue <= 0 || (fuelValue / 80) <= 0) continue;
            if (isSuperheated()) {
                if (!LargeBoilerFuelBackend.isAllowedSolidFuel(tInput)) continue;
            } else if (fuelValue * 2L / this.getEUt() <= 1) {
                continue;
            }
            for (int i = 0; i < Math.min(tInput.stackSize, maxFuelInputs); i++) {
                fuels.add(new FuelCandidate(tInput, null, fuelValue, false, true));
            }
        }

        if (fuels.isEmpty()) {
            resetFuelState();
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        // Sort descending by energy so the highest-value fuel is always primary
        fuels.sort((left, right) -> Long.compare(right.effectiveEnergy, left.effectiveEnergy));

        // Walk the sorted list, deplete fluids eagerly, collect up to maxFuelInputs that succeed
        List<FuelCandidate> selectedFuels = new ArrayList<>();
        for (FuelCandidate candidate : fuels) {
            if (selectedFuels.size() >= maxFuelInputs) break;
            if (candidate.fluidStack != null) {
                if (!depleteInput(new FluidStack(candidate.fluidStack.getFluid(), 1000))) continue;
            }
            selectedFuels.add(candidate);
        }

        if (selectedFuels.isEmpty()) {
            resetFuelState();
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        FuelCandidate primaryFuel = selectedFuels.get(0);
        long totalFuelValue = 0;
        for (FuelCandidate fuel : selectedFuels) {
            totalFuelValue += fuel.effectiveEnergy;
        }

        // Consume solid fuels and collect any container outputs
        List<ItemStack> outputItems = new ArrayList<>();
        for (FuelCandidate fuel : selectedFuels) {
            if (fuel.itemStack != null) {
                ItemStack container = GTUtility.getContainerItem(fuel.itemStack, true);
                if (container != null) outputItems.add(container);
                fuel.itemStack.stackSize -= 1;
            }
        }
        this.mOutputItems = outputItems.isEmpty() ? null : outputItems.toArray(new ItemStack[0]);
        updateSlots();

        // Compute burn time from primary fuel, carrying the remainder into the next call
        long burnTicksWithRemainder = primaryFuel.effectiveEnergy + excessFuel;
        long baseBurnTicks = burnTicksWithRemainder / FUEL_TO_BURN_TIME_DIVISOR;
        excessFuel = (int) (burnTicksWithRemainder % FUEL_TO_BURN_TIME_DIVISOR);
        if (baseBurnTicks <= 0) {
            resetFuelState();
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        if (primaryFuel.isSolid && !isSuperheated()) {
            baseBurnTicks = (long) (baseBurnTicks * getLongBurntimeRatio(primaryFuel.effectiveEnergy));
        }

        double fuelMultiplier = Math.max(1D, (double) totalFuelValue / primaryFuel.effectiveEnergy);
        this.currentFuelCount = selectedFuels.size();
        this.currentMaxEfficiency = (int) Math.min(maxFuelInputs * 10000, Math.round(10000D * fuelMultiplier));

        int baseChangePerTick = primaryFuel.isDiesel ? getEfficiencyIncrease() * 4 : getEfficiencyIncrease();
        int rawBurnTime = GTUtility.safeInt(baseBurnTicks, 1);
        setupBoilerRecipe(runtimeBoost(rawBurnTime), (int) Math.ceil(baseChangePerTick * fuelMultiplier));

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void resetFuelState() {
        this.mMaxProgresstime = 0;
        this.mEUt = 0;
        this.mEfficiencyIncrease = 0;
        this.efficiencyChangePerTick = 0;
        this.currentFuelCount = 0;
        this.currentMaxEfficiency = 10000;
    }

    private void setupBoilerRecipe(int rawBurnTime, int changePerTick) {
        int safeBurnTime = Math.max(1, rawBurnTime);
        this.mMaxProgresstime = adjustBurnTimeForConfig(safeBurnTime);
        this.efficiencyChangePerTick = getEfficiencyChangePerTick(safeBurnTime, changePerTick);
        this.mEfficiencyIncrease = 0;
        this.mEUt = adjustEUtForConfig(getEUt());
    }

    private int getEfficiencyChangePerTick(int burnTime, int changePerTick) {
        return (long) burnTime * changePerTick > 5000L ? 20 : Math.max(1, changePerTick);
    }

    private double getLongBurntimeRatio(long fuelValue) {
        double logScale = Math.log((float) fuelValue / 1600) / Math.log(9);
        return 1 + logScale * 0.025;
    }

    abstract int runtimeBoost(int mTime);

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mEUt > 0) {
            int maxEff = getCorrectedMaxEfficiency(mInventory[1]);
            adjustEfficiencyTowards(maxEff, Math.max(1, efficiencyChangePerTick));
            int tGeneratedEU = GTUtility.safeInt(this.mEUt * 2L * this.mEfficiency / 10000L);
            if (tGeneratedEU > 0) {
                int waterToConsume = getWaterConsumptionForSteam(tGeneratedEU);
                startRecipeProcessing();
                if (isSuperheated()) {
                    if (consumeWater(waterToConsume)) {
                        int superheatedSteam = getSuperheatedSteam(tGeneratedEU);
                        if (superheatedSteam > 0) {
                            addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", superheatedSteam));
                        }
                    } else {
                        GTLog.writeExplosionLog(this, "Boiler had no water");
                        explodeMultiblock();
                    }
                } else {
                    if (consumeWater(waterToConsume)) {
                        addOutput(Materials.Steam.getGas(tGeneratedEU));
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
        aNBT.setInteger("currentMaxEfficiency", currentMaxEfficiency);
        aNBT.setInteger("currentFuelCount", currentFuelCount);
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
        currentMaxEfficiency = aNBT.hasKey("currentMaxEfficiency") ? aNBT.getInteger("currentMaxEfficiency") : 10000;
        currentFuelCount = aNBT.getInteger("currentFuelCount");
        mEfficiencyIncrease = 0;
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        fireboxAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 20
            && fireboxAmount >= 5
            && !mMufflerHatches.isEmpty();
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

    private double getCurrentSteamOutputPerTick() {
        if (mEUt <= 0 || mMaxProgresstime <= 0) return 0D;
        double steamEquivalent = mEUt * 2D * mEfficiency / 10000D;
        return isSuperheated() ? steamEquivalent / superToNormalSteam : steamEquivalent;
    }

    private double getCurrentWaterConsumptionPerTick() {
        if (mEUt <= 0 || mMaxProgresstime <= 0) return 0D;
        double steamEquivalent = mEUt * 2D * mEfficiency / 10000D;
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
        if (currentFuelCount > 1) {
            info.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.large_boiler.scanner.fuel_combination",
                    currentFuelCount,
                    getMaxFuelInputs(),
                    formatNumber(currentMaxEfficiency / 100D)));
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
        double energyPerTick = tag.getDouble("largeBoilerEnergyPerTick");
        if (energyPerTick > 0D) {
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.large_boiler.energy_output",
                    formatNumber(energyPerTick),
                    "EU"));
        }
        double waterPerTick = tag.getDouble("largeBoilerWaterPerTick");
        if (waterPerTick > 0D) {
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.large_boiler.water_consumption",
                    formatNumber(waterPerTick),
                    "L"));
        }
        if (tag.getInteger("largeBoilerFuelCount") > 1) {
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.large_boiler.fuel_combination",
                    tag.getInteger("largeBoilerFuelCount"),
                    tag.getInteger("largeBoilerMaxFuelCount"),
                    formatNumber(tag.getFloat("largeBoilerTargetEfficiency"))));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setDouble("largeBoilerSteamPerTick", getCurrentSteamOutputPerTick());
        tag.setDouble("largeBoilerEnergyPerTick", getCurrentEnergyOutputPerTick());
        tag.setDouble("largeBoilerWaterPerTick", getCurrentWaterConsumptionPerTick());
        tag.setInteger("largeBoilerFuelCount", currentFuelCount);
        tag.setInteger("largeBoilerMaxFuelCount", getMaxFuelInputs());
        tag.setFloat("largeBoilerTargetEfficiency", currentMaxEfficiency / 100.0F);
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

    private static final class FuelCandidate {

        private final ItemStack itemStack;
        private final FluidStack fluidStack;
        private final long effectiveEnergy;
        private final boolean isDiesel;
        private final boolean isSolid;

        private FuelCandidate(ItemStack itemStack, FluidStack fluidStack, long effectiveEnergy, boolean isDiesel,
            boolean isSolid) {
            this.itemStack = itemStack;
            this.fluidStack = fluidStack;
            this.effectiveEnergy = Math.max(1L, effectiveEnergy);
            this.isDiesel = isDiesel;
            this.isSolid = isSolid;
        }
    }
}
