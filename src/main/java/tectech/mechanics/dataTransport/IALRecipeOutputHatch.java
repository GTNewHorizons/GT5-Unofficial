package tectech.mechanics.dataTransport;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

/// Implemented on hatches that transmit AL recipes from a databank into data pipes or the wireless network.
public interface IALRecipeOutputHatch extends IMetaTileEntity {

    ALRecipeDataPacket getAssemblyLineRecipes();

    void setAssemblyLineRecipes(ALRecipeDataPacket packet);

    void updateTexture(int id);

    boolean isValid();
}
