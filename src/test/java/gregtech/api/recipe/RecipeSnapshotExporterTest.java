package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.Item;

import org.junit.jupiter.api.Test;

import gregtech.api.recipe.RecipeLookupValidator.RecipeLookupValidationTarget;
import gregtech.api.util.GTRecipe;

class RecipeSnapshotExporterTest {

    @Test
    void snapshotExportFlagDefaultsToFalse() {
        String previous = System.getProperty(RecipeSnapshotExporter.EXPORT_PROPERTY);
        System.clearProperty(RecipeSnapshotExporter.EXPORT_PROPERTY);
        try {
            assertFalse(RecipeSnapshotExporter.shouldExportSnapshot());
        } finally {
            restoreProperty(RecipeSnapshotExporter.EXPORT_PROPERTY, previous);
        }
    }

    @Test
    void snapshotExportFlagRequiresNonBlankPath() {
        String previous = System.getProperty(RecipeSnapshotExporter.EXPORT_PROPERTY);
        System.setProperty(RecipeSnapshotExporter.EXPORT_PROPERTY, "   ");
        try {
            assertFalse(RecipeSnapshotExporter.shouldExportSnapshot());
        } finally {
            restoreProperty(RecipeSnapshotExporter.EXPORT_PROPERTY, previous);
        }
    }

    @Test
    void canonicalHashIsStableForIdenticalRecipes() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        RecipeCategory category = RecipeValidationTestSupport.category();
        Item input = RecipeValidationTestSupport.item("snapshot.validation.input");
        Item output = RecipeValidationTestSupport.item("snapshot.validation.output");
        GTRecipe first = RecipeValidationTestSupport.recipe(input, output, category);
        GTRecipe second = RecipeValidationTestSupport.recipe(input, output, category);

        String firstCanonical = RecipeLookupValidator.describeRecipeCanonical(first);
        String secondCanonical = RecipeLookupValidator.describeRecipeCanonical(second);
        assertEquals(firstCanonical, secondCanonical);
        assertEquals(
            RecipeSnapshotExporter.hashCanonical(firstCanonical),
            RecipeSnapshotExporter.hashCanonical(secondCanonical));
    }

    @Test
    void snapshotAggregationIsIndependentOfRecipeIterationOrder() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        RecipeCategory category = RecipeValidationTestSupport.category();
        Item inputA = RecipeValidationTestSupport.item("snapshot.validation.a.input");
        Item outputA = RecipeValidationTestSupport.item("snapshot.validation.a.output");
        Item inputB = RecipeValidationTestSupport.item("snapshot.validation.b.input");
        Item outputB = RecipeValidationTestSupport.item("snapshot.validation.b.output");
        GTRecipe recipeA = RecipeValidationTestSupport.recipe(inputA, outputA, category);
        GTRecipe recipeB = RecipeValidationTestSupport.recipe(inputA, outputA, category);
        GTRecipe recipeC = RecipeValidationTestSupport.recipe(inputB, outputB, category);

        RecipeSnapshotExporter.RecipeSnapshot forward = buildSnapshotForRecipes(
            Arrays.asList(recipeA, recipeB, recipeC));
        RecipeSnapshotExporter.RecipeSnapshot reverse = buildSnapshotForRecipes(
            Arrays.asList(recipeC, recipeB, recipeA));

        assertEquals(forward.summary.instanceCount, reverse.summary.instanceCount);
        assertEquals(forward.summary.uniqueKeyCount, reverse.summary.uniqueKeyCount);
        assertEquals(
            RecipeSnapshotExporter.aggregateKeyCounts(forward),
            RecipeSnapshotExporter.aggregateKeyCounts(reverse));

        String duplicateKey = RecipeSnapshotExporter
            .hashCanonical(RecipeLookupValidator.describeRecipeCanonical(recipeA));
        assertEquals(2, forward.maps.get("gt.recipe.test.snapshot").keys.get(duplicateKey).count);
    }

    @Test
    void snapshotSkipsDisabledFakeAndNoInputRecipes() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        RecipeCategory category = RecipeValidationTestSupport.category();
        GTRecipe valid = RecipeValidationTestSupport.recipe(
            RecipeValidationTestSupport.item("snapshot.validation.valid.input"),
            RecipeValidationTestSupport.item("snapshot.validation.valid.output"),
            category);
        GTRecipe disabled = RecipeValidationTestSupport.recipe(
            RecipeValidationTestSupport.item("snapshot.validation.disabled.input"),
            RecipeValidationTestSupport.item("snapshot.validation.disabled.output"),
            category);
        disabled.mEnabled = false;
        GTRecipe fake = RecipeValidationTestSupport.recipe(
            RecipeValidationTestSupport.item("snapshot.validation.fake.input"),
            RecipeValidationTestSupport.item("snapshot.validation.fake.output"),
            category);
        fake.mFakeRecipe = true;
        GTRecipe noInputs = RecipeValidationTestSupport
            .recipeWithoutInputs(RecipeValidationTestSupport.item("snapshot.validation.no_input.output"), category);

        RecipeSnapshotExporter.RecipeSnapshot snapshot = buildSnapshotForRecipes(
            Arrays.asList(valid, disabled, fake, noInputs));

        assertEquals(1, snapshot.summary.instanceCount);
        assertEquals(1, snapshot.summary.uniqueKeyCount);
        assertEquals(4, snapshot.summary.rawInstanceCount);
        assertEquals(1, snapshot.summary.skippedDisabledRecipes);
        assertEquals(1, snapshot.summary.skippedFakeRecipes);
        assertEquals(1, snapshot.summary.skippedNoInputRecipes);
        assertEquals(4, snapshot.maps.get("gt.recipe.test.snapshot").instances.size());
    }

    @Test
    void snapshotV2IncludesRawInstanceEntries() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        RecipeCategory category = RecipeValidationTestSupport.category();
        GTRecipe recipe = RecipeValidationTestSupport.recipe(
            RecipeValidationTestSupport.item("snapshot.validation.raw.input"),
            RecipeValidationTestSupport.item("snapshot.validation.raw.output"),
            category);

        RecipeSnapshotExporter.RecipeSnapshot snapshot = buildSnapshotForRecipes(Collections.singletonList(recipe));
        assertEquals(RecipeSnapshotExporter.FORMAT_VERSION, snapshot.formatVersion);
        RecipeSnapshotExporter.InstanceEntry instance = snapshot.maps.get("gt.recipe.test.snapshot").instances.get(0);
        assertEquals(
            RecipeSnapshotDiff.gameplayFingerprint(RecipeLookupValidator.describeRecipeCanonical(recipe)),
            instance.gameplay);
        assertEquals(RecipeLookupValidator.describeRecipeForValidation(recipe), instance.raw);
    }

    private static RecipeSnapshotExporter.RecipeSnapshot buildSnapshotForRecipes(List<GTRecipe> recipes) {
        RecipeMapBackend backend = RecipeValidationTestSupport.backendForValidationTests();
        for (GTRecipe recipe : recipes) {
            backend.compileRecipe(recipe);
        }
        return RecipeSnapshotExporter.buildSnapshot(
            Collections.singletonList(new RecipeLookupValidationTarget("gt.recipe.test.snapshot", backend)));
    }

    private static void restoreProperty(String property, String previous) {
        if (previous == null) {
            System.clearProperty(property);
        } else {
            System.setProperty(property, previous);
        }
    }
}
