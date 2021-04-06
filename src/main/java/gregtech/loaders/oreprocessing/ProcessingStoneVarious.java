package gregtech.loaders.oreprocessing;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ProcessingStoneVarious implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingStoneVarious() {
        OrePrefixes.stoneSmooth.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {

    }

    public void registerOreAsync(OrePrefixes aPrefix, gregtech.api.enums.Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        GT_Values.RA.addAssemblerRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Circuit_Integrated.getWithDamage(0L, 1L), new ItemStack(Blocks.stone_button, 1), 100, 4);
        GT_Values.RA.addAssemblerRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Circuit_Integrated.getWithDamage(0L, 2L), new ItemStack(Blocks.stone_pressure_plate, 1), 200, 4);
    }
}
