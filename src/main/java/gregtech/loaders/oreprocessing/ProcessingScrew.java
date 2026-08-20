package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingScrew implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingScrew INSTANCE;

    public ProcessingScrew() {
        INSTANCE = this;
        OrePrefixes.screw.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            if (GTOreDictUnificator.get(OrePrefixes.bolt, material, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.bolt, material, 1L))
                    .itemOutputs(GTUtility.copyAmount(1, stack))
                    .duration(((int) Math.max(MaterialUtils.mass(material) / 8L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(latheRecipes);
            }
            if (MaterialUtils.unifiable(material)) {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.screw, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "fX", "X ", 'X', MaterialParts.craftIngredient(OrePrefixes.bolt, material) });
                }
            }
        }
    }
}
