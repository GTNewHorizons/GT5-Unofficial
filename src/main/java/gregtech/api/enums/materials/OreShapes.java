package gregtech.api.enums.materials;

import java.util.Locale;
import java.util.Map;

import net.minecraft.block.Block;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.StoneType;
import gregtech.api.material.GTMaterialProperties;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.GTOreAdapter;

/// Hand-maintained block [Shape] declarations for GT's ores. Unlike `block`, `ore` and `oreSmall` carry a real
/// generation bit (`ORE`), so membership follows the capability-bit pipeline rather than a curated list.
///
/// Variants are named after [StoneType]'s enum constants, lowercased, in declaration order -- see
/// [#STONE_TYPE_NAMES]. This name list is fixed permanently once shipped: a variant name is part of a placed
/// block's save identity (`materiallib:ore_<variant>`/`materiallib:oreSmall_<variant>`), so it must never be
/// reordered or renamed. `oreSmall` omits the two `StoneCategory.Ice` variants (`packedice`, `blueice`):
/// legacy small ore never generates on ice stone (`GTOreAdapter#supports`), so those two combinations are
/// permanently unreachable and were dropped rather than declared and left forever empty.
///
/// [#init] runs before `Materials#init` populates every field that class-of-interest data depends
/// on (block shapes must resolve before `Materials#init` references `ore`/`oreSmall`), so
/// [#STONE_TYPE_NAMES]/[#SMALL_ORE_EXCLUDED]/[#KNOWN_VARIANT_BASES] are plain string data rather than derived
/// from the live [StoneType] enum, and [#stoneTypeOf] resolves a variant back to its [StoneType] lazily,
/// called only from behavior hooks that run during real gameplay, long after every mod's preInit (and
/// `Materials.init`) has finished.
public class OreShapes {

    // spotless:off
    public static Shape ore;
    public static Shape oreSmall;

    /// [StoneType]'s enum constant names, lowercased, in declaration order -- see this class's javadoc for why
    /// this is a hand-copied literal rather than derived by reflecting over the live enum.
    private static final String[] STONE_TYPE_NAMES = {
        "Stone", "Netherrack", "Endstone", "BlackGranite", "RedGranite", "Marble", "Basalt", "Moon", "Mars",
        "Asteroid", "Phobos", "Deimos", "Ceres", "Io", "Europa", "Ganymede", "Callisto", "Enceladus", "Titan",
        "Miranda", "Oberon", "Proteus", "Triton", "Pluto", "Haumea", "MakeMake", "Venus", "Mercury",
        "AlphaCentauri", "TCetiE", "VegaB", "BarnardaE", "BarnardaF", "Horus", "AnubisAndMaahes", "PackedIce",
        "SethIce", "SethClay", "Deepslate", "Tuff", "BlueIce",
    };

    /// [StoneType]s of [gregtech.api.enums.StoneCategory#Ice], excluded from [#oreSmall]'s variant list --
    /// see this class's javadoc.
    private static final String[] SMALL_ORE_EXCLUDED = { "PackedIce", "BlueIce" };
    // spotless:on

    /// The variant whose per-material icon stands in for the whole shape wherever GregTech draws ore art without
    /// a shape block behind it -- [gregtech.common.blocks.GTBlockOre] and the ore marker overlay both composite
    /// the stone layer themselves, so they need the tinted ore icon alone. Every variant resolves the same
    /// `ore`/`oreSmall` texture unless a texture set ships per-variant art, and this one is the only variant both
    /// shapes are guaranteed to declare ([#SMALL_ORE_EXCLUDED] drops two from `oreSmall`).
    public static final String ICON_VARIANT = variantOf(STONE_TYPE_NAMES[0]);

