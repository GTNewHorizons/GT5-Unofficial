package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

class OreRecipeRegistrationGuardTest {

    @AfterEach
    void tearDown() {
        OreRecipeRegistrationGuard.end();
        OreRecipeRegistrationGuard.clearReverseRecipeKeysForTesting();
    }

    @Test
    void inactiveGuardAlwaysAllowsProcessing() {
        assertTrue(OreRecipeRegistrationGuard.tryProcess(OrePrefixes.ingot, "ingotCopper", "ProcessingShaping"));
        assertTrue(OreRecipeRegistrationGuard.tryProcess(OrePrefixes.ingot, "ingotCopper", "ProcessingShaping"));
    }

    @Test
    void dedupesPerRegistratorPrefixAndOreDictName() {
        OreRecipeRegistrationGuard.begin();
        assertTrue(
            OreRecipeRegistrationGuard
                .tryProcess(OrePrefixes.ingot, Materials.Copper, "ingotCopper", "ProcessingShaping"));
        assertFalse(
            OreRecipeRegistrationGuard
                .tryProcess(OrePrefixes.ingot, Materials.Copper, "ingotCopper", "ProcessingShaping"));
        assertTrue(
            OreRecipeRegistrationGuard
                .tryProcess(OrePrefixes.ingot, Materials.Copper, "ingotCopper", "ProcessingIngot"));
        assertTrue(
            OreRecipeRegistrationGuard
                .tryProcess(OrePrefixes.ingotHot, Materials.Copper, "ingotHotCopper", "ProcessingIngot"));
        assertTrue(
            OreRecipeRegistrationGuard
                .tryProcess(OrePrefixes.block, Materials.Copper, "blockCopper", "ProcessingBlock"));
        assertFalse(
            OreRecipeRegistrationGuard
                .tryProcess(OrePrefixes.block, Materials.Copper, "blockCopperRailcraft", "ProcessingBlock"));
    }

    @Test
    void stackAwareProcessingDedupesAliasesButKeepsMetaVariants() {
        OreRecipeRegistrationGuard.begin();
        Item item = new Item().setHasSubtypes(true);
        ItemStack metaZero = new ItemStack(item, 1, 0);
        ItemStack metaOne = new ItemStack(item, 1, 1);

        assertTrue(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.ore, Materials.Iron, metaZero, "ProcessingOre"));
        assertFalse(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.ore, Materials.Iron, metaZero, "ProcessingOre"));
        assertTrue(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.ore, Materials.Iron, metaOne, "ProcessingOre"));
        assertTrue(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.ore, Materials.Copper, metaZero, "ProcessingOre"));
        assertTrue(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.ore, Materials.Iron, metaZero, "ProcessingGem"));
    }

    @Test
    void dedupesReverseRecipesPerMaterialAndPrefix() {
        assertTrue(
            OreRecipeRegistrationGuard.tryRegisterReverseRecipe("macerator", Materials.Copper, OrePrefixes.ingot));
        assertFalse(
            OreRecipeRegistrationGuard.tryRegisterReverseRecipe("macerator", Materials.Copper, OrePrefixes.ingot));
        assertTrue(
            OreRecipeRegistrationGuard.tryRegisterReverseRecipe("macerator", Materials.Copper, OrePrefixes.nugget));
        assertTrue(
            OreRecipeRegistrationGuard.tryRegisterReverseRecipe("fluidSmelting", Materials.Copper, OrePrefixes.ingot));
    }

    @Test
    void endDisablesGuard() {
        OreRecipeRegistrationGuard.begin();
        assertTrue(OreRecipeRegistrationGuard.tryProcess(OrePrefixes.dust, "dustIron", "ProcessingShaping"));
        assertFalse(OreRecipeRegistrationGuard.tryProcess(OrePrefixes.dust, "dustIron", "ProcessingShaping"));
        OreRecipeRegistrationGuard.end();
        assertTrue(OreRecipeRegistrationGuard.tryProcess(OrePrefixes.dust, "dustIron", "ProcessingShaping"));
    }
}
