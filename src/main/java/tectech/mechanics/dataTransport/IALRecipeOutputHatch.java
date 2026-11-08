package tectech.mechanics.dataTransport;

/// Implemented on hatches that transmit AL recipes from a databack into data pipes or the wireless network.
public interface IALRecipeOutputHatch {

    ALRecipeDataPacket getAssemblyLineRecipes();

    void setAssemblyLineRecipes(ALRecipeDataPacket packet);

    boolean isValid();
}
