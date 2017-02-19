package gtPlusPlus.core.util.item;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BasicSpawnEgg;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.dusts.BaseItemDustUnique;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemCentidust;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemDecidust;
import gtPlusPlus.core.item.tool.staballoy.MultiPickaxeBase;
import gtPlusPlus.core.item.tool.staballoy.MultiSpadeBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.core.util.wrapper.var;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemUtils {

	public static ItemStack getSimpleStack(Item x){
		return getSimpleStack(x, 1);
	}
	public static ItemStack getSimpleStack(Block x){
		return getSimpleStack(Item.getItemFromBlock(x), 1);
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
		ItemStack moreTemp = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+S, 1);

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
			//Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
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

	public static ItemStack getItemStackWithMeta(boolean MOD, String FQRN, String itemName, int meta, int itemstackSize){
		if (MOD){
			try {
				Item em = null;			
				Item em1 = getItem(FQRN);
				//Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
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

	public static ItemStack simpleMetaStack(String FQRN, int meta, int itemstackSize){		
		try {
			Item em = null;			
			Item em1 = getItem(FQRN);
			//Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
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
			if (item == null){
				return null;
			}			
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
			//Utils.LOG_ERROR(item.getUnlocalizedName()+" not found. [NULL]");
			return null;
		}		
	}

	public static ItemStack getCorrectStacktype(String fqrn, int stackSize){
		String oreDict = "ore:";
		ItemStack temp;
		if (fqrn.toLowerCase().contains(oreDict.toLowerCase())){
			String sanitizedName = fqrn.replace(oreDict, "");
			temp = ItemUtils.getItemStack(sanitizedName, stackSize);
			return temp;
		}
		String[] fqrnSplit = fqrn.split(":");
		String temp1;
		String temp2;
		temp1 = fqrnSplit[1];
		if (fqrnSplit.length < 3){temp2 = "0";}
		else {temp2 = fqrnSplit[2];}
		temp = ItemUtils.getItemStackWithMeta(LoadedMods.MiscUtils, fqrn, temp1, Integer.parseInt(temp2), stackSize);
		return temp;			
	}	

	public static ItemStack getCorrectStacktype(Object item_Input, int stackSize) {
		if (item_Input instanceof String){
			return getItemStackOfAmountFromOreDictNoBroken((String) item_Input, stackSize);
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

	public static void generateSpawnEgg(String entityModID, String parSpawnName, int colourEgg, int colourOverlay){
		Item itemSpawnEgg = new BasicSpawnEgg(entityModID, parSpawnName, colourEgg, colourOverlay).setUnlocalizedName("spawn_egg_"+parSpawnName.toLowerCase()).setTextureName(CORE.MODID+":spawn_egg");
		GameRegistry.registerItem(itemSpawnEgg, "spawnEgg"+parSpawnName);
	}


	public static ItemStack[] validItemsForOreDict(String oredictName){
		List<?> validNames = MaterialUtils.oreDictValuesForEntry(oredictName);
		ItemStack[] inputs = new ItemStack[validNames.size()];
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
		if (CORE.DEBUG){
			Utils.LOG_INFO("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(1));
			Utils.LOG_INFO("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(2));
			Utils.LOG_INFO("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(3));
			Utils.LOG_INFO("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(4));
			Utils.LOG_INFO("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(5));		
		}
		ItemStack returnValue = getItemStackOfAmountFromOreDict(oredictName, amount);
		if (returnValue.getItem().getClass() != ModItems.AAA_Broken.getClass() || returnValue.getItem() != ModItems.AAA_Broken){		
			return returnValue;
		}
		Utils.LOG_INFO(oredictName+" was not valid.");	
		return null;
	}

	public static ItemStack getGregtechDust(Materials material, int amount){
		ItemStack returnValue = GT_OreDictUnificator.get(OrePrefixes.dust, material, 1L);
		if (returnValue != null){
			if (returnValue.getItem().getClass() != ModItems.AAA_Broken.getClass() || returnValue.getItem() != ModItems.AAA_Broken){		
				return returnValue;
			}
		}
		Utils.LOG_INFO(material+" was not valid.");	
		return null;
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

	public static Item[] generateSpecialUseDusts(Material material, boolean onlyLargeDust){
		String materialName = material.getLocalizedName();
		String unlocalizedName = Utils.sanitizeString(materialName);
		int Colour = material.getRgbAsHex();
		Item[] output = null;
		if (onlyLargeDust == false){
			output = new Item[]{
					new BaseItemDustUnique("itemDust"+unlocalizedName, materialName, Colour, "Dust"),
					new BaseItemDustUnique("itemDustSmall"+unlocalizedName, materialName, Colour, "Small"),
					new BaseItemDustUnique("itemDustTiny"+unlocalizedName, materialName, Colour, "Tiny")};
		} else{
			output = new Item[]{
					new BaseItemDustUnique("itemDust"+unlocalizedName, materialName, Colour, "Dust")
			};
		}
		return output;
	}

	public static MultiPickaxeBase generateMultiPick(boolean GT_Durability, Materials material){
		ToolMaterial customMaterial = Utils.generateToolMaterialFromGT(material);
		return generateMultiPick(GT_Durability, customMaterial, material.mDefaultLocalName, material.mDurability, material.mRGBa);
	}

	public static MultiPickaxeBase generateMultiPick(Material material){
		ToolMaterial customMaterial = Utils.generateToolMaterial(material);
		return generateMultiPick(true, customMaterial, material.getLocalizedName(), (int) material.vDurability, material.getRGBA());
	}

	public static MultiPickaxeBase generateMultiPick(boolean GT_Durability, ToolMaterial customMaterial, String name, int durability, short[] rgba){
		Utils.LOG_INFO("Generating a Multi-Pick out of "+name);
		short[] rgb = rgba;
		int dur = customMaterial.getMaxUses();
		Utils.LOG_WARNING("Determined durability for "+name+" is "+dur);
		if (GT_Durability){
			dur = durability*100;
			Utils.LOG_WARNING("Using gregtech durability value, "+name+" is now "+dur+".");
		}
		else if (dur <= 0){
			dur = durability;
			Utils.LOG_WARNING("Determined durability too low, "+name+" is now "+dur+" based on the GT material durability.");
		}
		if (dur <= 0){
			Utils.LOG_WARNING("Still too low, "+name+" will now go unused.");
			return null;
		}
		MultiPickaxeBase MP_Redstone = new MultiPickaxeBase(
				name+" Multipick",
				(customMaterial),
				dur,
				Utils.rgbtoHexValue(rgb[0],rgb[1],rgb[2])
				);

		if (MP_Redstone.isValid){
			return MP_Redstone;
		}	
		Utils.LOG_INFO("Pickaxe was not valid.");
		return null;
	}






	public static MultiSpadeBase generateMultiShovel(boolean GT_Durability, Materials material){
		ToolMaterial customMaterial = Utils.generateToolMaterialFromGT(material);
		return generateMultiShovel(GT_Durability, customMaterial, material.mDefaultLocalName, material.mDurability, material.mRGBa);
	}

	public static MultiSpadeBase generateMultiShovel(Material material){
		ToolMaterial customMaterial = Utils.generateToolMaterial(material);
		return generateMultiShovel(true, customMaterial, material.getLocalizedName(), (int) material.vDurability, material.getRGBA());
	}

	public static MultiSpadeBase generateMultiShovel(boolean GT_Durability, ToolMaterial customMaterial, String name, int durability, short[] rgba){
		Utils.LOG_WARNING("Generating a Multi-Spade out of "+name);
		short[] rgb = rgba;
		int dur = customMaterial.getMaxUses();
		Utils.LOG_WARNING("Determined durability for "+name+" is "+dur);
		if (GT_Durability){
			dur = durability*100;
			Utils.LOG_WARNING("Using gregtech durability value, "+name+" is now "+dur+".");
		}
		else if (dur <= 0){
			dur = durability;
			Utils.LOG_WARNING("Determined durability too low, "+name+" is now "+dur+" based on the GT material durability.");
		}
		if (dur <= 0){
			Utils.LOG_WARNING("Still too low, "+name+" will now go unused.");
			return null;
		}
		MultiSpadeBase MP_Redstone = new MultiSpadeBase(
				name+" Multispade",
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
			if (placeholder != null) {
				generateDecidust(placeholder);
			}						
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
			if (placeholder != null) {
				generateCentidust(placeholder);
			}						
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
		return ItemUtils.getItemStackWithMeta(LoadedMods.Gregtech, "gregtech:gt.integrated_circuit", "Gregtech Circuit", Meta, 0);
	}
	public static ItemStack[] getBlockDrops(ArrayList<ItemStack> blockDrops) {
		if (blockDrops == null){
			return null;
		}
		if (blockDrops.isEmpty()){
			return null;
		}
		ItemStack[] outputs = new ItemStack[blockDrops.size()];
		short forCounter = 0;
		for (ItemStack I : blockDrops){
			outputs[forCounter] = I;
		}
		return outputs;
	}

}
