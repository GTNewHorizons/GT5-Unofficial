package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;

public class ProcessingStoneCobble implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingStoneCobble() {
        OrePrefixes.stoneCobble.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(8, aStack), GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(new ItemStack(Blocks.furnace, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(sAssemblerRecipes);
    }
}
