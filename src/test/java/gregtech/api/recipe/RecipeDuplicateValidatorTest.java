package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import net.minecraft.item.Item;

import org.junit.jupiter.api.Test;

import gregtech.api.recipe.RecipeLookupValidator.RecipeLookupValidationTarget;
import gregtech.api.util.GTRecipe;

class RecipeDuplicateValidatorTest {

    @Test
    void duplicateValidationFlagDefaultsToFalse() {
        String previous = System.getProperty(RecipeDuplicateValidator.VALIDATE_DUPLICATES_PROPERTY);
        System.clearProperty(RecipeDuplicateValidator.VALIDATE_DUPLICATES_PROPERTY);
        try {
            assertFalse(RecipeDuplicateValidator.shouldValidateDuplicates());
        } finally {
            restoreProperty(RecipeDuplicateValidator.VALIDATE_DUPLICATES_PROPERTY, previous);
        }
    }

    @Test
    void duplicateValidationFlagCanBeEnabled() {
        String previous = System.getProperty(RecipeDuplicateValidator.VALIDATE_DUPLICATES_PROPERTY);
        System.setProperty(RecipeDuplicateValidator.VALIDATE_DUPLICATES_PROPERTY, "true");
        try {
            assertTrue(RecipeDuplicateValidator.shouldValidateDuplicates());
        } finally {
            restoreProperty(RecipeDuplicateValidator.VALIDATE_DUPLICATES_PROPERTY, previous);
        }
    }

