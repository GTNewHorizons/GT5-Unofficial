package gregtech.api.recipe.metadata;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleRecipeInfo;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NanochipAssemblyRecipeInfo extends RecipeMetadataKey<ModuleRecipeInfo> {

    public static final NanochipAssemblyRecipeInfo INSTANCE = new NanochipAssemblyRecipeInfo();

    private NanochipAssemblyRecipeInfo() {
        super(ModuleRecipeInfo.class, "nanochip_assembly_module_recipe_info");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        // TODO: Implement
    }
}
