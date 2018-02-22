package gtPlusPlus.core.util.minecraft;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BasicSpawnEgg;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.dusts.BaseItemDustUnique;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemCentidust;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemDecidust;
import gtPlusPlus.core.item.base.plates.BaseItemPlate_OLD;
import gtPlusPlus.core.item.tool.staballoy.MultiPickaxeBase;
import gtPlusPlus.core.item.tool.staballoy.MultiSpadeBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_DustGeneration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUtils {

	public static ItemStack getSimpleStack(final Item x){
		return getSimpleStack(x, 1);
	}
	public static ItemStack getSimpleStack(final Block x){
		return getSimpleStack(Item.getItemFromBlock(x), 1);
	}
	public static ItemStack getSimpleStack(final Block x, final int i){
		return getSimpleStack(Item.getItemFromBlock(x), i);
	}
	public static ItemStack getSimpleStack(final Item x, final int i){
		try {
			final ItemStack r = new ItemStack(x, i);
			return r.copy();
		} catch(final Throwable e){
			return null;
		}
	}
	public static ItemStack getSimpleStack(final ItemStack x, final int i){
		try {
			final ItemStack r = x.copy();
			r.stackSize = i;
			return r;
		} catch(final Throwable e){
			return null;
		}
	}

	public static final int WILDCARD_VALUE = Short.MAX_VALUE;
	public static ItemStack getWildcardStack(final Item x){
		final ItemStack y = new ItemStack(x, 1, WILDCARD_VALUE);
		return y;
	}


	public static ItemStack getIC2Cell(final String S){
		final ItemStack moreTemp = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+S, 1);

		if (moreTemp == null){
			final int cellID = 0;
			final ItemStack temp =GT_ModHandler.getModItem("IC2", "itemCellEmpty", 1L, cellID);
			return temp != null ? temp : null;
		}

		return moreTemp;
	}

	public static ItemStack getIC2Cell(final int meta){
		final ItemStack temp = GT_ModHandler.getModItem("IC2", "itemCellEmpty", 1L, meta);
		return temp != null ? temp : null;
	}

	public static ItemStack getEmptyCell(){
		return getEmptyCell(1);
	}

	public static ItemStack getEmptyCell(int i){
		final ItemStack temp = GT_ModHandler.getModItem("IC2", "itemCellEmpty", i, 0);
		return temp != null ? temp : null;
	}

	public static void getItemForOreDict(final String FQRN, final String oreDictName, final String itemName, final int meta){
		try {
			Item em = null;
			final Item em1 = getItem(FQRN);
			//Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
			if (em1 != null){
				em = em1;
			}
			if (em != null){

				final ItemStack metaStack = new ItemStack(em,1,meta);
				GT_OreDictUnificator.registerOre(oreDictName, metaStack);

				/*ItemStack itemStackWithMeta = new ItemStack(em,1,meta);
				GT_OreDictUnificator.registerOre(oreDictName, new ItemStack(itemStackWithMeta.getItem()));*/
			}
		} catch (final NullPointerException e) {
			Logger.ERROR(itemName+" not found. [NULL]");
		}
	}

	public static void addItemToOreDictionary(final ItemStack stack, final String oreDictName){
		try {
			GT_OreDictUnificator.registerOre(oreDictName, stack);
		} catch (final NullPointerException e) {
			Logger.ERROR(stack.getDisplayName()+" not registered. [NULL]");
		}
	}

	public static ItemStack getItemStackWithMeta(final boolean MOD, final String FQRN, final String itemName, final int meta, final int itemstackSize){
		if (MOD){
			try {
				Item em = null;
				final Item em1 = getItem(FQRN);
				//Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
				if (em1 != null){
					if (null == em){
						em = em1;
					}
					if (em != null){
						final ItemStack metaStack = new ItemStack(em,itemstackSize,meta);
						return metaStack;
					}
				}
				return null;
			} catch (final NullPointerException e) {
				Logger.ERROR(itemName+" not found. [NULL]");
				return null;
			}
		}
		return null;
	}

	public static ItemStack simpleMetaStack(final String FQRN, final int meta, final int itemstackSize){
		try {
			Item em = null;
			final Item em1 = getItem(FQRN);
			//Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
			if (em1 != null){
				if (null == em){
					em = em1;
				}
				if (em != null){
					final ItemStack metaStack = new ItemStack(em,itemstackSize,meta);
					return metaStack;
				}
			}
			return null;
		} catch (final NullPointerException e) {
			Logger.ERROR(FQRN+" not found. [NULL]");
			return null;
		}
	}

	@SuppressWarnings("unused")
	public static ItemStack simpleMetaStack(final Item item, final int meta, final int size){
		try {
			if (item == null){
				return null;
			}
			Item em = item;
			final Item em1 = item;
			Logger.WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
			if (em1 != null){
				if (null == em){
					em = em1;
				}
				if (em != null){
					final ItemStack metaStack = new ItemStack(em,size,meta);
					return metaStack;
				}
			}
			return null;
		} catch (final NullPointerException e) {
			//Utils.LOG_ERROR(item.getUnlocalizedName()+" not found. [NULL]");
			return null;
		}
	}

	public static ItemStack simpleMetaStack(final Block block, final int meta, final int size) {
		return simpleMetaStack(Item.getItemFromBlock(block), meta, size);
	}

	public static ItemStack getCorrectStacktype(final String fqrn, final int stackSize){
		final String oreDict = "ore:";
		ItemStack temp;
		if (fqrn.toLowerCase().contains(oreDict.toLowerCase())){
			final String sanitizedName = fqrn.replace(oreDict, "");
			temp = ItemUtils.getItemStack(sanitizedName, stackSize);
			return temp;
		}
		final String[] fqrnSplit = fqrn.split(":");
		String temp1;
		String temp2;
		temp1 = fqrnSplit[1];
		if (fqrnSplit.length < 3){temp2 = "0";}
		else {temp2 = fqrnSplit[2];}
		temp = ItemUtils.getItemStackWithMeta(LoadedMods.MiscUtils, fqrn, temp1, Integer.parseInt(temp2), stackSize);
		return temp;
	}

	public static ItemStack getCorrectStacktype(final Object item_Input, final int stackSize) {
		if (item_Input instanceof String){
			return getItemStackOfAmountFromOreDictNoBroken((String) item_Input, stackSize);
		}
		else if (item_Input instanceof ItemStack){
			return (ItemStack) item_Input;
		}
		return null;
	}

	public static Item getItem(final String fqrn) // fqrn = fully qualified resource name
	{
		final String[] fqrnSplit = fqrn.split(":");
		return GameRegistry.findItem(fqrnSplit[0], fqrnSplit[1]);
	}

	public static ItemStack getItemStack(final String fqrn, final int Size) // fqrn = fully qualified resource name
	{
		final String[] fqrnSplit = fqrn.split(":");
		return GameRegistry.findItemStack(fqrnSplit[0], fqrnSplit[1], Size);
	}

	public static void generateSpawnEgg(final String entityModID, final String parSpawnName, final int colourEgg, final int colourOverlay){
		final Item itemSpawnEgg = new BasicSpawnEgg(entityModID, parSpawnName, colourEgg, colourOverlay).setUnlocalizedName("spawn_egg_"+parSpawnName.toLowerCase()).setTextureName(CORE.MODID+":spawn_egg");
		GameRegistry.registerItem(itemSpawnEgg, "spawnEgg"+parSpawnName);
	}


	public static ItemStack[] validItemsForOreDict(final String oredictName){
		final List<?> validNames = MaterialUtils.oreDictValuesForEntry(oredictName);
		final ItemStack[] inputs = new ItemStack[validNames.size()];
		for (int i=0; i<validNames.size();i++){
			inputs[i] = (ItemStack) validNames.get(i);
		}
		return inputs;
	}

	public static ItemStack getItemStackOfAmountFromOreDict(final String oredictName, final int amount){

		String mTemp = oredictName;

		//Banned Materials and replacements for GT5.8 compat.		
		if (oredictName.toLowerCase().contains("rutile")){
			mTemp = oredictName.replace("Rutile", "Titanium");
		}
		if (oredictName.toLowerCase().contains("vanadiumsteel")){
			mTemp = oredictName.replace("VanadiumSteel", "StainlessSteel");
		}
		
		ItemStack mTempItem = getItemStackOfAmountFromOreDictNoBroken(mTemp, amount);
		if (mTempItem != null) {
			return mTempItem;
		}

		final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(mTemp);
		if (!oreDictList.isEmpty()){
			final ItemStack returnValue = oreDictList.get(0).copy();
			returnValue.stackSize = amount;
			return returnValue;
		}
		return getSimpleStack(ModItems.AAA_Broken, amount);
	}

	public static ItemStack getItemStackOfAmountFromOreDictNoBroken(final String oredictName, final int amount){
		if (CORE.DEBUG){
			Logger.WARNING("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(1));
			Logger.WARNING("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(2));
			Logger.WARNING("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(3));
			Logger.WARNING("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(4));
			Logger.WARNING("Looking up: "+oredictName+" - from method: "+ReflectionUtils.getMethodName(5));
		}
		try{

			//Adds a check to grab dusts using GT methodology if possible.
			ItemStack returnValue = null;
			if (oredictName.toLowerCase().contains("dust")){
				final String MaterialName = oredictName.toLowerCase().replace("dust", "");
				final Materials m = Materials.get(MaterialName);
				returnValue = getGregtechDust(m, amount);
				if (returnValue != null){
					return returnValue;
				}
				else {
					returnValue = getGregtechDust(oredictName, amount);
					if (returnValue != null) {
						return returnValue;						
					}
				}
			}

			if (returnValue == null){
				returnValue = getItemStackOfAmountFromOreDict(oredictName, amount);
				if (returnValue != null){
					if ((returnValue.getItem().getClass() != ModItems.AAA_Broken.getClass()) || (returnValue.getItem() != ModItems.AAA_Broken)){						
						return returnValue.copy();
					}
				}
			}
			Logger.WARNING(oredictName+" was not valid.");
			return null;
		}
		catch (final Throwable t){
			return null;
		}
	}

	public static ItemStack getGregtechDust(final Materials material, final int amount){
		final ItemStack returnValue = GT_OreDictUnificator.get(OrePrefixes.dust, material, 1L);
		if (returnValue != null){
			if ((returnValue.getItem().getClass() != ModItems.AAA_Broken.getClass()) || (returnValue.getItem() != ModItems.AAA_Broken)){
				return returnValue.copy();
			}
		}
		Logger.WARNING(material+" was not valid.");
		return null;
	}

	public static Item[] generateDusts(final String unlocalizedName, final String materialName, final int materialTier, final Material matInfo, final int Colour){
		final int radioactive = getRadioactivityLevel(materialName);
		final Item[] output = {
				new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, radioactive),
				new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, radioactive),
				new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, radioactive)};
		return output;
	}

	//NullFormula
	public static Item[] generateSpecialUseDusts(final String unlocalizedName, final String materialName, final int Colour){
		return generateSpecialUseDusts(unlocalizedName, materialName, "NullFormula", Colour);
	}

	public static Item[] generateSpecialUseDusts(final String unlocalizedName, final String materialName, String mChemForm, final int Colour){
		final Item[] output = {
				new BaseItemDustUnique("itemDust"+unlocalizedName, materialName, mChemForm, Colour, "Dust"),
				new BaseItemDustUnique("itemDustSmall"+unlocalizedName, materialName, mChemForm, Colour, "Small"),
				new BaseItemDustUnique("itemDustTiny"+unlocalizedName, materialName, mChemForm, Colour, "Tiny")};
		return output;
	}

	public static Item generateSpecialUsePlate(final String internalName, final String displayName, final short[] rgb, final int radioactivity){
		return generateSpecialUsePlate(internalName, displayName, Utils.rgbtoHexValue(rgb[0], rgb[1], rgb[2]), radioactivity);
	}

	public static Item generateSpecialUsePlate(final String internalName, final String displayName, final String mFormula, final short[] rgb, final int radioactivity){
		return generateSpecialUsePlate(internalName, displayName, mFormula, Utils.rgbtoHexValue(rgb[0], rgb[1], rgb[2]), radioactivity);
	}

	public static Item generateSpecialUsePlate(final String internalName, final String displayName, final int rgb, final int radioactivity){
		return new BaseItemPlate_OLD(internalName, displayName, rgb, radioactivity);
	}

	public static Item generateSpecialUsePlate(final String internalName, final String displayName, final String mFormula, final int rgb, final int radioactivity){
		return new BaseItemPlate_OLD(internalName, displayName, mFormula, rgb, radioactivity);
	}

	public static Item[] generateSpecialUseDusts(final Material material, final boolean onlyLargeDust){
		final String materialName = material.getLocalizedName();
		final String unlocalizedName = Utils.sanitizeString(materialName);
		final int Colour = material.getRgbAsHex();
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

		RecipeGen_DustGeneration.generateRecipes(material);

		return output;
	}

	public static MultiPickaxeBase generateMultiPick(final boolean GT_Durability, final Materials material){
		final ToolMaterial customMaterial = Utils.generateToolMaterialFromGT(material);
		final int enchantLevel = material.mEnchantmentToolsLevel;
		final Object enchant = new Pair(material.mEnchantmentTools, enchantLevel);
		return generateMultiPick(GT_Durability, customMaterial, material.mDefaultLocalName, material.mDurability, material.mRGBa, enchant);
	}

	public static MultiPickaxeBase generateMultiPick(final Material material){
		final ToolMaterial customMaterial = Utils.generateToolMaterial(material);
		return generateMultiPick(true, customMaterial, material.getLocalizedName(), (int) material.vDurability, material.getRGBA(), null);
	}

	public static MultiPickaxeBase generateMultiPick(final boolean GT_Durability, final ToolMaterial customMaterial, final String name, final int durability, final short[] rgba, final Object enchantment){
		Logger.WARNING("Generating a Multi-Pick out of "+name);
		final short[] rgb = rgba;
		int dur = customMaterial.getMaxUses();
		Logger.WARNING("Determined durability for "+name+" is "+dur);
		if (GT_Durability){
			dur = durability*100;
			Logger.WARNING("Using gregtech durability value, "+name+" is now "+dur+".");
		}
		else if (dur <= 0){
			dur = durability;
			Logger.WARNING("Determined durability too low, "+name+" is now "+dur+" based on the GT material durability.");
		}
		if (dur <= 0){
			Logger.WARNING("Still too low, "+name+" will now go unused.");
			return null;
		}

		Object enchant;
		if (enchantment != null){
			if (enchantment instanceof Pair){
				enchant = enchantment;
			}
		}
		else {
			enchant = null;
		}

		final MultiPickaxeBase MP_Redstone = new MultiPickaxeBase(
				name+" Multipick",
				(customMaterial),
				dur,
				Utils.rgbtoHexValue(rgb[0],rgb[1],rgb[2]),
				enchantment);

		if (MP_Redstone.isValid){
			return MP_Redstone;
		}
		Logger.WARNING("Pickaxe was not valid.");
		return null;
	}






	public static MultiSpadeBase generateMultiShovel(final boolean GT_Durability, final Materials material){
		final ToolMaterial customMaterial = Utils.generateToolMaterialFromGT(material);
		return generateMultiShovel(GT_Durability, customMaterial, material.mDefaultLocalName, material.mDurability, material.mRGBa);
	}

	public static MultiSpadeBase generateMultiShovel(final Material material){
		final ToolMaterial customMaterial = Utils.generateToolMaterial(material);
		return generateMultiShovel(true, customMaterial, material.getLocalizedName(), (int) material.vDurability, material.getRGBA());
	}

	public static MultiSpadeBase generateMultiShovel(final boolean GT_Durability, final ToolMaterial customMaterial, final String name, final int durability, final short[] rgba){
		Logger.WARNING("Generating a Multi-Spade out of "+name);
		final short[] rgb = rgba;
		int dur = customMaterial.getMaxUses();
		Logger.WARNING("Determined durability for "+name+" is "+dur);
		if (GT_Durability){
			dur = durability*100;
			Logger.WARNING("Using gregtech durability value, "+name+" is now "+dur+".");
		}
		else if (dur <= 0){
			dur = durability;
			Logger.WARNING("Determined durability too low, "+name+" is now "+dur+" based on the GT material durability.");
		}
		if (dur <= 0){
			Logger.WARNING("Still too low, "+name+" will now go unused.");
			return null;
		}
		final MultiSpadeBase MP_Redstone = new MultiSpadeBase(
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












	public static BaseItemDecidust generateDecidust(final Materials material){
		if (GT_OreDictUnificator.get(OrePrefixes.dust, material, 1L) != null){
			final Material placeholder = MaterialUtils.generateMaterialFromGtENUM(material);
			if (placeholder != null) {
				generateDecidust(placeholder);
			}
		}
		return null;
	}

	public static BaseItemDecidust generateDecidust(final Material material){
		if ((material.getDust(1) != null) && MaterialUtils.hasValidRGBA(material.getRGBA())){
			final BaseItemDecidust Decidust = new BaseItemDecidust(material);
			return Decidust;
		}
		return null;
	}

	public static BaseItemCentidust generateCentidust(final Materials material){
		if (GT_OreDictUnificator.get(OrePrefixes.dust, material, 1L) != null){
			final Material placeholder = MaterialUtils.generateMaterialFromGtENUM(material);
			if (placeholder != null) {
				generateCentidust(placeholder);
			}
		}
		return null;
	}

	public static BaseItemCentidust generateCentidust(final Material material){
		if ((material.getDust(1) != null) && MaterialUtils.hasValidRGBA(material.getRGBA())){
			final BaseItemCentidust Centidust = new BaseItemCentidust(material);
			return Centidust;
		}
		return null;
	}

	public static boolean isRadioactive(final String materialName){
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

	public static int getRadioactivityLevel(final String materialName){
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

	public static String getArrayStackNames(final ItemStack[] aStack){
		String itemNames = "Item Array: ";
		for (final ItemStack alph : aStack){

			if (alph != null){
				final String temp = itemNames;
				itemNames = temp + ", " + alph.getDisplayName() + " x" + alph.stackSize;
			}
			else {
				final String temp = itemNames;
				itemNames = temp + ", " + "null" + " x" + "0";
			}
		}
		return itemNames;

	}

	public static String[] getArrayStackNamesAsArray(final ItemStack[] aStack){
		final String[] itemNames = {};
		int arpos = 0;
		for (final ItemStack alph : aStack){
			itemNames[arpos] = alph.getDisplayName();
			arpos++;
		}
		return itemNames;

	}

	public static String getFluidArrayStackNames(final FluidStack[] aStack){
		String itemNames = "Fluid Array: ";
		for (final FluidStack alph : aStack){
			final String temp = itemNames;
			itemNames = temp + ", " + alph.getFluid().getName() + " x" + alph.amount;
		}
		return itemNames;

	}

	public static ItemStack getGregtechCircuit(final int Meta){
		return ItemUtils.getItemStackWithMeta(LoadedMods.Gregtech, "gregtech:gt.integrated_circuit", "Gregtech Circuit", Meta, 0);
	}
	public static ItemStack[] getBlockDrops(final ArrayList<ItemStack> blockDrops) {
		if (blockDrops == null){
			return null;
		}
		if (blockDrops.isEmpty()){
			return null;
		}
		final ItemStack[] outputs = new ItemStack[blockDrops.size()];
		short forCounter = 0;
		for (final ItemStack I : blockDrops){
			outputs[forCounter++] = I;
		}
		return outputs;
	}

	private static String getModId(final Item item) {
		try {
			final GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(item);
			final String modname = (id.modId == null ? id.name : id.modId);
			return (id == null) || id.modId.equals("") ? "minecraft" : modname;
		} catch (final Throwable t){
			try {
				final UniqueIdentifier t2 = GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(item));
				final String modname = (t2.modId == null ? t2.name : t2.modId);
				return (t2 == null) || t2.modId.equals("") ? "minecraft" : modname;
			}
			catch (final Throwable t3){
				return "bad modid";
			}
		}
	}

	public static String getModId(final ItemStack key) {
		return getModId(key.getItem());
	}

	//Take 2 - GT/GT++ Dusts
	public static ItemStack getGregtechDust(final String oredictName, final int amount){
		final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
		if (!oreDictList.isEmpty()){
			ItemStack returnvalue;
			for (int xrc=0;xrc<oreDictList.size();xrc++){
				final String modid = getModId(oreDictList.get(xrc).getItem());
				if (modid.equals("gregtech") || modid.equals(CORE.MODID)){
					returnvalue = oreDictList.get(xrc).copy();
					returnvalue.stackSize = amount;
					return returnvalue;
				}
			}
		}
		return getNonTinkersDust(oredictName, amount);
	}

	//Anything But Tinkers Dust
	public static ItemStack getNonTinkersDust(final String oredictName, final int amount){
		final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
		if (!oreDictList.isEmpty()){
			ItemStack returnvalue;
			for (int xrc=0;xrc<oreDictList.size();xrc++){
				final String modid = getModId(oreDictList.get(xrc).getItem());
				if (!modid.equals("tconstruct")){
					returnvalue = oreDictList.get(xrc).copy();
					returnvalue.stackSize = amount;
					return returnvalue;
				}
			}
		}
		//If only Tinkers dust exists, bow down and just use it.
		return getItemStackOfAmountFromOreDictNoBroken(oredictName, amount);
	}
	public static ItemStack getGregtechOreStack(OrePrefixes mPrefix, Materials mMat, int mAmount) {

		String mName = MaterialUtils.getMaterialName(mMat);

		String mItemName = mPrefix.name()+mName;
		//Utils.LOG_INFO("[Component Maker] Trying to get "+mItemName+".");
		ItemStack gregstack = ItemUtils.getItemStackOfAmountFromOreDict(mItemName, mAmount);		
		if (gregstack == null){
			//Utils.LOG_INFO("[Component Maker] Failed to get "+mItemName+".");
			return null;
		}	
		//Utils.LOG_INFO("[Component Maker] Found "+mItemName+".");
		return (gregstack);
	}
	

	public static ItemStack getOrePrefixStack(OrePrefixes mPrefix, Material mMat, int mAmount) {

		String mName = Utils.sanitizeString(mMat.getLocalizedName());

		String mItemName = mPrefix.name()+mName;
		ItemStack gregstack = ItemUtils.getItemStackOfAmountFromOreDict(mItemName, mAmount);		
		if (gregstack == null){
			return null;
		}	
		return (gregstack);
	}

	public static ItemStack[] getStackOfAllOreDictGroup(String oredictname){
		final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictname);
		if (!oreDictList.isEmpty()){
			final ItemStack[] returnValues = new ItemStack[oreDictList.size()];			
			for (int i=0;i<oreDictList.size();i++){
				if (oreDictList.get(i) != null){
					returnValues[i] = oreDictList.get(i);					
				}
			}			
			return returnValues.length>0 ? returnValues : null;
		}
		else {
			return null;
		}
	}


}
