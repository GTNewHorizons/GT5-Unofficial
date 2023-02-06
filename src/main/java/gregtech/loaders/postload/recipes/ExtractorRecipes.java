package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.loaders.postload.GT_MachineRecipeLoader.isEBXLLoaded;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class ExtractorRecipes implements Runnable {

    @Override
    public void run() {
        GT_ModHandler.addExtractionRecipe(new ItemStack(Blocks.bookshelf, 1, 32767), new ItemStack(Items.book, 3, 0));
        GT_ModHandler.addExtractionRecipe(
                new ItemStack(Items.slime_ball, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 2L));
        GT_ModHandler.addExtractionRecipe(
                ItemList.IC2_Resin.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L));
        GT_ModHandler.addExtractionRecipe(
                GT_ModHandler.getIC2Item("rubberSapling", 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        GT_ModHandler.addExtractionRecipe(
                GT_ModHandler.getIC2Item("rubberLeaves", 16L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Cell_Air.get(1L), ItemList.Cell_Empty.get(1L));
        if (isEBXLLoaded) {
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "waterplant1", 1, 0),
                    new ItemStack(Items.dye, 4, 2));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "vines", 1, 0),
                    new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 11),
                    new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 10),
                    new ItemStack(Items.dye, 4, 5));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 9),
                    new ItemStack(Items.dye, 4, 14));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 8),
                    new ItemStack(Items.dye, 4, 14));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 7),
                    new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 6),
                    new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 5),
                    new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 0),
                    new ItemStack(Items.dye, 4, 9));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 4),
                    new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 3),
                    new ItemStack(Items.dye, 4, 13));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 3),
                    new ItemStack(Items.dye, 4, 5));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 2),
                    new ItemStack(Items.dye, 4, 5));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 1),
                    new ItemStack(Items.dye, 4, 12));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 15),
                    new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 14),
                    new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 13),
                    new ItemStack(Items.dye, 4, 9));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 12),
                    new ItemStack(Items.dye, 4, 14));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 11),
                    new ItemStack(Items.dye, 4, 7));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 7),
                    new ItemStack(Items.dye, 4, 7));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 2),
                    new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 13),
                    new ItemStack(Items.dye, 4, 6));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 6),
                    new ItemStack(Items.dye, 4, 12));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 5),
                    new ItemStack(Items.dye, 4, 10));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 2),
                    new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 1),
                    new ItemStack(Items.dye, 4, 9));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 0),
                    new ItemStack(Items.dye, 4, 13));

            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 7),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 0));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 1),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 12),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 4),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 6),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 2));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 8),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 3));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 3),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 3));
        }
    }
}
