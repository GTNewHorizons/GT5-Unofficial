package gregtech.api.material;

import java.util.EnumSet;
import java.util.List;

import com.github.bsideup.jabel.Desugar;

/// The bartworks-side data of a material that was (or merged with) a `Werkstoff`, ported verbatim from the
/// `werkstoff.json` dump by `scripts/mu/gen_materials.py`. Kept in one composite property
/// ([GTMaterialProperties#WERKSTOFF]) rather than spread over the shared per-material keys because a werkstoff
/// merged with a pre-existing gregtech material may carry different scalar values than the gregtech dump's
/// (which win the shared keys); the legacy `Werkstoff` reconstruction and the bartworks recipe consumers need
/// the bartworks-side values regardless.
///
/// - `ids`: every legacy werkstoff `mID` this material covers (more than one when two same-name werkstoffe
/// folded into one declaration).
/// - `type`: the `Werkstoff.Types` enum constant name.
/// - `prefixes`: the dumped `generatedPrefixes` ground truth (every `OrePrefixes` name `hasItemType` reported),
/// including the prefixes that stay on legacy blocks (`sheetmetal`, `frameGt`) and so have no MaterialLib
/// shape.
/// - `contents`: the werkstoff `CONTENTS` list (chemical make-up), distinct from the gregtech
/// `GTMaterialProperties#COMPOSITION` so a merge never alters the legacy `Materials` reconstruction. Each
/// entry records which legacy registry it referenced -- see [WerkstoffRefStack].
/// - `oreByProducts`: the werkstoff byproduct list exactly as the `Werkstoff` constructor left it (three
/// self entries -- werkstoff-kind references to this material's own name -- when the declaration passed no
/// list, else the declared list verbatim); amounts are always 1.
/// - `subTags`: the explicitly-added `SubTag` names (contents-derived tags stay dynamic).
/// - `durabilityOverride`/`speedOverride`/`qualityOverride`: the raw tool-stat overrides; `0` means the legacy
/// formula computes the value from protons/melting point/mass/contents.
/// - `mixCircuit`: the programmed-circuit number for the mixer recipe, `-1` when unset.
/// - `ebfGasTimeMultiplier`: `-1.0` selects the proton-count default (see `BlastFurnaceGasStat`).
@Desugar
public record WerkstoffData(List<Integer> ids, String type, String pool, int meltingPoint, int boilingPoint,
    long protons, long neutrons, long mass, int meltingVoltage, int durabilityOverride, float speedOverride,
    int qualityOverride, float durabilityModifier, int enchantmentLevel, double ebfGasTimeMultiplier,
    double ebfGasAmountMultiplier, int mixCircuit, EnumSet<GTWerkstoffFlag> flags, List<String> prefixes,
    List<WerkstoffRefStack> contents, List<WerkstoffRefStack> oreByProducts, List<String> subTags,
    List<String> additionalOreDict, String formula) {}
