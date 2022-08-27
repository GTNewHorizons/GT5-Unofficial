/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.client.textures;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@SideOnly(Side.CLIENT)
public class PrefixTextureLinker implements Runnable {

    public static Map<OrePrefixes, HashMap<TextureSet, Textures.ItemIcons.CustomIcon>> texMap = new HashMap<>();
    public static Map<OrePrefixes, HashMap<TextureSet, IIconContainer>> texMapBlocks = new HashMap<>();
    public static Map<TextureSet, Short> blockTexMap = new HashMap<>();

    private static void fillBlockTexMap() {
        blockTexMap.put(TextureSet.SET_QUARTZ, TextureSet.INDEX_block4);
        Stream.of(WerkstoffLoader.blockCasing, WerkstoffLoader.blockCasingAdvanced)
                .forEach(prefixes -> {
                    HashMap<TextureSet, IIconContainer> curr = new HashMap<>();
                    Arrays.stream(TextureSet.class.getFields())
                            .filter(field -> field.getName().contains("SET"))
                            .forEach(SET -> {
                                try {
                                    curr.put(
                                            (TextureSet) SET.get(null),
                                            new Textures.BlockIcons.CustomIcon("materialicons/"
                                                    + SET.getName().substring(4) + "/" + prefixes));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            });
                    texMapBlocks.put(prefixes, curr);
                });
    }

    private static void fillItemTexMap() {
        Arrays.stream(OrePrefixes.values())
                .filter(prefixes -> prefixes != OrePrefixes.rod
                        && prefixes.mTextureIndex == -1
                        && Werkstoff.GenerationFeatures.getPrefixDataRaw(prefixes) != 0)
                .forEach(prefixes -> {
                    HashMap<TextureSet, Textures.ItemIcons.CustomIcon> curr = new HashMap<>();
                    Arrays.stream(TextureSet.class.getFields())
                            .filter(field -> field.getName().contains("SET"))
                            .forEach(SET -> {
                                try {
                                    curr.put(
                                            (TextureSet) SET.get(null),
                                            new Textures.ItemIcons.CustomIcon("materialicons/"
                                                    + SET.getName().substring(4) + "/" + prefixes));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            });
                    texMap.put(prefixes, curr);
                });
    }

    @Override
    public void run() {
        fillItemTexMap();
        fillBlockTexMap();
    }
}
