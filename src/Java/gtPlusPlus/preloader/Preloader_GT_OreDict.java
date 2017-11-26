package gtPlusPlus.preloader;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class Preloader_GT_OreDict {

	public static boolean shouldPreventRegistration(final String string, final ItemStack bannedItem) {
		try {
			if (CORE_Preloader.enableOldGTcircuits){
				if ((bannedItem != null) && ItemUtils.getModId(bannedItem).toLowerCase().equals("gregtech")){
					final int damageValue = bannedItem.getItemDamage() - 32000;
					if (bannedItem.getItem() instanceof GT_MetaGenerated_Item_01) { // 700-720
						if ((damageValue >= 700) && (damageValue <= 720)) {
							return true;
						}
					}
					else {
						try {
							if (Class.forName("gregtech.common.items.GT_MetaGenerated_Item_03") != null) { // 6/11/12/14/16/20/30-57/69-73/79-96
								final Class<?> MetaItem03 = Class.forName("gregtech.common.items.GT_MetaGenerated_Item_03");
								if (isInstanceOf(MetaItem03, bannedItem.getItem())) {
									if ((damageValue == 6) || (damageValue == 7) || (damageValue == 11) || (damageValue == 12) || (damageValue == 14)
											|| (damageValue == 16) || (damageValue == 20) || (damageValue == 21) || (damageValue == 22)) {
										return true;
									}
									else if ((damageValue >= 30) && (damageValue <= 57)) {
										return true;
									}
									else if ((damageValue >= 69) && (damageValue <= 73)) {
										return true;
									}
									else if ((damageValue >= 78) && (damageValue <= 96)) {
										return true;
									}
								}
							}
						}
						catch (final ClassNotFoundException e) {
						}
					}
				}
			}

			//Mekanism Support - Let's not make Mek Osmium useful in GT anymore.
			if ((((bannedItem != null) && (ItemUtils.getModId(bannedItem).toLowerCase().equals("mekanism"))) || (LoadedMods.Mekanism)) && !LoadedMods.RedTech){
				//Circuits
				if (Class.forName("mekanism.common.item.ItemControlCircuit") != null) {
					final Class<?> MekCircuit = Class.forName("mekanism.common.item.ItemControlCircuit");
					if (isInstanceOf(MekCircuit, bannedItem.getItem())) {
						for (int r=0;r<4;r++){
							if (bannedItem.getItemDamage() == r){
								FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing "+bannedItem.getDisplayName()+" from the OreDictionary to balance Mekanism.");
								return true;
							}
						}
					}
				}
				//Ingots
				if (Class.forName("mekanism.common.item.ItemIngot") != null) {
					final Class<?> MekIngot = Class.forName("mekanism.common.item.ItemIngot");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 1){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing "+bannedItem.getDisplayName()+" from the OreDictionary to balance Mekanism.");
							return true;
						}
					}
				}
				//Dirty Dust
				if (Class.forName("mekanism.common.item.ItemDirtyDust") != null) {
					final Class<?> MekIngot = Class.forName("mekanism.common.item.ItemDirtyDust");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing "+bannedItem.getDisplayName()+" from the OreDictionary to balance Mekanism.");
							return true;
						}
					}
				}
				//Dust
				if (Class.forName("mekanism.common.item.ItemDust") != null) {
					final Class<?> MekIngot = Class.forName("mekanism.common.item.ItemDust");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing "+bannedItem.getDisplayName()+" from the OreDictionary to balance Mekanism.");
							return true;
						}
					}
				}
				//Crystal
				if (Class.forName("mekanism.common.item.ItemCrystal") != null) {
					final Class<?> MekIngot = Class.forName("mekanism.common.item.ItemCrystal");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing "+bannedItem.getDisplayName()+" from the OreDictionary to balance Mekanism.");
							return true;
						}
					}
				}
				//Shard
				if (Class.forName("mekanism.common.item.ItemShard") != null) {
					final Class<?> MekIngot = Class.forName("mekanism.common.item.ItemShard");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing "+bannedItem.getDisplayName()+" from the OreDictionary to balance Mekanism.");
							return true;
						}
					}
				}
				//Clump
				if (Class.forName("mekanism.common.item.ItemClump") != null) {
					final Class<?> MekIngot = Class.forName("mekanism.common.item.ItemClump");
					if (isInstanceOf(MekIngot, bannedItem.getItem())) {
						if (bannedItem.getItemDamage() == 2){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing "+bannedItem.getDisplayName()+" from the OreDictionary to balance Mekanism.");
							return true;
						}
					}
				}
				//Ores
				if (Class.forName("mekanism.common.item.ItemBlockOre") != null) {
					final Class<?> MekOre = Class.forName("mekanism.common.item.ItemBlockOre");
					if (isInstanceOf(MekOre, bannedItem.getItem()) || (bannedItem == ItemUtils.simpleMetaStack("Mekanism:OreBlock", 0, 1))) {
						if (bannedItem.getItemDamage() == 0){
							FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Removing "+bannedItem.getDisplayName()+" from the OreDictionary to balance Mekanism.");
							return true;
						}
					}
				}
			}

		} catch (final Throwable e) {
			FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "A mod tried to register an invalid item with the OreDictonary.");
			if (bannedItem != null){
				FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Please report this issue to the authors of "+ItemUtils.getModId(bannedItem));
			}
			FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, e.getMessage());
		}
		return false;
	}

	// Simplification of Life.
	private static boolean isInstanceOf(final Class<?> clazz, final Object obj) {
		return clazz.isInstance(obj);
	}

}
