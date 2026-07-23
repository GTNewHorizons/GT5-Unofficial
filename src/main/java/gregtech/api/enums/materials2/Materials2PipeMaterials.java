package gregtech.api.enums.materials2;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialEdit;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.TierEU;

/// The pipe-family stat tables: one declared row per material carrying wires/cables, fluid pipes, or item
/// pipes, applied as [Materials2PipeProperties] values through [MaterialLibAPI#editMaterial]. The values
/// duplicate the literals [gregtech.loaders.preload.LoaderMetaPipeEntities] registers its pipe MTEs with, and
/// the two must stay in agreement.
///
/// The wooden and High Pressure fluid pipes exist in three sizes whose capacities follow no base-value
/// formula, so they carry per-size capacity constants here instead of a [Materials2PipeProperties#BASE_PIPE_FLOW]
/// value.
///
/// Rows reference [Materials2Materials] and [Materials2Markers] fields, so [#init] must run after those are
/// assigned; [gregtech.api.enums.Materials2#init] orders it so.
public class Materials2PipeMaterials {

    /// Fluid capacities (small, medium, large) of the wooden fluid pipes.
    public static final int[] WOOD_FLUID_PIPE_CAPACITY = { 10, 30, 60 };
    /// Maximum fluid temperature of the wooden fluid pipes, which also are not gas-proof.
    public static final int WOOD_FLUID_PIPE_HEAT_RESISTANCE = 350;
    /// Fluid capacities (small, medium, large) of the High Pressure (Redstone) fluid pipes.
    public static final int[] HIGH_PRESSURE_FLUID_PIPE_CAPACITY = { 4800, 7200, 9600 };
    /// Maximum fluid temperature of the High Pressure (Redstone) fluid pipes.
    public static final int HIGH_PRESSURE_FLUID_PIPE_HEAT_RESISTANCE = 1500;

    private record WireCable(Material material, int cableLoss, int amperage, long voltage) {}

    private record WireOnly(Material material, int wireLoss, int amperage, long voltage, boolean shock) {}

    private record FluidPipe(Material material, int baseCapacity, int heatResistance) {}

    private record ItemPipe(Material material, int hugeSlots, boolean smallPipes) {}

    public static void init() {
        applyWireCables();
        applyFluidPipes();
        applyItemPipes();
    }

