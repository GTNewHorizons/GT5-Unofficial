package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import static com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay.NO_DECAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMPrimitiveTemplate;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMType;

/**
 * Created by danie_000 on 22.10.2016.
 */
public class EMPrimitiveDefinition extends EMPrimitiveTemplate {
    public static final EMPrimitiveDefinition
            nbtE__ = new EMPrimitiveDefinition("tt.keyword.PrimitiveNBTERROR", "!", 0, 0D, -1, Integer.MIN_VALUE, "!"),
            null__ =
                    new EMPrimitiveDefinition(
                            "tt.keyword.PrimitiveNULLPOINTER", ".", 0, 0D, -3, Integer.MIN_VALUE + 1, "."),
            space = new EMPrimitiveDefinition("tt.keyword.PrimitiveSpace", "_", 0, 0D, -4, Integer.MIN_VALUE + 2, "_"),
            space_ =
                    new EMPrimitiveDefinition(
                            "tt.keyword.PrimitivePresence", "~_", 0, 0D, -4, Integer.MIN_VALUE + 3, "~_"),
            mass = new EMPrimitiveDefinition("tt.keyword.PrimitiveMass", "*", 0, 1, -4, Integer.MIN_VALUE + 4, "*"),
            mass_ =
                    new EMPrimitiveDefinition(
                            "tt.keyword.PrimitiveDarkMass", "~*", 0, 1, -4, Integer.MIN_VALUE + 5, "~*"),
            energy =
                    new EMPrimitiveDefinition("tt.keyword.PrimitiveEnergy", "E", 4, 0D, -4, Integer.MIN_VALUE + 6, "E"),
            energy_ =
                    new EMPrimitiveDefinition(
                            "tt.keyword.PrimitiveDarkEnergy", "~E", -4, 0, -4, Integer.MIN_VALUE + 7, "~E"),
            magic =
                    new EMPrimitiveDefinition(
                            "tt.keyword.PrimitiveMagic", "Ma", 5, 1e5D, 0, Integer.MIN_VALUE + 8, "Ma"),
            magic_ =
                    new EMPrimitiveDefinition(
                            "tt.keyword.PrimitiveAntimagic", "~Ma", -5, 1e5D, 0, Integer.MIN_VALUE + 9, "~Ma");

    protected EMPrimitiveDefinition(String name, String symbol, int type, double mass, int color, int ID, String bind) {
        super(name, symbol, type, mass, 0, color, ID, bind);
    }

    public static void run(EMDefinitionsRegistry registry) {
        registry.registerDefinitionClass(new EMType(EMPrimitiveDefinition.class, "tt.keyword.Primitive"));
        nbtE__.init(registry, null, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        null__.init(registry, null, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);

        space.init(registry, space_, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        space_.init(registry, space, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);

        mass.init(registry, mass_, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        mass_.init(registry, mass, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);

        energy.init(registry, energy_, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        energy_.init(registry, energy, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);

        magic.init(registry, magic_, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        magic_.init(registry, magic, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
    }

    @Override
    public String getLocalizedTypeName() {
        return translateToLocal("tt.keyword.Primitive");
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }
}
