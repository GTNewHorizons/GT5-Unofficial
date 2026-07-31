package gregtech.api.enums.materials;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialEdit;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;

/// The pipe-family stat tables: one declared row per material carrying wires/cables, fluid pipes, or item
/// pipes, applied as [PipeProperties] values through [MaterialLibAPI#editMaterial]. The values are exact
/// literals, not derived: each row pins the stats its material's pipes have always had.
///
/// Editing rather than declaring is deliberate here (unlike [ShapeData]'s declare-path): every target key
/// comes off a live [Material] field, so [MaterialLibAPI#editMaterial]'s silently-skipped-edit hazard --
/// naming a material that does not exist -- cannot arise.
///
/// Only stat-bearing membership lives here. Frame and sheetmetal membership carries no stats, so it is
/// declared on each material's own builder in [Materials] instead.
///
/// The wooden and High Pressure fluid pipes exist in three sizes whose capacities follow no base-value
/// formula, so they carry per-size capacity constants here instead of a [PipeProperties#BASE_PIPE_FLOW]
/// value.
///
/// Rows reference [Materials] and [MaterialFacades] fields, so [#init] must run after those are
/// assigned; [MaterialSystem#init] orders it so.
public class PipeMaterials {

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
        applyModGatedFrames();
    }

    // spotless:off
    private static void applyWireCables() {
        WireCable[] wireCables = {
            new WireCable(Materials.RedAlloy, 0, 1, TierEU.ULV),
            new WireCable(Materials.Cobalt, 1, 2, TierEU.LV),
            new WireCable(Materials.Lead, 2, 2, TierEU.LV),
            new WireCable(Materials.Tin, 1, 1, TierEU.LV),
            new WireCable(Materials.Zinc, 1, 1, TierEU.LV),
            new WireCable(Materials.SolderingAlloy, 1, 1, TierEU.LV),
            new WireCable(Materials.RedstoneAlloy, 0, 1, TierEU.LV),
            new WireCable(Materials.Iron, 3, 2, TierEU.MV),
            new WireCable(Materials.Nickel, 3, 3, TierEU.MV),
            new WireCable(Materials.Cupronickel, 3, 4, TierEU.MV),
            new WireCable(Materials.Copper, 2, 1, TierEU.MV),
            new WireCable(Materials.AnnealedCopper, 1, 1, TierEU.MV),
            new WireCable(Materials.ElectricalSteel, 1, 3, TierEU.MV),
            new WireCable(Materials.Kanthal, 3, 5, TierEU.HV),
            new WireCable(Materials.Gold, 2, 3, TierEU.HV),
            new WireCable(Materials.Electrum, 1, 2, TierEU.HV),
            new WireCable(Materials.Silver, 1, 1, TierEU.HV),
            new WireCable(Materials.BlueAlloy, 1, 2, TierEU.HV),
            new WireCable(Materials.EnergeticAlloy, 1, 4, TierEU.HV),
            new WireCable(Materials.Nichrome, 4, 6, TierEU.EV),
            new WireCable(Materials.Steel, 3, 2, TierEU.EV),
            new WireCable(Materials.BlackSteel, 1, 4, TierEU.EV),
            new WireCable(Materials.Titanium, 2, 4, TierEU.EV),
            new WireCable(Materials.Aluminium, 1, 1, TierEU.EV),
            new WireCable(Materials.TPVAlloy, 1, 6, TierEU.EV),
            new WireCable(Materials.VibrantAlloy, 3, 6, TierEU.EV),
            new WireCable(Materials.Platinum, 1, 2, TierEU.IV),
            new WireCable(Materials.TungstenSteel, 4, 4, TierEU.IV),
            new WireCable(Materials.Tungsten, 2, 6, TierEU.IV),
            new WireCable(Materials.EndSteel, 3, 6, TierEU.IV),
            new WireCable(Materials.Osmium, 2, 4, TierEU.LuV),
            new WireCable(Materials.HSSG, 2, 4, TierEU.LuV),
            new WireCable(Materials.NiobiumTitanium, 2, 4, TierEU.LuV),
            new WireCable(Materials.VanadiumGallium, 4, 4, TierEU.LuV),
            new WireCable(Materials.YttriumBariumCuprate, 3, 6, TierEU.LuV),
            new WireCable(Materials.MelodicAlloy, 2, 4, TierEU.LuV),
            new WireCable(Materials.HSSE, 4, 6, TierEU.LuV),
            new WireCable(Materials.Naquadah, 2, 2, TierEU.ZPM),
            new WireCable(Materials.Signalium, 8, 12, TierEU.ZPM),
            new WireCable(Materials.Trinium, 3, 6, TierEU.ZPM),
            new WireCable(Materials.Osmiridium, 1, 16, TierEU.ZPM),
            new WireCable(Materials.NaquadahAlloy, 4, 6, TierEU.UV),
            new WireCable(Materials.Duranium, 2, 4, TierEU.UV),
            new WireCable(Materials.Lumiium, 16, 8, TierEU.UV),
            new WireCable(Materials.StellarAlloy, 12, 12, TierEU.UV),
            new WireCable(Materials.ElectrumFlux, 3, 6, TierEU.UV),
            new WireCable(Materials.Bedrockium, 6, 3, TierEU.UHV),
            new WireCable(Materials.HSSS, 4, 8, TierEU.UHV),
            new WireCable(Materials.Draconium, 32, 8, TierEU.UEV),
            new WireCable(Materials.NetherStar, 16, 4, TierEU.UIV),
            new WireCable(Materials.Quantium, 32, 4, TierEU.UMV),
        };

        WireOnly[] wireOnly = {
            new WireOnly(Materials.Graphene, 2, 1, TierEU.IV, false),
            new WireOnly(Materials.Ichorium, 8, 12, TierEU.UHV, false),
            new WireOnly(Materials.Hypogen, 0, 8, TierEU.UIV, false),
            new WireOnly(Materials.BlackPlutonium, 8, 8, TierEU.UXV, true),
            new WireOnly(Materials.DraconiumAwakened, 64, 8, TierEU.MAX, true),
            new WireOnly(Materials.Infinity, 0, 8192, TierEU.MAX, false),
            new WireOnly(Materials.SpaceTime, 0, 1_000_000, TierEU.MAX, false),
        };

        WireOnly[] superconductorBases = {
            new WireOnly(Materials.Pentacadmiummagnesiumhexaoxid, 2, 1, TierEU.MV, true),
            new WireOnly(Materials.Titaniumonabariumdecacoppereikosaoxid, 8, 2, TierEU.HV, true),
            new WireOnly(Materials.Uraniumtriplatinid, 16, 3, TierEU.EV, true),
            new WireOnly(Materials.Vanadiumtriindinid, 64, 4, TierEU.IV, true),
            new WireOnly(Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 256, 6, TierEU.LuV, true),
            new WireOnly(Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1024, 8, TierEU.ZPM, true),
            new WireOnly(Materials.Longasssuperconductornameforuvwire, 4096, 12, TierEU.UV, true),
            new WireOnly(Materials.Longasssuperconductornameforuhvwire, 16384, 16, TierEU.UHV, true),
            new WireOnly(Materials.SuperconductorUEVBase, 65536, 24, TierEU.UEV, true),
            new WireOnly(Materials.SuperconductorUIVBase, 262144, 32, TierEU.UIV, true),
            new WireOnly(Materials.SuperconductorUMVBase, 1_048_576, 32, TierEU.UMV, true),
        };

        WireOnly[] superconductorMarkers = {
            new WireOnly(MaterialFacades.SuperconductorMV, 0, 4, TierEU.MV, false),
            new WireOnly(MaterialFacades.SuperconductorHV, 0, 6, TierEU.HV, false),
            new WireOnly(MaterialFacades.SuperconductorEV, 0, 8, TierEU.EV, false),
            new WireOnly(MaterialFacades.SuperconductorIV, 0, 12, TierEU.IV, false),
            new WireOnly(MaterialFacades.SuperconductorLuV, 0, 16, TierEU.LuV, false),
            new WireOnly(MaterialFacades.SuperconductorZPM, 0, 24, TierEU.ZPM, false),
            new WireOnly(MaterialFacades.SuperconductorUV, 0, 32, TierEU.UV, false),
            new WireOnly(MaterialFacades.SuperconductorUHV, 0, 48, TierEU.UHV, false),
            new WireOnly(MaterialFacades.SuperconductorUEV, 0, 64, TierEU.UEV, false),
            new WireOnly(MaterialFacades.SuperconductorUIV, 0, 64, TierEU.UIV, false),
            new WireOnly(MaterialFacades.SuperconductorUMV, 0, 64, TierEU.UMV, false),
        };
        // spotless:on

        for (WireCable row : wireCables) {
            edit(row.material()).setProperty(PipeProperties.BASE_CABLE_AMP, row.amperage())
                .setProperty(PipeProperties.BASE_CABLE_VOLT, row.voltage())
                .setProperty(PipeProperties.BASE_CABLE_LOSS, row.cableLoss())
                .generateShapes(wireShapes())
                .generateShapes(cableShapes());
        }
        // These wires break the twice-cable-loss default: the two redstone alloys pair lossless cables with
        // lossy wires, and CrackRecipeAdder.registerWire derives cable loss as a quarter of wire loss.
        edit(Materials.RedAlloy).setProperty(PipeProperties.WIRE_LOSS, 1);
        edit(Materials.RedstoneAlloy).setProperty(PipeProperties.WIRE_LOSS, 2);
        edit(Materials.Signalium).setProperty(PipeProperties.WIRE_LOSS, 32);
        edit(Materials.Lumiium).setProperty(PipeProperties.WIRE_LOSS, 64);
        edit(Materials.Bedrockium).setProperty(PipeProperties.WIRE_LOSS, 48);
        edit(Materials.Quantium).setProperty(PipeProperties.WIRE_LOSS, 128);

        for (WireOnly[] rows : new WireOnly[][] { wireOnly, superconductorBases, superconductorMarkers }) {
            for (WireOnly row : rows) {
                MaterialEdit edit = edit(row.material()).setProperty(PipeProperties.BASE_CABLE_AMP, row.amperage())
                    .setProperty(PipeProperties.BASE_CABLE_VOLT, row.voltage())
                    .setProperty(PipeProperties.WIRE_LOSS, row.wireLoss())
                    .setProperty(PipeProperties.NO_CABLE, true)
                    .generateShapes(wireShapes());
                if (!row.shock()) {
                    edit.setProperty(PipeProperties.NO_SHOCK, true);
                }
            }
        }
    }

    /// The wire sizes this table grants as one unit: a material carrying any of them carries all six. Public
    /// so a consumer can state that unit as its own precondition instead of re-listing the sizes.
    public static Shape[] wireShapes() {
        return new Shape[] { PipeShapes.wireGt01, PipeShapes.wireGt02, PipeShapes.wireGt04, PipeShapes.wireGt08,
            PipeShapes.wireGt12, PipeShapes.wireGt16 };
    }

    private static Shape[] cableShapes() {
        return new Shape[] { PipeShapes.cableGt01, PipeShapes.cableGt02, PipeShapes.cableGt04, PipeShapes.cableGt08,
            PipeShapes.cableGt12, PipeShapes.cableGt16 };
    }

    // spotless:off
    private static void applyFluidPipes() {
        FluidPipe[] fluidPipes = {
            new FluidPipe(Materials.Copper, 20, 1000),
            new FluidPipe(Materials.Bronze, 120, 2000),
            new FluidPipe(Materials.Steel, 240, 2500),
            new FluidPipe(Materials.StainlessSteel, 360, 3000),
            new FluidPipe(Materials.Titanium, 480, 5000),
            new FluidPipe(Materials.TungstenSteel, 600, 7500),
            new FluidPipe(Materials.Polybenzimidazole, 600, 1000),
            new FluidPipe(Materials.Plastic, 360, 350),
            new FluidPipe(Materials.NiobiumTitanium, 900, 2900),
            new FluidPipe(Materials.Enderium, 1800, 15000),
            new FluidPipe(Materials.Naquadah, 9000, 19000),
            new FluidPipe(Materials.Neutronium, 16800, 1_000_000),
            new FluidPipe(Materials.NetherStar, 19200, 1_000_000),
            new FluidPipe(Materials.MysteriousCrystal, 24000, 1_000_000),
            new FluidPipe(Materials.DraconiumAwakened, 45000, 10_000_000),
            new FluidPipe(Materials.Infinity, 60000, 10_000_000),
            new FluidPipe(Materials.CastIron, 180, 2250),
            new FluidPipe(Materials.Polytetrafluoroethylene, 480, 600),
            new FluidPipe(Materials.SpaceTime, 250000, Integer.MAX_VALUE),
            new FluidPipe(Materials.TranscendentMetal, 220000, Integer.MAX_VALUE),
            new FluidPipe(Materials.RadoxPoly, 5000, 1500),
            // The gtPlusPlus/goodgenerator pipes were declared as per-second throughputs; these rows store
            // the medium pipe's per-tick capacity, 12 * (declared / 20) with truncating division.
            new FluidPipe(Materials.Staballoy, 7500, 7500),
            new FluidPipe(Materials.Tantalloy60, 6000, 4250),
            new FluidPipe(Materials.Tantalloy61, 7200, 5800),
            new FluidPipe(Materials.Void, 960, 25000),
            new FluidPipe(Materials.Europium, 7200, 7500),
            new FluidPipe(Materials.Potin, 300, 2000),
            new FluidPipe(Materials.MaragingSteel300, 8400, 2500),
            new FluidPipe(Materials.MaragingSteel350, 9600, 2500),
            new FluidPipe(Materials.Inconel690, 9000, 4800),
            new FluidPipe(Materials.Inconel792, 9600, 5500),
            new FluidPipe(Materials.HastelloyX, 12000, 4200),
            new FluidPipe(Materials.TriniumNaquadahCarbonite, 12, 250000),
            new FluidPipe(Materials.Tungsten, 2592, 7200),
            new FluidPipe(Materials.DarkSteel, 1392, 2750),
            new FluidPipe(Materials.Clay, 60, 500),
            new FluidPipe(Materials.Lead, 204, 1200),
            new FluidPipe(Materials.Incoloy903, 15000, 8000),
        };
        // spotless:on

        for (FluidPipe row : fluidPipes) {
            MaterialEdit edit = edit(row.material()).setProperty(PipeProperties.BASE_PIPE_FLOW, row.baseCapacity())
                .setProperty(PipeProperties.PIPE_HEAT_RESISTANCE, row.heatResistance());
            // These two rows exist only with their parent mod (Void pipes with Thaumcraft, DarkSteel with
            // EnderIO); the properties themselves stay unconditional.
            if (row.material() == Materials.Void && !Mods.Thaumcraft.isModLoaded()) continue;
            if (row.material() == Materials.DarkSteel && !Mods.EnderIO.isModLoaded()) continue;
            edit.generateShapes(
                PipeShapes.pipeTiny,
                PipeShapes.pipeSmall,
                PipeShapes.pipeMedium,
                PipeShapes.pipeLarge,
                PipeShapes.pipeHuge,
                PipeShapes.pipeQuadruple,
                PipeShapes.pipeNonuple);
        }

        edit(Materials.Wood).setProperty(PipeProperties.PIPE_HEAT_RESISTANCE, WOOD_FLUID_PIPE_HEAT_RESISTANCE)
            .setProperty(PipeProperties.PIPE_GAS_PROOF, false)
            .generateShapes(PipeShapes.pipeSmall, PipeShapes.pipeMedium, PipeShapes.pipeLarge);
        edit(Materials.Clay).setProperty(PipeProperties.PIPE_GAS_PROOF, false);
        edit(Materials.Redstone)
            .setProperty(PipeProperties.PIPE_HEAT_RESISTANCE, HIGH_PRESSURE_FLUID_PIPE_HEAT_RESISTANCE)
            .generateShapes(PipeShapes.pipeSmall, PipeShapes.pipeMedium, PipeShapes.pipeLarge);
    }

    // spotless:off
    private static void applyItemPipes() {
        ItemPipe[] itemPipes = {
            new ItemPipe(Materials.Tin, 2, true),
            new ItemPipe(Materials.Brass, 4, true),
            new ItemPipe(Materials.Electrum, 8, true),
            new ItemPipe(Materials.Platinum, 16, true),
            new ItemPipe(Materials.Osmium, 32, true),
            new ItemPipe(Materials.ElectrumFlux, 64, true),
            new ItemPipe(Materials.BlackPlutonium, 128, true),
            new ItemPipe(Materials.Bedrockium, 256, true),
            new ItemPipe(Materials.PolyvinylChloride, 16, false),
            new ItemPipe(Materials.Nickel, 4, false),
            new ItemPipe(Materials.Cobalt, 8, false),
            new ItemPipe(Materials.Aluminium, 8, false),
            new ItemPipe(Materials.Quantium, 512, true),
        };
        // spotless:on

        for (ItemPipe row : itemPipes) {
            MaterialEdit edit = edit(row.material()).setProperty(PipeProperties.BASE_ITEM_PIPE_SLOTS, row.hugeSlots())
                .generateShapes(
                    PipeShapes.itemPipeMedium,
                    PipeShapes.itemPipeLarge,
                    PipeShapes.itemPipeHuge,
                    PipeShapes.itemPipeRestrictiveMedium,
                    PipeShapes.itemPipeRestrictiveLarge,
                    PipeShapes.itemPipeRestrictiveHuge);
            if (row.smallPipes()) {
                edit.generateShapes(
                    PipeShapes.itemPipeTiny,
                    PipeShapes.itemPipeSmall,
                    PipeShapes.itemPipeRestrictiveTiny,
                    PipeShapes.itemPipeRestrictiveSmall);
            } else {
                edit.setProperty(PipeProperties.NO_SMALL_ITEM_PIPES, true);
            }
        }
    }

    /// HSLA's frame is the one row that cannot be a `generateShape` on the material's own declaration: it
    /// exists only when RotaryCraft supplies the recipe, and a builder chain cannot be conditional. Every
    /// other frame and sheetmetal membership is declared in [Materials].
    private static void applyModGatedFrames() {
        if (Mods.RotaryCraft.isModLoaded()) {
            edit(Materials.HSLA).generateShape(PipeShapes.frameGt);
        }
    }

    private static MaterialEdit edit(Material material) {
        return MaterialLibAPI.editMaterial(material.getModId(), material.getName());
    }

    private PipeMaterials() {}
}
