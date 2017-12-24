package gtPlusPlus.core.util.wrapper;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class var{

	private ItemStack temp = null;
	private final String sanitizedName;
	private final String fqrn;

	public var(String o){
		final String t = this.sanitize('<', o);
		final String t2 = this.sanitize('>', t);
		this.sanitizedName = t2;
		o = this.sanitize('"', t2);
		this.fqrn = o;
	}

	private String sanitize(final char token, String input){
		for (int i=0;i<input.length();i++) {
			if (input.charAt(i) == token) {
				input = input.replace(input.charAt(i), ' ');
				Logger.WARNING("MATCH FOUND");
			}
			input = input.replaceAll(" ", "");
		}
		final String output = input;
		return output;
	}

	public String getFQRN(){
		final String s = this.fqrn;
		return s;
	}

	public String getsanitizedName(){
		final String s = this.sanitizedName;
		return s;
	}

	private ItemStack getOreDictStack(final int stackSize){
		final ItemStack v = ItemUtils.getItemStack(this.sanitizedName, stackSize);
		return v;
	}

	public ItemStack getStack(final int stackSize){
		final String oreDict = "ore:";
		if (this.fqrn.toLowerCase().contains(oreDict.toLowerCase())){
			final ItemStack v = this.getOreDictStack(stackSize);
			return v;
		}
		final String[] fqrnSplit = this.fqrn.split(":");
		String meta = "0";
		try {
			if(fqrnSplit[2] != null){meta = fqrnSplit[2];}
			this.temp = ItemUtils.getItemStackWithMeta(LoadedMods.MiscUtils, this.fqrn, fqrnSplit[1], Integer.parseInt(meta), stackSize);
		}
		catch (final ArrayIndexOutOfBoundsException a){
			this.temp = ItemUtils.getItemStackWithMeta(LoadedMods.MiscUtils, this.fqrn, fqrnSplit[1], Integer.parseInt(meta), stackSize);
		}
		return this.temp;
	}

}