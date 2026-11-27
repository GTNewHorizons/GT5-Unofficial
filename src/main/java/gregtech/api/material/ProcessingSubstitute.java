package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;
import com.ruling_0.materiallib.api.MaterialRef;

/// What ore processing yields in place of a material, declared through
/// [GTMaterialProperties#PROCESSING_SUBSTITUTE]. The platinum-group metals yield the platinum line's metallic powders
/// and residues, the naquadah family its oxide mixtures, and Cerium and Samarium their lanthanide concentrates, so
/// those materials come out of an ore only through their processing lines. [ProcessingSubstitutes] applies it as
/// recipes are built.
@Desugar
public record ProcessingSubstitute(MaterialRef into, Rule rule) {

    /// Which dusts a substitute replaces, at what amount, and in which recipes.
    public enum Rule {

        /// Every dust form at twice the amount, including smelting results and the crafting-table hammer recipes.
        PLATINUM_METAL(true, true, true, 2, true),
        /// Every dust form at twice the amount. The crafting-table hammer recipes give one dust per dust.
        PLATINUM_RESIDUE(true, true, false, 1, true),
        /// Whole, impure and purified dusts at twice the amount. Cauldron washing keeps the material.
        PLATINUM_TRACE(true, false, false, 0, false),
        /// Whole, small and tiny dusts, doubled by ore processing and the crafting-table hammer recipes and kept at
        /// their amount by decomposition.
        CONCENTRATE(false, true, false, 2, true);

        /// Platinum-group rules also replace impure and purified dusts, always double, and spare materials flagged
        /// [GTMaterialFlag#KEEPS_PLATINUM_GROUP_OUTPUTS].
        final boolean platinumGroup;
        final boolean smallDusts;
        final boolean smelting;
        /// Zero when the crafting-table hammer recipes keep the material.
        final int craftingMultiplier;
        /// Whether cauldron washing yields one substitute dust per impure or purified dust.
        final boolean washes;

        Rule(boolean platinumGroup, boolean smallDusts, boolean smelting, int craftingMultiplier, boolean washes) {
            this.platinumGroup = platinumGroup;
            this.smallDusts = smallDusts;
            this.smelting = smelting;
            this.craftingMultiplier = craftingMultiplier;
            this.washes = washes;
        }
    }
}
