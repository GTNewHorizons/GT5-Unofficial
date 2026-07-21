package gregtech.loaders.materials;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.material.MarkerMaterial;

/// Recognition entries: names that generate no items of their own but exist so `gregtech.common.GTProxy#registerOre`
/// resolves every ore-dictionary registration another mod fires against them by name, so a foreign entry such as
/// `gemCitrine`, `oreFluorite`, or `dustAgate` unifies against one of these instead of falling through unrecognized.
///
/// Most entries carry only a name and whether an entry named for them unifies, held as [RecognitionMarker] and
/// looked up through [#getRecognitionMarker]. A handful carry more because something besides name recognition
/// reads them: `Ammonium` is read by `bartworks.system.material.WerkstoffReconstruction` as a composition
/// reference (`AmmoniumChloride`'s contents name it); `Leather` and `Sand` are read by
/// `gregtech.loaders.preload.LoaderGTItemData` as the material of an `ItemData`/`MaterialStack` for vanilla-item
/// recycling; `Limestone` and `Prismarine` are read by `ItemComb`/`NetheriteRecipes`/
/// `RecipeLoaderChemicalSkips` as an `IOreMaterial` to fetch or register registered-ore stacks; `Fluix` and
/// `Quartz` carry [SubTag]s and are steered by name in `GTProxy#registerRecognitionOre`, and `Fluix`
/// additionally reaches `OrePrefixes#processOre` so its crystal and dust recipes generate. `Advanced` names
/// IC2's advanced alloy plate for [OrePrefixes#plateAlloy], which is unifiable but not material-based: an entry
/// registered under it without a material resolves to [Materials#_NULL] in `GTProxy#registerUnificationEntries`,
/// and every `plateAlloy` entry then contends for that one shared unification key, so such an entry unifies into
/// whichever plate claims it first. These eight stay [MarkerMaterial]s, looked up through [#getMarker], and keep
/// their `Materials` field so the rest of the code can reference them directly.
public class RecognitionMaterials {

    private static final int DEFAULT_ARGB = 0x00ffffff;
    private static final SubTag[] NO_SUBTAGS = {};

    private static final Map<String, MarkerMaterial> MARKERS_BY_NAME = new LinkedHashMap<>();
    private static final Map<String, RecognitionMarker> RECOGNITION_MARKERS_BY_NAME = new LinkedHashMap<>();

    private RecognitionMaterials() {}

    /// A foreign ore-dictionary name GregTech recognizes during unification. It names an entry and nothing more --
    /// it is not a material and generates no items.
    public record RecognitionMarker(String name, boolean unifiable) {}

    /// The [MarkerMaterial] whose internal name is `name`, or null if `name` is not one of the recognition
    /// entries that stays a marker (see the class javadoc).
    public static MarkerMaterial getMarker(String name) {
        return MARKERS_BY_NAME.get(name);
    }

    /// The [RecognitionMarker] whose name is `name`, or null if no recognition entry claims that name.
    public static RecognitionMarker getRecognitionMarker(String name) {
        return RECOGNITION_MARKERS_BY_NAME.get(name);
    }

