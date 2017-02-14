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
import gtPlusPlus.xmod.gregtech.loaders.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class MaterialGenerator {	

	public static void generate(final Material matInfo){	
		generate(matInfo, true);	
	}


	public static void generate(final Material matInfo, boolean generateEverything){	
		generate(matInfo, generateEverything, true);
	}
	
	public static void generate(final Material matInfo, boolean generateEverything, boolean generateBlastSmelterRecipes){	
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
			sRadiation = matInfo.vRadioationLevel;
		}

		if (generateEverything == true){		
			if (sRadiation >= 1){
				Item temp;
				Block tempBlock;
				tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
				temp = new BaseItemIngot(matInfo);

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
				temp = new BaseItemIngot(matInfo);
				if (hotIngot){
					temp = new BaseItemIngotHot(matInfo);
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
		} else {
			Item temp;
			Block tempBlock;
			tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
			temp = new BaseItemIngot(matInfo);
			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);
			temp = new BaseItemNugget(matInfo);
			temp = new BaseItemPlate(matInfo);
			temp = new BaseItemPlateDouble(matInfo);
		}


		//Add A jillion Recipes - old code
		RecipeGen_AlloySmelter.generateRecipes(matInfo);
		RecipeGen_Assembler.generateRecipes(matInfo);
		RecipeGen_BlastSmelter.generateARecipe(matInfo);
		RecipeGen_DustGeneration.generateRecipes(matInfo);	
		RecipeGen_Extruder.generateRecipes(matInfo);
		RecipeGen_Fluids.generateRecipes(matInfo);
		RecipeGen_Plates.generateRecipes(matInfo);
		RecipeGen_ShapedCrafting.generateRecipes(matInfo);

	}
	public static void generateNuclearMaterial(final Material matInfo){	
		generateNuclearMaterial(matInfo, true);
	}
	
	public static void generateNuclearMaterial(final Material matInfo, boolean generatePlates){	
		String unlocalizedName = matInfo.getUnlocalizedName();
		String materialName = matInfo.getLocalizedName();
		short[] C = matInfo.getRGBA();
		int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);

		int sRadiation = 0;
		if (matInfo.vRadioationLevel != 0){
			sRadiation = matInfo.vRadioationLevel;
		}	
		
		Item temp;
		Block tempBlock;
		
		tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
		temp = new BaseItemIngot(matInfo);
		temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", 3, sRadiation);
		temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", 2, sRadiation);
		temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", 1, sRadiation);
		temp = new BaseItemNugget(matInfo);
		temp = new BaseItemPlate(matInfo);
		temp = new BaseItemPlateDouble(matInfo);
		
		RecipeGen_Plates.generateRecipes(matInfo);
		RecipeGen_Extruder.generateRecipes(matInfo);
		RecipeGen_ShapedCrafting.generateRecipes(matInfo);
		RecipeGen_Fluids.generateRecipes(matInfo);
		RecipeGen_Assembler.generateRecipes(matInfo);
		RecipeGen_DustGeneration.generateRecipes(matInfo, true);
	}


}
