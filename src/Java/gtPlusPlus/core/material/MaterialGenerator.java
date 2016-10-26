package gtPlusPlus.core.material;

import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.item.base.bolts.BaseItemBolt;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.gears.BaseItemGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.ingots.BaseItemIngotHot;
import gtPlusPlus.core.item.base.nugget.BaseItemNugget;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.base.rings.BaseItemRing;
import gtPlusPlus.core.item.base.rods.BaseItemRod;
import gtPlusPlus.core.item.base.rods.BaseItemRodLong;
import gtPlusPlus.core.item.base.rotors.BaseItemRotor;
import gtPlusPlus.core.item.base.screws.BaseItemScrew;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_BlastSmelter;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_DustGeneration;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Extruder;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Plates;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_ShapedCrafting;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class MaterialGenerator implements Runnable {
	
	final Material toGenerate;
	
	public MaterialGenerator(final Material M){
		this.toGenerate = M;
	}
	
	@Override
	public void run() {
		generateItemsFromMaterial(toGenerate);		
	}
	

	public static void generateItemsFromMaterial(final Material matInfo){	
		String unlocalizedName = matInfo.getUnlocalizedName();
		String materialName = matInfo.getLocalizedName();
		short[] C = matInfo.getRGBA();
		int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);
		boolean hotIngot = matInfo.requiresBlastFurnace();
		int materialTier = matInfo.vTier; //TODO
	
		if (materialTier > 10 || materialTier <= 0){
			materialTier = 2;
		}	
	
		int sRadiation = 0;
		if (ItemUtils.isRadioactive(materialName)){
			sRadiation = ItemUtils.getRadioactivityLevel(materialName);
		}
	
		if (sRadiation >= 1){
			Item temp;
			Block tempBlock;
			tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
			temp = new BaseItemIngot("itemIngot"+unlocalizedName, materialName, Colour, sRadiation);
	
			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);
			temp = new BaseItemNugget(matInfo);
			temp = new BaseItemPlate(matInfo);
			temp = new BaseItemRod(matInfo);
			temp = new BaseItemRodLong(matInfo);
		}
	
		else {
			Item temp;
			Block tempBlock;
			tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
			tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.FRAME, Colour);
			temp = new BaseItemIngot("itemIngot"+unlocalizedName, materialName, Colour, sRadiation);
			if (hotIngot){
				Item tempIngot = temp;
				temp = new BaseItemIngotHot("itemHotIngot"+unlocalizedName, materialName, ItemUtils.getSimpleStack(tempIngot, 1), materialTier);
			}
			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);
			temp = new BaseItemNugget(matInfo);
			temp = new BaseItemPlate(matInfo);
			temp = new BaseItemPlateDouble(matInfo);
			temp = new BaseItemBolt(matInfo);
			temp = new BaseItemRod(matInfo);
			temp = new BaseItemRodLong(matInfo);
			temp = new BaseItemRing(matInfo);
			temp = new BaseItemScrew(matInfo);
			temp = new BaseItemRotor(matInfo);
			temp = new BaseItemGear(matInfo);
		}		
	
		//Add A jillion Recipes
		RecipeGen_Plates.generateRecipes(matInfo);
		RecipeGen_Extruder.generateRecipes(matInfo);
		RecipeGen_ShapedCrafting.generateRecipes(matInfo);
		RecipeGen_DustGeneration.generateRecipes(matInfo);
		RecipeGen_BlastSmelter.generateARecipe(matInfo);		
	
	}

}
