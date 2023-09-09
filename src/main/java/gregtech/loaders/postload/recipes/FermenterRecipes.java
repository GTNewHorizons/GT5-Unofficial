package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFermentingRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

public class FermenterRecipes implements Runnable {

    @Override
    public void run() {

        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Biomass.getFluid(100))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(100))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 100))
            .fluidOutputs(Materials.FermentedBiomass.getFluid(100))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("milk", 50))
            .fluidOutputs(getFluidStack("potion.mundane", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.lemonjuice", 50))
            .fluidOutputs(getFluidStack("potion.limoncello", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.applejuice", 50))
            .fluidOutputs(getFluidStack("potion.cider", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.goldenapplejuice", 50))
            .fluidOutputs(getFluidStack("potion.goldencider", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.idunsapplejuice", 50))
            .fluidOutputs(getFluidStack("potion.notchesbrew", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.reedwater", 50))
            .fluidOutputs(getFluidStack("potion.rum", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.rum", 50))
            .fluidOutputs(getFluidStack("potion.piratebrew", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.grapejuice", 50))
            .fluidOutputs(getFluidStack("potion.wine", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.wine", 50))
            .fluidOutputs(getFluidStack("potion.vinegar", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.wheatyjuice", 50))
            .fluidOutputs(getFluidStack("potion.scotch", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.scotch", 50))
            .fluidOutputs(getFluidStack("potion.glenmckenner", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.wheatyhopsjuice", 50))
            .fluidOutputs(getFluidStack("potion.beer", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.hopsjuice", 50))
            .fluidOutputs(getFluidStack("potion.darkbeer", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.darkbeer", 50))
            .fluidOutputs(getFluidStack("potion.dragonblood", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.beer", 75))
            .fluidOutputs(getFluidStack("potion.vinegar", 50))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.cider", 75))
            .fluidOutputs(getFluidStack("potion.vinegar", 50))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.goldencider", 75))
            .fluidOutputs(getFluidStack("potion.vinegar", 50))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.rum", 75))
            .fluidOutputs(getFluidStack("potion.vinegar", 50))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.wine", 75))
            .fluidOutputs(getFluidStack("potion.vinegar", 50))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.awkward", 50))
            .fluidOutputs(getFluidStack("potion.weakness", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.mundane", 50))
            .fluidOutputs(getFluidStack("potion.weakness", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.thick", 50))
            .fluidOutputs(getFluidStack("potion.weakness", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.poison", 50))
            .fluidOutputs(getFluidStack("potion.damage", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.health", 50))
            .fluidOutputs(getFluidStack("potion.damage", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.waterbreathing", 50))
            .fluidOutputs(getFluidStack("potion.damage", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.nightvision", 50))
            .fluidOutputs(getFluidStack("potion.invisibility", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.fireresistance", 50))
            .fluidOutputs(getFluidStack("potion.slowness", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.speed", 50))
            .fluidOutputs(getFluidStack("potion.slowness", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.strength", 50))
            .fluidOutputs(getFluidStack("potion.weakness", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.regen", 50))
            .fluidOutputs(getFluidStack("potion.poison", 25))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.poison.strong", 50))
            .fluidOutputs(getFluidStack("potion.damage.strong", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.health.strong", 50))
            .fluidOutputs(getFluidStack("potion.damage.strong", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.nightvision.long", 50))
            .fluidOutputs(getFluidStack("potion.invisibility.long", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.regen.strong", 50))
            .fluidOutputs(getFluidStack("potion.poison.strong", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.fireresistance.long", 50))
            .fluidOutputs(getFluidStack("potion.slowness.long", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.speed.long", 50))
            .fluidOutputs(getFluidStack("potion.slowness.long", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.strength.long", 50))
            .fluidOutputs(getFluidStack("potion.weakness.long", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(getFluidStack("potion.regen.long", 50))
            .fluidOutputs(getFluidStack("potion.poison.long", 10))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(2)
            .addTo(sFermentingRecipes);

    }
}
