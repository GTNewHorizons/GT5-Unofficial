package gregtech.api.enums.materials;

import static gregtech.api.enums.materials.GTShapeStore.reg;

import java.util.Locale;
import java.util.Map;

import net.minecraft.block.Block;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StandardProperties;

import gregtech.api.enums.StoneType;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.GTOreAdapter;

/// Block [Shape] declarations for GT's ores.
///
/// Variants are the lowercased names of [StoneType]'s constants (see [#STONE_TYPE_NAMES]). A variant name is part
/// of a placed block's save identity, so the list must never be reordered or renamed. `oreSmall` has no variants
/// for ice stone, where small ores never generate.
///
/// [#init] runs before [Materials#init], too early to load [StoneType]. The variant data is therefore plain
/// strings, and [#stoneTypeOf] resolves a [StoneType] lazily.
public class OreShapes {

    // spotless:off
    public static Shape ore;
    public static Shape oreSmall;

    /// [StoneType]'s constant names, in declaration order, copied by hand.
    private static final String[] STONE_TYPE_NAMES = {
        "Stone", "Netherrack", "Endstone", "BlackGranite", "RedGranite", "Marble", "Basalt", "Moon", "Mars",
        "Asteroid", "Phobos", "Deimos", "Ceres", "Io", "Europa", "Ganymede", "Callisto", "Enceladus", "Titan",
        "Miranda", "Oberon", "Proteus", "Triton", "Pluto", "Haumea", "MakeMake", "Venus", "Mercury",
        "AlphaCentauri", "TCetiE", "VegaB", "BarnardaE", "BarnardaF", "Horus", "AnubisAndMaahes", "PackedIce",
        "SethIce", "SethClay", "Deepslate", "Tuff", "BlueIce",
    };

    /// [StoneType]s of [gregtech.api.enums.StoneCategory#Ice], excluded from [#oreSmall]'s variants.
    private static final String[] SMALL_ORE_EXCLUDED = { "PackedIce", "BlueIce" };
    // spotless:on

    /// The variant whose icon stands for the whole shape wherever GregTech draws ore art with no block behind it.
    public static final String ICON_VARIANT = variantOf(STONE_TYPE_NAMES[0]);

    /// The untinted background texture of each [StoneType], as a block atlas icon name.
    /// [com.ruling_0.materiallib.api.BlockShapeBuilder#variantBase] does not check that it exists, so a wrong
    /// path renders as `missingno`.
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
            .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_ore")
            .oreDict("ore")
            .variants(oreVariants)
            .drops(
                (material, variant, fortune, isSilkTouch, harvester) -> GTOreAdapter.INSTANCE
                    .shapeDrops(material, variant, GTUtility.isRealPlayer(harvester) ? fortune : 0, isSilkTouch, false))
            .hardness((material, variant) -> stoneBlock(variant).blockHardness)
            .resistance((material, variant) -> stoneBlock(variant).getExplosionResistance(null))
            .harvestLevel((material, variant) -> GTOreAdapter.INSTANCE.harvestLevel(material, 0));
        for (var entry : KNOWN_VARIANT_BASES.entrySet()) {
            oreBuilder.variantBase(variantOf(entry.getKey()), entry.getValue());
        }
        ore = reg(oreBuilder.build());

        var oreSmallBuilder = MaterialLibAPI.newBlockShape("gregtech", "oreSmall")
            .displayName("Small %s Ore")
            .property(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.small_material_ore")
            .oreDict("oreSmall")
            .variants(smallOreVariants)
            .drops(
                (material, variant, fortune, isSilkTouch, harvester) -> GTOreAdapter.INSTANCE
                    .shapeDrops(material, variant, GTUtility.isRealPlayer(harvester) ? fortune : 0, isSilkTouch, true))
            .hardness((material, variant) -> stoneBlock(variant).blockHardness)
            .resistance((material, variant) -> stoneBlock(variant).getExplosionResistance(null))
            .harvestLevel((material, variant) -> GTOreAdapter.INSTANCE.harvestLevel(material, -1));
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

    private static Block stoneBlock(String variant) {
        StoneType stoneType = stoneTypeOf(variant);
        return (stoneType == null ? StoneType.Stone : stoneType).getStone()
            .getBlock();
    }

    public static String variantOf(String stoneTypeName) {
        return stoneTypeName.toLowerCase(Locale.ROOT);
    }

    /// The variant whose icon carries `stoneType`'s ore art. Small ore on ice stone falls back to
    /// [#ICON_VARIANT].
    public static String iconVariantOf(StoneType stoneType, boolean small) {
        String stoneTypeName = stoneType.name();
        if (small && isSmallOreExcluded(stoneTypeName)) return ICON_VARIANT;
        return variantOf(stoneTypeName);
    }

    /// The [StoneType] a variant name resolves to, or null if `variant` names none of [#STONE_TYPE_NAMES].
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
