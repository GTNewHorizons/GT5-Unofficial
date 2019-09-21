package com.github.bartimaeusnek.bartworks.client.textures;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;

import java.util.Arrays;
import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class PrefixTextureLinker implements Runnable {
    public static HashMap<OrePrefixes,HashMap<TextureSet, Textures.ItemIcons.CustomIcon>> texMap = new HashMap<>();

    {
        GregTech_API.sBeforeGTLoad.add(this);
    }

    @Override
    public void run() {

        for (OrePrefixes prefixes : OrePrefixes.values()) {
            HashMap curr = new HashMap<>();
            if (prefixes.mTextureIndex == -1 && Werkstoff.GenerationFeatures.prefixLogic.get(prefixes) != 0) {
                Arrays.stream(TextureSet.class.getFields()).filter(field -> field.getName().contains("SET")).forEach(SET -> {
                    try {
                        curr.put(SET.get(null), new Textures.ItemIcons.CustomIcon("materialicons/" + SET.getName().substring(4) + "/" + prefixes));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
                texMap.put(prefixes, curr);
            }
        }
    }
}
