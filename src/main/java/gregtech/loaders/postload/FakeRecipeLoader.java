package gregtech.loaders.postload;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class FakeRecipeLoader implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("reactorLithiumCell", 1))
            .itemOutputs(GTModHandler.getIC2Item("TritiumCell", 1))
            .setNEIDesc(
                GTUtility.breakLines(
                    StatCollector.translateToLocal("GT5U.nei.nuclear.breeder.neutron_reflecting"),
                    StatCollector.translateToLocalFormatted("GT5U.nei.nuclear.breeder.reactor_hull_heat", 3_000, 1),
                    StatCollector.translateToLocalFormatted("GT5U.nei.nuclear.breeder.required_pulse", 10_000)))
            .duration(0)
            .eut(0)
            .addTo(RecipeMaps.ic2NuclearFakeRecipes);
    }
}
