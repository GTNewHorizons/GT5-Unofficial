package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMPrimitiveTemplate;

public abstract class EMFermionDefinition extends EMPrimitiveTemplate {
    protected EMFermionDefinition(
            String name, String symbol, int generation, double mass, int charge, int color, int ID, String bind) {
        super(name, symbol, generation, mass, charge, color, ID, bind);
    }

    @Override
    public String getLocalizedTypeName() {
        return translateToLocal("tt.keyword.Fermion");
    }
}
