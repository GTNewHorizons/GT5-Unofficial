package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.cokeOvenRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

@SuppressWarnings("PointlessArithmeticExpression")
public class CokeOvenRecipes implements Runnable {

    @Override
    public void run() {
        if (!Mods.Railcraft.isModLoaded()) return;

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1))
            .itemOutputs(RailcraftToolItems.getCoalCoke(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (500)))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(0)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1))
            .itemOutputs(EnumCube.COKE_BLOCK.getItem(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (4_500)))
            .duration(13 * MINUTES + 30 * SECONDS)
            .eut(0)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.cactus))
            .itemOutputs(GregtechItemList.CactusCharcoal.get(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (30)))
            .duration(25 * SECONDS)
            .eut(0)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CactusCharcoal.get(1))
            .itemOutputs(GregtechItemList.CactusCoke.get(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (30)))
            .duration(25 * SECONDS)
            .eut(0)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.reeds))
            .itemOutputs(GregtechItemList.SugarCharcoal.get(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (30)))
            .duration(25 * SECONDS)
            .eut(0)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.SugarCharcoal.get(1))
            .itemOutputs(GregtechItemList.SugarCoke.get(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (30)))
            .duration(25 * SECONDS)
            .eut(0)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.WoodPellet.get(2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 3))
            .duration(1 * MINUTES)
            .eut(0)
            .addTo(cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("logWood", 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (250)))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(0)
            .addTo(cokeOvenRecipes);
    }
}
