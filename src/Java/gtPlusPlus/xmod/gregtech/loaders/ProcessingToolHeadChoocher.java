package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_OreRecipeRegistrator;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import net.minecraft.item.ItemStack;

public class ProcessingToolHeadChoocher implements Interface_OreRecipeRegistrator, Runnable {
    public ProcessingToolHeadChoocher() {
        GregtechOrePrefixes.toolSkookumChoocher.add(this);
    }

    @Override
	public void registerOre(GregtechOrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)) {
            if (aMaterial != Materials.Rubber)
            if ((!aMaterial.contains(SubTag.WOOD)) && (!aMaterial.contains(SubTag.BOUNCY)) && (!aMaterial.contains(SubTag.NO_SMASHING))) {
                GT_ModHandler.addCraftingRecipe(MetaGeneratedGregtechTools.INSTANCE.getToolWithStats(16, 1, aMaterial, aMaterial, null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"IhI", "III", " I ", Character.valueOf('I'), OrePrefixes.ingot.get(aMaterial)});
              }
        }
    }

	@Override
	public void registerOre(GregtechOrePrefixes aPrefix,
			GT_Materials aMaterial, String aOreDictName, String aModName,
			ItemStack aStack) {
		// TODO Auto-generated method stub
		
	}
	
	public void materialsLoops(){
		Materials[] i = Materials.values();
		int size = i.length;
		Materials aMaterial = null;
		for (int r=0;r<size;r++){
			aMaterial = i[r];			
			if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)) {
	            if (aMaterial != Materials.Rubber)
	            if ((!aMaterial.contains(SubTag.WOOD)) && (!aMaterial.contains(SubTag.BOUNCY)) && (!aMaterial.contains(SubTag.NO_SMASHING))) {
	                GT_ModHandler.addCraftingRecipe(MetaGeneratedGregtechTools.INSTANCE.getToolWithStats(7734, 1, aMaterial, aMaterial, null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"P H", "PIP", " I ", Character.valueOf('I'), OrePrefixes.ingot.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(aMaterial), Character.valueOf('H'), OrePrefixes.toolHeadHammer.get(aMaterial)});
	              }
	        }
			
			
		}
		
		
	}

	@Override
	public void run() {
		Utils.LOG_INFO("Generating Skookum Choochers of all GT Materials.");
		materialsLoops();		
	}
	
	
}