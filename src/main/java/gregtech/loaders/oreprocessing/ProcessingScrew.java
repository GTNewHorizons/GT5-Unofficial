package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

public class ProcessingScrew implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingScrew() {
        OrePrefixes.screw.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!aMaterial.contains(SubTag.NO_WORKING)) {
            if (GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L))
                    .itemOutputs(GTUtility.copyAmount(1, aStack))
                    .duration(((int) Math.max(aMaterial.getMass() / 8L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(latheRecipes);
            }
            if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial))
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L),
                        GTProxy.tBits,
                        new Object[] { "fX", "X ", 'X', OrePrefixes.bolt.get(aMaterial) });
                }
        }
    }
}
