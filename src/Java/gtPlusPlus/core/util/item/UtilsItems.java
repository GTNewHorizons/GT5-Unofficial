package gtPlusPlus.core.util.item;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BasicSpawnEgg;
import gtPlusPlus.core.item.base.bolts.BaseItemBolt;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.dusts.BaseItemDustUnique;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemCentidust;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemDecidust;
import gtPlusPlus.core.item.base.gears.BaseItemGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.ingots.BaseItemIngotHot;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.base.rings.BaseItemRing;
import gtPlusPlus.core.item.base.rods.BaseItemRod;
import gtPlusPlus.core.item.base.rods.BaseItemRodLong;
import gtPlusPlus.core.item.base.rotors.BaseItemRotor;
import gtPlusPlus.core.item.base.screws.BaseItemScrew;
import gtPlusPlus.core.item.tool.staballoy.MultiPickaxeBase;
import gtPlusPlus.core.item.tool.staballoy.MultiSpadeBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.wrapper.var;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_DustGeneration;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Extruder;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Plates;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_ShapedCrafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class UtilsItems {

	public static ItemStack getSimpleStack(Item x){
		return getSimpleStack(x, 1);
	}
	public static ItemStack getSimpleStack(Item x, int i){
		try {
			ItemStack r = new ItemStack(x, i);
			return r;
		} catch(Throwable e){
			return null;
		}
	}
	public static ItemStack getSimpleStack(ItemStack x, int i){
		try {
			ItemStack r = x.copy();
			r.stackSize = i;
			return r;
		} catch(Throwable e){
			return null;
		}
	}

	public static ItemStack getIC2Cell(String S){
		ItemStack moreTemp = UtilsItems.getItemStackOfAmountFromOreDictNoBroken("cell"+S, 1);

		if (moreTemp == null){
			int cellID = 0;
			ItemStack temp =GT_ModHandler.getModItem("IC2", "itemCellEmpty", 1L, cellID);
			return temp != null ? temp : null;
		}

		return moreTemp;
	}

	public static ItemStack getIC2Cell(int meta){
		ItemStack temp = GT_ModHandler.getModItem("IC2", "itemCellEmpty", 1L, meta);
		return temp != null ? temp : null;
	}


	public static void getItemForOreDict(String FQRN, String oreDictName, String itemName, int meta){
		try {
			Item em = null;			
			Item em1 = getItem(FQRN);
			Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
			if (em1 != null){
				em = em1;
			}
			if (em != null){

				ItemStack metaStack = new ItemStack(em,1,meta);
				GT_OreDictUnificator.registerOre(oreDictName, metaStack);

				/*ItemStack itemStackWithMeta = new ItemStack(em,1,meta);
				GT_OreDictUnificator.registerOre(oreDictName, new ItemStack(itemStackWithMeta.getItem()));*/
			}
		} catch (NullPointerException e) {
			Utils.LOG_ERROR(itemName+" not found. [NULL]");
		}
	}

	public static void addItemToOreDictionary(ItemStack stack, String oreDictName){
		try {
			GT_OreDictUnificator.registerOre(oreDictName, stack);
		} catch (NullPointerException e) {
			Utils.LOG_ERROR(stack.getDisplayName()+" not registered. [NULL]");
		}
	}

	@SuppressWarnings("unused")
	public static ItemStack getItemStackWithMeta(boolean MOD, String FQRN, String itemName, int meta, int itemstackSize){
		if (MOD){
			try {
				Item em = null;			
				Item em1 = getItem(FQRN);
				Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
				if (em1 != null){
					if (null == em){
						em = em1;
					}
					if (em != null){
						ItemStack metaStack = new ItemStack(em,itemstackSize,meta);
						return metaStack;
					}
				}
				return null;
			} catch (NullPointerException e) {
				Utils.LOG_ERROR(itemName+" not found. [NULL]");
				return null;
			}	
		}
		return null;
	}

	@SuppressWarnings("unused")
	public static ItemStack simpleMetaStack(String FQRN, int meta, int itemstackSize){		
		try {
			Item em = null;			
			Item em1 = getItem(FQRN);
			Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
			if (em1 != null){
				if (null == em){
					em = em1;
				}
				if (em != null){
					ItemStack metaStack = new ItemStack(em,itemstackSize,meta);
					return metaStack;
				}
			}
			return null;
		} catch (NullPointerException e) {
			Utils.LOG_ERROR(FQRN+" not found. [NULL]");
			return null;
		}		
	}

	@SuppressWarnings("unused")
	public static ItemStack simpleMetaStack(Item item, int meta, int itemstackSize){		
		try {
			Item em = item;			
			Item em1 = item;
			Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
			if (em1 != null){
				if (null == em){
					em = em1;
				}
				if (em != null){
					ItemStack metaStack = new ItemStack(em,itemstackSize,meta);
					return metaStack;
				}
			}
			return null;
		} catch (NullPointerException e) {
			Utils.LOG_ERROR(item.getUnlocalizedName()+" not found. [NULL]");
			return null;
		}		
	}

	public static ItemStack getCorrectStacktype(String fqrn, int stackSize){
		String oreDict = "ore:";
		ItemStack temp;
		if (fqrn.toLowerCase().contains(oreDict.toLowerCase())){
			String sanitizedName = fqrn.replace(oreDict, "");
			temp = UtilsItems.getItemStack(sanitizedName, stackSize);
			return temp;
		}
		String[] fqrnSplit = fqrn.split(":");
		if(fqrnSplit[2] == null){fqrnSplit[2] = "0";}			
		temp = UtilsItems.getItemStackWithMeta(LoadedMods.MiscUtils, fqrn, fqrnSplit[1], Integer.parseInt(fqrnSplit[2]), stackSize);
		return temp;			
	}	

	public static ItemStack getCorrectStacktype(Object item_Input, int stackSize) {
		if (item_Input instanceof String){
			return getCorrectStacktype(item_Input, stackSize);
		}
		else if (item_Input instanceof ItemStack){
			return (ItemStack) item_Input;
		}
		if (item_Input instanceof var){
			return ((var) item_Input).getStack(stackSize);
		}
		return null;
	}

	public static Item getItem(String fqrn) // fqrn = fully qualified resource name
	{
		String[] fqrnSplit = fqrn.split(":");
		return GameRegistry.findItem(fqrnSplit[0], fqrnSplit[1]);
	}

	public static ItemStack getItemStack(String fqrn, int Size) // fqrn = fully qualified resource name
	{
		String[] fqrnSplit = fqrn.split(":");
		return GameRegistry.findItemStack(fqrnSplit[0], fqrnSplit[1], Size);
	}

	// TODO
	/*public static FluidStack getFluidStack(Materials m, int Size) // fqrn = fully qualified resource name
	{
		String[] fqrnSplit = fqrn.split(":");

		FluidStack x = (FluidStack) "Materials."+m+".getFluid"(Size);

		return GameRegistry.findItemStack(fqrnSplit[0], fqrnSplit[1], Size);
	}*/
	
	
	public static void generateSpawnEgg(String entityModID, String parSpawnName, int colourEgg, int colourOverlay){
		Item itemSpawnEgg = new BasicSpawnEgg(entityModID, parSpawnName, colourEgg, colourOverlay).setUnlocalizedName("spawn_egg_"+parSpawnName.toLowerCase()).setTextureName(CORE.MODID+":spawn_egg");
		GameRegistry.registerItem(itemSpawnEgg, "spawnEgg"+parSpawnName);
	}


	public static ItemStack[] validItemsForOreDict(String oredictName){
		List<?> validNames = MaterialUtils.oreDictValuesForEntry(oredictName);
		ItemStack[] inputs = null;
		for (int i=0; i<validNames.size();i++){
			inputs[i] = (ItemStack) validNames.get(i);
		}
		return inputs;		
	}

	public static ItemStack getItemStackOfAmountFromOreDict(String oredictName, int amount){
		ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
		if (!oreDictList.isEmpty()){
			ItemStack returnValue = oreDictList.get(0).copy();
			returnValue.stackSize = amount;
			return returnValue;
		}
		return getSimpleStack(ModItems.AAA_Broken, amount);
	}

	public static ItemStack getItemStackOfAmountFromOreDictNoBroken(String oredictName, int amount){
		ItemStack returnValue = getItemStackOfAmountFromOreDict(oredictName, amount);

		if (returnValue.getItem().getClass() != ModItems.AAA_Broken.getClass() || returnValue.getItem() != ModItems.AAA_Broken){		
			return returnValue;
		}
		Utils.LOG_INFO(oredictName+" was not valid.");	
		return null;
	}

	public static ItemStack getItemStackOfAmountFromOreDictNoBrokenExcluding(String excludeModName, String oredictName, int amount){
		ItemStack returnValue = getItemStackOfAmountFromOreDict(oredictName, amount);

		if (returnValue.getItem().getClass() != ModItems.AAA_Broken.getClass() || returnValue.getItem() != ModItems.AAA_Broken){	
			if (returnValue.getClass().toString().toLowerCase().contains(excludeModName.toLowerCase())){
				ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
				if (!oreDictList.isEmpty()){
					returnValue = oreDictList.get(1).copy();
					returnValue.stackSize = amount;
					return returnValue;
				}
			}	
			else {
				ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
				if (!oreDictList.isEmpty()){
					returnValue = oreDictList.get(1).copy();
					returnValue.stackSize = amount;
					return returnValue;
				}
			}
			return returnValue;
		}
		Utils.LOG_INFO(oredictName+" was not valid.");	
		return null;
	}

	public static void generateItemsFromMaterial(Material matInfo){

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
		if (isRadioactive(materialName)){
			sRadiation = getRadioactivityLevel(materialName);
		}

		if (sRadiation >= 1){
			Item temp;
			Block tempBlock;
			tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
			temp = new BaseItemIngot("itemIngot"+unlocalizedName, materialName, Colour, sRadiation);

			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);

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
				temp = new BaseItemIngotHot("itemHotIngot"+unlocalizedName, materialName, UtilsItems.getSimpleStack(tempIngot, 1), materialTier);
			}
			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);

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

		FluidUtils.generateFluid(matInfo, 1);

	}

	public static Item[] generateDusts(String unlocalizedName, String materialName, int materialTier, Material matInfo, int Colour){
		int radioactive = getRadioactivityLevel(materialName);
		Item[] output = {
				new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, radioactive),
				new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, radioactive),
				new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, radioactive)};
		return output;
	}

	public static Item[] generateSpecialUseDusts(String unlocalizedName, String materialName, int Colour){
		Item[] output = {
				new BaseItemDustUnique("itemDust"+unlocalizedName, materialName, Colour, "Dust"),
				new BaseItemDustUnique("itemDustSmall"+unlocalizedName, materialName, Colour, "Small"),
				new BaseItemDustUnique("itemDustTiny"+unlocalizedName, materialName, Colour, "Tiny")};
		return output;
	}

	public static MultiPickaxeBase generateMultiPick(boolean GT_Durability, Materials material){
		ToolMaterial customMaterial = Utils.generateMaterialFromGT(material);
		Utils.LOG_WARNING("Generating a Multi-Pick out of "+material.name());
		short[] rgb;
		rgb = material.getRGBA();
		int dur = customMaterial.getMaxUses();
		Utils.LOG_WARNING("Determined durability for "+material.name()+" is "+dur);
		if (GT_Durability){
			dur = material.mDurability*100;
			Utils.LOG_WARNING("Using gregtech durability value, "+material.name()+" is now "+dur+".");
		}
		else if (dur <= 0){
			dur = material.mDurability;
			Utils.LOG_WARNING("Determined durability too low, "+material.name()+" is now "+dur+" based on the GT material durability.");
		}

		if (dur <= 0){
			Utils.LOG_WARNING("Still too low, "+material.name()+" will now go unused.");
			return null;
		}

		MultiPickaxeBase MP_Redstone = new MultiPickaxeBase(
				material.name()+" Multipick",
				(customMaterial),
				dur,
				Utils.rgbtoHexValue(rgb[0],rgb[1],rgb[2])
				);

		if (MP_Redstone.isValid){
			return MP_Redstone;
		}		
		return null;

	}

	public static MultiSpadeBase generateMultiShovel(boolean GT_Durability, Materials material){
		ToolMaterial customMaterial = Utils.generateMaterialFromGT(material);
		Utils.LOG_WARNING("Generating a Multi-Shovel out of "+material.name());
		short[] rgb;
		rgb = material.getRGBA();
		int dur = customMaterial.getMaxUses();
		Utils.LOG_WARNING("Determined durability for "+material.name()+" is "+dur);
		if (GT_Durability){
			dur = material.mDurability*100;
			Utils.LOG_WARNING("Using gregtech durability value, "+material.name()+" is now "+dur+".");
		}
		else if (dur <= 0){
			dur = material.mDurability;
			Utils.LOG_WARNING("Determined durability too low, "+material.name()+" is now "+dur+" based on the GT material durability.");
		}

		if (dur <= 0){
			Utils.LOG_WARNING("Still too low, "+material.name()+" will now go unused.");
			return null;
		}

		MultiSpadeBase MP_Redstone = new MultiSpadeBase(
				material.name()+" Multishovel",
				(customMaterial),
				dur,
				Utils.rgbtoHexValue(rgb[0],rgb[1],rgb[2])
				);

		if (MP_Redstone.isValid){
			return MP_Redstone;
		}		
		return null;

	}

	public static BaseItemDecidust generateDecidust(Materials material){
		if (GT_OreDictUnificator.get(OrePrefixes.dust, material, 1L) != null){
			Material placeholder = MaterialUtils.generateMaterialFromGtENUM(material);
			if (placeholder != null)
			generateDecidust(placeholder);						
		}		
		return null;
	}

	public static BaseItemDecidust generateDecidust(Material material){
		if (material.getDust(1) != null && MaterialUtils.hasValidRGBA(material.getRGBA())){			
			BaseItemDecidust Decidust = new BaseItemDecidust(material);
			return Decidust;			
		}
		return null;
	}
	
	public static BaseItemCentidust generateCentidust(Materials material){
		if (GT_OreDictUnificator.get(OrePrefixes.dust, material, 1L) != null){
			Material placeholder = MaterialUtils.generateMaterialFromGtENUM(material);
			if (placeholder != null)
			generateCentidust(placeholder);						
		}		
		return null;
	}

	public static BaseItemCentidust generateCentidust(Material material){
		if (material.getDust(1) != null && MaterialUtils.hasValidRGBA(material.getRGBA())){			
			BaseItemCentidust Centidust = new BaseItemCentidust(material);
			return Centidust;			
		}
		return null;
	}

	public static boolean isRadioactive(String materialName){
		int sRadiation = 0;		
		if (materialName.toLowerCase().contains("uranium")){
			sRadiation = 2;
		}
		else if (materialName.toLowerCase().contains("plutonium")){
			sRadiation = 4;
		}
		else if (materialName.toLowerCase().contains("thorium")){
			sRadiation = 1;
		}
		if (sRadiation >= 1){
			return true;
		}
		return false;
	}

	public static int getRadioactivityLevel(String materialName){
		int sRadiation = 0;		
		if (materialName.toLowerCase().contains("uranium")){
			sRadiation = 2;
		}
		else if (materialName.toLowerCase().contains("plutonium")){
			sRadiation = 4;
		}
		else if (materialName.toLowerCase().contains("thorium")){
			sRadiation = 1;
		}
		return sRadiation;
	}

	public static String getArrayStackNames(ItemStack[] aStack){
		String itemNames = "Item Array: ";
		for (ItemStack alph : aStack){

			if (alph != null){
				String temp = itemNames;
				itemNames = temp + ", " + alph.getDisplayName() + " x" + alph.stackSize;				
			}
			else {
				String temp = itemNames;
				itemNames = temp + ", " + "null" + " x" + "0";
			}
		}
		return itemNames;

	}

	public static String[] getArrayStackNamesAsArray(ItemStack[] aStack){
		String[] itemNames = {};
		int arpos = 0;
		for (ItemStack alph : aStack){
			itemNames[arpos] = alph.getDisplayName();
			arpos++;
		}
		return itemNames;

	}

	public static String getFluidArrayStackNames(FluidStack[] aStack){
		String itemNames = "Fluid Array: ";
		for (FluidStack alph : aStack){
			String temp = itemNames;
			itemNames = temp + ", " + alph.getFluid().getName() + " x" + alph.amount;
		}
		return itemNames;

	}
	
	public static ItemStack getGregtechCircuit(int Meta){
		return UtilsItems.getItemStackWithMeta(LoadedMods.Gregtech, "gregtech:gt.integrated_circuit", "Gregtech Circuit", Meta, 0);
	}

}
