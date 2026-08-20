package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class BenderRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Aluminium, Shapes.stick, 20))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.stick, 48))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CastIron, Shapes.stick, 32))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Bronze, Shapes.stick, 32))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.stick, 24))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.stick, 16))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Titanium, Shapes.stick, 12))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, 9))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Iridium, Shapes.stick, 6))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Osmium, Shapes.stick, 3))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.stick, 2))
            .circuit(10)
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Obsidian, Shapes.stick, 24))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.stick, 12))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, 6))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Iridium, Shapes.stick, 3))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Osmium, Shapes.stick, 1))
            .circuit(11)
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Aluminium, Shapes.stick, 20))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.stick, 48))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CastIron, Shapes.stick, 24))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Bronze, Shapes.stick, 32))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.stick, 16))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.stick, 12))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Titanium, Shapes.stick, 8))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, 6))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Iridium, Shapes.stick, 4))
            .circuit(12)
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Osmium, Shapes.stick, 2))
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
            .itemInputs(MaterialLibAPI.getStack(Materials.Tin, Shapes.plate, 2))
            .circuit(12)
            .itemOutputs(ItemList.Cell_Empty.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.plate, 1))
            .circuit(12)
            .itemOutputs(ItemList.Cell_Empty.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Polytetrafluoroethylene, Shapes.plate, 1))
            .circuit(12)
            .itemOutputs(ItemList.Cell_Empty.get(4L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.plate, 3))
            .circuit(12)
            .itemOutputs(new ItemStack(Items.bucket, 1, 0))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CastIron, Shapes.plate, 3))
            .circuit(12)
            .itemOutputs(new ItemStack(Items.bucket, 1, 0))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.itemCasing, 2))
            .circuit(2)
            .itemOutputs(ItemList.IC2_Fuel_Rod_Empty.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(benderRecipes);

        if (MaterialLibAPI.getStack(Materials.Tin, Shapes.itemCasing, 1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.Tin, Shapes.itemCasing, 1))
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
