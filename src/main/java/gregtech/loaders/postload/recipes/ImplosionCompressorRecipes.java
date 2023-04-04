package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sImplosionRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class ImplosionCompressorRecipes implements Runnable {

    @Override
    public void run() {
        addImplosionRecipe(
                ItemList.IC2_Compressed_Coal_Chunk.get(1L),
                8,
                new ItemStack[] { ItemList.IC2_Industrial_Diamond.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L) });

        addImplosionRecipe(
                ItemList.Ingot_IridiumAlloy.get(1L),
                8,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Iridium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L) });

        if (GalacticraftMars.isModLoaded()) {

            addImplosionRecipe(
                    ItemList.Ingot_Heavy1.get(1L),
                    8,
                    new ItemStack[] { getModItem(GalacticraftCore.ID, "item.heavyPlating", 1L),
                            GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.StainlessSteel, 1L) });

            addImplosionRecipe(
                    ItemList.Ingot_Heavy2.get(1L),
                    16,
                    new ItemStack[] { getModItem(GalacticraftMars.ID, "item.null", 1L, 3),
                            GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 2L) });

            addImplosionRecipe(
                    ItemList.Ingot_Heavy3.get(1L),
                    24,
                    new ItemStack[] { getModItem(GalacticraftMars.ID, "item.itemBasicAsteroids", 1L),
                            GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Platinum, 3L) });
        }
    }

    public void addImplosionRecipe(ItemStack input, int explosiveAmount, ItemStack[] outputs) {

        int tExplosives = Math.min(explosiveAmount, 64);
        int tGunpowder = tExplosives << 1; // Worst
        int tDynamite = Math.max(1, tExplosives >> 1); // good
        int tTNT = tExplosives; // Slightly better
        int tITNT = Math.max(1, tExplosives >> 2); // the best

        if (tGunpowder < 65) {
            GT_Values.RA.stdBuilder()
                        .itemInputs(input, ItemList.Block_Powderbarrel.get(tGunpowder))
                        .itemOutputs(outputs)
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(1 * SECONDS)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(sImplosionRecipes);
        }

        if (tDynamite < 17) {
            GT_Values.RA.stdBuilder()
                        .itemInputs(input, GT_ModHandler.getIC2Item("dynamite", tDynamite, null))
                        .itemOutputs(outputs)
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(1 * SECONDS)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(sImplosionRecipes);
        }

        GT_Values.RA.stdBuilder()
                    .itemInputs(input, new ItemStack(Blocks.tnt, tTNT))
                    .itemOutputs(outputs)
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(sImplosionRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(input, GT_ModHandler.getIC2Item("industrialTnt", tITNT))
                    .itemOutputs(outputs)
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(sImplosionRecipes);
    }
}
