package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.ForbiddenMagic;
import static gregtech.api.enums.Mods.TaintedMagic;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeConstants;

public class FuelRecipes implements Runnable {

    // todo: add an enum for the fuel type, int values are mysterious
    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("biogasCell", 1L))
            .metadata(FUEL_VALUE, 40)
            .metadata(FUEL_TYPE, 1)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.golden_apple, 1, 1))
            .itemOutputs(new ItemStack(Items.apple, 1))
            .metadata(FUEL_VALUE, 6400)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Thaumcraft.ID, "ItemShard", 1L, 6))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ForbiddenMagic.ID, "GluttonyShard", 1L))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ForbiddenMagic.ID, "FMResource", 1L, 3))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ForbiddenMagic.ID, "NetherShard", 1L))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ForbiddenMagic.ID, "NetherShard", 1L, 1))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ForbiddenMagic.ID, "NetherShard", 1L, 2))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ForbiddenMagic.ID, "NetherShard", 1L, 3))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ForbiddenMagic.ID, "NetherShard", 1L, 4))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ForbiddenMagic.ID, "NetherShard", 1L, 5))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ForbiddenMagic.ID, "NetherShard", 1L, 6))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TaintedMagic.ID, "ItemMaterial", 1L, 3))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TaintedMagic.ID, "ItemMaterial", 1L, 4))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TaintedMagic.ID, "ItemMaterial", 1L, 5))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ThaumicTinkerer.ID, "kamiResource", 1L, 6))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(ThaumicTinkerer.ID, "kamiResource", 1L, 7))
            .metadata(FUEL_VALUE, 720)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);

    }
}
