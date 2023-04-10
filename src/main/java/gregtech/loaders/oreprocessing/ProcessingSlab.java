package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.Railcraft;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;

public class ProcessingSlab implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingSlab() {
        OrePrefixes.slab.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.startsWith("slabWood")) {
            if (Railcraft.isModLoaded()) {
                GT_Values.RA.addChemicalBathRecipe(
                    GT_Utility.copyAmount(3L, aStack),
                    Materials.Creosote.getFluid(300L),
                    ItemList.RC_Tie_Wood.get(3L),
                    null,
                    null,
                    null,
                    200,
                    4);
            }
        }
    }
}
