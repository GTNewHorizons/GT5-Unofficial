package gregtech.loaders.postload;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials2.Materials;
import gregtech.api.material.MaterialUtils;

/// The materials whose legacy meta-item generation included the metal item set (`MaterialUtils#generates` `METAL`),
/// keyed by their legacy 1000-slot generated-material id, for [PosteaTransformers]' framebox item migration:
/// the legacy per-material framebox items existed exactly for this set, and foreign item ids sharing the
/// framebox damage range (e.g. DEFC overlapping the Bastnasite slot) must not be migrated.
///
/// Rows are the materials whose [gregtech.api.material.GTMaterialProperties#GENERATION_FLAGS] carry `METAL`,
/// declared in ascending id order.
public final class LegacyMetalItemsTable {

    private static final boolean[] IDS = build();

    private LegacyMetalItemsTable() {}

    /// Whether a legacy generated-material id belongs to a material that generated metal items.
    public static boolean contains(int id) {
        return id >= 0 && id < IDS.length && IDS[id];
    }

    private static boolean[] build() {
        Material[] rows = { Materials.Lithium, Materials.Beryllium, Materials.Carbon, Materials.Magnesium,
            Materials.Aluminium, Materials.Silicon, Materials.Potassium, Materials.Scandium, Materials.Titanium,
            Materials.Vanadium, Materials.Chrome, Materials.Manganese, Materials.Iron, Materials.Cobalt,
            Materials.Nickel, Materials.Copper, Materials.Zinc, Materials.Gallium, Materials.Arsenic,
            Materials.Rubidium, Materials.Strontium, Materials.Yttrium, Materials.Niobium, Materials.Molybdenum,
            Materials.Palladium, Materials.Silver, Materials.Indium, Materials.Tin, Materials.Antimony,
            Materials.Tellurium, Materials.Caesium, Materials.Barium, Materials.Lanthanum, Materials.Cerium,
            Materials.Praseodymium, Materials.Neodymium, Materials.Promethium, Materials.Samarium, Materials.Europium,
            Materials.Gadolinium, Materials.Terbium, Materials.Dysprosium, Materials.Holmium, Materials.Erbium,
            Materials.Thulium, Materials.Ytterbium, Materials.Lutetium, Materials.Tantalum, Materials.Tungsten,
            Materials.Osmium, Materials.Iridium, Materials.Platinum, Materials.Gold, Materials.Lead, Materials.Bismuth,
            Materials.Thorium, Materials.Uranium235, Materials.Uranium, Materials.Plutonium, Materials.Plutonium241,
            Materials.Americium, Materials.TengamPurified, Materials.TengamAttuned, Materials.HellishMetal,
            Materials.Neutronium, Materials.SuperconductorUIVBase, Materials.Netherite, Materials.SuperconductorUMVBase,
            Materials.Universium, Materials.Eternity, Materials.Magmatter, Materials.SixPhasedCopper, Materials.Mellion,
            Materials.Creon, Materials.hotprotohalkonite, Materials.protohalkonite, Materials.hotexohalkonite,
            Materials.exohalkonite, Materials.prismaticnaquadah, Materials.Hexanite, Materials.Shijima,
            Materials.Churitsu, Materials.Manasteel, Materials.Terrasteel, Materials.ElvenElementium,
            Materials.GaiaSpirit, Materials.Livingwood, Materials.Dreamwood, Materials.Bronze, Materials.Brass,
            Materials.Invar, Materials.Electrum, Materials.CastIron, Materials.Steel, Materials.StainlessSteel,
            Materials.PigIron, Materials.RedAlloy, Materials.BlueAlloy, Materials.Cupronickel, Materials.Nichrome,
            Materials.Kanthal, Materials.Magnalium, Materials.SolderingAlloy, Materials.BatteryAlloy,
            Materials.TungstenSteel, Materials.Osmiridium, Materials.Sunnarium, Materials.Adamantium,
            Materials.ElectrumFlux, Materials.Enderium, Materials.HSLA, Materials.InfusedGold, Materials.Naquadah,
            Materials.NaquadahAlloy, Materials.NaquadahEnriched, Materials.Naquadria, Materials.Duranium,
            Materials.Tritanium, Materials.Thaumium, Materials.Mithril, Materials.AstralSilver, Materials.BlackSteel,
            Materials.DamascusSteel, Materials.ShadowIron, Materials.ShadowSteel, Materials.IronWood,
            Materials.Steeleaf, Materials.MeteoricIron, Materials.MeteoricSteel, Materials.DarkIron,
            Materials.CobaltBrass, Materials.Ultimet, Materials.AnnealedCopper, Materials.FierySteel,
            Materials.RedSteel, Materials.BlueSteel, Materials.SterlingSilver, Materials.RoseGold,
            Materials.BlackBronze, Materials.BismuthBronze, Materials.IronMagnetic, Materials.SteelMagnetic,
            Materials.NeodymiumMagnetic, Materials.VanadiumGallium, Materials.YttriumBariumCuprate,
            Materials.NiobiumNitride, Materials.NiobiumTitanium, Materials.ChromiumDioxide, Materials.Knightmetal,
            Materials.TinAlloy, Materials.DarkSteel, Materials.ElectricalSteel, Materials.EnergeticAlloy,
            Materials.VibrantAlloy, Materials.Shadow, Materials.ConductiveIron, Materials.TungstenCarbide,
            Materials.VanadiumSteel, Materials.HSSG, Materials.HSSE, Materials.HSSS, Materials.PulsatingIron,
            Materials.Soularium, Materials.EnderiumBase, Materials.RedstoneAlloy, Materials.Ardite,
            Materials.Reinforced, Materials.Galgadorian, Materials.EnhancedGalgadorian, Materials.Manyullyn,
            Materials.Mytryl, Materials.BlackPlutonium, Materials.CallistoIce, Materials.Ledox, Materials.Quantium,
            Materials.Duralumin, Materials.Oriharukon, Materials.InfinityCatalyst, Materials.Bedrockium,
            Materials.Infinity, Materials.MysteriousCrystal, Materials.SamariumMagnetic, Materials.Alumite,
            Materials.EndSteel, Materials.CrudeSteel, Materials.CrystallineAlloy, Materials.MelodicAlloy,
            Materials.StellarAlloy, Materials.CrystallinePinkSlime, Materials.EnergeticSilver, Materials.VividAlloy,
            Materials.Epoxid, Materials.Silicone, Materials.Polycaprolactam, Materials.Polytetrafluoroethylene,
            Materials.Alduorite, Materials.Rubracium, Materials.Vulcanite, Materials.Force, Materials.Vinteum,
            Materials.TPVAlloy, Materials.TranscendentMetal, Materials.EnrichedHolmium,
            Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials.WhiteDwarfMatter,
            Materials.BlackDwarfMatter, Materials.SpaceTime, Materials.Polybenzimidazole,
            Materials.EpoxidFiberReinforced, Materials.BorosilicateGlass, Materials.NickelZincFerrite,
            Materials.PolyphenyleneSulfide, Materials.StyreneButadieneRubber, Materials.Polystyrene,
            Materials.PolyvinylChloride, Materials.Kevlar, Materials.HeeEndium, Materials.NickelAluminide,
            Materials.Obsidian, Materials.Wood, Materials.DeepIron, Materials.SiliconSolarGrade, Materials.Trinium,
            Materials.Plastic, Materials.Rubber, Materials.Desh, Materials.WoodSealed, Materials.Chrysotile,
            Materials.Realgar, Materials.Vyroxeres, Materials.Ceruclase, Materials.Tartarite, Materials.Orichalcum,
            Materials.Void, Materials.SuperconductorUEVBase, Materials.Draconium, Materials.DraconiumAwakened,
            Materials.BloodInfusedIron, Materials.Ichorium, Materials.RadoxPoly, Materials.GalliumArsenide,
            Materials.IndiumGalliumPhosphide, Materials.CosmicNeutronium, Materials.FleroviumGT5U,
            Materials.Longasssuperconductornameforuhvwire, Materials.Longasssuperconductornameforuvwire,
            Materials.Pentacadmiummagnesiumhexaoxid, Materials.Titaniumonabariumdecacoppereikosaoxid,
            Materials.Uraniumtriplatinid, Materials.Vanadiumtriindinid,
            Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            Materials.Tetranaquadahdiindiumhexaplatiumosminid };

        boolean[] ids = new boolean[1000];
        for (int i = 0; i < rows.length; i++) {
            Material material = rows[i];
            if (material == null) {
                throw new IllegalStateException("Unresolved legacy metal-items row at index " + i);
            }
            int id = MaterialUtils.oldSubId(material);
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