    public static void load() {
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
        for (RecognitionMarker marker : RECOGNITION_MARKERS) {
            RECOGNITION_MARKERS_BY_NAME.put(marker.name(), marker);
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
        marker(m -> Materials.Advanced = m, "Advanced", "Advanced Alloy"),
        marker(m -> Materials.Ammonium = m, "Ammonium", "Ammonium"),
        marker(m -> Materials.Fluix = m, "Fluix", "Fluix", TextureSet.SET_NONE, DEFAULT_ARGB, true, SubTag.CRYSTAL, SubTag.CRYSTALLISABLE, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.QUARTZ),
        marker(m -> Materials.Leather = m, "Leather", "Leather", TextureSet.SET_ROUGH, 0x7f969650, true),
        marker(m -> Materials.Limestone = m, "Limestone", "Limestone"),
        marker(m -> Materials.Prismarine = m, "Prismarine", "Prismarine"),
        marker(m -> Materials.Quartz = m, "Quartz", "Quartz", TextureSet.SET_QUARTZ, DEFAULT_ARGB, false, SubTag.CRYSTAL, SubTag.CRYSTALLISABLE, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.QUARTZ),
        marker(m -> Materials.Sand = m, "Sand", "Sand"), };

    private static final RecognitionMarker[] RECOGNITION_MARKERS = {
        new RecognitionMarker("Adamite", true),
        new RecognitionMarker("Agate", true),
        new RecognitionMarker("Alfium", true),
        new RecognitionMarker("Debris", true),
        new RecognitionMarker("Andesite", true),
        new RecognitionMarker("Aquamarine", true),
        new RecognitionMarker("Bitumen", true),
        new RecognitionMarker("Black", true),
        new RecognitionMarker("Bloodstone", true),
        new RecognitionMarker("BrickNether", false),
        new RecognitionMarker("Chimerite", true),
        new RecognitionMarker("Chrysocolla", true),
        new RecognitionMarker("Citrine", true),
        new RecognitionMarker("Cluster", true),
        new RecognitionMarker("Cobblestone", false),
        new RecognitionMarker("Coral", true),
        new RecognitionMarker("Crystal", false),
        new RecognitionMarker("CrystalFlux", true),
        new RecognitionMarker("Cyanite", true),
        new RecognitionMarker("DarkStone", true),
        new RecognitionMarker("Demonite", true),
        new RecognitionMarker("Draconic", true),
        new RecognitionMarker("Drulloy", true),
        new RecognitionMarker("Ender", true),
        new RecognitionMarker("Energized", true),
        new RecognitionMarker("Fluorite", true),
        new RecognitionMarker("Flux", true),
        new RecognitionMarker("Infernal", true),
        new RecognitionMarker("InfusedDull", true),
        new RecognitionMarker("InfusedTeslatite", true),
        new RecognitionMarker("InfusedVis", true),
        new RecognitionMarker("Invisium", true),
        new RecognitionMarker("IridiumSodiumOxide", true),
        new RecognitionMarker("Lodestone", true),
        new RecognitionMarker("Luminite", true),
        new RecognitionMarker("Magma", true),
        new RecognitionMarker("Mawsitsit", true),
        new RecognitionMarker("Metal", false),
        new RecognitionMarker("Meteorite", true),
        new RecognitionMarker("Mimichite", true),
        new RecognitionMarker("Moonstone", true),
        new RecognitionMarker("Mud", true),
        new RecognitionMarker("Mutation", true),
        new RecognitionMarker("Nano", true),
        new RecognitionMarker("Nether", true),
        new RecognitionMarker("Onyx", true),
        new RecognitionMarker("Organic", false),
        new RecognitionMarker("OsmiumTetroxide", true),
        new RecognitionMarker("Painite", true),
        new RecognitionMarker("Peanutwood", true),
        new RecognitionMarker("Peat", true),
        new RecognitionMarker("Petroleum", true),
        new RecognitionMarker("Pewter", true),
        new RecognitionMarker("Piko", true),
        new RecognitionMarker("PurpleAlloy", true),
        new RecognitionMarker("Randomite", true),
        new RecognitionMarker("Red", true),
        new RecognitionMarker("RubberTreeSap", true),
        new RecognitionMarker("SodiumPeroxide", true),
        new RecognitionMarker("SolutionBlueVitriol", true),
        new RecognitionMarker("SolutionNickelSulfate", true),
        new RecognitionMarker("Sunstone", true),
        new RecognitionMarker("TNT", true),
        new RecognitionMarker("Tar", true),
        new RecognitionMarker("Unknown", false),
        new RecognitionMarker("Unstableingot", true),
        new RecognitionMarker("Voidstone", true), };
    // spotless:on
}
