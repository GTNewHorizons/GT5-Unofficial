package gregtech.loaders.materials;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.material.MarkerMaterial;

/// Recognition entries: names that generate no items of their own but must exist so the rest of the code can
/// name, identity-match, or associate composition against them.
///
/// - Name resolution: `gregtech.common.GTProxy#registerOre` resolves every ore-dictionary registration another
/// mod fires against these by name, so a foreign entry such as `gemCitrine`, `oreFluorite`, or `dustAgate`
/// unifies against one of these instead of falling through unrecognized.
/// - Ore-unification identity: the same handler compares the resolved material by identity to steer specific
/// ore-dictionary families.
/// - Composition association: `gregtech.loaders.preload.LoaderGTItemData` references a few of these as the
/// material of an `ItemData` (for example `Materials.Sand` for sand and sandstone recycling); `ItemData` and
/// `MaterialStack` accept any `IOreMaterial`.
///
/// Most entries are [MarkerMaterial]s: each holds only a name, display name, [TextureSet], packed ARGB tint,
/// and whether an entry named for it unifies, and carries no composition. Because a marker is not a `Materials`
/// and therefore absent from `MATERIALS_MAP`, `GTProxy#registerOre` consults [#getMarker] as a fallback when
/// `Materials#get` misses. The RGBA and texture set reproduce the values a marker's consumers observe (for
/// example a bee comb reads its tint through `CombType`).
///
/// `Quartz` is a marker like the rest, but carries [SubTag]s (`CRYSTAL`, `CRYSTALLISABLE`, `NO_SMASHING`,
/// `NO_SMELTING`, `QUARTZ`) and is steered by name in `GTProxy#registerRecognitionOre`, which reproduces its
/// `crystal`/`craftingQuartz` cross-registrations.
///
/// `Fluix` is still a full `Materials`: a foreign `gem`/`crystal` entry named for it flows through
/// `OrePrefixes#processOre` and the `Materials`-typed ore-processing pipeline to generate its crystal and
/// dust recipes, which requires a real `Materials` in `MATERIALS_MAP`.
public class RecognitionMaterials {

    private static final int DEFAULT_ARGB = 0x00ffffff;
    private static final SubTag[] NO_SUBTAGS = {};

    private static final Map<String, MarkerMaterial> MARKERS_BY_NAME = new LinkedHashMap<>();

    private RecognitionMaterials() {}

    /// The [MarkerMaterial] whose internal name is `name`, or null if no recognition marker claims that name.
    public static MarkerMaterial getMarker(String name) {
        return MARKERS_BY_NAME.get(name);
    }

    public static void load() {
        Materials.Fluix = new MaterialBuilder().setName("Fluix")
            .setDefaultLocalName("Fluix")
            .addDustItems()
            .addGemItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .constructMaterial();

        for (Marker marker : MARKERS) {
            MarkerMaterial material = new MarkerMaterial(
                marker.name(),
                marker.localName(),
                marker.textureSet(),
                marker.argb(),
                marker.unifiable());
            material.add(marker.subTags());
            MARKERS_BY_NAME.put(marker.name(), material);
            marker.field()
                .accept(material);
        }
    }

    private record Marker(Consumer<MarkerMaterial> field, String name, String localName, TextureSet textureSet,
        int argb, boolean unifiable, SubTag[] subTags) {}

    private static Marker marker(Consumer<MarkerMaterial> field, String name, String localName) {
        return new Marker(field, name, localName, TextureSet.SET_NONE, DEFAULT_ARGB, true, NO_SUBTAGS);
    }

    private static Marker marker(Consumer<MarkerMaterial> field, String name, String localName, TextureSet textureSet,
        int argb, boolean unifiable) {
        return new Marker(field, name, localName, textureSet, argb, unifiable, NO_SUBTAGS);
    }

    private static Marker marker(Consumer<MarkerMaterial> field, String name, String localName, TextureSet textureSet,
        int argb, boolean unifiable, SubTag... subTags) {
        return new Marker(field, name, localName, textureSet, argb, unifiable, subTags);
    }

