package gregtech.loaders.materials;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;

/// Recognition entries: names that generate no items of their own but exist so `gregtech.common.GTProxy#registerOre`
/// resolves every ore-dictionary registration another mod fires against them by name, so a foreign entry such as
/// `gemCitrine`, `oreFluorite`, or `dustAgate` unifies against one of these instead of falling through unrecognized.
///
/// Every entry is a [RecognitionMarker], looked up through [#getRecognitionMarker]. Most carry only a name and
/// whether an entry named for them unifies. A handful carry more because something besides name recognition
/// reads them, and have a declared field here so the rest of the code can reference them directly: `Ammonium`
/// names the same composition reference `AmmoniumChloride`'s contents use; `Leather` and `Sand` are read by
/// `gregtech.loaders.preload.LoaderGTItemData` as the material of an `ItemData`/`MaterialStack` for vanilla-item
/// recycling; `Limestone` and `Prismarine` are read by `ItemComb`/`NetheriteRecipes`/
/// `RecipeLoaderChemicalSkips` to fetch or register registered-ore stacks; `Fluix` and
/// `Quartz` carry [SubTag]s and are steered by name in `GTProxy#registerRecognitionOre`, and `Fluix`
/// additionally reaches `OrePrefixes#processOre` so its crystal and dust recipes generate. `Advanced` names
/// IC2's advanced alloy plate for `OrePrefixes#plateAlloy`, which is unifiable but not material-based: an entry
/// registered under it without a material resolves to `Materials#_NULL` in `GTProxy#registerUnificationEntries`,
/// and every `plateAlloy` entry then contends for that one shared unification key, so such an entry unifies into
/// whichever plate claims it first.
public class RecognitionMaterials {

    public static RecognitionMarker Quartz;
    public static RecognitionMarker Advanced;
    public static RecognitionMarker Fluix;
    public static RecognitionMarker Ammonium;
    public static RecognitionMarker Limestone;
    public static RecognitionMarker Sand;
    public static RecognitionMarker Leather;
    public static RecognitionMarker Prismarine;

    private static final int DEFAULT_ARGB = 0x00ffffff;

    private static final Map<String, RecognitionMarker> RECOGNITION_MARKERS_BY_NAME = new LinkedHashMap<>();

    private RecognitionMaterials() {}

