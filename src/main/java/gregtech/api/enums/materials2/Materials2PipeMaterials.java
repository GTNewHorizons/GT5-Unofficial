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
/// Rows reference [Materials] and [Materials2Backings] fields, so [#init] must run after those are
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
            new WireCable(Materials.Kanthal, 3, 5, TierEU.HV),
            new WireCable(Materials.Gold, 2, 3, TierEU.HV),
            new WireCable(Materials.Electrum, 1, 2, TierEU.HV),
            new WireCable(Materials.Silver, 1, 1, TierEU.HV),
            new WireCable(Materials.BlueAlloy, 1, 2, TierEU.HV),
            new WireCable(Materials.Nichrome, 4, 6, TierEU.EV),
            new WireCable(Materials.Steel, 3, 2, TierEU.EV),
            new WireCable(Materials.BlackSteel, 1, 4, TierEU.EV),
            new WireCable(Materials.Titanium, 2, 4, TierEU.EV),
            new WireCable(Materials.Aluminium, 1, 1, TierEU.EV),
            new WireCable(Materials.TPVAlloy, 1, 6, TierEU.EV),
            new WireCable(Materials.Platinum, 1, 2, TierEU.IV),
            new WireCable(Materials.TungstenSteel, 4, 4, TierEU.IV),
            new WireCable(Materials.Tungsten, 2, 6, TierEU.IV),
            new WireCable(Materials.Osmium, 2, 4, TierEU.LuV),
            new WireCable(Materials.HSSG, 2, 4, TierEU.LuV),
            new WireCable(Materials.NiobiumTitanium, 2, 4, TierEU.LuV),
            new WireCable(Materials.VanadiumGallium, 4, 4, TierEU.LuV),
            new WireCable(Materials.YttriumBariumCuprate, 3, 6, TierEU.LuV),
            new WireCable(Materials.Naquadah, 2, 2, TierEU.ZPM),
            new WireCable(Materials.Signalium, 8, 12, TierEU.ZPM),
            new WireCable(Materials.NaquadahAlloy, 4, 6, TierEU.UV),
            new WireCable(Materials.Duranium, 2, 4, TierEU.UV),
            new WireCable(Materials.Lumiium, 16, 8, TierEU.UV),
        };

        WireOnly[] wireOnly = {
            new WireOnly(Materials.Graphene, 2, 1, TierEU.IV, false),
            new WireOnly(Materials.Ichorium, 8, 12, TierEU.UHV, false),
            new WireOnly(Materials.Hypogen, 0, 8, TierEU.UIV, false),
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
            new WireOnly(Materials2Backings.SuperconductorMV, 0, 4, TierEU.MV, false),
            new WireOnly(Materials2Backings.SuperconductorHV, 0, 6, TierEU.HV, false),
            new WireOnly(Materials2Backings.SuperconductorEV, 0, 8, TierEU.EV, false),
            new WireOnly(Materials2Backings.SuperconductorIV, 0, 12, TierEU.IV, false),
            new WireOnly(Materials2Backings.SuperconductorLuV, 0, 16, TierEU.LuV, false),
            new WireOnly(Materials2Backings.SuperconductorZPM, 0, 24, TierEU.ZPM, false),
            new WireOnly(Materials2Backings.SuperconductorUV, 0, 32, TierEU.UV, false),
            new WireOnly(Materials2Backings.SuperconductorUHV, 0, 48, TierEU.UHV, false),
            new WireOnly(Materials2Backings.SuperconductorUEV, 0, 64, TierEU.UEV, false),
            new WireOnly(Materials2Backings.SuperconductorUIV, 0, 64, TierEU.UIV, false),
            new WireOnly(Materials2Backings.SuperconductorUMV, 0, 64, TierEU.UMV, false),
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
        edit(Materials.RedAlloy).setProperty(Materials2PipeProperties.WIRE_LOSS, 1);
        edit(Materials.RedstoneAlloy).setProperty(Materials2PipeProperties.WIRE_LOSS, 2);
        edit(Materials.Signalium).setProperty(Materials2PipeProperties.WIRE_LOSS, 32);
        edit(Materials.Lumiium).setProperty(Materials2PipeProperties.WIRE_LOSS, 64);

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

    /// The wire sizes this table grants as one unit: a material carrying any of them carries all six. Public
    /// so a consumer can state that unit as its own precondition instead of re-listing the sizes.
    public static Shape[] wireShapes() {
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
            MaterialEdit edit = edit(row.material())
                .setProperty(Materials2PipeProperties.BASE_PIPE_FLOW, row.baseCapacity())
                .setProperty(Materials2PipeProperties.PIPE_HEAT_RESISTANCE, row.heatResistance());
            // These two rows exist only with their parent mod (Void pipes with Thaumcraft, DarkSteel with
            // EnderIO); the properties themselves stay unconditional.
            if (row.material() == Materials.Void && !Mods.Thaumcraft.isModLoaded()) continue;
            if (row.material() == Materials.DarkSteel && !Mods.EnderIO.isModLoaded()) continue;
            edit.generateShapes(
                Materials2PipeShapes.pipeTiny,
                Materials2PipeShapes.pipeSmall,
                Materials2PipeShapes.pipeMedium,
                Materials2PipeShapes.pipeLarge,
                Materials2PipeShapes.pipeHuge,
                Materials2PipeShapes.pipeQuadruple,
                Materials2PipeShapes.pipeNonuple);
        }

        edit(Materials.Wood)
            .setProperty(Materials2PipeProperties.PIPE_HEAT_RESISTANCE, WOOD_FLUID_PIPE_HEAT_RESISTANCE)
            .setProperty(Materials2PipeProperties.PIPE_GAS_PROOF, false)
            .generateShapes(
                Materials2PipeShapes.pipeSmall,
                Materials2PipeShapes.pipeMedium,
                Materials2PipeShapes.pipeLarge);
        edit(Materials.Clay).setProperty(Materials2PipeProperties.PIPE_GAS_PROOF, false);
        edit(Materials.Redstone)
            .setProperty(Materials2PipeProperties.PIPE_HEAT_RESISTANCE, HIGH_PRESSURE_FLUID_PIPE_HEAT_RESISTANCE)
            .generateShapes(
                Materials2PipeShapes.pipeSmall,
                Materials2PipeShapes.pipeMedium,
                Materials2PipeShapes.pipeLarge);
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
            Materials.Adamantium, Materials.Alduorite, Materials.Aluminium,
            Materials.Alumite, Materials.Americium, Materials.AnnealedCopper,
            Materials.Antimony, Materials.Ardite, Materials.Arsenic,
            Materials.AstralSilver, Materials.Barium, Materials.BatteryAlloy,
            Materials.Bedrockium, Materials.Beryllium, Materials.Bismuth,
            Materials.BismuthBronze, Materials.BlackBronze,
            Materials.BlackDwarfMatter, Materials.BlackPlutonium,
            Materials.BlackSteel, Materials.BloodInfusedIron, Materials.BlueAlloy,
            Materials.BlueSteel, Materials.BorosilicateGlass, Materials.Brass,
            Materials.Bronze, Materials.Caesium, Materials.CallistoIce,
            Materials.Carbon, Materials.CastIron, Materials.Cerium,
            Materials.Ceruclase, Materials.Chrome, Materials.ChromiumDioxide,
            Materials.Chrysotile, Materials.Churitsu, Materials.Cobalt,
            Materials.CobaltBrass, Materials.ConductiveIron, Materials.Copper,
            Materials.CosmicNeutronium, Materials.Creon, Materials.CrudeSteel,
            Materials.CrystallineAlloy, Materials.CrystallinePinkSlime,
            Materials.Cupronickel, Materials.DamascusSteel, Materials.DarkIron,
            Materials.DarkSteel, Materials.DeepIron, Materials.Desh,
            Materials.Draconium, Materials.DraconiumAwakened, Materials.Dreamwood,
            Materials.Duralumin, Materials.Duranium, Materials.Dysprosium,
            Materials.ElectricalSteel, Materials.Electrum, Materials.ElectrumFlux,
            Materials.ElvenElementium, Materials.EndSteel, Materials.Enderium,
            Materials.EnderiumBase, Materials.EnergeticAlloy,
            Materials.EnergeticSilver, Materials.EnhancedGalgadorian,
            Materials.EnrichedHolmium, Materials.Epoxid,
            Materials.EpoxidFiberReinforced, Materials.Erbium, Materials.Eternity,
            Materials.Europium, Materials.FierySteel, Materials.FleroviumGT5U,
            Materials.Force, Materials.Gadolinium, Materials.GaiaSpirit,
            Materials.Galgadorian, Materials.Gallium, Materials.GalliumArsenide,
            Materials.Gold, Materials.HSSE, Materials.HSSG,
            Materials.HSSS, Materials.HeeEndium, Materials.HellishMetal,
            Materials.Hexanite, Materials.Holmium, Materials.Ichorium,
            Materials.Indium, Materials.IndiumGalliumPhosphide, Materials.Infinity,
            Materials.InfinityCatalyst, Materials.InfusedGold, Materials.Invar,
            Materials.Iridium, Materials.Iron, Materials.IronMagnetic,
            Materials.IronWood, Materials.Kanthal, Materials.Kevlar,
            Materials.Knightmetal, Materials.Lanthanum, Materials.Lead,
            Materials.Ledox, Materials.Lithium, Materials.Livingwood,
            Materials.Longasssuperconductornameforuhvwire,
            Materials.Longasssuperconductornameforuvwire, Materials.Lutetium,
            Materials.Magmatter, Materials.Magnalium, Materials.Magnesium,
            Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials.Manasteel,
            Materials.Manganese, Materials.Manyullyn, Materials.Mellion,
            Materials.MelodicAlloy, Materials.MeteoricIron, Materials.MeteoricSteel,
            Materials.Mithril, Materials.Molybdenum, Materials.MysteriousCrystal,
            Materials.Mytryl, Materials.Naquadah, Materials.NaquadahAlloy,
            Materials.NaquadahEnriched, Materials.Naquadria, Materials.Neodymium,
            Materials.NeodymiumMagnetic, Materials.Netherite, Materials.Neutronium,
            Materials.Nichrome, Materials.Nickel, Materials.NickelAluminide,
            Materials.NickelZincFerrite, Materials.Niobium, Materials.NiobiumNitride,
            Materials.NiobiumTitanium, Materials.Obsidian, Materials.Orichalcum,
            Materials.Oriharukon, Materials.Osmiridium, Materials.Osmium,
            Materials.Palladium, Materials.Pentacadmiummagnesiumhexaoxid,
            Materials.PigIron, Materials.Plastic, Materials.Platinum,
            Materials.Plutonium, Materials.Plutonium241, Materials.Polybenzimidazole,
            Materials.Polycaprolactam, Materials.PolyphenyleneSulfide,
            Materials.Polystyrene, Materials.Polytetrafluoroethylene,
            Materials.PolyvinylChloride, Materials.Potassium, Materials.Praseodymium,
            Materials.Promethium, Materials.PulsatingIron, Materials.Quantium,
            Materials.RadoxPoly, Materials.Realgar, Materials.RedAlloy,
            Materials.RedSteel, Materials.RedstoneAlloy, Materials.Reinforced,
            Materials.RoseGold, Materials.Rubber, Materials.Rubidium,
            Materials.Rubracium, Materials.Samarium, Materials.SamariumMagnetic,
            Materials.Scandium, Materials.Shadow, Materials.ShadowIron,
            Materials.ShadowSteel, Materials.Shijima, Materials.Silicon,
            Materials.SiliconSolarGrade, Materials.Silicone, Materials.Silver,
            Materials.SixPhasedCopper, Materials.SolderingAlloy, Materials.Soularium,
            Materials.SpaceTime, Materials.StainlessSteel, Materials.Steel,
            Materials.SteelMagnetic, Materials.Steeleaf, Materials.StellarAlloy,
            Materials.SterlingSilver, Materials.Strontium,
            Materials.StyreneButadieneRubber, Materials.Sunnarium,
            Materials.SuperconductorUEVBase, Materials.SuperconductorUIVBase,
            Materials.SuperconductorUMVBase, Materials.TPVAlloy, Materials.Tantalum,
            Materials.Tartarite, Materials.Tellurium, Materials.TengamAttuned,
            Materials.TengamPurified, Materials.Terbium, Materials.Terrasteel,
            Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            Materials.Tetranaquadahdiindiumhexaplatiumosminid, Materials.Thaumium,
            Materials.Thorium, Materials.Thulium, Materials.Tin,
            Materials.TinAlloy, Materials.Titanium,
            Materials.Titaniumonabariumdecacoppereikosaoxid, Materials.TranscendentMetal,
            Materials.Trinium, Materials.Tritanium, Materials.Tungsten,
            Materials.TungstenCarbide, Materials.TungstenSteel, Materials.Ultimet,
            Materials.Universium, Materials.Uranium, Materials.Uranium235,
            Materials.Uraniumtriplatinid, Materials.Vanadium,
            Materials.VanadiumGallium, Materials.VanadiumSteel,
            Materials.Vanadiumtriindinid, Materials.VibrantAlloy, Materials.Vinteum,
            Materials.VividAlloy, Materials.Void, Materials.Vulcanite,
            Materials.Vyroxeres, Materials.WhiteDwarfMatter, Materials.Wood,
            Materials.WoodSealed, Materials.Ytterbium, Materials.Yttrium,
            Materials.YttriumBariumCuprate, Materials.Zinc, Materials.exohalkonite,
            Materials.hotexohalkonite, Materials.hotprotohalkonite,
            Materials.prismaticnaquadah, Materials.protohalkonite };
    }
    // spotless:on

    private static void applyFrames() {
        for (Material material : frameMaterials()) {
            edit(material).generateShape(Materials2PipeShapes.frameGt);
        }
        // HSLA's frame recipe requires RotaryCraft, so it is generated here instead of through frameMaterials().
        if (Mods.RotaryCraft.isModLoaded()) {
            edit(Materials.HSLA).generateShape(Materials2PipeShapes.frameGt);
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
            Materials.AbyssalAlloy, Materials.Arcanite, Materials.ArceusAlloy2B,
            Materials.AstralTitanium, Materials.BlackMetal, Materials.BloodSteel,
            Materials.Botmium, Materials.CelestialTungsten, Materials.ChromaticGlass,
            Materials.CinobiteA243, Materials.Dragonblood, Materials.EglinSteel,
            Materials.EnergyCrystal, Materials.Germanium, Materials.Grisium,
            Materials.HS188A, Materials.HastelloyC276, Materials.HastelloyN,
            Materials.HastelloyW, Materials.HastelloyX, Materials.HeLiCoPtEr,
            Materials.Hypogen, Materials.Incoloy020, Materials.IncoloyDS,
            Materials.IncoloyMA956, Materials.Inconel625, Materials.Inconel690,
            Materials.Inconel792, Materials.Iodine, Materials.LafiumCompound,
            Materials.Laurenium, Materials.MaragingSteel250, Materials.MaragingSteel300,
            Materials.MaragingSteel350, Materials.NiobiumCarbide, Materials.Nitinol60,
            Materials.Octiron, Materials.Pikyonium64B, Materials.Potin,
            Materials.Quantum, Materials.Rhenium, Materials.Selenium,
            Materials.SiliconCarbide, Materials.Staballoy, Materials.Stellite,
            Materials.Talonite, Materials.Tantalloy60, Materials.Tantalloy61,
            Materials.TantalumCarbide, Materials.Thallium, Materials.Titansteel,
            Materials.TriniumNaquadahCarbonite, Materials.TriniumTitaniumAlloy, Materials.Tumbaga,
            Materials.TungstenTitaniumCarbide, Materials.WatertightSteel, Materials.Zeron100,
            Materials.ZirconiumCarbide };
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
            Materials.Ruthenium, Materials.Rhodium, Materials.RhodiumPlatedPalladium,
            Materials.Tiberium, Materials.Ruridit,
            Materials.HighDurabilityCompoundSteel, Materials.AdemicSteel,
            Materials.AtomicSeparationCatalyst, Materials.ExtremelyUnstableNaquadah,
            Materials.Zircaloy4, Materials.Zircaloy2, Materials.Incoloy903,
            Materials.AdamantiumAlloy, Materials.MARM200Steel,
            Materials.MARCeM200Steel, Materials.Signalium, Materials.Lumiium,
            Materials.ArtheriumSn, Materials.TanmolyiumBetaC, Materials.Dalisenite,
            Materials.Hikarium, Materials.Tairitsu, Materials.PreciousMetalsAlloy,
            Materials.EnrichedNaquadahAlloy, Materials.MetastableOganesson,
            Materials.Shirabon, Materials.Mumetal };
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
            Materials.Lithium, Materials.Beryllium, Materials.Magnesium,
            Materials.Aluminium, Materials.Silicon, Materials.Potassium,
            Materials.Scandium, Materials.Titanium, Materials.Vanadium,
            Materials.Chrome, Materials.Manganese, Materials.Iron,
            Materials.Cobalt, Materials.Nickel, Materials.Copper,
            Materials.Zinc, Materials.Gallium, Materials.Arsenic,
            Materials.Rubidium, Materials.Strontium, Materials.Yttrium,
            Materials.Niobium, Materials.Molybdenum, Materials.Palladium,
            Materials.Silver, Materials.Indium, Materials.Tin,
            Materials.Antimony, Materials.Tellurium, Materials.Caesium,
            Materials.Barium, Materials.Lanthanum, Materials.Cerium,
            Materials.Praseodymium, Materials.Neodymium, Materials.Promethium,
            Materials.Samarium, Materials.Europium, Materials.Gadolinium,
            Materials.Terbium, Materials.Dysprosium, Materials.Holmium,
            Materials.Erbium, Materials.Thulium, Materials.Ytterbium,
            Materials.Lutetium, Materials.Tantalum, Materials.Tungsten,
            Materials.Osmium, Materials.Iridium, Materials.Platinum,
            Materials.Gold, Materials.Lead, Materials.Bismuth,
            Materials.Thorium, Materials.Uranium235, Materials.Uranium,
            Materials.Plutonium, Materials.Plutonium241, Materials.Americium,
            Materials.TengamPurified, Materials.TengamAttuned,
            Materials.HellishMetal, Materials.Neutronium,
            Materials.SuperconductorUIVBase, Materials.SuperconductorUMVBase,
            Materials.SixPhasedCopper, Materials.Mellion, Materials.Creon,
            Materials.prismaticnaquadah, Materials.Shijima, Materials.Churitsu,
            Materials.Manasteel, Materials.Terrasteel, Materials.ElvenElementium,
            Materials.Bronze, Materials.Brass, Materials.Invar,
            Materials.Electrum, Materials.CastIron, Materials.Steel,
            Materials.StainlessSteel, Materials.PigIron, Materials.RedAlloy,
            Materials.BlueAlloy, Materials.Cupronickel, Materials.Nichrome,
            Materials.Kanthal, Materials.Magnalium, Materials.SolderingAlloy,
            Materials.BatteryAlloy, Materials.TungstenSteel, Materials.Osmiridium,
            Materials.Sunnarium, Materials.Adamantium, Materials.ElectrumFlux,
            Materials.Enderium, Materials.InfusedGold, Materials.Naquadah,
            Materials.NaquadahAlloy, Materials.NaquadahEnriched, Materials.Naquadria,
            Materials.Duranium, Materials.Tritanium, Materials.Thaumium,
            Materials.Mithril, Materials.AstralSilver, Materials.BlackSteel,
            Materials.DamascusSteel, Materials.ShadowIron, Materials.ShadowSteel,
            Materials.IronWood, Materials.Steeleaf, Materials.MeteoricIron,
            Materials.MeteoricSteel, Materials.DarkIron, Materials.CobaltBrass,
            Materials.Ultimet, Materials.AnnealedCopper, Materials.FierySteel,
            Materials.RedSteel, Materials.BlueSteel, Materials.SterlingSilver,
            Materials.RoseGold, Materials.BlackBronze, Materials.BismuthBronze,
            Materials.IronMagnetic, Materials.SteelMagnetic,
            Materials.NeodymiumMagnetic, Materials.VanadiumGallium,
            Materials.YttriumBariumCuprate, Materials.NiobiumNitride,
            Materials.NiobiumTitanium, Materials.ChromiumDioxide,
            Materials.Knightmetal, Materials.TinAlloy, Materials.EnergeticAlloy,
            Materials.VibrantAlloy, Materials.Shadow, Materials.TungstenCarbide,
            Materials.VanadiumSteel, Materials.HSSG, Materials.HSSE,
            Materials.HSSS, Materials.Soularium, Materials.EnderiumBase,
            Materials.Ardite, Materials.Reinforced, Materials.Galgadorian,
            Materials.EnhancedGalgadorian, Materials.Manyullyn, Materials.Mytryl,
            Materials.BlackPlutonium, Materials.CallistoIce, Materials.Ledox,
            Materials.Quantium, Materials.Duralumin, Materials.Oriharukon,
            Materials.InfinityCatalyst, Materials.Infinity,
            Materials.MysteriousCrystal, Materials.SamariumMagnetic,
            Materials.Alumite, Materials.EndSteel, Materials.CrudeSteel,
            Materials.CrystallineAlloy, Materials.MelodicAlloy,
            Materials.EnergeticSilver, Materials.VividAlloy, Materials.Alduorite,
            Materials.Rubracium, Materials.Vulcanite, Materials.Force,
            Materials.Vinteum, Materials.TPVAlloy, Materials.TranscendentMetal,
            Materials.EnrichedHolmium, Materials.BlackDwarfMatter,
            Materials.SpaceTime, Materials.NickelZincFerrite, Materials.HeeEndium,
            Materials.NickelAluminide, Materials.DeepIron,
            Materials.SiliconSolarGrade, Materials.Trinium, Materials.Desh,
            Materials.Chrysotile, Materials.Realgar, Materials.Vyroxeres,
            Materials.Ceruclase, Materials.Tartarite, Materials.Orichalcum,
            Materials.Void, Materials.SuperconductorUEVBase,
            Materials.BloodInfusedIron, Materials.GalliumArsenide,
            Materials.IndiumGalliumPhosphide, Materials.FleroviumGT5U,
            Materials.Longasssuperconductornameforuhvwire,
            Materials.Longasssuperconductornameforuvwire,
            Materials.Pentacadmiummagnesiumhexaoxid,
            Materials.Titaniumonabariumdecacoppereikosaoxid, Materials.Uraniumtriplatinid,
            Materials.Vanadiumtriindinid,
            Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            Materials.Tetranaquadahdiindiumhexaplatiumosminid };
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
