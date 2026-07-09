package gregtech.api.enums.materials2;

import java.util.Locale;
import java.util.Map;

import net.minecraft.block.Block;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.StoneType;
import gregtech.common.ores.GTOreAdapter;

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
/// reordered or renamed. `shapeOreSmall` omits the two `StoneCategory.Ice` variants (`packedice`, `blueice`):
/// legacy small ore never generates on ice stone (`GTOreAdapter#supports`), so those two combinations are
/// permanently unreachable and were dropped rather than declared and left forever empty.
///
/// [#init] deliberately never touches the live [StoneType] enum (nor, transitively, [gregtech.api.enums.Materials]):
/// `StoneType`'s constants are built by referencing `Materials.Stone`/`Materials.Netherrack`/etc. directly (see
/// its source), so resolving even one -- e.g. iterating `StoneType.STONE_TYPES` -- forces `Materials`'
/// class-initializer to run. `Materials`' static initializer rebuilds every legacy field from
/// `Materials2Materials` (see `MaterialsLegacyBridge`), which is empty until `Materials2Materials#init` runs;
/// [Materials2.java] calls this class's [#init] earlier than that (block shapes must resolve before
/// `Materials2DataN#init` references `shapeOre`/`shapeOreSmall`), so an early touch would silently fall back to
/// bare-JUnit stub data (`Materials2Materials.Iron == null`) for every legacy material for the rest of the boot,
/// not just ore's. [#STONE_TYPE_NAMES]/[#SMALL_ORE_EXCLUDED]/[#KNOWN_VARIANT_BASES] are therefore plain string
/// data, and [#stoneTypeOf] resolves a variant back to its [StoneType] lazily, called only from behavior hooks
/// that run during real gameplay, long after every mod's preInit (and `Materials2Materials.init`) has finished.
public class Materials2OreShapes {

    // spotless:off
    public static Shape shapeOre;
    public static Shape shapeOreSmall;

    /// [StoneType]'s enum constant names, lowercased, in declaration order -- see this class's javadoc for why
    /// this is a hand-copied literal rather than derived by reflecting over the live enum.
    private static final String[] STONE_TYPE_NAMES = {
        "Stone", "Netherrack", "Endstone", "BlackGranite", "RedGranite", "Marble", "Basalt", "Moon", "Mars",
        "Asteroid", "Phobos", "Deimos", "Ceres", "Io", "Europa", "Ganymede", "Callisto", "Enceladus", "Titan",
        "Miranda", "Oberon", "Proteus", "Triton", "Pluto", "Haumea", "MakeMake", "Venus", "Mercury",
        "AlphaCentauri", "TCetiE", "VegaB", "BarnardaE", "BarnardaF", "Horus", "AnubisAndMaahes", "PackedIce",
        "SethIce", "SethClay", "Deepslate", "Tuff", "BlueIce",
    };

    /// [StoneType]s of [gregtech.api.enums.StoneCategory#Ice], excluded from [#shapeOreSmall]'s variant list --
    /// see this class's javadoc.
    private static final String[] SMALL_ORE_EXCLUDED = { "PackedIce", "BlueIce" };
    // spotless:on

    /// Untinted per-variant background textures MaterialLib is confident are correct without a live-game check:
    /// vanilla Minecraft stone/netherrack/end stone/hardened clay, and GT's own granite/marble/basalt block art
    /// (`textures/blocks/iconsets/<NAME>.png`, converted the same way as every other GT block icon). The
    /// remaining 33 variants (GalaxySpace/Galacticraft planets and moons, Horus, AnubisAndMaahes, the two Ice
    /// stones, SethIce, Deepslate, Tuff) back onto cross-mod textures this migration could not verify a resource
    /// path for without a live client; those variants render as a single tinted layer (no base) until a
    /// follow-up in-client pass adds them -- see [com.ruling_0.materiallib.api.BlockShapeBuilder#variantBase]'s
    /// contract: a variant with no base texture renders fine, just without the stone-background art.
    private static final Map<String, String> KNOWN_VARIANT_BASES = Map.of(
        "Stone",
        "minecraft:blocks/stone",
        "Netherrack",
        "minecraft:blocks/netherrack",
        "Endstone",
        "minecraft:blocks/end_stone",
        "BlackGranite",
        "gregtech:iconsets/GRANITE_BLACK_STONE",
        "RedGranite",
        "gregtech:iconsets/GRANITE_RED_STONE",
        "Marble",
        "gregtech:iconsets/MARBLE_STONE",
        "Basalt",
        "gregtech:iconsets/BASALT_STONE",
        "SethClay",
        "minecraft:blocks/hardened_clay");

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
            .drops(
                (material, variant, fortune, isSilkTouch) -> GTOreAdapter.INSTANCE
                    .shapeDrops(material, variant, fortune, isSilkTouch, false))
            .hardness((material, variant) -> stoneBlock(variant).blockHardness)
            .resistance((material, variant) -> stoneBlock(variant).getExplosionResistance(null))
            .harvestLevel((material, variant) -> GTOreAdapter.INSTANCE.harvestLevel(material, 0));
        for (var entry : KNOWN_VARIANT_BASES.entrySet()) {
            oreBuilder.variantBase(variantOf(entry.getKey()), entry.getValue());
        }
        shapeOre = oreBuilder.build();

        var oreSmallBuilder = MaterialLibAPI.newBlockShape("gregtech", "oreSmall")
            .displayName("Small %s Ore")
            .oreDict("oreSmall")
            .variants(smallOreVariants)
            .drops(
                (material, variant, fortune, isSilkTouch) -> GTOreAdapter.INSTANCE
                    .shapeDrops(material, variant, fortune, isSilkTouch, true))
            .hardness((material, variant) -> stoneBlock(variant).blockHardness)
            .resistance((material, variant) -> stoneBlock(variant).getExplosionResistance(null))
            .harvestLevel((material, variant) -> GTOreAdapter.INSTANCE.harvestLevel(material, -1));
        for (var entry : KNOWN_VARIANT_BASES.entrySet()) {
            if (!isSmallOreExcluded(entry.getKey())) {
                oreSmallBuilder.variantBase(variantOf(entry.getKey()), entry.getValue());
            }
        }
        shapeOreSmall = oreSmallBuilder.build();
    }

    private static boolean isSmallOreExcluded(String stoneTypeName) {
        for (String excluded : SMALL_ORE_EXCLUDED) {
            if (excluded.equals(stoneTypeName)) return true;
        }
        return false;
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
