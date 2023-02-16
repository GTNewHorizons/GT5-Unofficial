package gregtech.loaders.postload.recipes;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class AlloySmelterRecipes implements Runnable {

    private final MaterialStack[][] mAlloySmelterList = {
            { new MaterialStack(Materials.Tetrahedrite, 3L), new MaterialStack(Materials.Tin, 1L),
                    new MaterialStack(Materials.Bronze, 3L) },
            { new MaterialStack(Materials.Tetrahedrite, 3L), new MaterialStack(Materials.Zinc, 1L),
                    new MaterialStack(Materials.Brass, 3L) },
            { new MaterialStack(Materials.Copper, 3L), new MaterialStack(Materials.Tin, 1L),
                    new MaterialStack(Materials.Bronze, 4L) },
            { new MaterialStack(Materials.Copper, 3L), new MaterialStack(Materials.Zinc, 1L),
                    new MaterialStack(Materials.Brass, 4L) },
            { new MaterialStack(Materials.Copper, 1L), new MaterialStack(Materials.Nickel, 1L),
                    new MaterialStack(Materials.Cupronickel, 2L) },
            { new MaterialStack(Materials.Copper, 1L), new MaterialStack(Materials.Redstone, 4L),
                    new MaterialStack(Materials.RedAlloy, 1L) },
            { new MaterialStack(Materials.AnnealedCopper, 3L), new MaterialStack(Materials.Tin, 1L),
                    new MaterialStack(Materials.Bronze, 4L) },
            { new MaterialStack(Materials.AnnealedCopper, 3L), new MaterialStack(Materials.Zinc, 1L),
                    new MaterialStack(Materials.Brass, 4L) },
            { new MaterialStack(Materials.AnnealedCopper, 1L), new MaterialStack(Materials.Nickel, 1L),
                    new MaterialStack(Materials.Cupronickel, 2L) },
            { new MaterialStack(Materials.AnnealedCopper, 1L), new MaterialStack(Materials.Redstone, 4L),
                    new MaterialStack(Materials.RedAlloy, 1L) },
            { new MaterialStack(Materials.Iron, 1L), new MaterialStack(Materials.Tin, 1L),
                    new MaterialStack(Materials.TinAlloy, 2L) },
            { new MaterialStack(Materials.WroughtIron, 1L), new MaterialStack(Materials.Tin, 1L),
                    new MaterialStack(Materials.TinAlloy, 2L) },
            { new MaterialStack(Materials.Iron, 2L), new MaterialStack(Materials.Nickel, 1L),
                    new MaterialStack(Materials.Invar, 3L) },
            { new MaterialStack(Materials.WroughtIron, 2L), new MaterialStack(Materials.Nickel, 1L),
                    new MaterialStack(Materials.Invar, 3L) },
            { new MaterialStack(Materials.Tin, 9L), new MaterialStack(Materials.Antimony, 1L),
                    new MaterialStack(Materials.SolderingAlloy, 10L) },
            { new MaterialStack(Materials.Lead, 4L), new MaterialStack(Materials.Antimony, 1L),
                    new MaterialStack(Materials.BatteryAlloy, 5L) },
            { new MaterialStack(Materials.Gold, 1L), new MaterialStack(Materials.Silver, 1L),
                    new MaterialStack(Materials.Electrum, 2L) },
            { new MaterialStack(Materials.Magnesium, 1L), new MaterialStack(Materials.Aluminium, 2L),
                    new MaterialStack(Materials.Magnalium, 3L) },
            { new MaterialStack(Materials.Silver, 1L), new MaterialStack(Materials.Electrotine, 4L),
                    new MaterialStack(Materials.BlueAlloy, 1L) },
            { new MaterialStack(Materials.Boron, 1L), new MaterialStack(Materials.Glass, 7L),
                    new MaterialStack(Materials.BorosilicateGlass, 8L) } };

    @Override
    public void run() {
        for (MaterialStack[] tMats : mAlloySmelterList) {
            ItemStack tDust1 = GT_OreDictUnificator.get(OrePrefixes.dust, tMats[0].mMaterial, tMats[0].mAmount);
            ItemStack tDust2 = GT_OreDictUnificator.get(OrePrefixes.dust, tMats[1].mMaterial, tMats[1].mAmount);
            ItemStack tIngot1 = GT_OreDictUnificator.get(OrePrefixes.ingot, tMats[0].mMaterial, tMats[0].mAmount);
            ItemStack tIngot2 = GT_OreDictUnificator.get(OrePrefixes.ingot, tMats[1].mMaterial, tMats[1].mAmount);
            ItemStack tOutputIngot = GT_OreDictUnificator.get(OrePrefixes.ingot, tMats[2].mMaterial, tMats[2].mAmount);
            if (tOutputIngot != GT_Values.NI) {
                GT_ModHandler
                        .addAlloySmelterRecipe(tIngot1, tDust2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
                GT_ModHandler
                        .addAlloySmelterRecipe(tIngot1, tIngot2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
                GT_ModHandler
                        .addAlloySmelterRecipe(tDust1, tIngot2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
                GT_ModHandler
                        .addAlloySmelterRecipe(tDust1, tDust2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
            }
        }

        GT_Values.RA.addAlloySmelterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 2L),
                ItemList.TE_Hardened_Glass.get(2L),
                200,
                16);
        GT_Values.RA.addAlloySmelterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Lead, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 2L),
                ItemList.TE_Hardened_Glass.get(2L),
                200,
                16);
        GT_Values.RA.addAlloySmelterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L),
                200,
                8); // We use rubber
    }
}
