package gtPlusPlus.xmod.gregtech.api.recipe;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import net.minecraft.item.ItemStack;

public class ProcessingSkookumChoocherToolRecipes implements IOreRecipeRegistrator {
    public ProcessingSkookumChoocherToolRecipes() {
        //GregtechOrePrefixes.toolSkookumChoocher.add(this);
    }

    @Override
	public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        GT_ModHandler.addShapelessCraftingRecipe(MetaGeneratedGregtechTools.INSTANCE.getToolWithStats(7734, 1, aMaterial, aMaterial, null), new Object[]{aOreDictName, OrePrefixes.stick.get(aMaterial), OrePrefixes.screw.get(aMaterial), ToolDictNames.craftingToolScrewdriver});
    }
}