    // spotless:off
    private static final Marker[] MARKERS = {
        marker(m -> Materials.Adamite = m, "Adamite", "Adamite"),
        marker(m -> Materials.Agate = m, "Agate", "Agate"),
        marker(m -> Materials.Alfium = m, "Alfium", "Alfium"),
        marker(m -> Materials.Ammonium = m, "Ammonium", "Ammonium"),
        marker(m -> Materials.AncientDebris = m, "Debris", "Ancient Debris", TextureSet.SET_NONE, 0x00351a0b, true),
        marker(m -> Materials.Andesite = m, "Andesite", "Andesite"),
        marker(m -> Materials.Aquamarine = m, "Aquamarine", "Aquamarine"),
        marker(m -> Materials.Bitumen = m, "Bitumen", "Bitumen"),
        marker(m -> Materials.Black = m, "Black", "Black", TextureSet.SET_NONE, 0x00000000, true),
        marker(m -> Materials.Bloodstone = m, "Bloodstone", "Bloodstone"),
        marker(m -> Materials.BrickNether = m, "BrickNether", "BrickNether", TextureSet.SET_DULL, DEFAULT_ARGB, false),
        marker(m -> Materials.Chimerite = m, "Chimerite", "Chimerite"),
        marker(m -> Materials.Chrysocolla = m, "Chrysocolla", "Chrysocolla"),
        marker(m -> Materials.Citrine = m, "Citrine", "Citrine"),
        marker(m -> Materials.Cluster = m, "Cluster", "Cluster", TextureSet.SET_NONE, 0x7fffffff, true),
        marker(m -> Materials.Cobblestone = m, "Cobblestone", "Cobblestone", TextureSet.SET_DULL, DEFAULT_ARGB, false),
        marker(m -> Materials.Coral = m, "Coral", "Coral", TextureSet.SET_NONE, 0x00ff80ff, true),
        marker(m -> Materials.Crystal = m, "Crystal", "Crystal", TextureSet.SET_SHINY, DEFAULT_ARGB, false),
        marker(m -> Materials.CrystalFlux = m, "CrystalFlux", "Flux Crystal", TextureSet.SET_QUARTZ, 0x00643264, true),
        marker(m -> Materials.Cyanite = m, "Cyanite", "Cyanite"),
        marker(m -> Materials.DarkStone = m, "DarkStone", "Dark Stone"),
        marker(m -> Materials.Demonite = m, "Demonite", "Demonite"),
        marker(m -> Materials.Draconic = m, "Draconic", "Draconic"),
        marker(m -> Materials.Drulloy = m, "Drulloy", "Drulloy"),
        marker(m -> Materials.Ender = m, "Ender", "Ender"),
        marker(m -> Materials.Energized = m, "Energized", "Energized"),
        marker(m -> Materials.Fluorite = m, "Fluorite", "Fluorite"),
        marker(m -> Materials.Flux = m, "Flux", "Flux"),
        marker(m -> Materials.Infernal = m, "Infernal", "Infernal"),
        marker(m -> Materials.InfusedDull = m, "InfusedDull", "Vacuus", TextureSet.SET_SHARDS, 0x00646464, true),
        marker(m -> Materials.InfusedTeslatite = m, "InfusedTeslatite", "Infused Teslatite", TextureSet.SET_NONE, 0x0064b4ff, true),
        marker(m -> Materials.InfusedVis = m, "InfusedVis", "Auram", TextureSet.SET_SHARDS, 0x00ff00ff, true),
        marker(m -> Materials.Invisium = m, "Invisium", "Invisium"),
        marker(m -> Materials.IridiumSodiumOxide = m, "IridiumSodiumOxide", "Iridium Sodium Oxide"),
        marker(m -> Materials.Leather = m, "Leather", "Leather", TextureSet.SET_ROUGH, 0x7f969650, true),
        marker(m -> Materials.Limestone = m, "Limestone", "Limestone"),
        marker(m -> Materials.Lodestone = m, "Lodestone", "Lodestone"),
        marker(m -> Materials.Luminite = m, "Luminite", "Luminite", TextureSet.SET_NONE, 0x00fafafa, true),
        marker(m -> Materials.Magma = m, "Magma", "Magma", TextureSet.SET_NONE, 0x00ff4000, true),
        marker(m -> Materials.Mawsitsit = m, "Mawsitsit", "Mawsitsit"),
        marker(m -> Materials.Metal = m, "Metal", "Metal", TextureSet.SET_METALLIC, DEFAULT_ARGB, false),
        marker(m -> Materials.Meteorite = m, "Meteorite", "Meteorite", TextureSet.SET_NONE, 0x0050233c, true),
        marker(m -> Materials.Mimichite = m, "Mimichite", "Mimichite", TextureSet.SET_GEM_VERTICAL, DEFAULT_ARGB, true),
        marker(m -> Materials.Moonstone = m, "Moonstone", "Moonstone"),
        marker(m -> Materials.Mud = m, "Mud", "Mud"),
        marker(m -> Materials.Mutation = m, "Mutation", "Mutation"),
        marker(m -> Materials.Nano = m, "Nano", "Bio"),
        marker(m -> Materials.Nether = m, "Nether", "Nether"),
        marker(m -> Materials.Onyx = m, "Onyx", "Onyx"),
        marker(m -> Materials.Organic = m, "Organic", "Organic", TextureSet.SET_LEAF, DEFAULT_ARGB, false),
        marker(m -> Materials.OsmiumTetroxide = m, "OsmiumTetroxide", "Osmium Tetroxide"),
        marker(m -> Materials.Painite = m, "Painite", "Painite"),
        marker(m -> Materials.Peanutwood = m, "Peanutwood", "Peanut Wood"),
        marker(m -> Materials.Peat = m, "Peat", "Peat"),
        marker(m -> Materials.Petroleum = m, "Petroleum", "Petroleum"),
        marker(m -> Materials.Pewter = m, "Pewter", "Pewter"),
        marker(m -> Materials.Piko = m, "Piko", "Bio"),
        marker(m -> Materials.Prismarine = m, "Prismarine", "Prismarine"),
        marker(m -> Materials.PurpleAlloy = m, "PurpleAlloy", "Purple Alloy", TextureSet.SET_NONE, 0x0064b4ff, true),
        marker(m -> Materials.Quartz = m, "Quartz", "Quartz", TextureSet.SET_QUARTZ, DEFAULT_ARGB, false, SubTag.CRYSTAL, SubTag.CRYSTALLISABLE, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.QUARTZ),
        marker(m -> Materials.Randomite = m, "Randomite", "Randomite"),
        marker(m -> Materials.Red = m, "Red", "Red", TextureSet.SET_NONE, 0x00ff0000, true),
        marker(m -> Materials.RubberTreeSap = m, "RubberTreeSap", "Rubber Tree Sap"),
        marker(m -> Materials.Sand = m, "Sand", "Sand"),
        marker(m -> Materials.SodiumPeroxide = m, "SodiumPeroxide", "Sodium Peroxide"),
        marker(m -> Materials.SolutionBlueVitriol = m, "SolutionBlueVitriol", "Blue Vitriol Solution"),
        marker(m -> Materials.SolutionNickelSulfate = m, "SolutionNickelSulfate", "Nickel Sulfate Solution"),
        marker(m -> Materials.Sunstone = m, "Sunstone", "Sunstone"),
        marker(m -> Materials.TNT = m, "TNT", "TNT"),
        marker(m -> Materials.Tar = m, "Tar", "Tar", TextureSet.SET_NONE, 0x000a0a0a, true),
        marker(m -> Materials.Unknown = m, "Unknown", "Unknown", TextureSet.SET_DULL, DEFAULT_ARGB, false),
        marker(m -> Materials.UnstableIngot = m, "Unstableingot", "Unstable", TextureSet.SET_NONE, 0x7fffffff, true),
        marker(m -> Materials.Voidstone = m, "Voidstone", "Voidstone", TextureSet.SET_NONE, 0xc8ffffff, true), };
    // spotless:on
}
