package gtPlusPlus.core.recipe;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;

import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;

public class RECIPES_SeleniumProcessing {

    public static void init() {

        // We need this
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(MISC_MATERIALS.SELENIUM_DIOXIDE, false);

        // Makes Selenium Dioxide
        processCopperRecipes();

        // Liquify the Dried Dioxide
        AddGregtechRecipe.addCokeAndPyrolyseRecipes(
            MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
            13,
            FluidUtils.getSteam(500),
            null,
            MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(1000),
            20,
            1024);

        // Produce Selenious Acid
        AddGregtechRecipe.addCokeAndPyrolyseRecipes(
            MISC_MATERIALS.SELENIUM_DIOXIDE.getCell(1),
            14,
            FluidUtils.getHotWater(4000),
            CI.emptyCells(1),
            MISC_MATERIALS.SELENIOUS_ACID.getFluidStack(1000),
            20,
            2048);

        // Make Selenium
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(14), ELEMENT.getInstance().CARBON.getDust(16))
            .itemOutputs( ELEMENT.getInstance().SELENIUM.getIngot(1), ELEMENT.getInstance().SELENIUM.getIngot(1))
            .outputChances(100_00, 20_00, 20_00)
            .fluidInputs( MISC_MATERIALS.SELENIOUS_ACID.getFluidStack(750),
                Materials.SulfuricAcid.getFluid(8000))
            .fluidOutputs(ELEMENT.getInstance().SELENIUM.getFluidStack(144 * 1))
            .eut(7200)
            .duration(5* MINUTES)
            .specialValue(3700)
            .addTo(alloyBlastSmelterRecipes);
    }

    public static void processCopperRecipes() {

        // Copper
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Copper, 1), // Item Input
            },
            FluidUtils.getHotWater(1000), // Fluid
            MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(20), // Fluid
            new ItemStack[] { ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Copper, 1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1), }, // Output
            new int[] { 10000, 100, 100, 500, 500, 500, 1000, 1000, 1000 },
            40 * 20, // Time in ticks
            1024); // EU

        // Tetra
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Tetrahedrite, 1), // Item
                                                                                                     // Input
            },
            FluidUtils.getHotWater(1000), // Fluid
            MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(10), // Fluid
            new ItemStack[] { ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Tetrahedrite, 1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1), }, // Output
            new int[] { 10000, 100, 100, 300, 300, 300, 800, 800, 800 },
            40 * 20, // Time in ticks
            1024); // EU

        // Chalco
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 1), // Item
                                                                                                     // Input
            },
            FluidUtils.getHotWater(1000), // Fluid
            MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(10), // Fluid
            new ItemStack[] { ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Chalcopyrite, 1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1), }, // Output
            new int[] { 10000, 100, 100, 300, 300, 300, 800, 800, 800 },
            40 * 20, // Time in ticks
            1024); // EU

        // Malachite
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Malachite, 1), // Item Input
            },
            FluidUtils.getHotWater(1000), // Fluid
            MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(10), // Fluid
            new ItemStack[] { ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Malachite, 1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1), MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1), }, // Output
            new int[] { 10000, 100, 100, 300, 300, 300, 800, 800, 800 },
            40 * 20, // Time in ticks
            1024); // EU
    }
}
