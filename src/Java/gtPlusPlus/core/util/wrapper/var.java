package gtPlusPlus.core.util.wrapper;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class var{
		
		private ItemStack temp = null;
		private String sanitizedName;
		private String fqrn;
		
		public var(String o){
			String t = sanitize('<', o);
			String t2 = sanitize('>', t);
			sanitizedName = t2;
			o = sanitize('"', t2);
			fqrn = o;			
		}
		
		private String sanitize(char token, String input){
			 for (int i=0;i<input.length();i++) {
		            if (input.charAt(i) == token) {
		            input = input.replace(input.charAt(i), ' ');
		                Utils.LOG_WARNING("MATCH FOUND");
		            }
		            input = input.replaceAll(" ", "");
			 }
			String output = input;
			return output;			
		}
		
		public String getFQRN(){
			String s = fqrn;
			return s;
		}
		
		public String getsanitizedName(){
			String s = sanitizedName;
			return s;
		}
		
		private ItemStack getOreDictStack(int stackSize){
			ItemStack v = ItemUtils.getItemStack(sanitizedName, stackSize);
			return v;
		}
		
		public ItemStack getStack(int stackSize){
			String oreDict = "ore:";
			if (fqrn.toLowerCase().contains(oreDict.toLowerCase())){
				ItemStack v = getOreDictStack(stackSize);
				return v;
			}
			String[] fqrnSplit = fqrn.split(":");
			String meta = "0";
			try {
			if(fqrnSplit[2] != null){meta = fqrnSplit[2];}
			temp = ItemUtils.getItemStackWithMeta(LoadedMods.MiscUtils, fqrn, fqrnSplit[1], Integer.parseInt(meta), stackSize);
			}
			catch (ArrayIndexOutOfBoundsException a){
				temp = ItemUtils.getItemStackWithMeta(LoadedMods.MiscUtils, fqrn, fqrnSplit[1], Integer.parseInt(meta), stackSize);
			}
			return temp;			
		}		
		
	}