package com.elisis.gtnhlanth.common.register;

import com.elisis.gtnhlanth.common.tileentity.Digester;
import com.elisis.gtnhlanth.common.tileentity.DissolutionTank;

import net.minecraft.item.ItemStack;

public final class LanthItemList {
	
	public static ItemStack DIGESTER;
	public static ItemStack DISSOLUTION_TANK;

	public static void register() {
		
		LanthItemList.DIGESTER = new Digester(10500, "Digester", "Digester").getStackForm(1L);
		LanthItemList.DISSOLUTION_TANK = new DissolutionTank(10501, "Dissolution Tank", "Dissolution Tank").getStackForm(1L);
		
	}
	
	

}
