package miscutil.core.util.item;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_OreDictUnificator;

import java.util.ArrayList;

import miscutil.core.block.base.BasicBlock.BlockTypes;
import miscutil.core.block.base.BlockBaseModular;
import miscutil.core.item.ModItems;
import miscutil.core.item.base.BasicSpawnEgg;
import miscutil.core.item.base.bolts.BaseItemBolt;
import miscutil.core.item.base.dusts.BaseItemDust;
import miscutil.core.item.base.gears.BaseItemGear;
import miscutil.core.item.base.ingots.BaseItemIngot;
import miscutil.core.item.base.ingots.BaseItemIngotHot;
import miscutil.core.item.base.plates.BaseItemPlate;
import miscutil.core.item.base.rings.BaseItemRing;
import miscutil.core.item.base.rods.BaseItemRod;
import miscutil.core.item.base.rods.BaseItemRodLong;
import miscutil.core.item.base.rotors.BaseItemRotor;
import miscutil.core.item.base.screws.BaseItemScrew;
import miscutil.core.item.tool.staballoy.MultiPickaxeBase;
import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.lib.MaterialInfo;
import miscutil.core.util.Utils;
import miscutil.core.util.wrapper.var;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class UtilsItems {

	public static ItemStack getItemStackOfItem(Boolean modToCheck, String mod_itemname_meta){
		if (modToCheck){
			try{
				Item em = null;

				Item em1 = getItem(mod_itemname_meta);
				Utils.LOG_WARNING("Found: "+em1.toString());
				if (em1 != null){
					em = em1;
				}						
				if (em != null ){
					ItemStack returnStack = new ItemStack(em,1);				
					return returnStack;
				}
				Utils.LOG_WARNING(mod_itemname_meta+" not found.");
				return null;
			} catch (NullPointerException e) {
				Utils.LOG_ERROR(mod_itemname_meta+" not found. [NULL]");
				return null;
			}
		}
		return null;
	}

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

	public static Item getItemInPlayersHand(){
		Minecraft mc = Minecraft.getMinecraft();
		Item heldItem = null;

		try{heldItem = mc.thePlayer.getHeldItem().getItem();
		}catch(NullPointerException e){return null;}

		if (heldItem != null){
			return heldItem;
		}

		return null;
	}

	public static void generateSpawnEgg(String entityModID, String parSpawnName, int colourEgg, int colourOverlay){
		Item itemSpawnEgg = new BasicSpawnEgg(entityModID, parSpawnName, colourEgg, colourOverlay).setUnlocalizedName("spawn_egg_"+parSpawnName.toLowerCase()).setTextureName(CORE.MODID+":spawn_egg");
		GameRegistry.registerItem(itemSpawnEgg, "spawnEgg"+parSpawnName);
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
	
	public static void generateItemsFromMaterial(String unlocalizedName, String materialName, int materialTier, MaterialInfo matInfo, int Colour, boolean hotIngot){
		if (materialTier > 10 || materialTier <= 0){
			materialTier = 2;
		}
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
			Item temp;
			Block tempBlock;
			tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
			temp = new BaseItemIngot("itemIngot"+unlocalizedName, materialName, Colour, sRadiation);
			
			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", hotIngot, materialTier, sRadiation);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", hotIngot, materialTier, sRadiation);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", hotIngot, materialTier, sRadiation);
			
			temp = new BaseItemPlate("itemPlate"+unlocalizedName, materialName, Colour, materialTier, sRadiation);
			temp = new BaseItemRod("itemRod"+unlocalizedName, materialName, Colour, materialTier, sRadiation);
			temp = new BaseItemRodLong("itemRodLong"+unlocalizedName, materialName, Colour, materialTier, sRadiation);
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
			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", hotIngot, materialTier, sRadiation);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", hotIngot, materialTier, sRadiation);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", hotIngot, materialTier, sRadiation);
			
			temp = new BaseItemPlate("itemPlate"+unlocalizedName, materialName, Colour, materialTier, sRadiation);
			temp = new BaseItemRod("itemRod"+unlocalizedName, materialName, Colour, materialTier, sRadiation);
			temp = new BaseItemRodLong("itemRodLong"+unlocalizedName, materialName, Colour, materialTier, sRadiation);
			temp = new BaseItemRing("itemRing"+unlocalizedName, materialName, Colour, materialTier);
			temp = new BaseItemBolt("itemBolt"+unlocalizedName, materialName, Colour, materialTier);
			temp = new BaseItemScrew("itemScrew"+unlocalizedName, materialName, Colour, materialTier);
			temp = new BaseItemRotor("itemRotor"+unlocalizedName, materialName, Colour);
			temp = new BaseItemGear("itemGear"+unlocalizedName, materialName, Colour, materialTier);
		}
		
	}
	
	public static MultiPickaxeBase generateMultiPick(Materials material){
		ToolMaterial customMaterial = Utils.generateMaterialFromGT(material);
		short[] rgb;
		rgb = material.getRGBA();
		int dur = customMaterial.getMaxUses();
		
		if (dur <= 0){
			dur = material.mDurability;
		}
		
		MultiPickaxeBase MP_Redstone = new MultiPickaxeBase(
				material.name()+" Multipick",
				(customMaterial),
				dur,
				Utils.rgbtoHexValue(rgb[0],rgb[1],rgb[2])
				);
		
		return MP_Redstone;
		
	}
	
	public static String getArrayStackNames(ItemStack[] aStack){
		String itemNames = "Item Array: ";
		for (ItemStack alph : aStack){
			String temp = itemNames;
			itemNames = temp + ", " + alph.getDisplayName() + " x" + alph.stackSize;
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

}
