package gtPlusPlus.core.material;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.item.base.bolts.BaseItemBolt;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.gears.BaseItemGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.ingots.BaseItemIngotHot;
import gtPlusPlus.core.item.base.nugget.BaseItemNugget;
import gtPlusPlus.core.item.base.ore.*;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.base.rings.BaseItemRing;
import gtPlusPlus.core.item.base.rods.BaseItemRod;
import gtPlusPlus.core.item.base.rods.BaseItemRodLong;
import gtPlusPlus.core.item.base.rotors.BaseItemRotor;
import gtPlusPlus.core.item.base.screws.BaseItemScrew;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.loaders.*;

public class MaterialGenerator {

	public static void generate(final Material matInfo){
		generate(matInfo, true);
	}


	public static void generate(final Material matInfo, final boolean generateEverything){
		generate(matInfo, generateEverything, true);
	}

	public static boolean generate(final Material matInfo, final boolean generateEverything, final boolean generateBlastSmelterRecipes){
		try {
			final String unlocalizedName = matInfo.getUnlocalizedName();
			final String materialName = matInfo.getLocalizedName();
			final short[] C = matInfo.getRGBA();
			final int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);
			final boolean hotIngot = matInfo.requiresBlastFurnace();
			int materialTier = matInfo.vTier; //TODO

			if ((materialTier > 10) || (materialTier <= 0)){
				materialTier = 2;
			}

			int sRadiation = 0;
			if (ItemUtils.isRadioactive(materialName) || (matInfo.vRadiationLevel != 0)){
				sRadiation = matInfo.vRadiationLevel;
			}

			if (matInfo.getState() == MaterialState.SOLID){
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
			}
			else if (matInfo.getState() == MaterialState.LIQUID){
				Item temp;
				if (generateEverything == true){
					Block tempBlock;
					tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
				}
				temp = new BaseItemIngot(matInfo);
				temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
				temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
				temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);
				temp = new BaseItemNugget(matInfo);
				temp = new BaseItemPlate(matInfo);
				temp = new BaseItemPlateDouble(matInfo);
			}
			else if (matInfo.getState() == MaterialState.PURE_LIQUID){
				FluidUtils.generateFluidNoPrefix(unlocalizedName,	materialName, matInfo.getMeltingPointK(), C);
				return true;
			}
			else if (matInfo.getState() == MaterialState.ORE){
					
			}

			//Add A jillion Recipes - old code
			RecipeGen_AlloySmelter.generateRecipes(matInfo);
			RecipeGen_Assembler.generateRecipes(matInfo);
			if (generateBlastSmelterRecipes){
				RecipeGen_BlastSmelter.generateARecipe(matInfo);
			}
			RecipeGen_DustGeneration.generateRecipes(matInfo);
			RecipeGen_Extruder.generateRecipes(matInfo);
			RecipeGen_Fluids.generateRecipes(matInfo);
			RecipeGen_Plates.generateRecipes(matInfo);
			RecipeGen_ShapedCrafting.generateRecipes(matInfo);
			RecipeGen_MaterialProcessing.generateRecipes(matInfo);
			new RecipeGen_Recycling(matInfo);
			return true;
			
			} catch (final Throwable t)
		
		{
			Logger.WARNING(""+matInfo.getLocalizedName()+" failed to generate.");
			return false;
		}
	}

	public static void generateDusts(final Material matInfo){
		final String unlocalizedName = matInfo.getUnlocalizedName();
		final String materialName = matInfo.getLocalizedName();
		final short[] C = matInfo.getRGBA();
		final int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);
		int materialTier = matInfo.vTier; //TODO

		if ((materialTier > 10) || (materialTier <= 0)){
			materialTier = 2;
		}

		int sRadiation = 0;
		if (ItemUtils.isRadioactive(materialName) || (matInfo.vRadiationLevel != 0)){
			sRadiation = matInfo.vRadiationLevel;
		}

		if (matInfo.getState() == MaterialState.SOLID){
			Item temp;
			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation, false);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation, false);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation, false);
		}

		//Add A jillion Recipes - old code
		RecipeGen_DustGeneration.addMixerRecipe_Standalone(matInfo);
		RecipeGen_Fluids.generateRecipes(matInfo);
		RecipeGen_MaterialProcessing.generateRecipes(matInfo);
		//RecipeGen_Recycling.generateRecipes(matInfo);
	}

	public static void generateNuclearMaterial(final Material matInfo){
		generateNuclearMaterial(matInfo, true);
	}

	@SuppressWarnings("unused")
	public static void generateNuclearMaterial(final Material matInfo, final boolean generatePlates){
		try {
			final String unlocalizedName = matInfo.getUnlocalizedName();
			final String materialName = matInfo.getLocalizedName();
			final short[] C = matInfo.getRGBA();
			final int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);

			int sRadiation = 0;
			if (matInfo.vRadiationLevel != 0){
				sRadiation = matInfo.vRadiationLevel;
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
			RecipeGen_MaterialProcessing.generateRecipes(matInfo);
			new RecipeGen_Recycling(matInfo);
		} catch (final Throwable t){
			Logger.WARNING(""+matInfo.getLocalizedName()+" failed to generate.");
		}
	}

	@SuppressWarnings("unused")
	public static void generateOreMaterial(final Material matInfo){
		try {
			
			if (matInfo == null){
				Logger.DEBUG_MATERIALS("Invalid Material while constructing null material.");
				return;
			}
			
			final String unlocalizedName = matInfo.getUnlocalizedName();
			final String materialName = matInfo.getLocalizedName();
			final short[] C = matInfo.getRGBA();
			final Integer Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);

			
			if (Colour == null){
				Logger.DEBUG_MATERIALS("Invalid Material while constructing "+materialName+".");
				return;
			}
			
			int sRadiation = 0;
			if (matInfo.vRadiationLevel > 0){
				sRadiation = matInfo.vRadiationLevel;
			}

			Item temp;
			Block tempBlock;
			
			

			tempBlock = new BlockBaseOre(matInfo, BlockTypes.ORE, Colour.intValue());			

			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", matInfo.vTier, sRadiation, false);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", matInfo.vTier, sRadiation, false);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", matInfo.vTier, sRadiation, false);
			temp = new BaseItemCrushedOre(matInfo);
			temp = new BaseItemCentrifugedCrushedOre(matInfo);
			temp = new BaseItemPurifiedCrushedOre(matInfo);
			temp = new BaseItemImpureDust(matInfo);
			temp = new BaseItemPurifiedDust(matInfo);

			RecipeGen_Ore.generateRecipes(matInfo);
			
		} catch (final Throwable t){
			Logger.MATERIALS("[Error] "+(matInfo != null ? matInfo.getLocalizedName() : "Null Material")+" failed to generate.");
			t.printStackTrace();
		}
	}


}
