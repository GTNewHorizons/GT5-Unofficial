package gtPlusPlus.core.util.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LocaleUtils {

	public static boolean GenerateFakeLocaleFile() {
		for (ModContainer modcontainer : Loader.instance().getModList()){	
			if (modcontainer.getModId().toLowerCase().equals("miscutils")) {
				String S = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
				writeToFile(S);
				dumpItemsAndBlocksForModContainer(modcontainer);
			}
		}
		return true;
	}

	public static boolean dumpItemsAndBlocksForModContainer(ModContainer mod) {
		writeToFile("Dumping Items from "+mod.getModId()+".");
		for (Object C : GameData.getItemRegistry()) {
			
			try {
			
			if (C != null) {
				if (C instanceof Item) {	
					Item R = (Item) C;		
					ItemStack IS = ItemUtils.getSimpleStack(R);	
					String modid = ItemUtils.getModId(IS);
					if (modid.equals("miscutils") || modid.equals("ToxicEverglades")) {
						String S = "["+modid+"] "+IS.getUnlocalizedName()+".name=";
						writeToFile(S);
					}						
				}
			}
			
			}
			catch (Throwable T) {}
			
		}
		writeToFile("Dumping Blocks from "+mod.getModId()+".");			
		for (Object B : GameData.getBlockRegistry()) {
			
			try {
			
			if (B != null) {
				if (B instanceof Block) {
					Block R = (Block) B;		
					ItemStack IS = ItemUtils.getSimpleStack(R);	
					String modid = ItemUtils.getModId(IS);
					if (modid.equals("miscutils") || modid.equals("ToxicEverglades")) {
						String S = "["+modid+"] "+IS.getUnlocalizedName()+".name=";
						writeToFile(S);
					}
				}
			}
			
			}
			catch (Throwable T) {}
		
		}


		return true;
	}

	public static void writeToFile(String S) {
		try {
			File F = new File(Utils.getMcDir(), "config/GTplusplus/en_US.lang");
			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter(F, true));
			writer.write(S);
			writer.newLine();
			writer.close();
		}
		catch (IOException e) {}
	}


}
