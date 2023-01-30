package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class FormingPressRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 1),
                100,
                120);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 1),
                100,
                120);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 2),
                200,
                120);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1L),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 3),
                100,
                480);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 2L, 4),
                200,
                120);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherQuartz, 1L),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 5),
                300,
                120);
        GT_Values.RA.addFormingPressRecipe(
                new ItemStack(Items.comparator, 1, 32767),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0),
                getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 6),
                300,
                120);
        GT_Values.RA.addFormingPressRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 10),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 13),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 16),
                200,
                16);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CertusQuartz, 1L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 13),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 16),
                200,
                16);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 14),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 17),
                200,
                16);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 15),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 18),
                200,
                16);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SiliconSG, 1L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 19),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 20),
                200,
                16);

        GT_Values.RA.addFormingPressRecipe(
                ItemList.Food_Dough_Sugar.get(4L),
                ItemList.Shape_Mold_Cylinder.get(0L),
                ItemList.Food_Raw_Cake.get(1L),
                384,
                4);
        GT_Values.RA.addFormingPressRecipe(
                new ItemStack(Blocks.glass, 1, 32767),
                ItemList.Shape_Mold_Arrow.get(0L),
                ItemList.Arrow_Head_Glass_Emtpy.get(1L),
                64,
                4);

        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Cupronickel, 1L),
                ItemList.Shape_Mold_Credit.get(0L),
                ItemList.Credit_Greg_Cupronickel.get(4L),
                100,
                16);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Brass, 1L),
                ItemList.Shape_Mold_Credit.get(0L),
                ItemList.Coin_Doge.get(4L),
                100,
                16);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                ItemList.Shape_Mold_Credit.get(0L),
                ItemList.Credit_Iron.get(4L),
                100,
                16);
        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                ItemList.Shape_Mold_Credit.get(0L),
                ItemList.Credit_Iron.get(4L),
                100,
                16);

        GT_Values.RA.addFormingPressRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brick, 1L),
                ItemList.Shape_Mold_Ingot.get(0L),
                new ItemStack(Items.brick, 1, 0),
                100,
                16);
    }
}
