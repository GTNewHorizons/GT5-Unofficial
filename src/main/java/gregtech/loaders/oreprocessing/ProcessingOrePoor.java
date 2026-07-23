package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingOrePoor implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingOrePoor INSTANCE;

    public ProcessingOrePoor() {
        INSTANCE = this;
        OrePrefixes.orePoor.add(this);
        OrePrefixes.oreSmall.add(this);
        OrePrefixes.oreNormal.add(this);
        OrePrefixes.oreRich.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        int multiplier = 1;
        switch (prefix.getName()) {
            case "oreSmall":
                multiplier = 1;
                break;
            case "orePoor":
                multiplier = 2;
                break;
            case "oreNormal":
                multiplier = 3;
                break;
            case "oreRich":
                multiplier = 4;
            default:
                break;
        }

        if (MU.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustTiny, material, multiplier))
            .duration(10)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dustTiny, material, 2 * multiplier),
                GTOreDictUnificator
                    .get(OrePrefixes.dustTiny, GTUtility.selectItemInList(0, material, MU.oreByProducts(material)), 1L),
                GTOreDictUnificator.getDust(prefix.mSecondaryMaterial))
            .outputChances(10000, 100 * 5 * multiplier, 10000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        if (MU.hasFlag(material, GTMaterialFlag.NO_SMELTING)) GTModHandler.addSmeltingRecipe(
            GTUtility.copyAmount(1, stack),
            GTOreDictUnificator.get(OrePrefixes.nugget, MU.directSmelting(material), multiplier));
    }
}