    /// A foreign ore-dictionary name GregTech recognizes during unification. It names an entry and nothing more --
    /// it is not a material and generates no items. The data-carrying entries in [#MARKERS] flow through the
    /// marker-typed forms of the transitional plumbing (`ItemData`, `MaterialStack`, `GTOreDictUnificator`,
    /// `OrePrefixes#processOre`), which resolve a marker to its registered backing exactly where the
    /// `Materials`-typed forms resolve a facade. Implements [ISubTagContainer] so a marker's [SubTag]s test
    /// through the same `contains` check a `Materials` instance or [MaterialSubTagView] does.
    ///
    /// [#toString] returns the name so a marker routed through a name-keyed path stringifies to the exact
    /// ore-dictionary name a `Materials`-backed marker produced.
    public record RecognitionMarker(String name, String localName, TextureSet textureSet, int argb, boolean unifiable,
        Set<SubTag> subTags) implements ISubTagContainer {

        public RecognitionMarker {
            subTags = new LinkedHashSet<>(subTags);
        }

        public RecognitionMarker(String name, boolean unifiable) {
            this(name, name, TextureSet.SET_NONE, DEFAULT_ARGB, unifiable, Set.of());
        }

        public String getInternalName() {
            return name;
        }

        public String getLocalizedNameKey() {
            return "Material." + getInternalName().toLowerCase();
        }

        public short[] getRGBA() {
            return new short[] { (short) ((argb >>> 16) & 0xFF), (short) ((argb >>> 8) & 0xFF), (short) (argb & 0xFF),
                (short) ((argb >>> 24) & 0xFF) };
        }

        @Override
        public boolean contains(SubTag tag) {
            return subTags.contains(tag);
        }

        @Override
        public RecognitionMarker add(SubTag... tags) {
            if (tags != null) for (SubTag tag : tags) if (tag != null) subTags.add(tag);
            return this;
        }

        @Override
        public boolean remove(SubTag tag) {
            return subTags.remove(tag);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /// The [RecognitionMarker] whose name is `name`, or null if no recognition entry claims that name.
    public static RecognitionMarker getRecognitionMarker(String name) {
        return RECOGNITION_MARKERS_BY_NAME.get(name);
    }

    public static void load() {
        for (Marker marker : MARKERS) {
            RECOGNITION_MARKERS_BY_NAME.put(
                marker.marker()
                    .name(),
                marker.marker());
            marker.field()
                .accept(marker.marker());
        }
        for (RecognitionMarker marker : RECOGNITION_MARKERS) {
            RECOGNITION_MARKERS_BY_NAME.put(marker.name(), marker);
        }
    }

    /// Registers a shapeless MaterialLib [com.ruling_0.materiallib.api.Material] backing every marker in
    /// [#MARKERS] and [#RECOGNITION_MARKERS], skipping any whose name already names a MaterialLib material (that
    /// name unifies into the existing material, so a duplicate would merge its shapes into the marker) and
    /// logging every such skip, since a silently-merged marker would otherwise steal that material's identity
    /// unnoticed. Carries [GTMaterialProperties#UNIFIABLE] and, for a marker with [SubTag]s, a
    /// [GTMaterialProperties#FLAGS] set of the identically-named [GTMaterialFlag] each [SubTag] maps to, which
    /// `MU#hasFlag` reads. Runs during material registration, after
    /// [gregtech.api.enums.materials2.Materials2Materials#init], so the skip check sees every real material.
    public static void registerBackingMaterials() {
        List<RecognitionMarker> markers = Stream.concat(
            Stream.of(MARKERS)
                .map(Marker::marker),
            Stream.of(RECOGNITION_MARKERS))
            .toList();
        int registered = 0;
        for (RecognitionMarker m : markers) {
            if (MaterialLibAPI.getMaterial("gregtech", m.name()) != null) {
                GTMod.GT_FML_LOGGER.info(
                    "RecognitionMaterials: skipping backing material for '{}', a MaterialLib material with that name already exists",
                    m.name());
                continue;
            }
            com.ruling_0.materiallib.api.MaterialBuilder builder = MaterialLibAPI
                .newMaterial(
                    "gregtech",
                    m.name(),
                    com.ruling_0.materiallib.api.TextureSet.of("gregtech", m.textureSet().mSetName))
                .setProperty(GTMaterialProperties.LEGACY_NAME, m.name())
                .setProperty(GTMaterialProperties.LOCAL_NAME, m.localName())
                .setProperty(GTMaterialProperties.ARGB, m.argb())
                .setProperty(GTMaterialProperties.UNIFIABLE, m.unifiable());
            if (!m.subTags()
                .isEmpty()) {
                EnumSet<GTMaterialFlag> flags = EnumSet.noneOf(GTMaterialFlag.class);
                for (SubTag tag : m.subTags()) flags.add(GTMaterialFlag.valueOf(tag.mName));
                builder = builder.setProperty(GTMaterialProperties.FLAGS, flags);
            }
            builder.build();
            registered++;
        }
        GTMod.GT_FML_LOGGER.info("RecognitionMaterials: registered {} backing materials", registered);
    }

    private record Marker(Consumer<RecognitionMarker> field, RecognitionMarker marker) {}

    // spotless:off
    private static final Marker[] MARKERS = {
        new Marker(m -> Advanced = m, new RecognitionMarker("Advanced", "Advanced Alloy", TextureSet.SET_NONE, DEFAULT_ARGB, true, Set.of())),
        new Marker(m -> Ammonium = m, new RecognitionMarker("Ammonium", "Ammonium", TextureSet.SET_NONE, DEFAULT_ARGB, true, Set.of())),
        new Marker(m -> Fluix = m, new RecognitionMarker("Fluix", "Fluix", TextureSet.SET_NONE, DEFAULT_ARGB, true, Set.of(SubTag.CRYSTAL, SubTag.CRYSTALLISABLE, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.QUARTZ))),
        new Marker(m -> Leather = m, new RecognitionMarker("Leather", "Leather", TextureSet.SET_ROUGH, 0x7f969650, true, Set.of())),
        new Marker(m -> Limestone = m, new RecognitionMarker("Limestone", "Limestone", TextureSet.SET_NONE, DEFAULT_ARGB, true, Set.of())),
        new Marker(m -> Prismarine = m, new RecognitionMarker("Prismarine", "Prismarine", TextureSet.SET_NONE, DEFAULT_ARGB, true, Set.of())),
        new Marker(m -> Quartz = m, new RecognitionMarker("Quartz", "Quartz", TextureSet.SET_QUARTZ, DEFAULT_ARGB, false, Set.of(SubTag.CRYSTAL, SubTag.CRYSTALLISABLE, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.QUARTZ))),
        new Marker(m -> Sand = m, new RecognitionMarker("Sand", "Sand", TextureSet.SET_NONE, DEFAULT_ARGB, true, Set.of())), };

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