    // spotless:off
    private static void applyWireCables() {
        WireCable[] wireCables = {
            new WireCable(Materials2Materials.RedAlloy, 0, 1, TierEU.ULV),
            new WireCable(Materials2Materials.Cobalt, 1, 2, TierEU.LV),
            new WireCable(Materials2Materials.Lead, 2, 2, TierEU.LV),
            new WireCable(Materials2Materials.Tin, 1, 1, TierEU.LV),
            new WireCable(Materials2Materials.Zinc, 1, 1, TierEU.LV),
            new WireCable(Materials2Materials.SolderingAlloy, 1, 1, TierEU.LV),
            new WireCable(Materials2Materials.Iron, 3, 2, TierEU.MV),
            new WireCable(Materials2Materials.Nickel, 3, 3, TierEU.MV),
            new WireCable(Materials2Materials.Cupronickel, 3, 4, TierEU.MV),
            new WireCable(Materials2Materials.Copper, 2, 1, TierEU.MV),
            new WireCable(Materials2Materials.AnnealedCopper, 1, 1, TierEU.MV),
            new WireCable(Materials2Materials.Kanthal, 3, 5, TierEU.HV),
            new WireCable(Materials2Materials.Gold, 2, 3, TierEU.HV),
            new WireCable(Materials2Materials.Electrum, 1, 2, TierEU.HV),
            new WireCable(Materials2Materials.Silver, 1, 1, TierEU.HV),
            new WireCable(Materials2Materials.BlueAlloy, 1, 2, TierEU.HV),
            new WireCable(Materials2Materials.Nichrome, 4, 6, TierEU.EV),
            new WireCable(Materials2Materials.Steel, 3, 2, TierEU.EV),
            new WireCable(Materials2Materials.BlackSteel, 1, 4, TierEU.EV),
            new WireCable(Materials2Materials.Titanium, 2, 4, TierEU.EV),
            new WireCable(Materials2Materials.Aluminium, 1, 1, TierEU.EV),
            new WireCable(Materials2Materials.TPVAlloy, 1, 6, TierEU.EV),
            new WireCable(Materials2Materials.Platinum, 1, 2, TierEU.IV),
            new WireCable(Materials2Materials.TungstenSteel, 4, 4, TierEU.IV),
            new WireCable(Materials2Materials.Tungsten, 2, 6, TierEU.IV),
            new WireCable(Materials2Materials.Osmium, 2, 4, TierEU.LuV),
            new WireCable(Materials2Materials.HSSG, 2, 4, TierEU.LuV),
            new WireCable(Materials2Materials.NiobiumTitanium, 2, 4, TierEU.LuV),
            new WireCable(Materials2Materials.VanadiumGallium, 4, 4, TierEU.LuV),
            new WireCable(Materials2Materials.YttriumBariumCuprate, 3, 6, TierEU.LuV),
            new WireCable(Materials2Materials.Naquadah, 2, 2, TierEU.ZPM),
            new WireCable(Materials2Materials.NaquadahAlloy, 4, 6, TierEU.UV),
            new WireCable(Materials2Materials.Duranium, 2, 4, TierEU.UV),
        };

        WireOnly[] wireOnly = {
            new WireOnly(Materials2Materials.Graphene, 2, 1, TierEU.IV, false),
            new WireOnly(Materials2Materials.Ichorium, 8, 12, TierEU.UHV, false),
            new WireOnly(Materials2Materials.SpaceTime, 0, 1_000_000, TierEU.MAX, false),
        };

        WireOnly[] superconductorBases = {
            new WireOnly(Materials2Materials.Pentacadmiummagnesiumhexaoxid, 2, 1, TierEU.MV, true),
            new WireOnly(Materials2Materials.Titaniumonabariumdecacoppereikosaoxid, 8, 2, TierEU.HV, true),
            new WireOnly(Materials2Materials.Uraniumtriplatinid, 16, 3, TierEU.EV, true),
            new WireOnly(Materials2Materials.Vanadiumtriindinid, 64, 4, TierEU.IV, true),
            new WireOnly(Materials2Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 256, 6, TierEU.LuV, true),
            new WireOnly(Materials2Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1024, 8, TierEU.ZPM, true),
            new WireOnly(Materials2Materials.Longasssuperconductornameforuvwire, 4096, 12, TierEU.UV, true),
            new WireOnly(Materials2Materials.Longasssuperconductornameforuhvwire, 16384, 16, TierEU.UHV, true),
            new WireOnly(Materials2Materials.SuperconductorUEVBase, 65536, 24, TierEU.UEV, true),
            new WireOnly(Materials2Materials.SuperconductorUIVBase, 262144, 32, TierEU.UIV, true),
            new WireOnly(Materials2Materials.SuperconductorUMVBase, 1_048_576, 32, TierEU.UMV, true),
        };

        WireOnly[] superconductorMarkers = {
            new WireOnly(Materials2Markers.SuperconductorMV, 0, 4, TierEU.MV, false),
            new WireOnly(Materials2Markers.SuperconductorHV, 0, 6, TierEU.HV, false),
            new WireOnly(Materials2Markers.SuperconductorEV, 0, 8, TierEU.EV, false),
            new WireOnly(Materials2Markers.SuperconductorIV, 0, 12, TierEU.IV, false),
            new WireOnly(Materials2Markers.SuperconductorLuV, 0, 16, TierEU.LuV, false),
            new WireOnly(Materials2Markers.SuperconductorZPM, 0, 24, TierEU.ZPM, false),
            new WireOnly(Materials2Markers.SuperconductorUV, 0, 32, TierEU.UV, false),
            new WireOnly(Materials2Markers.SuperconductorUHV, 0, 48, TierEU.UHV, false),
            new WireOnly(Materials2Markers.SuperconductorUEV, 0, 64, TierEU.UEV, false),
            new WireOnly(Materials2Markers.SuperconductorUIV, 0, 64, TierEU.UIV, false),
            new WireOnly(Materials2Markers.SuperconductorUMV, 0, 64, TierEU.UMV, false),
        };
        // spotless:on

        for (WireCable row : wireCables) {
            edit(row.material()).setProperty(Materials2PipeProperties.BASE_CABLE_AMP, row.amperage())
                .setProperty(Materials2PipeProperties.BASE_CABLE_VOLT, row.voltage())
                .setProperty(Materials2PipeProperties.BASE_CABLE_LOSS, row.cableLoss());
        }
        // Red alloy wires lose 1 while its cables lose 0, so the twice-cable-loss default does not hold.
        edit(Materials2Materials.RedAlloy).setProperty(Materials2PipeProperties.WIRE_LOSS, 1);

        for (WireOnly[] rows : new WireOnly[][] { wireOnly, superconductorBases, superconductorMarkers }) {
            for (WireOnly row : rows) {
                MaterialEdit edit = edit(row.material())
                    .setProperty(Materials2PipeProperties.BASE_CABLE_AMP, row.amperage())
                    .setProperty(Materials2PipeProperties.BASE_CABLE_VOLT, row.voltage())
                    .setProperty(Materials2PipeProperties.WIRE_LOSS, row.wireLoss())
                    .setProperty(Materials2PipeProperties.NO_CABLE, true);
                if (!row.shock()) {
                    edit.setProperty(Materials2PipeProperties.NO_SHOCK, true);
                }
            }
        }
    }

