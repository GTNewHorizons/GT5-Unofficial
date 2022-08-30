package gregtech.loaders.oreprocessing;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.item.ItemStack;

public class ProcessingFineWire implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingFineWire() {
        OrePrefixes.wireFine.add(this);
    }

    @Override
    public void registerOre(
            OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_SMASHING)) {
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                    GT_Utility.getIntegratedCircuit(1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 2L),
                    100,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                    GT_Utility.getIntegratedCircuit(2),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L),
                    150,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 2L),
                    GT_Utility.getIntegratedCircuit(4),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L),
                    200,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 4L),
                    GT_Utility.getIntegratedCircuit(8),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt08, aMaterial, 1L),
                    250,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 6L),
                    GT_Utility.getIntegratedCircuit(12),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt12, aMaterial, 1L),
                    300,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 8L),
                    GT_Utility.getIntegratedCircuit(16),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, aMaterial, 1L),
                    350,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                    GT_Utility.getIntegratedCircuit(1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L),
                    50,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L),
                    GT_Utility.getIntegratedCircuit(2),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L),
                    100,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 4L),
                    GT_Utility.getIntegratedCircuit(4),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L),
                    150,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 8L),
                    GT_Utility.getIntegratedCircuit(8),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt08, aMaterial, 1L),
                    200,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 12L),
                    GT_Utility.getIntegratedCircuit(12),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt12, aMaterial, 1L),
                    250,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 16L),
                    GT_Utility.getIntegratedCircuit(16),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, aMaterial, 1L),
                    300,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                    GT_Utility.getIntegratedCircuit(3),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 8L),
                    100,
                    4);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                    GT_Utility.getIntegratedCircuit(3),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 4L),
                    50,
                    4);
        }
        if ((aMaterial.mUnificatable)
                && (aMaterial.mMaterialInto == aMaterial)
                && !aMaterial.contains(SubTag.NO_WORKING)) {
            GT_ModHandler.addCraftingRecipe(GT_Utility.copyAmount(1L, aStack), GT_Proxy.tBits, new Object[] {
                "Xx", 'X', OrePrefixes.foil.get(aMaterial)
            });
        }
    }
}