    /// Untinted per-variant background textures, one per [StoneType], each a `"<domain>:<path>"` icon name
    /// resolved on the block atlas (basePath `textures/blocks/`, per
    /// [com.ruling_0.materiallib.api.BlockShapeBuilder#variantBase]'s contract) -- vanilla Minecraft blocks, GT's
    /// own granite/marble/basalt block art (`textures/blocks/iconsets/<NAME>.png`, converted the same way as
    /// every other GT block icon), and cross-mod stone/terrain textures (GalaxySpace, Galacticraft, AmunRa,
    /// EtFuturumRequiem). Every path here was confirmed against the actual dependency jar's block icon
    /// registration bytecode and on-disk texture file, not guessed: a wrong path renders as `missingno` in
    /// world, since [com.ruling_0.materiallib.api.BlockShapeBuilder#variantBase] registers it unconditionally,
    /// with no existence check.
    private static final Map<String, String> KNOWN_VARIANT_BASES = Map.ofEntries(
        Map.entry("Stone", "minecraft:stone"),
        Map.entry("Netherrack", "minecraft:netherrack"),
        Map.entry("Endstone", "minecraft:end_stone"),
        Map.entry("BlackGranite", "gregtech:iconsets/GRANITE_BLACK_STONE"),
        Map.entry("RedGranite", "gregtech:iconsets/GRANITE_RED_STONE"),
        Map.entry("Marble", "gregtech:iconsets/MARBLE_STONE"),
        Map.entry("Basalt", "gregtech:iconsets/BASALT_STONE"),
        Map.entry("Moon", "galacticraftmoon:bottom"),
        Map.entry("Mars", "galacticraftmars:bottom"),
        Map.entry("Asteroid", "galacticraftasteroids:asteroid1"),
        Map.entry("Phobos", "galaxyspace:phobos/phobosstone"),
        Map.entry("Deimos", "galaxyspace:deimos/deimossubgrunt"),
        Map.entry("Ceres", "galaxyspace:ceres/ceressubgrunt"),
        Map.entry("Io", "galaxyspace:io/iostone"),
        Map.entry("Europa", "galaxyspace:europa/europaice"),
        Map.entry("Ganymede", "galaxyspace:ganymede/ganymedesubgrunt"),
        Map.entry("Callisto", "galaxyspace:callisto/callistosubgrunt"),
        Map.entry("Enceladus", "galaxyspace:enceladus/enceladusgrunt"),
        Map.entry("Titan", "galaxyspace:titan/titanstone"),
        Map.entry("Miranda", "galaxyspace:miranda/mirandastone"),
        Map.entry("Oberon", "galaxyspace:oberon/oberonstone"),
        Map.entry("Proteus", "galaxyspace:proteus/proteusstone"),
        Map.entry("Triton", "galaxyspace:triton/tritonstone"),
        Map.entry("Pluto", "galaxyspace:pluto/plutostone"),
        Map.entry("Haumea", "galaxyspace:haumea/haumeagrunt"),
        Map.entry("MakeMake", "galaxyspace:makemake/makemakesubgrunt"),
        Map.entry("Venus", "galaxyspace:venus/venussubgrunt"),
        Map.entry("Mercury", "galaxyspace:mercury/mercurymetalcore"),
        Map.entry("AlphaCentauri", "galaxyspace:acentauribb/acentauribbsubgrunt"),
        Map.entry("TCetiE", "galaxyspace:tcetie/tcetiestone"),
        Map.entry("VegaB", "galaxyspace:vegaB/vegaBsubgrunt"),
        Map.entry("BarnardaE", "galaxyspace:barnardaE/barnardaEsubgrunt"),
        Map.entry("BarnardaF", "galaxyspace:barnardaF/barnardaFsubgrunt"),
        Map.entry("Horus", "minecraft:obsidian"),
        Map.entry("AnubisAndMaahes", "amunra:basalt"),
        Map.entry("PackedIce", "minecraft:ice_packed"),
        Map.entry("SethIce", "minecraft:ice_packed"),
        Map.entry("SethClay", "minecraft:hardened_clay"),
        Map.entry("Deepslate", "minecraft:deepslate"),
        Map.entry("Tuff", "minecraft:tuff"),
        Map.entry("BlueIce", "minecraft:blue_ice"));

