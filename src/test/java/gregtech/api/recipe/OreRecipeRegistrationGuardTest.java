package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
        assertTrue(
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
    void stackAwareProcessingKeepsDistinctItemIdentitiesWithSameUnlocalizedName() {
        OreRecipeRegistrationGuard.begin();
        Item firstItem = new Item().setUnlocalizedName("test.zinc.ore");
        Item secondItem = new Item().setUnlocalizedName("test.zinc.ore");
        ItemStack firstStack = new ItemStack(firstItem, 1, 0);
        ItemStack secondStack = new ItemStack(secondItem, 1, 0);

        assertTrue(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.ore, Materials.Zinc, firstStack, "ProcessingOre"));
        assertTrue(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.ore, Materials.Zinc, secondStack, "ProcessingOre"));
        assertFalse(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.ore, Materials.Zinc, firstStack, "ProcessingOre"));
    }

    @Test
    void stackAwareProcessingKeepsDistinctNbtVariants() {
        OreRecipeRegistrationGuard.begin();
        Item item = new Item().setUnlocalizedName("test.crop.seed");
        ItemStack firstSeed = taggedStack(item, "stoneLily");
        ItemStack secondSeed = taggedStack(item, "endStoneLily");

        assertTrue(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.dust, Materials.Stone, firstSeed, "ProcessingCrop"));
        assertFalse(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.dust, Materials.Stone, firstSeed, "ProcessingCrop"));
        assertTrue(
            OreRecipeRegistrationGuard.tryProcessStack(OrePrefixes.dust, Materials.Stone, secondSeed, "ProcessingCrop"));
    }

    @Test
    void dedupesReverseRecipesPerConcreteStack() {
        OreRecipeRegistrationGuard.begin();
        Item firstItem = new Item().setUnlocalizedName("test.reverse.silicon");
        Item secondItem = new Item().setUnlocalizedName("test.reverse.silicon");
        ItemStack firstStack = new ItemStack(firstItem, 1, 0);
        ItemStack secondStack = new ItemStack(secondItem, 1, 0);

        assertTrue(
            OreRecipeRegistrationGuard
                .tryRegisterReverseRecipe("macerator", Materials.Silicon, OrePrefixes.ingot, firstStack));
        assertFalse(
            OreRecipeRegistrationGuard
                .tryRegisterReverseRecipe("macerator", Materials.Silicon, OrePrefixes.ingot, firstStack));
        assertTrue(
            OreRecipeRegistrationGuard
                .tryRegisterReverseRecipe("macerator", Materials.Silicon, OrePrefixes.ingot, secondStack));
        assertTrue(
            OreRecipeRegistrationGuard
                .tryRegisterReverseRecipe("fluidSmelting", Materials.Silicon, OrePrefixes.ingot, firstStack));
    }

    @Test
    void reverseRecipeDedupingKeepsDistinctNbtVariants() {
        OreRecipeRegistrationGuard.begin();
        Item item = new Item().setUnlocalizedName("test.reverse.crop.seed");
        ItemStack firstSeed = taggedStack(item, "stoneLily");
        ItemStack secondSeed = taggedStack(item, "endStoneLily");

        assertTrue(
            OreRecipeRegistrationGuard
                .tryRegisterReverseRecipe("macerator", Materials.Stone, OrePrefixes.dust, firstSeed));
        assertFalse(
            OreRecipeRegistrationGuard
                .tryRegisterReverseRecipe("macerator", Materials.Stone, OrePrefixes.dust, firstSeed));
        assertTrue(
            OreRecipeRegistrationGuard
                .tryRegisterReverseRecipe("macerator", Materials.Stone, OrePrefixes.dust, secondSeed));
    }

    @Test
    void reverseRecipeGuardIsInactiveOutsideOreProcessingBatch() {
        Item item = new Item().setUnlocalizedName("test.reverse.outside.batch");
        ItemStack stack = new ItemStack(item, 1, 0);

        assertTrue(OreRecipeRegistrationGuard.tryRegisterReverseRecipe("macerator", Materials.Stone, OrePrefixes.dust, stack));
        assertTrue(OreRecipeRegistrationGuard.tryRegisterReverseRecipe("macerator", Materials.Stone, OrePrefixes.dust, stack));
    }

    @Test
    void beginClearsReverseRecipeGuardState() {
        Item item = new Item().setUnlocalizedName("test.reverse.new.batch");
        ItemStack stack = new ItemStack(item, 1, 0);

        OreRecipeRegistrationGuard.begin();
        assertTrue(OreRecipeRegistrationGuard.tryRegisterReverseRecipe("macerator", Materials.Stone, OrePrefixes.dust, stack));
        assertFalse(OreRecipeRegistrationGuard.tryRegisterReverseRecipe("macerator", Materials.Stone, OrePrefixes.dust, stack));
        OreRecipeRegistrationGuard.end();

        OreRecipeRegistrationGuard.begin();
        assertTrue(OreRecipeRegistrationGuard.tryRegisterReverseRecipe("macerator", Materials.Stone, OrePrefixes.dust, stack));
    }

    @Test
    void endDisablesGuard() {
        OreRecipeRegistrationGuard.begin();
        assertTrue(OreRecipeRegistrationGuard.tryProcess(OrePrefixes.dust, "dustIron", "ProcessingShaping"));
        assertFalse(OreRecipeRegistrationGuard.tryProcess(OrePrefixes.dust, "dustIron", "ProcessingShaping"));
        OreRecipeRegistrationGuard.end();
        assertTrue(OreRecipeRegistrationGuard.tryProcess(OrePrefixes.dust, "dustIron", "ProcessingShaping"));
    }

    private static ItemStack taggedStack(Item item, String crop) {
        ItemStack stack = new ItemStack(item, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("crop", crop);
        stack.setTagCompound(tag);
        return stack;
    }
}
