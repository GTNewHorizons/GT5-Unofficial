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

import gregtech.GTMod;
import gregtech.api.enums.Element;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
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

    private final Map<Materials, GTRecipe> recipesByMaterial = new HashMap<>();

    public ReplicatorBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder.recipeEmitter(ReplicatorBackend::replicatorRecipeEmitter));
    }

    @Override
    public GTRecipe compileRecipe(GTRecipe recipe) {
        super.compileRecipe(recipe);
        Materials material = recipe.getMetadata(GTRecipeConstants.MATERIAL);
        assert material != null; // checked by replicatorRecipeEmitter
        recipesByMaterial.put(material, recipe);
        return recipe;
    }

    @Override
    protected boolean doesOverwriteFindRecipe() {
        return true;
    }

    @Override
    protected GTRecipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GTRecipe cachedRecipe) {
        if (specialSlot == null) {
            return null;
        }
        Materials foundMaterial = getMaterialFromDataOrb(specialSlot);
        if (foundMaterial == null) {
            return null;
        }
        return recipesByMaterial.getOrDefault(foundMaterial, null);
    }

    @Nullable
    private static Materials getMaterialFromDataOrb(ItemStack stack) {
        if (ItemList.Tool_DataOrb.isStackEqual(stack, false, true) && BehaviourDataOrb.getDataTitle(stack)
            .equals("Elemental-Scan")) {
            return Element.get(BehaviourDataOrb.getDataName(stack)).mLinkedMaterials.stream()
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    private static Collection<GTRecipe> replicatorRecipeEmitter(GTRecipeBuilder builder) {
        Materials material = builder.getMetadata(GTRecipeConstants.MATERIAL);
        if (material == null) {
            throw new IllegalStateException("GTRecipeConstants.MATERIAL must be set for replicator recipe");
        }
        return Optional.of(material)
            .map(material1 -> material1.mElement)
            .map(Element::getMass)
            .map(ReplicatorBackend::getUUMAmountFromMass)
            .flatMap(
                uum -> builder.fluidInputs(Materials.UUMatter.getFluid(uum))
                    .duration(GTUtility.safeInt(uum * 512L, 1))
                    .eut(TierEU.RECIPE_LV)
                    .ignoreCollision()
                    .noOptimize()
                    .build())
            .map(Collections::singletonList)
            .orElse(Collections.emptyList());
    }

    private static int getUUMAmountFromMass(long mass) {
        return GTUtility.safeInt((long) Math.pow(mass, GTMod.gregtechproxy.replicatorExponent), 1);
    }
}
