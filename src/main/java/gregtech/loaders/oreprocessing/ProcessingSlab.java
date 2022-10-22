package gregtech.loaders.oreprocessing;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.GT_Values.MOD_ID_RC;

public class ProcessingSlab implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingSlab() {
        OrePrefixes.slab.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if (aOreDictName.startsWith("slabWood")) {
            if (Loader.isModLoaded(MOD_ID_RC)) {
            GT_Values.RA.addChemicalBathRecipe(GT_Utility.copyAmount(3L, aStack), Materials.Creosote.getFluid(300L), ItemList.RC_Tie_Wood.get(3L), null, null, null, 200, 4);
            }
        }

    }
}
