package gregtech.loaders.postload.recipes;

import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

public class FermenterRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addFermentingRecipe(
                Materials.Biomass.getFluid(100),
                Materials.FermentedBiomass.getFluid(100),
                150,
                false);
        GT_Values.RA.addFermentingRecipe(
                new FluidStack(FluidRegistry.getFluid("ic2biomass"), 100),
                Materials.FermentedBiomass.getFluid(100),
                150,
                false);

        GT_Values.RA.addFermentingRecipe(getFluidStack("milk", 50), getFluidStack("potion.mundane", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.lemonjuice", 50),
                getFluidStack("potion.limoncello", 25),
                1024,
                true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.applejuice", 50),
                getFluidStack("potion.cider", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.goldenapplejuice", 50),
                getFluidStack("potion.goldencider", 25),
                1024,
                true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.idunsapplejuice", 50),
                getFluidStack("potion.notchesbrew", 25),
                1024,
                true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.reedwater", 50),
                getFluidStack("potion.rum", 25),
                1024,
                true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.rum", 50),
                getFluidStack("potion.piratebrew", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.grapejuice", 50),
                getFluidStack("potion.wine", 25),
                1024,
                false);
        GT_Values.RA
                .addFermentingRecipe(getFluidStack("potion.wine", 50), getFluidStack("potion.vinegar", 10), 2048, true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.wheatyjuice", 50),
                getFluidStack("potion.scotch", 25),
                1024,
                true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.scotch", 50),
                getFluidStack("potion.glenmckenner", 10),
                2048,
                true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.wheatyhopsjuice", 50),
                getFluidStack("potion.beer", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.hopsjuice", 50),
                getFluidStack("potion.darkbeer", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.darkbeer", 50),
                getFluidStack("potion.dragonblood", 10),
                2048,
                true);

        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.beer", 75),
                getFluidStack("potion.vinegar", 50),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.cider", 75),
                getFluidStack("potion.vinegar", 50),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.goldencider", 75),
                getFluidStack("potion.vinegar", 50),
                2048,
                true);
        GT_Values.RA
                .addFermentingRecipe(getFluidStack("potion.rum", 75), getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.wine", 75),
                getFluidStack("potion.vinegar", 50),
                2048,
                false);

        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.awkward", 50),
                getFluidStack("potion.weakness", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.mundane", 50),
                getFluidStack("potion.weakness", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.thick", 50),
                getFluidStack("potion.weakness", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.poison", 50),
                getFluidStack("potion.damage", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.health", 50),
                getFluidStack("potion.damage", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.waterbreathing", 50),
                getFluidStack("potion.damage", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.nightvision", 50),
                getFluidStack("potion.invisibility", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.fireresistance", 50),
                getFluidStack("potion.slowness", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.speed", 50),
                getFluidStack("potion.slowness", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.strength", 50),
                getFluidStack("potion.weakness", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.regen", 50),
                getFluidStack("potion.poison", 25),
                1024,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.poison.strong", 50),
                getFluidStack("potion.damage.strong", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.health.strong", 50),
                getFluidStack("potion.damage.strong", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.speed.strong", 50),
                getFluidStack("potion.slowness.strong", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.strength.strong", 50),
                getFluidStack("potion.weakness.strong", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.nightvision.long", 50),
                getFluidStack("potion.invisibility.long", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.regen.strong", 50),
                getFluidStack("potion.poison.strong", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.poison.long", 50),
                getFluidStack("potion.damage.long", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.waterbreathing.long", 50),
                getFluidStack("potion.damage.long", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.fireresistance.long", 50),
                getFluidStack("potion.slowness.long", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.speed.long", 50),
                getFluidStack("potion.slowness.long", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.strength.long", 50),
                getFluidStack("potion.weakness.long", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.regen.long", 50),
                getFluidStack("potion.poison.long", 10),
                2048,
                false);
    }
}
