package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.GalacticraftMars;
import static gregtech.api.util.GT_ModHandler.getModItem;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class ImplosionCompressorRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addImplosionRecipe(
                ItemList.IC2_Compressed_Coal_Chunk.get(1L),
                8,
                ItemList.IC2_Industrial_Diamond.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L));

        GT_Values.RA.addImplosionRecipe(
                ItemList.Ingot_IridiumAlloy.get(1L),
                8,
                GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Iridium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L));

        if (GalacticraftMars.isModLoaded()) {

            GT_Values.RA.addImplosionRecipe(
                    ItemList.Ingot_Heavy1.get(1L),
                    8,
                    getModItem("GalacticraftCore", "item.heavyPlating", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.StainlessSteel, 1L));
            GT_Values.RA.addImplosionRecipe(
                    ItemList.Ingot_Heavy2.get(1L),
                    16,
                    getModItem("GalacticraftMars", "item.null", 1L, 3),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 2L));
            GT_Values.RA.addImplosionRecipe(
                    ItemList.Ingot_Heavy3.get(1L),
                    24,
                    getModItem("GalacticraftMars", "item.itemBasicAsteroids", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Platinum, 3L));
        }
    }
}
