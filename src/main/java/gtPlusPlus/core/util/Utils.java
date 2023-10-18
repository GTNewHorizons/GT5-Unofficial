package gtPlusPlus.core.util;

import java.awt.Color;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.EnumUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.resources.ItemCell;

public class Utils {

    public static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    public static boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public static TC_AspectStack getTcAspectStack(final TC_Aspects aspect, final long size) {
        return getTcAspectStack(aspect.name(), (int) size);
    }

    public static TC_AspectStack getTcAspectStack(final String aspect, final long size) {
        return getTcAspectStack(aspect, (int) size);
    }

    public static TC_AspectStack getTcAspectStack(final TC_Aspects aspect, final int size) {
        return getTcAspectStack(aspect.name(), size);
    }

    public static TC_AspectStack getTcAspectStack(final String aspect, final int size) {

        TC_AspectStack returnValue = null;

        if (aspect.equalsIgnoreCase("COGNITIO")) {
            // Adds in Compat for older GT Versions which Misspell aspects.
            try {
                if (EnumUtils.isValidEnum(TC_Aspects.class, "COGNITIO")) {
                    Logger.WARNING("TC Aspect found - " + aspect);
                    returnValue = new TC_AspectStack(TC_Aspects.valueOf("COGNITIO"), size);
                } else {
                    Logger.INFO(
                            "Fallback TC Aspect found - " + aspect
                                    + " - PLEASE UPDATE GREGTECH TO A NEWER VERSION TO REMOVE THIS MESSAGE - THIS IS NOT AN ERROR");
                    returnValue = new TC_AspectStack(TC_Aspects.valueOf("COGNITO"), size);
                }
            } catch (final NoSuchFieldError r) {
                Logger.INFO("Invalid Thaumcraft Aspects - Report this issue to Alkalus");
            }
        } else if (aspect.equalsIgnoreCase("EXANIMUS")) {
            // Adds in Compat for older GT Versions which Misspell aspects.
            try {
                if (EnumUtils.isValidEnum(TC_Aspects.class, "EXANIMUS")) {
                    Logger.WARNING("TC Aspect found - " + aspect);
                    returnValue = new TC_AspectStack(TC_Aspects.valueOf("EXANIMUS"), size);
                } else {
                    Logger.INFO(
                            "Fallback TC Aspect found - " + aspect
                                    + " - PLEASE UPDATE GREGTECH TO A NEWER VERSION TO REMOVE THIS MESSAGE - THIS IS NOT AN ERROR");
                    returnValue = new TC_AspectStack(TC_Aspects.valueOf("EXAMINIS"), size);
                }
            } catch (final NoSuchFieldError r) {
                Logger.INFO("Invalid Thaumcraft Aspects - Report this issue to Alkalus");
            }

        } else if (aspect.equalsIgnoreCase("PRAECANTATIO")) {
            // Adds in Compat for older GT Versions which Misspell aspects.
            try {
                if (EnumUtils.isValidEnum(TC_Aspects.class, "PRAECANTATIO")) {
                    Logger.WARNING("TC Aspect found - " + aspect);
                    returnValue = new TC_AspectStack(TC_Aspects.valueOf("PRAECANTATIO"), size);
                } else {
                    Logger.INFO(
                            "Fallback TC Aspect found - " + aspect
                                    + " - PLEASE UPDATE GREGTECH TO A NEWER VERSION TO REMOVE THIS MESSAGE - THIS IS NOT AN ERROR");
                    returnValue = new TC_AspectStack(TC_Aspects.valueOf("PRAECANTIO"), size);
                }
            } catch (final NoSuchFieldError r) {
                Logger.INFO("Invalid Thaumcraft Aspects - Report this issue to Alkalus");
            }
        } else {
            Logger.WARNING("TC Aspect found - " + aspect);
            returnValue = new TC_AspectStack(TC_Aspects.valueOf(aspect), size);
        }

        return returnValue;
    }

    // Register an event to both busses.
    public static void registerEvent(Object o) {
        MinecraftForge.EVENT_BUS.register(o);
        FMLCommonHandler.instance().bus().register(o);
    }

    // Send a message to all players on the server
    public static void sendServerMessage(final String translationKey) {
        sendServerMessage(new ChatComponentText(translationKey));
    }

    // Send a message to all players on the server
    public static void sendServerMessage(final IChatComponent chatComponent) {
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(chatComponent);
    }

    /**
     * Returns if that Liquid is IC2Steam.
     */
    public static boolean isIC2Steam(final FluidStack aFluid) {
        if (aFluid == null) {
            return false;
        }
        return aFluid.isFluidEqual(getIC2Steam(1));
    }

    /**
     * Returns a Liquid Stack with given amount of IC2Steam.
     */
    public static FluidStack getIC2Steam(final long aAmount) {
        return FluidRegistry.getFluidStack("ic2steam", (int) aAmount);
    }

