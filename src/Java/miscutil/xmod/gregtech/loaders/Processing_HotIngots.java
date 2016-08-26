package miscutil.xmod.gregtech.loaders;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import miscutil.core.util.Utils;
import miscutil.xmod.gregtech.api.enums.GregtechOrePrefixes;
import miscutil.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import miscutil.xmod.gregtech.api.interfaces.internal.Interface_OreRecipeRegistrator;
import net.minecraft.item.ItemStack;

public class Processing_HotIngots implements Interface_OreRecipeRegistrator {
    public Processing_HotIngots() {
    	Utils.LOG_INFO("Generating Hot Ingot.");
        GregtechOrePrefixes.ingotHot.add(this);
    }

    @Override
	public void registerOre(GregtechOrePrefixes aPrefix, GT_Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        GT_Values.RA.addVacuumFreezerRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), (int) Math.max(aMaterial.getMass() * 3L, 1L));
    }
}
