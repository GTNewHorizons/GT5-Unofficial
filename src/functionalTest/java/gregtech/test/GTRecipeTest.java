package gregtech.test;

import static gregtech.api.GregTechAPI.sBlockOres1;
import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.ItemList.Circuit_Parts_Crystal_Chip_Master;
import static gregtech.api.enums.ItemList.IC2_LapotronCrystal;
import static gregtech.api.enums.OrePrefixes.circuit;
import static gregtech.api.enums.OrePrefixes.lens;
import static gregtech.api.util.GTOreDictUnificator.get;
import static gregtech.api.util.GTUtility.copyAmount;
import static net.minecraft.init.Blocks.chest;
import static net.minecraft.init.Blocks.iron_ore;
import static net.minecraft.init.Blocks.lapis_block;
import static net.minecraft.init.Blocks.log;
import static net.minecraft.init.Blocks.planks;
import static net.minecraft.init.Blocks.stone;
import static net.minecraft.init.Blocks.stone_slab;
import static net.minecraft.init.Items.glass_bottle;
import static net.minecraft.init.Items.iron_ingot;
import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GTRecipe;

class GTRecipeTest {

    static RecipeMap<?> recipeMap;
    static GTRecipe lapotronChipRecipe;

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
            .itemInputs(new ItemStack(lapis_block, 1), get(circuit, Materials.HV, 1))
            .itemOutputs(IC2_LapotronCrystal.get(1))
            .duration(0)
            .eut(0)
            .addTo(recipeMap);

        lapotronChipRecipe = RA.stdBuilder()
            .itemInputs(IC2_LapotronCrystal.getWildcard(1), copyAmount(0, get(lens, Materials.BlueTopaz, 1)))
            .itemOutputs(Circuit_Parts_Crystal_Chip_Master.get(3))
            .duration(0)
            .eut(0)
            .addTo(recipeMap)
            .toArray(new GTRecipe[0])[0];

        RA.stdBuilder()
            .itemInputs(new ItemStack(sBlockOres1, 1, 32))
            .itemOutputs(new ItemStack(iron_ingot, 1))
            .duration(0)
            .eut(0)
            .addTo(recipeMap);

        RA.stdBuilder()
            .itemInputs(new ItemStack(stone_slab, 64), new ItemStack(stone_slab, 64))
            .itemOutputs(new ItemStack(stone, 2))
            .duration(0)
            .eut(0)
            .addTo(recipeMap);

        ItemStack dataStick = ItemList.Tool_DataStick.get(0);
        NBTTagCompound dataStickTag = new NBTTagCompound();
        dataStickTag.setInteger("integer", 123456);
        dataStick.setTagCompound(dataStickTag);
        RA.stdBuilder()
            .itemInputs(dataStick)
            .itemOutputs(new ItemStack(chest, 1))
            .duration(0)
            .eut(0)
            .addTo(recipeMap);

