package tectech.mechanics.dataTransport;

import org.jetbrains.annotations.NotNull;

/// Implemented on hatches that inject AL recipes into their connected multi.
public interface IALRecipeInputHatch {

    @NotNull
    ALRecipeDataPacket getAssemblyLineRecipes();

    boolean isValid();

    void setActive(boolean active);

}
