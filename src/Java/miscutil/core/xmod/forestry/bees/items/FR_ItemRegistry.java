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
import miscutil.core.lib.LoadedMods;
import miscutil.core.xmod.forestry.bees.alveary.FR_BlockAlveary;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.core.items.ItemBlockForestry;
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
	public static MB_ItemFrame haveFrameBusy;
	
	//Extra Bee Frame Items
	public static MB_ItemFrame hiveFrameCocoa;
	public static MB_ItemFrame hiveFrameCaged;
	public static MB_ItemFrame hiveFrameSoul;
	public static MB_ItemFrame hiveFrameClay;
	public static MB_ItemFrame hiveFrameNova;
	
	//Alveary Stuff
	public static FR_BlockAlveary alveary;

	public static void Register() {		

		//Forestry Frames
		//frameUntreated = registerItem(new FR_ItemHiveFrame(80, 0.9f), "frameUntreated");
		//frameImpregnated = registerItem(new FR_ItemHiveFrame(240, 0.4f), "frameImpregnated");
		//frameProven = registerItem(new FR_ItemHiveFrame(720, 0.3f), "frameProven");

		//Magic Bee like Frames
		hiveFrameAccelerated = new MB_ItemFrame(MB_FrameType.ACCELERATED, "Longevity for bees isn't very common.");
		hiveFrameVoid = new MB_ItemFrame(MB_FrameType.VOID, EnumRarity.rare, "??? (Dungeon Loot)");
		hiveFrameMutagenic = new MB_ItemFrame(MB_FrameType.MUTAGENIC, EnumRarity.epic, "Evolution of the fitest, finest and fastest.");
		haveFrameBusy = new MB_ItemFrame(MB_FrameType.BUSY, "Busy bee, Busy Bee, make more honey please for me.");
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new ItemStack(hiveFrameVoid), 1, 1, 18));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(hiveFrameVoid), 1, 3, 23));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(hiveFrameMutagenic), 1, 1, 18));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(hiveFrameMutagenic), 1, 3, 23));
		
		//Extra Bee like Frames
		if (!LoadedMods.ExtraBees){
		hiveFrameCocoa = new MB_ItemFrame(MB_FrameType.COCOA, EnumRarity.common, "");
		hiveFrameCaged = new MB_ItemFrame(MB_FrameType.CAGE, EnumRarity.common, "");
		hiveFrameSoul = new MB_ItemFrame(MB_FrameType.SOUL, EnumRarity.common, "");
		hiveFrameClay = new MB_ItemFrame(MB_FrameType.CLAY, EnumRarity.common, "");
		hiveFrameNova = new MB_ItemFrame(MB_FrameType.NOVA, EnumRarity.epic, "A Creative Only Frame.");
		
		alveary = registerBlock(new FR_BlockAlveary(), ItemBlockForestry.class, "alveary");
		}




	}

	protected static <T extends Item> T registerItem(T item, String name) {
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, StringUtil.cleanItemName(item));
		return item;
	}
	
	protected static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemClass, String name, Object... itemCtorArgs) {
		block.setBlockName("for." + name);
		GameRegistry.registerBlock(block, itemClass, StringUtil.cleanBlockName(block), itemCtorArgs);
		return block;
		}
}


