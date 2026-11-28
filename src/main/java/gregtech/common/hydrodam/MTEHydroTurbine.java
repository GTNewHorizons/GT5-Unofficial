package gregtech.common.hydrodam;

import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;

import java.util.List;
import java.util.Set;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.MultiblockTooltipBuilder;

/// Drains water from the dam behind it, producing {@link MTEHydroPump#EU_PER_LITRE} EU per litre at up to
/// {@link #MAX_EFFICIENCY} efficiency. The water goes to an optional output hatch, or is voided. Never creates dam
/// water.
public class MTEHydroTurbine extends MTEHydroDamMachine<MTEHydroTurbine> {

    /// Players use dams to store energy, so the round trip through a pump and turbine should lose very little.
    public static final double MAX_EFFICIENCY = 0.99;

    private static final CheckRecipeResult DYNAMO_FULL = SimpleCheckRecipeResult.ofFailure("hydrodam_dynamo_full");

    /// Built on first use, after the casing blocks exist.
    private static IStructureDefinition<MTEHydroTurbine> structureDefinition;

    public MTEHydroTurbine(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    public MTEHydroTurbine(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MTEHydroTurbine(mName);
    }

    @Override
    public IStructureDefinition<MTEHydroTurbine> getStructureDefinition() {
        if (structureDefinition == null) {
            structureDefinition = createStructureDefinition(MTEHydroTurbine.class, Dynamo, OutputHatch, Maintenance);
        }

        return structureDefinition;
    }

    @Override
    protected void checkDamHatches(@NotNull List<StructureError> errors) {
        checkOneDynamoHatch(errors);
        if (mOutputHatches.size() > 1) {
            errors.add(
                StructureErrors.hatchCount(ErrorType.TOO_MANY, HatchElement.OutputHatch, mOutputHatches.size(), 1));
        }
        checkOneMaintenanceHatch(errors);
    }

    @Override
    protected double getMaxDamEfficiency() {
        return MAX_EFFICIENCY;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        DamVolumes volumes = DamVolumes.get(getBaseMetaTileEntity().getWorld());
        WaterVolume volume = getVolumeBehind(volumes);

        // The level never drops below the inlet, so that a fast turbine visibly stops at it instead of emptying the
        // dam.
        long available = volume == null ? 0
            : (long) (volume.getLitres() - volume.getCapacityBelow(getWaterPosition().posY));

        if (available < 1) return NO_DAM_WATER;

        double efficiency = getDamEfficiency();

        if (efficiency <= 0) return NO_ENERGY;

        long budget = getLitreBudget(efficiency);

        if (budget <= 0) return DYNAMO_FULL;

        long litres = Math.min(available, budget);

        // The output hatch is optional. Without one, or without void protection, water that doesn't fit is voided.
        MTEHatchOutput output = mOutputHatches.isEmpty() ? null : mOutputHatches.get(0);

        if (output != null && protectsExcessFluid()) {
            litres = Math.min(litres, output.fill(water(litres), false));

            if (litres <= 0) return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        }

        long removed = (long) volumes.removeLitres(volume, litres);

        if (output != null) output.fill(water(removed), true);
        startGeneratingCycle(removed, removed * MTEHydroPump.EU_PER_LITRE * efficiency);

        return CheckRecipeResultRegistry.GENERATING;
    }

    /// The litres the dynamo can take the energy of this tick: limited by its voltage times its amperage, and by its
    /// free buffer so that no energy is voided.
    private long getLitreBudget(double efficiency) {
        MTEHatchDynamo dynamo = mDynamoHatches.get(0);

        long throughput = dynamo.maxEUOutput() * dynamo.maxAmperesOut();
        long freeSpace = dynamo.getBaseMetaTileEntity()
            .getEUCapacity()
            - dynamo.getBaseMetaTileEntity()
                .getStoredEU();

        long eu = Math.max(0, Math.min(throughput, freeSpace));

        return (long) Math.floor(eu / efficiency / MTEHydroPump.EU_PER_LITRE);
    }

    private static FluidStack water(long litres) {
        return new FluidStack(FluidRegistry.WATER, (int) Math.min(litres, Integer.MAX_VALUE));
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public Set<VoidingMode> getAllowedVoidingModes() {
        return VoidingMode.FLUID_ONLY_MODES;
    }

    @Override
    public String getThroughputLangKey() {
        return "GT5U.waila.hydrodam.draining";
    }

    @Override
    protected IIconContainer getFrontOverlay(boolean active) {
        return active ? Textures.BlockIcons.STEAM_TURBINE_SIDE_ACTIVE : Textures.BlockIcons.STEAM_TURBINE_SIDE;
    }

    @Override
    protected IIconContainer getFrontGlowOverlay(boolean active) {
        return active ? Textures.BlockIcons.STEAM_TURBINE_SIDE_ACTIVE_GLOW
            : Textures.BlockIcons.STEAM_TURBINE_SIDE_GLOW;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        String anyCasing = StatCollector.translateToLocal("gt.mbtt.structure.any_casing");

        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.hydro_turbine"))
            .addMarkdown(
                new ResourceLocation("gregtech", "hydro-turbine"),
                ImmutableMap.of(
                    "eu_per_litre",
                    MTEHydroPump.EU_PER_LITRE,
                    "max_efficiency",
                    Math.round(MAX_EFFICIENCY * 100),
                    "loss_per_problem",
                    LOSS_PER_MAINTENANCE_PROBLEM * 100));

        addStructureInfo(tt).addDynamoHatch("1", anyCasing, 1)
            .addOutputHatch("0-1", anyCasing, 1)
            .toolTipFinisher();

        return tt;
    }
}
