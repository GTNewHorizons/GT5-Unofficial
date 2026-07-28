package gregtech.loaders.postload;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;

/// The materials whose legacy meta-item generation included the metal item set (`MU#hasMetalItems`),
/// keyed by their legacy 1000-slot generated-material id, for [PosteaTransformers]' framebox item migration:
/// the legacy per-material framebox items existed exactly for this set, and foreign item ids sharing the
/// framebox damage range (e.g. DEFC overlapping the Bastnasite slot) must not be migrated.
///
/// Rows transcribe the live `METAL` generation flag over the id space at the spine cutover
/// (`scripts/mu/dumps/gt-materials.json` `generationFlags`, cross-checked identical against the
/// `GENERATION_FLAGS` capture in `ml-materials.json`), declared in ascending id order.
public final class LegacyMetalItemsTable {

    private static final boolean[] IDS = build();

    private LegacyMetalItemsTable() {}

    /// Whether a legacy generated-material id belongs to a material that generated metal items.
    public static boolean contains(int id) {
        return id >= 0 && id < IDS.length && IDS[id];
    }

    private static boolean[] build() {
        Material[] rows = { Materials2Materials.Lithium, Materials2Materials.Beryllium, Materials2Materials.Carbon,
            Materials2Materials.Magnesium, Materials2Materials.Aluminium, Materials2Materials.Silicon,
            Materials2Materials.Potassium, Materials2Materials.Scandium, Materials2Materials.Titanium,
            Materials2Materials.Vanadium, Materials2Materials.Chrome, Materials2Materials.Manganese,
            Materials2Materials.Iron, Materials2Materials.Cobalt, Materials2Materials.Nickel,
            Materials2Materials.Copper, Materials2Materials.Zinc, Materials2Materials.Gallium,
            Materials2Materials.Arsenic, Materials2Materials.Rubidium, Materials2Materials.Strontium,
            Materials2Materials.Yttrium, Materials2Materials.Niobium, Materials2Materials.Molybdenum,
            Materials2Materials.Palladium, Materials2Materials.Silver, Materials2Materials.Indium,
            Materials2Materials.Tin, Materials2Materials.Antimony, Materials2Materials.Tellurium,
            Materials2Materials.Caesium, Materials2Materials.Barium, Materials2Materials.Lanthanum,
            Materials2Materials.Cerium, Materials2Materials.Praseodymium, Materials2Materials.Neodymium,
            Materials2Materials.Promethium, Materials2Materials.Samarium, Materials2Materials.Europium,
            Materials2Materials.Gadolinium, Materials2Materials.Terbium, Materials2Materials.Dysprosium,
            Materials2Materials.Holmium, Materials2Materials.Erbium, Materials2Materials.Thulium,
            Materials2Materials.Ytterbium, Materials2Materials.Lutetium, Materials2Materials.Tantalum,
            Materials2Materials.Tungsten, Materials2Materials.Osmium, Materials2Materials.Iridium,
            Materials2Materials.Platinum, Materials2Materials.Gold, Materials2Materials.Lead,
            Materials2Materials.Bismuth, Materials2Materials.Thorium, Materials2Materials.Uranium235,
            Materials2Materials.Uranium, Materials2Materials.Plutonium, Materials2Materials.Plutonium241,
            Materials2Materials.Americium, Materials2Materials.TengamPurified, Materials2Materials.TengamAttuned,
            Materials2Materials.HellishMetal, Materials2Materials.Neutronium, Materials2Materials.SuperconductorUIVBase,
            Materials2Materials.Netherite, Materials2Materials.SuperconductorUMVBase, Materials2Materials.Universium,
            Materials2Materials.Eternity, Materials2Materials.Magmatter, Materials2Materials.SixPhasedCopper,
            Materials2Materials.Mellion, Materials2Materials.Creon, Materials2Materials.hotprotohalkonite,
            Materials2Materials.protohalkonite, Materials2Materials.hotexohalkonite, Materials2Materials.exohalkonite,
            Materials2Materials.prismaticnaquadah, Materials2Materials.Hexanite, Materials2Materials.Shijima,
            Materials2Materials.Churitsu, Materials2Materials.Manasteel, Materials2Materials.Terrasteel,
            Materials2Materials.ElvenElementium, Materials2Materials.GaiaSpirit, Materials2Materials.Livingwood,
            Materials2Materials.Dreamwood, Materials2Materials.Bronze, Materials2Materials.Brass,
            Materials2Materials.Invar, Materials2Materials.Electrum, Materials2Materials.CastIron,
            Materials2Materials.Steel, Materials2Materials.StainlessSteel, Materials2Materials.PigIron,
            Materials2Materials.RedAlloy, Materials2Materials.BlueAlloy, Materials2Materials.Cupronickel,
            Materials2Materials.Nichrome, Materials2Materials.Kanthal, Materials2Materials.Magnalium,
            Materials2Materials.SolderingAlloy, Materials2Materials.BatteryAlloy, Materials2Materials.TungstenSteel,
            Materials2Materials.Osmiridium, Materials2Materials.Sunnarium, Materials2Materials.Adamantium,
            Materials2Materials.ElectrumFlux, Materials2Materials.Enderium, Materials2Materials.HSLA,
            Materials2Materials.InfusedGold, Materials2Materials.Naquadah, Materials2Materials.NaquadahAlloy,
            Materials2Materials.NaquadahEnriched, Materials2Materials.Naquadria, Materials2Materials.Duranium,
            Materials2Materials.Tritanium, Materials2Materials.Thaumium, Materials2Materials.Mithril,
            Materials2Materials.AstralSilver, Materials2Materials.BlackSteel, Materials2Materials.DamascusSteel,
            Materials2Materials.ShadowIron, Materials2Materials.ShadowSteel, Materials2Materials.IronWood,
            Materials2Materials.Steeleaf, Materials2Materials.MeteoricIron, Materials2Materials.MeteoricSteel,
            Materials2Materials.DarkIron, Materials2Materials.CobaltBrass, Materials2Materials.Ultimet,
            Materials2Materials.AnnealedCopper, Materials2Materials.FierySteel, Materials2Materials.RedSteel,
            Materials2Materials.BlueSteel, Materials2Materials.SterlingSilver, Materials2Materials.RoseGold,
            Materials2Materials.BlackBronze, Materials2Materials.BismuthBronze, Materials2Materials.IronMagnetic,
            Materials2Materials.SteelMagnetic, Materials2Materials.NeodymiumMagnetic,
            Materials2Materials.VanadiumGallium, Materials2Materials.YttriumBariumCuprate,
            Materials2Materials.NiobiumNitride, Materials2Materials.NiobiumTitanium,
            Materials2Materials.ChromiumDioxide, Materials2Materials.Knightmetal, Materials2Materials.TinAlloy,
            Materials2Materials.DarkSteel, Materials2Materials.ElectricalSteel, Materials2Materials.EnergeticAlloy,
            Materials2Materials.VibrantAlloy, Materials2Materials.Shadow, Materials2Materials.ConductiveIron,
            Materials2Materials.TungstenCarbide, Materials2Materials.VanadiumSteel, Materials2Materials.HSSG,
            Materials2Materials.HSSE, Materials2Materials.HSSS, Materials2Materials.PulsatingIron,
            Materials2Materials.Soularium, Materials2Materials.EnderiumBase, Materials2Materials.RedstoneAlloy,
            Materials2Materials.Ardite, Materials2Materials.Reinforced, Materials2Materials.Galgadorian,
            Materials2Materials.EnhancedGalgadorian, Materials2Materials.Manyullyn, Materials2Materials.Mytryl,
            Materials2Materials.BlackPlutonium, Materials2Materials.CallistoIce, Materials2Materials.Ledox,
            Materials2Materials.Quantium, Materials2Materials.Duralumin, Materials2Materials.Oriharukon,
            Materials2Materials.InfinityCatalyst, Materials2Materials.Bedrockium, Materials2Materials.Infinity,
            Materials2Materials.MysteriousCrystal, Materials2Materials.SamariumMagnetic, Materials2Materials.Alumite,
            Materials2Materials.EndSteel, Materials2Materials.CrudeSteel, Materials2Materials.CrystallineAlloy,
            Materials2Materials.MelodicAlloy, Materials2Materials.StellarAlloy,
            Materials2Materials.CrystallinePinkSlime, Materials2Materials.EnergeticSilver,
            Materials2Materials.VividAlloy, Materials2Materials.Epoxid, Materials2Materials.Silicone,
            Materials2Materials.Polycaprolactam, Materials2Materials.Polytetrafluoroethylene,
            Materials2Materials.Alduorite, Materials2Materials.Rubracium, Materials2Materials.Vulcanite,
            Materials2Materials.Force, Materials2Materials.Vinteum, Materials2Materials.TPVAlloy,
            Materials2Materials.TranscendentMetal, Materials2Materials.EnrichedHolmium,
            Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2Materials.WhiteDwarfMatter,
            Materials2Materials.BlackDwarfMatter, Materials2Materials.SpaceTime, Materials2Materials.Polybenzimidazole,
            Materials2Materials.EpoxidFiberReinforced, Materials2Materials.BorosilicateGlass,
            Materials2Materials.NickelZincFerrite, Materials2Materials.PolyphenyleneSulfide,
            Materials2Materials.StyreneButadieneRubber, Materials2Materials.Polystyrene,
            Materials2Materials.PolyvinylChloride, Materials2Materials.Kevlar, Materials2Materials.HeeEndium,
            Materials2Materials.NickelAluminide, Materials2Materials.Obsidian, Materials2Materials.Wood,
            Materials2Materials.DeepIron, Materials2Materials.SiliconSolarGrade, Materials2Materials.Trinium,
            Materials2Materials.Plastic, Materials2Materials.Rubber, Materials2Materials.Desh,
            Materials2Materials.WoodSealed, Materials2Materials.Chrysotile, Materials2Materials.Realgar,
            Materials2Materials.Vyroxeres, Materials2Materials.Ceruclase, Materials2Materials.Tartarite,
            Materials2Materials.Orichalcum, Materials2Materials.Void, Materials2Materials.SuperconductorUEVBase,
            Materials2Materials.Draconium, Materials2Materials.DraconiumAwakened, Materials2Materials.BloodInfusedIron,
            Materials2Materials.Ichorium, Materials2Materials.RadoxPoly, Materials2Materials.GalliumArsenide,
            Materials2Materials.IndiumGalliumPhosphide, Materials2Materials.CosmicNeutronium,
            Materials2Materials.FleroviumGT5U, Materials2Materials.Longasssuperconductornameforuhvwire,
            Materials2Materials.Longasssuperconductornameforuvwire, Materials2Materials.Pentacadmiummagnesiumhexaoxid,
            Materials2Materials.Titaniumonabariumdecacoppereikosaoxid, Materials2Materials.Uraniumtriplatinid,
            Materials2Materials.Vanadiumtriindinid,
            Materials2Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            Materials2Materials.Tetranaquadahdiindiumhexaplatiumosminid };

        boolean[] ids = new boolean[1000];
        for (int i = 0; i < rows.length; i++) {
            Material material = rows[i];
            if (material == null) {
                throw new IllegalStateException("Unresolved legacy metal-items row at index " + i);
            }
            int id = MU.oldSubId(material);
            if (id < 0 || id >= ids.length) {
                throw new IllegalStateException("Legacy metal-items row " + material.getName() + " has no legacy id");
            }
            if (ids[id]) {
                throw new IllegalStateException("Duplicate legacy metal-items id " + id);
            }
            ids[id] = true;
        }
        return ids;
    }
}
