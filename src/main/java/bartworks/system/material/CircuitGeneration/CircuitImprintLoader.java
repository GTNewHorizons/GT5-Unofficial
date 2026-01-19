/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material.CircuitGeneration;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.google.common.collect.ArrayListMultimap;

import bartworks.system.material.WerkstoffLoader;
import bartworks.util.BWUtil;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public class CircuitImprintLoader {

    // Todo: fix recipeTagMap to add the items back to the creative tab.
    public static final ArrayListMultimap<NBTTagCompound, GTRecipe> recipeTagMap = ArrayListMultimap.create();

    public static void registerItemstacks() {
        ItemList.CircuitImprint_NANDChipArray.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.NandChip.get(1)), 0, 1));
        ItemList.SlicedCircuit_NANDChipArray.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.NandChip.get(1)), 1, 1));

        ItemList.CircuitImprint_Microprocessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Microprocessor.get(1)), 0, 1));
        ItemList.SlicedCircuit_Microprocessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Microprocessor.get(1)), 1, 1));

        ItemList.CircuitImprint_ElectronicCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(GTModHandler.getIC2Item("electronicCircuit", 1)), 0, 1));
        ItemList.SlicedCircuit_ElectronicCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(GTModHandler.getIC2Item("electronicCircuit", 1)), 1, 1));
        ItemList.CircuitImprint_GoodElectronicCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Good.get(1)), 0, 1));
        ItemList.SlicedCircuit_GoodElectronicCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Good.get(1)), 1, 1));

        ItemList.CircuitImprint_IntegratedLogicCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Basic.get(1L)), 0, 1));
        ItemList.SlicedCircuit_IntegratedLogicCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Basic.get(1L)), 1, 1));
        ItemList.CircuitImprint_GoodIntegratedCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Integrated_Good.get(1)), 0, 1));
        ItemList.SlicedCircuit_GoodIntegratedCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Integrated_Good.get(1)), 1, 1));
        ItemList.CircuitImprint_AdvancedCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(GTModHandler.getIC2Item("advancedCircuit", 1)), 0, 1));
        ItemList.SlicedCircuit_AdvancedCircuit.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(GTModHandler.getIC2Item("advancedCircuit", 1)), 1, 1));

        ItemList.CircuitImprint_IntegratedProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Processor.get(1)), 0, 1));
        ItemList.SlicedCircuit_IntegratedProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Processor.get(1)), 1, 1));
        ItemList.CircuitImprint_ProcessorAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Advanced.get(1)), 0, 1));
        ItemList.SlicedCircuit_ProcessorAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Advanced.get(1)), 1, 1));
        ItemList.CircuitImprint_Workstation.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Data.get(1)), 0, 1));
        ItemList.SlicedCircuit_Workstation.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Data.get(1)), 1, 1));
        ItemList.CircuitImprint_Mainframe.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Elite.get(1)), 0, 1));
        ItemList.SlicedCircuit_Mainframe.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Elite.get(1)), 1, 1));

        ItemList.CircuitImprint_NanoProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Nanoprocessor.get(1)), 0, 1));
        ItemList.SlicedCircuit_NanoProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Nanoprocessor.get(1)), 1, 1));
        ItemList.CircuitImprint_NanoAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Nanocomputer.get(1)), 0, 1));
        ItemList.SlicedCircuit_NanoAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Nanocomputer.get(1)), 1, 1));
        ItemList.CircuitImprint_NanoSupercomputer.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Elitenanocomputer.get(1)), 0, 1));
        ItemList.SlicedCircuit_NanoSupercomputer.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Elitenanocomputer.get(1)), 1, 1));
        ItemList.CircuitImprint_NanoMainframe.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Master.get(1)), 0, 1));
        ItemList.SlicedCircuit_NanoMainframe.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Master.get(1)), 1, 1));

        ItemList.CircuitImprint_QuantumProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantumprocessor.get(1)), 0, 1));
        ItemList.SlicedCircuit_QuantumProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantumprocessor.get(1)), 1, 1));
        ItemList.CircuitImprint_QuantumAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantumcomputer.get(1)), 0, 1));
        ItemList.SlicedCircuit_QuantumAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantumcomputer.get(1)), 1, 1));
        ItemList.CircuitImprint_QuantumSupercomputer.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Masterquantumcomputer.get(1)), 0, 1));
        ItemList.SlicedCircuit_QuantumSupercomputer.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Masterquantumcomputer.get(1)), 1, 1));
        ItemList.CircuitImprint_QuantumMainframe.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantummainframe.get(1)), 0, 1));
        ItemList.SlicedCircuit_QuantumMainframe.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Quantummainframe.get(1)), 1, 1));

        ItemList.CircuitImprint_CrystalProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalprocessor.get(1)), 0, 1));
        ItemList.SlicedCircuit_CrystalProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalprocessor.get(1)), 1, 1));
        ItemList.CircuitImprint_CrystalAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalcomputer.get(1)), 0, 1));
        ItemList.SlicedCircuit_CrystalAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalcomputer.get(1)), 1, 1));
        ItemList.CircuitImprint_CrystalSupercomputer.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Ultimatecrystalcomputer.get(1)), 0, 1));
        ItemList.SlicedCircuit_CrystalSupercomputer.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Ultimatecrystalcomputer.get(1)), 1, 1));
        ItemList.CircuitImprint_CrystalMainframe.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalmainframe.get(1)), 0, 1));
        ItemList.SlicedCircuit_CrystalMainframe.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Crystalmainframe.get(1)), 1, 1));

        ItemList.CircuitImprint_WetwareProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Neuroprocessor.get(1)), 0, 1));
        ItemList.SlicedCircuit_WetwareProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Neuroprocessor.get(1)), 1, 1));
        ItemList.CircuitImprint_WetwareAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Wetwarecomputer.get(1)), 0, 1));
        ItemList.SlicedCircuit_WetwareAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Wetwarecomputer.get(1)), 1, 1));
        ItemList.CircuitImprint_WetwareSupercomputer.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Wetwaresupercomputer.get(1)), 0, 1));
        ItemList.SlicedCircuit_WetwareSupercomputer.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Wetwaresupercomputer.get(1)), 1, 1));

        ItemList.CircuitImprint_BiowareProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Bioprocessor.get(1)), 0, 1));
        ItemList.SlicedCircuit_BiowareProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Bioprocessor.get(1)), 1, 1));
        ItemList.CircuitImprint_BiowareAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Biowarecomputer.get(1)), 0, 1));
        ItemList.SlicedCircuit_BiowareAssembly.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_Biowarecomputer.get(1)), 1, 1));

        ItemList.CircuitImprint_OpticalProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_OpticalProcessor.get(1)), 0, 1));
        ItemList.SlicedCircuit_OpticalProcessor.set(
            BWMetaItems.getCircuitParts()
                .getStackWithNBT(getTagFromStack(ItemList.Circuit_OpticalProcessor.get(1)), 1, 1));

        if (Forestry.isModLoaded()) {
            ItemList.CircuitImprint_BasicCircuitBoard.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 0)), 0, 1));
            ItemList.SlicedCircuit_BasicCircuitBoard.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 0)), 1, 1));
            ItemList.CircuitImprint_EnhancedCircuitBoard.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 1)), 0, 1));
            ItemList.SlicedCircuit_EnhancedCircuitBoard.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 1)), 1, 1));
            ItemList.CircuitImprint_RefinedCircuitBoard.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 2)), 0, 1));
            ItemList.SlicedCircuit_RefinedCircuitBoard.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 2)), 1, 1));
            ItemList.CircuitImprint_IntricateCircuitBoard.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 3)), 0, 1));
            ItemList.SlicedCircuit_IntricateCircuitBoard.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(getTagFromStack(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 3)), 1, 1));
        }

        if (Railcraft.isModLoaded()) {
            ItemList.CircuitImprint_ControllerCircuit.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(
                        getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 0)),
                        0,
                        1));
            ItemList.SlicedCircuit_ControllerCircuit.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(
                        getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 0)),
                        1,
                        1));
            ItemList.CircuitImprint_ReceiverCircuit.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(
                        getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 1)),
                        0,
                        1));
            ItemList.SlicedCircuit_ReceiverCircuit.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(
                        getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 1)),
                        1,
                        1));
            ItemList.CircuitImprint_SignalCircuit.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(
                        getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 2)),
                        0,
                        1));
            ItemList.SlicedCircuit_SignalCircuit.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(
                        getTagFromStack(GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 2)),
                        1,
                        1));
        }

        if (NewHorizonsCoreMod.isModLoaded()) {
            ItemList.CircuitImprint_HighEnergyFlowCircuit.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(
                        getTagFromStack(getModItem(NewHorizonsCoreMod.ID, "HighEnergyFlowCircuit", 64, 0)),
                        0,
                        1));
            ItemList.SlicedCircuit_HighEnergyFlowCircuit.set(
                BWMetaItems.getCircuitParts()
                    .getStackWithNBT(
                        getTagFromStack(getModItem(NewHorizonsCoreMod.ID, "HighEnergyFlowCircuit", 64, 0)),
                        1,
                        1));
        }
    }

    public static void makeCuttingRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.NandChipArray.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_NANDChipArray.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Microprocessor.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_Microprocessor.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("electronicCircuit", 1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_ElectronicCircuit.get(1))
            .duration(15 * SECONDS)
            .eut(16)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Good.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_GoodElectronicCircuit.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Basic.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_IntegratedLogicCircuit.get(1))
            .duration(15 * SECONDS)
            .eut(16)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Integrated_Good.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_GoodIntegratedCircuit.get(1))
            .duration(15 * SECONDS)
            .eut(24)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("advancedCircuit", 1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_AdvancedCircuit.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Processor.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_IntegratedProcessor.get(1))
            .duration(15 * SECONDS)
            .eut(60)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Advanced.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_ProcessorAssembly.get(1))
            .duration(15 * SECONDS)
            .eut(96)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Data.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_Workstation.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Elite.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_Mainframe.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Nanoprocessor.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_NanoProcessor.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Nanocomputer.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_NanoAssembly.get(1))
            .duration(15 * SECONDS)
            .eut(600)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Elitenanocomputer.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_NanoSupercomputer.get(1))
            .duration(15 * SECONDS)
            .eut(600)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Master.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_NanoMainframe.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Quantumprocessor.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_QuantumProcessor.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Quantumcomputer.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_QuantumAssembly.get(1))
            .duration(15 * SECONDS)
            .eut(2400)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Masterquantumcomputer.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_QuantumSupercomputer.get(1))
            .duration(15 * SECONDS)
            .eut(2400)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Quantummainframe.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_QuantumMainframe.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Crystalprocessor.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_CrystalProcessor.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Crystalcomputer.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_CrystalAssembly.get(1))
            .duration(15 * SECONDS)
            .eut(9600)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Ultimatecrystalcomputer.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_CrystalSupercomputer.get(1))
            .duration(15 * SECONDS)
            .eut(9600)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Crystalmainframe.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_CrystalMainframe.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Neuroprocessor.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_WetwareProcessor.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Wetwarecomputer.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_WetwareAssembly.get(1))
            .duration(15 * SECONDS)
            .eut(38400)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Wetwaresupercomputer.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_WetwareSupercomputer.get(1))
            .duration(15 * SECONDS)
            .eut(38400)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Bioprocessor.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_BiowareProcessor.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Biowarecomputer.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_BiowareAssembly.get(1))
            .duration(15 * SECONDS)
            .eut(153600)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_OpticalProcessor.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.SlicedCircuit_OpticalProcessor.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .requiresCleanRoom()
            .addTo(cutterRecipes);

        // recipes behind a mod loaded check because the ItemList entry is conditionally set, so if Forestry isn't
        // present the ItemList entries will cause an IllegalAccessError
        if (Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 0), ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.SlicedCircuit_BasicCircuitBoard.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .requiresCleanRoom()
                .requireMods(Forestry)
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 1), ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.SlicedCircuit_EnhancedCircuitBoard.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .requiresCleanRoom()
                .requireMods(Forestry)
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 2), ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.SlicedCircuit_RefinedCircuitBoard.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .requiresCleanRoom()
                .requireMods(Forestry)
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(Forestry.ID, "chipsets", 1, 3), ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.SlicedCircuit_IntricateCircuitBoard.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .requiresCleanRoom()
                .requireMods(Forestry)
                .addTo(cutterRecipes);
        }

        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 0),
                    ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.SlicedCircuit_ControllerCircuit.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .requiresCleanRoom()
                .requireMods(Railcraft)
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 1),
                    ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.SlicedCircuit_ReceiverCircuit.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .requiresCleanRoom()
                .requireMods(Railcraft)
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTModHandler.getModItem(Railcraft.ID, "part.circuit", 1, 2),
                    ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.SlicedCircuit_SignalCircuit.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .requiresCleanRoom()
                .requireMods(Railcraft)
                .addTo(cutterRecipes);
        }

        if (NewHorizonsCoreMod.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    getModItem(NewHorizonsCoreMod.ID, "HighEnergyFlowCircuit", 1, 0),
                    ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.SlicedCircuit_HighEnergyFlowCircuit.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .requiresCleanRoom()
                .requireMods(NewHorizonsCoreMod)
                .addTo(cutterRecipes);
        }
    }

    public static void makeCraftingRecipes() {
        List<ItemStack> circuitImprintsDefault = Arrays.asList(
            ItemList.CircuitImprint_NANDChipArray.get(1),
            ItemList.CircuitImprint_Microprocessor.get(1),
            ItemList.CircuitImprint_ElectronicCircuit.get(1),
            ItemList.CircuitImprint_GoodElectronicCircuit.get(1),
            ItemList.CircuitImprint_IntegratedLogicCircuit.get(1),
            ItemList.CircuitImprint_GoodIntegratedCircuit.get(1),
            ItemList.CircuitImprint_AdvancedCircuit.get(1),
            ItemList.CircuitImprint_IntegratedProcessor.get(1),
            ItemList.CircuitImprint_ProcessorAssembly.get(1),
            ItemList.CircuitImprint_Workstation.get(1),
            ItemList.CircuitImprint_Mainframe.get(1),
            ItemList.CircuitImprint_NanoProcessor.get(1),
            ItemList.CircuitImprint_NanoAssembly.get(1),
            ItemList.CircuitImprint_NanoSupercomputer.get(1),
            ItemList.CircuitImprint_NanoMainframe.get(1),
            ItemList.CircuitImprint_QuantumProcessor.get(1),
            ItemList.CircuitImprint_QuantumAssembly.get(1),
            ItemList.CircuitImprint_QuantumSupercomputer.get(1),
            ItemList.CircuitImprint_QuantumMainframe.get(1),
            ItemList.CircuitImprint_CrystalProcessor.get(1),
            ItemList.CircuitImprint_CrystalAssembly.get(1),
            ItemList.CircuitImprint_CrystalSupercomputer.get(1),
            ItemList.CircuitImprint_CrystalMainframe.get(1),
            ItemList.CircuitImprint_WetwareProcessor.get(1),
            ItemList.CircuitImprint_WetwareAssembly.get(1),
            ItemList.CircuitImprint_WetwareSupercomputer.get(1),
            ItemList.CircuitImprint_BiowareProcessor.get(1),
            ItemList.CircuitImprint_BiowareAssembly.get(1),
            ItemList.CircuitImprint_OpticalProcessor.get(1));
        List<ItemStack> circuitImprints = new ArrayList<>(circuitImprintsDefault);

        List<ItemStack> slicedCircuitsDefault = Arrays.asList(
            ItemList.SlicedCircuit_NANDChipArray.get(1),
            ItemList.SlicedCircuit_Microprocessor.get(1),
            ItemList.SlicedCircuit_ElectronicCircuit.get(1),
            ItemList.SlicedCircuit_GoodElectronicCircuit.get(1),
            ItemList.SlicedCircuit_IntegratedLogicCircuit.get(1),
            ItemList.SlicedCircuit_GoodIntegratedCircuit.get(1),
            ItemList.SlicedCircuit_AdvancedCircuit.get(1),
            ItemList.SlicedCircuit_IntegratedProcessor.get(1),
            ItemList.SlicedCircuit_ProcessorAssembly.get(1),
            ItemList.SlicedCircuit_Workstation.get(1),
            ItemList.SlicedCircuit_Mainframe.get(1),
            ItemList.SlicedCircuit_NanoProcessor.get(1),
            ItemList.SlicedCircuit_NanoAssembly.get(1),
            ItemList.SlicedCircuit_NanoSupercomputer.get(1),
            ItemList.SlicedCircuit_NanoMainframe.get(1),
            ItemList.SlicedCircuit_QuantumProcessor.get(1),
            ItemList.SlicedCircuit_QuantumAssembly.get(1),
            ItemList.SlicedCircuit_QuantumSupercomputer.get(1),
            ItemList.SlicedCircuit_QuantumMainframe.get(1),
            ItemList.SlicedCircuit_CrystalProcessor.get(1),
            ItemList.SlicedCircuit_CrystalAssembly.get(1),
            ItemList.SlicedCircuit_CrystalSupercomputer.get(1),
            ItemList.SlicedCircuit_CrystalMainframe.get(1),
            ItemList.SlicedCircuit_WetwareProcessor.get(1),
            ItemList.SlicedCircuit_WetwareAssembly.get(1),
            ItemList.SlicedCircuit_WetwareSupercomputer.get(1),
            ItemList.SlicedCircuit_BiowareProcessor.get(1),
            ItemList.SlicedCircuit_BiowareAssembly.get(1),
            ItemList.SlicedCircuit_OpticalProcessor.get(1));
        List<ItemStack> slicedCircuits = new ArrayList<>(slicedCircuitsDefault);

        if (Forestry.isModLoaded()) {
            List<ItemStack> circuitImprintsForestry = Arrays.asList(
                ItemList.CircuitImprint_BasicCircuitBoard.get(1),
                ItemList.CircuitImprint_EnhancedCircuitBoard.get(1),
                ItemList.CircuitImprint_RefinedCircuitBoard.get(1),
                ItemList.CircuitImprint_IntricateCircuitBoard.get(1));
            circuitImprints.addAll(circuitImprintsForestry);

            List<ItemStack> slicedCircuitsForestry = Arrays.asList(
                ItemList.SlicedCircuit_BasicCircuitBoard.get(1),
                ItemList.SlicedCircuit_EnhancedCircuitBoard.get(1),
                ItemList.SlicedCircuit_RefinedCircuitBoard.get(1),
                ItemList.SlicedCircuit_IntricateCircuitBoard.get(1));
            slicedCircuits.addAll(slicedCircuitsForestry);
        }

        if (Railcraft.isModLoaded()) {
            List<ItemStack> circuitImprintsRailcraft = Arrays.asList(
                ItemList.CircuitImprint_ControllerCircuit.get(1),
                ItemList.CircuitImprint_ReceiverCircuit.get(1),
                ItemList.CircuitImprint_SignalCircuit.get(1));
            circuitImprints.addAll(circuitImprintsRailcraft);

            List<ItemStack> slicedCircuitsRailcraft = Arrays.asList(
                ItemList.SlicedCircuit_ControllerCircuit.get(1),
                ItemList.SlicedCircuit_ReceiverCircuit.get(1),
                ItemList.SlicedCircuit_SignalCircuit.get(1));
            slicedCircuits.addAll(slicedCircuitsRailcraft);
        }

        if (NewHorizonsCoreMod.isModLoaded()) {
            circuitImprints.add(ItemList.CircuitImprint_HighEnergyFlowCircuit.get(1));
            slicedCircuits.add(ItemList.SlicedCircuit_HighEnergyFlowCircuit.get(1));
        }

        ItemStack imprintSupportingBoard = BWMetaItems.getCircuitParts()
            .getStack(3);
        ItemStack exquisitePrasiolite = WerkstoffLoader.Prasiolite.get(OrePrefixes.gemExquisite, 1);
        long bitmask = GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.KEEPNBT
            | GTModHandler.RecipeBits.BUFFERED;
        for (int i = 0; i < circuitImprints.size(); i++) {
            ItemStack slicedCircuit = slicedCircuits.get(i);
            ItemStack circuitImprint = circuitImprints.get(i);
            Object[] imprintRecipe = { " X ", "GPG", " X ", 'P', slicedCircuit, 'G', exquisitePrasiolite, 'X',
                imprintSupportingBoard };
            ShapedOreRecipe gtrecipe = BWUtil.createGTCraftingRecipe(circuitImprint, bitmask, imprintRecipe);
            GameRegistry.addRecipe(gtrecipe);
        }
    }

    public static NBTTagCompound getTagFromStack(ItemStack stack) {
        if (GTUtility.isStackValid(stack)) return BWUtil.setStackSize(stack.copy(), 1)
            .writeToNBT(new NBTTagCompound());
        return new NBTTagCompound();
    }

    public static ItemStack getStackFromTag(NBTTagCompound tagCompound) {
        return ItemStack.loadItemStackFromNBT(tagCompound);
    }
}
