package com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions;

import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.transformations.AspectDefinitionCompat;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMPrimitiveTemplate;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMType;

import static com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay.NO_DECAY;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 06.05.2017.
 */
public final class EMPrimalAspectDefinition extends EMPrimitiveTemplate {
    public static final EMPrimalAspectDefinition
            magic_air = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Air"), "a`", 1e1D, 45,"a`"),
            magic_earth = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Earth"), "e`", 1e9D, 44,"e`"),
            magic_fire = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Fire"), "f`", 1e3D, 43,"f`"),
            magic_water = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Water"), "w`", 1e7D, 42,"w`"),
            magic_order = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Order"), "o`", 1e5D, 40,"o`"),
            magic_entropy = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Chaos"), "c`", 1e5D, 41,"c`");

    private EMPrimalAspectDefinition(String name, String symbol, double mass, int ID,String bind) {
        super(name, symbol, 0, mass, 0, -1, ID,bind);
    }

    public static void run(EMDefinitionsRegistry registry) {
        registry.registerDefinitionClass(new EMType(EMPrimalAspectDefinition.class,"tt.keyword.Primal"));
        magic_air.init(registry,null, -1F, -1, -1, NO_DECAY);
        magic_earth.init(registry,null, -1F, -1, -1, NO_DECAY);
        magic_fire.init(registry,null, -1F, -1, -1, NO_DECAY);
        magic_water.init(registry,null, -1F, -1, -1, NO_DECAY);
        magic_order.init(registry,null, -1F, -1, -1, NO_DECAY);
        magic_entropy.init(registry,null, -1F, -1, -1, NO_DECAY);
    }

    @Override
    public String getLocalizedName() {
        String name = AspectDefinitionCompat.aspectDefinitionCompat.getAspectLocalizedName(this);
        if (name != null) {
            return translateToLocal("tt.keyword.Primal") + ": " + getUnlocalizedName()+" ("+name+")";
        } else {
            return translateToLocal("tt.keyword.Primal") + ": " + getUnlocalizedName();
        }
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }
}