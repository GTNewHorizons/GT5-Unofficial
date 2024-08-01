package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.recipe.RecipeMaps.neutroniumCompressorRecipes;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

//TODO: Move all of this to coremod later

public class NeutroniumCompressorRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(7296, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1)))
            .itemOutputs(getModItem(Avaritia.ID, "Singularity", 1L, 0))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1215, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Gold, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1215, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lapis, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(7296, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Redstone, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1215, GT_OreDictUnificator.get(OrePrefixes.block, Materials.NetherQuartz, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(3648, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Copper, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(3648, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Tin, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(3648, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lead, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(7296, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Silver, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(3648, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Nickel, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(608, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Enderium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(3648, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(729, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Emerald, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(729, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Diamond, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1824, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Aluminium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1824, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Brass, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1824, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Bronze, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(7296, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(912, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Electrum, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1824, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Invar, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(3648, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Magnesium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(406, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Osmium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(608, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Olivine, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(608, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Ruby, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(608, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Sapphire, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(912, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Steel, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(2024, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Titanium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(244, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Tungsten, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(507, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Uranium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(3648, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Zinc, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(365, GT_OreDictUnificator.get(OrePrefixes.block, Materials.TricalciumPhosphate, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(136, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Palladium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(153, GT_OreDictUnificator.get(OrePrefixes.block, Materials.DamascusSteel, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(304, GT_OreDictUnificator.get(OrePrefixes.block, Materials.BlackSteel, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(16, GT_OreDictUnificator.get(OrePrefixes.block, Materials.ElectrumFlux, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1824, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Cinnabar, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(406, GT_OreDictUnificator.get(OrePrefixes.block, Materials.ShadowSteel, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(62, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iridium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(512, GT_OreDictUnificator.get(OrePrefixes.block, Materials.NetherStar, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(406, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Platinum, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(66, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Naquadria, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(244, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Plutonium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(912, GT_OreDictUnificator.get(OrePrefixes.block, Materials.MeteoricIron, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(203, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Desh, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(62, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Europium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1296, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Draconium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(760, GT_OreDictUnificator.get(OrePrefixes.block, Materials.DraconiumAwakened, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(912, GT_OreDictUnificator.get(OrePrefixes.block, Materials.ConductiveIron, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(912, GT_OreDictUnificator.get(OrePrefixes.block, Materials.ElectricalSteel, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(191, GT_OreDictUnificator.get(OrePrefixes.block, Materials.EnergeticAlloy, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(912, GT_OreDictUnificator.get(OrePrefixes.block, Materials.DarkSteel, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(912, GT_OreDictUnificator.get(OrePrefixes.block, Materials.PulsatingIron, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(912, GT_OreDictUnificator.get(OrePrefixes.block, Materials.RedstoneAlloy, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(456, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Soularium, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(145, GT_OreDictUnificator.get(OrePrefixes.block, Materials.VibrantAlloy, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(66, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Unstableingot, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1215, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Electrotine, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1824, GT_OreDictUnificator.get(OrePrefixes.block, Materials.AluminiumBrass, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(229, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Alumite, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(304, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Ardite, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(1824, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Cobalt, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(608, GT_OreDictUnificator.get(OrePrefixes.block, Materials.EnderPearl, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmountUnsafe(308, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Manyullyn, 1)))
            .itemOutputs(ItemList.Sensor_IV.get(1))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(neutroniumCompressorRecipes);
        //TODO INFCAT
    }


}
