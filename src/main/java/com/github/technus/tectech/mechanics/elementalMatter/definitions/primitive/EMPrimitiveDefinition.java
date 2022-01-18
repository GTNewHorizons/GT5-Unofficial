package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMPrimitiveTemplate;

import static com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay.NO_DECAY;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class EMPrimitiveDefinition extends EMPrimitiveTemplate {
    public static final EMPrimitiveDefinition
            nbtE__ = new EMPrimitiveDefinition("NBT ERROR", "!", 0, 0D, Integer.MIN_VALUE, 0),
            null__ = new EMPrimitiveDefinition("NULL POINTER", ".", 0, 0D, -3, Integer.MIN_VALUE),
            space__ = new EMPrimitiveDefinition("Space", "_", 0, 0D, -4, Integer.MIN_VALUE+1),
            magic = new EMPrimitiveDefinition("Magic", "Ma", 4, 1e5D, 0, 1),
            magic_ = new EMPrimitiveDefinition("Antimagic", "~Ma", -4, 1e5D, 0, 2);

    private EMPrimitiveDefinition(String name, String symbol, int type, double mass, int color, int ID) {
        super(name, symbol, type, mass, 0, color, ID);
    }

    public static void run() {
        nbtE__.init(null__, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        null__.init(null__, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        space__.init(space__, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        magic.init(magic_, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        magic_.init(magic, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
    }

    @Override
    public String getLocalizedName() {
        return translateToLocal("tt.keyword.Primitive")+": " + getName();
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }
}