    public static int rgbtoHexValue(final int r, final int g, final int b) {
        if ((r > 255) || (g > 255) || (b > 255) || (r < 0) || (g < 0) || (b < 0)) {
            return 0;
        }
        final Color c = new Color(r, g, b);
        String temp = Integer.toHexString(c.getRGB() & 0xFFFFFF).toUpperCase();
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
            hexColorMap.put(a, Integer.toHexString(0x1000000 | i).substring(1).toUpperCase());
            Logger.WARNING("" + Integer.toHexString(0x1000000 | i).substring(1).toUpperCase());
        }
        return hexColorMap;
    }

    public static String appenedHexNotationToString(final Object hexAsStringOrInt) {
        final String hexChar = "0x";
        String result;
        if (hexAsStringOrInt.getClass() == String.class) {

            if (((String) hexAsStringOrInt).length() != 6) {
                final String temp = padWithZerosLefts((String) hexAsStringOrInt, 6);
            }
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

    private static short cellID = 15;

    public static ItemStack createInternalNameAndFluidCell(final String s) {
        Logger.WARNING("1");
        final InternalName yourName = EnumHelper.addEnum(InternalName.class, s, new Class[0], new Object[0]);
        Logger.WARNING("2 " + yourName.name());
        final ItemCell item = (ItemCell) Ic2Items.cell.getItem();
        Logger.WARNING("3 " + item.getUnlocalizedName());
        try {
            Logger.WARNING("4");
            final Class<? extends ItemCell> clz = item.getClass();
            Logger.WARNING("5 " + clz.getSimpleName());
            final Method methode = clz.getDeclaredMethod("addCell", int.class, InternalName.class, Block[].class);
            Logger.WARNING("6 " + methode.getName());
            methode.setAccessible(true);
            Logger.WARNING("7 " + methode.isAccessible());
            final ItemStack temp = (ItemStack) methode.invoke(item, cellID++, yourName, new Block[0]);
            Logger.WARNING("Successfully created " + temp.getDisplayName() + "s.");
            FluidContainerRegistry.registerFluidContainer(
                    FluidUtils.getFluidStack(s.toLowerCase(), 1000),
                    temp.copy(),
                    Ic2Items.cell.copy());
            ItemUtils.addItemToOreDictionary(temp.copy(), "cell" + s);
            return temp;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sanitizeString(final String input, final char[] dontRemove) {

        // List of characters to remove
        final HashSet<Character> toRemoveSet = new HashSet<>();
        Collections.addAll(
                toRemoveSet,
                ' ',
                '-',
                '_',
                '~',
                '?',
                '!',
                '@',
                '#',
                '$',
                '%',
                '^',
                '&',
                '*',
                '(',
                ')',
                '{',
                '}',
                '[',
                ']');

        // Remove characters from the toRemoveSet if they are in dontRemove
        for (char e : dontRemove) {
            toRemoveSet.remove(e);
        }

        // Construct a sanitized string
        StringBuilder sanitized = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (!toRemoveSet.contains(c)) {
                sanitized.append(c);
            }
        }

        return sanitized.toString();
    }

    public static String sanitizeString(final String input) {
        String temp;
        String output;

        temp = input.replace(" ", "");
        temp = temp.replace("-", "");
        temp = temp.replace("_", "");
        temp = temp.replace("?", "");
        temp = temp.replace("!", "");
        temp = temp.replace("@", "");
        temp = temp.replace("#", "");
        temp = temp.replace("(", "");
        temp = temp.replace(")", "");
        temp = temp.replace("{", "");
        temp = temp.replace("}", "");
        temp = temp.replace("[", "");
        temp = temp.replace("]", "");
        temp = temp.replace(" ", "");
        output = temp;
        return output;
    }

    public static String sanitizeStringKeepBrackets(final String input) {
        String temp;
        String output;

        temp = input.replace(" ", "");
        temp = temp.replace("-", "");
        temp = temp.replace("_", "");
        temp = temp.replace("?", "");
        temp = temp.replace("!", "");
        temp = temp.replace("@", "");
        temp = temp.replace("#", "");
        temp = temp.replace(" ", "");
        output = temp;
        return output;
    }

    public static String addBookTitleLocalization(final String aTitle) {
        return GT_LanguageManager
                .addStringLocalization("Book." + aTitle + ".Name", aTitle, !GregTech_API.sPostloadFinished);
    }

    public static String[] addBookPagesLocalization(final String aTitle, final String[] aPages) {
        String[] aLocalizationPages = new String[aPages.length];
        for (byte i = 0; i < aPages.length; i = (byte) (i + 1)) {
            aLocalizationPages[i] = GT_LanguageManager.addStringLocalization(
                    "Book." + aTitle + ".Page" + ((i < 10) ? "0" + i : Byte.valueOf(i)),
                    aPages[i],
                    !GregTech_API.sPostloadFinished);
        }
        return aLocalizationPages;
    }

    public static ItemStack getWrittenBook(ItemStack book, int ID, String mapping, String title, String author,
            String[] pages) {

        if (GT_Utility.isStringInvalid(mapping)) {
            return null;
        }

        ItemStack stack = CORE.sBookList.get(mapping);
        if (stack != null) {
            return GT_Utility.copyAmount(1L, stack);
        }

        if (GT_Utility.isStringInvalid(title) || GT_Utility.isStringInvalid(author) || pages.length <= 0) {
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
                    GT_Log.err.println("WARNING: String for written Book too long! -> " + pages[i]);
                }
            } else {
                Logger.INFO("WARNING: Too much Pages for written Book! -> " + title);
                GT_Log.err.println("WARNING: Too much Pages for written Book! -> " + title);
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
        GT_Log.out.println(logMessage);

        NBTUtils.createIntegerTagCompound(stack, "stats", "mMeta", ID);
        CORE.sBookList.put(mapping, stack);

        Logger.INFO(String.format("Creating book: %s by %s. Using Meta %d.", title, author, ID));

        return GT_Utility.copy(stack);
    }

}