    @Test
    void duplicateValidatorReportsCollisionCheckDuplicates() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.duplicate.test.basic";
        RecipeCategory category = RecipeValidationTestSupport.category();
        Item input = RecipeValidationTestSupport.item("duplicate.validation.basic.input");
        GTRecipe first = RecipeValidationTestSupport
            .recipe(input, RecipeValidationTestSupport.item("duplicate.validation.basic.first.output"), category);
        GTRecipe second = RecipeValidationTestSupport
            .recipe(input, RecipeValidationTestSupport.item("duplicate.validation.basic.second.output"), category);
        RecipeMapBackend backend = RecipeValidationTestSupport.backendForValidationTests();
        backend.compileRecipe(first);
        backend.compileRecipe(second);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeDuplicateValidator.validateDuplicates(mapName, backend));

        assertTrue(
            error.getMessage()
                .contains("GT recipe duplicate validation found 1 duplicate(s)"));
        assertTrue(
            error.getMessage()
                .contains("map=" + mapName));
        assertTrue(
            error.getMessage()
                .contains("queryRecipe="));
        assertTrue(
            error.getMessage()
                .contains("duplicateMatches="));
        assertTrue(
            error.getMessage()
                .contains("duplicateMatchCount=1"));
        assertTrue(
            error.getMessage()
                .contains("duplicate.validation.basic.input"));
    }

    @Test
    void duplicateValidatorReportsEachDuplicateGroupOnce() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.duplicate.test.group";
        RecipeCategory category = RecipeValidationTestSupport.category();
        Item input = RecipeValidationTestSupport.item("duplicate.validation.group.input");
        GTRecipe first = RecipeValidationTestSupport
            .recipe(input, RecipeValidationTestSupport.item("duplicate.validation.group.first.output"), category);
        GTRecipe second = RecipeValidationTestSupport
            .recipe(input, RecipeValidationTestSupport.item("duplicate.validation.group.second.output"), category);
        GTRecipe third = RecipeValidationTestSupport
            .recipe(input, RecipeValidationTestSupport.item("duplicate.validation.group.third.output"), category);
        RecipeMapBackend backend = RecipeValidationTestSupport.backendForValidationTests();
        backend.compileRecipe(first);
        backend.compileRecipe(second);
        backend.compileRecipe(third);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeDuplicateValidator.validateDuplicates(mapName, backend));

        assertTrue(
            error.getMessage()
                .contains("GT recipe duplicate validation found 1 duplicate(s)"));
        assertTrue(
            error.getMessage()
                .contains("duplicateMatchCount=2"));
    }

    @Test
    void duplicateValidatorSkipsFakeDisabledAndNoInputRecipes() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        RecipeCategory category = RecipeValidationTestSupport.category();
        RecipeMapBackend backend = RecipeValidationTestSupport.backendForValidationTests();
        GTRecipe fakeRecipe = RecipeValidationTestSupport.recipe(
            RecipeValidationTestSupport.item("duplicate.validation.fake.input"),
            RecipeValidationTestSupport.item("duplicate.validation.fake.output"),
            category);
        fakeRecipe.mFakeRecipe = true;
        GTRecipe disabledRecipe = RecipeValidationTestSupport.recipe(
            RecipeValidationTestSupport.item("duplicate.validation.disabled.input"),
            RecipeValidationTestSupport.item("duplicate.validation.disabled.output"),
            category);
        disabledRecipe.mEnabled = false;
        GTRecipe noInputRecipe = RecipeValidationTestSupport
            .recipeWithoutInputs(RecipeValidationTestSupport.item("duplicate.validation.no_input.output"), category);
        backend.compileRecipe(fakeRecipe);
        backend.compileRecipe(disabledRecipe);
        backend.compileRecipe(noInputRecipe);

        assertDoesNotThrow(
            () -> RecipeDuplicateValidator.validateDuplicates("gt.recipe.duplicate.test.skipped", backend));
    }

    @Test
    void duplicateValidatorReportIncludesCapturedCallsite() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.duplicate.test.callsite";
        RecipeCategory category = RecipeValidationTestSupport.category();
        Item input = RecipeValidationTestSupport.item("duplicate.validation.callsite.input");
        GTRecipe first = RecipeValidationTestSupport
            .recipe(input, RecipeValidationTestSupport.item("duplicate.validation.callsite.first.output"), category);
        GTRecipe second = RecipeValidationTestSupport
            .recipe(input, RecipeValidationTestSupport.item("duplicate.validation.callsite.second.output"), category);
        first.stackTraces = new java.util.ArrayList<>();
        first.stackTraces.add(
            Arrays.asList(
                "gregtech.api.util.GTRecipe$GTRecipe_WithAlt.<init>(GTRecipe.java:1429)",
                "gregtech.loaders.oreprocessing.ProcessingDust.registerOre(ProcessingDust.java:565)"));
        second.stackTraces = new java.util.ArrayList<>();
        second.stackTraces.add(
            Arrays.asList(
                "gregtech.api.util.GTRecipe$GTRecipe_WithAlt.<init>(GTRecipe.java:1429)",
                "gregtech.loaders.oreprocessing.ProcessingDust.registerOre(ProcessingDust.java:593)"));
        RecipeMapBackend backend = RecipeValidationTestSupport.backendForValidationTests();
        backend.compileRecipe(first);
        backend.compileRecipe(second);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeDuplicateValidator.validateDuplicates(mapName, backend));

        assertTrue(
            error.getMessage()
                .contains("ProcessingDust.registerOre(ProcessingDust.java:565)"));
        assertTrue(
            error.getMessage()
                .contains("ProcessingDust.registerOre(ProcessingDust.java:593)"));
    }

    @Test
    void duplicateValidatorSkipsAddonMapsWithExpectedCollisions() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        RecipeCategory category = RecipeValidationTestSupport.category();
        Item input = RecipeValidationTestSupport.item("duplicate.validation.addon.input");
        RecipeMapBackend backend = RecipeValidationTestSupport.backendForValidationTests();
        backend.compileRecipe(
            RecipeValidationTestSupport
                .recipe(input, RecipeValidationTestSupport.item("duplicate.validation.addon.first.output"), category));
        backend.compileRecipe(
            RecipeValidationTestSupport
                .recipe(input, RecipeValidationTestSupport.item("duplicate.validation.addon.second.output"), category));

        for (String mapName : Arrays
            .asList("gt.recipe.spaceMining", "gt.recipe.fog_molten", "cropsnh.recipes.cropBreeder")) {
            assertDoesNotThrow(
                () -> RecipeDuplicateValidator
                    .validateDuplicates(Collections.singletonList(new RecipeLookupValidationTarget(mapName, backend))),
                mapName);
        }
    }

    @Test
    void duplicateValidatorSkipsSpaceMiningMap() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        RecipeCategory category = RecipeValidationTestSupport.category();
        Item input = RecipeValidationTestSupport.item("duplicate.validation.space_mining.input");
        RecipeMapBackend backend = RecipeValidationTestSupport.backendForValidationTests();
        backend.compileRecipe(
            RecipeValidationTestSupport.recipe(
                input,
                RecipeValidationTestSupport.item("duplicate.validation.space_mining.first.output"),
                category));
        backend.compileRecipe(
            RecipeValidationTestSupport.recipe(
                input,
                RecipeValidationTestSupport.item("duplicate.validation.space_mining.second.output"),
                category));

        assertDoesNotThrow(
            () -> RecipeDuplicateValidator.validateDuplicates(
                Collections.singletonList(new RecipeLookupValidationTarget("gt.recipe.spaceMining", backend))));
    }

    @Test
    void duplicateValidatorCollectionEntryPointSkipsOverwriteFindBackends() {
        RecipeMapBackend overwriteBackend = new RecipeMapBackend(new RecipeMapBackendPropertiesBuilder()) {

            @Override
            public boolean doesOverwriteFindRecipe() {
                return true;
            }
        };

        assertDoesNotThrow(
            () -> RecipeDuplicateValidator.validateDuplicates(
                Collections.singletonList(
                    new RecipeLookupValidationTarget("gt.recipe.duplicate.test.overwrite", overwriteBackend))));
    }

    @Test
    void duplicateValidationFlagEnablesCallsiteCapture() {
        RecipeValidationTestSupport.ensureMinecraftStackComparisonItem();
        String lookupPrevious = System.getProperty(RecipeLookupValidator.VALIDATE_LOOKUP_PROPERTY);
        String duplicatePrevious = System.getProperty(RecipeDuplicateValidator.VALIDATE_DUPLICATES_PROPERTY);
        System.clearProperty(RecipeLookupValidator.VALIDATE_LOOKUP_PROPERTY);
        System.clearProperty(RecipeLookupValidator.CAPTURE_CALLSITE_PROPERTY);
        System.setProperty(RecipeDuplicateValidator.VALIDATE_DUPLICATES_PROPERTY, "true");
        try {
            assertTrue(RecipeLookupValidator.shouldCaptureRecipeCallsites());
            RecipeCategory category = RecipeValidationTestSupport.category();
            GTRecipe recipe = RecipeValidationTestSupport.recipe(
                RecipeValidationTestSupport.item("duplicate.validation.capture.input"),
                RecipeValidationTestSupport.item("duplicate.validation.capture.output"),
                category);
            assertNotNull(recipe.stackTraces);
        } finally {
            restoreProperty(RecipeLookupValidator.VALIDATE_LOOKUP_PROPERTY, lookupPrevious);
            restoreProperty(RecipeDuplicateValidator.VALIDATE_DUPLICATES_PROPERTY, duplicatePrevious);
        }
    }

    private static void restoreProperty(String key, String value) {
        if (value == null) {
            System.clearProperty(key);
        } else {
            System.setProperty(key, value);
        }
    }
}
