package gregtech.loaders.oreprocessing;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.item.ItemStack;

public class ProcessingRotor implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingRotor() {
        OrePrefixes.rotor.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial) && !aMaterial.contains(SubTag.NO_WORKING)) {
            GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"PhP", "SRf", "PdP", 'P', aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial) : OrePrefixes.plate.get(aMaterial), 'R', OrePrefixes.ring.get(aMaterial), 'S', OrePrefixes.screw.get(aMaterial)});
            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 4L), GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L), GT_Utility.getIntegratedCircuit(4)}, Materials.Tin.getMolten(32), GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L), 240, 24);
            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 4L), GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L), GT_Utility.getIntegratedCircuit(4)}, Materials.Lead.getMolten(48), GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L), 240, 24);
            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 4L), GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L), GT_Utility.getIntegratedCircuit(4)}, Materials.SolderingAlloy.getMolten(16), GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L), 240, 24);
            GT_Values.RA.addExtruderRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 5L), ItemList.Shape_Extruder_Rotor.get(0L), GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L), 200, 60);
            if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Rotor.get(0L), aMaterial.getMolten(612L), GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L), 100, 60);
         }
        }
    }
}
