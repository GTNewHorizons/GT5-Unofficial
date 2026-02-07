package bartworks.common.loaders.recipes;

import static gregtech.api.enums.Mods.CropLoadCore;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.common.loaders.BioItemList;
import bartworks.util.BWUtil;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;

public class Extractor implements Runnable {

    @Override
    public void run() {
        List<ItemStack> oreCropVine = OreDictionary.getOres("cropVine", false);
        if (CropLoadCore.isModLoaded() && !oreCropVine.isEmpty()) {
            for (ItemStack stack : oreCropVine) {

                GTValues.RA.stdBuilder()
                    .itemInputs(BWUtil.setStackSize(stack, 12))
                    .itemOutputs(ItemList.DetergentPowder.get(1))
                    .duration(25 * SECONDS)
                    .eut((int) TierEU.RECIPE_HV)
                    .addTo(extractorRecipes);

            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Chip_Stemcell.get(1L))
            .itemOutputs(ItemList.PlasmaMembrane.get(1))
            .duration(25 * SECONDS)
            .eut((int) TierEU.RECIPE_LuV)
            .addTo(extractorRecipes);

    }
}
