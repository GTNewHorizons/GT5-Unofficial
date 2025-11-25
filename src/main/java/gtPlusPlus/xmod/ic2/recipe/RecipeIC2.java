package gtPlusPlus.xmod.ic2.recipe;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;

public class RecipeIC2 {

    public static void initRecipes() {
        addAdvancedHazmat();

        // Rotor Blades
        GTModHandler.addCraftingRecipe(
            GregtechItemList.EnergeticAlloyRotorBlade.get(1),
            new Object[] { "PPP", "PRP", "PPP", 'P', "plateEnergeticAlloy", 'R', "ringStainlessSteel" });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.TungstenSteelRotorBlade.get(1),
            new Object[] { "PPP", "PRP", "PPP", 'P', "plateTungstenSteel", 'R', "ringTungstenSteel" });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.VibrantAlloyRotorBlade.get(1),
            new Object[] { "PPP", "PRP", "PPP", 'P', "plateVibrantAlloy", 'R', "ringChrome" });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.IridiumRotorBlade.get(1),
            new Object[] { "PPP", "PRP", "PPP", 'P', "plateAlloyIridium", 'R', "ringOsmiridium" });

        // Shaft Extruder Shape
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Shape_Extruder_WindmillShaft.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "hXS", "XPX", "fXd", 'P', ItemList.Shape_Extruder_Rod, 'X',
                OrePrefixes.plate.get(Materials.DarkSteel), 'S', OrePrefixes.screw.get(Materials.DarkSteel) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Shape_Extruder_WindmillShaft.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "hXS", "XPX", "fXd", 'P', ItemList.Shape_Extruder_Rod, 'X',
                OrePrefixes.plate.get(Materials.TungstenSteel), 'S', OrePrefixes.screw.get(Materials.TungstenSteel) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Shape_Extruder_WindmillShaft.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "hXS", "XPX", "fXd", 'P', ItemList.Shape_Extruder_Rod, 'X',
                OrePrefixes.plate.get(Materials.Molybdenum), 'S', OrePrefixes.screw.get(Materials.Molybdenum) });

        // Shafts
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.EnergeticAlloy, 9),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0))
            .itemOutputs(GregtechItemList.EnergeticAlloyShaft.get(1))
            .duration(2 * MINUTES + 8 * SECONDS)
            .eut(250)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.EnergeticAlloy, 1),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0))
            .itemOutputs(GregtechItemList.EnergeticAlloyShaft.get(1))
            .duration(2 * MINUTES + 8 * SECONDS)
            .eut(250)
            .addTo(extruderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 9),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0))
            .itemOutputs(GregtechItemList.TungstenSteelShaft.get(1))
            .duration(4 * MINUTES + 16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.TungstenSteel, 1),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0))
            .itemOutputs(GregtechItemList.TungstenSteelShaft.get(1))
            .duration(4 * MINUTES + 16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(extruderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.VibrantAlloy, 9),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0))
            .itemOutputs(GregtechItemList.VibrantAlloyShaft.get(1))
            .duration(8 * MINUTES + 32 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.VibrantAlloy, 1),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0))
            .itemOutputs(GregtechItemList.VibrantAlloyShaft.get(1))
            .duration(8 * MINUTES + 32 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(extruderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 9),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0))
            .itemOutputs(GregtechItemList.IridiumShaft.get(1))
            .duration(17 * MINUTES + 4 * SECONDS)
            .eut(4000)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Iridium, 1),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0))
            .itemOutputs(GregtechItemList.IridiumShaft.get(1))
            .duration(17 * MINUTES + 4 * SECONDS)
            .eut(4000)
            .addTo(extruderRecipes);

        // Gearbox Rotors
        GTModHandler.addCraftingRecipe(
            GregtechItemList.EnergeticAlloyRotor.get(1),
            new Object[] { "SBh", "BRB", "wBS", 'B', GregtechItemList.EnergeticAlloyRotorBlade.get(1), 'R',
                "ringStainlessSteel", 'S', GregtechItemList.EnergeticAlloyShaft.get(1) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.TungstenSteelRotor.get(1),
            new Object[] { "SBh", "BRB", "wBS", 'B', GregtechItemList.TungstenSteelRotorBlade.get(1), 'R',
                "ringTungstenSteel", 'S', GregtechItemList.TungstenSteelShaft.get(1) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.VibrantAlloyRotor.get(1),
            new Object[] { "SBh", "BRB", "wBS", 'B', GregtechItemList.VibrantAlloyRotorBlade.get(1), 'R', "ringChrome",
                'S', GregtechItemList.VibrantAlloyShaft.get(1) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.IridiumRotor.get(1),
            new Object[] { "SBh", "BRB", "wBS", 'B', GregtechItemList.IridiumRotorBlade.get(1), 'R', "ringOsmiridium",
                'S', GregtechItemList.IridiumShaft.get(1) });
    }

    private static void addAdvancedHazmat() {
        // Advanced Hazmat Helmet
        GTValues.RA.stdBuilder()
            .itemInputs(
                Ic2Items.hazmatHelmet.copy(),
                new ItemStack(Blocks.wool, 16, 4), // Yellow Wool
                GTUtility.copyAmount(8, Ic2Items.coil),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Cobalt, 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Aluminium, 4))
            .circuit(2)
            .itemOutputs(GregtechItemList.Armour_Hazmat_Advanced_Helmet.get(1))
            .fluidInputs(Materials.Rubber.getMolten(144 * 4))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Advanced Hazmat Suit
        GTValues.RA.stdBuilder()
            .itemInputs(
                Ic2Items.hazmatChestplate.copy(),
                new ItemStack(Blocks.wool, 64, 4), // Yellow Wool
                GTUtility.copyAmount(32, Ic2Items.coil),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Cobalt, 16),
                MaterialsAlloy.SILICON_CARBIDE.getGear(8))
            .circuit(2)
            .itemOutputs(GregtechItemList.Armour_Hazmat_Advanced_Chest.get(1))
            .fluidInputs(Materials.Rubber.getMolten(144 * 10))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Advanced Hazmat Leggings
        GTValues.RA.stdBuilder()
            .itemInputs(
                Ic2Items.hazmatLeggings.copy(),
                new ItemStack(Blocks.wool, 32, 4), // Yellow Wool
                GTUtility.copyAmount(16, Ic2Items.coil),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Cobalt, 8),
                MaterialsAlloy.SILICON_CARBIDE.getGear(4))
            .circuit(2)
            .itemOutputs(GregtechItemList.Armour_Hazmat_Advanced_Legs.get(1))
            .fluidInputs(Materials.Rubber.getMolten(144 * 8))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Advanced Hazmat Boots
        GTValues.RA.stdBuilder()
            .itemInputs(
                Ic2Items.hazmatBoots.copy(),
                new ItemStack(Blocks.wool, 16, 15), // Black Wool
                GTUtility.copyAmount(6, Ic2Items.coil),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 8),
                MaterialsAlloy.TUMBAGA.getGear(4))
            .circuit(2)
            .itemOutputs(GregtechItemList.Armour_Hazmat_Advanced_Boots.get(1))
            .fluidInputs(Materials.Rubber.getMolten(144 * 6))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }
}
