package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// An amount of a Thaumcraft aspect, identified by its `TCAspects.TC_Aspects` enum constant name.
@Desugar
public record AspectRefStack(String name, int amount) {}
