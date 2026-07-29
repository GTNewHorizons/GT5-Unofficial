package gregtech.api.enums.materials2;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialEdit;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;

/// The pipe-family stat tables: one declared row per material carrying wires/cables, fluid pipes, or item
/// pipes, applied as [Materials2PipeProperties] values through [MaterialLibAPI#editMaterial]. The values
/// reproduce the literals the retired per-material pipe MTE registrations received (GregTech's own pipe
/// loader, [gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits], and goodgenerator's
/// [goodgenerator.util.CrackRecipeAdder]).
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
        applyFrames();
        applyGtppFrame();
        applyWerkstoffFrameAndSheetmetal();
        applySheetmetal();
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
            new WireCable(Materials2Materials.RedstoneAlloy, 0, 1, TierEU.LV),
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
            new WireCable(Materials2Materials.Signalium, 8, 12, TierEU.ZPM),
            new WireCable(Materials2Materials.NaquadahAlloy, 4, 6, TierEU.UV),
            new WireCable(Materials2Materials.Duranium, 2, 4, TierEU.UV),
            new WireCable(Materials2Materials.Lumiium, 16, 8, TierEU.UV),
        };

        WireOnly[] wireOnly = {
            new WireOnly(Materials2Materials.Graphene, 2, 1, TierEU.IV, false),
            new WireOnly(Materials2Materials.Ichorium, 8, 12, TierEU.UHV, false),
            new WireOnly(Materials2Materials.Hypogen, 0, 8, TierEU.UIV, false),
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
                .setProperty(Materials2PipeProperties.BASE_CABLE_LOSS, row.cableLoss())
                .generateShapes(wireShapes())
                .generateShapes(cableShapes());
        }
        // These wires break the twice-cable-loss default: the two redstone alloys pair lossless cables with
        // lossy wires, and CrackRecipeAdder.registerWire derives cable loss as a quarter of wire loss.
        edit(Materials2Materials.RedAlloy).setProperty(Materials2PipeProperties.WIRE_LOSS, 1);
        edit(Materials2Materials.RedstoneAlloy).setProperty(Materials2PipeProperties.WIRE_LOSS, 2);
        edit(Materials2Materials.Signalium).setProperty(Materials2PipeProperties.WIRE_LOSS, 32);
        edit(Materials2Materials.Lumiium).setProperty(Materials2PipeProperties.WIRE_LOSS, 64);

        for (WireOnly[] rows : new WireOnly[][] { wireOnly, superconductorBases, superconductorMarkers }) {
            for (WireOnly row : rows) {
                MaterialEdit edit = edit(row.material())
                    .setProperty(Materials2PipeProperties.BASE_CABLE_AMP, row.amperage())
                    .setProperty(Materials2PipeProperties.BASE_CABLE_VOLT, row.voltage())
                    .setProperty(Materials2PipeProperties.WIRE_LOSS, row.wireLoss())
                    .setProperty(Materials2PipeProperties.NO_CABLE, true)
                    .generateShapes(wireShapes());
                if (!row.shock()) {
                    edit.setProperty(Materials2PipeProperties.NO_SHOCK, true);
                }
            }
        }
    }

    private static Shape[] wireShapes() {
        return new Shape[] { Materials2PipeShapes.wireGt01, Materials2PipeShapes.wireGt02,
            Materials2PipeShapes.wireGt04, Materials2PipeShapes.wireGt08, Materials2PipeShapes.wireGt12,
            Materials2PipeShapes.wireGt16 };
    }

    private static Shape[] cableShapes() {
        return new Shape[] { Materials2PipeShapes.cableGt01, Materials2PipeShapes.cableGt02,
            Materials2PipeShapes.cableGt04, Materials2PipeShapes.cableGt08, Materials2PipeShapes.cableGt12,
            Materials2PipeShapes.cableGt16 };
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
            // The gtPlusPlus/goodgenerator pipes were declared as per-second throughputs; these rows store
            // the medium pipe's per-tick capacity, 12 * (declared / 20) with truncating division.
            new FluidPipe(Materials2Materials.Staballoy, 7500, 7500),
            new FluidPipe(Materials2Materials.Tantalloy60, 6000, 4250),
            new FluidPipe(Materials2Materials.Tantalloy61, 7200, 5800),
            new FluidPipe(Materials2Materials.Void, 960, 25000),
            new FluidPipe(Materials2Materials.Europium, 7200, 7500),
            new FluidPipe(Materials2Materials.Potin, 300, 2000),
            new FluidPipe(Materials2Materials.MaragingSteel300, 8400, 2500),
            new FluidPipe(Materials2Materials.MaragingSteel350, 9600, 2500),
            new FluidPipe(Materials2Materials.Inconel690, 9000, 4800),
            new FluidPipe(Materials2Materials.Inconel792, 9600, 5500),
            new FluidPipe(Materials2Materials.HastelloyX, 12000, 4200),
            new FluidPipe(Materials2Materials.TriniumNaquadahCarbonite, 12, 250000),
            new FluidPipe(Materials2Materials.Tungsten, 2592, 7200),
            new FluidPipe(Materials2Materials.DarkSteel, 1392, 2750),
            new FluidPipe(Materials2Materials.Clay, 60, 500),
            new FluidPipe(Materials2Materials.Lead, 204, 1200),
            new FluidPipe(Materials2Materials.Incoloy903, 15000, 8000),
        };
        // spotless:on

        for (FluidPipe row : fluidPipes) {
            MaterialEdit edit = edit(row.material())
                .setProperty(Materials2PipeProperties.BASE_PIPE_FLOW, row.baseCapacity())
                .setProperty(Materials2PipeProperties.PIPE_HEAT_RESISTANCE, row.heatResistance());
            // These two rows exist only with their parent mod (Void pipes with Thaumcraft, DarkSteel with
            // EnderIO); the properties themselves stay unconditional.
            if (row.material() == Materials2Materials.Void && !Mods.Thaumcraft.isModLoaded()) continue;
            if (row.material() == Materials2Materials.DarkSteel && !Mods.EnderIO.isModLoaded()) continue;
            edit.generateShapes(
                Materials2PipeShapes.pipeTiny,
                Materials2PipeShapes.pipeSmall,
                Materials2PipeShapes.pipeMedium,
                Materials2PipeShapes.pipeLarge,
                Materials2PipeShapes.pipeHuge,
                Materials2PipeShapes.pipeQuadruple,
                Materials2PipeShapes.pipeNonuple);
        }

        edit(Materials2Materials.Wood)
            .setProperty(Materials2PipeProperties.PIPE_HEAT_RESISTANCE, WOOD_FLUID_PIPE_HEAT_RESISTANCE)
            .setProperty(Materials2PipeProperties.PIPE_GAS_PROOF, false)
            .generateShapes(
                Materials2PipeShapes.pipeSmall,
                Materials2PipeShapes.pipeMedium,
                Materials2PipeShapes.pipeLarge);
        edit(Materials2Materials.Clay).setProperty(Materials2PipeProperties.PIPE_GAS_PROOF, false);
        edit(Materials2Materials.Redstone)
            .setProperty(Materials2PipeProperties.PIPE_HEAT_RESISTANCE, HIGH_PRESSURE_FLUID_PIPE_HEAT_RESISTANCE)
            .generateShapes(
                Materials2PipeShapes.pipeSmall,
                Materials2PipeShapes.pipeMedium,
                Materials2PipeShapes.pipeLarge);
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
                .setProperty(Materials2PipeProperties.BASE_ITEM_PIPE_SLOTS, row.hugeSlots())
                .generateShapes(
                    Materials2PipeShapes.itemPipeMedium,
                    Materials2PipeShapes.itemPipeLarge,
                    Materials2PipeShapes.itemPipeHuge,
                    Materials2PipeShapes.itemPipeRestrictiveMedium,
                    Materials2PipeShapes.itemPipeRestrictiveLarge,
                    Materials2PipeShapes.itemPipeRestrictiveHuge);
            if (row.smallPipes()) {
                edit.generateShapes(
                    Materials2PipeShapes.itemPipeTiny,
                    Materials2PipeShapes.itemPipeSmall,
                    Materials2PipeShapes.itemPipeRestrictiveTiny,
                    Materials2PipeShapes.itemPipeRestrictiveSmall);
            } else {
                edit.setProperty(Materials2PipeProperties.NO_SMALL_ITEM_PIPES, true);
            }
        }
    }

    // spotless:off
    /// Frame membership: the materials the legacy frame registrations serve, i.e. every generated legacy
    /// material whose dumped [gregtech.api.material.GTMaterialProperties#GENERATION_FLAGS] carry `METAL`
    /// (the [gregtech.api.material.MaterialUtils#generates] `METAL` mirror) and whose
    /// [gregtech.api.material.GTMaterialProperties#OLD_SUB_ID] marks a generated slot. The list is declared
    /// rather than derived because membership must be settled during material registration, before MaterialLib
    /// property reads are available.
    public static Material[] frameMaterials() {
        return new Material[] {
            Materials2Materials.Adamantium, Materials2Materials.Alduorite, Materials2Materials.Aluminium,
            Materials2Materials.Alumite, Materials2Materials.Americium, Materials2Materials.AnnealedCopper,
            Materials2Materials.Antimony, Materials2Materials.Ardite, Materials2Materials.Arsenic,
            Materials2Materials.AstralSilver, Materials2Materials.Barium, Materials2Materials.BatteryAlloy,
            Materials2Materials.Bedrockium, Materials2Materials.Beryllium, Materials2Materials.Bismuth,
            Materials2Materials.BismuthBronze, Materials2Materials.BlackBronze,
            Materials2Materials.BlackDwarfMatter, Materials2Materials.BlackPlutonium,
            Materials2Materials.BlackSteel, Materials2Materials.BloodInfusedIron, Materials2Materials.BlueAlloy,
            Materials2Materials.BlueSteel, Materials2Materials.BorosilicateGlass, Materials2Materials.Brass,
            Materials2Materials.Bronze, Materials2Materials.Caesium, Materials2Materials.CallistoIce,
            Materials2Materials.Carbon, Materials2Materials.CastIron, Materials2Materials.Cerium,
            Materials2Materials.Ceruclase, Materials2Materials.Chrome, Materials2Materials.ChromiumDioxide,
            Materials2Materials.Chrysotile, Materials2Materials.Churitsu, Materials2Materials.Cobalt,
            Materials2Materials.CobaltBrass, Materials2Materials.ConductiveIron, Materials2Materials.Copper,
            Materials2Materials.CosmicNeutronium, Materials2Materials.Creon, Materials2Materials.CrudeSteel,
            Materials2Materials.CrystallineAlloy, Materials2Materials.CrystallinePinkSlime,
            Materials2Materials.Cupronickel, Materials2Materials.DamascusSteel, Materials2Materials.DarkIron,
            Materials2Materials.DarkSteel, Materials2Materials.DeepIron, Materials2Materials.Desh,
            Materials2Materials.Draconium, Materials2Materials.DraconiumAwakened, Materials2Materials.Dreamwood,
            Materials2Materials.Duralumin, Materials2Materials.Duranium, Materials2Materials.Dysprosium,
            Materials2Materials.ElectricalSteel, Materials2Materials.Electrum, Materials2Materials.ElectrumFlux,
            Materials2Materials.ElvenElementium, Materials2Materials.EndSteel, Materials2Materials.Enderium,
            Materials2Materials.EnderiumBase, Materials2Materials.EnergeticAlloy,
            Materials2Materials.EnergeticSilver, Materials2Materials.EnhancedGalgadorian,
            Materials2Materials.EnrichedHolmium, Materials2Materials.Epoxid,
            Materials2Materials.EpoxidFiberReinforced, Materials2Materials.Erbium, Materials2Materials.Eternity,
            Materials2Materials.Europium, Materials2Materials.FierySteel, Materials2Materials.FleroviumGT5U,
            Materials2Materials.Force, Materials2Materials.Gadolinium, Materials2Materials.GaiaSpirit,
            Materials2Materials.Galgadorian, Materials2Materials.Gallium, Materials2Materials.GalliumArsenide,
            Materials2Materials.Gold, Materials2Materials.HSSE, Materials2Materials.HSSG,
            Materials2Materials.HSSS, Materials2Materials.HeeEndium, Materials2Materials.HellishMetal,
            Materials2Materials.Hexanite, Materials2Materials.Holmium, Materials2Materials.Ichorium,
            Materials2Materials.Indium, Materials2Materials.IndiumGalliumPhosphide, Materials2Materials.Infinity,
            Materials2Materials.InfinityCatalyst, Materials2Materials.InfusedGold, Materials2Materials.Invar,
            Materials2Materials.Iridium, Materials2Materials.Iron, Materials2Materials.IronMagnetic,
            Materials2Materials.IronWood, Materials2Materials.Kanthal, Materials2Materials.Kevlar,
            Materials2Materials.Knightmetal, Materials2Materials.Lanthanum, Materials2Materials.Lead,
            Materials2Materials.Ledox, Materials2Materials.Lithium, Materials2Materials.Livingwood,
            Materials2Materials.Longasssuperconductornameforuhvwire,
            Materials2Materials.Longasssuperconductornameforuvwire, Materials2Materials.Lutetium,
            Materials2Materials.Magmatter, Materials2Materials.Magnalium, Materials2Materials.Magnesium,
            Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2Materials.Manasteel,
            Materials2Materials.Manganese, Materials2Materials.Manyullyn, Materials2Materials.Mellion,
            Materials2Materials.MelodicAlloy, Materials2Materials.MeteoricIron, Materials2Materials.MeteoricSteel,
            Materials2Materials.Mithril, Materials2Materials.Molybdenum, Materials2Materials.MysteriousCrystal,
            Materials2Materials.Mytryl, Materials2Materials.Naquadah, Materials2Materials.NaquadahAlloy,
            Materials2Materials.NaquadahEnriched, Materials2Materials.Naquadria, Materials2Materials.Neodymium,
            Materials2Materials.NeodymiumMagnetic, Materials2Materials.Netherite, Materials2Materials.Neutronium,
            Materials2Materials.Nichrome, Materials2Materials.Nickel, Materials2Materials.NickelAluminide,
            Materials2Materials.NickelZincFerrite, Materials2Materials.Niobium, Materials2Materials.NiobiumNitride,
            Materials2Materials.NiobiumTitanium, Materials2Materials.Obsidian, Materials2Materials.Orichalcum,
            Materials2Materials.Oriharukon, Materials2Materials.Osmiridium, Materials2Materials.Osmium,
            Materials2Materials.Palladium, Materials2Materials.Pentacadmiummagnesiumhexaoxid,
            Materials2Materials.PigIron, Materials2Materials.Plastic, Materials2Materials.Platinum,
            Materials2Materials.Plutonium, Materials2Materials.Plutonium241, Materials2Materials.Polybenzimidazole,
            Materials2Materials.Polycaprolactam, Materials2Materials.PolyphenyleneSulfide,
            Materials2Materials.Polystyrene, Materials2Materials.Polytetrafluoroethylene,
            Materials2Materials.PolyvinylChloride, Materials2Materials.Potassium, Materials2Materials.Praseodymium,
            Materials2Materials.Promethium, Materials2Materials.PulsatingIron, Materials2Materials.Quantium,
            Materials2Materials.RadoxPoly, Materials2Materials.Realgar, Materials2Materials.RedAlloy,
            Materials2Materials.RedSteel, Materials2Materials.RedstoneAlloy, Materials2Materials.Reinforced,
            Materials2Materials.RoseGold, Materials2Materials.Rubber, Materials2Materials.Rubidium,
            Materials2Materials.Rubracium, Materials2Materials.Samarium, Materials2Materials.SamariumMagnetic,
            Materials2Materials.Scandium, Materials2Materials.Shadow, Materials2Materials.ShadowIron,
            Materials2Materials.ShadowSteel, Materials2Materials.Shijima, Materials2Materials.Silicon,
            Materials2Materials.SiliconSolarGrade, Materials2Materials.Silicone, Materials2Materials.Silver,
            Materials2Materials.SixPhasedCopper, Materials2Materials.SolderingAlloy, Materials2Materials.Soularium,
            Materials2Materials.SpaceTime, Materials2Materials.StainlessSteel, Materials2Materials.Steel,
            Materials2Materials.SteelMagnetic, Materials2Materials.Steeleaf, Materials2Materials.StellarAlloy,
            Materials2Materials.SterlingSilver, Materials2Materials.Strontium,
            Materials2Materials.StyreneButadieneRubber, Materials2Materials.Sunnarium,
            Materials2Materials.SuperconductorUEVBase, Materials2Materials.SuperconductorUIVBase,
            Materials2Materials.SuperconductorUMVBase, Materials2Materials.TPVAlloy, Materials2Materials.Tantalum,
            Materials2Materials.Tartarite, Materials2Materials.Tellurium, Materials2Materials.TengamAttuned,
            Materials2Materials.TengamPurified, Materials2Materials.Terbium, Materials2Materials.Terrasteel,
            Materials2Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            Materials2Materials.Tetranaquadahdiindiumhexaplatiumosminid, Materials2Materials.Thaumium,
            Materials2Materials.Thorium, Materials2Materials.Thulium, Materials2Materials.Tin,
            Materials2Materials.TinAlloy, Materials2Materials.Titanium,
            Materials2Materials.Titaniumonabariumdecacoppereikosaoxid, Materials2Materials.TranscendentMetal,
            Materials2Materials.Trinium, Materials2Materials.Tritanium, Materials2Materials.Tungsten,
            Materials2Materials.TungstenCarbide, Materials2Materials.TungstenSteel, Materials2Materials.Ultimet,
            Materials2Materials.Universium, Materials2Materials.Uranium, Materials2Materials.Uranium235,
            Materials2Materials.Uraniumtriplatinid, Materials2Materials.Vanadium,
            Materials2Materials.VanadiumGallium, Materials2Materials.VanadiumSteel,
            Materials2Materials.Vanadiumtriindinid, Materials2Materials.VibrantAlloy, Materials2Materials.Vinteum,
            Materials2Materials.VividAlloy, Materials2Materials.Void, Materials2Materials.Vulcanite,
            Materials2Materials.Vyroxeres, Materials2Materials.WhiteDwarfMatter, Materials2Materials.Wood,
            Materials2Materials.WoodSealed, Materials2Materials.Ytterbium, Materials2Materials.Yttrium,
            Materials2Materials.YttriumBariumCuprate, Materials2Materials.Zinc, Materials2Materials.exohalkonite,
            Materials2Materials.hotexohalkonite, Materials2Materials.hotprotohalkonite,
            Materials2Materials.prismaticnaquadah, Materials2Materials.protohalkonite };
    }
    // spotless:on

    private static void applyFrames() {
        for (Material material : frameMaterials()) {
            edit(material).generateShape(Materials2PipeShapes.frameGt);
        }
        // HSLA's frame recipe requires RotaryCraft, so it is generated here instead of through frameMaterials().
        if (Mods.RotaryCraft.isModLoaded()) {
            edit(Materials2Materials.HSLA).generateShape(Materials2PipeShapes.frameGt);
        }
    }

    // spotless:off
    /// gtPlusPlus's frame membership: every material reached by `MaterialGenerator.generate`'s solid-state,
    /// non-radioactive gate. `TungstenCarbide` is deliberately absent -- it is built through
    /// `MaterialUtils#generateMaterialFromGtENUM`, which never runs that gate. The entry that suggests
    /// otherwise comes from `Material#getComponentByPrefix` caching a resolved MaterialLib stack after a
    /// `getFrameBox()` recipe read, not from a real frame-block construction.
    public static Material[] gtppFrameMaterials() {
        return new Material[] {
            Materials2Materials.AbyssalAlloy, Materials2Materials.Arcanite, Materials2Materials.ArceusAlloy2B,
            Materials2Materials.AstralTitanium, Materials2Materials.BlackMetal, Materials2Materials.BloodSteel,
            Materials2Materials.Botmium, Materials2Materials.CelestialTungsten, Materials2Materials.ChromaticGlass,
            Materials2Materials.CinobiteA243, Materials2Materials.Dragonblood, Materials2Materials.EglinSteel,
            Materials2Materials.EnergyCrystal, Materials2Materials.Germanium, Materials2Materials.Grisium,
            Materials2Materials.HS188A, Materials2Materials.HastelloyC276, Materials2Materials.HastelloyN,
            Materials2Materials.HastelloyW, Materials2Materials.HastelloyX, Materials2Materials.HeLiCoPtEr,
            Materials2Materials.Hypogen, Materials2Materials.Incoloy020, Materials2Materials.IncoloyDS,
            Materials2Materials.IncoloyMA956, Materials2Materials.Inconel625, Materials2Materials.Inconel690,
            Materials2Materials.Inconel792, Materials2Materials.Iodine, Materials2Materials.LafiumCompound,
            Materials2Materials.Laurenium, Materials2Materials.MaragingSteel250, Materials2Materials.MaragingSteel300,
            Materials2Materials.MaragingSteel350, Materials2Materials.NiobiumCarbide, Materials2Materials.Nitinol60,
            Materials2Materials.Octiron, Materials2Materials.Pikyonium64B, Materials2Materials.Potin,
            Materials2Materials.Quantum, Materials2Materials.Rhenium, Materials2Materials.Selenium,
            Materials2Materials.SiliconCarbide, Materials2Materials.Staballoy, Materials2Materials.Stellite,
            Materials2Materials.Talonite, Materials2Materials.Tantalloy60, Materials2Materials.Tantalloy61,
            Materials2Materials.TantalumCarbide, Materials2Materials.Thallium, Materials2Materials.Titansteel,
            Materials2Materials.TriniumNaquadahCarbonite, Materials2Materials.TriniumTitaniumAlloy, Materials2Materials.Tumbaga,
            Materials2Materials.TungstenTitaniumCarbide, Materials2Materials.WatertightSteel, Materials2Materials.Zeron100,
            Materials2Materials.ZirconiumCarbide };
    }
    // spotless:on

    private static void applyGtppFrame() {
        for (Material material : gtppFrameMaterials()) {
            edit(material).generateShape(Materials2PipeShapes.frameGt);
        }
    }

    // spotless:off
    /// Bartworks-backed frame and sheetmetal membership: every material whose retired
    /// [gregtech.api.material.GTMaterialProperties#WERKSTOFF_PREFIXES] carried a `frameGt` entry also carried a
    /// `sheetmetal` entry -- the two sets are identical for bartworks-backed materials, so one declared list
    /// serves both shapes.
    public static Material[] werkstoffFrameAndSheetmetalMaterials() {
        return new Material[] {
            Materials2Materials.Ruthenium, Materials2Materials.Rhodium, Materials2Materials.RhodiumPlatedPalladium,
            Materials2Materials.Tiberium, Materials2Materials.Ruridit,
            Materials2Materials.HighDurabilityCompoundSteel, Materials2Materials.AdemicSteel,
            Materials2Materials.AtomicSeparationCatalyst, Materials2Materials.ExtremelyUnstableNaquadah,
            Materials2Materials.Zircaloy4, Materials2Materials.Zircaloy2, Materials2Materials.Incoloy903,
            Materials2Materials.AdamantiumAlloy, Materials2Materials.MARM200Steel,
            Materials2Materials.MARCeM200Steel, Materials2Materials.Signalium, Materials2Materials.Lumiium,
            Materials2Materials.ArtheriumSn, Materials2Materials.TanmolyiumBetaC, Materials2Materials.Dalisenite,
            Materials2Materials.Hikarium, Materials2Materials.Tairitsu, Materials2Materials.PreciousMetalsAlloy,
            Materials2Materials.EnrichedNaquadahAlloy, Materials2Materials.MetastableOganesson,
            Materials2Materials.Shirabon, Materials2Materials.Mumetal };
    }
    // spotless:on

    private static void applyWerkstoffFrameAndSheetmetal() {
        for (Material material : werkstoffFrameAndSheetmetalMaterials()) {
            edit(material).generateShape(Materials2PipeShapes.frameGt)
                .generateShape(Materials2BlockShapes.sheetmetal);
        }
    }

    // spotless:off
    /// Native GT sheetmetal membership: every legacy material pinned by the legacy dump's `generatedPrefixes`
    /// ground truth, i.e. every generated legacy material that passed the legacy sheetmetal generation
    /// predicate. Declared rather than derived for the same reason as [#frameMaterials].
    public static Material[] sheetmetalMaterials() {
        return new Material[] {
            Materials2Materials.Lithium, Materials2Materials.Beryllium, Materials2Materials.Magnesium,
            Materials2Materials.Aluminium, Materials2Materials.Silicon, Materials2Materials.Potassium,
            Materials2Materials.Scandium, Materials2Materials.Titanium, Materials2Materials.Vanadium,
            Materials2Materials.Chrome, Materials2Materials.Manganese, Materials2Materials.Iron,
            Materials2Materials.Cobalt, Materials2Materials.Nickel, Materials2Materials.Copper,
            Materials2Materials.Zinc, Materials2Materials.Gallium, Materials2Materials.Arsenic,
            Materials2Materials.Rubidium, Materials2Materials.Strontium, Materials2Materials.Yttrium,
            Materials2Materials.Niobium, Materials2Materials.Molybdenum, Materials2Materials.Palladium,
            Materials2Materials.Silver, Materials2Materials.Indium, Materials2Materials.Tin,
            Materials2Materials.Antimony, Materials2Materials.Tellurium, Materials2Materials.Caesium,
            Materials2Materials.Barium, Materials2Materials.Lanthanum, Materials2Materials.Cerium,
            Materials2Materials.Praseodymium, Materials2Materials.Neodymium, Materials2Materials.Promethium,
            Materials2Materials.Samarium, Materials2Materials.Europium, Materials2Materials.Gadolinium,
            Materials2Materials.Terbium, Materials2Materials.Dysprosium, Materials2Materials.Holmium,
            Materials2Materials.Erbium, Materials2Materials.Thulium, Materials2Materials.Ytterbium,
            Materials2Materials.Lutetium, Materials2Materials.Tantalum, Materials2Materials.Tungsten,
            Materials2Materials.Osmium, Materials2Materials.Iridium, Materials2Materials.Platinum,
            Materials2Materials.Gold, Materials2Materials.Lead, Materials2Materials.Bismuth,
            Materials2Materials.Thorium, Materials2Materials.Uranium235, Materials2Materials.Uranium,
            Materials2Materials.Plutonium, Materials2Materials.Plutonium241, Materials2Materials.Americium,
            Materials2Materials.TengamPurified, Materials2Materials.TengamAttuned,
            Materials2Materials.HellishMetal, Materials2Materials.Neutronium,
            Materials2Materials.SuperconductorUIVBase, Materials2Materials.SuperconductorUMVBase,
            Materials2Materials.SixPhasedCopper, Materials2Materials.Mellion, Materials2Materials.Creon,
            Materials2Materials.prismaticnaquadah, Materials2Materials.Shijima, Materials2Materials.Churitsu,
            Materials2Materials.Manasteel, Materials2Materials.Terrasteel, Materials2Materials.ElvenElementium,
            Materials2Materials.Bronze, Materials2Materials.Brass, Materials2Materials.Invar,
            Materials2Materials.Electrum, Materials2Materials.CastIron, Materials2Materials.Steel,
            Materials2Materials.StainlessSteel, Materials2Materials.PigIron, Materials2Materials.RedAlloy,
            Materials2Materials.BlueAlloy, Materials2Materials.Cupronickel, Materials2Materials.Nichrome,
            Materials2Materials.Kanthal, Materials2Materials.Magnalium, Materials2Materials.SolderingAlloy,
            Materials2Materials.BatteryAlloy, Materials2Materials.TungstenSteel, Materials2Materials.Osmiridium,
            Materials2Materials.Sunnarium, Materials2Materials.Adamantium, Materials2Materials.ElectrumFlux,
            Materials2Materials.Enderium, Materials2Materials.InfusedGold, Materials2Materials.Naquadah,
            Materials2Materials.NaquadahAlloy, Materials2Materials.NaquadahEnriched, Materials2Materials.Naquadria,
            Materials2Materials.Duranium, Materials2Materials.Tritanium, Materials2Materials.Thaumium,
            Materials2Materials.Mithril, Materials2Materials.AstralSilver, Materials2Materials.BlackSteel,
            Materials2Materials.DamascusSteel, Materials2Materials.ShadowIron, Materials2Materials.ShadowSteel,
            Materials2Materials.IronWood, Materials2Materials.Steeleaf, Materials2Materials.MeteoricIron,
            Materials2Materials.MeteoricSteel, Materials2Materials.DarkIron, Materials2Materials.CobaltBrass,
            Materials2Materials.Ultimet, Materials2Materials.AnnealedCopper, Materials2Materials.FierySteel,
            Materials2Materials.RedSteel, Materials2Materials.BlueSteel, Materials2Materials.SterlingSilver,
            Materials2Materials.RoseGold, Materials2Materials.BlackBronze, Materials2Materials.BismuthBronze,
            Materials2Materials.IronMagnetic, Materials2Materials.SteelMagnetic,
            Materials2Materials.NeodymiumMagnetic, Materials2Materials.VanadiumGallium,
            Materials2Materials.YttriumBariumCuprate, Materials2Materials.NiobiumNitride,
            Materials2Materials.NiobiumTitanium, Materials2Materials.ChromiumDioxide,
            Materials2Materials.Knightmetal, Materials2Materials.TinAlloy, Materials2Materials.EnergeticAlloy,
            Materials2Materials.VibrantAlloy, Materials2Materials.Shadow, Materials2Materials.TungstenCarbide,
            Materials2Materials.VanadiumSteel, Materials2Materials.HSSG, Materials2Materials.HSSE,
            Materials2Materials.HSSS, Materials2Materials.Soularium, Materials2Materials.EnderiumBase,
            Materials2Materials.Ardite, Materials2Materials.Reinforced, Materials2Materials.Galgadorian,
            Materials2Materials.EnhancedGalgadorian, Materials2Materials.Manyullyn, Materials2Materials.Mytryl,
            Materials2Materials.BlackPlutonium, Materials2Materials.CallistoIce, Materials2Materials.Ledox,
            Materials2Materials.Quantium, Materials2Materials.Duralumin, Materials2Materials.Oriharukon,
            Materials2Materials.InfinityCatalyst, Materials2Materials.Infinity,
            Materials2Materials.MysteriousCrystal, Materials2Materials.SamariumMagnetic,
            Materials2Materials.Alumite, Materials2Materials.EndSteel, Materials2Materials.CrudeSteel,
            Materials2Materials.CrystallineAlloy, Materials2Materials.MelodicAlloy,
            Materials2Materials.EnergeticSilver, Materials2Materials.VividAlloy, Materials2Materials.Alduorite,
            Materials2Materials.Rubracium, Materials2Materials.Vulcanite, Materials2Materials.Force,
            Materials2Materials.Vinteum, Materials2Materials.TPVAlloy, Materials2Materials.TranscendentMetal,
            Materials2Materials.EnrichedHolmium, Materials2Materials.BlackDwarfMatter,
            Materials2Materials.SpaceTime, Materials2Materials.NickelZincFerrite, Materials2Materials.HeeEndium,
            Materials2Materials.NickelAluminide, Materials2Materials.DeepIron,
            Materials2Materials.SiliconSolarGrade, Materials2Materials.Trinium, Materials2Materials.Desh,
            Materials2Materials.Chrysotile, Materials2Materials.Realgar, Materials2Materials.Vyroxeres,
            Materials2Materials.Ceruclase, Materials2Materials.Tartarite, Materials2Materials.Orichalcum,
            Materials2Materials.Void, Materials2Materials.SuperconductorUEVBase,
            Materials2Materials.BloodInfusedIron, Materials2Materials.GalliumArsenide,
            Materials2Materials.IndiumGalliumPhosphide, Materials2Materials.FleroviumGT5U,
            Materials2Materials.Longasssuperconductornameforuhvwire,
            Materials2Materials.Longasssuperconductornameforuvwire,
            Materials2Materials.Pentacadmiummagnesiumhexaoxid,
            Materials2Materials.Titaniumonabariumdecacoppereikosaoxid, Materials2Materials.Uraniumtriplatinid,
            Materials2Materials.Vanadiumtriindinid,
            Materials2Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            Materials2Materials.Tetranaquadahdiindiumhexaplatiumosminid };
    }
    // spotless:on

    private static void applySheetmetal() {
        for (Material material : sheetmetalMaterials()) {
            edit(material).generateShape(Materials2BlockShapes.sheetmetal);
        }
    }

    private static MaterialEdit edit(Material material) {
        return MaterialLibAPI.editMaterial(material.getModId(), material.getName());
    }

    private Materials2PipeMaterials() {}
}
