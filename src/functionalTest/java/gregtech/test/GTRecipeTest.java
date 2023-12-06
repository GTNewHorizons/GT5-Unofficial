package gregtech.test;

import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.enums.ItemList.Circuit_Advanced;
import static gregtech.api.enums.ItemList.Circuit_Nanoprocessor;
import static gregtech.api.enums.ItemList.Circuit_Parts_Crystal_Chip_Master;
import static gregtech.api.enums.ItemList.IC2_LapotronCrystal;
import static gregtech.api.enums.Materials.Advanced;
import static gregtech.api.enums.Materials.BlueTopaz;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.OrePrefixes.circuit;
import static gregtech.api.enums.OrePrefixes.lens;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_OreDictUnificator.get;
import static gregtech.api.util.GT_OreDictUnificator.isItemStackInstanceOf;
import static gregtech.api.util.GT_Utility.copyAmount;
import static net.minecraft.init.Blocks.chest;
import static net.minecraft.init.Blocks.iron_ore;
import static net.minecraft.init.Blocks.lapis_block;
import static net.minecraft.init.Blocks.log;
import static net.minecraft.init.Blocks.planks;
import static net.minecraft.init.Items.iron_ingot;
import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GT_Recipe;

class GTRecipeTest {

    static RecipeMap<?> recipeMap;
    static GT_Recipe lapotronChipRecipe;

    @BeforeAll
    static void setup() {
        recipeMap = RecipeMapBuilder.of("__test__")
            .maxIO(9, 1, 1, 0)
            .build();

        RA.stdBuilder()
            .itemInputs(new ItemStack(log, 2, WILDCARD_VALUE), new ItemStack(planks, 2, WILDCARD_VALUE))
            .itemOutputs(new ItemStack(chest, 1))
            .duration(0)
            .eut(0)
            .addTo(recipeMap);

        RA.stdBuilder()
            .itemInputs(new ItemStack(lapis_block, 1), get(circuit, Advanced, 1))
            .itemOutputs(IC2_LapotronCrystal.get(1))
            .duration(0)
            .eut(0)
            .addTo(recipeMap);

        lapotronChipRecipe = RA.stdBuilder()
            .itemInputs(IC2_LapotronCrystal.getWildcard(1), copyAmount(0, get(lens, BlueTopaz, 1)))
            .itemOutputs(Circuit_Parts_Crystal_Chip_Master.get(3))
            .duration(0)
            .eut(0)
            .addTo(recipeMap)
            .toArray(new GT_Recipe[0])[0];

        RA.stdBuilder()
            .itemInputs(getModItem(GregTech.ID, "gt.blockores", 1, 32))
            .itemOutputs(new ItemStack(iron_ingot, 1))
            .duration(0)
            .eut(0)
            .addTo(recipeMap);
    }

    @Test
    void ensureRecipesAdded() {
        assertEquals(
            recipeMap.getAllRecipes()
                .size(),
            4);
    }

    @Test
    void findWithExactSameInputs() {
        GT_Recipe recipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(lapis_block, 1), get(circuit, Advanced, 1))
            .find();
        assertNotNull(recipe);
    }

    @Test
    void findWildcardWithExactSameInputs() {
        GT_Recipe chestRecipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(log, 2, WILDCARD_VALUE), new ItemStack(planks, 2, WILDCARD_VALUE))
            .find();
        assertNotNull(chestRecipe);

        GT_Recipe lapotronChipRecipe = recipeMap.findRecipeQuery()
            .items(IC2_LapotronCrystal.getWildcard(1), copyAmount(0, get(lens, BlueTopaz, 1)))
            .find();
        assertNotNull(lapotronChipRecipe);
    }

    @Test
    void findWildcardWithDifferentMeta() {
        // https://github.com/GTNewHorizons/GT5-Unofficial/pull/2364/commits/e7112fce5f24431f3a4ad19288d662b93cbb91f2
        GT_Recipe recipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(log, 2, 0), new ItemStack(planks, 2, 1))
            .find();
        assertNotNull(recipe);
    }

    @Test
    void findWithNBT() {
        // https://github.com/GTNewHorizons/GT5-Unofficial/pull/2364/commits/844a38662b05494b42a4439bbc0e6d4d7df1a683
        ItemStack lapisBlock = new ItemStack(lapis_block, 1);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setFloat("charge", 123456);
        lapisBlock.stackTagCompound = tag;
        GT_Recipe recipe = recipeMap.findRecipeQuery()
            .items(lapisBlock, get(circuit, Advanced, 1))
            .find();
        assertNotNull(recipe);
    }

    @Test
    void rejectWithInsufficientAmount() {
        GT_Recipe recipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(log, 1, 0), new ItemStack(planks, 1, 0))
            .find();
        assertNull(recipe);
    }

    @Test
    void rejectWithoutNonConsumable() {
        // https://github.com/GTNewHorizons/GT5-Unofficial/pull/2364/commits/bfc93bff7ed34616021e8c5b6dbdc50dd7096af5
        GT_Recipe recipe = recipeMap.findRecipeQuery()
            .items(IC2_LapotronCrystal.get(1))
            .cachedRecipe(lapotronChipRecipe)
            .find();
        assertNull(recipe);
    }

    @Test
    void findOredicted() {
        // https://github.com/GTNewHorizons/GT5-Unofficial/pull/2373
        assertTrue(
            isItemStackInstanceOf(Circuit_Nanoprocessor.get(1), "circuitAdvanced"),
            "Nanoprocessor is not registered as HV circuit");
        GT_Recipe recipeByNanoProcessor = recipeMap.findRecipeQuery()
            .items(new ItemStack(lapis_block, 1), Circuit_Nanoprocessor.get(1))
            .find();
        assertNotNull(recipeByNanoProcessor);

        assertTrue(
            isItemStackInstanceOf(Circuit_Advanced.get(1), "circuitAdvanced"),
            "Processor Assembly is not registered as HV circuit");
        GT_Recipe recipeByCircuitAssembly = recipeMap.findRecipeQuery()
            .items(new ItemStack(lapis_block, 1), Circuit_Advanced.get(1))
            .find();
        assertNotNull(recipeByCircuitAssembly);
    }

    @Test
    void findWithSpecificOreDictionary() {
        // https://github.com/GTNewHorizons/GT5-Unofficial/pull/2379
        // We cannot use circuit assembling recipe like the issue mentioned above,
        // as mUnificationTarget is not set for circuits in GT5.
        // But it works in the same way; specific circuit -> GT ore block, unificated circuit -> vanilla ore block
        GT_Recipe recipeCorrectOre = recipeMap.findRecipeQuery()
            .items(getModItem(GregTech.ID, "gt.blockores", 1, 32))
            .find();
        assertNotNull(recipeCorrectOre);

        GT_Recipe recipeWrongOre = recipeMap.findRecipeQuery()
            .items(new ItemStack(iron_ore, 1))
            .find();
        assertNull(recipeWrongOre);
    }
}
