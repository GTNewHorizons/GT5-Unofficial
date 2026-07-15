package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class BenderRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.stick, (int) (20L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.stick, (int) (48L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CastIron, Materials2Shapes.stick, (int) (32L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.stick, (int) (32L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.stick, (int) (24L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.stick, (int) (16L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.stick, (int) (12L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.stick, (int) (9L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.stick, (int) (6L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.stick, (int) (3L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Osmiridium, Materials2Shapes.stick, (int) (2L)))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Obsidian, Materials2Shapes.stick, (int) (24L)))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.stick, (int) (12L)))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.stick, (int) (6L)))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.stick, (int) (3L)))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.stick, (int) (1L)))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.stick, (int) (20L)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.stick, (int) (48L)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CastIron, Materials2Shapes.stick, (int) (24L)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.stick, (int) (32L)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.stick, (int) (16L)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.stick, (int) (12L)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.stick, (int) (8)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.stick, (int) (6L)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.stick, (int) (4L)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.stick, (int) (2L)))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Mixed_Metal_Ingot.get(1L))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get("plateAlloyAdvanced", 1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.plate, (int) (2L)))
            .circuit(12)
            .itemOutputs(ItemList.Cell_Empty.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.plate, (int) (1L)))
            .circuit(12)
            .itemOutputs(ItemList.Cell_Empty.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.Polytetrafluoroethylene, Materials2Shapes.plate, (int) (1L)))
            .circuit(12)
            .itemOutputs(ItemList.Cell_Empty.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.plate, (int) (3L)))
            .circuit(12)
            .itemOutputs(new ItemStack(Items.bucket, 1, 0))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CastIron, Materials2Shapes.plate, (int) (3L)))
            .circuit(12)
            .itemOutputs(new ItemStack(Items.bucket, 1, 0))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.itemCasing, (int) (2L)))
            .circuit(2)
            .itemOutputs(ItemList.IC2_Fuel_Rod_Empty.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.tairitsu.get(OrePrefixes.ingot, 9))
            .circuit(9)
            .itemOutputs(GGMaterial.tairitsu.get(OrePrefixes.plateDense, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(benderRecipes);

        if (MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.itemCasing, (int) (1L)) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.itemCasing, (int) (1L)))
                .circuit(1)
                .itemOutputs(ItemList.IC2_Food_Can_Empty.get(1L))
                .duration(20 * TICKS)
                .eut((int) TierEU.RECIPE_HV)
                .addTo(benderRecipes);
        }

        // From ProcessingFood - foodDough (remove furnace smelting)
        for (net.minecraft.item.ItemStack stack : OreDictionary.getOres("foodDough")) {
            GTModHandler.removeFurnaceSmelting(stack);
        }

        // From ProcessingFood - foodDough bender
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("foodDough", 1))
            .circuit(1)
            .itemOutputs(ItemList.Food_Flat_Dough.get(1L))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(benderRecipes);

    }
}
