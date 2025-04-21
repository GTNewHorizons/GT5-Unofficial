package gtrailways.items;

import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtrailways.GTRailways;

public class Items {

    public static final Item SAFETY_OVERALLS = new SafetyOveralls().setUnlocalizedName("safetyOveralls")
        .setTextureName(GTRailways.MODID + ":safety_overalls");

    public static void init() {
        GTRailways.LOGGER.info("registering items");
        GameRegistry.registerItem(SAFETY_OVERALLS, "safety_overalls");
    }

    public static void addRecipes() {
        ItemStack safetyOveralls = new ItemStack(SAFETY_OVERALLS);
        ItemStack railcraftPants = new ItemStack(GameRegistry.findItem(Railcraft.ID, "armor.overalls"));
        GTValues.RA.stdBuilder()
            .itemInputs(
                railcraftPants,
                // Preparation for nuking IC2
                Loader.isModLoaded("IC2")
                    ? new ItemStack(GameRegistry.findItem(IndustrialCraft2.ID, "itemArmorHazmatLeggings"))
                    : GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 6L))
            .itemOutputs(safetyOveralls)
            .duration(30 * SECONDS)
            .eut(30)
            .addTo(assemblerRecipes);
        if (Loader.isModLoaded("IC2")) {
            GTModHandler.addCraftingRecipe(
                safetyOveralls,
                0,
                new Object[] {"SPS", "SrS", " H ",
                    'S', new ItemStack(net.minecraft.init.Items.string),
                    'P', railcraftPants,
                    'H', new ItemStack(GameRegistry.findItem(IndustrialCraft2.ID, "itemArmorHazmatLeggings"))
                }
            );
        } else {
            GTModHandler.addCraftingRecipe(
                safetyOveralls,
                0,
                new Object[] {"RPR", "RrR", "R R",
                    'R', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 1L),
                    'P', railcraftPants
                }
            );
        }
    }
}