        ItemStack glass = new ItemStack(glass_bottle, 2);
        NBTTagCompound glassTag = new NBTTagCompound();
        glassTag.setInteger("integer", 123456);
        glass.setTagCompound(glassTag);
        RA.stdBuilder()
            .itemInputs(glass)
            .itemOutputs(new ItemStack(chest, 1))
            .duration(0)
            .eut(0)
            .nbtSensitive()
            .addTo(recipeMap);
    }

    @Test
    void ensureRecipesAdded() {
        assertEquals(
            recipeMap.getAllRecipes()
                .size(),
            7);
    }

    @Test
    void findWithExactSameInputs() {
        GTRecipe recipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(lapis_block, 1), get(circuit, Materials.HV, 1))
            .find();
        assertNotNull(recipe);

        GTRecipe stoneRecipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(stone_slab, 128))
            .find();
        assertNotNull(stoneRecipe);
    }

    @Test
    void findWildcardWithExactSameInputs() {
        GTRecipe chestRecipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(log, 2, WILDCARD_VALUE), new ItemStack(planks, 2, WILDCARD_VALUE))
            .find();
        assertNotNull(chestRecipe);

        GTRecipe lapotronChipRecipe = recipeMap.findRecipeQuery()
            .items(IC2_LapotronCrystal.getWildcard(1), copyAmount(0, get(lens, Materials.BlueTopaz, 1)))
            .find();
        assertNotNull(lapotronChipRecipe);
    }

    @Test
    void findWildcardWithDifferentMeta() {
        // https://github.com/GTNewHorizons/GT5-Unofficial/pull/2364/commits/e7112fce5f24431f3a4ad19288d662b93cbb91f2
        GTRecipe recipe = recipeMap.findRecipeQuery()
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
        GTRecipe recipe = recipeMap.findRecipeQuery()
            .items(lapisBlock, get(circuit, Materials.HV, 1))
            .find();
        assertNotNull(recipe);

        // For NBT sensitive recipes
        ItemStack glass = new ItemStack(glass_bottle, 2);
        NBTTagCompound glassTag = new NBTTagCompound();
        glassTag.setInteger("integer", 123456);
        glass.setTagCompound(glassTag);
        GTRecipe nbtSensitiveRecipe = recipeMap.findRecipeQuery()
            .items(glass)
            .find();
        assertNotNull(nbtSensitiveRecipe);

        // For items that need to check NBT, e.g. data sticks
        ItemStack dataStick = ItemList.Tool_DataStick.get(0);
        NBTTagCompound dataStickTag = new NBTTagCompound();
        dataStickTag.setInteger("integer", 123456);
        dataStick.setTagCompound(dataStickTag);
        GTRecipe checkNBTRecipe = recipeMap.findRecipeQuery()
            .items(dataStick)
            .find();
        assertNotNull(checkNBTRecipe);
    }

    @Test
    void rejectWithInsufficientAmount() {
        GTRecipe recipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(log, 1, 0), new ItemStack(planks, 1, 0))
            .find();
        assertNull(recipe);

        GTRecipe stoneRecipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(stone_slab, 127))
            .find();
        assertNull(stoneRecipe);
    }

    @Test
    void rejectWithoutNonConsumable() {
        // https://github.com/GTNewHorizons/GT5-Unofficial/pull/2364/commits/bfc93bff7ed34616021e8c5b6dbdc50dd7096af5
        GTRecipe recipe = recipeMap.findRecipeQuery()
            .items(IC2_LapotronCrystal.get(1))
            .cachedRecipe(lapotronChipRecipe)
            .find();
        assertNull(recipe);
    }

    @Test
    void rejectWithoutCorrectNBT() {
        // For NBT sensitive recipes
        GTRecipe nbtSensitiveRecipe = recipeMap.findRecipeQuery()
            .items(new ItemStack(glass_bottle, 2))
            .find();
        assertNull(nbtSensitiveRecipe);

        // For items that need to check NBT, e.g. data sticks
        GTRecipe checkNBTRecipe = recipeMap.findRecipeQuery()
            .items(ItemList.Tool_DataStick.get(0))
            .find();
        assertNull(checkNBTRecipe);
    }

    @Test
    void findWithSpecificOreDictionary() {
        // https://github.com/GTNewHorizons/GT5-Unofficial/pull/2379
        // We cannot use circuit assembling recipe like the issue mentioned above,
        // as mUnificationTarget is not set for circuits in GT5.
        // But it works in the same way; specific circuit -> GT ore block, unificated circuit -> vanilla ore block
        GTRecipe recipeCorrectOre = recipeMap.findRecipeQuery()
            .items(new ItemStack(sBlockOres1, 1, 32))
            .find();
        assertNotNull(recipeCorrectOre);

        GTRecipe recipeWrongOre = recipeMap.findRecipeQuery()
            .items(new ItemStack(iron_ore, 1))
            .find();
        assertNull(recipeWrongOre);
    }
}