    public static void init() {
        String[] oreVariants = new String[STONE_TYPE_NAMES.length];
        for (int i = 0; i < STONE_TYPE_NAMES.length; i++) {
            oreVariants[i] = variantOf(STONE_TYPE_NAMES[i]);
        }
        String[] smallOreVariants = new String[STONE_TYPE_NAMES.length - SMALL_ORE_EXCLUDED.length];
        int smallIndex = 0;
        for (String stoneTypeName : STONE_TYPE_NAMES) {
            if (!isSmallOreExcluded(stoneTypeName)) {
                smallOreVariants[smallIndex++] = variantOf(stoneTypeName);
            }
        }

        var oreBuilder = MaterialLibAPI.newBlockShape("gregtech", "ore")
            .displayName("%s Ore")
            .oreDict("ore")
            .variants(oreVariants)
            .drops((material, variant, fortune, isSilkTouch) -> {
                if (isWerkstoff(material))
                    return BWOreAdapter.INSTANCE.shapeDrops(material, variant, fortune, isSilkTouch, false);
                return GTOreAdapter.INSTANCE.shapeDrops(material, variant, fortune, isSilkTouch, false);
            })
            .hardness((material, variant) -> stoneBlock(variant).blockHardness)
            .resistance((material, variant) -> stoneBlock(variant).getExplosionResistance(null))
            .harvestLevel((material, variant) -> {
                if (isWerkstoff(material)) return BWOreAdapter.INSTANCE.harvestLevel(material, 0);
                return GTOreAdapter.INSTANCE.harvestLevel(material, 0);
            });
        for (var entry : KNOWN_VARIANT_BASES.entrySet()) {
            oreBuilder.variantBase(variantOf(entry.getKey()), entry.getValue());
        }
        ore = oreBuilder.build();

        var oreSmallBuilder = MaterialLibAPI.newBlockShape("gregtech", "oreSmall")
            .displayName("Small %s Ore")
            .oreDict("oreSmall")
            .variants(smallOreVariants)
            .drops(
                (material, variant, fortune, isSilkTouch) -> isWerkstoff(material)
                    ? BWOreAdapter.INSTANCE.shapeDrops(material, variant, fortune, isSilkTouch, true)
                    : GTOreAdapter.INSTANCE.shapeDrops(material, variant, fortune, isSilkTouch, true))
            .hardness((material, variant) -> stoneBlock(variant).blockHardness)
            .resistance((material, variant) -> stoneBlock(variant).getExplosionResistance(null))
            .harvestLevel(
                (material, variant) -> isWerkstoff(material) ? BWOreAdapter.INSTANCE.harvestLevel(material, -1)
                    : GTOreAdapter.INSTANCE.harvestLevel(material, -1));
        for (var entry : KNOWN_VARIANT_BASES.entrySet()) {
            if (!isSmallOreExcluded(entry.getKey())) {
                oreSmallBuilder.variantBase(variantOf(entry.getKey()), entry.getValue());
            }
        }
        oreSmall = oreSmallBuilder.build();
    }

    private static boolean isSmallOreExcluded(String stoneTypeName) {
        for (String excluded : SMALL_ORE_EXCLUDED) {
            if (excluded.equals(stoneTypeName)) return true;
        }
        return false;
    }

    /// Whether `material` originates from a bartworks material -- both [GTOreAdapter] and
    /// [BWOreAdapter] can resolve behavior for any material sharing this shape (every werkstoff also carries a
    /// legacy name), so the drop/harvest-level hooks above dispatch on this property instead of "which adapter
    /// resolves it" to route each material to the adapter that actually owns its ore behavior (BW ore has a
    /// flat harvest level and no per-material `isValidForStone` gate, unlike GT's).
    private static boolean isWerkstoff(Material material) {
        return material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) != null;
    }

    private static Block stoneBlock(String variant) {
        StoneType stoneType = stoneTypeOf(variant);
        return (stoneType == null ? StoneType.Stone : stoneType).getStone()
            .getBlock();
    }

    public static String variantOf(String stoneTypeName) {
        return stoneTypeName.toLowerCase(Locale.ROOT);
    }

    /// The [StoneType] a variant name resolves to, or null if `variant` names none of [#STONE_TYPE_NAMES].
    /// Resolved lazily via [StoneType#valueOf] -- see this class's javadoc for why eagerly caching this at
    /// [#init] time is unsafe.
    public static StoneType stoneTypeOf(String variant) {
        for (String stoneTypeName : STONE_TYPE_NAMES) {
            if (variantOf(stoneTypeName).equals(variant)) {
                return StoneType.valueOf(stoneTypeName);
            }
        }
        return null;
    }

    private OreShapes() {}
}
