package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.objects.SubstituteFluidStack;
import gregtech.api.util.GTOreDictUnificator;

public class CircuitAssemblerRecipes implements Runnable {

    @Override
    public void run() {
        registerRailcraftRecipes();
        registerForestryRecipes();

    }

    public void registerRailcraftRecipes() {
        if (!Railcraft.isModLoaded()) {
            return;
        }

        // Railcraft Circuits

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Coated_Basic.get(1),
                GTOreDictUnificator.get("circuitBasic", 1),
                ItemList.Cover_Controller.get(1))
            .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 4L, 0))
            .fluidInputs(SubstituteFluidStack.soldering(HALF_INGOTS))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Coated_Basic.get(1),
                GTOreDictUnificator.get("circuitBasic", 1),
                ItemList.Sensor_LV.get(1))
            .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 4L, 1))
            .fluidInputs(SubstituteFluidStack.soldering(HALF_INGOTS))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Coated_Basic.get(1),
                GTOreDictUnificator.get("circuitBasic", 1),
                getModItem(Railcraft.ID, "part.signal.lamp", 1L, 0))
            .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 4L, 2))
            .fluidInputs(SubstituteFluidStack.soldering(HALF_INGOTS))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Phenolic_Good.get(1),
                GTOreDictUnificator.get("circuitGood", 1),
                ItemList.Cover_Controller.get(1))
            .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 8L, 0))
            .fluidInputs(SubstituteFluidStack.soldering(HALF_INGOTS))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Phenolic_Good.get(1),
                GTOreDictUnificator.get("circuitGood", 1),
                ItemList.Sensor_LV.get(1))
            .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 8L, 1))
            .fluidInputs(SubstituteFluidStack.soldering(HALF_INGOTS))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Phenolic_Good.get(1),
                GTOreDictUnificator.get("circuitGood", 1),
                getModItem(Railcraft.ID, "part.signal.lamp", 1L, 0))
            .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 8L, 2))
            .fluidInputs(SubstituteFluidStack.soldering(HALF_INGOTS))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Epoxy_Advanced.get(1),
                GTOreDictUnificator.get("circuitAdvanced", 1),
                ItemList.Cover_Controller.get(1))
            .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 16L, 0))
            .fluidInputs(SubstituteFluidStack.soldering(HALF_INGOTS))
            .duration(25 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Epoxy_Advanced.get(1),
                GTOreDictUnificator.get("circuitAdvanced", 1),
                ItemList.Sensor_LV.get(1))
            .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 16L, 1))
            .fluidInputs(SubstituteFluidStack.soldering(HALF_INGOTS))
            .duration(25 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Epoxy_Advanced.get(1),
                GTOreDictUnificator.get("circuitAdvanced", 1),
                getModItem(Railcraft.ID, "part.signal.lamp", 1L, 0))
            .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 16L, 2))
            .fluidInputs(SubstituteFluidStack.soldering(HALF_INGOTS))
            .duration(25 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);
    }

    public void registerForestryRecipes() {
        if (!Forestry.isModLoaded()) {
            return;
        }

        // alternative version of the copper electron tube

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RedAlloy, Shapes.plate, (int) (1)),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.bolt, (int) (2)),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.wireFine, (int) (2)),
                MaterialLibAPI.getStack(Materials.AnnealedCopper, Shapes.stickLong, (int) (2)))
            .itemOutputs(getModItem(Forestry.ID, "thermionicTubes", 4L, 0))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glass, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        // alternative version of the iron electron tube

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RedAlloy, Shapes.plate, (int) (1)),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.bolt, (int) (2)),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.wireFine, (int) (2)),
                MaterialLibAPI.getStack(Materials.CastIron, Shapes.stickLong, (int) (2)))
            .itemOutputs(getModItem(Forestry.ID, "thermionicTubes", 4L, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glass, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        ItemStack[] rodMaterials = new ItemStack[] {
            MaterialLibAPI.getStack(Materials.Copper, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Tin, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Bronze, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Iron, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Gold, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Diamond, Shapes.stickLong, (int) (2L)),
            getModItem(NewHorizonsCoreMod.ID, "LongObsidianRod", 2L, 0),
            MaterialLibAPI.getStack(Materials.Blaze, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Rubber, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Emerald, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Apatite, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Lapis, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.EnderEye, Shapes.stickLong, (int) (2L)),
            MaterialLibAPI.getStack(Materials.Uranium, Shapes.stickLong, (int) (2L)), };

        for (int metaid = 0; metaid < rodMaterials.length; metaid++) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.RedAlloy, Shapes.plate, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Gold, Shapes.bolt, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Copper, Shapes.wireFine, (int) (2)),
                    rodMaterials[metaid])
                .itemOutputs(getModItem(Forestry.ID, "thermionicTubes", 4L, metaid))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glass, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Coated_Basic.get(1),
                GTOreDictUnificator.get("circuitPrimitive", 2),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.foil, (int) (2)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.screw, (int) (4)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.wireFine, (int) (1)))
            .circuit(1)
            .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 0))
            .fluidInputs(SubstituteFluidStack.soldering(4 * INGOTS))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Coated_Basic.get(1),
                GTOreDictUnificator.get("circuitBasic", 2),
                MaterialLibAPI.getStack(Materials.Bronze, Shapes.foil, (int) (2)),
                MaterialLibAPI.getStack(Materials.Bronze, Shapes.screw, (int) (4)),
                MaterialLibAPI.getStack(Materials.Bronze, Shapes.wireFine, (int) (1)))
            .circuit(1)
            .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 1))
            .fluidInputs(SubstituteFluidStack.soldering(4 * INGOTS))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Phenolic_Good.get(1),
                GTOreDictUnificator.get("circuitGood", 2),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.foil, (int) (2)),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.screw, (int) (4)),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.wireFine, (int) (1)))
            .circuit(1)
            .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 2))
            .fluidInputs(SubstituteFluidStack.soldering(576))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Phenolic_Good.get(1),
                GTOreDictUnificator.get("circuitAdvanced", 2),
                MaterialLibAPI.getStack(Materials.Electrum, Shapes.foil, (int) (2)),
                MaterialLibAPI.getStack(Materials.Electrum, Shapes.screw, (int) (4)),
                MaterialLibAPI.getStack(Materials.Electrum, Shapes.wireFine, (int) (1)))
            .circuit(1)
            .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 3))
            .fluidInputs(SubstituteFluidStack.soldering(576))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);
    }
}
