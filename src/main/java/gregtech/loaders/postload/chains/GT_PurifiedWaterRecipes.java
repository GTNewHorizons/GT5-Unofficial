package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationPlantGrade1Recipes;
import static gregtech.api.recipe.RecipeMaps.purificationPlantGrade2Recipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.metadata.PurificationPlantBaseChanceKey;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationPlant;

public class GT_PurifiedWaterRecipes {

    static final PurificationPlantBaseChanceKey BASE_CHANCE = PurificationPlantBaseChanceKey.INSTANCE;

    public static void run() {
        final int duration = GT_MetaTileEntity_PurificationPlant.CYCLE_TIME_TICKS;

        // Grade 1 - Sifter
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Component_Filter.get(1))
            .fluidInputs(GT_ModHandler.getDistilledWater(1000L))
            .fluidOutputs(Materials.Grade1PurifiedWater.getFluid(900L))
            .itemOutputs(new ItemStack(Items.stick, 1), Materials.Stone.getDust(1), Materials.Gold.getNuggets(1))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_LuV)
            .metadata(BASE_CHANCE, 70.0f)
            .addTo(purificationPlantGrade1Recipes);

        // Grade 2 - Coagulation
        for (int i = 1; i <= 10; ++i) {
            GT_Values.RA.stdBuilder()
                .fluidInputs(
                    Materials.Grade1PurifiedWater.getFluid(1000L),
                    Materials.IronIIIChloride.getFluid(i * 1000000))
                .fluidOutputs(
                    Materials.Grade2PurifiedWater.getFluid(900),
                    Materials.FerrousWastewater.getFluid(i * 10000000))
                .ignoreCollision()
                .itemOutputs(
                    new ItemStack(Items.clay_ball, 1),
                    Materials.QuartzSand.getDust(1),
                    Materials.PolyvinylChloride.getNuggets(1))
                .outputChances(1000, 500, 100)
                .duration(duration)
                .eut(TierEU.RECIPE_LuV)
                .metadata(BASE_CHANCE, i * 10.0f)
                .addTo(purificationPlantGrade2Recipes);
        }

        // Add recipe to reprocess ferrous waste water

        // This amount seems high, but it does perfectly cycle hydrochloric acid.
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.FerrousWastewater.getFluid(90000L))
            .itemOutputs(Materials.Iron.getDust(9))
            .fluidOutputs(Materials.Water.getFluid(36000L), Materials.DilutedHydrochloricAcid.getFluid(54000L))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);
    }
}
