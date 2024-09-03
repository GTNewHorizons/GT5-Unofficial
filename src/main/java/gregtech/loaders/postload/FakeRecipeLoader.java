package gregtech.loaders.postload;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;

public class FakeRecipeLoader implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("reactorLithiumCell", 1))
            .itemOutputs(GTModHandler.getIC2Item("TritiumCell", 1))
            .setNEIDesc(
                "Neutron reflecting Breeder",
                String.format("Every %d reactor hull heat", 3_000),
                String.format("increase speed by %d00%%", 1),
                String.format("Required pulses: %d", 10_000))
            .duration(0)
            .eut(0)
            .addTo(RecipeMaps.ic2NuclearFakeRecipes);
    }
}
