package gregtech.api.enums.materials2;

import java.util.Locale;
import java.util.Map;

import net.minecraft.block.Block;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.StoneType;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.GTPPOreAdapter;

/// Hand-maintained block [Shape] declarations for GT's ores (unlike [Materials2Shapes], not
/// `gen_shapes.py`-generated: `ore`/`oreSmall` are block-kind, which that generator excludes -- see
/// `scripts/mu/gen_shapes.py`'s `is_block_kind`). Membership is driven by the dumped `generatedPrefixes`
/// ground truth (see `scripts/mu/gen_materials.py`'s `ore_shape_lines`), same as any other capability-bit
/// prefix; unlike `block`, `ore`/`oreSmall` carry a real generation bit (`ORE`) so no `legacy-blocks.json`-style
/// curated membership dump was needed.
///
/// Variants are named after [StoneType]'s enum constants, lowercased, in declaration order -- see
/// [#STONE_TYPE_NAMES]. This name list is fixed permanently once shipped: a variant name is part of a placed
/// block's save identity (`materiallib:ore_<variant>`/`materiallib:oreSmall_<variant>`), so it must never be
/// reordered or renamed. `oreSmall` omits the two `StoneCategory.Ice` variants (`packedice`, `blueice`):
/// legacy small ore never generates on ice stone (`GTOreAdapter#supports`), so those two combinations are
/// permanently unreachable and were dropped rather than declared and left forever empty.
///
/// [#init] deliberately never touches the live [StoneType] enum (nor, transitively, [gregtech.api.enums.Materials]):
/// `StoneType`'s constants are built by referencing `Materials.Stone`/`Materials.Netherrack`/etc. directly (see
/// its source), so resolving even one -- e.g. iterating `StoneType.STONE_TYPES` -- forces `Materials`'
/// class-initializer to run. `Materials`' static initializer rebuilds every legacy field from
/// `Materials2Materials` (see `MaterialsLegacyBridge`), which is empty until `Materials2Materials#init` runs;
/// [Materials2.java] calls this class's [#init] earlier than that (block shapes must resolve before
/// `Materials2Materials#init` references `ore`/`oreSmall`), so an early touch would silently fall back to
/// bare-JUnit stub data (`Materials2Materials.Iron == null`) for every legacy material for the rest of the boot,
/// not just ore's. [#STONE_TYPE_NAMES]/[#SMALL_ORE_EXCLUDED]/[#KNOWN_VARIANT_BASES] are therefore plain string
/// data, and [#stoneTypeOf] resolves a variant back to its [StoneType] lazily, called only from behavior hooks
/// that run during real gameplay, long after every mod's preInit (and `Materials2Materials.init`) has finished.
public class Materials2OreShapes {

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
                if (isGtpp(material)) return GTPPOreAdapter.INSTANCE.shapeDrops(material, fortune, isSilkTouch);
                return GTOreAdapter.INSTANCE.shapeDrops(material, variant, fortune, isSilkTouch, false);
            })
            .hardness((material, variant) -> stoneBlock(variant).blockHardness)
            .resistance((material, variant) -> stoneBlock(variant).getExplosionResistance(null))
            .harvestLevel((material, variant) -> {
                if (isWerkstoff(material)) return BWOreAdapter.INSTANCE.harvestLevel(material, 0);
                if (isGtpp(material)) return GTPPOreAdapter.INSTANCE.harvestLevel(material);
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

    /// Whether `material` was reconstructed from a bartworks `Werkstoff` -- both [GTOreAdapter] and
    /// [BWOreAdapter] can resolve *a* [gregtech.api.enums.Materials] for any material sharing this shape (every
    /// werkstoff also owns a legacy bridge `Materials` instance, see `BridgeMaterialsLoader`), so the drop/
    /// harvest-level hooks above dispatch on this property instead of "which adapter resolves it" to route each
    /// material to the adapter that actually owns its ore behavior (BW ore had a flat harvest level and no
    /// per-material `isValidForStone` gate, unlike GT's).
    private static boolean isWerkstoff(Material material) {
        return material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) != null;
    }

    /// Whether `material` is a *pure* gtpp material with no live [gregtech.api.enums.Materials] counterpart --
    /// [GTOreAdapter] already owns drop/harvest-level behavior for a gtpp name-merge material (its own dump
    /// captured real per-material formulas that predate gtpp entirely), so this excludes any material
    /// [MU#materialOf] can still resolve, mirroring [GTPPOreAdapter#materialOf]'s own discriminator.
    private static boolean isGtpp(Material material) {
        return material.getProperty(GTMaterialProperties.GTPP_STATE) != null && MU.materialOf(material) == null;
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

    private Materials2OreShapes() {}
}
