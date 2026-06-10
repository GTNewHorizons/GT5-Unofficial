package gregtech.loaders.postload;

import static gregtech.api.util.GTRecipeConstants.BIODOME_DIMENSION_STRING;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gtneioreplugin.plugin.block.ModBlocks;

public class BiodomeCalibrationLoader {

    // Overworld
    public static void load() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ow"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "Overworld")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Nether
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                new ItemStack(Blocks.netherrack, 64000),
                new ItemStack(Blocks.soul_sand, 64000),
                new ItemStack(Items.quartz, 64000),
                new ItemStack(Blocks.nether_brick, 64000))
            // new ItemStack(Blocks.nether_brick, 64000),
            // new ItemStack(Blocks.nether_brick, 64000))
            .fluidInputs(
                new FluidStack(FluidRegistry.LAVA, 256000),
                Materials.NetherAir.getFluid(256000),
                Materials.SulfuricAcid.getFluid(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ne"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "Nether")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // End
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("ED"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "The End")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // End Asteroids
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("EA"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "EndAsteroid")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Twilight Forest
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("TF"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "Twilight Forest")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Everglades
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Eg"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "dimensionDarkWorld")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 1

        // Moon
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Mo"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "moon")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 2

        // Mars
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ma"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "mars")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Deimos
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("De"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "deimos")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Phobos
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ph"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "phobos")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 3

        // Asteroids
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("As"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "asteroids")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Callisto
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ca"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "callisto")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Ceres
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ce"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "ceres")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Europa
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Eu"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "europa")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Ganymede
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ga"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "ganymed")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Ross128b
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Rb"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "ross128b")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 4

        // Venus
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ve"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "venus")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Mercury
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Me"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "mercury")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Io
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Io"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "iojupiter")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 5

        // Enceladus
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("En"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "enceladus")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Miranda
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Mi"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "miranda")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Oberon
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ob"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "oberon")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Titan
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ti"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "titan")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Ross128ba
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ra"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "ross128ba")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 6

        // Proteus
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Pr"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "proteus")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Triton
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Tr"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "triton")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 7

        // Pluto
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Pl"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "pluto")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Makemake
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("MM"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "makemake")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Haumea
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ha"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "haumea")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Kuiper Belt
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("KB"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "kuiperbelt")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 8

        // Barnard C
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("BC"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "barnarda2")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Barnard E
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("BE"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "barnarda4")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Barnard F
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("BF"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "barnarda5")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Centauri A
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("CB"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "centauribb")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // T Ceti E
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("TE"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "tcetie")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Vega B
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("VB"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "vega1")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 9

        // Anubis
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("An"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "anubis")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Horus
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Ho"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "horus")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Maahes
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Mh"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "maahes")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Neper
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Np"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "neper")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Seth
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("Se"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "seth")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Mehen Belt
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("MB"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "asteroidbeltmehen")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);

        // Tier 10

        // Deep Dark
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.grass, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64),
                new ItemStack(Blocks.stone, 64))
            .fluidInputs(
                new FluidStack(FluidRegistry.WATER, 256000),
                Materials.Air.getGas(256000),
                Materials.Silicon.getMolten(256000))
            .itemOutputs(new ItemStack(ModBlocks.getBlock("DD"), 1))
            .metadata(BIODOME_DIMENSION_STRING, "Underdark")
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);
    }
}
