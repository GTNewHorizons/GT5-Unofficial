package gtPlusPlus.xmod.railcraft;

import static gregtech.api.enums.Materials.BioDiesel;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.item.base.BaseItemBurnable;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.railcraft.utils.RailcraftUtils;
import mods.railcraft.api.fuel.FuelManager;
import mods.railcraft.common.core.RailcraftConfig;

public class HandlerRailcraft {

    public static void preInit() {
        GregtechItemList.CustomCoalCoke.set(
            new BaseItemBurnable("itemCoalCoke", tabMisc, 64, 0, "Used for metallurgy.", "fuelCoke", 3200, 0)
                .setTextureName(GTPlusPlus.ID + ":burnables/itemCoalCoke"));

        GregtechItemList.CactusCharcoal.set(
            new BaseItemBurnable(
                "itemCactusCharcoal",
                tabMisc,
                64,
                0,
                "Used for smelting.",
                "fuelCactusCharcoal",
                400,
                0).setTextureName(GTPlusPlus.ID + ":burnables/itemCactusCharcoal"));

        GregtechItemList.SugarCharcoal.set(
            new BaseItemBurnable("itemSugarCharcoal", tabMisc, 64, 0, "Used for smelting.", "fuelSugarCharcoal", 400, 0)
                .setTextureName(GTPlusPlus.ID + ":burnables/itemSugarCharcoal"));

        GregtechItemList.CactusCoke.set(
            new BaseItemBurnable("itemCactusCoke", tabMisc, 64, 0, "Used for smelting.", "fuelCactusCoke", 800, 0)
                .setTextureName(GTPlusPlus.ID + ":burnables/itemCactusCoke"));

        GregtechItemList.SugarCoke.set(
            new BaseItemBurnable("itemSugarCoke", tabMisc, 64, 0, "Used for smelting.", "fuelSugarCoke", 800, 0)
                .setTextureName(GTPlusPlus.ID + ":burnables/itemSugarCoke"));

        GTOreDictUnificator.registerOre("itemCharcoalCactus", GregtechItemList.CactusCharcoal.get(1));
        GTOreDictUnificator.registerOre("itemCokeCactus", GregtechItemList.CactusCoke.get(1));
        GTOreDictUnificator.registerOre("itemCharcoalSugar", GregtechItemList.SugarCharcoal.get(1));
        GTOreDictUnificator.registerOre("itemCokeSugar", GregtechItemList.SugarCoke.get(1));
    }

    public static void postInit() {
        addCokingRecipes(
            new ItemStack(Blocks.cactus),
            GregtechItemList.CactusCharcoal.get(1),
            GregtechItemList.CactusCoke.get(1));
        addCokingRecipes(
            new ItemStack(Items.reeds),
            GregtechItemList.SugarCharcoal.get(1),
            GregtechItemList.SugarCoke.get(1));
        // Taken from the Railcraft code
        if (Railcraft.isModLoaded()) {
            int bioheat = (int) (16000 * RailcraftConfig.boilerBiofuelMultiplier());
            FuelManager.addBoilerFuel(
                BioDiesel.getFluid(1L)
                    .getFluid(),
                bioheat);
        }
    }

    private static void addCokingRecipes(ItemStack plant, ItemStack charcoal, ItemStack coke) {
        // Recipes for the Charcoals and Cokes, outputting either Creosote or Charcoal Byproducts depending on the
        // fluid input
        GTValues.RA.stdBuilder()
            .itemInputs(plant)
            .circuit(3)
            .itemOutputs(charcoal)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (100)))
            .eut(TierEU.RECIPE_LV / 2)
            .duration(1 * SECONDS)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(plant)
            .circuit(4)
            .itemOutputs(charcoal)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (100)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CharcoalByproducts,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (200)))
            .eut(TierEU.RECIPE_LV / 2)
            .duration(10 * TICKS)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(charcoal)
            .circuit(3)
            .itemOutputs(coke)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (200)))
            .eut(TierEU.RECIPE_LV / 2)
            .duration(2 * SECONDS)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(charcoal)
            .circuit(4)
            .itemOutputs(coke)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (50)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CharcoalByproducts,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (100)))
            .eut(TierEU.RECIPE_LV / 2)
            .duration(1 * SECONDS)
            .addTo(cokeOvenRecipes);

        // Generate Wood Tar and Wood Gas from these Cokes
        GTValues.RA.stdBuilder()
            .itemInputs(coke)
            .circuit(5)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustSmall, (int) (1)))
            .fluidInputs(Materials.Steam.getGas(100))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.shapeFluidLiquid, (int) (200)))
            .eut(TierEU.RECIPE_HV / 2)
            .duration(3 * SECONDS)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(coke)
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustSmall, (int) (1)))
            .fluidInputs(Materials.Steam.getGas(100))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodGas, Materials2FluidShapes.shapeFluidGas, (int) (300)))
            .eut(TierEU.RECIPE_HV / 2)
            .duration(3 * SECONDS)
            .addTo(cokeOvenRecipes);

        // Fluid Extracting the Charcoals for Wood Tar
        GTValues.RA.stdBuilder()
            .itemInputs(charcoal)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, (int) (1)))
            .outputChances(1000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.shapeFluidLiquid, (int) (50)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidExtractionRecipes);

        // Processing the Charcoals with Oxygen to get CO and CO2
        // C + O = CO
        GTValues.RA.stdBuilder()
            .itemInputs(charcoal)
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (500)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CarbonMonoxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (500)))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // C + 2O = CO2
        GTValues.RA.stdBuilder()
            .itemInputs(charcoal)
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CarbonDioxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (1_000)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        if (Railcraft.isModLoaded()) {
            RailcraftUtils.addCokeOvenRecipe(
                plant,
                true,
                true,
                charcoal,
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (30)),
                500);
            RailcraftUtils.addCokeOvenRecipe(
                charcoal,
                true,
                true,
                coke,
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (30)),
                500);

            if (NewHorizonsCoreMod.isModLoaded()) {
                RailcraftUtils.addAdvancedCokeOvenRecipe(plant, true, true, charcoal, 20);
                RailcraftUtils.addAdvancedCokeOvenRecipe(charcoal, true, true, coke, 20);
            }
        }
    }
}
