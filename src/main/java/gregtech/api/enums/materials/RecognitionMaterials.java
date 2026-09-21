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
import com.ruling_0.materiallib.api.MaterialBuilder;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.TextureSet;

import gregtech.GTLoggers;
import gregtech.api.enums.SubTag;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;

/// Recognition entries: names that generate no items of their own but exist so
/// [gregtech.common.oredict.OreDictRegistrationHandler] resolves the ore-dictionary registrations another mod
/// fires against them by name, so a foreign entry such as `itemLeather`, `shardPrismarine`, or `blockWoolBlack`
/// resolves to one of these instead of falling through unrecognized and being blacklisted as a colour variant.
///
/// Every entry is a shapeless MaterialLib [Material] registered by [#registerBackingMaterials], looked up through
/// [#getRecognitionMarker]. Most carry only a name and whether an entry named for them unifies; the handful that
/// other code references directly also have a declared field here. `Ammonium` is the one entry whose name already
/// belongs to a real MaterialLib material, so it maps to that material instead of a registered backing.
///
/// A name whose items form a coherent material with MaterialLib shapes belongs in
/// [gregtech.api.enums.materials.XModMaterials] as a real material instead.
public class RecognitionMaterials {

    public static Material Quartz;
    public static Material Advanced;
    public static Material Ammonium;
    public static Material Limestone;
    public static Material Sand;
    public static Material Leather;
    public static Material Prismarine;

    private static final int DEFAULT_TINT = 0xFFFFFFFF;

    private static final Map<String, Material> RECOGNITION_MARKERS_BY_NAME = new LinkedHashMap<>();

    private RecognitionMaterials() {}

    /// The declared name and properties of a recognition entry, read by [#registerBackingMaterials] to build or
    /// find its backing [Material].
    private record MarkerSpec(String name, String localName, String textureSet, int tint, boolean unifiable,
        Set<SubTag> subTags) {

        MarkerSpec(String name, boolean unifiable) {
            this(name, name, "NONE", DEFAULT_TINT, unifiable, Set.of());
        }
    }

    /// The registered backing [Material] whose name is `name`, or null if no recognition entry claims that
    /// name.
    public static @Nullable Material getRecognitionMarker(String name) {
        return RECOGNITION_MARKERS_BY_NAME.get(name);
    }

    /// Registers a shapeless MaterialLib [Material] backing every marker in [#MARKERS] and [#RECOGNITION_MARKERS],
    /// skipping and logging any whose name already names a MaterialLib material. Carries
    /// [GTMaterialProperties#UNIFIABLE] and, for a marker with [SubTag]s, a [GTMaterialProperties#FLAGS] set of
    /// the identically-named [GTMaterialFlag] each [SubTag] maps to. Populates the by-name lookup
    /// [#getRecognitionMarker] serves and the declared fields from the same registered-or-found material. Runs
    /// during material registration, after [Materials#init], so the skip check sees every real material.
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
                GTLoggers.GT_FML_LOGGER.info(
                    "RecognitionMaterials: skipping backing material for '{}', a MaterialLib material with that name already exists",
                    m.name());
            } else {
                MaterialBuilder builder = MaterialLibAPI
                    .newMaterial("gregtech", m.name(), TextureSet.of("gregtech", m.textureSet()))
                    .setProperty(GTMaterialProperties.LOCAL_NAME, m.localName())
                    .setTint(m.tint())
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
        GTLoggers.GT_FML_LOGGER.info("RecognitionMaterials: registered {} backing materials", registered);
    }

    private record Marker(Consumer<Material> field, MarkerSpec spec) {}

    // spotless:off
    private static final Marker[] MARKERS = {
        new Marker(m -> Advanced = m, new MarkerSpec("Advanced", "Advanced Alloy", "NONE", DEFAULT_TINT, true, Set.of())),
        new Marker(m -> Ammonium = m, new MarkerSpec("Ammonium", "Ammonium", "NONE", DEFAULT_TINT, true, Set.of())),
        new Marker(m -> Leather = m, new MarkerSpec("Leather", "Leather", "ROUGH", 0x7F969650, true, Set.of())),
        new Marker(m -> Limestone = m, new MarkerSpec("Limestone", "Limestone", "NONE", DEFAULT_TINT, true, Set.of())),
        new Marker(m -> Prismarine = m, new MarkerSpec("Prismarine", "Prismarine", "NONE", DEFAULT_TINT, true, Set.of())),
        new Marker(m -> Quartz = m, new MarkerSpec("Quartz", "Quartz", "QUARTZ", DEFAULT_TINT, false, Set.of(SubTag.CRYSTAL, SubTag.CRYSTALLISABLE, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.QUARTZ))),
        new Marker(m -> Sand = m, new MarkerSpec("Sand", "Sand", "NONE", DEFAULT_TINT, true, Set.of())), };

    private static final MarkerSpec[] RECOGNITION_MARKERS = {
        new MarkerSpec("Debris", true),
        new MarkerSpec("Andesite", true),
        new MarkerSpec("Black", true),
        new MarkerSpec("BrickNether", false),
        new MarkerSpec("Cobblestone", false),
        new MarkerSpec("Crystal", false),
        new MarkerSpec("Ender", true),
        new MarkerSpec("Peat", true),
        new MarkerSpec("Red", true),
        new MarkerSpec("Unknown", false), };
    // spotless:on
}
