package gregtech.api.enums.materials;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.SubTag;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;

/// Recognition entries: names that generate no items of their own but exist so `gregtech.common.GTProxy#registerOre`
/// resolves every ore-dictionary registration another mod fires against them by name, so a foreign entry such as
/// `gemCitrine`, `oreFluorite`, or `dustAgate` unifies against one of these instead of falling through unrecognized.
///
/// Every entry is a shapeless MaterialLib [Material] registered by [#registerBackingMaterials], looked up through
/// [#getRecognitionMarker]. Most carry only a name and whether an entry named for them unifies. A handful carry
/// more because something besides name recognition reads them, and have a declared field here so the rest of the
/// code can reference them directly: `Ammonium` names the same composition reference `AmmoniumChloride`'s
/// contents use -- and is the one entry whose name already belongs to a real MaterialLib material, so it maps to
/// that material instead of a registered backing; `Leather` and `Sand` are read by
/// `gregtech.loaders.preload.LoaderGTItemData` as the material of an `ItemData`/`MaterialStack` for vanilla-item
/// recycling; `Limestone` and `Prismarine` are read by `ItemComb`/`NetheriteRecipes`/
/// `RecipeLoaderChemicalSkips` to fetch or register registered-ore stacks; `Fluix` and
/// `Quartz` carry [GTMaterialProperties#FLAGS] and are steered by name in `GTProxy#registerRecognitionOre`, and
/// `Fluix` additionally reaches `OrePrefixes#processRecognitionOre` so its crystal and dust recipes generate.
/// `Advanced`
/// names IC2's advanced alloy plate for `OrePrefixes#plateAlloy`, which is unifiable but not material-based: an
/// entry registered under it without a material resolves to `Materials#NULL`
/// (`GTProxy#resolveCensusMaterial`'s fallback), and every `plateAlloy` entry then contends for that one
/// shared unification key in `GTProxy#registerUnificationEntries`, so such an entry unifies into whichever
/// plate claims it first.
public class RecognitionMaterials {

    public static Material Quartz;
    public static Material Advanced;
    public static Material Fluix;
    public static Material Ammonium;
    public static Material Limestone;
    public static Material Sand;
    public static Material Leather;
    public static Material Prismarine;

    private static final int DEFAULT_ARGB = 0x00ffffff;

    private static final Map<String, Material> RECOGNITION_MARKERS_BY_NAME = new LinkedHashMap<>();

    private RecognitionMaterials() {}

    /// The declared name and properties of a recognition entry, read by [#registerBackingMaterials] to build or
    /// find its backing [Material]. Carries no behaviour of its own -- it is pure declaration data, not part of
    /// the marker's runtime identity.
    private record MarkerSpec(String name, String localName, String textureSet, int argb, boolean unifiable,
        Set<SubTag> subTags) {

        MarkerSpec(String name, boolean unifiable) {
            this(name, name, "NONE", DEFAULT_ARGB, unifiable, Set.of());
        }
    }

    /// The registered backing [Material] whose name is `name`, or null if no recognition entry claims that
    /// name.
    public static @Nullable Material getRecognitionMarker(String name) {
        return RECOGNITION_MARKERS_BY_NAME.get(name);
    }

    /// Fallback for a bare JUnit run, where [Materials#init] -- and so
    /// [#registerBackingMaterials] -- never runs because nothing fires MaterialLib's registration event.
    /// Resolves whatever MaterialLib already has registered under each marker name, without registering
    /// anything itself; a name with nothing registered under it stays unresolved.
    public static void load() {
        for (Marker marker : MARKERS) {
            Material ml = MaterialLibAPI.getMaterial(
                "gregtech",
                marker.spec()
                    .name());
            if (ml != null) {
                marker.field()
                    .accept(ml);
                RECOGNITION_MARKERS_BY_NAME.put(
                    marker.spec()
                        .name(),
                    ml);
            }
        }
        for (MarkerSpec spec : RECOGNITION_MARKERS) {
            Material ml = MaterialLibAPI.getMaterial("gregtech", spec.name());
            if (ml != null) RECOGNITION_MARKERS_BY_NAME.put(spec.name(), ml);
        }
    }

