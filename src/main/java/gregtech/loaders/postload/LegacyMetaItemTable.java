package gregtech.loaders.postload;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.OrePrefixes;

/// The oredict prefix each saved damage value of a legacy GregTech meta-item material range stood for. The material
/// is the legacy id `damage % 1000` (see [gregtech.api.enums.materials.LegacyMaterialIDIndex]). [PosteaTransformers]
/// reads this to turn saved stacks into MaterialLib items.
final class LegacyMetaItemTable {

    private LegacyMetaItemTable() {}

    private static final OrePrefixes PLACEHOLDER = OrePrefixes.___placeholder___;

    /// One prefix per block of 1000 damage values below 32000, in block order.
    private static final Map<String, OrePrefixes[]> META_ITEM_PREFIXES = Map.of(
        "gt.metaitem.01",
        blocks(
            OrePrefixes.dustTiny,
            OrePrefixes.dustSmall,
            OrePrefixes.dust,
            OrePrefixes.dustImpure,
            OrePrefixes.dustPure,
            OrePrefixes.crushed,
            OrePrefixes.crushedPurified,
            OrePrefixes.crushedCentrifuged,
            OrePrefixes.gem,
            OrePrefixes.nugget,
            null,
            OrePrefixes.ingot,
            OrePrefixes.ingotHot,
            PLACEHOLDER,
            PLACEHOLDER,
            PLACEHOLDER,
            PLACEHOLDER,
            OrePrefixes.plate,
            OrePrefixes.plateDouble,
            OrePrefixes.plateTriple,
            OrePrefixes.plateQuadruple,
            OrePrefixes.plateQuintuple,
            OrePrefixes.plateDense,
            OrePrefixes.stick,
            OrePrefixes.lens,
            OrePrefixes.round,
            OrePrefixes.bolt,
            OrePrefixes.screw,
            OrePrefixes.ring,
            OrePrefixes.foil,
            OrePrefixes.cell,
            OrePrefixes.cellPlasma),
        "gt.metaitem.02",
        blocks(
            PLACEHOLDER,
            PLACEHOLDER,
            PLACEHOLDER,
            PLACEHOLDER,
            PLACEHOLDER,
            OrePrefixes.toolHeadHammer,
            OrePrefixes.toolHeadFile,
            OrePrefixes.toolHeadSaw,
            OrePrefixes.toolHeadDrill,
            OrePrefixes.toolHeadChainsaw,
            OrePrefixes.toolHeadWrench,
            PLACEHOLDER,
            PLACEHOLDER,
            PLACEHOLDER,
            PLACEHOLDER,
            OrePrefixes.toolHeadBuzzSaw,
            OrePrefixes.turbineBlade,
            null,
            OrePrefixes.itemCasing,
            OrePrefixes.wireFine,
            OrePrefixes.gearGtSmall,
            OrePrefixes.rotor,
            OrePrefixes.stickLong,
            OrePrefixes.springSmall,
            OrePrefixes.spring,
            PLACEHOLDER,
            PLACEHOLDER,
            OrePrefixes.gemChipped,
            OrePrefixes.gemFlawed,
            OrePrefixes.gemFlawless,
            OrePrefixes.gemExquisite,
            OrePrefixes.gearGt),
        "gt.metaitem.03",
        blocks(
            PLACEHOLDER,
            PLACEHOLDER,
            PLACEHOLDER,
            PLACEHOLDER,
            OrePrefixes.nanite,
            OrePrefixes.rawOre,
            OrePrefixes.plateSuperdense));

    /// The cracked cells of `gt.metaitem.99`, one block of 1000 each from damage 10000.
    private static final List<OrePrefixes> CRACKED_CELLS = List.of(
        OrePrefixes.cellHydroCracked1,
        OrePrefixes.cellHydroCracked2,
        OrePrefixes.cellHydroCracked3,
        OrePrefixes.cellSteamCracked1,
        OrePrefixes.cellSteamCracked2,
        OrePrefixes.cellSteamCracked3);

    private static OrePrefixes[] blocks(OrePrefixes... prefixes) {
        return Arrays.copyOf(prefixes, 32);
    }

    /// Returns the prefix a `gt.metaitem.0N` damage below 32000 stood for, or null for an unused block or another
    /// item.
    static @Nullable OrePrefixes metaItemPrefix(String itemName, int damage) {
        OrePrefixes[] prefixes = META_ITEM_PREFIXES.get(itemName);
        if (prefixes == null || damage < 0 || damage >= 32000) return null;
        return prefixes[damage / 1000];
    }

    /// Returns the prefix a `gt.metaitem.99` damage stood for: molten cells below 1000, then the cracked cells
    /// from 10000. Null for any other damage.
    static @Nullable OrePrefixes cellPrefix(int damage) {
        if (damage < 0) return null;
        if (damage < 1000) return OrePrefixes.cellMolten;
        int block = damage / 1000 - 10;
        return block >= 0 && block < CRACKED_CELLS.size() ? CRACKED_CELLS.get(block) : null;
    }
}
