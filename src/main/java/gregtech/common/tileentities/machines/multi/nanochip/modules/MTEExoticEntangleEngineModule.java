package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTEExoticEntangleEngineModuleGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchNanochipRedstone;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import gregtech.common.tileentities.machines.multi.nanochip.util.ConditioningTable;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;

public class MTEExoticEntangleEngineModule extends MTENanochipAssemblyModuleBase<MTEExoticEntangleEngineModule> {

    protected static final int ENTANGLE_ENGINE_OFFSET_X = 3;
    protected static final int ENTANGLE_ENGINE_OFFSET_Y = 5;
    protected static final int ENTANGLE_ENGINE_OFFSET_Z = 0;
    // Slices run front to back, rows top to bottom; the bottom row stays empty so the shared base can occupy it.
    protected static final String[][] ENTANGLE_ENGINE_STRING = new String[][] {
        { "AAAAAAA", "A     A", "A     A", "A     A", "A     A", "       " },
        { "ABBBBBA", "B     B", "D     D", "D     D", "C     C", "       " },
        { "ABDDDBA", "B  A  B", "D     D", "D  A  D", "C     C", "       " },
        { "ABDDDBA", "B  D  B", "D ADA D", "D  D  D", "C ADA C", "       " },
        { "ABDDDBA", "B  A  B", "D     D", "D  A  D", "C     C", "       " },
        { "ABBBBBA", "B     B", "D     D", "D     D", "C     C", "       " },
        { "AAAAAAA", "A     A", "A     A", "A     A", "A     A", "       " } };
    public static final IStructureDefinition<MTEExoticEntangleEngineModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEExoticEntangleEngineModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ENTANGLE_ENGINE_STRING)
        // Exo-Halkonite Frame Box, also forms the entangled strands in the interior
        .addElement('A', ofFrame(Materials.ExoHalkonite))
        // Nanochip Mesh Interface Casing
        .addElement('B', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('C', Casings.NanochipReinforcementCasing.asElement())
        // Nanochip Complex Glass
        .addElement('D', Casings.NanochipComplexGlass.asElement())
        .build();

    public MTEExoticEntangleEngineModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEExoticEntangleEngineModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.ExoticEntangleEngine;
    }

    @Override
    public IStructureDefinition<MTEExoticEntangleEngineModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        super.checkMachine(aBaseMetaTileEntity, aStack, errors);
        if (!errors.isEmpty()) return;
        checkHasInputHatch(errors);
    }

    @Override
    public int structureOffsetX() {
        return ENTANGLE_ENGINE_OFFSET_X;
    }

    @Override
    public int structureOffsetY() {
        return ENTANGLE_ENGINE_OFFSET_Y;
    }

    @Override
    public int structureOffsetZ() {
        return ENTANGLE_ENGINE_OFFSET_Z;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return createNanochipModuleTextures(
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE,
            OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_GLOW,
            OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_ACTIVE,
            OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_ACTIVE_GLOW);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.exotic_entangle_engine.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.1"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.2"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.3"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.4"))
            .addSeparator()
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.5"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.6"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.7"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.8"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.9"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.10"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.11"))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.flavor.1")))
            .beginStructureBlock(7, 8, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.interface.structure.module_controller"))
            // Nanochip Mesh Interface Casing
            .addCasing("26", translateToLocal("gt.blockcasings12.1.name"), false)
            // Nanochip Reinforcement Casing
            .addCasing("10", translateToLocal("gt.blockcasings12.2.name"), false)
            // Exo-Halkonite Frame Box
            .addCasing("48", "Exo-Halkonite Frame Box", false)
            // Nanochip Complex Glass
            .addCasing("33", translateToLocal("gt.blockglass1.8.name"), false)
            .addInputHatch("1+", translateToLocal("GT5U.tooltip.nac.interface.structure.module_hatches"), 3)
            .addMiscHatch(
                "0+",
                TOOLTIP_VCI_LONG,
                translateToLocal("GT5U.tooltip.nac.interface.structure.module_hatches"),
                3)
            .addMiscHatch(
                "0+",
                TOOLTIP_VCO_LONG,
                translateToLocal("GT5U.tooltip.nac.interface.structure.module_hatches"),
                3)
            .addStructureInfo("")
            .addStructureFooter(translateToLocal("GT5U.tooltip.nac.interface.structure.module_cost"))
            .addStructureFooter(translateToLocal("GT5U.tooltip.nac.interface.structure.module_power"))
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEExoticEntangleEngineModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipExoticEntangleEngine;
    }

    // Stability = A*exp(S + module entropy - (H + module enthalpy)/T)

    private static final int MINUTE_TICKS = 1200;
    private static final float TEMP_RISE_PER_MINUTE = 0.1f;
    private static final float TEMP_FALL_PER_SECOND = 0.01f;
    private static final float STABILITY_LERP_PER_TICK = 0.005f;
    private static final float MUTATION_THRESHOLD = 80f;
    private static final float CONVEYOR_MUTATION_THRESHOLD = 50f;
    private static final float OUTPUT_REDIRECT_THRESHOLD = 20f;
    private static final float COLD_ENTHALPY = 0f;
    private static final float HOT_ENTHALPY = 0f;
    private static final float COLD_MULTIPLIER = 0.8f;
    private static final float HOT_MULTIPLIER = 1.25f;

    private float stability = 100f;
    private float temperatureRatio = 0f;
    private float conditioningEnthalpy = 0f;
    private float conditioningEntropy = 0f;
    private float heatRateMultiplier = 1f;

    public float getStability() {
        return stability;
    }

    public float getTemperatureRatio() {
        return temperatureRatio;
    }

    public float getConditioningEnthalpy() {
        return conditioningEnthalpy;
    }

    public float getConditioningEntropy() {
        return conditioningEntropy;
    }

    public boolean isTemperatureFrozen() {
        return baseMulti != null && baseMulti.exoticT3Active;
    }

    public void setTemperatureRatio(double ratio) {
        if (!isTemperatureFrozen()) return;
        temperatureRatio = GTUtility.clamp((float) ratio, 0f, 1f);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setFloat("stability", stability);
        aNBT.setFloat("temperatureRatio", temperatureRatio);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        stability = aNBT.hasKey("stability") ? aNBT.getFloat("stability") : 100f;
        temperatureRatio = aNBT.hasKey("temperatureRatio") ? aNBT.getFloat("temperatureRatio") : 0f;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide() || !isConnected()) return;
        if (aTick % 20 == 0) {
            updateConditioning();
            updateRedstoneOutput();
            if (!isTemperatureFrozen() && mMaxProgresstime <= 0) {
                temperatureRatio = Math.max(0f, temperatureRatio - TEMP_FALL_PER_SECOND);
            }
        }
        if (mMaxProgresstime > 0 && aTick % MINUTE_TICKS == 0) {
            consumeConditioning();
            heatUp();
        }
        updateStability();
        if (mMaxProgresstime > 0) trySpatialMutation();
    }

    private void updateConditioning() {
        conditioningEnthalpy = 0f;
        conditioningEntropy = 0f;
        heatRateMultiplier = 1f;
        for (FluidStack fluid : conditioningFluids()) {
            float[] hs = ConditioningTable.getHS(fluid);
            conditioningEnthalpy += hs[0];
            conditioningEntropy += hs[1];
            if (hs[0] < COLD_ENTHALPY) heatRateMultiplier *= COLD_MULTIPLIER;
            else if (hs[0] >= HOT_ENTHALPY) heatRateMultiplier *= HOT_MULTIPLIER;
        }
    }

    private List<FluidStack> conditioningFluids() {
        List<FluidStack> found = new ArrayList<>();
        Set<Fluid> seen = new HashSet<>();
        for (FluidStack fluid : getStoredFluids()) {
            if (fluid == null || !seen.add(fluid.getFluid())) continue;
            if (ConditioningTable.getHS(fluid) != null) found.add(fluid);
        }
        return found;
    }

    private void heatUp() {
        if (isTemperatureFrozen()) return;
        temperatureRatio = Math.min(1f, temperatureRatio + TEMP_RISE_PER_MINUTE * heatRateMultiplier);
    }

    private void updateStability() {
        float target = ConditioningTable
            .getStabilityTarget(conditioningEnthalpy, conditioningEntropy, temperatureRatio);
        stability += (target - stability) * STABILITY_LERP_PER_TICK;
    }

    private void consumeConditioning() {
        List<FluidStack> fluids = conditioningFluids();
        float discount = ConditioningTable.getConsumptionMultiplier(fluids);
        for (FluidStack fluid : fluids) {
            int amount = Math.round(ConditioningTable.getConsumption(fluid) * discount);
            depleteInput(new FluidStack(fluid.getFluid(), amount));
        }
    }

    // Spatial instability
    private void trySpatialMutation() {
        if (stability >= CONVEYOR_MUTATION_THRESHOLD) return;
        float chance = (CONVEYOR_MUTATION_THRESHOLD - stability) / 100f * 0.01f;
        if (random.nextFloat() >= chance) return;

        List<MTEHatchVacuumConveyorInput> sources = new ArrayList<>();
        for (var hatchList : vacuumConveyorInputs.allHatches()) {
            for (var hatch : hatchList) {
                if (hatch.contents != null && !hatch.contents.isEmpty()) sources.add(hatch);
            }
        }
        if (sources.isEmpty()) return;

        List<MTEHatchVacuumConveyorOutput> destinations = new ArrayList<>();
        for (var hatchList : vacuumConveyorOutputs.allHatches()) {
            for (var hatch : hatchList) {
                destinations.add(hatch);
            }
        }
        if (destinations.isEmpty()) return;

        MTEHatchVacuumConveyorInput source = sources.get(random.nextInt(sources.size()));
        CircuitComponentPacket displaced = source.contents;
        source.contents = null;
        MTEHatchVacuumConveyorOutput destination = destinations.get(random.nextInt(destinations.size()));
        destination.unifyPacket(new CircuitComponentPacket(displaced.getComponents()));
    }

    // Temporal instability
    @Override
    public CheckRecipeResult checkProcessing() {
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful() && mOutputItems != null && stability < MUTATION_THRESHOLD) {
            mOutputItems = mutateOutputs(mOutputItems);
        }
        return result;
    }

    private ItemStack[] mutateOutputs(ItemStack[] outputItems) {
        float chance = (MUTATION_THRESHOLD - stability) / 200f;
        float roll = random.nextFloat();
        if (roll < chance) return replaceOutputs(outputItems, false);
        if (roll < chance * 2f) return replaceOutputs(outputItems, true);
        return outputItems;
    }

    // Spatial instability level 2
    @Override
    public boolean addItemOutputs(ItemStack[] outputItems) {
        if (stability < OUTPUT_REDIRECT_THRESHOLD
            && random.nextFloat() < (CONVEYOR_MUTATION_THRESHOLD - stability) / 200f
            && addOutputsToInputConveyors(outputItems)) {
            return true;
        }
        return super.addItemOutputs(outputItems);
    }

    private boolean addOutputsToInputConveyors(ItemStack[] outputItems) {
        List<MTEHatchVacuumConveyorInput> destinations = new ArrayList<>();
        for (var hatchList : vacuumConveyorInputs.allHatches()) {
            destinations.addAll(hatchList);
        }
        if (destinations.isEmpty()) return false;

        MTEHatchVacuumConveyorInput destination = destinations.get(random.nextInt(destinations.size()));
        for (ItemStack stack : outputItems) {
            if (GTUtility.isStackInvalid(stack)) continue;
            destination.unifyPacket(
                new CircuitComponentPacket(
                    CircuitComponent.getFromFakeStackUnsafe(stack),
                    stack.stackSize,
                    GTUtility.getStackCustomName(stack)));
        }
        return true;
    }

    private static ItemStack[] replaceOutputs(ItemStack[] outputs, boolean toProduct) {
        ItemStack[] replaced = new ItemStack[outputs.length];
        for (int i = 0; i < outputs.length; i++) {
            ItemStack stack = outputs[i];
            CircuitComponent replacement = getMutationResult(CircuitComponent.tryGetFromFakeStack(stack), toProduct);
            replaced[i] = replacement == null ? stack : replacement.getFakeStack(stack.stackSize);
        }
        return replaced;
    }

    private static CircuitComponent getMutationResult(CircuitComponent component, boolean toProduct) {
        if (toProduct) {
            return component == CircuitComponent.SingularityTolerantCacheMatrix
                || component == CircuitComponent.QuantumEntangledComputationUnit ? CircuitComponent.ExoticProcessor
                    : null;
        }
        if (component == CircuitComponent.SingularityTolerantCacheMatrix) return CircuitComponent.MajoranaZeroMode;
        if (component == CircuitComponent.QuantumEntangledComputationUnit) return CircuitComponent.CasimirContainer;
        return null;
    }

    private void updateRedstoneOutput() {
        for (MTEHatchNanochipRedstone hatch : redstoneHatches) {
            hatch.setStabilitySignal(computeRedstoneSignal(hatch.getChannel()));
        }
    }

    private byte computeRedstoneSignal(int channel) {
        float value = channel == 3 ? temperatureRatio * 100f : stability;
        return (byte) GTUtility.clamp((int) (value * 15f / 100f), 0, 15);
    }

    @Override
    public String[] getInfoData() {
        String[] base = super.getInfoData();
        // Temporal instability replaces the output of every completed recipe, so warn about it
        boolean unstable = stability < MUTATION_THRESHOLD;
        String[] info = Arrays.copyOf(base, base.length + (unstable ? 3 : 2));
        info[base.length] = translateToLocalFormatted(
            "GT5U.tooltip.nac.module.scanner.stability",
            String.format("%.1f%%", stability));
        info[base.length + 1] = translateToLocalFormatted(
            "GT5U.tooltip.nac.module.scanner.temperature",
            String.format("%.1f%%", temperatureRatio * 100f));
        if (unstable) {
            info[base.length + 2] = translateToLocalFormatted(
                "GT5U.tooltip.nac.module.scanner.temporal_instability",
                String.format("%.1f%%", MUTATION_THRESHOLD - stability));
        }
        return info;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEExoticEntangleEngineModuleGui(this);
    }
}
