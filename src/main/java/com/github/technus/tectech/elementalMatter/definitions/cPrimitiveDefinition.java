package com.github.technus.tectech.elementalMatter.definitions;

import com.github.technus.tectech.elementalMatter.classes.cElementalPrimitive;
import com.github.technus.tectech.elementalMatter.interfaces.iElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.elementalMatter.classes.cElementalDecay.noDecay;
import static com.github.technus.tectech.elementalMatter.classes.cElementalDecay.noProduct;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class cPrimitiveDefinition extends cElementalPrimitive {
    public static final cPrimitiveDefinition
            debug__ = new cPrimitiveDefinition("Fallback of a bug", "!", 0, 0f, 0, Integer.MIN_VALUE, Integer.MIN_VALUE),
        //Never create null__ particle - if u need fallback use debug__
            null__ = new cPrimitiveDefinition("This is a bug", ".", 0, 0F, 0, -3, Integer.MAX_VALUE),
            space__ = new cPrimitiveDefinition("Space", "_", 0, 0F, 0, -4, 0),
            magic = new cPrimitiveDefinition("Magic", "Ma", 4, -1F, 0, 0, 1),
            magic_ = new cPrimitiveDefinition("Antimagic", "~Ma", -4, -1F, 0, 0, 2);

    protected cPrimitiveDefinition(String name, String symbol, int type, float mass, int charge, int color, int ID) {
        super(name, symbol, type, mass, charge, color, ID);
    }

    public static iElementalDefinition fromNBT(NBTTagCompound content) {
        return bindsBO.get(content.getInteger("c"));
    }

    public static void run() {
        debug__.init(null__, -1F, -1, -1, noDecay);
        null__.init(null__, -1F, -1, -1, noProduct);
        space__.init(space__, -1F, -1, -1, noProduct);
        magic.init(magic_, -1F, -1, -1, noDecay);
        magic_.init(magic, -1F, -1, -1, noDecay);
    }

    @Override
    public String getName() {
        return "Primitive: " + name;
    }
}