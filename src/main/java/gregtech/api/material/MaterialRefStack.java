package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// An amount of a referenced material, e.g. a composition or ore byproduct entry.
@Desugar
public record MaterialRefStack(MaterialRef material, long amount) {}
