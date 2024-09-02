package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTUtility;

public class ProcessingStoneCobble implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingStoneCobble() {
        OrePrefixes.stoneCobble.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(8, aStack), GTUtility.getIntegratedCircuit(8))
            .itemOutputs(new ItemStack(Blocks.furnace, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);
    }
}
