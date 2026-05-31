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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.objects.SubstituteFluidStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GTRecipe;

class GTRecipeTest {

    static RecipeMap<?> recipeMap;
    static GTRecipe lapotronChipRecipe;
    static RecipeMap<?> lookupMap;
    static GTRecipe itemOnlyRecipe;
    static AtomicInteger lookupMapIds = new AtomicInteger();

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

        lookupMap = RecipeMapBuilder.of("__lookup_test__")
            .maxIO(9, 1, 2, 1)
            .build();

        itemOnlyRecipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(lookupMap));
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

    @Test
    void findItemOnlyRecipe() {
        GTRecipe recipe = lookupMap.findRecipeQuery()
            .items(new ItemStack(log, 1, 0))
            .find();

        assertSame(itemOnlyRecipe, recipe);
    }

    @Test
    void findFluidOnlyRecipeWhenItemInputsAreNotRequired() {
        RecipeMap<?> map = lookupMap("__fluid_only__").minInputs(0, 1)
            .build();
        FluidStack water = new FluidStack(FluidRegistry.WATER, 1_000);
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .fluidInputs(water)
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        GTRecipe found = map.findRecipeQuery()
            .fluids(new FluidStack(FluidRegistry.WATER, 1_000))
            .find();

        assertSame(recipe, found);
    }

    @Test
    void repeatedEquivalentInputsRequireEnoughTotalStackSize() {
        RecipeMap<?> map = lookupMap("__repeated_items__").build();
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(stone_slab, 64), new ItemStack(stone_slab, 64))
                .itemOutputs(new ItemStack(stone, 2))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(stone_slab, 127))
                .find());
        assertSame(
            recipe,
            map.findRecipeQuery()
                .items(new ItemStack(stone_slab, 128))
                .find());
    }

    @Test
    void oreDictionaryAlternativeMatchesRegisteredStackOnly() {
        RecipeMap<?> map = lookupMap("__ore_alt__").build();
        String oreName = uniqueOreName("lookupTestOre");
        OreDictionary.registerOre(oreName, new ItemStack(log, 1, 0));
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new OreDictItemStack(oreName, 2))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertSame(
            recipe,
            map.findRecipeQuery()
                .items(new ItemStack(log, 2, 0))
                .find());
        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(planks, 2, 0))
                .find());
    }

    @Test
    void oreDictionaryAndExactRecipesBothRemainReachable() {
        RecipeMap<?> map = lookupMap("__ore_before_exact__").build();
        String oreName = uniqueOreName("lookupTestOreOrder");
        OreDictionary.registerOre(oreName, new ItemStack(log, 1, 0));
        GTRecipe oreRecipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new OreDictItemStack(oreName, 1))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));
        GTRecipe exactRecipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(stone, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        GTRecipe firstMatch = map.findRecipeQuery()
            .items(new ItemStack(log, 1, 0))
            .find();
        assertNotNull(firstMatch);
        assertTrue(firstMatch == oreRecipe || firstMatch == exactRecipe);

        List<GTRecipe> matches = map.findRecipeQuery()
            .items(new ItemStack(log, 1, 0))
            .findAll()
            .collect(Collectors.toList());
        assertEquals(2, matches.size());
        assertTrue(matches.contains(oreRecipe));
        assertTrue(matches.contains(exactRecipe));
    }

    @Test
    void fluidAlternativesFindSameRecipe() {
        RecipeMap<?> map = lookupMap("__fluid_alt__").minInputs(0, 1)
            .build();
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .fluidInputs(
                    new SubstituteFluidStack(
                        new FluidStack(FluidRegistry.WATER, 1_000),
                        new FluidStack(FluidRegistry.LAVA, 500)))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertSame(
            recipe,
            map.findRecipeQuery()
                .fluids(new FluidStack(FluidRegistry.WATER, 1_000))
                .find());
        assertSame(
            recipe,
            map.findRecipeQuery()
                .fluids(new FluidStack(FluidRegistry.LAVA, 500))
                .find());
    }

    @Test
    void nbtSensitiveRecipesRequireMatchingTagsButNormalRecipesDoNot() {
        RecipeMap<?> map = lookupMap("__nbt__").build();
        GTRecipe normalRecipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(lapis_block, 1))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));
        ItemStack taggedGlass = new ItemStack(glass_bottle, 1);
        taggedGlass.setTagCompound(tagWithInteger(123456));
        GTRecipe nbtRecipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(taggedGlass)
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .nbtSensitive()
                .addTo(map));

        ItemStack taggedLapis = new ItemStack(lapis_block, 1);
        taggedLapis.setTagCompound(tagWithInteger(1));
        assertSame(
            normalRecipe,
            map.findRecipeQuery()
                .items(taggedLapis)
                .find());

        ItemStack matchingGlass = new ItemStack(glass_bottle, 1);
        matchingGlass.setTagCompound(tagWithInteger(123456));
        assertSame(
            nbtRecipe,
            map.findRecipeQuery()
                .items(matchingGlass)
                .find());

        ItemStack mismatchingGlass = new ItemStack(glass_bottle, 1);
        mismatchingGlass.setTagCompound(tagWithInteger(654321));
        assertNull(
            map.findRecipeQuery()
                .items(mismatchingGlass)
                .find());
    }

    @Test
    void specialSlotSensitiveMapRequiresMatchingSpecialSlot() {
        RecipeMap<?> map = lookupMap("__special_slot__").specialSlotSensitive()
            .build();
        ItemStack specialSlot = new ItemStack(iron_ingot, 1);
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .special(specialSlot)
                .duration(0)
                .eut(0)
                .addTo(map));

        assertSame(
            recipe,
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .specialSlot(new ItemStack(iron_ingot, 1))
                .find());
        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .find());
        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .specialSlot(new ItemStack(planks, 1, 0))
                .find());
    }

    @Test
    void fakeRecipeIsNotFoundButStillCountsAsKnownInput() {
        RecipeMap<?> map = lookupMap("__fake__").build();
        GTRecipe hiddenRecipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .hidden()
                .addTo(map));
        ItemStack fakeInput = new ItemStack(planks, 1, 0);
        onlyRecipe(
            RA.stdBuilder()
                .itemInputs(fakeInput)
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .fake()
                .addTo(map));

        assertSame(
            hiddenRecipe,
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .find());
        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(planks, 1, 0))
                .find());
        assertTrue(map.containsInput(new ItemStack(planks, 1, 0)));
    }

    @Test
    void fakeOreAndAltFluidRecipeCountsAsKnownInputOnly() {
        RecipeMap<?> map = lookupMap("__fake_alt_contains__").minInputs(0, 1)
            .build();
        String oreName = uniqueOreName("lookupTestFakeContainsOre");
        OreDictionary.registerOre(oreName, new ItemStack(log, 1, 0));
        onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new OreDictItemStack(oreName, 1))
                .fluidInputs(
                    new SubstituteFluidStack(
                        new FluidStack(FluidRegistry.WATER, 1_000),
                        new FluidStack(FluidRegistry.LAVA, 500)))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .fake()
                .addTo(map));

        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .fluids(new FluidStack(FluidRegistry.WATER, 1_000))
                .find());
        assertTrue(map.containsInput(new ItemStack(log, 1, 0)));
        assertTrue(map.containsInput(FluidRegistry.WATER));
        assertTrue(map.containsInput(FluidRegistry.LAVA));
    }

    @Test
    void collisionCheckFindsExistingRecipe() {
        RecipeMap<?> map = lookupMap("__collision__").build();
        onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 4, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertTrue(
            map.findRecipeQuery()
                .items(new ItemStack(log, 4, 0))
                .checkCollision());
        assertFalse(
            map.findRecipeQuery()
                .items(new ItemStack(planks, 4, 0))
                .checkCollision());
    }

    @Test
    void collisionCheckIgnoresStackSizes() {
        RecipeMap<?> map = lookupMap("__collision_size__").build();
        onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 4, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .find());
        assertTrue(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .checkCollision());
    }

    @Test
    void removedRecipeIsRejectedFromWarmedCacheMap() {
        RecipeMap<?> map = lookupMap("__stale_cache_map_remove__").build();
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertSame(
            recipe,
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .caching(true)
                .find());
        map.getBackend()
            .removeRecipe(recipe);
        assertFalse(
            map.getAllRecipes()
                .contains(recipe));

        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .find());
    }

    @Test
    void clearRecipesRejectsWarmedCacheMapWhenRecipeMapIsEmpty() {
        RecipeMap<?> map = lookupMap("__stale_cache_map_clear__").build();
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertSame(
            recipe,
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .caching(true)
                .find());
        map.getBackend()
            .clearRecipes();
        assertTrue(
            map.getAllRecipes()
                .isEmpty());

        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .find());
    }

    @Test
    void removedRecipeIsRejectedFromCachedRecipeArgument() {
        RecipeMap<?> map = lookupMap("__stale_cached_recipe__").build();
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        map.getBackend()
            .removeRecipe(recipe);
        assertFalse(
            map.getAllRecipes()
                .contains(recipe));

        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .cachedRecipe(recipe)
                .find());
    }

    @Test
    void reInitRejectsRemovedRecipeFromWarmedCacheMapAndKeepsRemainingRecipe() {
        RecipeMap<?> map = lookupMap("__stale_cache_map_reinit__").build();
        GTRecipe removed = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));
        GTRecipe remaining = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 1))
                .itemOutputs(new ItemStack(stone, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertSame(
            removed,
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .caching(true)
                .find());
        map.getBackend()
            .removeRecipe(removed);
        map.getBackend()
            .reInit();

        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .find());
        assertSame(
            remaining,
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 1))
                .find());
    }

    @Test
    void removeThenReInitRejectsRemovedRecipeAndKeepsRemainingTrieCandidate() {
        RecipeMap<?> map = lookupMap("__remove_reinit_remaining__").build();
        GTRecipe removed = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));
        GTRecipe remaining = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 2))
                .itemOutputs(new ItemStack(stone, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        map.getBackend()
            .removeRecipe(removed);
        map.getBackend()
            .reInit();

        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .find());
        assertSame(
            remaining,
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 2))
                .find());
    }

    @Test
    void containsInputTracksRemoveClearAndReInit() {
        RecipeMap<?> map = lookupMap("__contains_lifecycle__").minInputs(1, 1)
            .build();
        GTRecipe removed = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 1_000))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertTrue(map.containsInput(new ItemStack(log, 1, 0)));
        assertTrue(map.containsInput(FluidRegistry.WATER));

        map.getBackend()
            .removeRecipe(removed);
        assertFalse(map.containsInput(new ItemStack(log, 1, 0)));
        assertFalse(map.containsInput(FluidRegistry.WATER));

        onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(planks, 1, 0))
                .fluidInputs(new FluidStack(FluidRegistry.LAVA, 1_000))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));
        map.getBackend()
            .reInit();
        assertTrue(map.containsInput(new ItemStack(planks, 1, 0)));
        assertTrue(map.containsInput(FluidRegistry.LAVA));

        map.getBackend()
            .clearRecipes();
        assertFalse(map.containsInput(new ItemStack(planks, 1, 0)));
        assertFalse(map.containsInput(FluidRegistry.LAVA));
    }

    @Test
    void findAllReturnsDistinctRecipesInRegistrationOrder() {
        RecipeMap<?> map = lookupMap("__find_all_order__").build();
        GTRecipe first = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));
        GTRecipe second = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, 0))
                .itemOutputs(new ItemStack(stone, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        assertSame(
            first,
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0))
                .find());

        List<GTRecipe> results = map.findRecipeQuery()
            .items(new ItemStack(log, 1, 0))
            .findAll()
            .collect(Collectors.toList());

        assertEquals(Arrays.asList(first, second), results);
        assertEquals(
            results.size(),
            results.stream()
                .distinct()
                .count());
    }

    @Test
    void findAllDocumentsDuplicatePolicyForAlternativeIndexes() {
        RecipeMap<?> map = lookupMap("__find_all_alt_duplicates__").minInputs(0, 1)
            .build();
        String oreName = uniqueOreName("lookupTestDuplicateOre");
        OreDictionary.registerOre(oreName, new ItemStack(log, 1, 0));
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new OreDictItemStack(oreName, 1))
                .fluidInputs(
                    new SubstituteFluidStack(
                        new FluidStack(FluidRegistry.WATER, 1_000),
                        new FluidStack(FluidRegistry.LAVA, 500)))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(0)
                .eut(0)
                .addTo(map));

        List<GTRecipe> itemAndWaterResults = map.findRecipeQuery()
            .items(new ItemStack(log, 1, 0))
            .fluids(new FluidStack(FluidRegistry.WATER, 1_000))
            .findAll()
            .collect(Collectors.toList());
        List<GTRecipe> itemAndLavaResults = map.findRecipeQuery()
            .items(new ItemStack(log, 1, 0))
            .fluids(new FluidStack(FluidRegistry.LAVA, 500))
            .findAll()
            .collect(Collectors.toList());

        assertEquals(Collections.singletonList(recipe), itemAndWaterResults);
        assertEquals(
            itemAndWaterResults.size(),
            itemAndWaterResults.stream()
                .distinct()
                .count());
        assertEquals(Collections.singletonList(recipe), itemAndLavaResults);
        assertEquals(
            itemAndLavaResults.size(),
            itemAndLavaResults.stream()
                .distinct()
                .count());
    }

    @Test
    void findsRareDiscriminatorAmongManyRecipesSharingCommonInput() {
        RecipeMap<?> map = lookupMap("__stress_common_rare__").maxIO(2, 1, 0, 0)
            .build();
        GTRecipe expected = null;
        for (int i = 0; i < 96; i++) {
            GTRecipe recipe = onlyRecipe(
                RA.stdBuilder()
                    .itemInputs(new ItemStack(log, 1, 0), new ItemStack(iron_ingot, 1, i))
                    .itemOutputs(new ItemStack(chest, 1))
                    .duration(0)
                    .eut(0)
                    .addTo(map));
            if (i == 73) {
                expected = recipe;
            }
        }

        assertSame(
            expected,
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0), new ItemStack(iron_ingot, 1, 73))
                .find());
        assertNull(
            map.findRecipeQuery()
                .items(new ItemStack(log, 1, 0), new ItemStack(iron_ingot, 1, 200))
                .find());
    }

    @SuppressWarnings("deprecation")
    @Test
    void recipeMapAddBypassesLegacyAddRecipeInputValidation() {
        RecipeMap<?> map = lookupMap("__direct_add_bypass__").minInputs(1, 1)
            .build();
        GTRecipe legacyRejected = RA.stdBuilder()
            .itemOutputs(new ItemStack(chest, 1))
            .duration(0)
            .eut(0)
            .build()
            .orElseThrow(AssertionError::new);
        GTRecipe bypassed = RA.stdBuilder()
            .itemOutputs(new ItemStack(stone, 1))
            .duration(0)
            .eut(0)
            .build()
            .orElseThrow(AssertionError::new);

        assertNull(map.addRecipe(legacyRejected, false, false, false));
        assertSame(bypassed, map.add(bypassed));
        assertTrue(
            map.getAllRecipes()
                .contains(bypassed));
    }

    private static RecipeMapBuilder<?> lookupMap(String suffix) {
        return RecipeMapBuilder.of("__lookup_test__" + suffix + lookupMapIds.incrementAndGet())
            .maxIO(9, 1, 2, 1);
    }

    private static GTRecipe onlyRecipe(Collection<GTRecipe> recipes) {
        assertEquals(1, recipes.size());
        return recipes.iterator()
            .next();
    }

    private static String uniqueOreName(String prefix) {
        return prefix + lookupMapIds.incrementAndGet();
    }

    private static NBTTagCompound tagWithInteger(int value) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("integer", value);
        return tag;
    }
}
