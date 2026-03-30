/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bwcrossmod.GTpp.loader;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.util.RegistryNamespaced;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.util.log.DebugLog;
import bwcrossmod.BartWorksCrossmod;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.item.base.rods.BaseItemRod;
import gtPlusPlus.core.item.base.rods.BaseItemRodLong;
import gtPlusPlus.core.material.Material;

public class RadioHatchCompat {

    public static HashSet<String> TranslateSet = new HashSet<>();

    @SuppressWarnings("unchecked")
    public static void run() {
        DebugLog.log("Starting Generation of missing GT++ rods/longrods");
        try {
            Field cOwners = GameData.class.getDeclaredField("customOwners");
            cOwners.setAccessible(true);
            Field map = RegistryNamespaced.class.getDeclaredField("field_148758_b");
            map.setAccessible(true);
            Map<Item, String> UniqueIdentifierMap = (Map<Item, String>) map.get(GameData.getItemRegistry());

            Map<GameRegistry.UniqueIdentifier, ModContainer> ownerItems = (Map<GameRegistry.UniqueIdentifier, ModContainer>) cOwners
                .get(null);
            ModContainer gtpp = null;
            ModContainer bartworks = null;

            for (ModContainer container : Loader.instance()
                .getModList()) {
                if (gtpp != null && bartworks != null) break;
                if (BartWorksCrossmod.MOD_ID.equalsIgnoreCase(container.getModId())) {
                    bartworks = container;
                } else if (Mods.GTPlusPlus.ID.equalsIgnoreCase(container.getModId())) {
                    gtpp = container;
                }
            }

            for (Material mats : Material.mMaterialMap) {
                if (!mats.isRadioactive) continue;

                if (OreDictionary.getOres("stick" + mats.getUnlocalizedName())
                    .isEmpty()) {
                    Item itemRod = new BaseItemRod(mats);
                    UniqueIdentifierMap.replace(itemRod, "miscutils:" + itemRod.getUnlocalizedName());
                    GameRegistry.UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(itemRod);
                    ownerItems.replace(ui, bartworks, gtpp);
                    String translate = itemRod.getUnlocalizedName() + ".name=" + mats.getDefaultLocalName() + " Rod";
                    RadioHatchCompat.TranslateSet.add(translate);
                    DebugLog.log(translate);
                    DebugLog.log("Generate: " + BaseItemComponent.ComponentTypes.ROD + mats.getUnlocalizedName());
                }

                if (OreDictionary.getOres("stickLong" + mats.getUnlocalizedName())
                    .isEmpty()) {
                    Item itemRodLong = new BaseItemRodLong(mats);
                    UniqueIdentifierMap.replace(itemRodLong, "miscutils:" + itemRodLong.getUnlocalizedName());
                    GameRegistry.UniqueIdentifier ui2 = GameRegistry.findUniqueIdentifierFor(itemRodLong);
                    ownerItems.replace(ui2, bartworks, gtpp);
                    DebugLog.log("Generate: " + BaseItemComponent.ComponentTypes.RODLONG + mats.getUnlocalizedName());
                }

            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
