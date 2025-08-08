package gtPlusPlus.core.util;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.GregTechAPI;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class Utils {

    public static boolean isServer() {
        return FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer();
    }

    public static boolean isClient() {
        return FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient();
    }

    public static int rgbtoHexValue(final int r, final int g, final int b) {
        if ((r > 255) || (g > 255) || (b > 255) || (r < 0) || (g < 0) || (b < 0)) {
            return 0;
        }
        final int rgb = ((0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
        return rgb & 0xFFFFFF;
    }

    public static File getMcDir() {
        if (Utils.isClient()) {
            if (Minecraft.getMinecraft() != null) {
                return Minecraft.getMinecraft().mcDataDir;
            }
        }
        return new File(".");
    }

    public static String addBookTitleLocalization(final String aTitle) {
        return GTLanguageManager
            .addStringLocalization("Book." + aTitle + ".Name", aTitle, !GregTechAPI.sPostloadFinished);
    }

    public static String[] addBookPagesLocalization(final String aTitle, final String[] aPages) {
        String[] aLocalizationPages = new String[aPages.length];
        for (byte i = 0; i < aPages.length; i = (byte) (i + 1)) {
            aLocalizationPages[i] = GTLanguageManager.addStringLocalization(
                "Book." + aTitle + ".Page" + ((i < 10) ? "0" + i : Byte.valueOf(i)),
                aPages[i],
                !GregTechAPI.sPostloadFinished);
        }
        return aLocalizationPages;
    }

    public static ItemStack getWrittenBook(ItemStack book, int ID, String mapping, String title, String author,
        String[] pages) {

        if (GTUtility.isStringInvalid(mapping)) {
            return null;
        }

        ItemStack stack = GTPPCore.sBookList.get(mapping);
        if (stack != null) {
            return GTUtility.copyAmount(1L, stack);
        }

        if (GTUtility.isStringInvalid(title) || GTUtility.isStringInvalid(author) || pages.length == 0) {
            return null;
        }

        stack = (book == null) ? new ItemStack(ModItems.itemCustomBook, 1, ID) : book;

        NBTTagCompound NBT = new NBTTagCompound();
        String localizationTitle = addBookTitleLocalization(title);
        NBT.setString("title", localizationTitle);
        NBT.setString("author", author);

        NBTTagList NBTList = new NBTTagList();
        String[] localizationPages = addBookPagesLocalization(title, pages);

        for (byte i = 0; i < pages.length; i++) {
            pages[i] = localizationPages[i].replaceAll("<BR>", "\n");
            if (i < 48) {
                if (pages[i].length() < 256) {
                    NBTList.appendTag(new NBTTagString(pages[i]));
                } else {
                    Logger.INFO("WARNING: String for written Book too long! -> " + pages[i]);
                    GTLog.err.println("WARNING: String for written Book too long! -> " + pages[i]);
                }
            } else {
                Logger.INFO("WARNING: Too much Pages for written Book! -> " + title);
                GTLog.err.println("WARNING: Too much Pages for written Book! -> " + title);
                break;
            }
        }

        String credits = String.format(
            "Credits to %s for writing this Book. This was Book Nr. %d at its creation. Gotta get 'em all!",
            author,
            ID);
        NBTList.appendTag(new NBTTagString(credits));
        NBT.setTag("pages", NBTList);

        stack.setTagCompound(NBT);

        String logMessage = String.format(
            "GT++_Mod: Added Book to Book++ List  -  Mapping: '%s'  -  Name: '%s'  -  Author: '%s'",
            mapping,
            title,
            author);
        GTLog.out.println(logMessage);

        NBTUtils.createIntegerTagCompound(stack, "stats", "mMeta", ID);
        GTPPCore.sBookList.put(mapping, stack);

        Logger.INFO(String.format("Creating book: %s by %s. Using Meta %d.", title, author, ID));

        return GTUtility.copy(stack);
    }

}
