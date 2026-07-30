package gregtech.api.recipe.maps;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.GTMod;
import gregtech.api.enums.Element;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.items.behaviors.BehaviourDataOrb;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ReplicatorBackend extends RecipeMapBackend {

    private final Map<Element, GTRecipe> recipesByElement = new HashMap<>();

    public ReplicatorBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder.recipeEmitter(ReplicatorBackend::replicatorRecipeEmitter));
    }

    @Override
    public GTRecipe compileRecipe(GTRecipe recipe) {
        super.compileRecipe(recipe);
        addRecipeToElementIndex(recipe);
        return recipe;
    }

    @Override
    public void removeRecipes(Collection<? extends GTRecipe> recipesToRemove) {
        super.removeRecipes(recipesToRemove);
        rebuildElementIndex();
    }

    @Override
    public void clearRecipes() {
        super.clearRecipes();
        recipesByElement.clear();
    }

    @Override
    public void reInit() {
        super.reInit();
        rebuildElementIndex();
    }

    private void rebuildElementIndex() {
        recipesByElement.clear();
        for (GTRecipe recipe : getAllRecipes()) {
            addRecipeToElementIndex(recipe);
        }
    }

    private void addRecipeToElementIndex(GTRecipe recipe) {
        Material material = recipe.getMetadata(GTRecipeConstants.MATERIAL);
        assert material != null; // checked by replicatorRecipeEmitter
        Element element = MaterialUtils.element(material);
        if (element != null) {
            recipesByElement.put(element, recipe);
        }
    }

    @Override
    public boolean doesOverwriteFindRecipe() {
        return true;
    }

    @Override
    protected GTRecipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GTRecipe cachedRecipe) {
        if (specialSlot == null) {
            return null;
        }
        Element foundElement = getElementFromDataOrb(specialSlot);
        if (foundElement == null) {
            return null;
        }
        GTRecipe recipeFound = recipesByElement.get(foundElement);
        if (recipeFound == null) {
            return null;
        }
        return recipeFound.maxParallelCalculatedByInputs(1, fluids, items) < 1 ? null : recipeFound;
    }

    @Nullable
    private static Element getElementFromDataOrb(ItemStack stack) {
        if (ItemList.Tool_DataOrb.isStackEqual(stack, false, true) && BehaviourDataOrb.getDataTitle(stack)
            .equals("Elemental-Scan")) {
            return Element.get(BehaviourDataOrb.getDataName(stack));
        }
        return null;
    }

    private static Collection<GTRecipe> replicatorRecipeEmitter(GTRecipeBuilder builder) {
        Material material = builder.getMetadata(GTRecipeConstants.MATERIAL);
        if (material == null) {
            throw new IllegalStateException("GTRecipeConstants.MATERIAL must be set for replicator recipe");
        }
        return Optional.of(material)
            .map(MaterialUtils::element)
            .map(Element::getMass)
            .map(ReplicatorBackend::getUUMAmountFromMass)
            .flatMap(
                uum -> builder.fluidInputs(MaterialUtils.fluid(Materials.UUMatter, uum))
                    .duration(GTUtility.safeInt(uum * 512L, 1))
                    .eut(TierEU.RECIPE_LV)
                    .ignoreCollision()
                    .build())
            .map(Collections::singletonList)
            .orElse(Collections.emptyList());
    }

    private static int getUUMAmountFromMass(long mass) {
        return GTUtility.safeInt((long) Math.pow(mass, GTMod.proxy.replicatorExponent), 1);
    }
}
