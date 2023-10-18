package gtPlusPlus.core.util.data;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GTPlusPlusEverglades;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class LocaleUtils {

    public static void generateFakeLocaleFile() {
        for (ModContainer modcontainer : Loader.instance().getModList()) {
            if (modcontainer.getModId().toLowerCase().equals(GTPlusPlus.ID)) {
                String S = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
                writeToFile(S);
                dumpItemsAndBlocksForModContainer(modcontainer);
            }
        }
    }

    public static void dumpItemsAndBlocksForModContainer(ModContainer mod) {
        writeToFile("Dumping Items from " + mod.getModId() + ".");
        for (Object C : GameData.getItemRegistry()) {

            try {

                if (C != null) {
                    if (C instanceof Item R) {
                        ItemStack IS = ItemUtils.getSimpleStack(R);
                        String modid = ItemUtils.getModId(IS);
                        if (modid.equals(GTPlusPlus.ID) || modid.equals(GTPlusPlusEverglades.ID)) {
                            String S = "[" + modid + "] " + IS.getUnlocalizedName() + ".name=";
                            writeToFile(S);
                        }
                    }
                }

            } catch (Throwable ignored) {}
        }
        writeToFile("Dumping Blocks from " + mod.getModId() + ".");
        for (Object B : GameData.getBlockRegistry()) {

            try {

                if (B != null) {
                    if (B instanceof Block R) {
                        ItemStack IS = ItemUtils.getSimpleStack(R);
                        String modid = ItemUtils.getModId(IS);
                        if (modid.equals(GTPlusPlus.ID) || modid.equals(GTPlusPlusEverglades.ID)) {
                            String S = "[" + modid + "] " + IS.getUnlocalizedName() + ".name=";
                            writeToFile(S);
                        }
                    }
                }

            } catch (Throwable ignored) {}
        }

    }

    public static void writeToFile(String S) {
        try {
            File F = new File(Utils.getMcDir(), "config/GTplusplus/en_US.lang");
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(F, true));
            writer.write(S);
            writer.newLine();
            writer.close();
        } catch (IOException ignored) {}
    }
}
