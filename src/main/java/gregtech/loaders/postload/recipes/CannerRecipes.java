package gregtech.loaders.postload.recipes;

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
        GT_Values.RA.addCannerRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 1L),
                GT_ModHandler.getIC2Item("reactorLithiumCell", 1, 1),
                null,
                16,
                64);

        GT_Values.RA.addCannerRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 3),
                ItemList.ThoriumCell_1.get(1L),
                null,
                30,
                16);
        GT_Values.RA.addCannerRecipe(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 3),
                ItemList.NaquadahCell_1.get(1L),
                null,
                30,
                16);
        GT_Values.RA.addCannerRecipe(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 3),
                ItemList.MNqCell_1.get(1L),
                null,
                30,
                16);
        GT_Values.RA.addCannerRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_ModHandler.getIC2Item("UranFuel", 1),
                ItemList.Uraniumcell_1.get(1),
                null,
                30,
                16);
        GT_Values.RA.addCannerRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_ModHandler.getIC2Item("MOXFuel", 1),
                ItemList.Moxcell_1.get(1),
                null,
                30,
                16);
    }
}