    /// Registers a shapeless MaterialLib [Material] backing every marker in [#MARKERS] and [#RECOGNITION_MARKERS],
    /// skipping any whose name already names a MaterialLib material (that name unifies into the existing
    /// material, so a duplicate would merge its shapes into the marker) and logging every such skip, since a
    /// silently-merged marker would otherwise steal that material's identity unnoticed -- `Ammonium` is the one
    /// name this always applies to. Carries [GTMaterialProperties#UNIFIABLE] and, for a marker with [SubTag]s, a
    /// [GTMaterialProperties#FLAGS] set of the identically-named [GTMaterialFlag] each [SubTag] maps to, which
    /// `MaterialUtils#hasFlag` reads. Also populates the by-name lookup [#getRecognitionMarker] serves, and the 8
    /// declared
    /// fields, from the same registered-or-found material. Runs during material registration, after
    /// [Materials#init], so the skip check sees every real material.
    public static void registerBackingMaterials() {
        List<MarkerSpec> markers = Stream.concat(
            Stream.of(MARKERS)
                .map(Marker::spec),
            Stream.of(RECOGNITION_MARKERS))
            .toList();
        int registered = 0;
        for (MarkerSpec m : markers) {
            Material material = MaterialLibAPI.getMaterial("gregtech", m.name());
            if (material != null) {
                GTMod.GT_FML_LOGGER.info(
                    "RecognitionMaterials: skipping backing material for '{}', a MaterialLib material with that name already exists",
                    m.name());
            } else {
                com.ruling_0.materiallib.api.MaterialBuilder builder = MaterialLibAPI
                    .newMaterial(
                        "gregtech",
                        m.name(),
                        com.ruling_0.materiallib.api.TextureSet.of("gregtech", m.textureSet()))
                    .setProperty(GTMaterialProperties.LOCAL_NAME, m.localName())
                    .setProperty(GTMaterialProperties.ARGB, m.argb())
                    .setProperty(GTMaterialProperties.UNIFIABLE, m.unifiable());
                if (!m.subTags()
                    .isEmpty()) {
                    EnumSet<GTMaterialFlag> flags = EnumSet.noneOf(GTMaterialFlag.class);
                    for (SubTag tag : m.subTags()) flags.add(GTMaterialFlag.valueOf(tag.mName));
                    builder = builder.setProperty(GTMaterialProperties.FLAGS, flags);
                }
                material = builder.build();
                registered++;
            }
            RECOGNITION_MARKERS_BY_NAME.put(m.name(), material);
        }
        for (Marker marker : MARKERS) {
            marker.field()
                .accept(
                    RECOGNITION_MARKERS_BY_NAME.get(
                        marker.spec()
                            .name()));
        }
        GTMod.GT_FML_LOGGER.info("RecognitionMaterials: registered {} backing materials", registered);
    }

    private record Marker(Consumer<Material> field, MarkerSpec spec) {}

    // spotless:off
    private static final Marker[] MARKERS = {
        new Marker(m -> Advanced = m, new MarkerSpec("Advanced", "Advanced Alloy", "NONE", DEFAULT_ARGB, true, Set.of())),
        new Marker(m -> Ammonium = m, new MarkerSpec("Ammonium", "Ammonium", "NONE", DEFAULT_ARGB, true, Set.of())),
        new Marker(m -> Fluix = m, new MarkerSpec("Fluix", "Fluix", "NONE", DEFAULT_ARGB, true, Set.of(SubTag.CRYSTAL, SubTag.CRYSTALLISABLE, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.QUARTZ))),
        new Marker(m -> Leather = m, new MarkerSpec("Leather", "Leather", "ROUGH", 0x7f969650, true, Set.of())),
        new Marker(m -> Limestone = m, new MarkerSpec("Limestone", "Limestone", "NONE", DEFAULT_ARGB, true, Set.of())),
        new Marker(m -> Prismarine = m, new MarkerSpec("Prismarine", "Prismarine", "NONE", DEFAULT_ARGB, true, Set.of())),
        new Marker(m -> Quartz = m, new MarkerSpec("Quartz", "Quartz", "QUARTZ", DEFAULT_ARGB, false, Set.of(SubTag.CRYSTAL, SubTag.CRYSTALLISABLE, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.QUARTZ))),
        new Marker(m -> Sand = m, new MarkerSpec("Sand", "Sand", "NONE", DEFAULT_ARGB, true, Set.of())), };

