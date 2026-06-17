package gregtech.api.recipe;

import java.util.ArrayList;
import java.util.List;

public final class RecipeValidationRunner {

    private RecipeValidationRunner() {}

    public static void run(boolean validateLookup, boolean validateDuplicates, Runnable lookupRunner,
        Runnable duplicateRunner) {
        List<IllegalStateException> failures = new ArrayList<>();
        if (validateLookup) {
            try {
                lookupRunner.run();
            } catch (IllegalStateException e) {
                failures.add(e);
            }
        }
        if (validateDuplicates) {
            try {
                duplicateRunner.run();
            } catch (IllegalStateException e) {
                failures.add(e);
            }
        }
        if (!failures.isEmpty()) {
            throw combinedRecipeValidationException(failures);
        }
    }

    private static IllegalStateException combinedRecipeValidationException(List<IllegalStateException> failures) {
        if (failures.size() == 1) {
            return failures.get(0);
        }
        StringBuilder message = new StringBuilder("GT recipe validation found issues in multiple validators.");
        for (int i = 0; i < failures.size(); i++) {
            message.append("\n\n")
                .append(i + 1)
                .append(") ")
                .append(
                    failures.get(i)
                        .getMessage());
        }
        IllegalStateException combined = new IllegalStateException(message.toString());
        for (IllegalStateException failure : failures) {
            combined.addSuppressed(failure);
        }
        return combined;
    }
}
