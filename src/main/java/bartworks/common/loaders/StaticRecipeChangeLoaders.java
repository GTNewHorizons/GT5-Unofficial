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

package bartworks.common.loaders;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.Element;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

public class StaticRecipeChangeLoaders {

    private StaticRecipeChangeLoaders() {}

    public static void unificationRecipeEnforcer() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            StaticRecipeChangeLoaders.runMaterialLinker(werkstoff);
            if (!werkstoff.getGenerationFeatures().enforceUnification) continue;
            StaticRecipeChangeLoaders.runUnficationDeleter(werkstoff);
        }
    }

    private static void runUnficationDeleter(Werkstoff werkstoff) {
        if (werkstoff.getType() == Werkstoff.Types.ELEMENT && werkstoff.getBridgeMaterial() != null
            && Element.get(werkstoff.getToolTip()) != Element._NULL) {
            werkstoff.getBridgeMaterial().mElement = Element.get(werkstoff.getToolTip());
            Element.get(werkstoff.getToolTip()).mLinkedMaterials = new ArrayList<>();
            Element.get(werkstoff.getToolTip()).mLinkedMaterials.add(werkstoff.getBridgeMaterial());
        }

        for (OrePrefixes prefixes : OrePrefixes.VALUES) if (werkstoff.hasItemType(prefixes)) {
            GTOreDictUnificator.set(prefixes, werkstoff.getBridgeMaterial(), werkstoff.get(prefixes), true, true);
            for (ItemStack stack : OreDictionary.getOres(prefixes + werkstoff.getVarName())) {
                GTOreDictUnificator.addAssociation(prefixes, werkstoff.getBridgeMaterial(), stack, false);
                GTOreDictUnificator.getAssociation(stack).mUnificationTarget = werkstoff.get(prefixes);
            }
        }
    }

    private static void runMaterialLinker(Werkstoff werkstoff) {
        if (werkstoff.getType() == Werkstoff.Types.ELEMENT && werkstoff.getBridgeMaterial() != null
            && Element.get(werkstoff.getToolTip()) != Element._NULL) {
            werkstoff.getBridgeMaterial().mElement = Element.get(werkstoff.getToolTip());
            Element.get(werkstoff.getToolTip()).mLinkedMaterials = new ArrayList<>();
            Element.get(werkstoff.getToolTip()).mLinkedMaterials.add(werkstoff.getBridgeMaterial());
        }

        for (OrePrefixes prefixes : OrePrefixes.VALUES)
            if (werkstoff.hasItemType(prefixes) && werkstoff.getBridgeMaterial() != null) {
                GTOreDictUnificator.set(prefixes, werkstoff.getBridgeMaterial(), werkstoff.get(prefixes), true, true);
                for (ItemStack stack : OreDictionary.getOres(prefixes + werkstoff.getVarName())) {
                    GTOreDictUnificator.addAssociation(prefixes, werkstoff.getBridgeMaterial(), stack, false);
                }
            }
    }

    public static void addElectricImplosionCompressorRecipes() {
        // Custom EIC recipes.
        new ElectricImplosionCompressorRecipes().run();
    }
}
