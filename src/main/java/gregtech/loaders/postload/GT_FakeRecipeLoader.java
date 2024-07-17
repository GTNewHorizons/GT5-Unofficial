package gregtech.loaders.postload;

import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;

public class GT_FakeRecipeLoader implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getIC2Item("reactorLithiumCell", 1))
            .itemOutputs(GT_ModHandler.getIC2Item("TritiumCell", 1))
            .setNEIDesc(
                "Neutron reflecting Breeder",
                String.format("Every %d reactor hull heat",3_000),
                String.format("increase speed by %d00%%", 1),
                String.format("Required pulses: %d",10_000))
            .duration(0)
            .eut(0)
            .addTo(RecipeMaps.ic2NuclearFakeRecipes);
    }
}
