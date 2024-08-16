package gregtech.api.recipe.metadata;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleSpeed;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NanochipAssemblyModuleSpeedKey extends RecipeMetadataKey<ModuleSpeed> {

    public static final NanochipAssemblyModuleSpeedKey INSTANCE = new NanochipAssemblyModuleSpeedKey();

    private NanochipAssemblyModuleSpeedKey() {
        super(ModuleSpeed.class, "nanochip_assembly_module_speed");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        // TODO: Implement
    }
}
