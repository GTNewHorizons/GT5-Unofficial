package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingStoneCobble implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingStoneCobble() {
        OrePrefixes.stoneCobble.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        // todo: not actually in the game. removed somewhere? better remove here then.
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(1L, aStack),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L))
            .itemOutputs(new ItemStack(Blocks.lever, 1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(1)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(8L, aStack), GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(new ItemStack(Blocks.furnace, 1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(sAssemblerRecipes);
        // todo: not actually in the game. removed somewhere? better remove here then.
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(7L, aStack),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))
            .itemOutputs(new ItemStack(Blocks.dropper, 1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(sAssemblerRecipes);
        // todo: not actually in the game. removed somewhere? better remove here then.
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(7L, aStack), new ItemStack(Items.bow, 1, 0))
            .itemOutputs(new ItemStack(Blocks.dispenser, 1))
            .fluidInputs(Materials.Redstone.getMolten(144L))
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(sAssemblerRecipes);
    }
}
