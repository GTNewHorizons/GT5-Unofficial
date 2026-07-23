package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.MaterialStack;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTOreDictUnificator;

public class AlloySmelterRecipes implements Runnable {

    private final MaterialStack[][] mAlloySmelterList = {
        { new MaterialStack(Materials2Materials.Tetrahedrite, 3L), new MaterialStack(Materials2Materials.Tin, 1L),
            new MaterialStack(Materials2Materials.Bronze, 3L) },
        { new MaterialStack(Materials2Materials.Tetrahedrite, 3L), new MaterialStack(Materials2Materials.Zinc, 1L),
            new MaterialStack(Materials2Materials.Brass, 3L) },
        { new MaterialStack(Materials2Materials.Copper, 3L), new MaterialStack(Materials2Materials.Tin, 1L),
            new MaterialStack(Materials2Materials.Bronze, 4L) },
        { new MaterialStack(Materials2Materials.Copper, 3L), new MaterialStack(Materials2Materials.Zinc, 1L),
            new MaterialStack(Materials2Materials.Brass, 4L) },
        { new MaterialStack(Materials2Materials.Copper, 1L), new MaterialStack(Materials2Materials.Nickel, 1L),
            new MaterialStack(Materials2Materials.Cupronickel, 2L) },
        { new MaterialStack(Materials2Materials.Copper, 1L), new MaterialStack(Materials2Materials.Redstone, 4L),
            new MaterialStack(Materials2Materials.RedAlloy, 1L) },
        { new MaterialStack(Materials2Materials.AnnealedCopper, 3L), new MaterialStack(Materials2Materials.Tin, 1L),
            new MaterialStack(Materials2Materials.Bronze, 4L) },
        { new MaterialStack(Materials2Materials.AnnealedCopper, 3L), new MaterialStack(Materials2Materials.Zinc, 1L),
            new MaterialStack(Materials2Materials.Brass, 4L) },
        { new MaterialStack(Materials2Materials.AnnealedCopper, 1L), new MaterialStack(Materials2Materials.Nickel, 1L),
            new MaterialStack(Materials2Materials.Cupronickel, 2L) },
        { new MaterialStack(Materials2Materials.AnnealedCopper, 1L),
            new MaterialStack(Materials2Materials.Redstone, 4L), new MaterialStack(Materials2Materials.RedAlloy, 1L) },
        { new MaterialStack(Materials2Materials.Iron, 1L), new MaterialStack(Materials2Materials.Tin, 1L),
            new MaterialStack(Materials2Materials.TinAlloy, 2L) },
        { new MaterialStack(Materials2Materials.CastIron, 1L), new MaterialStack(Materials2Materials.Tin, 1L),
            new MaterialStack(Materials2Materials.TinAlloy, 2L) },
        { new MaterialStack(Materials2Materials.Iron, 2L), new MaterialStack(Materials2Materials.Nickel, 1L),
            new MaterialStack(Materials2Materials.Invar, 3L) },
        { new MaterialStack(Materials2Materials.CastIron, 2L), new MaterialStack(Materials2Materials.Nickel, 1L),
            new MaterialStack(Materials2Materials.Invar, 3L) },
        { new MaterialStack(Materials2Materials.Tin, 9L), new MaterialStack(Materials2Materials.Antimony, 1L),
            new MaterialStack(Materials2Materials.SolderingAlloy, 10L) },
        { new MaterialStack(Materials2Materials.Lead, 4L), new MaterialStack(Materials2Materials.Antimony, 1L),
            new MaterialStack(Materials2Materials.BatteryAlloy, 5L) },
        { new MaterialStack(Materials2Materials.Gold, 1L), new MaterialStack(Materials2Materials.Silver, 1L),
            new MaterialStack(Materials2Materials.Electrum, 2L) },
        { new MaterialStack(Materials2Materials.Magnesium, 1L), new MaterialStack(Materials2Materials.Aluminium, 2L),
            new MaterialStack(Materials2Materials.Magnalium, 3L) },
        { new MaterialStack(Materials2Materials.Silver, 1L), new MaterialStack(Materials2Materials.Electrotine, 4L),
            new MaterialStack(Materials2Materials.BlueAlloy, 1L) },
        { new MaterialStack(Materials2Materials.Boron, 1L), new MaterialStack(Materials2Materials.Glass, 7L),
            new MaterialStack(Materials2Materials.BorosilicateGlass, 8L) } };

    @Override
    public void run() {
        for (MaterialStack[] materials : mAlloySmelterList) {
            ItemStack dust1 = GTOreDictUnificator.get(OrePrefixes.dust, materials[0].mMaterial, materials[0].mAmount);
            ItemStack dust2 = GTOreDictUnificator.get(OrePrefixes.dust, materials[1].mMaterial, materials[1].mAmount);
            ItemStack ingot1 = GTOreDictUnificator.get(OrePrefixes.ingot, materials[0].mMaterial, materials[0].mAmount);
            ItemStack ingot2 = GTOreDictUnificator.get(OrePrefixes.ingot, materials[1].mMaterial, materials[1].mAmount);
            ItemStack outputIngot = GTOreDictUnificator
                .get(OrePrefixes.ingot, materials[2].mMaterial, materials[2].mAmount);
            if (outputIngot != GTValues.NI) {
                if (ingot1 != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(ingot1, dust2)
                        .itemOutputs(outputIngot)
                        .duration((int) materials[2].mAmount * 50)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(alloySmelterRecipes);
                    if (ingot2 != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(ingot1, ingot2)
                            .itemOutputs(outputIngot)
                            .duration((int) materials[2].mAmount * 50)
                            .eut(TierEU.RECIPE_LV / 2)
                            .addTo(alloySmelterRecipes);
                    }
                }

                if (ingot2 != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(dust1, ingot2)
                        .itemOutputs(outputIngot)
                        .duration((int) materials[2].mAmount * 50)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(alloySmelterRecipes);
                }

                GTValues.RA.stdBuilder()
                    .itemInputs(dust1, dust2)
                    .itemOutputs(outputIngot)
                    .duration((int) materials[2].mAmount * 50)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(alloySmelterRecipes);

            }
        }

        // We use rubber
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get("dustRawRubber", 3L),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (1L)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Rubber, Materials2Shapes.ingot, (int) (1L)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(alloySmelterRecipes);

        // Bartworks Glass Tube
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dust, (int) (2L)),
                ItemList.Shape_Mold_Rod_Long.get(0L))
            .itemOutputs(new ItemStack(ItemRegistry.PUMPPARTS, 1, 0))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(alloySmelterRecipes);

        // From ProcessingDye - glass dyeing
        for (Dyes dye : Dyes.VALUES) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dust, (int) (8L)),
                    new OreDictItemStack(dye.name(), 1))
                .itemOutputs(new net.minecraft.item.ItemStack(Blocks.stained_glass, 8, 15 - dye.mIndex))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(alloySmelterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    new net.minecraft.item.ItemStack(Blocks.glass, 8, WILDCARD),
                    new OreDictItemStack(dye.name(), 1))
                .itemOutputs(new net.minecraft.item.ItemStack(Blocks.stained_glass, 8, 15 - dye.mIndex))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(alloySmelterRecipes);
        }
    }
}
