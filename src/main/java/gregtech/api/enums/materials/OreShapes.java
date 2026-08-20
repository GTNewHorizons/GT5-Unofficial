package gregtech.api.enums.materials;

import static gregtech.api.enums.materials.GTShapeStore.reg;

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
/// legacy small ore never generates on ice stone (`GTOreAdapter#supports`).
///
/// [#init] runs before `Materials#init`, which references `ore`/`oreSmall`, so touching [StoneType] from this
/// class at that point would load it too early. [#STONE_TYPE_NAMES]/[#SMALL_ORE_EXCLUDED]/
/// [#KNOWN_VARIANT_BASES] are therefore plain string data, and [#stoneTypeOf] resolves a variant back to its
/// [StoneType] lazily, from behavior hooks that only run during gameplay.
public class OreShapes {

    // spotless:off
    public static Shape ore;
    public static Shape oreSmall;

    /// [StoneType]'s enum constant names, in declaration order -- a hand-copied literal, see this class's
    /// javadoc.
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
    /// a shape block behind it. Every variant resolves the same `ore`/`oreSmall` texture unless a texture set
    /// ships per-variant art, and this is the only variant both shapes declare.
    public static final String ICON_VARIANT = variantOf(STONE_TYPE_NAMES[0]);

    /// Untinted per-variant background textures, one per [StoneType], each a `"<domain>:<path>"` icon name
    /// resolved on the block atlas (basePath `textures/blocks/`, per
    /// [com.ruling_0.materiallib.api.BlockShapeBuilder#variantBase]'s contract) -- vanilla Minecraft blocks, GT's
    /// own granite/marble/basalt block art (`textures/blocks/iconsets/<NAME>.png`, converted the same way as
    /// every other GT block icon), and cross-mod stone/terrain textures (GalaxySpace, Galacticraft, AmunRa,
    /// EtFuturumRequiem). [com.ruling_0.materiallib.api.BlockShapeBuilder#variantBase] registers a path
    /// unconditionally with no existence check, so a wrong one renders as `missingno` in world.
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
        ore = reg(oreBuilder.build());

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
        oreSmall = reg(oreSmallBuilder.build());
    }

    private static boolean isSmallOreExcluded(String stoneTypeName) {
        for (String excluded : SMALL_ORE_EXCLUDED) {
            if (excluded.equals(stoneTypeName)) return true;
        }
        return false;
    }

    /// Whether `material` originates from a bartworks material, and so takes [BWOreAdapter]'s ore behavior
    /// rather than [GTOreAdapter]'s.
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

    /// The variant whose per-material icon carries `stoneType`'s ore art, so that a texture set shipping
    /// `ore_<variant>.png`/`oreSmall_<variant>.png` reskins that stone type alone. Small ore on ice stone falls
    /// back to [#ICON_VARIANT], the only variant [#oreSmall] declares for it -- see this class's javadoc.
    public static String iconVariantOf(StoneType stoneType, boolean small) {
        String stoneTypeName = stoneType.name();
        if (small && isSmallOreExcluded(stoneTypeName)) return ICON_VARIANT;
        return variantOf(stoneTypeName);
    }

    /// The [StoneType] a variant name resolves to, or null if `variant` names none of [#STONE_TYPE_NAMES].
    /// Resolved lazily via [StoneType#valueOf]; caching it at [#init] time is unsafe, see this class's javadoc.
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
