package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.purificationPlantGrade1Recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationPlant;

public class GT_PurifiedWaterRecipes {

    public static void run() {
        final int duration = GT_MetaTileEntity_PurificationPlant.CYCLE_TIME_TICKS;

        GT_Values.RA.stdBuilder()
            .fluidInputs(GT_ModHandler.getDistilledWater(1000L))
            .fluidOutputs(Materials.Grade1PurifiedWater.getFluid(900L))
            .itemOutputs(new ItemStack(Items.stick, 1), Materials.Stone.getDust(1), Materials.Gold.getNuggets(1))
            .outputChances(100, 50, 10)
            .duration(duration)
            .eut(TierEU.RECIPE_LuV)
            .addTo(purificationPlantGrade1Recipes);
    }
}
