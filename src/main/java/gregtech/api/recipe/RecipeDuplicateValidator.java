package gregtech.api.recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.ModContainer;
import gregtech.api.recipe.RecipeLookupValidator.RecipeLookupValidationTarget;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public final class RecipeDuplicateValidator {

    public static final String VALIDATE_DUPLICATES_PROPERTY = "gt.recipe.duplicates.validate";

    /**
     * Maps where input-collision duplicates are expected gameplay or addon-internal registration, not GT ore-autogen
     * bugs. Space mining shares tier inputs; runtime picks from a distance-weighted asteroid pool (axis X).
     */
    private static final Set<String> SKIPPED_MAP_NAMES = Set.of(
        "gt.recipe.spaceMining",
        "gt.recipe.fakespaceprojects",
        "gt.recipe.fog_molten",
        "gt.recipe.large_hadron_collider",
        "gt.recipe.largeboilerfakefuels",
        "gt.recipe.semifluidboilerfuels",
        "gtnhlanth.recipe.tc",
        "cropsnh.recipes.cropBreeder");

    private final List<RecipeLookupValidationTarget> targets;
    private final List<String> duplicateIssues = new ArrayList<>();
    private final Set<String> reportedDuplicateKeys = new HashSet<>();
    private int skippedDisabledRecipes;
    private int skippedFakeRecipes;
    private int skippedNoInputRecipes;

    private RecipeDuplicateValidator(List<RecipeLookupValidationTarget> targets) {
        this.targets = targets;
    }

    public static boolean shouldValidateDuplicates() {
        return Boolean.getBoolean(VALIDATE_DUPLICATES_PROPERTY);
    }

    public static void validateDuplicates() {
        validateDuplicates(RecipeMap.ALL_RECIPE_MAPS.values());
    }

    public static void validateDuplicates(String mapName, RecipeMapBackend backend) {
        validateDuplicates(Collections.singletonList(new RecipeLookupValidationTarget(mapName, backend)));
    }

    static void validateDuplicates(Collection<? extends RecipeMap<?>> recipeMaps) {
        List<RecipeLookupValidationTarget> targets = new ArrayList<>();
        for (RecipeMap<?> recipeMap : recipeMaps) {
            RecipeMapBackend backend = recipeMap.getBackend();
            String mapName = recipeMapName(recipeMap);
            if (backend.doesOverwriteFindRecipe() || shouldSkipMap(mapName)) {
                continue;
            }
            targets.add(new RecipeLookupValidationTarget(mapName, backend));
        }
        validateDuplicates(targets);
    }

    static void validateDuplicates(List<RecipeLookupValidationTarget> targets) {
        List<RecipeLookupValidationTarget> filteredTargets = targets.stream()
            .filter(target -> !target.backend.doesOverwriteFindRecipe() && !shouldSkipMap(target.mapName))
            .collect(Collectors.toList());
        new RecipeDuplicateValidator(filteredTargets).validate();
    }

    private static String recipeMapName(RecipeMap<?> recipeMap) {
        return recipeMap == null ? "<unbound>" : recipeMap.unlocalizedName;
    }

    static boolean shouldSkipMap(String mapName) {
        return mapName != null && SKIPPED_MAP_NAMES.contains(mapName);
    }

    private void validate() {
        for (RecipeLookupValidationTarget target : targets) {
            RecipeMapBackend backend = target.backend;
            List<GTRecipe> recipes = new ArrayList<>(backend.allRecipes());
            for (GTRecipe recipe : recipes) {
                if (!shouldValidateRecipe(recipe)) {
                    continue;
                }
                List<GTRecipe> duplicates = duplicateMatches(target, recipe);
                if (!duplicates.isEmpty()) {
                    addDuplicateIssue(target, recipe, duplicates);
                }
            }
        }

        if (!duplicateIssues.isEmpty()) {
            throw buildException();
        }
    }

    private boolean shouldValidateRecipe(GTRecipe recipe) {
        if (!recipe.mEnabled) {
            skippedDisabledRecipes++;
            return false;
        }
        if (recipe.mFakeRecipe) {
            skippedFakeRecipes++;
            return false;
        }
        if (!hasInputs(recipe)) {
            skippedNoInputRecipes++;
            return false;
        }
        return true;
    }

    private boolean hasInputs(GTRecipe recipe) {
        if (recipe.mInputs != null) {
            for (ItemStack item : recipe.mInputs) {
                if (item != null) {
                    return true;
                }
            }
        }
        if (recipe.mFluidInputs != null) {
            for (FluidStack fluid : recipe.mFluidInputs) {
                if (fluid != null && fluid.getFluid() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<GTRecipe> duplicateMatches(RecipeLookupValidationTarget target, GTRecipe queryRecipe) {
        RecipeMapBackend backend = target.backend;
        List<GTRecipe> matches = backend
            .matchRecipeStream(queryRecipe.mInputs, queryRecipe.mFluidInputs, null, null, false, true, true)
            .filter(candidate -> candidate != queryRecipe)
            .collect(Collectors.toList());
        if ("gtpp.recipe.quantumforcesmelter".equals(target.mapName)) {
            return filterQftCatalystMatches(queryRecipe, matches);
        }
        return matches;
    }

    private static List<GTRecipe> filterQftCatalystMatches(GTRecipe queryRecipe, List<GTRecipe> matches) {
        ItemStack queryCatalyst = queryRecipe.getMetadata(GTRecipeConstants.QFT_CATALYST);
        if (queryCatalyst == null) {
            return matches;
        }
        return matches.stream()
            .filter(candidate -> sameQftCatalyst(queryCatalyst, candidate))
            .collect(Collectors.toList());
    }

    private static boolean sameQftCatalyst(ItemStack queryCatalyst, GTRecipe candidate) {
        ItemStack candidateCatalyst = candidate.getMetadata(GTRecipeConstants.QFT_CATALYST);
        return GTUtility.areStacksEqual(queryCatalyst, candidateCatalyst, false);
    }

    private void addDuplicateIssue(RecipeLookupValidationTarget target, GTRecipe queryRecipe,
        List<GTRecipe> duplicates) {
        if (!shouldReportDuplicateGroup(queryRecipe, duplicates)) {
            return;
        }
        List<GTRecipe> duplicateGroup = new ArrayList<>(duplicates);
        duplicateGroup.add(queryRecipe);
        String duplicateKey = duplicateKey(target.mapName, duplicateGroup);
        if (!reportedDuplicateKeys.add(duplicateKey)) {
            return;
        }

        StringBuilder issue = new StringBuilder();
        issue.append("map=")
            .append(target.mapName)
            .append('\n')
            .append("queryRecipe=")
            .append(RecipeLookupValidator.describeRecipeForValidation(queryRecipe));
        issue.append('\n')
            .append("duplicateMatches=")
            .append(RecipeLookupValidator.describeRecipeListForValidation(duplicates));
        issue.append('\n')
            .append("duplicateMatchCount=")
            .append(duplicates.size());
        duplicateIssues.add(issue.toString());
    }

    private static boolean shouldReportDuplicateGroup(GTRecipe queryRecipe, List<GTRecipe> duplicates) {
        List<GTRecipe> group = new ArrayList<>(duplicates.size() + 1);
        group.add(queryRecipe);
        group.addAll(duplicates);
        if (group.stream()
            .anyMatch(RecipeDuplicateValidator::hasNonGregTechOwner)) {
            return false;
        }
        if (hasParallelGregTechRegistrationPath(group)) {
            return false;
        }
        if (group.stream()
            .anyMatch(RecipeDuplicateValidator::involvesOreAutogenRegistration)) {
            return true;
        }
        return group.stream()
            .anyMatch(RecipeDuplicateValidator::hasUnknownRegistrationCallsite);
    }

    private static boolean hasParallelGregTechRegistrationPath(List<GTRecipe> group) {
        boolean oreAutogen = group.stream()
            .anyMatch(RecipeDuplicateValidator::involvesOreAutogenRegistration);
        boolean otherGregtechPath = group.stream()
            .anyMatch(recipe -> {
                if (involvesOreAutogenRegistration(recipe)) {
                    return false;
                }
                String callsite = RecipeLookupValidator.registrationCallsite(recipe);
                return !"<disabled>".equals(callsite) && !"[]".equals(callsite) && callsite.contains("gregtech.");
            });
        return oreAutogen && otherGregtechPath;
    }

    private static boolean hasUnknownRegistrationCallsite(GTRecipe recipe) {
        String callsite = RecipeLookupValidator.registrationCallsite(recipe);
        return "<disabled>".equals(callsite) || "[]".equals(callsite);
    }

    private static boolean hasNonGregTechOwner(GTRecipe recipe) {
        if (recipe.owners == null || recipe.owners.isEmpty()) {
            return false;
        }
        for (ModContainer owner : recipe.owners) {
            if (owner != null && !"gregtech".equals(owner.getModId())) {
                return true;
            }
        }
        return false;
    }

    private static boolean involvesOreAutogenRegistration(GTRecipe recipe) {
        String callsite = RecipeLookupValidator.registrationCallsite(recipe);
        return callsite.contains("oreprocessing.") || callsite.contains("activateOreDictHandler")
            || callsite.contains("processOre(");
    }

    private String duplicateKey(String mapName, List<GTRecipe> recipes) {
        List<Integer> identities = recipes.stream()
            .map(System::identityHashCode)
            .sorted()
            .collect(Collectors.toList());
        return mapName + ":" + identities;
    }

    private IllegalStateException buildException() {
        StringBuilder message = new StringBuilder();
        message.append("GT recipe duplicate validation found ")
            .append(duplicateIssues.size())
            .append(" duplicate(s) across ")
            .append(targets.size())
            .append(" map(s).")
            .append("\nskipped recipe(s): disabled=")
            .append(skippedDisabledRecipes)
            .append(", fake=")
            .append(skippedFakeRecipes)
            .append(", noInputs=")
            .append(skippedNoInputRecipes);
        for (int i = 0; i < duplicateIssues.size(); i++) {
            message.append("\n\n")
                .append(i + 1)
                .append(") ")
                .append(duplicateIssues.get(i));
        }
        return new IllegalStateException(message.toString());
    }
}
