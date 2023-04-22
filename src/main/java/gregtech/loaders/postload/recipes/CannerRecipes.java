package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCannerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class CannerRecipes implements Runnable {

    @Override
    public void run() {
        // fuel rod canner recipes

        if (IndustrialCraft2.isModLoaded()) {
            // todo: remove tiny dust in this recipe
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_ModHandler.getIC2Item("fuelRod", 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 1L))
                .itemOutputs(GT_ModHandler.getIC2Item("reactorLithiumCell", 1, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(16 * TICKS)
                .eut(64)
                .addTo(sCannerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_ModHandler.getIC2Item("fuelRod", 1),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 3))
                .itemOutputs(ItemList.ThoriumCell_1.get(1L))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(sCannerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("UranFuel", 1))
                .itemOutputs(ItemList.Uraniumcell_1.get(1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(sCannerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("MOXFuel", 1))
                .itemOutputs(ItemList.Moxcell_1.get(1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(sCannerRecipes);
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 3))
            .itemOutputs(ItemList.NaquadahCell_1.get(1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(sCannerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 3))
            .itemOutputs(ItemList.MNqCell_1.get(1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(sCannerRecipes);
    }
}
