package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeBuilder.WILDCARD;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;

import java.util.Locale;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingDye implements IOreRecipeRegistrator {

    public ProcessingDye() {
        OrePrefixes.dye.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        Dyes aDye = Dyes.get(oreDictName);
        if ((aDye.mIndex >= 0) && (aDye.mIndex < 16) && (GT_Utility.getContainerItem(stack, true) == null)) {
            registerAlloySmelter(stack, aDye);
            registerMixer(stack, aDye);
            registerChemicalReactor(stack, aDye);
        }
    }

    public void registerMixer(ItemStack stack, Dyes dye) {
        String fluidName = "dye.watermixed." + dye.name()
            .toLowerCase(Locale.ENGLISH);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1, stack), GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Water.getFluid(216L))
            .fluidOutputs(FluidRegistry.getFluidStack(fluidName, 192))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1, stack), GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(GT_ModHandler.getDistilledWater(288L))
            .fluidOutputs(FluidRegistry.getFluidStack(fluidName, 216))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(mixerRecipes);
    }

    public void registerAlloySmelter(ItemStack stack, Dyes dye) {
        RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 8L),
                GT_Utility.copyAmount(1, stack))
            .itemOutputs(new ItemStack(Blocks.stained_glass, 8, 15 - dye.mIndex))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(alloySmelterRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.glass, 8, WILDCARD), GT_Utility.copyAmount(1, stack))
            .itemOutputs(new ItemStack(Blocks.stained_glass, 8, 15 - dye.mIndex))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(alloySmelterRecipes);
    }

    public void registerChemicalReactor(ItemStack stack, Dyes dye) {
        String fluidName = "dye.chemical." + dye.name()
            .toLowerCase(Locale.ENGLISH);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1, stack), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 2))
            .fluidInputs(Materials.SulfuricAcid.getFluid(432))
            .fluidOutputs(FluidRegistry.getFluidStack(fluidName, 288))
            .duration(30 * SECONDS)
            .eut(48)
            .addTo(UniversalChemical);
    }
}
