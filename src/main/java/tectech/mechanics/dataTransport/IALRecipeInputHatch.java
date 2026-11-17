package tectech.mechanics.dataTransport;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

/// Implemented on hatches that inject AL recipes into their connected multi.
public interface IALRecipeInputHatch extends IMetaTileEntity {

    @NotNull
    ALRecipeDataPacket getAssemblyLineRecipes();

    boolean isValid();

    void updateTexture(int id);

    void setActive(boolean active);
}
