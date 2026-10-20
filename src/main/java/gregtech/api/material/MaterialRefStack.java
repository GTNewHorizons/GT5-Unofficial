package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;
import com.ruling_0.materiallib.api.MaterialRef;

/// An amount of a referenced material, e.g. a composition or ore byproduct entry.
@Desugar
public record MaterialRefStack(MaterialRef material, long amount) {}
