package gregtech.api.recipe.maps;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Element;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.items.behaviors.Behaviour_DataOrb;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ReplicatorBackend extends RecipeMapBackend {

    public ReplicatorBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder.recipeEmitter(ReplicatorBackend::replicatorRecipeEmitter));
    }

    @Override
    protected boolean doesOverwriteFindRecipe() {
        return true;
    }

    @Override
    protected GT_Recipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GT_Recipe cachedRecipe) {
        if (specialSlot == null) {
            return null;
        }
        Materials foundMaterial = getMaterialFromDataOrb(specialSlot);
        if (foundMaterial == null) {
            return null;
        }
        if (cachedRecipe != null) {
            Materials cachedRecipeMaterial = cachedRecipe.getMetadata(GT_RecipeConstants.MATERIAL);
            if (foundMaterial == cachedRecipeMaterial) {
                return cachedRecipe;
            }
        }
        for (GT_Recipe recipe : getAllRecipes()) {
            Materials recipeMaterial = recipe.getMetadata(GT_RecipeConstants.MATERIAL);
            if (foundMaterial == recipeMaterial) {
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    private static Materials getMaterialFromDataOrb(ItemStack stack) {
        if (ItemList.Tool_DataOrb.isStackEqual(stack, false, true) && Behaviour_DataOrb.getDataTitle(stack)
            .equals("Elemental-Scan")) {
            return Element.get(Behaviour_DataOrb.getDataName(stack)).mLinkedMaterials.stream()
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    private static Collection<GT_Recipe> replicatorRecipeEmitter(GT_RecipeBuilder builder) {
        Materials material = builder.getMetadata(GT_RecipeConstants.MATERIAL);
        if (material == null) {
            throw new IllegalStateException("GT_RecipeConstants.MATERIAL must be set for replicator recipe");
        }
        return Optional.of(material)
            .map(material1 -> material1.mElement)
            .map(Element::getMass)
            .map(ReplicatorBackend::getUUMAmountFromMass)
            .flatMap(
                uum -> builder.fluidInputs(Materials.UUMatter.getFluid(uum))
                    .duration(GT_Utility.safeInt(uum * 512L, 1))
                    .eut(TierEU.RECIPE_LV)
                    .ignoreCollision()
                    .noOptimize()
                    .build())
            .map(Collections::singletonList)
            .orElse(Collections.emptyList());
    }

    private static final double EXPONENT = GregTech_API.sOPStuff.get("Replicator", "Nerf Exponent", 1.2D);

    private static int getUUMAmountFromMass(long mass) {
        return GT_Utility.safeInt((long) Math.pow(mass, EXPONENT), 1);
    }
}
