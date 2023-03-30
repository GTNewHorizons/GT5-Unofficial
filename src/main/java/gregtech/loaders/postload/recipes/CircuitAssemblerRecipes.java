package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.loaders.postload.GT_MachineRecipeLoader.solderingMats;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class CircuitAssemblerRecipes implements Runnable {

    @Override
    public void run() {
        registerRailcraftRecipes();
        registerForestryRecipes();


    }

    public void registerRailcraftRecipes(){
        if (!Railcraft.isModLoaded()) {
            return;
        }

        for (Materials tMat : solderingMats) {
            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            // Railcraft Circuits
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Coated_Basic.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 1), ItemList.Cover_Controller.get(1L) },
                tMat.getMolten(144L * tMultiplier / 2L),
                getModItem(Railcraft.modID, "part.circuit", 4L, 0),
                300,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Coated_Basic.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 1), ItemList.Sensor_LV.get(1L) },
                tMat.getMolten(144L * tMultiplier / 2L),
                getModItem(Railcraft.modID, "part.circuit", 4L, 1),
                300,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Coated_Basic.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 1), getModItem(Railcraft.modID, "part.signal.lamp", 1L, 0) },
                tMat.getMolten(144L * tMultiplier / 2L),
                getModItem(Railcraft.modID, "part.circuit", 4L, 2),
                300,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Phenolic_Good.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1), ItemList.Cover_Controller.get(1L) },
                tMat.getMolten(144L * tMultiplier / 2L),
                getModItem(Railcraft.modID, "part.circuit", 8L, 0),
                400,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Phenolic_Good.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1), ItemList.Sensor_LV.get(1L) },
                tMat.getMolten(144L * tMultiplier / 2L),
                getModItem(Railcraft.modID, "part.circuit", 8L, 1),
                400,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Phenolic_Good.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1), getModItem(Railcraft.modID, "part.signal.lamp", 1L, 0) },
                tMat.getMolten(144L * tMultiplier / 2L),
                getModItem(Railcraft.modID, "part.circuit", 8L, 2),
                400,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Epoxy_Advanced.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1), ItemList.Cover_Controller.get(1L) },
                tMat.getMolten(144L * tMultiplier / 2L),
                getModItem(Railcraft.modID, "part.circuit", 16L, 0),
                500,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Epoxy_Advanced.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1), ItemList.Sensor_LV.get(1L) },
                tMat.getMolten(144L * tMultiplier / 2L),
                getModItem(Railcraft.modID, "part.circuit", 16L, 1),
                500,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Epoxy_Advanced.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1), getModItem(Railcraft.modID, "part.signal.lamp", 1L, 0) },
                tMat.getMolten(144L * tMultiplier / 2L),
                getModItem(Railcraft.modID, "part.circuit", 16L, 2),
                500,
                (int) TierEU.RECIPE_LV);

            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Coated_Basic.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Primitive, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Iron, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Iron, 1), GT_Utility.getIntegratedCircuit(1) },
                tMat.getMolten(1152L * tMultiplier / 2L),
                getModItem(Forestry.modID, "chipsets", 1L, 0),
                200,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Coated_Basic.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Bronze, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Bronze, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Bronze, 1), GT_Utility.getIntegratedCircuit(1) },
                tMat.getMolten(1152L * tMultiplier / 2L),
                getModItem(Forestry.modID, "chipsets", 1L, 1),
                200,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Phenolic_Good.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Steel, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 1), GT_Utility.getIntegratedCircuit(1) },
                tMat.getMolten(1152L * tMultiplier / 2L),
                getModItem(Forestry.modID, "chipsets", 1L, 2),
                200,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Phenolic_Good.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Electrum, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 1), GT_Utility.getIntegratedCircuit(1) },
                tMat.getMolten(1152L * tMultiplier / 2L),
                getModItem(Forestry.modID, "chipsets", 1L, 3),
                200,
                (int) TierEU.RECIPE_LV);

        }
    }

    public void registerForestryRecipes(){
        if (!Forestry.isModLoaded()){
            return ;
        }

        // alternative version of the copper electron tube
        GT_Values.RA.addCircuitAssemblerRecipe(
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.AnnealedCopper, 2L) },
            Materials.Glass.getMolten(576L),
            getModItem(Forestry.modID, "thermionicTubes", 4L, 0),
            200,
            (int) TierEU.RECIPE_LV);

        // alternative version of the iron electron tube
        GT_Values.RA.addCircuitAssemblerRecipe(
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.WroughtIron, 2L) },
            Materials.Glass.getMolten(576L),
            getModItem(Forestry.modID, "thermionicTubes", 4L, 3),
            200,
            (int) TierEU.RECIPE_LV);


        ItemStack[] rodMaterials = new ItemStack[]{
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Copper, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Tin, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Bronze, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Iron, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Gold, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Diamond, 2L),
            getModItem(NewHorizonsCoreMod.modID, "item.LongObsidianRod", 2L, 0),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Blaze, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Rubber, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Emerald, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Apatite, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Lapis, 2L) ,
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.EnderEye, 2L),
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Uranium, 2L),
        };

        for (int metaid = 0; metaid <rodMaterials.length; metaid++){
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), rodMaterials[metaid]},
                Materials.Glass.getMolten(576L),
                getModItem(Forestry.modID, "thermionicTubes", 4L, metaid),
                200,
                (int) TierEU.RECIPE_LV);
        }

        for (Materials tMat : solderingMats) {

            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Coated_Basic.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Primitive, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Iron, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Iron, 1), GT_Utility.getIntegratedCircuit(1) },
                tMat.getMolten(1152L * tMultiplier / 2L),
                getModItem(Forestry.modID, "chipsets", 1L, 0),
                200,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Coated_Basic.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Bronze, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Bronze, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Bronze, 1), GT_Utility.getIntegratedCircuit(1) },
                tMat.getMolten(1152L * tMultiplier / 2L),
                getModItem(Forestry.modID, "chipsets", 1L, 1),
                200,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Phenolic_Good.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Steel, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 1), GT_Utility.getIntegratedCircuit(1) },
                tMat.getMolten(1152L * tMultiplier / 2L),
                getModItem(Forestry.modID, "chipsets", 1L, 2),
                200,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Phenolic_Good.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Electrum, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 1), GT_Utility.getIntegratedCircuit(1) },
                tMat.getMolten(1152L * tMultiplier / 2L),
                getModItem(Forestry.modID, "chipsets", 1L, 3),
                200,
                (int) TierEU.RECIPE_LV);

        }
    }
}
