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

package com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration;

import static gregtech.api.enums.ItemList.Circuit_Board_Advanced;
import static gregtech.api.enums.ItemList.Circuit_Board_Basic;
import static gregtech.api.enums.ItemList.Circuit_Board_Bio;
import static gregtech.api.enums.ItemList.Circuit_Board_Bio_Ultra;
import static gregtech.api.enums.ItemList.Circuit_Board_Coated;
import static gregtech.api.enums.ItemList.Circuit_Board_Coated_Basic;
import static gregtech.api.enums.ItemList.Circuit_Board_Elite;
import static gregtech.api.enums.ItemList.Circuit_Board_Epoxy;
import static gregtech.api.enums.ItemList.Circuit_Board_Epoxy_Advanced;
import static gregtech.api.enums.ItemList.Circuit_Board_Fiberglass;
import static gregtech.api.enums.ItemList.Circuit_Board_Fiberglass_Advanced;
import static gregtech.api.enums.ItemList.Circuit_Board_Multifiberglass;
import static gregtech.api.enums.ItemList.Circuit_Board_Multifiberglass_Elite;
import static gregtech.api.enums.ItemList.Circuit_Board_Optical;
import static gregtech.api.enums.ItemList.Circuit_Board_Phenolic;
import static gregtech.api.enums.ItemList.Circuit_Board_Phenolic_Good;
import static gregtech.api.enums.ItemList.Circuit_Board_Plastic;
import static gregtech.api.enums.ItemList.Circuit_Board_Plastic_Advanced;
import static gregtech.api.enums.ItemList.Circuit_Board_Wetware;
import static gregtech.api.enums.ItemList.Circuit_Board_Wetware_Extreme;
import static gregtech.api.enums.ItemList.Circuit_Chip_BioCPU;
import static gregtech.api.enums.ItemList.Circuit_Chip_Biocell;
import static gregtech.api.enums.ItemList.Circuit_Chip_CPU;
import static gregtech.api.enums.ItemList.Circuit_Chip_CrystalCPU;
import static gregtech.api.enums.ItemList.Circuit_Chip_CrystalSoC;
import static gregtech.api.enums.ItemList.Circuit_Chip_CrystalSoC2;
import static gregtech.api.enums.ItemList.Circuit_Chip_HPIC;
import static gregtech.api.enums.ItemList.Circuit_Chip_ILC;
import static gregtech.api.enums.ItemList.Circuit_Chip_LPIC;
import static gregtech.api.enums.ItemList.Circuit_Chip_NAND;
import static gregtech.api.enums.ItemList.Circuit_Chip_NOR;
import static gregtech.api.enums.ItemList.Circuit_Chip_NPIC;
import static gregtech.api.enums.ItemList.Circuit_Chip_NanoCPU;
import static gregtech.api.enums.ItemList.Circuit_Chip_NeuroCPU;
import static gregtech.api.enums.ItemList.Circuit_Chip_Optical;
import static gregtech.api.enums.ItemList.Circuit_Chip_PIC;
import static gregtech.api.enums.ItemList.Circuit_Chip_PPIC;
import static gregtech.api.enums.ItemList.Circuit_Chip_QPIC;
import static gregtech.api.enums.ItemList.Circuit_Chip_QuantumCPU;
import static gregtech.api.enums.ItemList.Circuit_Chip_Ram;
import static gregtech.api.enums.ItemList.Circuit_Chip_Simple_SoC;
import static gregtech.api.enums.ItemList.Circuit_Chip_SoC;
import static gregtech.api.enums.ItemList.Circuit_Chip_SoC2;
import static gregtech.api.enums.ItemList.Circuit_Chip_Stemcell;
import static gregtech.api.enums.ItemList.Circuit_Chip_UHPIC;
import static gregtech.api.enums.ItemList.Circuit_Chip_ULPIC;
import static gregtech.api.enums.ItemList.Circuit_Integrated;
import static gregtech.api.enums.ItemList.Circuit_Integrated_Good;
import static gregtech.api.enums.ItemList.Circuit_Parts_Advanced;
import static gregtech.api.enums.ItemList.Circuit_Parts_CapacitorASMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_CapacitorSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_CapacitorXSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_Chip_Bioware;
import static gregtech.api.enums.ItemList.Circuit_Parts_Crystal_Chip_Elite;
import static gregtech.api.enums.ItemList.Circuit_Parts_Crystal_Chip_Master;
import static gregtech.api.enums.ItemList.Circuit_Parts_Crystal_Chip_Wetware;
import static gregtech.api.enums.ItemList.Circuit_Parts_DiodeASMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_DiodeSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_DiodeXSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_GlassFiber;
import static gregtech.api.enums.ItemList.Circuit_Parts_InductorASMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_InductorSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_InductorXSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_PetriDish;
import static gregtech.api.enums.ItemList.Circuit_Parts_ResistorASMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_ResistorSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_ResistorXSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_TransistorASMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_TransistorSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_TransistorXSMD;
import static gregtech.api.enums.ItemList.Circuit_Parts_Vacuum_Tube;
import static gregtech.api.enums.ItemList.Circuit_Parts_Wiring_Advanced;
import static gregtech.api.enums.ItemList.Circuit_Parts_Wiring_Basic;
import static gregtech.api.enums.ItemList.Circuit_Parts_Wiring_Elite;
import static gregtech.api.enums.ItemList.Optical_Cpu_Containment_Housing;
import static gregtech.api.enums.ItemList.Optically_Compatible_Memory;
import static gregtech.api.enums.ItemList.Optically_Perfected_CPU;
import static gregtech.api.enums.ItemList.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.client.renderer.BW_GT_ItemRenderer;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;

public class CircuitPartLoader implements Runnable {

