package com.github.technus.tectech.loader.recipe;

import static gregtech.api.enums.GT_Values.RA;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.compatibility.dreamcraft.DreamCraftRecipeLoader;
import com.github.technus.tectech.compatibility.gtpp.GtppAtomLoader;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMHadronDefinition;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.item.EuMeterGT;
import com.gtnewhorizon.structurelib.StructureLibAPI;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class BaseRecipeLoader {

    public static Materials getOrDefault(String name, Materials def) {
        Materials mat = Materials.get(name);
        return mat == Materials._NULL || mat == null ? def : mat;
    }

    public void run(EMTransformationRegistry transformationInfo) {
        EMAtomDefinition.setTransformations(transformationInfo);
        EMHadronDefinition.setTransformations(transformationInfo);
        if (Loader.isModLoaded(Reference.GTPLUSPLUS)) {
            new GtppAtomLoader().setTransformations(transformationInfo);
        }

        // ===================================================================================================
        // Recipes init - common goes here rest goes into methods below
        // ===================================================================================================

        for (int i = 0; i <= 15; i++) {
            RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(i + 1),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1) },
                    Materials.Aluminium.getMolten(864),
                    new ItemStack(StructureLibAPI.getBlockHint(), 1, i),
                    32,
                    120);
        }

        // Front Rotation Tool
        GT_ModHandler.addCraftingRecipe(
                GT_ModHandler.getModItem(StructureLibAPI.MOD_ID, "item.structurelib.frontRotationTool", 1L, 0),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "fPR", " RP", "S h", 'P', OrePrefixes.plate.get(Materials.Cobalt), 'R',
                        OrePrefixes.stick.get(Materials.Cobalt), 'S', OrePrefixes.stick.get(Materials.Wood), });

        // BLUEprint
        // GT_ModHandler.addShapelessCraftingRecipe(new ItemStack(ConstructableTriggerItem.INSTANCE, 1),
        // GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
        // new Object[]{Dyes.dyeBlue, OrePrefixes.plate.get(Materials.Paper), Dyes.dyeBlue,
        // Dyes.dyeWhite});

        // GT EU reader
        GT_ModHandler.addCraftingRecipe(
                new ItemStack(EuMeterGT.INSTANCE, 1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "PGW", "SCW", "BRN", 'P', OrePrefixes.plateDouble.get(Materials.Steel), 'G',
                        OrePrefixes.plate.get(Materials.Glass), 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'S',
                        OrePrefixes.stick.get(Materials.Brass), 'C', ItemList.Casing_Coil_Cupronickel.get(1), 'B',
                        Dyes.dyeBlue, 'R', Dyes.dyeRed, 'N', Dyes.dyeBlack, });

        // Owner detector
        GT_ModHandler.addCraftingRecipe(
                CustomItemList.Machine_OwnerDetector.get(1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "PPP", "GEG", "PPP", 'P', OrePrefixes.plate.get(Materials.IronMagnetic), 'G',
                        OrePrefixes.plate.get(Materials.Glass), 'E', OrePrefixes.gem.get(Materials.EnderPearl) });

        // Data Bank
        RA.addAssemblylineRecipe(
                ItemList.Hatch_DataAccess_EV.get(1),
                20000,
                new Object[] { CustomItemList.Machine_Multi_Switch.get(1),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 2 }, ItemList.Tool_DataOrb.get(1),
                        ItemList.Cover_Screen.get(1), },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Hydrogen.getGas(1000), },
                CustomItemList.Machine_Multi_DataBank.get(1),
                12000,
                14000);

        // Bucks
        // RA.addAssemblerRecipe(CustomItemList.)

        // recipe for ass line data hatches
        RA.addAssemblerRecipe(
                ItemList.Hatch_DataAccess_EV.get(1),
                CustomItemList.dataIn_Hatch.get(1),
                CustomItemList.dataInAss_Hatch.get(1),
                2048,
                12000);
        RA.addAssemblerRecipe(
                ItemList.Hatch_DataAccess_EV.get(1),
                CustomItemList.dataOut_Hatch.get(1),
                CustomItemList.dataOutAss_Hatch.get(1),
                2048,
                12000);

        if (Loader.isModLoaded(Reference.DREAMCRAFT)) {
            new DreamCraftRecipeLoader().run(transformationInfo); // init recipes for GTNH version
        }  else {
            new BloodyRecipeLoader().run(transformationInfo); // init recipes for NON-GTNH version
        }
    }
}
