package gtPlusPlus.xmod.gregtech.loaders.recipe;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.TierEU;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.ELEMENT;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.FUSION_THRESHOLD;

public class RecipeLoader_GTNH {

    public static void generate() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Shape_Mold_Ball.get(0L)
            )
            .itemOutputs(
                new ItemStack(Items.ender_pearl, 1, 0)
            )
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("ender"), 250)
            )
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        // MK4
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.Plutonium241.getMolten(144),
                Materials.Helium.getGas(1000)
            )
            .fluidOutputs(ELEMENT.getInstance().CURIUM.getFluidStack(144))
            .duration(4*SECONDS+16*TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD,500_000_000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(ELEMENT.getInstance().CURIUM.getFluidStack(144),
                Materials.Helium.getPlasma(144))
            .fluidOutputs(ELEMENT.getInstance().CALIFORNIUM.getFluidStack(144))
            .duration(6*SECONDS+8*TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD,750_000_000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.Plutonium241.getMolten(144),
                Materials.Calcium.getPlasma(144)
            )
            .fluidOutputs(
                Materials.Flerovium.getMolten(144)
            )
            .duration(8*SECONDS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD,1_000_000_000)
            .addTo(fusionRecipes);
    }
}
