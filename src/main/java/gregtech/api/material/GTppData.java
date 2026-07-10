package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// The gtPlusPlus-side data of a material ported from `gtpp-materials.json` by `scripts/mu/gen_materials.py`.
/// Kept in one composite property ([GTMaterialProperties#GTPP]) rather than spread over the shared
/// per-material keys, for the same reason as [WerkstoffData]: a gtPlusPlus material merged with a
/// pre-existing gregtech material may carry different scalar values than the gregtech dump's (which wins the
/// shared keys, see `scripts/mu/gen_materials.py`'s gtpp-fold region), but the legacy
/// `gtPlusPlus.core.material.Material` reconstruction and gtplusplus recipe consumers need the
/// gtPlusPlus-side values regardless.
///
/// - `tier`/`voltageMultiplier`: the legacy `Material.vTier`/`vVoltageMultiplier`; no gregtech equivalent.
/// - `meltingPointK`/`boilingPointK`: the legacy `Material.meltingPointC`/`boilingPointC` fields converted
/// with the same formula as `Material`'s own `MathUtils#celsiusToKelvin` (`round(celsius + 273.15)`) --
/// despite the field names, the dump values are already Kelvin for every material gtPlusPlus copied straight
/// from a gregtech `Materials` constant (the overwhelming majority of the ~138 same-name folds), since those
/// definitions pass the gregtech constant's Kelvin melting point as the "Celsius" constructor argument
/// verbatim; converting anyway reproduces exactly what the legacy class itself computed either way.
/// - `durability`: the legacy `Material.vDurability`, a broader "toughness" stat on a different scale than
/// [GTMaterialProperties#DURABILITY] (gregtech's narrower tool-durability dump field) -- kept separate rather
/// than merged into it.
/// - `usesBlastFurnace`/`isRadioactive`/`radiationLevel`: legacy `Material` fields with no gregtech
/// equivalent ([GTWerkstoffFlag#RADIOACTIVE] is bartworks-only, not shared by [GTMaterialFlag]).
/// - `hasOre`: whether the legacy `Material` generated an ore chain; informational only -- actual shape
/// membership already carries the real signal.
/// - `chemicalFormula`: the legacy `Material.vChemicalFormula` display string.
@Desugar
public record GTppData(int tier, long voltageMultiplier, int meltingPointK, int boilingPointK, int durability,
    boolean usesBlastFurnace, boolean isRadioactive, int radiationLevel, boolean hasOre, String chemicalFormula) {}
