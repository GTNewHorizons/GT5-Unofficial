package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// An amount of a material referenced from werkstoff data ([GTMaterialProperties#WERKSTOFF_ORE_BYPRODUCTS]).
/// `werkstoff` records which registry the legacy declaration referenced: `true` for another `Werkstoff`,
/// `false` for a legacy `Materials` constant. The name alone cannot recover this -- e.g. bartworks declares
/// both a Calcium werkstoff and ore-byproduct entries pointing at `Materials.Calcium` -- and the legacy
/// `Werkstoff` reconstruction must resolve each reference against the same registry the declaration used.
@Desugar
public record WerkstoffRefStack(MaterialRef material, long amount, boolean werkstoff) {}
