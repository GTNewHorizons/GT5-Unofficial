package miscutil.core.util.item;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.base.BasicSpawnEgg;
import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.core.util.wrapper.var;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
		try {
			ItemStack r = new ItemStack(x, 1);
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

}
