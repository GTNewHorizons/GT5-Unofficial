package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.loaders.materials.LegacyNameDomain;

public class ProcessingLog implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingLog() {
        OrePrefixes.log.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (!LegacyNameDomain.contains(material)) return;

        short meta = (short) stack.getItemDamage();

        if (meta == Short.MAX_VALUE) {
            List<IRecipe> recipeCandidates = GTModHandler.getRecipeCandidates(stack);
            if ((GTUtility.areStacksEqual(
                GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1, stack), false, null),
                new ItemStack(Items.coal, 1, 1)))) {
                GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1, stack));
            }
            for (int i = 0; i < 32767; i++) {
                if ((GTUtility.areStacksEqual(
                    GTModHandler.getSmeltingOutput(new ItemStack(stack.getItem(), 1, i), false, null),
                    new ItemStack(Items.coal, 1, 1)))) {
                    GTModHandler.removeFurnaceSmelting(new ItemStack(stack.getItem(), 1, i));
                }
                ItemStack tStack = GTModHandler
                    .getRecipeOutputFrom(recipeCandidates, new ItemStack(stack.getItem(), 1, i));
                if (tStack == null) {
                    if (i >= 16) {
                        break;
                    }
                } else {
                    ItemStack tPlanks = GTUtility.copyOrNull(tStack);
                    if (tPlanks != null) {
                        tPlanks.stackSize = (tPlanks.stackSize * 3 / 2);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(stack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyOrNull(tPlanks),
                                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, (int) (1)))
                            .fluidInputs(MaterialUtils.fluid(Materials.dimensionallyshiftedsuperfluid, 1L))
                            .duration(4 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(stack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyOrNull(tPlanks),
                                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, (int) (1)))
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.Lubricant,
                                    Materials2FluidShapes.fluidLiquid,
                                    (int) (1)))
                            .duration(10 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(stack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, (int) (2)))
                            .fluidInputs(GTUtility.getWater(Math.min(1_000, 200 * 8 / 320)))
                            .duration(20 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(stack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, (int) (2)))
                            .fluidInputs(GTModHandler.getDistilledWater(3))
                            .duration(20 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cutterRecipes);
                        GTModHandler.removeRecipeDelayed(new ItemStack(stack.getItem(), 1, i));
                        GTModHandler.addCraftingRecipe(
                            GTUtility.copyAmount(
                                GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
                            new Object[] { "s", "L", 'L', new ItemStack(stack.getItem(), 1, i) });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(tStack.stackSize / (GTMod.proxy.mNerfedWoodPlank ? 2 : 1), tStack),
                            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
                            new Object[] { new ItemStack(stack.getItem(), 1, i) });
                    }
                }
            }
        } else {
            if ((GTUtility.areStacksEqual(
                GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1, stack), false, null),
                new ItemStack(Items.coal, 1, 1)))) {
                GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1, stack));
            }
            ItemStack tStack = GTModHandler.getRecipeOutput(GTUtility.copyAmount(1, stack));
            if (tStack != null) {
                ItemStack tPlanks = GTUtility.copyOrNull(tStack);
                if (tPlanks != null) {
                    tPlanks.stackSize = (tPlanks.stackSize * 3 / 2);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(
                            GTUtility.copyOrNull(tPlanks),
                            MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, (int) (1)))
                        .fluidInputs(MaterialUtils.fluid(Materials.dimensionallyshiftedsuperfluid, 1L))
                        .duration(4 * SECONDS)
                        .eut(TierEU.RECIPE_ULV)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(
                            GTUtility.copyOrNull(tPlanks),
                            MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, (int) (1)))
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials.Lubricant,
                                Materials2FluidShapes.fluidLiquid,
                                (int) (1)))
                        .duration(10 * SECONDS)
                        .eut(TierEU.RECIPE_ULV)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, (int) (2)))
                        .fluidInputs(GTUtility.getWater(Math.min(1_000, 200 * 8 / 320)))
                        .duration(20 * SECONDS)
                        .eut(TierEU.RECIPE_ULV)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, (int) (2)))
                        .fluidInputs(GTModHandler.getDistilledWater(3))
                        .duration(20 * SECONDS)
                        .eut(TierEU.RECIPE_ULV)
                        .addTo(cutterRecipes);
                    GTModHandler.removeRecipeDelayed(GTUtility.copyAmount(1, stack));
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(
                            GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                            tStack),
                        GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
                        new Object[] { "s", "L", 'L', GTUtility.copyAmount(1, stack) });
                    GTModHandler.addShapelessCraftingRecipe(
                        GTUtility.copyAmount(tStack.stackSize / (GTMod.proxy.mNerfedWoodPlank ? 2 : 1), tStack),
                        new Object[] { GTUtility.copyAmount(1, stack) });
                }
            }
        }

        if ((GTUtility.areStacksEqual(
            GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1, stack), false, null),
            new ItemStack(Items.coal, 1, 1)))) {
            GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1, stack));
        }
    }
}
