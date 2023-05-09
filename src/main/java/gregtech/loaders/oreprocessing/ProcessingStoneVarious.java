package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;

public class ProcessingStoneVarious implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingStoneVarious() {
        OrePrefixes.stone.add(this);
        OrePrefixes.stoneCobble.add(this);
        OrePrefixes.stoneBricks.add(this);
        OrePrefixes.stoneChiseled.add(this);
        OrePrefixes.stoneCracked.add(this);
        OrePrefixes.stoneMossy.add(this);
        OrePrefixes.stoneMossyBricks.add(this);
        OrePrefixes.stoneSmooth.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, gregtech.api.enums.Materials aMaterial, String aOreDictName,
        String aModName, ItemStack aStack) {
        if (aPrefix == OrePrefixes.stoneSmooth) {

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(new ItemStack(Blocks.stone_button, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(2L, aStack), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(new ItemStack(Blocks.stone_pressure_plate, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);
        }
    }
}
