package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class ForgeHammerRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stonebrick, 1, 0))
            .itemOutputs(new ItemStack(Blocks.stonebrick, 1, 2))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stone, 1, 0))
            .itemOutputs(new ItemStack(Blocks.cobblestone, 1, 0))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.cobblestone, 1, 0))
            .itemOutputs(new ItemStack(Blocks.gravel, 1, 0))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.gravel, 1, 0))
            .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sandstone, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.ice, 1, 0))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.packed_ice, 1, 0))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 2))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.brick_block, 1, 0))
            .itemOutputs(new ItemStack(Items.brick, 3, 0))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.nether_brick, 1, 0))
            .itemOutputs(new ItemStack(Items.netherbrick, 3, 0))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass, 1, 32767))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.glass, 1, 32767))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1))
            .duration(10 * TICKS)
            .eut(10)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass_pane, 1, 32767))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.glass_pane, 1, 32767))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Brick.getIngots(1))
            .itemOutputs(Materials.Brick.getDustSmall(1))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Firebrick.get(1))
            .itemOutputs(Materials.Fireclay.getDust(1))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Firebricks.get(1))
            .itemOutputs(ItemList.Firebrick.get(3))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1L), GregtechItemList.Laser_Lens_Special.get(1))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(2880L))
            .fluidOutputs(MaterialsUEVplus.Space.getMolten(1440L), MaterialsUEVplus.Time.getMolten(1440L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(hammerRecipes);

        if (HardcoreEnderExpansion.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(HardcoreEnderExpansion.ID, "endium_ore", 1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 1))
                .duration(16)
                .eut(10)
                .addTo(hammerRecipes);
        }
    }
}
