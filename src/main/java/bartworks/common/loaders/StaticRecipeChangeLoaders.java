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

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;

public class StaticRecipeChangeLoaders {

    private StaticRecipeChangeLoaders() {}

    public static void unificationRecipeEnforcer() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            if (material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) continue;
            StaticRecipeChangeLoaders.runMaterialLinker(material);
            if (!Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.ENFORCE_ORE_DICT_UNIFICATION))) continue;
            StaticRecipeChangeLoaders.runUnficationDeleter(material);
        }
    }

    private static void runUnficationDeleter(Material material) {
        String internalName = MaterialUtils.internalName(material);
        for (OrePrefixes prefixes : OrePrefixes.VALUES)
            if (Materials2WerkstoffIndex.generatesPrefix(material, prefixes)) {
                GTOreDictUnificator.set(prefixes, material, MU.stack(prefixes, material, 1), true, true);
                for (ItemStack stack : OreDictionary.getOres(prefixes + internalName)) {
                    GTOreDictUnificator.addAssociation(prefixes, material, stack, false);
                    GTOreDictUnificator.getAssociation(stack).mUnificationTarget = MU.stack(prefixes, material, 1);
                }
            }
    }

    private static void runMaterialLinker(Material material) {
        String internalName = MaterialUtils.internalName(material);
        for (OrePrefixes prefixes : OrePrefixes.VALUES)
            if (Materials2WerkstoffIndex.generatesPrefix(material, prefixes)) {
                GTOreDictUnificator.set(prefixes, material, MU.stack(prefixes, material, 1), true, true);
                for (ItemStack stack : OreDictionary.getOres(prefixes + internalName)) {
                    GTOreDictUnificator.addAssociation(prefixes, material, stack, false);
                }
            }
    }

    public static void addElectricImplosionCompressorRecipes() {
        // Custom EIC recipes.
        new ElectricImplosionCompressorRecipes().run();
    }
}