    // spotless:off
    private static void applyFluidPipes() {
        FluidPipe[] fluidPipes = {
            new FluidPipe(Materials2Materials.Copper, 20, 1000),
            new FluidPipe(Materials2Materials.Bronze, 120, 2000),
            new FluidPipe(Materials2Materials.Steel, 240, 2500),
            new FluidPipe(Materials2Materials.StainlessSteel, 360, 3000),
            new FluidPipe(Materials2Materials.Titanium, 480, 5000),
            new FluidPipe(Materials2Materials.TungstenSteel, 600, 7500),
            new FluidPipe(Materials2Materials.Polybenzimidazole, 600, 1000),
            new FluidPipe(Materials2Materials.Plastic, 360, 350),
            new FluidPipe(Materials2Materials.NiobiumTitanium, 900, 2900),
            new FluidPipe(Materials2Materials.Enderium, 1800, 15000),
            new FluidPipe(Materials2Materials.Naquadah, 9000, 19000),
            new FluidPipe(Materials2Materials.Neutronium, 16800, 1_000_000),
            new FluidPipe(Materials2Materials.NetherStar, 19200, 1_000_000),
            new FluidPipe(Materials2Materials.MysteriousCrystal, 24000, 1_000_000),
            new FluidPipe(Materials2Materials.DraconiumAwakened, 45000, 10_000_000),
            new FluidPipe(Materials2Materials.Infinity, 60000, 10_000_000),
            new FluidPipe(Materials2Materials.CastIron, 180, 2250),
            new FluidPipe(Materials2Materials.Polytetrafluoroethylene, 480, 600),
            new FluidPipe(Materials2Materials.SpaceTime, 250000, Integer.MAX_VALUE),
            new FluidPipe(Materials2Materials.TranscendentMetal, 220000, Integer.MAX_VALUE),
            new FluidPipe(Materials2Materials.RadoxPoly, 5000, 1500),
        };
        // spotless:on

        for (FluidPipe row : fluidPipes) {
            edit(row.material()).setProperty(Materials2PipeProperties.BASE_PIPE_FLOW, row.baseCapacity())
                .setProperty(Materials2PipeProperties.PIPE_HEAT_RESISTANCE, row.heatResistance());
        }

        edit(Materials2Materials.Wood)
            .setProperty(Materials2PipeProperties.PIPE_HEAT_RESISTANCE, WOOD_FLUID_PIPE_HEAT_RESISTANCE)
            .setProperty(Materials2PipeProperties.PIPE_GAS_PROOF, false);
        edit(Materials2Materials.Redstone)
            .setProperty(Materials2PipeProperties.PIPE_HEAT_RESISTANCE, HIGH_PRESSURE_FLUID_PIPE_HEAT_RESISTANCE);
    }

    // spotless:off
    private static void applyItemPipes() {
        ItemPipe[] itemPipes = {
            new ItemPipe(Materials2Materials.Tin, 2, true),
            new ItemPipe(Materials2Materials.Brass, 4, true),
            new ItemPipe(Materials2Materials.Electrum, 8, true),
            new ItemPipe(Materials2Materials.Platinum, 16, true),
            new ItemPipe(Materials2Materials.Osmium, 32, true),
            new ItemPipe(Materials2Materials.ElectrumFlux, 64, true),
            new ItemPipe(Materials2Materials.BlackPlutonium, 128, true),
            new ItemPipe(Materials2Materials.Bedrockium, 256, true),
            new ItemPipe(Materials2Materials.PolyvinylChloride, 16, false),
            new ItemPipe(Materials2Materials.Nickel, 4, false),
            new ItemPipe(Materials2Materials.Cobalt, 8, false),
            new ItemPipe(Materials2Materials.Aluminium, 8, false),
            new ItemPipe(Materials2Materials.Quantium, 512, true),
        };
        // spotless:on

        for (ItemPipe row : itemPipes) {
            MaterialEdit edit = edit(row.material())
                .setProperty(Materials2PipeProperties.BASE_ITEM_PIPE_SLOTS, row.hugeSlots());
            if (!row.smallPipes()) {
                edit.setProperty(Materials2PipeProperties.NO_SMALL_ITEM_PIPES, true);
            }
        }
    }

    private static MaterialEdit edit(Material material) {
        return MaterialLibAPI.editMaterial(material.getModId(), material.getName());
    }

    private Materials2PipeMaterials() {}
}
