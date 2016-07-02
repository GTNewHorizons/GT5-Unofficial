/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package miscutil.core.xmod.forestry.bees.items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.core.utils.StringUtil;

public class FR_ItemRegistry {


	//----- Apiary Frames ----------------------
	//public static FR_ItemHiveFrame frameUntreated;
	//public static FR_ItemHiveFrame frameImpregnated;
	//public static FR_ItemHiveFrame frameProven;
	
	//Magic Bee Frame Items
	public static MB_ItemFrame hiveFrameAccelerated;
	public static MB_ItemFrame hiveFrameVoid;
	public static MB_ItemFrame hiveFrameMutagenic;

	public static void Register() {		

		//Forestry Frames
		//frameUntreated = registerItem(new FR_ItemHiveFrame(80, 0.9f), "frameUntreated");
		//frameImpregnated = registerItem(new FR_ItemHiveFrame(240, 0.4f), "frameImpregnated");
		//frameProven = registerItem(new FR_ItemHiveFrame(720, 0.3f), "frameProven");

		//Magic Bee like Frames
		hiveFrameAccelerated = new MB_ItemFrame(MB_FrameType.ACCELERATED);
		hiveFrameVoid = new MB_ItemFrame(MB_FrameType.VOID, EnumRarity.rare);
		hiveFrameMutagenic = new MB_ItemFrame(MB_FrameType.MUTAGENIC, EnumRarity.epic);
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new ItemStack(hiveFrameVoid), 1, 1, 18));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(hiveFrameVoid), 1, 3, 23));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(hiveFrameMutagenic), 1, 1, 18));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(hiveFrameMutagenic), 1, 3, 23));




	}

	protected static <T extends Item> T registerItem(T item, String name) {
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, StringUtil.cleanItemName(item));
		return item;
	}
}


