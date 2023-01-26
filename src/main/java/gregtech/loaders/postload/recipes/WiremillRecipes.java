package gregtech.loaders.postload.recipes;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class WiremillRecipes implements Runnable{
    @Override
    public void run() {
        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 1L),
                GT_ModHandler.getIC2Item("copperCableItem", 3L),
                100,
                2);
            GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnnealedCopper, 1L),
                GT_ModHandler.getIC2Item("copperCableItem", 3L),
                100,
                2);
            GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 1L),
                GT_ModHandler.getIC2Item("tinCableItem", 4L),
                150,
                1);
            GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                GT_ModHandler.getIC2Item("ironCableItem", 6L),
                200,
                2);
            GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                GT_ModHandler.getIC2Item("ironCableItem", 6L),
                200,
                2);
            GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L),
                GT_ModHandler.getIC2Item("goldCableItem", 6L),
                200,
                1);
        }
    }
}