    @Override
    public void run() {
        CircuitPartLoader.makeCircuitParts();
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) new BW_GT_ItemRenderer();
    }

    public static void makeCircuitParts() {
        ItemList[] itemLists = values();
        for (ItemList single : itemLists) {
            if (!single.hasBeenSet()) continue;
            if (single.toString().contains("Wafer") || single.toString().contains("Circuit_Silicon_Ingot")
                    || single.toString().contains("Raw")
                    || single.toString().contains("raw")
                    || single.toString().contains("Glass_Tube")
                    || single == Circuit_Parts_GlassFiber
                    || single == Circuit_Parts_Advanced
                    || single == Circuit_Parts_Wiring_Advanced
                    || single == Circuit_Parts_Wiring_Elite
                    || single == Circuit_Parts_Wiring_Basic
                    || single == Circuit_Integrated
                    || single == Circuit_Parts_PetriDish
                    || single == Circuit_Parts_Vacuum_Tube
                    || single == Circuit_Integrated_Good) {

                CircuitImprintLoader.blacklistSet.add(single.get(1));
            }
        }

        for (ItemList single : CIRCUIT_PARTS) {
            if (!single.hasBeenSet()) continue;
            ItemStack itemStack = single.get(1);
            if (!GT_Utility.isStackValid(itemStack)) continue;
            ArrayList<String> toolTip = new ArrayList<>();
            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                single.getItem().addInformation(single.get(1).copy(), null, toolTip, true);
            String tt = (toolTip.size() > 0 ? toolTip.get(0) : "");
            // tt += "Internal Name = "+single;
            String localised = GT_LanguageManager
                    .getTranslation(GT_LanguageManager.getTranslateableItemStackName(itemStack));
            BW_Meta_Items.getNEWCIRCUITS().addItem(CircuitImprintLoader.reverseIDs, "Wrap of " + localised + "s", tt);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { single.get(16).copy(), GT_Utility.getIntegratedCircuit(16) },
                    Materials.Plastic.getMolten(72),
                    BW_Meta_Items.getNEWCIRCUITS().getStack(CircuitImprintLoader.reverseIDs),
                    600,
                    30);
            CircuitImprintLoader.circuitIIconRefs.put(CircuitImprintLoader.reverseIDs, single);
            CircuitImprintLoader.reverseIDs--;
        }
    }

    /**
     * Contains all the circuit parts we want to generate wrapped version of. New entries MUST be placed at the END of
     * this list, to prevent id shift.
     */
    private static final List<ItemList> CIRCUIT_PARTS = Collections.unmodifiableList(
            Arrays.asList(
                    Circuit_Board_Basic,
                    Circuit_Board_Advanced,
                    Circuit_Board_Elite,
                    Circuit_Parts_Crystal_Chip_Elite,
                    Circuit_Parts_Crystal_Chip_Master,
                    Circuit_Board_Coated,
                    Circuit_Board_Coated_Basic,
                    Circuit_Board_Phenolic,
                    Circuit_Board_Phenolic_Good,
                    Circuit_Board_Epoxy,
                    Circuit_Board_Epoxy_Advanced,
                    Circuit_Board_Fiberglass,
                    Circuit_Board_Fiberglass_Advanced,
                    Circuit_Board_Multifiberglass_Elite,
                    Circuit_Board_Multifiberglass,
                    Circuit_Board_Wetware,
                    Circuit_Board_Wetware_Extreme,
                    Circuit_Board_Plastic,
                    Circuit_Board_Plastic_Advanced,
                    Circuit_Board_Bio,
                    Circuit_Board_Bio_Ultra,
                    Circuit_Parts_ResistorSMD,
                    Circuit_Parts_InductorSMD,
                    Circuit_Parts_DiodeSMD,
                    Circuit_Parts_TransistorSMD,
                    Circuit_Parts_CapacitorSMD,
                    Circuit_Parts_ResistorASMD,
                    Circuit_Parts_DiodeASMD,
                    Circuit_Parts_TransistorASMD,
                    Circuit_Parts_CapacitorASMD,
                    Circuit_Chip_ILC,
                    Circuit_Chip_Ram,
                    Circuit_Chip_NAND,
                    Circuit_Chip_NOR,
                    Circuit_Chip_CPU,
                    Circuit_Chip_SoC,
                    Circuit_Chip_SoC2,
                    Circuit_Chip_PIC,
                    Circuit_Chip_Simple_SoC,
                    Circuit_Chip_HPIC,
                    Circuit_Chip_UHPIC,
                    Circuit_Chip_ULPIC,
                    Circuit_Chip_LPIC,
                    Circuit_Chip_NPIC,
                    Circuit_Chip_PPIC,
                    Circuit_Chip_QPIC,
                    Circuit_Chip_NanoCPU,
                    Circuit_Chip_QuantumCPU,
                    Circuit_Chip_CrystalCPU,
                    Circuit_Chip_CrystalSoC,
                    Circuit_Chip_CrystalSoC2,
                    Circuit_Chip_NeuroCPU,
                    Circuit_Chip_BioCPU,
                    Circuit_Chip_Stemcell,
                    Circuit_Chip_Biocell,
                    Circuit_Parts_ResistorXSMD,
                    Circuit_Parts_DiodeXSMD,
                    Circuit_Parts_TransistorXSMD,
                    Circuit_Parts_CapacitorXSMD,
                    Circuit_Parts_InductorASMD,
                    Circuit_Parts_InductorXSMD,
                    Circuit_Chip_Optical,
                    Circuit_Board_Optical,
                    Optically_Perfected_CPU,
                    Optical_Cpu_Containment_Housing,
                    Optically_Compatible_Memory,
                    Circuit_Parts_Crystal_Chip_Wetware,
                    Circuit_Parts_Chip_Bioware));
}
