package gregtech.common.hydrodam;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;

import java.util.List;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;

/// Pumps water from an input hatch into the dam behind it, using one EU per litre. Creates the dam's first water block
/// if there is none.
public class MTEHydroPump extends MTEHydroDamMachine<MTEHydroPump> {

    public static final long EU_PER_LITRE = 1;

    /// Built on first use, after the casing blocks exist.
    private static IStructureDefinition<MTEHydroPump> structureDefinition;

    private static final CheckRecipeResult NO_WATER_INPUT = SimpleCheckRecipeResult
        .ofFailure("hydrodam_no_water_input");
    private static final CheckRecipeResult BLOCKED = SimpleCheckRecipeResult.ofFailure("hydrodam_blocked");
    private static final CheckRecipeResult DAM_FULL = SimpleCheckRecipeResult.ofFailure("hydrodam_full");

    public MTEHydroPump(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    public MTEHydroPump(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MTEHydroPump(mName);
    }

    @Override
    public IStructureDefinition<MTEHydroPump> getStructureDefinition() {
        if (structureDefinition == null) {
            structureDefinition = createStructureDefinition(MTEHydroPump.class, Energy, InputHatch, Maintenance);
        }

        return structureDefinition;
    }

    @Override
    protected void checkDamHatches(@NotNull List<StructureError> errors) {
        checkOneEnergyHatch(errors);
        checkOneInputHatch(errors);
        checkOneMaintenanceHatch(errors);
    }

    @Override
    protected double getMaxDamEfficiency() {
        return 1;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        long water = getStoredWater();

        if (water <= 0) return NO_WATER_INPUT;

        double efficiency = getDamEfficiency();

        if (efficiency <= 0) return NO_ENERGY;

        double energy = getConsumableEnergy(getEnergyBudget());
        long litres = Math.min(water, (long) Math.floor(energy * efficiency / EU_PER_LITRE));

        if (litres <= 0) return NO_ENERGY;

        double stored = pumpIntoDam(litres);

        if (stored < 0) return BLOCKED;

        // The dam may store a fraction of a litre when it's nearly full. Round up so that water is never created.
        long consumed = (long) Math.ceil(stored);

        if (consumed == 0) return DAM_FULL;

        depleteInput(new FluidStack(FluidRegistry.WATER, (int) consumed));
        startConsumingCycle(consumed, consumed * EU_PER_LITRE / efficiency);

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /// The EU the energy hatch can supply this tick: its stored EU, capped by its voltage times its amperage.
    private long getEnergyBudget() {
        MTEHatchEnergy hatch = mEnergyHatches.get(0);

        return Math.min(
            hatch.getBaseMetaTileEntity()
                .getStoredEU(),
            hatch.maxEUInput() * hatch.maxAmperesIn());
    }

    private long getStoredWater() {
        long water = 0;

        for (FluidStack fluid : getStoredFluids()) {
            if (fluid.getFluid() == FluidRegistry.WATER) water += fluid.amount;
        }

        return Math.min(water, Integer.MAX_VALUE);
    }

    /// Adds water to the dam behind this machine. If there is no water there, it falls into a dam just below the
    /// outlet,
    /// or starts a new one at the outlet.
    ///
    /// @return the litres that were stored, or -1 if there's no dam and the block behind can't hold water
    private double pumpIntoDam(long litres) {
        DamVolumes volumes = DamVolumes.get(getBaseMetaTileEntity().getWorld());
        WaterVolume volume = getVolumeBehind(volumes);

        if (volume != null) return volumes.addLitres(volume, litres);

        ChunkCoordinates back = getWaterPosition();
        World world = getBaseMetaTileEntity().getWorld();

        if (!world.blockExists(back.posX, back.posY, back.posZ)) return -1;
        if (!VolumeFill.canHoldWater(world, back.posX, back.posY, back.posZ)) return -1;

        // Water poured from the outlet falls into any dam just below it.
        WaterVolume below = volumes.getVolumeBelow(back.posX, back.posY, back.posZ);

        if (below != null) return volumes.addLitres(below, litres);

        // A new volume stores everything that fits; the rest is voided, so only create it with what one cell holds.
        long initial = Math.min(litres, (long) WaterVolume.LITRES_PER_CELL);

        WaterVolume created = volumes.createVolume(back.posX, back.posY, back.posZ, initial);

        if (created == null) return -1;

        return initial;
    }

    @Override
    public String getThroughputLangKey() {
        return "GT5U.waila.hydrodam.pumping";
    }

    @Override
    protected IIconContainer getFrontOverlay(boolean active) {
        return active ? Textures.BlockIcons.OVERLAY_FRONT_WATER_PUMP_ACTIVE
            : Textures.BlockIcons.OVERLAY_FRONT_WATER_PUMP;
    }

    @Override
    protected IIconContainer getFrontGlowOverlay(boolean active) {
        return active ? Textures.BlockIcons.OVERLAY_FRONT_WATER_PUMP_ACTIVE_GLOW
            : Textures.BlockIcons.OVERLAY_FRONT_WATER_PUMP_GLOW;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        String anyCasing = StatCollector.translateToLocal("gt.mbtt.structure.any_casing");

        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.hydro_pump"))
            .addMarkdown(
                new ResourceLocation("gregtech", "hydro-pump"),
                ImmutableMap.of("eu_per_litre", EU_PER_LITRE, "loss_per_problem", LOSS_PER_MAINTENANCE_PROBLEM * 100));

        addStructureInfo(tt).addEnergyHatch("1", anyCasing, 1)
            .addInputHatch("1", anyCasing, 1)
            .toolTipFinisher();

        return tt;
    }
}