    private static final MarkerSpec[] RECOGNITION_MARKERS = {
        new MarkerSpec("Adamite", true),
        new MarkerSpec("Agate", true),
        new MarkerSpec("Alfium", true),
        new MarkerSpec("Debris", true),
        new MarkerSpec("Andesite", true),
        new MarkerSpec("Aquamarine", true),
        new MarkerSpec("Bitumen", true),
        new MarkerSpec("Black", true),
        new MarkerSpec("Bloodstone", true),
        new MarkerSpec("BrickNether", false),
        new MarkerSpec("Chimerite", true),
        new MarkerSpec("Chrysocolla", true),
        new MarkerSpec("Citrine", true),
        new MarkerSpec("Cluster", true),
        new MarkerSpec("Cobblestone", false),
        new MarkerSpec("Coral", true),
        new MarkerSpec("Crystal", false),
        new MarkerSpec("CrystalFlux", true),
        new MarkerSpec("Cyanite", true),
        new MarkerSpec("DarkStone", true),
        new MarkerSpec("Demonite", true),
        new MarkerSpec("Draconic", true),
        new MarkerSpec("Drulloy", true),
        new MarkerSpec("Ender", true),
        new MarkerSpec("Energized", true),
        new MarkerSpec("Fluorite", true),
        new MarkerSpec("Flux", true),
        new MarkerSpec("Infernal", true),
        new MarkerSpec("InfusedDull", true),
        new MarkerSpec("InfusedTeslatite", true),
        new MarkerSpec("InfusedVis", true),
        new MarkerSpec("Invisium", true),
        new MarkerSpec("IridiumSodiumOxide", true),
        new MarkerSpec("Lodestone", true),
        new MarkerSpec("Luminite", true),
        new MarkerSpec("Magma", true),
        new MarkerSpec("Mawsitsit", true),
        new MarkerSpec("Metal", false),
        new MarkerSpec("Meteorite", true),
        new MarkerSpec("Mimichite", true),
        new MarkerSpec("Moonstone", true),
        new MarkerSpec("Mud", true),
        new MarkerSpec("Mutation", true),
        new MarkerSpec("Nano", true),
        new MarkerSpec("Nether", true),
        new MarkerSpec("Onyx", true),
        new MarkerSpec("Organic", false),
        new MarkerSpec("OsmiumTetroxide", true),
        new MarkerSpec("Painite", true),
        new MarkerSpec("Peanutwood", true),
        new MarkerSpec("Peat", true),
        new MarkerSpec("Petroleum", true),
        new MarkerSpec("Pewter", true),
        new MarkerSpec("Piko", true),
        new MarkerSpec("PurpleAlloy", true),
        new MarkerSpec("Randomite", true),
        new MarkerSpec("Red", true),
        new MarkerSpec("RubberTreeSap", true),
        new MarkerSpec("SodiumPeroxide", true),
        new MarkerSpec("SolutionBlueVitriol", true),
        new MarkerSpec("SolutionNickelSulfate", true),
        new MarkerSpec("Sunstone", true),
        new MarkerSpec("TNT", true),
        new MarkerSpec("Tar", true),
        new MarkerSpec("Unknown", false),
        new MarkerSpec("Unstableingot", true),
        new MarkerSpec("Voidstone", true), };
    // spotless:on
}
