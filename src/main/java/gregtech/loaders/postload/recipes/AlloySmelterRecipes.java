package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.MaterialStack;
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
        for (MaterialStack[] materials : mAlloySmelterList) {
            ItemStack dust1 = GT_OreDictUnificator.get(OrePrefixes.dust, materials[0].mMaterial, materials[0].mAmount);
            ItemStack dust2 = GT_OreDictUnificator.get(OrePrefixes.dust, materials[1].mMaterial, materials[1].mAmount);
            ItemStack ingot1 = GT_OreDictUnificator.get(
                    OrePrefixes.ingot,
                    materials[0].mMaterial,
                    materials[0].mAmount);
            ItemStack ingot2 = GT_OreDictUnificator.get(
                    OrePrefixes.ingot,
                    materials[1].mMaterial,
                    materials[1].mAmount);
            ItemStack outputIngot = GT_OreDictUnificator.get(
                    OrePrefixes.ingot,
                    materials[2].mMaterial,
                    materials[2].mAmount);
            if (outputIngot != GT_Values.NI) {
                if (ingot1 != null) {
                    GT_Values.RA.stdBuilder()
                                .itemInputs(ingot1, dust2)
                                .itemOutputs(outputIngot)
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((int) materials[2].mAmount * 50)
                                .eut(16)
                                .addTo(sAlloySmelterRecipes);
                    if (ingot2 != null) {
                        GT_Values.RA.stdBuilder()
                                    .itemInputs(ingot1, ingot2)
                                    .itemOutputs(outputIngot)
                                    .noFluidInputs()
                                    .noFluidOutputs()
                                    .duration((int) materials[2].mAmount * 50)
                                    .eut(16)
                                    .addTo(sAlloySmelterRecipes);
                    }
                }

                if (ingot2 != null) {
                    GT_Values.RA.stdBuilder()
                                .itemInputs(dust1, ingot2)
                                .itemOutputs(outputIngot)
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((int) materials[2].mAmount * 50)
                                .eut(16)
                                .addTo(sAlloySmelterRecipes);
                }

                GT_Values.RA.stdBuilder()
                            .itemInputs(dust1, dust2)
                            .itemOutputs(outputIngot)
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration((int) materials[2].mAmount * 50)
                            .eut(16)
                            .addTo(sAlloySmelterRecipes);

            }
        }
		
        // We use rubber
        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(10 * SECONDS)
                    .eut(8)
                    .addTo(sAlloySmelterRecipes);
    }
}
