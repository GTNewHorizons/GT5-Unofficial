package gtPlusPlus.core.util;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

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

    // Send a message to all players on the server
    public static void sendServerMessage(final String translationKey) {
        sendServerMessage(new ChatComponentText(translationKey));
    }

    // Send a message to all players on the server
    public static void sendServerMessage(final IChatComponent chatComponent) {
        MinecraftServer.getServer()
            .getConfigurationManager()
            .sendChatMsg(chatComponent);
    }

    public static int rgbtoHexValue(final int r, final int g, final int b) {
        if ((r > 255) || (g > 255) || (b > 255) || (r < 0) || (g < 0) || (b < 0)) {
            return 0;
        }
        final Color c = new Color(r, g, b);
        String temp = Integer.toHexString(c.getRGB() & 0xFFFFFF)
            .toUpperCase();
        temp = Utils.appenedHexNotationToString(temp);
        return Integer.decode(temp);
    }

    /*
     * http://javadevnotes.com/java-left-pad-string-with-zeros-examples
     */
    public static String padWithZerosLefts(final String originalString, final int length) {
        final StringBuilder sb = new StringBuilder();
        while ((sb.length() + originalString.length()) < length) {
            sb.append('0');
        }
        sb.append(originalString);
        return sb.toString();
    }

    /*
     * Original Code by Chandana Napagoda - https://cnapagoda.blogspot.com.au/2011/03/java-hex-color-code-generator.
     * html
     */
    public static Map<Integer, String> hexColourGeneratorRandom(final int colorCount) {
        final HashMap<Integer, String> hexColorMap = new HashMap<>();
        for (int a = 0; a < colorCount; a++) {
            String code = "" + (int) (Math.random() * 256);
            code = code + code + code;
            final int i = Integer.parseInt(code);
            String hexString = Integer.toHexString(0x1000000 | i)
                .substring(1)
                .toUpperCase();
            hexColorMap.put(a, hexString);
        }
        return hexColorMap;
    }

    public static String appenedHexNotationToString(final Object hexAsStringOrInt) {
        final String hexChar = "0x";
        String result;
        if (hexAsStringOrInt.getClass() == String.class) {
            result = hexChar + hexAsStringOrInt;
            return result;
        } else if (hexAsStringOrInt.getClass() == Integer.class) {
            String aa = String.valueOf(hexAsStringOrInt);
            if (aa.length() != 6) {
                result = padWithZerosLefts(aa, 6);
            } else {
                result = hexChar + hexAsStringOrInt;
            }
            return result;
        } else {
            return null;
        }
    }

    public static File getMcDir() {
        if (Utils.isClient()) {
            if (Minecraft.getMinecraft() != null) {
                return Minecraft.getMinecraft().mcDataDir;
            }
        }
        return new File(".");
    }

    public static String sanitizeStringKeepDashes(final String input) {
        final char[] chars = input.toCharArray();
        int i = 0;
        for (final char c : chars) {
            switch (c) {
                case ' ':
                case '~':
                case '?':
                case '!':
                case '@':
                case '#':
                case '$':
                case '%':
                case '^':
                case '&':
                case '*':
                case '(':
                case ')':
                case '{':
                case '}':
                case '[':
                case ']':
                    continue;
            }
            chars[i++] = c;
        }
        return new String(chars, 0, i);
    }

    public static String sanitizeString(final String input) {
        final char[] chars = input.toCharArray();
        int i = 0;
        for (final char c : chars) {
            switch (c) {
                case ' ':
                case '-':
                case '_':
                case '?':
                case '!':
                case '@':
                case '#':
                case '(':
                case ')':
                case '{':
                case '}':
                case '[':
                case ']':
                    continue;
            }
            chars[i++] = c;
        }
        return new String(chars, 0, i);
    }

    public static String sanitizeStringKeepBrackets(final String input) {
        final char[] chars = input.toCharArray();
        int i = 0;
        for (final char c : chars) {
            switch (c) {
                case ' ':
                case '-':
                case '_':
                case '?':
                case '!':
                case '@':
                case '#':
                    continue;
            }
            chars[i++] = c;
        }
        return new String(chars, 0, i);
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
