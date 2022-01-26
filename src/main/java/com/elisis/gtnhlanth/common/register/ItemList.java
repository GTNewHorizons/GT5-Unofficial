package com.elisis.gtnhlanth.common.register;

import com.elisis.gtnhlanth.common.tileentity.Digester;

import net.minecraft.item.ItemStack;

public final class ItemList {
	
	public static ItemStack DIGESTER;

	public static void register() {
		
		ItemList.DIGESTER = new Digester(10500, "Digester", "Digester").getStackForm(1L);
		
	}
	
	

}
