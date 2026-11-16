package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainItemPipeCasings;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.MTECZPullerGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.loaders.postload.recipes.CZPullerRecipes;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTECZPuller extends MTEExtendedPowerMultiBlockBase<MTECZPuller>
    implements ISurvivalConstructable, ICasingTextureProvider, INEIPreviewModifier {

    private static final String TAG_BUFFER_MATERIAL = "czBufferMaterial";
    private static final String TAG_BUFFER_AMOUNT = "czBufferAmount";
    private static final String TAG_CURRENT_TEMPERATURE = "czCurrentTemperature";
    private static final String TAG_TEMPERATURE_HALF_UNITS = "czTemperatureHalfUnits";
    private static final String TAG_TEMPERATURE_HEATING_REMAINDER = "czTemperatureHeatingRemainder";
    private static final String TAG_GROWTH_MODE = "czGrowthMode";
    private static final String TAG_TARGET_TEMPERATURE = "czTargetTemperature";
    private static final int TIER_1_TANK_CAPACITY = 1_024;
    private static final int TIER_2_TANK_CAPACITY = 65_536;
    private static final int HEAT_EU_PER_TICK = 120;

    private static final int OFFSET_X_1 = 2;
    private static final int OFFSET_Y_1 = 8;
    private static final int OFFSET_Z_1 = 0;
    private static final int OFFSET_X_2 = 5;
    private static final int OFFSET_Y_2 = 10;
    private static final int OFFSET_Z_2 = 2;
    private static final int CASING_T1_ADDITIONAL = 33;
    private static final int CASING_T2_ADDITIONAL = 103;

    private MTEHatchInputBus seedInputBus;
    private int capacityLimit = TIER_1_TANK_CAPACITY;
    private HeatingCoilLevel coilLevel = null;
    private static final String TIER_1 = "tier1";
    private static final String TIER_2 = "tier2";
    private byte structureTier = 1;

    private CZPullerRecipes.GrowthRecipe bufferMaterial;
    private int bufferAmount;
    private int currentTemperature;
    private int temperatureHalfUnits;
    private long temperatureHeatingRemainder;
    private boolean preheatedMode;
    private int targetTemperature;
    private int pipeTier = -1;
    private int casingAmountTier1;
    private int casingAmountTier2;
    private int seedBusCount;

    private static final IStructureDefinition<MTECZPuller> STRUCTURE_DEFINITION = StructureDefinition
        .<MTECZPuller>builder()
        // spotless:off
        .addShape(
            TIER_1,
            transpose(
                new String[][] {
                    { "     ", "     ", "  G  ", "     ", "     " },
                    { "     ", "     ", "  B  ", "     ", "     " },
                    { "     ", "  B  ", " BCB ", "  B  ", "     " },
                    { "     ", "  B  ", " BAB ", "  B  ", "     " },
                    { "  B  ", " BBB ", "BBABB", " BBB ", "  B  " },
                    { " EBE ", "EDDDE", "BDADB", "EDDDE", " EBE " },
                    { " EBE ", "EDDDE", "BDADB", "EDDDE", " EBE " },
                    { " EBE ", "EDDDE", "BDADB", "EDDDE", " EBE " },
                    { " F~F ", "FFFFF", "FFFFF", "FFFFF", " FFF " }}))
        .addShape(
            TIER_2,
            transpose(
                new String[][] {
                    { "           ", "           ", "           ", "           ", "           ", "     H     ", "           ", "           ", "           ", "           ", "           " },
                    { "           ", "           ", "           ", "           ", "           ", "     H     ", "           ", "           ", "           ", "           ", "           " },
                    { "           ", "           ", "           ", "           ", "    I I    ", "     H     ", "    I I    ", "           ", "           ", "           ", "           " },
                    { "           ", "           ", "  JIILIIJ  ", "  II   II  ", "  I I I I  ", "  L  H  L  ", "  I I I I  ", "  II   II  ", "  JIILIIJ  ", "           ", "           " },
                    { "           ", " JI     IJ ", " I  JJJ  I ", "   IJJJI   ", "  JJAIAJJ  ", "  JJIHIJJ  ", "  JJAIAJJ  ", "   IJJJI   ", " I  JJJ  I ", " JI     IJ ", "           " },
                    { "JI       IJ", "I         I", "    DDD    ", "   DJJJD   ", "  DJAJAJD  ", "  DJJHJJD  ", "  DJAJAJD  ", "   DJJJD   ", "    DDD    ", "I         I", "JI       IJ" },
                    { "I         I", "           ", "    DDD    ", "   DJJJD   ", "  DJA AJD  ", "  DJ   JD  ", "  DJA AJD  ", "   DJJJD   ", "    DDD    ", "           ", "I         I" },
                    { "I         I", "           ", "    HHH    ", "   HJJJH   ", "  HJA AJH  ", "  HJ   JH  ", "  HJA AJH  ", "   HJJJH   ", "    HHH    ", "           ", "I         I" },
                    { "I         I", "           ", "    DDD    ", "   DJJJD   ", "  DJA AJD  ", "  DJ   JD  ", "  DJA AJD  ", "   DJJJD   ", "    DDD    ", "           ", "I         I" },
                    { "I         I", "           ", "    DDD    ", "   DJJJD   ", "  DJA AJD  ", "  DJ   JD  ", "  DJA AJD  ", "   DJJJD   ", "    DDD    ", "           ", "I         I" },
                    { "II       II", "I         I", "    K~K    ", "   IKKKI   ", "  KKKKKKK  ", "  KKKKKKK  ", "  KKKKKKK  ", "   IKKKI   ", "    KKK    ", "I         I", "II       II" }
                }))
        // spotless:on
        .addElement('A', chainItemPipeCasings(-1, MTECZPuller::setItemPipeTier, MTECZPuller::getItemPipeTier))
        .addElement('B', Casings.SolidSteelMachineCasing.asElement())
        .addElement('C', Casings.SteelGearBoxCasing.asElement())
        .addElement(
            'D',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTECZPuller::setCoilLevel, MTECZPuller::getCoilLevel))))
        .addElement('E', ofFrame(Materials.Steel))
        .addElement(
            'F',
            buildHatchAdder(MTECZPuller.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .hint(1)
                .casingIndex(Casings.SolidSteelMachineCasing.textureId)
                .buildAndChain(onElementPass(x -> ++x.casingAmountTier1, Casings.SolidSteelMachineCasing.asElement())))
        .addElement(
            'G',
            buildHatchAdder(MTECZPuller.class).atLeast(InputBus)
                .adder(MTECZPuller::addSeedBusToMachineList)
                .casingIndex(Casings.SolidSteelMachineCasing.textureId)
                .hint(2)
                .buildAndChain(Casings.SolidSteelMachineCasing.asElement()))
        .addElement('H', ofFrame(Materials.TengamAttuned))
        .addElement('I', ofFrame(Materials.SuperconductorZPMBase))
        .addElement('J', Casings.HeatResistantTriniumPlatedCasing.asElement())
        .addElement(
            'K',
            buildHatchAdder(MTECZPuller.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .hint(1)
                .casingIndex(Casings.HeatResistantTriniumPlatedCasing.textureId)
                .buildAndChain(
                    onElementPass(x -> ++x.casingAmountTier2, Casings.HeatResistantTriniumPlatedCasing.asElement())))
        .addElement(
            'L',
            buildHatchAdder(MTECZPuller.class).atLeast(InputBus)
                .adder(MTECZPuller::addSeedBusToMachineList)
                .casingIndex(Casings.HeatResistantTriniumPlatedCasing.textureId)
                .hint(2)
                .buildAndChain(Casings.HeatResistantTriniumPlatedCasing.asElement()))
        .build();

    public MTECZPuller(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECZPuller(String aName) {
        super(aName);
    }

    public boolean addSeedBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!(aMetaTileEntity instanceof MTEHatchInputBus seedBus)) return false;
        seedBusCount++;
        int textureID = structureTier == 2 ? Casings.HeatResistantTriniumPlatedCasing.textureId
            : Casings.SolidSteelMachineCasing.textureId;
        seedBus.updateTexture(textureID);
        seedBus.mRecipeMap = null;
        if (seedInputBus == null) seedInputBus = seedBus;
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECZPuller(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("CZ Puller, CZP")
            .beginStructureBlock(11, 11, 11, false)
            .addController("Front bottom center")
            .addEnergyHatch("1+", "Any bottom Casing", 1)
            .addInputBus("1+", "Any bottom Casing", 1)
            .addInputHatch("1+", "Any bottom Casing", 1)
            .addOutputBus("1+", "Any bottom Casing", 1)
            .addMaintenanceHatch("1", "Any bottom Casing", 1)
            .addInputBus("1", "Dedicated seed-crystal bus position", 2)
            .addStructureInfo("")
            .addStructureInfo(StatCollector.translateToLocal("GT5U.MBTT.Tiers.One"))
            .addCasing("40+", Casings.StableTitaniumMachineCasing.getLocalizedName(), false)
            .addCasing("24", "Steel Frame Box", false)
            .addCasing("24", StatCollector.translateToLocal("GT5U.structure.heating_coil"), true)
            .addCasing("5", "Item Pipe Casing", true)
            .addStructureInfo("")
            .addStructureInfo(StatCollector.translateToLocal("GT5U.MBTT.Tiers.Two"))
            .addCasing("120+", Casings.HeatResistantTriniumPlatedCasing.getLocalizedName(), false)
            .addCasing("84", "Superconductor Base ZPM Frame Box", false)
            .addCasing("64", StatCollector.translateToLocal("GT5U.structure.heating_coil"), true)
            .addCasing("24", "Item Pipe Casing", true)
            .addCasing("22", "Attuned Tengam Frame Box", false)
            .addStructureInfo("")
            .addMasterChannel(StatCollector.translateToLocal("channels.gregtech.master.structuretier"))
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .addSubChannel(GTStructureChannels.ITEM_PIPE_CASING)
            .toolTipFinisher();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        int materialIndex = bufferMaterial == null ? -1 : CZPullerRecipes.ALL.indexOf(bufferMaterial);
        aNBT.setInteger(TAG_BUFFER_MATERIAL, materialIndex);
        aNBT.setInteger(TAG_BUFFER_AMOUNT, materialIndex < 0 ? 0 : Math.max(0, bufferAmount));
        aNBT.setInteger(TAG_CURRENT_TEMPERATURE, currentTemperature);
        aNBT.setInteger(TAG_TEMPERATURE_HALF_UNITS, temperatureHalfUnits);
        aNBT.setLong(TAG_TEMPERATURE_HEATING_REMAINDER, temperatureHeatingRemainder);
        aNBT.setBoolean(TAG_GROWTH_MODE, preheatedMode);
        aNBT.setInteger(TAG_TARGET_TEMPERATURE, targetTemperature);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        int materialOrdinal = aNBT.getInteger(TAG_BUFFER_MATERIAL);
        bufferMaterial = materialOrdinal >= 0 && materialOrdinal < CZPullerRecipes.ALL.size()
            ? CZPullerRecipes.ALL.get(materialOrdinal)
            : null;
        bufferAmount = Math.max(0, aNBT.getInteger(TAG_BUFFER_AMOUNT));
        if (bufferAmount == 0) bufferMaterial = null;
        currentTemperature = Math.max(0, aNBT.getInteger(TAG_CURRENT_TEMPERATURE));
        temperatureHalfUnits = Math.max(currentTemperature * 2, aNBT.getInteger(TAG_TEMPERATURE_HALF_UNITS));
        temperatureHeatingRemainder = Math.max(0, aNBT.getLong(TAG_TEMPERATURE_HEATING_REMAINDER));
        preheatedMode = aNBT.getBoolean(TAG_GROWTH_MODE);
        targetTemperature = Math.max(0, aNBT.getInteger(TAG_TARGET_TEMPERATURE));
    }

    @Override
    public IStructureDefinition<MTECZPuller> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.czPullerRecipes;
    }

    @Override
    @Nonnull
    public CheckRecipeResult checkProcessing() {
        if (structureTier < 1 || seedInputBus == null || bufferMaterial == null || coilLevel == null)
            return CheckRecipeResultRegistry.NO_RECIPE;
        SeedSelection selection = findSeedSelection();
        if (selection == null || selection.material != bufferMaterial
            || selection.material.requiredStructureTier > structureTier
            || isCoilBelowMinimum(selection.material)
            || currentTemperature < selection.material.targetTemperature) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        if (GTUtility.getTier(getMaxInputVoltage()) < selection.material.energyTier)
            return CheckRecipeResultRegistry.NO_RECIPE;

        int maxParallel = calculateParallelLimit(
            GTUtility.getTier(getMaxInputVoltage()),
            selection.material.energyTier);
        int parallel = Math.min(maxParallel, countSeeds(selection.seed));
        parallel = Math.min(parallel, bufferAmount / selection.material.materialAmount);
        parallel = Math.min(parallel, countAvailableGas(selection.material) / selection.material.gasAmountMb);
        if (parallel < 1) return CheckRecipeResultRegistry.NO_RECIPE;
        long totalEu = (long) selection.material.eut * parallel;
        if (totalEu > Integer.MAX_VALUE) return CheckRecipeResultRegistry.NO_RECIPE;
        ItemStack bouleOutput = selection.material.output(parallel);
        if (!canOutputAll(new ItemStack[] { bouleOutput })
            || !depleteInput(selection.material.growthFluid(selection.material.gasAmountMb * parallel), true)) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        if (!drainEnergyInput((int) totalEu)) return CheckRecipeResultRegistry.NO_RECIPE;

        if (!consumeSeeds(selection.seed, parallel)
            || !depleteInput(selection.material.growthFluid(selection.material.gasAmountMb * parallel))) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        bufferAmount -= selection.material.materialAmount * parallel;
        if (bufferAmount == 0) bufferMaterial = null;
        targetTemperature = selection.material.targetTemperature;
        mOutputItems = new ItemStack[] { bouleOutput };
        mOutputFluids = null;
        mEUt = -(int) totalEu;
        mMaxProgresstime = Math.max(1, calculateGrowthDuration(selection.material) - 1);
        mProgresstime = 0;
        mEfficiency = 10_000;
        mEfficiencyIncrease = 10_000;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private int calculateGrowthDuration(CZPullerRecipes.GrowthRecipe material) {
        int effectivePipeTier = Math.max(1, pipeTier);
        return Math.max(1, material.durationTicks * 2 / effectivePipeTier);
    }

    private int calculateParallelLimit(int energyTier, int recipeTier) {
        if (energyTier < recipeTier) return 0;
        return 1 << Math.min(30, energyTier - recipeTier);
    }

    private boolean isCoilBelowMinimum(CZPullerRecipes.GrowthRecipe material) {
        return coilLevel == null || coilLevel.ordinal() < material.minimumCoil.ordinal();
    }

    private int countAvailableGas(CZPullerRecipes.GrowthRecipe material) {
        return countAvailableFluid(material.growthFluid(1));
    }

    private int countAvailableFluid(FluidStack required) {
        long total = 0;
        for (FluidStack stored : getStoredFluids()) {
            if (stored != null && stored.amount > 0 && stored.isFluidEqual(required)) total += stored.amount;
        }
        return (int) Math.min(Integer.MAX_VALUE, total);
    }

    private int countAvailableDust(CZPullerRecipes.GrowthRecipe material) {
        int total = 0;
        for (MTEHatchInputBus bus : mInputBusses) {
            if (bus == null || bus.mInventory == null) continue;
            for (ItemStack stack : bus.mInventory) {
                if (isSameItem(stack, material.materialDust)) {
                    total = (int) Math.min(Integer.MAX_VALUE, (long) total + stack.stackSize);
                }
            }
        }
        return total;
    }

    private CZPullerRecipes.GrowthRecipe findInputDustMaterial() {
        for (CZPullerRecipes.GrowthRecipe material : CZPullerRecipes.ALL) {
            if (material.requiredStructureTier <= structureTier && countAvailableDust(material) > 0) return material;
        }
        return null;
    }

    private boolean transferDustToBuffer(CZPullerRecipes.GrowthRecipe material) {
        if (material == null || material.requiredStructureTier > structureTier) return false;
        if (bufferMaterial != null && bufferMaterial != material) return false;
        int remaining = Math.clamp(capacityLimit - bufferAmount, 0, countAvailableDust(material));
        int transferLimit = remaining;
        if (transferLimit <= 0) return false;
        for (MTEHatchInputBus bus : mInputBusses) {
            if (bus == null || bus.mInventory == null || remaining == 0) continue;
            for (int i = 0; i < bus.mInventory.length && remaining > 0; i++) {
                ItemStack stack = bus.mInventory[i];
                if (!isSameItem(stack, material.materialDust)) continue;
                int moved = Math.min(stack.stackSize, remaining);
                stack.stackSize -= moved;
                remaining -= moved;
                if (stack.stackSize <= 0) bus.mInventory[i] = null;
            }
            bus.updateSlots();
        }
        int accepted = transferLimit - remaining;
        if (accepted > 0) {
            bufferMaterial = material;
            bufferAmount += accepted;
        }
        return accepted > 0;
    }

    private CZPullerRecipes.GrowthRecipe chooseHeatingMaterial() {
        SeedSelection seed = findSeedSelection();
        if (bufferMaterial != null) {
            if (bufferMaterial.requiredStructureTier > structureTier) return null;
            if (seed != null) {
                if (seed.material != bufferMaterial
                    || bufferAmount + countAvailableDust(bufferMaterial) < bufferMaterial.materialAmount
                    || countAvailableGas(bufferMaterial) < bufferMaterial.gasAmountMb
                    || countSeeds(seed.seed) == 0
                    || GTUtility.getTier(getMaxInputVoltage()) < bufferMaterial.energyTier) return null;
                return bufferMaterial;
            }
            if (preheatedMode && bufferAmount > 0) return bufferMaterial;
            return capacityLimit > bufferAmount && countAvailableDust(bufferMaterial) > 0 ? bufferMaterial : null;
        }

        CZPullerRecipes.GrowthRecipe input = seed == null ? findInputDustMaterial() : seed.material;
        if (input == null || input.requiredStructureTier > structureTier) return null;
        if (seed != null && countAvailableDust(input) < input.materialAmount) return null;
        if (seed != null && countAvailableGas(input) < input.gasAmountMb) return null;
        if (seed != null && (countSeeds(seed.seed) == 0 || GTUtility.getTier(getMaxInputVoltage()) < input.energyTier))
            return null;
        return input;
    }

    private void updateCZProcess(IGregTechTileEntity base, long tick) {
        if (!base.isAllowedToWork()) {
            coolDown(tick);
            return;
        }
        if (mMaxProgresstime > 0) return;
        CZPullerRecipes.GrowthRecipe material = chooseHeatingMaterial();
        if (material == null || isCoilBelowMinimum(material)) {
            targetTemperature = 0;
            coolDown(tick);
            return;
        }
        targetTemperature = material.targetTemperature;
        long coilTierIndex = getCoilTierIndex();
        if (currentTemperature > targetTemperature) {
            if (tick % 20 == 0) {
                temperatureHalfUnits = Math.max(targetTemperature * 2, temperatureHalfUnits - 2);
                currentTemperature = temperatureHalfUnits / 2;
            }
            return;
        }
        if (currentTemperature >= targetTemperature) {
            if (transferDustToBuffer(material)) scheduleRecipeCheckImmediate();
            if (preheatedMode) {
                if (!maintainPreheatedTemperature(material, coilTierIndex, tick)) coolDown(tick);
                return;
            }
            coolDown(tick);
            return;
        }
        long heatEu = preheatedMode ? 30L * (1L << Math.min(30, (int) coilTierIndex)) : HEAT_EU_PER_TICK;
        FluidStack heatMaintainingGas = null;
        if (preheatedMode && tick % 20 == 0) {
            heatMaintainingGas = material.growthFluid((int) (10L * coilTierIndex));
            if (heatMaintainingGas == null || !depleteInput(heatMaintainingGas, true)) return;
        }
        if (heatEu > Integer.MAX_VALUE || !drainEnergyInput((int) heatEu)) return;
        if (heatMaintainingGas != null && !depleteInput(heatMaintainingGas)) return;
        boolean wasBelowTarget = currentTemperature < targetTemperature;
        if (wasBelowTarget) heatTemperature(coilTierIndex);
        boolean reachedTarget = wasBelowTarget && currentTemperature >= targetTemperature;
        boolean transferredDust = currentTemperature >= targetTemperature && transferDustToBuffer(material);
        if (reachedTarget || transferredDust) {
            scheduleRecipeCheckImmediate();
        }
    }

    private boolean maintainPreheatedTemperature(CZPullerRecipes.GrowthRecipe material, long coilTierIndex, long tick) {
        FluidStack maintainingFluid = null;
        if (tick % 20 == 0) {
            maintainingFluid = material.growthFluid((int) (10L * coilTierIndex));
            if (!depleteInput(maintainingFluid, true)) return false;
        }

        long maintenanceEu = 30L * (1L << Math.min(30, (int) coilTierIndex));
        if (maintenanceEu > Integer.MAX_VALUE || !drainEnergyInput((int) maintenanceEu)) return false;
        return maintainingFluid == null || depleteInput(maintainingFluid);
    }

    private void coolDown(long tick) {
        if (tick % 20 == 0) {
            temperatureHeatingRemainder = 0;
            temperatureHalfUnits = Math.max(0, temperatureHalfUnits - 2);
            currentTemperature = temperatureHalfUnits / 2;
        }
    }

    private long getCoilTierIndex() {
        return coilLevel.getTier() + 1L;
    }

    private void heatTemperature(long coilTierIndex) {
        double speedKelvinPerSecond = preheatedMode ? 75.0 + Math.pow(1.8, coilTierIndex) : 12.5 * coilTierIndex;
        temperatureHeatingRemainder += Math.round(speedKelvinPerSecond * 100.0);
        long addedHalfUnits = temperatureHeatingRemainder / 1_000;
        temperatureHeatingRemainder %= 1_000;
        temperatureHalfUnits = (int) Math.min((long) targetTemperature * 2, temperatureHalfUnits + addedHalfUnits);
        currentTemperature = temperatureHalfUnits / 2;
        if (temperatureHalfUnits >= targetTemperature * 2) temperatureHeatingRemainder = 0;
    }

    private static final class SeedSelection {

        private final ItemStack seed;
        private final CZPullerRecipes.GrowthRecipe material;

        private SeedSelection(ItemStack seed, CZPullerRecipes.GrowthRecipe material) {
            this.seed = seed;
            this.material = material;
        }
    }

    private SeedSelection findSeedSelection() {
        if (seedInputBus == null || seedInputBus.mInventory == null) return null;
        for (ItemStack stack : seedInputBus.mInventory) {
            if (stack == null || stack.stackSize <= 0) continue;
            for (CZPullerRecipes.GrowthRecipe material : CZPullerRecipes.ALL) {
                if (isSameItem(stack, material.seed)) return new SeedSelection(material.seed, material);
            }
        }
        return null;
    }

    private static boolean isSameItem(ItemStack left, ItemStack right) {
        return left != null && right != null
            && left.getItem() == right.getItem()
            && left.getItemDamage() == right.getItemDamage();
    }

    private int countSeeds(ItemStack seed) {
        int count = 0;
        if (seedInputBus == null || seedInputBus.mInventory == null) return 0;
        for (ItemStack stack : seedInputBus.mInventory) {
            if (isSameItem(stack, seed)) count = (int) Math.min(Integer.MAX_VALUE, (long) count + stack.stackSize);
        }
        return count;
    }

    private boolean consumeSeeds(ItemStack seed, int count) {
        int remaining = count;
        for (int i = 0; i < seedInputBus.mInventory.length && remaining > 0; i++) {
            ItemStack stack = seedInputBus.mInventory[i];
            if (!isSameItem(stack, seed)) continue;
            int used = Math.min(stack.stackSize, remaining);
            stack.stackSize -= used;
            remaining -= used;
            if (stack.stackSize <= 0) seedInputBus.mInventory[i] = null;
        }
        seedInputBus.updateSlots();
        return remaining == 0;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!getBaseMetaTileEntity().isAllowedToWork()) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return false;
        }
        return super.onRunningTick(aStack);
    }

    private void setItemPipeTier(int tier) {
        pipeTier = tier;
    }

    private int getItemPipeTier() {
        return pipeTier;
    }

    @Override
    public void checkMachine(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        int tier;
        structureTier = 1;
        if (checkPiece(TIER_1, OFFSET_X_1, OFFSET_Y_1, OFFSET_Z_1, null)) {
            tier = 1;
        } else {
            clearHatches();
            structureTier = 2;
            if (!checkPiece(TIER_2, OFFSET_X_2, OFFSET_Y_2, OFFSET_Z_2, errors)) {
                structureTier = 0;
                capacityLimit = 0;
                return;
            }
            tier = 2;
        }

        if (tier == 1) {
            checkCasingMin(errors, casingAmountTier1 + CASING_T1_ADDITIONAL, 40);
        } else {
            checkCasingMin(errors, casingAmountTier2 + CASING_T2_ADDITIONAL, 120);
        }
        if (pipeTier < 0) {
            errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        }
        if (seedBusCount != 1 || seedInputBus == null) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.cz_puller_seed_buses"));
        }
        checkHasAnyEnergy(errors);
        checkOneMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
        checkHasOutputBus(errors);
        HeatingCoilLevel minimumCoil = tier == 2 ? HeatingCoilLevel.UIV : HeatingCoilLevel.MV;
        if (coilLevel == null || coilLevel.ordinal() < minimumCoil.ordinal()) {
            errors.add(StructureErrorRegistry.COIL_LEVEL_NOT_ENOUGH);
        }
        if (errors.isEmpty()) {
            structureTier = (byte) tier;
            capacityLimit = tier == 2 ? TIER_2_TANK_CAPACITY : TIER_1_TANK_CAPACITY;
            if (seedInputBus != null) {
                int textureID = tier == 2 ? Casings.HeatResistantTriniumPlatedCasing.textureId
                    : Casings.SolidSteelMachineCasing.textureId;
                seedInputBus.updateTexture(textureID);
            }
        } else {
            structureTier = 0;
            capacityLimit = 0;
        }
    }

    @Override
    public void onValueUpdate(byte value) {
        structureTier = value;
    }

    @Override
    public byte getUpdateData() {
        return structureTier;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        structureTier = 0;
        capacityLimit = 0;
        seedInputBus = null;
        seedBusCount = 0;
        coilLevel = null;
        pipeTier = -1;
        casingAmountTier1 = 0;
        casingAmountTier2 = 0;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTECZPullerGui(this);
    }

    public boolean isPreheatedMode() {
        return preheatedMode;
    }

    public void setPreheatedMode(boolean preheatedMode) {
        this.preheatedMode = preheatedMode;
    }

    public int getStoredGasAmount() {
        FluidStack displayedGas = getDisplayedGasFluid();
        return displayedGas == null ? 0 : countAvailableFluid(displayedGas);
    }

    public String getStoredGasName() {
        FluidStack displayedGas = getDisplayedGasFluid();
        return displayedGas == null ? "Gas" : displayedGas.getLocalizedName();
    }

    private FluidStack getDisplayedGasFluid() {
        CZPullerRecipes.GrowthRecipe material = bufferMaterial;
        if (material == null) {
            SeedSelection seed = findSeedSelection();
            material = seed == null ? findInputDustMaterial() : seed.material;
        }
        return material == null ? null : material.growthFluid(1);
    }

    public int getStoredMaterialAmount() {
        return bufferAmount;
    }

    public String getBufferMaterialName() {
        return bufferMaterial == null ? "Empty" : bufferMaterial.displayName;
    }

    public int getBufferCapacity() {
        return capacityLimit;
    }

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel level) {
        coilLevel = level;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int requestedTier = stackSize == null ? 1 : stackSize.stackSize;
        if (requestedTier <= 1) buildPiece(TIER_1, stackSize, hintsOnly, 2, 8, 0);
        else buildPiece(TIER_2, stackSize, hintsOnly, 5, 10, 2);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int requestedTier = stackSize == null ? 1 : stackSize.stackSize;
        return requestedTier <= 1
            ? survivalBuildPiece(TIER_1, stackSize, OFFSET_X_1, OFFSET_Y_1, OFFSET_Z_1, elementBudget, env, false, true)
            : survivalBuildPiece(
                TIER_2,
                stackSize,
                OFFSET_X_2,
                OFFSET_Y_2,
                OFFSET_Z_2,
                elementBudget,
                env,
                false,
                true);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            sideDirection,
            facingDirection,
            active,
            OVERLAY_FRONT_ENGRAVER,
            OVERLAY_FRONT_ENGRAVER_GLOW,
            OVERLAY_FRONT_ENGRAVER_ACTIVE,
            OVERLAY_FRONT_ENGRAVER_ACTIVE_GLOW);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            updateCZProcess(aBaseMetaTileEntity, aTick);
            if (mMachine && aTick % 20 == 0
                && mMaxProgresstime <= 0
                && bufferMaterial != null
                && bufferAmount > 0
                && currentTemperature >= bufferMaterial.targetTemperature
                && findSeedSelection() != null) {
                scheduleRecipeCheckImmediate();
            }
        }
    }

    @Override
    public ITexture getCasingTexture() {
        return structureTier == 2 ? Casings.HeatResistantTriniumPlatedCasing.getCasingTexture()
            : Casings.SolidSteelMachineCasing.getCasingTexture();
    }

    @Override
    public void onPreviewConstruct(@NotNull ItemStack trigger) {
        setPreviewTier(trigger);
    }

    @Override
    public void onPreviewStructureComplete(@NotNull ItemStack trigger) {
        setPreviewTier(trigger);
    }

    private void setPreviewTier(@NotNull ItemStack trigger) {
        structureTier = (byte) (trigger.stackSize >= 2 ? 2 : 1);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("czBufferMaterial", getBufferMaterialName());
        tag.setInteger("czBufferAmount", bufferAmount);
        tag.setInteger("czBufferCapacity", capacityLimit);
        tag.setInteger("czTemperature", currentTemperature);
        tag.setInteger("czTargetTemperature", targetTemperature);
    }

    @Override
    public void getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(stack, tooltip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        tooltip.add("Growth material: " + tag.getString("czBufferMaterial"));
        tooltip.add(
            "Internal material: " + tag.getInteger("czBufferAmount")
                + " / "
                + tag.getInteger("czBufferCapacity")
                + " dust");
        tooltip.add(
            "Temperature: " + tag.getInteger("czTemperature") + " / " + tag.getInteger("czTargetTemperature") + " K");
    }
}
