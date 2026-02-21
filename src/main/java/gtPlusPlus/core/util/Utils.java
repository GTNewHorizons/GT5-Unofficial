package gtPlusPlus.core.util;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTSplit;
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

    public static String getBookTitleLocalization(final String aTitle) {
        return StatCollector.translateToLocal("Book." + aTitle + ".Name");
    }

    public static String[] getBookPagesLocalization(final String aTitle, final String[] aPages) {
        String[] aLocalizationPages = new String[aPages.length];
        for (byte i = 0; i < aPages.length; i = (byte) (i + 1)) {
            aLocalizationPages[i] = StatCollector
                .translateToLocal("Book." + aTitle + ".Page" + ((i < 10) ? "0" + i : Byte.valueOf(i)))
                .replace(GTSplit.LB, "\n");
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
        String localizationTitle = getBookTitleLocalization(title);
        NBT.setString("title", localizationTitle);
        NBT.setString("author", author);

        NBTTagList NBTList = new NBTTagList();
        String[] localizationPages = getBookPagesLocalization(title, pages);

        for (byte i = 0; i < pages.length; i++) {
            if (i < 48) {
                if (pages[i].length() < 256) {
                    NBTList.appendTag(new NBTTagString(localizationPages[i]));
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

        String credits = StatCollector.translateToLocalFormatted("gt.book.credits", author, ID);
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

    public static String[] splitLocalizedWithAuthor(String key, String authorName) {
        return GTSplit
            .splitLocalizedWithSuffix(key, StatCollector.translateToLocalFormatted("GTPP.core.GT_Tooltip", authorName));
    }

    public static String[] splitLocalizedWithAlkalus(String key) {
        return GTSplit.splitLocalizedWithSuffix(key, GTPPCore.GT_Tooltip.get());
    }

    public static String[] splitLocalizedFormattedWithAuthor(String key, String authorName, Object... objects) {
        return GTSplit.splitLocalizedFormattedWithSuffix(
            key,
            StatCollector.translateToLocalFormatted("GTPP.core.GT_Tooltip", authorName),
            objects);
    }

    public static String[] splitLocalizedFormattedWithAlkalus(String key, Object... objects) {
        return GTSplit.splitLocalizedFormattedWithSuffix(key, GTPPCore.GT_Tooltip.get(), objects);
    }

    public static String[] splitLocalizedWithPrefixAndAlkalus(String prefix, String key) {
        return GTSplit.splitLocalizedWithWarped(key, prefix, GTPPCore.GT_Tooltip.get());
    }

    public static String[] splitLocalizedFormattedWithPrefixAndAlkalus(String prefix, String key, Object... objects) {
        return GTSplit.splitLocalizedFormattedWithWarped(key, prefix, GTPPCore.GT_Tooltip.get(), objects);
    }
}
