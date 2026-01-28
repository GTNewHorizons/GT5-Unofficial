package gregtech.loaders.postload;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gtneioreplugin.plugin.block.ModBlocks;

public class BiodomeCalibrationLoader {

    public static void load() {
        // ECB
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
            .duration(1)
            .eut(1)
            .fake()
            .addTo(RecipeMaps.biodomeFakeCalibrationRecipes);
    }
}
